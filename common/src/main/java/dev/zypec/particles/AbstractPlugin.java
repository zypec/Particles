package dev.zypec.particles;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractPlugin extends JavaPlugin {

    @Override
    public void onDisable() {
        if (Particles.getDatabaseManager() != null)
            Particles.getDatabaseManager().close();
    }

    public void disable(String message) {
        getLogger().warning(message);
        getPluginLoader().disablePlugin(this);
    }
}