package tld.plugin.migs;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class ClickListener implements Listener {
    @EventHandler
    public void someoneKnocked(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        Inventory inventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        if (inventory.getName().equals(InvCtmr.createOwnerInventory(player.getWorld(),0,0,0,0,true).getName())) { 
            event.setCancelled(true);
        }
    }
}
