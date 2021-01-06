package Myanrueller;

import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Translate;

import java.util.ArrayList;

public class GoPiece extends Group {

    private int x, y;
    private ArrayList<Integer> history;
    private int player;
    private Ellipse piece;
    private Translate translate;

    public GoPiece(int x, int y, int player){
        this.x = x;
        this.y = y;
        history = new ArrayList<Integer>();
        translate = new Translate();
        piece = new Ellipse();

        setPiece(player);

        piece.getTransforms().add(translate);
        getChildren().add(piece);

        DropShadow ds = new DropShadow(5.0, 3.0, 3.0, Color.color(0.4, 0.4, 0.4));
        piece.setEffect(ds);
    }

    public boolean isRepeatableState(){
        int size = history.size();
        if(size < 5) return false;
        return history.get(size - 1) == history.get(size - 3);
    }

    public void undoLastMove(){
        player = history.get(history.size() - 2);
        setColor(player);
        history.remove(history.size() - 1);
    }

    @Override
    public void resize(double width, double height){
        super.resize(width, height);

        piece.setCenterX(width / 2);
        piece.setCenterY(width / 2.2);
    }

    @Override
    public void relocate(double x, double y){
        super.relocate(x, y);
        translate.setX(x);
        translate.setY(y);
    }

    public void setPiece(final int Type){
        player = Type;
        history.add(Type);
        setColor(player);
    }

    private void setColor(int player){
        switch (player){
            case 0:
                piece.setFill(Color.TRANSPARENT);
                break;
            case 1:
                piece.setFill(Color.WHITE);
                break;
            case 2:
                piece.setFill(Color.BLACK);
        }
    }

    // method that will set the piece type
    public void setPiecegrey() {

        piece.setFill(Color.rgb(0,0,0,0.5));
    }

    // method that will set the piece type
    public void setPiecewhite() {

        piece.setFill(Color.TRANSPARENT);
    }

    // returns the type of this piece
    public int getPiece() {
        // NOTE: this is to keep the compiler happy until you get to this point
        int player_value = 0;

        if(piece.getFill() == Color.WHITE) {

            player_value = 2;

        }else if(piece.getFill() == Color.BLACK) {

            player_value = 1;

        }

        return player_value;
    }

    public boolean isEmpty(){
        return this.player == 0;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlayer(){
        return player;
    }

    public void setForMoveLevel(int level) {
        if (history.size() < level) history.add(player);
    }

}
