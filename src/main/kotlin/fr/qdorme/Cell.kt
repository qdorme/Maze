package fr.qdorme

class Cell(val col : Int, val row:Int, val active:Boolean=true){
    var visited = false

    val linkedCells = mutableListOf<Cell>()

    fun linkCell(cell: Cell){
        if (!linkedCells.contains(cell)){
            linkedCells.add(cell)
            cell.linkCell(this)
        }
    }

    fun isLinkedTo(to:String):Boolean{
        return when(to){
            "up" -> linkedCells.any{ cell -> cell.row == row - 1}
            "right" -> linkedCells.any{ cell -> cell.col == col + 1}
            "left" -> linkedCells.any{ cell -> cell.col == col - 1}
            "down" -> linkedCells.any{ cell -> cell.row == row + 1}
            else -> false
        }
    }

}