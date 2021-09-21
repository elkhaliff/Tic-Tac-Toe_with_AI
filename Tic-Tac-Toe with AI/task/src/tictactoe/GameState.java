package tictactoe;

public enum GameState {
    DRAW("Draw"),
    X_WINS("X wins"),
    O_WINS("O wins"),
    NOT_FINISHED(""); //Game not finished

    private String state;

    GameState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}