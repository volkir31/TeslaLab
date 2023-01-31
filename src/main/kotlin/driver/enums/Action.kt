package driver.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.awt.event.KeyEvent

@Serializable
@SerialName("Action")
enum class Action(val code: Int) : CommandInterface {
    ENGINE_START(code = KeyEvent.VK_S),
    ENGINE_STOP(code = KeyEvent.VK_DEAD_ACUTE),
    CHARGE(code = KeyEvent.VK_F),
    NONE(code = -1);

    companion object {
        fun fromKey(value: KeyEvent) = values().first { it.code == value.keyCode }
        fun isValid(value: KeyEvent) = values().any { it.code == value.keyCode }
    }
}