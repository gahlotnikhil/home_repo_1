package com.ngs.cform.listener;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.ngs.cform.manager.ExcelDataManager;
import com.ngs.cform.manager.WordManager;
import com.ngs.cform.model.RecordModel;
import com.ngs.cform.util.ConfigSession;

public class DownloadListener implements ActionListener {

	private ExcelDataManager dataManager;
	private WordManager wordManager;
	private RecordGetter recordGetter;
	
	public DownloadListener(RecordGetter recordGetter) {
		ConfigSession configSession = ConfigSession.getConfigSession();
		this.dataManager = configSession.getDataManager();
		this.wordManager = configSession.getWordManager();
		this.recordGetter = recordGetter;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		RecordModel record = recordGetter.getRecord();
        
        XWPFDocument xwpfDocument = wordManager.createWordDoc(record, dataManager.getAvatarImage(record.getId())); 
        
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
	      File outFile = new File(selectedDir.getPath() + "/" + "record_" +record.getId() + ".docx");
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
