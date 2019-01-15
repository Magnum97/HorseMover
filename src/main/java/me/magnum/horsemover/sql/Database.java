package me.magnum.horsemover.sql;

import lombok.Data;
import me.magnum.lib.Common;
import me.vagdedes.mysql.basic.Config;
import me.vagdedes.mysql.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static me.magnum.horsemover.HorseMover.plugin;
import static me.magnum.horsemover.HorseMover.pre;
import static me.magnum.horsemover.util.Settings.*;
import static me.vagdedes.mysql.database.SQL.createTable;
import static me.vagdedes.mysql.database.SQL.tableExists;

public @Data
class Database {
//    private final MagSQL magSQL;

	public static String dBa;
	public static String dBb;
	public static String tableFrom;
	public static String tableTo;

	String playerId;
//	String ign;
//	String horsename;

	PreparedStatement update;
	PreparedStatement get;
	PreparedStatement insert;
	PreparedStatement delete;

	//<Player ,  <HorseName,  Horse>>
	public HashMap<Player, HashMap<String, AbstractHorse>> activeHorses = new HashMap<Player, HashMap<String, AbstractHorse>>();

	public Dataworks dataworks = new Dataworks(plugin, host, port, database, username, password);
//	public Dataworks dataworks = new DataWorks(this,
//			getConfig().getString("Database.Host" , "localhost"),
//			getConfig().getString("Database.Port" , "3306"),
//			getConfig().getString("Database.Database" , "horserpg"),
//			getConfig().getString("Database.UserName" , "root"),
//			getConfig().getString("Database.Password" , "password"));


	private void dbSet (char database) {
		String noSSL = "?useSSL=false";
		switch (database) {
			case 'a':
				Config.setDatabase(dBa + noSSL);
				break;
			case 'b':
				Config.setDatabase(dBb + noSSL);
				break;
		}
	}

