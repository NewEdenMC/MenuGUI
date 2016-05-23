package co.neweden.menugui;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

    Main plugin;

    public Commands(Main plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "reload": reloadCommand(sender); break;
                default: sender.sendMessage(Util.formatString("&cThe sub-command you ran is not valid."));
            }
            return true;
        }

        sender.sendMessage(Util.formatString(
                "&bMenuGUI Sub-commands\n" +
                "&f- &areload&e: reload the plugin"
        ));
        if (plugin.getConfig().getBoolean("apiOnlyMode", false)) {
            sender.sendMessage(Util.formatString("&cMenuGUI is running in API only mode, therefor limited sub-commands are available"));
            return true;
        }
        return true;
    }

    private void reloadCommand(CommandSender sender) {
        if (plugin.reload())
            sender.sendMessage(Util.formatString("&aReloaded plugin."));
        else
            sender.sendMessage(Util.formatString("&cUnable to reload plugin, check the server console for errors."));
    }

}
