package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.basics.geom.Rectangle;
import com.itextpdf.core.pdf.PdfArray;
import com.itextpdf.core.pdf.PdfDictionary;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfName;
import com.itextpdf.core.pdf.PdfNumber;
import com.itextpdf.core.pdf.PdfOutputStream;
import com.itextpdf.core.pdf.PdfStream;
import com.itextpdf.core.pdf.PdfString;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.model.Document;
import com.itextpdf.samples.GenericTest;

import java.io.FileInputStream;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

@Ignore
@Category(SampleTest.class)
public class Listing_16_16_Pdf3D extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part4/chapter16/Listing_16_16_Pdf3D.pdf";
    public static String RESOURCE = "./src/test/resources/book/part4/chapter16/teapot.u3d";

    public static void main(String args[]) throws Exception {
        new Listing_16_16_Pdf3D().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Rectangle rect = new Rectangle(100, 400, 500, 800);
//        rect.setBorder(Rectangle.BOX);
//        rect.setBorderWidth(0.5f);
//        rect.setBorderColor(new BaseColor(0xFF, 0x00, 0x00));
//        doc.add(rect);

        PdfStream stream3D = new PdfStream(pdfDoc, new FileInputStream(RESOURCE));
        stream3D.put(PdfName.Type, new PdfName("3D"));
        stream3D.put(PdfName.Subtype, new PdfName("U3D"));
        // TODO No flateCompress
        // stream3D.flateCompress();
        stream3D.setCompressionLevel(PdfOutputStream.DEFAULT_COMPRESSION);
        // TODO No addToBody
        // PdfIndirectObject streamObject = pdfDoc.addToBody(stream3D);
        // TODO No writeLength
        //stream3D.writeLength();

        PdfDictionary dict3D = new PdfDictionary();
        dict3D.put(PdfName.Type, new PdfName("3DView"));
        dict3D.put(new PdfName("XN"), new PdfString("Default"));
        dict3D.put(new PdfName("IN"), new PdfString("Unnamed"));
        dict3D.put(new PdfName("MS"), PdfName.M);
        dict3D.put(new PdfName("C2W"),
                new PdfArray(new float[]{1, 0, 0, 0, 0, -1, 0, 1, 0, 3, -235, 28}));
        dict3D.put(PdfName.CO, new PdfNumber(235));

        // TODO NO addToBody
        // PdfIndirectObject dictObject = writer.addToBody(dict3D);

        //PdfAnnotation annot = new PdfAnnotation(pdfDoc, rect);
        // annot.put(PdfName.Contents, new PdfString("3D Model"));
        // annot.put(PdfName.Subtype, new PdfName("3D"));
        // annot.put(PdfName.Type, PdfName.Annot);
//        annot.put(new PdfName("3DD"), streamObject.getIndirectReference());
//        annot.put(new PdfName("3DV"), dictObject.getIndirectReference());
//        PdfAppearance ap = writer.getDirectContent().createAppearance(rect.getWidth(), rect.getHeight());
//        annot.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, ap);
//        annot.setPage();

//        pdfDoc.addNewPage().addAnnotation(annot);
        doc.close();
    }
}
