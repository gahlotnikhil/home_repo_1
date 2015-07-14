package com.ngs.cform;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;

import com.ngs.cform.listener.DownloadListener;
import com.ngs.cform.listener.EditListener;
import com.ngs.cform.listener.RecordGetter;
import com.ngs.cform.listener.RefreshListener;
import com.ngs.cform.listener.RemoveListener;
import com.ngs.cform.listener.RowSelectionListener;
import com.ngs.cform.model.RecordModel;
import com.ngs.cform.panel.ListViewPanel;
import com.ngs.cform.panel.RecordViewPanel;
import com.ngs.cform.util.ConfigSession;

public class HotelCFormApplication extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ConfigSession configSession;
	
	private  Logger logger = Logger.getLogger(HotelCFormApplication.class);
	
	private static ProgressBar progressBar = new ProgressBar();
	
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
    	//tabbedPane.addTab("Settings", settingsContainer );
    	
    	setSize(1050, 600);
    	setPreferredSize(new Dimension(1050, 600));
    	scrollPane.setPreferredSize(this.getPreferredSize());
    	tabbedPane.setPreferredSize(this.getPreferredSize());
    	mainContainer.setPreferredSize(this.getPreferredSize());
    	searchContainer.setPreferredSize(this.getPreferredSize());
    	
    	mainContainer.add(tabbedPane);
    	
    	progressBar.addProgress(5);
    	
    	add(mainContainer);
    	mainContainer.setVisible(true);
    	
    	RecordViewPanel recordViewPanel = new RecordViewPanel();
    	recordViewPanel.contstructAddRecordContainer(addRecordContainer);
    	
    	progressBar.addProgress(5);
    	
    	constructSearchContainer(searchContainer);
    	
    	constructSettingsContainer(settingsContainer);
    	
    	progressBar.addProgress(10);
    	
    	setVisible(true);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("C Form Application");
 
        progressBar.completeProgress();
	}
    
    private void loadConfigFiles() {
		configSession = ConfigSession.loadSession(progressBar);
		logger.info("Configuration initialized...");
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

	private void constructSearchContainer(JPanel searchContainer) {
		final JButton viewBtn = new JButton("View");
		final JButton downloadBtn = new JButton("Download");
		final JButton editBtn = new JButton("Edit");
		final JButton refreshBtn = new JButton("Refresh");
		final JButton removeBtn = new JButton("Remove");
		viewBtn.setEnabled(false);
		downloadBtn.setEnabled(false);
		editBtn.setEnabled(false);
		removeBtn.setEnabled(false);
		
		RowSelectionListener rowSelectionListener = new RowSelectionListener() {
			
			@Override
			public void onRowSelection(Vector row) {
				viewBtn.setEnabled(row != null);
				downloadBtn.setEnabled(row != null);
				editBtn.setEnabled(row != null);
				removeBtn.setEnabled(row != null);
			}
		};
		
		final ListViewPanel recordView = new ListViewPanel(rowSelectionListener);
		
		recordView.createView(configSession.getDataManager().findAll());
		
		searchContainer.add(recordView);
		
		
		viewBtn.addActionListener(new EditListener(new RecordGetter() {

			@Override
			public RecordModel getRecord() {
				Vector row = recordView.getSelectedRow();;
		        Long id = Long.parseLong(row.get(0).toString());
		        return configSession.getDataManager().find(id);
			}
		}, false));
		
		downloadBtn.addActionListener(new DownloadListener(new RecordGetter() {

			@Override
			public RecordModel getRecord() {
				Vector row = recordView.getSelectedRow();;
		        Long id = Long.parseLong(row.get(0).toString());
		        return configSession.getDataManager().find(id);
			}
		}));
		
		editBtn.addActionListener(new EditListener(new RecordGetter() {

			@Override
			public RecordModel getRecord() {
				Vector row = recordView.getSelectedRow();;
		        Long id = Long.parseLong(row.get(0).toString());
		        return configSession.getDataManager().find(id);
			}
		}, true));
		
		refreshBtn.addActionListener(new RefreshListener(recordView));
		
		removeBtn.addActionListener(new RemoveListener(recordView));
		
		JPanel btnPanel = new JPanel(new GridLayout(0,1));
		
		btnPanel.add(viewBtn);
		btnPanel.add(downloadBtn);
		btnPanel.add(editBtn);
		btnPanel.add(refreshBtn);
		btnPanel.add(removeBtn);
		
		searchContainer.add(btnPanel);
	}
	
	public static void main(String args[])
	   {
	        new HotelCFormApplication();
	    }
}

