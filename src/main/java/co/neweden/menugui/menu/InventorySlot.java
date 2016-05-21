package co.neweden.menugui.menu;

import co.neweden.menugui.MenuGUI;
import co.neweden.menugui.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.ChatPaginator;

import java.util.*;
import java.util.logging.Level;

public class InventorySlot extends SlotFrame implements Listener {

    private MenuInstance menu;
    private Integer slot;
    private TreeMap<Integer, SlotFrame> keys = new TreeMap<>();
    private BukkitTask task;
    private String clickCommand;

    public InventorySlot(MenuInstance menu, Integer slot) {
        this.menu = menu;
        this.slot = slot;
        keys.put(0, this);
        Bukkit.getPluginManager().registerEvents(this, MenuGUI.getPlugin());
    }

    public SlotFrame atTick(Integer tick) {
        if (keys.containsKey(tick))
            return keys.get(tick);
        else {
            SlotFrame frame = new SlotFrame();
            keys.put(tick, frame);
            return frame;
        }
    }

    private void updateSlot(Integer tick, SlotFrame frame, ItemStack item) {
        try {
            if (tick == 0) item.setType(Material.AIR); // Reset ItemStack if we are at the start of the animation
            if (frame.material != null) item.setType(frame.material);
            if (frame.amount != null) item.setAmount(frame.amount);
            if (frame.durability != null) item.setDurability(frame.durability);
            if (frame.enchantEffect != null) {
                if (frame.enchantEffect)
                    item.addUnsafeEnchantment(Enchantment.LUCK, 1);
                else
                    item.removeEnchantment(Enchantment.LUCK);
            }
            ItemMeta meta = item.getItemMeta();
            if (frame.displayName != null) meta.setDisplayName(Util.formatString("&r" + frame.displayName));
            if (frame.hoverText.size() > 0) {
                List<String> lore = new ArrayList<>();
                if (!frame.clearHover && meta.getLore() != null) lore.addAll(meta.getLore());
                for (String text : frame.hoverText) {
                    text = Util.addLineBreaks(text, ChatPaginator.AVERAGE_CHAT_PAGE_WIDTH / 2);
                    String[] split = text.split("\n");
                    for (int i = 0; i < split.length; i++) {
                        lore.add(Util.formatString("&r" + split[i]));
                    }
                }
                meta.setLore(lore);
            }
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
            if (frame.command != null) clickCommand = frame.command;
            menu.inv.setItem(slot, item);
        } catch (Throwable e) {
            menu.getMenu().getLogger().log(Level.SEVERE, String.format("Exception occurred while updating slot %s at frame %s.", slot, tick), e);
        }
    }

    public void run() {
        final ItemStack item = new ItemStack(Material.AIR);
        if (keys.size() == 1) {
            updateSlot(0, keys.get(0), item);
            return;
        }
        task = new BukkitRunnable() {
            Integer counter = 0;
            @Override
            public void run() {
                if (keys.get(counter) != null) {
                    updateSlot(counter, keys.get(counter), item);
                    if (keys.get(counter).repeate) { counter = 0; return; }
                }
                counter++;
            }
        }.runTaskTimer(MenuGUI.getPlugin(), 0L, 1L);
    }

    HashMap<Player, String> commandQueue = new HashMap<>();

    @EventHandler (priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        if (!event.getInventory().equals(menu.inv)) return;
        if (task != null) task.cancel();
        if (commandQueue.containsKey(player)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.performCommand(commandQueue.get(player));
                    commandQueue.remove(player);
                }
            }.runTaskLater(MenuGUI.getPlugin(), 5L);
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!event.getClickedInventory().equals(menu.inv) || event.getSlot() != slot) return;
        if (clickCommand != null) {
            Player player = (Player) event.getWhoClicked();
            commandQueue.put(player, clickCommand);
            player.closeInventory();
        }
        event.setCancelled(true);
    }

}
