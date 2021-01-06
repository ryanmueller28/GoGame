package Myanrueller;

public interface GameLogicInterface {
    public void placePiece(int x, int y, int player) throws Exception;

    public GoPiece getPiece(int x, int y);

    public boolean canPlacePiece(int x, int y);

    public boolean isGameOver();

    public String declareWinner();

    public int playerOneScore();

    public int playerTwoScore();

    public void endGame();

}
