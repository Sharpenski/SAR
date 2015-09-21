package package1;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SAR {
	
	JFrame mainframe;
	JPanel panel1, panel2, panel3, panel4;
	JTextField searchBox;
	JButton search, replace;
	JCheckBox sensitive, suggestion;
	
	JFrame repframe;
	JPanel ranel1, ranel2;
	JTextField repBox;
	JButton enter;
	
	HashMap<Integer, Tuple> theMap;
	String fileToScan, content, toRepper;

	public SAR(String fileToScan) {
		this.fileToScan = fileToScan;
		generateGUI();
	}
	
	public void generateGUI() {
		mainframe = new JFrame("Search and Replace");
		mainframe.setBackground(Color.decode("#1F7A8F"));
		mainframe.setLayout(new GridLayout(3,1));
		mainframe.setSize(500, 150);
		mainframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		
		panel1 = new JPanel(new FlowLayout());
		searchBox = new JTextField("Enter your search term...", 30);
		searchBox.setHorizontalAlignment(JTextField.CENTER);
		searchBox.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				replace.setEnabled(false);
				searchBox.setText("");
			}
		});;
		panel1.add(searchBox);
		mainframe.add(panel1);
		
		
		panel2 = new JPanel(new FlowLayout());
		search = new JButton("Search");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> tokens = new ArrayList<String>();
				try {
					tokens = scanFile();
				} catch (IOException e1) {
					e1.printStackTrace();
					System.exit(0);
				}
				buildMap(tokens);
				toRepper = searchBox.getText();
				int occ;
				if((occ = inMap(toRepper)) > 0) {
					if(occ == 1) {
						searchBox.setText("Found a match!");
					} else {
						searchBox.setText("Found " + occ + " matches!");
					}
					replace.setEnabled(true);
				} else {
					searchBox.setText("No matches.");
				}
			}
		});
		panel2.add(search);
		replace = new JButton("Replace");
		replace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateRepGUI(toRepper);
			}
		});
		replace.setEnabled(false);
		panel2.add(replace);
		mainframe.add(panel2);
		
		panel3 = new JPanel(new FlowLayout());
		sensitive = new JCheckBox("Make case-sensitive?");
		panel3.add(sensitive);
		suggestion = new JCheckBox("Turn on suggestion?");
		panel3.add(suggestion);
		mainframe.add(panel3);
		
		mainframe.setLocationRelativeTo(null);
		mainframe.setVisible(true);
	}
	
	public void generateRepGUI(final String toRep) {
		repframe = new JFrame("Search and Replace");
		repframe.setLayout(new GridLayout(2, 1));
		repframe.setSize(400, 90);
		repframe.setBackground(Color.decode("#0099CC"));
		repframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				repframe.dispose();
			}
		}); 
		
		panel1 = new JPanel(new FlowLayout());
		repBox = new JTextField("Replace with...", 30);
		repBox.setHorizontalAlignment(JTextField.CENTER);
		panel1.add(repBox);
		repframe.add(panel1);
		
		panel2 = new JPanel(new FlowLayout());
		enter = new JButton("Finish");
		enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pattern p = Pattern.compile(toRep);
				Matcher m = p.matcher(content);
				content = m.replaceAll(repBox.getText());
				repframe.dispose();
				searchBox.setText("Replaced!");
				replace.setEnabled(false);
				try {
					writeToFile();
				} catch (IOException e1) {
					System.err.println("The file you originally read from has been moved. "
							+ "Please relocate it so that I can write back to it. Thanks.");
				}
			}
		});
		panel2.add(enter);
		repframe.add(panel2);
		
		repframe.setLocationRelativeTo(mainframe);
		repframe.setVisible(true);
	}
	
	public void writeToFile() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileToScan));
		for(char ch : content.toCharArray()) {
			if(ch == '\n') {
				bw.newLine();
			} else {
				bw.write(ch);
			}
		}
		bw.close();
	}
	
	public ArrayList<String> scanFile() throws IOException {
		FileReader  fr = new FileReader(fileToScan);
		BufferedReader br = new BufferedReader(fr);
		content = "";
		String c;
		while((c = br.readLine()) != null) {
			content = String.format("%s\n", content.concat(c));
		}
		//System.out.println(content);
		
		StringTokenizer st = new StringTokenizer(content, " .,!?()[]\n", false);
		ArrayList<String> tokens = new ArrayList<String>();
		while(st.hasMoreTokens()) {
			tokens.add(st.nextToken());
		}
		br.close();
		//System.out.println(tokens);
		
		return tokens;
	}
	
	public void buildMap(ArrayList<String> tokens) {
		String temp;
		int code;
		theMap = new HashMap<Integer, Tuple>();
		Iterator<String> itr = tokens.iterator();
		while(itr.hasNext()) {
			temp = itr.next().toLowerCase();
			if(theMap.containsKey(code = temp.toLowerCase().hashCode())) {
				theMap.get(code).occur++;
			} else {
				theMap.put(code, new Tuple(temp, 1));
			}
		}
		theMap = (HashMap<Integer, Tuple>) sortByKeys(theMap);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <K extends Comparable, V> Map<K,V> sortByKeys(Map<K,V> map) {
		List<K> keys = new LinkedList<K>(map.keySet());
		Collections.sort(keys);
		Map<K,V> sortedMap = new LinkedHashMap<K,V>();
		for(K key : keys) {
			sortedMap.put(key, map.get(key));
		}
		return sortedMap;
	}
	
	public int inMap(String token) {
		int code = token.toLowerCase().hashCode();
		if(theMap.containsKey(code)) {
			return theMap.get(code).occur;
		}
		return 0;
	}
	
	public static void main(String [] args) {
		@SuppressWarnings("unused")
		SAR test1 = new SAR(args[0]);
	}

}
