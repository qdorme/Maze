package fr.qdorme

import java.awt.image.BufferedImage
import java.util.*

class Maze(val colsNumber:Int, val rowsNumber:Int, mask: BufferedImage?): Observable() {

    val cells = mutableListOf<List<Cell>>()
    private val unvisitedCells = mutableListOf<Cell>()
    var generated = false

    init{
        for(col in 0 until colsNumber){
            val rows = mutableListOf<Cell>()
            cells.add(rows)
            for (row in 0 until rowsNumber){
                if (mask == null) {
                    val cell = Cell(col, row)
                    rows.add(cell)
                    unvisitedCells.add(cell)
                }else{
                    val cell = Cell(col, row, mask.getRGB(col, rowsNumber - row -1) == -1)
                    rows.add(cell)
                    if(cell.active)
                        unvisitedCells.add(cell)
                }
            }
        }
    }

    private fun getPossibleCellsToGo(cell: Cell):MutableList<Cell>{
        val possibleCells = getNeighbors(cell)
        possibleCells.retainAll { cell -> !cell.visited }
        return possibleCells
    }

    private fun getPossibleCellsToConnect(cell: Cell):MutableList<Cell>{
        val possibleCells = getNeighbors(cell)
        possibleCells.retainAll { cell -> cell.visited }
        return possibleCells
    }

    private fun getNeighbors(cell: Cell):MutableList<Cell>{
        val possibleCells = mutableListOf<Cell>()
        if(cell.row - 1 >= 0 && cells[cell.col][cell.row-1].active)
            possibleCells.add(cells[cell.col][cell.row-1])
        if(cell.row + 1 < rowsNumber && cells[cell.col][cell.row+1].active)
            possibleCells.add(cells[cell.col][cell.row+1])
        if(cell.col - 1 >= 0 && cells[cell.col-1][cell.row].active)
            possibleCells.add(cells[cell.col-1][cell.row])
        if(cell.col + 1 < colsNumber && cells[cell.col+1][cell.row].active)
            possibleCells.add(cells[cell.col+1][cell.row])

        if(possibleCells.size < 4)
            cell.bordered = true
        return possibleCells
    }

    fun generate(){
        val remainingPossibleCells = mutableListOf<Cell>()
        var currentCell:Cell? = unvisitedCells.shuffled()[0]
        while (unvisitedCells.size > 0){
            //println("=======================================")
            //println(" current cell $currentCell")
            if(currentCell == null) {
                //println(" select cell from remaining cells")
                currentCell = remainingPossibleCells.shuffled()[0]
                //println(" select cell $currentCell")
                val closeVisitedCells = getPossibleCellsToConnect(currentCell)
                if(closeVisitedCells.size > 0){
                    val neighbor = closeVisitedCells.shuffled()[0]
                    currentCell.linkCell(neighbor)
                    //println(" connect remaiing to $neighbor")
                }
            }
            currentCell.visited=true
            unvisitedCells.remove(currentCell)
            val possibleCellsToGo = getPossibleCellsToGo(currentCell)
            //println(" possible cells = $possibleCellsToGo")
            currentCell = if(possibleCellsToGo.size>0){
                val newCell = possibleCellsToGo.shuffled()[0]
                //println(" selected cell $newCell")
                possibleCellsToGo.remove(newCell)
                currentCell.linkCell(newCell)
                remainingPossibleCells.addAll(possibleCellsToGo)
                newCell
            }else{
                null
            }
            remainingPossibleCells.retainAll{ cell -> !cell.visited }
            //println(" remaining cells $remainingPossibleCells")
            setChanged()
            notifyObservers()
            Thread.sleep(35L)
        }
    }

    fun findEntries(){
        var arbitraryCell = cells.filter { cols -> cols.any { cell -> cell.active } }[0].filter{cell -> cell.active}[0]
        propagateDistance(arbitraryCell,1)
        val firstEntry = cells
                .map { cols -> cols.filter{cell -> cell.bordered }.maxBy { it.distance  } }
                .maxBy { it!!.distance }!!
        firstEntry?.entry=true

        cells.forEach { cols->cols.forEach { if (it.active) it.distance = 0 } }
        propagateDistance(firstEntry,1)
        val secondEntry = cells
                .map { cols -> cols.filter{cell -> cell.bordered }.maxBy { it.distance  } }
                .maxBy { it!!.distance }!!
        secondEntry?.entry=true

        generated = true

        setChanged()
        notifyObservers()
    }

    fun propagateDistance(cell:Cell,distance:Int){
        cell.distance = distance
        setChanged()
        notifyObservers()
        Thread.sleep(35L)
        cell.linkedCells.forEach { cell ->
            if(cell.distance == 0)
                propagateDistance(cell,distance +1)
        }
    }
}