package penowl.plugin.migs;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class FakeHolder implements InventoryHolder{
	
	public Location grl = null;
	public String name = null;
	
	@Override
    public Inventory getInventory() {
        return null;
    }
	
	public void setLoc(Location loc) {
		grl = loc;
	}
	
	public Location getLoc() {
		return grl;
	}
	
	public FakeHolder(Location loc, String tag) {
		grl = loc;
		name = tag;
		
	}
	
}
