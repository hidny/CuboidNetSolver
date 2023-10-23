# Summary of the Cuboid Repo

This repo is a simplified version of the code I used to get all the nets that fold the 11x1x1 and 5x3x2 cuboids. Though it's slower than the more complicated and optimized code in the weirdmathstuff repo, this repo is fast enough to find all of the 8x1x1 and 5x2x1 nets within 2 days (the optimized version only takes 4 hours).

I think I'll explain the simplified code, and then explain all the optimizations later.
 
## How the main program works
 
The main program to run is DFSIntersectFinder2.java  .

The main program runs a function that is centered around finding nets for an Nx1x1 cuboid by starting from the bottom cell of the Nx1x1 cuboid.
As it solves for the Nx1x1 cuboid, it also makes sure that it can also fill in another cuboid, so it can find nets that fold into both cuboids. 
If you want to only solve the Nx1x1 cuboid, just make sure the other cuboid is the same Nx1x1 cuboid.
(I don't feel like having a new file optimized to cover that case. I want to keep this repository simple)

I've left a few valid cuboid settings for you to try in the main function. Feel free to uncomment the one you want and run it:
(Note that the Nx1x1 cuboid should always be the first argument.)
### Main function in question:
```
public static void main(String args[]) {
		System.out.println("Fold Resolver Ordered Regions intersection skip symmetries Nx1x1:");

		
		//solveCuboidIntersections(new CuboidToFoldOn(5, 1, 1), new CuboidToFoldOn(3, 2, 1));
		
		//solveCuboidIntersections(new CuboidToFoldOn(7, 1, 1), new CuboidToFoldOn(3, 3, 1));
		
		//solveCuboidIntersections(new CuboidToFoldOn(8, 1, 1), new CuboidToFoldOn(5, 2, 1));
		//It got 35675 solutions
		//With apparent holes, I got 35697 solutions
		
		//solveCuboidIntersections(new CuboidToFoldOn(9, 1, 1), new CuboidToFoldOn(4, 3, 1));
		//It got 4469 solutions and it took about 41.5 hours (for the optimized code)
		
		//solveCuboidIntersections(new CuboidToFoldOn(11, 1, 1), new CuboidToFoldOn(5, 3, 1));
		//This took about 2 months with 3-4 cores running constantly.

		//solveCuboidIntersections(new CuboidToFoldOn(11, 1, 1), new CuboidToFoldOn(7, 2, 1));
		//I haven't tried this, but I imagine it being slower than the previous one.
		
		solveCuboidIntersections(new CuboidToFoldOn(13, 1, 1), new CuboidToFoldOn(3, 3, 3));
		// This one is too slow. I either need to find an amazing optimization, or just throw more compute power at it.
		//(Update: it will now take around 5 months to finish)
}
```
## The first loop
The first loop in the solveCuboidIntersections function involves going through every possible way the bottom of the Nx1x1 could be matched/attached with a cell in the other cuboid. For example, we have 22 possible places to put the bottom of the 5x1x1 cuboid in the 3x2x1 cuboid. 22 is the worst case because it's equal to the area of the 3x2x1 cuboid (and the 5x1x1 cuboid). The way I consider the match/attachment configuration unique is by making sure a list of 4 numbers is unique:
1) The number of cells above the other cuboid and on the same face.
1) The number of cells right of the other cuboid and on the same face.
1) The number of cells below the other cuboid and on the same face.
1) The number of cells left of the other cuboid and on the same face.

Here's a map of the 3x2x1 cuboid followed by a list of the ways to attach the bottom of the Nx1x1 cuboid. I hope that looking a this will give you an idea of what's going on (This is from the output folder):
```
          0   1                     

  2       5   6      11      14  15 
  3       7   8      12      16  17 
  4       9  10      13      18  19 

         20  21                     

         19  18                     
         17  16                     
         15  14                     

Unique rotation lists:
Cell and rotation: 0 and 0
0, 1, 0, 0, 
Cell and rotation: 0 and 1
1, 0, 0, 0, 
Cell and rotation: 0 and 2
0, 0, 0, 1, 
Cell and rotation: 0 and 3
0, 0, 1, 0, 
Cell and rotation: 2 and 0
0, 0, 2, 0, 
Cell and rotation: 2 and 1
0, 2, 0, 0, 
Cell and rotation: 2 and 2
2, 0, 0, 0, 
Cell and rotation: 2 and 3
0, 0, 0, 2, 
Cell and rotation: 3 and 0
1, 0, 1, 0, 
Cell and rotation: 3 and 1
0, 1, 0, 1, 
Cell and rotation: 5 and 0
0, 1, 2, 0, 
Cell and rotation: 5 and 1
1, 2, 0, 0, 
Cell and rotation: 5 and 2
2, 0, 0, 1, 
Cell and rotation: 5 and 3
0, 0, 1, 2, 
Cell and rotation: 6 and 0
0, 0, 2, 1, 
Cell and rotation: 6 and 1
0, 2, 1, 0, 
Cell and rotation: 6 and 2
2, 1, 0, 0, 
Cell and rotation: 6 and 3
1, 0, 0, 2, 
Cell and rotation: 7 and 0
1, 1, 1, 0, 
Cell and rotation: 7 and 1
1, 1, 0, 1, 
Cell and rotation: 7 and 2
1, 0, 1, 1, 
Cell and rotation: 7 and 3
0, 1, 1, 1, 
Num starting points and rotations to check: 22
```
Note that when it comes to the 3x3x3 cuboid, there's only 9 unique ways to attach the bottom of the 13x1x1 cuboid because it's a cube with lots of symmetries. So that's good...
The code for this logic is in PivotCellDescription.java.



