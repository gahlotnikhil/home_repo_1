package com.ngs.cform;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.ngs.cform.field.FormFieldGenerator;
import com.ngs.cform.field.FormFieldPair;
import com.ngs.cform.listener.OpenCamListner;
import com.ngs.cform.listener.RefreshListener;
import com.ngs.cform.listener.RemoveListener;
import com.ngs.cform.manager.ExcelDataManager;
import com.ngs.cform.manager.WordManager;
import com.ngs.cform.model.RecordModel;
import com.ngs.cform.panel.ListViewPanel;
import com.ngs.cform.resource.ResourceConfig;
import com.ngs.cform.util.GeneralUtils;

public class HotelCFormApplication extends JFrame implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton btn1, btn2, captureBtn, openCamBtn;
    private ExcelDataManager dataManager;
    private WordManager wordManager;
    private FormFieldGenerator fieldGenerator;
    private BufferedImage avatarImage;
    private Properties configProperties;
    private ResourceConfig resourceConfig;
    private Properties properties;
	
    public HotelCFormApplication() {
    	
    	loadConfigFiles();
    	
    	JPanel mainContainer = new JPanel(new FlowLayout());
    	
    	JPanel addRecordContainer = new JPanel(new FlowLayout());
    	
    	JPanel searchContainer = new JPanel(new FlowLayout());
    	
    	JPanel settingsContainer = new JPanel(new GridLayout(0, 1));
    	
    	JTabbedPane tabbedPane = new JTabbedPane();
    	
    	JScrollPane scrollPane = new JScrollPane(addRecordContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
    	tabbedPane.addTab("New Record", scrollPane );
    	tabbedPane.addTab("Search", searchContainer );
    	tabbedPane.addTab("Settings", settingsContainer );
    	
    	setSize(1050, 600);
    	setPreferredSize(new Dimension(1050, 600));
    	scrollPane.setPreferredSize(this.getPreferredSize());
    	tabbedPane.setPreferredSize(this.getPreferredSize());
    	mainContainer.setPreferredSize(this.getPreferredSize());
    	searchContainer.setPreferredSize(this.getPreferredSize());
    	
    	mainContainer.add(tabbedPane);
    	
    	add(mainContainer);
    	mainContainer.setVisible(true);
    	
    	contstructAddRecordContainer(addRecordContainer);
    	
    	constructSearchContainer(searchContainer);
    	
    	constructSettingsContainer(settingsContainer);
    	
    	setVisible(true);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Registration Form in Java");
 
	}
    
	private void constructSettingsContainer(JPanel settingsContainer) {
		// Word template
		JPanel templatePanel = new JPanel(new FlowLayout());
		JButton templateSelectBtn = new JButton("Select a template");
		
		final JTextField templateText = new JTextField();
		templateText.setEditable(false);
		templateText.setPreferredSize(new Dimension(300, 20));
		
		templateSelectBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select a word template.");
			    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			    chooser.setAcceptAllFileFilterUsed(false);
			    chooser.addChoosableFileFilter(new FileFilter() {
					@Override
					public String getDescription() {
						return "*.doc";
					}
					@Override
					public boolean accept(File file) {
						String filename = file.getName();
						return filename.endsWith(".doc") || filename.endsWith(".docx");
					}
				});
			    
			    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			    	templateText.setText(chooser.getSelectedFile().getAbsolutePath());
			    }
			}
		});
		
		JButton setToDefaultTemplateBtn = new JButton("Set as default");
		setToDefaultTemplateBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		JButton downloadDefaultTemplateBtn = new JButton("Download default");
		downloadDefaultTemplateBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		templatePanel.add(templateText);
		templatePanel.add(templateSelectBtn);
		templatePanel.add(setToDefaultTemplateBtn);
		templatePanel.add(downloadDefaultTemplateBtn);
		
		
		// XL data sheet
		JPanel xlPanel = new JPanel(new FlowLayout());
		JButton xlsSelectBtn = new JButton("Select an xl file");
		xlsSelectBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		JButton setToDefaultXlsBtn = new JButton("Set as default");
		setToDefaultXlsBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		JButton downloadDefaultXlsBtn = new JButton("Download default");
		downloadDefaultXlsBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		JTextField xlsText = new JTextField();
		xlsText.setEditable(false);
		xlsText.setPreferredSize(new Dimension(300, 20));
		
		xlPanel.add(xlsText);
		xlPanel.add(xlsSelectBtn);
		xlPanel.add(setToDefaultXlsBtn);
		xlPanel.add(downloadDefaultXlsBtn);
		
		settingsContainer.add(templatePanel);
		settingsContainer.add(xlPanel);
	}

	private void loadConfigFiles() {
		try {
			configProperties = GeneralUtils.loadProperties("../config.properties");
			
			resourceConfig = new ResourceConfig(configProperties.get("resourceXML") != null ? String.valueOf(configProperties.get("xlDataFile")) : null);
			
			properties = GeneralUtils.loadProperties("../resource.properties");
			
			File file = new File(String.valueOf(configProperties.get("xlDataFile")));
			dataManager = new ExcelDataManager(file, resourceConfig);
			wordManager = new WordManager(new File(String.valueOf(configProperties.get("wordFormatFile"))), properties);
			
			fieldGenerator = new FormFieldGenerator(resourceConfig, properties);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void constructSearchContainer(JPanel searchContainer) {
		
		ListViewPanel recordView = new ListViewPanel(resourceConfig, properties);
		
		recordView.createView(dataManager.findAll());
		
		searchContainer.add(recordView);
		
		JButton viewBtn = new JButton("View");
		viewBtn.addActionListener(new ViewListener(recordView));
		
		JButton editBtn = new JButton("Edit");
		editBtn.addActionListener(new EditListener(recordView));
		
		JButton refreshBtn = new JButton("Refresh");
		refreshBtn.addActionListener(new RefreshListener(recordView, dataManager));
		
		JButton removeBtn = new JButton("Remove");
		removeBtn.addActionListener(new RemoveListener(recordView, dataManager));
		
		JPanel btnPanel = new JPanel(new GridLayout(0,1));
		
		btnPanel.add(viewBtn);
		btnPanel.add(editBtn);
		btnPanel.add(refreshBtn);
		btnPanel.add(removeBtn);
		
		searchContainer.add(btnPanel);
	}
	
	private void addFormFields(JPanel panel, RecordModel record, boolean editMode) {
		Map<String, FormFieldPair> fields = fieldGenerator.generateFields(record, editMode);
		List<String> mandatoryList = resourceConfig.getMandatoryFieldKeys();
		for (Map.Entry<String, FormFieldPair> entry : fields.entrySet()) {
			// Label
			JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			labelPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			if (mandatoryList.contains(entry.getKey())) {
				JLabel mand = new JLabel("*");
				mand.setForeground(Color.RED);
				labelPanel.add(mand);
			}
			labelPanel.add(entry.getValue().getLabel());
			panel.add(labelPanel);
			panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			
			// Right panel
			panel.add(entry.getValue().getPanel());
		}
	}
	
	private void contstructEditRecordContainer(JPanel editRecordContainer, RecordModel record, boolean editMode) {
		JPanel leftContainer = new JPanel(new GridLayout(0,2));
		JPanel rightContainer = new JPanel(new FlowLayout());
		rightContainer.setPreferredSize(new Dimension(180, 1300));
		
        addFormFields(leftContainer, record, false);
        
        leftContainer.add(new JPanel());
        
        if (editMode) {
	        JButton savebtn = new JButton("Save");
			savebtn.setSize(30, 20);
			savebtn.addActionListener(this);
	        JPanel btnPanel = new JPanel(new FlowLayout());
	        btnPanel.add(savebtn);
	        leftContainer.add(btnPanel);
        }
        
        leftContainer.setPreferredSize(new Dimension(800, 1300));
        
		JPanel camPanel = new JPanel(new FlowLayout());
		JPanel imagePanel = new JPanel();
		byte[] imageData = dataManager.getAvatarImage(record.getId());
		JLabel imgLabel = new JLabel(new ImageIcon(imageData));
        imagePanel.add(imgLabel);
        camPanel.add(imagePanel);
        rightContainer.add(camPanel);
		
        editRecordContainer.add(leftContainer);
        editRecordContainer.add(rightContainer);
	}

	private void contstructAddRecordContainer(JPanel addRecordContainer) {
		JPanel leftContainer = new JPanel(new GridLayout(0,2));
		JPanel rightContainer = new JPanel(new FlowLayout());
		rightContainer.setPreferredSize(new Dimension(180, 1300));
		
        btn1 = new JButton("Submit");
        btn2 = new JButton("Clear");
        btn1.setSize(30, 20);
        btn1.addActionListener(this);
        btn2.addActionListener(this);
 
        addFormFields(leftContainer, null, true);
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(btn1);
        btnPanel.add(btn2);
        
        leftContainer.add(new JPanel());
        leftContainer.add(btnPanel);
        
        leftContainer.setPreferredSize(new Dimension(800, 1300));
        
        Webcam webcam = Webcam.getDefault();
        //webcam.setViewSize(WebcamResolution.QVGA.getSize());
		final WebcamPanel panel = new WebcamPanel(webcam, false);
        //panel.setMirrored(true);
        
//		panel.setBounds(600, 30, WebcamResolution.QVGA.getSize().width, WebcamResolution.QVGA.getSize().height);
		
		JPanel camPanel = new JPanel(new FlowLayout());
		camPanel.add(panel);
		rightContainer.add(camPanel);
		
		JPanel imagePanel = new JPanel();
		
		JPanel camBtnContainer = new JPanel(new GridLayout(0,2));
		
		openCamBtn = new JButton("Open");
//		openCamBtn.setBounds(700, 300, 100, 30);
		
		openCamBtn.addActionListener(new OpenCamListner(panel, imagePanel));
		
		camBtnContainer.add(openCamBtn);
		
//        imagePanel.setBounds(600, 30, WebcamResolution.QVGA.getSize().width, WebcamResolution.QVGA.getSize().height);
        JLabel imgLabel = new JLabel();
        imagePanel.add(imgLabel);
        camPanel.add(imagePanel);
		
		imagePanel.setVisible(false);
		
		captureBtn = new JButton("Capture");
//		captureBtn.setBounds(800, 300, 100, 30);
		
		captureBtn.addActionListener(new CaptureListner(webcam, panel, imagePanel));
		
		camBtnContainer.add(captureBtn);	
		
		rightContainer.add(camBtnContainer);
		
		
		addRecordContainer.add(leftContainer);
		addRecordContainer.add(rightContainer);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("Submit".equals(e.getActionCommand())) {
			
			RecordModel record = fieldGenerator.getDataModel();
			
			if (fieldGenerator.isMandatoryAvailable(record)) {
				dataManager.add(record, avatarImage);
				JOptionPane.showMessageDialog(null, "Record Saved successfully!");
			} else {
				JOptionPane.showMessageDialog(null, "Please fill mandatory fields to proceed!");
			}
		} else {
			// clear
			
		}
		
		
	}
	
	public static void main(String args[])
	   {
	        new HotelCFormApplication();
	    }
	
	class CaptureListner implements ActionListener {
		
		private Webcam webcam;
		
		private WebcamPanel webcamPanel;
		
		private JPanel imagePanel;
		
		public CaptureListner(Webcam webcam, WebcamPanel panel, JPanel imagePanel) {
			this.webcam = webcam;
			this.webcamPanel = panel;
			this.imagePanel = imagePanel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
	        SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					avatarImage = webcam.getImage();
					JLabel imgLabel = new JLabel(new ImageIcon(avatarImage));
					
					webcamPanel.stop();
					
					webcamPanel.setVisible(false);
					imagePanel.setVisible(true);
					imagePanel.remove(0);
					imagePanel.add(imgLabel);
					
				}
			});
		}
		
	}
	
	class EditListener implements ActionListener {
		private ListViewPanel recordView;
		
		public EditListener(ListViewPanel recordView) {
			this.recordView = recordView;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			Vector row = recordView.getSelectedRow();;
	        Long id = (Long) row.get(0);
	        RecordModel record = dataManager.find(id);
	        
	        JPanel editRecordContainer = new JPanel(new FlowLayout());
	        
	        contstructEditRecordContainer(editRecordContainer, record, true);
	        
	        JScrollPane scrollPane = new JScrollPane(editRecordContainer);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	        scrollPane.setPreferredSize(new Dimension(1050, 600));
	        
//	        int result = JOptionPane.showConfirmDialog(null, editRecordContainer, "Test",
//	                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//	            if (result == JOptionPane.OK_OPTION) {
//	            	
//	            } else {
//	            	
//	            }
	        
//	        JFrame frame = new JFrame ("MyPanel");
//            frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
//            frame.getContentPane().add (editRecordContainer);
//            frame.pack();
//            frame.setVisible (true);

	        JOptionPane.showMessageDialog(null,scrollPane,"Edit record",JOptionPane.PLAIN_MESSAGE);
	        
		}
	}
	
	class ViewListener implements ActionListener {
		
		private ListViewPanel recordView;
		
		public ViewListener(ListViewPanel recordView) {
			this.recordView = recordView;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
	        Vector row = recordView.getSelectedRow();;
	        Long id = Long.parseLong(row.get(0).toString());
	        RecordModel record = dataManager.find(id);
	        
	        XWPFDocument xwpfDocument = wordManager.createWordDoc(record, dataManager.getAvatarImage(id)); 
	        
	        JFileChooser chooser = new JFileChooser();
		    chooser.setCurrentDirectory(new java.io.File("."));
		    chooser.setDialogTitle("Select a directory to download the doc.");
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    chooser.setAcceptAllFileFilterUsed(false);

		    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		      System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
		      System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
		      
		      try {
		      File selectedDir = chooser.getSelectedFile();
		      File outFile = new File(selectedDir.getPath() + "/" + "record_" +id + ".docx");
		      FileOutputStream os = new FileOutputStream(outFile);
		      xwpfDocument.write(os);
		      
		      Desktop desktop = Desktop.getDesktop();
		      desktop.open(outFile);
		      
		      try {
					os.close();
				} catch (Exception excp) {
					excp.printStackTrace();
				}
		      } catch(Exception excp) {
		    	  excp.printStackTrace();
		      }
		    } else {
		      System.out.println("No Selection ");
		    }
		}
	}

	
}

