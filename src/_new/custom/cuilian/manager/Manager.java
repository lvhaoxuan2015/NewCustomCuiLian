package _new.custom.cuilian.manager;

import _new.custom.cuilian.attribute.Attribute;
import _new.custom.cuilian.level.Level;
import _new.custom.cuilian.stone.Stone;
import java.util.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Manager {

    public List<Level> customCuilianLevelList = new ArrayList<>();
    public List<Stone> customCuilianStoneList = new ArrayList<>();
    public List<Material> ItemList = new ArrayList<>();
    public Level NULLLevel;
    public Attribute NULLAttribute;
    public Stone NULLStone;

    public Manager() {
        NULLAttribute = new Attribute(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new ArrayList<>());
        NULLStone = new Stone(new ItemStack(Material.AIR), "", new ArrayList<>(), 0);
        NULLLevel = new Level(-1, new ArrayList<>(), "", false, false, NULLAttribute, new ItemStack(Material.AIR), false, 0);
    }
}
