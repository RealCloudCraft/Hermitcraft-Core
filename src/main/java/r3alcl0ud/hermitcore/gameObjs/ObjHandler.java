package r3alcl0ud.hermitcore.gameObjs;

import r3alcl0ud.hermitcore.HECore;
import r3alcl0ud.hermitcore.config.HermitCoreConfig;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class ObjHandler {

	public static void register() {

	}

	@SuppressWarnings("unchecked")
	public static void removeRecipes(String toDelete) {

	    ItemStack resultItem = new ItemStack((Item)Item.itemRegistry.getObject(toDelete));
	    resultItem.stackSize = 1;
	    resultItem.setItemDamage(0);

	    List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();

	    for (int i = 0; i < recipes.size(); i++)
	    {
	        IRecipe tmpRecipe = recipes.get(i);

	        ItemStack recipeResult = tmpRecipe.getRecipeOutput();
	        if(recipeResult != null) 
	        {
	            //recipeResult.stackSize = 1;
	            recipeResult.setItemDamage(0);
	        }

	        if (ItemStack.areItemStacksEqual(resultItem, recipeResult))
	        {
	            recipes.remove(i--);
	        }
	    }

	}
	public static void addRecipes () 
	{
		
	}
}
