package fluke.waveswing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import fluke.waveswing.wfc.Tile;

public class PaintCanvas extends JPanel 
{
	private static final long serialVersionUID = 1L;
	public static int tileSize = 30;
	public static int border = 4;
	public static boolean setup = true;
	public static BufferedImage paintImage = new BufferedImage(Gui.canvasWidth, Gui.canvasHeight, BufferedImage.TYPE_4BYTE_ABGR);
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics draw = paintImage.createGraphics();
		//map border
		draw.fillRect(0, 0, Gui.canvasWidth, Gui.canvasHeight);
		draw.setColor(Color.GRAY);
		draw.fillRect(0, 0, border, tileSize*Main.mapTileSize+border);
		draw.fillRect(0, 0, tileSize*Main.mapTileSize+border, border);
		draw.fillRect(tileSize*Main.mapTileSize+border, 0, border, tileSize*Main.mapTileSize+border*2);
		draw.fillRect(0, tileSize*Main.mapTileSize+border, tileSize*Main.mapTileSize+border*2, border);
		
		printTileset(draw);
		printMap(draw);
		//outline(draw);
		g.drawImage(paintImage, 0, 0, null);
		draw.dispose();
	}
	
	public void drawTile(Graphics g, Tile t, int x, int y)
	{
		for(int n = 0; n < t.data.length; n++)
		{
			int subX = (tileSize/3) * (n%3);
			int subY = (tileSize/3) * (n/3);
			g.setColor(new Color(t.data[n]));
			g.fillRect(x+subX, y+subY, tileSize/3, tileSize/3);
		}

	}
	
	public void printTileset(Graphics g)
	{
		for(int n = 0; n < Main.tileset.tiles.length; n++)
		{
			Tile t = Main.tileset.tiles[n];
			int tilesPerLine = (Gui.canvasWidth - (tileSize*Main.mapTileSize+border*4)) / (tileSize*3);
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
	
	public static int getTileIndexClicked(int clickX, int clickY)
	{
		int tilesPerLine = (Gui.canvasWidth - (tileSize*Main.mapTileSize+border*4)) / (tileSize*3);
		int startX = tileSize*Main.mapTileSize+border*4;
		int startY = border;
		if(clickX < startX || clickY < startY || clickX > (startX+tileSize*3*tilesPerLine))
			return -1;

		int x = (clickX - startX) / (tileSize*3);
		int y = (clickY - startY) / (tileSize*2);
		int index = x + y*tilesPerLine;
		if(index >= Main.tileset.tiles.length)
			return -1;
		else
			return index;
	}

}
