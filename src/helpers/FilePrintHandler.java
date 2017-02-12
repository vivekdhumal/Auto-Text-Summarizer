/**
 * FilePrintHandler class performs file printing job
 *  
 * @author Team ATS
 * @version 1.0
 */
package helpers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import com.itextpdf.awt.geom.Point2D;

public class FilePrintHandler implements Printable {
	static String summery = null;
	static AttributedString summerizedData = null;
	
	/**
	 * This method will add printing job to online printer for the summary
	 *  
	 * @param String summaryResult
	 */
	public void printPDF(final String summeryResult) {
		summery = summeryResult;
		System.out.println(summery);
		/** Create an AttributedString object from the text read */
		summerizedData = new AttributedString(summery);

		/**
		 * Get a Printer Job
		 */
		PrinterJob printerJob = PrinterJob.getPrinterJob();

		/**
		 * Create a book. A book contains a pair of page painters called
		 * printables. Also you have different pageformats.
		 */
		Book book = new Book();
		/**
		 * Append the Printable Object (this one itself, as it implements a
		 * printable interface) and the page format.
		 */
		book.append(new FilePrintHandler(), new PageFormat());
		/**
		 * Set the object to be printed (the Book) into the PrinterJob. Doing
		 * this before bringing up the print dialog allows the print dialog to
		 * correctly display the page range to be printed and to dissallow any
		 * print settings not appropriate for the pages to be printed.
		 */
		printerJob.setPageable(book);

		/**
		 * Calling the printDialog will pop up the Printing Dialog. If you want
		 * to print without user confirmation, you can directly call
		 * printerJob.print()
		 *
		 * doPrint will be false, if the user cancels the print operation.
		 */
		boolean doPrint = printerJob.printDialog();

		if (doPrint) {
			try {
				printerJob.print();
			} catch (PrinterException ex) {
				System.err.println("Error occurred while trying to Print: "
						+ ex);
			}
		}

	}

	/**
	 * This method comes from the Printable interface. The method implementation
	 * in this class prints a page of text.
	 */
	public int print(Graphics g, PageFormat format, int pageIndex) {

		Graphics2D graphics2d = (Graphics2D) g;
		/**
		 * Move the origin from the corner of the Paper to the corner of the
		 * imageable area.
		 */
		graphics2d.translate(format.getImageableX(), format.getImageableY());

		/** Setting the text color **/
		graphics2d.setPaint(Color.black);
		/**
		 * Use a LineBreakMeasurer instance to break our text into lines that
		 * fit the imageable area of the page.
		 */
		Point2D.Float pen = new Point2D.Float();
		AttributedCharacterIterator charIterator = summerizedData.getIterator();
		LineBreakMeasurer measurer = new LineBreakMeasurer(charIterator,
				graphics2d.getFontRenderContext());
		float wrappingWidth = (float) format.getImageableWidth();

		while (measurer.getPosition() < charIterator.getEndIndex()) {
			TextLayout layout = measurer.nextLayout(wrappingWidth);
			pen.y += layout.getAscent();
			float dx = layout.isLeftToRight() ? 0 : (wrappingWidth - layout
					.getAdvance());
			layout.draw(graphics2d, pen.x + dx, pen.y);
			pen.y += layout.getDescent() + layout.getLeading();
		}
		return Printable.PAGE_EXISTS;
	}

}
