package co.neweden.menugui;

import co.neweden.menugui.menu.Menu;
import co.neweden.menugui.menu.MenuInstance;
import co.neweden.menugui.menu.SlotFrame;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

public class MenuGUI {

    protected static Main plugin;
    protected static Connection db;
    protected static Set<Menu> menus = new HashSet<>();
    protected static Boolean apiOnlyMode;

    private MenuGUI() { }

    public static Set<Menu> getMenus() { return new HashSet<>(menus); }

    public static Menu getMenu(String menuName) {
        for (Menu menu : getMenus()) {
            if (menu.getName().equals(menuName))
                return menu;
        }
        return null;
    }

    public static Menu newMenu(String menuName) {
        Menu menu = new Menu(plugin, menuName);
        menus.add(menu);
        return menu;
    }

    public static boolean unloadMenu(Menu menu) {
        if (menu.getOpenCommand() != null) {
            if (!menu.getOpenCommand().unregister()) return false;
        }
        if (!menus.remove(menu)) return false;
        for (MenuInstance instance : menu.getMenuInstances()) {
            instance.closeMenu();
        }
        return true;
    }

    public static Main getPlugin() { return plugin; }

    public static Boolean isAPIOnlyMode() { return apiOnlyMode; }

}
