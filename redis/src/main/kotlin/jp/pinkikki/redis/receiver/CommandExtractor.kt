package jp.pinkikki.redis.receiver

import org.springframework.stereotype.Component

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