# Depth 1 of the algo details:
Once inside the first loop, we enter into an algorithm that recursively adds 1 cell(or square) to the net at a time by attaching it sideways or above/below a cell/square that's already there. In order to avoid finding the same solution several times, but with different symmetries, I went out of my way to force the first depth to only have 5 configurations and added hard-coded rules for where the top cell is allowed to be. The details of the rules live in SymmetryResolver.java. If these rules weren't there, it would still work, but I think it would be 5-10 times slower because of all the redundant work the algorithm would do without the rules. I'll explain in more detail in each section. The reason I'm bothering to explain this is because I think this background info will help explain a lot of what the output looks like.

## General rules
1. Top cell attaches to just as many cells as bottom cell or less
2. We need to get to the top cell by going either above or to the right of bottom cell
3. Index 0 is the bottom and index (Area - 1) is the top. In the case of the examples, it's 13.
## The 5 phases
### The cross phase
In this phase, the bottom cell has the max of 4 cell neighbours attached to it. Also, the path to the top cell goes through the cell above the bottom cell, and the top cell should not be left of the bottom cell.
#### Example:
```
|..|..|..|12|..|
|..|..|..|13|..|
|..|..| 9| 6| 3|
|..|11| 8| 5| 2|
|..|10|..|..|..|
| 1| 0| 7|..|..|
|..| 4|..|..|..|
```

### The 'T' phase
In this phase, the bottom cell has 3 neighbours, and the cell to the left of it is blank. The path to the top cell could be through the cell above the bottom cell or the cell to the right of the bottom cell, but the top cell must not be lower than the bottom cell.

#### Examples:
```
| 3| 6|..|..|
|..|13| 9|..|
|..|12|..|..|
|..|11|..|..|
|..|10|..|..|
|..| 0| 7| 8|
| 1| 4|..|..|
| 2| 5|..|..|

|..|..|..|..| 3|13| 9|
| 1|10|..|11|12|..|..|
|..| 0| 7| 8|..|..|..|
|..| 4|..| 5| 6|..|..|
|..|..|..| 2|..|..|..|
```

### The 'L' phase
In this phase, the bottom cell has 2 neighbours, and those neighbours are above and to the right of the bottom cell, and the path to the top cell goes through the cell above the bottom cell.

#### Example:
```
|..|13|..|..|..|..|..|..|
| 6| 3|12| 9|..|..|..|..|
|..|..|11| 8| 5| 2|..|..|
|..|..|..|..| 4| 1|10|..|
|..|..|..|..|..|..| 0| 7|
```
### The pipe phase
In this phase, the bottom cell has 2 neighbours, and those neighbours are above and below the bottom cell. The path to the top cell goes through the cell above the bottom cell, and the top cell should not be left of the bottom cell. (The rules are similar to the cross phase)

#### Example:
```
|..|12| 3|..|..|
|..|..|13|..|..|
|..|..| 9| 6|..|
|..|11| 8| 5| 2|
| 1|10| 7|..|..|
|..| 0|..|..|..|
|..| 4|..|..|..|
```

### The simple phase
In this phase, the top and bottom cell only have 1 neighbour. Also, the top cell should not be left of the bottom cell. (The rules are similar to the cross phase)
This phase is by far the fastest for the algorithm to go through and easiest to look at because the top cell is on the top, the bottom cell is on the bottom, and there's 4 cells in each row in between. Also, the 4 cells of each row must have a different x position mod 4. I also <b>think</b> there's only 4 ways for an in-between row to be configured, which makes me <b>think</b> this phase is liable to be optimized with an even better algorithm.
(See the CuboidSimplePhaseNetSearch repo for my attempt at searching for nets of only this phase) Proving that there are only 4 ways to do it should be simple enough, but I haven't done it yet :(. I'm convinced it's true because I've experimentally verified it for N=1 to 6, and I can't imagine suddenly finding a counter-example once N=8+.

