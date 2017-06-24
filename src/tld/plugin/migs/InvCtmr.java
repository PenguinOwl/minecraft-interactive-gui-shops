package tld.plugin.migs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;


@SuppressWarnings("unused")
public class InvCtmr extends JavaPlugin {

	private final ClickListener clickListener = new ClickListener();
	
    @Override
    public void onEnable() {
       getConfig();
       PluginManager pm = getServer().getPluginManager();
       pm.registerEvents(clickListener, this);
       PluginDescriptionFile pdfFile = this.getDescription();
       getLogger().info( "A wild " + pdfFile.getName() + " version " + pdfFile.getVersion() + " appearerd!" );
    }
   
    @Override
    public void onDisable() {
       saveConfig();
    }
    
    public static ItemStack mmai(Material type, int amount, short dmg, String name) {
    	ItemStack item = new ItemStack(type, amount, dmg);
    	setName(item,name);
    	return item;
    }
    
    public static ItemStack setName(ItemStack items, String name){
        ItemMeta meta = items.getItemMeta();
        meta.setDisplayName(name);
        items.setItemMeta(meta);
        return items;
    }
    
    public static Inventory createOwnerInventory(int ilx, int ily, int ilz, int cost, boolean filter) {
        Inventory temp = Bukkit.createInventory(null, 45, "Owner Interface"); 
    	for(int x = 0; x < 45; x = x + 1) {
    		ItemStack blank = setName(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15)," ");
    		temp.setItem(x, blank);
    	}
    	temp.setItem(28, mmai(Material.HOPPER, 1, (short) 0, ChatColor.RESET + "-$1"));
    	temp.setItem(29, mmai(Material.HOPPER, 10, (short) 0, ChatColor.RESET + "-$10"));
    	temp.setItem(30, mmai(Material.HOPPER, 50, (short) 0, ChatColor.RESET + "-$50"));
    	temp.setItem(10, mmai(Material.SULPHUR, 1, (short) 0, ChatColor.RESET + "+$1"));
    	temp.setItem(11, mmai(Material.SULPHUR, 10, (short) 0, ChatColor.RESET + "+$10"));
    	temp.setItem(12, mmai(Material.SULPHUR, 50, (short) 0, ChatColor.RESET + "+$50"));
    	temp.setItem(20, mmai(Material.DOUBLE_PLANT, 50, (short) 0, ChatColor.RESET + "$" + String.valueOf(cost)));
    	temp.setItem(5, mmai(Material.WEB, 50, (short) 0, ChatColor.RESET + "Filter?"));
    	temp.setItem(14, tfwool(filter));
		return temp;
    }
    
    public static ItemStack tfwool(boolean bln) {
    	if (bln) {
    		return mmai(Material.WOOL, 1, (short) 5, ChatColor.RESET + "" + ChatColor.RED + "FALSE");
    	} else {
    		return mmai(Material.WOOL, 1, (short) 14, ChatColor.RESET + "" + ChatColor.GREEN + "TRUE");
    	}
    }
    
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
        if(command.getName().equalsIgnoreCase("masterinv")){
        	Player player = (Player) sender;
        	player.openInventory(createOwnerInventory(0,0,0,0,true));
            return true;
        }
        return false;
    }
	

}

