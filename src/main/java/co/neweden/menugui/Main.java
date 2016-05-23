package co.neweden.menugui;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        MenuGUI.plugin = this;
        saveDefaultConfig();
        if (!getConfig().getBoolean("apiOnlyMode", false)) {
            loadDBConnection();
            setupDB();
            Loader loader = new Loader();
            loader.loadDBMenus();
        } else
            getLogger().log(Level.INFO, "Based on the config option apiOnlyMode the plugin will run in API only mode.");
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

    private void setupDB() {
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
        }
    }

}
