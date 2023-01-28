package car

import driver.Command
import driver.Gui
import driver.enums.Action
import driver.enums.Direction
import jade.core.Agent
import jade.core.behaviours.TickerBehaviour
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class Car : Agent() {
    var position: Pair<Int, Int> = Pair(0, 0)
    var engineIsStarted = false
    override fun setup() {
        addBehaviour(object : TickerBehaviour(this, 1000) {
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
