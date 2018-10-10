package jp.pinkikki.rabbitmq.sender

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class SendController(private val rabbitTemplate: RabbitTemplate) {

    @GetMapping("/send/{command}")
    fun send(@PathVariable command: String): Mono<ResponseEntity<*>> {
        return Mono.fromCallable {
            rabbitTemplate.convertAndSend("sample", "battle/$command", command)
            ResponseEntity
                    .accepted()
                    .build<Any>()
        }
    }
}

