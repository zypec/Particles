package dev.zypec.particles.effect.script.visual;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import dev.zypec.particles.Particles;
import dev.zypec.particles.effect.data.EffectData;
import dev.zypec.particles.effect.data.PlayerEffectData;
import dev.zypec.particles.effect.handler.HandlerEvent;
import dev.zypec.particles.effect.script.Script;
import dev.zypec.particles.effect.script.argument.type.VectorArgument;
import dev.zypec.particles.effect.script.particle.config.LocationOrigin;
import dev.zypec.particles.util.nms.particles.PacketHandler;
import org.bukkit.GameMode;
import org.bukkit.Location;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true, chain = true)
public class Lightning extends Script {

    private LocationOrigin origin;
    private VectorArgument position;

    @Override
    public TickResult tick(EffectData data, HandlerEvent event, int times) {
        Location origin = null;
        var player = data instanceof PlayerEffectData playerEffectData ? playerEffectData.player : null;
        if (player != null && player.getGameMode() == GameMode.SPECTATOR) return TickResult.NORMAL;

        var entity = switch (event) {
            case STATIC -> {
                origin = data.getLocation().clone();
                yield null;
            }
            case ELYTRA, STANDING, MOVING, SNEAKING, TAKE_DAMAGE -> player;
            case MOB_KILL, PLAYER_KILL, PROJECTILE, PROJECTILE_HIT, MOB_DAMAGE, PLAYER_DAMAGE, RIDE_VEHICLE ->
                    data instanceof PlayerEffectData playerEffectData ? playerEffectData.getTargetEntity() : null;
        };
        if (entity == null && origin == null) return null;

        if (origin == null)
            origin = this.origin.getLocation(entity);

        if (this.position != null)
            origin.add(this.position.get(this, data));

        PacketHandler.NMS.strikeLightning(origin, Particles.getPlayerManager().defaultFilter(player));
        return TickResult.NORMAL;
    }

    @Override
    public Script clone() {
        return null;
    }
}
