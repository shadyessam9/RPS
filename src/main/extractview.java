package main;
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
import javafx.scene.control.*;
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

public class extractview implements Initializable {
    @FXML
    private TableView dashboard;
    @FXML
    private Label en;
    @FXML
    private Label avrn;
    @FXML
    private Label cn;
    @FXML
    private Label et;
    @FXML
    private Label pn;
    @FXML
    private Label ev;
    @FXML
    private TableColumn ws;
    @FXML
    private TableColumn unit;
    @FXML
    private TableColumn quan;
    @FXML
    private TableColumn cla;
    @FXML
    private TableColumn la;
    @FXML
    private TableColumn cur;
    @FXML
    private TableColumn tot;
    @FXML
    private TableColumn wo;
    @FXML
    private TableColumn cut;
    @FXML
    private TableColumn nw;
    @FXML
    private TableColumn no;
    private double xOffset = 0.0D;
    private double yOffset = 0.0D;
    Connection conn ;
    Statement statement0;
    Statement statement1;
    Statement statement2;
    Object idp;
    Object ide;
    private ObservableList<extractview.ShowData> data;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idp=projects.idp;
        ide=projectview.ide;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://"+ Main.ip+"/fouadcompany", "root", "");
            statement0= conn.createStatement();
            statement0.execute("SELECT * FROM extracts  WHERE  id= '"+ide+"'");
            ResultSet results0 = statement0.getResultSet();
            while (results0.next()) {
                en.setText(results0.getString("number"));
                et.setText(results0.getString("type"));
                avrn.setText(results0.getString("avrn"));
                ev.setText(String.valueOf(results0.getInt("pay")));
                statement1 = conn.createStatement();
                statement1.execute("SELECT * FROM customers WHERE id= '" + results0.getString("client_id") + "'");
                ResultSet results1 = statement1.getResultSet();
                while (results1.next()) {
                    cn.setText(results1.getString("name"));
                }
                statement2 = conn.createStatement();
                statement2.execute("SELECT * FROM projects WHERE id= '" + results0.getString("project_id") + "'");
                ResultSet results2 = statement2.getResultSet();
                while (results2.next()) {
                    pn.setText(results2.getString("name"));
                }
            }

        }catch (Exception e) {
            System.out.print(e);
        }

        try {
            conn = DriverManager.getConnection("jdbc:mysql://"+Main.ip+"/fouadcompany", "root", "");
            statement2 = conn.createStatement();
            statement2.execute("SELECT * FROM bands WHERE  extract_id= '"+ide+"' AND project_id= '"+idp+"' ");
            ResultSet results = statement2.getResultSet();
            data= FXCollections.observableArrayList();
            while (results.next()) {
                data.add(new extractview.ShowData(results.getString("statement"),results.getString("unit"),results.getString("quan"),results.getString("class"),results.getString("last"),results.getString("current"),results.getString("total"),results.getString("totalwork"),results.getString("cut"),results.getString("cutleft"),results.getString("notes")));
            }
            ws.setCellValueFactory(new PropertyValueFactory("WORKSTATEMENT"));
            unit.setCellValueFactory(new PropertyValueFactory("UNIT"));
            quan.setCellValueFactory(new PropertyValueFactory("QUANTITY"));
            cla.setCellValueFactory(new PropertyValueFactory("CLASS"));
            la.setCellValueFactory(new PropertyValueFactory("LAST"));
            cur.setCellValueFactory(new PropertyValueFactory("CURRENT"));
            tot.setCellValueFactory(new PropertyValueFactory("TOTAL"));
            wo.setCellValueFactory(new PropertyValueFactory("WORK"));
            cut.setCellValueFactory(new PropertyValueFactory("CUT"));
            nw.setCellValueFactory(new PropertyValueFactory("NETWORK"));
            no.setCellValueFactory(new PropertyValueFactory("NOTES"));
            dashboard.setItems(data);
            dashboard.getSelectionModel().clearSelection();
            conn.close();
        } catch (Exception e) {
            dashboard.setItems(null);
            System.out.print(e);
        }
    }
    public class ShowData {
        private final StringProperty WORKSTATEMENT;
        private final StringProperty UNIT;
        private final StringProperty QUANTITY;
        private final StringProperty CLASS;
        private final StringProperty LAST;
        private final StringProperty CURRENT;
        private final StringProperty TOTAL;
        private final StringProperty WORK;
        private final StringProperty CUT;
        private final StringProperty NETWORK;
        private final StringProperty NOTES;
        public ShowData(String WORKSTATEMENT,String UNIT, String QUANTITY, String CLASS,String LAST,String CURRENT, String TOTAL, String WORK,String CUT,String NETWORK, String NOTES ) {
            this.WORKSTATEMENT= new SimpleStringProperty(WORKSTATEMENT);
            this.UNIT= new SimpleStringProperty(UNIT);
            this.QUANTITY= new SimpleStringProperty(QUANTITY);
            this.CLASS= new SimpleStringProperty(CLASS);
            this.LAST= new SimpleStringProperty(LAST);
            this.CURRENT= new SimpleStringProperty(CURRENT);
            this.TOTAL= new SimpleStringProperty(TOTAL);
            this.WORK= new SimpleStringProperty(WORK);
            this.CUT= new SimpleStringProperty(CUT);
            this.NETWORK= new SimpleStringProperty(NETWORK);
            this.NOTES= new SimpleStringProperty(NOTES);
        }
        public String getWORKSTATEMENT() { return WORKSTATEMENT.get();}
        public void setWORKSTATEMENT(String value) { WORKSTATEMENT.setValue(value); }
        public StringProperty WORKSTATEMENTProperty(){ return WORKSTATEMENT; }
        public String getUNIT() { return UNIT.get();}
        public void setUNIT(String value) { UNIT.setValue(value); }
        public StringProperty UNITProperty(){ return UNIT; }
        public String getQUANTITY() { return QUANTITY.get();}
        public void setQUANTITY(String value) { QUANTITY.setValue(value); }
        public StringProperty QUANTITYProperty(){ return QUANTITY; }
        public String getCLASS() { return CLASS.get();}
        public void setCLASS(String value) { CLASS.setValue(value); }
        public StringProperty CLASSProperty(){ return CLASS; }
        public String getLAST() { return LAST.get(); }
        public void setLAST(String value) { LAST.setValue(value); }
        public StringProperty LASTProperty(){ return LAST; }
        public String getCURRENT() { return CURRENT.get();}
        public void setCURRENT(String value) { CURRENT.setValue(value); }
        public StringProperty CURRENTProperty(){ return CURRENT; }
        public String getTOTAL() { return TOTAL.get();}
        public void setTOTAL(String value) { TOTAL.setValue(value); }
        public StringProperty TOTALProperty(){ return TOTAL; }
        public String getWORK() { return WORK.get();}
        public void setWORK(String value) { WORK.setValue(value); }
        public StringProperty WORKProperty(){ return WORK; }
        public String getCUT() { return CUT.get(); }
        public void setCUT(String value) { CUT.setValue(value); }
        public StringProperty CUTProperty(){ return CUT; }
        public String getNETWORK() { return NETWORK.get();}
        public void setNETWORK(String value) { NETWORK.setValue(value); }
        public StringProperty NETWORKProperty(){ return NETWORK; }
        public String getNOTES() { return NOTES.get();}
        public void setNOTES(String value) { NOTES.setValue(value); }
        public StringProperty NOTESProperty(){return NOTES; }
    }
    public void projectview (ActionEvent event) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("retail/projectview.fxml"));
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
