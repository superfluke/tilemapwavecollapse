package fluke.waveswing.wfc;

import java.util.Arrays;
import java.util.Random;

public class Tile
{
	public static boolean nerfVoidTile = false;
	private static Random rand = new Random();
	public static int numColors = 2;
	@Override
	public int hashCode()
	{
		return Arrays.hashCode(data);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}

	public int[] data = {rand.nextInt(numColors), rand.nextInt(numColors), rand.nextInt(numColors), rand.nextInt(numColors)};
	public int weight = rand.nextInt(6)+5;
	
	public Tile()
	{
		if(nerfVoidTile)
			if(this.equals(Map.voidTile))
				this.weight = 1;
	}
	
	public Tile(int[] data, int weight)
	{
		this.data = data;
		this.weight = weight;
	}
	
}
