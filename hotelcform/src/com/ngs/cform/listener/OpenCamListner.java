package com.ngs.cform.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.sarxos.webcam.WebcamPanel;

public class OpenCamListner implements ActionListener {

	private WebcamPanel webcamPanel;
	private JPanel imagePanel;

	public OpenCamListner(WebcamPanel panel, JPanel imagePanel) {
		this.webcamPanel = panel;
		this.imagePanel = imagePanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				webcamPanel.start();
				webcamPanel.setVisible(true);
				imagePanel.setVisible(false);
			}
		});
	}

}
