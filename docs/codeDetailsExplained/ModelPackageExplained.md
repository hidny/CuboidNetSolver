
## Model Package Explained

This code mainly defines a virtual model of the cuboid. The one I went with was to basically tell the program that the cuboid is a flat 2D map that look like this:
```
Map
          0   1                     

  2       5   6      11      14  15 
  3       7   8      12      16  17 
  4       9  10      13      18  19 

         20  21                     

(In this case, it's the 3x2x1 cuboid)                     
```
I then said that from any cell, you can go up, right, left, or down. The only thing that could happen is that you move to another cell and possibly rotation your orientation on the map. For moving right when you're on cell #7 leads you to being on cell #8, but moving right when you're on cell #1 leads you to being on cell #11 and rotated 90 degrees clockwise. Another example is going up from cell #14 leads to being on cell #1 and rotation 180 degrees.

I carefully crafted the rules for what happens when you move out of bounds in such a way that it's like you're on a cuboid and I still log the results of those rules at the start of every run. The logs show what happens when you go up from the cell, right of the cell, below the cell, and left of the cell. I probably should have deleted these logs a long time ago, but whatever.

The model package also has the CuboidToFoldOn.java class which holds the current state of the cuboid. (i.e. which cells are used and how the used cells are rotated relative to the flat map described above)

### Example of the record of the cell state transition
```
Printed flattened cuboid with bonus square at bottom...
This is the map I will use to encode neighbours and get somewhere... hopefully.
Neighbours for 0:
15 with 180 rotation
1
5
2 with 90 counter

Neighbours for 1:
14 with 180 rotation
11 with 90 clockwise
6
0

Neighbours for 2:
0 with 90 clockwise
5
3
15
(...)
```