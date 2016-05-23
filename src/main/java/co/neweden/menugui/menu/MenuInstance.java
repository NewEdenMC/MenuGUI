package co.neweden.menugui.menu;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;

public class MenuInstance {

    private Menu menu;
    protected HashMap<Integer, InventorySlot> slots = new HashMap<>();
    protected Inventory inv;

    public MenuInstance(Menu menu, Inventory inv) {
        this.menu = menu;
        this.inv = inv;
    }

    public Menu getMenu() { return menu; }

    public InventorySlot getSlot(Integer slot) {
        if (slots.containsKey(slot))
            return slots.get(slot);
        else {
            InventorySlot invSlot = new InventorySlot(this, slot);
            slots.put(slot, invSlot);
            return invSlot;
        }
    }

    public List<HumanEntity> getViewers() { return inv.getViewers(); }

    public void closeMenu() {
        for (HumanEntity human : getViewers()) {
            human.closeInventory();
        }
    }

}
