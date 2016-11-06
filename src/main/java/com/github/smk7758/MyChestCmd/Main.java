package com.github.smk7758.MyChestCmd;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	public ConsoleLog cLog = new ConsoleLog(this);

	public String PluginName = getDescription().getName();
	public boolean DebugMode = false;
	public String PluginPrefix = "[" + ChatColor.GREEN + PluginName + ChatColor.RESET + "] ";
	public String cPrefix = "[" + PluginName + "] ";
	public String pInfo = "[" + ChatColor.RED + "Info" + ChatColor.RESET + "] ";
	public String pError = "[" + ChatColor.RED + "ERROR" + ChatColor.RESET + "] ";
	HashMap<String, Inventory> inv_player = new HashMap<>(); //path|Inv

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("MyChestCmd").setExecutor(new CommandExcuter(this));
		getCommand("Chest").setExecutor(new CommandExcuter(this));
		getCommand("MakeChest").setExecutor(new CommandExcuter(this));
		getCommand("DeleteChest").setExecutor(new CommandExcuter(this));
		getCommand("ListChest").setExecutor(new CommandExcuter(this));
//		getCommand("MyChestCmdEnder").setExecutor(new CommandExcuter(this));
		saveDefaultConfig();
		reloadConfig();
		DebugMode = getConfig().getBoolean("DebugMode"); //ãNìÆéûÇÃDebugModeÅB
	}

	@Override
	public void onDisable() {
		saveInv();
	}

	public void saveInv() {
		for (String path : inv_player.keySet()) {
			int counter = 0;
			for (ItemStack item : inv_player.get(path).getContents()) {
				if (getConfig().contains(path + "." + counter)) {
					getConfig().set(path, null); //ì‰ÅB
				}
				if (item != null && item.getType() != Material.AIR) {
					getConfig().set(path + "." + counter, item);
				}
				counter++;
			}
		}
		saveConfig();
	}
}
