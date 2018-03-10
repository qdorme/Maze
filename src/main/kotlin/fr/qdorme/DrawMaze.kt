package fr.qdorme

import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.util.*
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

const val CELL_SIZE = 10

class DrawMaze(private val maze: Maze):Observer{

    override fun update(o: Observable?, arg: Any?) {
        refresh()
    }

    private val image = BufferedImage(maze.colsNumber * CELL_SIZE +1 , maze.rowsNumber * CELL_SIZE +1 , BufferedImage.TYPE_INT_ARGB)
    private val frame = JFrame("Maze")
    private val panel: JPanel

    init {
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(image.width + 38 , image.height + 60)
        frame.isVisible = true

        panel = object : JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                drawMaze(image.graphics)
                g.drawImage(image,10,10,null)
            }
        }
        frame.add(panel)
        panel.isDoubleBuffered=true
        panel.revalidate()
    }

    fun drawMaze(g:Graphics){

        maze.cells.forEach { rows -> rows.forEach{ cell ->
            if(cell.active) {
                val x1 = cell.col * CELL_SIZE
                val x2 = (cell.col + 1) * CELL_SIZE
                val y2 = (maze.rowsNumber - cell.row - 1) * CELL_SIZE
                val y1 = (maze.rowsNumber - cell.row) * CELL_SIZE

                if (cell.visited) {
                    g.color = Color(100, 100, 150, 50)
                    g.fillRect(x1, y2, CELL_SIZE, CELL_SIZE)
                }

                g.color = Color.black
                if (!cell.isLinkedTo("up"))
                    g.drawLine(x1, y2, x2, y2)
                if (!cell.isLinkedTo("right"))
                    g.drawLine(x2, y1, x2, y2)
                if (!cell.isLinkedTo("down"))
                    g.drawLine(x1, y1, x2, y1)
                if (!cell.isLinkedTo("left"))
                    g.drawLine(x1, y1, x1, y2)
            }
        }}
    }

    fun refresh() {
        image.graphics.color = Color.WHITE
        image.graphics.fillRect(0, 0, maze.colsNumber * CELL_SIZE + 1, maze.rowsNumber * CELL_SIZE + 1)
        panel.updateUI()
    }
}