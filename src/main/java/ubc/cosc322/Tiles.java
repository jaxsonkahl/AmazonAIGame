package ubc;

public class Tiles {
    private int row, column;

    public Tiles(int row, int column) {
		this.row = row;
		this.column = column;
	}

    public int getRow(){
        return row;
    }

    public void setRow(int row){
        this.row = row;
    }

    public int getColumn(){
        return column;
    }

    public void setColumn(int column){
        this.column = column;
    }

}
