package main;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class login implements Initializable {
    @FXML
    private ImageView i;

    private double xOffset = 0.0D;
    private double yOffset = 0.0D;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FadeTransition trans = new FadeTransition(Duration.seconds(2), i);
        trans.setFromValue(1.0);
        trans.setToValue(.20);
        trans.setCycleCount(FadeTransition.INDEFINITE);
        trans.setAutoReverse(true);
        trans.play();
    }

    public void home (ActionEvent event) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("home.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root, Color.TRANSPARENT));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
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
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
        //   stage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/c1.png")));
        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

}
