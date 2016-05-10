package co.neweden.menugui;

import co.neweden.menugui.menu.Command;
import co.neweden.menugui.menu.Logger;

public class Menu {

    private Main plugin;
    private String name;
    private String title;
    private String openCommand;
    private Command command;
    private Logger logger;

    public Menu(Main plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        title = name;
        logger = new Logger(this);
    }

    public Main getPlugin() { return plugin; }

    public String getName() { return name; }

    public Logger getLogger() { return logger; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Command getOpenCommand() { return command; }
    public void setOpenCommand(String command) {
        this.command = new Command(this, command);
        this.command.register();
    }

}
