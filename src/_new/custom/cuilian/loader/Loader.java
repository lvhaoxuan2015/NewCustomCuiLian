package _new.custom.cuilian.loader;

import _new.custom.cuilian.NewCustomCuiLian;
import static _new.custom.cuilian.NewCustomCuiLian.*;
import _new.custom.cuilian.attribute.Attribute;
import _new.custom.cuilian.level.Level;
import _new.custom.cuilian.newapi.NewAPI;
import _new.custom.cuilian.stone.Stone;
import _new.custom.cuilian.variable.Variable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Loader {

    public static void readCuiLianItem(Plugin p) {
        customCuilianManager.ItemList.clear();
        File file1 = new File(p.getDataFolder(), "Item.yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            for (String str : config1.getStringList("arms")) {
                arms.add(Material.getMaterial(str));
            }
            for (String str : config1.getStringList("helmet")) {
                helmet.add(Material.getMaterial(str));
            }
            for (String str : config1.getStringList("chestplate")) {
                chestplate.add(Material.getMaterial(str));
            }
            for (String str : config1.getStringList("leggings")) {
                leggings.add(Material.getMaterial(str));
            }
            for (String str : config1.getStringList("boots")) {
                boots.add(Material.getMaterial(str));
            }
            customCuilianManager.ItemList.addAll(arms);
            customCuilianManager.ItemList.addAll(helmet);
            customCuilianManager.ItemList.addAll(chestplate);
            customCuilianManager.ItemList.addAll(leggings);
            customCuilianManager.ItemList.addAll(boots);
        } else {
            p.saveResource("Item.yml", true);
            readCuiLianItem(p);
        }
        powerArms.clear();
        powerHelmet.clear();
        powerChestplate.clear();
        powerLeggings.clear();
        powerBoots.clear();
        File file2 = new File(p.getDataFolder(), "power.yml");
        YamlConfiguration config2 = YamlConfiguration.loadConfiguration(file2);
        if (file2.exists()) {
            powerArms = config2.getStringList("arms");
            powerHelmet = config2.getStringList("helmet");
            powerChestplate = config2.getStringList("chestplate");
            powerLeggings = config2.getStringList("leggings");
            powerBoots = config2.getStringList("boots");
        } else {
            p.saveResource("power.yml", true);
            readCuiLianItem(p);
        }
        localArms.clear();
        localHelmet.clear();
        localChestplate.clear();
        localLeggings.clear();
        localBoots.clear();
        File file3 = new File(p.getDataFolder(), "local.yml");
        YamlConfiguration config3 = YamlConfiguration.loadConfiguration(file3);
        if (file3.exists()) {
            localArms = config3.getStringList("arms");
            localHelmet = config3.getStringList("helmet");
            localChestplate = config3.getStringList("chestplate");
            localLeggings = config3.getStringList("leggings");
            localBoots = config3.getStringList("boots");
        } else {
            p.saveResource("local.yml", true);
            readCuiLianItem(p);
        }
    }

    public static void readYml(Plugin p) {
        customCuilianManager.customCuilianLevelList.clear();
        File file0 = new File(p.getDataFolder(), "config.yml");
        YamlConfiguration config0 = YamlConfiguration.loadConfiguration(file0);
        if (file0.exists()) {
            NewCustomCuiLian.usingDefaultPower = config0.getBoolean("UsingDefaultPower");
        } else {
            p.saveResource("config.yml", true);
            readYml(p);
        }
        File file1 = new File(p.getDataFolder(), "cuilian" + NewAPI.getFileVersion() + ".yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            int i;
            for (i = 0; config1.getString(i + ".lore") != null; i++) {
                Attribute a = E(config1, i);
                boolean notice = config1.getBoolean(i + ".Notice");
                List<String> cuilianLore = NewAPI.replace(config1.getStringList(i + ".lore"), "&", "§");
                String DisplayName = "";
                List<String> lore = new ArrayList<>();
                Material itemtype = null;
                String addLore = "";
                ItemStack item = null;
                ItemMeta im = null;
                if (config1.getBoolean(i + ".quenchingProtectRune")) {
                    DisplayName = config1.getString(i + ".quenchingProtectRuneItem.item.DisplayName").replace("&", "§");
                    lore = NewAPI.replace(config1.getStringList(i + ".quenchingProtectRuneItem.item.Lore"), "&", "§");
                    itemtype = Material.getMaterial(config1.getString(i + ".quenchingProtectRuneItem.item.Type"));
                    addLore = config1.getString(i + ".quenchingProtectRuneItem.item.addLore").replace("&", "§");
                    item = new ItemStack(itemtype);
                    im = item.getItemMeta();
                    im.setDisplayName(DisplayName);
                    im.setLore(lore);
                    item.setItemMeta(im);
                }
                Level level = new Level(i, cuilianLore, addLore, notice, config1.getBoolean(i + ".suitEffect"), a, item, config1.getBoolean(i + ".quenchingProtectRune"), config1.getInt(i + ".moveLevelUse"));
                customCuilianManager.customCuilianLevelList.add(level);
                for (Stone stone : customCuilianManager.customCuilianStoneList) {
                    String StoneId = stone.id;
                    HashMap<Level, Double> LevelProbability = stone.levelProbability;
                    Double probability = 0D;
                    if (config1.get(i + "." + StoneId) != null) {
                        probability = config1.getDouble(i + "." + StoneId);
                        LevelProbability.put(level, probability);
                    } else {
                        LevelProbability.put(level, 100D);
                    }
                }
            }
            Max = i - 1;
        } else {
            p.saveResource("cuilian" + NewAPI.getFileVersion() + ".yml", true);
            readYml(p);
        }
    }

    public static void readItem(Plugin p) {
        customCuilianManager.customCuilianStoneList.clear();
        File file1 = new File(p.getDataFolder(), "cuilianStone" + NewAPI.getFileVersion() + ".yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            for (int i = 0; config1.get(i + ".DisplayName") != null; i++) {
                String name = config1.getString(i + ".DisplayName").replace("&", "§");
                String stoneId = config1.getString(i + ".Id").replace("&", "§");
                List<String> lore = NewAPI.replace(config1.getStringList(i + ".Lore"), "&", "§");
                int riseLevel = config1.getInt(i + ".riseLevel");
                Material itemtype = Material.getMaterial(config1.getString(i + ".Type"));
                List<Integer> dropLevel = config1.getIntegerList(i + ".dropLevel");
                ItemStack item = new ItemStack(itemtype);
                ItemMeta id = item.getItemMeta();
                id.addEnchant(Enchantment.OXYGEN, 1, true);
                id.setDisplayName(name);
                id.setLore(lore);
                item.setItemMeta(id);
                Stone s = new Stone(item, stoneId, dropLevel, riseLevel);
                customCuilianManager.customCuilianStoneList.add(s);
            }
        } else {
            p.saveResource("cuilianStone" + NewAPI.getFileVersion() + ".yml", true);
            readItem(p);
        }
    }

    public static Attribute E(YamlConfiguration config1, int i) {
        HashMap<String, Integer> damageMap = new HashMap<>();
        damageMap.putAll(NewAPI.getHashMap("arms", config1, i, "damage"));
        damageMap.putAll(NewAPI.getHashMap("helmet", config1, i, "damage"));
        damageMap.putAll(NewAPI.getHashMap("chestplate", config1, i, "damage"));
        damageMap.putAll(NewAPI.getHashMap("leggings", config1, i, "damage"));
        damageMap.putAll(NewAPI.getHashMap("boots", config1, i, "damage"));
        HashMap<String, Integer> bloodSuckMap = new HashMap<>();
        bloodSuckMap.putAll(NewAPI.getHashMap("arms", config1, i, "bloodSuck"));
        bloodSuckMap.putAll(NewAPI.getHashMap("helmet", config1, i, "bloodSuck"));
        bloodSuckMap.putAll(NewAPI.getHashMap("chestplate", config1, i, "bloodSuck"));
        bloodSuckMap.putAll(NewAPI.getHashMap("leggings", config1, i, "bloodSuck"));
        bloodSuckMap.putAll(NewAPI.getHashMap("boots", config1, i, "bloodSuck"));
        HashMap<String, Integer> jumpMap = new HashMap<>();
        jumpMap.putAll(NewAPI.getHashMap("arms", config1, i, "jump"));
        jumpMap.putAll(NewAPI.getHashMap("helmet", config1, i, "jump"));
        jumpMap.putAll(NewAPI.getHashMap("chestplate", config1, i, "jump"));
        jumpMap.putAll(NewAPI.getHashMap("leggings", config1, i, "jump"));
        jumpMap.putAll(NewAPI.getHashMap("boots", config1, i, "jump"));
        HashMap<String, Integer> defenseMap = new HashMap<>();
        defenseMap.putAll(NewAPI.getHashMap("arms", config1, i, "defense"));
        defenseMap.putAll(NewAPI.getHashMap("helmet", config1, i, "defense"));
        defenseMap.putAll(NewAPI.getHashMap("chestplate", config1, i, "defense"));
        defenseMap.putAll(NewAPI.getHashMap("leggings", config1, i, "defense"));
        defenseMap.putAll(NewAPI.getHashMap("boots", config1, i, "defense"));
        HashMap<String, Integer> reboundDamageMap = new HashMap<>();
        reboundDamageMap.putAll(NewAPI.getHashMap("arms", config1, i, "reboundDamage"));
        reboundDamageMap.putAll(NewAPI.getHashMap("helmet", config1, i, "reboundDamage"));
        reboundDamageMap.putAll(NewAPI.getHashMap("chestplate", config1, i, "reboundDamage"));
        reboundDamageMap.putAll(NewAPI.getHashMap("leggings", config1, i, "reboundDamage"));
        reboundDamageMap.putAll(NewAPI.getHashMap("boots", config1, i, "reboundDamage"));
        HashMap<String, Integer> experienceMap = new HashMap<>();
        experienceMap.putAll(NewAPI.getHashMap("arms", config1, i, "experience"));
        experienceMap.putAll(NewAPI.getHashMap("helmet", config1, i, "experience"));
        experienceMap.putAll(NewAPI.getHashMap("chestplate", config1, i, "experience"));
        experienceMap.putAll(NewAPI.getHashMap("leggings", config1, i, "experience"));
        experienceMap.putAll(NewAPI.getHashMap("boots", config1, i, "experience"));
        List<HashMap<String, Double>> list = new ArrayList<>();
        for (Variable v : NewCustomCuiLian.variables) {
            HashMap<String, Double> map = new HashMap<>();
            String name = v.name;
            map.putAll(NewAPI.getHashMap("arms", config1, i, name));
            map.putAll(NewAPI.getHashMap("helmet", config1, i, name));
            map.putAll(NewAPI.getHashMap("chestplate", config1, i, name));
            map.putAll(NewAPI.getHashMap("leggings", config1, i, name));
            map.putAll(NewAPI.getHashMap("boots", config1, i, name));
            list.add(map);
        }
        return new Attribute(damageMap, bloodSuckMap, jumpMap, defenseMap, reboundDamageMap, experienceMap, list);
    }

    public static void addTypeToItem(Material m, String ss, Plugin p) {
        String s = m.name();
        File file = new File(p.getDataFolder(), "Item.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<String> list = config.getStringList(ss);
        if (!list.contains(s)) {
            list.add(s);
            config.set(ss, list);
            customCuilianManager.ItemList.add(m);
            readCuiLianItem(p);
            NewCustomCuiLian.reloadRecipe();
            try {
                config.save(new File(p.getDataFolder(), "Item.yml"));
            } catch (IOException ex) {
            }
        }
    }

    public static void readVariables(Plugin p) {
        NewCustomCuiLian.variables.clear();
        File file1 = new File(p.getDataFolder(), "variables.yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            List<String> list = config1.getStringList("variables");
            for (String s : list) {
                NewCustomCuiLian.variables.add(new Variable(s, new ArrayList<>()));
            }
        } else {
            p.saveResource("variables.yml", true);
            readVariables(p);
        }
    }
}
