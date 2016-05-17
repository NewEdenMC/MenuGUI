package co.neweden.menugui.menu;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MenuPopulateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private MenuInstance menu;

    public MenuPopulateEvent(MenuInstance menuInstance) {
        this.menu = menuInstance;
    }

    public MenuInstance getMenuInstance() { return menu; }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

}
