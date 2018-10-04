package _new.custom.cuilian.command;

import _new.custom.cuilian.NewCustomCuiLian;
import _new.custom.cuilian.language.Language;
import _new.custom.cuilian.newapi.NewAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.CommandExecutor;
import _new.custom.cuilian.loader.Loader;
import _new.custom.cuilian.movelevel.MoveLevelInventory;

public class Commander implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equals("openMoveLevelInv")){
            MoveLevelInventory.open((Player) sender);
        } else {
            if (!sender.isOp()) {
                sender.sendMessage("[" + NewCustomCuiLian.getPlugin(NewCustomCuiLian.class).getName() + "]" + "不是OP");
            } else if ((args.length == 2) && (args[0].equals("set"))) {
                Player p = (Player) sender;
                NewAPI.setItemInHand(p, NewAPI.setItemCuiLian(NewAPI.getItemInHand(p), NewAPI.getLevelByInteger(Integer.valueOf(args[1]) - 1), p));
            } else if (args.length == 0) {
                sender.sendMessage("§c§m§l  §6§m§l  §e§m§l  §a§m§l  §b§m§l  §e§l淬炼§b§m§l  §a§m§l  §e§m§l  §6§m§l  §c§m§l  ");
                sender.sendMessage("§c§l▏   §c/cuilian cuilianshi(淬炼石) id 数量 玩家");
                sender.sendMessage("§c§l▏   §c/cuilian baohufu(保护符) 对应的等级 数量 玩家");
                sender.sendMessage("§c§l▏   §c/cuilian openMoveLevelInv 打开移星面板");
                sender.sendMessage("§c§l▏   §c/cuilian set 等级");
                sender.sendMessage("§c§l▏   §c/cuilian reload 重载插件");
                sender.sendMessage("§c§l▏   §c/cuilian getType 获取物品类型");
                sender.sendMessage("§c§l▏   §c/cuilian addToItem arms|helmet|chestplate|leggings|boots 把手持物品加入可淬炼列表");
                sender.sendMessage("§c§m§l  §6§m§l  §e§m§l  §a§m§l  §b§m§l  §e§l淬炼§b§m§l  §a§m§l  §e§m§l  §6§m§l  §c§m§l  ");
            } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                Loader.readVariables(NewCustomCuiLian.getPlugin(NewCustomCuiLian.class));
                Loader.readCuiLianItem(NewCustomCuiLian.getPlugin(NewCustomCuiLian.class));
                Loader.readItem(NewCustomCuiLian.getPlugin(NewCustomCuiLian.class));
                Loader.readYml(NewCustomCuiLian.getPlugin(NewCustomCuiLian.class));
                Language.loadLanguage(NewCustomCuiLian.getPlugin(NewCustomCuiLian.class));
                NewCustomCuiLian.reloadRecipe();
                sender.sendMessage("[" + NewCustomCuiLian.getPlugin(NewCustomCuiLian.class).getName() + "]" + "插件重载完毕");
            } else if (args.length == 1 && args[0].equalsIgnoreCase("getType")) {
                Player p = (Player) sender;
                p.sendMessage(NewAPI.getItemInHand(p).getType().name());
            } else if (args.length == 2 && args[0].equalsIgnoreCase("addToItem")) {
                Player p = (Player) sender;
                Loader.addTypeToItem(NewAPI.getItemInHand(p).getType(), args[1], NewCustomCuiLian.getPlugin(NewCustomCuiLian.class));
                sender.sendMessage("[" + NewCustomCuiLian.getPlugin(NewCustomCuiLian.class).getName() + "]" + "成功将手持物品:" + NewAPI.getItemInHand(p).getType() + "加入Item.yml." + args[1]);
            } else if (args.length == 4) {
                Player pp = NewCustomCuiLian.getPlugin(NewCustomCuiLian.class).getServer().getPlayer(args[3]);
                if (pp == null) {
                    sender.sendMessage("[" + NewCustomCuiLian.getPlugin(NewCustomCuiLian.class).getName() + "]" + "§c玩家不在线");
                }
                if (args[0].equalsIgnoreCase("cuilianshi")) {
                    int sl = Integer.parseInt(args[2]);
                    if (NewCustomCuiLian.customCuilianManager.customCuilianStoneList.size() - 1 < Integer.parseInt(args[1]) - 1 || Integer.parseInt(args[1]) - 1 < 0) {
                        pp.sendMessage("[" + NewCustomCuiLian.getPlugin(NewCustomCuiLian.class).getName() + "]" + "不存在的淬炼石");
                    }
                    ItemStack item = NewCustomCuiLian.customCuilianManager.customCuilianStoneList.get(Integer.parseInt(args[1]) - 1).cuiLianStone;
                    item.setAmount(sl);
                    pp.getInventory().addItem(item);
                } else if (args[0].equalsIgnoreCase("baohufu")) {
                    int sl = Integer.parseInt(args[2]);
                    if (NewCustomCuiLian.customCuilianManager.customCuilianLevelList.get(Integer.parseInt(args[1]) - 1).quenchingProtectRune == null) {
                        pp.sendMessage("[" + NewCustomCuiLian.getPlugin(NewCustomCuiLian.class).getName() + "]" + "不存在的保护符");
                    }
                    ItemStack item = NewCustomCuiLian.customCuilianManager.customCuilianLevelList.get(Integer.parseInt(args[1]) - 1).quenchingProtectRune;
                    item.setAmount(sl);
                    pp.getInventory().addItem(item);
                }
            }
        }
        return true;
    }
}
