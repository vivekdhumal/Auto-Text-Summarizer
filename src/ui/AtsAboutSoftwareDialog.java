/**
 * AtsAboutSoftwareDialog is extending JDialog to make custom dialog box
 * to display information about software and its copyright details
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class AtsAboutSoftwareDialog extends JDialog {
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public AtsAboutSoftwareDialog() {
		setTitle("About Software");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			panel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));
			contentPanel.add(panel, BorderLayout.NORTH);
			{
				JLabel lblAutoTextSummarizer = new JLabel("Auto Text Summarizer");
				lblAutoTextSummarizer.setForeground(new Color(255, 140, 0));
				lblAutoTextSummarizer.setFont(new Font("Tahoma", Font.BOLD, 16));
				panel.add(lblAutoTextSummarizer);
			}
			{
				JLabel lblNewLabel = new JLabel("BETA");
				panel.add(lblNewLabel);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			{
				JLabel lblautoTextSummarization = new JLabel("<html>\r\n<body style=\"text-align:justify;\">\r\n<p><center>Copyright &copy; 2015 by Team Auto Text Summarization,<br>All rights reserved.</center></p>\r\n<p></p>\r\n<p><strong>Auto text summarization</strong> tools works with pdf, txt,doc files as input  and extracts relevant information. The tool is based on single document extraction method. It finds the most \"important\" words and then ranks the \"importance\" of sentences according to the co-occurrences of these \"important\" words. The summary of the document then becomes the \"top n\" sentences in chronological order.</p>\r\n</body>\r\n</html>");
				lblautoTextSummarization.setHorizontalAlignment(SwingConstants.CENTER);
				lblautoTextSummarization.setFont(new Font("Tahoma", Font.PLAIN, 13));
				lblautoTextSummarization.setVerticalAlignment(SwingConstants.TOP);
				lblautoTextSummarization.setPreferredSize(new Dimension(410, 170));
				panel.add(lblautoTextSummarization);
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
