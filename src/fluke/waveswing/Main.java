package fluke.waveswing;

import fluke.waveswing.wfc.Map;
import fluke.waveswing.wfc.TileSet;

public class Main 
{
	
	
	public static int tileSetAttempts = 14;
	public static int mapTileSize = 16;
	public static TileSet tileset = new TileSet();
	public static Map wfc; 

	static
	{
		reset("tileset.png");
	}
	
	public static void main(String[] args) throws InterruptedException 
	{
		new Gui();
	}
	
	public static void reset(String tilesetName)
	{
		if(!tileset.isCurrentTileset(tilesetName))
		{
			tileset.loadFromFile(tilesetName);
		}
		wfc = new Map(mapTileSize);
	}


}
