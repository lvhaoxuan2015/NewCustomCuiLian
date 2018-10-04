package _new.custom.cuilian.movelevel;

import _new.custom.cuilian.NewCustomCuiLian;
import _new.custom.cuilian.level.Level;
import _new.custom.cuilian.newapi.NewAPI;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MoveLevelInventory implements Listener {

    public static void open(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, Config.MOVE_LEVEL_INVENTORY_NAME);
        p.openInventory(inv);
        check(inv);
    }

    public static void check(Inventory inv) {
        ItemStack bariier = new ItemStack(Material.DIAMOND);
        inv.setItem(0, bariier);
        inv.setItem(1, bariier);
        inv.setItem(2, bariier);
        inv.setItem(6, bariier);
        inv.setItem(7, bariier);
        inv.setItem(8, bariier);
        if (NewAPI.getLevelByItem(inv.getItem(5)) != NewCustomCuiLian.customCuilianManager.NULLLevel) {
            ItemStack item = new ItemStack(Material.ANVIL);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§a把左边装备所有等级移给右边装备");
            List<String> lore = new ArrayList();
            lore.add("§7该装备移等级需要：§a" + NewAPI.getLevelByItem(inv.getItem(5)).moveLevelUse);
            lore.add("§7把左边装备所有等级移给右边装备。");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(4, item);
        } else {
            ItemStack item = new ItemStack(Material.ANVIL);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§a把左边装备所有等级移给右边装备");
            List<String> lore = new ArrayList();
            lore.add("§7把左边装备所有等级移给右边装备。");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(4, item);
        }
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();
        if (inv == null) {
            return;
        }
        if (e.getRawSlot() < 0) {
            return;
        }
        if (inv.getName().equals(Config.MOVE_LEVEL_INVENTORY_NAME)) {
            check(inv);
            if (e.getRawSlot() <= 8) {
                int slot = e.getRawSlot();
                if ((slot <= 8 && slot >= 0) && slot != 3 && slot != 5 && slot != 4) {
                    e.setCancelled(true);
                } else if (slot == 4) {
                    if (inv.getItem(3) == null || inv.getItem(5) == null || NewAPI.getLevelByItem(inv.getItem(3)) == NewCustomCuiLian.customCuilianManager.NULLLevel) {
                        e.setCancelled(true);
                        return;
                    }
                    int h = NewAPI.getLevelByItem(inv.getItem(5)).levelValue + NewAPI.getLevelByItem(inv.getItem(3)).levelValue + 1;
                    if (h > NewCustomCuiLian.Max) {
                        h = NewCustomCuiLian.Max;
                    }
                    Integer moveLevel = NewAPI.getLevelByItem(inv.getItem(3)).levelValue + 1 - NewAPI.getLevelByInteger(h).moveLevelUse;
                    Level newLevel;
                    if (NewAPI.getLevelByItem(inv.getItem(5)).levelValue + moveLevel > NewCustomCuiLian.Max) {
                        newLevel = NewAPI.getLevelByInteger(NewCustomCuiLian.Max);
                    } else {
                        newLevel = NewAPI.getLevelByInteger(NewAPI.getLevelByItem(inv.getItem(5)).levelValue + moveLevel);
                    }
                    inv.setItem(3, NewAPI.setItemCuiLian(inv.getItem(3), NewCustomCuiLian.customCuilianManager.NULLLevel, p));
                    inv.setItem(5, NewAPI.setItemCuiLian(inv.getItem(5), newLevel, p));
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getPlayer();
        if (inv.getName().equals(Config.MOVE_LEVEL_INVENTORY_NAME)) {
            if (inv.getItem(3) != null || inv.getItem(5) != null) {
                if (inv.getItem(3) != null) {
                    p.getInventory().addItem(inv.getItem(3));
                }
                if (inv.getItem(5) != null) {
                    p.getInventory().addItem(inv.getItem(5));
                }
                p.sendMessage("§b物品已经回到你的背包");
            }
        }
    }
}
