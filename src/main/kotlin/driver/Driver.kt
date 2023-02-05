package driver

import driver.enums.CommandInterface
import jade.core.Agent
import jade.core.behaviours.TickerBehaviour
import jade.domain.AMSService
import jade.domain.FIPAAgentManagement.AMSAgentDescription
import jade.domain.FIPAAgentManagement.SearchConstraints
import jade.lang.acl.ACLMessage
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class Driver : Agent() {
    private var commands: ConcurrentHashMap<Int, CommandInterface> = ConcurrentHashMap()

    override fun setup() {
        val gui = Gui
        gui.driver = this
        gui.showGui()

        addBehaviour(object : TickerBehaviour(this, 1) {
            override fun onTick() {
                commands.forEach { (index, command) ->
                    val cmd = Command(command)
                    val json = Json {
                        useArrayPolymorphism = true
                    }
                    val message = ACLMessage(ACLMessage.PROPOSE)
                    val constraints = SearchConstraints()
                    constraints.maxResults = -1L
                    val agents = AMSService.search(this@Driver, AMSAgentDescription(), constraints)

                    agents.filter { it.name.localName == "car" }.forEach {
                        message.addReceiver(it.name)
                        message.content = json.encodeToString(cmd)
                        message.replyWith = "cfp" + System.currentTimeMillis()
                        myAgent.send(message)
                    }

                    commands.remove(index)
                }
            }
        })
    }

    fun handle(command: CommandInterface) = run {
        commands.put(commands.size + 1, command)
    }
}

@Serializable
data class Command(val command: CommandInterface)
