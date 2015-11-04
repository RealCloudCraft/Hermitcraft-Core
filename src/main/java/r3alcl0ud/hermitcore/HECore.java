package r3alcl0ud.hermitcore;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import r3alcl0ud.hermitcore.common.ServerProxy;
import r3alcl0ud.hermitcore.config.HermitCoreConfig;
import r3alcl0ud.hermitcore.gameObjs.ObjHandler;
import r3alcl0ud.hermitcore.common.IProxy;

import java.io.File;

@Mod(modid = HECore.MODID, name = HECore.MODNAME, version = HECore.$version)
public class HECore {
	public static final String MODID = "HermitcraftCore";
	public static final String MODNAME = "Hermitcraft Core";
	public static final String $version = "1.0.1";

	public static File CONFIG_DIR;

	@Instance(MODID)
	public static HECore instance;

	@SidedProxy(clientSide = "r3alcl0ud.hermitcore.common.ClientProxy", serverSide = "r3alcl0ud.hermitcore.common.ServerProxy")
	public static IProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		CONFIG_DIR = new File(event.getModConfigurationDirectory(),
				"HermitCore");

		if (!CONFIG_DIR.exists()) {
			CONFIG_DIR.mkdirs();
		}
		HermitCoreConfig.init(new File(CONFIG_DIR, "HECore.cfg"));

		
		for (int i = 0; i < (HermitCoreConfig.toDelete.length); i++)
		{
		ObjHandler.removeRecipes(HermitCoreConfig.toDelete[i]);
		}
	}

	@EventHandler
	public void load(FMLPreInitializationEvent event) {

	}

	@EventHandler
	public void postInit(FMLPreInitializationEvent event) {

	}

}