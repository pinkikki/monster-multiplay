package jp.pinkikki.receiver

import org.springframework.amqp.rabbit.connection.ConnectionFactory
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
            container.setMessageListener { message ->
                sink.next(ServerSentEvent
                        .builder(message.toString())
                        .build())
            }
            sink.onRequest { container.start() }
            sink.onDispose { container.stop() }
        }
    }
}
