package fluke.waveswing.wfc;

import java.util.HashSet;

import fluke.waveswing.Main;

public class TileSet
{
	public Tile[] tiles;
	
	public TileSet(int size)
	{
		HashSet<Tile> hashtiles = new HashSet<Tile>();
		for(int n = 0; n < size; n++)
			hashtiles.add(new Tile());
		Main.tileSetSize = hashtiles.size();
		tiles = hashtiles.toArray(new Tile[0]);
	}
}
