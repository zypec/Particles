package dev.zypec.particles.effect.task;

import lombok.AllArgsConstructor;
import dev.zypec.particles.effect.EffectManager;
import dev.zypec.particles.effect.data.PlayerEffectData;
import dev.zypec.particles.effect.handler.HandlerEvent;
import dev.zypec.particles.util.TimeKeeper;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class EffectsTask extends BukkitRunnable {

    private final EffectManager effectManager;

    @Override
    public void run() {
        TimeKeeper.increaseTime();
        var iterator = effectManager.getData().entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            var data = entry.getValue();

            var location = data.getLocation();
            if (location == null) {
                iterator.remove();
                continue;
            }

            var current = data.getCurrentEffect();
            if (current == null) {
                iterator.remove();
                continue;
            }

            var event = data.getCurrentEvent();
            if (data instanceof PlayerEffectData playerEffectData && (event == null || !event.isSpecial())) {
                var player = playerEffectData.player;
                data.setCurrentEvent(player.isGliding() ? HandlerEvent.ELYTRA : (player.isSneaking() ? HandlerEvent.SNEAKING : (playerEffectData.isMoving() ? HandlerEvent.MOVING : HandlerEvent.STANDING)));
            }
            current.doTick(data);
        }
    }
}