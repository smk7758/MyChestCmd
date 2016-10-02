package com.github.smk7758.MyChestCmd;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	// private static final Main instance = new Main();
	// private Main() {
	// }
	// public static Main getInstance() {
	// return instance;
	// }
	// CommandExcuter Cmder = CommandExcuter.getInstance();

	HashMap<String, Inventory> inv_player = new HashMap<>();

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("MyChestCmd").setExecutor(new CommandExcuter(this));
		getCommand("MyChestCmdEnder").setExecutor(new CommandExcuter(this));
		saveDefaultConfig();
		reloadConfig();
	}

	@Override
	public void onDisable() {
		saveInv();
	}

	public void saveInv() {
		for (String player_name : inv_player.keySet()) {
			int counter = 0;
			for (ItemStack item : inv_player.get(player_name).getContents()) {
				if (getConfig().contains("InventoryContents." + player_name + "." + counter)) {
					getConfig().set("InventoryContents." + player_name, null);
				}
				if (item != null && item.getType() != Material.AIR) {
					getConfig().set("InventoryContents." + player_name + "." + counter, item);
				}
				counter++;
			}
		}
		saveConfig();
	}

}
