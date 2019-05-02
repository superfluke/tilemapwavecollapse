package fluke.waveswing;

import fluke.waveswing.wfc.Map;
import fluke.waveswing.wfc.TileSet;

public class Main 
{
	
	
	public static int tileSetAttempts = 20;
	public static int tileSetSize;
	public static int mapTileSize = 16;
	public static TileSet tileset = new TileSet(tileSetAttempts);
	public static Map wfc = new Map(mapTileSize, tileset);


	public static void main(String[] args) throws InterruptedException 
	{
		new Gui();
	}


}
