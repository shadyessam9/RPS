package main;

import com.itextpdf.text.DocumentException;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class addextract implements Initializable {

    @FXML
    private Label extractid;
    @FXML
    private Label bandid;
    @FXML
    private Label extractnumber;
    @FXML
    private DatePicker date1;
    @FXML
    private DatePicker date2;
    @FXML
    private ComboBox extracttype;
    @FXML
    private ComboBox project;
    @FXML
    private ComboBox toaccount;
    @FXML
    private ComboBox site;
    @FXML
    private TextField avrn;
    @FXML
    private TextField addedvalue;
    @FXML
    private TextField nettotal;
    @FXML
    private ComboBox bandtype;
    @FXML
    private ComboBox bandname;
    @FXML
    private ComboBox bandunit;
    @FXML
    private ComboBox item;
    @FXML
    private TextField price1;
    @FXML
    private TextField quan1;
    @FXML
    private TextField bandquantity;
    @FXML
    private TextField bandclass;
    @FXML
    private TextField bandlastvalue;
    @FXML
    private TextField bandcurrentvalue;
    @FXML
    private TextField bandtotalvalue;
    @FXML
    private TextField bandtotalwork;
    @FXML
    private TextField cut;
    @FXML
    private TextField insurance;
    @FXML
    private TextField left;
    @FXML
    private TextField statement;
    @FXML
    private AnchorPane apane;


    String extid;
    String entryid;
    String banid;
    String date;
    String key;
    String parentid;
    String insacid;
    String customer;
    String warehouse;
    float extnumber;
    double av;
    double cut1;
    double mtotal;
    double cut2;
    double perc;
    double insv;
    double t=0;
    private double xOffset = 0.0D;
    private double yOffset = 0.0D;

    DateTimeFormatter dtr1 = DateTimeFormatter.ofPattern("mm-ss-yyyy");
    LocalDateTime now1 = LocalDateTime.now();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        DateTimeFormatter dtr = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        date = dtr.format(now);


        extid ="2"+dtr1.format(now1).replace("-", "");
        extractid.setText(extid);



        extracttype.setItems(FXCollections.observableArrayList(getData1()));
        project.setItems(FXCollections.observableArrayList(getData2()));
        toaccount.setItems(FXCollections.observableArrayList(getData4()));
        bandunit.setItems(FXCollections.observableArrayList(getData5()));
        bandname.setItems(FXCollections.observableArrayList(getData6()));
        bandtype.setItems(FXCollections.observableArrayList(getData7()));
        bandname.getItems().add(0,"NEW BAND");
        item.setItems(FXCollections.observableArrayList(getData8()));




        bandtype.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
         key= (String) newValue;
        });

        bandname.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(newValue.equals("NEW BAND")){
                banid = String.valueOf(Integer.parseInt("3"+dtr1.format(now1).replace("-", "")));
                bandlastvalue.setText("0");
            }else{
                try {
                    Connection conn  = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    Statement statement = conn.createStatement();
                    statement.execute("SELECT * FROM bands WHERE id='"+String.valueOf(newValue).replaceAll("[^0-9]", "")+"' AND project_id='"+String.valueOf(project.getValue()).replaceAll("[^0-9]", "")+"' AND num > 0 ORDER BY num DESC LIMIT 1 ");
                    ResultSet results = statement.getResultSet();
                    while (results.next()) {
                        bandlastvalue.setText(results.getString("total"));
                        bandunit.setValue(results.getString("unit"));
                        bandclass.setText(results.getString("class"));
                        bandquantity.setText(results.getString("quan"));
                        banid =String.valueOf(results.getString("id"));
                    }
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        project.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                Statement statement = conn.createStatement();
                statement.execute("SELECT * FROM projects WHERE id='"+String.valueOf(newValue).replaceAll("[^0-9]", "")+"'");
                ResultSet results = statement.getResultSet();
                while (results.next()) {
                    customer= results.getString("cust_id");
                }
                conn.close();
                site.setItems(FXCollections.observableArrayList(getData3()));
            } catch (Exception e) {
                System.out.println(e);
            }
        });


        bandcurrentvalue.textProperty().addListener((observable, oldValue, newValue) -> {
            bandtotalvalue.setText(String.valueOf(Double.parseDouble(newValue)+Double.parseDouble(bandlastvalue.getText())));
        });

        bandtotalvalue.textProperty().addListener((observable, oldValue, newValue) -> {
            bandtotalwork.setText(String.valueOf(Double.parseDouble(newValue)*Double.parseDouble(bandclass.getText())));
        });

          cut.textProperty().addListener((observable, oldValue, newValue) -> {
              perc=Double.parseDouble(newValue)/100;
              cut1=Double.parseDouble(bandtotalwork.getText())-(Double.parseDouble(bandtotalwork.getText())*perc);
           });

        insurance.textProperty().addListener((observable, oldValue, newValue) -> {
            perc=Double.parseDouble(newValue)/100;
            cut2=cut1-(cut1*perc);
            System.out.println(cut2);
            left.setText(String.valueOf(Double.parseDouble(bandtotalwork.getText())-(cut1+cut2)));
            insv=Double.parseDouble(left.getText())-(Double.parseDouble(left.getText())*perc);
        });

        item.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                Statement statement = conn.createStatement();
                statement.execute("SELECT * FROM bills_items WHERE item_id='"+String.valueOf(newValue).replaceAll("[^0-9]", "")+"'");
                ResultSet results = statement.getResultSet();
                while (results.next()) {
                    price1.setText(String.valueOf(results.getInt("price")));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

    }
    private List<Object> getData1() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM extractinfo");
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
            statement.execute("SELECT * FROM projects");
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
    private List<Object> getData3() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM project_sites WHERE project_id='"+String.valueOf(project.getValue()).replaceAll("[^0-9]", "")+"'");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                try {
                    Statement statement1 = conn.createStatement();
                    statement1.execute("SELECT * FROM sites WHERE id='"+results.getString("site_id")+"'");
                    ResultSet results1 = statement1.getResultSet();
                    while (results1.next()) {
                        search.add(results1.getString("name")+":"+results1.getString("id"));
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return search;
    }
    private List<Object> getData4() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM acctree WHERE parent_id='300'");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search.add(results.getString("title")+":"+results.getString("id"));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return search;
    }
    private List<Object> getData5() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM extractinfo");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search.add(results.getString("unit"));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return search;
    }
    private List<Object> getData6() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM bands ");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search.add(results.getString("statement")+":"+results.getString("id"));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return search;
    }
    private List<Object> getData7() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM band_types");
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
    private List<Object> getData8() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM items");
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

    public void send (ActionEvent event) throws IOException, DocumentException {
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO bands (project_id,costcenter_id,extract_id, id , statement , unit, quan, class, last, current, total, totalwork, cut, cutleft, notes, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, String.valueOf(project.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(2, String.valueOf(project.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(3, extid);
            stmt.setString(4, banid);
            stmt.setString(5, statement.getText());
            stmt.setString(6, (String) bandunit.getValue());
            stmt.setString(7,  bandquantity.getText());
            stmt.setString(8, bandclass.getText());
            stmt.setString(9,  bandlastvalue.getText() );
            stmt.setString(10, bandcurrentvalue.getText());
            stmt.setString(11, bandtotalvalue.getText());
            stmt.setString(12, bandtotalwork.getText());
            stmt.setString(13, cut.getText());
            stmt.setDouble(14, Double.parseDouble(left.getText()));
            stmt.setString(15, "'"+String.valueOf(project.getValue()).replaceAll("[^A-Za-z]+", "")+"' PROJECT EXTRACT '"+extractnumber.getText()+"'");
            stmt.setString(16, (String) bandtype.getValue());
            stmt.executeUpdate();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        av=Double.parseDouble(left.getText())*Double.parseDouble(addedvalue.getText());
        bandtotalwork.setText(String.valueOf((Double.parseDouble(left.getText())+av)));
        extnumber=extnumber+1;
        extractnumber.setText(String.valueOf(extnumber));
        t=t+Double.parseDouble(left.getText());
        nettotal.setText(String.valueOf(t));
}


    public void items (ActionEvent event) throws IOException, DocumentException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM warehouses WHERE site_id='"+ String.valueOf(site.getValue()).replaceAll("[^0-9]", "")+"'");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
               warehouse=results.getString("id");
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://"+Main.ip+"/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("UPDATE warehouse_items SET quan = quan -'" +Integer.parseInt((quan1.getText()))+"' WHERE item_id='"+String.valueOf(item.getValue()).replaceAll("[^0-9]", "")+"'");
            stmt.execute();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO warehouse_operations (extract_id, project_id, wh_id, item_id, item, price, quan,total, type, dt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, extid);
            stmt.setString(2, String.valueOf(project.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(3, warehouse);
            stmt.setString(4, String.valueOf(item.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(5, (String) item.getValue());
            stmt.setInt(6, Integer.parseInt(price1.getText()));
            stmt.setInt(7, Integer.parseInt(quan1.getText()));
            mtotal=(Integer.parseInt((price1.getText()))*Integer.parseInt((quan1.getText())));
            stmt.setString(8, String.valueOf(mtotal));
            stmt.setString(9, "extract");
            stmt.setString(10, date);
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void post (ActionEvent event) throws IOException, DocumentException {
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO extracts (id, number, type , pay, project_id, costcenter_id, client_id, avrn, dt, acc_dt, user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, extid);
            stmt.setInt(2, (int) extnumber);
            stmt.setString(3, (String) extracttype.getValue());
            stmt.setInt(4, (int) Double.parseDouble(nettotal.getText()));
            stmt.setString(5, String.valueOf(project.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(6, String.valueOf(project.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(7, customer);
            stmt.setString(8, avrn.getText());
            stmt.setString(9, String.valueOf(date1.getValue()));
            stmt.setString(10,String.valueOf(date2.getValue()));
            stmt.setString(11,"a");
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            entryid = "10"+dtr1.format(now1).replace("-", "");
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO journal (id, acc_id , asc_id, parent_id, costcenter_id, debit, credit, notes, post_dt, dt, user, post, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, String.valueOf((Integer.valueOf(entryid)+1)));
            stmt.setString(2, String.valueOf(customer));
            stmt.setString(3, extid);
             try {
                 Statement statement = conn.createStatement();
                 statement.execute("SELECT * FROM acctree WHERE id='"+customer+"'");
                 ResultSet results = statement.getResultSet();while (results.next()){
                    parentid=results.getString("parent_id");

                 }
              } catch (SQLException throwables) {
                   throwables.printStackTrace();
               }
            stmt.setString(4, parentid);
            stmt.setString(5, String.valueOf(project.getValue()).replaceAll("[^0-9]", ""));
            stmt.setInt(6, 0);
            stmt.setInt(7, (int) Double.parseDouble(nettotal.getText()));
            stmt.setString(8, "'"+project+"' PROJECT EXTRACT '"+extractnumber+"'");
            stmt.setString(9, String.valueOf(date1.getValue()));
            stmt.setString(10, String.valueOf(date2.getValue()));
            stmt.setString(11, "a");
            stmt.setString(12, "yes");
            stmt.setString(13, "extract");
            stmt.executeUpdate();
            stmt = conn.prepareStatement("INSERT INTO journal (id, acc_id , asc_id, parent_id, costcenter_id, debit, credit, notes, post_dt, dt, user, post, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, String.valueOf((Integer.valueOf(entryid)+1)));
            stmt.setString(2, String.valueOf(toaccount.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(3, extid);
                    try {
                       Statement statement = conn.createStatement();
                       statement.execute("SELECT * FROM acctree WHERE id='"+String.valueOf(toaccount.getValue()).replaceAll("[^0-9]", "")+"'");
                       ResultSet results = statement.getResultSet();
                       while (results.next()){
                          parentid= results.getString("parent_id");

                    }
                  } catch (SQLException throwables) {
                      throwables.printStackTrace();
                  }
            stmt.setString(4, parentid);
            stmt.setString(5, String.valueOf(project.getValue()).replaceAll("[^0-9]", ""));
            stmt.setInt(6, (int) Double.parseDouble(nettotal.getText()));
            stmt.setInt(7, 0);
            stmt.setString(8, " '"+project+"' PROJECT EXTRACT '"+extractnumber+"'");
            stmt.setString(9, String.valueOf(date1.getValue()));
            stmt.setString(10, String.valueOf(date2.getValue()));
            stmt.setString(11, "a");
            stmt.setString(12, "yes");
            stmt.setString(13, "extract");
            stmt.executeUpdate();
            conn.close();
            post2();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void post2 () throws IOException, DocumentException {
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO journal (id, acc_id , asc_id, parent_id, costcenter_id, debit, credit, notes, post_dt, dt, user, post, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, String.valueOf((Integer.valueOf(entryid)+3)));
            stmt.setString(2, customer);
            stmt.setString(3, extid);
            try {
                Statement statement = conn.createStatement();
                statement.execute("SELECT * FROM acctree WHERE id='"+customer+"'");
                ResultSet results = statement.getResultSet();while (results.next()){
                    parentid=results.getString("parent_id");

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            stmt.setString(4, parentid);
            stmt.setString(5, String.valueOf(project.getValue()).replaceAll("[^0-9]", ""));
            stmt.setInt(6, (int) insv);
            stmt.setInt(7, 0);
            stmt.setString(8, "'"+project+"' PROJECT EXTRACT '"+extractnumber+"'");
            stmt.setString(9, String.valueOf(date1.getValue()));
            stmt.setString(10,String.valueOf(date2.getValue()));
            stmt.setString(11, "a");
            stmt.setString(12, "yes");
            stmt.setString(13, "extract");
            stmt.executeUpdate();
            stmt = conn.prepareStatement("INSERT INTO journal (id, acc_id , asc_id, parent_id, costcenter_id, debit, credit, notes, post_dt, dt, user, post, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, String.valueOf((Integer.valueOf(entryid)+4)));
            try {
                Statement statement = conn.createStatement();
                statement.execute("SELECT * FROM projects WHERE id='"+String.valueOf(project.getValue()).replaceAll("[^0-9]", "")+"'");
                ResultSet results = statement.getResultSet();
                while (results.next()){
                    insacid= results.getString("insu_acc_id");

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            stmt.setString(2, insacid);
            stmt.setString(3, extid);
            try {
                Statement statement = conn.createStatement();
                statement.execute("SELECT * FROM acctree WHERE id='"+insacid+"'");
                ResultSet results = statement.getResultSet();
                while (results.next()){
                    parentid= results.getString("parent_id");

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            stmt.setString(4, parentid);
            stmt.setString(5, String.valueOf(project.getValue()).replaceAll("[^0-9]", ""));
            stmt.setInt(6, 0);
            stmt.setInt(7, (int) insv);
            stmt.setString(8, " '"+project+"' PROJECT EXTRACT '"+extractnumber+"'");
            stmt.setString(9, String.valueOf(date1.getValue()));
            stmt.setString(10, String.valueOf(date2.getValue()));
            stmt.setString(11, "a");
            stmt.setString(12, "yes");
            stmt.setString(13, "extract");
            stmt.executeUpdate();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        Parent root = null;
        try {

            root = FXMLLoader.load(getClass().getResource("retail/projects.fxml"));
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
        apane.getScene().getWindow().hide();

    }


    public void create (ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("create.fxml"));
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
            //   stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/assetes/icon.png")));
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            System.out.println(e);
        }


    }

    }






