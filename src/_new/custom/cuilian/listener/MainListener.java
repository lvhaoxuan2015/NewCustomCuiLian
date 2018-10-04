package _new.custom.cuilian.listener;

import _new.custom.cuilian.NewCustomCuiLian;
import _new.custom.cuilian.newapi.NewAPI;
import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MainListener implements Listener {

    HashMap<Integer, Double> ShEntityIdMap = new HashMap<>();
    HashMap<Integer, Double> XxEntityIdMap = new HashMap<>();

    //优化完成
    @EventHandler(priority = EventPriority.HIGHEST)
    public void EntityDamageEvent(EntityDamageEvent e) {
        if ((e.getEntity() instanceof Player) && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            double damage = e.getDamage();
            Player p = (Player) e.getEntity();
            damage -= NewAPI.getDefense(NewAPI.addAll(NewAPI.getItemInHand(p), NewAPI.getItemInOffHand(p), p.getInventory().getHelmet(), p.getInventory().getChestplate(), p.getInventory().getLeggings(), p.getInventory().getBoots()));
            if (damage < 0) {
                damage = 0;
            }
            e.setDamage(damage);
        }
    }

    //优化完成
    @EventHandler(priority = EventPriority.HIGHEST)
    public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
        Entity Damager = e.getDamager();
        double damage = e.getDamage();
        if (((Damager instanceof Damageable) || (Damager instanceof Projectile)) && (e.getEntity() instanceof Damageable)) {
            if (Damager instanceof Damageable) {
                if ((e.getEntity() instanceof Player) && (Damager instanceof Player)) {
                    Player Damagerr = (Player) Damager;
                    Player hPlayer = (Player) e.getEntity();
                    List<ItemStack> list1 = NewAPI.addAll(NewAPI.getItemInHand(Damagerr), NewAPI.getItemInOffHand(Damagerr), Damagerr.getInventory().getHelmet(), Damagerr.getInventory().getChestplate(), Damagerr.getInventory().getLeggings(), Damagerr.getInventory().getBoots());
                    List<ItemStack> list2 = NewAPI.addAll(NewAPI.getItemInHand(hPlayer), NewAPI.getItemInOffHand(hPlayer), hPlayer.getInventory().getHelmet(), hPlayer.getInventory().getChestplate(), hPlayer.getInventory().getLeggings(), hPlayer.getInventory().getBoots());
                    boolean canPvp = true;
                    if (canPvp) {
                        damage += NewAPI.getBloodSuck(list1) * damage / 100;
                        damage += NewAPI.getDamage(list1);
                        if (Damagerr.getHealth() + NewAPI.getBloodSuck(list1) * damage / 100 < NewAPI.getMaxHealth(Damagerr)) {
                            Damagerr.setHealth(Damagerr.getHealth() + NewAPI.getBloodSuck(list1) * damage / 100);
                        } else {
                            Damagerr.setHealth(NewAPI.getMaxHealth(Damagerr));
                        }
                        if (NewAPI.getReboundDamage(list2) * damage / 100 > 0) {
                            Damagerr.damage(NewAPI.getReboundDamage(list2) * damage / 100);
                        }
                        damage -= NewAPI.getDefense(list2);
                        try {
                            NewAPI.removeDurability(list2);
                        } catch (NoSuchMethodError ee){                           
                        }                        
                    }
                } else if (!(e.getEntity() instanceof Player) && (Damager instanceof Player)) {
                    Player Damagerr = (Player) Damager;
                    List<ItemStack> list1 = NewAPI.addAll(NewAPI.getItemInHand(Damagerr), NewAPI.getItemInOffHand(Damagerr), Damagerr.getInventory().getHelmet(), Damagerr.getInventory().getChestplate(), Damagerr.getInventory().getLeggings(), Damagerr.getInventory().getBoots());
                    damage += NewAPI.getBloodSuck(list1) * damage / 100;
                    damage += NewAPI.getDamage(list1);
                    if (Damagerr.getHealth() + NewAPI.getBloodSuck(list1) * damage / 100 < NewAPI.getMaxHealth(Damagerr)) {
                        Damagerr.setHealth(Damagerr.getHealth() + NewAPI.getBloodSuck(list1) * damage / 100);
                    } else {
                        Damagerr.setHealth(NewAPI.getMaxHealth(Damagerr));
                    }
                } else if ((e.getEntity() instanceof Player) && !(Damager instanceof Player)) {
                    Damageable Damagerr = (Damageable) Damager;
                    Player hPlayer = (Player) e.getEntity();
                    List<ItemStack> list2 = NewAPI.addAll(NewAPI.getItemInHand(hPlayer), NewAPI.getItemInOffHand(hPlayer), hPlayer.getInventory().getHelmet(), hPlayer.getInventory().getChestplate(), hPlayer.getInventory().getLeggings(), hPlayer.getInventory().getBoots());
                    if (NewAPI.getReboundDamage(list2) * damage / 100 > 0) {
                        Damagerr.damage(NewAPI.getReboundDamage(list2) * damage / 100);
                    }
                    damage -= NewAPI.getDefense(list2);
                    try {
                        NewAPI.removeDurability(list2);
                    } catch (NoSuchMethodError ee){                           
                    }
                }
            } else {
                Projectile psw = (Projectile) Damager;
                ProjectileSource ps = psw.getShooter();
                if ((ps instanceof Player) && (e.getEntity() instanceof Player)) {
                    Player p = (Player) ps;
                    Player hPlayer = (Player) e.getEntity();
                    List<ItemStack> list2 = NewAPI.addAll(NewAPI.getItemInHand(hPlayer), NewAPI.getItemInOffHand(hPlayer), hPlayer.getInventory().getHelmet(), hPlayer.getInventory().getChestplate(), hPlayer.getInventory().getLeggings(), hPlayer.getInventory().getBoots());
                    boolean canPvp = true;
                    if (canPvp) {
                        if (NewAPI.getReboundDamage(list2) * damage / 100 > 0) {
                            p.damage(NewAPI.getReboundDamage(list2) * damage / 100);
                        }
                        if (XxEntityIdMap.get(psw.getEntityId()) != null && ShEntityIdMap.get(psw.getEntityId()) != null){
                            damage += XxEntityIdMap.get(psw.getEntityId()) * damage / 100;
                            damage += ShEntityIdMap.get(psw.getEntityId());    
                            if (p.getHealth() + XxEntityIdMap.get(psw.getEntityId()) * damage / 100 < NewAPI.getMaxHealth(p)) {
                                p.setHealth(p.getHealth() + XxEntityIdMap.get(psw.getEntityId()) * damage / 100);
                            } else {
                                p.setHealth(NewAPI.getMaxHealth(p));
                            }    
                        } 
                        damage -= NewAPI.getDefense(list2);
                        try {
                            NewAPI.removeDurability(list2);
                        } catch (NoSuchMethodError ee){                           
                        }
                    }
                } else if (!(ps instanceof Player) && (e.getEntity() instanceof Player)) {
                    Damageable p = (Damageable) ps;
                    Player hPlayer = (Player) e.getEntity();
                    List<ItemStack> list2 = NewAPI.addAll(NewAPI.getItemInHand(hPlayer), NewAPI.getItemInOffHand(hPlayer), hPlayer.getInventory().getHelmet(), hPlayer.getInventory().getChestplate(), hPlayer.getInventory().getLeggings(), hPlayer.getInventory().getBoots());
                    damage -= NewAPI.getDefense(list2);
                    if (NewAPI.getReboundDamage(list2) * damage / 100 > 0) {
                        p.damage(NewAPI.getReboundDamage(list2) * damage / 100);
                    }
                    try {
                        NewAPI.removeDurability(list2);
                    } catch (NoSuchMethodError ee){                           
                    }
                } else if ((ps instanceof Player) && !(e.getEntity() instanceof Player)) {
                    Player p = (Player) ps;
                    if (XxEntityIdMap.get(psw.getEntityId()) != null && ShEntityIdMap.get(psw.getEntityId()) != null){
                        damage += XxEntityIdMap.get(psw.getEntityId()) * damage / 100;
                        damage += ShEntityIdMap.get(psw.getEntityId());    
                        if (p.getHealth() + XxEntityIdMap.get(psw.getEntityId()) * damage / 100 < NewAPI.getMaxHealth(p)) {
                            p.setHealth(p.getHealth() + XxEntityIdMap.get(psw.getEntityId()) * damage / 100);
                        } else {
                            p.setHealth(NewAPI.getMaxHealth(p));
                        }    
                    }                                     
                }
            }
        }
        if (damage < 0) {
            damage = 0;
        }
        e.setDamage(damage);
    }

    //优化完成
    @EventHandler(priority = EventPriority.HIGHEST)
    public void ProjectileLaunchEvent(ProjectileLaunchEvent e) {
        Projectile psw = (Projectile) e.getEntity();
        ProjectileSource ps = psw.getShooter();
        if (ps instanceof Player) {
            Player p = (Player) ps;
            ShEntityIdMap.put(psw.getEntityId(), NewAPI.getDamage(NewAPI.addAll(NewAPI.getItemInHand(p), NewAPI.getItemInOffHand(p), p.getInventory().getHelmet(), p.getInventory().getChestplate(), p.getInventory().getLeggings(), p.getInventory().getBoots())));
            XxEntityIdMap.put(psw.getEntityId(), NewAPI.getBloodSuck(NewAPI.addAll(NewAPI.getItemInHand(p), NewAPI.getItemInOffHand(p), p.getInventory().getHelmet(), p.getInventory().getChestplate(), p.getInventory().getLeggings(), p.getInventory().getBoots())));
        }
    }

    //优化完成
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        int level = 0;
        level = NewAPI.getJump(NewAPI.addAll(NewAPI.getItemInHand(p), NewAPI.getItemInOffHand(p), p.getInventory().getHelmet(), p.getInventory().getChestplate(), p.getInventory().getLeggings(), p.getInventory().getBoots()));
        if (level != 0) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, level));
        }
    }

    //优化完成
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerExpChangeEvent(PlayerExpChangeEvent e) {
        int value = e.getAmount();
        Player p = e.getPlayer();
        Double i = NewAPI.getExperience(NewAPI.addAll(NewAPI.getItemInHand(p), NewAPI.getItemInOffHand(p), p.getInventory().getHelmet(), p.getInventory().getChestplate(), p.getInventory().getLeggings(), p.getInventory().getBoots()));
        int jy = 0;
        if (i != 0) {
            jy = (int) (value * (100 + i) / 100);
        } else {
            return;
        }
        e.setAmount(jy);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (NewCustomCuiLian.playerSuitEffectList.containsKey(p.getName())) {
            NewAPI.setMaxHealth(p, NewCustomCuiLian.playerSuitEffectHealthList.get(p.getName()));
            NewCustomCuiLian.playerSuitEffectHealthList.remove(p.getName());
            NewCustomCuiLian.playerSuitEffectList.remove(p.getName());
        }
    }
}
