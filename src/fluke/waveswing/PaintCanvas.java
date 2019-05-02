package fluke.waveswing;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import fluke.waveswing.wfc.Tile;

public class PaintCanvas extends JPanel 
{
	private static final long serialVersionUID = 1L;
	public static int tileSize = 28;
	public static int border = 4;
	public static boolean setup = true;
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//map border
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, border, tileSize*Main.mapTileSize+border);
		g.fillRect(0, 0, tileSize*Main.mapTileSize+border, border);
		g.fillRect(tileSize*Main.mapTileSize+border, 0, border, tileSize*Main.mapTileSize+border*2);
		g.fillRect(0, tileSize*Main.mapTileSize+border, tileSize*Main.mapTileSize+border*2, border);
		
		printTileset(g);
		printMap(g);
		outline(g);
	}
	
	public void drawTile(Graphics g, Tile t, int x, int y)
	{
		for(int n = 0; n < t.data.length; n++)
		{
			int subX = (tileSize/2) * (n%2);
			int subY = (tileSize/2) * (n/2);
			switch(t.data[n])
			{
				case 0:
					g.setColor(Color.BLUE);
					break;
				case 1:
					g.setColor(Color.RED);
					break;
				case 2:
					g.setColor(Color.CYAN);
					break;
				case 3:
					g.setColor(Color.GREEN);
					break;
				case 9:
					g.setColor(Color.BLACK);
				default:
			}
			g.fillRect(x+subX, y+subY, tileSize/2, tileSize/2);
		}

	}
	
	public void printTileset(Graphics g)
	{
		for(int n = 0; n < Main.tileset.tiles.length; n++)
		{
			Tile t = Main.tileset.tiles[n];
			int tilesPerLine = Gui.canvasWidth = (tileSize*Main.mapTileSize+border*4) / (tileSize*4);
			//int tilesPerLine = 4;
			int x = tileSize*Main.mapTileSize+border*4+tileSize*3*(n%tilesPerLine);
			int y = border+tileSize*2*(n/tilesPerLine);
			drawTile(g, t, x, y);
			g.setColor(Color.BLACK);
			g.drawString(Integer.toString(t.weight), x+tileSize+border, y+10);	
		}
	}
	
	public void printMap(Graphics g)
	{
		for(int mx = 0; mx < Main.mapTileSize; mx++)
		{
			for(int my = 0; my < Main.mapTileSize; my++)
			{
				Tile t = Main.wfc.map[mx][my];
				if(t != null)
					drawTile(g, t, mx*tileSize+border, my*tileSize+border);
			}
		}
	}
	
	public void outline(Graphics g)
	{
		for(int n = 0; n < Main.mapTileSize+1; n++)
		{
			g.setColor(Color.WHITE);
			g.fillRect(n*tileSize+border, border, 1, Main.mapTileSize*tileSize);
			g.fillRect(border, n*tileSize+border, Main.mapTileSize*tileSize, 1);

		}
	}

}
