package com.ngs.cform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressBar {

	private static final int MINIMUM = 0;

	private static final int MAXIMUM = 100;

	private JProgressBar pbar;
	
	private JFrame frame;

	public ProgressBar() {
		JPanel progressPanel = new JPanel(new BorderLayout());
		pbar = new JProgressBar();
		pbar.setMinimum(MINIMUM);
		pbar.setMaximum(MAXIMUM);
		progressPanel.add(pbar, BorderLayout.CENTER);

		frame = new JFrame("Loading... Please wait...");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(progressPanel);
		frame.pack();
		frame.setVisible(true);
		//frame.setBounds(400, 300, 500, 100);
		pbar.setValue(5);
		pbar.setStringPainted(true);
		
		frame.setSize(500, 100);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
	}

	public void addProgress(final int value) {
		updateProgress(pbar.getValue() + value);
	}
	
	public void updateProgress(final int value) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pbar.setValue(value);
				
				if (MAXIMUM <= value) {
					frame.dispose();
				}
			}
		});
	}
	
	public void completeProgress() {
		updateProgress(100);
	}
}
