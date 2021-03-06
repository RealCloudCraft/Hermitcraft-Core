package hermitcore.config;

import hermitcore.HECore;
import hermitcore.utils.FileHelper;
import hermitcore.utils.HELogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import moze_intel.projecte.emc.NormalizedSimpleStack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class CustomRecordParser {
	private static final String VERSION = "#0.2";
	private static File CONFIG;
	private static boolean loaded;

	public static void init() {
		CONFIG = new File(HECore.CONFIG_DIR, "custom_record.cfg");
		loaded = false;

		if (!CONFIG.exists()) 
		{
			try 
			{
				if (CONFIG.createNewFile()) 
				{
					writeDefaultFile();
					loaded = true;
				}
			} 
			catch (IOException e) 
			{
				HELogger.logFatal("Exception in file I/O: couldn't create custom configuration files.");
				e.printStackTrace();
				return;
			}
		}
		else
		{
			BufferedReader reader = null;

			try
			{
				reader = new BufferedReader(new FileReader(CONFIG));

				String line = reader.readLine();

				if (line == null || !line.equals(VERSION))
				{
					HELogger.logFatal("Found old custom records file: resetting.");
					writeDefaultFile();
				}
			}
			catch (IOException e)
			{
				HELogger.logFatal("Exception in file I/O: couldn't create custom configuration files.");
				e.printStackTrace();
			}
			finally
			{
				FileHelper.closeStream(reader);
			}

			loaded = true;
		}
	}
	
	public static Map<NormalizedSimpleStack, Integer> userValues = Maps.newHashMap();
	
	public static void readUserData()
	{
		if (!loaded)
		{
			HELogger.logFatal("ERROR: configurations files are not loaded!");
			return;
		}
		Entry entry;
		LineNumberReader reader = null;
		userValues.clear();
		try
		{
			reader = new LineNumberReader(new FileReader(CONFIG));
			int i = 0;
			while ((entry = getNextEntry(reader)) != null)
			{
				if(!entry.name.contains(":"))
				{					
					userValues.put(NormalizedSimpleStack.fromSerializedItem(entry.name) , i);
					i++;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			FileHelper.closeStream(reader);
		}
	}
	
	public static boolean addToFile(String recName, String resUrl)
	{
		if (!loaded)
		{
			HELogger.logFatal("ERROR: configurations files are not loaded!");
			return false;
		}
		
		PrintWriter writer = null;
		boolean result = false;
		
		return result;
	}
	
	private static List<String> readAllFile()
	{
		List<String> list = Lists.newArrayList();
		BufferedReader reader = null;

		try
		{
			reader = new BufferedReader(new FileReader(CONFIG));

			String s;

			while ((s = reader.readLine()) != null)
			{
				list.add(s);
			}

			return list;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			FileHelper.closeStream(reader);
		}

		return Lists.newArrayList();
	}

	private static List<Entry> getAllEntries()
	{
		List<Entry> list = Lists.newArrayList();
		LineNumberReader reader = null;

		try
		{
			reader = new LineNumberReader(new FileReader(CONFIG));

			Entry e;

			while ((e = getNextEntry(reader)) != null)
			{
				list.add(e);
			}

			return list;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			FileHelper.closeStream(reader);
		}

		return Lists.newArrayList();
	}
	private static Entry getNextEntry(LineNumberReader reader) throws IOException
	{
		String line;
		
		while ((line = getNextLine(reader)) != null)
		{
			if (line.charAt(0) == 'S')
			{
				String name = line.substring(2);
				int nameIndex = reader.getLineNumber();

				line = getNextLine(reader);
				
				if (line == null || line.charAt(0) != 'U')
				{
					continue;
				}
				
				String url = "";
				int urlIndex = reader.getLineNumber();
				
				try
				{
					url = line.substring(2);
				}
				catch (NumberFormatException e)
				{
					e.printStackTrace();
					continue;
				}
			return new Entry(name, url);
			}
		}
			
		return null;
	}
	private static String getNextLine(LineNumberReader reader) throws IOException
	{
		String line;

		while ((line = reader.readLine()) != null)
		{
			line = line.trim();

			if (line.isEmpty() || line.length() < 3 || line.charAt(0) == '#' || line.charAt(1) != ':')
			{
				continue;
			}

			return line;
		}

		return null;
	}

	private static void writeDefaultFile() throws IOException
	{

		PrintWriter writer = null;
		
		try
		{
			writer = new PrintWriter(CONFIG);

			writer.println(VERSION);
			writer.println("Custom Records file");
			writer.println("This file is used for custom record registration");
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			FileHelper.closeStream(writer);
		}
	}
	
	private static class Entry
	{
		public String name;
		public String url;
		public int nameIndex;
		public int urlIndex;
		
		public Entry (String name, String url)
		{
			this.name = name;
			this.url = url;
			this.nameIndex = nameIndex;
			this.urlIndex = urlIndex;
		}
	}
}
