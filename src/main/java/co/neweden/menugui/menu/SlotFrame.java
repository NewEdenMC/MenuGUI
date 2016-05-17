package co.neweden.menugui.menu;

import org.bukkit.Material;

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

    protected Boolean repeate = false;
    public SlotFrame repeate() { repeate = true; return this; }

}
