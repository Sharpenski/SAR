package package1;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Utility {
	
	public static void pause() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void newLabel(JLabel[] labels, JFrame mainframe, String fore, int capacity, int ldex) {
		JLabel l;
		if(ldex < capacity) {
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
	
	public static void scroll(JLabel[] labels, String message) {
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

}
