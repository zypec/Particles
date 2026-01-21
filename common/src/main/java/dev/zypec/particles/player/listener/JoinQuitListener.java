package dev.zypec.particles.player.listener;

import lombok.AllArgsConstructor;
import dev.zypec.particles.Particles;
import dev.zypec.particles.effect.EffectManager;
import dev.zypec.particles.locale.Translations;
import dev.zypec.particles.permission.Permissions;
import dev.zypec.particles.player.PlayerManager;
import dev.zypec.particles.util.message.MessageUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class JoinQuitListener implements Listener {

    private final PlayerManager playerManager;
    private final EffectManager effectManager;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(PlayerJoinEvent event) {
        var player = event.getPlayer();
        playerManager.initializePlayer(player, data -> {
            if (!player.isOnline()) {
                playerManager.remove(player);
                return;
            }
            if (player.hasPermission(Permissions.ADMIN) && Particles.isNotificationsEnabled() && data.isNotificationsEnabled())
                MessageUtils.sendParsed(player, Translations.NOTIFICATION);
        });
    }

    @EventHandler
    public void on(PlayerQuitEvent event) {
        playerManager.remove(event.getPlayer());
        effectManager.getData().remove(event.getPlayer().getUniqueId().toString());
    }
}