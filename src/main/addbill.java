package main;

import com.itextpdf.text.DocumentException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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

public class addbill implements Initializable {

    @FXML
    private Label billid;
    @FXML
    private Label billnumber;
    @FXML
    private DatePicker date1;
    @FXML
    private DatePicker date2;
    @FXML
    private ComboBox purchaseaccount;
    @FXML
    private ComboBox vendoraccount;
    @FXML
    private ComboBox project;
    @FXML
    private ComboBox item;
    @FXML
    private ComboBox wh;
    @FXML
    private TextField addedvalue;
    @FXML
    private TextField nettotal;
    @FXML
    private TextField price;
    @FXML
    private TextField quantity;
    @FXML
    private AnchorPane apane;

    String date;
    String id;
    String id1;
    String p_id;
    String unit;
    int total=0;
    int ntotal=0;
    float adv=0;
    double taxed=0;
    double mtotal=0;
    String siteid;
    String it2;
    int p2;
    int q2;
    int c=0;


    private double xOffset = 0.0D;
    private double yOffset = 0.0D;

    DateTimeFormatter dtr1 = DateTimeFormatter.ofPattern("mm-ss-yyyy");
    LocalDateTime now1 = LocalDateTime.now();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        id = "4"+dtr1.format(now1).replace("-", "");
        billid.setText(id);


        project.setItems(FXCollections.observableArrayList(getData1()));
        vendoraccount.setItems(FXCollections.observableArrayList(getData2()));
        item.setItems(FXCollections.observableArrayList(getData3()));
        purchaseaccount.setItems(FXCollections.observableArrayList(getData4()));
        wh.setItems(FXCollections.observableArrayList(getData5()));


        item.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                Statement statement = conn.createStatement();
                statement.execute("SELECT * FROM bills_items WHERE item_id='"+String.valueOf(newValue).replaceAll("[^0-9]", "")+"'");
                ResultSet results = statement.getResultSet();
                while (results.next()) {
                    price.setText(String.valueOf(results.getInt("price")));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        DateTimeFormatter dtr = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        date = dtr.format(now);


    }

