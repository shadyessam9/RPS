package main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;



public class accounting implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            host.getChildren().clear();
            Pane newLoadedPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("journal.fxml")));
            host.getChildren().add(newLoadedPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double xOffset = 0.0D;
    private double yOffset = 0.0D;

   @FXML
   public AnchorPane acc;
    @FXML
    private Pane host;
    @FXML
    public void customers(ActionEvent event) throws IOException {
        host.getChildren().clear();
        Pane newLoadedPane =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customers.fxml")));
        host.getChildren().add(newLoadedPane);
    }
    @FXML
    public void suppliers(ActionEvent event) throws IOException {
        host.getChildren().clear();
        Pane newLoadedPane =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("suppliers.fxml")));
        host.getChildren().add(newLoadedPane);

    }
    @FXML
    public void journal(ActionEvent event) throws IOException {
        host.getChildren().clear();
        Pane newLoadedPane =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("journal.fxml")));
        host.getChildren().add(newLoadedPane);

    }
    @FXML
    public void trailbalance(ActionEvent event) throws IOException {
        host.getChildren().clear();
        Pane newLoadedPane =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("trailbalance.fxml")));
        host.getChildren().add(newLoadedPane);

    }
    @FXML
    public void generalledger(ActionEvent event) throws IOException {
        host.getChildren().clear();
        Pane newLoadedPane =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("genralledger.fxml")));
        host.getChildren().add(newLoadedPane);

    }
    public void home (ActionEvent event) throws IOException {
        Parent root;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home.fxml")));
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

