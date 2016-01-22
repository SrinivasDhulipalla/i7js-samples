package com.itextpdf.samples.book.part4.chapter13;

import com.itextpdf.basics.geom.PageSize;
import com.itextpdf.core.pdf.PdfArray;
import com.itextpdf.core.pdf.PdfDictionary;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfName;
import com.itextpdf.core.pdf.PdfReader;
import com.itextpdf.core.pdf.PdfString;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.core.xmp.XMPException;
import com.itextpdf.model.Document;
import com.itextpdf.model.element.AreaBreak;
import com.itextpdf.model.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

@Ignore
@Category(SampleTest.class)
public class Listing_13_11_PageLabelExample extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part4/chapter13/Listing_13_11_PageLabelExample.pdf";
    public static final String RESULT
            = "results/part4/chapter13/page_labels.pdf";
    public static final String RESULT2
            = "results/part4/chapter13/page_labels_changed.pdf";
    public static final String LABELS
            = "results/part4/chapter13/page_labels.txt";
    public static final String[] SQL = {
            "SELECT country FROM film_country ORDER BY country",
            "SELECT name FROM film_director ORDER BY name",
            "SELECT title FROM film_movietitle ORDER BY title"
    };
    /** SQL statements */
    public static final String[] FIELD = { "country", "name", "title" };

    public static void main(String args[]) throws IOException, SQLException, XMPException {
        new Listing_13_11_PageLabelExample().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws IOException, SQLException, XMPException {
        createPdf(RESULT);
        listPageLabels(RESULT, LABELS);
        manipulatePageLabel(RESULT, RESULT2);
    }

    /**
     * Creates a PDF document.
     * @param dest the path to the new PDF document
     * @throws    IOException
     * @throws    SQLException
     */
    public void createPdf(String dest)
            throws IOException, SQLException{
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A5);
        int[] start = new int[3];
        pdfDoc.addNewPage();
        for (int i = 0; i < 3; i++) {
            start[i] = pdfDoc.getPageNumber(pdfDoc.getLastPage());
            addParagraphs(doc, connection, SQL[i], FIELD[i]);
            doc.add(new AreaBreak());
        }
        // TODO No PdfPageLabels
        // PdfPageLabels labels = new PdfPageLabels();
        // labels.addPageLabel(start[0], PdfPageLabels.UPPERCASE_LETTERS);
        // labels.addPageLabel(start[1], PdfPageLabels.DECIMAL_ARABIC_NUMERALS);
        // labels.addPageLabel(start[2],
        //         PdfPageLabels.DECIMAL_ARABIC_NUMERALS, "Movies-", start[2] - start[1] + 1);
        // writer.setPageLabels(labels);
        pdfDoc.close();
        connection.close();
    }

    /**
     * Performs an SQL query and writes the results to a PDF using the Paragraph object.
     * @param doc    The document to which the paragraphs have to be added
     * @param connection  The database connection that has to be used
     * @param sql         The SQL statement
     * @param field       The name of the field that has to be shown
     * @throws SQLException
     * @throws IOException
     */
    public void addParagraphs(Document doc, DatabaseConnection connection, String sql, String field)
            throws SQLException,IOException {
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        while (rs.next()) {
            doc.add(new Paragraph(new String(rs.getBytes(field), "UTF-8")));
        }
    }

    /**
     * Reads the page labels from an existing PDF
     * @param src  the path to an existing PDF
     * @param dest the path to the resulting text file
     * @throws IOException
     */
    public void listPageLabels(String src, String dest) throws IOException {
        // no PDF, just a text file
        PrintStream out = new PrintStream(new FileOutputStream(dest));
        PdfReader reader = new PdfReader(src);
        // TODO No PdfPageLabels
        // String[] labels = PdfPageLabels.getPageLabels(reader);
        //for (int i = 0; i < labels.length; i++) {
        //    out.println(labels[i]);
        //}
        out.flush();
        out.close();
        reader.close();
    }

    /**
     * Manipulates the page labels at the lowest PDF level.
     * @param src  The path to the source file
     * @param dest The path to the changed PDF
     * @throws IOException
     */
    public void manipulatePageLabel(String src, String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfDictionary root = pdfDoc.getCatalog().getPdfObject();
        PdfDictionary labels = root.getAsDictionary(new PdfName("PageLabels"));
        PdfArray nums = labels.getAsArray(PdfName.Nums);
        int n;
        PdfDictionary pagelabel;
        for (int i = 0; i < nums.size(); i++) {
            n = nums.getAsNumber(i).getIntValue();
            i++;
            if (n == 5) {
                pagelabel = nums.getAsDictionary(i);
                pagelabel.remove(new PdfName("ST"));
                pagelabel.put(PdfName.P, new PdfString("Film-"));
            }
        }
        pdfDoc.close();
    }
}
