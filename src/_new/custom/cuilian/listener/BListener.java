package _new.custom.cuilian.listener;

import _new.custom.cuilian.NewCustomCuiLian;
import static _new.custom.cuilian.NewCustomCuiLian.Max;
import _new.custom.cuilian.language.Language;
import _new.custom.cuilian.level.Level;
import _new.custom.cuilian.manager.QuenchingProtectRuneManager;
import _new.custom.cuilian.manager.FurnaceManager;
import _new.custom.cuilian.newapi.NewAPI;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import static org.bukkit.plugin.java.JavaPlugin.getPlugin;
import org.bukkit.event.EventPriority;
import static _new.custom.cuilian.NewCustomCuiLian.customCuilianManager;

public class BListener implements Listener {

    //优化完成
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        ItemStack Helmet = p.getInventory().getHelmet();
        ItemStack Chestplate = p.getInventory().getChestplate();
        ItemStack Leggings = p.getInventory().getLeggings();
        ItemStack Boots = p.getInventory().getBoots();
        int hdj = 0;
        int cdj = 0;
        int ldj = 0;
        int bdj = 0;
        if (Helmet != null && Helmet.getType() != Material.AIR) {
            hdj = NewAPI.getLevelByItem(Helmet).levelValue;
        }
        if (Chestplate != null && Chestplate.getType() != Material.AIR) {
            cdj = NewAPI.getLevelByItem(Chestplate).levelValue;
        }
        if (Leggings != null && Leggings.getType() != Material.AIR) {
            ldj = NewAPI.getLevelByItem(Leggings).levelValue;
        }
        if (Boots != null && Boots.getType() != Material.AIR) {
            bdj = NewAPI.getLevelByItem(Boots).levelValue;
        }
        if (hdj == cdj && ldj == bdj && cdj == ldj && ldj != -1) {
            if (NewCustomCuiLian.customCuilianManager.customCuilianLevelList.get(ldj).canSendNotice) {
                Bukkit.broadcastMessage(Language.JOIN_SERVER_NOTICE.replace("%p", p.getName()));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void InventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getRawSlot() < 0) {
            return;
        }
        ItemStack item = e.getCurrentItem();
        if (!e.isRightClick()) {
            return;
        }
        if (e.getInventory().getType() != InventoryType.CRAFTING && e.getInventory().getType() != InventoryType.PLAYER) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (QuenchingProtectRuneManager.QuenchingProtectRuneUsingMap.containsKey(p.getName())) {
            if (e.isRightClick()) {
                if (e.getInventory().getType() != InventoryType.PLAYER && e.getInventory().getType() != InventoryType.CRAFTING) {
                    p.sendMessage(Language.APPEND_TO_THE_RIGHT_KEY);
                    return;
                }
                if (item == null || item.getType().equals(Material.AIR)) {
                    e.setCancelled(true);
                    p.closeInventory();
                    p.sendMessage(Language.ADD_CANCEL_PROMPT);
                    QuenchingProtectRuneManager.QuenchingProtectRuneUsingMap.remove(p.getName());
                    QuenchingProtectRuneManager.QuenchingProtectRuneFirstItemMap.remove(p.getName());
                    return;
                }
                ItemStack fitem = QuenchingProtectRuneManager.QuenchingProtectRuneFirstItemMap.get(p.getName());
                if (!NewCustomCuiLian.customCuilianManager.ItemList.contains(item.getType())) {
                    e.setCancelled(true);
                    QuenchingProtectRuneManager.QuenchingProtectRuneUsingMap.remove(p.getName());
                    QuenchingProtectRuneManager.QuenchingProtectRuneFirstItemMap.remove(p.getName());
                    p.sendMessage(Language.CAN_NOT_ADD_PROMPT);
                    return;
                }
                if (!p.getInventory().contains(fitem)) {
                    p.sendMessage(Language.CAN_NOT_ADD_PROMPT);
                    QuenchingProtectRuneManager.QuenchingProtectRuneUsingMap.remove(p.getName());
                    QuenchingProtectRuneManager.QuenchingProtectRuneFirstItemMap.remove(p.getName());
                    e.setCancelled(true);
                    return;
                }
                if ((NewAPI.getLevelByItem(item).levelValue >= NewAPI.getLevelByQuenchingProtectRune(fitem).levelValue) && (!Objects.equals(NewAPI.getLevelByQuenchingProtectRuneString(item).levelValue, NewAPI.getLevelByQuenchingProtectRune(fitem).levelValue))) {
                    Level l = NewAPI.getLevelByQuenchingProtectRune(fitem);
                    item = NewAPI.addCuilianQuenchingProtectRune(item, fitem);
                    p.sendMessage(Language.CAN_ADD_PROMPT.replace("%s", l.quenchingProtectRuneString));
                    e.setCancelled(true);
                    int sl = fitem.getAmount() - 1;
                    p.getInventory().remove(fitem);
                    ItemStack i = fitem.clone();
                    i.setAmount(sl);
                    p.getInventory().addItem(i);
                    p.closeInventory();
                    QuenchingProtectRuneManager.QuenchingProtectRuneUsingMap.remove(p.getName());
                    QuenchingProtectRuneManager.QuenchingProtectRuneFirstItemMap.remove(p.getName());
                }
            }
        } else {
            if (e.isRightClick()) {
                if (NewAPI.isQuenchingProtectRuneMapItemMetaHasItemMeta(meta)) {
                    if (!p.getInventory().contains(item)) {
                        p.sendMessage(Language.APPEND_TO_THE_RIGHT_KEY);
                        e.setCancelled(true);
                        return;
                    }
                    QuenchingProtectRuneManager.QuenchingProtectRuneUsingMap.put(p.getName(), meta);
                    QuenchingProtectRuneManager.QuenchingProtectRuneFirstItemMap.put(p.getName(), item);
                    Level l = NewAPI.getLevelByQuenchingProtectRune(item);
                    for (String str : Language.ADD_PROMPT_LIST) {
                        p.sendMessage(str.replace("%s", l.quenchingProtectRuneString));
                    }
                    e.setCancelled(true);
                    p.closeInventory();
                }
            }
        }
    }

