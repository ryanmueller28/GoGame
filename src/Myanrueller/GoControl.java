package Myanrueller;

import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class GoControl extends Control {

    GoBoard rbBoard;
    SideControl sideControl;

    public GoControl(){
        setSkin(new GoControlSkin(this));
        rbBoard = new GoBoard();
        rbBoard.setPrefSize(800, 600);
        getChildren().add(rbBoard);

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try{
                    rbBoard.placePiece(mouseEvent.getX(), mouseEvent.getY());
                    sideControl.updateDisplay();
                }catch (Exception e){
                    sideControl.updateDisplay(e.getMessage());
                }
            }
        });

        setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.SPACE)
                    rbBoard.resetGame();
            }
        });
    }

    public void setSideControl(SideControl sideControl) {
        this.sideControl = sideControl;
    }

    @Override
    public void resize(double width, double height){
        super.resize(width, height);
        rbBoard.resize(width, height);
    }

    public void pass() throws Exception{
        rbBoard.pass();
    }

    public int[] updateScore(){
        return rbBoard.getScore();
    }

    public void endGame(){
        rbBoard.endGame();
    }

    public void reset(){
        rbBoard.resetGame();
    }
}
