package car

import driver.Command
import driver.Gui
import driver.enums.Action
import jade.core.Agent
import jade.core.behaviours.TickerBehaviour
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class Car : Agent() {
    val position: Pair<Int, Int> = Pair(0, 0)
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
                    Gui.updateState(command.command)
                }
            }
        })
    }

}
