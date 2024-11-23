package DupRemover;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

import Model.CuboidToFoldOn;
import Model.Utils;
import Coord.Coord2D;
import Coord.CoordWithRotationAndIndex;

public class UniqueCheckRecordCuts {


	public static int NUM_REFLECTIONS = 2;
	public static int NUM_ROTATIONS = 4;
	public static int NUM_SYMMETRIES = NUM_REFLECTIONS*NUM_ROTATIONS;
	
	public static int CHECK_SYMMETRIES_ONE_DIM_FACTOR = 2;

	public static int MAX_WIDTH_PLUS_ONE = 256;

	public static HashMap<BigInteger, BigInteger> uniqListNoCut = new HashMap<BigInteger, BigInteger>();
	public static HashSet<BigInteger> uniqList = new HashSet<BigInteger>();

	public static BigInteger debugLastScore = null;
	
	public static boolean isUnique(Coord2D paperToDevelop[], int arrayIndex[][], boolean array[][], CuboidToFoldOn cuboid) {

		int borders[] = Utils.getBorders(paperToDevelop);
		
		int firsti = borders[0];
		int lasti = borders[1];
		int firstj = borders[2];
		int lastj = borders[3];
		
		
		int heightShape = lasti - firsti + 1;
		int widthShape = lastj - firstj + 1;
		
		
		int arrayIndexAdjusted[][] = new int[heightShape][widthShape];
		boolean arrayAdjusted[][] = new boolean[heightShape][widthShape];
		
		
		for(int i=0; i<arrayIndexAdjusted.length; i++) {
			for(int j=0; j<arrayIndexAdjusted[0].length; j++) {
				
				arrayIndexAdjusted[i][j] = arrayIndex[firsti + i][firstj + j];
				arrayAdjusted[i][j] = array[firsti + i][firstj + j];
				
			}
		}
		

		//TODO: should this be highest?
		BigInteger lowestScore = BigInteger.ZERO;
		BigInteger lowestScoreNoCut = BigInteger.ZERO;
		
		BigInteger	scores[][] = new BigInteger[NUM_SYMMETRIES][2];
		for(int i=0; i<NUM_SYMMETRIES; i++) {
			scores[i] = getScoreRotatedAndFlipped(i, arrayIndexAdjusted, arrayAdjusted, cuboid);
			//System.out.println("i = " + i + ": " + scores[i][0]);
			
			if(i==0 || lowestScore.compareTo(scores[i][1]) > 0 ) {
				lowestScoreNoCut = scores[i][0];
				lowestScore = scores[i][1];
			}
		}
		
		//TODO: for sanity test:
		BasicUniqueCheckImproved.isUnique(paperToDevelop, array);
		
		if(lowestScoreNoCut.compareTo(BasicUniqueCheckImproved.debugLastScore) != 0) {
			System.out.println("oops!");
			System.out.println(lowestScoreNoCut);
			System.out.println("vs");
			System.out.println(BasicUniqueCheckImproved.debugLastScore);
			System.exit(1);
		}
		
		if(! uniqList.contains(lowestScore)) {
			uniqList.add(lowestScore);
			
			if(! uniqListNoCut.containsKey(lowestScoreNoCut)) {
				uniqListNoCut.put(lowestScoreNoCut, lowestScore);
				
			} else {
				System.out.println("BINGO:");
				
				System.out.println(lowestScore);
				System.out.println("vs:");
				System.out.println(uniqListNoCut.get(lowestScoreNoCut));
				
				//System.exit(1);
				
			}
			
			//System.out.println("Max number: " + max);
			
			return true;
		} else {
			return false;
		}
		
		
	}
	
	

