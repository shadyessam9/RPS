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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class customers implements Initializable {

    @FXML
    private TableColumn cn;
    @FXML
    private TableColumn va;
    @FXML
    private TableColumn en;
    @FXML
    private TableColumn et;
    @FXML
    private TableColumn ep;
    @FXML
    private TableColumn bb;
    @FXML
    private TableView custlist;
    @FXML
    private TableView dashboard;
    @FXML
    private Label n;
    @FXML
    private Label cg;
    @FXML
    private Label ca;
    @FXML
    private Label ct;


    Connection conn ;
    Statement statement0;
    Statement statement1;
    public static Object ide;
    Object cid;

    private double xOffset = 0.0D;
    private double yOffset = 0.0D;


    private ObservableList<customers.ShowData1> data1;
    private ObservableList<customers.ShowData2> data2;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        va.setStyle( "-fx-alignment: CENTER;");
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM customers");
            ResultSet results = statement.getResultSet();
            data1 = FXCollections.observableArrayList();
            while (results.next()) {
                data1.add(new customers.ShowData1("   "+results.getString("name"),results.getString("id")));
            }
            cn.setCellValueFactory(new PropertyValueFactory("customer"));
            va.setCellValueFactory(new PropertyValueFactory("account"));
            custlist.setItems(data1);
            custlist.getSelectionModel().clearSelection();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        custlist.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList<TablePosition> selectedCells = custlist.getSelectionModel().getSelectedCells() ;
        selectedCells.addListener((ListChangeListener.Change<? extends TablePosition> change) -> {
            if (selectedCells.size() > 0) {
                TablePosition selectedCell = selectedCells.get(0);
                TableColumn column = selectedCell.getTableColumn();
                if(column == va){
                    int rowIndex = selectedCell.getRow();
                    cid = column.getCellObservableValue(rowIndex).getValue();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                conn = DriverManager.getConnection("jdbc:mysql://"+Main.ip+"/fouadcompany", "root", "");
                                statement0= conn.createStatement();
                                statement0.execute("SELECT * FROM customers WHERE  id= '"+cid+"'");
                                ResultSet results0 = statement0.getResultSet();
                                while (results0.next()) {
                                    n.setText(results0.getString("name"));
                                    cg.setText("ALEXANDRIA");
                                    ca.setText(results0.getString("adress"));
                                    ct.setText(results0.getString("telephone"));
                                }

                            }catch (Exception e) {
                                System.out.print(e);
                            }
                            try {
                                conn = DriverManager.getConnection("jdbc:mysql://"+Main.ip+"/fouadcompany", "root", "");
                                statement1 = conn.createStatement();
                                statement1.execute("SELECT * FROM extracts WHERE  client_id= '"+cid+"' ");
                                ResultSet results = statement1.getResultSet();
                                data2= FXCollections.observableArrayList();
                                while (results.next()) {
                                    data2.add(new customers.ShowData2(results.getString("number"),results.getString("type"),results.getString("pay"),results.getString("id")));
                                }
                                en.setCellValueFactory(new PropertyValueFactory("number"));
                                et.setCellValueFactory(new PropertyValueFactory("type"));
                                ep.setCellValueFactory(new PropertyValueFactory("pay"));
                                bb.setCellValueFactory(new PropertyValueFactory("id"));
                                dashboard.setItems(data2);
                                dashboard.getSelectionModel().clearSelection();
                                conn.close();
                                custlist.getSelectionModel().clearSelection();
                            } catch (Exception e) {
                                dashboard.setItems(null);
                                System.out.print(e);
                            }
                        }});
                }
            }
        });
        dashboard.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList<TablePosition> selectedCells1 = dashboard.getSelectionModel().getSelectedCells() ;
        selectedCells1.addListener((ListChangeListener.Change<? extends TablePosition> change) -> {
            if (selectedCells1.size() > 0) {
                TablePosition selectedCell1 = selectedCells1.get(0);
                TableColumn column1 = selectedCell1.getTableColumn();
                if(column1 == bb){
                    int rowIndex = selectedCell1.getRow();
                    ide = column1.getCellObservableValue(rowIndex).getValue();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Parent root = null;
                            try {
                                root = FXMLLoader.load(getClass().getResource("retail/extractview2.fxml"));
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
                viewextracts();
                viewbands();
            }});
    }



    public void viewextracts(){
        va.setCellFactory(col -> {
            TableCell<Data, Object> cell = new TableCell<Data, Object>() {
                public void updateItem(Object item, boolean empty) {
                    final javafx.scene.control.Label view = new Label("VIEW CUSTOMER  ");
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
    public void viewbands(){
        bb.setCellFactory(col -> {
            TableCell<Data, Object> cell = new TableCell<Data, Object>() {
                public void updateItem(Object item, boolean empty) {
                    final Label view = new Label("VIEW BANDS");
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
    public class ShowData1 {
        private final StringProperty customer;
        private final StringProperty account;

        public ShowData1(String customer,String account) {
            this.customer = new SimpleStringProperty(customer);
            this.account = new SimpleStringProperty(account);
        }
        public String getcustomer() { return customer.get();}
        public void setcustomer(String value) { customer.setValue(value); }
        public StringProperty customerProperty(){
            return customer;
        }

        public String getaccount() { return account.get();}
        public void setaccount(String value) { account.setValue(value); }
        public StringProperty accountProperty(){
            return account;
        }

    }

    public class ShowData2 {
        private final StringProperty number;
        private final StringProperty type;
        private final StringProperty pay;
        private final StringProperty id;
        public ShowData2(String number,String type, String pay, String id ) {
            this.number= new SimpleStringProperty(number);
            this.type= new SimpleStringProperty(type);
            this.pay= new SimpleStringProperty(pay);
            this.id= new SimpleStringProperty(id);
        }
        public String getnumber() { return number.get();}
        public void setnumber(String value) { number.setValue(value); }
        public StringProperty numberProperty(){
            return number;
        }
        public String gettype() { return type.get();}
        public void settype(String value) { type.setValue(value); }
        public StringProperty typeProperty(){
            return type;
        }
        public String getpay() { return pay.get();}
        public void setpay(String value) { pay.setValue(value); }
        public StringProperty payProperty(){
            return pay;
        }
        public String getid() { return id.get(); }
        public void setid(String value) { id.setValue(value); }
        public StringProperty idProperty(){
            return id;
        }
    }
}