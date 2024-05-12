package com.teqless.minewars.listeners;

import com.teqless.minewars.MineWars;
import com.teqless.minewars.game.GameRules;
import com.teqless.minewars.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        if(MineWars.getHandler().getState() != GameState.IN_GAME) {
            event.setCancelled(true);
        }

        if(event.getCause() == EntityDamageEvent.DamageCause.FALL &! GameRules.FALL_DAMAGE) event.setCancelled(true);


    }

}
