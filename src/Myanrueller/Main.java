package Myanrueller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    private HBox spMainLayout;
    private GoControl go;
    private SideControl side;
    @Override
    public void init(){
        spMainLayout = new HBox();
        go = new GoControl();
        spMainLayout.getChildren().add(go);
        side = new SideControl(go);

        spMainLayout.getChildren().add(side);
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Go Game");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
