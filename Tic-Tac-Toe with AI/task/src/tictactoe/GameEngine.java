package tictactoe;

import java.util.Scanner;

/**
 * Основной класс приложения
 *  @author Andrey Zotov aka OldFox
 *  rows - Количество строк
 *  cols - Количество колонок
 */
public class GameEngine {
    final String cmdExit = "exit";
    final String cmdStart = "start";

    GameMap gameMap;

    private Gamer gamer1;
    private Gamer gamer2;

    public GameEngine(int cells) {
        gameMap = new GameMap(cells);
    }

    public boolean setParamGame() {
        String input;
        final String badParams = "Bad parameters!";
        while (true) {
            input = getString("Input command: ").trim();
            if (input.equals(cmdExit)) {
                return false;
            } else if (input.contains(cmdStart)) {
                String[] arrCommand = input.toUpperCase().split("\\s+");
                if (arrCommand.length != 3) {
                    println(badParams);
                } else {
                    gamer1 = new Gamer(Cell.X, arrCommand[1]);
                    gamer2 = new Gamer(Cell.O, arrCommand[2]);
                    return true;
                }
            } else {
                println(badParams);
            }
        }
    }

    public void startGame() {
        println(gameMap.toString());

        Cell side = Cell.X;
        String checkGameStr = "";
        while (checkGameStr.equals("")) { // Цикл ожидание хода, проверка результатов
            if (Cell.X.equals(side)) {
                gamer1.turn(gameMap);
            } else {
                gamer2.turn(gameMap);
            }
            gameMap.statXO(); // Сбор статистики по заполненным клеткам
            checkGameStr = gameMap.checkGame().getState();
            println(gameMap.toString());
            side = (side.equals(Cell.O)) ? Cell.X : Cell.O;
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
}