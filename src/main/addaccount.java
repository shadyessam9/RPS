package main;
import com.itextpdf.text.DocumentException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class addaccount implements Initializable {
    @FXML
    private ComboBox ma;
    @FXML
    private ComboBox al1;
    @FXML
    private ComboBox al2;
    @FXML
    private ComboBox al3;
    @FXML
    private ComboBox al4;
    @FXML
    private CheckBox c1;
    @FXML
    private CheckBox c2;
    @FXML
    private CheckBox c3;
    @FXML
    private CheckBox c4;
    @FXML
    private CheckBox c5;
    @FXML
    private ComboBox pa;
    @FXML
    private TextField an;
    @FXML
    private Label aid;
    @FXML
    private AnchorPane apane;

    Object level;
    Object pid0;
    Object pid1;
    Object pid2;
    Object pid3;
    Object pid4;
    String id;
    int id1;
    Object pid;

    private double xOffset = 0.0D;
    private double yOffset = 0.0D;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DateTimeFormatter dtr1 = DateTimeFormatter.ofPattern("mm-ss-dd-MM-yyyy");
        LocalDateTime now1 = LocalDateTime.now();
        id = "9"+dtr1.format(now1).replace("-", "");
        aid.setText(id);


        ma.setItems(FXCollections.observableArrayList(getData()));

        c1.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
               if(newValue.equals(true)){
                   c2.setSelected(false);
                   c3.setSelected(false);
                   c4.setSelected(false);
                   c5.setSelected(false);
                   al1.setDisable(true);
                   al2.setDisable(true);
                   al3.setDisable(true);
                   al4.setDisable(true);
                   al1.setValue(" ");
                   al2.setValue(" ");
                   al3.setValue(" ");
                   al4.setValue(" ");
                   level="0";
               }
            }
        });


        c2.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(ma.getSelectionModel().isEmpty()){
                    al1.setDisable(true);
                }else{
                    if(newValue.equals(true)){
                        al1.setDisable(false);
                        al2.setDisable(true);
                        al3.setDisable(true);
                        al4.setDisable(true);
                        al2.setValue(" ");
                        al3.setValue(" ");
                        al4.setValue(" ");
                        level="1";
                        al1.setItems(FXCollections.observableArrayList(getData1()));
                    }else{
                        al1.setValue(" ");
                        level="0";
                        al1.setDisable(true);
                    }
                }

            }
        });

        c3.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(ma.getSelectionModel().isEmpty()&&al1.getSelectionModel().isEmpty()){
                    al1.setDisable(true);
                    al2.setDisable(true);
                }else{
                    if(newValue.equals(true)){
                        al1.setDisable(false);
                        al2.setDisable(false);
                        al3.setDisable(true);
                        al4.setDisable(true);
                        al3.setValue(" ");
                        al4.setValue(" ");
                        level="2";
                        al2.setItems(FXCollections.observableArrayList(getData1()));
                    }else{
                        al2.setValue(" ");
                        level="1";
                        al2.setDisable(true);
                    }
                }
            }
        });

        c4.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(ma.getSelectionModel().isEmpty()&&al1.getSelectionModel().isEmpty()&&al2.getSelectionModel().isEmpty()){
                    al1.setDisable(true);
                    al2.setDisable(true);
                    al3.setDisable(true);
                }else{
                    if(newValue.equals(true)){
                        al1.setDisable(false);
                        al2.setDisable(false);
                        al3.setDisable(false);
                        al4.setDisable(true);
                        al4.setValue(" ");
                        level="3";
                        al3.setItems(FXCollections.observableArrayList(getData1()));
                    }else{
                        al3.setValue(" ");
                        level="2";
                        al3.setDisable(true);
                    }
                }
            }
        });

        c5.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(ma.getSelectionModel().isEmpty()&&al1.getSelectionModel().isEmpty()&&al2.getSelectionModel().isEmpty()&&al3.getSelectionModel().isEmpty()){
                    al1.setDisable(true);
                    al2.setDisable(true);
                    al3.setDisable(true);
                    al4.setDisable(true);
                }else{
                    if(newValue.equals(true)){
                        al1.setDisable(false);
                        al2.setDisable(false);
                        al3.setDisable(false);
                        al4.setDisable(false);
                        level="4";
                        al4.setItems(FXCollections.observableArrayList(getData1()));
                    }else{
                    al4.setValue(" ");
                    level="3";
                    al4.setDisable(true);
                    }
                }

            }
        });



        ma.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
           pid0= String.valueOf(newValue).replaceAll("[^0-9]", "");
        });
        al1.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            pid1= String.valueOf(newValue).replaceAll("[^0-9]", "");
        });
        al2.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            pid2= String.valueOf(newValue).replaceAll("[^0-9]", "");
        });
        al3.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            pid3= String.valueOf(newValue).replaceAll("[^0-9]", "");
        });
        al4.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            pid4= String.valueOf(newValue).replaceAll("[^0-9]", "");
        });

    }



    private List<Object> getData() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM acctree WHERE level = '0'");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search.add(results.getString("title")+":"+results.getString("id"));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return search;
    }


    private List<Object> getData1() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM acctree WHERE level = '"+level+"'");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search.add(results.getString("title")+":"+results.getString("id"));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return search;

    }


    public void post (ActionEvent event) throws IOException, DocumentException {
        try
        {

            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO acctree (id, title , pid0 , pid1 , pid2 , pid3 , pid4 , level) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1,id);
            stmt.setString(2, an.getText());
            stmt.setString(3, String.valueOf(pid0));
            stmt.setString(4, String.valueOf(pid1));
            stmt.setString(5, String.valueOf(pid2));
            stmt.setString(6, String.valueOf(pid3));
            stmt.setString(7, String.valueOf(pid4));
            stmt.setInt(8,   Integer.parseInt(String.valueOf(level))+1);
            stmt.executeUpdate();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        Parent root = null;
        try {

            root = FXMLLoader.load(getClass().getResource("retail/addaccount.fxml"));
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

    public void create (ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("create.fxml"));
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
