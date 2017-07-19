package penowl.plugin.migs;

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
            getConfig();
        	Bukkit.broadcastMessage(ChatColor.GREEN + "[MIGS] " + ChatColor.RESET+"Reloaded shops.");
            return true;
        }
        return false;
    }

}

