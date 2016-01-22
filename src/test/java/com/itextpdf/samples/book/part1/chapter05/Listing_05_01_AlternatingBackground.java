package com.itextpdf.samples.book.part1.chapter05;

import com.itextpdf.basics.geom.PageSize;
import com.itextpdf.basics.geom.Rectangle;
import com.itextpdf.canvas.PdfCanvas;
import com.itextpdf.core.color.Color;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.model.Document;
import com.itextpdf.model.Property;
import com.itextpdf.model.element.AreaBreak;
import com.itextpdf.model.element.Cell;
import com.itextpdf.model.element.Table;
import com.itextpdf.model.renderer.CellRenderer;
import com.itextpdf.model.renderer.TableRenderer;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_05_01_AlternatingBackground extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter05/Listing_05_01_AlternatingBackground.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_05_01_AlternatingBackground().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // create the database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        List<Date> days = PojoFactory.getDays(connection);
        int d = 1;
        for (Date day : days) {
            if (1 != d) {
                doc.add(new AreaBreak());
            }
            Table table = getTable(connection, day);
            table.setNextRenderer(new AlternatingBackgroundTableRenderer(table, new Table.RowRange(0, 50)));
            doc.add(table);
            d++;
        }
        doc.close();
        connection.close();
    }

    public Table getTable(DatabaseConnection connection, Date day) throws UnsupportedEncodingException, SQLException {
        Table table = new Table(new float[]{2, 1, 2, 5, 1});
        table.setWidthPercent(100);
        // TODO No facility to set default cell properties
        // table.getDefaultCell().setPadding(3);
        // TODO No setUseAscender(boolean) and setUseDescender(boolean)
        // table.getDefaultCell().setUseAscender(true);
        // table.getDefaultCell().setUseDescender(true);
        // table.getDefaultCell().setColspan(5);
        // table.getDefaultCell().setBackgroundColor(BaseColor.RED);
        // table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addHeaderCell(new Cell(1, 5)
                .add(day.toString())
                .setPadding(3)
                .setBackgroundColor(Color.RED)
                .setTextAlignment(Property.TextAlignment.CENTER));
        // table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        // table.getDefaultCell().setColspan(1);
        // table.getDefaultCell().setBackgroundColor(BaseColor.ORANGE);
        table.addHeaderCell(new Cell()
                .add("Location")
                .setBackgroundColor(Color.ORANGE)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addHeaderCell(new Cell()
                .add("Time")
                .setBackgroundColor(Color.ORANGE)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addHeaderCell(new Cell()
                .add("Run Length")
                .setBackgroundColor(Color.ORANGE)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addHeaderCell(new Cell()
                .add("Title")
                .setBackgroundColor(Color.ORANGE)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addHeaderCell(new Cell()
                .add("Year")
                .setBackgroundColor(Color.ORANGE)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addFooterCell(new Cell()
                .add("Location")
                .setBackgroundColor(Color.ORANGE)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addFooterCell(new Cell()
                .add("Time")
                .setBackgroundColor(Color.ORANGE)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addFooterCell(new Cell()
                .add("Run Length")
                .setBackgroundColor(Color.ORANGE)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addFooterCell(new Cell()
                .add("Title")
                .setBackgroundColor(Color.ORANGE)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addFooterCell(new Cell()
                .add("Year")
                .setBackgroundColor(Color.ORANGE)
                .setTextAlignment(Property.TextAlignment.LEFT));
        List<Screening> screenings = PojoFactory.getScreenings(connection, day);
        Movie movie;
        for (Screening screening : screenings) {
            movie = screening.getMovie();
            table.addCell(screening.getLocation());
            table.addCell(String.format("%1$tH:%1$tM", screening.getTime()));
            table.addCell(String.format("%d '", movie.getDuration()));
            table.addCell(movie.getMovieTitle());
            table.addCell(String.valueOf(movie.getYear()));
        }
        return table;
    }
}


class AlternatingBackgroundTableRenderer extends TableRenderer {
    private boolean isOdd = true;

    public AlternatingBackgroundTableRenderer(Table modelElement, Table.RowRange rowRange) {
        super(modelElement, rowRange);
    }

    public AlternatingBackgroundTableRenderer(Table modelElement) {
        super(modelElement);
    }

    @Override
    protected TableRenderer[] split(int row, boolean hasContent) {
        return super.split(row, hasContent);
    }

    @Override
    public AlternatingBackgroundTableRenderer getNextRenderer() {
        return new AlternatingBackgroundTableRenderer((Table) modelElement);
    }

    @Override
    public void draw(PdfDocument document, PdfCanvas canvas) {
        for (int i = 0; i < rows.size() && null != rows.get(i) && null != rows.get(i)[0]; i++) {
            CellRenderer[] renderers = rows.get(i);
            Rectangle rect = new Rectangle(renderers[0].getOccupiedArea().getBBox().getLeft(),
                    renderers[0].getOccupiedArea().getBBox().getBottom(),
                    renderers[renderers.length - 1].getOccupiedArea().getBBox().getRight() -
                            renderers[0].getOccupiedArea().getBBox().getLeft(),
                    renderers[0].getOccupiedArea().getBBox().getHeight());
            canvas.saveState();
            if (isOdd) {
                canvas.setFillColor(Color.WHITE);
                isOdd = false;
            } else {
                canvas.setFillColor(Color.YELLOW);
                isOdd = true;
            }
            canvas.rectangle(rect);
            canvas.fill();
            canvas.stroke();
            canvas.restoreState();
        }
        super.draw(document, canvas);
    }
}

