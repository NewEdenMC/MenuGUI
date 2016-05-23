package co.neweden.menugui;

import co.neweden.menugui.menu.InventorySlot;
import co.neweden.menugui.menu.Menu;
import co.neweden.menugui.menu.MenuInstance;
import co.neweden.menugui.menu.MenuPopulateEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class Loader implements Listener {

    private Set<Menu> managedMenus = new HashSet<>();

    public Loader() {
        Bukkit.getPluginManager().registerEvents(this, MenuGUI.getPlugin());
    }

    protected boolean loadDBMenus() {
        MenuGUI.getPlugin().getLogger().log(Level.INFO, "Preparing to initialize menus from database.");
        ResultSet rs;
        try {
            Statement st = MenuGUI.db.createStatement();
            rs = st.executeQuery("SELECT * FROM menus;");
            while (rs.next()) {
                if (!rs.getBoolean("enabled")) continue;
                Menu menu = MenuGUI.newMenu(rs.getString("name"));
                managedMenus.add(menu);
                menu.setTitle(rs.getString("title"));
                menu.setOpenCommand(rs.getString("command"), rs.getString("commandDescription"), rs.getString("commandPermissionNode"));
                menu.setNumRows(rs.getInt("rows"));
                MenuGUI.getPlugin().getLogger().log(Level.INFO, "Menu " + menu.getName() + " initialized");
            }
        } catch (SQLException e) {
            MenuGUI.getPlugin().getLogger().log(Level.SEVERE, "SQLException occurred while trying to get data from menus table of database.", e);
        }
        if (MenuGUI.getMenus().size() == 0) {
            MenuGUI.getPlugin().getLogger().log(Level.INFO, "No menus initialized from databases, other plugins that use this API may now initialize their onw menus.");
            return false;
        }
        return true;
    }

    @EventHandler
    public void onMenuPopulate(MenuPopulateEvent event) {
        if (!managedMenus.contains(event.getMenuInstance().getMenu())) return;
        MenuInstance instance = event.getMenuInstance();
        try {
            Statement st = MenuGUI.db.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM menu_" + instance.getMenu().getName() + ";");
            while (rs.next()) {
                InventorySlot slot = instance.getSlot(rs.getInt("slot"));
                slot.setMaterial(Material.getMaterial(rs.getString("material")));
                if (!rs.getString("displayName").isEmpty()) slot.setDisplayName(rs.getString("displayName"));
                slot.setAmount(rs.getInt("amount"));
                slot.setDurability(rs.getShort("durability"));
                slot.enableEnchantEffect(rs.getBoolean("enchantEffect"));
                if (rs.getBlob("hoverText").length() > 0) slot.addHoverText(rs.getBlob("hoverText"));
                if (rs.getBlob("animationJSON") != null) slot.animationFromJSON(rs.getBlob("animationJSON"));
                if (!rs.getString("clickCommand").isEmpty()) slot.setClickCommand(rs.getString("clickCommand"));
            }
        } catch (SQLException e) {
            instance.getMenu().getLogger().log(Level.SEVERE, "SQLException occurred while trying to populate data for managed menu.", e);
        }
    }

}
