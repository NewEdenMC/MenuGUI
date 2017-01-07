package co.neweden.menugui.menu;

import co.neweden.menugui.MenuGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MenuInstance implements Listener {

    private Menu menu;
    protected HashMap<Integer, InventorySlot> slots = new HashMap<>();
    protected Inventory inv;

    public MenuInstance(Menu menu, Inventory inv) {
        this.menu = menu;
        this.inv = inv;
        Bukkit.getPluginManager().registerEvents(this, MenuGUI.getPlugin());
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

    public Collection<HumanEntity> getViewers() { return new ArrayList<>(inv.getViewers()); }

    public void repopulate() {
        for (InventorySlot slot : slots.values()) {
            if (slot.task != null) slot.task.cancel();
        }
        slots.clear();
        for (HumanEntity viewer : getViewers()) {
            MenuPopulateEvent event = new MenuPopulateEvent(this, (Player) viewer);
            Bukkit.getPluginManager().callEvent(event);
        }
        for (int i = 0; i < menu.getNumRows() * 9; i++) {
            if (slots.containsKey(i)) {
                slots.get(i).run();
            } else
                inv.setItem(i, new ItemStack(Material.AIR));
        }
    }

    public void closeMenu() {
        for (HumanEntity human : getViewers()) {
            human.closeInventory();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(inv) && event.getView().getBottomInventory().equals(event.getClickedInventory()))
            event.setCancelled(true);
    }

}
