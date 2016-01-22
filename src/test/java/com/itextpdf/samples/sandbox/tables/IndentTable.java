package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.canvas.PdfCanvas;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.model.Document;
import com.itextpdf.model.Property;
import com.itextpdf.model.element.Cell;
import com.itextpdf.model.element.Paragraph;
import com.itextpdf.model.element.Table;
import com.itextpdf.samples.GenericTest;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

@Ignore
@Category(SampleTest.class)
public class IndentTable extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/indent_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new IndentTable().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        PdfCanvas cb = new PdfCanvas(pdfDoc.addNewPage()); //  writer.getDirectContent();
        cb.moveTo(36, 842);
        cb.lineTo(36, 0);
        cb.stroke();
        Table table = new Table(8);
        table.setHorizontalAlignment(Property.HorizontalAlignment.LEFT);
        table.setWidth(150);
        for (int aw = 0; aw < 16; aw++) {
            table.addCell(new Cell().add(new Paragraph("hi")));
        }
        // TODO can't add table to paragraph
//        Paragraph p = new Paragraph();
//        p.setMarginLeft(36);
//        p.add(table);
//        doc.add(p);
        doc.add(table);

        doc.close();
    }
}