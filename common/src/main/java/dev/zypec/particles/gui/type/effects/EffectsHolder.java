package dev.zypec.particles.gui.type.effects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import dev.zypec.particles.effect.handler.HandlerEvent;
import dev.zypec.particles.gui.GUIHolder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EffectsHolder extends GUIHolder {
    private HandlerEvent filter;
    private List<HandlerEvent> availableFilters;
    private boolean playerMixGUI;
}