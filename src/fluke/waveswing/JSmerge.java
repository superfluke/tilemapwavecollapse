package fluke.waveswing;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.json.simple.JSONObject;

public class JSmerge
{
	public static String mergeObjs(JSONObject objs)
	{
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		// read script file
		try
		{
			engine.eval(Files.newBufferedReader(Paths.get("bobmerge.txt"), StandardCharsets.UTF_8));
		} catch (ScriptException | IOException e)
		{
			e.printStackTrace();
		}

		Invocable inv = (Invocable) engine;
		// call function from script file
		String merged = null;
		try
		{
			merged = (String) inv.invokeFunction("MergeVox", objs.toString());
		} catch (NoSuchMethodException | ScriptException e)
		{
			e.printStackTrace();
		}
		return merged;
	}
}
