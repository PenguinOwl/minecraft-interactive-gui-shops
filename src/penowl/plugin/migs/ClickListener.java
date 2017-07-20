package penowl.plugin.migs;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class ClickListener implements Listener {

	private static Plugin plugin;

	@SuppressWarnings("static-access")
	public ClickListener(Plugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void someoneKnocked(InventoryClickEvent event) {
		ItemStack clicked = event.getCurrentItem();
		Inventory inventory = event.getClickedInventory();
		Player player = (Player) event.getWhoClicked();
		int slot = event.getSlot();
		Location loc = player.getTargetBlock((Set<Material>) null, 10).getLocation();
		World w = loc.getWorld();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		Boolean nonadmin = false;
		String configloc = "shops."+String.valueOf(w)+"."+String.valueOf(x)+"."+String.valueOf(y)+"."+String.valueOf(z);
		if (inventory.getHolder() instanceof FakeHolder) {
			nonadmin = plugin.getConfig().getString(configloc+".owner").length() > 16;
		}
		if (inventory.getHolder() != null) {
			if (inventory.getName().equals("Owner Interface") && inventory.getHolder() instanceof FakeHolder) { 
				event.setCancelled(true);
				if (slot == 28) {
					plugin.getConfig().set(configloc+".price", plugin.getConfig().getInt(configloc+".price")-1);
				}
				if (slot == 29) {
					plugin.getConfig().set(configloc+".price", plugin.getConfig().getInt(configloc+".price")-10);
				}
				if (slot == 30) {
					plugin.getConfig().set(configloc+".price", plugin.getConfig().getInt(configloc+".price")-50);
				}
				if (slot == 10) {
					plugin.getConfig().set(configloc+".price", plugin.getConfig().getInt(configloc+".price")+1);
				}
				if (slot == 11) {
					plugin.getConfig().set(configloc+".price", plugin.getConfig().getInt(configloc+".price")+10);
				}
				if (slot == 12) {
					plugin.getConfig().set(configloc+".price", plugin.getConfig().getInt(configloc+".price")+50);
				}
				if (slot == 24) {
					if (plugin.getConfig().getBoolean(configloc+".buy")) {
						plugin.getConfig().set(configloc+".buy", false);
					} else {
						plugin.getConfig().set(configloc+".buy", true);
					}
				}
				inventory = InvManagement.createOwnerInventory(w,x,y,z);
				player.closeInventory();
				player.openInventory(inventory);
			}
			if (inventory.getName().equals("Customer Interface") && inventory.getHolder() instanceof FakeHolder) { 
				Block chest = (new Location(Bukkit.getWorld(plugin.getConfig().getString(configloc+".chestw")),plugin.getConfig().getInt(configloc+".chestx"),plugin.getConfig().getInt(configloc+".chesty"),plugin.getConfig().getInt(configloc+".chestz"))).getBlock();
				event.setCancelled(true);
				Boolean work = true;
				Chest chs = (Chest) chest.getState();
				Inventory sc = chs.getBlockInventory();
				if (slot == 3 && clicked.getType() != Material.BARRIER) {
					if (InvCtmr.economy.getBalance(Bukkit.getOfflinePlayer(player.getUniqueId())) >= (double) plugin.getConfig().getInt(configloc+".price")) {
						if (nonadmin) {
							work = InvManagement.removeItems(clicked, 1, sc, 27);
						}
						if (work) {
							InvCtmr.economy.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getInt(configloc+".price"));
							if (nonadmin) {
								InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.getConfig().getString(configloc+".owner"))), plugin.getConfig().getInt(configloc+".price"));
							}
							player.getInventory().addItem(clicked);
						}
					}
				}
				if (slot == 4 && clicked.getType() != Material.STAINED_GLASS_PANE) {
					if (InvCtmr.economy.getBalance(Bukkit.getOfflinePlayer(player.getUniqueId())) >= (double) plugin.getConfig().getInt(configloc+".price")*8) {
						if (nonadmin) {
							work = InvManagement.removeItems(clicked, 8, sc, 27);
						}
						if (work) {
							InvCtmr.economy.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getInt(configloc+".price")*8);
							if (nonadmin) {
								InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.getConfig().getString(configloc+".owner"))), plugin.getConfig().getInt(configloc+".price")*8);
							}
							player.getInventory().addItem(clicked);
						}
					}
				}
				if (slot == 5 && clicked.getType() != Material.STAINED_GLASS_PANE) {
					if (InvCtmr.economy.getBalance(Bukkit.getOfflinePlayer(player.getUniqueId())) >= (double) plugin.getConfig().getInt(configloc+".price")*64) {
						if (nonadmin) {
							work = InvManagement.removeItems(clicked, 64, sc, 27);
						}
						if (work) {
							InvCtmr.economy.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getInt(configloc+".price")*64);
							if (nonadmin) {
								InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.getConfig().getString(configloc+".owner"))), plugin.getConfig().getInt(configloc+".price")*64);
							}
							player.getInventory().addItem(clicked);
						}
					}
				}

				inventory = InvManagement.createCustomerInventory(w,x,y,z);
				player.closeInventory();
				player.openInventory(inventory);
			}
		}
		if (inventory.getName().equals("Seller Interface") && inventory.getHolder() instanceof FakeHolder) { 
			Block chest = (new Location(Bukkit.getWorld(plugin.getConfig().getString(configloc+".chestw")),plugin.getConfig().getInt(configloc+".chestx"),plugin.getConfig().getInt(configloc+".chesty"),plugin.getConfig().getInt(configloc+".chestz"))).getBlock();
			Chest chs = (Chest) chest.getState();
			event.setCancelled(true);
			if (slot == 3 && clicked.getType() != Material.BARRIER) {
				if (nonadmin) {
					if (InvCtmr.economy.getBalance(Bukkit.getOfflinePlayer(UUID.fromString(plugin.getConfig().getString(configloc+".owner")))) >= (double) plugin.getConfig().getInt(configloc+".price")) {
						Boolean work = InvManagement.removeItems(clicked, 1, player.getInventory(), 36);
						if (work) {
							InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getInt(configloc+".price"));
							if (nonadmin) {
								InvCtmr.economy.withdrawPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.getConfig().getString(configloc+".owner"))), plugin.getConfig().getInt(configloc+".price"));
							}
							if (nonadmin) {
								chs.getBlockInventory().addItem(clicked);
							}
						}
					}
				} else {
					Boolean work = InvManagement.removeItems(clicked, 1, player.getInventory(), 36);
					if (work) {
						InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getInt(configloc+".price")*1);
					}
				}
			}
			if (slot == 4 && clicked.getType() != Material.BARRIER) {
				if (nonadmin) {
					if (InvCtmr.economy.getBalance(Bukkit.getOfflinePlayer(UUID.fromString(plugin.getConfig().getString(configloc+".owner")))) >= (double) plugin.getConfig().getInt(configloc+".price")*8) {
						Boolean work = InvManagement.removeItems(clicked, 8, player.getInventory(), 36);
						if (work) {
							InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getInt(configloc+".price")*8);
						}
					}
				} else {
					Boolean work = InvManagement.removeItems(clicked, 8, player.getInventory(), 36);
					if (work) {
						InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getInt(configloc+".price")*8);
					}
				}
			}
			if (slot == 5 && clicked.getType() != Material.BARRIER) {
				if (nonadmin) {
					if (InvCtmr.economy.getBalance(Bukkit.getOfflinePlayer(UUID.fromString(plugin.getConfig().getString(configloc+".owner")))) >= (double) plugin.getConfig().getInt(configloc+".price")*64) {
						Boolean work = InvManagement.removeItems(clicked, 64, player.getInventory(), 36);
						if (work) {
							InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getInt(configloc+".price")*64);
						}
					}
				} else {
					Boolean work = InvManagement.removeItems(clicked, 64, player.getInventory(), 36);
					if (work) {
						InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getInt(configloc+".price")*64);
					}
				}
			}

			inventory = InvManagement.createSellerInventory(w,x,y,z,player);
			player.closeInventory();
			player.openInventory(inventory);
		}
	}
}
