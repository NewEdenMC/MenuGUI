package co.neweden.menugui.menu;

import org.bukkit.inventory.Inventory;

import java.util.HashMap;

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

}
