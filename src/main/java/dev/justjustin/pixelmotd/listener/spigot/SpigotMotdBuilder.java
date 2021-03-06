package dev.justjustin.pixelmotd.listener.spigot;

import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.listener.Icon;
import dev.justjustin.pixelmotd.listener.MotdBuilder;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

import java.io.File;

public class SpigotMotdBuilder extends MotdBuilder<JavaPlugin, CachedServerIcon> {

    public SpigotMotdBuilder(PixelMOTD<JavaPlugin> plugin, SlimeLogs logs) {
        super(plugin, logs);
    }

    @Override
    public Icon<CachedServerIcon> createIcon(MotdType motdType, File icon) {
        return new SpigotIcon(
                getLogs(),
                motdType,
                icon
        );
    }
}