#### Examples:
````
|..|..|..|..|..|..|..|..|13|..|
|..|..|..|..|..|..|12| 9| 6| 3|
|..|..|..| 8| 5| 2|11|..|..|..|
| 4| 1|10| 7|..|..|..|..|..|..|
|..|..| 0|..|..|..|..|..|..|..|

|..|..|13|..|..|..|..|
|..|..|12| 9| 6| 3|..|
| 5|..|..| 8|..| 2|11|
| 4| 1|10| 7|..|..|..|
|..|..| 0|..|..|..|..|
```

The 4 ways the in-between rows can be:
```
{1, 1, 1, 1, 0, 0, 0}, or
{1, 1, 0, 1, 0, 0, 1}, or
{1, 0, 1, 1, 0, 1, 0}, or
{1, 0, 0, 1, 0, 1, 1}
```

# A more detailed explanation of the recursive algo and the 'secret sauce'

Before explaining the approach, I'm going to highlight the problem the naive approach has, so I could highlight the necessity of the 'secret sauce' approach.

I don't think my attempt at just explaining the 'secret sauce' in the patreonPostSummaryWithMoreDetails.txt file will make sense unless you understand the problem it solves.

# Idea 1

The first naive approach to have a function recursively add a cell one-by-one at every legal location above/below or right/left of a cell that's already in the net.
This will work, but the problem is that it will waste a lot of time finding the same solution over and over again.
This starts to be a problem for even a small cuboid like the 4x1x1 cuboid.

# Idea 2

The second approach is to record all the configurations that were already seen, so that you could avoid rechecking the same positions over and over again.
Based on what I read in the previous papers, they solved the 5x1x1 and 3x2x1 cuboid using this approach and for the 7x1x1 cuboid and 3x3x1 cuboid, they had to stop using this approach at some point because it took too much memory. They had to switch to another approach before getting to depth 22 out of 30.
"This simple idea works up to 22 for two boxes of size 1x1x5 and 1x3x5 in [1] (...) However, for the surface area 30, it does not work even on a supercomputer (CRAY XC30) due to memory overflow when i = 22"
."
(2015 - "Common Developments of Three Incongruent Boxes of Area 30" by Dawei Xu, Takashi Horiyama, Toshihiro Shirakawa, and Ryuhei Uehara) They might have had a bunch of clever tricks to deal with the memory issues they had, but they still had to deal with holding a bunch of memory at the same time.

# Idea 3 (the secret-sauce)
Believe it or not, there's an improvement that can be made that doesn't involve recording the previous configurations.
The trick is to add an artificial constraint on how to add cells to the cuboid while just barely searching all possibilities. With a bit of experimentation, I found that if I artificially force the order in which I add cells to match the order/path in which a deterministic breadth-first search algorithm would explore the net from the bottom cell, the algorithm will have three nice properties:
1. The algo will search every possible net because a breadth-first search algorithm is more than capable of searching any finite net.
2. At every step of the recursive function, the number of options will be constrained by the last cell added, severely reducing its branching factor.
3. Because the breadth-first search algorithm is deterministic, if we ignore symmetry for a minute, every net and partial net will have only 1 way the breadth-first search algo will explore it, so without bothering to record previously checked paths, we will visit every possible net once. If we don't ignore symmetry, every net could be visited up to 8* times, which is still not too bad.

I don't think it's possible to over-estimate how important idea #3 is. I think it's a greater improvement than all the other optimizations I created combined.

Update: Through working on the problem of just enumerating polyominoes on a square lattice, I came to the realization that the algorithm I just described is just a slight variant of Redelmeier’s algorithm. The definitions and description are superficially different, but both algorithms accomplish the same goals
with similar time and space complexities. Though I am very biased, I feel that understanding both algorithms and how they are related is more enlightening than just understanding one of them. (See high-level summary of the algorithm here: https://en.wikipedia.org/wiki/Polyomino and https://math.stackexchange.com/questions/1861614/enumerating-polyominos)

At this point, I hope the picture in pics/exampleTransformation.png is starting to make sense.

*: Technically, if you're looking for a net that fits two cuboid shapes, the max number of duplicates is more like 8 times the number of iterations of the first loop, but it's still relatively small, and, in practice, that number is probably way above average.
 