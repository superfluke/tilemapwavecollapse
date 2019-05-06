package fluke.waveswing;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
WALL = 0
DIRT = 1
FLOOR = 2
GRID = 3
GREY = 4
DEFAULT = 5
ROOF = 6
FLAG = 7
 */
public class JsonMap
{
	public static final int WALL = 0xFF808080;
	public static final int DIRT = 0xFF553311;
	public static final int WOOD = 0xFFAA6633;
	public static final int VOXEL_SIZE = 2;
	
	@SuppressWarnings("unchecked")
	public static void createJsonMap(BufferedImage img)
	{
		JSONObject mapout = new JSONObject();
    	mapout.put("name", "mcmap");
    	mapout.put("modURL", "");
    	mapout.put("ambient", 9937064);
    	mapout.put("light", 15923452);
    	mapout.put("sky", 14477549);
    	mapout.put("fog", 9280160);
    	mapout.put("fogD", 900);
    	mapout.put("camPos", getSizeArray(60, 60, 60));
    	mapout.put("spawns", new ArrayList<Integer>());
    	JSONArray objects = new JSONArray(); 
    	
    	for(int x = 0; x < img.getWidth(); x+=PaintCanvas.tileSize/3)
    	{
    		for(int y = 0; y < img.getHeight(); y+=PaintCanvas.tileSize/3)
    		{
    			int pixelColor = img.getRGB(x, y);
    			if(pixelColor != 0xFF000000)
				{
    				final Integer stupidFinalX = new Integer(x);
    				final Integer stupidFinalZ = new Integer(y);
    				
    				Map newthing = new LinkedHashMap(2);
    	    		ArrayList<Integer> krPos = new ArrayList<Integer>() {{
    	    			add(stupidFinalX*VOXEL_SIZE);
    	    			add(0);
    	    			add(stupidFinalZ*VOXEL_SIZE);
    	    		}};

    	    		int height = pixelColor == WALL ? 60 : 4;
    	    		newthing.put("p", krPos);
    	        	newthing.put("s", getSizeArray(height));
    	        	
    	        	if(pixelColor == DIRT)
    	        	{
    	        		newthing.put("t", 1);
    	        	}
    	        	else if(pixelColor == WOOD)
    	        	{
    	        		newthing.put("t", 2);
    	        	}
    	        	
    	        	
    	        	objects.add(newthing);
				}
    		}
    	}
    	
    	objects = addBorderWall(objects, img.getWidth()*VOXEL_SIZE, img.getHeight()*VOXEL_SIZE);
    	mapout.put("objects", objects);
    	System.out.println("Starting merge with " + objects.size() + " objects");
    	String mergedMap = JSmerge.mergeObjs(mapout);
    	JSONParser parser = new JSONParser();
    	try
		{
			mapout = (JSONObject) parser.parse(mergedMap);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
    	System.out.println("Finished merge with " + ((JSONArray)mapout.get("objects")).size() + " objects");
    	saveToFile(mergedMap);
    	StringSelection stringSelection = new StringSelection(mergedMap);
    	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    	clipboard.setContents(stringSelection, null);
    	System.out.println("Map copied to clipboard");
	}
	
	public static void saveToFile(String data)
	{
		PrintWriter pw = null;
		try {
			pw = new PrintWriter("krunkermap.txt");
			pw.write(data);
			System.out.println("Saved to file");
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
        {
            pw.flush(); 
            pw.close(); 
        }
	}
	
	@SuppressWarnings("serial")
	public static ArrayList<Integer> getSizeArray(int x, int y, int z)
	{
		return new ArrayList<Integer>() {{
			add(x);
			add(y);
			add(z);
		}};
	}
	
	public static ArrayList<Integer> getSizeArray(int height)
	{
		return getSizeArray(VOXEL_SIZE*(PaintCanvas.tileSize/3), height, VOXEL_SIZE*(PaintCanvas.tileSize/3));
	}
	
	public static JSONArray addBorderWall(JSONArray objs, int width, int height)
	{
		int wallSize = 10;
		for(int n = 0; n < 4; n++)
		{
			boolean isEastWest = n < 2;
			boolean isNearWall = n % 2 == 0;
			int wallOffset = (int) (wallSize * 1.5);
			final Integer posX = isEastWest ? width/2-wallSize : (isNearWall ? -wallOffset : width - wallSize/2);
			final Integer posZ = !isEastWest ? height/2-wallSize : (isNearWall ? -wallOffset : height - wallSize/2);
			int sizeX = isEastWest ? width : wallSize;
			int sizeZ = !isEastWest ? height : wallSize;
			
			Map newthing = new LinkedHashMap(2);
			ArrayList<Integer> krPos = new ArrayList<Integer>() {{
				add(posX);
				add(0);
				add(posZ);
			}};
	
			newthing.put("p", krPos);
	    	newthing.put("s", getSizeArray(sizeX, 60, sizeZ));
	    	objs.add(newthing);
		}
		
		return objs;
	}

}
