package me.Melo.MelosBuildTools;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Rotation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin implements Listener {
	
	ShapedRecipe InvisibleInatorRecipe = getInvisibleInatorRecipe();
	@Override
	public void onEnable() {
		Bukkit.addRecipe(InvisibleInatorRecipe);
		Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "MelosBuildTools Ready");
		this.getServer().getPluginManager().registerEvents(this, this);
		
	}
	
	@Override
	public void onDisable() {
		Bukkit.clearRecipes();
	}
	
	int repeatTimes;
	int effRange;
	public void getINATORArgs( String[] arguments) {
		//get range 
		if (arguments.length > 0) {
			try {
				effRange = Integer.parseInt(arguments[0]);
			}
			catch (NumberFormatException e) {
				effRange = 2;
			}
		} 
		else {
			effRange = 2;
		}
	}
	
	public ItemStack GetINATORItemStack() {
		ItemStack item = new ItemStack(Material.GOLDEN_CARROT);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(ChatColor.GOLD + "INVISIBLE-INATOR");
		meta.setUnbreakable(true);
		meta.addEnchant( Enchantment.DURABILITY , 1, true);
		
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		ArrayList<String> lore = new ArrayList<String>();
		
		lore.add("makes itemframes invisible");
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		
		return item;
	}

	
	//EventHandler
	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer(); //get player
		//tests
		if (player.isSneaking()) return;//check for sneaking
		ItemStack mainHand = player.getInventory().getItemInMainHand();
		if (mainHand == null) return;// check for item
		if (!mainHand.hasItemMeta()) return; //check for meta
		ItemMeta meta = mainHand.getItemMeta();//get meta
		if (!meta.hasDisplayName()) return;//check for display name
		if (!meta.getDisplayName().contains("INVISIBLE-INATOR") || !meta.hasLore()) return;//check for name/lore
		if (!(event.getRightClicked() instanceof ItemFrame)) return; // stop the code if it's not an itemframe
		 	
		
	    ItemFrame itemFrame = (ItemFrame) event.getRightClicked(); // get the ItemFrame object
	    itemFrame.setVisible(!itemFrame.isVisible()); // if it's visible, make it invisible and vice versa
	    
	    Rotation itemframeRotation = itemFrame.getRotation();
	    itemFrame.setRotation(itemframeRotation);
	    event.setCancelled(true);
	    
	}
	//
	@Override
	public  boolean onCommand(CommandSender sender, Command cmd,String label,String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Console cannot run this command");
			return true;
		}
		Player player = (Player) sender;
		if (label.equalsIgnoreCase("invisibleinator")) {
			if (player.hasPermission("melosbuildtools.invisibleinator")) {
				
				if (!(player.getInventory().contains(Material.AIR))) return false;
				player.sendMessage(ChatColor.AQUA + "Item delivered!");
				
				player.getInventory().addItem(GetINATORItemStack());
				return true;
			}
			else {
				player.sendMessage(ChatColor.RED + "No Permission");
				return true;
			}
		
		} 
		return false;
		
	}
	
	// recipies
	public ShapedRecipe getInvisibleInatorRecipe() {
		
		ItemStack item = GetINATORItemStack();
		
		NamespacedKey key = new NamespacedKey(this, "INVISIBLE-INATOR");
		
		ShapedRecipe recipe = new ShapedRecipe(key, item);
		
		recipe.shape(
				" F ",
				" G ",
				"   "
				);
		recipe.setIngredient('G',Material.GOLDEN_CARROT);
		recipe.setIngredient('F',Material.FERMENTED_SPIDER_EYE);
		
		return recipe;
	}
	
}
