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

public class suppliers implements Initializable {

    @FXML
    private TableColumn sn;
    @FXML
    private TableColumn va;
    @FXML
    private TableColumn id;
    @FXML
    private TableColumn dt;
    @FXML
    private TableColumn net;
    @FXML
    private TableColumn bb;
    @FXML
    private TableView suplist;
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
    public static Object idb;
    public static Object sid;

    private double xOffset = 0.0D;
    private double yOffset = 0.0D;


    private ObservableList<suppliers.ShowData1> data1;
    private ObservableList<suppliers.ShowData2> data2;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        va.setStyle( "-fx-alignment: CENTER;");
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM suppliers");
            ResultSet results = statement.getResultSet();
            data1 = FXCollections.observableArrayList();
            while (results.next()) {
                data1.add(new suppliers.ShowData1("   "+results.getString("name"),results.getString("id")));
            }
            sn.setCellValueFactory(new PropertyValueFactory("supplier"));
            va.setCellValueFactory(new PropertyValueFactory("account"));
            suplist.setItems(data1);
            suplist.getSelectionModel().clearSelection();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        suplist.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList<TablePosition> selectedCells = suplist.getSelectionModel().getSelectedCells() ;
        selectedCells.addListener((ListChangeListener.Change<? extends TablePosition> change) -> {
            if (selectedCells.size() > 0) {
                TablePosition selectedCell = selectedCells.get(0);
                TableColumn column = selectedCell.getTableColumn();
                if(column == va){
                    int rowIndex = selectedCell.getRow();
                    sid = column.getCellObservableValue(rowIndex).getValue();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                conn = DriverManager.getConnection("jdbc:mysql://"+Main.ip+"/fouadcompany", "root", "");
                                statement0= conn.createStatement();
                                statement0.execute("SELECT * FROM suppliers WHERE  id= '"+sid+"'");
                                ResultSet results0 = statement0.getResultSet();
                                while (results0.next()) {
                                    n.setText(results0.getString("name"));
                                    cg.setText(results0.getString("governate"));
                                    ca.setText(results0.getString("adress"));
                                    ct.setText(results0.getString("telephone"));
                                }

                            }catch (Exception e) {
                                System.out.print(e);
                            }
                            try {
                                conn = DriverManager.getConnection("jdbc:mysql://"+ Main.ip+"/fouadcompany", "root", "");
                                statement1 = conn.createStatement();
                                statement1.execute("SELECT * FROM bills WHERE  sup_id= '"+sid+"' ");
                                ResultSet results = statement1.getResultSet();
                                data2= FXCollections.observableArrayList();
                                while (results.next()) {
                                    data2.add(new suppliers.ShowData2(results.getString("id"),results.getString("dt"),results.getString("net_pay")));
                                }
                                id.setCellValueFactory(new PropertyValueFactory("id"));
                                dt.setCellValueFactory(new PropertyValueFactory("date"));
                                net.setCellValueFactory(new PropertyValueFactory("pay"));
                                bb.setCellValueFactory(new PropertyValueFactory("id"));
                                dashboard.setItems(data2);
                                dashboard.getSelectionModel().clearSelection();
                                conn.close();
                                suplist.getSelectionModel().clearSelection();
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
                    idb = column1.getCellObservableValue(rowIndex).getValue();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Parent root = null;
                            try {
                                root = FXMLLoader.load(getClass().getResource("retail/billview.fxml"));
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
                    final javafx.scene.control.Label view = new Label("VIEW SUPPLIER ");
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
                    final Label view = new Label("VIEW BILL");
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
        private final StringProperty supplier;
        private final StringProperty account;

        public ShowData1(String supplier,String account) {
            this.supplier = new SimpleStringProperty(supplier);
            this.account = new SimpleStringProperty(account);
        }
        public String getsupplier() { return supplier.get();}
        public void setsupplier(String value) { supplier.setValue(value); }
        public StringProperty supplierProperty(){
            return supplier;
        }

        public String getaccount() { return account.get();}
        public void setaccount(String value) { account.setValue(value); }
        public StringProperty accountProperty(){
            return account;
        }

    }

    public class ShowData2 {
        private final StringProperty id;
        private final StringProperty date;
        private final StringProperty pay;


        public ShowData2(String id,String date, String pay  ) {
            this.id= new SimpleStringProperty(id);
            this.date= new SimpleStringProperty(date);
            this.pay= new SimpleStringProperty(pay);

        }

        public String getid() { return id.get(); }
        public void setid(String value) { id.setValue(value); }
        public StringProperty idProperty(){
            return id;
        }

        public String getdate() { return date.get();}
        public void setdate(String value) { date.setValue(value); }
        public StringProperty dateProperty(){
            return date;
        }

        public String getpay() { return pay.get();}
        public void setpay(String value) { pay.setValue(value); }
        public StringProperty payProperty(){
            return pay;
        }


    }
}
