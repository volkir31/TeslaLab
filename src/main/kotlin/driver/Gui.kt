package driver

import car.valueObject.Fuel
import driver.enums.Action
import driver.enums.CommandInterface
import driver.enums.Direction
import driver.enums.MessageLevel
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

object Gui : JFrame("CAR") {
    private val messageBlock: JLabel = JLabel()
    private val fuelBlock: JLabel = JLabel()
    private val gameField = GameField()
    var driver: Driver? = null

    init {
        add(gameField)
        isVisible = true
        val p = JPanel()
        p.layout = GridLayout(1, 1)
        messageBlock.also {
            it.text = ""
            it.horizontalAlignment = SwingConstants.CENTER
            it.background = Color.RED
            it.isVisible = true
        }
        fuelBlock.also {
            it.text = ""
            it.horizontalAlignment = SwingConstants.CENTER
            it.background = Color.RED
            it.isVisible = true
        }
        p.add(messageBlock)
        p.add(fuelBlock)
        contentPane.add(p, BorderLayout.SOUTH)
        isResizable = false
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher {
            if (it.id != KeyEvent.KEY_PRESSED) {
                return@addKeyEventDispatcher false
            }
            val command = when {
                it.isAltDown && it.keyCode != 18 && Action.isValid(it) -> Action.fromKey(it)
                Direction.isValid(it) -> Direction.fromKey(it)
                else -> Action.NONE
            }

            driver!!.handle(command)

            return@addKeyEventDispatcher true
        }

        minimumSize = Dimension().also {
            it.width = 500
            it.height = 500
        }

        addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                driver!!.doDelete()
            }
        })
    }

    fun showGui() {
        size = Dimension(900, 900)
        location = Point(400, 0)
        isAlwaysOnTop = true
        super.setVisible(true)
    }

    fun updateState(position: Pair<Int, Int>) = run {
        gameField.move(position)
    }

    fun displayMessage(level: MessageLevel, message: String) {
        if (level == MessageLevel.Error) {
            messageBlock.text = message
        }
    }

    fun updateFuel(fuel: Fuel) {
        fuelBlock.text = fuel.toString()
    }

    fun clearError() = run { messageBlock.text = "" }
}