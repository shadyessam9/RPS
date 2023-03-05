package main;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    private double xOffset = 0;
    private double yOffset = 0;
    public static  String ip = "localhost";
    @Override
    public void start(Stage primaryStage) throws Exception{
      try{
          Parent root =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
          primaryStage.setScene(new Scene(root,Color.TRANSPARENT));
       //   primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/assetes/icon.png")));
          primaryStage.initStyle(StageStyle.UNDECORATED);
          primaryStage.initStyle(StageStyle.TRANSPARENT);
          root.setOnMousePressed(new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent event) {
                  xOffset = event.getSceneX();
                  yOffset = event.getSceneY();
              }
          });
          root.setOnMouseDragged(new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent event) {
                  primaryStage.setX(event.getScreenX() - xOffset);
                  primaryStage.setY(event.getScreenY() - yOffset);
              }
          });
          primaryStage.show();
      }catch(IOException e){
              System.out.println(e);
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
