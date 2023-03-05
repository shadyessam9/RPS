package main;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
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

public class tbreports implements Initializable {

    @FXML
    private ComboBox type;
    @FXML
    private ComboBox comparison;
    @FXML
    private ComboBox level;
    @FXML
    private TextField periods;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        level.getItems().add("ALL");
        level.getItems().add("0");
        level.getItems().add("1");
        level.getItems().add("2");
        level.getItems().add("3");
        level.getItems().add("4");
        level.getItems().add("5");


        type.getItems().add("COMPARISON");
        type.getItems().add("CURRENT MONTH");
        type.getItems().add("CURRENT QUARTER");
        type.getItems().add("CURRENT YEAR");
        type.getItems().add("LAST MONTH");
        type.getItems().add("LAST QUARTER");
        type.getItems().add("LAST YEAR");
        type.getItems().add("SPECIFIC");
        comparison.getItems().add("LAST PERIODS");
        comparison.getItems().add("LAST PERIODS LAST YEAR");


        comparison.setDisable(true);
        periods.setDisable(true);
        dt1.setDisable(true);
        dt2.setDisable(true);
        conf.setDisable(true);


        type.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(newValue.equals("COMPARISON")){
                comparison.setDisable(false);
                periods.setDisable(false);
                conf.setDisable(false);
            }else if (newValue.equals("SPECIFIC")){
                dt1.setDisable(false);
                dt2.setDisable(false);
                conf.setDisable(false);
            }else{
                comparison.setDisable(true);
                periods.setDisable(true);
                dt1.setDisable(true);
                dt2.setDisable(true);
                conf.setDisable(false);
            }
        });

        DateTimeFormatter dtr1 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter m = DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter y = DateTimeFormatter.ofPattern("yyyy");
        LocalDateTime now1 = LocalDateTime.now();


        month = m.format(now1).replace("-", "");
        year =  y.format(now1).replace("-", "");



    }

    public void createrep() throws Exception {
        if(type.getValue().equals("COMPARISON")){
            if(comparison.getValue().equals("LAST PERIODS")){

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
                Font largeBold = new Font(Font.FontFamily.COURIER, 25, Font.BOLD| Font.UNDERLINE);
                Font myArabicFont = FontFactory.getFont("my_arabic" ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                PdfWriter.getInstance(document, new FileOutputStream("sample3.pdf"));
                document.open();
                Paragraph Title = new Paragraph("El Foaud For Contracting Works & Commerce Co \n 01005251258 \n  ELFOUADCOMMERCE@YAHOO.COM");
                Title.setAlignment(Element.ALIGN_RIGHT);
                Title.setSpacingAfter(5F);
                document.add(Title);
                Paragraph detailsd = new Paragraph(this.dt+" "+this.hour);
                detailsd.setAlignment(Element.ALIGN_RIGHT);
                detailsd.setSpacingAfter(5.0F);
                document.add(detailsd);

                Image companyLogo = Image.getInstance("/vulcan/src/assetes/reportlogo.png");
                companyLogo.scalePercent(7);
                companyLogo.setSpacingAfter(50F);
                companyLogo.setAbsolutePosition(0, PageSize.A4.getHeight() - companyLogo.getScaledHeight()+20);
                document.add(companyLogo);

                LineSeparator ls3 = new LineSeparator();
                document.add(new Chunk(ls3));

                Paragraph type = new Paragraph("TRAIL BALANCE",largeBold);
                type.setAlignment(Element.ALIGN_LEFT);
                type.setSpacingAfter(5F);
                type.setSpacingBefore(10F);
                document.add(type);
                Paragraph details = new Paragraph("FOR"+" : "+month+"-"+year);
                details.setAlignment(Element.ALIGN_LEFT);
                details.setSpacingAfter(5.0F);
                document.add(details);



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

                    Paragraph line1 = new Paragraph("ACCOUNT NAME : ");
                    line1.setAlignment(0);
                    PdfPTable table = new PdfPTable(1);
                    table.getDefaultCell().setNoWrap(false);
                    PdfPCell text = new PdfPCell(new Phrase(accountname+"-"+results.getString("id"), myArabicFont));
                    text.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    text.setNoWrap(false);
                    text.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    text.setBorder(0);
                    table.addCell(text);
                    table.setHorizontalAlignment(0);
                    table.getDefaultCell().setBorder(0);
                    table.setSpacingAfter(10F);
                    line1.setSpacingBefore(10F);
                    table.setSpacingBefore(5F);
                    document.add(line1);
                    document.add(table);



                    int i;
                    for(i = Integer.valueOf(month)-1; i >= (Integer.valueOf(month)-Integer.valueOf(periods.getText())) && i>0; i--)
                    {

                        key1=i;
                        key2=year;

                        PdfPTable table2 = new PdfPTable(new float[] { 2, 2, 2 });
                        table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table2.addCell("OPENING BALANCE"+"-"+year);
                        table2.addCell(month+"-"+year);
                        table2.addCell(key1+"-"+key2);
                        PdfPTable obtable2 = new PdfPTable(new float[] {1,1});
                        obtable2.getDefaultCell().setBorder(0);
                        PdfPCell table_celld3 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld3.setHorizontalAlignment(1);
                        table_celld3.setBorder(0);
                        PdfPCell table_cellc3 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc3.setHorizontalAlignment(1);
                        table_cellc3.setBorder(0);
                        obtable2.addCell(table_celld3);
                        obtable2.addCell(table_cellc3);
                        table2.addCell(obtable2);
                        PdfPTable mtable2 = new PdfPTable(new float[] {1,1});
                        mtable2.getDefaultCell().setBorder(0);
                        PdfPCell table_celld4 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld4.setHorizontalAlignment(1);
                        table_celld4.setBorder(0);
                        PdfPCell table_cellc4 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc4.setHorizontalAlignment(1);
                        table_cellc4.setBorder(0);
                        mtable2.addCell(table_celld4);
                        mtable2.addCell(table_cellc4);
                        table2.addCell(mtable2);
                        table2.setHeaderRows(1);
                        PdfPTable ctable = new PdfPTable(new float[] {1,1});
                        ctable.getDefaultCell().setBorder(0);
                        PdfPCell table_celld5 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld5.setHorizontalAlignment(1);
                        table_celld5.setBorder(0);
                        PdfPCell table_cellc5 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc5.setHorizontalAlignment(1);
                        table_cellc5.setBorder(0);
                        ctable.addCell(table_celld5);
                        ctable.addCell(table_cellc5);
                        table2.addCell(ctable);
                        table2.setHeaderRows(1);
                        table2.setSpacingAfter(10F);
                        PdfPCell[] cells1 = table2.getRow(0).getCells();
                        for (int j=0;j<cells1.length;j++){
                            cells1[j].setBackgroundColor(BaseColor.GRAY);
                        }

                        PdfPTable mtable3 = new PdfPTable(new float[] {1,1});
                        PdfPTable mtable4 = new PdfPTable(new float[] {1,1});
                        PdfPTable obtable3= new PdfPTable(new float[] {1,1});
                        try {
                            tdb1=0;
                            tcr1=0;
                            tdb2=0;
                            tcr2=0;
                            tdb3=0;
                            tcr3=0;
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement3 = conn.createStatement();
                            statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND month(dt)='"+key1+"' AND YEAR(dt)='"+key2+"' ");
                            ResultSet results1 = statement3.getResultSet();
                            while (results1.next()) {
                                tmdb= results1.getInt("sdb");
                                tdb1=tdb1+tmdb;
                                tmcr= results1.getInt("scr");
                                tcr1=tcr1+tmcr;
                                PdfPCell table_celld6= new PdfPCell(new Phrase(new Chunk((String.valueOf(tmdb)), font)));
                                table_celld6.setUseDescender(true);
                                table_celld6.setHorizontalAlignment(1);
                                table_celld6.setRunDirection(3);
                                PdfPCell table_cellc6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tmcr)), font)));
                                table_cellc6.setUseDescender(true);
                                table_cellc6.setHorizontalAlignment(1);
                                table_cellc6.setRunDirection(3);
                                mtable3.addCell(table_celld6);
                                mtable3.addCell(table_cellc6);
                            }
                            statement2 = conn.createStatement();
                            statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "'  AND  YEAR(dt) = YEAR(CURDATE())");
                            ResultSet results2 = statement2.getResultSet();
                            while (results2.next()) {
                                opdb= results2.getInt("sdb");
                                tdb1=tdb1+opdb;
                                opcr= results2.getInt("scr");
                                tcr1=tcr1+opcr;
                                PdfPCell table_celld6 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opdb), font)));
                                table_celld6.setUseDescender(true);
                                table_celld6.setHorizontalAlignment(1);
                                table_celld6.setRunDirection(3);
                                PdfPCell table_cellc6 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opcr), font)));
                                table_cellc6.setUseDescender(true);
                                table_cellc6.setHorizontalAlignment(1);
                                table_cellc6.setRunDirection(3);
                                obtable3.addCell(table_celld6);
                                obtable3.addCell(table_cellc6);

                            }
                            statement3 = conn.createStatement();
                            statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND MONTH(dt) = MONTH(CURRENT_DATE())");
                            ResultSet results3 = statement3.getResultSet();
                            while (results3.next()) {
                                tmdb= results3.getInt("sdb");
                                tdb2=tdb2+tmdb;
                                tmcr= results3.getInt("scr");
                                tcr2=tcr2+tmcr;

                                PdfPCell table_celld7= new PdfPCell(new Phrase(new Chunk((String.valueOf(tmdb)), font)));
                                table_celld7.setUseDescender(true);
                                table_celld7.setHorizontalAlignment(1);
                                table_celld7.setRunDirection(3);
                                PdfPCell table_cellc7 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tmcr)), font)));
                                table_cellc7.setUseDescender(true);
                                table_cellc7.setHorizontalAlignment(1);
                                table_cellc7.setRunDirection(3);
                                mtable4.addCell(table_celld7);
                                mtable4.addCell(table_cellc7);

                            }


                        } catch (Exception e) {
                            System.out.print(e);
                        }
                        table2.addCell(obtable3);
                        table2.addCell(mtable4);
                        table2.addCell(mtable3);
                        document.add(table2);
                        if(i==i){break;}
                    }
                    LineSeparator ls1 = new LineSeparator();
                    document.add(new Chunk(ls1));
                }
                System.out.println("Done");
                document.close();
                conn.close();
            }else if(comparison.getValue().equals("LAST PERIODS LAST YEAR")){

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
                Font myArabicFont = FontFactory.getFont("my_arabic" ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
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
                    Paragraph line1 = new Paragraph("ACCOUNT NAME : ");
                    line1.setAlignment(0);
                    PdfPTable table = new PdfPTable(1);
                    table.getDefaultCell().setNoWrap(false);
                    PdfPCell text = new PdfPCell(new Phrase(accountname+"-"+results.getString("id"), myArabicFont));
                    text.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    text.setNoWrap(false);
                    text.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    text.setBorder(0);
                    table.addCell(text);
                    table.setHorizontalAlignment(0);
                    table.getDefaultCell().setBorder(0);
                    table.setSpacingAfter(10F);
                    line1.setSpacingBefore(10F);
                    table.setSpacingBefore(5F);
                    document.add(line1);
                    document.add(table);

                    int i;
                    for(i = Integer.valueOf(month)-1; i >= (Integer.valueOf(month)-Integer.valueOf(periods.getText())) && i>0; i--)
                    {

                        key1=i;
                        key2=Integer.parseInt(year)-1;

                        PdfPTable table2 = new PdfPTable(new float[] { 2, 2, 2 });
                        table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table2.addCell("OPENING BALANCE"+"-"+year);
                        table2.addCell(month+"-"+year);
                        table2.addCell(key1+"-"+key2);
                        PdfPTable obtable2 = new PdfPTable(new float[] {1,1});
                        obtable2.getDefaultCell().setBorder(0);
                        PdfPCell table_celld3 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld3.setHorizontalAlignment(1);
                        table_celld3.setBorder(0);
                        PdfPCell table_cellc3 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc3.setHorizontalAlignment(1);
                        table_cellc3.setBorder(0);
                        obtable2.addCell(table_celld3);
                        obtable2.addCell(table_cellc3);
                        table2.addCell(obtable2);
                        PdfPTable mtable2 = new PdfPTable(new float[] {1,1});
                        mtable2.getDefaultCell().setBorder(0);
                        PdfPCell table_celld4 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld4.setHorizontalAlignment(1);
                        table_celld4.setBorder(0);
                        PdfPCell table_cellc4 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc4.setHorizontalAlignment(1);
                        table_cellc4.setBorder(0);
                        mtable2.addCell(table_celld4);
                        mtable2.addCell(table_cellc4);
                        table2.addCell(mtable2);
                        table2.setHeaderRows(1);
                        PdfPTable ctable = new PdfPTable(new float[] {1,1});
                        ctable.getDefaultCell().setBorder(0);
                        PdfPCell table_celld5 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
                        table_celld5.setHorizontalAlignment(1);
                        table_celld5.setBorder(0);
                        PdfPCell table_cellc5 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
                        table_cellc5.setHorizontalAlignment(1);
                        table_cellc5.setBorder(0);
                        ctable.addCell(table_celld5);
                        ctable.addCell(table_cellc5);
                        table2.addCell(ctable);
                        table2.setHeaderRows(1);
                        table2.setSpacingAfter(10F);
                        PdfPCell[] cells1 = table2.getRow(0).getCells();
                        for (int j=0;j<cells1.length;j++){
                            cells1[j].setBackgroundColor(BaseColor.GRAY);
                        }

                        PdfPTable mtable3 = new PdfPTable(new float[] {1,1});
                        PdfPTable mtable4 = new PdfPTable(new float[] {1,1});
                        PdfPTable obtable3= new PdfPTable(new float[] {1,1});
                        try {
                            tdb1=0;
                            tcr1=0;
                            tdb2=0;
                            tcr2=0;
                            tdb3=0;
                            tcr3=0;
                            conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                            statement3 = conn.createStatement();
                            statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND month(dt)='"+key1+"' AND YEAR(dt)='"+key2+"' ");
                            ResultSet results1 = statement3.getResultSet();
                            while (results1.next()) {
                                tmdb= results1.getInt("sdb");
                                tdb1=tdb1+tmdb;
                                tmcr= results1.getInt("scr");
                                tcr1=tcr1+tmcr;
                                PdfPCell table_celld6= new PdfPCell(new Phrase(new Chunk((String.valueOf(tmdb)), font)));
                                table_celld6.setUseDescender(true);
                                table_celld6.setHorizontalAlignment(1);
                                table_celld6.setRunDirection(3);
                                PdfPCell table_cellc6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tmcr)), font)));
                                table_cellc6.setUseDescender(true);
                                table_cellc6.setHorizontalAlignment(1);
                                table_cellc6.setRunDirection(3);
                                mtable3.addCell(table_celld6);
                                mtable3.addCell(table_cellc6);
                            }
                            statement2 = conn.createStatement();
                            statement2.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM openningbalances WHERE parent_id='" + results.getString("id") + "'  AND  YEAR(dt) = YEAR(CURDATE())");
                            ResultSet results2 = statement2.getResultSet();
                            while (results2.next()) {
                                opdb= results2.getInt("sdb");
                                tdb1=tdb1+opdb;
                                opcr= results2.getInt("scr");
                                tcr1=tcr1+opcr;
                                PdfPCell table_celld6 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opdb), font)));
                                table_celld6.setUseDescender(true);
                                table_celld6.setHorizontalAlignment(1);
                                table_celld6.setRunDirection(3);
                                PdfPCell table_cellc6 = new PdfPCell(new Phrase(new Chunk(String.valueOf(opcr), font)));
                                table_cellc6.setUseDescender(true);
                                table_cellc6.setHorizontalAlignment(1);
                                table_cellc6.setRunDirection(3);
                                obtable3.addCell(table_celld6);
                                obtable3.addCell(table_cellc6);

                            }
                            statement3 = conn.createStatement();
                            statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND MONTH(dt) = MONTH(CURRENT_DATE())");
                            ResultSet results3 = statement3.getResultSet();
                            while (results3.next()) {
                                tmdb= results3.getInt("sdb");
                                tdb2=tdb2+tmdb;
                                tmcr= results3.getInt("scr");
                                tcr2=tcr2+tmcr;

                                PdfPCell table_celld7= new PdfPCell(new Phrase(new Chunk((String.valueOf(tmdb)), font)));
                                table_celld7.setUseDescender(true);
                                table_celld7.setHorizontalAlignment(1);
                                table_celld7.setRunDirection(3);
                                PdfPCell table_cellc7 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tmcr)), font)));
                                table_cellc7.setUseDescender(true);
                                table_cellc7.setHorizontalAlignment(1);
                                table_cellc7.setRunDirection(3);
                                mtable4.addCell(table_celld7);
                                mtable4.addCell(table_cellc7);

                            }



                        } catch (Exception e) {
                            System.out.print(e);
                        }
                        table2.addCell(obtable3);
                        table2.addCell(mtable4);
                        table2.addCell(mtable3);
                        document.add(table2);
                        if(i==i){break;}
                    }
                    LineSeparator ls3 = new LineSeparator();
                    document.add(new Chunk(ls3));
                }
                System.out.println("Done");
                document.close();
                conn.close();
            }

        }else if(type.getValue().equals("CURRENT MONTH")){
            comparison.setDisable(true);
            periods.setDisable(true);
            dt1.setDisable(true);
            dt2.setDisable(true);
            conf.setDisable(true);
            DateTimeFormatter t = DateTimeFormatter.ofPattern("hh:mm a");
            LocalDateTime now1 = LocalDateTime.now();
            this.hour = t.format(now1);
            DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime now = LocalDateTime.now();
            this.dt = dtr.format(now);
            Document document = new Document();
            FontFactory.registerDirectories();
            Font font = FontFactory.getFont("arial", "Identity-H", true, 11.0F);
            PdfPTable table = new PdfPTable(new float[] { 2, 2, 2, 2 });
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell("ACCOUNT");
            table.addCell("OPENING BALANCE");
            table.addCell(month+"-"+year);
            table.addCell("TOTAL");
            table.addCell("      ");
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
            PdfPTable ttable = new PdfPTable(new float[] {1,1});
            ttable.getDefaultCell().setBorder(0);
            PdfPCell table_celld3 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
            table_celld3.setHorizontalAlignment(1);
            table_celld3.setBorder(0);
            PdfPCell table_cellc3 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
            table_cellc3.setHorizontalAlignment(1);
            table_cellc3.setBorder(0);
            ttable.addCell(table_celld3);
            ttable.addCell(table_cellc3);
            table.addCell( ttable);
            table.setHeaderRows(1);
            PdfPCell[] cells = table.getRow(0).getCells();
            for (int j=0;j<cells.length;j++){
                cells[j].setBackgroundColor(BaseColor.GRAY);
            }
            try {
                tdb1=0;
                tcr1=0;
                tdb2=0;
                tcr2=0;
                tdb3=0;
                tcr3=0;
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
                    PdfPTable tbtable1 = new PdfPTable(new float[] {1,1});
                    PdfPCell table_celld6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(todb)), font)));
                    table_celld6.setUseDescender(true);
                    table_celld6.setHorizontalAlignment(1);
                    table_celld6.setRunDirection(3);
                    PdfPCell table_cellc6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tocr)), font)));
                    table_cellc6.setUseDescender(true);
                    table_cellc6.setHorizontalAlignment(1);
                    table_cellc6.setRunDirection(3);
                    tbtable1.addCell(table_celld6);
                    tbtable1.addCell(table_cellc6);
                    table.addCell(tbtable1);
                }
                table.addCell("TOTALS");
                PdfPTable totals1 = new PdfPTable(new float[] {1,1});
                PdfPCell totals1d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb1)), font)));
                totals1d.setUseDescender(true);
                totals1d.setHorizontalAlignment(1);
                totals1d.setRunDirection(3);
                PdfPCell totals1c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr1)), font)));
                totals1c.setUseDescender(true);
                totals1c.setHorizontalAlignment(1);
                totals1c.setRunDirection(3);
                totals1.addCell(totals1d);
                totals1.addCell(totals1c);
                table.addCell(totals1);
                PdfPTable totals2 = new PdfPTable(new float[] {1,1});
                PdfPCell totals2d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb2)), font)));
                totals2d.setUseDescender(true);
                totals2d.setHorizontalAlignment(1);
                totals2d.setRunDirection(3);
                PdfPCell totals2c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr2)), font)));
                totals2c.setUseDescender(true);
                totals2c.setHorizontalAlignment(1);
                totals2c.setRunDirection(3);
                totals2.addCell(totals2d);
                totals2.addCell(totals2c);
                table.addCell(totals2);
                PdfPTable totals3 = new PdfPTable(new float[] {1,1});
                PdfPCell totals3d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb3)), font)));
                totals3d.setUseDescender(true);
                totals3d.setHorizontalAlignment(1);
                totals3d.setRunDirection(3);
                PdfPCell totals3c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr3)), font)));
                totals3c.setUseDescender(true);
                totals3c.setHorizontalAlignment(1);
                totals3c.setRunDirection(3);
                totals3.addCell(totals3d);
                totals3.addCell(totals3c);
                table.addCell(totals3);
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
                table.setSpacingBefore(50F);
                document.add(table);
                document.close();
                System.out.println("Done");
                conn.close();
            } catch (Exception e) {
                System.out.print(e);
            }
        }else if(type.getValue().equals("CURRENT QUARTER")){
            comparison.setDisable(true);
            periods.setDisable(true);
            dt1.setDisable(true);
            dt2.setDisable(true);
            conf.setDisable(true);
            DateTimeFormatter t = DateTimeFormatter.ofPattern("hh:mm a");
            LocalDateTime now1 = LocalDateTime.now();
            this.hour = t.format(now1);
            DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime now = LocalDateTime.now();
            this.dt = dtr.format(now);
            Document document = new Document();
            FontFactory.registerDirectories();
            Font font = FontFactory.getFont("arial", "Identity-H", true, 11.0F);
            try {
                conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                statement4 = conn.createStatement();
                statement4.execute("SELECT QUARTER(curdate()) AS q");
                ResultSet results3 = statement4.getResultSet();
                while (results3.next()) {
                    quarter= results3.getString("q");
                }
            }catch (Exception e) {
                System.out.print(e);
            }
            PdfPTable table = new PdfPTable(new float[] { 2, 2, 2, 2 });
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell("ACCOUNT");
            table.addCell("OPENING BALANCE");
            table.addCell(quarter+"-"+year);
            table.addCell("TOTAL");
            table.addCell("      ");
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
            PdfPTable ttable = new PdfPTable(new float[] {1,1});
            ttable.getDefaultCell().setBorder(0);
            PdfPCell table_celld3 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
            table_celld3.setHorizontalAlignment(1);
            table_celld3.setBorder(0);
            PdfPCell table_cellc3 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
            table_cellc3.setHorizontalAlignment(1);
            table_cellc3.setBorder(0);
            ttable.addCell(table_celld3);
            ttable.addCell(table_cellc3);
            table.addCell( ttable);
            table.setHeaderRows(1);
            PdfPCell[] cells = table.getRow(0).getCells();
            for (int j=0;j<cells.length;j++){
                cells[j].setBackgroundColor(BaseColor.GRAY);
            }
            try {
                tdb1=0;
                tcr1=0;
                tdb2=0;
                tcr2=0;
                tdb3=0;
                tcr3=0;
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
                    PdfPTable tbtable1 = new PdfPTable(new float[] {1,1});
                    PdfPCell table_celld6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(todb)), font)));
                    table_celld6.setUseDescender(true);
                    table_celld6.setHorizontalAlignment(1);
                    table_celld6.setRunDirection(3);
                    PdfPCell table_cellc6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tocr)), font)));
                    table_cellc6.setUseDescender(true);
                    table_cellc6.setHorizontalAlignment(1);
                    table_cellc6.setRunDirection(3);
                    tbtable1.addCell(table_celld6);
                    tbtable1.addCell(table_cellc6);
                    table.addCell(tbtable1);
                }
                table.addCell("TOTALS");
                PdfPTable totals1 = new PdfPTable(new float[] {1,1});
                PdfPCell totals1d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb1)), font)));
                totals1d.setUseDescender(true);
                totals1d.setHorizontalAlignment(1);
                totals1d.setRunDirection(3);
                PdfPCell totals1c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr1)), font)));
                totals1c.setUseDescender(true);
                totals1c.setHorizontalAlignment(1);
                totals1c.setRunDirection(3);
                totals1.addCell(totals1d);
                totals1.addCell(totals1c);
                table.addCell(totals1);
                PdfPTable totals2 = new PdfPTable(new float[] {1,1});
                PdfPCell totals2d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb2)), font)));
                totals2d.setUseDescender(true);
                totals2d.setHorizontalAlignment(1);
                totals2d.setRunDirection(3);
                PdfPCell totals2c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr2)), font)));
                totals2c.setUseDescender(true);
                totals2c.setHorizontalAlignment(1);
                totals2c.setRunDirection(3);
                totals2.addCell(totals2d);
                totals2.addCell(totals2c);
                table.addCell(totals2);
                PdfPTable totals3 = new PdfPTable(new float[] {1,1});
                PdfPCell totals3d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb3)), font)));
                totals3d.setUseDescender(true);
                totals3d.setHorizontalAlignment(1);
                totals3d.setRunDirection(3);
                PdfPCell totals3c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr3)), font)));
                totals3c.setUseDescender(true);
                totals3c.setHorizontalAlignment(1);
                totals3c.setRunDirection(3);
                totals3.addCell(totals3d);
                totals3.addCell(totals3c);
                table.addCell(totals3);
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
                Paragraph details = new Paragraph("QUARTER"+" : "+quarter+"-"+year);
                details.setAlignment(1);
                details.setSpacingAfter(5.0F);
                document.add(details);
                Image companyLogo = Image.getInstance("/vulcan/src/assetes/reportlogo.png");
                companyLogo.scalePercent(7);
                companyLogo.setSpacingAfter(50F);
                companyLogo.setAbsolutePosition(0, PageSize.A4.getHeight() - companyLogo.getScaledHeight()+20);
                document.add(companyLogo);
                table.setSpacingBefore(50F);
                document.add(table);
                document.close();
                System.out.println("Done");
                conn.close();
            } catch (Exception e) {
                System.out.print(e);
            }
        }else if(type.getValue().equals("CURRENT YEAR")){
            comparison.setDisable(true);
            periods.setDisable(true);
            dt1.setDisable(true);
            dt2.setDisable(true);
            conf.setDisable(true);
            comparison.setDisable(true);
            periods.setDisable(true);
            dt1.setDisable(true);
            dt2.setDisable(true);
            conf.setDisable(true);
            DateTimeFormatter t = DateTimeFormatter.ofPattern("hh:mm a");
            LocalDateTime now1 = LocalDateTime.now();
            this.hour = t.format(now1);
            DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime now = LocalDateTime.now();
            this.dt = dtr.format(now);
            Document document = new Document();
            FontFactory.registerDirectories();
            Font font = FontFactory.getFont("arial", "Identity-H", true, 11.0F);
            PdfPTable table = new PdfPTable(new float[] { 2, 2, 2, 2 });
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell("ACCOUNT");
            table.addCell("OPENING BALANCE");
            table.addCell(year);
            table.addCell("TOTAL");
            table.addCell("     ");
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
            PdfPTable ttable = new PdfPTable(new float[] {1,1});
            ttable.getDefaultCell().setBorder(0);
            PdfPCell table_celld3 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
            table_celld3.setHorizontalAlignment(1);
            table_celld3.setBorder(0);
            PdfPCell table_cellc3 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
            table_cellc3.setHorizontalAlignment(1);
            table_cellc3.setBorder(0);
            ttable.addCell(table_celld3);
            ttable.addCell(table_cellc3);
            table.addCell( ttable);
            table.setHeaderRows(1);
            PdfPCell[] cells = table.getRow(0).getCells();
            for (int j=0;j<cells.length;j++){
                cells[j].setBackgroundColor(BaseColor.GRAY);
            }
            try {
                tdb1=0;
                tcr1=0;
                tdb2=0;
                tcr2=0;
                tdb3=0;
                tcr3=0;
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
                    PdfPTable tbtable1 = new PdfPTable(new float[] {1,1});
                    PdfPCell table_celld6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(todb)), font)));
                    table_celld6.setUseDescender(true);
                    table_celld6.setHorizontalAlignment(1);
                    table_celld6.setRunDirection(3);
                    PdfPCell table_cellc6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tocr)), font)));
                    table_cellc6.setUseDescender(true);
                    table_cellc6.setHorizontalAlignment(1);
                    table_cellc6.setRunDirection(3);
                    tbtable1.addCell(table_celld6);
                    tbtable1.addCell(table_cellc6);
                    table.addCell(tbtable1);
                }
                table.addCell("TOTALS");
                PdfPTable totals1 = new PdfPTable(new float[] {1,1});
                PdfPCell totals1d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb1)), font)));
                totals1d.setUseDescender(true);
                totals1d.setHorizontalAlignment(1);
                totals1d.setRunDirection(3);
                PdfPCell totals1c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr1)), font)));
                totals1c.setUseDescender(true);
                totals1c.setHorizontalAlignment(1);
                totals1c.setRunDirection(3);
                totals1.addCell(totals1d);
                totals1.addCell(totals1c);
                table.addCell(totals1);
                PdfPTable totals2 = new PdfPTable(new float[] {1,1});
                PdfPCell totals2d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb2)), font)));
                totals2d.setUseDescender(true);
                totals2d.setHorizontalAlignment(1);
                totals2d.setRunDirection(3);
                PdfPCell totals2c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr2)), font)));
                totals2c.setUseDescender(true);
                totals2c.setHorizontalAlignment(1);
                totals2c.setRunDirection(3);
                totals2.addCell(totals2d);
                totals2.addCell(totals2c);
                table.addCell(totals2);
                PdfPTable totals3 = new PdfPTable(new float[] {1,1});
                PdfPCell totals3d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb3)), font)));
                totals3d.setUseDescender(true);
                totals3d.setHorizontalAlignment(1);
                totals3d.setRunDirection(3);
                PdfPCell totals3c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr3)), font)));
                totals3c.setUseDescender(true);
                totals3c.setHorizontalAlignment(1);
                totals3c.setRunDirection(3);
                totals3.addCell(totals3d);
                totals3.addCell(totals3c);
                table.addCell(totals3);
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
                Paragraph details = new Paragraph("FOR"+" : "+year);
                details.setAlignment(1);
                details.setSpacingAfter(5.0F);
                document.add(details);
                Image companyLogo = Image.getInstance("/vulcan/src/assetes/reportlogo.png");
                companyLogo.scalePercent(7);
                companyLogo.setSpacingAfter(50F);
                companyLogo.setAbsolutePosition(0, PageSize.A4.getHeight() - companyLogo.getScaledHeight()+20);
                document.add(companyLogo);
                table.setSpacingBefore(50F);
                document.add(table);
                document.close();
                System.out.println("Done");
                conn.close();
            } catch (Exception e) {
                System.out.print(e);
            }
        }else if(type.getValue().equals("LAST MONTH")){
            comparison.setDisable(true);
            periods.setDisable(true);
            dt1.setDisable(true);
            dt2.setDisable(true);
            conf.setDisable(true);
            comparison.setDisable(true);
            periods.setDisable(true);
            dt1.setDisable(true);
            dt2.setDisable(true);
            conf.setDisable(true);
            DateTimeFormatter t = DateTimeFormatter.ofPattern("hh:mm a");
            LocalDateTime now1 = LocalDateTime.now();
            this.hour = t.format(now1);
            DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime now = LocalDateTime.now();
            this.dt = dtr.format(now);
            Document document = new Document();
            FontFactory.registerDirectories();
            Font font = FontFactory.getFont("arial", "Identity-H", true, 11.0F);
            PdfPTable table = new PdfPTable(new float[] { 2, 2, 2, 2 });
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell("ACCOUNT");
            table.addCell("OPENING BALANCE");
            table.addCell(now.minusMonths(1) +"-"+year);
            table.addCell("TOTAL");
            table.addCell("      ");
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
            PdfPTable ttable = new PdfPTable(new float[] {1,1});
            ttable.getDefaultCell().setBorder(0);
            PdfPCell table_celld3 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
            table_celld3.setHorizontalAlignment(1);
            table_celld3.setBorder(0);
            PdfPCell table_cellc3 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
            table_cellc3.setHorizontalAlignment(1);
            table_cellc3.setBorder(0);
            ttable.addCell(table_celld3);
            ttable.addCell(table_cellc3);
            table.addCell( ttable);
            table.setHeaderRows(1);
            PdfPCell[] cells = table.getRow(0).getCells();
            for (int j=0;j<cells.length;j++){
                cells[j].setBackgroundColor(BaseColor.GRAY);
            }
            try {
                tdb1=0;
                tcr1=0;
                tdb2=0;
                tcr2=0;
                tdb3=0;
                tcr3=0;
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
                    PdfPTable tbtable1 = new PdfPTable(new float[] {1,1});
                    PdfPCell table_celld6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(todb)), font)));
                    table_celld6.setUseDescender(true);
                    table_celld6.setHorizontalAlignment(1);
                    table_celld6.setRunDirection(3);
                    PdfPCell table_cellc6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tocr)), font)));
                    table_cellc6.setUseDescender(true);
                    table_cellc6.setHorizontalAlignment(1);
                    table_cellc6.setRunDirection(3);
                    tbtable1.addCell(table_celld6);
                    tbtable1.addCell(table_cellc6);
                    table.addCell(tbtable1);
                }
                table.addCell("TOTALS");
                PdfPTable totals1 = new PdfPTable(new float[] {1,1});
                PdfPCell totals1d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb1)), font)));
                totals1d.setUseDescender(true);
                totals1d.setHorizontalAlignment(1);
                totals1d.setRunDirection(3);
                PdfPCell totals1c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr1)), font)));
                totals1c.setUseDescender(true);
                totals1c.setHorizontalAlignment(1);
                totals1c.setRunDirection(3);
                totals1.addCell(totals1d);
                totals1.addCell(totals1c);
                table.addCell(totals1);
                PdfPTable totals2 = new PdfPTable(new float[] {1,1});
                PdfPCell totals2d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb2)), font)));
                totals2d.setUseDescender(true);
                totals2d.setHorizontalAlignment(1);
                totals2d.setRunDirection(3);
                PdfPCell totals2c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr2)), font)));
                totals2c.setUseDescender(true);
                totals2c.setHorizontalAlignment(1);
                totals2c.setRunDirection(3);
                totals2.addCell(totals2d);
                totals2.addCell(totals2c);
                table.addCell(totals2);
                PdfPTable totals3 = new PdfPTable(new float[] {1,1});
                PdfPCell totals3d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb3)), font)));
                totals3d.setUseDescender(true);
                totals3d.setHorizontalAlignment(1);
                totals3d.setRunDirection(3);
                PdfPCell totals3c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr3)), font)));
                totals3c.setUseDescender(true);
                totals3c.setHorizontalAlignment(1);
                totals3c.setRunDirection(3);
                totals3.addCell(totals3d);
                totals3.addCell(totals3c);
                table.addCell(totals3);
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
                Paragraph details = new Paragraph("FOR"+" : "+now.minusMonths(1) +"-"+year);
                details.setAlignment(1);
                details.setSpacingAfter(5.0F);
                document.add(details);
                Image companyLogo = Image.getInstance("/vulcan/src/assetes/reportlogo.png");
                companyLogo.scalePercent(7);
                companyLogo.setSpacingAfter(50F);
                companyLogo.setAbsolutePosition(0, PageSize.A4.getHeight() - companyLogo.getScaledHeight()+20);
                document.add(companyLogo);
                table.setSpacingBefore(50F);
                document.add(table);
                document.close();
                System.out.println("Done");
                conn.close();
            } catch (Exception e) {
                System.out.print(e);
            }
        }else if(type.getValue().equals("LAST QUARTER")){
            comparison.setDisable(true);
            periods.setDisable(true);
            dt1.setDisable(true);
            dt2.setDisable(true);
            conf.setDisable(true);
            comparison.setDisable(true);
            periods.setDisable(true);
            dt1.setDisable(true);
            dt2.setDisable(true);
            conf.setDisable(true);
            DateTimeFormatter t = DateTimeFormatter.ofPattern("hh:mm a");
            LocalDateTime now1 = LocalDateTime.now();
            this.hour = t.format(now1);
            DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime now = LocalDateTime.now();
            this.dt = dtr.format(now);
            Document document = new Document();
            FontFactory.registerDirectories();
            Font font = FontFactory.getFont("arial", "Identity-H", true, 11.0F);
            statement4 = conn.createStatement();
            statement4.execute("SELECT QUARTER(curdate() - INTERVAL 1 quarter) AS q");
            ResultSet results3 = statement4.getResultSet();
            while (results3.next()) {
                quarter= results3.getString("q");

            }
            PdfPTable table = new PdfPTable(new float[] { 2, 2, 2, 2 });
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell("ACCOUNT");
            table.addCell("OPENING BALANCE");
            table.addCell(quarter+"-"+year);
            table.addCell("TOTAL");
            table.addCell("      ");
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
            PdfPTable ttable = new PdfPTable(new float[] {1,1});
            ttable.getDefaultCell().setBorder(0);
            PdfPCell table_celld3 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
            table_celld3.setHorizontalAlignment(1);
            table_celld3.setBorder(0);
            PdfPCell table_cellc3 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
            table_cellc3.setHorizontalAlignment(1);
            table_cellc3.setBorder(0);
            ttable.addCell(table_celld3);
            ttable.addCell(table_cellc3);
            table.addCell( ttable);
            table.setHeaderRows(1);
            PdfPCell[] cells = table.getRow(0).getCells();
            for (int j=0;j<cells.length;j++){
                cells[j].setBackgroundColor(BaseColor.GRAY);
            }
            try {
                tdb1=0;
                tcr1=0;
                tdb2=0;
                tcr2=0;
                tdb3=0;
                tcr3=0;
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
                    statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND QUARTER(dt) = QUARTER(curdate() - INTERVAL 1 quarter)");
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
                    PdfPTable tbtable1 = new PdfPTable(new float[] {1,1});
                    PdfPCell table_celld6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(todb)), font)));
                    table_celld6.setUseDescender(true);
                    table_celld6.setHorizontalAlignment(1);
                    table_celld6.setRunDirection(3);
                    PdfPCell table_cellc6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tocr)), font)));
                    table_cellc6.setUseDescender(true);
                    table_cellc6.setHorizontalAlignment(1);
                    table_cellc6.setRunDirection(3);
                    tbtable1.addCell(table_celld6);
                    tbtable1.addCell(table_cellc6);
                    table.addCell(tbtable1);
                }
                table.addCell("TOTALS");
                PdfPTable totals1 = new PdfPTable(new float[] {1,1});
                PdfPCell totals1d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb1)), font)));
                totals1d.setUseDescender(true);
                totals1d.setHorizontalAlignment(1);
                totals1d.setRunDirection(3);
                PdfPCell totals1c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr1)), font)));
                totals1c.setUseDescender(true);
                totals1c.setHorizontalAlignment(1);
                totals1c.setRunDirection(3);
                totals1.addCell(totals1d);
                totals1.addCell(totals1c);
                table.addCell(totals1);
                PdfPTable totals2 = new PdfPTable(new float[] {1,1});
                PdfPCell totals2d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb2)), font)));
                totals2d.setUseDescender(true);
                totals2d.setHorizontalAlignment(1);
                totals2d.setRunDirection(3);
                PdfPCell totals2c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr2)), font)));
                totals2c.setUseDescender(true);
                totals2c.setHorizontalAlignment(1);
                totals2c.setRunDirection(3);
                totals2.addCell(totals2d);
                totals2.addCell(totals2c);
                table.addCell(totals2);
                PdfPTable totals3 = new PdfPTable(new float[] {1,1});
                PdfPCell totals3d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb3)), font)));
                totals3d.setUseDescender(true);
                totals3d.setHorizontalAlignment(1);
                totals3d.setRunDirection(3);
                PdfPCell totals3c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr3)), font)));
                totals3c.setUseDescender(true);
                totals3c.setHorizontalAlignment(1);
                totals3c.setRunDirection(3);
                totals3.addCell(totals3d);
                totals3.addCell(totals3c);
                table.addCell(totals3);
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
                Paragraph details = new Paragraph("FOR"+quarter+"-"+year);
                details.setAlignment(1);
                details.setSpacingAfter(5.0F);
                document.add(details);
                Image companyLogo = Image.getInstance("/vulcan/src/assetes/reportlogo.png");
                companyLogo.scalePercent(7);
                companyLogo.setSpacingAfter(50F);
                companyLogo.setAbsolutePosition(0, PageSize.A4.getHeight() - companyLogo.getScaledHeight()+20);
                document.add(companyLogo);
                table.setSpacingBefore(50F);
                document.add(table);
                document.close();
                System.out.println("Done");
                conn.close();
            } catch (Exception e) {
                System.out.print(e);
            }
        }else if(type.getValue().equals("LAST YEAR")){
            comparison.setDisable(true);
            periods.setDisable(true);
            dt1.setDisable(true);
            dt2.setDisable(true);
            conf.setDisable(true);
            comparison.setDisable(true);
            periods.setDisable(true);
            dt1.setDisable(true);
            dt2.setDisable(true);
            conf.setDisable(true);
            DateTimeFormatter t = DateTimeFormatter.ofPattern("hh:mm a");
            LocalDateTime now1 = LocalDateTime.now();
            this.hour = t.format(now1);
            DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime now = LocalDateTime.now();
            this.dt = dtr.format(now);
            Document document = new Document();
            FontFactory.registerDirectories();
            Font font = FontFactory.getFont("arial", "Identity-H", true, 11.0F);
            PdfPTable table = new PdfPTable(new float[] { 2, 2, 2, 2 });
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell("ACCOUNT");
            table.addCell("OPENING BALANCE");
            table.addCell(String.valueOf(now.minusYears(1)));
            table.addCell("TOTAL");
            table.addCell("     ");
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
            PdfPTable ttable = new PdfPTable(new float[] {1,1});
            ttable.getDefaultCell().setBorder(0);
            PdfPCell table_celld3 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
            table_celld3.setHorizontalAlignment(1);
            table_celld3.setBorder(0);
            PdfPCell table_cellc3 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
            table_cellc3.setHorizontalAlignment(1);
            table_cellc3.setBorder(0);
            ttable.addCell(table_celld3);
            ttable.addCell(table_cellc3);
            table.addCell( ttable);
            table.setHeaderRows(1);
            PdfPCell[] cells = table.getRow(0).getCells();
            for (int j=0;j<cells.length;j++){
                cells[j].setBackgroundColor(BaseColor.GRAY);
            }
            try {
                tdb1=0;
                tcr1=0;
                tdb2=0;
                tcr2=0;
                tdb3=0;
                tcr3=0;
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
                    PdfPTable tbtable1 = new PdfPTable(new float[] {1,1});
                    PdfPCell table_celld6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(todb)), font)));
                    table_celld6.setUseDescender(true);
                    table_celld6.setHorizontalAlignment(1);
                    table_celld6.setRunDirection(3);
                    PdfPCell table_cellc6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tocr)), font)));
                    table_cellc6.setUseDescender(true);
                    table_cellc6.setHorizontalAlignment(1);
                    table_cellc6.setRunDirection(3);
                    tbtable1.addCell(table_celld6);
                    tbtable1.addCell(table_cellc6);
                    table.addCell(tbtable1);
                }
                table.addCell("TOTALS");
                PdfPTable totals1 = new PdfPTable(new float[] {1,1});
                PdfPCell totals1d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb1)), font)));
                totals1d.setUseDescender(true);
                totals1d.setHorizontalAlignment(1);
                totals1d.setRunDirection(3);
                PdfPCell totals1c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr1)), font)));
                totals1c.setUseDescender(true);
                totals1c.setHorizontalAlignment(1);
                totals1c.setRunDirection(3);
                totals1.addCell(totals1d);
                totals1.addCell(totals1c);
                table.addCell(totals1);
                PdfPTable totals2 = new PdfPTable(new float[] {1,1});
                PdfPCell totals2d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb2)), font)));
                totals2d.setUseDescender(true);
                totals2d.setHorizontalAlignment(1);
                totals2d.setRunDirection(3);
                PdfPCell totals2c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr2)), font)));
                totals2c.setUseDescender(true);
                totals2c.setHorizontalAlignment(1);
                totals2c.setRunDirection(3);
                totals2.addCell(totals2d);
                totals2.addCell(totals2c);
                table.addCell(totals2);
                PdfPTable totals3 = new PdfPTable(new float[] {1,1});
                PdfPCell totals3d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb3)), font)));
                totals3d.setUseDescender(true);
                totals3d.setHorizontalAlignment(1);
                totals3d.setRunDirection(3);
                PdfPCell totals3c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr3)), font)));
                totals3c.setUseDescender(true);
                totals3c.setHorizontalAlignment(1);
                totals3c.setRunDirection(3);
                totals3.addCell(totals3d);
                totals3.addCell(totals3c);
                table.addCell(totals3);
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
                Paragraph details = new Paragraph(String.valueOf(now.minusYears(1)));
                details.setAlignment(1);
                details.setSpacingAfter(5.0F);
                document.add(details);
                Image companyLogo = Image.getInstance("/vulcan/src/assetes/reportlogo.png");
                companyLogo.scalePercent(7);
                companyLogo.setSpacingAfter(50F);
                companyLogo.setAbsolutePosition(0, PageSize.A4.getHeight() - companyLogo.getScaledHeight()+20);
                document.add(companyLogo);
                table.setSpacingBefore(50F);
                document.add(table);
                document.close();
                System.out.println("Done");
                conn.close();
            } catch (Exception e) {
                System.out.print(e);
            }
        }else if(type.getValue().equals("SPECIFIC")){
            comparison.setDisable(true);
            periods.setDisable(true);
            dt1.setDisable(false);
            dt2.setDisable(false);
            conf.setDisable(false);
            comparison.setDisable(true);
            periods.setDisable(true);
            dt1.setDisable(true);
            dt2.setDisable(true);
            conf.setDisable(true);
            comparison.setDisable(true);
            periods.setDisable(true);
            dt1.setDisable(true);
            dt2.setDisable(true);
            conf.setDisable(true);
            DateTimeFormatter t = DateTimeFormatter.ofPattern("hh:mm a");
            LocalDateTime now1 = LocalDateTime.now();
            this.hour = t.format(now1);
            DateTimeFormatter dtr = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime now = LocalDateTime.now();
            this.dt = dtr.format(now);
            Document document = new Document();
            FontFactory.registerDirectories();
            Font font = FontFactory.getFont("arial", "Identity-H", true, 11.0F);
            PdfPTable table = new PdfPTable(new float[] { 2, 2, 2, 2 });
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell("ACCOUNT");
            table.addCell("OPENING BALANCE");
            table.addCell(String.valueOf(now.minusYears(1)));
            table.addCell("TOTAL");
            table.addCell("     ");
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
            PdfPTable ttable = new PdfPTable(new float[] {1,1});
            ttable.getDefaultCell().setBorder(0);
            PdfPCell table_celld3 = new PdfPCell(new Phrase(new Chunk("DEBIT", font)));
            table_celld3.setHorizontalAlignment(1);
            table_celld3.setBorder(0);
            PdfPCell table_cellc3 = new PdfPCell(new Phrase(new Chunk("CREDIT", font)));
            table_cellc3.setHorizontalAlignment(1);
            table_cellc3.setBorder(0);
            ttable.addCell(table_celld3);
            ttable.addCell(table_cellc3);
            table.addCell( ttable);
            table.setHeaderRows(1);
            PdfPCell[] cells = table.getRow(0).getCells();
            for (int j=0;j<cells.length;j++){
                cells[j].setBackgroundColor(BaseColor.GRAY);
            }
            try {
                tdb1=0;
                tcr1=0;
                tdb2=0;
                tcr2=0;
                tdb3=0;
                tcr3=0;
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
                    statement3.execute("SELECT SUM(debit) as sdb , SUM(credit) as scr FROM journal WHERE parent_id='" + results.getString("id") +"' AND dt between '"+dt1.getValue()+"' AND '"+dt2.getValue()+"' ");                    ResultSet results2 = statement3.getResultSet();
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
                    PdfPTable tbtable1 = new PdfPTable(new float[] {1,1});
                    PdfPCell table_celld6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(todb)), font)));
                    table_celld6.setUseDescender(true);
                    table_celld6.setHorizontalAlignment(1);
                    table_celld6.setRunDirection(3);
                    PdfPCell table_cellc6 = new PdfPCell(new Phrase(new Chunk((String.valueOf(tocr)), font)));
                    table_cellc6.setUseDescender(true);
                    table_cellc6.setHorizontalAlignment(1);
                    table_cellc6.setRunDirection(3);
                    tbtable1.addCell(table_celld6);
                    tbtable1.addCell(table_cellc6);
                    table.addCell(tbtable1);
                }
                table.addCell("TOTALS");
                PdfPTable totals1 = new PdfPTable(new float[] {1,1});
                PdfPCell totals1d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb1)), font)));
                totals1d.setUseDescender(true);
                totals1d.setHorizontalAlignment(1);
                totals1d.setRunDirection(3);
                PdfPCell totals1c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr1)), font)));
                totals1c.setUseDescender(true);
                totals1c.setHorizontalAlignment(1);
                totals1c.setRunDirection(3);
                totals1.addCell(totals1d);
                totals1.addCell(totals1c);
                table.addCell(totals1);
                PdfPTable totals2 = new PdfPTable(new float[] {1,1});
                PdfPCell totals2d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb2)), font)));
                totals2d.setUseDescender(true);
                totals2d.setHorizontalAlignment(1);
                totals2d.setRunDirection(3);
                PdfPCell totals2c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr2)), font)));
                totals2c.setUseDescender(true);
                totals2c.setHorizontalAlignment(1);
                totals2c.setRunDirection(3);
                totals2.addCell(totals2d);
                totals2.addCell(totals2c);
                table.addCell(totals2);
                PdfPTable totals3 = new PdfPTable(new float[] {1,1});
                PdfPCell totals3d  = new PdfPCell(new Phrase(new Chunk((String.valueOf(tdb3)), font)));
                totals3d.setUseDescender(true);
                totals3d.setHorizontalAlignment(1);
                totals3d.setRunDirection(3);
                PdfPCell totals3c = new PdfPCell(new Phrase(new Chunk((String.valueOf(tcr3)), font)));
                totals3c.setUseDescender(true);
                totals3c.setHorizontalAlignment(1);
                totals3c.setRunDirection(3);
                totals3.addCell(totals3d);
                totals3.addCell(totals3c);
                table.addCell(totals3);
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
                Paragraph details = new Paragraph(String.valueOf(now.minusYears(1)));
                details.setAlignment(1);
                details.setSpacingAfter(5.0F);
                document.add(details);
                Image companyLogo = Image.getInstance("/vulcan/src/assetes/reportlogo.png");
                companyLogo.scalePercent(7);
                companyLogo.setSpacingAfter(50F);
                companyLogo.setAbsolutePosition(0, PageSize.A4.getHeight() - companyLogo.getScaledHeight()+20);
                document.add(companyLogo);
                table.setSpacingBefore(50F);
                document.add(table);
                document.close();
                System.out.println("Done");
                conn.close();
            } catch (Exception e) {
                System.out.print(e);
            }
        }

    }

}
