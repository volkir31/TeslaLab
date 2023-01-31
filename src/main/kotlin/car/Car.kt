package car

import car.valueObject.Fuel
import driver.Command
import driver.Gui
import driver.enums.Action
import driver.enums.CommandInterface
import driver.enums.Direction
import driver.enums.MessageLevel
import jade.core.Agent
import jade.core.behaviours.TickerBehaviour
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class Car : Agent() {
    var position: Pair<Int, Int> = Pair(0, 0)
    var engineIsStarted = false
    val fuel = Fuel(100.0)
    override fun setup() {
        addBehaviour(object : TickerBehaviour(this, 500) {
            override fun onTick() {
                val msg = myAgent.receive() ?: return
                if (msg.sender.localName == "driver") {
                    val json = Json {
                        useArrayPolymorphism = true
                    }

                    val command = json.decodeFromString<Command>(msg.content)
                    if (command.command == Action.ENGINE_START && !engineIsStarted) {
                        engineIsStarted = true;
                    }
                    if (command.command == Action.ENGINE_STOP && engineIsStarted) {
                        engineIsStarted = false;
                    }

                    if (!engineIsStarted && command.command is Direction) {
                        Gui.displayMessage(MessageLevel.Error, "Невозможно начать ехать с заглушенным двигателем")
                    } else {
                        Gui.clearError()
                    }

                    if (command.command is Direction) {
                        position += command.command.move()
                    }
                    Gui.updateState(command.command)
                }
            }
        })
    }
}

operator fun Pair<Int, Int>.plus(pair: Pair<Int, Int>): Pair<Int, Int> = Pair(this.first + pair.first, this.second + pair.second)
