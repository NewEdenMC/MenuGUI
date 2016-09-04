package co.neweden.menugui.menu;

public abstract class MenuRunnable implements Runnable {

    protected MenuInstance instance;

    public MenuInstance getMenuInstance() {
        return instance;
    }

}
