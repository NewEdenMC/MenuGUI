package co.neweden.menugui;

import co.neweden.menugui.menu.Command;
import co.neweden.menugui.menu.InventorySlot;
import co.neweden.menugui.menu.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class Menu {

    private Main plugin;
    private String name;
    private String title;
    private String openCommand;
    private Command command;
    private Logger logger;
    private HashMap<Integer, InventorySlot> slots = new HashMap<>();
    private Integer rows = 1;

    public Menu(Main plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        title = name;
        logger = new Logger(this);
    }

    public Main getPlugin() { return plugin; }

    public String getName() { return name; }

    public Logger getLogger() { return logger; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getNumRows() { return rows; }
    public void setNumRows(Integer numRows) { rows = numRows; }

    public Command getOpenCommand() { return command; }
    public void setOpenCommand(String command) {
        this.command = new Command(this, command);
        this.command.register();
    }

    public InventorySlot getSlot(Integer slot) {
        if (slots.containsKey(slot))
            return slots.get(slot);
        else {
            InventorySlot invSlot = new InventorySlot(this, slot);
            slots.put(slot, invSlot);
            return invSlot;
        }
    }

    public void openMenu(Player player) {
        Inventory inv = Bukkit.createInventory(player, rows * 9, getTitle());
        /* Demo testing code */
        getSlot(1).setMaterial(Material.DIRT).setDisplayName("Ima door!").addHoverText("Yep!");
        getSlot(1).enableEnchantEffect(true);
        getSlot(1).atTick(20).setMaterial(Material.STONE);
        getSlot(1).atTick(40).setMaterial(Material.GRASS).enableEnchantEffect(false);
        getSlot(1).atTick(60).setMaterial(Material.WOOD).setDisplayName("No door!");
        getSlot(1).atTick(80).repeate();
        /* End demo testing code */
        for (Map.Entry<Integer, InventorySlot> slot : slots.entrySet()) {
            slot.getValue().run(player, inv);
        }
        player.openInventory(inv);
    }

}
