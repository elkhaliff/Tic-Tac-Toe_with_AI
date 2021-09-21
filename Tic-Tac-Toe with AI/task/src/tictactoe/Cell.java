package tictactoe;

public enum Cell {
    X("X"), O("O"), EMPTY(" ");
    final private String value;

    Cell(String c) {
        this.value = c;
    }

    public static Cell getOtherSide(Cell cell) {
        return cell.equals(X) ? O : X;
    }

    public String getValue() { return value; }
}
