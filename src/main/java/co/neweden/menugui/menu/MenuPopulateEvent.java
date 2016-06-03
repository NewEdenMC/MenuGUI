package co.neweden.menugui.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MenuPopulateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private MenuInstance menu;
    private Player opener;

    public MenuPopulateEvent(MenuInstance menuInstance, Player opener) {
        this.menu = menuInstance;
        this.opener = opener;
    }

    public MenuInstance getMenuInstance() { return menu; }

    public Player getOpener() { return opener; }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }

}
