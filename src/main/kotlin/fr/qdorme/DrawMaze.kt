package fr.qdorme

import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

val cellSize = 10

class DrawMaze(private val maze: Maze){

    private val canvas = BufferedImage(maze.cols * cellSize + 1 , maze.rows * cellSize + 1 , BufferedImage.TYPE_INT_ARGB)
    private val frame = JFrame("Maze")
    private val panel: JPanel

    init {
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.setSize(canvas.width + 13 , canvas.height + 60)
        frame.isVisible = true

        panel = object : JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                drawMaze(canvas.graphics)
                g.drawImage(canvas, 0, 0, maze.cols * cellSize + 1 , maze.rows * cellSize + 1 , this)
            }
        }
        frame.add(panel)
        panel.revalidate()
    }

    fun drawMaze(g:Graphics){
        maze.cells.forEach{ cell ->
            val x1 = cell.col * cellSize
            val x2 = (cell.col + 1) * cellSize
            val y2 = (maze.rows - cell.row) * cellSize
            val y1 = (maze.rows - cell.row + 1) * cellSize

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
    }

    fun refresh() {
        canvas.graphics.color = Color.WHITE
        canvas.graphics.fillRect(0, 0, maze.cols * cellSize + 1, maze.rows * cellSize + 1)
        panel.updateUI()
    }
}