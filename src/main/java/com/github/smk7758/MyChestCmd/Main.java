package com.github.smk7758.MyChestCmd;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
HashMap<String,Inventory> inv_player = new HashMap<>();

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
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
				if (getConfig().contains("InventoryContents."+player_name+"."+counter)) {
					getConfig().set("InventoryContents."+player_name, null);
				}
				if (item != null && item.getType() != Material.AIR ) {
					getConfig().set("InventoryContents."+player_name+"."+counter, item);
				}
				counter++;
			}
		}
		saveConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("MyChestCmd")) {
			if (sender instanceof Player) {
				Player player = (Player)sender;
				Inventory inv = null;
				if (!inv_player.containsKey(player.getName()) || inv_player == null) {
					inv = Bukkit.createInventory(null, 27, player.getName() + "'s MyChest"); //InvÇÃçÏê¨
					if (getConfig().contains("InventoryContents."+player.getName())) {
						ItemStack[] inx = new ItemStack[27];
						for (int i = 0; i < 26; i++) {
							inx[i] = getConfig().getItemStack("InventoryContents."+player.getName()+"."+i); //ContentséÊìæÅ®ì¸ÇÍÇÈ
						}
						inv.setContents(inx); //ContentséwíËÅB
					}
				} else {
					inv = inv_player.get(player.getName()); //InvÇéùÇ¡ÇƒÇ≠ÇÈÅB
				}
				player.openInventory(inv);
				inv = player.getOpenInventory().getTopInventory(); //Ç±Ç±ÇÊÇ≠ÇÌÇ©Ç¡ÇƒÇ»Ç¢Å©
				inv_player.put(player.getName(), inv);
			} else {
				sender.sendMessage("Must send from Player.");
			}
		}
		if (cmd.getName().equalsIgnoreCase("MyChestCmdEnder"))
		{
			if (sender instanceof Player) {
				Player p = (Player)sender;
				p.openInventory(p.getEnderChest());
			}
		}
		return false;
	}
}
