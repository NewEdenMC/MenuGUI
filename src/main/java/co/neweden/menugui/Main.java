package co.neweden.menugui;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        MenuGUI.plugin = this;
    }

}
