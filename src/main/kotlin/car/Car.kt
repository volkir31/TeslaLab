package car

import car.valueObject.Fuel
import driver.Command
import driver.Gui
import driver.enums.Action
import driver.enums.Direction
import driver.enums.MessageLevel
import jade.core.Agent
import jade.core.behaviours.TickerBehaviour
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.math.max

class Car : Agent() {
    var position: Pair<Int, Int> = Pair(0, 0)
    var engineIsStarted = false
    val fuel = Fuel()
    var countMaxAutoCharging = 3

    init {
        Gui.updateFuel(fuel)
    }

    override fun setup() {
        addBehaviour(object : TickerBehaviour(this, 1) {
            override fun onTick() {
                if (engineIsStarted) {
                    fuel.spend()
                    Gui.updateFuel(fuel);
                }

                if (fuel.isNeedCharge() && countMaxAutoCharging > 0) {
                    fuel.charge(20.0)
                    countMaxAutoCharging--
                    Gui.displayMessage(MessageLevel.Error, "Осталось жизней: $countMaxAutoCharging")
                    Gui.updateFuel(fuel)
                }

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

                    if (command.command == Action.CHARGE) {
                        fuel.charge(100.0)
                        Gui.updateFuel(fuel)
                    }

                    if (fuel.percent <= 0.0) {
                        Gui.displayMessage(MessageLevel.Error, "Закончилось топливо")
                        return
                    }

                    if (!engineIsStarted && command.command is Direction) {
                        Gui.displayMessage(MessageLevel.Error, "Невозможно начать ехать с заглушенным двигателем")
                        return
                    } else {
                        Gui.clearError()
                    }

                    if (command.command is Direction) {
                        Gui.updateState(command.command.move())
                    }
                }
            }
        })
    }
}

operator fun Pair<Int, Int>.plus(pair: Pair<Int, Int>): Pair<Int, Int> = run {
    val x = this.first + pair.first
    val y = this.second + pair.second

    Pair(max(x, 0), max(y, 0))
}
