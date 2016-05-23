package co.neweden.menugui;

import co.neweden.menugui.menu.Menu;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;

public class MenuGUI {

    protected static Main plugin;
    protected static Connection db;
    protected static Set<Menu> menus = new HashSet<>();

    private MenuGUI() { }

    public static Set<Menu> getMenus() { return new HashSet<>(menus); }

    public static Menu newMenu(String menuName) {
        Menu menu = new Menu(plugin, menuName);
        menus.add(menu);
        return menu;
    }

    public static Main getPlugin() { return plugin; }

}
