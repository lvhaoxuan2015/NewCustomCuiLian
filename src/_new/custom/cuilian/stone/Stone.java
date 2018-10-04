package _new.custom.cuilian.stone;

import _new.custom.cuilian.level.Level;
import java.util.HashMap;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class Stone {

    public HashMap<Level, Double> levelProbability = new HashMap<>();
    public ItemStack cuiLianStone;
    public List<Integer> dropLevel = new ArrayList<>();
    public String id = "";
    public int riseLevel = 0;

    public Stone(ItemStack cuiLianStone, String id, List<Integer> dropLevel, int riseLevel) {
        this.cuiLianStone = cuiLianStone;
        this.id = id;
        this.dropLevel = dropLevel;
        this.riseLevel = riseLevel;
    }
}
