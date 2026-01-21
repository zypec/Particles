package dev.zypec.particles.gui.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import dev.zypec.particles.gui.type.GUIType;

import java.util.Map;

@Getter
@AllArgsConstructor
public class GUIStyle {
    private String id;
    private Map<GUIType, GUILayout> layouts;
}