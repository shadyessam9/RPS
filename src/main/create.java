package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class create implements Initializable {


    private double xOffset = 0.0D;
    private double yOffset = 0.0D;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void extract (ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("addextract.fxml"));
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
            //   stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/assetes/icon.png")));
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            System.out.println(e);
        }


    }

    public void project (ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("addproject.fxml"));
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
            //   stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/assetes/icon.png")));
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            System.out.println(e);
        }


    }

    public void bill (ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("addbill.fxml"));
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
            //   stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/assetes/icon.png")));
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            System.out.println(e);
        }


    }

    public void account (ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("addaccount.fxml"));
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
            //   stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/assetes/icon.png")));
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            System.out.println(e);
        }


    }

    public void entry (ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("entry.fxml"));
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
            //   stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/assetes/icon.png")));
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            System.out.println(e);
        }


    }

    public void entity (ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("addcs.fxml"));
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
            //   stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/assetes/icon.png")));
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            System.out.println(e);
        }


    }

    public void home (ActionEvent event) {
        Parent root;
        try {
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
            //   stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/assetes/icon.png")));
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            System.out.println(e);
        }


    }

}
