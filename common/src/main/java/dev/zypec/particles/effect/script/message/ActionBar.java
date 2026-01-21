package dev.zypec.particles.effect.script.message;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import dev.zypec.particles.effect.data.EffectData;
import dev.zypec.particles.effect.data.PlayerEffectData;
import dev.zypec.particles.effect.handler.HandlerEvent;
import dev.zypec.particles.effect.script.Script;
import dev.zypec.particles.util.message.MessageUtils;

@AllArgsConstructor
@NoArgsConstructor
public class ActionBar extends Script {

    private String message;

    @Override
    public TickResult tick(EffectData data, HandlerEvent event, int times) {
        if (data instanceof PlayerEffectData playerEffectData)
            MessageUtils.sendActionBarParsed(playerEffectData.player, data.replaceVariables(effect, message));
        return TickResult.NORMAL;
    }

    @Override
    public ActionBar clone() {
        return new ActionBar(message);
    }
}