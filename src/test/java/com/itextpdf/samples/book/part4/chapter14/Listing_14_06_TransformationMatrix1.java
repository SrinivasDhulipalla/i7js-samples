package com.itextpdf.samples.book.part4.chapter14;

import com.itextpdf.basics.geom.PageSize;
import com.itextpdf.basics.geom.Rectangle;
import com.itextpdf.canvas.PdfCanvas;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfPage;
import com.itextpdf.core.pdf.PdfReader;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.core.pdf.xobject.PdfFormXObject;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.samples.GenericTest;

import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_14_06_TransformationMatrix1 extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part4/chapter14/Listing_14_06_TransformationMatrix1.pdf";
    public static final String RESOURCE = "./src/test/resources/book/part4/chapter14/logo.pdf";

    public static void main(String args[]) throws IOException {
        new Listing_14_06_TransformationMatrix1().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.setDefaultPageSize(new PageSize(new Rectangle(-595, -842, 595*2, 842*2)));

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        canvas.moveTo(-595, 0);
        canvas.lineTo(595, 0);
        canvas.moveTo(0, -842);
        canvas.lineTo(0, 842);
        canvas.stroke();
        // Read the PDF containing the logo
        PdfDocument srcDoc = new PdfDocument(new PdfReader(RESOURCE));
        PdfPage curPage = srcDoc.getPage(1);
        PdfFormXObject xObject = curPage.copyAsFormXObject(pdfDoc);
        // add it at different positions using different transformations
        canvas.saveState();
        canvas.addXObject(xObject, 0, 0);
        canvas.concatMatrix(0.5f, 0, 0, 0.5f, -595, 0);
        canvas.addXObject(xObject, 0, 0);
        canvas.concatMatrix(1, 0, 0, 1, 595, 595);
        canvas.addXObject(xObject, 0, 0);
        canvas.restoreState();

        canvas.saveState();
        canvas.concatMatrix(1, 0, 0.4f, 1, -750, -650);
        canvas.addXObject(xObject, 0, 0);
        canvas.restoreState();

        canvas.saveState();
        canvas.concatMatrix(0, -1, -1, 0, 650, 0);
        canvas.addXObject(xObject, 0, 0);
        canvas.concatMatrix(0.2f, 0, 0, 0.5f, 0, 300);
        canvas.addXObject(xObject, 0, 0);
        canvas.restoreState();

        pdfDoc.close();
        srcDoc.close();
    }
}
