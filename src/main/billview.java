package main;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class billview implements Initializable {
    @FXML
    private Label bd;
    @FXML
    private Label bid;
    @FXML
    private Label pd;
    @FXML
    private Label accd;
    @FXML
    private Label sn;
    @FXML
    private Label t;
    @FXML
    private Label at;
    @FXML
    private Label nt;
    @FXML
    private TableColumn i;
    @FXML
    private TableColumn p;
    @FXML
    private TableColumn qn;
    @FXML
    private TableView itemslist;

    Object idb;

    private ObservableList<billview.ShowData> data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idb=suppliers.idb;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    Statement statement = conn.createStatement();
                    statement.execute("SELECT * FROM bills_items WHERE bill_id='"+idb+"'");
                    ResultSet results = statement.getResultSet();
                    data = FXCollections.observableArrayList();
                    while (results.next()) {
                        data.add(new billview.ShowData(results.getString("item"),results.getString("quan"),results.getString("price")));
                    }
                    i.setCellValueFactory(new PropertyValueFactory("item"));
                    qn.setCellValueFactory(new PropertyValueFactory("quan"));
                    p.setCellValueFactory(new PropertyValueFactory("pay"));
                    itemslist.setItems(data);
                    itemslist.getSelectionModel().clearSelection();
                    conn.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    Statement statement = conn.createStatement();
                    statement.execute("SELECT * FROM bills WHERE id= '"+idb+"'");
                    ResultSet results = statement.getResultSet();
                    while (results.next()) {
                        bid.setText(results.getString("id"));
                        bd.setText(results.getString("dt"));
                        pd.setText(results.getString("post_dt"));
                        accd.setText(results.getString("acc_dt"));
                        sn.setText((String) suppliers.sid);
                        t.setText(results.getString("total_pay"));
                        at.setText(results.getString("added_value_tax"));
                        nt.setText(results.getString("net_pay"));
                    }
                    conn.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }});
    }

    public class ShowData {
        private final StringProperty item;
        private final StringProperty quan;
        private final StringProperty pay;


        public ShowData(String item, String quan, String pay) {
            this.item= new SimpleStringProperty(item);
            this.quan= new SimpleStringProperty(quan);
            this.pay= new SimpleStringProperty(pay);
        }


        public String getitem() { return item.get();}
        public void setitem(String value) { item.setValue(value); }
        public StringProperty itemProperty(){
            return item;
        }
        public String getquan() { return quan.get();}
        public void setquan(String value) { quan.setValue(value); }
        public StringProperty quanProperty(){
            return quan;
        }
        public String getpay() { return pay.get();}
        public void setpay(String value) { pay.setValue(value); }
        public StringProperty payProperty(){
            return pay;
        }
    }



    public void suppliers (ActionEvent event) throws IOException {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
