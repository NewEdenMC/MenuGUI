package co.neweden.menugui;

import co.neweden.menugui.menu.Menu;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        MenuGUI.plugin = this;
        startup();
        getCommand("menugui").setExecutor(new Commands(this));
    }

    private boolean startup() {
        saveDefaultConfig();
        MenuGUI.apiOnlyMode = getConfig().getBoolean("apiOnlyMode", false);
        if (!MenuGUI.isAPIOnlyMode()) {
            if (!loadDBConnection()) return false;
            if (!setupDB()) return false;
            Loader loader = new Loader();
            if (!loader.loadDBMenus()) return false;
        } else
            getLogger().log(Level.INFO, "Based on the config option apiOnlyMode the plugin will run in API only mode.");
        return true;
    }

    public boolean reload() {
        for (Menu menu : MenuGUI.getMenus()) {
            if (!MenuGUI.unloadMenu(menu)) return false;
        }

        MenuGUI.menus.clear();
        if (!MenuGUI.menus.isEmpty()) return false;

        try {
            MenuGUI.db.close();
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "Unable to close database connection", e);
            return false;
        }

        return startup();
    }

    private boolean loadDBConnection() {
        String host = getConfig().getString("mysql.host", null);
        String port = getConfig().getString("mysql.port", null);
        String database = getConfig().getString("mysql.database", null);
        if (host == null || port == null || database == null) {
            getLogger().log(Level.INFO, "No database information received from config, the plugin will run in API only mode.");
            return false;
        }

        String url = String.format("jdbc:mysql://%s:%s/%s", host, port, database);

        try {
            MenuGUI.db = DriverManager.getConnection(url, getConfig().getString("mysql.user", ""), getConfig().getString("mysql.password", ""));
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "An SQLException occurred while trying to connect to the database, the plugin will run in API only mode.", e);
            return false;
        }
        getLogger().log(Level.INFO, "Connected to MySQL Database");
        return true;
    }

    private boolean setupDB() {
        try {
            MenuGUI.db.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS `menus` (\n" +
                    "  `name` varchar(32) NOT NULL,\n" +
                    "  `enabled` tinyint(1) NOT NULL DEFAULT '1',\n" +
                    "  `title` varchar(128) DEFAULT NULL,\n" +
                    "  `rows` int(11) DEFAULT '1',\n" +
                    "  `command` varchar(64) DEFAULT NULL,\n" +
                    "  `commandDescription` varchar(128) DEFAULT NULL,\n" +
                    "  `commandPermissionNode` varchar(128) DEFAULT NULL,\n" +
                    "  PRIMARY KEY (`name`)\n" +
                    ")"
            );
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "Unable to setup setup database", e);
            return false;
        }
        return true;
    }

}
