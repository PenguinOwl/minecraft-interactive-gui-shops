package penowl.plugin.migs;

import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class InteractListener implements Listener {
	
	private Plugin plugin;

    public InteractListener(Plugin plugin)
    {
        this.plugin = plugin;
    }
	
    @EventHandler
    public void someoneBlocked(PlayerInteractEvent event) {
    	if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
    	int blockx = event.getClickedBlock().getLocation().getBlockX();
    	int blocky = event.getClickedBlock().getLocation().getBlockY();
    	int blockz = event.getClickedBlock().getLocation().getBlockZ();
    	World blockw = event.getClickedBlock().getLocation().getWorld();
    	Player player = event.getPlayer();
    	String configloc = "shops."+String.valueOf(blockw)+"."+String.valueOf(blockx)+"."+String.valueOf(blocky)+"."+String.valueOf(blockz);
    	int xdif = 0;
		int zdif = 0;
		if (event.getClickedBlock().getType() == Material.WALL_SIGN) {
			org.bukkit.material.Sign com = (org.bukkit.material.Sign) event.getClickedBlock().getState().getData();
			BlockFace sa = com.getAttachedFace();
			if (sa == BlockFace.NORTH) {
				zdif = -1;
			} else if (sa == BlockFace.SOUTH) {
				zdif = 1;
			} else if (sa == BlockFace.EAST) {
				xdif = 1;
			} else if (sa == BlockFace.WEST) {
				xdif = -1;
			} else {
		
			}
		}
		Location chloc = new Location(blockw, xdif + blockx, blocky, zdif + blockz);
        	if (event.getClickedBlock().getType() == Material.WALL_SIGN) {
        		if (event.getMaterial() != null) {
        			if (plugin.getConfig().getString(configloc+".owner") != null) {
        				if (plugin.getConfig().getString(configloc+".owner").compareTo(player.getUniqueId().toString()) == 0 && player.isSneaking() == false) {
        					if (event.getMaterial()==Material.WOOD_HOE) {
        						plugin.getConfig().set(configloc+".owner", null);
        						org.bukkit.block.Sign wop = (org.bukkit.block.Sign) event.getClickedBlock().getState();
        						wop.setLine(0, ChatColor.RED + "" + ChatColor.BOLD + "[SHOP]");
        						wop.update(true,false);
        						player.sendMessage(InvManagement.$migs + "Shop destroyed.");
        						plugin.saveConfig();
        					} else if(event.getMaterial()==Material.WOOD_SPADE) {
        						if (plugin.getConfig().getBoolean(configloc+".enabled")) {
        							plugin.getConfig().set(configloc+".enabled",false);
        							org.bukkit.block.Sign wop = (org.bukkit.block.Sign) event.getClickedBlock().getState();
            						wop.setLine(0, ChatColor.YELLOW + "" + ChatColor.BOLD + "[SHOP]");
            						wop.update(true,false);
            						player.sendMessage(InvManagement.$migs + "Shop closed.");
        						} else {
        							plugin.getConfig().set(configloc+".enabled",true);
        							org.bukkit.block.Sign wop = (org.bukkit.block.Sign) event.getClickedBlock().getState();
            						wop.setLine(0, ChatColor.BLUE + "" + ChatColor.BOLD + "[SHOP]");
            						wop.update(true,false);
            						player.sendMessage(InvManagement.$migs + "Shop opened.");
        						}
        					} else {
        						player.openInventory(InvManagement.createOwnerInventory(blockw, blockx, blocky, blockz));
        					}
        				} else {
        					if (plugin.getConfig().getBoolean(configloc+".enabled")) {
        						if (plugin.getConfig().getBoolean(configloc+".buy")) {
        							player.openInventory(InvManagement.createCustomerInventory(blockw, blockx, blocky, blockz));
        						} else {
        							player.openInventory(InvManagement.createSellerInventory(blockw, blockx, blocky, blockz, player));
        						}
        					}
        				}
        			} else {
        				if (event.getMaterial()==Material.WOOD_HOE) {
        					if (chloc.getBlock().getType()==Material.CHEST || chloc.getBlock().getType()==Material.TRAPPED_CHEST) {
        						player.sendMessage(InvManagement.$migs + "Creating Shop...");
        						plugin.getConfig().set(configloc+".owner", player.getUniqueId().toString());
        						plugin.getConfig().set(configloc+".price", 0);
        						plugin.getConfig().set(configloc+".filter", false);
        						plugin.getConfig().set(configloc+".buy", true);
        						plugin.getConfig().set(configloc+".alerts", false);
        						plugin.getConfig().set(configloc+".chestx", xdif + blockx);
        						plugin.getConfig().set(configloc+".chestz", zdif + blockz);
        						plugin.getConfig().set(configloc+".chesty", blocky);
        						plugin.getConfig().set(configloc+".chestw", blockw.getName());
        						plugin.getConfig().set(configloc+".enabled", true);
        						plugin.saveConfig();
        						org.bukkit.block.Sign wop = (org.bukkit.block.Sign) event.getClickedBlock().getState();
        						wop.setLine(0, ChatColor.BLUE + "" + ChatColor.BOLD + "[SHOP]");
        						wop.update(true,false);
        						player.sendMessage(InvManagement.$migs + "Shop created!");
        					}
        				}
        			}
        		}
        	}
        }
    }
}