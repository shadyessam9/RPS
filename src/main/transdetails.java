package main;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class transdetails implements Initializable {

    @FXML
    private Button src;
    @FXML
    private Label id;
    @FXML
    private Label un;
    @FXML
    private Label dt;
    @FXML
    private Label pdt;
    @FXML
    private Label an;
    @FXML
    private Label pan;
    @FXML
    private Label cc;
    @FXML
    private Label n;
    @FXML
    private Label num;
    @FXML
    private Label db;
    @FXML
    private Label cr;
    @FXML
    private Button post;
    @FXML
    private Button disc;

    private double xOffset = 0.0D;
    private double yOffset = 0.0D;
    Statement statement1 ;
    Statement statement2;
    Statement statement3;
    Statement statement4;
    String accountname;
    String parentaccountname;
    String costcenter;
    public static Object eid;
    public static Object pid;
    Object key;
    Object key1;
    Object key2;
    String val;
    Connection conn ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        val= String.valueOf(journal.tid);
       if (val.equals("null")){
           key=transview.tid;
        }else {key=journal.tid;}
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                        statement1 = conn.createStatement();
                        statement1.execute("SELECT * FROM journal  WHERE  id ='" + key + "' ");
                        ResultSet results = statement1.getResultSet();
                        while (results.next()) {
                            if(results.getString("post").equals("yes")){
                                post.setVisible(false);
                                disc.setVisible(false);
                            }else if (results.getString("post").equals("no")){
                                post.setVisible(true);
                                disc.setVisible(true);
                            }
                            eid=results.getString("asc_id");
                            pid=results.getString("costcenter_id");
                            id.setText(results.getString("id"));
                            dt.setText(results.getString("dt"));
                            pdt.setText(results.getString("post_dt"));
                            statement2 = conn.createStatement();
                            statement2.execute("SELECT * FROM acctree WHERE id='" + results.getString("acc_id") + "'");
                            ResultSet results1 = statement2.getResultSet();
                            while (results1.next()) {
                                accountname = results1.getString("title");
                            }
                            an.setText(accountname);
                            statement3 = conn.createStatement();
                            statement3.execute("SELECT * FROM acctree WHERE id='" + results.getString("parent_id") + "'");
                            ResultSet results2 = statement3.getResultSet();
                            while (results2.next()) {
                                parentaccountname = results2.getString("title");
                            }
                            pan.setText(parentaccountname);
                            statement4 = conn.createStatement();
                            statement4.execute("select * from projects WHERE id= '" + results.getString("costcenter_id") + "'");
                            ResultSet results3 = statement4.getResultSet();
                            while (results3.next()) {
                                costcenter = String.valueOf(results3.getString("name"));
                            }
                            cc.setText(costcenter);
                            db.setText( results.getString("debit"));
                            cr.setText(results.getString("credit"));
                            num.setText(results.getString("num"));
                            n.setText(results.getString("notes"));
                            key1=results.getString("asc_id");
                            if (results.getString("type").equals("extract")){src.setVisible(true);}
                            else if (results.getString("type").equals("bill")) {src.setVisible(true);}
                            else if (results.getString("type").equals("entry")) {src.setVisible(false);}
                            un.setText(results.getString("user"));
                            key2=results.getString("type");
                        }
                        conn.close();
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                }});

    }

    public void source (){
        System.out.println(key2);
        if (key2.equals("extract")){
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("retail/extractviewe.fxml"));
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
        } else if (key2.equals("bill")) {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("retail/billviewe.fxml"));
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
        }
    }



}
