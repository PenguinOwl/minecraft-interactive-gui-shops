package penowl.plugin.migs;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.World;
import org.bukkit.block.Barrel;
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
		if (event.getClickedInventory() != null) {
			if (event.getClickedInventory().getHolder() != null) {
				if (event.getClickedInventory().getHolder() instanceof FakeHolder) {
					event.setCancelled(true);
					ItemStack clicked = event.getCurrentItem();
					Inventory inventory = event.getClickedInventory();
					Player player = (Player) event.getWhoClicked();
					int slot = event.getSlot();
					Location loc = null;
					if (inventory.getHolder() instanceof FakeHolder) {
						loc = ((FakeHolder) inventory.getHolder()).getLoc();
					}
					World w = loc.getWorld();
					int x = loc.getBlockX();
					int y = loc.getBlockY();
					int z = loc.getBlockZ();
					Boolean nonadmin = false;
					String configloc = "shops."+String.valueOf(w)+"."+String.valueOf(x)+"."+String.valueOf(y)+"."+String.valueOf(z);
					if (inventory.getHolder() != null) {
						if (inventory.getHolder() instanceof FakeHolder) {
							nonadmin = plugin.getConfig().getString(configloc+".owner").length() > 16;
						}
					}
					if (inventory.getHolder() != null) {
						if (((FakeHolder)inventory.getHolder()).name.equals("Owner Interface") && inventory.getHolder() instanceof FakeHolder) { 
							event.setCancelled(true);
							if (!event.isShiftClick()) {
								if (slot == 28 && plugin.getConfig().getDouble(configloc+".price") >= 1.00) {
									plugin.getConfig().set(configloc+".price", InvManagement.round(plugin.getConfig().getDouble(configloc+".price")-1.00, 2));
								}
								if (slot == 29 && plugin.getConfig().getDouble(configloc+".price") >= 10.00) {
									plugin.getConfig().set(configloc+".price", InvManagement.round(plugin.getConfig().getDouble(configloc+".price")-10.00, 2));
								}
								if (slot == 30 && plugin.getConfig().getDouble(configloc+".price") >= 50.00) {
									plugin.getConfig().set(configloc+".price", InvManagement.round(plugin.getConfig().getDouble(configloc+".price")-50.00, 2));
								}
								if (slot == 10) {
									plugin.getConfig().set(configloc+".price", InvManagement.round(plugin.getConfig().getDouble(configloc+".price")+1.00, 2));
								}
								if (slot == 11) {
									plugin.getConfig().set(configloc+".price", InvManagement.round(plugin.getConfig().getDouble(configloc+".price")+10.00, 2));
								}
								if (slot == 12) {
									plugin.getConfig().set(configloc+".price", InvManagement.round(plugin.getConfig().getDouble(configloc+".price")+50.00, 2));
								}
							} else {
								if (slot == 28 && plugin.getConfig().getDouble(configloc+".price") >= 0.01) {
									plugin.getConfig().set(configloc+".price", InvManagement.round(plugin.getConfig().getDouble(configloc+".price")-0.01, 2));
								}
								if (slot == 29 && plugin.getConfig().getDouble(configloc+".price") >= 0.10) {
									plugin.getConfig().set(configloc+".price", InvManagement.round(plugin.getConfig().getDouble(configloc+".price")-0.10, 2));
								}
								if (slot == 30 && plugin.getConfig().getDouble(configloc+".price") >= 0.50) {
									plugin.getConfig().set(configloc+".price", InvManagement.round(plugin.getConfig().getDouble(configloc+".price")-0.50, 2));
								}
								if (slot == 10) {
									plugin.getConfig().set(configloc+".price", InvManagement.round(plugin.getConfig().getDouble(configloc+".price")+0.01, 2));
								}
								if (slot == 11) {
									plugin.getConfig().set(configloc+".price", InvManagement.round(plugin.getConfig().getDouble(configloc+".price")+0.10, 2));
								}
								if (slot == 12) {
									plugin.getConfig().set(configloc+".price", InvManagement.round(plugin.getConfig().getDouble(configloc+".price")+0.50, 2));
								}
							}
							if (slot == 24) {
								if (plugin.getConfig().getBoolean(configloc+".buy")) {
									plugin.getConfig().set(configloc+".buy", false);
								} else {
									plugin.getConfig().set(configloc+".buy", true);
								}
							}
							inventory.setContents(InvManagement.createOwnerInventory(w,x,y,z).getContents());
						}
						if (((FakeHolder)inventory.getHolder()).name.equals("Customer Interface") && inventory.getHolder() instanceof FakeHolder) { 
							Block chest = (new Location(Bukkit.getWorld(plugin.getConfig().getString(configloc+".chestw")),plugin.getConfig().getInt(configloc+".chestx"),plugin.getConfig().getInt(configloc+".chesty"),plugin.getConfig().getInt(configloc+".chestz"))).getBlock();
							event.setCancelled(true);
							Boolean work = true;
							Inventory sc;
							if (chest.getState() instanceof Barrel) { 
								Barrel chdata = (Barrel) chest.getState();
								sc = chdata.getInventory();
							} else {
								Chest chdata = (Chest) chest.getState();
								sc = chdata.getBlockInventory();
							}
							Boolean refresh = true;
							DecimalFormat df = new DecimalFormat("#0.00");
							if (slot == 3) {
								if (InvCtmr.economy.getBalance(Bukkit.getOfflinePlayer(player.getUniqueId())) >= plugin.getConfig().getDouble(configloc+".price")) {
									if (nonadmin) {
										work = InvManagement.removeItems(clicked, 1, sc, 27);
									}
									if (work) {
										InvCtmr.economy.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getDouble(configloc+".price"));
										if (nonadmin) {
											InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.getConfig().getString(configloc+".owner"))), plugin.getConfig().getDouble(configloc+".price"));
										}
										player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Tone.C));
										player.sendMessage(ChatColor.AQUA+"Bought "+ChatColor.GREEN+"1 "+ChatColor.RESET+(!clicked.getItemMeta().hasDisplayName() ? clicked.getType().name().replace("_", " ").toLowerCase() : clicked.getItemMeta().getDisplayName())+ChatColor.RESET+ChatColor.AQUA+" for "+ChatColor.YELLOW+df.format(plugin.getConfig().getDouble(configloc+".price")*1));
										player.getInventory().addItem(clicked);
									}
								} else {
									InvManagement.inverror("Not enough money", player);
									refresh = false;
								}
							}
							if (slot == 4) {
								if (InvCtmr.economy.getBalance(Bukkit.getOfflinePlayer(player.getUniqueId())) >= (double) plugin.getConfig().getDouble(configloc+".price")*8) {
									if (nonadmin) {
										work = InvManagement.removeItems(clicked, 8, sc, 27);
									}
									if (work) {
										InvCtmr.economy.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getDouble(configloc+".price")*8);
										if (nonadmin) {
											InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.getConfig().getString(configloc+".owner"))), plugin.getConfig().getDouble(configloc+".price")*8);
										}
										player.getInventory().addItem(clicked);
										player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Tone.C));
										player.sendMessage(ChatColor.AQUA+"Bought "+ChatColor.GREEN+"8 "+ChatColor.RESET+(!clicked.getItemMeta().hasDisplayName() ? clicked.getType().name().replace("_", " ").toLowerCase() : clicked.getItemMeta().getDisplayName())+ChatColor.RESET+ChatColor.AQUA+" for "+ChatColor.YELLOW+df.format(plugin.getConfig().getDouble(configloc+".price")*8));
									}
								} else {
									InvManagement.inverror("Not enough money", player);
									refresh = false;
								}
							}
							if (slot == 5) {
								if (InvCtmr.economy.getBalance(Bukkit.getOfflinePlayer(player.getUniqueId())) >= (double) plugin.getConfig().getDouble(configloc+".price")*64) {
									if (nonadmin) {
										work = InvManagement.removeItems(clicked, 64, sc, 27);
									}
									if (work) {
										InvCtmr.economy.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getDouble(configloc+".price")*64);
										if (nonadmin) {
											InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(UUID.fromString(plugin.getConfig().getString(configloc+".owner"))), plugin.getConfig().getDouble(configloc+".price")*64);
										}
										player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Tone.C));
										player.sendMessage(ChatColor.AQUA+"Bought "+ChatColor.GREEN+"64 "+ChatColor.RESET+(!clicked.getItemMeta().hasDisplayName() ? clicked.getType().name().replace("_", " ").toLowerCase() : clicked.getItemMeta().getDisplayName())+ChatColor.RESET+ChatColor.AQUA+" for "+ChatColor.YELLOW+df.format(plugin.getConfig().getDouble(configloc+".price")*64));
										player.getInventory().addItem(clicked);
									}
								} else {
									InvManagement.inverror("Not enough money", player);
									refresh = false;
								}
							}

							if (refresh) {
								inventory.setContents(InvManagement.createCustomerInventory(w,x,y,z).getContents());
							}
						}
					}
					if (((FakeHolder)inventory.getHolder()).name.equals("Error") && inventory.getHolder() instanceof FakeHolder) {
						event.setCancelled(true);
					}
					if (((FakeHolder)inventory.getHolder()).name.equals("Seller Interface") && inventory.getHolder() instanceof FakeHolder) { 
						Block chest = (new Location(Bukkit.getWorld(plugin.getConfig().getString(configloc+".chestw")),plugin.getConfig().getInt(configloc+".chestx"),plugin.getConfig().getInt(configloc+".chesty"),plugin.getConfig().getInt(configloc+".chestz"))).getBlock();
						Chest chs = (Chest) chest.getState();
						event.setCancelled(true);
						Boolean refresh = true;
						DecimalFormat df = new DecimalFormat("#0.00");
						if (slot == 3 && clicked.getType() != Material.BARRIER) {
							if (nonadmin) {
								if (InvCtmr.economy.getBalance(Bukkit.getOfflinePlayer(UUID.fromString(plugin.getConfig().getString(configloc+".owner")))) >= (double) plugin.getConfig().getDouble(configloc+".price")*1) {
									Boolean work = InvManagement.removeItems(clicked, 1, player.getInventory(), 36);
									if (work) {
										InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getDouble(configloc+".price")*1);
										player.playNote(player.getLocation(), Instrument.BELL, Note.natural(0, Tone.F));
										player.sendMessage(ChatColor.LIGHT_PURPLE+"Sold "+ChatColor.GREEN+"1 "+ChatColor.RESET+(!clicked.getItemMeta().hasDisplayName() ? clicked.getType().name().replace("_", " ").toLowerCase() : clicked.getItemMeta().getDisplayName())+ChatColor.RESET+ChatColor.LIGHT_PURPLE+" for "+ChatColor.YELLOW+df.format(plugin.getConfig().getDouble(configloc+".price")*1));
									}
								} else {
									InvManagement.inverror("Owner does not have enough money", player);
									refresh = false;
								}
							} else {
								Boolean work = InvManagement.removeItems(clicked, 1, player.getInventory(), 36);
								if (work) {
									InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getDouble(configloc+".price")*1);
									player.playNote(player.getLocation(), Instrument.BELL, Note.natural(0, Tone.F));
									player.sendMessage(ChatColor.LIGHT_PURPLE+"Sold "+ChatColor.GREEN+"1 "+ChatColor.RESET+(!clicked.getItemMeta().hasDisplayName() ? clicked.getType().name().replace("_", " ").toLowerCase() : clicked.getItemMeta().getDisplayName())+ChatColor.RESET+ChatColor.LIGHT_PURPLE+" for "+ChatColor.YELLOW+df.format(plugin.getConfig().getDouble(configloc+".price")*1));
								}
							}
						}
						if (slot == 4 && clicked.getType() != Material.BARRIER) {
							if (nonadmin) {
								if (InvCtmr.economy.getBalance(Bukkit.getOfflinePlayer(UUID.fromString(plugin.getConfig().getString(configloc+".owner")))) >= (double) plugin.getConfig().getDouble(configloc+".price")*8) {
									Boolean work = InvManagement.removeItems(clicked, 8, player.getInventory(), 36);
									if (work) {
										InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getDouble(configloc+".price")*8);
										player.playNote(player.getLocation(), Instrument.BELL, Note.natural(0, Tone.F));
										player.sendMessage(ChatColor.LIGHT_PURPLE+"Sold "+ChatColor.GREEN+"8 "+ChatColor.RESET+(!clicked.getItemMeta().hasDisplayName() ? clicked.getType().name().replace("_", " ").toLowerCase() : clicked.getItemMeta().getDisplayName())+ChatColor.RESET+ChatColor.LIGHT_PURPLE+" for "+ChatColor.YELLOW+df.format(plugin.getConfig().getDouble(configloc+".price")*8));
									}
								} else {
									InvManagement.inverror("Owner does not have enough money", player);
									refresh = false;
								}
							} else {
								Boolean work = InvManagement.removeItems(clicked, 8, player.getInventory(), 36);
								if (work) {
									InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getDouble(configloc+".price")*8);
									player.playNote(player.getLocation(), Instrument.BELL, Note.natural(0, Tone.F));
									player.sendMessage(ChatColor.LIGHT_PURPLE+"Sold "+ChatColor.GREEN+"8 "+ChatColor.RESET+(!clicked.getItemMeta().hasDisplayName() ? clicked.getType().name().replace("_", " ").toLowerCase() : clicked.getItemMeta().getDisplayName())+ChatColor.RESET+ChatColor.LIGHT_PURPLE+" for "+ChatColor.YELLOW+df.format(plugin.getConfig().getDouble(configloc+".price")*8));
								}
							}
						}
						if (slot == 5 && clicked.getType() != Material.BARRIER) {
							if (nonadmin) {
								if (InvCtmr.economy.getBalance(Bukkit.getOfflinePlayer(UUID.fromString(plugin.getConfig().getString(configloc+".owner")))) >= (double) plugin.getConfig().getDouble(configloc+".price")*64) {
									Boolean work = InvManagement.removeItems(clicked, 64, player.getInventory(), 36);
									if (work) {
										InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getDouble(configloc+".price")*64);
										player.playNote(player.getLocation(), Instrument.BELL, Note.natural(0, Tone.F));
										player.sendMessage(ChatColor.LIGHT_PURPLE+"Sold "+ChatColor.GREEN+"64 "+ChatColor.RESET+(!clicked.getItemMeta().hasDisplayName() ? clicked.getType().name().replace("_", " ").toLowerCase() : clicked.getItemMeta().getDisplayName())+ChatColor.RESET+ChatColor.LIGHT_PURPLE+" for "+ChatColor.YELLOW+df.format(plugin.getConfig().getDouble(configloc+".price")*64));
									}
								} else {
									InvManagement.inverror("Owner does not have enough money", player);
									refresh = false;
								}
							} else {
								Boolean work = InvManagement.removeItems(clicked, 64, player.getInventory(), 36);
								if (work) {
									InvCtmr.economy.depositPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), plugin.getConfig().getDouble(configloc+".price")*64);
									player.playNote(player.getLocation(), Instrument.BELL, Note.natural(0, Tone.F));
									player.sendMessage(ChatColor.LIGHT_PURPLE+"Sold "+ChatColor.GREEN+"64 "+ChatColor.RESET+(!clicked.getItemMeta().hasDisplayName() ? clicked.getType().name().replace("_", " ").toLowerCase() : clicked.getItemMeta().getDisplayName())+ChatColor.RESET+ChatColor.LIGHT_PURPLE+" for "+ChatColor.YELLOW+df.format(plugin.getConfig().getDouble(configloc+".price")*64));
								}
							}
						}

						if (refresh) {
							inventory.setContents(InvManagement.createSellerInventory(w,x,y,z,player).getContents());
						}
					}
				}
			}
		}
	}
}
