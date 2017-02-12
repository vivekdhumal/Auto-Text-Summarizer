/**
 * AtsDashboard class main UI Design and actions related to controls
 * 
 * @author Team ATS
 * @version 1.0
 */
package ui;

import helpers.FileHandler;
import helpers.FilePrintHandler;
import helpers.SummaryHelper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;

import org.apache.pdfbox.util.ExtensionFileFilter;

import net.miginfocom.swing.MigLayout;

public class AtsDashboard {

	private JFrame frmAutoTextSummarizer;
	private JPanel panelWrapper, panelTop, panelLeft, panelRight, statusPanel;
	private JLabel lblFileInfo, lblSummaryLength, lblSummaryReport, lblWords,
	lblStatus;
	private JSlider summaryLenghtSlider;
	private JTextArea outputTextArea;
	private JScrollPane jScroll;
	private JToolBar toolBar;
	private JButton btnBrowse, btnMyContent, btnSummaryReport, btnSave,
	btnPrint, btnSend, btnCopy, btnClearAll;
	private JMenuBar menuBar;
	private JMenu mnFile, mnEdit, mnHelp;
	private JMenuItem mntmSummarizeFile, mntmSummarizeContent,
	mntmSummaryReport, mntmCopy, mntmExit, mntmSave, mntmClearAll,
	mntmAbout, mntmSend, mntmSelectAll, mntmInvertSelection, mntmPrint;
	private String summaryInput;
	private JList<String> lstImpWords;
	private DefaultListModel<String> listModel;
	private SummaryHelper summaryHelper;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AtsDashboard window = new AtsDashboard();
					window.frmAutoTextSummarizer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AtsDashboard() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * and add actions event to controls
	 */
	private void initialize() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ======================== Main Window
		// =================================
		frmAutoTextSummarizer = new JFrame();
		summaryHelper = new SummaryHelper();
		new JDesktopPane();
		frmAutoTextSummarizer.setTitle("Auto Text Summarizer");
		frmAutoTextSummarizer.setExtendedState(Frame.MAXIMIZED_BOTH);
		frmAutoTextSummarizer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAutoTextSummarizer.setSize(800, 700);
		frmAutoTextSummarizer.getContentPane().setLayout(new BorderLayout());
		// ========================================================================

		// ========================== UI Panels And Controls
		// ==========================================
		panelWrapper = new JPanel();
		panelWrapper.setLayout(new BorderLayout(10, 0));
		panelWrapper.setBackground(Color.WHITE);
		panelWrapper.setBorder(new EmptyBorder(0, 10, 0, 10));
		panelTop = new JPanel();
		panelTop.setBackground(Color.WHITE);
		panelLeft = new JPanel();
		panelRight = new JPanel();

		lblFileInfo = new JLabel("No Document");
		lblFileInfo.setFont(new Font("Arial", Font.BOLD, 20));
		lblSummaryLength = new JLabel("Summary length : ( 10 % )");
		lblSummaryReport = new JLabel("Important Words (Top 20)");
		lblWords = new JLabel("Text");
		JPanel panelSummaryLength = new JPanel();
		panelSummaryLength.setBackground(Color.WHITE);
		lblFileInfo.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/document.png")));

		// ========================== JSlider =================================
		summaryLenghtSlider = new JSlider(JSlider.HORIZONTAL, 5, 75, 25);
		summaryLenghtSlider.setMinorTickSpacing(2);
		summaryLenghtSlider.setMajorTickSpacing(5);
		summaryLenghtSlider.setPaintTicks(true);
		summaryLenghtSlider.setPaintLabels(true);
		summaryLenghtSlider.setBackground(Color.WHITE);
		summaryLenghtSlider.setLabelTable(summaryLenghtSlider
				.createStandardLabels(10));
		summaryLenghtSlider.setValue(10);
		summaryLenghtSlider.setEnabled(false);
		// ================================================================================

		outputTextArea = new JTextArea();
		outputTextArea.setEditable(false);
		outputTextArea.setWrapStyleWord(true);
		outputTextArea.setLineWrap(true);
		outputTextArea.setMargin(new Insets(10, 10, 10, 10));
		outputTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
		outputTextArea.setFocusable(true);
		jScroll = new JScrollPane(outputTextArea);
		panelRight.setLayout(new BorderLayout());
		panelRight.add(jScroll, BorderLayout.CENTER);
		// panelRight.setBorder(new MatteBorder(1,1,1,1,Color.LIGHT_GRAY));

