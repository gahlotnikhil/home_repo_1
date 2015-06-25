package com.ngs.cform.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.ngs.cform.manager.ExcelDataManager;
import com.ngs.cform.panel.ListViewPanel;

public class RemoveListener implements ActionListener {

	private ListViewPanel recordView;

	private ExcelDataManager dataManager;

	public RemoveListener(ListViewPanel recordView, ExcelDataManager dataManager) {
		this.recordView = recordView;
		this.dataManager = dataManager;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		int ok = JOptionPane.showConfirmDialog(null,
				"Are you sure that you want to delete the record?");

		if (JOptionPane.YES_OPTION == ok) {
			Vector row = recordView.getSelectedRow();
			;
			Long id = (Long) row.get(0);
			dataManager.remove(id);

			recordView.refreshTable(dataManager.findAll());
		}
	}

}
