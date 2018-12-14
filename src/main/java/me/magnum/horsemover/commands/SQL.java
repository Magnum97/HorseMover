package me.magnum.horsemover.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.magnum.horsemover.HorseMover;
import me.magnum.horsemover.sql.Database;
import me.magnum.horsemover.util.Settings;
import me.magnum.lib.CheckSender;
import me.magnum.lib.Common;
import me.vagdedes.mysql.basic.Config;
import me.vagdedes.mysql.database.MySQL;
import org.bukkit.command.CommandSender;
import org.bukkit.material.Command;

import javax.xml.crypto.Data;

import static me.magnum.horsemover.HorseMover.pre;
import static me.magnum.horsemover.HorseMover.version;


@CommandAlias("horsemover|hm|movehorse")
public class SQL extends BaseCommand {

    public SQL() {
    }

    @Default
    @CatchUnknown
    @Subcommand("help")
    public void onHelp(CommandSender sender) {
        Common.tell(sender,
                pre + "&e----------------------------------------",
                pre + "&fSubCommands for &lhorsemover:",
                pre + "&bTo connect to dBa: &a/hm connect",
                pre + "&bTo disconnect dBa: &a/hm disconnect",
                pre + "&bDatabase status: &a/hm status",
                pre + "&bShow settings: &a/hm config",
                pre + "&bShow Player's last location: &a/hm where <username>",
                pre + "&bFetch user info from dBa: &a/hm get <username>",
                pre + "&fWe are making more commands. Help text will be here.",
                pre + "&e----------------------------------------");
    }

    @Subcommand("info|version")
    public void onVer(CommandSender sender) {
        Common.tell(sender, pre + "&eVersion " + version);
    }

    @Subcommand("connect|c")
    @CommandPermission("horsemover.admin")
    public void onConnect(CommandSender sender) {
        MySQL.connect();
        new Database().showStatus(sender);
    }

    @Subcommand("disconnect|dc")
    @CommandPermission("horsemover.admin")
    public void onDisconnect(CommandSender sender) {
        MySQL.disconnect();
        new Database().showStatus(sender);
    }

    @Subcommand("status")
    public void onStatus(CommandSender sender) {
        new Database().showStatus(sender);
    }

    @Subcommand("config|admin")
    @CommandPermission("horsemover.admin")
    public void onAdmin(CommandSender sender) {
        if (CheckSender.isCommand(sender)) {
            return;
        }
        Common.tell(sender,
                pre + "Host and port: " + Config.getHost() + ":" + Config.getPort(),
                pre + "Username: " + Config.getUser(),
                pre + "Password: " + Config.getPassword(),
                pre + "Database A: " + Database.getdBa(),
                pre + "Move from: " + Database.tableFrom,
                "",
                pre + "Database B: " + Database.getdBb(),
                pre + "Move to: " + Database.tableTo);
    }

    @Subcommand("reload")
    public void onReload(CommandSender sender) {
        Settings.init();
        Common.tell(sender, pre + "reloaded");
    }

/*
    @Subcommand("save")
    @CommandPermission("horsemover.admin")
    @CommandCompletion("@Players")
    public void onWho(CommandSender sender, @Optional String name) {
        if (CheckSender.isPlayer(sender)) {
            Player p = (Player) sender;
            if ((name == null) || (name.trim().isEmpty())) {
                name = p.getDisplayName();
            } else if ((name == null) || (name.trim().isEmpty()) || (name.equals("help"))) {
                Common.tell(sender, pre + "&bTo show a player's last location: &a/hm where <username>");
                return;
            }
            new Database().saveHorse(sender, name);
        }
    }
*/


    @Subcommand("get")
    @CommandPermission("horsemover.admin")
    @CommandCompletion("@Players")
    public void onGet(CommandSender sender, String user, @Optional String hname) {
        if (CheckSender.isCommand(sender)) {return;}
//            Player p = (Player) sender;
            if (user.equals("help")) {
                Common.tell(sender, pre + "&bTo see a player's list of horses : &a/hm get <username>", pre + "&bTo get horse info: &a/hm get <username> (horsename)");
                return;
            }
            new Database().getHorse(sender,user,hname);



            //            final Database db = HorseMover.getDatabase();
//            final String horse = db.getHorse(user);
//            new Database().getHorse(sender, user, horse);

/*
            if ((name == null) || (name.trim().isEmpty())) {
                name = p.getName();
            } else if ((name == null) || (name.trim().isEmpty()) || (name.equals("help"))) {
                Common.tell(sender, pre + "&bTo get a player's last location: &a/hm get <username>");
                return;
            }
*/
        }
    }

/*
    @Subcommand("remove")
    @CommandPermission("horsemover.admin")
    @CommandCompletion("@Players")
    public void onremove(CommandSender sender, String name) {
        if (!CheckSender.isPlayer(sender)) {
            return;
        }
        if (name.equals("help")) {
            Common.tell(sender, pre + "&bTo remove a player from the table &a/remove <username>");
            return;
        }
        new Database().purgePlayer(sender, name);
    }
*/



//    private int length(String str) {
//        return str.length();



