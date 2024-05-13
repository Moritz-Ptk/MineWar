package com.teqless.minewar.listeners;

import com.teqless.minewar.MineWar;
import com.teqless.minewar.game.GameRules;
import com.teqless.minewar.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        if(MineWar.getHandler().getState() != GameState.IN_GAME) {
            event.setCancelled(true);
        }

        if(event.getCause() == EntityDamageEvent.DamageCause.FALL &! GameRules.FALL_DAMAGE) event.setCancelled(true);


    }

}
