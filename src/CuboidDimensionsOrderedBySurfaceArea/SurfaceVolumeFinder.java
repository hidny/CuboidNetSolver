package CuboidDimensionsOrderedBySurfaceArea;

import java.util.ArrayList;

public class SurfaceVolumeFinder {

	//This is just a setup for a joke about finding the folds for a 4D hypercuboid.
	public static void main(String args[]) {
		
		ArrayList <cuboidDimensionComparable4D>list = new ArrayList <cuboidDimensionComparable4D>();
		
		int MAX_DIM = 50;
		for(int a=1; a<10; a++) {
			System.out.println(a);
			for(int b=a; b<MAX_DIM; b++) {
				for(int c=b; c<MAX_DIM; c++) {
					for(int d=c; d<MAX_DIM; d++) {
					
						int surfaceVolume = 2 * (a*b*c + a*b*d + a*c*d + b*c*d);
					
						list.add(new cuboidDimensionComparable4D(d, c, b, a, surfaceVolume));
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
			
			cuboidDimensionComparable4D tmp = list.get(i);
			list.set(i, list.get(swapIndex));
			list.set(swapIndex, tmp);
			
		}
		
		for(int i=0; i<list.size(); i++) {
			
			if(list.get(i).getVolume() > 1000) {
				break;
			}
			System.out.println(list.get(i));
			
			if(i + 1 < list.size() && list.get(i+1).getVolume() > list.get(i).getVolume()) {
				System.out.println();
			}
		}
	}
	
	/*
	 * Here's the list I got: 

	 */
}
