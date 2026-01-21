package dev.zypec.particles.effect.data;

import dev.zypec.particles.effect.script.variable.data.VariableData;
import org.bukkit.Location;

import java.util.List;

public class EmptyEffectData extends EffectData {

    public EmptyEffectData(List<VariableData> variables) {
        this.variables = variables;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public Double getVariable(String variable) {
        return null;
    }
}