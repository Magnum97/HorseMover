package me.magnum.horsemover.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.*;
import com.HakYazilim.HorseRPGv3.utils.HorseBreeds;
import me.magnum.horsemover.HorseMover;
import me.magnum.horsemover.sql.Database;
import me.magnum.horsemover.util.Page1;
import me.magnum.horsemover.util.RPGHorse;
import me.magnum.horsemover.util.Settings;
import me.magnum.lib.CheckSender;
import me.magnum.lib.Common;
import me.vagdedes.mysql.basic.Config;
import me.vagdedes.mysql.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static me.magnum.horsemover.HorseMover.*;


@CommandAlias("horsemover|hm|movehorse")
@CommandPermission("horsemover.use")
public class HM extends BaseCommand {
	
	public HM () {
	}
	
	// @Default
	// @CatchUnknown
	@Subcommand("help")
	public void onHelp (CommandSender sender) {
		Common.tell(sender, pre + "&FVersion&A " + version,
		            pre + "&e-------------Commands-------------------",
		            pre,
		            pre + "&fSubCommands for &lhorsemover:",
		            pre + "&bHorse info: &a/hm get &e<username> <horse name>",
		            pre + "&FTo backup a horse: &A/hm copy &eusername horsename",
		            pre,
		            pre + "&bShow settings: &a/hm config",
		            pre + "&FConnect, disconnect or reconnect database:",
		            pre + "&a/hm connect &F|&A /hm disconnect &F|&A /reconnect",
		            pre,
		            pre + "&bDatabase status: &a/horsemover status",
		            pre + "&fReload settings &a/hm reload",
		            pre,
		            pre + "&fMore help: &e/hm <cmd> help",
		            pre + "&e----------------------------------------");
		//pre + "&bFetch user info from dba: &a/hm get <username> <horsename>",
	}
	
	@HelpCommand
	public void doHelp (CommandSender sender, CommandHelp help) {
		Common.tell(sender, pre + ("Anti Grief Help"));
		help.showHelp();
	}
	
	@Subcommand("get")
	@CommandCompletion("@Players")
	@Description("Show horse information")
	public void onGet (CommandSender sender, @Default("help") String user, @Default("all") String horsename) {
		if (CheckSender.isCommand(sender)) {
			return;
		}
		if (user.equals("help")) {
			Common.tell(sender, pre + "&bTo see a player's list of horses : &a/hm get <username>", pre + "&bTo get horse info: &a/hm get <username> (horsename)");
			return;
		}
		new BukkitRunnable() {
			@Override
			public void run () {
				
				Map <String, HashMap <String, Object>> horses = new Database().getHorseList(user);
				
				if (horsename.equalsIgnoreCase("all")) {
					horses.forEach((k, v) -> {
						Common.tell(sender, pre + (String.format("Horses: %s", k)));
					});
				}
				else {
					if (horses.containsKey(horsename)) {
						String id = horses.get(horsename).get("id").toString();
						String color = horses.get(horsename).get("color").toString();
						String style = horses.get(horsename).get("style").toString();
						String breed = horses.get(horsename).get("breed").toString();
						String gender = horses.get(horsename).get("gender").toString();
						
						String result =
								"&E ID#:&a " + id + " &eName: &a" +
										horsename + " &7is a &f" +
										color + " " +
										style + " " +
										breed + " " +
										gender;
						Common.tell(sender, pre + result);
					}
					else {
						Common.tell(sender, pre + "No Match");
					}
				}
			}
		}.runTaskAsynchronously(HorseMover.plugin);
	}
	
