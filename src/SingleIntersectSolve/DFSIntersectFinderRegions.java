package SingleIntersectSolve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import Coord.Coord2D;
import Coord.CoordWithRotationAndIndex;
//import Cuboid.SymmetryResolver.SymmetryResolver;
//import DupRemover.BasicUniqueCheckImproved;
//import FancyTricks.ThreeBombHandler;
//import FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;
import DupRemover.BasicUniqueCheckImproved;
import SolutionResolver.SolutionResolverInterface;
import SolutionResolver.StandardResolverUsingMemory;
import GraphUtils.PivotCellDescription;
import Model.CuboidToFoldOn;
import Model.Utils;

public class DFSIntersectFinderRegions {

	
	public static final int NUM_ROTATIONS = 4;
	public static final int NUM_NEIGHBOURS = NUM_ROTATIONS;
	
	
	public static void solveCuboidIntersections(CuboidToFoldOn cuboidToWrap, CuboidToFoldOn cuboidToBringAlong) {
		solveCuboidIntersections(cuboidToWrap, cuboidToBringAlong, true);
	}
	
	public static void solveCuboidIntersections(CuboidToFoldOn cuboidToBuild, CuboidToFoldOn cuboidToBringAlong, boolean skipSymmetries) {
		SolutionResolverInterface solutionResolver = null;
		
		
		if(Utils.getTotalArea(cuboidToBuild.getDimensions()) != Utils.getTotalArea(cuboidToBringAlong.getDimensions())) {
			System.out.println("ERROR: The two cuboid to intersect don't have the same area.");
			System.exit(1);
		}
		
		// Set the solution resolver to different things depending on the size of the cuboid:
		solutionResolver = new StandardResolverUsingMemory();
		
		solveCuboidIntersections(cuboidToBuild, cuboidToBringAlong, skipSymmetries, solutionResolver);
	}

	public static void solveCuboidIntersections(CuboidToFoldOn cuboidToBuild, CuboidToFoldOn cuboidToBringAlong, boolean skipSymmetries, SolutionResolverInterface solutionResolver) {
		
		
		//cube.set start location 0 and rotation 0
		

		//TODO: LATER use hashes to help.. (record potential expansions, and no-nos...)
		Coord2D paperToDevelop[] = new Coord2D[Utils.getTotalArea(cuboidToBuild.getDimensions())];
		for(int i=0; i<paperToDevelop.length; i++) {
			paperToDevelop[i] = null;
		}
		
		int GRID_SIZE = 2*Utils.getTotalArea(cuboidToBuild.getDimensions());
	
		boolean paperUsed[][] = new boolean[GRID_SIZE][GRID_SIZE];
		int indexCuboidOnPaper[][] = new int[GRID_SIZE][GRID_SIZE];

		int indexCuboidOnPaper2ndCuboid[][] = new int[GRID_SIZE][GRID_SIZE];
		
		for(int i=0; i<paperUsed.length; i++) {
			for(int j=0; j<paperUsed[0].length; j++) {
				paperUsed[i][j] = false;
				indexCuboidOnPaper[i][j] = -1;
				indexCuboidOnPaper2ndCuboid[i][j] = -1;
			}
		}

		//Default start location GRID_SIZE / 2, GRID_SIZE / 2
		int START_I = GRID_SIZE/2;
		int START_J = GRID_SIZE/2;
		
		CuboidToFoldOn cuboid = new CuboidToFoldOn(cuboidToBuild);
		//Insert start cell:
		
		//Once this reaches the total area, we're done!
		int numCellsUsedDepth = 0;

		int START_INDEX = 0;
		int START_ROTATION = 0;
		paperUsed[START_I][START_J] = true;
		paperToDevelop[numCellsUsedDepth] = new Coord2D(START_I, START_J);
		
		cuboid.setCell(START_INDEX, START_ROTATION);
		indexCuboidOnPaper[START_I][START_J] = START_INDEX;
		numCellsUsedDepth += 1;
		
		
		//TODO: Later try intersecting with all of them at once, so it's easier to get distinct solutions,
		// and maybe it's faster?

		//TODO: 2nd one
		ArrayList<PivotCellDescription> startingPointsAndRotationsToCheck = PivotCellDescription.getUniqueRotationListsWithCellInfo(cuboidToBringAlong);
		
		System.out.println("Num starting points and rotations to check: " + startingPointsAndRotationsToCheck.size());
		
		//(Set i=1 for non-trial Nx1x1 intersections)
		for(int i=0; i<startingPointsAndRotationsToCheck.size(); i++) {
			
			int startIndex2ndCuboid =startingPointsAndRotationsToCheck.get(i).getCellIndex();
			int startRotation2ndCuboid = startingPointsAndRotationsToCheck.get(i).getRotationRelativeToCuboidMap();
			
			CuboidToFoldOn cuboidToBringAlongStartRot = new CuboidToFoldOn(cuboidToBringAlong);

			cuboidToBringAlongStartRot.setCell(startIndex2ndCuboid, startRotation2ndCuboid);
			indexCuboidOnPaper2ndCuboid[START_I][START_J] = startIndex2ndCuboid;
			
			int topBottombridgeUsedNx1x1[] = new int[Utils.getTotalArea(cuboidToBuild.getDimensions())];
			
			long debugIterations[] = new long[Utils.getTotalArea(cuboidToBuild.getDimensions())]; 
		
			doDepthFirstSearch(paperToDevelop, indexCuboidOnPaper, paperUsed, cuboid, numCellsUsedDepth, -1L, skipSymmetries, solutionResolver, cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid, topBottombridgeUsedNx1x1, false, debugIterations);
			
			
			System.out.println("Done with trying to intersect 2nd cuboid that has a start index of " + startIndex2ndCuboid + " and a rotation index of " + startRotation2ndCuboid +".");
			System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
			
		}
		
		//TODO: end todo 2nd one
		
		
		System.out.println("Final number of unique solutions: " + solutionResolver.getNumUniqueFound());
	}
	
	
	public static final int nugdeBasedOnRotation[][] = {{-1, 0, 1, 0}, {0, 1, 0 , -1}};
	public static long numIterations = 0;
	
