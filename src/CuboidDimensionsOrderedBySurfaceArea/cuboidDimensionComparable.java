package CuboidDimensionsOrderedBySurfaceArea;

public class cuboidDimensionComparable implements Comparable {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private int a;
	private int b;
	private int c;
	private int area;
	
	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}

	public int getC() {
		return c;
	}

	public int getArea() {
		return area;
	}

	public String toString() {
		
		String ret = a + " x " + b + " x " + c + ": " + area;
		
		return ret;
	}
	
	public cuboidDimensionComparable(int a, int b, int c, int area) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
		this.area = area;
	}

	@Override
	public int compareTo(Object o) {
		
		if(this.area > ((cuboidDimensionComparable)o).area) {
			return 1;
		} else if(this.area == ((cuboidDimensionComparable)o).area) {
			
			if(this.a > ((cuboidDimensionComparable)o).a) {
				return 1;
			} else if(this.a == ((cuboidDimensionComparable)o).a) {
				return 0;
			} else {
				return -1;
			}
			
		} else {
			return -1;
		}
	}

}
