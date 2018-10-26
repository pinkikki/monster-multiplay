package jp.pinkikki.redis.sender

import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class SendController(private val redisTemplate: ReactiveRedisTemplate<String, String>) {

    @GetMapping("/send/{command}")
    fun send(@PathVariable command:String) : Mono<Long> {
        return redisTemplate.convertAndSend("battle/$command", command)
    }
}
