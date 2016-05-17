package co.neweden.menugui.menu;

import org.bukkit.plugin.PluginLogger;

import java.util.logging.LogRecord;

public class Logger extends PluginLogger {

    private Menu menu;

    public Logger(Menu menu) {
        super(menu.getPlugin());
        this.menu = menu;
        setParent(menu.getPlugin().getLogger());
    }

    @Override
    public void log(LogRecord logRecord) {
        logRecord.setMessage("[" + menu.getName() + "] " + logRecord.getMessage());
        super.log(logRecord);
    }

}
