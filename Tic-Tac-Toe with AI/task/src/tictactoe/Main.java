package tictactoe;

import java.awt.Point;
import java.util.Scanner;

public class Main {
    public static void println(String string) {
        System.out.println(string);
    }
    public static void print(String string) {
        System.out.print(string);
    }

    public static String getString(String input) {
        Scanner scanner = new Scanner(System.in);
        print(input);
        return scanner.nextLine();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameEngine gameEngine = new GameEngine(3);
        println(gameEngine.toString());

        int row;
        int col;
        int err;
        gameEngine.statXO(); // Сбор статистики по заполненным клеткам
        String step = "X";
        String checkGameStr = "";

        while (checkGameStr.equals("")) { // Цикл получения координат - ожидание хода, проверка результатов
            try {
                if (step.equals("X")) {
                    print("Enter the coordinates: ");
                    String[] str = scanner.nextLine().split(" "); // Запрашиваем ход игрока,
                    if ((str.length < 2) || (!isNumeric(str[0])) || (!isNumeric(str[1]))) {
                        println("You should enter numbers!");
                        continue;
                    }
                    row = Integer.parseInt(str[0]);
                    col = Integer.parseInt(str[1]);
                } else {
                    println("Making move level \"easy\" (" + step + "): ");
                    Point point = gameEngine.turnAI();
                    row = point.x + 1;
                    col = point.y + 1;
                }
                err = gameEngine.setCoordinates(row, col, step); // устанавливаем ход на доску
                switch (err) {
                    case 0: {
                        gameEngine.statXO(); // Сбор статистики по заполненным клеткам
                        checkGameStr = gameEngine.checkGame();
                        println(gameEngine.toString());
                        step = (step.equals("O")) ? "X" : "O";
                        break;
                    }
                    case 1: println("Coordinates should be from 1 to 3!"); break;
                    case 2: println("This cell is occupied! Choose another one!"); break;
                }
            } catch (Exception e) {
                println("You should enter numbers!");
            }
        }
        println(checkGameStr);
    }
}