package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.basics.geom.PageSize;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.model.Document;
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
public class CellHeights extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/cell_heights.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CellHeights().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, PageSize.A5.rotate());

        Table table = new Table(2);
        // a long phrase
        Paragraph p = new Paragraph("Dr. iText or: How I Learned to Stop Worrying and Love PDF.");
        Cell cell = new Cell().add(p);
        // the prhase is wrapped
        table.addCell(new Cell().add(new Paragraph("wrap")));
        // TODO Implement noWrap == true
        //cell.setNoWrap(false);
        table.addCell(cell.clone(true));
        // the phrase isn't wrapped
        table.addCell(new Cell().add(new Paragraph("no wrap")));
        // TODO Implement noWrap == true
        // cell.setNoWrap(true);
        table.addCell(cell.clone(true));
        // a long phrase with newlines
        p = new Paragraph("Dr. iText or:\nHow I Learned to Stop Worrying\nand Love PDF.");
        cell = new Cell().add(p);
        table.addCell(new Cell().add(new Paragraph("No problems with heights")));
        // There is no problem in itext6 with Minimum anf Fixed heights.
        // If text's heights is bigger than cell's, cells's heights will grow instantly
        cell.setHeight(36f);
        table.addCell(cell.clone(true));
        // The last row is extended
        // TODO Implement setExtendLastRow(boolean)
        //table.setExtendLastRow(true);
        table.addCell(new Cell().add(new Paragraph("extend last row")));
        table.addCell(cell.clone(true));
        doc.add(table);

        doc.close();
    }
}
