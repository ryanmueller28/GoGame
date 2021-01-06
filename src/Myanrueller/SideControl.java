package Myanrueller;

import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.util.Duration;

public class SideControl extends VBox {
    private StackPane currentPlayer;
    private Label lblCurrentPlayer;

    private GridPane playerControl;
    private Label lblMessage;
    private Button btnPass;
    private Button btnOfferDraw;
    private Button btnEndGame;

    private VBox playerDisplay;
    private StackPane titleBox;
    private Label title;
    private GridPane scores;
    private Label lblPlayer1Name;
    private Label lblPlayer2Name;
    private Label lblPlayer1Score;
    private Label lblPlayer2Score;
    private Ellipse piece1;
    private Ellipse piece2;

    private int score1 = 0;
    private int score2 = 0;
    private String currentPlayerName = "Player 1";
    private String player1Name = "Player 1";
    private String player2Name = "Player 2";

    private GoControl goControl;

    SideControl(GoControl control){
        this.goControl = control;

        initGUI();

        goControl.setSideControl(this);
    }

    public void initGUI(){
        this.setPrefSize(800, 600);

        initCurrentPlayer();

        initPlayerControl();

        initPlayerDisplay();

        initEvent();
    }

    public void initCurrentPlayer(){
        currentPlayer = new StackPane();

        currentPlayer.setId("CurrentPlayer");
        lblCurrentPlayer = new Label();
        lblCurrentPlayer.textProperty().bind(new SimpleStringProperty(currentPlayerName));
        lblCurrentPlayer.setId("lblCurrentPlayer");

        currentPlayer.getChildren().add(lblCurrentPlayer);
        currentPlayer.setPrefSize(300, 200);
        this.getChildren().add(currentPlayer);
    }

    public void initPlayerControl(){
        playerControl = new GridPane();
        lblMessage = new Label("");
        btnPass = new Button("Pass");
        btnOfferDraw = new Button("Reset");

        btnEndGame = new Button("End Game");

        lblMessage.setId("Message");

        btnPass.setId("Action_Pass");
        btnOfferDraw.setId("Action_Reset");
        btnEndGame.setId("Action_END");

        playerControl.add(lblMessage, 0, 0, 4, 1);
        playerControl.add(btnPass, 0, 1, 1, 1);
        playerControl.add(btnOfferDraw, 1, 1, 1, 1);
        playerControl.add(btnEndGame, 2, 1, 1, 1);

        playerControl.setHgap(15);
        playerControl.setVgap(40);

        playerControl.setPrefSize(300, 200);

        playerControl.setAlignment(Pos.CENTER);
    }

    public void initPlayerDisplay(){
        playerDisplay = new VBox();
        titleBox = new StackPane();
        title = new Label("Players");
        scores = new GridPane();

        lblPlayer1Name = new Label(player1Name);
        lblPlayer2Name = new Label(player2Name);
        lblPlayer1Score = new Label();
        lblPlayer2Score = new Label();

        playerDisplay.setId("playerDisplay");
        playerDisplay.setSpacing(10);

        title.setId("title");

        titleBox.getChildren().add(title);
        titleBox.setId("titleBox");
        titleBox.setPrefSize(300, 400);

        piece1.setRadiusX(20);
        piece1.setRadiusY(20);
        piece2.setRadiusX(20);
        piece2.setRadiusY(20);
        DropShadow ds = new DropShadow();

        piece1.setEffect(ds);
        piece2.setEffect(ds);
        piece2.setFill(Color.WHITE);

        scores.add(piece1, 0, 0);
        scores.add(lblPlayer1Name, 1, 0);
        scores.add(lblPlayer1Score, 2, 0);
        scores.add(piece2, 0, 1);
        scores.add(lblPlayer2Name, 1, 1);
        scores.add(lblPlayer2Score, 2, 1);

        InnerShadow innerShadow = new InnerShadow(BlurType.GAUSSIAN, Color.color(0.4, 0.4, 0.4), 10, 0, 2, 2);

        scores.setEffect(innerShadow);

        scores.setPrefSize(300, 160);

        lblPlayer1Name.setId("LabelScore");
        lblPlayer2Name.setId("LabelScore");
        lblPlayer1Score.setId("LabelScore");
        lblPlayer2Score.setId("LabelScore");

        scores.setHgap(40);
        scores.setHgap(30);

        scores.setAlignment(Pos.CENTER);
        scores.setId("scores");

        playerDisplay.getChildren().add(titleBox);
        playerDisplay.getChildren().add(scores);
        playerDisplay.setPrefSize(300, 200);

        this.getChildren().add(playerDisplay);
    }

    public void initEvent(){
        btnPass.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    goControl.pass();
                    updateDisplay("");
                }catch (Exception e){
                    updateDisplay(e.getMessage());
                }
            }
        });

        btnEndGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                goControl.endGame();
                updateDisplay();
            }
        });

        btnOfferDraw.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateScoreLabel(goControl.updateScore());

                if (lblCurrentPlayer.getText().equals(player2Name)){
                    updateCurrentPlayer();
                }
            }
        });

    }

    public void updateDisplay(String message) {
        lblMessage.textProperty().bind(new SimpleStringProperty(message));
        if (message.equals("")){
            updateScoreLabel(goControl.updateScore());
            updateCurrentPlayer();
        }
    }

    public void updateDisplay(){
        updateDisplay("");
    }

    public void updateScoreLabel(int[] scores){
        score1 = scores[0];
        score2 = scores[1];

        lblPlayer1Score.textProperty().bind(new SimpleIntegerProperty(score1).asString());
        lblPlayer2Score.textProperty().bind(new SimpleIntegerProperty(score2).asString());
    }

    public void updateCurrentPlayer(){
        if (lblCurrentPlayer.getText().equals(player1Name)){
            currentPlayerName = player2Name;
        }else{
            currentPlayerName = player1Name;
        }

        ScaleTransition st = new ScaleTransition(Duration.millis(100), lblCurrentPlayer);
        st.setByX(1f);
        st.setByY(1f);
        st.setCycleCount(4);
        st.setAutoReverse(true);

        st.play();
        lblCurrentPlayer.textProperty().bind(new SimpleStringProperty(currentPlayerName));
    }

    @Override
    public void resize(double width, double height){
        super.resize(width, height);
    }


}
