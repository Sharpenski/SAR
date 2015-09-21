package package1;

public class Counter {
	
	private int c = 0;
	
	public void increment() {
		c++;
	}
	
	public void decrement() {
		c--;
	}
	
	public int value() {
		return c;
	}
	
	public static void main(String[] args) {
		Counter counter = new Counter();
		System.out.println(counter.value());
	}

}
