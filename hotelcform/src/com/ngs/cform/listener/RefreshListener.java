package com.ngs.cform.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.ngs.cform.manager.ExcelDataManager;
import com.ngs.cform.panel.ListViewPanel;
import com.ngs.cform.util.ConfigSession;

public class RefreshListener implements ActionListener {

	private ListViewPanel recordView;

	private ExcelDataManager dataManager;

	public RefreshListener(ListViewPanel recordView) {
		this.recordView = recordView;
		this.dataManager = ConfigSession.getConfigSession().getDataManager();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		recordView.refreshTable(dataManager.findAll());
	}

}
