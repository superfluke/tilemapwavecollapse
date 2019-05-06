package fluke.waveswing.wfc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import fluke.waveswing.Gui;
import fluke.waveswing.Main;

public class PaintTileEditor extends JPanel 
{
	private static final long serialVersionUID = 2L;
	public static int tileSize = 30;

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//g.fillRect(0, 0, 50, 50);
		Tile t = Main.tileset.tiles[Gui.tileEditorIndex];
		int tileX = Gui.tileEditorWidth/2 - tileSize/2;
		int yOffset = 25;
		drawTile(g, t, tileX, yOffset);
		g.setColor(Color.BLACK);
		g.setFont(new Font("sans", Font.PLAIN, 16));
		g.drawString("Tile " + Gui.tileEditorIndex, 4, yOffset*2+tileSize);	
		g.drawString("Weight: " + t.weight, 4, yOffset*2+tileSize+16);	
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
}