    //优化完成
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        if ((!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) || (!e.hasBlock()) || (!e.getClickedBlock().getType().equals(Material.FURNACE))) {
            return;
        }
        Player p = e.getPlayer();
        Furnace f = (Furnace) e.getClickedBlock().getState();
        if (FurnaceManager.FurnaceUsingMap.get(f.getLocation()) != null) {
            FurnaceManager.FurnaceUsingMap.remove(f.getLocation());
        }
        FurnaceManager.FurnaceUsingMap.put(f.getLocation(), p.getName());
    }

    //优化完成
    @EventHandler(priority = EventPriority.HIGHEST)
    public void FurnaceBurnEvent(FurnaceBurnEvent e) {
        ItemStack fuel = e.getFuel();
        Furnace f = (Furnace) e.getBlock().getState();
        if ((NewAPI.isStoneMapItemMetaHasItemMeta(fuel.getItemMeta()) && !customCuilianManager.ItemList.contains(f.getInventory().getSmelting().getType())) || (customCuilianManager.ItemList.contains(f.getInventory().getSmelting().getType()) && !NewAPI.isStoneMapItemMetaHasItemMeta(fuel.getItemMeta())) || NewAPI.getLevelByItem(f.getInventory().getSmelting()).levelValue + NewAPI.getStoneByItem(fuel).riseLevel > Max) {
            e.setCancelled(true);
            return;
        }
        if (NewAPI.isStoneMapItemMetaHasItemMeta(fuel.getItemMeta())) {
            if (customCuilianManager.ItemList.contains(f.getInventory().getSmelting().getType())) {
                FurnaceManager.FurnaceFuelMap.put(f.getLocation(), fuel);
                e.setBurning(true);
                e.setBurnTime(200);
            } else {
                e.setCancelled(true);
            }
        }
    }

    //优化完成
    @EventHandler(priority = EventPriority.HIGHEST)
    public void FurnaceSmeltEvent(FurnaceSmeltEvent e) {
        ItemStack smelt = e.getSource();
        Furnace f = (Furnace) e.getBlock().getState();
        ItemStack fuel = FurnaceManager.FurnaceFuelMap.get(f.getLocation());
        if (!FurnaceManager.FurnaceFuelMap.containsKey(f.getLocation()) && NewCustomCuiLian.customCuilianManager.ItemList.contains(smelt.getType())) {
            e.setResult(smelt);
            return;
        }
        if (!FurnaceManager.FurnaceFuelMap.containsKey(f.getLocation()) || !NewCustomCuiLian.customCuilianManager.ItemList.contains(smelt.getType()) || fuel == null) {
            return;
        }
        if (NewAPI.isStoneMapItemMetaHasItemMeta(fuel.getItemMeta()) && FurnaceManager.FurnaceUsingMap.get(f.getLocation()) != null) {
            Player p = (Player) getPlugin(NewCustomCuiLian.class).getServer().getPlayer(FurnaceManager.FurnaceUsingMap.get(f.getLocation()));
            smelt.setAmount(1);
            smelt = NewAPI.cuilian(fuel, smelt, p);
            e.setResult(smelt);
            FurnaceManager.FurnaceFuelMap.remove(f.getLocation());
        }
    }
}
