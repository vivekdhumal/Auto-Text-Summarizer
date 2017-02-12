/**
 * FileHandler class performs file related operation which includes extracting
 * text information from files and also save file to the local hard drive
 * 
 * @author Team ATS
 * @version 1.0
 */

package helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class FileHandler {
	FileReader fileReader = null;
	BufferedReader buffreader;

	/**
	 * This method is used to get only text information document it will also
	 * return null if the file extension is not supported only TXT,PDF,DOC,DOCX
	 * format are considered
	 * 
	 * @parameter file object of File Class
	 * @return String this returns text information from document
	 */
	public String getRawText(File file) {
		String extension = this.getFileExtension(file);
		System.out.println("extension: " + extension);
		try {
			if (extension.equalsIgnoreCase("txt")) {
				// extract data from txt file
				StringBuilder strBuilder = new StringBuilder();
				fileReader = new FileReader(file);
				buffreader = new BufferedReader(fileReader);
				String inputLine = buffreader.readLine();
				while (inputLine != null) {
					strBuilder.append(inputLine + "\n");
					inputLine = buffreader.readLine();
				}
				buffreader.close();
				fileReader.close();
				return strBuilder.toString();
			} else if (extension.equalsIgnoreCase("docx")) {
				// extract data from docx file
				XWPFDocument wordDoc = new XWPFDocument(new FileInputStream(
						file));
				XWPFWordExtractor wordExtractor = new XWPFWordExtractor(wordDoc);
				wordExtractor.close();
				return wordExtractor.getText();
			} else if (extension.equalsIgnoreCase("doc")) {
				// extract data from doc file
				HWPFDocument wordDoc = new HWPFDocument(new FileInputStream(
						file));
				WordExtractor wordExtractor = new WordExtractor(wordDoc);
				wordExtractor.close();
				return wordExtractor.getText();
			} else if (extension.equalsIgnoreCase("pdf")) {
				// extract data from pdf file
				PDDocument pdfdoc = PDDocument.load(file);
				PDFTextStripper textStriper = new PDFTextStripper();
				String pdftext = textStriper.getText(pdfdoc);
				pdfdoc.close();
				return pdftext;
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get file extension from file
	 * 
	 * @parameter file object of File Class
	 * @return String this returns extension of a file in string
	 */
	public String getFileExtension(File file) {
		String fileName = file.toString();
		String extension = "";
		int dot = fileName.lastIndexOf('.');
		extension = fileName.substring(dot + 1);
		return extension;
	}

	/**
	 * This method is used to save file to the local hard drive
	 * 
	 * @parameter file object of File Class
	 * @parameter text content to save into the file
	 * @parameter flag boolean flag for txt file to check whether file is
	 *            already exists or should create new txt file if flag is true
	 * @return int this is used to indicate whether file is successfully stored
	 *         or not, on success it will return 1 and on failure it will return 0.
	 */
	public int saveFile(File file, String text, Boolean flag) {
		String extension = this.getFileExtension(file);
		try {
			// save txt file
			if (extension.equalsIgnoreCase("txt")) {
				if (flag)
					file.createNewFile();
				BufferedWriter output = new BufferedWriter(new FileWriter(file));
				output.write(text);
				output.close();
				return 1;

			} else if (extension.equalsIgnoreCase("docx")
					|| extension.equalsIgnoreCase("doc")) {
				// save doc or docx file
				XWPFDocument document = new XWPFDocument();
				XWPFParagraph tmpParagraph = document.createParagraph();
				XWPFRun tmpRun = tmpParagraph.createRun();
				tmpRun.setText(text);
				tmpRun.setFontSize(18);
				document.write(new FileOutputStream(file));
			} else if (extension.equalsIgnoreCase("pdf")) {
				// save pdf file
				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(file));
				document.open();
				document.add(new Paragraph(text));
				document.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
