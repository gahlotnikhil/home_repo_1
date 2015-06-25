package com.ngs.cform.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.ngs.cform.model.RecordModel;
import com.ngs.cform.resource.ResourceConfig;

public class ListViewPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable table;

	private DefaultTableModel model;

	private final int itemsPerPage = 30;
	private int maxPageIndex;
	private int currentPageIndex = 1;

	private final JTextField field = new JTextField(2);
	private final JLabel label = new JLabel();
	
	private ResourceConfig resourceConfig;
	private Properties properties;

	public ListViewPanel(ResourceConfig resourceConfig, Properties properties) {
		this.resourceConfig = resourceConfig;
		this.properties = properties;
	}

	private TableRowSorter<TableModel> sorter;

	private final JButton first = new JButton(new AbstractAction("|<") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			currentPageIndex = 1;
			initFilterAndButton();
		}
	});

	private final JButton prev = new JButton(new AbstractAction("<") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			currentPageIndex -= 1;
			initFilterAndButton();
		}
	});

	private final JButton next = new JButton(new AbstractAction(">") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			currentPageIndex += 1;
			initFilterAndButton();
		}
	});

	private final JButton last = new JButton(new AbstractAction(">|") {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			currentPageIndex = maxPageIndex;
			initFilterAndButton();
		}
	});

	public void createView(List<RecordModel> data) {
		table = new JTable() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		refreshTable(data);

		this.add(constructUI());
	}

	private JComponent constructUI() {
		table.setFillsViewportHeight(true);

		JPanel po = new JPanel();
		po.add(field);
		po.add(label);
		JPanel box = new JPanel(new GridLayout(1, 4, 2, 2));
		for (JComponent r : Arrays.asList(first, prev, po, next, last)) {
			box.add(r);
			r.setPreferredSize(new Dimension(30, 30));
		}

		label.setText(String.format("/ %d", maxPageIndex));
		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		field.getInputMap(JComponent.WHEN_FOCUSED).put(enter, "Enter");
		field.getActionMap().put("Enter", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int v = Integer.parseInt(field.getText());
					if (v > 0 && v <= maxPageIndex) {
						currentPageIndex = v;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				initFilterAndButton();
			}
		});

		JPanel p = new JPanel(new BorderLayout());
		p.add(box, BorderLayout.NORTH);
		JScrollPane scrollPane  =new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(850, 500));
		p.add(scrollPane);
		return p;
	}

	private void initFilterAndButton() {
		sorter.setRowFilter(new RowFilter<TableModel, Integer>() {
			@Override
			public boolean include(
					Entry<? extends TableModel, ? extends Integer> entry) {
				int ti = currentPageIndex - 1;
				int ei = entry.getIdentifier();
				return ti * itemsPerPage <= ei
						&& ei < ti * itemsPerPage + itemsPerPage;
			}
		});
		first.setEnabled(currentPageIndex > 1);
		prev.setEnabled(currentPageIndex > 1);
		next.setEnabled(currentPageIndex < maxPageIndex);
		last.setEnabled(currentPageIndex < maxPageIndex);
		field.setText(Integer.toString(currentPageIndex));
	}

	private DefaultTableModel contructTableModel(List<RecordModel> data) {
		Vector<Vector<Object>> dataList = new Vector<Vector<Object>>();
		for (RecordModel record : data) {
			Vector<Object> rowList = new Vector<Object>();
			for (String key : resourceConfig.getSearchFieldKeys()) {
				Object val = record.getValueByKey(key);
				rowList.add(val);
			}

			dataList.add(rowList);
		}
		
		Vector<String> columnNames = new Vector<String>();
		
		for (String key : resourceConfig.getSearchFieldLabelKeys()) {
			columnNames.add(properties.getProperty(key, key));
		}

		return new DefaultTableModel(dataList, columnNames);
	}

	public void refreshTable(List<RecordModel> data) {
		model = contructTableModel(data);
		table.setModel(model);
		sorter = new TableRowSorter<TableModel>(
				model);
		table.setRowSorter(sorter);
		//table.getColumnModel().getColumn(0).setPreferredWidth(760);
		
		int rowCount = model.getRowCount();
		int v = rowCount % itemsPerPage == 0 ? 0 : 1;
		maxPageIndex = rowCount / itemsPerPage + v;
		initFilterAndButton();
	}

	@SuppressWarnings("rawtypes")
	public Vector getSelectedRow() {
		int index = table.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Vector row = (Vector) model.getDataVector().get((currentPageIndex - 1) * itemsPerPage + index);
		return row;
	}
}