	public void Setup () {
		dbSet('a');
		if (!MySQL.isConnected()) {
			MySQL.connect();
		} else MySQL.reconnect();

/*
        new BukkitRunnable() {
            @Override
            public void run() {
                ResultSet db = MagSQL.query("SHOW DATABASES LIKE '" + dBa + "';");

                try {
                    if (db.first()) {
                        return;
                    }
                    MagSQL.query("CREATE DATABASE IF NOT EXISTS " + dBa + ";");

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                }

            }
        }.runTaskAsynchronously(getPlugin());
        new BukkitRunnable() {
            @Override
            public void run() {
                ResultSet db = MagSQL.query("SHOW DATABASES LIKE '" + dBb + "';");

                try {
                    if (db.first()) {
                        return;
                    }
                    MagSQL.query("CREATE DATABASE IF NOT EXISTS " + dBb + ";");

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                }

            }
        }.runTaskAsynchronously(getPlugin());

*/
		dbSet('a');
		if (!(tableExists(tableFrom))) {
			createTable(tableFrom, "`ID` INT NOT NULL AUTO_INCREMENT COMMENT '',`o_UUID` VARCHAR(36) NOT NULL COMMENT '',`h_Name` VARCHAR(255) NOT NULL COMMENT '',`Rented` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '',`h_Color` VARCHAR(45) NULL COMMENT '',`h_Variant` VARCHAR(45) NULL COMMENT '',`h_Style` VARCHAR(45) NULL DEFAULT NULL COMMENT '',`h_Gender` VARCHAR(45) NULL COMMENT '',`h_Breed` VARCHAR(45) NULL COMMENT '',`h_Armor` VARCHAR(45) NULL COMMENT '',`h_Saddle` TINYINT(1) NULL DEFAULT 0 COMMENT '',`h_LastBreed` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '',`travel_dist` DOUBLE NOT NULL DEFAULT 0 COMMENT '',`s_Agility` DOUBLE NOT NULL DEFAULT 0 COMMENT '',`sw_Lvl`  TINYINT(1) NULL DEFAULT 0 COMMENT '',`ag_Lvl`  TINYINT(1) NULL DEFAULT 0 COMMENT '',`s_Energy` TINYINT(3) NOT NULL DEFAULT 100 COMMENT '',`s_Swiftness` DOUBLE NOT NULL DEFAULT 0 COMMENT '',PRIMARY KEY (`ID`)  COMMENT '',UNIQUE INDEX `ID_UNIQUE` (`ID` ASC)  COMMENT ''");
		}
		dbSet('b');
		MySQL.reconnect();
		if (!(tableExists(tableTo))) {
			createTable(tableTo, "`ID` INT NOT NULL AUTO_INCREMENT COMMENT '',`o_UUID` VARCHAR(36) NOT NULL COMMENT '',`h_Name` VARCHAR(255) NOT NULL COMMENT '',`Rented` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '',`h_Color` VARCHAR(45) NULL COMMENT '',`h_Variant` VARCHAR(45) NULL COMMENT '',`h_Style` VARCHAR(45) NULL DEFAULT NULL COMMENT '',`h_Gender` VARCHAR(45) NULL COMMENT '',`h_Breed` VARCHAR(45) NULL COMMENT '',`h_Armor` VARCHAR(45) NULL COMMENT '',`h_Saddle` TINYINT(1) NULL DEFAULT 0 COMMENT '',`h_LastBreed` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '',`travel_dist` DOUBLE NOT NULL DEFAULT 0 COMMENT '',`s_Agility` DOUBLE NOT NULL DEFAULT 0 COMMENT '',`sw_Lvl`  TINYINT(1) NULL DEFAULT 0 COMMENT '',`ag_Lvl`  TINYINT(1) NULL DEFAULT 0 COMMENT '',`s_Energy` TINYINT(3) NOT NULL DEFAULT 100 COMMENT '',`s_Swiftness` DOUBLE NOT NULL DEFAULT 0 COMMENT '',PRIMARY KEY (`ID`)  COMMENT '',UNIQUE INDEX `ID_UNIQUE` (`ID` ASC)  COMMENT ''");
		}
		if (!(tableExists("tblHorses"))) {
			createTable("tblHorses", "fldHorseId INT NOT NULL AUTO_INCREMENT,fldHorseUUID VARCHAR(255) NOT NULL,fldOwnerUUID VARCHAR(255) NOT NULL,fldBreed VARCHAR(255) NOT NULL,fldName VARCHAR(255) NOT NULL,fldSex VARCHAR(255) NOT NULL,fldAgilityLevel DECIMAL NOT NULL,fldSwiftnessLevel DECIMAL NOT NULL,fldGroundworkLevel DECIMAL NOT NULL,fldTravelDistance DECIMAL NOT NULL,fldSaddle VARCHAR(255) NOT NULL,fldAgilityXp DECIMAL NOT NULL,fldSwiftnessXp DECIMAL NOT NULL,fldGroundworkXp DECIMAL NOT NULL,fldEnergy DECIMAL NOT NULL,fldTrait VARCHAR(255) NOT NULL,fldSicknessList VARCHAR(255) NOT NULL,fldVaccineList VARCHAR(255) NOT NULL,fldInjuryList VARCHAR(255) NOT NULL,fldTrustedPlayerList VARCHAR(255) NOT NULL,fldDead BIT NOT NULL,fldType INT NOT NULL,fldPregnant BIT NOT NULL,fldBirthtime BIGINT NOT NULL,fldBreedingtime BIGINT NOT NULL,fldLeaseOwner VARCHAR(255) NOT NULL,fldLeaseStart BIGINT NOT NULL,fldLeasePeriod BIGINT NOT NULL,fldStyle VARCHAR(255) NOT NULL,fldColor VARCHAR(255) NOT NULL,fldVariant VARCHAR(255) NOT NULL,fldArmor VARCHAR(255) NOT NULL,primary key(fldHorseId)");
        /* Axel's table
                    statement.execute("CREATE TABLE IF NOT EXISTS tblHorses(fldHorseId INT NOT NULL AUTO_INCREMENT,fldHorseUUID VARCHAR(255) NOT NULL,fldOwnerUUID VARCHAR(255) NOT NULL,fldBreed VARCHAR(255) NOT NULL,fldName VARCHAR(255) NOT NULL,fldSex VARCHAR(255) NOT NULL,fldAgilityLevel DECIMAL NOT NULL,fldSwiftnessLevel DECIMAL NOT NULL,fldGroundworkLevel DECIMAL NOT NULL,fldTravelDistance DECIMAL NOT NULL,fldSaddle VARCHAR(255) NOT NULL,fldAgilityXp DECIMAL NOT NULL,fldSwiftnessXp DECIMAL NOT NULL,fldGroundworkXp DECIMAL NOT NULL,fldEnergy DECIMAL NOT NULL,fldTrait VARCHAR(255) NOT NULL,fldSicknessList VARCHAR(255) NOT NULL,fldVaccineList VARCHAR(255) NOT NULL,fldInjuryList VARCHAR(255) NOT NULL,fldTrustedPlayerList VARCHAR(255) NOT NULL,fldDead BIT NOT NULL,fldType INT NOT NULL,fldPregnant BIT NOT NULL,fldBirthtime BIGINT NOT NULL,fldBreedingtime BIGINT NOT NULL,fldLeaseOwner VARCHAR(255) NOT NULL,fldLeaseStart BIGINT NOT NULL,fldLeasePeriod BIGINT NOT NULL,fldStyle VARCHAR(255) NOT NULL,fldColor VARCHAR(255) NOT NULL,fldVariant VARCHAR(255) NOT NULL,fldArmor VARCHAR(255) NOT NULL,primary key(fldHorseId))");

 */
		}
	}

