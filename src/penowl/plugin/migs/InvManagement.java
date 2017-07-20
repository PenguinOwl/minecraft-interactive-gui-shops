package penowl.plugin.migs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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

	public static ItemStack setName(ItemStack items, String name){
		ItemMeta meta = items.getItemMeta();
		meta.setDisplayName(name);
		items.setItemMeta(meta);
		return items;
	}

	public static Inventory createOwnerInventory(World ilw, int ilx, int ily, int ilz) {
		String configloc = "shops."+String.valueOf(ilw)+"."+String.valueOf(ilx)+"."+String.valueOf(ily)+"."+String.valueOf(ilz);
		Inventory temp = Bukkit.createInventory(new FakeHolder(), 45, "Owner Interface"); 
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
		temp.setItem(20, mmai(Material.DOUBLE_PLANT, 1, (short) 0, ChatColor.RESET + "$" + String.valueOf(plugin.getConfig().getInt(configloc+".price"))));
		temp.setItem(24, sbwool(plugin.getConfig().getBoolean(configloc+".buy")));
		//    	temp.setItem(5, mmai(Material.WEB, 1, (short) 0, ChatColor.RESET + "Filter?"));
		//    	temp.setItem(14, tfwool(plugin.getConfig().getBoolean(configloc+".filter")));
		return temp;
	}

	private ItemStack setOwner(ItemStack ad, String name) {
		SkullMeta base = (SkullMeta) ad.getItemMeta();
		base.setOwner(name);
		ad.setItemMeta(base);
		return ad;
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
		Inventory temp = Bukkit.createInventory(new FakeHolder(), 9, "Customer Interface"); 
		for(int x = 0; x < 9; x = x + 1) {
			ItemStack blank = setName(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4)," ");
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
		temp.setItem(0, mmai(Material.DOUBLE_PLANT, 1, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Price: $" + String.valueOf(plugin.getConfig().getString(configloc + ".price"))));
		return temp;
	}

	public static Inventory createSellerInventory(World ilw, int ilx, int ily, int ilz, Player player) {
		String configloc = "shops."+String.valueOf(ilw)+"."+String.valueOf(ilx)+"."+String.valueOf(ily)+"."+String.valueOf(ilz);
		Inventory temp = Bukkit.createInventory(new FakeHolder(), 9, "Seller Interface"); 
		for(int x = 0; x < 9; x = x + 1) {
			ItemStack blank = setName(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 10)," ");
			temp.setItem(x, blank);
		}
		Location chestloc = new Location(Bukkit.getWorld(plugin.getConfig().getString(configloc + ".chestw")), plugin.getConfig().getInt(configloc + ".chestx"), plugin.getConfig().getInt(configloc + ".chesty"), plugin.getConfig().getInt(configloc + ".chestz"));
		ItemStack selling = fetchCurItem(chestloc);
		temp.setItem(4, taa(selling, 8));
		temp.setItem(5, taa(selling, 64));
		temp.setItem(3, taa(selling, 1));
		temp.setItem(0, mmai(Material.DOUBLE_PLANT, 1, (short) 0, ChatColor.RESET + "" + ChatColor.GREEN + "Value: $" + String.valueOf(plugin.getConfig().getString(configloc + ".price"))));
		return temp;
	}

	public static ItemStack tfwool(boolean bln) {
		if (bln) {
			return mmai(Material.WOOL, 1, (short) 5, ChatColor.RESET + "" + ChatColor.GREEN + "TRUE");
		} else {
			return mmai(Material.WOOL, 1, (short) 14, ChatColor.RESET + "" + ChatColor.RED + "FALSE");
		}
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

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
		if(command.getName().equalsIgnoreCase("masterinv")){
			Player player = (Player) sender;
			player.openInventory(createOwnerInventory(player.getWorld(),0,0,0));
			return true;
		}
		return false;
	}

}
