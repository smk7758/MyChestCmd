package com.github.smk7758.MyChestCmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CommandExcuter implements CommandExecutor {
	private Main plugin;

	public CommandExcuter(Main instance) {
		plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("MyChestCmd")) {
			if (args.length == 0) {
				sendCommandList(sender);
				return true;
			}
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("reload")) {
					if (!sender.hasPermission("MyChestCmd.reload") && (sender instanceof Player)) {
						plugin.cLog.sendPermissionErrorMessage(sender, "MyChestCmd.reload");
						return false;
					}
					plugin.reloadConfig();
					return true;
					//これ必要なのか分からない。
				}
				if (args[0].equalsIgnoreCase("help")) {
					plugin.cLog.sendMessage(sender, "This plugin can set your Chest. And you can set the name of Chest.", 0);
					sendCommandList(sender);
					return true;
				}
				if (args[0].equalsIgnoreCase("debug")) {
					if (!sender.hasPermission("MyChestCmd.debug") && (sender instanceof Player)) {
						plugin.cLog.sendPermissionErrorMessage(sender, "MyChestCmd.debug");
						return false;
					}
					if (plugin.DebugMode) {
						plugin.DebugMode = false;
						plugin.cLog.sendMessage(sender, "DebugMode has been false.", 0);
					} else {
						plugin.DebugMode = true;
						plugin.cLog.sendMessage(sender, "DebugMode has been true.", 0);
					}
					plugin.cLog.sendMessage(sender, "This is a check. DebugMode is true.", 3);
					return true;
				}
			}
			sendCommandList(sender);
		}
		if (cmd.getName().equalsIgnoreCase("Chest")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Must send from Player.");
				return false;
			}
			Player player = (Player) sender;
			if (!sender.hasPermission("MyChestCmd.chest")) {
				plugin.cLog.sendPermissionErrorMessage(sender, "MyChestCmd.chest");
				return false;
			}
			if (args.length == 0) {
				plugin.cLog.sendMessage(sender, "Please set Chest name.", 2);
				return false;
			}
			if (args.length > 0) {
				Inventory inv = null;
				String path = "InventoryContents." + player.getName() + "." + args[0];
				if (plugin.inv_player.containsKey(path) || plugin.inv_player.get(path) != null) { //HashMapの確認。
					plugin.cLog.sendMessage(sender, "HashMap", 3);
					inv = plugin.inv_player.get(path); //HashMapから取得する。
				} else {
					//HashMapに無い。
					if (plugin.getConfig().contains(path)) { //Configの確認。
						//Configから取得する。
						plugin.cLog.sendMessage(sender, "Config", 3);
						inv = Bukkit.createInventory(null, 27, args[0]); // Invの作成。
						ItemStack[] inx = new ItemStack[27];
						for (int i = 0; i < 26; i++) {
							inx[i] = plugin.getConfig().getItemStack(path + "." + i); // Contents取得→入れる
						}
						inv.setContents(inx); // Contents指定。
					} else {
						plugin.cLog.sendMessage(sender, "It doesn't exist.", 2);
						return false;
					}
				}
				player.openInventory(inv);
				inv = player.getOpenInventory().getTopInventory(); // なぜかコレで取得出来た(変更後の取得)。
				plugin.inv_player.put(path, inv);
				return true;
			}
		}
//		if (cmd.getName().equalsIgnoreCase("MyChestCmdEnder")) {
//			if (sender instanceof Player) {
//				Player p = (Player) sender;
//				p.openInventory(p.getEnderChest());
//				return true;
//			} else {
//				sender.sendMessage("Must send from Player.");
//			}
//		}
		if (cmd.getName().equalsIgnoreCase("MakeChest")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Must send from Player.");
				return false;
			}
			Player player = (Player) sender;
			if (!sender.hasPermission("MyChestCmd.make")) {
				plugin.cLog.sendPermissionErrorMessage(sender, "MyChestCmd.make");
				return false;
			}
			if (args.length == 0) {
				plugin.cLog.sendMessage(sender, "Please set Chest name.", 2);
				return false;
			}
			if (args.length > 0) {
				if (args[0].contains(".")) {
					plugin.cLog.sendMessage(sender, "Sorry you can't set \".\" inside the Chest.", 2);
					return false;
				}
				String path = "InventoryContents." + player.getName() + "." + args[0];
				if (plugin.inv_player.containsKey(path) || plugin.getConfig().contains(path)) {
					plugin.cLog.sendMessage(sender, args[0] + " is already setted.", 2);
					return false;
				}
				Inventory inv = Bukkit.createInventory(null, 27, args[0]); //Invの作成
				player.openInventory(inv);
				inv = player.getOpenInventory().getTopInventory(); // なぜかコレで取得出来た(変更後の取得)。
				plugin.inv_player.put(path, inv);
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("DeleteChest")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Must send from Player.");
				return false;
			}
			Player player = (Player) sender;
			if (!sender.hasPermission("MyChestCmd.delete")) {
				plugin.cLog.sendPermissionErrorMessage(sender, "MyChestCmd.delete");
				return false;
			}
			if (args.length == 0) {
				plugin.cLog.sendMessage(sender, "Please set Chest name.", 2);
				return false;
			}
			if (args.length > 0) {
				if (args[0].contains(".")) {
					plugin.cLog.sendMessage(sender, "Sorry you can't set \".\" inside the Chest.", 2);
					return false;
				}
				String path = "InventoryContents." + player.getName() + "." + args[0];
				if (plugin.inv_player.containsKey(path) || plugin.getConfig().contains(path)) {
					plugin.inv_player.remove(path);
					plugin.getConfig().set(path, null);
					plugin.saveConfig();
					plugin.cLog.sendMessage(sender, args[0] + " has been delete.", 0);
					return true;
				} else {
					plugin.cLog.sendMessage(sender, args[0] + " does not exist.", 2);
					return false;
				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("ListChest")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Must send from Player.");
				return false;
			}
			Player player = (Player) sender;
			if (!sender.hasPermission("MyChestCmd.list")) {
				plugin.cLog.sendPermissionErrorMessage(sender, "MyChestCmd.list");
				return false;
			}
			if (args.length == 0) {
				plugin.cLog.sendMessage(sender, "<Chest List>", 0);
				for (String path : plugin.inv_player.keySet()) { //HashMapより。
					plugin.cLog.sendMessage(sender, path, 3);
					if (player.getName().equals(path.split("\\.")[1])) {
						plugin.cLog.sendMessage(sender, path.split("\\.")[1], 0);
					}
				}
				if (plugin.getConfig().contains("InventoryContents." + player.getName())) { //Configより。
					for (String name : plugin.getConfig().getConfigurationSection(player.getName()).getKeys(false)) {
						plugin.cLog.sendMessage(sender, name, 0);
					}
				}
				return true;
			}
		}
		return false;
	}

	public void sendCommandList(CommandSender sender) {
		plugin.cLog.sendMessage(sender, "Command List!!", 0);
		plugin.cLog.sendMessage(sender, "View Chest: /chest <chest_name>", 0);
		plugin.cLog.sendMessage(sender, "Make Chest: /makechest, /chestmake", 0);
		plugin.cLog.sendMessage(sender, "Delete Chest: /deletechest, /chestdelete, /delchest, /chestdel", 0);
		plugin.cLog.sendMessage(sender, "Show Chest List: /listchest, /chestlist", 0);
		plugin.cLog.sendMessage(sender, "Other: /MyChestCmd, /mcc", 0);
	}
}