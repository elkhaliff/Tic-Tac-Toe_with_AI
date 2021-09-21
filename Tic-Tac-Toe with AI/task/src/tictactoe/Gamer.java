package tictactoe;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Gamer {
    private final Cell cell;
    private final GamerType gamerType;

    public enum GamerType {
        USER, EASY, MEDIUM, HARD
    }

    protected Gamer(Cell cell, String gamerType) {
        this.cell = cell;
        this.gamerType = GamerType.valueOf(gamerType);
    }

    public void turn(GameMap gameMap) {
        if (gamerType.equals(GamerType.USER))
            turnUser(gameMap);
        else
            turnAI(gameMap);
    }

    private static boolean isNumeric(String strNum) {
        if (strNum == null) return false;
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void turnUser(GameMap gameMap) {
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

            err = gameMap.setCoordinates(row, col, cell); // Заполняем ячейку соответствующим символом
            if (err == 1) {
                println("Coordinates should be from 1 to 3!");
            } else if (err == 2) {
                println("This cell is occupied! Choose another one!");
            }
        }
    }

    private void turnAI(GameMap gameMap) {
        println("Making move level \"" + gamerType + "\" (" + cell + "): ");
        ArrayList<Point> points = gameMap.getAvailableCells();
        Point point;

        switch (gamerType) {
            case EASY: {
                point = getEasy(points);
                break;
            }
            case MEDIUM: {
                point = getMedium(points, gameMap);
                break;
            }
            case HARD: {
                point = getHard(points, gameMap);
                break;
            }
            default: {
                point = points.get(0);
                break;
            }
        }

        gameMap.setOnField(point.x, point.y, cell); // Заполняем ячейку соответствующим символом
    }

    private Point getMedium(ArrayList<Point> points, GameMap gameMap) {
        GameState gameState;

        for (Point point: points) {
            gameMap.setOnField(point.x, point.y, cell);
            gameState = gameMap.checkGame();
            if ((Cell.X.equals(cell) && GameState.X_WINS.equals(gameState)) ||
                    (Cell.O.equals(cell) && GameState.O_WINS.equals(gameState))) {
                gameMap.setOnField(point.x, point.y, Cell.EMPTY);
                return point;
            }
            gameMap.setOnField(point.x, point.y, Cell.EMPTY);
        }

        for (Point point: points) {
            Cell otherSide = Cell.getOtherSide(cell);
            gameMap.setOnField(point.x, point.y, otherSide);
            gameState = gameMap.checkGame();
            if ((Cell.X.equals(otherSide) && GameState.X_WINS.equals(gameState)) ||
                    (Cell.O.equals(otherSide) && GameState.O_WINS.equals(gameState))) {
                gameMap.setOnField(point.x, point.y, Cell.EMPTY);
                return point;
            }
            gameMap.setOnField(point.x, point.y, Cell.EMPTY);
        }

        return getEasy(points);
    }

    private Point getHard(ArrayList<Point> points, GameMap gameMap) {
        Point bestTurn = getEasy(points);
        int bestScore = Integer.MIN_VALUE;

        for (Point point: points) {
            gameMap.setOnField(point.x, point.y, cell);
            int score = minimax(gameMap, Cell.getOtherSide(cell), false, cell, 1);
            gameMap.setOnField(point.x, point.y, Cell.EMPTY);
            if (score > bestScore) {
                bestScore = score;
                bestTurn = point;
            }
        }

        return bestTurn;
    }

    private int minimax(GameMap gameMap, Cell otherSide, boolean isMaximize, Cell cell, int depth) {
        GameState gameState = gameMap.checkGame();
        Point point;

        switch (gameState) {
            case X_WINS:
                return cell == Cell.X ? 10 - depth : depth - 10;
            case O_WINS:
                return cell == Cell.O ? 10 - depth : depth - 10;
            case DRAW:
                return 0;
        }

        int bestScore = isMaximize ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < gameMap.getRows(); i++) {
            for (int j = 0; j < gameMap.getCols(); j++) {
                if (gameMap.isEmpty(i, j)) {
                    point = new Point(i, j);
                    gameMap.setOnField(point.x, point.y, otherSide);
                    int score = minimax(gameMap, Cell.getOtherSide(otherSide), !isMaximize, cell, depth + 1);
                    gameMap.setOnField(point.x, point.y, Cell.EMPTY);
                    bestScore = isMaximize ? Math.max(bestScore, score) : Math.min(bestScore, score);
                }
            }
        }

        return bestScore;
    }

    private Point getEasy(ArrayList<Point> points) {
        Random random = new Random();
        int rnd = random.nextInt(points.size());
        return points.get(rnd);
    }

    private static void print(String string) {
        System.out.print(string);
    }
    private static void println(String string) {
        System.out.println(string);
    }
}
