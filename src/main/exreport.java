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

public class exreport implements Initializable {
    @FXML
    private ComboBox st;
    @FXML
    private ComboBox p;
    @FXML
    private ComboBox ex;


    String dt;
    String hour;
    String pv;
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
        p.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            pv=String.valueOf(newValue);
            ex.setItems(FXCollections.observableArrayList(getData2()));
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
    private java.util.List<Object> getData2() {
        List<Object> search = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM extracts WHERE project_id='"+String.valueOf(pv).replaceAll("[^0-9]", "")+"'");
            ResultSet results = statement.getResultSet();
            while (results.next()) {
                search.add(results.getString("type")+":"+results.getString("id"));
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



        PdfPTable table = new PdfPTable(new float[]{4});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setHorizontalAlignment(1);
        table.setSpacingBefore(20F);
        conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
        statement1 = conn.createStatement();
        statement1.execute("SELECT * FROM extracts  WHERE id='" +String.valueOf(ex.getValue()).replaceAll("[^0-9]", "")+ "'");
        ResultSet results = statement1.getResultSet();
        while (results.next()) {
            PdfPCell table_celln = new PdfPCell(new Phrase(new Chunk("EXTRACT : "+ results.getString("number"), myArabicFont)));
            table_celln.setUseDescender(true);
            table_celln.setHorizontalAlignment(1);
            table_celln.setBorder(0);
            table_celln.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_celln.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table.addCell(table_celln);
            PdfPCell table_cellt = new PdfPCell(new Phrase(new Chunk("TYPE : "+ results.getString("type"), myArabicFont)));
            table_cellt.setUseDescender(true);
            table_cellt.setHorizontalAlignment(1);
            table_cellt.setBorder(0);
            table_cellt.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cellt.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table.addCell(table_cellt);
            PdfPCell table_cellpy = new PdfPCell(new Phrase(new Chunk("TOTAL: "+ results.getString("pay"), myArabicFont)));
            table_cellpy.setUseDescender(true);
            table_cellpy.setHorizontalAlignment(1);
            table_cellpy.setBorder(0);
            table_cellpy.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cellpy.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table.addCell(table_cellpy);
            PdfPCell table_cellp = new PdfPCell(new Phrase(new Chunk(String.valueOf(ex.getValue()).replaceAll("[^0-9]", ""), myArabicFont)));
            table_cellp.setUseDescender(true);
            table_cellp.setHorizontalAlignment(1);
            table_cellp.setBorder(0);
            table_cellp.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cellp.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table.addCell(table_cellp);
            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
            statement1 = conn.createStatement();
            statement1.execute("SELECT * FROM extracts AS e  LEFT JOIN customers AS c ON e.client_id = c.id WHERE e.id='" +String.valueOf(ex.getValue()).replaceAll("[^0-9]", "")+ "'");
            ResultSet results1 = statement1.getResultSet();
            while (results1.next()) {
                customer = results1.getString("name");
            }
            PdfPCell table_cellc = new PdfPCell(new Phrase(new Chunk("CUSTOMER : "+customer, myArabicFont)));
            table_cellc.setUseDescender(true);
            table_cellc.setHorizontalAlignment(1);
            table_cellc.setBorder(0);
            table_cellc.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cellc.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table.addCell(table_cellc);
            PdfPCell table_cellav = new PdfPCell(new Phrase(new Chunk("ADDED VALUE NUMBER: "+ results.getString("avrn"), myArabicFont)));
            table_cellav.setUseDescender(true);
            table_cellav.setHorizontalAlignment(1);
            table_cellav.setBorder(0);
            table_cellav.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cellav.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table.addCell(table_cellav);
            }
        document.add(table);

        PdfPTable table1 = new PdfPTable(new float[]{6, 7, 5, 5, 5, 5, 5, 5, 5, 5, 7, 5});
        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table1.setSpacingBefore(20F);
        PdfPCell table_cellt12 = new PdfPCell(new Phrase(new Chunk("ملاحظات", myArabicFont)));
        table_cellt12.setUseDescender(true);
        table_cellt12.setHorizontalAlignment(1);
        table_cellt12.setBorder(0);
        table_cellt12.setHorizontalAlignment(Element.ALIGN_CENTER);
        table_cellt12.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table1.addCell(table_cellt12);
        PdfPCell table_cellt11 = new PdfPCell(new Phrase(new Chunk("الباقي بعد الاستطقتاع", myArabicFont)));
        table_cellt11.setUseDescender(true);
        table_cellt11.setHorizontalAlignment(1);
        table_cellt11.setBorder(0);
        table_cellt11.setHorizontalAlignment(Element.ALIGN_CENTER);
        table_cellt11.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table1.addCell(table_cellt11);
        PdfPCell table_cellt10 = new PdfPCell(new Phrase(new Chunk("اقطاع او حجز", myArabicFont)));
        table_cellt10.setUseDescender(true);
        table_cellt10.setHorizontalAlignment(1);
        table_cellt10.setBorder(0);
        table_cellt10.setHorizontalAlignment(Element.ALIGN_CENTER);
        table_cellt10.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table1.addCell(table_cellt10);
        PdfPCell table_cellt9 = new PdfPCell(new Phrase(new Chunk("جمله الاعمال", myArabicFont)));
        table_cellt9.setUseDescender(true);
        table_cellt9.setHorizontalAlignment(1);
        table_cellt9.setBorder(0);
        table_cellt9.setHorizontalAlignment(Element.ALIGN_CENTER);
        table_cellt9.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table1.addCell(table_cellt9);
        PdfPCell table_cellt8 = new PdfPCell(new Phrase(new Chunk("اجمالي", myArabicFont)));
        table_cellt8.setUseDescender(true);
        table_cellt8.setHorizontalAlignment(1);
        table_cellt8.setBorder(0);
        table_cellt8.setHorizontalAlignment(Element.ALIGN_CENTER);
        table_cellt8.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table1.addCell(table_cellt8);
        PdfPCell table_cellt7 = new PdfPCell(new Phrase(new Chunk("حاليه", myArabicFont)));
        table_cellt7.setUseDescender(true);
        table_cellt7.setHorizontalAlignment(1);
        table_cellt7.setBorder(0);
        table_cellt7.setHorizontalAlignment(Element.ALIGN_CENTER);
        table_cellt7.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table1.addCell(table_cellt7);
        PdfPCell table_cellt6 = new PdfPCell(new Phrase(new Chunk("سابقه", myArabicFont)));
        table_cellt6.setUseDescender(true);
        table_cellt6.setHorizontalAlignment(1);
        table_cellt6.setBorder(0);
        table_cellt6.setHorizontalAlignment(Element.ALIGN_CENTER);
        table_cellt6.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table1.addCell(table_cellt6);
        PdfPCell table_cellt5 = new PdfPCell(new Phrase(new Chunk("الفئه", myArabicFont)));
        table_cellt5.setUseDescender(true);
        table_cellt5.setHorizontalAlignment(1);
        table_cellt5.setBorder(0);
        table_cellt5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table_cellt5.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table1.addCell(table_cellt5);
        PdfPCell table_cellt4 = new PdfPCell(new Phrase(new Chunk("الكميه", myArabicFont)));
        table_cellt4.setUseDescender(true);
        table_cellt4.setHorizontalAlignment(1);
        table_cellt4.setBorder(0);
        table_cellt4.setHorizontalAlignment(Element.ALIGN_CENTER);
        table_cellt4.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table1.addCell(table_cellt4);
        PdfPCell table_cellt3 = new PdfPCell(new Phrase(new Chunk("الوحده", myArabicFont)));
        table_cellt3.setUseDescender(true);
        table_cellt3.setHorizontalAlignment(1);
        table_cellt3.setBorder(0);
        table_cellt3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table_cellt3.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table1.addCell(table_cellt3);
        PdfPCell table_cellt2 = new PdfPCell(new Phrase(new Chunk("بيان الاعمال", myArabicFont)));
        table_cellt2.setUseDescender(true);
        table_cellt2.setHorizontalAlignment(1);
        table_cellt2.setBorder(0);
        table_cellt2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table_cellt2.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table1.addCell(table_cellt2);
        PdfPCell table_cellt1 = new PdfPCell(new Phrase(new Chunk("البند", myArabicFont)));
        table_cellt1.setUseDescender(true);
        table_cellt1.setHorizontalAlignment(1);
        table_cellt1.setBorder(0);
        table_cellt1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table_cellt1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table1.addCell(table_cellt1);
        PdfPCell[] cells = table1.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.GRAY);
        }
        conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
        statement1 = conn.createStatement();
        statement1.execute("SELECT * FROM bands  WHERE extract_id='" +String.valueOf(ex.getValue()).replaceAll("[^0-9]", "")+ "'");
        ResultSet results2 = statement1.getResultSet();
        while (results2.next()) {
            PdfPCell table_cell1 = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("notes")), myArabicFont)));
            table_cell1.setUseDescender(true);
            table_cell1.setHorizontalAlignment(1);
            table_cell1.setBorder(0);
            table_cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table1.addCell(table_cell1);
            PdfPCell table_cell2 = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("cutleft")), myArabicFont)));
            table_cell2.setUseDescender(true);
            table_cell2.setHorizontalAlignment(1);
            table_cell2.setBorder(0);
            table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell2.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table1.addCell(table_cell2);
            PdfPCell table_cell3 = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("cut")), myArabicFont)));
            table_cell3.setUseDescender(true);
            table_cell3.setHorizontalAlignment(1);
            table_cell3.setBorder(0);
            table_cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell3.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table1.addCell(table_cell3);
            PdfPCell table_cell4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("totalwork")), myArabicFont)));
            table_cell4.setUseDescender(true);
            table_cell4.setHorizontalAlignment(1);
            table_cell4.setBorder(0);
            table_cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell4.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table1.addCell(table_cell4);
            PdfPCell table_cell5 = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("total")), myArabicFont)));
            table_cell5.setUseDescender(true);
            table_cell5.setHorizontalAlignment(1);
            table_cell5.setBorder(0);
            table_cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell5.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table1.addCell(table_cell5);
            PdfPCell table_cell6 = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("current")), myArabicFont)));
            table_cell6.setUseDescender(true);
            table_cell6.setHorizontalAlignment(1);
            table_cell6.setBorder(0);
            table_cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell6.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table1.addCell(table_cell6);
            PdfPCell table_cell7 = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("last")), myArabicFont)));
            table_cell7.setUseDescender(true);
            table_cell7.setHorizontalAlignment(1);
            table_cell7.setBorder(0);
            table_cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell7.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table1.addCell(table_cell7);
            PdfPCell table_cell8 = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("class")), myArabicFont)));
            table_cell8.setUseDescender(true);
            table_cell8.setHorizontalAlignment(1);
            table_cell8.setBorder(0);
            table_cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell8.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table1.addCell(table_cell8);
            PdfPCell table_cell9 = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("quan")), myArabicFont)));
            table_cell9.setUseDescender(true);
            table_cell9.setHorizontalAlignment(1);
            table_cell9.setBorder(0);
            table_cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell9.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table1.addCell(table_cell9);
            PdfPCell table_cell10 = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("unit")), myArabicFont)));
            table_cell10.setUseDescender(true);
            table_cell10.setHorizontalAlignment(1);
            table_cell10.setBorder(0);
            table_cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell10.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table1.addCell(table_cell10);
            PdfPCell table_cell11 = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("statement")), myArabicFont)));
            table_cell11.setUseDescender(true);
            table_cell11.setHorizontalAlignment(1);
            table_cell11.setBorder(0);
            table_cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell11.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table1.addCell(table_cell11);
            PdfPCell table_cell12 = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("num")), myArabicFont)));
            table_cell12.setUseDescender(true);
            table_cell12.setHorizontalAlignment(1);
            table_cell12.setBorder(0);
            table_cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell12.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table1.addCell(table_cell12);

        }
        conn.close();
        document.add(table1);
        document.close();
        System.out.println("Done");
    }



}
