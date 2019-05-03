package fluke.waveswing;

import fluke.waveswing.wfc.Map;
import fluke.waveswing.wfc.TileSet;

public class Main 
{
	
	
	public static int tileSetAttempts = 14;
	public static int tileSetSize;
	public static int mapTileSize = 16;
	public static TileSet tileset;
	public static Map wfc; 

	static
	{
		reset();
	}
	
	public static void main(String[] args) throws InterruptedException 
	{
		new Gui();
	}
	
	public static void reset()
	{
		tileset = new TileSet();
		tileset.loadFromFile("tileset.png");
//		tileset.addRandomTiles(tileSetAttempts);
//		tileset.finalizeSet();
		wfc = new Map(mapTileSize, tileset);
	}


}
