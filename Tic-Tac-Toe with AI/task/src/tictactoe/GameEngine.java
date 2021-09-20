package tictactoe;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Основной класс приложения
 *  @author Andrey Zotov aka OldFox
 *  rows - Количество строк
 *  cols - Количество колонок
 */
public class GameEngine {
    private final int rows; // Количество строк
    private final int cols; // Количество столбцов
    final String cmdExit = "exit";
    final String cmdStart = "start";
    final String cmdEasy = "easy";
    final String cmdUser = "user";

    final String symbolX = "X";
    final String symbolO = "O";
    final String empty = " ";
    private int cntX = 0;
    private int cntO = 0;
    String winX;
    String winO;

    private final String [][] fieldMap;
    private String gamerX;
    private String gamerO;

    public GameEngine(int cells) {
        this.rows = cells;
        this.cols = cells;
        /**
         * Инициализация массива рабочей области крестиков-ноликов
         */
        fieldMap = new String[rows][cols];

        StringBuilder wx = new StringBuilder();
        StringBuilder wo = new StringBuilder();
        for (int i = 0; i < cols; i++) {
            wx.append(symbolX);
            wo.append(symbolO);
        }
        winX = wx.toString();
        winO = wo.toString();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                setOnField(i, j, empty);
            }
        }
    }

    /**
     * Установка хода на доску
     */
    public void setOnField(int row, int col, String st) {
        fieldMap[row][col] = st;
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
                outStr.append(fieldMap[i][j]); outStr.append(" ");
            }
            outStr.append("|\n");
        }
        outStr.append(border);
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
                switch (fieldMap[i][j]) {
                    case symbolX: cntX++; break;
                    case symbolO: cntO++; break;
                }
            }
        }
    }

    /**
     * Проверка поля на ничью и выигрыш одного из игроков
     */
    public String checkGame() {
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

        if (isWinX) return "X wins";
        if (isWin0) return "O wins";
        if (!isEmptyCell()) return "Draw";
        return ""; // Game not finished
    }

    /**
     * Получение столбца данных
     */
    private String getCol(int col) {
        StringBuilder out = new StringBuilder();
        for (int r=0; r < rows; r++) {
            out.append(fieldMap[r][col]);
        }
        return out.toString();
    }

    /**
     * Получение строки данных
     */
    private String getRow(int row) {
        StringBuilder out = new StringBuilder();
        for (int c=0; c < cols; c++) {
            out.append(fieldMap[row][c]);
        }
        return out.toString();
    }

    /**
     * Получение правой диагонали
     */
    private String getRightDiagonal() {
        StringBuilder out = new StringBuilder();
        for (int c=0; c < cols; c++) {
            out.append(fieldMap[c][c]);
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
            out.append(fieldMap[r][--c]);
        }
        return out.toString();
    }

    /**
     * Проверка на незаполненность
     */
    public boolean isEmpty(int row, int col) {
        return (fieldMap[row][col].equals(empty));
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
    public int setCoordinates(int row, int col, String step) {
        if ((row < 1 || row > 3) || (col < 1 || col > 3)) {
            return 1;    // Coordinates should be from 1 to 3!
        }
        if (!isEmpty(row-1, col-1)) {
            return 2;    // This cell is occupied! Choose another one!
        }
        setOnField(row-1, col-1, step);
        return 0; // no errors
    }

    public boolean setParamGame() {
        String input;
        final String badParams = "Bad parameters!";
        while (true) {
            input = getString("Input command: ").trim();
            if (input.equals(cmdExit)) {
                return false;
            } else if (input.contains(cmdStart)) {
                String[] arrInput = input.split(" ");
                if ((arrInput.length < 3) ||
                    (!arrInput[1].equals(cmdEasy) && !arrInput[1].equals(cmdUser) &&
                    !arrInput[2].equals(cmdEasy) && !arrInput[2].equals(cmdUser))) {
                        println(badParams);
                } else {
                    gamerX = arrInput[1];
                    gamerO = arrInput[2];
                    return true;
                }
            } else {
                println(badParams);
            }
        }
    }

    public void startGame() {
        println(this.toString());

        String step = symbolX;
        String checkGameStr = "";

        while (checkGameStr.equals("")) { // Цикл получения координат - ожидание хода, проверка результатов
            if ((step.equals(symbolX) && gamerX.equals(cmdUser)) ||
                    (step.equals(symbolO) && gamerO.equals(cmdUser))) {
                turnUser(step);
            } else {
                String level = (step.equals(symbolX)) ? gamerX : gamerO;
                turnAI(step, level);
            }
            statXO(); // Сбор статистики по заполненным клеткам
            checkGameStr = checkGame();
            println(this.toString());
            step = (step.equals(symbolO)) ? symbolX : symbolO;
        }
        println(checkGameStr);
    }

    private static String getString(String input) {
        Scanner scanner = new Scanner(System.in);
        print(input);
        return scanner.nextLine();
    }

    private static void print(String string) {
        System.out.print(string);
    }

    private static void println(String string) {
        System.out.println(string);
    }

    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void turnUser(String step) {
        Scanner scanner = new Scanner(System.in);
        int row;
        int col;
        int err = -1;
        while (err != 0) {
            print("Enter the coordinates: ");
            String[] str = scanner.nextLine().split(" "); // Запрашиваем ход игрока,
            if ((str.length < 2) || (!isNumeric(str[0])) || (!isNumeric(str[1]))) {
                println("You should enter numbers!");
                continue;
            }
            row = Integer.parseInt(str[0]);
            col = Integer.parseInt(str[1]);

            err = setCoordinates(row, col, step); // устанавливаем ход на доску
            if (err == 1) {
                println("Coordinates should be from 1 to 3!");
            } else if (err == 2) {
                println("This cell is occupied! Choose another one!");
            }
        }
    }

    private void turnAI(String step, String level) {
        ArrayList<Point> points = new ArrayList<>();
        println("Making move level \"" + level + "\" (" + step + "): ");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isEmpty(i, j)) points.add(new Point(i, j));
            }
        }
        Random random = new Random();
        int rnd = random.nextInt(points.size());
        Point point = points.get(rnd);
        setCoordinates(point.x + 1, point.y + 1, step); // устанавливаем ход на доску
    }
}