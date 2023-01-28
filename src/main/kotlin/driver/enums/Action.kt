package driver.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.awt.event.KeyEvent

@Serializable
@SerialName("Action")
enum class Action(val code: Int) : CommandInterface {
    ENGINE_START(code = 83),
    ENGINE_STOP(code = 129),
    CHARGE(code = 70),
    NONE(code = -1);

    companion object {
        fun fromKey(value: KeyEvent) = values().first { it.code == value.keyCode }
        fun isValid(value: KeyEvent) = values().any { it.code == value.keyCode }
    }
}