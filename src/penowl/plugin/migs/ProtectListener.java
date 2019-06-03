package penowl.plugin.migs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ProtectListener implements Listener {

	private static Plugin plugin;

	@SuppressWarnings("static-access")
	public ProtectListener(Plugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (
				
				event.getBlock().getType() == Material.ACACIA_WALL_SIGN ||
				event.getBlock().getType() == Material.BIRCH_WALL_SIGN ||
				event.getBlock().getType() == Material.DARK_OAK_WALL_SIGN ||
				event.getBlock().getType() == Material.JUNGLE_WALL_SIGN ||
				event.getBlock().getType() == Material.OAK_WALL_SIGN ||
				event.getBlock().getType() == Material.SPRUCE_WALL_SIGN
				
			) {
			Location loc = event.getBlock().getLocation();
			String configloc = "shops."+String.valueOf(loc.getWorld())+"."+String.valueOf(loc.getBlockX())+"."+String.valueOf(loc.getBlockY())+"."+String.valueOf(loc.getBlockZ());
			if (plugin.getConfig().getString(configloc+".owner") != null) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.YELLOW+"Close this shop before breaking it.");
			}
		}
		if (event.getBlock().getType() == Material.CHEST || event.getBlock().getType() == Material.TRAPPED_CHEST) {
			Location loc = event.getBlock().getLocation();
			for (int x = -1; x < 2; x = x + 1) {
				for (int y = -1; y < 2; y = y + 1) {
					String configloc = "shops."+String.valueOf(loc.getWorld())+"."+String.valueOf(loc.getBlockX()+x)+"."+String.valueOf(loc.getBlockY())+"."+String.valueOf(loc.getBlockZ()+y);
					if (plugin.getConfig().getString(configloc+".owner") != null) {
						if (Bukkit.getWorld(plugin.getConfig().getString(configloc+".chestw")) == loc.getWorld() && (plugin.getConfig().getInt(configloc+".chestx")) == loc.getBlockX() && (plugin.getConfig().getInt(configloc+".chestz")) == loc.getBlockZ()) {
							event.setCancelled(true);
							event.getPlayer().sendMessage(ChatColor.YELLOW+"Close this shop before breaking it.");
						}
					}
				}
			}
		}
	}
}