	public void showStatus (CommandSender sender) {
		if (MySQL.isConnected()) {
			Common.tell(sender,
					pre + "Status: &aConnected to " + Config.getDatabase());
		} else {
			Common.tell(sender, pre + "Status: &cDatabase not connected.");
		}
	}

	public void getHorseList (CommandSender sender, String user) {
		MySQL.disconnect();
		dbSet('a');
		MySQL.connect();
		OfflinePlayer offp = Bukkit.getOfflinePlayer(user);
		playerId = offp.getUniqueId().toString();
		Map<String, HashMap<String, Object>> horses = new HashMap<String, HashMap<String, Object>>();

		try {
			ResultSet rs = MySQL.query("SELECT * FROM " + tableFrom + " WHERE o_UUID='" + playerId + "' ORDER BY h_name;");

			while (rs.next()) {
				HashMap<String, Object> horse = new HashMap<String, Object>();
				String hName = rs.getString("h_Name");
				horse.put("id", rs.getInt("ID"));
				horse.put("name", hName);
				horse.put("color", rs.getString("h_Color"));
				horse.put("variant", rs.getString("h_Variant"));
				horse.put("style", rs.getString("h_Style"));
				horse.put("gender", rs.getString("h_Gender"));
				horse.put("breed", rs.getString("h_Breed"));
				horse.put("armor", rs.getString("h_Armor"));
				horse.put("saddle", rs.getInt("h_Saddle"));
				horse.put("tDistance", (rs.getDouble("travel_dist")));
				horse.put("agility", rs.getDouble("s_Agility"));
				horse.put("switfness", rs.getDouble("s_Swiftness"));
				horse.put("agLvl", rs.getInt("ag_Lvl"));
				horse.put("swLvl", rs.getInt("sw_Lvl"));
				horse.put("energy", rs.getInt("s_Energy"));
				horses.put(hName, horse);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (!MySQL.isConnected()) {
				MySQL.disconnect();
			}
		}

		horses.forEach((k, v) -> {
//			String hname = horses.get()
			Common.tell(sender, pre + (String.format("Horses: %s", k)));
		});
	}

	public void getHorse (CommandSender sender, String user, String horsename) {
		MySQL.disconnect();
		dbSet('a');
		MySQL.connect();
		OfflinePlayer offp = Bukkit.getOfflinePlayer(user);
		playerId = offp.getUniqueId().toString();
		Map<String, HashMap<String, Object>> horses = new HashMap<String, HashMap<String, Object>>();

		try {
			ResultSet rs = MySQL.query("SELECT * FROM " + tableFrom + " WHERE o_UUID='" + playerId + "' ORDER BY h_name;");

			while (rs.next()) {
				HashMap<String, Object> horse = new HashMap<String, Object>();
				String hName = rs.getString("h_Name");
				horse.put("id", rs.getInt("ID"));
				horse.put("name", hName);
				horse.put("color", rs.getString("h_Color"));
//				horse.put("variant", rs.getString("h_Variant"));
//				horse.put("style", rs.getString("h_Style"));
				horse.put("breed", rs.getString("h_Breed"));
				horse.put("gender", rs.getString("h_Gender"));
//				horse.put("armor", rs.getString("h_Armor"));
//				horse.put("saddle", rs.getInt("h_Saddle"));
//				horse.put("tDistance", (rs.getDouble("travel_dist")));
//				horse.put("agility", rs.getDouble("s_Agility"));
//				horse.put("switfness", rs.getDouble("s_Swiftness"));
//				horse.put("agLvl", rs.getInt("ag_Lvl"));
//				horse.put("swLvl", rs.getInt("sw_Lvl"));
//				horse.put("energy", rs.getInt("s_Energy"));
				horses.put(hName, horse);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (!MySQL.isConnected()) {
				MySQL.disconnect();
			}
		}

		if (horses.containsKey(horsename)) {
			String result = "&E ID#:&a " + horses.get(horsename).get("id") + " &eName: &a" + horses.get(horsename).get("name") + " &7is a &f" + horses.get(horsename).get("color") + " " + horses.get(horsename).get("breed") + " " + horses.get(horsename).get("gender");
			Common.tell(sender, pre + result);
//			Common.tell(sender, pre+"ID Name color breed gender");
		} else
			Common.tell(sender, pre + "No Match");

/*
		horses.forEach((k, v) -> {
			if (k.equalsIgnoreCase(horsename)) {
				HashMap<String, Object> horsemap = new HashMap<>();
				horsemap = horses.get(horsename);
				horsemap.forEach((h, t) -> {
					String result = String.format("\n&E ID#:&a %d &eName: &f%s &8is a %s %s %s", v, v, v, v);
					Common.tell(sender, pre + result);
				});
			} else
				Common.tell(sender, pre + "No horse by that name found");
//			 String result = "\n&EID#: &a" + ID + "&e Name: &f" + hname + " &8is a " + color.toLowerCase() + " " + breed.toLowerCase() + " " + gender.toLowerCase();
		});
*/
	}

	// --Commented out by Inspection START (12/13/2018 1:04 AM):
	@SuppressWarnings ("deprication")
	public void oldgetHorse (CommandSender sender, String user, String horse) {
		MySQL.disconnect();
		dbSet('a');
		MySQL.connect();
		OfflinePlayer offp = Bukkit.getOfflinePlayer(user);
		playerId = offp.getUniqueId().toString();

		///
		if (horse.equalsIgnoreCase("all")) {
			try {
				ResultSet rs = MySQL.query("SELECT * FROM " + tableFrom + " WHERE o_UUID='" + playerId + "' ORDER BY h_name;");

				if (rs.first()) {
					rs.first();
					Common.tell(sender, pre + "&aHorses belonging to&e " + user);
					do {
						int ID = rs.getInt("ID");
						String hname = rs.getString("h_Name");
						String color = rs.getString("h_Color");
						String breed = rs.getString("h_Breed");
						String gender = rs.getString("h_Gender");
						String result = "\n&EID#: &a" + ID + "&e Name: &f" + hname + " &8is a " + color.toLowerCase() + " " + breed.toLowerCase() + " " + gender.toLowerCase();
//						String result = "\n&fName: " + hname + " is a " + color + " " + breed + " " + gender;
//						Common.tell(sender, result);
					} while (rs.next());
				} else Common.tell(sender, pre + "&eNo horses found for owner &f" + user);

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException npe) {
			}
		} else
			///
			try {
				ResultSet rs = MySQL.query("SELECT * FROM " + tableFrom + " WHERE o_UUID='" + playerId + "' AND h_name='" + horse + "' ORDER BY h_name;");


				if (rs.first()) {
					rs.first();
					do {
						int ID = rs.getInt("ID");
						String hname = ((ResultSet) rs).getNString("h_Name");
						String color = ((ResultSet) rs).getNString("h_Color");
						String breed = ((ResultSet) rs).getNString("h_Breed");
						String gender = ((ResultSet) rs).getNString("h_Gender");
						String result = "\n&EID#: &a" + ID + "&e Name: &f" + hname + " &8is a " + color.toLowerCase() + " " + breed.toLowerCase() + " " + gender.toLowerCase();
						Common.tell(sender, pre + result);
					} while (rs.next());
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException npe) {
			}
	}

	@SuppressWarnings ("deprication")
	public void copyHorse (CommandSender sender, String user, String horse) {
		MySQL.reconnect();
		OfflinePlayer offp = Bukkit.getOfflinePlayer(user);
		playerId = offp.getUniqueId().toString();
////            Common.tell(sender, pre + "&FUUID for " + user + " is " + uuid);
		try {
			dbSet('a');
			MySQL.reconnect();
			ResultSet rs = MySQL.query("SELECT * FROM " + tableFrom + " WHERE o_UUID='" + playerId + "' AND h_name='" + horse + "' ORDER BY h_name;");


			if (rs.first()) {
				rs.first();
				String hname = rs.getNString("h_Name");
				String color = rs.getNString("h_Color");
				String breed = rs.getNString("h_Breed");
				String gender = rs.getNString("h_Gender");
				String result = "\n&fName: " + hname + " is a " + color + " " + breed + " " + gender;
				Common.tell(sender, pre + result, pre + "&aCopying from &F" + tableFrom + "&a to " + tableTo);
				// INSERT INTO horse_mover_b.horse_new SELECT * from horse_mover_a.horse_old WHERE h_Name='Pokey';
				MySQL.update("INSERT INTO " + tableTo + " SELECT * from " + tableFrom + " WHERE o_UUID='" + playerId + "' AND h_Name='" + horse + "';");
			} else Common.tell(sender, pre + "&eNo horse named &f" + horse + "&e found for owner &f" + user);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException npe) {
		}
	}


/*
    public void saveHorse(String name, String color, String breed, String gender) {
        dbSet('b');
        MySQL.reconnect();
        try {
            MySQL.update("INSERT ");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/



/*
    public void saveHorse(String name, String color, String breed, String gender) {
        dbSet('b');
        MySQL.reconnect();
        try {
            MySQL.update("INSERT ");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/


//
//
///*
//        public void showPos (CommandSender sender, String user){
//            Player p = (Player) sender;
//            String pid = p.getUniqueId().toString();
//            String dname = p.getDisplayName();
////        setLoc(p.getLocation());
//            setX(p.getLocation().getBlockX());
//            setY(p.getLocation().getBlockY());
//            setZ(p.getLocation().getBlockZ());
//            Common.tell(sender, pre + "&E" + user + "s &7location is " + x + " " + y + " " + z);
//        }
//*/
//
///*
//        public void savePos (CommandSender sender, String user){
//            Player p = (Player) sender;
//            playerId = p.getUniqueId().toString();
//            ign = p.getName();
////        setLoc(p.getLocation());
//            x = p.getLocation().getBlockX();
//            y = p.getLocation().getBlockY();
//            z = p.getLocation().getBlockZ();
//            if (HM.exists("IGN", user, tableFrom)) {
//                HM.set("Player_UUID", playerId, "IGN", "=", user, tableFrom);
//                HM.set("x", x, "IGN", "=", user, tableFrom);
//                HM.set("y", y, "IGN", "=", user, tableFrom);
//                HM.set("z", z, "IGN", "=", user, tableFrom);
//
//            } else {
//
//                HM.insertData("Player_UUID, IGN, x, y, z", "'" + playerId + "'" + ", '" + user + "', '" + x + "', '" + y + "', '" + z + "'", tableFrom);
//            }
//
//
//        }
//*/
//
///*
//        public void purgePlayer (CommandSender sender, String user){
//            ign = user;
//            if (HM.exists("IGN", user, tableFrom)) {
//                HM.deleteData("IGN", "=", ign, tableFrom);
//            */
///*
//                public static void deleteData(String column, String logic_gate, String data, String table) {
//        if (data != null) {
//            data = "'" + data + "'";
//        }
//
//        MagSQL.update("DELETE FROM " + table + " WHERE " + column + logic_gate + data + ";");
//    }
//
//             *//*
//
//            }
//
//        }
//*/
//    }
// --Commented out by Inspection STOP (12/13/2018 1:04 AM)

	public static String getdBa () {
		return dBa;
	}

	public static String getdBb () {
		return dBb;
	}

	public static String getTableFrom () {
		return tableFrom;
	}

	public static String getTableTo () {
		return tableTo;
	}
}