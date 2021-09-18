package tictactoe;

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
        String inputStr = getString("Enter the cells: ");
        print(gameEngine.toString());

        int row;
        int col;
        int err = -1;
        gameEngine.statXO(); // Сбор статистики по заполненным клеткам
        String step = "X";
        String checkGameStr = "";

        while (checkGameStr == "") { // Цикл получения координат - ожидание хода, проверка результатов
            print("Enter the coordinates (" + step + "): ");
            try {
                String[] str = scanner.nextLine().split(" ");
                if ((str.length < 2) || (!isNumeric(str[0])) || (!isNumeric(str[1]))) {
                    println("You should enter numbers!");
                    continue;
                }
                row = Integer.parseInt(str[0]);
                col = Integer.parseInt(str[1]);
                err = gameEngine.setCoordinates(step, row, col); // Запрашиваем ход игрока, устанавливаем ход на доску
                switch (err) {
                    case 0: {
                        step = (step == "O") ? "X" : "O";
                        gameEngine.statXO(); // Сбор статистики по заполненным клеткам
                        checkGameStr = gameEngine.checkGame();
                        println(gameEngine.toString());
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