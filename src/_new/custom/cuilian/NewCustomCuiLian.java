package _new.custom.cuilian;

import _new.custom.cuilian.command.Commander;
import _new.custom.cuilian.listener.BListener;
import _new.custom.cuilian.listener.MainListener;
import _new.custom.cuilian.language.Language;
import _new.custom.cuilian.loader.Loader;
import _new.custom.cuilian.manager.Manager;
import _new.custom.cuilian.movelevel.Config;
import _new.custom.cuilian.movelevel.MoveLevelInventory;
import _new.custom.cuilian.newapi.NewAPI;
import _new.custom.cuilian.variable.Variable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

public class NewCustomCuiLian extends JavaPlugin {

    public static Manager customCuilianManager = new Manager();
    public static String ServerVersion = "";
    public static int Max = 0;
    public static List<Material> arms = new ArrayList<>();
    public static List<Material> helmet = new ArrayList<>();
    public static List<Material> chestplate = new ArrayList<>();
    public static List<Material> leggings = new ArrayList<>();
    public static List<Material> boots = new ArrayList<>();
    public static List<String> powerArms = new ArrayList<>();
    public static List<String> powerHelmet = new ArrayList<>();
    public static List<String> powerChestplate = new ArrayList<>();
    public static List<String> powerLeggings = new ArrayList<>();
    public static List<String> powerBoots = new ArrayList<>();
    public static List<String> localArms = new ArrayList<>();
    public static List<String> localHelmet = new ArrayList<>();
    public static List<String> localChestplate = new ArrayList<>();
    public static List<String> localLeggings = new ArrayList<>();
    public static List<String> localBoots = new ArrayList<>();
    public static HashMap<String, Integer> playerSuitEffectList = new HashMap<>();
    public static HashMap<String, Double> playerSuitEffectHealthList = new HashMap<>();
    public static Boolean usingDefaultPower;
    public static List<Variable> variables = new ArrayList<>();
    public static Config config = new Config();

    @Override
    public void onEnable() {
        try {
            ServerVersion = getServerVersion();
        } catch (NoSuchMethodException ex) {
        }
        this.saveDefaultConfig();
        this.reloadConfig();
        Server s = this.getServer();
        Loader.readVariables(this);
        Loader.readCuiLianItem(this);
        Loader.readItem(this);
        Loader.readYml(this);
        Language.loadLanguage(this);
        config.load(this);
        s.getPluginManager().registerEvents(new BListener(), this);
        s.getPluginManager().registerEvents(new MoveLevelInventory(), this);
        if (usingDefaultPower) {
            s.getPluginManager().registerEvents(new MainListener(), this);
        }
        s.getPluginCommand("cuilian").setExecutor(new Commander());
        reloadRecipe();
    }

    @Override
    public void onDisable() {
        for (String name : playerSuitEffectList.keySet()) {
            Player p = Bukkit.getPlayer(name);
            NewAPI.setMaxHealth(p, NewCustomCuiLian.playerSuitEffectHealthList.get(p.getName()));
            NewCustomCuiLian.playerSuitEffectHealthList.remove(p.getName());
            NewCustomCuiLian.playerSuitEffectList.remove(p.getName());
        }
    }

    public static void reloadRecipe() {
        for (int i = 0; i < customCuilianManager.ItemList.size(); i++) {
            FurnaceRecipe Furnace = new FurnaceRecipe(new ItemStack(customCuilianManager.ItemList.get(i)), customCuilianManager.ItemList.get(i));
            for (int j = 0; j <= customCuilianManager.ItemList.get(i).getMaxDurability(); j++) {
                Furnace.setInput(customCuilianManager.ItemList.get(i), j);
                try {
                    NewCustomCuiLian.getPlugin(NewCustomCuiLian.class).getServer().addRecipe(Furnace);
                } catch (IllegalStateException ex) {
                }
            }
        }
    }

    public String getServerVersion() throws NoSuchMethodException {
        String sv = null;
        Method[] m = PlayerInventory.class.getDeclaredMethods();
        try {
            Class clas = Class.forName("org.bukkit.Particle");
            sv = "1.13";
            return sv;
        } catch (ClassNotFoundException ex) {
        }
        for (Method e : m) {
            if (e.toGenericString().contains("getItemInMainHand")) {
                sv = "1.9+";
                return sv;
            }
        }
        if (sv == null) {
            if (Material.getMaterial("SLIME_BLOCK") != null) {
                sv = "1.8";
                return sv;
            } else {
                sv = "1.7";
                return sv;
            }
        }
        return sv;
    }
}
