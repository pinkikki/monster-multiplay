package jp.pinkikki.redis.receiver

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class ReceiveController(private var redisConnectionFactory: ReactiveRedisConnectionFactory, private var commandExtractor: CommandExtractor) {

    @GetMapping("/receive/{scene}")
    fun receive(@PathVariable scene: String): Flux<ServerSentEvent<String>> {
        return ReactiveRedisMessageListenerContainer(redisConnectionFactory)
                .receive(*commandExtractor.extract(scene).map { ChannelTopic("battle/$it") }.toTypedArray())
                .map { ServerSentEvent.builder("message: ${it.message}").build() }
    }
}
