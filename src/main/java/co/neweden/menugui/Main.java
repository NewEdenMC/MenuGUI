package co.neweden.menugui;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    private Connection db;

    @Override
    public void onEnable() {
        MenuGUI.plugin = this;
        saveDefaultConfig();
        if (!getConfig().getBoolean("apiOnlyMode", false)) {
            loadDBConnection();
            loadDBMenus();
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
            db = DriverManager.getConnection(url, getConfig().getString("mysql.user", ""), getConfig().getString("mysql.password", ""));
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "An SQLException occurred while trying to connect to the database, the plugin will run in API only mode.", e);
            return false;
        }
        getLogger().log(Level.INFO, "Connected to MySQL Database");
        return true;
    }

    private void loadDBMenus() {
        getLogger().log(Level.INFO, "Preparing to initialize menus from database.");
        ResultSet rs;
        try {
            Statement st = db.createStatement();
            rs = st.executeQuery("SELECT * FROM menus;");
            while (rs.next()) {
                if (!rs.getBoolean("enabled")) continue;
                Menu menu = MenuGUI.newMenu(rs.getString("name"));
                menu.setTitle(rs.getString("title"));
                menu.setOpenCommand(rs.getString("command"));
                getLogger().log(Level.INFO, "Menu " + menu.getName() + " initialized");
            }
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "SQLException occurred while trying to get data from menus table of database.", e);
        }
        if (MenuGUI.getMenus().size() == 0) {
            getLogger().log(Level.INFO, "No menus initialized from databases, other plugins that use this API may now initialize their onw menus.");
        }
    }

}
