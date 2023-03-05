package main;
import java.io.IOException;
import java.net.URL;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class splashscreen implements Initializable{
    @FXML
    private AnchorPane apane;
    private double xOffset = 0;
    private double yOffset = 0;
    boolean hasConnection ;
    ScheduledExecutorService executor1 = Executors.newScheduledThreadPool(1);
    @Override
    public void initialize(URL url, ResourceBundle rb){
        executor1.scheduleAtFixedRate(portRunnable, 0, 1, TimeUnit.SECONDS);
    }


    Runnable portRunnable = new Runnable() {
        public void run() {
            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                if (conn == null) {
                    hasConnection = false;
                } else {
                    hasConnection = true;
                    conn.close();
                    new sc().start();
                    executor1.shutdownNow();
                }
            } catch (Exception ex) {
                System.out.print(ex);
            }
        }
    };

    class sc extends Thread{
        @Override
        public void run(){
            try {
                Thread.sleep(1000);
                Platform.runLater(new Runnable() {
                                      @Override
                                      public void run() {
                                          Parent root = null;
                                          try {

                                              root = FXMLLoader.load(getClass().getResource("retail/projects.fxml"));
                                          } catch (IOException e) {
                                              e.printStackTrace();
                                          }
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
                                          stage.show();
                                          apane.getScene().getWindow().hide();
                                      }
                                  }
                );
            } catch (InterruptedException ex) {
                Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,ex);
            }
        }
    }
}
