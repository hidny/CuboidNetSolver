package SolutionResolver;

import Coord.Coord2D;

public interface SolutionResolverInterface {

	
	public long resolveSolution(Model.CuboidToFoldOn cuboidDimensionsAndNeighbours, Coord2D paperToDevelop[], int indexCuboidonPaper[][][], boolean paperUsed[][]);
	
	
	//TODO: actually use this for all classes instead of doing the weird spooky action at a distance:
	public long getNumUniqueFound();
}
