package com.github.smk7758.MyChestCmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CommandExcuter implements CommandExecutor {
	// private static final CommandExcuter instance = new CommandExcuter();
	// private CommandExcuter() {
	// }
	// public static CommandExcuter getInstance() {
	// return instance;
	// }
	// Main plugin = Main.getInstance();

	private Main plugin;

	public CommandExcuter(Main instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("MyChestCmd")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				Inventory inv = null;
				if (!plugin.inv_player.containsKey(player.getName()) || plugin.inv_player == null) {
					inv = Bukkit.createInventory(null, 27, player.getName() + "'s MyChest"); // InvÇÃçÏê¨
					if (plugin.getConfig().contains("InventoryContents." + player.getName())) {
						ItemStack[] inx = new ItemStack[27];
						for (int i = 0; i < 26; i++) {
							inx[i] = plugin.getConfig().getItemStack("InventoryContents." + player.getName() + "." + i); // ContentséÊìæÅ®ì¸ÇÍÇÈ
						}
						inv.setContents(inx); // ContentséwíËÅB
					}
				}
				else {
					inv = plugin.inv_player.get(player.getName()); // InvÇéùÇ¡ÇƒÇ≠ÇÈÅB
				}
				player.openInventory(inv);
				inv = player.getOpenInventory().getTopInventory(); // Ç±Ç±ÇÊÇ≠ÇÌÇ©Ç¡ÇƒÇ»Ç¢Å©
				plugin.inv_player.put(player.getName(), inv);
				return true;
			}
			else {
				sender.sendMessage("Must send from Player.");
			}
		}
		if (cmd.getName().equalsIgnoreCase("MyChestCmdEnder")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				p.openInventory(p.getEnderChest());
				return true;
			}
			else {
				sender.sendMessage("Must send from Player.");
			}
		}
		return false;
	}

}
