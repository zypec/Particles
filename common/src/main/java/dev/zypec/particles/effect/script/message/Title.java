package dev.zypec.particles.effect.script.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import dev.zypec.particles.effect.data.EffectData;
import dev.zypec.particles.effect.data.PlayerEffectData;
import dev.zypec.particles.effect.handler.HandlerEvent;
import dev.zypec.particles.effect.script.Script;
import dev.zypec.particles.util.message.MessageUtils;

@AllArgsConstructor
@Builder
@Getter
public class Title extends Script {

    private String title, subtitle;
    private int fadeIn, stay, fadeOut;

    @Override
    public TickResult tick(EffectData data, HandlerEvent event, int times) {
        if (data instanceof PlayerEffectData playerEffectData)
            MessageUtils.sendTitleParsed(playerEffectData.player, data.replaceVariables(effect, title), data.replaceVariables(effect, subtitle), fadeIn, stay, fadeOut);
        return TickResult.NORMAL;
    }

    @Override
    public Script clone() {
        return new Title(title, subtitle, fadeIn, stay, fadeOut);
    }
}