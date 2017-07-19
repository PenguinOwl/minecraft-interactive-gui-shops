package penowl.plugin.migs;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class FakeHolder implements InventoryHolder{
	
	@Override
    public Inventory getInventory() {
        return null;
    }
	
}
