package fr.qdorme

import java.awt.image.BufferedImage

class Maze(val cols:Int, val rows:Int, mask: BufferedImage?){

    val cells = mutableListOf<Cell>()
    private val unvisitedCells = mutableListOf<Cell>()

    init{
        for(col in 0 until cols){
            for (row in 0 until rows){
                if (mask == null) {
                    val cell = Cell(col, row)
                    cells.add(cell)
                    unvisitedCells.add(cell)
                }else{
                    val cell = Cell(col, row, mask.getRGB(col, row) == -1)
                    cells.add(cell)
                    unvisitedCells.add(cell)
                }
            }
        }
    }

    fun getPossibleCellsToConnect(cell: Cell):List<Cell>{
        val possibleCells = mutableListOf<Cell>()
        val indexUp = cell.row-1 + cell.col*rows
        val indexRight = cell.row + (cell.col+1)*rows
        val indexDown = cell.row+1 + cell.col*rows
        val indexLeft = cell.row + (cell.col-1)*rows

        if(indexUp > -1 && indexUp < cells.size && !cells[indexUp].visited)
            possibleCells.add(cells[indexUp])
        if(indexRight > -1 && indexRight < cells.size && !cells[indexRight].visited)
            possibleCells.add(cells[indexRight])
        if(indexDown > -1 && indexDown < cells.size && !cells[indexDown].visited)
            possibleCells.add(cells[indexDown])
        if(indexLeft > -1 && indexLeft < cells.size && !cells[indexLeft].visited)
            possibleCells.add(cells[indexLeft])

        return possibleCells
    }

    fun generate(){
        var currentCell:Cell? = unvisitedCells.shuffled()[0]
        while (unvisitedCells.size > 0){
            if(currentCell == null) {
                currentCell = cells.filter { cell -> getPossibleCellsToConnect(cell).size > 0 }.shuffled()[0]
            }
            currentCell.visited=true
            unvisitedCells.remove(currentCell)
            val possibleCellsToConnect = getPossibleCellsToConnect(currentCell)
            if(possibleCellsToConnect.size>0){
                var newCell = possibleCellsToConnect.shuffled()[0]
                currentCell.linkCell(newCell)
                currentCell = newCell
            }else{
                currentCell = null
            }
        }
    }
}