package _new.custom.cuilian.level;

import _new.custom.cuilian.attribute.Attribute;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class Level {

    public Integer levelValue;
    public List<String> levelString;
    public String quenchingProtectRuneString;
    public Boolean canSendNotice;
    public Boolean canUsingSuitEffect;
    public Attribute attribute;
    public ItemStack quenchingProtectRune;
    public Boolean hasQuenchingProtectRune;
    public Integer moveLevelUse;

    public Level(Integer levelValue, List<String> levelString, String quenchingProtectRuneString, Boolean canSendNotice, Boolean canUsingSuitEffect, Attribute attribute, ItemStack quenchingProtectRune, Boolean hasQuenchingProtectRune, Integer moveLevelUse) {
        this.levelValue = levelValue;
        this.levelString = levelString;
        this.quenchingProtectRuneString = quenchingProtectRuneString;
        this.canSendNotice = canSendNotice;
        this.canUsingSuitEffect = canUsingSuitEffect;
        this.attribute = attribute;
        this.quenchingProtectRune = quenchingProtectRune;
        this.hasQuenchingProtectRune = hasQuenchingProtectRune;
        this.moveLevelUse = moveLevelUse;
    }
}
