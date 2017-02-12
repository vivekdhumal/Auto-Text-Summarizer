/**
 * AtsReportDialog is extending JDialog to make custom dialog box
 * to display summary report
 * 
 * @author Team ATS
 * @version 1.0
 */
package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import net.miginfocom.swing.MigLayout;

public class AtsReportDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private static int originalSenteces=0, summarySentences=0, compressionRate = 0;

	private static String words = "";

	/**
	 * getter and setter methods
	 * 
	 */
	public static void setSentencesLength(int originalSentecesCount, int summarySentencesCount) {
		originalSenteces = originalSentecesCount;
		summarySentences = summarySentencesCount;
	}
	
	public static void setImpWords(String impWords) {
		words = impWords;
	}
	
	public static void setCompression(int compression){
		compressionRate = compression;
	}

	public int getCompression() {
		return compressionRate;
	}

	public String getImpWords() {
		return words;
	}

	public int getOriginalSentenceCount() {
		return originalSenteces;
	}

	public int getSummarySentenceCount() {
		return summarySentences;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parent JFrame
	 */
	public AtsReportDialog(JFrame parent) {
		super(parent);
		setTitle("Summary Report");
		setBounds(100, 100, 479, 480);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			panel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
			contentPanel.add(panel, BorderLayout.NORTH);
			{
				JLabel label = new JLabel("Auto Text Summarizer");
				label.setForeground(new Color(255, 140, 0));
				label.setFont(new Font("Tahoma", Font.BOLD, 16));
				panel.add(label);
			}
			{
				JLabel label = new JLabel("BETA");
				panel.add(label);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBackground(Color.WHITE);
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new MigLayout("", "[][][]", "[][][][][][][]"));
			{
				JLabel lblLength = new JLabel("Sentences");
				lblLength.setFont(new Font("Tahoma", Font.BOLD, 13));
				panel.add(lblLength, "cell 0 0");
			}
			{
				int originalSentencesCount = getOriginalSentenceCount(); 
				JLabel lblOriginal = new JLabel("Original : "+originalSentencesCount);
				lblOriginal.setFont(new Font("Tahoma", Font.PLAIN, 13));
				panel.add(lblOriginal, "cell 0 1");
			}
			{
				int summarySentencesCount = getSummarySentenceCount(); 
				JLabel lblSummary = new JLabel("Summary : "+summarySentencesCount);
				lblSummary.setFont(new Font("Tahoma", Font.PLAIN, 13));
				panel.add(lblSummary, "cell 2 1");
			}
			{
				JLabel lblImportantWords = new JLabel("Important Words (Top 50)");
				lblImportantWords.setFont(new Font("Tahoma", Font.BOLD, 13));
				panel.add(lblImportantWords, "cell 0 3");
			}
			{
				String impWords = getImpWords();
				JLabel lblWords = new JLabel("<html><p style='text-align:justify;'>"+impWords+"</p></html>");
				lblWords.setFont(new Font("Tahoma", Font.PLAIN, 13));
				lblWords.setPreferredSize(new Dimension(410,  50));
				lblWords.setVerticalAlignment(SwingConstants.TOP);
				panel.add(lblWords, "cell 0 4 3 1");
			}
			{
				int percentageCompression = getCompression();
				JLabel lblTotalReduction = new JLabel("Total Reduction : "+percentageCompression+"%");
				lblTotalReduction.setFont(new Font("Tahoma", Font.BOLD, 13));
				panel.add(lblTotalReduction, "cell 0 6");
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
