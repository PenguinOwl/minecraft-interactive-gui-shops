package penowl.plugin.migs;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import penowl.plugin.migs.Updater.UpdateResult;
import penowl.plugin.migs.Updater.UpdateType;


@SuppressWarnings("unused")
public class InvCtmr extends JavaPlugin {

	private final ClickListener clickListener = new ClickListener(this);
	private final InteractListener interactListener = new InteractListener(this);
	private final InvManagement invManagement = new InvManagement(this);
	private final ProtectListener protectListener = new ProtectListener(this);

	public static Economy economy = null;

	private Boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
		return (economyProvider != null);
	}

	@Override
	public void onEnable() {
		getConfig();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(interactListener, this);
		pm.registerEvents(clickListener, this);
		pm.registerEvents(protectListener, this);
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info( "A wild " + pdfFile.getName() + " version " + pdfFile.getVersion() + " appeared!" );
		setupEconomy();
		if (this.getConfig().getBoolean("auto-update")) {
			Updater updater = new Updater(this, 272481, this.getFile(), UpdateType.DEFAULT, true);
			if (updater.getResult() == UpdateResult.SUCCESS) {
			    this.getLogger().info("Downloaded " + updater.getLatestName() + ". Restart or reload your server to use.");
			}
		}
	}

	@Override
	public void onDisable() {
		saveConfig();
	}

	public boolean onCommand(CommandSender sender, Command command, String flag, String[] args){
		Boolean perm = false;
		Boolean perm1 = false;
		Boolean perm2 = false;
		if (sender instanceof Player) {
			Player player = (Player) sender;
			perm = player.hasPermission("migs.set");
			perm1 = player.hasPermission("migs.get");
			perm2 = player.hasPermission("migs.admin");
		}
		if(command.getName().equalsIgnoreCase("migs")){
			if (args.length>0) {
				if (args[0].compareTo("reload")==0&&perm2) {
					getConfig();
					Bukkit.broadcastMessage(ChatColor.GREEN + "[MIGS] " + ChatColor.RESET+"Reloaded shops.");
					return true;
				} else if ((args[0].compareTo("inspect")==0 || args[0].compareTo("is")==0) && args.length > 1 &&perm) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						int blockx = player.getTargetBlock((Set<Material>) null, 10).getLocation().getBlockX();
						int blocky = player.getTargetBlock((Set<Material>) null, 10).getLocation().getBlockY();
						int blockz = player.getTargetBlock((Set<Material>) null, 10).getLocation().getBlockZ();
						World blockw = player.getTargetBlock((Set<Material>) null, 10).getLocation().getWorld();
						String configloc = "shops."+String.valueOf(blockw)+"."+String.valueOf(blockx)+"."+String.valueOf(blocky)+"."+String.valueOf(blockz);
						player.sendMessage(InvManagement.$migs+this.getConfig().getString(configloc+"."+args[1]));
					}
					return true;
				} else if ((args[0].compareTo("set")==0 || args[0].compareTo("set")==0) && args.length > 3 &&perm1) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						int blockx = player.getTargetBlock((Set<Material>) null, 10).getLocation().getBlockX();
						int blocky = player.getTargetBlock((Set<Material>) null, 10).getLocation().getBlockY();
						int blockz = player.getTargetBlock((Set<Material>) null, 10).getLocation().getBlockZ();
						World blockw = player.getTargetBlock((Set<Material>) null, 10).getLocation().getWorld();
						String configloc = "shops."+String.valueOf(blockw)+"."+String.valueOf(blockx)+"."+String.valueOf(blocky)+"."+String.valueOf(blockz);
						player.sendMessage(InvManagement.$migs+"Set config.");
						if (args[3].compareTo("int")==0) {
							this.getConfig().set(configloc+"."+args[1],Integer.valueOf(args[2]));
						} else if (args[3].compareTo("dub")==0) {
							this.getConfig().set(configloc+"."+args[1],Double.valueOf(args[2]));
						} else {
							this.getConfig().set(configloc+"."+args[1],String.valueOf(args[2]));
						}
						this.saveConfig();
					}
					return true;
				}
			}
		}
		return false;
	}

}

