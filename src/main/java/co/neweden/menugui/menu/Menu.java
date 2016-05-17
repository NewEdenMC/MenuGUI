package co.neweden.menugui.menu;

import co.neweden.menugui.Main;
import co.neweden.menugui.menu.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class Menu {

    private Main plugin;
    private String name;
    private String title;
    private String openCommand;
    private Command command;
    private Logger logger;
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

    public void openMenu(Player player) {
        Inventory inv = Bukkit.createInventory(player, rows * 9, getTitle());
        MenuInstance instance = new MenuInstance(this, inv);
        MenuPopulateEvent event = new MenuPopulateEvent(instance);
        Bukkit.getPluginManager().callEvent(event);
        /* Demo testing code */
        instance.getSlot(1).setMaterial(Material.DIRT).setDisplayName("Ima door!").addHoverText("Yep!");
        instance.getSlot(1).enableEnchantEffect(true);
        instance.getSlot(1).atTick(20).setMaterial(Material.STONE);
        instance.getSlot(1).atTick(40).setMaterial(Material.GRASS).enableEnchantEffect(false);
        instance.getSlot(1).atTick(60).setMaterial(Material.WOOD).clearHoverText().addHoverText("This item is a Minecraft item, it can be used for things, but right now we use it for stuff in this menu.");
        instance.getSlot(1).atTick(80).repeate();
        /* End demo testing code */
        for (Map.Entry<Integer, InventorySlot> slot : instance.slots.entrySet()) {
            slot.getValue().run(player, inv);
        }
        player.openInventory(inv);
    }

}
