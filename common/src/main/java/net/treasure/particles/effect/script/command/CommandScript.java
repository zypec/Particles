package net.treasure.particles.effect.script.command;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.treasure.particles.TreasureParticles;
import net.treasure.particles.effect.data.EffectData;
import net.treasure.particles.effect.data.PlayerEffectData;
import net.treasure.particles.effect.handler.HandlerEvent;
import net.treasure.particles.effect.script.Script;
import org.bukkit.Bukkit;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
public class CommandScript extends Script {

    private String command;
    private boolean fromPlayer = true;

    @Override
    public TickResult tick(EffectData data, HandlerEvent event, int times) {
        var command = data.replaceVariables(effect, this.command);
        if (data instanceof PlayerEffectData playerData) {
            command = command.replaceAll("%player%", playerData.player.getName());
            if (fromPlayer) {
                var finalCommand = command;
                Bukkit.getScheduler().runTask(TreasureParticles.getPlugin(), () -> playerData.player.performCommand(finalCommand));
                return TickResult.NORMAL;
            }
        }

        if (!fromPlayer) {
            var finalCommand = command;
            Bukkit.getScheduler().runTask(TreasureParticles.getPlugin(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand));
        }

        return TickResult.NORMAL;
    }

    @Override
    public CommandScript clone() {
        return new CommandScript(command, fromPlayer);
    }
}