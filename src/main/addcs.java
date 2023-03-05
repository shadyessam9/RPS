package main;
import com.itextpdf.text.DocumentException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class addcs implements Initializable {
    @FXML
    private Label kind;
    @FXML
    private Label sid;
    @FXML
    private TextField n;
    @FXML
    private TextField tel;
    @FXML
    private TextField adr;
    @FXML
    private ComboBox gov;
    @FXML
    private ComboBox type;
    @FXML
    private AnchorPane apane;


    String dt;
    String id;
    String pid;
    String table;
    String p_id;
    double level;
    private double xOffset = 0.0D;
    private double yOffset = 0.0D;

    DateTimeFormatter dtr1 = DateTimeFormatter.ofPattern("mm-ss-yyyy");
    LocalDateTime now1 = LocalDateTime.now();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        type.getItems().add("CUSTOMER");
        type.getItems().add("SUPPLIER");
        type.getItems().add("BANK");
        DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        dt = dtr.format(now);


        id = "5"+dtr1.format(now1).replace("-", "");
        sid.setText(String.valueOf(id));
        gov.setItems(FXCollections.observableArrayList(getData1()));

        type.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
           if(newValue=="CUSTOMER"){
               tel.setDisable(false);
               adr.setDisable(false);
               gov.setDisable(false);
               try {
                   Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                   Statement statement = conn.createStatement();
                   statement.execute("SELECT * FROM acctree WHERE title='عملاء'");
                   ResultSet results = statement.getResultSet();
                   while (results.next()) {
                       pid= String.valueOf(results.getString("id"));
                   }
                   conn.close();
               } catch (Exception e) {
                   System.out.println(e);
               }
               table="customers";
               kind.setText("CUSTOMER");
           }else if (newValue=="SUPPLIER"){
               tel.setDisable(false);
               adr.setDisable(false);
               gov.setDisable(false);
               try {
                   Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                   Statement statement = conn.createStatement();
                   statement.execute("SELECT * FROM acctree WHERE title='موردين'");
                   ResultSet results = statement.getResultSet();
                   while (results.next()) {
                       pid= String.valueOf(results.getString("id"));
                   }
                   conn.close();
               } catch (Exception e) {
                   System.out.println(e);
               }
               table="suppliers";
               kind.setText("SUPPLIER");
           }else if (newValue=="BANK"){
               try {
                   kind.setText("BANK");
                   tel.setDisable(true);
                   adr.setDisable(true);
                   gov.setDisable(true);
                   Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                   Statement statement = conn.createStatement();
                   statement.execute("SELECT * FROM acctree WHERE title='بنوك'");
                   ResultSet results = statement.getResultSet();
                   while (results.next()) {
                       pid= String.valueOf(results.getString("id"));
                   }
                   conn.close();
               } catch (Exception e) {
                   System.out.println(e);
               }
               table="banks";
           }
        });
    }

    private List<Object> getData1() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM governates");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search.add(results.getString("city"));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return search;
    }

    public void add (ActionEvent event) throws IOException, DocumentException {
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO "+table+" (id, parent_id , name, adress , governate , telephone ) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setString(1, id);
            stmt.setString(2, pid);
            stmt.setString(3, n.getText());
            stmt.setString(4, adr.getText());
            stmt.setString(5, (String) gov.getValue());
            stmt.setString(6, tel.getText());
            stmt.executeUpdate();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        if(type.getValue()=="CUSTOMER"){
            try
            {
                Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO acctree (id, title , parent_id, level) VALUES (?, ?, ?, ?)");
                stmt.setString(1, id);
                try {
                    Statement statement = conn.createStatement();
                    statement.execute("SELECT * FROM acctree WHERE title='عملاء'");
                    ResultSet results = statement.getResultSet();
                    while (results.next()){
                        p_id= String.valueOf(results.getString("id"));
                        level=results.getInt("level");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                stmt.setString(2, n.getText());
                stmt.setString(3, p_id);
                stmt.setInt(4, (int) (level+1));
                stmt.executeUpdate();
                conn.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }else if (type.getValue()=="SUPPLIER"){
            try
            {
                Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO acctree (id, title , parent_id, level) VALUES (?, ?, ?, ?)");
                stmt.setInt(1, Integer.valueOf(id));
                try {
                    Statement statement = conn.createStatement();
                    statement.execute("SELECT * FROM acctree WHERE title='موردين'");
                    ResultSet results = statement.getResultSet();
                    while (results.next()){
                        p_id= String.valueOf(results.getString("id"));
                        level=results.getInt("level");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                stmt.setString(2, n.getText());
                stmt.setString(3, p_id);
                stmt.setInt(4, (int) (level+1));
                stmt.executeUpdate();
                conn.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }else if (type.getValue()=="BANK"){
            try
            {
                Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO acctree (id, title , parent_id, level) VALUES (?, ?, ?, ?)");
                stmt.setString(1, id);
                try {
                    Statement statement = conn.createStatement();
                    statement.execute("SELECT * FROM acctree WHERE title='بنوك'");
                    ResultSet results = statement.getResultSet();
                    while (results.next()){
                        p_id= String.valueOf(results.getString("id"));
                        level=results.getInt("level");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                stmt.setString(2, n.getText());
                stmt.setString(3, p_id);
                stmt.setInt(4, (int) (level+1));
                stmt.executeUpdate();
                conn.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        Parent root = null;
        try {

            root = FXMLLoader.load(getClass().getResource("retail/addcs.fxml"));
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
