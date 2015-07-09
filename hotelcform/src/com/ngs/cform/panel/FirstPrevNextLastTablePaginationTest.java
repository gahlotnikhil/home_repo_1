package com.ngs.cform.panel;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

public class FirstPrevNextLastTablePaginationTest {
	
	private  Logger logger = Logger.getLogger(FirstPrevNextLastTablePaginationTest.class);
	
	private final String[] columnNames = { "Year", "String", "Comment" };
	
	private final DefaultTableModel model = new DefaultTableModel(null,
			columnNames) {
		@Override
		public Class<?> getColumnClass(int column) {
			return (column == 0) ? Integer.class : Object.class;
		}
	};
	
	private final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
			model);
	
	private final JTable table = new JTable(model);
	
	private final JButton first = new JButton(new AbstractAction("|<") {
		@Override
		public void actionPerformed(ActionEvent e) {
			currentPageIndex = 1;
			initFilterAndButton();
		}
	});
	
	private final JButton prev = new JButton(new AbstractAction("<") {
		@Override
		public void actionPerformed(ActionEvent e) {
			currentPageIndex -= 1;
			initFilterAndButton();
		}
	});
	
	private final JButton next = new JButton(new AbstractAction(">") {
		@Override
		public void actionPerformed(ActionEvent e) {
			currentPageIndex += 1;
			initFilterAndButton();
		}
	});
	
	private final JButton last = new JButton(new AbstractAction(">|") {
		@Override
		public void actionPerformed(ActionEvent e) {
			currentPageIndex = maxPageIndex;
			initFilterAndButton();
		}
	});
	
	private final JTextField field = new JTextField(2);
	
	private final JLabel label = new JLabel();

	public JComponent makeUI() {
		table.setFillsViewportHeight(true);
		table.setRowSorter(sorter);
		for (int i = 0; i <= 2013; i++) {
			model.addRow(new Object[] { i, "Test: " + i,
					(i % 2 == 0) ? "" : "comment..." });
		}

		JPanel po = new JPanel();
		po.add(field);
		po.add(label);
		JPanel box = new JPanel(new GridLayout(1, 4, 2, 2));
		for (JComponent r : Arrays.asList(first, prev, po, next, last)) {
			box.add(r);
		}

		int rowCount = model.getRowCount();
		int v = rowCount % itemsPerPage == 0 ? 0 : 1;
		maxPageIndex = rowCount / itemsPerPage + v;
		initFilterAndButton();
		label.setText(String.format("/ %d", maxPageIndex));
		KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		field.getInputMap(JComponent.WHEN_FOCUSED).put(enter, "Enter");
		field.getActionMap().put("Enter", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int v = Integer.parseInt(field.getText());
					if (v > 0 && v <= maxPageIndex) {
						currentPageIndex = v;
					}
				} catch (Exception ex) {
					logger.error("Error occurred while performing navigation.", ex);
				}
				initFilterAndButton();
			}
		});

		JPanel p = new JPanel(new BorderLayout());
		p.add(box, BorderLayout.NORTH);
		p.add(new JScrollPane(table));
		return p;
	}

	private final int itemsPerPage = 100;
	private int maxPageIndex;
	private int currentPageIndex = 1;

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

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static void createAndShowGUI() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.getContentPane().add(
				new FirstPrevNextLastTablePaginationTest().makeUI());
		f.setSize(320, 240);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}