		// lblSummaryReport.setBackground(Color.ORANGE);
		lblSummaryReport.setForeground(Color.WHITE);
		lblSummaryReport.setFont(new Font("Arial", Font.BOLD, 14));
		lblWords.setFont(new Font("Arial", Font.BOLD, 12));
		lblSummaryReport.setBorder(new MatteBorder(0, 0, 1, 0, Color.WHITE));
		lblSummaryReport.setSize(200, 30);
		listModel = new DefaultListModel<String>();
		lstImpWords = new JList<String>(listModel);
		lstImpWords.setBackground(Color.DARK_GRAY);
		lstImpWords.setForeground(Color.ORANGE);
		lstImpWords.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelLeft.setLayout(new MigLayout("", "[]", "[][][]"));
		panelLeft.add(lblSummaryReport, "cell 0 0");
		panelLeft.add(lstImpWords, "cell 0 1,grow");
		// panelLeft.add(lblWords);
		panelLeft.setPreferredSize(new Dimension(200, panelLeft.getHeight()));
		panelLeft.setBackground(Color.DARK_GRAY);
		panelLeft.setForeground(Color.WHITE);

		panelTop.setLayout(new BorderLayout());
		panelTop.add(lblFileInfo, BorderLayout.WEST);
		panelSummaryLength.add(lblSummaryLength);
		panelSummaryLength.add(summaryLenghtSlider);
		panelTop.add(panelSummaryLength, BorderLayout.EAST);
		panelWrapper.add(panelTop, BorderLayout.NORTH);
		panelWrapper.add(panelLeft, BorderLayout.WEST);
		panelWrapper.add(panelRight, BorderLayout.CENTER);
		// =============================================================================

		// ================== MenuBar ==================================
		menuBar = new JMenuBar();

		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmSummarizeFile = new JMenuItem("Summarize File");
		mntmSummarizeFile.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/browse.png")));
		mnFile.add(mntmSummarizeFile);

		mntmSummarizeContent = new JMenuItem("Summarize My Content");
		mntmSummarizeContent.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/document_pencil.png")));
		mnFile.add(mntmSummarizeContent);

		mntmSummaryReport = new JMenuItem("Summary Report");
		mntmSummaryReport.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/report.png")));
		mnFile.add(mntmSummaryReport);
		mnFile.addSeparator();

		mntmSave = new JMenuItem("Save Summary");
		mntmSave.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/save.png")));
		mnFile.add(mntmSave);

		mntmSend = new JMenuItem("Send Summary By Email");
		mntmSend.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/send.png")));
		mnFile.add(mntmSend);
		mnFile.addSeparator();

		mntmPrint = new JMenuItem("Print Summary");
		mntmPrint.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/printer.png")));
		mnFile.add(mntmPrint);
		mnFile.addSeparator();

		mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		mntmCopy = new JMenuItem("Copy");
		mntmCopy.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/copy.png")));
		mnEdit.add(mntmCopy);

		mntmClearAll = new JMenuItem("Clear All");
		mntmClearAll.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/delete.png")));
		mnEdit.add(mntmClearAll);
		mnEdit.addSeparator();

		mntmSelectAll = new JMenuItem("Select All");
		mnEdit.add(mntmSelectAll);

		mntmInvertSelection = new JMenuItem("Invert Selection");
		mnEdit.add(mntmInvertSelection);

		mnHelp = new JMenu("Help");

		mntmAbout = new JMenuItem("About ATS");
		mnHelp.add(mntmAbout);

		mnFile.setMnemonic('F');
		mnEdit.setMnemonic('E');
		mnHelp.setMnemonic('H');
		mntmSummarizeFile.setMnemonic('z');
		mntmSummarizeContent.setMnemonic('c');
		mntmSave.setMnemonic('S');
		mntmSend.setMnemonic('E');
		mntmPrint.setMnemonic('P');
		mntmExit.setMnemonic('x');
		mntmCopy.setMnemonic('C');
		mntmSelectAll.setMnemonic('A');

		mntmSave.setEnabled(false);
		mntmSend.setEnabled(false);
		mntmPrint.setEnabled(false);
		mntmCopy.setEnabled(false);
		mntmClearAll.setEnabled(false);
		mntmSelectAll.setEnabled(false);
		mntmInvertSelection.setEnabled(false);
		mntmSummaryReport.setEnabled(false);

		mntmSummarizeFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmSummarizeContent.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_M, Toolkit.getDefaultToolkit()
				.getMenuShortcutKeyMask()));
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmSend.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		menuBar.add(mnHelp);
		// =====================================================================================================

		// ========================== ToolBar
		// ====================================================================
		toolBar = new JToolBar();
		btnBrowse = new JButton("Summarize File");
		btnBrowse.setToolTipText("Summarize File (Ctrl+O)");
		btnBrowse.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/browse.png")));
		btnBrowse.setVerticalTextPosition(SwingConstants.CENTER);
		btnBrowse.setHorizontalTextPosition(SwingConstants.RIGHT);
		toolBar.add(btnBrowse);
		btnMyContent = new JButton("Summarize My Content");
		btnMyContent.setToolTipText("Summarize My Content (Ctrl+M)");
		btnMyContent.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/document_pencil.png")));
		btnMyContent.setVerticalTextPosition(SwingConstants.CENTER);
		btnMyContent.setHorizontalTextPosition(SwingConstants.RIGHT);
		toolBar.add(btnMyContent);
		btnSummaryReport = new JButton("Summary Report");
		btnSummaryReport.setToolTipText("Summary Report");
		btnSummaryReport.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/report.png")));
		btnSummaryReport.setVerticalTextPosition(SwingConstants.CENTER);
		btnSummaryReport.setHorizontalTextPosition(SwingConstants.RIGHT);
		toolBar.add(btnSummaryReport);
		toolBar.addSeparator();
		btnSave = new JButton("Save");
		btnSave.setToolTipText("Save (Ctrl+S)");
		btnSave.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/save.png")));
		btnSave.setVerticalTextPosition(SwingConstants.CENTER);
		btnSave.setHorizontalTextPosition(SwingConstants.RIGHT);
		toolBar.add(btnSave);
		btnPrint = new JButton("Print");
		btnPrint.setToolTipText("Print (Ctrl+P)");
		btnPrint.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/printer.png")));
		btnPrint.setVerticalTextPosition(SwingConstants.CENTER);
		btnPrint.setHorizontalTextPosition(SwingConstants.RIGHT);
		toolBar.add(btnPrint);
		btnSend = new JButton("Send");
		btnSend.setToolTipText("Send (Ctrl+E)");
		btnSend.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/send.png")));
		btnSend.setVerticalTextPosition(SwingConstants.CENTER);
		btnSend.setHorizontalTextPosition(SwingConstants.RIGHT);
		toolBar.add(btnSend);
		toolBar.addSeparator();
		btnCopy = new JButton("Copy");
		btnCopy.setToolTipText("Copy (Ctrl+C)");
		btnCopy.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/copy.png")));
		btnCopy.setVerticalTextPosition(SwingConstants.CENTER);
		btnCopy.setHorizontalTextPosition(SwingConstants.RIGHT);
		toolBar.add(btnCopy);
		btnClearAll = new JButton("Clear all");
		btnClearAll.setToolTipText("Clear all");
		btnClearAll.setIcon(new ImageIcon(AtsDashboard.class
				.getResource("/resources/images/delete.png")));
		btnClearAll.setVerticalTextPosition(SwingConstants.CENTER);
		btnClearAll.setHorizontalTextPosition(SwingConstants.RIGHT);
		toolBar.add(btnClearAll);

		btnSave.setEnabled(false);
		btnCopy.setEnabled(false);
		btnPrint.setEnabled(false);
		btnSend.setEnabled(false);
		btnClearAll.setEnabled(false);
		btnSummaryReport.setEnabled(false);
		// ==========================================================================================================

		// =========================== Status Panel
		// =================================================================
		statusPanel = new JPanel();
		statusPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
		frmAutoTextSummarizer.getContentPane().add(statusPanel,
				BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(frmAutoTextSummarizer
				.getWidth(), 25));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		lblStatus = new JLabel("Ready");
		lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(lblStatus);
		// ===========================================================================================================

		// ========================= Add All To Main Frame
		// ===========================================================
		frmAutoTextSummarizer.setJMenuBar(menuBar);
		frmAutoTextSummarizer.getContentPane().add(toolBar, BorderLayout.NORTH);
		frmAutoTextSummarizer.getContentPane().add(panelWrapper,
				BorderLayout.CENTER);
		// ===========================================================================================================

		// ============================ All action events
		// ============================================================
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				summarizeFiles();
			}
		});
		btnMyContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				summarizeMyContent();
			}
		});
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSummary();
			}
		});
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// new SendEmail(outputTextArea.getText());
				sendSummary();
			}
		});
		btnClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAll();
			}
		});

		btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyContent();
			}
		});

		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printSummary();
			}
		});

		btnSummaryReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewReport();
			}
		});

		mntmSummarizeFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				summarizeFiles();
			}
		});

		mntmSummaryReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewReport();
			}
		});

		mntmSummarizeContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				summarizeMyContent();
			}
		});

		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSummary();
			}
		});

		mntmSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendSummary();
			}
		});

		mntmSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputTextArea.requestFocusInWindow();
				outputTextArea.selectAll();
			}
		});

		mntmInvertSelection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputTextArea.replaceSelection(outputTextArea.getText());
			}
		});

		mntmClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAll();
			}
		});

		mntmCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyContent();
			}
		});
		mntmPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printSummary();
			}
		});

		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AtsAboutSoftwareDialog about = new AtsAboutSoftwareDialog();
				about.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				about.setLocationRelativeTo(frmAutoTextSummarizer);
				about.setModal(true);
				about.setVisible(true);
			}
		});

		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		lstImpWords.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()
						&& lstImpWords.getSelectedValue() != null) {
					String word = lstImpWords.getSelectedValue().toString();
					try {
						highlightText(word);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		summaryLenghtSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (!summaryLenghtSlider.getValueIsAdjusting()) {
					int sliderValue = summaryLenghtSlider.getValue();
					lblSummaryLength.setText("Summary length : ( "
							+ sliderValue + " % )");
					if (summaryInput.length() > 0) {
						summaryHelper.setCompressionRate(sliderValue);
						String summary = "Summary :-";
						summary += getSummary(summaryInput);
						outputTextArea.setText(summary);
					}
				}

			}
		});

		// ==============================================================================================================
	}

	/**
	 * This method will return summary of provided content
	 * @param content String
	 * @return String summary
	 */
	public String getSummary(String content) {
		List<String> sortsublistKeyword;
		Map<Integer, Double> scoresenteces = summaryHelper
				.prepareSentenceScore(content);
		ArrayList<Integer> sentenceSort = summaryHelper
				.sortSentences(scoresenteces);
		StringBuilder summary = summaryHelper.makeSummary(
				summaryHelper.getSentences(content), sentenceSort);
		List<String> sortKeyword = summaryHelper.findImportantwords(content);
		if (sortKeyword.size() >= 20)
			sortsublistKeyword = sortKeyword.subList(0, 20);
		else
			sortsublistKeyword = sortKeyword;
		System.out
		.println("sorted Keyword size = " + sortsublistKeyword.size());
		System.out.println("sorted Keyword = " + sortsublistKeyword);
		listModel.clear();
		for (String keyWord : sortsublistKeyword) {
			listModel.addElement(keyWord.substring(0, 1).toUpperCase()
					+ keyWord.substring(1));
		}
		return summary.toString();
	}

	/**
	 * This method will perform summarize file operation
	 */
	public void summarizeFiles() {
		final FileHandler fileParser = new FileHandler(); // init FileParser
		// class
		final JFileChooser fc = new JFileChooser(); // open file dialog
		fc.setFileFilter(new FileNameExtensionFilter("Document Files", "doc",
				"docx", "pdf", "txt"));
		int returnVal = fc.showOpenDialog(frmAutoTextSummarizer);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile(); // get file reference in file
			// object
			String fileName = file.toString(); // get file path
			lblStatus.setText("File Path: " + fileName);
			lblFileInfo.setText(file.getName());
			if (file.canRead() && file.exists()) {
				String rawText = fileParser.getRawText(file);
				if (rawText != null) {
					summaryInput = rawText;
					String summary = "Summary :-";
					summary += getSummary(rawText);
					outputTextArea.setText(summary);
					btnSave.setEnabled(true);
					btnCopy.setEnabled(true);
					btnPrint.setEnabled(true);
					btnSend.setEnabled(true);
					btnClearAll.setEnabled(true);
					btnSummaryReport.setEnabled(true);
					mntmSummaryReport.setEnabled(true);

					mntmSave.setEnabled(true);
					mntmSend.setEnabled(true);
					mntmPrint.setEnabled(true);
					mntmCopy.setEnabled(true);
					mntmClearAll.setEnabled(true);
					mntmSelectAll.setEnabled(true);
					mntmInvertSelection.setEnabled(true);

					summaryLenghtSlider.setValue(10);
					lblSummaryLength.setText("Summary length : ( 10 % )");
					summaryLenghtSlider.setEnabled(true);
				} else {
					lblFileInfo.setText("No Document");
					lblStatus.setText("File Path: Empty");
					JOptionPane
					.showMessageDialog(
							frmAutoTextSummarizer,
							"Only .txt, .pdf, .doc, docx file format supported",
							"Alert", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane
				.showMessageDialog(frmAutoTextSummarizer,
						"File may not be exists or do not have a read permission");
			}
		}
	}

	/**
	 * This method will perform summarize operation on content provided by user
	 */
	public void summarizeMyContent() {
		JTextArea myTextArea = new JTextArea(18, 60);
		JScrollPane scrollPane = new JScrollPane(myTextArea);
		myTextArea.setLineWrap(true);

		int selection = JOptionPane.showConfirmDialog(frmAutoTextSummarizer,
				scrollPane, "Paste your content here",
				JOptionPane.OK_CANCEL_OPTION);
		if (selection == JOptionPane.OK_OPTION) {
			if (myTextArea.getText().length() > 0) {

				summaryInput = myTextArea.getText();
				String summary = "Summary :-";
				summary += getSummary(myTextArea.getText());
				outputTextArea.setText(summary);
				btnSave.setEnabled(true);
				btnCopy.setEnabled(true);
				btnPrint.setEnabled(true);
				btnSend.setEnabled(true);
				btnClearAll.setEnabled(true);
				btnSummaryReport.setEnabled(true);
				mntmSummaryReport.setEnabled(true);

				mntmSave.setEnabled(true);
				mntmSend.setEnabled(true);
				mntmPrint.setEnabled(true);
				mntmCopy.setEnabled(true);
				mntmClearAll.setEnabled(true);
				mntmSelectAll.setEnabled(true);
				mntmInvertSelection.setEnabled(true);

				summaryLenghtSlider.setValue(10);
				lblSummaryLength.setText("Summary length : ( 10 % )");
				summaryLenghtSlider.setEnabled(true);
			} else {
				JOptionPane.showMessageDialog(null, "Please provide content to make summary","Alert", JOptionPane.ERROR_MESSAGE);
				summarizeMyContent();
			}
		}
	}	


	/**
	 * This method will perform save summary operation
	 */
	public void saveSummary() {
		final FileHandler fileParser = new FileHandler(); // init FileParser
		// class
		String text = outputTextArea.getText();
		JFileChooser fileChooser = new JFileChooser(".");
		int rVal = fileChooser.showSaveDialog(frmAutoTextSummarizer);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			try {
				File fileToSave = fileChooser.getSelectedFile();
				if (!fileToSave.exists()) {
					fileParser.saveFile(fileToSave, text, true);
				} else {
					int dialogResult = JOptionPane
							.showConfirmDialog(
									frmAutoTextSummarizer,
									"File is already exists, Do you want to replace the content?",
									"Warning", JOptionPane.YES_NO_OPTION);
					if (dialogResult == JOptionPane.YES_OPTION) {
						fileParser.saveFile(fileToSave, text, false);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			JOptionPane.showMessageDialog(frmAutoTextSummarizer,
					"File has been saved "
							+ fileChooser.getSelectedFile().getName());
			System.out.println("directory : "
					+ fileChooser.getCurrentDirectory().toString());
		}
	}


	/**
	 * This method will display report dialog box with details of summary report
	 */

	public void viewReport() {
		int oSentences = summaryHelper.getSentences(summaryInput).size();
		int sSentences = summaryHelper.getSentences(outputTextArea.getText())
				.size();
		int compression = summaryLenghtSlider.getValue();
		List<String> sortKeyword = summaryHelper
				.findImportantwords(summaryInput);
		if (sortKeyword.size() >= 50)
			sortKeyword = sortKeyword.subList(0, 50);
		String words = sortKeyword.toString().replace("[", "").replace("]", "");
		AtsReportDialog.setSentencesLength(oSentences, sSentences);
		AtsReportDialog.setCompression(compression);
		AtsReportDialog.setImpWords(words);
		AtsReportDialog reportDialog = new AtsReportDialog(
				frmAutoTextSummarizer);
		reportDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		reportDialog.setLocationRelativeTo(frmAutoTextSummarizer);
		reportDialog.setModal(true);
		reportDialog.setVisible(true);
	}

	/**
	 * This method will clear all output area and will reset menubar and toolbar to its original state
	 */
	public void clearAll() {
		btnSave.setEnabled(false);
		btnCopy.setEnabled(false);
		btnPrint.setEnabled(false);
		btnSend.setEnabled(false);
		btnClearAll.setEnabled(false);
		outputTextArea.setText("");
		lblFileInfo.setText("No Document");
		lblStatus.setText("Ready");
		btnSummaryReport.setEnabled(false);
		mntmSummaryReport.setEnabled(false);

		mntmSave.setEnabled(false);
		mntmSend.setEnabled(false);
		mntmPrint.setEnabled(false);
		mntmCopy.setEnabled(false);
		mntmClearAll.setEnabled(false);
		mntmSelectAll.setEnabled(false);
		mntmInvertSelection.setEnabled(false);

		listModel.removeAllElements();
		summaryInput = "";
		lblSummaryLength.setText("Summary length : ( 10 % )");
		summaryLenghtSlider.setValue(10);
		summaryLenghtSlider.setEnabled(false);
	}

	/**
	 * This method will perform text copy operation
	 */
	public void copyContent() {
		String selectedContent;
		selectedContent = outputTextArea.getSelectedText();
		if (selectedContent == null)
			selectedContent = outputTextArea.getText();
		StringSelection sourceContent = new StringSelection(selectedContent);
		Toolkit.getDefaultToolkit().getSystemClipboard()
		.setContents(sourceContent, sourceContent);
		lblStatus.setText("Coppied To Clipboard");
	}

	/**
	 * This method will summary printing operation
	 */
	public void printSummary() {
		String summeryResult = outputTextArea.getText();
		FilePrintHandler fp = new FilePrintHandler();
		fp.printPDF(summeryResult);
	}


	/**
	 * This method will display dialog box which sends summary through email
	 */
	public void sendSummary() {
		//check Internet connection
		Socket socket = new Socket();
		InetSocketAddress address = new InetSocketAddress("www.google.com", 80);
		try {
			//Internet available
			socket.connect(address, 3000);
			socket.close();
			AtsSendEmailDialog sendEmailDialog = new AtsSendEmailDialog(
					frmAutoTextSummarizer, outputTextArea.getText());
			sendEmailDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			sendEmailDialog.setLocationRelativeTo(frmAutoTextSummarizer);
			sendEmailDialog.setModal(true);
			sendEmailDialog.setVisible(true);
		} catch (Exception e) {
			//Internet not available
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Please check your internet connection", "Alert",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * This method will highlight the words in the output text area
	 * 
	 * @param pattern String to highlight
	 */
	public void highlightText(String pattern) throws BadLocationException {
		removeHighlights();
		Highlighter highlighter = outputTextArea.getHighlighter();
		Document doc = outputTextArea.getDocument();
		String text = doc.getText(0, doc.getLength());
		int pos = 0;
		while ((pos = text.toUpperCase().indexOf(pattern.toUpperCase(), pos)) >= 0) {
			// Create highlighted using private painter and apply around pattern
			highlighter.addHighlight(pos, pos + pattern.length(),
					myHighlightPainter);
			pos += pattern.length();
		}
	}

	/**
	 * This method will remove all highlight from the words in the output text area	 * 
	 */
	public void removeHighlights() {
		Highlighter hilite = outputTextArea.getHighlighter();
		Highlighter.Highlight[] hilites = hilite.getHighlights();
		for (int i = 0; i < hilites.length; i++) {
			if (hilites[i].getPainter() instanceof MyHighlightPainter) {
				hilite.removeHighlight(hilites[i]);
			}
		}
	}

	// An instance of the private subclass of the default highlight painter
	Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(
			Color.YELLOW);

	// A private subclass of the default highlight painter
	class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
		public MyHighlightPainter(Color color) {
			super(color);
		}
	}
}
