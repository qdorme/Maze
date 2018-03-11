package fr.qdorme

class Cell(val col : Int, val row:Int, val active:Boolean=true){
    var visited = false
    var distance = 0
    var bordered = false
    var entry = false
    var exit : Cell? = null

    val linkedCells = mutableListOf<Cell>()

    fun linkCell(cell: Cell){
        if (!linkedCells.contains(cell)){
            linkedCells.add(cell)
            cell.linkCell(this)
        }
    }

    fun isLinkedTo(to:String):Boolean{
        return when(to){
            "down" -> linkedCells.any{ cell -> cell.row == row - 1} || row-1 == exit?.row?:-2
            "right" -> linkedCells.any{ cell -> cell.col == col + 1} || col+1 == exit?.col?:-2
            "left" -> linkedCells.any{ cell -> cell.col == col - 1} || col-1 == exit?.col?:-2
            "up" -> linkedCells.any{ cell -> cell.row == row + 1} || row+1 == exit?.row?:-2
            else -> false
        }
    }

    override fun toString(): String {
        return "cell [$col-$row]"
    }

}