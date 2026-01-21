package dev.zypec.particles.gui.type.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import dev.zypec.particles.gui.GUIHolder;
import dev.zypec.particles.effect.handler.HandlerEvent;

@Getter
@AllArgsConstructor
public class AdminHolder extends GUIHolder {
    private FilterCategory category;
    private HandlerEvent event;
}