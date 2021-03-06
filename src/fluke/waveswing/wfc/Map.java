package fluke.waveswing.wfc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

import fluke.waveswing.Gui;
import fluke.waveswing.Main;

public class Map
{
	public Tile[][] map;
	public TileProbability[][] tileProbs;
	public int size;
	public static Random rand = new Random();
	public static Tile voidTile = new Tile(new int[] {0xFF000000,0xFF000000,0xFF000000,0xFF000000,0xFF000000,0xFF000000,0xFF000000,0xFF000000,0xFF000000}, 0);
	public static Tile fullTile = new Tile(new int[] {0xFF553311,0xFF553311,0xFF553311,0xFF553311,0xFF553311,0xFF553311,0xFF553311,0xFF553311,0xFF553311}, 250);
	
	public enum Dir
	{
		NORTH(new int[]{0,-1}),
		EAST(new int[]{1,0}),
		SOUTH(new int[]{0,1}),
		WEST(new int[]{-1,0});
		
		public final int[] cords;
		private Dir(int[] cords)
		{
			this.cords = cords;
		}
	}
	
	public Map(int size)
	{
//		addTileToSet(voidTile);
//		addTileToSet(fullTile);
		map = new Tile[size][size];
		tileProbs = new TileProbability[size][size];
		for(int x = 0; x < size; x++)
			for(int y = 0; y < size; y++)
				tileProbs[x][y] = new TileProbability();
			
		this.size = size;
		
//		voidEdges();
//		forceSetTile(5, 5, fullTile);
//		forceSetTile(8, 8, fullTile);
		setRandomTiles(4);
	}
	
	//add tile to set if it doesn't already exist
	public void addTileToSet(Tile t)
	{
		if(!Arrays.asList(Main.tileset.tiles).contains(t))
		{
			int tileSetSize = Main.tileset.tiles.length;
			Tile[] newset = new Tile[tileSetSize+1];
			System.arraycopy(Main.tileset.tiles, 0, newset, 0, tileSetSize);
			newset[tileSetSize] = t;
			Main.tileset.tiles = newset;
		}
	}
	
	//ensures numTiles unique locations are set. Does not pick tiles 2 or less to the edge of map
	public void setRandomTiles(int numTiles)
	{
		HashSet<int[]> cords = new HashSet<int[]>();
		
		while(cords.size() < numTiles)
		{
			int[] newCord = new int[] {rand.nextInt(Main.mapTileSize-4)+2, rand.nextInt(Main.mapTileSize-4)+2};
			cords.add(newCord);
		}
		
		for(int[] cord : cords)
		{
			setTile(cord[0], cord[1]);
			propagateChange(cord);
		}
	}
	
	//selects tile based on weight, adjusting the probabilities at that location
	public void setTile(int x, int y)
	{
		Tile t = tileProbs[x][y].getWeightedTile(true);
		map[x][y] = t;
	}
	
	public boolean allTilesSet()
	{
		for(int x = 0; x < size; x++)
			for(int y = 0; y < size; y++)
				if(map[x][y] == null)
					return false;
		return true;
	}

	public void collapseMap()
	{
		while(!allTilesSet())
		{
			collapseNext();		
		}
	}
	
	public boolean collapseNext()
	{
		int[] nextTileCords = getLowestEntropy();
		if(setTile(nextTileCords))
		{
			propagateChange(nextTileCords);
			return true;
		}
		else
			return false;
		
	}
	
	public boolean setTile(int[] cords)
	{
		Tile t = tileProbs[cords[0]][cords[1]].getWeightedTile(true);
		if(t != null)
		{
			map[cords[0]][cords[1]] = t;
			return true;
		}
		else
			return false;
	}
	
	public void propagateChange(int x, int y)
	{
		propagateChange(new int[] {x, y});
	}
	
