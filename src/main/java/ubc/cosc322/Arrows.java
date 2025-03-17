package ubc.cosc322;

public class Arrows extends Tiles{
	public Arrows(int row, int col) {
		super(row,col);
	}

	public int[] move() {
		return new int[] {this.getRow(), this.getColumn()};
	}
}