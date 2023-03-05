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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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

public class entry implements Initializable {
    @FXML
    private Label eid;
    @FXML
    private DatePicker dt;
    @FXML
    private ComboBox level;
    @FXML
    private ComboBox an;
    @FXML
    private ComboBox cc;
    @FXML
    private TextField notes;
    @FXML
    private TextField db;
    @FXML
    private TextField cr;

    Object link;
    Object dat;
    Object id;
    Object pid0;
    Object pid1;
    Object pid2;
    Object pid3;
    Object pid4;
    Double deb;
    Double cre;

    Statement statement1;


    private double xOffset = 0.0D;
    private double yOffset = 0.0D;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DateTimeFormatter dtr = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        dat = dtr.format(now);
        DateTimeFormatter dtr1 = DateTimeFormatter.ofPattern("hh-mm-ss-dd-MM-yyyy");
        LocalDateTime now1 = LocalDateTime.now();
        id = "9"+dtr1.format(now1).replace("-", "");
        eid.setText((String) id);

        level.getItems().add("1");
        level.getItems().add("2");
        level.getItems().add("3");
        level.getItems().add("4");
        level.getItems().add("5");

        cc.setItems(FXCollections.observableArrayList(getData2()));

        level.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            link=newValue;
            an.setItems(FXCollections.observableArrayList(getData1()));
        });
    }

    private List<Object> getData1() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM acctree WHERE level='" +link+"'");
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


    private List<Object> getData2() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM projects");
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



    public void post (ActionEvent event) throws IOException, DocumentException {

        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO tempentry (id, acc_id , asc_id, pid0, pid1, pid2, pid3, pid4, costcenter_id, debit, credit, notes, post_dt, dt, user, post, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, (String) id);
            stmt.setString(2, String.valueOf(an.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(3,  "NULL");
            try {
                Statement statement = conn.createStatement();
                statement.execute("SELECT * FROM acctree WHERE id='" +String.valueOf(an.getValue()).replaceAll("[^0-9]", "")+"'");
                ResultSet results = statement.getResultSet();
                while (results.next()) {
                    pid0=results.getString("pid0");
                    pid1=results.getString("pid1");
                    pid2=results.getString("pid2");
                    pid3=results.getString("pid3");
                    pid4=results.getString("pid4");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            stmt.setString(4, (String) pid0);
            stmt.setString(5, (String) pid1);
            stmt.setString(6, (String) pid2);
            stmt.setString(7, (String) pid3);
            stmt.setString(8, (String) pid4);
            stmt.setString(9, String.valueOf(cc.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(10, String.valueOf(db.getText()));
            stmt.setString(11, String.valueOf(cr.getText()));
            stmt.setString(12, notes.getText());
            stmt.setString(13, String.valueOf(dat));
            stmt.setString(14, String.valueOf(dt.getValue()));
            stmt.setString(15, "a");
            stmt.setString(16, "yes");
            stmt.setString(17, "entry");
            stmt.executeUpdate();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        level.setValue(" ");
        an.setValue(" ");
        cc.setValue(" ");
        db.setText(" ");
        cr.setText(" ");
        notes.setText(" ");
    }

    public void create (ActionEvent event) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO `journal` (`num`   ,`id`  , `acc_id` ,  `asc_id` , `pid0` , `pid1`, `pid2`, `pid3`, `pid4`,  `costcenter_id`  , `debit`  , `credit`,  `notes`  , `post_dt`  , `dt`, `user`,  `post`,  `type` ) SELECT `num`  , `id` , `acc_id` , `asc_id` , `pid0` , `pid1`, `pid2`, `pid3`, `pid4`, `costcenter_id`  , `debit`  , `credit` , `notes`  , `post_dt`  , `dt`  , `user` , `post`  , `type` FROM `tempentry` ON DUPLICATE KEY UPDATE   `id` = VALUES(`id`)");
        PreparedStatement stmt1 = conn.prepareStatement("TRUNCATE TABLE tempentry");
        try {
            statement1 = conn.createStatement();
            statement1.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM tempentry ");
            ResultSet results1 = statement1.getResultSet();
            while (results1.next()) {
             if(results1.getInt("sdb")==results1.getInt("scr")){
                 stmt.executeUpdate();
                 stmt1.executeUpdate();
             }else {
                 stmt1.executeUpdate();
             }
            }
            } catch (Exception e) {
            System.out.print(e);
        }

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
