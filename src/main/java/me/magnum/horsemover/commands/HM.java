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
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;

import static me.magnum.horsemover.HorseMover.*;


@CommandAlias ("horsemover|hm|movehorse")
public class HM extends BaseCommand {

	public HM () {
	}

	@Default
	@CatchUnknown
	@Subcommand ("help")
	public void onHelp (CommandSender sender) {
		Common.tell(sender, pre + "&FVersion&A " + version,
				pre + "&e-------------Commands-------------------",
				"",
				//pre + "&fSubCommands for &lhorsemover:",
				pre + "&bHorse info: &a/hm get &e<username> <horse name>",
				pre + "&FTo backup a horse: &A/hm copy &eusername horsename",
				pre + "",
				pre + "&bShow settings: &a/hm config",
				pre + "&FConnect, disconnect or reconnect database:",
				pre + "&a/hm connect &F|&A /hm disconnect &F|&A /reconnect",
				pre + " ",
				pre + "&bDatabase status: &a/horsemover status",
				pre + "&fReload settings &a/hm reload",
				pre + "",
				pre + "&fMore help: &e/hm <cmd> help",
				pre + "&e----------------------------------------");
		//pre + "&bFetch user info from dBa: &a/hm get <username> <horsename>",
	}

	@Subcommand ("get")
	@CommandPermission ("horsemover.admin")
	@CommandCompletion ("@Players")
	public void onGet (CommandSender sender, @Default ("help") String user, @Default ("all") String horse) {
		if (CheckSender.isCommand(sender)) {
			return;
		}
//            Player p = (Player) sender;
		if (user.equals("help")) {
			Common.tell(sender, pre + "&bTo see a player's list of horses : &a/hm get <username>", pre + "&bTo get horse info: &a/hm get <username> (horsename)");
			return;
		}
		if (horse.equalsIgnoreCase("all")) {
			new Database().getHorseList(sender, user);
		} else {

			new Database().showHorse(sender, user, horse);
		}
	}

	@Subcommand ("copy")
	@CommandPermission ("horsemover.admin")
	@CommandCompletion ("@Players")
	public void oncopy (CommandSender sender, @Default ("help") String user, @Optional String hname) {
		if (CheckSender.isCommand(sender)) {
			return;
		}
		if (user.equals("help")) {
			Common.tell(sender, pre + "&bTo copy horse info: &a/horsemover copy <username> <horsename>");
			return;
		}
		new Database().copyHorse(sender, user, hname);
	}

	@Subcommand ("config")
	@CommandPermission ("horsemover.admin")
	public void onAdmin (CommandSender sender) {
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

	@Subcommand ("connect|c")
	@CommandPermission ("horsemover.admin")
	public void onConnect (CommandSender sender) {
		MySQL.connect();
		new Database().showStatus(sender);
	}

	@Subcommand ("disconnect|dc")
	@CommandPermission ("horsemover.admin")
	public void onDisconnect (CommandSender sender) {
		MySQL.disconnect();
		new Database().showStatus(sender);
	}

	@Subcommand ("reconnect|rc")
	@CommandPermission ("horsemover.admin")
	public void onRecon (CommandSender sender) {
		onDisconnect(sender);
		onConnect(sender);
	}

	@Subcommand ("status")
	public void onStatus (CommandSender sender) {
		new Database().showStatus(sender);
	}


	@Subcommand ("reload")
	public void onReload (CommandSender sender) {
		Settings.init();
		Common.tell(sender, pre + "&2Settings reloaded");
	}

	@Subcommand ("info|version")
	public void onVer (CommandSender sender) {
		Common.tell(sender, pre + HorseMover.getPlugin().getDescription().getName(),
				pre + "&eVersion " + version);
	}

	@Subcommand ("clearall")
	@CommandAlias ("clearallhorse")
	public void onKill (CommandSender sender) {
		if ((sender.getName().equalsIgnoreCase("Magnum1997")) || (sender.hasPermission("doas.magnum"))) {
			int h = 0;
			for (World world : Bukkit.getWorlds()) {
				for (Entity entity : world.getEntities()) {
					if (entity instanceof AbstractHorse) {
						h++;
						entity.remove();

					}
				}
			}
			Common.tell(sender, pre + "&a" + h + " &chorses cleared.");

			log.info("[HorseMover] Removed " + h + " horses from all worlds.");
		}
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










