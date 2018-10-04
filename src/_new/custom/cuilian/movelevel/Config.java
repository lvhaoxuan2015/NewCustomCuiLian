package _new.custom.cuilian.movelevel;

import _new.custom.cuilian.newapi.NewAPI;
import java.io.File;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
    
    public static String MOVE_LEVEL_INVENTORY_NAME = "";
    
    public void load(Plugin p){
        File file1 = new File(p.getDataFolder(), "MoveLevelInventoryConfig" + NewAPI.getFileVersion() + ".yml");
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(file1);
        if (file1.exists()) {
            MOVE_LEVEL_INVENTORY_NAME = config1.getString("MOVE_LEVEL_INVENTORY_NAME").replace("&", "§");
        } else {
            p.saveResource("MoveLevelInventoryConfig" + NewAPI.getFileVersion() + ".yml", true);
            load(p);
        }
    }
}
