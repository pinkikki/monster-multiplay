package jp.pinkikki.receiver

import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class CommandExtractor {

    fun extract(scene: String): List<String> {
        return when (scene) {
            "school" -> listOf("attack", "magic")
            "ghost" -> listOf("run")
            else -> throw RuntimeException()
        }
    }
}
