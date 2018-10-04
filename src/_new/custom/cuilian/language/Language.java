package _new.custom.cuilian.language;

import _new.custom.cuilian.NewCustomCuiLian;
import _new.custom.cuilian.newapi.NewAPI;
import _new.custom.cuilian.variable.Variable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Language {

    public static String JOIN_SERVER_NOTICE = "";
    public static String APPEND_TO_THE_RIGHT_KEY = "";
    public static String ADD_CANCEL_PROMPT = "";
    public static String CAN_NOT_ADD_PROMPT = "";
    public static String CAN_ADD_PROMPT = "";
    public static List<String> ADD_PROMPT_LIST = new ArrayList<>();
    public static String CAN_CUILIAN_PROMPT = "";
    public static String ALL_SERVER_PROMPT = "";
    public static String CAN_NOT_CUILIAN = "";
    public static String HAS_BAOHUFU_CUILIAN_OVER;
    public static String CUILIAN_OVER = "";
    public static String CUILIAN_OVER_ZERO = "";
    public static String FIRST = "";
    public static String FUCK_BAOHUFU = "";
    public static List<String> UNDER_LINE = new ArrayList<>();
    public static List<String> DAMAGE_LORE = new ArrayList<>();
    public static List<String> BLOOD_SUCKING_LORE = new ArrayList<>();
    public static List<String> DEFENSE_LORE = new ArrayList<>();
    public static List<String> JUMP_LORE = new ArrayList<>();
    public static List<String> ANTI_INJURY_LORE = new ArrayList<>();
    public static List<String> FALL_LORE = new ArrayList<>();
    public static List<String> EXPRIENCE = new ArrayList<>();
    public static String TZXG_TRUE = "";
    public static String TZXG_FALSE = "";

    public static void loadLanguage(Plugin p) {
        File file1 = new File(p.getDataFolder(), "language" + NewAPI.getFileVersion() + ".yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            JOIN_SERVER_NOTICE = config1.getString("JOIN_SERVER_NOTICE").replace("&", "§");
            APPEND_TO_THE_RIGHT_KEY = config1.getString("APPEND_TO_THE_RIGHT_KEY").replace("&", "§");
            ADD_CANCEL_PROMPT = config1.getString("ADD_CANCEL_PROMPT").replace("&", "§");
            CAN_NOT_ADD_PROMPT = config1.getString("CAN_NOT_ADD_PROMPT").replace("&", "§");
            CAN_ADD_PROMPT = config1.getString("CAN_ADD_PROMPT").replace("&", "§");
            ADD_PROMPT_LIST = NewAPI.replace(config1.getStringList("ADD_PROMPT_LIST"), "&", "§");
            CAN_CUILIAN_PROMPT = config1.getString("CAN_CUILIAN_PROMPT").replace("&", "§");
            ALL_SERVER_PROMPT = config1.getString("ALL_SERVER_PROMPT").replace("&", "§");
            CAN_NOT_CUILIAN = config1.getString("CAN_NOT_CUILIAN").replace("&", "§");
            HAS_BAOHUFU_CUILIAN_OVER = config1.getString("HAS_BAOHUFU_CUILIAN_OVER").replace("&", "§");
            CUILIAN_OVER = config1.getString("CUILIAN_OVER").replace("&", "§");
            CUILIAN_OVER_ZERO = config1.getString("CUILIAN_OVER_ZERO").replace("&", "§");
            FIRST = config1.getString("FIRST").replace("&", "§");
            FUCK_BAOHUFU = config1.getString("FUCK_BAOHUFU").replace("&", "§");
            UNDER_LINE = NewAPI.replace(config1.getStringList("UNDER_LINE"), "&", "§");
            DAMAGE_LORE = NewAPI.replace(config1.getStringList("DAMAGE_LORE"), "&", "§");
            BLOOD_SUCKING_LORE = NewAPI.replace(config1.getStringList("BLOOD_SUCKING_LORE"), "&", "§");
            DEFENSE_LORE = NewAPI.replace(config1.getStringList("DEFENSE_LORE"), "&", "§");
            JUMP_LORE = NewAPI.replace(config1.getStringList("JUMP_LORE"), "&", "§");
            ANTI_INJURY_LORE = NewAPI.replace(config1.getStringList("ANTI_INJURY_LORE"), "&", "§");
            FALL_LORE = NewAPI.replace(config1.getStringList("FALL_LORE"), "&", "§");
            EXPRIENCE = NewAPI.replace(config1.getStringList("EXPRIENCE"), "&", "§");
            TZXG_TRUE = config1.getString("TZXG_TRUE").replace("&", "§");
            TZXG_FALSE = config1.getString("TZXG_FALSE").replace("&", "§");
            for (Variable v : NewCustomCuiLian.variables) {
                v.lore = NewAPI.replace(config1.getStringList(v.name), "&", "§");
            }
        } else {
            p.saveResource("language" + NewAPI.getFileVersion() + ".yml", true);
            loadLanguage(p);
        }
    }
}
