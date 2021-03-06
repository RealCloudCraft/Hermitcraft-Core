package hermitcore;

import hermitcore.common.GUIEventHandler;
import hermitcore.common.IProxy;
import hermitcore.config.CustomRecordParser;
import hermitcore.config.HermitCoreConfig;
import hermitcore.gameObjs.ObjectHandler;
import hermitcore.gui.GuiHandler;
import hermitcore.library.HermitRegistry;
import hermitcore.library.HermitTabs;
import hermitcore.library.crafting.LiquidCasting;
import hermitcore.network.PacketHandler;
import hermitcore.network.PacketTile;
import hermitcore.tcon.smeltery.HermitSmeltery;
import hermitcore.tcon.tools.HermitTools;
import hermitcore.utils.helper.MusicDownloader;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import mantle.pulsar.config.ForgeCFG;
import mantle.pulsar.config.IConfiguration;
import mantle.pulsar.control.PulseManager;
import moze_intel.projecte.emc.NormalizedSimpleStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@SuppressWarnings("unused")
@Mod(modid = HECore.MODID, name = HECore.MODNAME, version = "${version}", dependencies = "required-after:TConstruct@[1.7.10-1.8.7,);after:ForgeMultipart@[1.1.1.321,);after:*")
public class HECore
{
	public static final String MODID = "hermitcore";
	public static final String MODNAME = "Hermitcraft Core";
	public static final String modVersion = "${version}";

	public static final GuiHandler guiHandler = new GuiHandler();

	public static Random random = new Random();

	public static File CONFIG_DIR;
	public static File ASSETS_DIR;
	public static File MUSIC_DIR;

	@Instance(MODID)
	public static HECore instance;

	@SidedProxy(clientSide = "hermitcore.common.ClientProxy", serverSide = "hermitcore.common.ServerProxy")
	public static IProxy proxy;

	private IConfiguration pulseCFG;
	public static PulseManager pulsar;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		CONFIG_DIR = new File(event.getModConfigurationDirectory(), "HermitCore");
		ASSETS_DIR = new File(event.getModConfigurationDirectory(), "HermitCore/Assets");
		MUSIC_DIR = new File(event.getModConfigurationDirectory(), "HermitCore/Assets/Music");

		if (!CONFIG_DIR.exists())
		{
			CONFIG_DIR.mkdirs();
		}
		if (!ASSETS_DIR.exists())
		{
			ASSETS_DIR.mkdirs();
		}
		if (!MUSIC_DIR.exists())
		{
			MUSIC_DIR.mkdirs();
		}

		pulseCFG = new PulsarCFG(HermitCoreConfig.configFile("Modules.cfg"),
				"Tinker's Construct Addon: Hermitcraft Core Addon for Tinkers Construct");
		pulseCFG.load();
		pulsar = new PulseManager(MODID, pulseCFG);

		PacketHandler.instance.init();

		HermitCoreConfig.init(new File(CONFIG_DIR, "HECore.cfg"));

		CustomRecordParser.init();

		HermitRegistry.recordTab = new HermitTabs("HermitcoreRecords");

		for (int i = 0; i < (HermitCoreConfig.toDelete.length); i++)
		{
			ObjectHandler.removeRecipes(HermitCoreConfig.toDelete[i]);
		}

		ObjectHandler.register();
		ObjectHandler.addRecipes();

		pulsar.registerPulse(new HermitSmeltery());
		pulsar.registerPulse(new HermitTools());

		tableCasting = new LiquidCasting();
		basinCasting = new LiquidCasting();

		pulsar.preInit(event);

	}

	@EventHandler
	public void Init(FMLInitializationEvent event)
	{
		// GUI stuff
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, guiHandler);

		// Block model stuff
		proxy.registerRenderers();

		// Packets
		PacketTile.init();

		pulsar.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

		PacketHandler.instance.postInit();

		if (FMLCommonHandler.instance().getSide().isClient())
		{
			MinecraftForge.EVENT_BUS.register(new GUIEventHandler((Minecraft.getMinecraft())));
		}

		pulsar.postInit(event);
	}

	/**
	 * Downloads missing music files
	 * 
	 * @param event
	 *            Server instance
	 */
	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{

		System.out.println(Minecraft.getMinecraft().toString());

		for (int i = 0; i < HermitCoreConfig.recordName.length; i++)
		{
			// String fileURL = "http://" + event.getServer() + "/" +
			// HermitCoreConfig.recordName[i] + ".ogg"; //Left out, for now
			// convert the music file to a .ogg and put it on dropbox or
			// somthing
			String fileURL = HermitCoreConfig.recordURL[i];
			String saveDir = HECore.MUSIC_DIR.toString();
			try
			{
				MusicDownloader.downloadFile(fileURL, saveDir);
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}

	}

	public static LiquidCasting getTableCasting()
	{
		return tableCasting;
	}

	public static LiquidCasting getBasinCasting()
	{
		return basinCasting;
	}

	public static LiquidCasting tableCasting;
	public static LiquidCasting basinCasting;

}
