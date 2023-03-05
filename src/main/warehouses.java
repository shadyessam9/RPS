package main;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class warehouses implements Initializable {

    @FXML
    private TableView dashboard1;
    @FXML
    private TableColumn src;
    @FXML
    private TableColumn pn;
    @FXML
    private TableColumn whn;
    @FXML
    private TableColumn it2;
    @FXML
    private TableColumn u2;
    @FXML
    private TableColumn quan2;
    @FXML
    private TableColumn t;
    @FXML
    private TableView dashboard2;
    @FXML
    private TableColumn it1;
    @FXML
    private  TableColumn u1;
    @FXML
    private  TableColumn quan1;
    @FXML
    private  TableColumn price1;
    @FXML
    private  TableColumn price2;
    @FXML
    private Label whnum;
    @FXML
    private Label itnum;
    @FXML
    private Label nh;
    @FXML
    private Label in;
    @FXML
    private Label out;
    @FXML
    private ComboBox whs;
    @FXML
    private ComboBox type;


    Statement statement1 ;
    Statement statement2;
    Statement statement3;
    String warehouse;
    String project;
    String  wn;
    String itn;
    String  th;
    String  i;
    String  o;
    String date;


    private double xOffset = 0.0D;
    private double yOffset = 0.0D;

    private ObservableList<warehouses.ShowData1> data1;
    private ObservableList<warehouses.ShowData2> data2;
    Connection conn ;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateTimeFormatter dtr = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        date = dtr.format(now);
       whs.setItems(FXCollections.observableArrayList(getData1()));
       whs.getItems().add(0,"ALL");
       type.getItems().add("OUT");
        type.getItems().add("IN");
        whs.getSelectionModel().select("ALL");
        type.getSelectionModel().select("IN");
        ScheduledExecutorService executor2 = Executors.newScheduledThreadPool(1);
        executor2.scheduleAtFixedRate(tableRunnable1, 0, 1, TimeUnit.SECONDS);
        ScheduledExecutorService executor1 = Executors.newScheduledThreadPool(1);
        executor1.scheduleAtFixedRate(tableRunnable2, 0, 1, TimeUnit.SECONDS);
        ScheduledExecutorService executor3 = Executors.newScheduledThreadPool(1);
        executor3.scheduleAtFixedRate(dataRunnable, 0, 1, TimeUnit.SECONDS);
        ScheduledExecutorService executor4 = Executors.newScheduledThreadPool(1);
        executor4.scheduleAtFixedRate(uiRunnable, 0, 1, TimeUnit.SECONDS);
    }

    private List<Object> getData1() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM warehouses");
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


    Runnable tableRunnable1 = new Runnable(){
        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(type.getValue().equals("IN")){
                        if(whs.getValue().equals("ALL")){
                            try {
                                conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                                statement1 = conn.createStatement();
                                statement1.execute("SELECT * FROM warehouse_operations  WHERE  dt ='" +date+ "' AND type='bill' ");
                                ResultSet results = statement1.getResultSet();
                                data1 = FXCollections.observableArrayList();
                                while (results.next()) {
                                    statement2 = conn.createStatement();
                                    statement2.execute("SELECT * FROM projects WHERE id='" + results.getString("project_id") + "'");
                                    ResultSet results1 = statement2.getResultSet();
                                    while (results1.next()) {
                                        project = results1.getString("name");
                                    }
                                    statement3 = conn.createStatement();
                                    statement3.execute("SELECT * FROM warehouses WHERE id='"+ results.getString("wh_id")+"'");
                                    ResultSet results2 = statement3.getResultSet();
                                    while (results2.next()) {
                                        warehouse = results2.getString("name");
                                    }
                                    data1.add(new warehouses.ShowData1(results.getString("bill_id"), project, warehouse, results.getString("item"), results.getString("price"),results.getString("quan"), results.getString("total")));
                                }
                                src.setCellValueFactory(new PropertyValueFactory("id"));
                                pn.setCellValueFactory(new PropertyValueFactory("project"));
                                whn.setCellValueFactory(new PropertyValueFactory("wh"));
                                price2.setCellValueFactory(new PropertyValueFactory("price"));
                                it2.setCellValueFactory(new PropertyValueFactory("item"));
                                quan2.setCellValueFactory(new PropertyValueFactory("quan"));
                                t.setCellValueFactory(new PropertyValueFactory("total"));
                                dashboard1.setItems(data1);
                                dashboard1.getSelectionModel().clearSelection();
                                conn.close();


                            } catch (Exception e) {
                                dashboard1.setItems(null);
                                System.out.print(e);
                            }
                        }else{
                            try {
                                conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                                statement1 = conn.createStatement();
                                statement1.execute("SELECT * FROM warehouse_operations  WHERE  dt ='" +date+ "' AND wh_id='"+String.valueOf(whs.getValue()).replaceAll("[^0-9]", "")+"' AND type='bill' ");
                                ResultSet results = statement1.getResultSet();
                                data1 = FXCollections.observableArrayList();
                                while (results.next()) {
                                    statement2 = conn.createStatement();
                                    statement2.execute("SELECT * FROM projects WHERE id='" + results.getString("project_id") + "'");
                                    ResultSet results1 = statement2.getResultSet();
                                    while (results1.next()) {
                                        project = results1.getString("name");
                                    }
                                    statement3 = conn.createStatement();
                                    statement3.execute("SELECT * FROM warehouses WHERE id='" + results.getString("wh_id") + "'");
                                    ResultSet results2 = statement3.getResultSet();
                                    while (results2.next()) {
                                        warehouse = results2.getString("name");
                                    }

                                    data1.add(new warehouses.ShowData1(results.getString("bill_id"), project, warehouse, results.getString("item"),results.getString("price"),results.getString("quan"), results.getString("total")));
                                }
                                src.setCellValueFactory(new PropertyValueFactory("id"));
                                pn.setCellValueFactory(new PropertyValueFactory("project"));
                                whn.setCellValueFactory(new PropertyValueFactory("wh"));
                                price2.setCellValueFactory(new PropertyValueFactory("price"));
                                it2.setCellValueFactory(new PropertyValueFactory("item"));
                                quan2.setCellValueFactory(new PropertyValueFactory("quan"));
                                t.setCellValueFactory(new PropertyValueFactory("total"));
                                dashboard1.setItems(data1);
                                dashboard1.getSelectionModel().clearSelection();
                                conn.close();


                            } catch (Exception e) {
                                dashboard1.setItems(null);
                                System.out.print(e);
                            }
                        }
                    }else if(type.getValue().equals("OUT")){
                        if(whs.getValue().equals("ALL")){
                            try {
                                conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                                statement1 = conn.createStatement();
                                statement1.execute("SELECT * FROM warehouse_operations  WHERE  dt ='" +date+ "' AND type='extract' ");
                                ResultSet results = statement1.getResultSet();
                                data1 = FXCollections.observableArrayList();
                                while (results.next()) {
                                    statement2 = conn.createStatement();
                                    statement2.execute("SELECT * FROM projects WHERE id='" + results.getString("project_id") + "'");
                                    ResultSet results1 = statement2.getResultSet();
                                    while (results1.next()) {
                                        project = results1.getString("name");
                                    }
                                    statement3 = conn.createStatement();
                                    statement3.execute("SELECT * FROM warehouses WHERE id='" + results.getString("wh_id") + "'");
                                    ResultSet results2 = statement3.getResultSet();
                                    while (results2.next()) {
                                        warehouse = results2.getString("name");
                                    }

                                    data1.add(new warehouses.ShowData1(results.getString("extract_id"), project, warehouse, results.getString("item"),results.getString("price"),results.getString("quan"), results.getString("total")));
                                }
                                src.setCellValueFactory(new PropertyValueFactory("id"));
                                pn.setCellValueFactory(new PropertyValueFactory("project"));
                                whn.setCellValueFactory(new PropertyValueFactory("wh"));
                                price2.setCellValueFactory(new PropertyValueFactory("price"));
                                it2.setCellValueFactory(new PropertyValueFactory("item"));
                                quan2.setCellValueFactory(new PropertyValueFactory("quan"));
                                t.setCellValueFactory(new PropertyValueFactory("total"));
                                dashboard1.setItems(data1);
                                dashboard1.getSelectionModel().clearSelection();
                                conn.close();


                            } catch (Exception e) {
                                dashboard1.setItems(null);
                                System.out.print(e);
                            }
                        }else{
                            try {
                                conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                                statement1 = conn.createStatement();
                                statement1.execute("SELECT * FROM warehouse_operations  WHERE  dt ='" +date+ "' AND wh_id='"+String.valueOf(whs.getValue()).replaceAll("[^0-9]", "")+"' AND type='extract' ");
                                ResultSet results = statement1.getResultSet();
                                data1 = FXCollections.observableArrayList();
                                while (results.next()) {
                                    statement2 = conn.createStatement();
                                    statement2.execute("SELECT * FROM projects WHERE id='" + results.getString("project_id") + "'");
                                    ResultSet results1 = statement2.getResultSet();
                                    while (results1.next()) {
                                        project = results1.getString("name");
                                    }
                                    statement3 = conn.createStatement();
                                    statement3.execute("SELECT * FROM warehouses WHERE id='" + results.getString("wh_id") + "'");
                                    ResultSet results2 = statement3.getResultSet();
                                    while (results2.next()) {
                                        warehouse = results2.getString("name");
                                    }

                                    data1.add(new warehouses.ShowData1(results.getString("extract_id"), project, warehouse, results.getString("item"), results.getString("price"),results.getString("quan"), results.getString("total")));
                                }
                                src.setCellValueFactory(new PropertyValueFactory("id"));
                                pn.setCellValueFactory(new PropertyValueFactory("project"));
                                whn.setCellValueFactory(new PropertyValueFactory("wh"));
                                price2.setCellValueFactory(new PropertyValueFactory("price"));
                                it2.setCellValueFactory(new PropertyValueFactory("item"));
                                quan2.setCellValueFactory(new PropertyValueFactory("quan"));
                                t.setCellValueFactory(new PropertyValueFactory("total"));
                                dashboard1.setItems(data1);
                                dashboard1.getSelectionModel().clearSelection();
                                conn.close();


                            } catch (Exception e) {
                                dashboard1.setItems(null);
                                System.out.print(e);
                            }
                        }
                    }
                }});
        }
    };


    Runnable tableRunnable2 = new Runnable(){
        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if(whs.getValue().equals("ALL")){
                        try {
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            statement1.execute("SELECT * FROM warehouse_items  GROUP BY item_id ");
                            ResultSet results = statement1.getResultSet();
                            data2 = FXCollections.observableArrayList();
                            while (results.next()) {
                                data2.add(new warehouses.ShowData2( results.getString("item"),results.getString("unit"),results.getString("quan"), results.getString("price")));
                            }
                            it1.setCellValueFactory(new PropertyValueFactory("item"));
                            u1.setCellValueFactory(new PropertyValueFactory("unit"));
                            quan1.setCellValueFactory(new PropertyValueFactory("quan"));
                            price1.setCellValueFactory(new PropertyValueFactory("price"));
                            dashboard2.setItems(data2);
                            dashboard2.getSelectionModel().clearSelection();
                            conn.close();


                        } catch (Exception e) {
                            dashboard1.setItems(null);
                            System.out.print(e);
                        }
                    }else{
                        try {
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            statement1.execute("SELECT * FROM warehouse_items  WHERE wh_id='"+String.valueOf(whs.getValue()).replaceAll("[^0-9]", "")+"' AND type='bill' GROUP BY item_id ");
                            ResultSet results = statement1.getResultSet();
                            data2 = FXCollections.observableArrayList();
                            while (results.next()) {
                                data2.add(new warehouses.ShowData2( results.getString("item"),results.getString("unit"),results.getString("quan"), results.getString("price")));
                            }
                            it1.setCellValueFactory(new PropertyValueFactory("item"));
                            u1.setCellValueFactory(new PropertyValueFactory("unit"));
                            quan1.setCellValueFactory(new PropertyValueFactory("quan"));
                            price1.setCellValueFactory(new PropertyValueFactory("price"));
                            dashboard2.setItems(data2);
                            dashboard2.getSelectionModel().clearSelection();
                            conn.close();


                        } catch (Exception e) {
                            dashboard1.setItems(null);
                            System.out.print(e);
                        }
                    }
                }});
        }
    };


    Runnable dataRunnable = new Runnable() {
        public void run() {
            if(whs.getValue().equals("ALL")){
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    Statement statement = conn.createStatement();
                    statement.execute("select count(*) from warehouses");
                    ResultSet results = statement.getResultSet();
                    while (results.next()) {
                       wn=String.valueOf(results.getInt("count(*)"));
                    }
                } catch (Exception e) { }
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    Statement statement = conn.createStatement();
                    statement.execute("select count(*) from warehouse_items ");
                    ResultSet results = statement.getResultSet();
                    while (results.next()) {
                        itn=String.valueOf(results.getInt("count(*)"));
                    }
                } catch (Exception e) { }
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    Statement statement = conn.createStatement();
                    statement.execute("Select SUM(`price` * quan) as sumload from warehouse_items  ");
                    ResultSet results = statement.getResultSet();
                    while (results.next()) {
                        th=String.valueOf(results.getInt("sumload"));
                    }
                } catch (Exception e) { }
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    Statement statement = conn.createStatement();
                    statement.execute("Select Sum(price) as sumload from warehouse_items WHERE type='bill' ");
                    ResultSet results = statement.getResultSet();
                    while (results.next()) {
                        i=String.valueOf(results.getInt("sumload"));
                    }
                } catch (Exception e) { }
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    Statement statement = conn.createStatement();
                    statement.execute("Select Sum(price) as sumload from warehouse_items WHERE type='extract' ");
                    ResultSet results = statement.getResultSet();
                    while (results.next()) {
                        o=String.valueOf(results.getInt("sumload"));
                    }
                } catch (Exception e) { }
            }else {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    Statement statement = conn.createStatement();
                    statement.execute("select count(*) from warehouses WHERE wh_id='"+String.valueOf(whs.getValue()).replaceAll("[^0-9]", "")+"' ");
                    ResultSet results = statement.getResultSet();
                    while (results.next()) {
                        wn=String.valueOf(results.getInt("count(*)"));
                    }
                } catch (Exception e) { }
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    Statement statement = conn.createStatement();
                    statement.execute("select count(*) warehouse_items WHERE wh_id='"+String.valueOf(whs.getValue()).replaceAll("[^0-9]", "")+"' ");
                    ResultSet results = statement.getResultSet();
                    while (results.next()) {
                        itn=String.valueOf(results.getInt("count(*)"));
                    }
                } catch (Exception e) { }
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    Statement statement = conn.createStatement();
                    statement.execute("Select SUM(`price` * quan) as sumload from warehouse_items WHERE wh_id='"+String.valueOf(whs.getValue()).replaceAll("[^0-9]", "")+"' ");
                    ResultSet results = statement.getResultSet();
                    while (results.next()) {
                        th=String.valueOf(results.getInt("sumload"));
                    }
                } catch (Exception e) { }
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    Statement statement = conn.createStatement();
                    statement.execute("Select Sum(price) as sumload from warehouse_items WHERE type='bill' AND wh_id='"+String.valueOf(whs.getValue()).replaceAll("[^0-9]", "")+"'");
                    ResultSet results = statement.getResultSet();
                    while (results.next()) {
                        i=String.valueOf(results.getInt("sumload"));
                    }
                } catch (Exception e) { }
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    Statement statement = conn.createStatement();
                    statement.execute("Select Sum(price) as sumload from warehouse_items WHERE type='extract' AND wh_id='"+String.valueOf(whs.getValue()).replaceAll("[^0-9]", "")+"'");
                    ResultSet results = statement.getResultSet();
                    while (results.next()) {
                        o=String.valueOf(results.getInt("sumload"));
                        conn.close();
                    }
                } catch (Exception e) { }
            }

        }};

    Runnable uiRunnable = new Runnable() {
        public void run() {
            try {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        whnum.setText(wn);
                        itnum.setText(itn);
                        nh.setText(th+"$");
                        in.setText(i);
                        out.setText(o);
                    }
                });
            } catch (Exception e) {

            }
        }
    };


    public class ShowData1 {
        private final StringProperty id;
        private final StringProperty project;
        private final StringProperty wh;
        private final StringProperty item;
        private final StringProperty price;
        private final StringProperty quan;
        private final StringProperty total;





        public ShowData1(String id,String project,String wh, String item , String price, String quan, String total) {
            this.id = new SimpleStringProperty(id);
            this.project= new SimpleStringProperty(project);
            this.wh= new SimpleStringProperty(wh);
            this.item= new SimpleStringProperty(item);
            this.price= new SimpleStringProperty(price);
            this.quan= new SimpleStringProperty(quan);
            this.total= new SimpleStringProperty(total);


        }
        public String getid() { return id.get();}
        public void setid(String value) { id.setValue(value); }
        public StringProperty idProperty(){
            return id;
        }

        public String getproject() { return project.get();}
        public void setproject(String value) { project.setValue(value); }
        public StringProperty projectProperty(){
            return project;
        }

        public String getwh() { return wh.get(); }
        public void setwh(String value) { wh.setValue(value); }
        public StringProperty whProperty(){
            return wh;
        }

        public String getitem() { return item.get(); }
        public void setitem(String value) { item.setValue(value); }
        public StringProperty itemProperty(){ return item;
        }
        public String getprice() { return price.get(); }
        public void setprice(String value) { price.setValue(value); }
        public StringProperty priceProperty(){
            return price;
        }


        public String getquan() { return quan.get(); }
        public void setquan(String value) { quan.setValue(value); }
        public StringProperty quanProperty(){
            return quan;
        }


        public String gettotal() { return total.get(); }
        public void settotal(String value) { total.setValue(value); }
        public StringProperty totalProperty(){
            return total;
        }



    }

    public class ShowData2 {
        private final StringProperty item;
        private final StringProperty unit;
        private final StringProperty quan;
        private final StringProperty price;

        public ShowData2( String item , String unit, String quan, String price) {

            this.item= new SimpleStringProperty(item);
            this.unit= new SimpleStringProperty(unit);
            this.quan= new SimpleStringProperty(quan);
            this.price= new SimpleStringProperty(price);


        }


        public String getitem() { return item.get(); }
        public void setitem(String value) { item.setValue(value); }
        public StringProperty itemProperty(){ return item;
        }
        public String getunit() { return unit.get(); }
        public void setunit(String value) { unit.setValue(value); }
        public StringProperty unitProperty(){
            return unit;
        }


        public String getquan() { return quan.get(); }
        public void setquan(String value) { quan.setValue(value); }
        public StringProperty quanProperty(){
            return quan;
        }


        public String getprice() { return price.get(); }
        public void setprice(String value) { price.setValue(value); }
        public StringProperty priceProperty(){
            return price;
        }



    }

    public void home (ActionEvent event) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("home.fxml"));
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
        //   stage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/c1.png")));
        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

}
