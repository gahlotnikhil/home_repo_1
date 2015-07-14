package com.ngs.cform.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.ngs.cform.field.FormFieldPair;
import com.ngs.cform.listener.DownloadListener;
import com.ngs.cform.listener.RecordGetter;
import com.ngs.cform.model.RecordModel;
import com.ngs.cform.util.ConfigSession;

public class RecordViewPanel extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConfigSession configSession;
    private CameraPanel cameraPanel;
	
	public RecordViewPanel() {
		this.configSession = ConfigSession.getConfigSession();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if ("Submit".equals(e.getActionCommand())) {
			
			RecordModel record = configSession.getFieldGenerator().getDataModel();
			
			if (configSession.getFieldGenerator().isMandatoryAvailable(record)) {
				configSession.getDataManager().add(record, cameraPanel.getCapturedImage());
				JOptionPane.showMessageDialog(null, "Record Saved successfully!");
				
				// clear fields
				clearForm();
			} else {
				JOptionPane.showMessageDialog(null, "Please fill mandatory fields to proceed!");
			}
		} if ("Save".equals(e.getActionCommand())) {
			RecordModel record = configSession.getFieldGenerator().getDataModel();
			
			if (configSession.getFieldGenerator().isMandatoryAvailable(record)) {
				configSession.getDataManager().update(record, cameraPanel.getCapturedImage());
				JOptionPane.showMessageDialog(null, "Record Saved successfully!");
			} else {
				JOptionPane.showMessageDialog(null, "Please fill mandatory fields to proceed!");
			}
			
		} if ("Clear".equals(e.getActionCommand()))  {
			clearForm();
		}
	}
	
	private void clearForm() {
		// clear
		configSession.getFieldGenerator().clearFields();
		cameraPanel.resetWebcam();
	}
	
	private void addFormFields(JPanel panel, RecordModel record, boolean editMode) {
		Map<String, FormFieldPair> fields = configSession.getFieldGenerator().generateFields(record, editMode);
		List<String> mandatoryList = configSession.getResourceConfig().getMandatoryFieldKeys();
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
			
			labelPanel.setSize(new Dimension(200, 300));
			entry.getValue().getPanel().setSize(new Dimension(200, 300));
			
			panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			
			// Right panel
			panel.add(entry.getValue().getPanel());
		}
	}

	public void contstructEditRecordContainer(JPanel editRecordContainer, final RecordModel record, boolean editMode) {
		JPanel leftContainer = new JPanel(new GridLayout(0,2));
		JPanel rightContainer = new JPanel(new FlowLayout());
		rightContainer.setPreferredSize(new Dimension(180, 1300));
		
        addFormFields(leftContainer, record, editMode);
        
        leftContainer.add(new JPanel());
        
        if (editMode) {
	        JButton savebtn = new JButton("Save");
			savebtn.setSize(30, 20);
			savebtn.addActionListener(this);
	        JPanel btnPanel = new JPanel(new FlowLayout());
	        btnPanel.add(savebtn);
	        leftContainer.add(btnPanel);
        } else {
        	JButton downloadBtn = new JButton("Download");
    		downloadBtn.addActionListener(new DownloadListener(new RecordGetter() {

    			@Override
    			public RecordModel getRecord() {
    		        return record;
    			}
    		}));
    		
    		JPanel btnPanel = new JPanel(new FlowLayout());
	        btnPanel.add(downloadBtn);
	        leftContainer.add(btnPanel);
        }
        
        leftContainer.setPreferredSize(new Dimension(800, 1300));
        
		JPanel camPanel = new JPanel(new FlowLayout());
		JPanel imagePanel = new JPanel();
		byte[] imageData = configSession.getDataManager().getAvatarImage(record.getId());
		JLabel imgLabel = new JLabel(new ImageIcon(imageData));
        imagePanel.add(imgLabel);
        camPanel.add(imagePanel);
        rightContainer.add(camPanel);
		
        editRecordContainer.add(leftContainer);
        editRecordContainer.add(rightContainer);
	}
	
	public void contstructAddRecordContainer(JPanel addRecordContainer) {
		JPanel leftContainer = new JPanel(new GridLayout(0,2));
		
		JButton btn1 = new JButton("Submit");
		JButton btn2 = new JButton("Clear");
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
        
		addRecordContainer.add(leftContainer);
		// Camera panel
		cameraPanel = new CameraPanel();
		addRecordContainer.add(cameraPanel);
	}
}