	public static BigInteger[] getScoreRotatedAndFlipped(int symmetryIndex, int arrayIndex[][], boolean array[][], CuboidToFoldOn cuboid) {
		

		boolean isVertFlip = (symmetryIndex & 4) == 0;
		boolean isHoriFlipped = (symmetryIndex & 2) == 0;
		boolean isFlipped = (symmetryIndex & 1) == 0;

		int arrayIndexAdjusted[][];
		boolean arrayAdjusted[][];
		
		if( ! isFlipped) {
			arrayIndexAdjusted = new int[arrayIndex.length][arrayIndex[0].length];
			arrayAdjusted = new boolean[arrayIndex.length][arrayIndex[0].length];
		} else {
			arrayIndexAdjusted = new int[arrayIndex[0].length][arrayIndex.length];
			arrayAdjusted = new boolean[arrayIndex[0].length][arrayIndex.length];
		}
		
		
		for(int i=0; i<arrayIndexAdjusted.length; i++) {
			for(int j=0; j<arrayIndexAdjusted[0].length; j++) {

				int iAlt = arrayIndexAdjusted.length - 1 - i;
				int jAlt = arrayIndexAdjusted[0].length - 1 - j;
				
				int iToUse = i;
				int jToUse = j;
				
				if(isVertFlip) {
					iToUse = iAlt;
				}
				if(isHoriFlipped) {
					jToUse = jAlt;
				}
				
				if( ! isFlipped) {
					arrayIndexAdjusted[i][j] = arrayIndex[iToUse][jToUse];
					arrayAdjusted[i][j] = array[iToUse][jToUse];
					
				} else {
					arrayIndexAdjusted[i][j] = arrayIndex[jToUse][iToUse];
					arrayAdjusted[i][j] = array[jToUse][iToUse];
					
				}
				
			}
		}
		
		
		
		return getScoresSimple(arrayIndexAdjusted, arrayAdjusted, cuboid);
	}
	
	public static BigInteger[] getScoresSimple(int arrayIndex[][], boolean array[][], CuboidToFoldOn cuboid) {


		BigInteger TWO = new BigInteger("2");
		
		BigInteger score = BigInteger.ZERO;
		
		//3 * 256^2 fixes a possible hash collision
		// I made it 3 instead of 1 because in future, I want placement of first and second binary 1 to mean something
		//It does mean something in the CuboidSimplePhaseNetSearch, but not here.
		score = new BigInteger((3 * MAX_WIDTH_PLUS_ONE * MAX_WIDTH_PLUS_ONE + array.length * MAX_WIDTH_PLUS_ONE + array[0].length) + "");

		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				score = score.multiply(TWO);
				
				if(array[i][j]) {
					score = score.add(BigInteger.ONE);
				}
			}
		}
		
		
		BigInteger output[] = new BigInteger[2];
		output[0] = score;

		//TODO: handle cuts here:
		
		//Vertical cuts:
		for(int i=0; i<array.length -1; i++) {
			for(int j=0; j<array[0].length; j++) {
				score = score.multiply(TWO);
				
				if(array[i][j] && array[i+1][j]) {
					
					int topIndex = arrayIndex[i][j];
					int bottomIndex = arrayIndex[i+1][j];
					
					if(areIndexesAttached(topIndex, bottomIndex, cuboid) == false) {
						score = score.add(BigInteger.ONE);
					}
				}
			}
		}
		
		//Horizontal cuts:
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length - 1; j++) {
				score = score.multiply(TWO);
				
				if(array[i][j] && array[i][j + 1]) {

					int leftIndex = arrayIndex[i][j];
					int rightIndex = arrayIndex[i][j+1];
					
					
					if(areIndexesAttached(leftIndex, rightIndex, cuboid) == false) {
						score = score.add(BigInteger.ONE);
					}
				}
			}
		}
		
		
		output[1] = score;
		return output;
	}
	
	
	public static boolean areIndexesAttached(int index1, int index2, CuboidToFoldOn cuboid) {
		CoordWithRotationAndIndex neighbourIndexes[] = cuboid.getNeighbours(index1);
		
		boolean indexesAttached = false;
		for(int k=0; k<neighbourIndexes.length; k++) {
			if(neighbourIndexes[k].getIndex() == index2) {
				indexesAttached = true;
				break;
			}
		}
		
		return indexesAttached;
	}
	
}
