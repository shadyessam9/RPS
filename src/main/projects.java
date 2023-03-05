package main;
import com.mysql.fabric.xmlrpc.base.Data;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.io.IOException;
public class projects implements Initializable {
    @FXML
    private TableColumn pn;
    @FXML
    private TableColumn cn;
    @FXML
    private TableColumn pl;
    @FXML
    private TableColumn pen;
    @FXML
    private TableColumn pbn;
    @FXML
    private TableColumn pcp;
    @FXML
    private  TableColumn extb;
    @FXML
    private TableView dashboard;
    @FXML
    private ComboBox filter;
    @FXML
    private ComboBox by;
    @FXML
    private AnchorPane projects;
    private double xOffset = 0.0D;
    private double yOffset = 0.0D;
    Statement statement0;
    Statement statement1 ;
    Statement statement2;
    Statement statement3;
    Statement statement4;
    Statement statement5;
    Statement statement6;
    String sitename;
    String custname ;
    Object key;
    String extn;
    float extv;
    float ppy;
    String bn;
    float perc;
    public static  Object idp ;
    private ObservableList<projects.ShowData> data;
    Connection conn ;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filter.getItems().add("ALL");
        filter.getItems().add("customer");
        filter.getItems().add("location");
        filter.getItems().add("completion");


        filter.getSelectionModel().selectFirst();

