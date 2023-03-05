package main;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import main.Main;

import java.io.FileOutputStream;
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




public class ereport implements Initializable {


    @FXML
    private ComboBox en;
    @FXML
    private ComboBox nm;
    @FXML
    private DatePicker dt1;
    @FXML
    private DatePicker dt2;



    String dt;
    String hour;
    Statement statement1 ;
    Statement statement2;
    Statement statement3;
    Statement statement4;
    String accountname;
    double income =0;
    double costs =0;
    String month;
    String year;
    String quarter;
    double balance;
    double db;
    double cr;
    Connection conn ;
    private double xOffset = 0.0D;
    private double yOffset = 0.0D;
    public static Object aid;
    public static Object search;
    public static Object d1;
    public static Object d2;
    double nt = 0;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        en.getItems().add("SUPPLIERS");
        en.getItems().add("CUSTOMERS");


        en.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(newValue.equals("SUPPLIERS")){
                nm.setItems(FXCollections.observableArrayList(getData()));
            }else if (newValue.equals("CUSTOMERS")){
                nm.setItems(FXCollections.observableArrayList(getData1()));
            }
        });

    }

    private java.util.List<Object> getData() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM suppliers");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search.add(results.getString("name")+":"+Integer.parseInt(results.getString("id")));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return search;
    }
    private java.util.List<Object> getData1() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM customers");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search.add(results.getString("name")+":"+Integer.parseInt(results.getString("id")));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return search;
    }



    public void createrep() throws Exception {
        DateTimeFormatter t = DateTimeFormatter.ofPattern("hh:mm a");
        LocalDateTime now1 = LocalDateTime.now();
        this.hour = t.format(now1);
        DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        this.dt = dtr.format(now);
        Document document = new Document();
        document.open();
        FontFactory.registerDirectories();
        FontFactory.register("c:/windows/fonts/tradbdo.ttf", "my_arabic");
        Font font = FontFactory.getFont("arial", "Identity-H", true, 11.0F);
        Font largeBold = new Font(Font.FontFamily.COURIER, 25, Font.BOLD | Font.UNDERLINE);
        Font myArabicFont = FontFactory.getFont("my_arabic", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        PdfWriter.getInstance(document, new FileOutputStream("sample3.pdf"));
        document.open();
        Paragraph Title = new Paragraph("El Foaud For Contracting Works & Commerce Co \n 01005251258 \n  ELFOUADCOMMERCE@YAHOO.COM");
        Title.setAlignment(Element.ALIGN_RIGHT);
        Title.setSpacingAfter(5F);
        document.add(Title);
        Paragraph detailsd = new Paragraph(this.dt + " " + this.hour);
        detailsd.setAlignment(Element.ALIGN_RIGHT);
        detailsd.setSpacingAfter(5.0F);
        document.add(detailsd);

        Image companyLogo = Image.getInstance("/vulcan/src/assetes/reportlogo.png");
        companyLogo.scalePercent(7);
        companyLogo.setSpacingAfter(50F);
        companyLogo.setAbsolutePosition(0, PageSize.A4.getHeight() - companyLogo.getScaledHeight() + 20);
        document.add(companyLogo);

        LineSeparator ls3 = new LineSeparator();
        document.add(new Chunk(ls3));


        Paragraph type = new Paragraph(String.valueOf(nm.getValue()).replaceAll("[0-9]", ""), largeBold);
        type.setAlignment(Element.ALIGN_LEFT);
        type.setSpacingAfter(5F);
        type.setSpacingBefore(10F);
        document.add(type);
        Paragraph details = new Paragraph("FORM" + " : " + dt1.getValue() + " TO " + " : " + dt2.getValue());
        details.setAlignment(Element.ALIGN_LEFT);
        details.setSpacingAfter(5.0F);
        document.add(details);

        PdfPTable table = new PdfPTable(new float[]{2, 2, 2, 2, 2, 2});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setSpacingBefore(20F);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("NUMBER");
        table.addCell("DATE");
        table.addCell("DEBIT");
        table.addCell("CREDIT");
        table.addCell("BALANCE");
        table.addCell("NOTES");
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }
            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            statement1 = conn.createStatement();
            statement1.execute("SELECT (SELECT (SUM(debit))-(SUM(credit)) FROM journal where acc_id='" + String.valueOf(nm.getValue()).replaceAll("[^0-9]", "") + "' AND `dt` between  YEAR(CURDATE())  AND '" + dt1.getValue() + "') + (SELECT (SUM(debit))-(SUM(credit)) FROM openningbalances where acc_id='"+String.valueOf(nm.getValue()).replaceAll("[^0-9]", "")+"' AND YEAR(dt) = YEAR(CURDATE())) AS SUM");
            ResultSet results = statement1.getResultSet();
            while (results.next()) {
                balance = results.getInt("sum");
                table.addCell("0");
                table.addCell(String.valueOf(dt1.getValue()));
                table.addCell(String.valueOf(balance));
                table.addCell("0");
                table.addCell(String.valueOf(balance));
                table.addCell("  ");
                conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                statement1 = conn.createStatement();
                statement1.execute("SELECT * FROM journal WHERE acc_id='" + String.valueOf(nm.getValue()).replaceAll("[^0-9]", "") +"' AND dt between '"+dt1.getValue()+"' AND '"+dt2.getValue()+"' ");
                ResultSet results1 = statement1.getResultSet();
                while (results1.next()) {
                    db = results1.getInt("debit");
                    cr = results1.getInt("credit");
                    balance = balance + (db - cr);
                    table.addCell(String.valueOf(results1.getInt("num")));
                    table.addCell(String.valueOf(results1.getString("dt")));
                    table.addCell(String.valueOf(results1.getInt("debit")));
                    table.addCell(String.valueOf(results1.getInt("credit")));
                    table.addCell(String.valueOf(balance));
                    table.addCell(String.valueOf(results1.getInt("notes")));
                }
            }
            document.add(table);
            document.close();
            System.out.println("Done");
    }
}
