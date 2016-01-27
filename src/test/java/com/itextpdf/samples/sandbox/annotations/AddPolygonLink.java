/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/27083206/itextshape-clickable-polygon-or-path
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.basics.geom.Rectangle;
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
import com.itextpdf.model.Document;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class AddPolygonLink extends GenericTest {
    public static final String SRC = "./src/test/resources/sandbox/annotations/hello.pdf";
    public static final String DEST = "./target/test/resources/sandbox/annotations/add_polygon_link.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddPolygonLink().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(new FileInputStream(SRC)),
                new PdfWriter(new FileOutputStream(DEST)));
        Document doc = new Document(pdfDoc);
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        canvas.moveTo(36, 700);
        canvas.lineTo(72, 760);
        canvas.lineTo(144, 720);
        canvas.lineTo(72, 730);
        canvas.closePathStroke();
        Rectangle linkLocation = new Rectangle(36, 700, 144, 760);
        PdfArray arrayOfQuadPoints = new PdfArray(new int[]{72, 730, 144, 720, 72, 760, 36, 700});
        PdfArray array = new PdfArray();
        array.add(doc.getPdfDocument().getPage(1).getPdfObject());
        array.add(PdfName.Fit);
        PdfDestination destination = PdfDestination.makeDestination(array);
        PdfLinkAnnotation linkAnnotation = new PdfLinkAnnotation(linkLocation)
                .setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT)
                .setAction(PdfAction.createGoTo(destination));
        linkAnnotation.put(PdfName.QuadPoints, arrayOfQuadPoints);
        pdfDoc.getFirstPage().addAnnotation(linkAnnotation);
        doc.close();
    }
}
