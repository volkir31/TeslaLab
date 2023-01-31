package driver

import driver.enums.Action
import driver.enums.CommandInterface
import driver.enums.Direction
import driver.enums.MessageLevel
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.image.ColorModel
import java.time.Instant
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

object Gui : JFrame("Да") {
    val messageBlock: JLabel = JLabel()
    var driver: Driver? = null
    init {
        val p = JPanel()
        p.layout = GridLayout(1, 1)
        messageBlock.also {
            it.text = "test test"
            it.horizontalAlignment = SwingConstants.CENTER
            it.background = Color.RED
            it.isVisible = true
        }
        p.add(messageBlock)
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

        addWindowListener( object: WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                driver!!.doDelete()
            }
        })
    }

    fun showGui() {
        pack()
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val centerX = screenSize.getWidth().toInt() / 2
        val centerY = screenSize.getHeight().toInt() / 2
        setLocation(centerX - width, centerY - height)
        super.setVisible(true)
    }

    fun updateState(command: CommandInterface) = run {
        println(command)
    }

    fun displayMessage(level: MessageLevel, message: String) {
        if (level == MessageLevel.Error) {
            messageBlock.text = message
        }
    }

    fun clearError() = run { messageBlock.text = "" }
}