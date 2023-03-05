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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class journal implements Initializable {
    @FXML
    private TableColumn n;
    @FXML
    private TableColumn an;
    @FXML
    private TableColumn pa;
    @FXML
    private TableColumn cc;
    @FXML
    private TableColumn db;
    @FXML
    private TableColumn cr;
    @FXML
    private  TableColumn dt;
    @FXML
    private  TableColumn us;
    @FXML
    private  TableColumn sb;
    @FXML
    private TableView dashboard;
    @FXML
    private ComboBox filter;
    @FXML
    private Circle live;


    private double xOffset = 0.0D;
    private double yOffset = 0.0D;
    Statement statement1 ;
    Statement statement2;
    Statement statement3;
    Statement statement4;
    String accountname;
    String parentaccountname;
    String costcenter;
    Object key;
    public static  Object tid ;
    private ObservableList<journal.ShowData> data;
    Connection conn ;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filter.getItems().add("LIVE");
        filter.getItems().add("PENDING");
        filter.getSelectionModel().selectFirst();
        key="yes";
        live.setVisible(true);
        live.setFill(Paint.valueOf("#37ff21"));
        filter.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(newValue.equals("LIVE")){
                key="yes";
                live.setVisible(true);
                live.setFill(Paint.valueOf("#37ff21"));
            }else if (newValue.equals("PENDING")){
                key="no";
                live.setVisible(true);
                live.setFill(Paint.valueOf("#FFEE00"));
            }
        });

        ScheduledExecutorService executor2 = Executors.newScheduledThreadPool(1);
        executor2.scheduleAtFixedRate(tableRunnable, 0, 1, TimeUnit.SECONDS);
        dashboard.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList<TablePosition> selectedCells = dashboard.getSelectionModel().getSelectedCells() ;
        selectedCells.addListener((ListChangeListener.Change<? extends TablePosition> change) -> {
            if (selectedCells.size() > 0) {
                TablePosition selectedCell = selectedCells.get(0);
                TableColumn column = selectedCell.getTableColumn();
                if(column == sb){
                    int rowIndex = selectedCell.getRow();
                    tid = column.getCellObservableValue(rowIndex).getValue();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Parent root = null;
                            try {
                                root = FXMLLoader.load(getClass().getResource("retail/transdetails.fxml"));
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


    }



    Runnable tableRunnable = new Runnable(){
        @Override
        public void run() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                        statement1 = conn.createStatement();
                        statement1.execute("SELECT * FROM journal  WHERE  post ='" +key+ "' ");
                        ResultSet results = statement1.getResultSet();
                        data = FXCollections.observableArrayList();
                        while (results.next()) {
                            statement2 = conn.createStatement();
                            statement2.execute("SELECT * FROM acctree WHERE id='" + results.getString("acc_id") + "'");
                            ResultSet results1 = statement2.getResultSet();
                            while (results1.next()) {
                                accountname = results1.getString("title");
                            }
                            statement3 = conn.createStatement();
                            statement3.execute("SELECT * FROM acctree WHERE id='" + results.getString("parent_id") + "'");
                            ResultSet results2 = statement3.getResultSet();
                            while (results2.next()) {
                                parentaccountname = results2.getString("title");
                            }
                            statement4 = conn.createStatement();
                            statement4.execute("select * from projects WHERE id= '" + results.getString("costcenter_id") + "'");
                            ResultSet results3 = statement4.getResultSet();
                            while (results3.next()) {
                                costcenter = String.valueOf(results3.getString("name"));
                            }

                            data.add(new journal.ShowData(results.getString("num"), accountname, parentaccountname, costcenter, results.getString("debit"),results.getString("credit"), results.getString("dt"),results.getString("user"),results.getString("id")));
                        }
                        n.setCellValueFactory(new PropertyValueFactory("num"));
                        an.setCellValueFactory(new PropertyValueFactory("aac"));
                        pa.setCellValueFactory(new PropertyValueFactory("pac"));
                        cc.setCellValueFactory(new PropertyValueFactory("cc"));
                        db.setCellValueFactory(new PropertyValueFactory("db"));
                        cr.setCellValueFactory(new PropertyValueFactory("cr"));
                        dt.setCellValueFactory(new PropertyValueFactory("dt"));
                        us.setCellValueFactory(new PropertyValueFactory("us"));
                        sb.setCellValueFactory(new PropertyValueFactory("sid"));
                        dashboard.setItems(data);
                        dashboard.getSelectionModel().clearSelection();
                        conn.close();


                    } catch (Exception e) {
                        dashboard.setItems(null);
                        System.out.print(e);
                    }
                }});
        }
    };

    public void viewtransaction(){
        sb.setCellFactory(col -> {
            TableCell<Data, Object> cell = new TableCell<Data, Object>() {
                public void updateItem(Object item, boolean empty) {
                    final Label view = new Label("VIEW DETAILS ");
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
        private final StringProperty num;
        private final StringProperty aac;
        private final StringProperty pac;
        private final StringProperty cc;
        private final StringProperty db;
        private final StringProperty cr;
        private final StringProperty dt;
        private final StringProperty us;
        private final StringProperty sid;


        public ShowData(String num,String aac, String pac , String cc, String db, String cr, String dt , String us, String sid) {
            this.num = new SimpleStringProperty(num);
            this.aac= new SimpleStringProperty(aac);
            this.pac= new SimpleStringProperty(pac);
            this.cc= new SimpleStringProperty(cc);
            this.db= new SimpleStringProperty(db);
            this.cr= new SimpleStringProperty(cr);
            this.dt= new SimpleStringProperty(dt);
            this.us= new SimpleStringProperty(us);
            this.sid= new SimpleStringProperty(sid);
        }
        public String getnum() { return num.get();}
        public void setnum(String value) { num.setValue(value); }
        public StringProperty numProperty(){
            return num;
        }

        public String getaac() { return aac.get();}
        public void setaac(String value) { aac.setValue(value); }
        public StringProperty aacProperty(){
            return aac;
        }

        public String getpac() { return pac.get(); }
        public void setpac(String value) { pac.setValue(value); }
        public StringProperty pacProperty(){
            return pac;
        }

        public String getcc() { return cc.get(); }
        public void setcc(String value) { cc.setValue(value); }
        public StringProperty ccProperty(){ return cc;
        }
        public String getdb() { return db.get(); }
        public void setdb(String value) { db.setValue(value); }
        public StringProperty dbProperty(){
            return db;
        }


        public String getcr() { return cr.get(); }
        public void setcr(String value) { cr.setValue(value); }
        public StringProperty crProperty(){
            return cr;
        }


        public String getdt() { return dt.get(); }
        public void setdt(String value) { dt.setValue(value); }
        public StringProperty dtProperty(){
            return dt;
        }



        public String getus() { return us.get(); }
        public void setus(String value) { us.setValue(value); }
        public StringProperty usProperty(){
            return us;
        }



        public String getsid() { return sid.get(); }
        public void setsid(String value) { sid.setValue(value); }
        public StringProperty sidProperty(){
            return sid;
        }
    }
}
