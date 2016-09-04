package co.neweden.menugui.menu;

import org.bukkit.Material;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SlotFrame {

    protected Material material;
    public SlotFrame setMaterial(Material material) { this.material = material; return this; }

    protected Integer amount;
    public SlotFrame setAmount(Integer amount) { this.amount = amount; return this; }

    protected Short durability;
    public SlotFrame setDurability(Short durability) { this.durability = durability; return this; }

    protected Boolean enchantEffect;
    public SlotFrame enableEnchantEffect(Boolean enable) { enchantEffect = enable; return this; }

    protected String displayName;
    public SlotFrame setDisplayName(String displayName) { this.displayName = displayName; return this; }

    protected List<String> hoverText = new ArrayList<>();
    public SlotFrame addHoverText(String text) { hoverText.add(text); return this; }
    public SlotFrame addHoverText(List<String> text) { hoverText.addAll(text); return this; }
    public SlotFrame addHoverText(Blob text) throws SQLException {
        String hText = new String(text.getBytes(1, (int) text.length()));
        addHoverText(hText);
        return this;
    }

    protected Boolean clearHover = false;
    public SlotFrame clearHoverText() { clearHover = true; return this; }

    protected String command;
    protected boolean closeOnClick = true;
    public SlotFrame setClickCommand(String command) { setClickCommand(command, true); return this; }
    public SlotFrame setClickCommand(String command, boolean closeOnClick) { this.command = command; this.closeOnClick = closeOnClick; return this; }

    protected MenuRunnable runnable;
    public SlotFrame runOnClick(MenuRunnable runnable) { this.runnable = runnable; return this; }

    protected Boolean repeat = false;
    public SlotFrame repeat() { repeat = true; return this; }

}
