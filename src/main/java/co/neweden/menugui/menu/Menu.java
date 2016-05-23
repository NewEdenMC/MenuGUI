package co.neweden.menugui.menu;

import co.neweden.menugui.Main;
import co.neweden.menugui.menu.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Menu {

    private Main plugin;
    private String name;
    private String title;
    private String openCommand;
    private Command command;
    private Logger logger;
    private Integer rows = 1;
    private Set<MenuInstance> instances = new HashSet<>();

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
    public void setOpenCommand(String command, String description) { setOpenCommand(command, description, null); }
    public void setOpenCommand(String command, String description, String permissionsNode) {
        this.command = new Command(this, command, description, permissionsNode);
        this.command.register();
    }

    public Set<MenuInstance> getMenuInstances() { return new HashSet<>(instances); }

    public void openMenu(Player player) {
        Inventory inv = Bukkit.createInventory(player, rows * 9, getTitle());
        MenuInstance instance = new MenuInstance(this, inv);
        instances.add(instance);
        MenuPopulateEvent event = new MenuPopulateEvent(instance);
        Bukkit.getPluginManager().callEvent(event);
        for (Map.Entry<Integer, InventorySlot> slot : instance.slots.entrySet()) {
            slot.getValue().run();
        }
        player.openInventory(inv);
    }

}
