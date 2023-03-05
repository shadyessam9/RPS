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

public class preport implements Initializable {
    @FXML
    private ComboBox st;
    @FXML
    private ComboBox p;

    String dt;
    String hour;
    Statement statement1 ;
    Statement statement2;
    Statement statement3;
    Statement statement4;
    String accountname;

    String customer;
    String site;
    double balance;

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
        st.getItems().add("CURRENT");
        st.getItems().add("COMPLETE");
        st.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(newValue.equals("CURRENT")){
                p.setItems(FXCollections.observableArrayList(getData()));
            }else if (newValue.equals("COMPLETE")){
                p.setItems(FXCollections.observableArrayList(getData1()));
            }
        });
    }
    private java.util.List<Object> getData() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM projects WHERE complete < 100");
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
    private java.util.List<Object> getData1() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM projects WHERE complete='100'");
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


        Paragraph type = new Paragraph("PROJECT : "+String.valueOf(p.getValue()).replaceAll("[0-9]", ""), myArabicFont);
        type.setAlignment(Element.ALIGN_LEFT);
        type.setSpacingAfter(5F);
        type.setSpacingBefore(10F);
        document.add(type);


            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            statement1 = conn.createStatement();
            statement1.execute("SELECT * FROM projects AS p  LEFT JOIN customers AS c ON p.cust_id = c.id WHERE p.id='" +String.valueOf(p.getValue()).replaceAll("[^0-9]", "")+ "'");
            ResultSet results = statement1.getResultSet();
            while (results.next()) {
                customer = results.getString("c.name");
                 balance = results.getInt("pay");
            }

            Paragraph cust = new Paragraph("CUSTOMER : " + customer, myArabicFont);
            cust.setAlignment(Element.ALIGN_LEFT);
            cust.setSpacingAfter(5F);
            cust.setSpacingBefore(10F);
            document.add(cust);


        conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
        statement1 = conn.createStatement();
        statement1.execute("SELECT * FROM projects AS p  LEFT JOIN sites AS s ON p.site_id = s.id WHERE p.id='"+String.valueOf(p.getValue()).replaceAll("[^0-9]", "")+"'");
        ResultSet results1 = statement1.getResultSet();
        while (results1.next()) {
            site=results1.getString("s.name");
        }

        Paragraph s = new Paragraph("SITE : "+site, myArabicFont);
        s.setAlignment(Element.ALIGN_LEFT);
        s.setSpacingAfter(5F);
        s.setSpacingBefore(10F);
        document.add(s);



        PdfPTable table = new PdfPTable(new float[]{2, 2});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setSpacingBefore(20F);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
        statement2 = conn.createStatement();
        statement2.execute("SELECT * FROM extracts WHERE project_id='"+String.valueOf(p.getValue()).replaceAll("[^0-9]", "")+"'");
        ResultSet results2 = statement2.getResultSet();
        while (results2.next()) {
            PdfPCell table_celln = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("number")), font)));
            table_celln.setUseDescender(true);
            table_celln.setHorizontalAlignment(1);
            table_celln.setBorder(0);
            table_celln.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_celln.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table.addCell(table_celln);
            PdfPCell table_cellp = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("pay")), font)));
            table_cellp.setUseDescender(true);
            table_cellp.setHorizontalAlignment(1);
            table_cellp.setBorder(0);
            table_cellp.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cellp.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table.addCell(table_cellp);
        }
        PdfPCell table_celltt = new PdfPCell(new Phrase(new Chunk(("TOTAL"), font)));
        table_celltt.setUseDescender(true);
        table_celltt.setHorizontalAlignment(1);
        table_celltt.setBorder(0);
        table_celltt.setHorizontalAlignment(Element.ALIGN_CENTER);
        table_celltt.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table.addCell(table_celltt);
        PdfPCell table_cellt = new PdfPCell(new Phrase(new Chunk((String.valueOf(balance)), font)));
        table_cellt.setUseDescender(true);
        table_cellt.setHorizontalAlignment(1);
        table_cellt.setBorder(0);
        table_cellt.setHorizontalAlignment(Element.ALIGN_CENTER);
        table_cellt.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table.addCell(table_cellt);
        document.add(table);
        document.close();
        System.out.println("Done");
    }



}
