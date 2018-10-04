package _new.custom.cuilian.newapi;

import _new.custom.cuilian.NewCustomCuiLian;
import static _new.custom.cuilian.NewCustomCuiLian.*;
import _new.custom.cuilian.language.Language;
import _new.custom.cuilian.level.Level;
import _new.custom.cuilian.stone.Stone;
import _new.custom.cuilian.variable.Variable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class NewAPI {

    Plugin plugin;

    //优化完成
    public NewAPI(Plugin p) {
        plugin = p;
    }

    public static List<String> replace(List<String> a, String b, String c) {
        List<String> ret = new ArrayList<>();
        for (String d : a) {
            ret.add(d.replace(b, c));
        }
        return ret;
    }

    //优化完成
    public static String getFileVersion() {
        if (ServerVersion.equalsIgnoreCase("1.9+") || ServerVersion.equalsIgnoreCase("1.13")) {
            return "-1_9";
        } else {
            return "-1_8";
        }
    }

    //优化完成
    public static ItemStack getItemInHand(Player p) {
        if (ServerVersion.equalsIgnoreCase("1.9+") || ServerVersion.equalsIgnoreCase("1.13")) {
            return p.getInventory().getItemInMainHand();
        }
        return p.getInventory().getItemInHand();
    }

    public static ItemStack getItemInOffHand(Player p) {
        if (ServerVersion.equalsIgnoreCase("1.9+") || ServerVersion.equalsIgnoreCase("1.13")) {
            return p.getInventory().getItemInOffHand();
        }
        return new ItemStack(Material.AIR);
    }

    //优化完成
    public static void setItemInHand(Player p, ItemStack i) {
        if (ServerVersion.equalsIgnoreCase("1.9+") || ServerVersion.equalsIgnoreCase("1.13")) {
            p.getInventory().setItemInMainHand(i);
        } else {
            p.getInventory().setItemInHand(i);
        }
    }

    //优化完成
    public static double getMaxHealth(Player p) {
        if (ServerVersion.equalsIgnoreCase("1.9+") || ServerVersion.equalsIgnoreCase("1.13")) {
            return p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        }
        return p.getMaxHealth();
    }

    //优化完成
    public static void setMaxHealth(Player p, double m) {
        if (ServerVersion.equalsIgnoreCase("1.9+") || ServerVersion.equalsIgnoreCase("1.13")) {
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(m);
        } else {
            p.setMaxHealth(m);
        }
    }

    //优化完成
    public static Level getLevelByQuenchingProtectRune(ItemStack i) {
        if (i != null) {
            if (i.hasItemMeta()) {
                if (i.getItemMeta().hasLore()) {
                    for (Level Key : customCuilianManager.customCuilianLevelList) {
                        if (Key.quenchingProtectRune != null) {
                            if (Key.quenchingProtectRune.getItemMeta().equals(i.getItemMeta())) {
                                return Key;
                            }
                        }
                    }
                }
            }
        }
        return customCuilianManager.NULLLevel;
    }

    //优化完成
    public static ItemStack cuilian(ItemStack cls, ItemStack item, Player p) {
        if (cls != null && item != null && p != null && customCuilianManager.ItemList.contains(item.getType())) {
            Level level = getLevelByItem(item);
            Stone stone = getStoneByItem(cls);
            int dx = getRandom(stone.dropLevel.get(0), stone.dropLevel.get(1)), sx = stone.riseLevel;
            double probability = Math.random() * 100 + 1;
            boolean flag = probability <= stone.levelProbability.get(getLevelByInteger(level.levelValue + sx));
            String sendMessage = null;
            if (flag) {
                item = NewAPI.setItemCuiLian(item, NewAPI.getLevelByInteger(level.levelValue + sx), p);
                sendMessage = Language.CAN_CUILIAN_PROMPT.replace("%s", customCuilianManager.customCuilianLevelList.get(level.levelValue + sx).levelString.get(0));
                if (level.levelValue + sx >= 5) {
                    Bukkit.broadcastMessage(Language.ALL_SERVER_PROMPT.replace("%p", p.getDisplayName()).replace("%d", cls.getItemMeta().getDisplayName()).replace("%s", customCuilianManager.customCuilianLevelList.get(level.levelValue + sx).levelString.get(0)));
                }
            } else {
                if (getLevelByQuenchingProtectRuneString(item).levelValue <= level.levelValue
                        && getLevelByQuenchingProtectRuneString(item) != customCuilianManager.NULLLevel
                        && level.levelValue - getLevelByQuenchingProtectRuneString(item).levelValue < stone.dropLevel.get(1)) {
                    dx = getRandom(0, level.levelValue - getLevelByQuenchingProtectRuneString(item).levelValue);
                    item = setItemCuiLian(item, NewAPI.getLevelByInteger(level.levelValue - dx), p);
                    sendMessage = Language.HAS_BAOHUFU_CUILIAN_OVER.replace("%s", NewAPI.getLevelByInteger(level.levelValue - dx).levelString.get(0)).replace("%d", String.valueOf(dx));
                } else {
                    if (level.levelValue - dx >= 0) {
                        item = setItemCuiLian(item, NewAPI.getLevelByInteger(level.levelValue - dx), p);
                        sendMessage = Language.CUILIAN_OVER.replace("%s", NewAPI.getLevelByInteger(level.levelValue - dx).levelString.get(0)).replace("%d", String.valueOf(dx));
                    } else {
                        item = setItemCuiLian(item, customCuilianManager.NULLLevel, p);
                        sendMessage = Language.CUILIAN_OVER_ZERO.replace("%d", String.valueOf(dx));
                    }
                }
            }
            p.sendMessage(sendMessage);
        }
        return item;
    }

    //优化完成
    public static ItemStack setItemCuiLian(ItemStack item, Level level, Player p) {
        if (customCuilianManager.ItemList.contains(item.getType())
                && level != customCuilianManager.NULLLevel) {
            List<String> lore = new ArrayList<>();
            if (item.getItemMeta().hasLore()) {
                lore = item.getItemMeta().getLore();
            }
            lore = NewAPI.cleanCuiLian(lore);
            lore.addAll(Language.UNDER_LINE);
            lore.addAll(level.levelString);
            lore.addAll(getLore(getListStringByType(item.getType()), level, item.getType()));
            if (lore.isEmpty()) {
                lore.add("");
            }
            lore = NewAPI.cleanQuenchingProtectRune(lore);
            ItemMeta meta = item.getItemMeta();
            meta.setLore(lore);
            item.setItemMeta(meta);
        } else if (level == customCuilianManager.NULLLevel) {
            List<String> lore = new ArrayList<>();
            if (item.getItemMeta().hasLore()) {
                lore = item.getItemMeta().getLore();
            }
            lore = NewAPI.cleanCuiLian(lore);
            lore = NewAPI.cleanQuenchingProtectRune(lore);
            ItemMeta meta = item.getItemMeta();
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    //优化完成
    public static List<String> cleanCuiLian(List<String> lore) {
        if (lore != null) {
            if (lore.containsAll(Language.UNDER_LINE)) {
                lore.removeAll(Language.UNDER_LINE);
            }
            for (Level Key : customCuilianManager.customCuilianLevelList) {
                if (lore.containsAll(Key.levelString)) {
                    lore.removeAll(Key.levelString);
                }
            }
            for (int j = 0; j < lore.size() && j >= 0; j++) {
                if (j >= 0) {
                    if (lore.get(j).contains(Language.FIRST)) {
                        lore.remove(j);
                        j--;
                    }
                }
            }
        }
        return lore;
    }

    //优化完成
    public static List<String> cleanQuenchingProtectRune(List<String> lore) {
        if (lore != null) {
            if (!lore.isEmpty()) {
                for (int j = 0; j < lore.size() && j >= 0; j++) {
                    for (Level l : customCuilianManager.customCuilianLevelList) {
                        if (l.hasQuenchingProtectRune && j >= 0) {
                            if (l.quenchingProtectRuneString.equalsIgnoreCase(lore.get(j)) && l.levelValue > getLevelByLoreList(lore).levelValue) {
                                lore.remove(j);
                                j--;
                            }
                        }
                    }
                }
            }
        }
        return lore;
    }

    //优化完成
    public static int getRandom(int min, int max) {
        if (min == max) {
            return 0;
        }
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    //优化完成
    public static Stone getStoneByItem(ItemStack i) {
        if (i != null) {
            if (i.hasItemMeta()) {
                if (i.getItemMeta().hasLore()) {
                    for (Stone Key : customCuilianManager.customCuilianStoneList) {
                        if (Key.cuiLianStone.getItemMeta().equals(i.getItemMeta())) {
                            return Key;
                        }
                    }
                }
            }
        }
        return customCuilianManager.NULLStone;
    }

    //优化完成
    public static Level getLevelByQuenchingProtectRuneString(ItemStack i) {
        Level l = customCuilianManager.NULLLevel;
        if (i != null) {
            if (i.hasItemMeta()) {
                if (i.getItemMeta().hasLore() && customCuilianManager.ItemList.contains(i.getType())) {
                    List<String> lore;
                    if (i.getItemMeta().hasLore()) {
                        lore = i.getItemMeta().getLore();
                    } else {
                        lore = new ArrayList<>();
                    }
                    for (String line : lore) {
                        for (Level Key : customCuilianManager.customCuilianLevelList) {
                            if (Key.quenchingProtectRuneString.equals(line) && Key.levelValue >= l.levelValue) {
                                l = Key;
                            }
                        }
                    }
                }
            }
        }
        return l;
    }

    //优化完成
    public static List<String> getListStringByType(Material itemid) {
        if (arms.contains(itemid)) {
            return powerArms;
        }
        if (boots.contains(itemid)) {
            return powerBoots;
        }
        if (chestplate.contains(itemid)) {
            return powerChestplate;
        }
        if (leggings.contains(itemid)) {
            return powerLeggings;
        }
        if (helmet.contains(itemid)) {
            return powerHelmet;
        }
        return null;
    }

    //优化完成
    public static String getType(Material itemid) {
        if (arms.contains(itemid)) {
            return "arms";
        }
        if (boots.contains(itemid)) {
            return "boots";
        }
        if (chestplate.contains(itemid)) {
            return "chestplate";
        }
        if (leggings.contains(itemid)) {
            return "leggings";
        }
        if (helmet.contains(itemid)) {
            return "helmet";
        }
        return "";
    }

    //优化完成
    public static List<String> getCuiLianTypeForLocal(Material itemid) {
        if (arms.contains(itemid)) {
            return localArms;
        }
        if (boots.contains(itemid)) {
            return localBoots;
        }
        if (chestplate.contains(itemid)) {
            return localChestplate;
        }
        if (leggings.contains(itemid)) {
            return localLeggings;
        }
        if (helmet.contains(itemid)) {
            return localHelmet;
        }
        return null;
    }

    //优化完成
    public static List<String> getLore(List<String> powerlist, Level level, Material itemid) {
        List<String> lore = new ArrayList<>();
        for (String str : powerlist) {
            if (str.equalsIgnoreCase("damage")) {
                for (String s : Language.DAMAGE_LORE) {
                    if (level.attribute.damage.get(getType(itemid)) != 0) {
                        lore.add(Language.FIRST + s.replace("%d", String.valueOf(level.attribute.damage.get(getType(itemid)))));
                    }
                }
            }
            if (str.equalsIgnoreCase("bloodSuck")) {
                for (String s : Language.BLOOD_SUCKING_LORE) {
                    if (level.attribute.bloodSuck.get(getType(itemid)) != 0) {
                        lore.add(Language.FIRST + s.replace("%d", String.valueOf(level.attribute.bloodSuck.get(getType(itemid)))));
                    }
                }
            }
            if (str.equalsIgnoreCase("experience")) {
                for (String s : Language.EXPRIENCE) {
                    if (level.attribute.experience.get(getType(itemid)) != 0) {
                        lore.add(Language.FIRST + s.replace("%d", String.valueOf(level.attribute.experience.get(getType(itemid)))));
                    }
                }
            }
            if (str.equalsIgnoreCase("defense")) {
                for (String s : Language.DEFENSE_LORE) {
                    if (level.attribute.defense.get(getType(itemid)) != 0) {
                        lore.add(Language.FIRST + s.replace("%d", String.valueOf(level.attribute.defense.get(getType(itemid)))));
                    }
                }
            }
            if (str.equalsIgnoreCase("reboundDamage")) {
                for (String s : Language.ANTI_INJURY_LORE) {
                    if (level.attribute.reboundDamage.get(getType(itemid)) != 0) {
                        lore.add(Language.FIRST + s.replace("%d", String.valueOf(level.attribute.reboundDamage.get(getType(itemid)))));
                    }
                }
            }
            if (str.equalsIgnoreCase("jump")) {
                for (String s : Language.JUMP_LORE) {
                    if (level.attribute.jump.get(getType(itemid)) != 0) {
                        lore.add(Language.FIRST + s.replace("%d", String.valueOf(level.attribute.jump.get(getType(itemid)))));
                    }
                }
            }
            for (Variable v : NewCustomCuiLian.variables) {
                if (str.equalsIgnoreCase(v.name)) {
                    for (String s : v.lore) {
                        if (level.attribute.list.get(NewCustomCuiLian.variables.indexOf(v)).get(getType(itemid)) != 0) {
                            lore.add(Language.FIRST + s.replace("%d", String.valueOf(level.attribute.list.get(NewCustomCuiLian.variables.indexOf(v)).get(getType(itemid)))));
                        }
                    }
                }
            }
        }
        return lore;
    }

    //优化完成
    public static ItemStack addCuilianQuenchingProtectRune(ItemStack i, ItemStack quenchingProtectRune) {
        if (i != null && quenchingProtectRune != null) {
            if (i.hasItemMeta() && quenchingProtectRune.hasItemMeta()) {
                if (i.getItemMeta().hasLore() && quenchingProtectRune.getItemMeta().hasLore()) {
                    if (NewAPI.getLevelByQuenchingProtectRune(quenchingProtectRune) != customCuilianManager.NULLLevel) {
                        ItemMeta meta = i.getItemMeta();
                        List<String> lore = new ArrayList();
                        if (meta.hasLore()) {
                            lore.addAll(meta.getLore());
                        }
                        lore = NewAPI.cleanQuenchingProtectRune(i.getItemMeta().getLore());
                        Level l = getLevelByQuenchingProtectRune(quenchingProtectRune);
                        lore.add(l.quenchingProtectRuneString);
                        meta.setLore(lore);
                        i.setItemMeta(meta);
                    }
                }
            }
        }
        return i;
    }

    //优化完成
    public static Double getExperience(List<ItemStack> itemlist) {
        Double value = 0D;
        for (ItemStack item : itemlist) {
            if (item != null) {
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().hasLore()) {
                        if (customCuilianManager.ItemList.contains(item.getType())) {
                            if (getLevelByItem(item) != customCuilianManager.NULLLevel) {
                                if (getListStringByType(item.getType()).contains("experience")) {
                                    value += getLevelByItem(item).attribute.experience.get(getType(item.getType()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return value;
    }

    //优化完成
    public static int getJump(List<ItemStack> itemlist) {
        int value = 0;
        for (ItemStack item : itemlist) {
            if (item != null) {
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().hasLore()) {
                        if (customCuilianManager.ItemList.contains(item.getType())) {
                            if (getLevelByItem(item) != customCuilianManager.NULLLevel) {
                                if (getListStringByType(item.getType()).contains("jump")) {
                                    value += getLevelByItem(item).attribute.jump.get(getType(item.getType()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return value;
    }

    //优化完成
    public static Double getDamage(List<ItemStack> itemlist) {
        Double value = 0D;
        for (ItemStack item : itemlist) {
            if (item != null) {
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().hasLore()) {
                        if (customCuilianManager.ItemList.contains(item.getType())) {
                            if (getLevelByItem(item) != customCuilianManager.NULLLevel) {
                                if (getListStringByType(item.getType()).contains("damage")) {
                                    value += getLevelByItem(item).attribute.damage.get(getType(item.getType()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return value;
    }

    //优化完成
    public static Double getBloodSuck(List<ItemStack> itemlist) {
        Double value = 0D;
        for (ItemStack item : itemlist) {
            if (item != null) {
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().hasLore()) {
                        if (customCuilianManager.ItemList.contains(item.getType())) {
                            if (getLevelByItem(item) != customCuilianManager.NULLLevel) {
                                if (getListStringByType(item.getType()).contains("bloodSuck")) {
                                    value += getLevelByItem(item).attribute.bloodSuck.get(getType(item.getType()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return value;
    }

    //优化完成
    public static Double getDefense(List<ItemStack> itemlist) {
        Double value = 0D;
        for (ItemStack item : itemlist) {
            if (item != null) {
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().hasLore()) {
                        if (customCuilianManager.ItemList.contains(item.getType())) {
                            if (getLevelByItem(item) != customCuilianManager.NULLLevel) {
                                if (getListStringByType(item.getType()).contains("defense")) {
                                    value += getLevelByItem(item).attribute.defense.get(getType(item.getType()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return value;
    }

    //优化完成
    public static Double getReboundDamage(List<ItemStack> itemlist) {
        Double value = 0D;
        for (ItemStack item : itemlist) {
            if (item != null) {
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().hasLore()) {
                        if (customCuilianManager.ItemList.contains(item.getType())) {
                            if (getLevelByItem(item) != customCuilianManager.NULLLevel) {
                                if (getListStringByType(item.getType()).contains("reboundDamage")) {
                                    value += getLevelByItem(item).attribute.reboundDamage.get(getType(item.getType()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return value;
    }

    //优化完成
    public static List<ItemStack> addAll(ItemStack i1, ItemStack i6, ItemStack i2, ItemStack i3, ItemStack i4, ItemStack i5) {
        List<ItemStack> item = new ArrayList<>();
        if (i1 != null) {
            if (i1.hasItemMeta()) {
                if (i1.getItemMeta().hasLore()) {
                    if (customCuilianManager.ItemList.contains(i1.getType())) {
                        if (NewAPI.getLevelByItem(i1) != customCuilianManager.NULLLevel) {
                            if (getCuiLianTypeForLocal(i1.getType()).contains("hand")) {
                                item.add(i1);
                            }
                        }
                    }
                }
            }
        }
        if (ServerVersion.equalsIgnoreCase("1.9+")) {
            if (i6 != null) {
                if (i6.hasItemMeta()) {
                    if (i6.getItemMeta().hasLore()) {
                        if (customCuilianManager.ItemList.contains(i6.getType())) {
                            if (NewAPI.getLevelByItem(i6) != customCuilianManager.NULLLevel) {
                                if (getCuiLianTypeForLocal(i6.getType()).contains("hand")) {
                                    item.add(i6);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (i2 != null) {
            if (i2.hasItemMeta()) {
                if (i2.getItemMeta().hasLore()) {
                    if (customCuilianManager.ItemList.contains(i2.getType())) {
                        if (NewAPI.getLevelByItem(i2) != customCuilianManager.NULLLevel) {
                            if (getCuiLianTypeForLocal(i2.getType()).contains("bag")) {
                                item.add(i2);
                            }
                        }
                    }
                }
            }
        }
        if (i3 != null) {
            if (i3.hasItemMeta()) {
                if (i3.getItemMeta().hasLore()) {
                    if (customCuilianManager.ItemList.contains(i3.getType())) {
                        if (NewAPI.getLevelByItem(i3) != customCuilianManager.NULLLevel) {
                            if (getCuiLianTypeForLocal(i3.getType()).contains("bag")) {
                                item.add(i3);
                            }
                        }
                    }
                }
            }
        }
        if (i4 != null) {
            if (i4.hasItemMeta()) {
                if (i4.getItemMeta().hasLore()) {
                    if (customCuilianManager.ItemList.contains(i4.getType())) {
                        if (NewAPI.getLevelByItem(i4) != customCuilianManager.NULLLevel) {
                            if (getCuiLianTypeForLocal(i4.getType()).contains("bag")) {
                                item.add(i4);
                            }
                        }
                    }
                }
            }
        }
        if (i5 != null) {
            if (i5.hasItemMeta()) {
                if (i5.getItemMeta().hasLore()) {
                    if (customCuilianManager.ItemList.contains(i5.getType())) {
                        if (NewAPI.getLevelByItem(i5) != customCuilianManager.NULLLevel) {
                            if (getCuiLianTypeForLocal(i5.getType()).contains("bag")) {
                                item.add(i5);
                            }
                        }
                    }
                }
            }
        }
        return item;
    }

    //优化完成
    public static Level getLevelByString(List<String> s) {
        for (Level l : NewCustomCuiLian.customCuilianManager.customCuilianLevelList) {
            if (l.levelString.equals(s)) {
                return l;
            }
        }
        return customCuilianManager.NULLLevel;
    }

    //优化完成
    public static Level getLevelByItem(ItemStack i) {
        if (i != null) {
            if (i.hasItemMeta()) {
                if (i.getItemMeta().hasLore()) {
                    for (Level Key : customCuilianManager.customCuilianLevelList) {
                        if (i.getItemMeta().getLore().containsAll(Key.levelString)) {
                            return Key;
                        }
                    }
                }
            }
        }
        return customCuilianManager.NULLLevel;
    }

    //优化完成
    public static Level getLevelByLoreList(List<String> lore) {
        if (lore != null) {
            if (!lore.isEmpty()) {
                for (Level Key : customCuilianManager.customCuilianLevelList) {
                    if (lore.containsAll(Key.levelString)) {
                        return Key;
                    }
                }
            }
        }
        return customCuilianManager.NULLLevel;
    }

    //优化完成
    public static Level getLevelByInteger(int s) {
        if (s > -1 && s <= Max) {
            return customCuilianManager.customCuilianLevelList.get(s);
        }
        return customCuilianManager.NULLLevel;
    }

    //优化完成
    public static boolean isStoneMapItemMetaHasItemMeta(ItemMeta meta) {
        if (meta != null) {
            for (Stone s : customCuilianManager.customCuilianStoneList) {
                if (s.cuiLianStone.hasItemMeta()) {
                    if (s.cuiLianStone.getItemMeta().equals(meta)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //优化完成
    public static boolean isQuenchingProtectRuneMapItemMetaHasItemMeta(ItemMeta meta) {
        if (meta != null) {
            for (Level s : customCuilianManager.customCuilianLevelList) {
                if (s.quenchingProtectRune != null) {
                    if (s.quenchingProtectRune.hasItemMeta()) {
                        if (s.quenchingProtectRune.getItemMeta().equals(meta)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //优化完成
    public static HashMap getHashMap(String s1, YamlConfiguration config, int i, String s2) {
        HashMap<String, Double> hashmap = new HashMap<>();
        if (config.get(i + "." + s1 + "." + s2) != null) {
            Double value = config.getDouble(i + "." + s1 + "." + s2);
            hashmap.put(s1, value);
        } else {
            hashmap.put(s1, 0D);
        }
        return hashmap;
    }

    public static List<ItemStack> removeDurability(List<ItemStack> items) {
        for (ItemStack item : items) {
            if (!item.getItemMeta().spigot().isUnbreakable()) {
                short nj = item.getDurability();
                if (nj - 1 > 0) {
                    item.setDurability((short) (nj - 1));
                } else {
                    item.setType(Material.AIR);
                }
            }
        }
        return items;
    }
}