	public void propagateChange(int[] startCords)
	{
		Stack<int[]> cords = new Stack<int[]>();
		cords.add(startCords);
		
		while(cords.size() > 0)
		{
			int[] currentCords = cords.pop();
			Tile[] possibleTiles = getTiles(currentCords);
			Dir[] validDirs = validDirections(currentCords);
			for(Dir d : validDirs)
			{
				int[] otherCords = new int[] {currentCords[0] + d.cords[0], currentCords[1] + d.cords[1]};
				if(isTileSet(otherCords))
					continue;
				
				Tile[] otherPossibleTiles = getTiles(otherCords);
				for(Tile otherTile : otherPossibleTiles)
				{
					boolean isPlaceable = false;
					for(Tile originalTile : possibleTiles)
					{
						if(isValidPlacement(originalTile, otherTile, d))
						{
							isPlaceable = true;
							break;
						}
					}
					
					if(!isPlaceable)
					{
						tileProbs[otherCords[0]][otherCords[1]].invalidateTile(otherTile);
						if(!cords.contains(otherCords))
							cords.add(otherCords); 
					}
				}
			}
			
			for(int x = 0; x < size; x++)
			{
				for(int y = 0; y < size; y++)
				{	
					boolean fuck = true;
					for(int prob : tileProbs[x][y].probs)
					{
						if(prob != 0)
							fuck = false;
					}
					if(fuck)
					{
						System.out.println("tile probabilities are in a bad state. sorry");
						Gui.giveTheFuckUp = true;
						Gui.reset = true;
						return;
					}
				}
			}
			
		}
	}
	
	//is tile able to be place. staring at t1, move in direction d and attempt to place t2
	public boolean isValidPlacement(Tile t1, Tile t2, Dir d)
	{
		switch(d)
		{
			case NORTH:
				return t1.data[0] == t2.data[6] && t1.data[1] == t2.data[7] && t1.data[2] == t2.data[8];
			case EAST:
				return t1.data[2] == t2.data[0] && t1.data[5] == t2.data[3] && t1.data[8] == t2.data[6];
			case SOUTH:
				return t1.data[6] == t2.data[0] && t1.data[7] == t2.data[1] && t1.data[8] == t2.data[2];
			case WEST:
				return t1.data[0] == t2.data[2] && t1.data[3] == t2.data[5] && t1.data[6] == t2.data[8];
			default:
				return false;
		}
	}

	//get valid directions for cords. ie don't move west when we are on left edge of map
	public Dir[] validDirections(int[] cords)
	{
		ArrayList<Dir> dirs = new ArrayList<Dir>();
		int x = cords[0];
		int y = cords[1];
		
		if(x > 0)
			dirs.add(Dir.WEST);
		if(x < size-1)
			dirs.add(Dir.EAST);
		if(y > 0)
			dirs.add(Dir.NORTH);
		if(y < size-1)
			dirs.add(Dir.SOUTH);
		
		return dirs.toArray(new Dir[0]);
	}
	
	//get valid tiles for cords
	public Tile[] getTiles(int[] cords)
	{
		int x = cords[0];
		int y = cords[1];
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		for(int n = 0; n < tileProbs[x][y].probs.length; n++)
		{
			if(tileProbs[x][y].probs[n] != 0)
				tiles.add(Main.tileset.tiles[n]);
		}
		return tiles.toArray(new Tile[0]);
	}

	//gets lowest entropy position on map with minor fuzzing for that extra rng flavor
	public int[] getLowestEntropy()
	{
		int[] cord = new int[2];
		double lowest = 999;
		for(int x = 0; x < size; x++)
		{
			for(int y = 0; y < size; y++)
			{
				if(map[x][y] == null)
				{
					double e = tileProbs[x][y].entropy - rand.nextFloat()/1000.0;
					if(e < lowest)
					{
						lowest = e;
						cord[0] = x;
						cord[1] = y;
					}
				}
			}
		}
		
		return cord;
	}
	
	//sets map edges to tiles fill with 0s
	public void voidEdges()
	{
		for(int x = 0; x < size; x++)
		{
			for(int y = 0; y < size; y++)
			{
				if(x == 0 || y == 0 || x == size-1 || y == size-1)
				{
					forceSetTile(x, y, voidTile);
				}
			}
		}	
	}
	
	//forces position to specific tile, updates probability map to match
	public void forceSetTile(int x, int y, Tile t)
	{
		map[x][y] = t;
		tileProbs[x][y].validateTile(t);
		propagateChange(x, y);
	}
	
	public boolean isTileSet(int[] cords)
	{
		return map[cords[0]][cords[1]] != null;
	}

}
