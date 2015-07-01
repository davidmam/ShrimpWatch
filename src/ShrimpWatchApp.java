import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.Color;

import javax.swing.JPanel;

import java.awt.GridLayout;
import java.io.File;


public class ShrimpWatchApp {

	private JFrame frame;
	private JButton btnStop;
	private JButton btnRun;
	private JButton btnConfigure;
	private DataLogger datalogger; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShrimpWatchApp window = new ShrimpWatchApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ShrimpWatchApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		datalogger= new DataLogger();
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("Shrimp Watcher");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 36));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblNewLabel, BorderLayout.NORTH);
		
		btnStop = new JButton("Stop");
		btnStop.setEnabled(false);
		btnStop.addActionListener(new StopButtonListener());
		btnStop.setForeground(Color.RED);
		btnStop.setFont(new Font("Lucida Grande", Font.BOLD, 22));
		btnStop.addActionListener(new StopButtonListener());
		frame.getContentPane().add(btnStop, BorderLayout.WEST);
		
		btnRun = new JButton("Run");
		btnRun.setEnabled(false);
		btnRun.addActionListener(new RunButtonListener());
		btnRun.setForeground(new Color(51, 204, 0));
		btnRun.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		frame.getContentPane().add(btnRun, BorderLayout.EAST);
		
		btnConfigure = new JButton("Configure");
		btnConfigure.addActionListener(new ConfigureButtonListener());
		frame.getContentPane().add(btnConfigure, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblFilenameLabel = new JLabel("Filename");
		panel.add(lblFilenameLabel);
		
		JLabel lblFilename = new JLabel("not selected");
		panel.add(lblFilename);
		
		JLabel lblRecordsLabel = new JLabel("Readings");
		panel.add(lblRecordsLabel);
		
		JLabel lblReadings = new JLabel("0");
		panel.add(lblReadings);
		
		JLabel lblPort = new JLabel("Port");
		panel.add(lblPort);
		
		JLabel lblNone = new JLabel("None");
		panel.add(lblNone);
	}

	private void setFile(File file){
		this.datalogger.setOutputFile(file);
	}
	// Inner classes
	
	private class ConfigureButtonListener implements ActionListener{
		private File selectedFile;
		private int returnVal;
        public void actionPerformed(ActionEvent e){
        	if (e.getSource()==btnConfigure){
        		final JFileChooser fc =new JFileChooser();
        		fc.addActionListener(this);
        		returnVal=fc.showOpenDialog(ShrimpWatchApp.this.frame);
        		if (returnVal == JFileChooser.APPROVE_OPTION) {
        	          selectedFile = fc.getSelectedFile();
        	          ShrimpWatchApp.this.setFile(selectedFile);
        	          btnRun.setEnabled(true);
        		}
        	}
        }
    }
	
	private class RunButtonListener implements ActionListener{
        
		public void actionPerformed(ActionEvent e) {
			ShrimpWatchApp.this.datalogger.startLogging();
			if (ShrimpWatchApp.this.datalogger.isLogging()){
				ShrimpWatchApp.this.btnRun.setEnabled(false);
				ShrimpWatchApp.this.btnStop.setEnabled(true);
				ShrimpWatchApp.this.btnConfigure.setEnabled(false);
			}
		}
        
    }
	private class StopButtonListener implements ActionListener{
        	public void actionPerformed(ActionEvent e) {
    			ShrimpWatchApp.this.datalogger.stopLogging();
    			if (!ShrimpWatchApp.this.datalogger.isLogging()){
    				ShrimpWatchApp.this.btnRun.setEnabled(true);
    				ShrimpWatchApp.this.btnStop.setEnabled(false);
    				ShrimpWatchApp.this.btnConfigure.setEnabled(true);
    			}
    		
        }
    }
	
}
