package main;

import com.mysql.fabric.xmlrpc.base.Data;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class trailbalance implements Initializable {

    @FXML
    private TableColumn an;
    @FXML
    private TableColumn odb;
    @FXML
    private TableColumn ocr;
    @FXML
    private TableColumn mdb;
    @FXML
    private TableColumn mcr;
    @FXML
    private TableColumn tdb;
    @FXML
    private TableColumn total;
    @FXML
    private TableColumn m1;
    @FXML
    private  TableColumn tcr;
    @FXML
    private TableView dashboard;
    @FXML
    private ComboBox type;
    @FXML
    private ComboBox comparison;
    @FXML
    private ComboBox level;
    @FXML
    private TextField periods;
    @FXML
    private DatePicker dt1;
    @FXML
    private DatePicker dt2;
    @FXML
    private Button conf;


    Statement statement1 ;
    Statement statement2;
    Statement statement3;
    Statement statement4;
    String accountname;
    double  opdb;
    double  opcr;
    double  tmdb;
    double  tmcr;
    double  todb;
    double  tocr;
    double  tdb1 = 0;
    double  tcr1 = 0;
    double  tdb2 = 0;
    double  tcr2 = 0;
    double tdb3 = 0;
    double  tcr3 = 0;
    Object key1;
    Object key2;
    String month;
    String year;
    String quarter;
    private ObservableList<trailbalance.ShowData> data;
    Connection conn ;



    TableColumn addcol ;

    TableColumn debitcol ;

    TableColumn creditcol ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DateTimeFormatter dtr1 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter m = DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter y = DateTimeFormatter.ofPattern("yyyy");
        LocalDateTime now1 = LocalDateTime.now();


        month = m.format(now1).replace("-", "");
        year =  y.format(now1).replace("-", "");

        level.getItems().add("ALL");
        level.getItems().add("0");
        level.getItems().add("1");
        level.getItems().add("2");
        level.getItems().add("3");
        level.getItems().add("4");
        level.getItems().add("5");


        type.getItems().add("COMPARISON");
        type.getItems().add("CURRENT MONTH");
        type.getItems().add("CURRENT QUARTER");
        type.getItems().add("CURRENT YEAR");
        type.getItems().add("LAST MONTH");
        type.getItems().add("LAST QUARTER");
        type.getItems().add("LAST YEAR");
        type.getItems().add("SPECIFIC");
        comparison.getItems().add("LAST PERIODS");
        comparison.getItems().add("LAST PERIODS LAST YEAR");


        type.setDisable(true);
        comparison.setDisable(true);
        periods.setDisable(true);
        dt1.setDisable(true);
        dt2.setDisable(true);
       // conf.setDisable(true);



        key1 = dtr1.format(now1).replace("-", "");
        // dt.setValue(LocalDate.from(now1));

        level.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            type.setDisable(false);
        });


        type.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
           if(newValue.equals("COMPARISON")){
               comparison.setDisable(false);
               periods.setDisable(false);
               dt1.setDisable(true);
               dt2.setDisable(true);
             //  conf.setDisable(true);
           }else if(newValue.equals("CURRENT MONTH")){
               comparison.setDisable(true);
               periods.setDisable(true);
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
                           tdb3=0;
                           tcr3=0;
                           conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                           statement1 = conn.createStatement();
                           if(level.getValue().equals("ALL")){
                               statement1.execute("SELECT * FROM acctree ");
                           }else {
                               statement1.execute("SELECT * FROM acctree WHERE level='"+level.getValue()+"' ");
                           }
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
                               statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND MONTH(dt) = MONTH(CURRENT_DATE())");
                               ResultSet results2 = statement3.getResultSet();
                               while (results2.next()) {
                                   tmdb= results2.getInt("sdb");
                                   tdb2=tdb2+tmdb;
                                   tmcr= results2.getInt("scr");
                                   tcr2=tcr2+tmcr;
                               }
                               todb=opdb+tmdb;
                               tdb3=tdb3+todb;
                               tocr=opcr+tmcr;
                               tcr3=tcr3+tocr;
                               data.add(new trailbalance.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(tmdb), String.valueOf(tmcr), String.valueOf(todb),String.valueOf(tocr), String.valueOf(tdb1),String.valueOf(tcr1)));
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
                           m1.setText(month+" "+year);
                           mdb.setCellValueFactory(new PropertyValueFactory("mdb"));
                           Label label5 = new Label("DEBIT");
                           Label label6 = new Label(String.valueOf(tdb2));
                           VBox headerGraphic3 = new VBox();
                           headerGraphic3.setAlignment(Pos.CENTER);
                           headerGraphic3.getChildren().addAll(label5,label6);
                           mdb.setGraphic(headerGraphic3);
                           mcr.setCellValueFactory(new PropertyValueFactory("mcr"));
                           Label label7 = new Label("CREDIT");
                           Label label8 = new Label(String.valueOf(tcr2));
                           VBox headerGraphic4 = new VBox();
                           headerGraphic4.setAlignment(Pos.CENTER);
                           headerGraphic4.getChildren().addAll(label7,label8);
                           mcr.setGraphic(headerGraphic4);
                           tdb.setCellValueFactory(new PropertyValueFactory("tdb"));
                           Label label9 = new Label("DEBIT");
                           Label label10 = new Label(String.valueOf(tdb3));
                           VBox headerGraphic5 = new VBox();
                           headerGraphic5.setAlignment(Pos.CENTER);
                           headerGraphic5.getChildren().addAll(label9,label10);
                           tdb.setGraphic(headerGraphic5);
                           tcr.setCellValueFactory(new PropertyValueFactory("tcr"));
                           Label label11 = new Label("CREDIT");
                           Label label12 = new Label(String.valueOf(tcr3));
                           VBox headerGraphic6 = new VBox();
                           headerGraphic6.setAlignment(Pos.CENTER);
                           headerGraphic6.getChildren().addAll(label11,label12);
                           tcr.setGraphic(headerGraphic6);
                           dashboard.setItems(data);
                           dashboard.getSelectionModel().clearSelection();
                           conn.close();
                       } catch (Exception e) {
                           dashboard.setItems(null);
                           System.out.print(e);
                       }
                   }});
           }else if(newValue.equals("CURRENT QUARTER")){
               comparison.setDisable(true);
               periods.setDisable(true);
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
                           tdb3=0;
                           tcr3=0;
                           conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                           statement1 = conn.createStatement();
                           if(level.getValue().equals("ALL")){
                               statement1.execute("SELECT * FROM acctree ");
                           }else {
                               statement1.execute("SELECT * FROM acctree WHERE level='"+level.getValue()+"' ");
                           }
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
                               statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND QUARTER(dt) = QUARTER(curdate())");
                               ResultSet results2 = statement3.getResultSet();
                               while (results2.next()) {
                                   tmdb= results2.getInt("sdb");
                                   tdb2=tdb2+tmdb;
                                   tmcr= results2.getInt("scr");
                                   tcr2=tcr2+tmcr;
                               }
                               todb=opdb+tmdb;
                               tdb3=tdb3+todb;
                               tocr=opcr+tmcr;
                               tcr3=tcr3+tocr;
                               data.add(new trailbalance.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(tmdb), String.valueOf(tmcr), String.valueOf(todb),String.valueOf(tocr), String.valueOf(tdb1),String.valueOf(tcr1)));
                           }
                           statement4 = conn.createStatement();
                           statement4.execute("SELECT QUARTER(curdate()) AS q");
                           ResultSet results3 = statement4.getResultSet();
                           while (results3.next()) {
                               quarter= results3.getString("q");

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
                           m1.setText(month+" "+year);
                           mdb.setCellValueFactory(new PropertyValueFactory("mdb"));
                           Label label5 = new Label("DEBIT");
                           Label label6 = new Label(String.valueOf(tdb2));
                           VBox headerGraphic3 = new VBox();
                           headerGraphic3.setAlignment(Pos.CENTER);
                           headerGraphic3.getChildren().addAll(label5,label6);
                           mdb.setGraphic(headerGraphic3);
                           mcr.setCellValueFactory(new PropertyValueFactory("mcr"));
                           Label label7 = new Label("CREDIT");
                           Label label8 = new Label(String.valueOf(tcr2));
                           VBox headerGraphic4 = new VBox();
                           headerGraphic4.setAlignment(Pos.CENTER);
                           headerGraphic4.getChildren().addAll(label7,label8);
                           mcr.setGraphic(headerGraphic4);
                           tdb.setCellValueFactory(new PropertyValueFactory("tdb"));
                           Label label9 = new Label("DEBIT");
                           Label label10 = new Label(String.valueOf(tdb3));
                           VBox headerGraphic5 = new VBox();
                           headerGraphic5.setAlignment(Pos.CENTER);
                           headerGraphic5.getChildren().addAll(label9,label10);
                           tdb.setGraphic(headerGraphic5);
                           tcr.setCellValueFactory(new PropertyValueFactory("tcr"));
                           Label label11 = new Label("CREDIT");
                           Label label12 = new Label(String.valueOf(tcr3));
                           VBox headerGraphic6 = new VBox();
                           headerGraphic6.setAlignment(Pos.CENTER);
                           headerGraphic6.getChildren().addAll(label11,label12);
                           tcr.setGraphic(headerGraphic6);
                           dashboard.setItems(data);
                           dashboard.getSelectionModel().clearSelection();
                           conn.close();
                       } catch (Exception e) {
                           dashboard.setItems(null);
                           System.out.print(e);
                       }

                   }});

           }else if(newValue.equals("CURRENT YEAR")){
               comparison.setDisable(true);
               periods.setDisable(true);
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
                           tdb3=0;
                           tcr3=0;
                           conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                           statement1 = conn.createStatement();
                           if(level.getValue().equals("ALL")){
                               statement1.execute("SELECT * FROM acctree ");
                           }else {
                               statement1.execute("SELECT * FROM acctree WHERE level='"+level.getValue()+"' ");
                           }
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
                               statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND  YEAR(dt) = YEAR(CURDATE())");
                               ResultSet results2 = statement3.getResultSet();
                               while (results2.next()) {
                                   tmdb= results2.getInt("sdb");
                                   tdb2=tdb2+tmdb;
                                   tmcr= results2.getInt("scr");
                                   tcr2=tcr2+tmcr;
                               }
                               todb=opdb+tmdb;
                               tdb3=tdb3+todb;
                               tocr=opcr+tmcr;
                               tcr3=tcr3+tocr;
                               data.add(new trailbalance.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(tmdb), String.valueOf(tmcr), String.valueOf(todb),String.valueOf(tocr), String.valueOf(tdb1),String.valueOf(tcr1)));
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
                           m1.setText(month+" "+year);
                           mdb.setCellValueFactory(new PropertyValueFactory("mdb"));
                           Label label5 = new Label("DEBIT");
                           Label label6 = new Label(String.valueOf(tdb2));
                           VBox headerGraphic3 = new VBox();
                           headerGraphic3.setAlignment(Pos.CENTER);
                           headerGraphic3.getChildren().addAll(label5,label6);
                           mdb.setGraphic(headerGraphic3);
                           mcr.setCellValueFactory(new PropertyValueFactory("mcr"));
                           Label label7 = new Label("CREDIT");
                           Label label8 = new Label(String.valueOf(tcr2));
                           VBox headerGraphic4 = new VBox();
                           headerGraphic4.setAlignment(Pos.CENTER);
                           headerGraphic4.getChildren().addAll(label7,label8);
                           mcr.setGraphic(headerGraphic4);
                           tdb.setCellValueFactory(new PropertyValueFactory("tdb"));
                           Label label9 = new Label("DEBIT");
                           Label label10 = new Label(String.valueOf(tdb3));
                           VBox headerGraphic5 = new VBox();
                           headerGraphic5.setAlignment(Pos.CENTER);
                           headerGraphic5.getChildren().addAll(label9,label10);
                           tdb.setGraphic(headerGraphic5);
                           tcr.setCellValueFactory(new PropertyValueFactory("tcr"));
                           Label label11 = new Label("CREDIT");
                           Label label12 = new Label(String.valueOf(tcr3));
                           VBox headerGraphic6 = new VBox();
                           headerGraphic6.setAlignment(Pos.CENTER);
                           headerGraphic6.getChildren().addAll(label11,label12);
                           tcr.setGraphic(headerGraphic6);
                           dashboard.setItems(data);
                           dashboard.getSelectionModel().clearSelection();
                           conn.close();
                       } catch (Exception e) {
                           dashboard.setItems(null);
                           System.out.print(e);
                       }

                   }});
           }else if(newValue.equals("LAST MONTH")){
               comparison.setDisable(true);
               periods.setDisable(true);
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
                           tdb3=0;
                           tcr3=0;
                           conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                           statement1 = conn.createStatement();
                           if(level.getValue().equals("ALL")){
                               statement1.execute("SELECT * FROM acctree ");
                           }else {
                               statement1.execute("SELECT * FROM acctree WHERE level='"+level.getValue()+"' ");
                           }
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
                                   tmdb= results2.getInt("sdb");
                                   tdb2=tdb2+tmdb;
                                   tmcr= results2.getInt("scr");
                                   tcr2=tcr2+tmcr;
                               }
                               todb=opdb+tmdb;
                               tdb3=tdb3+todb;
                               tocr=opcr+tmcr;
                               tcr3=tcr3+tocr;
                               data.add(new trailbalance.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(tmdb), String.valueOf(tmcr), String.valueOf(todb),String.valueOf(tocr), String.valueOf(tdb1),String.valueOf(tcr1)));
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
                           m1.setText(month+" "+year);
                           mdb.setCellValueFactory(new PropertyValueFactory("mdb"));
                           Label label5 = new Label("DEBIT");
                           Label label6 = new Label(String.valueOf(tdb2));
                           VBox headerGraphic3 = new VBox();
                           headerGraphic3.setAlignment(Pos.CENTER);
                           headerGraphic3.getChildren().addAll(label5,label6);
                           mdb.setGraphic(headerGraphic3);
                           mcr.setCellValueFactory(new PropertyValueFactory("mcr"));
                           Label label7 = new Label("CREDIT");
                           Label label8 = new Label(String.valueOf(tcr2));
                           VBox headerGraphic4 = new VBox();
                           headerGraphic4.setAlignment(Pos.CENTER);
                           headerGraphic4.getChildren().addAll(label7,label8);
                           mcr.setGraphic(headerGraphic4);
                           tdb.setCellValueFactory(new PropertyValueFactory("tdb"));
                           Label label9 = new Label("DEBIT");
                           Label label10 = new Label(String.valueOf(tdb3));
                           VBox headerGraphic5 = new VBox();
                           headerGraphic5.setAlignment(Pos.CENTER);
                           headerGraphic5.getChildren().addAll(label9,label10);
                           tdb.setGraphic(headerGraphic5);
                           tcr.setCellValueFactory(new PropertyValueFactory("tcr"));
                           Label label11 = new Label("CREDIT");
                           Label label12 = new Label(String.valueOf(tcr3));
                           VBox headerGraphic6 = new VBox();
                           headerGraphic6.setAlignment(Pos.CENTER);
                           headerGraphic6.getChildren().addAll(label11,label12);
                           tcr.setGraphic(headerGraphic6);
                           dashboard.setItems(data);
                           dashboard.getSelectionModel().clearSelection();
                           conn.close();
                       } catch (Exception e) {
                           dashboard.setItems(null);
                           System.out.print(e);
                       }

                   }});
           }else if(newValue.equals("LAST QUARTER")){
               comparison.setDisable(true);
               periods.setDisable(true);
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
                           tdb3=0;
                           tcr3=0;
                           conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                           statement1 = conn.createStatement();
                           if(level.getValue().equals("ALL")){
                               statement1.execute("SELECT * FROM acctree ");
                           }else {
                               statement1.execute("SELECT * FROM acctree WHERE level='"+level.getValue()+"' ");
                           }
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
                                   tmdb= results2.getInt("sdb");
                                   tdb2=tdb2+tmdb;
                                   tmcr= results2.getInt("scr");
                                   tcr2=tcr2+tmcr;
                               }
                               statement4 = conn.createStatement();
                               statement4.execute("SELECT QUARTER(curdate() - INTERVAL 1 quarter) AS q");
                               ResultSet results3 = statement4.getResultSet();
                               while (results3.next()) {
                                   quarter= results3.getString("q");

                               }
                               todb=opdb+tmdb;
                               tdb3=tdb3+todb;
                               tocr=opcr+tmcr;
                               tcr3=tcr3+tocr;
                               data.add(new trailbalance.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(tmdb), String.valueOf(tmcr), String.valueOf(todb),String.valueOf(tocr), String.valueOf(tdb1),String.valueOf(tcr1)));
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
                           m1.setText(month+" "+year);
                           mdb.setCellValueFactory(new PropertyValueFactory("mdb"));
                           Label label5 = new Label("DEBIT");
                           Label label6 = new Label(String.valueOf(tdb2));
                           VBox headerGraphic3 = new VBox();
                           headerGraphic3.setAlignment(Pos.CENTER);
                           headerGraphic3.getChildren().addAll(label5,label6);
                           mdb.setGraphic(headerGraphic3);
                           mcr.setCellValueFactory(new PropertyValueFactory("mcr"));
                           Label label7 = new Label("CREDIT");
                           Label label8 = new Label(String.valueOf(tcr2));
                           VBox headerGraphic4 = new VBox();
                           headerGraphic4.setAlignment(Pos.CENTER);
                           headerGraphic4.getChildren().addAll(label7,label8);
                           mcr.setGraphic(headerGraphic4);
                           tdb.setCellValueFactory(new PropertyValueFactory("tdb"));
                           Label label9 = new Label("DEBIT");
                           Label label10 = new Label(String.valueOf(tdb3));
                           VBox headerGraphic5 = new VBox();
                           headerGraphic5.setAlignment(Pos.CENTER);
                           headerGraphic5.getChildren().addAll(label9,label10);
                           tdb.setGraphic(headerGraphic5);
                           tcr.setCellValueFactory(new PropertyValueFactory("tcr"));
                           Label label11 = new Label("CREDIT");
                           Label label12 = new Label(String.valueOf(tcr3));
                           VBox headerGraphic6 = new VBox();
                           headerGraphic6.setAlignment(Pos.CENTER);
                           headerGraphic6.getChildren().addAll(label11,label12);
                           tcr.setGraphic(headerGraphic6);
                           dashboard.setItems(data);
                           dashboard.getSelectionModel().clearSelection();
                           conn.close();
                       } catch (Exception e) {
                           dashboard.setItems(null);
                           System.out.print(e);
                       }

                   }});
           }else if(newValue.equals("LAST YEAR")){
               comparison.setDisable(true);
               periods.setDisable(true);
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
                           tdb3=0;
                           tcr3=0;
                           conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                           statement1 = conn.createStatement();
                           if(level.getValue().equals("ALL")){
                               statement1.execute("SELECT * FROM acctree ");
                           }else {
                               statement1.execute("SELECT * FROM acctree WHERE level='"+level.getValue()+"' ");
                           }
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
                                   tmdb= results2.getInt("sdb");
                                   tdb2=tdb2+tmdb;
                                   tmcr= results2.getInt("scr");
                                   tcr2=tcr2+tmcr;
                               }
                               todb=opdb+tmdb;
                               tdb3=tdb3+todb;
                               tocr=opcr+tmcr;
                               tcr3=tcr3+tocr;
                               data.add(new trailbalance.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(tmdb), String.valueOf(tmcr), String.valueOf(todb),String.valueOf(tocr), String.valueOf(tdb1),String.valueOf(tcr1)));
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
                           m1.setText(month+" "+year);
                           mdb.setCellValueFactory(new PropertyValueFactory("mdb"));
                           Label label5 = new Label("DEBIT");
                           Label label6 = new Label(String.valueOf(tdb2));
                           VBox headerGraphic3 = new VBox();
                           headerGraphic3.setAlignment(Pos.CENTER);
                           headerGraphic3.getChildren().addAll(label5,label6);
                           mdb.setGraphic(headerGraphic3);
                           mcr.setCellValueFactory(new PropertyValueFactory("mcr"));
                           Label label7 = new Label("CREDIT");
                           Label label8 = new Label(String.valueOf(tcr2));
                           VBox headerGraphic4 = new VBox();
                           headerGraphic4.setAlignment(Pos.CENTER);
                           headerGraphic4.getChildren().addAll(label7,label8);
                           mcr.setGraphic(headerGraphic4);
                           tdb.setCellValueFactory(new PropertyValueFactory("tdb"));
                           Label label9 = new Label("DEBIT");
                           Label label10 = new Label(String.valueOf(tdb3));
                           VBox headerGraphic5 = new VBox();
                           headerGraphic5.setAlignment(Pos.CENTER);
                           headerGraphic5.getChildren().addAll(label9,label10);
                           tdb.setGraphic(headerGraphic5);
                           tcr.setCellValueFactory(new PropertyValueFactory("tcr"));
                           Label label11 = new Label("CREDIT");
                           Label label12 = new Label(String.valueOf(tcr3));
                           VBox headerGraphic6 = new VBox();
                           headerGraphic6.setAlignment(Pos.CENTER);
                           headerGraphic6.getChildren().addAll(label11,label12);
                           tcr.setGraphic(headerGraphic6);
                           dashboard.setItems(data);
                           dashboard.getSelectionModel().clearSelection();
                           conn.close();
                       } catch (Exception e) {
                           dashboard.setItems(null);
                           System.out.print(e);
                       }

                   }});
           }else if(newValue.equals("SPECIFIC")){
               comparison.setDisable(true);
               periods.setDisable(true);
               dt1.setDisable(false);
               dt2.setDisable(false);
               conf.setDisable(false);
           }else  if(type.getValue().equals("COMPARISON")){
               if(comparison.equals("LAST PERIODS")){
                   dashboard.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                   periods.setDisable(false);
                   dt1.setDisable(true);
                   dt2.setDisable(true);
                   conf.setDisable(true);
                   dashboard.getColumns().remove(total);
                   dashboard.getColumns().remove(m1);
                   int i;
                   for(i = Integer.valueOf(month)-1; i >= (Integer.valueOf(month)-Integer.valueOf(periods.getText())) && i>0; i--)
                   {
                       key1=i;
                       key2=year;
                       addc();
                       if(i==i){break;}
                   }

               } else if(comparison.equals("LAST PERIODS LAST YEAR")){
                   dashboard.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                   periods.setDisable(false);
                   dt1.setDisable(true);
                   dt2.setDisable(true);
                   conf.setDisable(true);
                   dashboard.getColumns().remove(total);
                   dashboard.getColumns().remove(m1);
                   int i;
                   for(i = Integer.valueOf(month)-1; i >= (Integer.valueOf(month)-Integer.valueOf(periods.getText())) && i>0; i--)
                   {
                       key1=i;
                       key2=Integer.parseInt(year)-1;
                       addc();
                       if(i==i){break;}
                   }
               }
           }
        });


    }




    public void calc() {
       if (type.getValue().equals("SPECIFIC")){
            try {
                tdb1=0;
                tcr1=0;
                tdb2=0;
                tcr2=0;
                tdb3=0;
                tcr3=0;
                conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                statement1 = conn.createStatement();
                if(level.getValue().equals("ALL")){
                    statement1.execute("SELECT * FROM acctree ");
                }else {
                    statement1.execute("SELECT * FROM acctree WHERE level='"+level.getValue()+"' ");
                }
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
                    statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND dt between '"+dt1.getValue()+"' AND '"+dt2.getValue()+"' ");
                    ResultSet results2 = statement3.getResultSet();
                    while (results2.next()) {
                        tmdb= results2.getInt("sdb");
                        tdb2=tdb2+tmdb;
                        tmcr= results2.getInt("scr");
                        tcr2=tcr2+tmcr;
                    }
                    todb=opdb+tmdb;
                    tdb3=tdb3+todb;
                    tocr=opcr+tmcr;
                    tcr3=tcr3+tocr;
                    data.add(new trailbalance.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(tmdb), String.valueOf(tmcr), String.valueOf(todb),String.valueOf(tocr), String.valueOf(tdb1),String.valueOf(tcr1)));
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
                m1.setText(month+" "+year);
                mdb.setCellValueFactory(new PropertyValueFactory("mdb"));
                Label label5 = new Label("DEBIT");
                Label label6 = new Label(String.valueOf(tdb2));
                VBox headerGraphic3 = new VBox();
                headerGraphic3.setAlignment(Pos.CENTER);
                headerGraphic3.getChildren().addAll(label5,label6);
                mdb.setGraphic(headerGraphic3);
                mcr.setCellValueFactory(new PropertyValueFactory("mcr"));
                Label label7 = new Label("CREDIT");
                Label label8 = new Label(String.valueOf(tcr2));
                VBox headerGraphic4 = new VBox();
                headerGraphic4.setAlignment(Pos.CENTER);
                headerGraphic4.getChildren().addAll(label7,label8);
                mcr.setGraphic(headerGraphic4);
                tdb.setCellValueFactory(new PropertyValueFactory("tdb"));
                Label label9 = new Label("DEBIT");
                Label label10 = new Label(String.valueOf(tdb3));
                VBox headerGraphic5 = new VBox();
                headerGraphic5.setAlignment(Pos.CENTER);
                headerGraphic5.getChildren().addAll(label9,label10);
                tdb.setGraphic(headerGraphic5);
                tcr.setCellValueFactory(new PropertyValueFactory("tcr"));
                Label label11 = new Label("CREDIT");
                Label label12 = new Label(String.valueOf(tcr3));
                VBox headerGraphic6 = new VBox();
                headerGraphic6.setAlignment(Pos.CENTER);
                headerGraphic6.getChildren().addAll(label11,label12);
                tcr.setGraphic(headerGraphic6);
                dashboard.setItems(data);
                dashboard.getSelectionModel().clearSelection();
                conn.close();
            } catch (Exception e) {
                dashboard.setItems(null);
                System.out.print(e);
            }
        }



    }

    public void addc() {
        addcol = new TableColumn(key1+"-"+key2);
        addcol.setMinWidth(300);
        debitcol = new TableColumn();
        debitcol.setMinWidth(130);
        creditcol = new TableColumn();
        creditcol.setMinWidth(130);
        addcol.getColumns().addAll(debitcol, creditcol);
        try {
            tdb1=0;
            tcr1=0;
            tdb2=0;
            tcr2=0;
            tdb3=0;
            tcr3=0;
            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            statement1 = conn.createStatement();
            if(level.getValue().equals("ALL")){
                statement1.execute("SELECT * FROM acctree ");
            }else {
                statement1.execute("SELECT * FROM acctree WHERE level='"+level.getValue()+"' ");
            }
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
                statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND month(dt)='"+key1+"' AND YEAR(dt)='"+key2+"' ");
                ResultSet results2 = statement3.getResultSet();
                while (results2.next()) {
                    tmdb= results2.getInt("sdb");
                    tdb2=tdb2+tmdb;
                    tmcr= results2.getInt("scr");
                    tcr2=tcr2+tmcr;
                }
                todb=opdb+tmdb;
                tdb3=tdb3+todb;
                tocr=opcr+tmcr;
                tcr3=tcr3+tocr;
                data.add(new trailbalance.ShowData(accountname,String.valueOf(opdb), String.valueOf(opcr), String.valueOf(tmdb), String.valueOf(tmcr), String.valueOf(todb),String.valueOf(tocr), String.valueOf(tdb1),String.valueOf(tcr1)));
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
            debitcol.setCellValueFactory(new PropertyValueFactory("mdb"));
            Label label5 = new Label("DEBIT");
            Label label6 = new Label(String.valueOf(tdb2));
            VBox headerGraphic3 = new VBox();
            headerGraphic3.setAlignment(Pos.CENTER);
            headerGraphic3.getChildren().addAll(label5,label6);
            debitcol.setGraphic(headerGraphic3);
            creditcol.setCellValueFactory(new PropertyValueFactory("mcr"));
            Label label7 = new Label("CREDIT");
            Label label8 = new Label(String.valueOf(tcr2));
            VBox headerGraphic4 = new VBox();
            headerGraphic4.setAlignment(Pos.CENTER);
            headerGraphic4.getChildren().addAll(label7,label8);
            creditcol.setGraphic(headerGraphic4);
            dashboard.setItems(data);
            dashboard.getSelectionModel().clearSelection();
            conn.close();
        } catch (Exception e) {
            dashboard.setItems(null);
            System.out.print(e);
        }

        Callback<TableColumn<Data, Void>, TableCell<Data, Void>> cellFactory = new Callback<TableColumn<Data, Void>, TableCell<Data, Void>>() {

            @Override
            public TableCell<Data, Void> call(final TableColumn<Data, Void> param) {
                final TableCell<Data, Void> cell = new TableCell<Data, Void>() {
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
                return cell;
            }
        };

        addcol.setCellFactory(cellFactory);
        dashboard.getColumns().add(addcol);
    }




    public class ShowData {
        private final StringProperty an;
        private final StringProperty odb;
        private final StringProperty ocr;
        private final StringProperty mdb;
        private final StringProperty mcr;
        private final StringProperty tdb;
        private final StringProperty tcr;
        private final StringProperty t1;
        private final StringProperty t2;




        public ShowData(String an,String odb, String ocr , String mdb, String mcr, String tdb, String tcr, String t1, String t2) {
            this.an = new SimpleStringProperty(an);
            this.odb= new SimpleStringProperty(odb);
            this.ocr= new SimpleStringProperty(ocr);
            this.mdb= new SimpleStringProperty(mdb);
            this.mcr= new SimpleStringProperty(mcr);
            this.tdb= new SimpleStringProperty(tdb);
            this.tcr= new SimpleStringProperty(tcr);
            this.t1= new SimpleStringProperty(t1);
            this.t2= new SimpleStringProperty(t2);

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

        public String getmdb() { return mdb.get(); }
        public void setmdb(String value) { mdb.setValue(value); }
        public StringProperty mdbProperty(){ return mdb;
        }
        public String getmcr() { return mcr.get(); }
        public void setmcr(String value) { mcr.setValue(value); }
        public StringProperty mcrProperty(){
            return mcr;
        }


        public String gettdb() { return tdb.get(); }
        public void settdb(String value) { tdb.setValue(value); }
        public StringProperty tdbProperty(){
            return tdb;
        }


        public String gettcr() { return tcr.get(); }
        public void settcr(String value) { tcr.setValue(value); }
        public StringProperty tcrProperty(){
            return tcr;
        }


        public String gett1() { return t1.get(); }
        public void sett1(String value) { t1.setValue(value); }
        public StringProperty t1Property(){
            return t1;
        }


        public String gett2() { return t2.get(); }
        public void sett2(String value) { t2.setValue(value); }
        public StringProperty t2Property(){
            return t2;
        }


    }



}
