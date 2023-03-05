package main;
import com.mysql.fabric.xmlrpc.base.Data;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;



public class genralledger implements Initializable {
    @FXML
    private TableColumn an;
    @FXML
    private TableColumn db;
    @FXML
    private TableColumn cr;
    @FXML
    private TableColumn bal;
    @FXML
    private TableColumn vt;
    @FXML
    private TableColumn odb;
    @FXML
    private TableColumn ocr;
    @FXML
    private ComboBox level;
    @FXML
    private ComboBox date;
    @FXML
    private DatePicker dt1;
    @FXML
    private DatePicker dt2;
    @FXML
    private Button conf;
    @FXML
    private TableView dashboard;


    private double xOffset = 0.0D;
    private double yOffset = 0.0D;
    Statement statement1 ;
    Statement statement2;
    Statement statement3;
    String accountname;
    public static Object aid;
    public static Object search;
    public static Object d1;
    public static Object d2;
    double  opdb;
    double  opcr;
    double  adb;
    double  acr;
    double  balance = 0;
    double  tdb1 = 0;
    double  tcr1 = 0;
    double  tdb2 = 0;
    double  tcr2 = 0;
    double tb = 0;

    private ObservableList<genralledger.ShowData> data;
    Connection conn ;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DateTimeFormatter dtr1 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter m = DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter y = DateTimeFormatter.ofPattern("yyyy");
        LocalDateTime now1 = LocalDateTime.now();


        dt1.setDisable(true);
        dt2.setDisable(true);
        conf.setDisable(true);

        level.getItems().add("1");
        level.getItems().add("2");
        level.getItems().add("3");
        level.getItems().add("4");
        level.getItems().add("5");
        date.getItems().add("CURRENT MONTH");
        date.getItems().add("CURRENT QUARTER");
        date.getItems().add("CURRENT YEAR");
        date.getItems().add("LAST MONTH");
        date.getItems().add("LAST QUARTER");
        date.getItems().add("LAST YEAR");
        date.getItems().add("SPECIFIC");

