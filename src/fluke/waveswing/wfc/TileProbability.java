package fluke.waveswing.wfc;

import java.util.Random;

import fluke.waveswing.Main;

public class TileProbability
{
	public int[] probs;
	public double entropy;
	public static Random rand = new Random();
	
	public TileProbability()
	{
		probs = new int[Main.tileset.tiles.length];
		for(int n = 0; n < Main.tileset.tiles.length; n++)
			probs[n] = Main.tileset.tiles[n].weight;
		updateEntropy();
	}
	
	public double getEntropy()
	{
		return entropy;
	}
	
	public void updateEntropy()
	{
		double sum = 0;
		double logSum = 0;
		for(int weight : probs)
		{
			if(weight != 0)
			{
				sum += weight;
				logSum += weight * Math.log(weight);
			}
		}
		
		entropy = Math.log(sum) - (logSum / sum);
	}
	
	public Tile getWeightedTile(boolean adjustProbs)
	{
		int sum = 0;
		for(int weight : probs)
			sum += weight;
		
		if(sum <= 0)
		{
			System.out.println("couldn't find a valid tile :[");
			return null;
		}
		
		int r = rand.nextInt(sum) + 1;
		int count = 0;
		Tile out = null;
		int tileIndex;
		for(tileIndex = 0; tileIndex < probs.length; tileIndex++)
		{
			count += probs[tileIndex];
			if(count >= r)
			{
				out = Main.tileset.tiles[tileIndex];
				break;
			}
		}
		
		if(adjustProbs)
		{
			for(int n = 0; n < probs.length; n++)
			{
				if(n != tileIndex)
					probs[n] = 0;
			}
			updateEntropy();
		}
		return out; 
	}
	
	//set weight of passed in tile to 0
	public void invalidateTile(Tile otherTile)
	{
		for(int n = 0; n < probs.length; n++)
		{
			if(Main.tileset.tiles[n].equals(otherTile))
			{
				probs[n] = 0;
				updateEntropy();
				break;
			}
		}	
	}
	
	//set weight of all tiles except passed in tile to 0
	public void validateTile(Tile otherTile)
	{
		for(int n = 0; n < probs.length; n++)
		{
			if(!Main.tileset.tiles[n].equals(otherTile))
			{
				probs[n] = 0;
			}
			else
			{
				probs[n] = 1;
			}
			updateEntropy();
		}
	}
}
