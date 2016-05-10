package co.neweden.menugui.menu;

import co.neweden.menugui.Menu;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.logging.Level;

public class Command extends BukkitCommand {

    private Menu menu;
    private String name;

    public Command(Menu menu, String commandName) {
        super(commandName);
        this.menu = menu;
        name = commandName;
    }

    public void register() {
        if (menu.getPlugin().getCommand(name) != null) return;
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(name, this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            menu.getLogger().log(Level.SEVERE, "Unable to register command: " + name, e);
        }
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        menu.getLogger().info(name);
        menu.getLogger().log(Level.WARNING, name);
        return true;
    }
}
