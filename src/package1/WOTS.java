package package1;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author tobydobbs
 * word on the street
 */
public class WOTS {
	
	JFrame mainframe;
	JPanel panel1, panel2;
	JLabel l1, l2, l3, l4, l5;
	JLabel[] labels = new JLabel[5];
	String message;
	int ldex = 0;

	public WOTS(String message) {
		this.message = message;
		genGUI();
	}

	public void genGUI() {
		mainframe = new JFrame("Word on the Street");
		mainframe.setLayout(new GridLayout(1,5));
		mainframe.setSize(400, 100);
		mainframe.setBackground(Color.black);
		mainframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		newLabel("#3399FF");
		newLabel("#00FF00");
		newLabel("#FFFF00");
		newLabel("#FF99FF");
		newLabel("#FF3300");
			
		mainframe.setLocationRelativeTo(null);
		mainframe.setVisible(true);
		while(true) {
			scroll();
		}
	}
	
	public void pause() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void newLabel(String fore) {
		JLabel l;
		if(ldex < 5) {
			l = new JLabel();
			l.setHorizontalAlignment(JLabel.CENTER);
			l.setOpaque(true);
			l.setForeground(Color.decode(fore));
			l.setFont(new Font("", Font.BOLD, 20));
			labels[ldex] = l;
			mainframe.add(l);
			ldex++;
		} else {
			System.err.println("You have exceeded the total number of available labels.");
		}
	}
	
	public void scroll() {
		for(int i=0; i < message.length() + 5; i++) {
			if(i >= message.length()) {
				labels[4].setText("");
			} else {
				labels[4].setText(Character.toString(message.charAt(i)));
			}
			pause();
			for(int j=0; j < 4; j++) {
				labels[j].setText(labels[j+1].getText());
			}
		}
	}
	
	public static void main(String [] args) {
		//System.out.println(Thread.currentThread().getName());
		@SuppressWarnings("unused")
		WOTS w = new WOTS("Do you remember that one time when they were chanting my name, and I used my strength to rip my blouse?");
	}
	
}
