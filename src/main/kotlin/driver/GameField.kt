package driver

import car.plus
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.ImageIcon
import javax.swing.JPanel
import javax.swing.Timer
import kotlin.math.max
import kotlin.math.min

class GameField : JPanel(), ActionListener {
    private val dotSize = 32
    private val countPx = 864
    private val maxCoordinateX = countPx / dotSize
    private val maxCoordinateY = maxCoordinateX - 2
    private var coordinate = Pair(0, 0)
    private val carImage = ImageIcon("src/main/resources/car.png").image.also {
        size = Dimension(32, 32)
    }
    private val timer = Timer(1, this)

    init {
        background = Color.black
        initGame()
        isFocusable = true
    }

    fun move(movement: Pair<Int, Int>) = run {
        var newPosition = coordinate + movement
        if (newPosition.first >= maxCoordinateX) {
            newPosition = Pair(maxCoordinateX, newPosition.second)
        }
        if (newPosition.second >= maxCoordinateY) {
            newPosition = Pair(newPosition.first, maxCoordinateY)
        }
        coordinate = newPosition
    }
//
//    fun canMove(movement: Pair<Int, Int>): Boolean = run {
//        if ()
//    }

    private fun initGame() {
        timer.start()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val x = min( coordinate.first, countPx / dotSize)
        val y = min( coordinate.second, countPx / dotSize)
        g.drawImage(carImage, x * dotSize, y * dotSize, dotSize, dotSize, this)
    }

    override fun actionPerformed(e: ActionEvent) {
        repaint()
    }
}