package co.neweden.menugui.menu;

import co.neweden.menugui.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.logging.Level;

public class Command extends BukkitCommand {

    private Menu menu;

    public Command(Menu menu, String commandName, String description, String permissionsNode) {
        super(commandName);
        this.menu = menu;
        if (description != null) setDescription(description);
        if (permissionsNode != null) {
            setPermission(permissionsNode);
            setPermissionMessage(Util.formatString("&cYou do not have permission to run that command."));
        }
    }

    public void register() {
        if (menu.getPlugin().getCommand(getName()) != null) return;
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(getName(), this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            menu.getLogger().log(Level.SEVERE, "Unable to register command: " + getName(), e);
        }
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (getPermission() != null) {
            if (!sender.hasPermission(getPermission())) {
                sender.sendMessage(getPermissionMessage());
                return true;
            }
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can run this command.");
            return true;
        }
        menu.openMenu((Player) sender);
        return true;
    }
}
