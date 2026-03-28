package CuboidDimensionsOrderedBySurfaceArea;

import java.util.ArrayList;

public class SurfaceAreaFinderAfterNx1x1Done {

	//For the longest time, I just referred to the prev papers to get the list of cuboids with different dimensions, but same surface area.
	// It turns out that the prev papers skipped a few entries!
	public static void main(String args[]) {
		
		ArrayList <cuboidDimensionComparable>list = new ArrayList <cuboidDimensionComparable>();
		
		int MAX_AREA = 106;
		for(int a=1; a<200; a++) {
			for(int b=a; b<200; b++) {
				
				if(a == 1 && b == 1) {
					continue;
				}
				
				for(int c=b; c<200; c++) {
					
					int surfaceArea = 2 * (a*b + a*c + b*c);
					
					if(surfaceArea <= MAX_AREA) {
						list.add(new cuboidDimensionComparable(c, b, a, surfaceArea));
					}
					
				}
			}
		}
		
		
		for(int i=0; i<list.size(); i++) {
			
			int swapIndex = i;
			
			for(int j=i+1; j<list.size(); j++) {
				
				if(list.get(swapIndex).compareTo(list.get(j)) > 0) {
					
					swapIndex = j;
				}
			}
			
			cuboidDimensionComparable tmp = list.get(i);
			list.set(i, list.get(swapIndex));
			list.set(swapIndex, tmp);
			
		}
		
		int numAreaHits[] = new int[MAX_AREA + 1];

		for(int i=0; i<list.size(); i++) {
			if(list.get(i).getArea() > MAX_AREA) {
				break;
			}
			numAreaHits[list.get(i).getArea()]++;
		}
		
		for(int i=0; i<list.size(); i++) {
			
			if(list.get(i).getArea() > MAX_AREA) {
				break;
			}
			if(numAreaHits[list.get(i).getArea()] < 3) {
				continue;
			}
			System.out.println(list.get(i));
			
			if(i + 1 < list.size() && list.get(i+1).getArea() > list.get(i).getArea()) {
				System.out.println();
			}
		}
	}
}
/*Targets for nets covering 3 cuboids once Nx1x1 is done:
 * By inspection, the max perimeter to deal with is 8. (2x2 and 3x1)
 * That's just barely doable!

10 x 2 x 1: 64
4 x 4 x 2: 64
7 x 2 x 2: 64

11 x 2 x 1: 70
5 x 5 x 1: 70
8 x 3 x 1: 70

9 x 3 x 1: 78
5 x 3 x 3: 78
7 x 4 x 1: 78

13 x 2 x 1: 82
6 x 5 x 1: 82
7 x 3 x 2: 82

14 x 2 x 1: 88
10 x 2 x 2: 88
8 x 4 x 1: 88
6 x 4 x 2: 88

15 x 2 x 1: 94
11 x 3 x 1: 94
5 x 4 x 3: 94
7 x 5 x 1: 94

11 x 2 x 2: 96
4 x 4 x 4: 96
6 x 6 x 1: 96

12 x 3 x 1: 102
7 x 3 x 3: 102
9 x 3 x 2: 102
*/

