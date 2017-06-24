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
   
    public static Inventory $owner = Bukkit.createInventory(null, 45, "Owner Interface"); {
    	for(int x = 0; x < 45; x = x + 1) {
    		ItemStack blank = setName(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15)," ");
    		$owner.setItem(x, blank);
    	}
    }
    
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
        if(command.getName().equalsIgnoreCase("masterinv")){
        	Player player = (Player) sender;
        	player.openInventory($owner);
            return true;
        }
        return false;
    }
	

}

