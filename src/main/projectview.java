package main;
import com.mysql.fabric.xmlrpc.base.Data;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
public class projectview implements Initializable {
    @FXML
    private WebView wv;
    @FXML
    private TableView dashboard;
    @FXML
    private AnchorPane projectview;
    @FXML
    private Label sn;
    @FXML
    private Label tv;
    @FXML
    private Label ev;
    @FXML
    private Label rv;
    @FXML
    private Label pn;
    @FXML
    private Label cn;
    @FXML
    private Label cl;
    @FXML
    private Label ct;
    @FXML
    private TableColumn en;
    @FXML
    private TableColumn et;
    @FXML
    private TableColumn ep;
    @FXML
    private TableColumn bb;
    @FXML
    private ComboBox filter;
    @FXML
    private ComboBox by;
    @FXML
    private ProgressIndicator pi;
    private double xOffset = 0.0D;
    private double yOffset = 0.0D;
    Connection conn ;
    Statement statement0;
    Statement statement1;
    Statement statement2;
    Statement statement3;
    Object idp;
    public static  Object ide ;
    String lat;
    String lng;
    Object key;
    float t;
    float e;
    float r;
    private ObservableList<projectview.ShowData> data;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idp=projects.idp;
        filter.getItems().add("ALL");
        filter.getItems().add("TYPE");
        filter.getItems().add("NUMBER");
        filter.getSelectionModel().selectFirst();
        dashboard.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList<TablePosition> selectedCells = dashboard.getSelectionModel().getSelectedCells() ;
        selectedCells.addListener((ListChangeListener.Change<? extends TablePosition> change) -> {
            if (selectedCells.size() > 0) {
                TablePosition selectedCell = selectedCells.get(0);
                TableColumn column = selectedCell.getTableColumn();
                if(column == bb){
                    int rowIndex = selectedCell.getRow();
                    ide = column.getCellObservableValue(rowIndex).getValue();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Parent root = null;
                            try {
                                root = FXMLLoader.load(getClass().getResource("retail/extractview.fxml"));
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
                            projectview.getScene().getWindow().hide();
                        }});
                }
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                viewbands();
            }});
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://"+ Main.ip+"/fouadcompany", "root", "");
                    statement0= conn.createStatement();
                    statement0.execute("SELECT * FROM projects  WHERE  id= '"+idp+"'");
                    ResultSet results0 = statement0.getResultSet();
                    while (results0.next()) {
                        pn.setText(results0.getString("name"));
                        statement1 = conn.createStatement();
                        statement1.execute("SELECT * FROM sites WHERE id= '" + results0.getString("site_id") + "'");
                        ResultSet results1 = statement1.getResultSet();
                        while (results1.next()) {
                            sn.setText(results1.getString("name"));
                            lat = results1.getString("lat");
                            lng = results1.getString("lng");
                            wv.getEngine().load("https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/geojson(%7B%22type%22%3A%22Point%22%2C%22coordinates%22%3A%5B31.233334%2C30.033333%5D%7D)/31.233334,31.233334,5/300x180?access_token=pk.eyJ1Ijoic2hhZHkwMDciLCJhIjoiY2t6ZTh6MDh3Mmc4eTJwbnhhbjNnZjB3cSJ9.C99v3ZWrxQOgH0vU0pfFQA");
                            wv.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                                if (Worker.State.SUCCEEDED.equals(newValue)) {
                                    pi.setVisible(false);
                                }else {
                                    pi.progressProperty().bind(wv.getEngine().getLoadWorker().progressProperty());
                                }
                            });
                        }
                        tv.setText(String.valueOf(results0.getInt("pay")));
                        t = results0.getInt("pay");
                        statement2 = conn.createStatement();
                        statement2.execute("select sum(pay) from extracts WHERE project_id= '" +idp+ "'");
                        ResultSet results2 = statement2.getResultSet();
                        while (results2.next()) {
                            ev.setText(String.valueOf(results2.getInt("sum(pay)")));
                            e = results2.getInt("sum(pay)");
                            r = t-e ;
                            rv.setText(String.valueOf(r));
                        }
                        statement3 = conn.createStatement();
                        statement3.execute("SELECT * FROM customers WHERE id= '" +results0.getString("cust_id")+ "'");
                        ResultSet results3 = statement3.getResultSet();
                        while (results3.next()) {
                            cn.setText(results3.getString("name"));
                            cl.setText(results3.getString("adress"));
                            ct.setText(results3.getString("telephone"));
                        }
                    }
                }catch (Exception e) {
                    System.out.print(e);
                }

                try {
                    conn = DriverManager.getConnection("jdbc:mysql://"+Main.ip+"/fouadcompany", "root", "");
                    statement1 = conn.createStatement();
                    if ("ALL".equals(filter.getValue())) {
                        by.setDisable(true);
                        statement1.execute("SELECT * FROM extracts WHERE  project_id= '"+idp+"' ");
                    }
                    ResultSet results = statement1.getResultSet();
                    data= FXCollections.observableArrayList();
                    while (results.next()) {
                        data.add(new projectview.ShowData(results.getString("number"),results.getString("type"),results.getString("pay"),results.getString("id")));
                    }
                    en.setCellValueFactory(new PropertyValueFactory("number"));
                    et.setCellValueFactory(new PropertyValueFactory("type"));
                    ep.setCellValueFactory(new PropertyValueFactory("pay"));
                    bb.setCellValueFactory(new PropertyValueFactory("id"));
                    dashboard.setItems(data);
                    dashboard.getSelectionModel().clearSelection();
                    conn.close();
                } catch (Exception e) {
                    dashboard.setItems(null);
                    System.out.print(e);
                }
            }});

        filter.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if ("ALL".equals(newValue)) {
                dashboard.setItems(null);
                by.getSelectionModel().clearSelection();
                by.setDisable(true);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            conn = DriverManager.getConnection("jdbc:mysql://"+Main.ip+"/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            statement1.execute("SELECT * FROM extracts WHERE  project_id= '"+idp+"' ");
                            ResultSet results = statement1.getResultSet();
                            data= FXCollections.observableArrayList();
                            while (results.next()) {
                                data.add(new projectview.ShowData(results.getString("number"),results.getString("type"),results.getString("pay"),results.getString("id")));
                            }
                            en.setCellValueFactory(new PropertyValueFactory("number"));
                            et.setCellValueFactory(new PropertyValueFactory("type"));
                            ep.setCellValueFactory(new PropertyValueFactory("pay"));
                            bb.setCellValueFactory(new PropertyValueFactory("id"));
                            dashboard.setItems(data);
                            dashboard.getSelectionModel().clearSelection();
                            conn.close();
                        } catch (Exception e) {
                            dashboard.setItems(null);
                            System.out.print(e);
                        }
                    }});
            } else if ("TYPE".equals(newValue)) {
                dashboard.setItems(null);
                by.setDisable(false);
                by.setItems(FXCollections.observableArrayList(getData1()));
                key = (Object) by.getValue();
            } else if ("NUMBER".equals(newValue)) {
                dashboard.setItems(null);
                by.setDisable(false);
                by.setItems(FXCollections.observableArrayList(getData2()));
                key = (Object) by.getValue();
            }
        });
        by.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(by.getValue()==null){}else {
                dashboard.setItems(null);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Object value = filter.getValue();
                        try {
                            conn = DriverManager.getConnection("jdbc:mysql://"+Main.ip+"/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            if ("TYPE".equals(value)) {
                                statement1.execute("SELECT * FROM extracts  WHERE  project_id= '"+idp+"' AND type= '"+newValue+"' ");
                            } else if ("NUMBER".equals(value)) {
                                statement1.execute("SELECT * FROM extracts  WHERE  project_id= '"+idp+"' AND number= '"+newValue+"' ");
                            }
                            ResultSet results = statement1.getResultSet();
                            data= FXCollections.observableArrayList();
                            while (results.next()) {
                                data.add(new projectview.ShowData(results.getString("number"),results.getString("type"),results.getString("pay"),results.getString("id")));
                            }
                            en.setCellValueFactory(new PropertyValueFactory("number"));
                            et.setCellValueFactory(new PropertyValueFactory("type"));
                            ep.setCellValueFactory(new PropertyValueFactory("pay"));
                            bb.setCellValueFactory(new PropertyValueFactory("id"));
                            dashboard.setItems(data);
                            dashboard.getSelectionModel().clearSelection();
                            conn.close();
                        } catch (Exception e) {
                            dashboard.setItems(null);
                            System.out.print(e);
                        }
                    }});
            }
        });
    }
    private List<Object> getData1() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("select * from extracts WHERE project_id= '" +idp+ "'");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search.add(results.getString("type"));
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
            statement.execute("select * from extracts WHERE project_id= '" +idp+ "'");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search.add(results.getString("number"));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return search;
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
    public class ShowData {
        private final StringProperty number;
        private final StringProperty type;
        private final StringProperty pay;
        private final StringProperty id;
        public ShowData(String number,String type, String pay, String id ) {
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
    public void projects (ActionEvent event) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("retail/projects.fxml"));
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
