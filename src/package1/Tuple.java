package package1;

/**
 * @author tobydobbs
 * A class to represent tuples for SAR
 */
public class Tuple {
	
	String a;
	int occur;

	public Tuple(String a, int occur) {
		this.a = a;
		this.occur = occur;
	}
	
	public String toString() {
		return "Term: " + a + ", Occurences: " + occur;
	}

}