	@Subcommand("copy")
	@CommandPermission("horsemover.admin")
	@CommandCompletion("@Players")
	@Description("Move a horse from 2.0 to 3.0")
	public void oncopy (CommandSender sender, @Default("help") String user, @Optional String horsename) {
		if (CheckSender.isCommand(sender)) {
			return;
		}
		if (user.equals("help")) {
			Common.tell(sender, pre + "&bTo copy horse info: &a/horsemover copy <username> <horsename>");
			return;
		}
		Map <String, HashMap <String, Object>> horses = new Database().getHorseList(user);
		if (horses.containsKey(horsename)) {
			String id = horses.get(horsename).get("id").toString();
			String color = horses.get(horsename).get("color").toString();
			String style = horses.get(horsename).get("style").toString();
			String breed = horses.get(horsename).get("breed").toString();
			String gender = horses.get(horsename).get("gender").toString();
			if (breed.equalsIgnoreCase("NOTCHOSEN")) {
				breed = (randomBreed().getBreedName());
			}
			String result =
					"&E ID#:&a " + id + " &eName: &a" +
							horsename + " &7is a &f" +
							color + " " +
							style + " " +
							breed + " " +
							gender;
			Common.tell(sender, pre + "&ePreparing to copy &f" + horsename + "&e for &f" + user,
			            pre + result);
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(user);
			Player caster = (Player) sender;
			//			Player owner = (Player) offlinePlayer;
			
			RPGHorse newHorse = new RPGHorse(offlinePlayer, horsename, color, style, breed, gender);
			Horse realHorse = caster.getWorld().spawn(caster.getLocation(), Horse.class);
			realHorse.setTamed(true);
			realHorse.setAdult();
			realHorse.setOwner(caster);
			realHorse.setCustomName(horsename);
			realHorse.setMetadata("horseRPG_uuid", new FixedMetadataValue((HorseMover.getPlugin()), newHorse.horseUUID));
			realHorse.setColor(newHorse.color);
			realHorse.setStyle(newHorse.style);
			realHorse.setInvulnerable(true);
			
			Common.tell(sender, pre + "Moving horse to new pasture...");
			
			new BukkitRunnable() {
				@Override
				public void run () {
					new Database().saveHorse(newHorse);
					new Database().killHorse(Integer.parseInt(id));
				}
			}.runTaskAsynchronously(HorseMover.plugin);
			
			Common.tell(sender, pre + "Cleaning up old pasture..", pre + "Done.");
			
			
		}
		else {
			Common.tell(sender, pre + "&cCould not find that horse.");
		}
		
		
	}
	
	@Subcommand("config")
	@CommandPermission("horsemover.admin")
	@Description("Show settings")
	public void onAdmin (CommandSender sender) {
		if (CheckSender.isCommand(sender)) {
			return;
		}
		Common.tell(sender,
		            pre + "Host and port: " + Config.getHost() + ":" + Config.getPort(),
		            pre + "&7Username: " + Config.getUser(),
		            pre + "&7Password: ******** &e<check settings.yml",
		            pre + "Database: " + Database.getDba(),
		            pre + "Move from: " + Database.tableFrom,
		            //                "",
		            //                pre + "Database B: " + Database.getDbb(),
		            pre + "Move to: " + Database.tableTo);
	}
	
	@Subcommand("connect|c")
	@CommandPermission("horsemover.admin")
	public void onConnect (CommandSender sender) {
		MySQL.connect();
		new Database().showStatus(sender);
	}
	
	@Subcommand("disconnect|dc")
	@CommandPermission("horsemover.admin")
	public void onDisconnect (CommandSender sender) {
		MySQL.disconnect();
		new Database().showStatus(sender);
	}
	
	@Subcommand("reconnect|rc")
	@CommandPermission("horsemover.admin")
	public void onRecon (CommandSender sender) {
		onDisconnect(sender);
		onConnect(sender);
	}
	
	@Subcommand("status")
	@CommandPermission("horsemover.admin")
	public void onStatus (CommandSender sender) {
		new Database().showStatus(sender);
	}
	
	
	@Subcommand("reload")
	@CommandPermission("horsemover.admin")
	@Description("Reload the configuration")
	public void onReload (CommandSender sender) {
		Settings.init();
		Common.tell(sender, pre + "&2Settings reloaded");
	}
	
	@Subcommand("info|version")
	public void onVer (CommandSender sender) {
		Common.tell(sender, pre + HorseMover.getPlugin().getDescription().getName(),
		            pre + "&eVersion " + version);
	}
	
	@Subcommand("clearall")
	@CommandAlias("clearallhorses")
	@CommandPermission("horsemover.clear")
	@Description("Despawn all horse entities (except llamas)")
	public void onKill (CommandSender sender) {
		int h = 0;
		int l = 0;
		for (World world : Bukkit.getWorlds()) {
			for (Entity entity : world.getEntities()) {
				if (entity instanceof AbstractHorse) {
					if (entity.getType().equals(EntityType.LLAMA)) {
						l++;
					}
					else {
						h++;
						entity.remove();
					}
				}
			}
		}
		Common.tell(sender,
		            pre +
				            "&a" + h + "&c horses cleared.",
		            pre +
				            "&a" + l + "&e llamas were saved.");
		
		log.info("[HorseMover] Removed " + h + " horses from all worlds.");
	}
	
	@Subcommand("gui")
	@CommandAlias("gui")
	@Description("Gui gui chewy chewy")
	public void onGui (CommandSender sender) {
		if (CheckSender.isPlayer(sender)) {
			Player player = (Player) sender;
			Page1.MENU_1.open(player);
		}
	}
	
	private static final List <HorseBreeds> VALUES = Collections.unmodifiableList((Arrays.asList(HorseBreeds.values())));
	private static final int SIZE = VALUES.size();
	private static final Random rand = new Random();
	
	private static HorseBreeds randomBreed () {
		return VALUES.get(rand.nextInt(SIZE));
	}
	
	
}
