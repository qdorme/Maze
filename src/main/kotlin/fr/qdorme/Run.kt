package fr.qdorme

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {

    var mask: BufferedImage?
    var cols:Int
    var rows:Int
    var maze : Maze
    var sleep = 15L
    when (args.size){
        1 ->  {
            mask = ImageIO.read(File(args[0]))
            cols = mask.data.width
            rows = mask.data.height
            maze = Maze(cols, rows, mask, sleep)
        }
        2 -> {
            try {
                cols = args[0].toInt() + 2
                rows = args[1].toInt() + 2
                maze = Maze(cols, rows,null, sleep)
            }catch (e:NumberFormatException){
                mask = ImageIO.read(File(args[0]))
                cols = mask.data.width
                rows = mask.data.height
                sleep = args[1].toLong()
                maze = Maze(cols, rows, mask, sleep)
            }
        }
        3 -> {
            cols = args[0].toInt() + 2
            rows = args[1].toInt() + 2
            sleep = args[2].toLong()
            maze = Maze(cols, rows,null, sleep)
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

