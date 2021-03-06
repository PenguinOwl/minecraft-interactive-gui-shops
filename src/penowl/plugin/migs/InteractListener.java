package penowl.plugin.migs;

import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
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
		Block blockBehind = event.getClickedBlock();
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			int blockx = event.getClickedBlock().getLocation().getBlockX();
			int blocky = event.getClickedBlock().getLocation().getBlockY();
			int blockz = event.getClickedBlock().getLocation().getBlockZ();
			World blockw = event.getClickedBlock().getLocation().getWorld();
			Player player = event.getPlayer();
			String configloc = "shops."+String.valueOf(blockw)+"."+String.valueOf(blockx)+"."+String.valueOf(blocky)+"."+String.valueOf(blockz);
			int xdif = 0;
			int zdif = 0;
			if (
					
					event.getClickedBlock().getType() == Material.ACACIA_WALL_SIGN ||
					event.getClickedBlock().getType() == Material.BIRCH_WALL_SIGN ||
					event.getClickedBlock().getType() == Material.DARK_OAK_WALL_SIGN ||
					event.getClickedBlock().getType() == Material.JUNGLE_WALL_SIGN ||
					event.getClickedBlock().getType() == Material.OAK_WALL_SIGN ||
					event.getClickedBlock().getType() == Material.SPRUCE_WALL_SIGN
					
				) {
				
				Block block = event.getClickedBlock();
				if (block != null && block.getState() instanceof Sign)
				{
				    BlockData data = block.getBlockData();
				    if (data instanceof Directional)
				    {
				        Directional directional = (Directional)data;
				        blockBehind = block.getRelative(directional.getFacing().getOppositeFace());
				    }
				}
			}
			String kg = plugin.getConfig().getString(configloc+".owner");
			if (kg == null) {
				kg = "sssssssssssssssssssssssssssssss";
			}
			Boolean perm = player.hasPermission("migs.admin");
			Boolean perm1 = player.hasPermission("migs.main");
			if (kg.length()<16) {
				Location chloc = blockBehind.getLocation();
				if (
						
						event.getClickedBlock().getType() == Material.ACACIA_WALL_SIGN ||
						event.getClickedBlock().getType() == Material.BIRCH_WALL_SIGN ||
						event.getClickedBlock().getType() == Material.DARK_OAK_WALL_SIGN ||
						event.getClickedBlock().getType() == Material.JUNGLE_WALL_SIGN ||
						event.getClickedBlock().getType() == Material.OAK_WALL_SIGN ||
						event.getClickedBlock().getType() == Material.SPRUCE_WALL_SIGN
						
					) {
					if (event.getMaterial() != null) {
						if (plugin.getConfig().getString(configloc+".owner") != null) {
							if (perm && player.isSneaking() == false) {
								if (event.getMaterial()==Material.DIAMOND_HOE ) {
									plugin.getConfig().set(configloc+".owner", null);
									org.bukkit.block.Sign wop = (org.bukkit.block.Sign) event.getClickedBlock().getState();
									wop.setLine(0, ChatColor.RED + "" + ChatColor.BOLD + "{SHOP}");
									wop.update(true,false);
									player.sendMessage(InvManagement.$migs + "Shop destroyed.");
									plugin.saveConfig();
								} else if(event.getMaterial()==Material.DIAMOND_SHOVEL) {
									if (plugin.getConfig().getBoolean(configloc+".enabled")) {
										plugin.getConfig().set(configloc+".enabled",false);
										org.bukkit.block.Sign wop = (org.bukkit.block.Sign) event.getClickedBlock().getState();
										wop.setLine(0, ChatColor.YELLOW + "" + ChatColor.BOLD + "{SHOP}");
										wop.update(true,false);
										player.sendMessage(InvManagement.$migs + "Shop closed.");
									} else {
										plugin.getConfig().set(configloc+".enabled",true);
										org.bukkit.block.Sign wop = (org.bukkit.block.Sign) event.getClickedBlock().getState();
										wop.setLine(0, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "{SHOP}");
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
							if (event.getMaterial()==Material.DIAMOND_HOE && perm) {
								if (chloc.getBlock().getType()==Material.CHEST || chloc.getBlock().getType()==Material.TRAPPED_CHEST || chloc.getBlock().getType()==Material.BARREL) {
									player.sendMessage(InvManagement.$migs + "Creating Shop...");
									plugin.getConfig().set(configloc+".owner", "admin");
									plugin.getConfig().set(configloc+".price", 0.00);
									plugin.getConfig().set(configloc+".buy", true);
									plugin.getConfig().set(configloc+".chestx", chloc.getBlockX());
									plugin.getConfig().set(configloc+".chestz", chloc.getBlockZ());
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
			} else {
				Location chloc = blockBehind.getLocation();
				if (
						
						event.getClickedBlock().getType() == Material.ACACIA_WALL_SIGN ||
						event.getClickedBlock().getType() == Material.BIRCH_WALL_SIGN ||
						event.getClickedBlock().getType() == Material.DARK_OAK_WALL_SIGN ||
						event.getClickedBlock().getType() == Material.JUNGLE_WALL_SIGN ||
						event.getClickedBlock().getType() == Material.OAK_WALL_SIGN ||
						event.getClickedBlock().getType() == Material.SPRUCE_WALL_SIGN
						
					) {
					if (event.getMaterial() != null) {
						if (plugin.getConfig().getString(configloc+".owner") != null) {
							if (plugin.getConfig().getString(configloc+".owner").compareTo(player.getUniqueId().toString()) == 0 && player.isSneaking() == false) {
								if (event.getMaterial()==Material.WOODEN_HOE) {
									plugin.getConfig().set(configloc+".owner", null);
									org.bukkit.block.Sign wop = (org.bukkit.block.Sign) event.getClickedBlock().getState();
									wop.setLine(0, ChatColor.RED + "" + ChatColor.BOLD + "[SHOP]");
									wop.update(true,false);
									player.sendMessage(InvManagement.$migs + "Shop destroyed.");
									plugin.saveConfig();
								} else if(event.getMaterial()==Material.WOODEN_SHOVEL) {
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
							if (event.getMaterial()==Material.WOODEN_HOE && perm1) {
								if (chloc.getBlock().getType()==Material.CHEST || chloc.getBlock().getType()==Material.TRAPPED_CHEST || chloc.getBlock().getType()==Material.BARREL) {
									player.sendMessage(InvManagement.$migs + "Creating Shop...");
									plugin.getConfig().set(configloc+".owner", player.getUniqueId().toString());
									plugin.getConfig().set(configloc+".price", 0.00);
									plugin.getConfig().set(configloc+".filter", false);
									plugin.getConfig().set(configloc+".buy", true);
									plugin.getConfig().set(configloc+".alerts", false);
									plugin.getConfig().set(configloc+".chestx", chloc.getBlockX());
									plugin.getConfig().set(configloc+".chestz", chloc.getBlockZ());
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
							if (event.getMaterial()==Material.DIAMOND_HOE && perm) {
								plugin.getConfig().set(configloc+".owner", null);
								org.bukkit.block.Sign wop = (org.bukkit.block.Sign) event.getClickedBlock().getState();
								wop.setLine(0, ChatColor.RED + "" + ChatColor.BOLD + "{SHOP}");
								wop.update(true,false);
								player.sendMessage(InvManagement.$migs + "Shop destroyed.");
								plugin.saveConfig();
							}
							if (event.getMaterial()==Material.DIAMOND_HOE && perm) {
								if (chloc.getBlock().getType()==Material.CHEST || chloc.getBlock().getType()==Material.TRAPPED_CHEST || chloc.getBlock().getType()==Material.BARREL) {
									player.sendMessage(InvManagement.$migs + "Creating Shop...");
									plugin.getConfig().set(configloc+".owner", "admin");
									plugin.getConfig().set(configloc+".price", 0.00);
									plugin.getConfig().set(configloc+".filter", false);
									plugin.getConfig().set(configloc+".buy", true);
									plugin.getConfig().set(configloc+".alerts", false);
									plugin.getConfig().set(configloc+".chestx", chloc.getBlockX());
									plugin.getConfig().set(configloc+".chestz", chloc.getBlockZ());
									plugin.getConfig().set(configloc+".chesty", blocky);
									plugin.getConfig().set(configloc+".chestw", blockw.getName());
									plugin.getConfig().set(configloc+".enabled", true);
									plugin.saveConfig();
									org.bukkit.block.Sign wop = (org.bukkit.block.Sign) event.getClickedBlock().getState();
									wop.setLine(0, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "{SHOP}");
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
}
