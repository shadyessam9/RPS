package main;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import java.io.FileOutputStream;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.itextpdf.text.pdf.draw.LineSeparator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Main;


public class reports implements Initializable {
    @FXML
    private ComboBox type;

    String key;
    String filename;
    String dt;
    String hour;


    Statement statement1 ;
    Statement statement2;
    Statement statement3;
    Statement statement4;
    String accountname;
    double income =0;
    double costs =0;
    double fa =0;
    double td =0;
    double tfa =0;
    double tca =0;
    double l =0;
    double n =0;
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


    double nt = 0;

    @FXML
    public AnchorPane acc;
    @FXML
    private Pane host;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        type.getItems().add("TRAIL BALANCE");
        type.getItems().add("GENERAL LEDGER");
        type.getItems().add("INCOME STATEMENT");
        type.getItems().add("BALANCE SHEET");
        type.getItems().add("ENTITY ACCOUNT");
        type.getItems().add("PROJECT");
        type.getItems().add("EXTRACT");




        type.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(newValue.equals("TRAIL BALANCE")) {
                try {
                    tb();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (newValue.equals("GENERAL LEDGER")){
                try {
                    gl();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (newValue.equals("ENTITY ACCOUNT")){
                try {
                    er();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (newValue.equals("PROJECT")){
                try {
                    pr();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (newValue.equals("EXTRACT")){
                try {
                    ex();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }


    @FXML
    private void tb() throws IOException {

        host.getChildren().clear();
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("tbreports.fxml"));
        host.getChildren().add(newLoadedPane);
    }
    @FXML
    private void gl() throws IOException {

        host.getChildren().clear();
        Pane newLoadedPane = FXMLLoader.load(getClass().getResource("glreports.fxml"));
        host.getChildren().add(newLoadedPane);
    }
    @FXML
    public void er() throws IOException {
        host.getChildren().clear();
        Pane newLoadedPane =  FXMLLoader.load(getClass().getResource("ereport.fxml"));
        host.getChildren().add(newLoadedPane);

    }
    @FXML
    public void pr() throws IOException {
        host.getChildren().clear();
        Pane newLoadedPane =  FXMLLoader.load(getClass().getResource("preport.fxml"));
        host.getChildren().add(newLoadedPane);

    }
    @FXML
    public void ex() throws IOException {
        host.getChildren().clear();
        Pane newLoadedPane =  FXMLLoader.load(getClass().getResource("exreport.fxml"));
        host.getChildren().add(newLoadedPane);

    }





    public void createrep() throws Exception {
        if(type.getValue().equals("INCOME STATEMENT")){
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


            FontFactory.registerDirectories();
            FontFactory.register("c:/windows/fonts/tradbdo.ttf", "my_arabic");
            Font font = FontFactory.getFont("my_arabic" ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED,10f);;


            PdfPTable table = new PdfPTable(new float[] { 2, 2 });
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.setSpacingBefore(20F);


            PdfPTable table1 = new PdfPTable(new float[] { 2, 2 });
            table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.setSpacingBefore(20F);

            PdfPTable table2 = new PdfPTable(new float[] { 2, 2 });
            table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.setSpacingBefore(5F);


            try{
                conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                statement1 = conn.createStatement();
                statement1.execute("SELECT (SELECT (SUM(debit))-SUM(credit) FROM journal where parent_id='200' AND YEAR(dt) = YEAR(CURDATE())) + (SELECT (SUM(debit))-SUM(credit) FROM openningbalances where parent_id='200' AND YEAR(dt) = YEAR(CURDATE())) AS sum");
                ResultSet results = statement1.getResultSet();
                while (results.next()) {
                    income=results.getInt("sum");
                    PdfPCell table_cellspace= new PdfPCell(new Phrase(new Chunk("      ", font)));
                    table_cellspace.setUseDescender(true);
                    table_cellspace.setHorizontalAlignment(1);
                    table_cellspace.setBorder(0);
                    table_cellspace.setBackgroundColor(BaseColor.GRAY);
                    table_cellspace.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_cellspace.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table.addCell(table_cellspace);
                    PdfPCell table_celltitle = new PdfPCell(new Phrase(new Chunk("الايرادات", font)));
                    table_celltitle.setUseDescender(true);
                    table_celltitle.setBackgroundColor(BaseColor.GRAY);
                    table_celltitle.setHorizontalAlignment(1);
                    table_celltitle.setBorder(0);
                    table_celltitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_celltitle.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table.addCell(table_celltitle);

                    statement2 = conn.createStatement();
                    statement2.execute("SELECT * FROM acctree WHERE parent_id='400'");
                    ResultSet results1 = statement2.getResultSet();
                    while (results1.next()) {
                        statement3 = conn.createStatement();
                        statement3.execute("SELECT (SELECT (SUM(debit))-SUM(credit) FROM journal where acc_id='"+results1.getString("id")+"' AND YEAR(dt) = YEAR(CURDATE())) + (SELECT (SUM(debit))-SUM(credit) FROM openningbalances where acc_id='"+results1.getString("id")+"' AND YEAR(dt) = YEAR(CURDATE())) AS sum");
                        ResultSet results2 = statement3.getResultSet();
                        while (results2.next()) {
                            PdfPCell table_cellv = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("sum")), font)));
                            table_cellv.setUseDescender(true);
                            table_cellv.setHorizontalAlignment(1);
                            table_cellv.setBorder(0);
                            table_cellv.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table_cellv.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                            table.addCell(table_cellv);
                        }
                        PdfPCell table_celln = new PdfPCell(new Phrase(new Chunk(String.valueOf(results1.getString("title")), font)));
                        table_celln.setUseDescender(true);
                        table_celln.setHorizontalAlignment(1);
                        table_celln.setBorder(0);
                        table_celln.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table_celln.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        table.addCell(table_celln);
                    }
                    PdfPCell table_cellincome = new PdfPCell(new Phrase(new Chunk(String.valueOf(income), font)));
                    table_cellincome.setUseDescender(true);
                    table_cellincome.setHorizontalAlignment(1);
                    table_cellincome.setBorder(0);
                    table_cellincome.setBackgroundColor(BaseColor.GRAY);
                    table_cellincome.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_cellincome.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table.addCell(table_cellincome);
                    PdfPCell table_celltotal = new PdfPCell(new Phrase(new Chunk("الاجمالي", font)));
                    table_celltotal.setUseDescender(true);
                    table_celltotal.setBackgroundColor(BaseColor.GRAY);
                    table_celltotal.setHorizontalAlignment(1);
                    table_celltotal.setBorder(0);
                    table_celltotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_celltotal.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table.addCell(table_celltotal);
                }
                document.add(table);
            } catch (Exception e) {
                System.out.print(e);
            }




            try{
                conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                statement1 = conn.createStatement();
                statement1.execute("SELECT (SELECT (SUM(debit))-SUM(credit) FROM journal where parent_id='300' AND YEAR(dt) = YEAR(CURDATE())) + (SELECT (SUM(debit))-SUM(credit) FROM openningbalances where parent_id='300' AND YEAR(dt) = YEAR(CURDATE())) AS sum");
                ResultSet results = statement1.getResultSet();
                while (results.next()) {
                    costs=results.getInt("sum");
                    PdfPCell table_cellspace= new PdfPCell(new Phrase(new Chunk("      ", font)));
                    table_cellspace.setUseDescender(true);
                    table_cellspace.setHorizontalAlignment(1);
                    table_cellspace.setBorder(0);
                    table_cellspace.setBackgroundColor(BaseColor.GRAY);
                    table_cellspace.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_cellspace.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table1.addCell(table_cellspace);
                    PdfPCell table_celltitle = new PdfPCell(new Phrase(new Chunk("المصروفات", font)));
                    table_celltitle.setUseDescender(true);
                    table_celltitle.setBackgroundColor(BaseColor.GRAY);
                    table_celltitle.setHorizontalAlignment(1);
                    table_celltitle.setBorder(0);
                    table_celltitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_celltitle.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table1.addCell(table_celltitle);

                    statement2 = conn.createStatement();
                    statement2.execute("SELECT * FROM acctree WHERE parent_id='300'");
                    ResultSet results1 = statement2.getResultSet();
                    while (results1.next()) {
                        statement3 = conn.createStatement();
                        statement3.execute("SELECT (SELECT (SUM(debit))-SUM(credit) FROM journal where acc_id='"+results1.getString("id")+"' AND YEAR(dt) = YEAR(CURDATE())) + (SELECT (SUM(debit))-SUM(credit) FROM openningbalances where acc_id='"+results1.getString("id")+"' AND YEAR(dt) = YEAR(CURDATE())) AS sum");
                        ResultSet results2 = statement3.getResultSet();
                        while (results2.next()) {
                            PdfPCell table_cellv = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("sum")), font)));
                            table_cellv.setUseDescender(true);
                            table_cellv.setHorizontalAlignment(1);
                            table_cellv.setBorder(0);
                            table_cellv.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table_cellv.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                            table1.addCell(table_cellv);
                            PdfPCell table_celln = new PdfPCell(new Phrase(new Chunk(String.valueOf(results1.getString("title")), font)));
                            table_celln.setUseDescender(true);
                            table_celln.setHorizontalAlignment(1);
                            table_celln.setBorder(0);
                            table_celln.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table_celln.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                            table1.addCell(table_celln);
                        }

                    }
                    PdfPCell table_cellincome = new PdfPCell(new Phrase(new Chunk(String.valueOf(costs), font)));
                    table_cellincome.setUseDescender(true);
                    table_cellincome.setHorizontalAlignment(1);
                    table_cellincome.setBorder(0);
                    table_cellincome.setBackgroundColor(BaseColor.GRAY);
                    table_cellincome.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_cellincome.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table1.addCell(table_cellincome);
                    PdfPCell table_celltotal = new PdfPCell(new Phrase(new Chunk("الاجمالي", font)));
                    table_celltotal.setUseDescender(true);
                    table_celltotal.setBackgroundColor(BaseColor.GRAY);
                    table_celltotal.setHorizontalAlignment(1);
                    table_celltotal.setBorder(0);
                    table_celltotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_celltotal.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table1.addCell(table_celltotal);
                }
                document.add(table1);
            } catch (Exception e) {
                System.out.print(e);
            }

            LineSeparator ls3 = new LineSeparator();
            document.add(new Chunk(ls3));


            nt=income-costs;

            PdfPCell table_cellnincome = new PdfPCell(new Phrase(new Chunk(String.valueOf(nt), font)));
            table_cellnincome.setUseDescender(true);
            table_cellnincome.setHorizontalAlignment(1);
            table_cellnincome.setBorder(0);
            table_cellnincome.setBackgroundColor(BaseColor.GRAY);
            table_cellnincome.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cellnincome.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table2.addCell(table_cellnincome);
            PdfPCell table_celltotal = new PdfPCell(new Phrase(new Chunk("اجمالي الدخل", font)));
            table_celltotal.setUseDescender(true);
            table_celltotal.setBackgroundColor(BaseColor.GRAY);
            table_celltotal.setHorizontalAlignment(1);
            table_celltotal.setBorder(0);
            table_celltotal.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_celltotal.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table2.addCell(table_celltotal);

            document.add(table2);


            document.close();
            System.out.println("Done");
        }else if (type.getValue().equals("BALANCE SHEET")){
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


            FontFactory.registerDirectories();
            FontFactory.register("c:/windows/fonts/tradbdo.ttf", "my_arabic");
            Font font = FontFactory.getFont("my_arabic" ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED,10f);;


            PdfPTable table = new PdfPTable(new float[] { 2, 2 });
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.setSpacingBefore(20F);


            PdfPTable table0 = new PdfPTable(new float[] { 2, 2 });
            table0.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table0.setSpacingBefore(20F);



            PdfPTable table1 = new PdfPTable(new float[] { 2, 2 });
            table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.setSpacingBefore(20F);

            PdfPTable table2 = new PdfPTable(new float[] { 2, 2 });
            table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.setSpacingBefore(20F);


            PdfPTable table3 = new PdfPTable(new float[] { 2, 2 });
            table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.setSpacingBefore(20F);


            PdfPTable table4 = new PdfPTable(new float[] { 2, 2 });
            table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.setSpacingBefore(20F);


            try{
                conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                statement1 = conn.createStatement();
                statement1.execute("SELECT (SELECT (SUM(debit))-SUM(credit) FROM journal where parent_id='9431717022022' AND YEAR(dt) = YEAR(CURDATE())) + (SELECT (SUM(debit))-SUM(credit) FROM openningbalances where parent_id='9431717022022' AND YEAR(dt) = YEAR(CURDATE())) AS sum");
                ResultSet results = statement1.getResultSet();
                while (results.next()) {
                    fa=results.getInt("sum");
                    PdfPCell table_cellspace= new PdfPCell(new Phrase(new Chunk("      ", font)));
                    table_cellspace.setUseDescender(true);
                    table_cellspace.setHorizontalAlignment(1);
                    table_cellspace.setBorder(0);
                    table_cellspace.setBackgroundColor(BaseColor.GRAY);
                    table_cellspace.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_cellspace.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table.addCell(table_cellspace);
                    PdfPCell table_celltitle = new PdfPCell(new Phrase(new Chunk("الاصول الثابته", font)));
                    table_celltitle.setUseDescender(true);
                    table_celltitle.setBackgroundColor(BaseColor.GRAY);
                    table_celltitle.setHorizontalAlignment(1);
                    table_celltitle.setBorder(0);
                    table_celltitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_celltitle.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table.addCell(table_celltitle);

                    statement2 = conn.createStatement();
                    statement2.execute("SELECT * FROM acctree WHERE parent_id='9431717022022'");
                    ResultSet results1 = statement2.getResultSet();
                    while (results1.next()) {
                        statement3 = conn.createStatement();
                        statement3.execute("SELECT (SELECT (SUM(debit))-SUM(credit) FROM journal where acc_id='"+results1.getString("id")+"' AND YEAR(dt) = YEAR(CURDATE())) + (SELECT (SUM(debit))-SUM(credit) FROM openningbalances where acc_id='"+results1.getString("id")+"' AND YEAR(dt) = YEAR(CURDATE())) AS sum");
                        ResultSet results2 = statement3.getResultSet();
                        while (results2.next()) {
                            PdfPCell table_cellv = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("sum")), font)));
                            table_cellv.setUseDescender(true);
                            table_cellv.setHorizontalAlignment(1);
                            table_cellv.setBorder(0);
                            table_cellv.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table_cellv.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                            table.addCell(table_cellv);
                        }
                        PdfPCell table_celln = new PdfPCell(new Phrase(new Chunk(String.valueOf(results1.getString("title")), font)));
                        table_celln.setUseDescender(true);
                        table_celln.setHorizontalAlignment(1);
                        table_celln.setBorder(0);
                        table_celln.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table_celln.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        table.addCell(table_celln);
                    }
                    PdfPCell table_cellincome = new PdfPCell(new Phrase(new Chunk(String.valueOf(fa), font)));
                    table_cellincome.setUseDescender(true);
                    table_cellincome.setHorizontalAlignment(1);
                    table_cellincome.setBorder(0);
                    table_cellincome.setBackgroundColor(BaseColor.GRAY);
                    table_cellincome.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_cellincome.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table.addCell(table_cellincome);
                    PdfPCell table_celltotal = new PdfPCell(new Phrase(new Chunk("الاجمالي", font)));
                    table_celltotal.setUseDescender(true);
                    table_celltotal.setBackgroundColor(BaseColor.GRAY);
                    table_celltotal.setHorizontalAlignment(1);
                    table_celltotal.setBorder(0);
                    table_celltotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_celltotal.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table.addCell(table_celltotal);
                }
                document.add(table);
            } catch (Exception e) {
                System.out.print(e);
            }


            try{
                conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                statement1 = conn.createStatement();
                statement1.execute("SELECT (SELECT (SUM(debit))-SUM(credit) FROM journal where parent_id='9511817022022' AND YEAR(dt) = YEAR(CURDATE())) + (SELECT (SUM(debit))-SUM(credit) FROM openningbalances where parent_id='9511817022022' AND YEAR(dt) = YEAR(CURDATE())) AS sum");
                ResultSet results = statement1.getResultSet();
                while (results.next()) {
                    td=results.getInt("sum");
                    PdfPCell table_cellspace= new PdfPCell(new Phrase(new Chunk("      ", font)));
                    table_cellspace.setUseDescender(true);
                    table_cellspace.setHorizontalAlignment(1);
                    table_cellspace.setBorder(0);
                    table_cellspace.setBackgroundColor(BaseColor.GRAY);
                    table_cellspace.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_cellspace.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table1.addCell(table_cellspace);
                    PdfPCell table_celltitle = new PdfPCell(new Phrase(new Chunk("مجمع الاهلاك", font)));
                    table_celltitle.setUseDescender(true);
                    table_celltitle.setBackgroundColor(BaseColor.GRAY);
                    table_celltitle.setHorizontalAlignment(1);
                    table_celltitle.setBorder(0);
                    table_celltitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_celltitle.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table1.addCell(table_celltitle);

                    statement2 = conn.createStatement();
                    statement2.execute("SELECT * FROM acctree WHERE parent_id='9511817022022'");
                    ResultSet results1 = statement2.getResultSet();
                    while (results1.next()) {
                        statement3 = conn.createStatement();
                        statement3.execute("SELECT (SELECT (SUM(debit))-SUM(credit) FROM journal where acc_id='"+results1.getString("id")+"' AND YEAR(dt) = YEAR(CURDATE())) + (SELECT (SUM(debit))-SUM(credit) FROM openningbalances where acc_id='"+results1.getString("id")+"' AND YEAR(dt) = YEAR(CURDATE())) AS sum");
                        ResultSet results2 = statement3.getResultSet();
                        while (results2.next()) {
                            PdfPCell table_cellv = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("sum")), font)));
                            table_cellv.setUseDescender(true);
                            table_cellv.setHorizontalAlignment(1);
                            table_cellv.setBorder(0);
                            table_cellv.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table_cellv.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                            table1.addCell(table_cellv);
                        }
                        PdfPCell table_celln = new PdfPCell(new Phrase(new Chunk(String.valueOf(results1.getString("title")), font)));
                        table_celln.setUseDescender(true);
                        table_celln.setHorizontalAlignment(1);
                        table_celln.setBorder(0);
                        table_celln.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table_celln.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        table1.addCell(table_celln);
                    }
                    PdfPCell table_cellincome = new PdfPCell(new Phrase(new Chunk(String.valueOf(td), font)));
                    table_cellincome.setUseDescender(true);
                    table_cellincome.setHorizontalAlignment(1);
                    table_cellincome.setBorder(0);
                    table_cellincome.setBackgroundColor(BaseColor.GRAY);
                    table_cellincome.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_cellincome.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table1.addCell(table_cellincome);
                    PdfPCell table_celltotal = new PdfPCell(new Phrase(new Chunk("الاجمالي", font)));
                    table_celltotal.setUseDescender(true);
                    table_celltotal.setBackgroundColor(BaseColor.GRAY);
                    table_celltotal.setHorizontalAlignment(1);
                    table_celltotal.setBorder(0);
                    table_celltotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_celltotal.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table1.addCell(table_celltotal);
                }
                document.add(table1);
            } catch (Exception e) {
                System.out.print(e);
            }


            tfa=fa-td;

            PdfPCell table_cellfa = new PdfPCell(new Phrase(new Chunk(String.valueOf(tfa), font)));
            table_cellfa.setUseDescender(true);
            table_cellfa.setHorizontalAlignment(1);
            table_cellfa.setBorder(0);
            table_cellfa.setBackgroundColor(BaseColor.GRAY);
            table_cellfa.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cellfa.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table0.addCell(table_cellfa);
            PdfPCell table_celltfa = new PdfPCell(new Phrase(new Chunk("اجمالي الاصول الثابته", font)));
            table_celltfa.setUseDescender(true);
            table_celltfa.setBackgroundColor(BaseColor.GRAY);
            table_celltfa.setHorizontalAlignment(1);
            table_celltfa.setBorder(0);
            table_celltfa.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_celltfa.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table0.addCell(table_celltfa);
            document.add(table0);



            try{
                conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                statement1 = conn.createStatement();
                statement1.execute("SELECT (SELECT (SUM(debit))-SUM(credit) FROM journal where parent_id='9434417022022' AND YEAR(dt) = YEAR(CURDATE())) + (SELECT (SUM(debit))-SUM(credit) FROM openningbalances where parent_id='9434417022022' AND YEAR(dt) = YEAR(CURDATE())) AS sum");
                ResultSet results = statement1.getResultSet();
                while (results.next()) {
                    tca=results.getInt("sum");
                    PdfPCell table_cellspace= new PdfPCell(new Phrase(new Chunk("      ", font)));
                    table_cellspace.setUseDescender(true);
                    table_cellspace.setHorizontalAlignment(1);
                    table_cellspace.setBorder(0);
                    table_cellspace.setBackgroundColor(BaseColor.GRAY);
                    table_cellspace.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_cellspace.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table2.addCell(table_cellspace);
                    PdfPCell table_celltitle = new PdfPCell(new Phrase(new Chunk("الاصول المتداوله", font)));
                    table_celltitle.setUseDescender(true);
                    table_celltitle.setBackgroundColor(BaseColor.GRAY);
                    table_celltitle.setHorizontalAlignment(1);
                    table_celltitle.setBorder(0);
                    table_celltitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_celltitle.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table2.addCell(table_celltitle);

                    statement2 = conn.createStatement();
                    statement2.execute("SELECT * FROM acctree WHERE parent_id='9511817022022'");
                    ResultSet results1 = statement2.getResultSet();
                    while (results1.next()) {
                        statement3 = conn.createStatement();
                        statement3.execute("SELECT (SELECT (SUM(debit))-SUM(credit) FROM journal where acc_id='"+results1.getString("id")+"' AND YEAR(dt) = YEAR(CURDATE())) + (SELECT (SUM(debit))-SUM(credit) FROM openningbalances where acc_id='"+results1.getString("id")+"' AND YEAR(dt) = YEAR(CURDATE())) AS sum");
                        ResultSet results2 = statement3.getResultSet();
                        while (results2.next()) {
                            PdfPCell table_cellv = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("sum")), font)));
                            table_cellv.setUseDescender(true);
                            table_cellv.setHorizontalAlignment(1);
                            table_cellv.setBorder(0);
                            table_cellv.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table_cellv.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                            table2.addCell(table_cellv);
                        }
                        PdfPCell table_celln = new PdfPCell(new Phrase(new Chunk(String.valueOf(results1.getString("title")), font)));
                        table_celln.setUseDescender(true);
                        table_celln.setHorizontalAlignment(1);
                        table_celln.setBorder(0);
                        table_celln.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table_celln.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        table2.addCell(table_celln);
                    }
                    PdfPCell table_cellincome = new PdfPCell(new Phrase(new Chunk(String.valueOf(tca), font)));
                    table_cellincome.setUseDescender(true);
                    table_cellincome.setHorizontalAlignment(1);
                    table_cellincome.setBorder(0);
                    table_cellincome.setBackgroundColor(BaseColor.GRAY);
                    table_cellincome.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_cellincome.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table2.addCell(table_cellincome);
                    PdfPCell table_celltotal = new PdfPCell(new Phrase(new Chunk("الاجمالي", font)));
                    table_celltotal.setUseDescender(true);
                    table_celltotal.setBackgroundColor(BaseColor.GRAY);
                    table_celltotal.setHorizontalAlignment(1);
                    table_celltotal.setBorder(0);
                    table_celltotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_celltotal.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table2.addCell(table_celltotal);
                }
                document.add(table2);
            } catch (Exception e) {
                System.out.print(e);
            }



            try{
                conn = DriverManager.getConnection("jdbc:mysql://" + Main.ip + "/fouadcompany", "root", "");
                statement1 = conn.createStatement();
                statement1.execute("SELECT (SELECT (SUM(debit))-SUM(credit) FROM journal where parent_id='200' AND YEAR(dt) = YEAR(CURDATE())) + (SELECT (SUM(debit))-SUM(credit) FROM openningbalances where parent_id='200' AND YEAR(dt) = YEAR(CURDATE())) AS sum");
                ResultSet results = statement1.getResultSet();
                while (results.next()) {
                    l=results.getInt("sum");
                    PdfPCell table_cellspace= new PdfPCell(new Phrase(new Chunk("      ", font)));
                    table_cellspace.setUseDescender(true);
                    table_cellspace.setHorizontalAlignment(1);
                    table_cellspace.setBorder(0);
                    table_cellspace.setBackgroundColor(BaseColor.GRAY);
                    table_cellspace.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_cellspace.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table3.addCell(table_cellspace);
                    PdfPCell table_celltitle = new PdfPCell(new Phrase(new Chunk("حقوق ملكيه & التزامات", font)));
                    table_celltitle.setUseDescender(true);
                    table_celltitle.setBackgroundColor(BaseColor.GRAY);
                    table_celltitle.setHorizontalAlignment(1);
                    table_celltitle.setBorder(0);
                    table_celltitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_celltitle.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table3.addCell(table_celltitle);

                    statement2 = conn.createStatement();
                    statement2.execute("SELECT * FROM acctree WHERE parent_id='200'");
                    ResultSet results1 = statement2.getResultSet();
                    while (results1.next()) {
                        statement3 = conn.createStatement();
                        statement3.execute("SELECT (SELECT (SUM(debit))-SUM(credit) FROM journal where acc_id='"+results1.getString("id")+"' AND YEAR(dt) = YEAR(CURDATE())) + (SELECT (SUM(debit))-SUM(credit) FROM openningbalances where acc_id='"+results1.getString("id")+"' AND YEAR(dt) = YEAR(CURDATE())) AS sum");
                        ResultSet results2 = statement3.getResultSet();
                        while (results2.next()) {
                            PdfPCell table_cellv = new PdfPCell(new Phrase(new Chunk(String.valueOf(results2.getString("sum")), font)));
                            table_cellv.setUseDescender(true);
                            table_cellv.setHorizontalAlignment(1);
                            table_cellv.setBorder(0);
                            table_cellv.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table_cellv.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                            table3.addCell(table_cellv);
                            PdfPCell table_celln = new PdfPCell(new Phrase(new Chunk(String.valueOf(results1.getString("title")), font)));
                            table_celln.setUseDescender(true);
                            table_celln.setHorizontalAlignment(1);
                            table_celln.setBorder(0);
                            table_celln.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table_celln.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                            table3.addCell(table_celln);
                        }

                    }
                    PdfPCell table_cellincome = new PdfPCell(new Phrase(new Chunk(String.valueOf(l), font)));
                    table_cellincome.setUseDescender(true);
                    table_cellincome.setHorizontalAlignment(1);
                    table_cellincome.setBorder(0);
                    table_cellincome.setBackgroundColor(BaseColor.GRAY);
                    table_cellincome.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_cellincome.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table3.addCell(table_cellincome);
                    PdfPCell table_celltotal = new PdfPCell(new Phrase(new Chunk("الاجمالي", font)));
                    table_celltotal.setUseDescender(true);
                    table_celltotal.setBackgroundColor(BaseColor.GRAY);
                    table_celltotal.setHorizontalAlignment(1);
                    table_celltotal.setBorder(0);
                    table_celltotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table_celltotal.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                    table3.addCell(table_celltotal);
                }
                document.add(table3);
            } catch (Exception e) {
                System.out.print(e);
            }

            LineSeparator ls3 = new LineSeparator();
            document.add(new Chunk(ls3));


            n=tfa+tca+(-l);

            PdfPCell table_cellnincome = new PdfPCell(new Phrase(new Chunk(String.valueOf(n), font)));
            table_cellnincome.setUseDescender(true);
            table_cellnincome.setHorizontalAlignment(1);
            table_cellnincome.setBorder(0);
            table_cellnincome.setBackgroundColor(BaseColor.GRAY);
            table_cellnincome.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cellnincome.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table4.addCell(table_cellnincome);
            PdfPCell table_celltotal = new PdfPCell(new Phrase(new Chunk("الصافي", font)));
            table_celltotal.setUseDescender(true);
            table_celltotal.setBackgroundColor(BaseColor.GRAY);
            table_celltotal.setHorizontalAlignment(1);
            table_celltotal.setBorder(0);
            table_celltotal.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_celltotal.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table4.addCell(table_celltotal);

            document.add(table4);


            document.close();
            System.out.println("Done");
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
