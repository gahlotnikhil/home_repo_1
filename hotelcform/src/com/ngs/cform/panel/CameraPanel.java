package com.ngs.cform.panel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

public class CameraPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private WebcamPanel webcamPanel;
	
	private JPanel imagePanel;
	
	private Webcam webcam;
	
	private BufferedImage capturedImage;
	
	private void setCapturedImage(BufferedImage capturedImage) {
		this.capturedImage = capturedImage;
	}

	public CameraPanel() {
		super(new FlowLayout());
		this.setPreferredSize(new Dimension(180, 1300));
		
		webcam = Webcam.getDefault();
		webcamPanel = new WebcamPanel(webcam, false);
		// webcamPanel.setMirrored(true);

		JPanel camPanel = new JPanel(new FlowLayout());
		camPanel.add(webcamPanel);

		imagePanel = new JPanel();

		JPanel camBtnContainer = new JPanel(new GridLayout(0, 2));

		JButton openCamBtn = new JButton("Open");

		openCamBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				openWebcam();
			}
		});

		camBtnContainer.add(openCamBtn);

		JLabel imgLabel = new JLabel();
		imagePanel.add(imgLabel);
		camPanel.add(imagePanel);

		imagePanel.setVisible(false);

		JButton captureBtn = new JButton("Capture");

		captureBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
		        SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						captureWebcamImage();
						
					}
				});
			}
		});

		camBtnContainer.add(captureBtn);

		this.add(camPanel);
		this.add(camBtnContainer);
	}
	
	public void captureWebcamImage() {
		setCapturedImage(webcam.getImage());
		JLabel imgLabel = new JLabel(new ImageIcon(capturedImage));
		
		webcamPanel.setVisible(false);
		imagePanel.setVisible(true);
		imagePanel.remove(0);
		imagePanel.add(imgLabel);
		
		stopWebcam();
	}
	
	public BufferedImage getCapturedImage() {
		return capturedImage;
	}
	
	public void openWebcam() {
		webcamPanel.setVisible(true);
		imagePanel.setVisible(false);
		
		startWebcam();
	}
	
	public void startWebcam() {
		webcamPanel.start();
	}
	
	public void stopWebcam() {
		webcamPanel.stop();
	}
	
	public void resetWebcam() {
		webcamPanel.setVisible(true);
		imagePanel.setVisible(false);
	}

}
