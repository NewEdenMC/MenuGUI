package co.neweden.menugui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;
import java.sql.SQLException;
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
                case "create": createCommand(sender, args); break;
                default: sender.sendMessage(Util.formatString("&cThe sub-command you ran is not valid."));
            }
            return true;
        }

        sender.sendMessage(Util.formatString(
                "&bMenuGUI Sub-commands\n" +
                "&f- &areload&e: reload the plugin"
        ));
        if (MenuGUI.isAPIOnlyMode()) {
            sender.sendMessage(Util.formatString("&cMenuGUI is running in API only mode, therefor limited sub-commands are available"));
            return true;
        }
        sender.sendMessage(Util.formatString(
                "&f- &acreate&e: create new menu"
        ));
        return true;
    }

    private void reloadCommand(CommandSender sender) {
        if (plugin.reload())
            sender.sendMessage(Util.formatString("&aReloaded plugin."));
        else
            sender.sendMessage(Util.formatString("&cUnable to reload plugin, check the server console for errors."));
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
            ResultSet rs = MenuGUI.db.createStatement().executeQuery("SELECT name FROM menus;");
            if (!rs.isBeforeFirst()) {
                sender.sendMessage(Util.formatString("&cThe menu name you have provided already exists."));
            }
            MenuGUI.db.createStatement().execute("INSERT INTO `menus` (`name`, `enabled`) VALUES ('" + args[1] + "', '1');");
            MenuGUI.db.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS `menu_" + args[1] + "` (\n" +
                    "  `slot` int(11) NOT NULL,\n" +
                    "  `material` varchar(128) NOT NULL,\n" +
                    "  `displayName` varchar(128) DEFAULT NULL,\n" +
                    "  `amount` int(11) NOT NULL DEFAULT '1',\n" +
                    "  `durability` int(11) NOT NULL DEFAULT '-1',\n" +
                    "  `hoverText` blob,\n" +
                    "  `enchantEffect` tinyint(1) NOT NULL DEFAULT '0',\n" +
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

}
