package main;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
import java.util.ResourceBundle;

public class glreports implements Initializable {
    @FXML
    private ComboBox level;
    @FXML
    private ComboBox date;
    @FXML
    private DatePicker dt1;
    @FXML
    private DatePicker dt2;
    @FXML
    private Button conf;


    String dt;
    String hour;

    Statement statement1 ;
    Statement statement2;
    Statement statement3;
    Statement statement4;
    String accountname;
    double  opdb;
    double  opcr;
    double  tmdb;
    double  tmcr;
    double  todb;
    double  tocr;
    double  tdb1 = 0;
    double  tcr1 = 0;
    double  tdb2 = 0;
    double  tcr2 = 0;
    double tdb3 = 0;
    double  tcr3 = 0;
    Object key1;
    Object key2;
    String month;
    String year;
    String quarter;
    Connection conn ;
    private double xOffset = 0.0D;
    private double yOffset = 0.0D;

    public static Object aid;
    public static Object search;
    public static Object d1;
    public static Object d2;
    String parentaccountname;
    String costcenter;
    double  adb;
    double  acr;
    double  balance = 0;

    double tb = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        level.getItems().add("ALL");
        level.getItems().add("0");
        level.getItems().add("1");
        level.getItems().add("2");
        level.getItems().add("3");
        level.getItems().add("4");
        level.getItems().add("5");
        date.getItems().add("CURRENT MONTH");
        date.getItems().add("CURRENT QUARTER");
        date.getItems().add("CURRENT YEAR");
        date.getItems().add("LAST MONTH");
        date.getItems().add("LAST QUARTER");
        date.getItems().add("LAST YEAR");
        date.getItems().add("SPECIFIC");

