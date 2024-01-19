package CuboidDimensionsOrderedBySurfaceArea;

public class cuboidDimensionComparable4D implements Comparable {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private int a;
	private int b;
	private int c;
	private int d;
	private int volume;
	
	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}

	public int getC() {
		return c;
	}

	public int getD() {
		return d;
	}
	public int getVolume() {
		return volume;
	}

	public String toString() {
		
		String ret = a + " x " + b + " x " + c + " x " + d + ": " + volume;
		
		return ret;
	}
	
	public cuboidDimensionComparable4D(int a, int b, int c, int d, int volume) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.volume = volume;
	}

	@Override
	public int compareTo(Object o) {
		
		if(this.volume > ((cuboidDimensionComparable4D)o).volume) {
			return 1;
		} else if(this.volume == ((cuboidDimensionComparable4D)o).volume) {
			
			if(this.a > ((cuboidDimensionComparable4D)o).a) {
				return 1;
			} else if(this.a == ((cuboidDimensionComparable4D)o).a) {
				return 0;
			} else {
				return -1;
			}
			
		} else {
			return -1;
		}
	}

}
