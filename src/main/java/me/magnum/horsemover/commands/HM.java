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
import org.bukkit.entity.Player;

import static me.magnum.horsemover.HorseMover.pre;
import static me.magnum.horsemover.HorseMover.version;


@CommandAlias("horsemover|hm|movehorse")
public class HM extends BaseCommand {

    public HM() {
    }

    @CatchUnknown
    @Subcommand("help")
    public void onHelp(CommandSender sender) {
        Common.tell(sender, pre + "&FVersion&A " + version,
                pre + "&e-------------Commands-------------------",
                pre + "&bTo get horse info: &a/horsemover get"); // "&e<username> (horse name)",
        if ((sender.isPermissionSet("horsemover.admin")) || (CheckSender.isConsole(sender))) {
            Common.tell(sender, pre + "&e-------- Admin Commands ----------------",
                    pre + "&FTo backup a horse: &A/horsemover copy", // &eusername horsename",
                    pre + "&bShow settings: &a/horsemover config",
                    pre + "&FConnect, disconnect or reconnect database:",
                    pre + "&a/hm connect &F|&A /hm disconnect &F|&A /reconnect",
                    pre + "&bDatabase status: &a/horsemover status",
                    pre + "&fReload settings &a/hm reload");
        }
        Common.tell(sender, pre + "",
                pre + "&fMore help: &e/horsemover <cmd> help",
                pre + "&e----------------------------------------");
        //pre + "&bFetch user info from dBa: &a/hm get <username> <horsename>",
//        Player p = (Player) sender;
    }

    @Subcommand("get")
    @CommandCompletion("@Players")
    public void onGet(CommandSender sender, @Default("help") String user, @Default("all") String horse) {
        if (CheckSender.isCommand(sender)) {
            return;
        }
        if (user.equals("help")) {
            Common.tell(sender,
                    pre + "&bTo get a list of player's horses : &a/horsemover get <username>", pre + "&bTo get horse info: &a/horsemover get <username> (horsename)");
            return;
        }
        new Database().getHorse(sender, user, horse);
    }

    @Subcommand("copy")
    @CommandPermission("horsemover.admin")
    @CommandCompletion("@Players")
    public void onCopy(CommandSender sender, @Default("help") String user, @Default(" ") String hname) {
        if (CheckSender.isCommand(sender)) {
            return;
        }
//            Player p = r;
        if ((user.equals("help"))) {
            Common.tell(sender, pre + "&bTo copy horse info: &a/horsemover copy <username> <horsename>");
            return;
        }
        if (hname.trim().isEmpty()) {
            Common.tell(sender, pre + "&cYou must specify a horse to copy.");
//            user = "help";
            return;
        }
        new Database().copyHorse(sender, user, hname);
    }

    @Subcommand("remove")
    @CommandPermission("horsemover.admin")
    @CommandCompletion("@Players")
    public void onRemove(CommandSender sender, @Default("help") String user, @Default(" ") String horse) {
        if (CheckSender.isCommand(sender)) {
            return;
        }
        if ((user.equals("help"))) {
            Common.tell(sender, pre + "&eTo remove horse from db: &a/horsemover remove <username> <horsename>");
            return;
        }
        if (horse.trim().isEmpty()) {
            Common.tell(sender, pre + "&cYou must specify a horse to remove.");
//            user = "help";
            return;
        }
        Common.tell(sender, pre + "Checkpoint A");
        new Database().removeHorse(sender, user, horse);
    }

    @Subcommand("config")
    @CommandPermission("horsemover.admin")
    public void onAdmin(CommandSender sender) {
        if (CheckSender.isCommand(sender)) {
            return;
        }
        Common.tell(sender,
                pre + "Host and port: " + Config.getHost() + ":" + Config.getPort(),
                pre + "&7Username: " + Config.getUser(),
                pre + "&7Password: ******** &e<check settings.yml",
                pre + "Database: " + Database.getdBa(),
                pre + "Move from: " + Database.tableFrom,
//                "",
//                pre + "Database B: " + Database.getdBb(),
                pre + "Move to: " + Database.tableTo);
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

    @Subcommand("reconnect|rc")
    @CommandPermission("horsemover.admin")
    public void onRecon(CommandSender sender) {
        onDisconnect(sender);
        onConnect(sender);
    }

    @Subcommand("status")
    @CommandPermission("horsemover.admin")
    public void onStatus(CommandSender sender) {
        new Database().showStatus(sender);
    }


    @Subcommand("reload")
    @CommandPermission("horsemover.admin")
    public void onReload(CommandSender sender) {
        Settings.init();
        Common.tell(sender, pre + "&2Settings reloaded");
    }

    @Default
    @Subcommand("info|version")
    public void onVer(CommandSender sender) {
        Common.tell(sender, pre + "&9--===&a " + HorseMover.getPlugin().getDescription().getFullName() + " &9===--",
                pre + "by &6" + HorseMover.getPlugin().getDescription().getAuthors(),
//                pre + "&eVersion " + version,
                pre + "&f see &e/horsemover help &ffor usage.");
    }

}











