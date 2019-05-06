package fluke.waveswing.wfc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import javax.imageio.ImageIO;

import fluke.waveswing.Main;

public class TileSet
{
	public Tile[] tiles;
	public String currentSet = "";
	private HashSet<Tile> hashtiles = new HashSet<Tile>();
	
	public TileSet()
	{
	}
	
	public void loadFromFile(String filename)
	{	
		BufferedImage img = null;
		currentSet = filename;
		hashtiles = new HashSet<Tile>();
		if(!filename.contains("\\")) //if it isn't being loaded from a user file
		{
			InputStream input = this.getClass().getClassLoader().getResourceAsStream(filename);
			try
			{
				img = ImageIO.read(input);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				img = ImageIO.read(new File(filename)); 
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		int count = 0;
		for(int x = 0; x < 15; x++)
		{
			for(int y = 0; y < 15; y++)
			{
				if(img.getRGB(x*5, y*5) != 0xFFFFFFFF) //white
				{
					boolean ignoreTile = img.getRGB(x*5+3, y*5+3)!=0xFFFFFFFF; //if bottom right pixel is not white ignore this tile
					if(!ignoreTile)
					{
						int[] tileColors = new int[9];
						img.getRGB(x*5, y*5, 3, 3, tileColors, 0, 3); //get 3x3 array of main colors
						boolean addRotations = img.getRGB(x*5+3, y*5)!=0xFFFFFFFF; //check if x+3 pixel is set for adding rotations
						int weight = getRed(img.getRGB(x*5, y*5+3)); //use y+3 red value for weight
						addTile(new Tile(tileColors, weight), addRotations);
						count = addRotations? count+4 : count+1;
					}
				}
			}
		}
		
		finalizeSet();
		System.out.println("Attempted to load " + count + " tiles from file");
		System.out.println(tiles.length + " tiles in final set");
	}
	
	public int getRed(int rgb)
	{
		return (rgb >> 16) & 0xFF;
	}
	
	public int getGreen(int rgb)
	{
		return (rgb >> 8) & 0xFF;
	}
	
	public int getBlue(int rgb)
	{
		return rgb & 0xFF;
	}
	
	public void addRandomTiles(int size)
	{
		hashtiles = new HashSet<Tile>();
		for(int n = 0; n < size; n++)
			addTile(new Tile(), true);
		tiles = hashtiles.toArray(new Tile[0]);
	}
	
	public void addTile(Tile t, boolean addRotations)
	{
		if(addRotations)
		{
			int[] rotData = t.data.clone();
			for(int rot = 0; rot < 4; rot++)
			{
				rotData = rotArray(rotData);
				hashtiles.add(new Tile(rotData, t.weight));
			}
		}
		else
		{
			hashtiles.add(t);
		}
	}
	
	public void removeTileFromSet(int tileIndex)
	{
		int tileSetSize = tiles.length;
		Tile[] newset = new Tile[tileSetSize-1];
		int newctr = 0;
		for(int n = 0; n < tiles.length; n++)
		{
			if(n != tileIndex)
			{
				newset[newctr++] = tiles[n];				
			}
		}
		Main.tileset.tiles = newset;
	}
	
	public void finalizeSet()
	{
		tiles = null;
		tiles = hashtiles.toArray(new Tile[0]);
	}
	
	//rotates 1d array by 90 degrees as if it was a 3x3
	public int[] rotArray(int[] in)
	{
		int[] out = new int[9];
		for(int x = 0; x < 3; x++)
			for(int y = 0; y < 3; y++)
				out[convert2d(2-y, x)] = in[convert2d(x, y)];
		return out;
	}
	
	//returns 1d index of elements pretending to be 2d 3x3 array
	public int convert2d(int x, int y)
	{
		return x + y * 3;
	}

	public boolean isCurrentTileset(String tilesetName)
	{
		return currentSet.equals(tilesetName);
	}
}