        dt1.setDisable(true);
        dt2.setDisable(true);
   //     conf.setDisable(true);


    }
   public void createrep() throws Exception {
            if(date.getValue().equals("CURRENT MONTH")){
                FontFactory.registerDirectories();
                FontFactory.register("c:/windows/fonts/tradbdo.ttf", "my_arabic");
                Font font = FontFactory.getFont("my_arabic" ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED,10f);
                DateTimeFormatter t = DateTimeFormatter.ofPattern("hh:mm a");
                LocalDateTime now1 = LocalDateTime.now();
                this.hour = t.format(now1);
                DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDateTime now = LocalDateTime.now();
                this.dt = dtr.format(now);
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream("sample3.pdf"));
                document.open();
                Paragraph Title = new Paragraph("  ELFOUAD COMPANY REPORTINGS ");
                Title.setAlignment(1);
                Title.setSpacingAfter(5F);
                document.add(Title);
                Paragraph type = new Paragraph("TRAIL BALANCE");
                type.setAlignment(1);
                type.setSpacingAfter(5F);
                document.add(type);
                Paragraph detailsd = new Paragraph(this.dt+" "+this.hour);
                detailsd.setAlignment(1);
                detailsd.setSpacingAfter(5.0F);
                document.add(detailsd);
                Paragraph details = new Paragraph("FOR"+" : "+month+"-"+year);
                details.setAlignment(1);
                details.setSpacingAfter(5.0F);
                document.add(details);
                Image companyLogo = Image.getInstance("/vulcan/src/assetes/reportlogo.png");
                companyLogo.scalePercent(7);
                companyLogo.setSpacingAfter(50F);
                companyLogo.setAbsolutePosition(0, PageSize.A4.getHeight() - companyLogo.getScaledHeight()+20);
                document.add(companyLogo);
                try {
                    conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    statement1 = conn.createStatement();
                    if(level.getValue().equals("ALL")){
                        statement1.execute("SELECT * FROM acctree ");
                    }else {
                        statement1.execute("SELECT * FROM acctree WHERE level='"+level.getValue()+"' ");
                    }
                    ResultSet results = statement1.getResultSet();
                    while (results.next()) {
                        accountname= results.getString("title");

                        PdfPTable table = new PdfPTable(new float[] { 2, 2, 2, 2 });
                        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell("ACCOUNT");
                        table.addCell("OPENING BALANCE");
                        table.addCell(month+"-"+year);
                        table.addCell("BALANCE");
                        table.addCell("       ");
                        PdfPTable obtable = new PdfPTable(new float[] {1,1});
                        obtable.getDefaultCell().setBorder(0);
                        PdfPCell table_celld2 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld2.setHorizontalAlignment(1);
                        table_celld2.setBorder(0);
                        PdfPCell table_cellc2 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc2.setHorizontalAlignment(1);
                        table_cellc2.setBorder(0);
                        obtable.addCell(table_celld2);
                        obtable.addCell(table_cellc2);
                        table.addCell( obtable);
                        PdfPTable mtable = new PdfPTable(new float[] {1,1});
                        mtable.getDefaultCell().setBorder(0);
                        PdfPCell table_celld1 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld1.setHorizontalAlignment(1);
                        table_celld1.setBorder(0);
                        PdfPCell table_cellc1 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc1.setHorizontalAlignment(1);
                        table_cellc1.setBorder(0);
                        mtable.addCell(table_celld1);
                        mtable.addCell(table_cellc1);
                        table.addCell(mtable);
                        table.addCell("       ");
                        table.setSpacingBefore(10F);
                        table.setHeaderRows(1);
                        PdfPCell[] cells = table.getRow(0).getCells();
                        for (int j=0;j<cells.length;j++){
                            cells[j].setBackgroundColor(BaseColor.GRAY);
                        }
                        PdfPCell table_cellan = new PdfPCell(new Phrase(new Chunk(accountname, font)));
                        table_cellan.setUseDescender(true);
                        table_cellan.setHorizontalAlignment(1);
                        table_cellan.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table_cellan.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        table.addCell(table_cellan);

                        statement2 = conn.createStatement();
                        statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "'  AND  YEAR(dt) = YEAR(CURDATE())");
                        ResultSet results1 = statement2.getResultSet();
                        while (results1.next()) {
                            opdb= results1.getInt("sdb");
                            tdb1=tdb1+opdb;
                            opcr= results1.getInt("scr");
                            tcr1=tcr1+opcr;
                            PdfPTable obtable1 = new PdfPTable(new float[] {1,1});
                            PdfPCell table_celld4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opdb), font)));
                            table_celld4.setUseDescender(true);
                            table_celld4.setHorizontalAlignment(1);
                            table_celld4.setRunDirection(3);
                            PdfPCell table_cellc4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opcr), font)));
                            table_cellc4.setUseDescender(true);
                            table_cellc4.setHorizontalAlignment(1);
                            table_cellc4.setRunDirection(3);
                            obtable1.addCell(table_celld4);
                            obtable1.addCell(table_cellc4);
                            table.addCell(obtable1);
                        }
                        statement3 = conn.createStatement();
                        statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND MONTH(dt) = MONTH(CURRENT_DATE())");
                        ResultSet results2 = statement3.getResultSet();
                        while (results2.next()) {
                            tmdb= results2.getInt("sdb");
                            tdb2=tdb2+tmdb;
                            tmcr= results2.getInt("scr");
                            tcr2=tcr2+tmcr;
                            PdfPTable mtable1 = new PdfPTable(new float[] {1,1});
                            PdfPCell table_celld5= new PdfPCell(new Phrase(new Chunk((String.valueOf(tmdb)), font)));
                            table_celld5.setUseDescender(true);
                            table_celld5.setHorizontalAlignment(1);
                            table_celld5.setRunDirection(3);
                            PdfPCell table_cellc5 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tmcr)), font)));
                            table_cellc5.setUseDescender(true);
                            table_cellc5.setHorizontalAlignment(1);
                            table_cellc5.setRunDirection(3);
                            mtable1.addCell(table_celld5);
                            mtable1.addCell(table_cellc5);
                            table.addCell(mtable1);
                        }

                        todb=opdb+tmdb;
                        tdb3=tdb3+todb;
                        tocr=opcr+tmcr;
                        tcr3=tcr3+tocr;
                        balance=(opdb+tmdb)-(opcr+tmcr);
                        tb=tb+balance;
                        table.addCell(String.valueOf(balance));
                        table.setSpacingAfter(10F);
                        document.add(table);


                        try {
                            PdfPTable table2 = new PdfPTable(new float[] { 1, 1, 1, 1, 1, 1, 1, 1 });
                            table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                            table2.addCell(new Phrase(new Chunk("NUM", font)));
                            table2.addCell(new Phrase(new Chunk("PARENT ACCOUNT", font)));
                            table2.addCell(new Phrase(new Chunk("COSTCENTER", font)));
                            table2.addCell(new Phrase(new Chunk("DEBIT", font)));
                            table2.addCell(new Phrase(new Chunk("CREDIT", font)));
                            table2.addCell(new Phrase(new Chunk("DATE", font)));
                            table2.addCell(new Phrase(new Chunk("BY", font)));
                            table2.addCell(new Phrase(new Chunk("NOTES", font)));
                            PdfPCell[] cells1 = table2.getRow(0).getCells();
                            for (int j=0;j<cells1.length;j++){
                                cells1[j].setBackgroundColor(BaseColor.GRAY);
                            }
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            statement1.execute("SELECT * FROM journal  WHERE  post ='yes' AND acc_id='"+results.getString("id")+"'");
                            ResultSet results3 = statement1.getResultSet();
                            while (results3.next()) {
                                table2.addCell(new Phrase(new Chunk(results3.getString("num"), font)));
                                statement3 = conn.createStatement();
                                statement3.execute("SELECT * FROM acctree WHERE id='" + results3.getString("parent_id") + "'");
                                ResultSet results5 = statement3.getResultSet();
                                while (results5.next()) {
                                    parentaccountname = results5.getString("title");
                                    PdfPCell table_cellpn = new PdfPCell(new Phrase(new Chunk(parentaccountname, font)));
                                    table_cellpn.setUseDescender(true);
                                    table_cellpn.setHorizontalAlignment(1);
                                    table_cellpn.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table_cellpn.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                                    table2.addCell(table_cellpn);
                                }
                                statement4 = conn.createStatement();
                                statement4.execute("select * from projects WHERE id= '" + results3.getString("costcenter_id") + "'");
                                ResultSet results6 = statement4.getResultSet();
                                while (results6.next()) {
                                    costcenter = String.valueOf(results6.getString("name"));
                                    PdfPCell table_cellcc = new PdfPCell(new Phrase(new Chunk(costcenter, font)));
                                    table_cellcc.setUseDescender(true);
                                    table_cellcc.setHorizontalAlignment(1);
                                    table_cellcc.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table_cellcc.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                                    table2.addCell(table_cellcc);
                                }
                                table2.addCell(new Phrase(new Chunk(results3.getString("debit"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("credit"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("dt"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("user"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("notes"), font)));
                            }
                            table2.setSpacingAfter(10F);
                            document.add(table2);
                        } catch (Exception e) {
                            System.out.print(e);
                        }
                        LineSeparator ls3 = new LineSeparator();
                        document.add(new Chunk(ls3));
                    }
                    document.close();
                    System.out.println("Done");
                    conn.close();
                } catch (Exception e) {
                    System.out.print(e);
                }
            }else if(date.getValue().equals("CURRENT QUARTER")){
                FontFactory.registerDirectories();
                FontFactory.register("c:/windows/fonts/tradbdo.ttf", "my_arabic");
                Font font = FontFactory.getFont("my_arabic" ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED,10f);;
                DateTimeFormatter t = DateTimeFormatter.ofPattern("hh:mm a");
                LocalDateTime now1 = LocalDateTime.now();
                this.hour = t.format(now1);
                DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDateTime now = LocalDateTime.now();
                this.dt = dtr.format(now);
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream("sample3.pdf"));
                document.open();
                Paragraph Title = new Paragraph("  ELFOUAD COMPANY REPORTINGS ");
                Title.setAlignment(1);
                Title.setSpacingAfter(5F);
                document.add(Title);
                Paragraph type = new Paragraph("TRAIL BALANCE");
                type.setAlignment(1);
                type.setSpacingAfter(5F);
                document.add(type);
                Paragraph detailsd = new Paragraph(this.dt+" "+this.hour);
                detailsd.setAlignment(1);
                detailsd.setSpacingAfter(5.0F);
                document.add(detailsd);
                Paragraph details = new Paragraph("FOR"+" : "+month+"-"+year);
                details.setAlignment(1);
                details.setSpacingAfter(5.0F);
                document.add(details);
                Image companyLogo = Image.getInstance("/vulcan/src/assetes/reportlogo.png");
                companyLogo.scalePercent(7);
                companyLogo.setSpacingAfter(50F);
                companyLogo.setAbsolutePosition(0, PageSize.A4.getHeight() - companyLogo.getScaledHeight()+20);
                document.add(companyLogo);


                try {
                    conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    statement1 = conn.createStatement();
                    if(level.getValue().equals("ALL")){
                        statement1.execute("SELECT * FROM acctree ");
                    }else {
                        statement1.execute("SELECT * FROM acctree WHERE level='"+level.getValue()+"' ");
                    }
                    ResultSet results = statement1.getResultSet();
                    while (results.next()) {
                        accountname= results.getString("title");

                        PdfPTable table = new PdfPTable(new float[] { 2, 2, 2, 2 });
                        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell("ACCOUNT");
                        table.addCell("OPENING BALANCE");
                        table.addCell(month+"-"+year);
                        table.addCell("BALANCE");
                        table.addCell("       ");
                        PdfPTable obtable = new PdfPTable(new float[] {1,1});
                        obtable.getDefaultCell().setBorder(0);
                        PdfPCell table_celld2 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld2.setHorizontalAlignment(1);
                        table_celld2.setBorder(0);
                        PdfPCell table_cellc2 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc2.setHorizontalAlignment(1);
                        table_cellc2.setBorder(0);
                        obtable.addCell(table_celld2);
                        obtable.addCell(table_cellc2);
                        table.addCell( obtable);
                        PdfPTable mtable = new PdfPTable(new float[] {1,1});
                        mtable.getDefaultCell().setBorder(0);
                        PdfPCell table_celld1 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld1.setHorizontalAlignment(1);
                        table_celld1.setBorder(0);
                        PdfPCell table_cellc1 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc1.setHorizontalAlignment(1);
                        table_cellc1.setBorder(0);
                        mtable.addCell(table_celld1);
                        mtable.addCell(table_cellc1);
                        table.addCell(mtable);
                        table.addCell("       ");
                        table.setSpacingBefore(10F);
                        table.setHeaderRows(1);
                        PdfPCell[] cells = table.getRow(0).getCells();
                        for (int j=0;j<cells.length;j++){
                            cells[j].setBackgroundColor(BaseColor.GRAY);
                        }
                        PdfPCell table_cellan = new PdfPCell(new Phrase(new Chunk(accountname, font)));
                        table_cellan.setUseDescender(true);
                        table_cellan.setHorizontalAlignment(1);
                        table_cellan.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table_cellan.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        table.addCell(table_cellan);

                        statement2 = conn.createStatement();
                        statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "'  AND  YEAR(dt) = YEAR(CURDATE())");
                        ResultSet results1 = statement2.getResultSet();
                        while (results1.next()) {
                            opdb= results1.getInt("sdb");
                            tdb1=tdb1+opdb;
                            opcr= results1.getInt("scr");
                            tcr1=tcr1+opcr;
                            PdfPTable obtable1 = new PdfPTable(new float[] {1,1});
                            PdfPCell table_celld4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opdb), font)));
                            table_celld4.setUseDescender(true);
                            table_celld4.setHorizontalAlignment(1);
                            table_celld4.setRunDirection(3);
                            PdfPCell table_cellc4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opcr), font)));
                            table_cellc4.setUseDescender(true);
                            table_cellc4.setHorizontalAlignment(1);
                            table_cellc4.setRunDirection(3);
                            obtable1.addCell(table_celld4);
                            obtable1.addCell(table_cellc4);
                            table.addCell(obtable1);
                        }
                        statement3 = conn.createStatement();
                        statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND QUARTER(dt) = QUARTER(curdate())");
                        ResultSet results2 = statement3.getResultSet();
                        while (results2.next()) {
                            tmdb= results2.getInt("sdb");
                            tdb2=tdb2+tmdb;
                            tmcr= results2.getInt("scr");
                            tcr2=tcr2+tmcr;
                            PdfPTable mtable1 = new PdfPTable(new float[] {1,1});
                            PdfPCell table_celld5= new PdfPCell(new Phrase(new Chunk((String.valueOf(tmdb)), font)));
                            table_celld5.setUseDescender(true);
                            table_celld5.setHorizontalAlignment(1);
                            table_celld5.setRunDirection(3);
                            PdfPCell table_cellc5 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tmcr)), font)));
                            table_cellc5.setUseDescender(true);
                            table_cellc5.setHorizontalAlignment(1);
                            table_cellc5.setRunDirection(3);
                            mtable1.addCell(table_celld5);
                            mtable1.addCell(table_cellc5);
                            table.addCell(mtable1);
                        }

                        todb=opdb+tmdb;
                        tdb3=tdb3+todb;
                        tocr=opcr+tmcr;
                        tcr3=tcr3+tocr;
                        balance=(opdb+tmdb)-(opcr+tmcr);
                        tb=tb+balance;
                        table.addCell(String.valueOf(balance));
                        table.setSpacingAfter(10F);
                        document.add(table);


                        try {
                            PdfPTable table2 = new PdfPTable(new float[] { 1, 1, 1, 1, 1, 1, 1, 1 });
                            table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                            table2.addCell(new Phrase(new Chunk("NUM", font)));
                            table2.addCell(new Phrase(new Chunk("PARENT ACCOUNT", font)));
                            table2.addCell(new Phrase(new Chunk("COSTCENTER", font)));
                            table2.addCell(new Phrase(new Chunk("DEBIT", font)));
                            table2.addCell(new Phrase(new Chunk("CREDIT", font)));
                            table2.addCell(new Phrase(new Chunk("DATE", font)));
                            table2.addCell(new Phrase(new Chunk("BY", font)));
                            table2.addCell(new Phrase(new Chunk("NOTES", font)));
                            PdfPCell[] cells1 = table2.getRow(0).getCells();
                            for (int j=0;j<cells1.length;j++){
                                cells1[j].setBackgroundColor(BaseColor.GRAY);
                            }
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            statement1.execute("SELECT * FROM journal  WHERE  post ='yes' AND acc_id='"+results.getString("id")+"' AND QUARTER(dt) = QUARTER(curdate())");
                            ResultSet results3 = statement1.getResultSet();
                            while (results3.next()) {
                                table2.addCell(new Phrase(new Chunk(results3.getString("num"), font)));
                                statement3 = conn.createStatement();
                                statement3.execute("SELECT * FROM acctree WHERE id='" + results3.getString("parent_id") + "'");
                                ResultSet results5 = statement3.getResultSet();
                                while (results5.next()) {
                                    parentaccountname = results5.getString("title");
                                    PdfPCell table_cellpn = new PdfPCell(new Phrase(new Chunk(parentaccountname, font)));
                                    table_cellpn.setUseDescender(true);
                                    table_cellpn.setHorizontalAlignment(1);
                                    table_cellpn.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table_cellpn.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                                    table2.addCell(table_cellpn);
                                }
                                statement4 = conn.createStatement();
                                statement4.execute("select * from projects WHERE id= '" + results3.getString("costcenter_id") + "'");
                                ResultSet results6 = statement4.getResultSet();
                                while (results6.next()) {
                                    costcenter = String.valueOf(results6.getString("name"));
                                    PdfPCell table_cellcc = new PdfPCell(new Phrase(new Chunk(costcenter, font)));
                                    table_cellcc.setUseDescender(true);
                                    table_cellcc.setHorizontalAlignment(1);
                                    table_cellcc.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table_cellcc.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                                    table2.addCell(table_cellcc);
                                }
                                table2.addCell(new Phrase(new Chunk(results3.getString("debit"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("credit"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("dt"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("user"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("notes"), font)));
                            }
                            table2.setSpacingAfter(10F);
                            document.add(table2);
                        } catch (Exception e) {
                            System.out.print(e);
                        }
                        LineSeparator ls3 = new LineSeparator();
                        document.add(new Chunk(ls3));
                    }
                    document.close();
                    System.out.println("Done");
                    conn.close();
                } catch (Exception e) {
                    System.out.print(e);
                }
            } else if(date.getValue().equals("CURRENT YEAR")){
                FontFactory.registerDirectories();
                FontFactory.register("c:/windows/fonts/tradbdo.ttf", "my_arabic");
                Font font = FontFactory.getFont("my_arabic" ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED,10f);;
                DateTimeFormatter t = DateTimeFormatter.ofPattern("hh:mm a");
                LocalDateTime now1 = LocalDateTime.now();
                this.hour = t.format(now1);
                DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDateTime now = LocalDateTime.now();
                this.dt = dtr.format(now);
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream("sample3.pdf"));
                document.open();
                Paragraph Title = new Paragraph("  ELFOUAD COMPANY REPORTINGS ");
                Title.setAlignment(1);
                Title.setSpacingAfter(5F);
                document.add(Title);
                Paragraph type = new Paragraph("TRAIL BALANCE");
                type.setAlignment(1);
                type.setSpacingAfter(5F);
                document.add(type);
                Paragraph detailsd = new Paragraph(this.dt+" "+this.hour);
                detailsd.setAlignment(1);
                detailsd.setSpacingAfter(5.0F);
                document.add(detailsd);
                Paragraph details = new Paragraph("FOR"+" : "+month+"-"+year);
                details.setAlignment(1);
                details.setSpacingAfter(5.0F);
                document.add(details);
                Image companyLogo = Image.getInstance("/vulcan/src/assetes/reportlogo.png");
                companyLogo.scalePercent(7);
                companyLogo.setSpacingAfter(50F);
                companyLogo.setAbsolutePosition(0, PageSize.A4.getHeight() - companyLogo.getScaledHeight()+20);
                document.add(companyLogo);


                try {
                    conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    statement1 = conn.createStatement();
                    if(level.getValue().equals("ALL")){
                        statement1.execute("SELECT * FROM acctree ");
                    }else {
                        statement1.execute("SELECT * FROM acctree WHERE level='"+level.getValue()+"' ");
                    }
                    ResultSet results = statement1.getResultSet();
                    while (results.next()) {
                        accountname= results.getString("title");

                        PdfPTable table = new PdfPTable(new float[] { 2, 2, 2, 2 });
                        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell("ACCOUNT");
                        table.addCell("OPENING BALANCE");
                        table.addCell(month+"-"+year);
                        table.addCell("BALANCE");
                        table.addCell("       ");
                        PdfPTable obtable = new PdfPTable(new float[] {1,1});
                        obtable.getDefaultCell().setBorder(0);
                        PdfPCell table_celld2 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld2.setHorizontalAlignment(1);
                        table_celld2.setBorder(0);
                        PdfPCell table_cellc2 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc2.setHorizontalAlignment(1);
                        table_cellc2.setBorder(0);
                        obtable.addCell(table_celld2);
                        obtable.addCell(table_cellc2);
                        table.addCell( obtable);
                        PdfPTable mtable = new PdfPTable(new float[] {1,1});
                        mtable.getDefaultCell().setBorder(0);
                        PdfPCell table_celld1 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld1.setHorizontalAlignment(1);
                        table_celld1.setBorder(0);
                        PdfPCell table_cellc1 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc1.setHorizontalAlignment(1);
                        table_cellc1.setBorder(0);
                        mtable.addCell(table_celld1);
                        mtable.addCell(table_cellc1);
                        table.addCell(mtable);
                        table.addCell("       ");
                        table.setSpacingBefore(10F);
                        table.setHeaderRows(1);
                        PdfPCell[] cells = table.getRow(0).getCells();
                        for (int j=0;j<cells.length;j++){
                            cells[j].setBackgroundColor(BaseColor.GRAY);
                        }
                        PdfPCell table_cellan = new PdfPCell(new Phrase(new Chunk(accountname, font)));
                        table_cellan.setUseDescender(true);
                        table_cellan.setHorizontalAlignment(1);
                        table_cellan.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table_cellan.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        table.addCell(table_cellan);

                        statement2 = conn.createStatement();
                        statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "'  AND  YEAR(dt) = YEAR(CURDATE())");
                        ResultSet results1 = statement2.getResultSet();
                        while (results1.next()) {
                            opdb= results1.getInt("sdb");
                            tdb1=tdb1+opdb;
                            opcr= results1.getInt("scr");
                            tcr1=tcr1+opcr;
                            PdfPTable obtable1 = new PdfPTable(new float[] {1,1});
                            PdfPCell table_celld4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opdb), font)));
                            table_celld4.setUseDescender(true);
                            table_celld4.setHorizontalAlignment(1);
                            table_celld4.setRunDirection(3);
                            PdfPCell table_cellc4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opcr), font)));
                            table_cellc4.setUseDescender(true);
                            table_cellc4.setHorizontalAlignment(1);
                            table_cellc4.setRunDirection(3);
                            obtable1.addCell(table_celld4);
                            obtable1.addCell(table_cellc4);
                            table.addCell(obtable1);
                        }
                        statement3 = conn.createStatement();
                        statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND  YEAR(dt) = YEAR(CURDATE())");
                        ResultSet results2 = statement3.getResultSet();
                        while (results2.next()) {
                            tmdb= results2.getInt("sdb");
                            tdb2=tdb2+tmdb;
                            tmcr= results2.getInt("scr");
                            tcr2=tcr2+tmcr;
                            PdfPTable mtable1 = new PdfPTable(new float[] {1,1});
                            PdfPCell table_celld5= new PdfPCell(new Phrase(new Chunk((String.valueOf(tmdb)), font)));
                            table_celld5.setUseDescender(true);
                            table_celld5.setHorizontalAlignment(1);
                            table_celld5.setRunDirection(3);
                            PdfPCell table_cellc5 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tmcr)), font)));
                            table_cellc5.setUseDescender(true);
                            table_cellc5.setHorizontalAlignment(1);
                            table_cellc5.setRunDirection(3);
                            mtable1.addCell(table_celld5);
                            mtable1.addCell(table_cellc5);
                            table.addCell(mtable1);
                        }

                        todb=opdb+tmdb;
                        tdb3=tdb3+todb;
                        tocr=opcr+tmcr;
                        tcr3=tcr3+tocr;
                        balance=(opdb+tmdb)-(opcr+tmcr);
                        tb=tb+balance;
                        table.addCell(String.valueOf(balance));
                        table.setSpacingAfter(10F);
                        document.add(table);


                        try {
                            PdfPTable table2 = new PdfPTable(new float[] { 1, 1, 1, 1, 1, 1, 1, 1 });
                            table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                            table2.addCell(new Phrase(new Chunk("NUM", font)));
                            table2.addCell(new Phrase(new Chunk("PARENT ACCOUNT", font)));
                            table2.addCell(new Phrase(new Chunk("COSTCENTER", font)));
                            table2.addCell(new Phrase(new Chunk("DEBIT", font)));
                            table2.addCell(new Phrase(new Chunk("CREDIT", font)));
                            table2.addCell(new Phrase(new Chunk("DATE", font)));
                            table2.addCell(new Phrase(new Chunk("BY", font)));
                            table2.addCell(new Phrase(new Chunk("NOTES", font)));
                            PdfPCell[] cells1 = table2.getRow(0).getCells();
                            for (int j=0;j<cells1.length;j++){
                                cells1[j].setBackgroundColor(BaseColor.GRAY);
                            }
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            statement1.execute("SELECT * FROM journal  WHERE  post ='yes' AND acc_id='"+results.getString("id")+"' AND  YEAR(dt) = YEAR(CURDATE())");
                            ResultSet results3 = statement1.getResultSet();
                            while (results3.next()) {
                                table2.addCell(new Phrase(new Chunk(results3.getString("num"), font)));
                                statement3 = conn.createStatement();
                                statement3.execute("SELECT * FROM acctree WHERE id='" + results3.getString("parent_id") + "'");
                                ResultSet results5 = statement3.getResultSet();
                                while (results5.next()) {
                                    parentaccountname = results5.getString("title");
                                    PdfPCell table_cellpn = new PdfPCell(new Phrase(new Chunk(parentaccountname, font)));
                                    table_cellpn.setUseDescender(true);
                                    table_cellpn.setHorizontalAlignment(1);
                                    table_cellpn.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table_cellpn.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                                    table2.addCell(table_cellpn);
                                }
                                statement4 = conn.createStatement();
                                statement4.execute("select * from projects WHERE id= '" + results3.getString("costcenter_id") + "'");
                                ResultSet results6 = statement4.getResultSet();
                                while (results6.next()) {
                                    costcenter = String.valueOf(results6.getString("name"));
                                    PdfPCell table_cellcc = new PdfPCell(new Phrase(new Chunk(costcenter, font)));
                                    table_cellcc.setUseDescender(true);
                                    table_cellcc.setHorizontalAlignment(1);
                                    table_cellcc.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table_cellcc.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                                    table2.addCell(table_cellcc);
                                }
                                table2.addCell(new Phrase(new Chunk(results3.getString("debit"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("credit"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("dt"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("user"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("notes"), font)));
                            }
                            table2.setSpacingAfter(10F);
                            document.add(table2);
                        } catch (Exception e) {
                            System.out.print(e);
                        }
                        LineSeparator ls3 = new LineSeparator();
                        document.add(new Chunk(ls3));
                    }
                    document.close();
                    System.out.println("Done");
                    conn.close();
                } catch (Exception e) {
                    System.out.print(e);
                }
            } else if(date.getValue().equals("LAST MONTH")){
                FontFactory.registerDirectories();
                FontFactory.register("c:/windows/fonts/tradbdo.ttf", "my_arabic");
                Font font = FontFactory.getFont("my_arabic" ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED,10f);;
                DateTimeFormatter t = DateTimeFormatter.ofPattern("hh:mm a");
                LocalDateTime now1 = LocalDateTime.now();
                this.hour = t.format(now1);
                DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDateTime now = LocalDateTime.now();
                this.dt = dtr.format(now);
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream("sample3.pdf"));
                document.open();
                Paragraph Title = new Paragraph("  ELFOUAD COMPANY REPORTINGS ");
                Title.setAlignment(1);
                Title.setSpacingAfter(5F);
                document.add(Title);
                Paragraph type = new Paragraph("TRAIL BALANCE");
                type.setAlignment(1);
                type.setSpacingAfter(5F);
                document.add(type);
                Paragraph detailsd = new Paragraph(this.dt+" "+this.hour);
                detailsd.setAlignment(1);
                detailsd.setSpacingAfter(5.0F);
                document.add(detailsd);
                Paragraph details = new Paragraph("FOR"+" : "+month+"-"+year);
                details.setAlignment(1);
                details.setSpacingAfter(5.0F);
                document.add(details);
                Image companyLogo = Image.getInstance("/vulcan/src/assetes/reportlogo.png");
                companyLogo.scalePercent(7);
                companyLogo.setSpacingAfter(50F);
                companyLogo.setAbsolutePosition(0, PageSize.A4.getHeight() - companyLogo.getScaledHeight()+20);
                document.add(companyLogo);


                try {
                    conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    statement1 = conn.createStatement();
                    if(level.getValue().equals("ALL")){
                        statement1.execute("SELECT * FROM acctree ");
                    }else {
                        statement1.execute("SELECT * FROM acctree WHERE level='"+level.getValue()+"' ");
                    }
                    ResultSet results = statement1.getResultSet();
                    while (results.next()) {
                        accountname= results.getString("title");

                        PdfPTable table = new PdfPTable(new float[] { 2, 2, 2, 2 });
                        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell("ACCOUNT");
                        table.addCell("OPENING BALANCE");
                        table.addCell(month+"-"+year);
                        table.addCell("BALANCE");
                        table.addCell("       ");
                        PdfPTable obtable = new PdfPTable(new float[] {1,1});
                        obtable.getDefaultCell().setBorder(0);
                        PdfPCell table_celld2 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld2.setHorizontalAlignment(1);
                        table_celld2.setBorder(0);
                        PdfPCell table_cellc2 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc2.setHorizontalAlignment(1);
                        table_cellc2.setBorder(0);
                        obtable.addCell(table_celld2);
                        obtable.addCell(table_cellc2);
                        table.addCell( obtable);
                        PdfPTable mtable = new PdfPTable(new float[] {1,1});
                        mtable.getDefaultCell().setBorder(0);
                        PdfPCell table_celld1 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld1.setHorizontalAlignment(1);
                        table_celld1.setBorder(0);
                        PdfPCell table_cellc1 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc1.setHorizontalAlignment(1);
                        table_cellc1.setBorder(0);
                        mtable.addCell(table_celld1);
                        mtable.addCell(table_cellc1);
                        table.addCell(mtable);
                        table.addCell("       ");
                        table.setSpacingBefore(10F);
                        table.setHeaderRows(1);
                        PdfPCell[] cells = table.getRow(0).getCells();
                        for (int j=0;j<cells.length;j++){
                            cells[j].setBackgroundColor(BaseColor.GRAY);
                        }
                        PdfPCell table_cellan = new PdfPCell(new Phrase(new Chunk(accountname, font)));
                        table_cellan.setUseDescender(true);
                        table_cellan.setHorizontalAlignment(1);
                        table_cellan.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table_cellan.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        table.addCell(table_cellan);

                        statement2 = conn.createStatement();
                        statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "'  AND  YEAR(dt) = YEAR(CURDATE())");
                        ResultSet results1 = statement2.getResultSet();
                        while (results1.next()) {
                            opdb= results1.getInt("sdb");
                            tdb1=tdb1+opdb;
                            opcr= results1.getInt("scr");
                            tcr1=tcr1+opcr;
                            PdfPTable obtable1 = new PdfPTable(new float[] {1,1});
                            PdfPCell table_celld4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opdb), font)));
                            table_celld4.setUseDescender(true);
                            table_celld4.setHorizontalAlignment(1);
                            table_celld4.setRunDirection(3);
                            PdfPCell table_cellc4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opcr), font)));
                            table_cellc4.setUseDescender(true);
                            table_cellc4.setHorizontalAlignment(1);
                            table_cellc4.setRunDirection(3);
                            obtable1.addCell(table_celld4);
                            obtable1.addCell(table_cellc4);
                            table.addCell(obtable1);
                        }
                        statement3 = conn.createStatement();
                        statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND MONTH(dt) = MONTH(CURRENT_DATE - INTERVAL 1 MONTH)");
                        ResultSet results2 = statement3.getResultSet();
                        while (results2.next()) {
                            tmdb= results2.getInt("sdb");
                            tdb2=tdb2+tmdb;
                            tmcr= results2.getInt("scr");
                            tcr2=tcr2+tmcr;
                            PdfPTable mtable1 = new PdfPTable(new float[] {1,1});
                            PdfPCell table_celld5= new PdfPCell(new Phrase(new Chunk((String.valueOf(tmdb)), font)));
                            table_celld5.setUseDescender(true);
                            table_celld5.setHorizontalAlignment(1);
                            table_celld5.setRunDirection(3);
                            PdfPCell table_cellc5 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tmcr)), font)));
                            table_cellc5.setUseDescender(true);
                            table_cellc5.setHorizontalAlignment(1);
                            table_cellc5.setRunDirection(3);
                            mtable1.addCell(table_celld5);
                            mtable1.addCell(table_cellc5);
                            table.addCell(mtable1);
                        }

                        todb=opdb+tmdb;
                        tdb3=tdb3+todb;
                        tocr=opcr+tmcr;
                        tcr3=tcr3+tocr;
                        balance=(opdb+tmdb)-(opcr+tmcr);
                        tb=tb+balance;
                        table.addCell(String.valueOf(balance));
                        table.setSpacingAfter(10F);
                        document.add(table);


                        try {
                            PdfPTable table2 = new PdfPTable(new float[] { 1, 1, 1, 1, 1, 1, 1, 1 });
                            table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                            table2.addCell(new Phrase(new Chunk("NUM", font)));
                            table2.addCell(new Phrase(new Chunk("PARENT ACCOUNT", font)));
                            table2.addCell(new Phrase(new Chunk("COSTCENTER", font)));
                            table2.addCell(new Phrase(new Chunk("DEBIT", font)));
                            table2.addCell(new Phrase(new Chunk("CREDIT", font)));
                            table2.addCell(new Phrase(new Chunk("DATE", font)));
                            table2.addCell(new Phrase(new Chunk("BY", font)));
                            table2.addCell(new Phrase(new Chunk("NOTES", font)));
                            PdfPCell[] cells1 = table2.getRow(0).getCells();
                            for (int j=0;j<cells1.length;j++){
                                cells1[j].setBackgroundColor(BaseColor.GRAY);
                            }
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            statement1.execute("SELECT * FROM journal  WHERE  post ='yes' AND acc_id='"+results.getString("id")+"'");
                            ResultSet results3 = statement1.getResultSet();
                            while (results3.next()) {
                                table2.addCell(new Phrase(new Chunk(results3.getString("num"), font)));
                                statement3 = conn.createStatement();
                                statement3.execute("SELECT * FROM acctree WHERE id='" + results3.getString("parent_id") + "'");
                                ResultSet results5 = statement3.getResultSet();
                                while (results5.next()) {
                                    parentaccountname = results5.getString("title");
                                    PdfPCell table_cellpn = new PdfPCell(new Phrase(new Chunk(parentaccountname, font)));
                                    table_cellpn.setUseDescender(true);
                                    table_cellpn.setHorizontalAlignment(1);
                                    table_cellpn.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table_cellpn.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                                    table2.addCell(table_cellpn);
                                }
                                statement4 = conn.createStatement();
                                statement4.execute("select * from projects WHERE id= '" + results3.getString("costcenter_id") + "'");
                                ResultSet results6 = statement4.getResultSet();
                                while (results6.next()) {
                                    costcenter = String.valueOf(results6.getString("name"));
                                    PdfPCell table_cellcc = new PdfPCell(new Phrase(new Chunk(costcenter, font)));
                                    table_cellcc.setUseDescender(true);
                                    table_cellcc.setHorizontalAlignment(1);
                                    table_cellcc.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table_cellcc.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                                    table2.addCell(table_cellcc);
                                }
                                table2.addCell(new Phrase(new Chunk(results3.getString("debit"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("credit"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("dt"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("user"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("notes"), font)));
                            }
                            table2.setSpacingAfter(10F);
                            document.add(table2);
                        } catch (Exception e) {
                            System.out.print(e);
                        }
                        LineSeparator ls3 = new LineSeparator();
                        document.add(new Chunk(ls3));
                    }
                    document.close();
                    System.out.println("Done");
                    conn.close();
                } catch (Exception e) {
                    System.out.print(e);
                }
            } else if(date.getValue().equals("LAST QUARTER")){
                FontFactory.registerDirectories();
                FontFactory.register("c:/windows/fonts/tradbdo.ttf", "my_arabic");
                Font font = FontFactory.getFont("my_arabic" ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED,10f);;
                DateTimeFormatter t = DateTimeFormatter.ofPattern("hh:mm a");
                LocalDateTime now1 = LocalDateTime.now();
                this.hour = t.format(now1);
                DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDateTime now = LocalDateTime.now();
                this.dt = dtr.format(now);
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream("sample3.pdf"));
                document.open();
                Paragraph Title = new Paragraph("  ELFOUAD COMPANY REPORTINGS ");
                Title.setAlignment(1);
                Title.setSpacingAfter(5F);
                document.add(Title);
                Paragraph type = new Paragraph("TRAIL BALANCE");
                type.setAlignment(1);
                type.setSpacingAfter(5F);
                document.add(type);
                Paragraph detailsd = new Paragraph(this.dt+" "+this.hour);
                detailsd.setAlignment(1);
                detailsd.setSpacingAfter(5.0F);
                document.add(detailsd);
                Paragraph details = new Paragraph("FOR"+" : "+month+"-"+year);
                details.setAlignment(1);
                details.setSpacingAfter(5.0F);
                document.add(details);
                Image companyLogo = Image.getInstance("/vulcan/src/assetes/reportlogo.png");
                companyLogo.scalePercent(7);
                companyLogo.setSpacingAfter(50F);
                companyLogo.setAbsolutePosition(0, PageSize.A4.getHeight() - companyLogo.getScaledHeight()+20);
                document.add(companyLogo);


                try {
                    conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                    statement1 = conn.createStatement();
                    if(level.getValue().equals("ALL")){
                        statement1.execute("SELECT * FROM acctree ");
                    }else {
                        statement1.execute("SELECT * FROM acctree WHERE level='"+level.getValue()+"' ");
                    }
                    ResultSet results = statement1.getResultSet();
                    while (results.next()) {
                        accountname= results.getString("title");

                        PdfPTable table = new PdfPTable(new float[] { 2, 2, 2, 2 });
                        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell("ACCOUNT");
                        table.addCell("OPENING BALANCE");
                        table.addCell(month+"-"+year);
                        table.addCell("BALANCE");
                        table.addCell("       ");
                        PdfPTable obtable = new PdfPTable(new float[] {1,1});
                        obtable.getDefaultCell().setBorder(0);
                        PdfPCell table_celld2 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld2.setHorizontalAlignment(1);
                        table_celld2.setBorder(0);
                        PdfPCell table_cellc2 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc2.setHorizontalAlignment(1);
                        table_cellc2.setBorder(0);
                        obtable.addCell(table_celld2);
                        obtable.addCell(table_cellc2);
                        table.addCell( obtable);
                        PdfPTable mtable = new PdfPTable(new float[] {1,1});
                        mtable.getDefaultCell().setBorder(0);
                        PdfPCell table_celld1 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld1.setHorizontalAlignment(1);
                        table_celld1.setBorder(0);
                        PdfPCell table_cellc1 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc1.setHorizontalAlignment(1);
                        table_cellc1.setBorder(0);
                        mtable.addCell(table_celld1);
                        mtable.addCell(table_cellc1);
                        table.addCell(mtable);
                        table.addCell("       ");
                        table.setSpacingBefore(10F);
                        table.setHeaderRows(1);
                        PdfPCell[] cells = table.getRow(0).getCells();
                        for (int j=0;j<cells.length;j++){
                            cells[j].setBackgroundColor(BaseColor.GRAY);
                        }
                        PdfPCell table_cellan = new PdfPCell(new Phrase(new Chunk(accountname, font)));
                        table_cellan.setUseDescender(true);
                        table_cellan.setHorizontalAlignment(1);
                        table_cellan.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table_cellan.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        table.addCell(table_cellan);

                        statement2 = conn.createStatement();
                        statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "'  AND  YEAR(dt) = YEAR(CURDATE())");
                        ResultSet results1 = statement2.getResultSet();
                        while (results1.next()) {
                            opdb= results1.getInt("sdb");
                            tdb1=tdb1+opdb;
                            opcr= results1.getInt("scr");
                            tcr1=tcr1+opcr;
                            PdfPTable obtable1 = new PdfPTable(new float[] {1,1});
                            PdfPCell table_celld4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opdb), font)));
                            table_celld4.setUseDescender(true);
                            table_celld4.setHorizontalAlignment(1);
                            table_celld4.setRunDirection(3);
                            PdfPCell table_cellc4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opcr), font)));
                            table_cellc4.setUseDescender(true);
                            table_cellc4.setHorizontalAlignment(1);
                            table_cellc4.setRunDirection(3);
                            obtable1.addCell(table_celld4);
                            obtable1.addCell(table_cellc4);
                            table.addCell(obtable1);
                        }
                        statement3 = conn.createStatement();
                        statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"'  AND QUARTER(dt) = QUARTER(curdate() - INTERVAL 1 quarter)");
                        ResultSet results2 = statement3.getResultSet();
                        while (results2.next()) {
                            tmdb= results2.getInt("sdb");
                            tdb2=tdb2+tmdb;
                            tmcr= results2.getInt("scr");
                            tcr2=tcr2+tmcr;
                            PdfPTable mtable1 = new PdfPTable(new float[] {1,1});
                            PdfPCell table_celld5= new PdfPCell(new Phrase(new Chunk((String.valueOf(tmdb)), font)));
                            table_celld5.setUseDescender(true);
                            table_celld5.setHorizontalAlignment(1);
                            table_celld5.setRunDirection(3);
                            PdfPCell table_cellc5 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tmcr)), font)));
                            table_cellc5.setUseDescender(true);
                            table_cellc5.setHorizontalAlignment(1);
                            table_cellc5.setRunDirection(3);
                            mtable1.addCell(table_celld5);
                            mtable1.addCell(table_cellc5);
                            table.addCell(mtable1);
                        }

                        todb=opdb+tmdb;
                        tdb3=tdb3+todb;
                        tocr=opcr+tmcr;
                        tcr3=tcr3+tocr;
                        balance=(opdb+tmdb)-(opcr+tmcr);
                        tb=tb+balance;
                        table.addCell(String.valueOf(balance));
                        table.setSpacingAfter(10F);
                        document.add(table);


                        try {
                            PdfPTable table2 = new PdfPTable(new float[] { 1, 1, 1, 1, 1, 1, 1, 1 });
                            table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                            table2.addCell(new Phrase(new Chunk("NUM", font)));
                            table2.addCell(new Phrase(new Chunk("PARENT ACCOUNT", font)));
                            table2.addCell(new Phrase(new Chunk("COSTCENTER", font)));
                            table2.addCell(new Phrase(new Chunk("DEBIT", font)));
                            table2.addCell(new Phrase(new Chunk("CREDIT", font)));
                            table2.addCell(new Phrase(new Chunk("DATE", font)));
                            table2.addCell(new Phrase(new Chunk("BY", font)));
                            table2.addCell(new Phrase(new Chunk("NOTES", font)));
                            PdfPCell[] cells1 = table2.getRow(0).getCells();
                            for (int j=0;j<cells1.length;j++){
                                cells1[j].setBackgroundColor(BaseColor.GRAY);
                            }
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement1 = conn.createStatement();
                            statement1.execute("SELECT * FROM journal  WHERE  post ='yes' AND acc_id='"+results.getString("id")+"'");
                            ResultSet results3 = statement1.getResultSet();
                            while (results3.next()) {
                                table2.addCell(new Phrase(new Chunk(results3.getString("num"), font)));
                                statement3 = conn.createStatement();
                                statement3.execute("SELECT * FROM acctree WHERE id='" + results3.getString("parent_id") + "'");
                                ResultSet results5 = statement3.getResultSet();
                                while (results5.next()) {
                                    parentaccountname = results5.getString("title");
                                    PdfPCell table_cellpn = new PdfPCell(new Phrase(new Chunk(parentaccountname, font)));
                                    table_cellpn.setUseDescender(true);
                                    table_cellpn.setHorizontalAlignment(1);
                                    table_cellpn.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table_cellpn.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                                    table2.addCell(table_cellpn);
                                }
                                statement4 = conn.createStatement();
                                statement4.execute("select * from projects WHERE id= '" + results3.getString("costcenter_id") + "'");
                                ResultSet results6 = statement4.getResultSet();
                                while (results6.next()) {
                                    costcenter = String.valueOf(results6.getString("name"));
                                    PdfPCell table_cellcc = new PdfPCell(new Phrase(new Chunk(costcenter, font)));
                                    table_cellcc.setUseDescender(true);
                                    table_cellcc.setHorizontalAlignment(1);
                                    table_cellcc.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    table_cellcc.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                                    table2.addCell(table_cellcc);
                                }
                                table2.addCell(new Phrase(new Chunk(results3.getString("debit"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("credit"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("dt"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("user"), font)));
                                table2.addCell(new Phrase(new Chunk(results3.getString("notes"), font)));
                            }
                            table2.setSpacingAfter(10F);
                            document.add(table2);
                        } catch (Exception e) {
                            System.out.print(e);
                        }
                        LineSeparator ls3 = new LineSeparator();
                        document.add(new Chunk(ls3));
                    }
                    document.close();
                    System.out.println("Done");
                    conn.close();
                } catch (Exception e) {
                    System.out.print(e);
                }
            }if(date.getValue().equals("LAST YEAR")){
           FontFactory.registerDirectories();
           FontFactory.register("c:/windows/fonts/tradbdo.ttf", "my_arabic");
           Font font = FontFactory.getFont("my_arabic" ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED,10f);;
           DateTimeFormatter t = DateTimeFormatter.ofPattern("hh:mm a");
           LocalDateTime now1 = LocalDateTime.now();
           this.hour = t.format(now1);
           DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd-MM-yyyy");
           LocalDateTime now = LocalDateTime.now();
           this.dt = dtr.format(now);
           Document document = new Document();
           PdfWriter.getInstance(document, new FileOutputStream("sample3.pdf"));
           document.open();
           Paragraph Title = new Paragraph("  ELFOUAD COMPANY REPORTINGS ");
           Title.setAlignment(1);
           Title.setSpacingAfter(5F);
           document.add(Title);
           Paragraph type = new Paragraph("TRAIL BALANCE");
           type.setAlignment(1);
           type.setSpacingAfter(5F);
           document.add(type);
           Paragraph detailsd = new Paragraph(this.dt+" "+this.hour);
           detailsd.setAlignment(1);
           detailsd.setSpacingAfter(5.0F);
           document.add(detailsd);
           Paragraph details = new Paragraph("FOR"+" : "+month+"-"+year);
           details.setAlignment(1);
           details.setSpacingAfter(5.0F);
           document.add(details);
           Image companyLogo = Image.getInstance("/vulcan/src/assetes/reportlogo.png");
           companyLogo.scalePercent(7);
           companyLogo.setSpacingAfter(50F);
           companyLogo.setAbsolutePosition(0, PageSize.A4.getHeight() - companyLogo.getScaledHeight()+20);
           document.add(companyLogo);


           try {
               conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
               statement1 = conn.createStatement();
               if(level.getValue().equals("ALL")){
                   statement1.execute("SELECT * FROM acctree ");
               }else {
                   statement1.execute("SELECT * FROM acctree WHERE level='"+level.getValue()+"' ");
               }
               ResultSet results = statement1.getResultSet();
               while (results.next()) {
                   accountname= results.getString("title");

                   PdfPTable table = new PdfPTable(new float[] { 2, 2, 2, 2 });
                   table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                   table.addCell("ACCOUNT");
                   table.addCell("OPENING BALANCE");
                   table.addCell(month+"-"+year);
                   table.addCell("BALANCE");
                   table.addCell("       ");
                   PdfPTable obtable = new PdfPTable(new float[] {1,1});
                   obtable.getDefaultCell().setBorder(0);
                   PdfPCell table_celld2 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                   table_celld2.setHorizontalAlignment(1);
                   table_celld2.setBorder(0);
                   PdfPCell table_cellc2 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                   table_cellc2.setHorizontalAlignment(1);
                   table_cellc2.setBorder(0);
                   obtable.addCell(table_celld2);
                   obtable.addCell(table_cellc2);
                   table.addCell( obtable);
                   PdfPTable mtable = new PdfPTable(new float[] {1,1});
                   mtable.getDefaultCell().setBorder(0);
                   PdfPCell table_celld1 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                   table_celld1.setHorizontalAlignment(1);
                   table_celld1.setBorder(0);
                   PdfPCell table_cellc1 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                   table_cellc1.setHorizontalAlignment(1);
                   table_cellc1.setBorder(0);
                   mtable.addCell(table_celld1);
                   mtable.addCell(table_cellc1);
                   table.addCell(mtable);
                   table.addCell("       ");
                   table.setSpacingBefore(10F);
                   table.setHeaderRows(1);
                   PdfPCell[] cells = table.getRow(0).getCells();
                   for (int j=0;j<cells.length;j++){
                       cells[j].setBackgroundColor(BaseColor.GRAY);
                   }
                   PdfPCell table_cellan = new PdfPCell(new Phrase(new Chunk(accountname, font)));
                   table_cellan.setUseDescender(true);
                   table_cellan.setHorizontalAlignment(1);
                   table_cellan.setHorizontalAlignment(Element.ALIGN_CENTER);
                   table_cellan.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                   table.addCell(table_cellan);

                   statement2 = conn.createStatement();
                   statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "'  AND YEAR(dt) = YEAR(CURRENT_DATE - INTERVAL 1 YEAR)");
                   ResultSet results1 = statement2.getResultSet();
                   while (results1.next()) {
                       opdb= results1.getInt("sdb");
                       tdb1=tdb1+opdb;
                       opcr= results1.getInt("scr");
                       tcr1=tcr1+opcr;
                       PdfPTable obtable1 = new PdfPTable(new float[] {1,1});
                       PdfPCell table_celld4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opdb), font)));
                       table_celld4.setUseDescender(true);
                       table_celld4.setHorizontalAlignment(1);
                       table_celld4.setRunDirection(3);
                       PdfPCell table_cellc4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opcr), font)));
                       table_cellc4.setUseDescender(true);
                       table_cellc4.setHorizontalAlignment(1);
                       table_cellc4.setRunDirection(3);
                       obtable1.addCell(table_celld4);
                       obtable1.addCell(table_cellc4);
                       table.addCell(obtable1);
                   }
                   statement3 = conn.createStatement();
                   statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND YEAR(dt) = YEAR(CURRENT_DATE - INTERVAL 1 YEAR)");
                   ResultSet results2 = statement3.getResultSet();
                   while (results2.next()) {
                       tmdb= results2.getInt("sdb");
                       tdb2=tdb2+tmdb;
                       tmcr= results2.getInt("scr");
                       tcr2=tcr2+tmcr;
                       PdfPTable mtable1 = new PdfPTable(new float[] {1,1});
                       PdfPCell table_celld5= new PdfPCell(new Phrase(new Chunk((String.valueOf(tmdb)), font)));
                       table_celld5.setUseDescender(true);
                       table_celld5.setHorizontalAlignment(1);
                       table_celld5.setRunDirection(3);
                       PdfPCell table_cellc5 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tmcr)), font)));
                       table_cellc5.setUseDescender(true);
                       table_cellc5.setHorizontalAlignment(1);
                       table_cellc5.setRunDirection(3);
                       mtable1.addCell(table_celld5);
                       mtable1.addCell(table_cellc5);
                       table.addCell(mtable1);
                   }

                   todb=opdb+tmdb;
                   tdb3=tdb3+todb;
                   tocr=opcr+tmcr;
                   tcr3=tcr3+tocr;
                   balance=(opdb+tmdb)-(opcr+tmcr);
                   tb=tb+balance;
                   table.addCell(String.valueOf(balance));
                   table.setSpacingAfter(10F);
                   document.add(table);


                   try {
                       PdfPTable table2 = new PdfPTable(new float[] { 1, 1, 1, 1, 1, 1, 1, 1 });
                       table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                       table2.addCell(new Phrase(new Chunk("NUM", font)));
                       table2.addCell(new Phrase(new Chunk("PARENT ACCOUNT", font)));
                       table2.addCell(new Phrase(new Chunk("COSTCENTER", font)));
                       table2.addCell(new Phrase(new Chunk("DEBIT", font)));
                       table2.addCell(new Phrase(new Chunk("CREDIT", font)));
                       table2.addCell(new Phrase(new Chunk("DATE", font)));
                       table2.addCell(new Phrase(new Chunk("BY", font)));
                       table2.addCell(new Phrase(new Chunk("NOTES", font)));
                       PdfPCell[] cells1 = table2.getRow(0).getCells();
                       for (int j=0;j<cells1.length;j++){
                           cells1[j].setBackgroundColor(BaseColor.GRAY);
                       }
                       conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                       statement1 = conn.createStatement();
                       statement1.execute("SELECT * FROM journal  WHERE  post ='yes' AND acc_id='"+results.getString("id")+"'");
                       ResultSet results3 = statement1.getResultSet();
                       while (results3.next()) {
                           table2.addCell(new Phrase(new Chunk(results3.getString("num"), font)));
                           statement3 = conn.createStatement();
                           statement3.execute("SELECT * FROM acctree WHERE id='" + results3.getString("parent_id") + "'");
                           ResultSet results5 = statement3.getResultSet();
                           while (results5.next()) {
                               parentaccountname = results5.getString("title");
                               PdfPCell table_cellpn = new PdfPCell(new Phrase(new Chunk(parentaccountname, font)));
                               table_cellpn.setUseDescender(true);
                               table_cellpn.setHorizontalAlignment(1);
                               table_cellpn.setHorizontalAlignment(Element.ALIGN_CENTER);
                               table_cellpn.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                               table2.addCell(table_cellpn);
                           }
                           statement4 = conn.createStatement();
                           statement4.execute("select * from projects WHERE id= '" + results3.getString("costcenter_id") + "'");
                           ResultSet results6 = statement4.getResultSet();
                           while (results6.next()) {
                               costcenter = String.valueOf(results6.getString("name"));
                               PdfPCell table_cellcc = new PdfPCell(new Phrase(new Chunk(costcenter, font)));
                               table_cellcc.setUseDescender(true);
                               table_cellcc.setHorizontalAlignment(1);
                               table_cellcc.setHorizontalAlignment(Element.ALIGN_CENTER);
                               table_cellcc.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                               table2.addCell(table_cellcc);
                           }
                           table2.addCell(new Phrase(new Chunk(results3.getString("debit"), font)));
                           table2.addCell(new Phrase(new Chunk(results3.getString("credit"), font)));
                           table2.addCell(new Phrase(new Chunk(results3.getString("dt"), font)));
                           table2.addCell(new Phrase(new Chunk(results3.getString("user"), font)));
                           table2.addCell(new Phrase(new Chunk(results3.getString("notes"), font)));
                       }
                       table2.setSpacingAfter(10F);
                       document.add(table2);
                   } catch (Exception e) {
                       System.out.print(e);
                   }
                   LineSeparator ls3 = new LineSeparator();
                   document.add(new Chunk(ls3));
               }
               document.close();
               System.out.println("Done");
               conn.close();
           } catch (Exception e) {
               System.out.print(e);
           }
       }if(date.getValue().equals("SPECIFIC")){
           FontFactory.registerDirectories();
           FontFactory.register("c:/windows/fonts/tradbdo.ttf", "my_arabic");
           Font font = FontFactory.getFont("my_arabic" ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED,10f);;
           DateTimeFormatter t = DateTimeFormatter.ofPattern("hh:mm a");
           LocalDateTime now1 = LocalDateTime.now();
           this.hour = t.format(now1);
           DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd-MM-yyyy");
           LocalDateTime now = LocalDateTime.now();
           this.dt = dtr.format(now);
           Document document = new Document();
           PdfWriter.getInstance(document, new FileOutputStream("sample3.pdf"));
           document.open();
           Paragraph Title = new Paragraph("  ELFOUAD COMPANY REPORTINGS ");
           Title.setAlignment(1);
           Title.setSpacingAfter(5F);
           document.add(Title);
           Paragraph type = new Paragraph("TRAIL BALANCE");
           type.setAlignment(1);
           type.setSpacingAfter(5F);
           document.add(type);
           Paragraph detailsd = new Paragraph(this.dt+" "+this.hour);
           detailsd.setAlignment(1);
           detailsd.setSpacingAfter(5.0F);
           document.add(detailsd);
           Paragraph details = new Paragraph("FOR"+" : "+month+"-"+year);
           details.setAlignment(1);
           details.setSpacingAfter(5.0F);
           document.add(details);
           Image companyLogo = Image.getInstance("/vulcan/src/assetes/reportlogo.png");
           companyLogo.scalePercent(7);
           companyLogo.setSpacingAfter(50F);
           companyLogo.setAbsolutePosition(0, PageSize.A4.getHeight() - companyLogo.getScaledHeight()+20);
           document.add(companyLogo);


           try {
               conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
               statement1 = conn.createStatement();
               if(level.getValue().equals("ALL")){
                   statement1.execute("SELECT * FROM acctree ");
               }else {
                   statement1.execute("SELECT * FROM acctree WHERE level='"+level.getValue()+"' ");
               }
               ResultSet results = statement1.getResultSet();
               while (results.next()) {
                   accountname= results.getString("title");

                   PdfPTable table = new PdfPTable(new float[] { 2, 2, 2, 2 });
                   table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                   table.addCell("ACCOUNT");
                   table.addCell("OPENING BALANCE");
                   table.addCell(month+"-"+year);
                   table.addCell("BALANCE");
                   table.addCell("       ");
                   PdfPTable obtable = new PdfPTable(new float[] {1,1});
                   obtable.getDefaultCell().setBorder(0);
                   PdfPCell table_celld2 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                   table_celld2.setHorizontalAlignment(1);
                   table_celld2.setBorder(0);
                   PdfPCell table_cellc2 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                   table_cellc2.setHorizontalAlignment(1);
                   table_cellc2.setBorder(0);
                   obtable.addCell(table_celld2);
                   obtable.addCell(table_cellc2);
                   table.addCell( obtable);
                   PdfPTable mtable = new PdfPTable(new float[] {1,1});
                   mtable.getDefaultCell().setBorder(0);
                   PdfPCell table_celld1 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                   table_celld1.setHorizontalAlignment(1);
                   table_celld1.setBorder(0);
                   PdfPCell table_cellc1 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                   table_cellc1.setHorizontalAlignment(1);
                   table_cellc1.setBorder(0);
                   mtable.addCell(table_celld1);
                   mtable.addCell(table_cellc1);
                   table.addCell(mtable);
                   table.addCell("       ");
                   table.setSpacingBefore(10F);
                   table.setHeaderRows(1);
                   PdfPCell[] cells = table.getRow(0).getCells();
                   for (int j=0;j<cells.length;j++){
                       cells[j].setBackgroundColor(BaseColor.GRAY);
                   }
                   PdfPCell table_cellan = new PdfPCell(new Phrase(new Chunk(accountname, font)));
                   table_cellan.setUseDescender(true);
                   table_cellan.setHorizontalAlignment(1);
                   table_cellan.setHorizontalAlignment(Element.ALIGN_CENTER);
                   table_cellan.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                   table.addCell(table_cellan);

                   statement2 = conn.createStatement();
                   statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "'  AND  YEAR(dt) = YEAR(CURDATE())");
                   ResultSet results1 = statement2.getResultSet();
                   while (results1.next()) {
                       opdb= results1.getInt("sdb");
                       tdb1=tdb1+opdb;
                       opcr= results1.getInt("scr");
                       tcr1=tcr1+opcr;
                       PdfPTable obtable1 = new PdfPTable(new float[] {1,1});
                       PdfPCell table_celld4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opdb), font)));
                       table_celld4.setUseDescender(true);
                       table_celld4.setHorizontalAlignment(1);
                       table_celld4.setRunDirection(3);
                       PdfPCell table_cellc4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opcr), font)));
                       table_cellc4.setUseDescender(true);
                       table_cellc4.setHorizontalAlignment(1);
                       table_cellc4.setRunDirection(3);
                       obtable1.addCell(table_celld4);
                       obtable1.addCell(table_cellc4);
                       table.addCell(obtable1);
                   }
                   statement3 = conn.createStatement();
                   statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND month(dt)='"+key1+"' AND YEAR(dt)='"+key2+"' ");
                   ResultSet results2 = statement3.getResultSet();
                   while (results2.next()) {
                       tmdb= results2.getInt("sdb");
                       tdb2=tdb2+tmdb;
                       tmcr= results2.getInt("scr");
                       tcr2=tcr2+tmcr;
                       PdfPTable mtable1 = new PdfPTable(new float[] {1,1});
                       PdfPCell table_celld5= new PdfPCell(new Phrase(new Chunk((String.valueOf(tmdb)), font)));
                       table_celld5.setUseDescender(true);
                       table_celld5.setHorizontalAlignment(1);
                       table_celld5.setRunDirection(3);
                       PdfPCell table_cellc5 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tmcr)), font)));
                       table_cellc5.setUseDescender(true);
                       table_cellc5.setHorizontalAlignment(1);
                       table_cellc5.setRunDirection(3);
                       mtable1.addCell(table_celld5);
                       mtable1.addCell(table_cellc5);
                       table.addCell(mtable1);
                   }
                   todb=opdb+tmdb;
                   tdb3=tdb3+todb;
                   tocr=opcr+tmcr;
                   tcr3=tcr3+tocr;
                   balance=(opdb+tmdb)-(opcr+tmcr);
                   tb=tb+balance;
                   table.addCell(String.valueOf(balance));
                   table.setSpacingAfter(10F);
                   document.add(table);
                   try {
                       PdfPTable table2 = new PdfPTable(new float[] { 1, 1, 1, 1, 1, 1, 1, 1 });
                       table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                       table2.addCell(new Phrase(new Chunk("NUM", font)));
                       table2.addCell(new Phrase(new Chunk("PARENT ACCOUNT", font)));
                       table2.addCell(new Phrase(new Chunk("COSTCENTER", font)));
                       table2.addCell(new Phrase(new Chunk("DEBIT", font)));
                       table2.addCell(new Phrase(new Chunk("CREDIT", font)));
                       table2.addCell(new Phrase(new Chunk("DATE", font)));
                       table2.addCell(new Phrase(new Chunk("BY", font)));
                       table2.addCell(new Phrase(new Chunk("NOTES", font)));
                       PdfPCell[] cells1 = table2.getRow(0).getCells();
                       for (int j=0;j<cells1.length;j++){
                           cells1[j].setBackgroundColor(BaseColor.GRAY);
                       }
                       conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                       statement1 = conn.createStatement();
                       statement1.execute("SELECT * FROM journal  WHERE  post ='yes' AND acc_id='"+results.getString("id")+"'");
                       ResultSet results3 = statement1.getResultSet();
                       while (results3.next()) {
                           table2.addCell(new Phrase(new Chunk(results3.getString("num"), font)));
                           statement3 = conn.createStatement();
                           statement3.execute("SELECT * FROM acctree WHERE id='" + results3.getString("parent_id") + "'");
                           ResultSet results5 = statement3.getResultSet();
                           while (results5.next()) {
                               parentaccountname = results5.getString("title");
                               PdfPCell table_cellpn = new PdfPCell(new Phrase(new Chunk(parentaccountname, font)));
                               table_cellpn.setUseDescender(true);
                               table_cellpn.setHorizontalAlignment(1);
                               table_cellpn.setHorizontalAlignment(Element.ALIGN_CENTER);
                               table_cellpn.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                               table2.addCell(table_cellpn);
                           }
                           statement4 = conn.createStatement();
                           statement4.execute("select * from projects WHERE id= '" + results3.getString("costcenter_id") + "'");
                           ResultSet results6 = statement4.getResultSet();
                           while (results6.next()) {
                               costcenter = String.valueOf(results6.getString("name"));
                               PdfPCell table_cellcc = new PdfPCell(new Phrase(new Chunk(costcenter, font)));
                               table_cellcc.setUseDescender(true);
                               table_cellcc.setHorizontalAlignment(1);
                               table_cellcc.setHorizontalAlignment(Element.ALIGN_CENTER);
                               table_cellcc.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                               table2.addCell(table_cellcc);
                           }
                           table2.addCell(new Phrase(new Chunk(results3.getString("debit"), font)));
                           table2.addCell(new Phrase(new Chunk(results3.getString("credit"), font)));
                           table2.addCell(new Phrase(new Chunk(results3.getString("dt"), font)));
                           table2.addCell(new Phrase(new Chunk(results3.getString("user"), font)));
                           table2.addCell(new Phrase(new Chunk(results3.getString("notes"), font)));
                       }
                       table2.setSpacingAfter(10F);
                       document.add(table2);
                   } catch (Exception e) {
                       System.out.print(e);
                   }
                   LineSeparator ls3 = new LineSeparator();
                   document.add(new Chunk(ls3));
               }
               document.close();
               System.out.println("Done");
               conn.close();
           } catch (Exception e) {
               System.out.print(e);
           }
       }

        }

}