	public static long doDepthFirstSearch(Coord2D paperToDevelop[], int indexCuboidonPaper[][], boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			long limitDupSolutions, boolean skipSymmetries, SolutionResolverInterface solutionResolver, CuboidToFoldOn cuboidToBringAlongStartRot, int indexCuboidOnPaper2ndCuboid[][],
			int topBottombridgeUsedNx1x1[],
			boolean debugNope, long debugIterations[]) {

		if(numCellsUsedDepth == cuboid.getNumCellsToFill()) {
			
			int indexes[][][] = new int[2][][];
			indexes[0] = indexCuboidonPaper;
			indexes[1] = indexCuboidOnPaper2ndCuboid;
			long tmp = solutionResolver.resolveSolution(cuboid, paperToDevelop, indexes, paperUsed);

			if(debugNope) {
				System.out.println("STOP!");
				System.out.println(numIterations);
				for(int i=0; i<numCellsUsedDepth; i++) {
					System.out.println("Iteration: " + debugIterations[i]);
				}
				System.exit(1);
			}
			return tmp;
		}
		
		
		//Display debug/what's-going-on update
		numIterations++;
		
		if(numIterations % 10000000L == 0) {
			
			System.out.println("Num iterations: " + numIterations);
			Utils.printFold(paperUsed);
			Utils.printFoldWithIndex(indexCuboidonPaper);
			Utils.printFoldWithIndex(indexCuboidOnPaper2ndCuboid);
			
			System.out.println("Solutions: " + solutionResolver.getNumUniqueFound());
			System.out.println();
			
			System.out.println("Last cell inserted: " + indexCuboidonPaper[paperToDevelop[numCellsUsedDepth - 1].i][paperToDevelop[numCellsUsedDepth - 1].j]);
			
			
		}
		//End display debug/what's-going-on update
		
		long retDuplicateSolutions = 0L;
		

		debugIterations[numCellsUsedDepth] = numIterations;
		
		//DEPTH-FIRST START:
		for(int i=regions[regionIndex].getMinOrderedCellCouldUsePerRegion(); i<paperToDevelop.length && paperToDevelop[i] != null; i++) {
			
			int indexToUse = indexCuboidonPaper[paperToDevelop[i].i][paperToDevelop[i].j];
			
			if( ! regions[regionIndex].getCellIndexToOrderOfDev().containsKey(indexToUse)) {
				continue;
				
			}/* else if(SymmetryResolver.skipSearchBecauseOfASymmetryArgDontCareAboutRotation
					(cuboid, paperToDevelop, indexCuboidonPaper, i,indexToUse)
				&& skipSymmetries) {
				continue;

				
			}*/ /*else	if( 2*numCellsUsedDepth < paperToDevelop.length && SymmetryResolver.skipSearchBecauseCuboidCouldProvablyNotBeBuiltThisWay
					(cuboid, paperToDevelop, indexCuboidonPaper, i,indexToUse, regions[regionIndex], topBottombridgeUsedNx1x1) && skipSymmetries) {
				
				break;
				
				//Maybe put this right after the contains key if condition? (regions[regionIndex].getCellIndexToOrderOfDev().containsKey(indexToUse))
			}*/

			CoordWithRotationAndIndex neighbours[] = cuboid.getNeighbours(indexToUse);
			
			int curRotation = cuboid.getRotationPaperRelativeToMap(indexToUse);
			
			int indexToUse2 = indexCuboidOnPaper2ndCuboid[paperToDevelop[i].i][paperToDevelop[i].j];
			int curRotationCuboid2 = cuboidToBringAlongStartRot.getRotationPaperRelativeToMap(indexToUse2);
			
			//Try to attach a cell onto indexToUse using all 4 rotations:
			for(int dirNewCellAdd=0; dirNewCellAdd<NUM_ROTATIONS; dirNewCellAdd++) {
				
				int neighbourArrayIndex = (dirNewCellAdd - curRotation + NUM_ROTATIONS) % NUM_ROTATIONS;
				
				if(cuboid.isCellIndexUsed(neighbours[neighbourArrayIndex].getIndex())) {
					
					//Don't reuse a used cell:
					continue;
					
				} else if(regions[regionIndex].getCellRegionsToHandleInRevOrder()[neighbours[neighbourArrayIndex].getIndex()] == false) {
					continue;
	
				} else if(regions[regionIndex].getCellIndexToOrderOfDev().get(indexToUse) == regions[regionIndex].getMinOrderedCellCouldUsePerRegion() 
						&& dirNewCellAdd <  regions[regionIndex].getMinCellRotationOfMinCellToDevPerRegion()) {
					continue;
				}

				int neighbourIndexCuboid2 = (dirNewCellAdd - curRotationCuboid2 + NUM_ROTATIONS) % NUM_ROTATIONS;
				
				int indexNewCell2 = cuboidToBringAlongStartRot.getNeighbours(indexToUse2)[neighbourIndexCuboid2].getIndex();
				
				if(cuboidToBringAlongStartRot.isCellIndexUsed(indexNewCell2)) {
					//no good!
					continue;
				}
				
				
				int new_i = paperToDevelop[i].i + nugdeBasedOnRotation[0][dirNewCellAdd];
				int new_j = paperToDevelop[i].j + nugdeBasedOnRotation[1][dirNewCellAdd];

				int indexNewCell = neighbours[neighbourArrayIndex].getIndex();
				
				
				if(paperUsed[new_i][new_j]) {
					//Cell we are considering to add is already there...
					continue;
				}
				
				
				int rotationNeighbourPaperRelativeToMap = (curRotation - neighbours[neighbourArrayIndex].getRot() + NUM_ROTATIONS) % NUM_ROTATIONS;
				int rotationNeighbourPaperRelativeToMap2 = (curRotationCuboid2 - cuboidToBringAlongStartRot.getNeighbours(indexToUse2)[neighbourIndexCuboid2].getRot() + NUM_ROTATIONS)  % NUM_ROTATIONS;
				
				//if(SymmetryResolver.skipSearchBecauseOfASymmetryArg
				//		(cuboid, paperToDevelop, i, indexCuboidonPaper, dirNewCellAdd, curRotation, paperUsed, indexToUse, indexNewCell)
				//	&& skipSymmetries == true) {
				//	continue;
				//}
				
				boolean cantAddCellBecauseOfOtherPaperNeighbours = false;
				/*boolean cantAddCellBecauseOfOtherPaperNeighbours = FoldResolveOrderedRegionsSkipSymmetries.cantAddCellBecauseOfOtherPaperNeighbours(paperToDevelop, indexCuboidonPaper,
						paperUsed, cuboid, numCellsUsedDepth,
						regions, regionIndex, indexToUse,
						indexNewCell, new_i, new_j, i
					);
				*/
				
				
				if( ! cantAddCellBecauseOfOtherPaperNeighbours) {
					
					//Setup for adding new cell:
					cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);
					cuboidToBringAlongStartRot.setCell(indexNewCell2, rotationNeighbourPaperRelativeToMap2);
					
					paperUsed[new_i][new_j] = true;
					indexCuboidonPaper[new_i][new_j] = indexNewCell;
					paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);

					if(indexToUse == 0) {
						topBottombridgeUsedNx1x1[indexNewCell] = dirNewCellAdd;
					} else if(indexToUse == cuboid.getCellsUsed().length - 1) {
						topBottombridgeUsedNx1x1[indexNewCell] = NUM_ROTATIONS + dirNewCellAdd;
					} else {
						topBottombridgeUsedNx1x1[indexNewCell] = topBottombridgeUsedNx1x1[indexToUse];
					}

					indexCuboidOnPaper2ndCuboid[new_i][new_j] = indexNewCell2;

					
					numCellsUsedDepth += 1;
					//End setup

					long newLimitDupSolutions = limitDupSolutions;
					if(limitDupSolutions >= 0) {
						newLimitDupSolutions -= retDuplicateSolutions;
					}
					
					
					retDuplicateSolutions += doDepthFirstSearch(paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth, newLimitDupSolutions, skipSymmetries, solutionResolver, cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid, topBottombridgeUsedNx1x1, debugNope, debugIterations);

					//Tear down (undo add of new cell)
					numCellsUsedDepth -= 1;
					

					
					paperUsed[new_i][new_j] = false;
					indexCuboidonPaper[new_i][new_j] = -1;
					paperToDevelop[numCellsUsedDepth] = null;

					indexCuboidOnPaper2ndCuboid[new_i][new_j] = -1;
					
					cuboid.removeCell(indexNewCell);
					cuboidToBringAlongStartRot.removeCell(indexNewCell2);
					
					//End tear down


				} // End recursive if cond
			} // End loop rotation
		} //End loop index

		return retDuplicateSolutions;
	}
	

	 
	/*https://www.sciencedirect.com/science/article/pii/S0925772117300160
	 * 
	 *  "From the necessary condition, the smallest possible surface area that can fold into two boxes is 22,
	 *   and the smallest possible surface area for three different boxes is 46.
	 *   (...) However, the area 46 is too huge to search. "
	 *  
	 *  Challenge accepted!
	 */

	public static void main(String args[]) {
		System.out.println("Fold Resolver Ordered Regions intersection skip symmetries Nx1x1:");

		
		//solveCuboidIntersections(new CuboidToFoldOn(13, 1, 1), new CuboidToFoldOn(3, 3, 3));
		
		solveCuboidIntersections(new CuboidToFoldOn(11, 1, 1), new CuboidToFoldOn(5, 3, 1));
		
		//solveCuboidIntersections(new CuboidToFoldOn(11, 1, 1), new CuboidToFoldOn(7, 2, 1));
		
		//solveCuboidIntersections(new CuboidToFoldOn(9, 1, 1), new CuboidToFoldOn(4, 3, 1));
		//It got 4469 solutions and it took about 41.5 hours
		
		//solveCuboidIntersections(new CuboidToFoldOn(8, 1, 1), new CuboidToFoldOn(5, 2, 1));
		//It got 35675 again, but this time it only took 3 hours! It took almost 2 days last time!
		
		//solveCuboidIntersections(new CuboidToFoldOn(7, 1, 1), new CuboidToFoldOn(3, 3, 1));
		////It got 1070 (again) (They got 1080, but I think they were wrong)
		
		//solveCuboidIntersections(new CuboidToFoldOn(5, 1, 1), new CuboidToFoldOn(3, 2, 1));
		//It got 2263!

		//solveCuboidIntersections(new CuboidToFoldOn(2, 1, 1), new CuboidToFoldOn(1, 2, 1));
		
		//Best 5,1,1: 3 minute 45 seconds (3014430 solutions) (December 27th)
		
		
		//Find non-trivial cuboid intersections:
		//solveCuboidIntersections(new CuboidToFoldOn(9, 1, 1), new CuboidToFoldOn(9, 1, 1));
		//Nx1x1: suprise intersections: (Not on OEIS :( )
		// I guess that the odds number are different from even?
		//1: 0
		//2: 72
		//3: 47
		//4: 204
		//5: 189
		//6: 372
		//7: 217
		//8: 1114
		//9: 495

		
		System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
		
		
		/* 5x1x1 on December 8th:
			Final number of unique solutions: 3014430
		//I hope it's right...
		//It took 6.5 hours.
		// Try to get it under 1 hour.
		 */
		
	}
	/*//TODO:
46	1 × 1 × 11, 1 × 2 × 7, 1 × 3 × 5
54	1 × 1 × 13, 1 × 3 × 6, 3 × 3 × 3
58	1 × 1 × 14, 1 × 2 × 9, 1 × 4 × 5
62	1 × 1 × 15, 1 × 3 × 7, 2 × 3 × 5
64	1 × 2 × 10, 2 × 2 × 7, 2 × 4 × 4
70	1 × 1 × 17, 1 × 2 × 11, 1 × 3 × 8, 1 × 5 × 5
88	1 × 2 × 14, 1 × 4 × 8, 2 × 2 × 10, 2 × 4 × 6
*/
	
}