        dashboard.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList<TablePosition> selectedCells = dashboard.getSelectionModel().getSelectedCells() ;
        selectedCells.addListener((ListChangeListener.Change<? extends TablePosition> change) -> {
            if (selectedCells.size() > 0) {
                TablePosition selectedCell = selectedCells.get(0);
                TableColumn column = selectedCell.getTableColumn();
                if(column == vt){
                    int rowIndex = selectedCell.getRow();
                    aid = column.getCellObservableValue(rowIndex).getValue();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Parent root = null;
                            try {
                                root = FXMLLoader.load(getClass().getResource("transview.fxml"));
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
                            dashboard.getSelectionModel().clearSelection();
                        }});

                }
            }
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                viewtransaction();
            }});

        level.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            search="CURRENT MONTH";
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        tdb1=0;
                        tcr1=0;
                        tdb2=0;
                        tcr2=0;
                        tb=0;
                        conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                        statement1 = conn.createStatement();
                        statement1.execute("SELECT * FROM acctree WHERE level = '"+newValue+"'  ");
                        ResultSet results = statement1.getResultSet();
                        data = FXCollections.observableArrayList();
                        while (results.next()) {
                            accountname= results.getString("title");
                            statement2 = conn.createStatement();
                            statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "' AND YEAR(dt) = YEAR(CURRENT_DATE())");
                            ResultSet results1 = statement2.getResultSet();
                            while (results1.next()) {
                                opdb= results1.getInt("sdb");
                                tdb1=tdb1+opdb;
                                opcr= results1.getInt("scr");
                                tcr1=tcr1+opcr;
                            }
                            statement3 = conn.createStatement();
                            statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND MONTH(dt) = MONTH(CURRENT_DATE())");
                            ResultSet results2 = statement3.getResultSet();
                            while (results2.next()) {
                                adb= results2.getInt("sdb");
                                tdb2=tdb2+adb;
                                acr= results2.getInt("scr");
                                tcr2=tcr2+acr;
                            }
                            balance=(opdb+adb)-(opcr+acr);
                            tb=tb+balance;
                            data.add(new genralledger.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(adb), String.valueOf(acr),String.valueOf(balance),results.getString("id")));
                        }
                        an.setCellValueFactory(new PropertyValueFactory("an"));
                        odb.setCellValueFactory(new PropertyValueFactory("odb"));
                        Label label1 = new Label("DEBIT");
                        Label label2 = new Label(String.valueOf(tdb1));
                        VBox headerGraphic1 = new VBox();
                        headerGraphic1.setAlignment(Pos.CENTER);
                        headerGraphic1.getChildren().addAll(label1,label2);
                        odb.setGraphic(headerGraphic1);
                        ocr.setCellValueFactory(new PropertyValueFactory("ocr"));
                        Label label3 = new Label("CREDIT");
                        Label label4 = new Label(String.valueOf(tcr1));
                        VBox headerGraphic2 = new VBox();
                        headerGraphic2.setAlignment(Pos.CENTER);
                        headerGraphic2.getChildren().addAll(label3,label4);
                        ocr.setGraphic(headerGraphic2);
                        db.setCellValueFactory(new PropertyValueFactory("db"));
                        Label label5 = new Label("CREDIT");
                        Label label6 = new Label(String.valueOf(tdb2));
                        VBox headerGraphic3 = new VBox();
                        headerGraphic3.setAlignment(Pos.CENTER);
                        headerGraphic3.getChildren().addAll(label5,label6);
                        db.setGraphic(headerGraphic3);
                        cr.setCellValueFactory(new PropertyValueFactory("cr"));
                        Label label7 = new Label("CREDIT");
                        Label label8 = new Label(String.valueOf(tcr2));
                        VBox headerGraphic4 = new VBox();
                        headerGraphic4.setAlignment(Pos.CENTER);
                        headerGraphic4.getChildren().addAll(label7,label8);
                        cr.setGraphic(headerGraphic4);
                        bal.setCellValueFactory(new PropertyValueFactory("bal"));
                        Label label9 = new Label("BALANCE");
                        Label label10 = new Label(String.valueOf(tb));
                        VBox headerGraphic5 = new VBox();
                        headerGraphic5.setAlignment(Pos.CENTER);
                        headerGraphic5.getChildren().addAll(label9,label10);
                        bal.setGraphic(headerGraphic5);
                        vt.setCellValueFactory(new PropertyValueFactory("id"));
                        dashboard.setItems(data);
                        dashboard.getSelectionModel().clearSelection();
                        conn.close();
                    } catch (Exception e) {
                        dashboard.setItems(null);
                        System.out.print(e);
                    }

                }});
        });


        date.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(newValue.equals("CURRENT MONTH")){
                search="CURRENT MONTH";
                dt1.setDisable(true);
                dt2.setDisable(true);
                conf.setDisable(true);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tdb1=0;
                            tcr1=0;
                            tdb2=0;
                            tcr2=0;
                            tb=0;
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            statement1.execute("SELECT * FROM acctree WHERE level = '"+level.getValue()+"'  ");
                            ResultSet results = statement1.getResultSet();
                            data = FXCollections.observableArrayList();
                            while (results.next()) {
                                accountname= results.getString("title");
                                statement2 = conn.createStatement();
                                statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "' AND YEAR(dt) = YEAR(CURRENT_DATE())");
                                ResultSet results1 = statement2.getResultSet();
                                while (results1.next()) {
                                    opdb= results1.getInt("sdb");
                                    tdb1=tdb1+opdb;
                                    opcr= results1.getInt("scr");
                                    tcr1=tcr1+opcr;
                                }
                                statement3 = conn.createStatement();
                                statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND MONTH(dt) = MONTH(CURRENT_DATE())");
                                ResultSet results2 = statement3.getResultSet();
                                while (results2.next()) {
                                    adb= results2.getInt("sdb");
                                    tdb2=tdb2+adb;
                                    acr= results2.getInt("scr");
                                    tcr2=tcr2+acr;
                                }
                                balance=(opdb+adb)-(opcr+acr);
                                tb=tb+balance;
                                data.add(new genralledger.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(adb), String.valueOf(acr),String.valueOf(balance),results.getString("id")));
                            }
                            an.setCellValueFactory(new PropertyValueFactory("an"));
                            odb.setCellValueFactory(new PropertyValueFactory("odb"));
                            Label label1 = new Label("DEBIT");
                            Label label2 = new Label(String.valueOf(tdb1));
                            VBox headerGraphic1 = new VBox();
                            headerGraphic1.setAlignment(Pos.CENTER);
                            headerGraphic1.getChildren().addAll(label1,label2);
                            odb.setGraphic(headerGraphic1);
                            ocr.setCellValueFactory(new PropertyValueFactory("ocr"));
                            Label label3 = new Label("CREDIT");
                            Label label4 = new Label(String.valueOf(tcr1));
                            VBox headerGraphic2 = new VBox();
                            headerGraphic2.setAlignment(Pos.CENTER);
                            headerGraphic2.getChildren().addAll(label3,label4);
                            ocr.setGraphic(headerGraphic2);
                            db.setCellValueFactory(new PropertyValueFactory("db"));
                            Label label5 = new Label("CREDIT");
                            Label label6 = new Label(String.valueOf(tdb2));
                            VBox headerGraphic3 = new VBox();
                            headerGraphic3.setAlignment(Pos.CENTER);
                            headerGraphic3.getChildren().addAll(label5,label6);
                            db.setGraphic(headerGraphic3);
                            cr.setCellValueFactory(new PropertyValueFactory("cr"));
                            Label label7 = new Label("CREDIT");
                            Label label8 = new Label(String.valueOf(tcr2));
                            VBox headerGraphic4 = new VBox();
                            headerGraphic4.setAlignment(Pos.CENTER);
                            headerGraphic4.getChildren().addAll(label7,label8);
                            cr.setGraphic(headerGraphic4);
                            bal.setCellValueFactory(new PropertyValueFactory("bal"));
                            Label label9 = new Label("BALANCE");
                            Label label10 = new Label(String.valueOf(tb));
                            VBox headerGraphic5 = new VBox();
                            headerGraphic5.setAlignment(Pos.CENTER);
                            headerGraphic5.getChildren().addAll(label9,label10);
                            bal.setGraphic(headerGraphic5);
                            vt.setCellValueFactory(new PropertyValueFactory("id"));
                            dashboard.setItems(data);
                            dashboard.getSelectionModel().clearSelection();
                            conn.close();
                        } catch (Exception e) {
                            dashboard.setItems(null);
                            System.out.print(e);
                        }

                    }});
            }else if(newValue.equals("CURRENT QUARTER")){
                search="CURRENT QUARTER";
                dt1.setDisable(true);
                dt2.setDisable(true);
                conf.setDisable(true);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tdb1=0;
                            tcr1=0;
                            tdb2=0;
                            tcr2=0;
                            tb=0;
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            statement1.execute("SELECT * FROM acctree WHERE level = '"+level.getValue()+"'  ");
                            ResultSet results = statement1.getResultSet();
                            data = FXCollections.observableArrayList();
                            while (results.next()) {
                                accountname= results.getString("title");
                                statement2 = conn.createStatement();
                                statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "' AND  YEAR(dt) = YEAR(CURDATE())");
                                ResultSet results1 = statement2.getResultSet();
                                while (results1.next()) {
                                    opdb= results1.getInt("sdb");
                                    tdb1=tdb1+opdb;
                                    opcr= results1.getInt("scr");
                                    tcr1=tcr1+opcr;
                                }
                                statement3 = conn.createStatement();
                                statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND QUARTER(dt) = QUARTER(curdate())");
                                ResultSet results2 = statement3.getResultSet();
                                while (results2.next()) {
                                    adb= results2.getInt("sdb");
                                    tdb2=tdb2+adb;
                                    acr= results2.getInt("scr");
                                    tcr2=tcr2+acr;
                                }
                                balance=(opdb+adb)-(opcr+acr);
                                tb=tb+balance;
                                data.add(new genralledger.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(adb), String.valueOf(acr),String.valueOf(balance),results.getString("id")));
                            }
                            an.setCellValueFactory(new PropertyValueFactory("an"));
                            odb.setCellValueFactory(new PropertyValueFactory("odb"));
                            Label label1 = new Label("DEBIT");
                            Label label2 = new Label(String.valueOf(tdb1));
                            VBox headerGraphic1 = new VBox();
                            headerGraphic1.setAlignment(Pos.CENTER);
                            headerGraphic1.getChildren().addAll(label1,label2);
                            odb.setGraphic(headerGraphic1);
                            ocr.setCellValueFactory(new PropertyValueFactory("ocr"));
                            Label label3 = new Label("CREDIT");
                            Label label4 = new Label(String.valueOf(tcr1));
                            VBox headerGraphic2 = new VBox();
                            headerGraphic2.setAlignment(Pos.CENTER);
                            headerGraphic2.getChildren().addAll(label3,label4);
                            ocr.setGraphic(headerGraphic2);
                            db.setCellValueFactory(new PropertyValueFactory("db"));
                            Label label5 = new Label("CREDIT");
                            Label label6 = new Label(String.valueOf(tdb2));
                            VBox headerGraphic3 = new VBox();
                            headerGraphic3.setAlignment(Pos.CENTER);
                            headerGraphic3.getChildren().addAll(label5,label6);
                            db.setGraphic(headerGraphic3);
                            cr.setCellValueFactory(new PropertyValueFactory("cr"));
                            Label label7 = new Label("CREDIT");
                            Label label8 = new Label(String.valueOf(tcr2));
                            VBox headerGraphic4 = new VBox();
                            headerGraphic4.setAlignment(Pos.CENTER);
                            headerGraphic4.getChildren().addAll(label7,label8);
                            cr.setGraphic(headerGraphic4);
                            bal.setCellValueFactory(new PropertyValueFactory("bal"));
                            Label label9 = new Label("BALANCE");
                            Label label10 = new Label(String.valueOf(tb));
                            VBox headerGraphic5 = new VBox();
                            headerGraphic5.setAlignment(Pos.CENTER);
                            headerGraphic5.getChildren().addAll(label9,label10);
                            bal.setGraphic(headerGraphic5);
                            vt.setCellValueFactory(new PropertyValueFactory("id"));
                            dashboard.setItems(data);
                            dashboard.getSelectionModel().clearSelection();
                            conn.close();
                        } catch (Exception e) {
                            dashboard.setItems(null);
                            System.out.print(e);
                        }

                    }});
            }else if(newValue.equals("CURRENT YEAR")){
                search="CURRENT YEAR";
                dt1.setDisable(true);
                dt2.setDisable(true);
                conf.setDisable(true);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tdb1=0;
                            tcr1=0;
                            tdb2=0;
                            tcr2=0;
                            tb=0;
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            statement1.execute("SELECT * FROM acctree WHERE level = '"+level.getValue()+"'  ");
                            ResultSet results = statement1.getResultSet();
                            data = FXCollections.observableArrayList();
                            while (results.next()) {
                                accountname= results.getString("title");
                                statement2 = conn.createStatement();
                                statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "' AND YEAR(dt) = YEAR(CURRENT_DATE())");
                                ResultSet results1 = statement2.getResultSet();
                                while (results1.next()) {
                                    opdb= results1.getInt("sdb");
                                    tdb1=tdb1+opdb;
                                    opcr= results1.getInt("scr");
                                    tcr1=tcr1+opcr;
                                }
                                statement3 = conn.createStatement();
                                statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND  YEAR(dt) = YEAR(CURDATE())");
                                ResultSet results2 = statement3.getResultSet();
                                while (results2.next()) {
                                    adb= results2.getInt("sdb");
                                    tdb2=tdb2+adb;
                                    acr= results2.getInt("scr");
                                    tcr2=tcr2+acr;
                                }
                                balance=(opdb+adb)-(opcr+acr);
                                tb=tb+balance;
                                data.add(new genralledger.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(adb), String.valueOf(acr),String.valueOf(balance),results.getString("id")));
                            }
                            an.setCellValueFactory(new PropertyValueFactory("an"));
                            odb.setCellValueFactory(new PropertyValueFactory("odb"));
                            Label label1 = new Label("DEBIT");
                            Label label2 = new Label(String.valueOf(tdb1));
                            VBox headerGraphic1 = new VBox();
                            headerGraphic1.setAlignment(Pos.CENTER);
                            headerGraphic1.getChildren().addAll(label1,label2);
                            odb.setGraphic(headerGraphic1);
                            ocr.setCellValueFactory(new PropertyValueFactory("ocr"));
                            Label label3 = new Label("CREDIT");
                            Label label4 = new Label(String.valueOf(tcr1));
                            VBox headerGraphic2 = new VBox();
                            headerGraphic2.setAlignment(Pos.CENTER);
                            headerGraphic2.getChildren().addAll(label3,label4);
                            ocr.setGraphic(headerGraphic2);
                            db.setCellValueFactory(new PropertyValueFactory("db"));
                            Label label5 = new Label("CREDIT");
                            Label label6 = new Label(String.valueOf(tdb2));
                            VBox headerGraphic3 = new VBox();
                            headerGraphic3.setAlignment(Pos.CENTER);
                            headerGraphic3.getChildren().addAll(label5,label6);
                            db.setGraphic(headerGraphic3);
                            cr.setCellValueFactory(new PropertyValueFactory("cr"));
                            Label label7 = new Label("CREDIT");
                            Label label8 = new Label(String.valueOf(tcr2));
                            VBox headerGraphic4 = new VBox();
                            headerGraphic4.setAlignment(Pos.CENTER);
                            headerGraphic4.getChildren().addAll(label7,label8);
                            cr.setGraphic(headerGraphic4);
                            bal.setCellValueFactory(new PropertyValueFactory("bal"));
                            Label label9 = new Label("BALANCE");
                            Label label10 = new Label(String.valueOf(tb));
                            VBox headerGraphic5 = new VBox();
                            headerGraphic5.setAlignment(Pos.CENTER);
                            headerGraphic5.getChildren().addAll(label9,label10);
                            bal.setGraphic(headerGraphic5);
                            vt.setCellValueFactory(new PropertyValueFactory("id"));
                            dashboard.setItems(data);
                            dashboard.getSelectionModel().clearSelection();
                            conn.close();
                        } catch (Exception e) {
                            dashboard.setItems(null);
                            System.out.print(e);
                        }

                    }});
            }else if(newValue.equals("LAST MONTH")){
                search="LAST MONTH";
                dt1.setDisable(true);
                dt2.setDisable(true);
                conf.setDisable(true);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tdb1=0;
                            tcr1=0;
                            tdb2=0;
                            tcr2=0;
                            tb=0;
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            statement1.execute("SELECT * FROM acctree WHERE level = '"+level.getValue()+"'  ");
                            ResultSet results = statement1.getResultSet();
                            data = FXCollections.observableArrayList();
                            while (results.next()) {
                                accountname= results.getString("title");
                                statement2 = conn.createStatement();
                                statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "'  AND  YEAR(dt) = YEAR(CURDATE())");
                                ResultSet results1 = statement2.getResultSet();
                                while (results1.next()) {
                                    opdb= results1.getInt("sdb");
                                    tdb1=tdb1+opdb;
                                    opcr= results1.getInt("scr");
                                    tcr1=tcr1+opcr;
                                }
                                statement3 = conn.createStatement();
                                statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND MONTH(dt) = MONTH(CURRENT_DATE - INTERVAL 1 MONTH)");
                                ResultSet results2 = statement3.getResultSet();
                                while (results2.next()) {
                                    adb= results2.getInt("sdb");
                                    tdb2=tdb2+adb;
                                    acr= results2.getInt("scr");
                                    tcr2=tcr2+acr;
                                }
                                balance=(opdb+adb)-(opcr+acr);
                                tb=tb+balance;
                                data.add(new genralledger.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(adb), String.valueOf(acr),String.valueOf(balance),results.getString("id")));
                            }
                            an.setCellValueFactory(new PropertyValueFactory("an"));
                            odb.setCellValueFactory(new PropertyValueFactory("odb"));
                            Label label1 = new Label("DEBIT");
                            Label label2 = new Label(String.valueOf(tdb1));
                            VBox headerGraphic1 = new VBox();
                            headerGraphic1.setAlignment(Pos.CENTER);
                            headerGraphic1.getChildren().addAll(label1,label2);
                            odb.setGraphic(headerGraphic1);
                            ocr.setCellValueFactory(new PropertyValueFactory("ocr"));
                            Label label3 = new Label("CREDIT");
                            Label label4 = new Label(String.valueOf(tcr1));
                            VBox headerGraphic2 = new VBox();
                            headerGraphic2.setAlignment(Pos.CENTER);
                            headerGraphic2.getChildren().addAll(label3,label4);
                            ocr.setGraphic(headerGraphic2);
                            db.setCellValueFactory(new PropertyValueFactory("db"));
                            Label label5 = new Label("CREDIT");
                            Label label6 = new Label(String.valueOf(tdb2));
                            VBox headerGraphic3 = new VBox();
                            headerGraphic3.setAlignment(Pos.CENTER);
                            headerGraphic3.getChildren().addAll(label5,label6);
                            db.setGraphic(headerGraphic3);
                            cr.setCellValueFactory(new PropertyValueFactory("cr"));
                            Label label7 = new Label("CREDIT");
                            Label label8 = new Label(String.valueOf(tcr2));
                            VBox headerGraphic4 = new VBox();
                            headerGraphic4.setAlignment(Pos.CENTER);
                            headerGraphic4.getChildren().addAll(label7,label8);
                            cr.setGraphic(headerGraphic4);
                            bal.setCellValueFactory(new PropertyValueFactory("bal"));
                            Label label9 = new Label("BALANCE");
                            Label label10 = new Label(String.valueOf(tb));
                            VBox headerGraphic5 = new VBox();
                            headerGraphic5.setAlignment(Pos.CENTER);
                            headerGraphic5.getChildren().addAll(label9,label10);
                            bal.setGraphic(headerGraphic5);
                            vt.setCellValueFactory(new PropertyValueFactory("id"));
                            dashboard.setItems(data);
                            dashboard.getSelectionModel().clearSelection();
                            conn.close();
                        } catch (Exception e) {
                            dashboard.setItems(null);
                            System.out.print(e);
                        }

                    }});
            }else if(newValue.equals("LAST QUARTER")){
                search="LAST QUARTER";
                dt1.setDisable(true);
                dt2.setDisable(true);
                conf.setDisable(true);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tdb1=0;
                            tcr1=0;
                            tdb2=0;
                            tcr2=0;
                            tb=0;
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            statement1.execute("SELECT * FROM acctree WHERE level = '"+level.getValue()+"'  ");
                            ResultSet results = statement1.getResultSet();
                            data = FXCollections.observableArrayList();
                            while (results.next()) {
                                accountname= results.getString("title");
                                statement2 = conn.createStatement();
                                statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "'  AND  YEAR(dt) = YEAR(CURDATE())");
                                ResultSet results1 = statement2.getResultSet();
                                while (results1.next()) {
                                    opdb= results1.getInt("sdb");
                                    tdb1=tdb1+opdb;
                                    opcr= results1.getInt("scr");
                                    tcr1=tcr1+opcr;
                                }
                                statement3 = conn.createStatement();
                                statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND QUARTER(dt) = QUARTER(curdate() - INTERVAL 1 quarter)");
                                ResultSet results2 = statement3.getResultSet();
                                while (results2.next()) {
                                    adb= results2.getInt("sdb");
                                    tdb2=tdb2+adb;
                                    acr= results2.getInt("scr");
                                    tcr2=tcr2+acr;
                                }
                                balance=(opdb+adb)-(opcr+acr);
                                tb=tb+balance;
                                data.add(new genralledger.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(adb), String.valueOf(acr),String.valueOf(balance),results.getString("id")));
                            }
                            an.setCellValueFactory(new PropertyValueFactory("an"));
                            odb.setCellValueFactory(new PropertyValueFactory("odb"));
                            Label label1 = new Label("DEBIT");
                            Label label2 = new Label(String.valueOf(tdb1));
                            VBox headerGraphic1 = new VBox();
                            headerGraphic1.setAlignment(Pos.CENTER);
                            headerGraphic1.getChildren().addAll(label1,label2);
                            odb.setGraphic(headerGraphic1);
                            ocr.setCellValueFactory(new PropertyValueFactory("ocr"));
                            Label label3 = new Label("CREDIT");
                            Label label4 = new Label(String.valueOf(tcr1));
                            VBox headerGraphic2 = new VBox();
                            headerGraphic2.setAlignment(Pos.CENTER);
                            headerGraphic2.getChildren().addAll(label3,label4);
                            ocr.setGraphic(headerGraphic2);
                            db.setCellValueFactory(new PropertyValueFactory("db"));
                            Label label5 = new Label("CREDIT");
                            Label label6 = new Label(String.valueOf(tdb2));
                            VBox headerGraphic3 = new VBox();
                            headerGraphic3.setAlignment(Pos.CENTER);
                            headerGraphic3.getChildren().addAll(label5,label6);
                            db.setGraphic(headerGraphic3);
                            cr.setCellValueFactory(new PropertyValueFactory("cr"));
                            Label label7 = new Label("CREDIT");
                            Label label8 = new Label(String.valueOf(tcr2));
                            VBox headerGraphic4 = new VBox();
                            headerGraphic4.setAlignment(Pos.CENTER);
                            headerGraphic4.getChildren().addAll(label7,label8);
                            cr.setGraphic(headerGraphic4);
                            bal.setCellValueFactory(new PropertyValueFactory("bal"));
                            Label label9 = new Label("BALANCE");
                            Label label10 = new Label(String.valueOf(tb));
                            VBox headerGraphic5 = new VBox();
                            headerGraphic5.setAlignment(Pos.CENTER);
                            headerGraphic5.getChildren().addAll(label9,label10);
                            bal.setGraphic(headerGraphic5);
                            vt.setCellValueFactory(new PropertyValueFactory("id"));
                            dashboard.setItems(data);
                            dashboard.getSelectionModel().clearSelection();
                            conn.close();
                        } catch (Exception e) {
                            dashboard.setItems(null);
                            System.out.print(e);
                        }

                    }});
            }else if(newValue.equals("LAST YEAR")){
                search="LAST YEAR";
                dt1.setDisable(true);
                dt2.setDisable(true);
                conf.setDisable(true);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tdb1=0;
                            tcr1=0;
                            tdb2=0;
                            tcr2=0;
                            tb=0;
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            statement1.execute("SELECT * FROM acctree WHERE level = '"+level.getValue()+"'  ");
                            ResultSet results = statement1.getResultSet();
                            data = FXCollections.observableArrayList();
                            while (results.next()) {
                                accountname= results.getString("title");
                                statement2 = conn.createStatement();
                                statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "'  AND  YEAR(dt) = YEAR(CURDATE())");
                                ResultSet results1 = statement2.getResultSet();
                                while (results1.next()) {
                                    opdb= results1.getInt("sdb");
                                    tdb1=tdb1+opdb;
                                    opcr= results1.getInt("scr");
                                    tcr1=tcr1+opcr;
                                }
                                statement3 = conn.createStatement();
                                statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND YEAR(dt) = YEAR(CURRENT_DATE - INTERVAL 1 YEAR)");
                                ResultSet results2 = statement3.getResultSet();
                                while (results2.next()) {
                                    adb= results2.getInt("sdb");
                                    tdb2=tdb2+adb;
                                    acr= results2.getInt("scr");
                                    tcr2=tcr2+acr;
                                }
                                balance=(opdb+adb)-(opcr+acr);
                                tb=tb+balance;
                                data.add(new genralledger.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(adb), String.valueOf(acr),String.valueOf(balance),results.getString("id")));
                            }
                            an.setCellValueFactory(new PropertyValueFactory("an"));
                            odb.setCellValueFactory(new PropertyValueFactory("odb"));
                            Label label1 = new Label("DEBIT");
                            Label label2 = new Label(String.valueOf(tdb1));
                            VBox headerGraphic1 = new VBox();
                            headerGraphic1.setAlignment(Pos.CENTER);
                            headerGraphic1.getChildren().addAll(label1,label2);
                            odb.setGraphic(headerGraphic1);
                            ocr.setCellValueFactory(new PropertyValueFactory("ocr"));
                            Label label3 = new Label("CREDIT");
                            Label label4 = new Label(String.valueOf(tcr1));
                            VBox headerGraphic2 = new VBox();
                            headerGraphic2.setAlignment(Pos.CENTER);
                            headerGraphic2.getChildren().addAll(label3,label4);
                            ocr.setGraphic(headerGraphic2);
                            db.setCellValueFactory(new PropertyValueFactory("db"));
                            Label label5 = new Label("CREDIT");
                            Label label6 = new Label(String.valueOf(tdb2));
                            VBox headerGraphic3 = new VBox();
                            headerGraphic3.setAlignment(Pos.CENTER);
                            headerGraphic3.getChildren().addAll(label5,label6);
                            db.setGraphic(headerGraphic3);
                            cr.setCellValueFactory(new PropertyValueFactory("cr"));
                            Label label7 = new Label("CREDIT");
                            Label label8 = new Label(String.valueOf(tcr2));
                            VBox headerGraphic4 = new VBox();
                            headerGraphic4.setAlignment(Pos.CENTER);
                            headerGraphic4.getChildren().addAll(label7,label8);
                            cr.setGraphic(headerGraphic4);
                            bal.setCellValueFactory(new PropertyValueFactory("bal"));
                            Label label9 = new Label("BALANCE");
                            Label label10 = new Label(String.valueOf(tb));
                            VBox headerGraphic5 = new VBox();
                            headerGraphic5.setAlignment(Pos.CENTER);
                            headerGraphic5.getChildren().addAll(label9,label10);
                            bal.setGraphic(headerGraphic5);
                            vt.setCellValueFactory(new PropertyValueFactory("id"));
                            dashboard.setItems(data);
                            dashboard.getSelectionModel().clearSelection();
                            conn.close();
                        } catch (Exception e) {
                            dashboard.setItems(null);
                            System.out.print(e);
                        }

                    }});
            }else if(newValue.equals("SPECIFIC")){
                search="SPECIFIC";
                dt1.setDisable(false);
                dt2.setDisable(false);
                conf.setDisable(false);
            }
        });


    }

    public void viewtransaction(){
        vt.setCellFactory(col -> {
            TableCell<Data, Object> cell = new TableCell<Data, Object>() {
                public void updateItem(Object item, boolean empty) {
                    final Label view = new Label("VIEW TRANSACTIONS");
                    view.setStyle("-fx-text-fill: #21053D;");
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(view);
                    }
                }
            };
            return cell;
        });
    }


    public void run() {
        try {
            d1=dt1.getValue();
            d2=dt2.getValue();
            tdb1=0;
            tcr1=0;
            tdb2=0;
            tcr2=0;
            tb=0;
            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            statement1 = conn.createStatement();
            statement1.execute("SELECT * FROM acctree WHERE level = '"+level.getValue()+"'  ");
            ResultSet results = statement1.getResultSet();
            data = FXCollections.observableArrayList();
            while (results.next()) {
                accountname= results.getString("title");
                statement2 = conn.createStatement();
                statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "' AND YEAR(dt) = YEAR(CURRENT_DATE())");
                ResultSet results1 = statement2.getResultSet();
                while (results1.next()) {
                    opdb= results1.getInt("sdb");
                    tdb1=tdb1+opdb;
                    opcr= results1.getInt("scr");
                    tcr1=tcr1+opcr;
                }
                statement3 = conn.createStatement();
                statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND dt between '"+dt1.getValue()+"' AND '"+dt2.getValue()+"' ");
                ResultSet results2 = statement3.getResultSet();
                while (results2.next()) {
                    adb= results2.getInt("sdb");
                    tdb2=tdb2+adb;
                    acr= results2.getInt("scr");
                    tcr2=tcr2+acr;
                }
                balance=(opdb+adb)-(opcr+acr);
                tb=tb+balance;
                data.add(new genralledger.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(adb), String.valueOf(acr),String.valueOf(balance),results.getString("id")));
            }
            an.setCellValueFactory(new PropertyValueFactory("an"));
            odb.setCellValueFactory(new PropertyValueFactory("odb"));
            Label label1 = new Label("DEBIT");
            Label label2 = new Label(String.valueOf(tdb1));
            VBox headerGraphic1 = new VBox();
            headerGraphic1.setAlignment(Pos.CENTER);
            headerGraphic1.getChildren().addAll(label1,label2);
            odb.setGraphic(headerGraphic1);
            ocr.setCellValueFactory(new PropertyValueFactory("ocr"));
            Label label3 = new Label("CREDIT");
            Label label4 = new Label(String.valueOf(tcr1));
            VBox headerGraphic2 = new VBox();
            headerGraphic2.setAlignment(Pos.CENTER);
            headerGraphic2.getChildren().addAll(label3,label4);
            ocr.setGraphic(headerGraphic2);
            db.setCellValueFactory(new PropertyValueFactory("db"));
            Label label5 = new Label("CREDIT");
            Label label6 = new Label(String.valueOf(tdb2));
            VBox headerGraphic3 = new VBox();
            headerGraphic3.setAlignment(Pos.CENTER);
            headerGraphic3.getChildren().addAll(label5,label6);
            db.setGraphic(headerGraphic3);
            cr.setCellValueFactory(new PropertyValueFactory("cr"));
            Label label7 = new Label("CREDIT");
            Label label8 = new Label(String.valueOf(tcr2));
            VBox headerGraphic4 = new VBox();
            headerGraphic4.setAlignment(Pos.CENTER);
            headerGraphic4.getChildren().addAll(label7,label8);
            cr.setGraphic(headerGraphic4);
            bal.setCellValueFactory(new PropertyValueFactory("bal"));
            Label label9 = new Label("BALANCE");
            Label label10 = new Label(String.valueOf(tb));
            VBox headerGraphic5 = new VBox();
            headerGraphic5.setAlignment(Pos.CENTER);
            headerGraphic5.getChildren().addAll(label9,label10);
            bal.setGraphic(headerGraphic5);
            vt.setCellValueFactory(new PropertyValueFactory("id"));
            dashboard.setItems(data);
            dashboard.getSelectionModel().clearSelection();
            conn.close();
        } catch (Exception e) {
            dashboard.setItems(null);
            System.out.print(e);
        }

    }



    public class ShowData {
        private final StringProperty an;
        private final StringProperty odb;
        private final StringProperty ocr;
        private final StringProperty db;
        private final StringProperty cr;
        private final StringProperty bal;
        private final StringProperty id;




        public ShowData(String an,String odb, String ocr , String db, String cr, String bal , String id) {
            this.an = new SimpleStringProperty(an);
            this.odb= new SimpleStringProperty(odb);
            this.ocr= new SimpleStringProperty(ocr);
            this.db= new SimpleStringProperty(db);
            this.cr= new SimpleStringProperty(cr);
            this.bal= new SimpleStringProperty(bal);
            this.id= new SimpleStringProperty(id);

        }
        public String getan() { return an.get();}
        public void setan(String value) { an.setValue(value); }
        public StringProperty anProperty(){
            return an;
        }

        public String getodb() { return odb.get();}
        public void setodb(String value) { odb.setValue(value); }
        public StringProperty odbProperty(){
            return odb;
        }

        public String getocr() { return ocr.get(); }
        public void setocr(String value) { ocr.setValue(value); }
        public StringProperty ocrProperty(){
            return ocr;
        }

        public String getdb() { return db.get(); }
        public void setdb(String value) { db.setValue(value); }
        public StringProperty dbProperty(){ return db;
        }
        public String getcr() { return cr.get(); }
        public void setcr(String value) { cr.setValue(value); }
        public StringProperty crProperty(){
            return cr;
        }
        public String getbal() { return bal.get(); }
        public void setbal(String value) { bal.setValue(value); }
        public StringProperty balProperty(){
            return bal;
        }


        public String getid() { return id.get(); }
        public void setid(String value) { id.setValue(value); }
        public StringProperty idProperty(){
            return id;
        }



    }


}
