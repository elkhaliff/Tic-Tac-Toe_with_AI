package tictactoe;

public class Main {
    public static void println(String string) {
        System.out.println(string);
    }
    public static void print(String string) {
        System.out.print(string);
    }

    public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine(3);
        if (!gameEngine.setParamGame()) {
            return;
        } else {
            gameEngine.startGame();
        }
    }
}