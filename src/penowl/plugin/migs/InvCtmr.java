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
	}

	@Override
	public void onDisable() {
		saveConfig();
	}

	public boolean onCommand(CommandSender sender, Command command, String flag, String[] args){
		if(command.getName().equalsIgnoreCase("migs")){
			if (args.length>0) {
				if (args[0].compareTo("reload")==0) {
					getConfig();
					Bukkit.broadcastMessage(ChatColor.GREEN + "[MIGS] " + ChatColor.RESET+"Reloaded shops.");
					return true;
				} else if ((args[0].compareTo("inspect")==0 || args[0].compareTo("is")==0) && args.length > 1) {
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
				} else if ((args[0].compareTo("set")==0 || args[0].compareTo("set")==0) && args.length > 3) {
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
						} else {
							this.getConfig().set(configloc+"."+args[1],args[2]);
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

