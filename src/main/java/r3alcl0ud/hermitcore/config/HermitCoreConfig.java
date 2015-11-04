package r3alcl0ud.hermitcore.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraft.item.Item;

import java.io.File;

import r3alcl0ud.hermitcore.utils.HELogger;

public final class HermitCoreConfig 
{
	public static boolean enableDebugLog;
	public static String toDelete[];

	
	public static void init(File configFile)
	{
		Configuration config = new Configuration(configFile);
		
		try
		{
			config.load();
			
			
			enableDebugLog = config.getBoolean("debugLogging", "misc", true, "Enable a more verbose debug logging");
		
			toDelete = config.get("removeItems", "recipes", new String[] {"minecraft:iron_hoe",  "minecraft:iron_pickaxe"}, "Put Items to remove crafting recipes here. Format: modname:itemname").getStringList();
			HELogger.logInfo("Loaded configuration file.");
		}
		catch (Exception e)
		{
			HELogger.logFatal("Caught exception while loading config file!");
			e.printStackTrace();
		}
		finally
		{
			if(config.hasChanged())
			{
				config.save();
			}
		}
	}
}