package me.magnum.horsemover;

import co.aikar.commands.BukkitCommandManager;
import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import lombok.Setter;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.magnum.horsemover.commands.HM;
import me.magnum.horsemover.sql.Database;
import me.magnum.horsemover.util.Settings;
import me.magnum.horsemover.util.SimpleConfig;
import me.vagdedes.mysql.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final @Setter
@Getter
class HorseMover extends JavaPlugin implements Listener {

    public static HorseMover plugin;
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
    public static Logger log = Bukkit.getLogger();
    public static InventoryManager iManager;
    public static HeadDatabaseAPI headAPI;


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
        iManager = new InventoryManager(plugin);
        iManager.init();
        this.getServer().getPluginManager().registerEvents(this, this);
    }
    
    @EventHandler
    private void hdbLoad (DatabaseLoadEvent event) {
        headAPI = new HeadDatabaseAPI();
        
    }
    private void registerCommand() {
        cmdMgr = new BukkitCommandManager(this);
        cmdMgr.enableUnstableAPI("help");
        cmdMgr.registerCommand(new HM());
    }

/*
        Common.registerCommand(new HM(
                "sql", "Check sql status", "/HM", Arrays.asList("status", "dba")));

    }
*/

    @Override
    public void onDisable() {
        if (MySQL.isConnected()) {
            MySQL.disconnect();

                    }
    }
}
