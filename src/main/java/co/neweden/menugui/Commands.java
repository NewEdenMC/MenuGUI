package co.neweden.menugui;

import co.neweden.menugui.menu.Menu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Commands implements CommandExecutor {

    Main plugin;

    public Commands(Main plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "reload": reloadCommand(sender); break;
                case "list": listCommand(sender); break;
                case "create": createCommand(sender, args); break;
                case "remove": removeCommand(sender, args); break;
                default: sender.sendMessage(Util.formatString("&cThe sub-command you ran is not valid."));
            }
            return true;
        }

        sender.sendMessage(Util.formatString(
                "&bMenuGUI Sub-commands\n" +
                "&f- &areload&e: reload the plugin\n" +
                "&f- &alist&f: list current menus"
        ));
        if (MenuGUI.isAPIOnlyMode()) {
            sender.sendMessage(Util.formatString("&cMenuGUI is running in API only mode, therefor limited sub-commands are available"));
            return true;
        }
        sender.sendMessage(Util.formatString(
                "&f- &acreate&e: create new menu\n" +
                "&f- &aremove&e: remove existing menu entry from database"
        ));
        return true;
    }

    private void reloadCommand(CommandSender sender) {
        if (plugin.reload())
            sender.sendMessage(Util.formatString("&aReloaded plugin."));
        else
            sender.sendMessage(Util.formatString("&cUnable to reload plugin, check the server console for errors."));
    }

    private void listCommand(CommandSender sender) {
        HashMap<String, String> menuInfo = new HashMap<>();
        for (Menu menu : MenuGUI.getMenus()) {
            menuInfo.put(menu.getName(), "&eAPI");
        }
        listCommandGetDBLIst(sender, menuInfo);
        if (menuInfo.isEmpty()) {
            sender.sendMessage(Util.formatString("&bNo available menus"));
            return;
        }
        sender.sendMessage(Util.formatString("&bAvailable menus:"));
        for (Map.Entry<String, String> entry : menuInfo.entrySet()) {
            sender.sendMessage(Util.formatString("&7- &f" + entry.getKey() + "&7: " + entry.getValue()));
        }
    }

    private void listCommandGetDBLIst(CommandSender sender, HashMap<String, String> menuInfo) {
        if (MenuGUI.db == null) return;
        try {
            ResultSet rs = MenuGUI.db.createStatement().executeQuery("SELECT name,enabled FROM menus;");
            while (rs.next()) {
                if (menuInfo.containsKey(rs.getString("name")))
                    menuInfo.put(rs.getString("name"), "&aDatabase (loaded)");
                else
                    menuInfo.put(rs.getString("name"), "&cDatabase (not loaded)");
            }
        } catch (SQLException e) {
            sender.sendMessage(Util.formatString("&cAn error has occurred, check the server console for any exceptions."));
            MenuGUI.getPlugin().getLogger().log(Level.SEVERE, "An SQL Exception occurred while trying to list menus", e);
        }
    }

    private void createCommand(CommandSender sender, String[] args) {
        if (MenuGUI.isAPIOnlyMode()) {
            sender.sendMessage(Util.formatString("&cThis command is not supported in API Only Mode"));
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(Util.formatString("&cYou did not specify a name for the menu, usage: create <name>"));
            return;
        }
        try {
            ResultSet rs = MenuGUI.db.createStatement().executeQuery("SELECT name FROM menus WHERE name='" + args[1] + "';");
            if (rs.isBeforeFirst()) {
                sender.sendMessage(Util.formatString("&cThe menu name you have provided already exists."));
                return;
            }
            MenuGUI.db.createStatement().executeUpdate("INSERT INTO `menus` (`name`, `enabled`) VALUES ('" + args[1] + "', '1');");
            MenuGUI.db.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS `menu_" + args[1] + "` (\n" +
                    "  `slot` int(11) NOT NULL,\n" +
                    "  `material` varchar(128) NOT NULL,\n" +
                    "  `displayName` varchar(128) DEFAULT NULL,\n" +
                    "  `amount` int(11) NOT NULL DEFAULT '1',\n" +
                    "  `durability` int(11) NOT NULL DEFAULT '-1',\n" +
                    "  `hoverText` blob,\n" +
                    "  `enchantEffect` tinyint(1) NOT NULL DEFAULT '0',\n" +
                    "  `headTextureHash` blob,\n" +
                    "  `animationJSON` blob,\n" +
                    "  `clickCommand` varchar(256) DEFAULT NULL,\n" +
                    "  PRIMARY KEY (`slot`)\n" +
                    ")"
            );
            sender.sendMessage(Util.formatString("&aMenu has been created, you can now configure it in the database, then run '/menugui reload' to load the menu."));
        } catch (SQLException e) {
            sender.sendMessage(Util.formatString("&cUnable to create menu, check the server console for errors."));
            MenuGUI.getPlugin().getLogger().log(Level.SEVERE, "Unable to create menu, an exception occurred while executing the SQL query.", e);
        }
    }

    private void removeCommand(CommandSender sender, String[] args) {
        if (MenuGUI.isAPIOnlyMode()) {
            sender.sendMessage(Util.formatString("&cThis command is not supported in API Only Mode"));
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(Util.formatString("&cYou did not specify the name of the menu to remove, usage: remove <name>"));
            return;
        }
        try {
            ResultSet rs = MenuGUI.db.createStatement().executeQuery("SELECT name FROM menus WHERE name='" + args[1] + "';");
            if (!rs.isBeforeFirst()) {
                sender.sendMessage(Util.formatString("&cThe menu name you have provided does not exists."));
                return;
            }
            MenuGUI.db.createStatement().executeUpdate("DELETE FROM menus WHERE name='" + args[1] + "';");

            Menu menu = MenuGUI.getMenu(args[1]);
            if (menu != null) {
                MenuGUI.unloadMenu(menu);
                sender.sendMessage(Util.formatString("&aMenu has been unloaded."));
            }

            sender.sendMessage(Util.formatString("&aMenu entry has been removed from the 'menus' database table, the database table 'menu_" + args[1] + "' has not been removed, however it is now safe to remove this table."));
        } catch (SQLException e) {
            sender.sendMessage(Util.formatString("&cUnable to remove menu, check the server console for errors."));
            MenuGUI.getPlugin().getLogger().log(Level.SEVERE, "Unable to remove menu, an exception occurred while executing the SQL query.", e);
        }
    }

}
