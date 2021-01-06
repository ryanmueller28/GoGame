package Myanrueller;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class GoBoard extends Pane {
    private Rectangle background;
    private Line[] horizontal;
    private Line[] vertical;
    private Translate[] horizontalT;
    private Translate[] verticalT;

    private GoPiece[][] render;
    private int currentPlayer = 1;
    private boolean inPlay = true;
    private int player1Score;
    private int player2Score;

    private double cellWidth;
    private double cellHeight;

    private int[][] surrounding;

    private boolean[][] canReverse;
    private int passCount = 0;
    private GameLogicInterface gameLogic;

    GoBoard(){
        render = new GoPiece[7][7];
        horizontal = new Line[7];
        vertical = new Line[7];
        horizontalT = new Translate[7];
        verticalT = new Translate[7];

        surrounding = new int[3][3];

        canReverse = new boolean[3][3];
        initializeRender();

        gameLogic = new GameLogic(render);

        initializeLinesBackground();

        resetGame();
    }

    private void initializeLinesBackground() {

        background = new Rectangle();
        Image image = new Image("wood.jpg");

        ImagePattern imagePattern = new ImagePattern(image);
        background.setId("pane");
        background.setFill(imagePattern);

        for (int i = 0; i < 7; i++){
            horizontal[i] = new Line();
            horizontal[i].setStroke(Color.BLACK);
            horizontal[i].setStartX(0);
            horizontal[i].setStartY(0);
            horizontal[i].setEndX(0);

            vertical[i] = new Line();
            vertical[i].setStroke(Color.BLACK);
            vertical[i].setStartX(0);
            vertical[i].setStartY(0);
            vertical[i].setEndX(0);

            horizontalT[i] = new Translate(0,0);
            horizontal[i].getTransforms().add(verticalT[i]);

            verticalT[i] = new Translate(0, 0);
            vertical[i].getTransforms().add(verticalT[i]);

        }

        getChildren().add(background);

        for (int i = 0; i < 7; i++){
            getChildren().add(horizontal[i]);
            getChildren().add(vertical[i]);
        }
    }

    public void resetGame() {
        resetRenders();

        initializeRender();

        gameLogic = new GameLogic(render);
    }

    public void resetRenders() {
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++){
                render[i][j].setPiece(0);
            }
        }
    }

    @Override
    public void resize(double width, double height){
        super.resize(width, height);

        cellWidth = width / 7.0;
        cellHeight = height / 7.0;

        background.setWidth(width);
        background.setHeight(height);

        horizontalResizeRelocate(width);
        verticalResizeRelocate(height);

        pieceResizeRelocate();
    }

    private void horizontalResizeRelocate(final double width){
        for (int i = 0; i < 7; i++){
            verticalT[i].setX((i + 0.5) * cellWidth);
            vertical[i].setEndY(width - cellHeight / 2);
            vertical[i].setStartY(cellHeight / 2);
        }
    }

    private void verticalResizeRelocate(final double height){
        for (int i = 0; i < 7; i++){
            verticalT[i].setX((i + 0.5) * cellWidth);
            vertical[i].setEndX(height - cellHeight / 2);
            vertical[i].setStartY(cellHeight / 2);
        }
    }

    private void swapPlayers(){
        this.currentPlayer = currentPlayer == 1 ? 2 : 1;
    }

    private void updateScores(){
        player1Score = 0;
        player2Score = 0;

        for (int i = 0; i < render.length; i++){
            for (int j = 0; j < render[i].length; j++){

                if (this.render[i][j].getPiece() == 1){
                    player1Score++;
                }

                if (this.render[i][j].getPiece() == 2){
                    player2Score++;
                }
            }
        }

        // Update label view
    }

    private void pieceResizeRelocate(){
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++){
                render[i][j].relocate(i * cellWidth, j * cellHeight);
                render[i][j].resize(cellWidth, cellHeight);
            }
        }
    }

    private int getPiece(final int x, final int y){
        int pieceSelected;

        if (x >= 0 && y >= 0 && x < 7 && y <7){
            pieceSelected = render[x][y].getPiece();
        }else{
            pieceSelected = -1;
        }
        return pieceSelected;
    }

    public void placePiece(final double x, final double y) throws Exception {
        final int cellX = (int) (x / cellWidth);
        final int cellY = (int) (y / cellHeight);

        if (!inPlay){ throw new Exception("Game over"); }

        gameLogic.placePiece(cellX, cellY, currentPlayer);

        swapPlayers();

        passCount = 0;
    }


    private void initializeRender() {

        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++){
                render[i][j] = new GoPiece(i , j, 0);
                getChildren().add(render[i][j]);
            }
        }
    }

    public int[] getScore(){
        int[] scores = new int[2];

        scores[0] = gameLogic.playerOneScore();
        scores[1] = gameLogic.playerTwoScore();

        return scores;
   }

    public void pass() throws Exception{
        passCount = passCount + 1;
        if (passCount >= 2){
            inPlay = false;
        }

        if (passCount >= 2) throw new Exception("Game over");
        swapPlayers();

    }

    public void endGame(){
        gameLogic.endGame();
    }

}
