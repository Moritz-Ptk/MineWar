package com.teqless.minewar.listeners;

import com.teqless.minewar.MineWar;
import com.teqless.minewar.game.GameHandler;
import com.teqless.minewar.game.GameRules;
import com.teqless.minewar.game.GameState;
import com.teqless.minewar.teams.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        if(MineWar.getHandler().getState() != GameState.IN_GAME) {
            event.setCancelled(true);
        }

        if(event.getCause() == EntityDamageEvent.DamageCause.FALL &! GameRules.FALL_DAMAGE) event.setCancelled(true);

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if(MineWar.getHandler().getState() != GameState.IN_GAME) {
            event.setCancelled(true);
            return;
        }

        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {

            GameHandler handler = MineWar.getHandler();

            User user = handler.getUser(event.getEntity().getUniqueId());
            User damager = handler.getUser(event.getDamager().getUniqueId());

            if(damager.isSpectating() || damager.getTeam().equals(user.getTeam())) {
                event.setCancelled(true);
            }

        }

    }


}
