package me.magnum.horsemover;

import co.aikar.commands.BukkitCommandManager;
import eu.hexagonmc.spigot.annotation.plugin.Plugin;
import lombok.Getter;
import lombok.Setter;
import me.magnum.horsemover.commands.HM;
import me.magnum.horsemover.sql.Database;
import me.magnum.horsemover.util.Settings;
import me.magnum.horsemover.util.SimpleConfig;
import me.vagdedes.mysql.database.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

public final @Setter
@Getter
class HorseMover extends JavaPlugin {

    private static HorseMover plugin;
    private static BukkitCommandManager cmdMgr;
    @Getter
    private static Database database;
  /*
    private static String database;
    public static String tableFrom;
    public static String tableTo;
*/
    public SimpleConfig cfg;
    public static String pre;
    public static String version;

    public static HorseMover getPlugin() {
        return plugin;
    }

    public static void setPre(String pre) {
        HorseMover.pre = pre;
    }

    @Override
    public void onEnable() {
        plugin = this;
        cfg = new SimpleConfig("settings.yml");
        Settings.init();
        registerCommand();
        version = getDescription().getVersion();

    }

    private void registerCommand() {
        cmdMgr = new BukkitCommandManager(this);
        cmdMgr.registerCommand(new HM());
    }

/*
        Common.registerCommand(new HM(
                "sql", "Check sql status", "/HM", Arrays.asList("status", "dBa")));

    }
*/

    @Override
    public void onDisable() {
        if (MySQL.isConnected()) {
            MySQL.disconnect();

                    }
    }
}
