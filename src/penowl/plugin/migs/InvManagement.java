package penowl.plugin.migs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("unused")
public class InvManagement {

	public static String $migs = ChatColor.GREEN + "[MIGS] " + ChatColor.RESET;

	private static Plugin plugin;

	@SuppressWarnings("static-access")
	public InvManagement(Plugin plugin)
	{
		this.plugin = plugin;
	}

	public static ItemStack mmai(Material type, int amount, short dmg, String name) {
		ItemStack item = new ItemStack(type, amount, dmg);
		setName(item,name);
		return item;
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static ItemStack setName(ItemStack items, String name){
		ItemMeta meta = items.getItemMeta();
		meta.setDisplayName(name);
		items.setItemMeta(meta);
		return items;
	}

	public static Inventory createOwnerInventory(World ilw, int ilx, int ily, int ilz) {
		String configloc = "shops."+String.valueOf(ilw)+"."+String.valueOf(ilx)+"."+String.valueOf(ily)+"."+String.valueOf(ilz);
		Inventory temp = Bukkit.createInventory(new FakeHolder(new Location(ilw,ilx,ily,ilz)), 45, "Owner Interface"); 
		for(int x = 0; x < 45; x = x + 1) {
			ItemStack blank = setName(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15)," ");
			temp.setItem(x, blank);
		}
		temp.setItem(2, ifbook("Prices","To change prices by major amounts,","left-click.","To change prices by minor amounts,","right-click."));
		temp.setItem(28, mmai(Material.HOPPER, 1, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Price adjust: " + ChatColor.RESET + "-1"));
		temp.setItem(15, ifbook("Buy/Sell","A buy shop will let players buy","the first item in the chest from you.", "A sell shop will let players sell","their items until the chest is full."));
		temp.setItem(29, mmai(Material.HOPPER, 10, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Price adjust: " + ChatColor.RESET + "-10"));
		temp.setItem(30, mmai(Material.HOPPER, 50, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Price adjust: " + ChatColor.RESET + "-50"));
		temp.setItem(10, mmai(Material.SULPHUR, 1, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Price adjust: " + ChatColor.RESET + "+1"));
		temp.setItem(11, mmai(Material.SULPHUR, 10, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Price adjust: " + ChatColor.RESET + "+10"));
		temp.setItem(12, mmai(Material.SULPHUR, 50, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Price adjust: " + ChatColor.RESET + "+50"));
		temp.setItem(20, mmai(Material.DOUBLE_PLANT, 1, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Price: " + String.valueOf(plugin.getConfig().getDouble(configloc+".price"))));
		temp.setItem(24, sbwool(plugin.getConfig().getBoolean(configloc+".buy")));
		//    	temp.setItem(5, mmai(Material.WEB, 1, (short) 0, ChatColor.RESET + "Filter?"));
		//    	temp.setItem(14, tfwool(plugin.getConfig().getBoolean(configloc+".filter")));
		return temp;
	}

	public static ItemStack setOwner(String name) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(name);
		meta.setDisplayName(ChatColor.GREEN + "Owner: " + ChatColor.RESET + "" + name);
		skull.setItemMeta(meta);
		return skull;
	}

	public static String getName(String configloc) {
		if (plugin.getConfig().getString(configloc+".owner").length() > 16) {
			UUID kl = UUID.fromString(plugin.getConfig().getString(configloc+".owner"));
			OfflinePlayer player = Bukkit.getOfflinePlayer(kl);
			return player.getName();
		} else {
			return "Admin";
		}
	}

	public static ItemStack head(String codc) {
		return setOwner(getName(codc));
	}

	public static ItemStack fetchCurItem(Location loc) {
		Block b = loc.getBlock();
		ItemStack base = null;
		if (b.getType() == Material.CHEST||b.getType() == Material.TRAPPED_CHEST) {
			Chest cht = (Chest) b.getState();
			Inventory cinv = cht.getBlockInventory();
			String name = null;
			ItemStack c = null;
			for (int scan = 0; scan < 27; scan++) {
				if ((cinv.getItem(scan) == null || c != null)) {
				} else {
					c = cinv.getItem(scan);
				}
			}
			ItemStack[] inv = cinv.getContents();

			int count = 0;
			for(int i = 0; i < inv.length; i++) {
				if(inv[i] != null){
					if(inv[i].isSimilar(c)) {
						int temp = inv[i].getAmount();
						count= count + temp;
					}
				}
			}
			if (c != null) {
				base = cinv.getItem(cinv.first(c)).clone();
				base.setAmount(count);
			} else {
				base = mmai(Material.BARRIER,1,(short) 0,"Out of stock");
			}
		}
		return base;
	}

	public static ItemStack fetchCurItemp(Player p) {
		ItemStack base = null;
		Inventory cinv = p.getInventory();
		String name = null;
		ItemStack c = null;
		for (int scan = 0; scan < 36; scan++) {
			if ((cinv.getItem(scan) == null || c != null)) {
			} else {
				c = cinv.getItem(scan);
			}
		}
		return base;
	}

	public static ItemStack taa(ItemStack is, int amount) {
		ItemStack kg = is.clone();
		kg.setAmount(amount);
		return kg;
	}

	public static Inventory createCustomerInventory(World ilw, int ilx, int ily, int ilz) {
		String configloc = "shops."+String.valueOf(ilw)+"."+String.valueOf(ilx)+"."+String.valueOf(ily)+"."+String.valueOf(ilz);
		Inventory temp = Bukkit.createInventory(new FakeHolder(new Location(ilw,ilx,ily,ilz)), 18, "Customer Interface"); 
		for(int x = 0; x < 18; x = x + 1) {
			ItemStack blank = setName(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4),"BUY");
			temp.setItem(x, blank);
		}
		Location chestloc = new Location(Bukkit.getWorld(plugin.getConfig().getString(configloc + ".chestw")), plugin.getConfig().getInt(configloc + ".chestx"), plugin.getConfig().getInt(configloc + ".chesty"), plugin.getConfig().getInt(configloc + ".chestz"));
		ItemStack selling = fetchCurItem(chestloc);
		if (selling.getAmount()>7) {
			temp.setItem(4, taa(selling, 8));
		}
		if (selling.getAmount()>63) {
			temp.setItem(5, taa(selling, 64));
		}
		temp.setItem(3, taa(selling, 1));
		temp.setItem(0, mmai(Material.DOUBLE_PLANT, 1, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Price: " + String.valueOf(plugin.getConfig().getDouble(configloc + ".price"))));
		temp.setItem(12, mmai(Material.DOUBLE_PLANT, 1, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Price: " + String.valueOf(plugin.getConfig().getDouble(configloc + ".price")*1)));
		temp.setItem(13, mmai(Material.DOUBLE_PLANT, 1, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Price: " + String.valueOf(plugin.getConfig().getDouble(configloc + ".price")*8)));
		temp.setItem(14, mmai(Material.DOUBLE_PLANT, 1, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Price: " + String.valueOf(plugin.getConfig().getDouble(configloc + ".price")*64)));
		temp.setItem(17, head(configloc));
		return temp;
	}

	public static Inventory createSellerInventory(World ilw, int ilx, int ily, int ilz, Player player) {
		String configloc = "shops."+String.valueOf(ilw)+"."+String.valueOf(ilx)+"."+String.valueOf(ily)+"."+String.valueOf(ilz);
		Inventory temp = Bukkit.createInventory(new FakeHolder(new Location(ilw,ilx,ily,ilz)), 18, "Seller Interface"); 
		for(int x = 0; x < 18; x = x + 1) {
			ItemStack blank = setName(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 10),"SELL");
			temp.setItem(x, blank);
		}
		Location chestloc = new Location(Bukkit.getWorld(plugin.getConfig().getString(configloc + ".chestw")), plugin.getConfig().getInt(configloc + ".chestx"), plugin.getConfig().getInt(configloc + ".chesty"), plugin.getConfig().getInt(configloc + ".chestz"));
		ItemStack selling = fetchCurItem(chestloc);
		Chest chdata = (Chest) chestloc.getBlock().getState();
		Inventory chest = chdata.getBlockInventory();
		if (getSpace(chest,selling)>=8) {
			temp.setItem(4, taa(selling, 8));
		} else {
			temp.setItem(4, mmai(Material.BARRIER,1,(short) 0,"Not enough space"));
		}
		if (getSpace(chest,selling)>=64) {
			temp.setItem(5, taa(selling, 64));
		} else {
			temp.setItem(5, mmai(Material.BARRIER,1,(short) 0,"Not enough space"));
		}
		if (getSpace(chest,selling)>=1) {
			temp.setItem(3, taa(selling, 1));
		} else {
			temp.setItem(3, mmai(Material.BARRIER,1,(short) 0,"Not enough space"));
		}
		temp.setItem(0, mmai(Material.DOUBLE_PLANT, 1, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Value: " + String.valueOf(plugin.getConfig().getDouble(configloc + ".price"))));
		temp.setItem(12, mmai(Material.DOUBLE_PLANT, 1, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Value: " + String.valueOf(plugin.getConfig().getDouble(configloc + ".price")*1)));
		temp.setItem(13, mmai(Material.DOUBLE_PLANT, 1, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Value: " + String.valueOf(plugin.getConfig().getDouble(configloc + ".price")*8)));
		temp.setItem(14, mmai(Material.DOUBLE_PLANT, 1, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Value: " + String.valueOf(plugin.getConfig().getDouble(configloc + ".price")*64)));
		temp.setItem(17, head(configloc));
		return temp;
	}

	public static ItemStack tfwool(boolean bln) {
		if (bln) {
			return mmai(Material.WOOL, 1, (short) 5, ChatColor.RESET + "" + ChatColor.GREEN + "TRUE");
		} else {
			return mmai(Material.WOOL, 1, (short) 14, ChatColor.RESET + "" + ChatColor.RED + "FALSE");
		}
	}

	public static int getSpace(Inventory inventory, ItemStack cur) {
		int count = 0;
		for(int x = 0; x < inventory.getSize(); x++) {
			if (inventory.getItem(x)==null) {
				count = count + cur.getMaxStackSize();
			} else if (inventory.getItem(x).isSimilar(cur)) {
				count = count + cur.getMaxStackSize() - inventory.getItem(x).getAmount();
			}
		}
		return count;
	}

	public static ItemStack sbwool(boolean bln) {
		if (bln) {
			return mmai(Material.WOOL, 1, (short) 4, ChatColor.RESET + "" + ChatColor.YELLOW + "BUY");
		} else {
			return mmai(Material.WOOL, 1, (short) 10, ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "SELL");
		}
	}

	public static Boolean removeItems(ItemStack is, int amount, Inventory ch, int tick) {
		ItemStack[] backup = ch.getContents();
		ItemStack c1 = is.clone();
		Boolean suc = true;
		ItemStack c2 = null;
		ItemStack k = null;
		fail:
			for (int sd = 0; sd != amount; sd++) {
				if1:
					for (int c = 0; c < tick; c++) {
						if (ch.getItem(c) != null) {
							c2 = ch.getItem(c).clone();
							if (c1.isSimilar(c2)) {
								k = ch.getItem(c).clone();
								k.setAmount(ch.getItem(c).getAmount()-1);
								ch.setItem(c, k);
								break if1;
							}
						}
						if (c == tick - 1) {
							suc = false;
							break fail;
						}
					}
			}
		if (!suc) {
			ch.setContents(backup);
		}
		return suc;
	}
	
	public static ItemStack ifbook(String name, String s1, String s2, String s3, String s4) {
		ItemStack item = mmai(Material.BOOK, 1, (short) 0, ChatColor.RESET+""+ChatColor.AQUA+name);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.RESET+""+ChatColor.GRAY+s1);
		lore.add(ChatColor.RESET+""+ChatColor.GRAY+s2);
		lore.add(ChatColor.RESET+""+ChatColor.GRAY+s3);
		lore.add(ChatColor.RESET+""+ChatColor.GRAY+s4);
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.FROST_WALKER, 1, true);
		item.setItemMeta(meta);
		return item;
	}

	public static void inverror(String error, Player player) {
		Inventory temp = Bukkit.createInventory(new FakeHolder(new Location(null,0,0,0)), 18, "ERROR"); 
		for(int x = 0; x < 18; x = x + 1) {
			ItemStack blank = setName(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14),error);
			temp.setItem(x, blank);
		}
		player.closeInventory();
		player.openInventory(temp);
	}

}
