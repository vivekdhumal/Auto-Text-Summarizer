/**
 * AtsSendEmailDialog is extending JDialog to make custom input dialog box
 * to take inputs from user and send email
 * 
 * @author Team ATS
 * @version 1.0
 */
package ui;

import helpers.SendEmail;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

public class AtsSendEmailDialog extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JTextField txtToEmaiAddress;
	private JTextArea txtMessage;
	private JTextField txtSubject;

	/**
	 * Create the dialog.
	 * 
	 * @param parent JFrame
	 * @param summaryResult String
	 */
	public AtsSendEmailDialog(JFrame parent, String summaryResult) {
		super(parent);
		setTitle("Send Email");
		setBounds(100, 100, 500, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][][grow]", "[][][grow]"));
		{
			JLabel lblRecipientAddress = new JLabel("Recipient Email Address*");
			contentPanel.add(lblRecipientAddress, "cell 0 0");
		}
		{
			txtToEmaiAddress = new JTextField();
			contentPanel.add(txtToEmaiAddress, "cell 2 0,growx");
			txtToEmaiAddress.setColumns(10);
		}
		{
			JLabel lblSubject = new JLabel("Subject*");
			contentPanel.add(lblSubject, "cell 0 1");
		}
		{
			txtSubject = new JTextField();
			contentPanel.add(txtSubject, "cell 2 1,growx");
			txtSubject.setColumns(10);
		}
		{
			JLabel lblMessage = new JLabel("Message*");
			lblMessage.setVerticalAlignment(SwingConstants.TOP);
			contentPanel.add(lblMessage, "cell 0 2,aligny top");
		}
		{
			txtMessage = new JTextArea();
			txtMessage.setWrapStyleWord(true);
			txtMessage.setLineWrap(true);
			//contentPanel.add(textArea, "cell 2 1,grow");
		}
		{
			JScrollPane scrollPane = new JScrollPane(txtMessage);
			contentPanel.add(scrollPane, "cell 2 2,grow");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnSend = new JButton("Send");
				btnSend.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
						if(txtToEmaiAddress.getText().length() == 0) {
							JOptionPane.showMessageDialog(null, "Please enter recipient address","Alert", JOptionPane.ERROR_MESSAGE);
							return;
						}
						if(!txtToEmaiAddress.getText().matches(EMAIL_REGEX)) {
							JOptionPane.showMessageDialog(null, "Please enter proper email address","Alert", JOptionPane.ERROR_MESSAGE);
							return;
						}
						if(txtSubject.getText().length() == 0) {
							JOptionPane.showMessageDialog(null, "Please enter subject","Alert", JOptionPane.ERROR_MESSAGE);
							return;
						}
						if(txtMessage.getText().length() == 0) {
							JOptionPane.showMessageDialog(null, "Please enter Message","Alert", JOptionPane.ERROR_MESSAGE);
							return;
						}
						SendEmail email = new SendEmail();
						boolean isMailSent = email.sendEmail(summaryResult, txtToEmaiAddress.getText(), txtMessage.getText(), txtSubject.getText());
						if(isMailSent) {
							JOptionPane.showMessageDialog(null, "Email sent successfully","Message", JOptionPane.PLAIN_MESSAGE);							
						} else {
							JOptionPane.showMessageDialog(null, "Email not sent, please try again","Alert", JOptionPane.ERROR_MESSAGE);							
						}
					}
				});
				btnSend.setActionCommand("Send");
				buttonPane.add(btnSend);
				getRootPane().setDefaultButton(btnSend);
			}
			{
				JButton btnCancel = new JButton("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				btnCancel.setActionCommand("Cancel");
				buttonPane.add(btnCancel);
			}
		}
	}

}
