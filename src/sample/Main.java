package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Face Recognition Prototype");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
//        Controller.startWebcamStream();

    }


    public static void main(String[] args) throws IOException {
//        System.out.println("sup");
//        BufferedImage br = WebcamHelper.getImage();

        launch(args);

    }
}
