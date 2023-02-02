package driver.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.awt.event.KeyEvent

@Serializable
@SerialName("Direction")
enum class Direction(val code: Int) : CommandInterface {
    FORWARD(code = KeyEvent.VK_UP),
    BACK(code = KeyEvent.VK_DOWN),
    LEFT(code = KeyEvent.VK_LEFT),
    RIGHT(code = KeyEvent.VK_RIGHT);

    fun move(): Pair<Int, Int> {
        return when (this) {
            FORWARD -> Pair(0, -1)
            BACK -> Pair(0, 1)
            LEFT -> Pair(-1, 0)
            RIGHT -> Pair(1, 0)
        }
    }

    companion object {
        fun fromKey(value: KeyEvent) = values().first { it.code == value.keyCode }
        fun isValid(value: KeyEvent) = values().any { it.code == value.keyCode }
    }
}