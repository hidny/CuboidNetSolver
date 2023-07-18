package CuboidDimensionsOrderedBySurfaceArea;

import java.util.ArrayList;

public class SurfaceAreaFinder {

	//For the longest time, I just referred to the prev papers to get the list of cuboids with different dimensions, but same surface area.
	// It turns out that the prev papers skipped a few entries!
	public static void main(String args[]) {
		
		ArrayList <cuboidDimensionComparable>list = new ArrayList <cuboidDimensionComparable>();
		
		for(int a=1; a<50; a++) {
			for(int b=a; b<50; b++) {
				for(int c=b; c<50; c++) {
					
					int surfaceArea = 2 * (a*b + a*c + b*c);
					
					list.add(new cuboidDimensionComparable(c, b, a, surfaceArea));
					
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
		
		for(int i=0; i<list.size(); i++) {
			
			if(list.get(i).getArea() > 200) {
				break;
			}
			System.out.println(list.get(i));
			
			if(i + 1 < list.size() && list.get(i+1).getArea() > list.get(i).getArea()) {
				System.out.println();
			}
		}
	}
	
	/*
	 * Here's the list I got: 
1 x 1 x 1: 6

2 x 1 x 1: 10

3 x 1 x 1: 14

2 x 2 x 1: 16

4 x 1 x 1: 18

3 x 2 x 1: 22
5 x 1 x 1: 22

2 x 2 x 2: 24

6 x 1 x 1: 26

4 x 2 x 1: 28

3 x 3 x 1: 30
7 x 1 x 1: 30

3 x 2 x 2: 32

5 x 2 x 1: 34
8 x 1 x 1: 34

4 x 3 x 1: 38
9 x 1 x 1: 38

4 x 2 x 2: 40
6 x 2 x 1: 40

3 x 3 x 2: 42
10 x 1 x 1: 42

5 x 3 x 1: 46
7 x 2 x 1: 46
11 x 1 x 1: 46

4 x 4 x 1: 48
5 x 2 x 2: 48

12 x 1 x 1: 50

4 x 3 x 2: 52
8 x 2 x 1: 52

3 x 3 x 3: 54
6 x 3 x 1: 54
13 x 1 x 1: 54

6 x 2 x 2: 56

5 x 4 x 1: 58
9 x 2 x 1: 58
14 x 1 x 1: 58

5 x 3 x 2: 62
7 x 3 x 1: 62
15 x 1 x 1: 62

4 x 4 x 2: 64
7 x 2 x 2: 64
10 x 2 x 1: 64

4 x 3 x 3: 66
16 x 1 x 1: 66

6 x 4 x 1: 68

5 x 5 x 1: 70
8 x 3 x 1: 70
11 x 2 x 1: 70
17 x 1 x 1: 70

6 x 3 x 2: 72
8 x 2 x 2: 72

18 x 1 x 1: 74

5 x 4 x 2: 76
12 x 2 x 1: 76

5 x 3 x 3: 78
7 x 4 x 1: 78
9 x 3 x 1: 78
19 x 1 x 1: 78

4 x 4 x 3: 80
9 x 2 x 2: 80

6 x 5 x 1: 82
7 x 3 x 2: 82
13 x 2 x 1: 82
20 x 1 x 1: 82

(...)

5 x 5 x 2: 90
6 x 3 x 3: 90
22 x 1 x 1: 90

5 x 4 x 3: 94
7 x 5 x 1: 94
11 x 3 x 1: 94
15 x 2 x 1: 94
23 x 1 x 1: 94

7 x 3 x 3: 102
9 x 3 x 2: 102
12 x 3 x 1: 102
25 x 1 x 1: 102

8 x 5 x 1: 106
17 x 2 x 1: 106
26 x 1 x 1: 106

5 x 5 x 3: 110
7 x 6 x 1: 110
13 x 3 x 1: 110
27 x 1 x 1: 110

7 x 5 x 2: 118
9 x 5 x 1: 118
11 x 4 x 1: 118
14 x 3 x 1: 118
19 x 2 x 1: 118
29 x 1 x 1: 118

7 x 4 x 3: 122
11 x 3 x 2: 122
30 x 1 x 1: 122

6 x 5 x 3: 126
7 x 7 x 1: 126
9 x 3 x 3: 126
15 x 3 x 1: 126
31 x 1 x 1: 126


5 x 5 x 4: 130
10 x 5 x 1: 130
21 x 2 x 1: 130
32 x 1 x 1: 130

9 x 6 x 1: 138
10 x 3 x 3: 138
13 x 4 x 1: 138
34 x 1 x 1: 138

7 x 5 x 3: 142
8 x 7 x 1: 142
11 x 5 x 1: 142
13 x 3 x 2: 142
17 x 3 x 1: 142
23 x 2 x 1: 142
35 x 1 x 1: 142

	 */
}
