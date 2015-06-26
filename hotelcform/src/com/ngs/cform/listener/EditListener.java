package com.ngs.cform.listener;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.ngs.cform.model.RecordModel;
import com.ngs.cform.panel.RecordViewPanel;

public class EditListener implements ActionListener {

	private RecordGetter recordGetter;
	private boolean editMode;
	
	public EditListener(RecordGetter recordGetter, boolean editMode) {
		this.recordGetter = recordGetter;
		this.editMode = editMode;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		RecordModel record = recordGetter.getRecord();
        
        JPanel editRecordContainer = new JPanel(new FlowLayout());
        
        RecordViewPanel recordViewPanel = new RecordViewPanel();
    	recordViewPanel.contstructEditRecordContainer(editRecordContainer, record, editMode);
        
        JScrollPane scrollPane = new JScrollPane(editRecordContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1050, 600));
        
//        int result = JOptionPane.showConfirmDialog(null, editRecordContainer, "Test",
//                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//            if (result == JOptionPane.OK_OPTION) {
//            	
//            } else {
//            	
//            }
        
//        JFrame frame = new JFrame ("MyPanel");
//        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add (editRecordContainer);
//        frame.pack();
//        frame.setVisible (true);

        JOptionPane.showMessageDialog(null,scrollPane,"Edit record",JOptionPane.PLAIN_MESSAGE);
        
	}

}
