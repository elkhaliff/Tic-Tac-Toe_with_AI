package tictactoe;

import java.awt.*;
import java.util.ArrayList;

public class GameMap {
    private final int rows; // Количество строк
    private final int cols; // Количество столбцов
    final String empty = " ";
    private int cntX = 0;
    private int cntO = 0;
    private int step;

    public int getStep() {
        return step;
    }

    String winX;
    String winO;

    private final Cell [][] fieldMap;

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public GameMap(int cells) {
        this.rows = cells;
        this.cols = cells;
        // Инициализация массива рабочей области крестиков-ноликов
        fieldMap = new Cell[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                setOnField(i, j, Cell.EMPTY);
            }
        }

        StringBuilder wx = new StringBuilder();
        StringBuilder wo = new StringBuilder();
        for (int i = 0; i < cells; i++) {
            wx.append(Cell.X.getValue());
            wo.append(Cell.O.getValue());
        }
        winX = wx.toString();
        winO = wo.toString();
        step = 0;

    }

    /**
     * Установка хода на доску
     */
    public void setOnField(int row, int col, Cell cell) {
        step++;
        fieldMap[row][col] = cell;
    }

    /**
     * Формирование строки из игрового поля данного класса
     * (в частности - получаем возможность вывода на печать)
     */
    @Override
    public String toString() {
        StringBuilder outStr = new StringBuilder();
        StringBuilder border = new StringBuilder();

        border.append("-".repeat(Math.max(0, cols * 3)));
        outStr.append(border);
        outStr.append("\n");
        for (int i = 0; i < rows; i++) {
            outStr.append("| ");
            for (int j = 0; j < cols; j++) {
                outStr.append(fieldMap[i][j].getValue()); outStr.append(" ");
            }
            outStr.append("|\n");
        }
        outStr.append(border);
        outStr.append("\n");

        return outStr.toString();
    }

    /**
     * Статистика крестиков и ноликов
     */
    public void statXO(){
        cntX = 0;
        cntO = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (Cell.X.equals(fieldMap[i][j]))
                    cntX++;
                else if (Cell.O.equals(fieldMap[i][j]))
                    cntO++;
            }
        }
    }

    /**
     * Проверка поля на ничью и выигрыш одного из игроков
     */
    public GameState checkGame() {
        boolean isWinX = false; // X wins
        boolean isWin0 = false; // O wins

        for (int r=0; r < rows; r++) {
            isWinX = getRow(r).equals(winX) || isWinX;
            isWin0 = getRow(r).equals(winO) || isWin0;
        }
        for (int c=0; c < rows; c++) {
            isWinX = getCol(c).equals(winX) || isWinX;
            isWin0 = getCol(c).equals(winO) || isWin0;
        }

        isWinX = getRightDiagonal().equals(winX) || getLeftDiagonal().equals(winX) || isWinX;
        isWin0 = getRightDiagonal().equals(winO) || getLeftDiagonal().equals(winO) || isWin0;

        if (isWinX) return GameState.X_WINS;
        if (isWin0) return GameState.O_WINS;
        if (!isEmptyCell()) return GameState.DRAW;
        return GameState.NOT_FINISHED;
    }

    /**
     * Получение столбца данных
     */
    private String getCol(int col) {
        StringBuilder out = new StringBuilder();
        for (int r=0; r < rows; r++) {
            out.append(fieldMap[r][col].getValue());
        }
        return out.toString();
    }

    /**
     * Получение строки данных
     */
    private String getRow(int row) {
        StringBuilder out = new StringBuilder();
        for (int c=0; c < cols; c++) {
            out.append(fieldMap[row][c].getValue());
        }
        return out.toString();
    }

    /**
     * Получение правой диагонали
     */
    private String getRightDiagonal() {
        StringBuilder out = new StringBuilder();
        for (int c=0; c < cols; c++) {
            out.append(fieldMap[c][c].getValue());
        }
        return out.toString();
    }

    /**
     * Получение левой диагонали
     */
    private String getLeftDiagonal() {
        StringBuilder out = new StringBuilder();
        var c = cols;
        for (int r=0; r < rows; r++) {
            out.append(fieldMap[r][--c].getValue());
        }
        return out.toString();
    }

    /**
     * Проверка на незаполненность
     */
    public boolean isEmpty(int row, int col) {
        return (Cell.EMPTY.equals(fieldMap[row][col]));
    }

    /**
     * Количество свободных ячеек (возможность хода)
     */
    private boolean isEmptyCell() {
        return (rows * cols - (cntX + cntO)) > 0;
    }

    /**
     * Проверка и установка нового значения
     */
    public int setCoordinates(int row, int col, Cell step) {
        if ((row < 1 || row > 3) || (col < 1 || col > 3)) {
            return 1;    // Coordinates should be from 1 to 3!
        }
        if (!isEmpty(row-1, col-1)) {
            return 2;    // This cell is occupied! Choose another one!
        }
        setOnField(row-1, col-1, step);
        return 0; // no errors
    }

    public ArrayList<Point> getAvailableCells() {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isEmpty(i, j)) points.add(new Point(i, j));
            }
        }
        return points;
    }
}
