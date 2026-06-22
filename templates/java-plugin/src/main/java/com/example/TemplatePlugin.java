package com.{{groupId}};

import org.bukkit.plugin.java.JavaPlugin;

public final class {{mainClass}} extends JavaPlugin {

    private static {{mainClass}} instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        registerCommands();
        registerListeners();
        getLogger().info("{{pluginName}} v" + getDescription().getVersion() + " ativado!");
    }

    @Override
    public void onDisable() {
        getLogger().info("{{pluginName}} desativado!");
    }

    public static {{mainClass}} getInstance() {
        return instance;
    }

    private void registerCommands() {
        var cmd = getCommand("{{commandName}}");
        if (cmd != null) {
            cmd.setExecutor(new {{mainCommandClass}}());
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new {{mainListenerClass}}(), this);
    }
}