    private List<Object> getData1() {
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
    private List<Object> getData2() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM suppliers");
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
    private List<Object> getData4() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM acctree WHERE parent_id='9431717022022' OR parent_id='9472017022022' parent_id='9472017022022' ");
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


    public void ai (ActionEvent event) throws IOException, DocumentException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM warehouses WHERE id='"+String.valueOf(wh.getValue()).replaceAll("[^0-9]", "")+"'");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                siteid=results.getString("site_id");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO bills_items (bill_id, item_id , item , price, quan) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, id);
            stmt.setString(2, String.valueOf(item.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(3, (String) item.getValue());
            stmt.setInt(4, Integer.parseInt(price.getText()));
            stmt.setInt(5, Integer.parseInt(quantity.getText()));
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO warehouse_items (wh_id, site_id , costcenter_id , item_id, item, unit, price, quan, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)  ON DUPLICATE KEY UPDATE  quan=quan +'"+Integer.parseInt(quantity.getText())+"',price=price +'"+Integer.parseInt(price.getText())+"'");
            stmt.setString(1, String.valueOf(wh.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(2, siteid);
            stmt.setString(3,String.valueOf(project.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(4, String.valueOf(item.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(5, (String) item.getValue());
            try {
                Statement statement = conn.createStatement();
                statement.execute("SELECT * FROM items WHERE id='"+String.valueOf(item.getValue()).replaceAll("[^0-9]", "")+"'");
                ResultSet results = statement.getResultSet();
                while (results.next()) {
                    unit=results.getString("unit");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            stmt.setString(6, unit);
            stmt.setInt(7, Integer.parseInt(price.getText()));
            stmt.setInt(8, Integer.parseInt(quantity.getText()));
            stmt.setString(9, "bill");
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO warehouse_operations (bill_id, project_id, wh_id, item_id, item, price, quan,total, type, dt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, id);
            stmt.setString(2, String.valueOf(project.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(3, String.valueOf(wh.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(4, String.valueOf(item.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(5, (String) item.getValue());
            stmt.setInt(6, Integer.parseInt(price.getText()));
            stmt.setInt(7, Integer.parseInt(quantity.getText()));
            mtotal=(Integer.parseInt((price.getText()))*Integer.parseInt((quantity.getText())));
            stmt.setString(8, String.valueOf(mtotal));
            stmt.setString(9, "bill");
            stmt.setString(10, date);
            stmt.executeUpdate();
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                total= total+ Integer.parseInt(quantity.getText())*Integer.parseInt(price.getText());
                adv=Float.parseFloat(addedvalue.getText())/100;
                taxed=total+(total*adv);
                ntotal= (int) (taxed);
                nettotal.setText(String.valueOf(ntotal));
                c=c+1;
                billnumber.setText(String.valueOf(c));
            }});

        it2= String.valueOf(item.getValue()).replaceAll("[^0-9]", "");
        p2=Integer.parseInt(price.getText());
        q2=Integer.parseInt(quantity.getText());
    }

    public void last (ActionEvent event) throws IOException, DocumentException {
        item.setValue(it2);
        price.setText(String.valueOf(p2));
        quantity.setText(String.valueOf(q2));
    }

    public void post (ActionEvent event) throws IOException, DocumentException {


        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO bills (id,dt, post_dt , acc_dt, costcenter_id, sup_id, total_pay, added_value_tax, net_pay, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, id);
            stmt.setString(2, String.valueOf(date1.getValue()));
            stmt.setString(3, date);
            stmt.setString(4, String.valueOf(date2.getValue()));
            stmt.setString(5, String.valueOf(project.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(6, String.valueOf(vendoraccount.getValue()).replaceAll("[^0-9]", ""));
            stmt.setInt(7, total);
            stmt.setInt(8,(Integer.parseInt(addedvalue.getText())) );
            stmt.setInt(9, Integer.parseInt(nettotal.getText()));
            stmt.setString(10, "123");
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            id1 = "10"+dtr1.format(now1).replace("-", "");
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO journal (id, acc_id , asc_id, parent_id, costcenter_id, debit, credit, notes, post_dt, dt, user, post, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, String.valueOf(Integer.parseInt(id1)+1));
            stmt.setString(2, String.valueOf(purchaseaccount.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(3, id);
             try {
                 Statement statement = conn.createStatement();
                 statement.execute("SELECT * FROM acctree WHERE id='"+String.valueOf(purchaseaccount.getValue()).replaceAll("[^0-9]", "")+"'");
                 ResultSet results = statement.getResultSet();
                 while (results.next()){
                    p_id=results.getString("parent_id");

                  }
               } catch (SQLException throwables) {
                    throwables.printStackTrace();
               }
            stmt.setString(4, p_id);
            stmt.setString(5, String.valueOf(project.getValue()).replaceAll("[^0-9]", ""));
            stmt.setInt(6, 0);
            stmt.setInt(7, total);
            stmt.setString(8, "BILL OF PURCHASE");
            stmt.setString(9, String.valueOf(date));
            stmt.setString(10, String.valueOf(date1.getValue()));
            stmt.setString(11, "a");
            stmt.setString(12, "yes");
            stmt.setString(13, "bill");
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            id1 = "10"+dtr1.format(now1).replace("-", "");
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO journal (id, acc_id , asc_id, parent_id, costcenter_id, debit, credit, notes, post_dt, dt, user, post, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, String.valueOf(Integer.parseInt(id1)+2));
            stmt.setString(2, String.valueOf(vendoraccount.getValue()).replaceAll("[^0-9]", ""));
            stmt.setString(3, id);
            try {
                Statement statement = conn.createStatement();
                statement.execute("SELECT * FROM acctree WHERE id='"+String.valueOf(vendoraccount.getValue()).replaceAll("[^0-9]", "")+"'");
                ResultSet results = statement.getResultSet();
                while (results.next()){
                    p_id=results.getString("parent_id");

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            stmt.setString(4, "200");
            stmt.setString(5, String.valueOf(project.getValue()).replaceAll("[^0-9]", ""));
            stmt.setInt(6, total);
            stmt.setInt(7, 0);
            stmt.setString(8, "BILL OF PURCHASE");
            stmt.setString(9, String.valueOf(date));
            stmt.setString(10, String.valueOf(date1.getValue()));
            stmt.setString(11, "a");
            stmt.setString(12, "yes");
            stmt.setString(13, "bill");
            stmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        Parent root = null;
        try {

            root = FXMLLoader.load(getClass().getResource("projects.fxml"));
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
