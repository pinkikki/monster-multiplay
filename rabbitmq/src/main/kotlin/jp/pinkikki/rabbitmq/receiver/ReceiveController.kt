package jp.pinkikki.rabbitmq.receiver

import com.rabbitmq.client.Channel
import org.springframework.amqp.core.AcknowledgeMode
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class ReceiveController(private val connectionFactory: ConnectionFactory, private val commandExtractor: CommandExtractor) {

    @GetMapping("/receive/{scene}")
    fun receive(@PathVariable scene: String): Flux<ServerSentEvent<String>> {
        return Flux.create { sink ->
            val container = SimpleMessageListenerContainer()
            container.connectionFactory = connectionFactory
            container.setQueueNames(*commandExtractor.extract(scene).map { "battle/$it" }.toTypedArray())
            container.acknowledgeMode = AcknowledgeMode.MANUAL
            container.messageListener = ChannelAwareMessageListener { message: Message, channel : Channel ->
                sink.next(ServerSentEvent
                        .builder(message.toString())
                        .build())
                channel.basicAck(message.messageProperties.deliveryTag, false)
            }
            sink.onRequest { container.start() }
            sink.onDispose { container.stop() }
        }
    }
}
