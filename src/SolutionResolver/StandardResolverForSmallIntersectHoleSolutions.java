package SolutionResolver;

import Model.CuboidToFoldOn;
import Model.Utils;

import java.util.LinkedList;
import java.util.Queue;

import Coord.Coord2D;
import DupRemover.BasicUniqueCheckImproved;
import DupRemover.UniqueCheckRecordCuts;

public class StandardResolverForSmallIntersectHoleSolutions implements SolutionResolverInterface {

	private long numUniqueFound = 0;
	private long numFound = 0;
	
	public StandardResolverForSmallIntersectHoleSolutions() {
		
		
	}
	
	@Override
	public long resolveSolution(CuboidToFoldOn cuboidDimensionsAndNeighbours, Coord2D[] paperToDevelop,
			int[][][] indexCuboidonPaper, boolean[][] paperUsed) {	

		if(numFound % 10000 == 0) {
			System.out.println(numFound +
				" (num unique: " + numUniqueFound + ")");
		}
		numFound++;
		

		if(hasHole(paperUsed)
				//&& BasicUniqueCheckImproved.isUnique(paperToDevelop, paperUsed)
				&& UniqueCheckRecordCuts.isUnique(paperToDevelop, indexCuboidonPaper[0], paperUsed, cuboidDimensionsAndNeighbours)
			) {
			numUniqueFound++;

			System.out.println("----");
			System.out.println("Unique solution WITH HOLE found:");
			Utils.printFold(paperUsed);
			System.out.println("Shape 1:");
			Utils.printFoldWithIndex(indexCuboidonPaper[0]);
			System.out.println("Shape 2:");
			Utils.printFoldWithIndex(indexCuboidonPaper[1]);
			
			System.out.println("Solution code: " + BasicUniqueCheckImproved.debugLastScore);
			System.out.println("Num unique solutions found: " + 
					numUniqueFound);

			

			return 1L;
		} else {

			//System.out.println("Solution not found");
			return 0L;
		}
	}
	
	//Reminder: Corners touching = touching
	private boolean hasHole(boolean[][] paperUsed) {
		
		int initI=-1;
		int initJ=-1;
		
		for(int i=0; i<paperUsed.length; i++) {
			for(int j=0; j<paperUsed[0].length; j++) {
				if( ! paperUsed[i][j]) {
					initI = i;
					initJ = j;
					break;
				}
			}
		}

		boolean found[][] = new boolean[paperUsed.length][paperUsed[0].length];
		
		for(int i=0; i<paperUsed.length; i++) {
			for(int j=0; j<paperUsed[0].length; j++) {
				found[i][j] = false;
			}
		}
		found[initI][initJ] = true;
		
		LinkedList<String> queue = new LinkedList<String>();
		
		queue.add(initI + "," + initJ);
		
		while(queue.isEmpty() == false) {
			
			String next = queue.remove();
			int icur = Integer.parseInt(next.split(",")[0]);
			int jcur = Integer.parseInt(next.split(",")[1]);
			
			for(int i=Math.max(0, icur-1); i<= Math.min(found.length - 1, icur+1); i++) {

				for(int j=Math.max(0, jcur-1); j<= Math.min(found[0].length - 1, jcur+1); j++) {
				
					if(! paperUsed[i][j] && !found[i][j]) {
						found[i][j] = true;
						queue.add(i + "," + j);
					}
				}
			}
			
		}
		
		for(int i=0; i<paperUsed.length; i++) {
			for(int j=0; j<paperUsed[0].length; j++) {
				if(! paperUsed[i][j] && ! found[i][j]) {
					return true;
				}
			}
		}
		
		return false;
	}

	public long getNumUniqueFound() {
		return numUniqueFound;
	}


	
}
