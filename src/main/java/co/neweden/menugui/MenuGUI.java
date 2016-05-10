package co.neweden.menugui;

import java.util.HashSet;
import java.util.Set;

public class MenuGUI {

    protected static Main plugin;
    private static Set<Menu> menus = new HashSet<>();

    private MenuGUI() { }

    public static Set<Menu> getMenus() { return new HashSet<>(menus); }

    public static Menu newMenu() {
        Menu menu = new Menu();
        menus.add(menu);
        return menu;
    }

}
