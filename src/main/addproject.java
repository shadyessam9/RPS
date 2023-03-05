package main;
import com.itextpdf.text.DocumentException;
import javafx.application.Platform;
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

public class addproject implements Initializable {

    @FXML
    private Label pid;
    @FXML
    private TextField pn;
    @FXML
    private TextField cp;
    @FXML
    private ComboBox cn;
    @FXML
    private ComboBox sn;
    @FXML
    private ComboBox ia;
    @FXML
    private TextField sa;
    @FXML
    private TextField i;
    @FXML
    private AnchorPane apane;

    String dt;
    String id;

    private double xOffset = 0.0D;
    private double yOffset = 0.0D;


    DateTimeFormatter dtr1 = DateTimeFormatter.ofPattern("hh-mm-ss-dd-MM-yyyy");
    LocalDateTime now1 = LocalDateTime.now();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        dt = dtr.format(now);

        id = "1"+dtr1.format(now1).replace("-", "");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pid.setText(id);
            }
        });

        cn.setItems(FXCollections.observableArrayList(getData1()));
        sn.setItems(FXCollections.observableArrayList(getData2()));
        ia.setItems(FXCollections.observableArrayList(getData3()));
    }

    private List<Object> getData1() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM customers");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search.add(results.getString("name")+":"+results.getString("id"));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return search;
    }

    private List<Object> getData2() {
        List<Object> search1 = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM sites");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search1.add(results.getString("name")+":"+results.getString("id"));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return search1;
    }

    private List<Object> getData3() {
        List<Object> search1 = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM acctree");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search1.add(results.getString("title")+":"+results.getString("id"));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return search1;
    }

    public void ap (ActionEvent event) throws IOException, DocumentException {

        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO projects (id, name , site_id, cust_id , insu_acc_id , complete , pay, user  ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, id);
            stmt.setString(2, pn.getText());
            stmt.setString(3, String.valueOf(sn.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(4, String.valueOf(cn.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(5, String.valueOf(ia.getValue()).replaceAll("[^0-9]", ""));
            stmt.setInt(6,0);
            stmt.setInt(7, Integer.parseInt(cp.getText()));
            stmt.setString(8,"a");
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }



        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO acctree (id, title , parent_id, level  ) VALUES (?, ?, ?, ?)");
            stmt.setString(1, id+1);
            stmt.setString(2, sa.getText());
            stmt.setString(3, "9472017022022");
            stmt.setString(4, "2");

            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }



        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO acctree (id, title , parent_id, level  ) VALUES (?, ?, ?, ?)");
            stmt.setString(1, id+2);
            stmt.setString(2, i.getText());
            stmt.setString(3, "400");
            stmt.setString(4, "1");

            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }


    public void send (ActionEvent event) throws IOException, DocumentException {
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO project_sites (project_id,site_id) VALUES (?, ?)");
            stmt.setString(1, id);
            stmt.setString(2, String.valueOf(sn.getValue()).replaceAll("[^0-9]", ""));

            stmt.executeUpdate();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


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
