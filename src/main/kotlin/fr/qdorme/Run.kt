package fr.qdorme

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {

    var mask: BufferedImage?
    var cols:Int
    var rows:Int
    var maze : Maze
    when (args.size){
        1 ->  {
            mask = ImageIO.read(File(args[0]))
            cols = mask.data.width
            rows = mask.data.height
            maze = Maze(cols, rows, mask)
        }
        2 -> {
            cols = args[0].toInt()
            rows = args[1].toInt()
            maze = Maze(cols, rows,null)
        }
        else -> {
            println("arguments number [${args.size}] invalid")
            return
        }
    }

    val drawMaze = DrawMaze(maze)
    //drawMaze.refresh()
    maze.addObserver(drawMaze)
    maze.generate()
    maze.findEntries()
}

