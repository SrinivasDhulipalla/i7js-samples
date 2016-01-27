/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/26983703/itext-how-to-stamp-image-on-existing-pdf-and-create-an-anchor
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.basics.geom.Rectangle;
import com.itextpdf.basics.image.Image;
import com.itextpdf.basics.image.ImageFactory;
import com.itextpdf.core.pdf.PdfArray;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfName;
import com.itextpdf.core.pdf.PdfReader;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.core.pdf.action.PdfAction;
import com.itextpdf.core.pdf.annot.PdfAnnotation;
import com.itextpdf.core.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.core.pdf.canvas.PdfCanvas;
import com.itextpdf.core.pdf.navigation.PdfDestination;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class AddImageLink extends GenericTest {
    public static final String SRC = "./src/test/resources/sandbox/annotations/primes.pdf";
    public static final String IMG = "./src/test/resources/sandbox/annotations/info.png";
    public static final String DEST = "./target/test/resources/sandbox/annotations/add_image_link.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddImageLink().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(new FileInputStream(SRC)),
                new PdfWriter(new FileOutputStream(DEST)));

        Image img = ImageFactory.getImage(IMG);
        float x = 10;
        float y = 650;
        float w = img.getWidth();
        float h = img.getHeight();
        new PdfCanvas(pdfDoc.getPage(1).newContentStreamAfter(), pdfDoc.getPage(1).getResources(), pdfDoc)
                .addImage(img, x, y, false);
        Rectangle linkLocation = new Rectangle(x, y, w, h);

        PdfArray array = new PdfArray();
        array.add(pdfDoc.getPage(pdfDoc.getNumberOfPages()).getPdfObject());
        array.add(PdfName.Fit);
        PdfDestination destination = PdfDestination.makeDestination(array);
        PdfAnnotation annotation = new PdfLinkAnnotation(linkLocation)
                .setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT)
                .setAction(PdfAction.createGoTo(destination)).setBorder(new PdfArray(new float[]{0, 0, 0}));
        pdfDoc.getPage(1).addAnnotation(annotation);

        pdfDoc.close();
    }
}