        dashboard.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList<TablePosition> selectedCells = dashboard.getSelectionModel().getSelectedCells() ;
        selectedCells.addListener((ListChangeListener.Change<? extends TablePosition> change) -> {
            if (selectedCells.size() > 0) {
                TablePosition selectedCell = selectedCells.get(0);
                TableColumn column = selectedCell.getTableColumn();
                if(column == extb){
                    int rowIndex = selectedCell.getRow();
                    idp = column.getCellObservableValue(rowIndex).getValue();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Parent root = null;
                            try {
                                root = FXMLLoader.load(getClass().getResource("retail/projectview.fxml"));
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
                            projects.getScene().getWindow().hide();
                            dashboard.getSelectionModel().clearSelection();
                        }});

                }
            }
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                viewextracts();
            }});
        
        if ("ALL".equals(filter.getValue())) {
            by.setDisable(true);
            dashboard.setItems(null);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                        statement1 = conn.createStatement();
                        statement1.execute("SELECT * FROM projects");
                        ResultSet results = statement1.getResultSet();
                        data = FXCollections.observableArrayList();
                        while (results.next()) {
                            statement2 = conn.createStatement();
                            statement2.execute("SELECT * FROM sites WHERE id= '" + results.getString("site_id") + "'");
                            ResultSet results1 = statement2.getResultSet();
                            while (results1.next()) {
                                sitename = results1.getString("name");
                            }
                            statement3 = conn.createStatement();
                            statement3.execute("SELECT * FROM customers WHERE id= '" + results.getString("cust_id") + "'");
                            ResultSet results2 = statement3.getResultSet();
                            while (results2.next()) {
                                custname = results.getString("name");
                            }
                            statement4 = conn.createStatement();
                            statement4.execute("select count(*) from extracts WHERE project_id= '" + results.getString("id") + "'");
                            ResultSet results3 = statement4.getResultSet();
                            while (results3.next()) {
                                extn = String.valueOf(results3.getInt("count(*)"));
                            }
                            statement5 = conn.createStatement();
                            statement5.execute("select count(*) from bands WHERE project_id= '" + results.getString("id") + "'");
                            ResultSet results4 = statement5.getResultSet();
                            while (results4.next()) {
                                bn = String.valueOf(results4.getInt("count(*)"));
                            }
                            data.add(new ShowData(results.getString("name"), custname, sitename, String.valueOf(extn), String.valueOf(bn), String.valueOf(results.getString("complete")) + " %", results.getString("id")));
                        }
                        pn.setCellValueFactory(new PropertyValueFactory("project"));
                        cn.setCellValueFactory(new PropertyValueFactory("customer"));
                        pl.setCellValueFactory(new PropertyValueFactory("location"));
                        pen.setCellValueFactory(new PropertyValueFactory("extracts"));
                        pbn.setCellValueFactory(new PropertyValueFactory("bands"));
                        pcp.setCellValueFactory(new PropertyValueFactory("percent"));
                        extb.setCellValueFactory(new PropertyValueFactory("id"));
                        dashboard.setItems(data);
                        dashboard.getSelectionModel().clearSelection();
                        conn.close();
                    } catch (Exception e) {
                        dashboard.setItems(null);
                        System.out.print(e);
                    }
                }});
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://"+Main.ip+"/fouadcompany", "root", "");
                    statement0= conn.createStatement();
                    statement0.execute("SELECT * FROM projects");
                    ResultSet results0 = statement0.getResultSet();
                    while (results0.next()) {
                        ppy = results0.getInt("pay");
                        statement6 = conn.createStatement();
                        statement6.execute("select sum(pay) from extracts WHERE project_id= '" +results0.getString("id")+ "'");
                        ResultSet results5 = statement6.getResultSet();
                        while (results5.next()) {
                            extv = results5.getInt("sum(pay)");
                            perc = (extv/ppy)*100;
                        }
                        PreparedStatement stmt = conn.prepareStatement("UPDATE projects SET  complete=?  where  id='" + results0.getString("id") + "' ");
                        stmt.setFloat(1, perc);
                        stmt.executeUpdate();
                    }
                }catch (Exception e) {
                    System.out.print(e);
                }
            }});

        filter.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            dashboard.setItems(null);
            by.getSelectionModel().clearSelection();
            if ("ALL".equals(newValue)) {
                by.setDisable(true);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            statement1.execute("SELECT * FROM projects");
                            ResultSet results = statement1.getResultSet();
                            data = FXCollections.observableArrayList();
                            while (results.next()) {
                                statement2 = conn.createStatement();
                                statement2.execute("SELECT * FROM sites WHERE id= '" + results.getString("site_id") + "'");
                                ResultSet results1 = statement2.getResultSet();
                                while (results1.next()) {
                                    sitename = results1.getString("name");
                                }
                                statement3 = conn.createStatement();
                                statement3.execute("SELECT * FROM customers WHERE id= '" + results.getString("cust_id") + "'");
                                ResultSet results2 = statement3.getResultSet();
                                while (results2.next()) {
                                    custname = results.getString("name");
                                }
                                statement4 = conn.createStatement();
                                statement4.execute("select count(*) from extracts WHERE project_id= '" + results.getString("id") + "'");
                                ResultSet results3 = statement4.getResultSet();
                                while (results3.next()) {
                                    extn = String.valueOf(results3.getInt("count(*)"));
                                }
                                statement5 = conn.createStatement();
                                statement5.execute("select count(*) from bands WHERE project_id= '" + results.getString("id") + "'");
                                ResultSet results4 = statement5.getResultSet();
                                while (results4.next()) {
                                    bn = String.valueOf(results4.getInt("count(*)"));
                                }
                                data.add(new ShowData(results.getString("name"), custname, sitename, String.valueOf(extn), String.valueOf(bn), String.valueOf(results.getString("complete")) + " %", results.getString("id")));
                            }
                            pn.setCellValueFactory(new PropertyValueFactory("project"));
                            cn.setCellValueFactory(new PropertyValueFactory("customer"));
                            pl.setCellValueFactory(new PropertyValueFactory("location"));
                            pen.setCellValueFactory(new PropertyValueFactory("extracts"));
                            pbn.setCellValueFactory(new PropertyValueFactory("bands"));
                            pcp.setCellValueFactory(new PropertyValueFactory("percent"));
                            extb.setCellValueFactory(new PropertyValueFactory("id"));
                            dashboard.setItems(data);
                            dashboard.getSelectionModel().clearSelection();
                            conn.close();
                        } catch (Exception e) {
                            dashboard.setItems(null);
                            System.out.print(e);
                        }
                    }});
            }  else if ("customer".equals(newValue)) {
                by.getSelectionModel().clearSelection();
                dashboard.setItems(null);
                by.setDisable(false);
                by.setItems(FXCollections.observableArrayList(getData2()));
                key = (Object) by.getValue();
            } else if ("location".equals(newValue)) {
                by.getSelectionModel().clearSelection();
                dashboard.setItems(null);
                by.setDisable(false);
                by.setItems(FXCollections.observableArrayList(getData3()));
                key = (Object) by.getValue();
            } else if ("completion".equals(newValue)) {
                dashboard.setItems(null);
                by.setDisable(false);
                by.setItems(FXCollections.observableArrayList(getData4()));
                key = (Object) by.getValue();
            }
        });

        by.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            dashboard.setItems(null);
            if(by.getValue()==null){}else{
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Object value = filter.getValue();
                        try {
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            if ("customer".equals(value)) {
                                statement1.execute("SELECT * FROM projects WHERE  cust_id= '" + String.valueOf(newValue).replaceAll("[^0-9]", "")+ "'");
                            } else if ("location".equals(value)) {
                                statement1.execute("SELECT * FROM projects WHERE  site_id= '" + String.valueOf(newValue).replaceAll("[^0-9]", "")+ "'");
                            } else if ("completion".equals(value)) {
                                statement1.execute("SELECT * FROM projects WHERE  complete <='" + String.valueOf(newValue).replaceAll("[^0-9]", "")+ "'");
                            }
                            ResultSet results = statement1.getResultSet();
                            data = FXCollections.observableArrayList();
                            while (results.next()) {
                                statement2 = conn.createStatement();
                                statement2.execute("SELECT * FROM sites WHERE id= '" + results.getString("site_id") + "'");
                                ResultSet results1 = statement2.getResultSet();
                                while (results1.next()) {
                                    sitename = results1.getString("name");
                                }
                                statement3 = conn.createStatement();
                                statement3.execute("SELECT * FROM customers WHERE id= '" + results.getString("cust_id") + "'");
                                ResultSet results2 = statement3.getResultSet();
                                while (results2.next()) {
                                    custname = results.getString("name");
                                }
                                statement4 = conn.createStatement();
                                statement4.execute("select count(*) from extracts WHERE project_id= '" + results.getString("id") + "'");
                                ResultSet results3 = statement4.getResultSet();
                                while (results3.next()) {
                                    extn = String.valueOf(results3.getInt("count(*)"));
                                }
                                statement5 = conn.createStatement();
                                statement5.execute("select count(*) from bands WHERE project_id= '" + results.getString("id") + "'");
                                ResultSet results4 = statement5.getResultSet();
                                while (results4.next()) {
                                    bn = String.valueOf(results4.getInt("count(*)"));
                                }
                                data.add(new ShowData(results.getString("name"), custname, sitename, String.valueOf(extn), String.valueOf(bn), String.valueOf(results.getString("complete")) + " %", results.getString("id")));
                            }
                            pn.setCellValueFactory(new PropertyValueFactory("project"));
                            cn.setCellValueFactory(new PropertyValueFactory("customer"));
                            pl.setCellValueFactory(new PropertyValueFactory("location"));
                            pen.setCellValueFactory(new PropertyValueFactory("extracts"));
                            pbn.setCellValueFactory(new PropertyValueFactory("bands"));
                            pcp.setCellValueFactory(new PropertyValueFactory("percent"));
                            extb.setCellValueFactory(new PropertyValueFactory("id"));
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
    public void viewextracts(){
        extb.setCellFactory(col -> {
            TableCell<Data, Object> cell = new TableCell<Data, Object>() {
                public void updateItem(Object item, boolean empty) {
                    final Label view = new Label("VIEW PROJECT");
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
    private List<Object> getData1() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM projects");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search.add(Integer.parseInt(results.getString("id"))+":"+results.getString("name"));
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
            statement.execute("SELECT * FROM customers");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search.add(Integer.parseInt(results.getString("id"))+":"+results.getString("name"));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return search;
    }
    private List<Object> getData3() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM sites");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search.add(Integer.parseInt(results.getString("id"))+":"+results.getString("name"));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return search;
    }
    private List<Object> getData4() {
        List<Object> search = new ArrayList<>();
        search.add(10);
        search.add(20);
        search.add(30);
        search.add(40);
        search.add(50);
        search.add(60);
        search.add(70);
        search.add(80);
        search.add(90);
        search.add(100);
        return search;
    }

    public class ShowData {
        private final StringProperty project;
        private final StringProperty customer;
        private final StringProperty location;
        private final StringProperty extracts;
        private final StringProperty bands;
        private final StringProperty percent;
        private final StringProperty id;
        public ShowData(String project,String customer, String location, String extracts , String bands, String percent, String id) {
            this.project = new SimpleStringProperty(project);
            this.customer= new SimpleStringProperty(customer);
            this.location= new SimpleStringProperty(location);
            this.extracts= new SimpleStringProperty(extracts);
            this.bands= new SimpleStringProperty(bands);
            this.percent= new SimpleStringProperty(percent);
            this.id= new SimpleStringProperty(id);
        }
        public String getproject() { return project.get();}
        public void setproject(String value) { project.setValue(value); }
        public StringProperty projectProperty(){
            return project;
        }
        public String getcustomer() { return customer.get();}
        public void setcustomer(String value) { customer.setValue(value); }
        public StringProperty customerProperty(){
            return customer;
        }
        public String getlocation() { return location.get();}
        public void setlocation(String value) { location.setValue(value); }
        public StringProperty locationProperty(){
            return location;
        }
        public String getextracts() { return extracts.get(); }
        public void setextracts(String value) { extracts.setValue(value); }
        public StringProperty extractsProperty(){
            return extracts;
        }
        public String getbands() { return bands.get(); }
        public void setbands(String value) { bands.setValue(value); }
        public StringProperty bandsProperty(){ return bands;
        }
        public String getpercent() { return percent.get(); }
        public void setpercent(String value) { percent.setValue(value); }
        public StringProperty percentProperty(){
            return percent;
        }
        public String getid() { return id.get(); }
        public void setid(String value) { id.setValue(value); }
        public StringProperty idProperty(){
            return id;
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
