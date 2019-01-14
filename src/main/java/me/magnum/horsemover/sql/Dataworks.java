package me.magnum.horsemover.sql;

import me.magnum.horsemover.HorseMover;
import me.magnum.lib.Common;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.HashMap;

public class Dataworks {

	HorseMover plugin;
	String Host;
	String Port;
	String Database;
	String Username;
	String Password;

//	v2Util v2Utils = new v2Util();


	public MySQ mySQ;

	public Dataworks (HorseMover plugin, String host, String port, String dataA, String username, String password) {
		plugin = plugin;
		Host = host;
		Port = port;
		Database = dataA;
		Username = username;
		Password = password;
	}

	public Connection connection = null;

	public void testConnection () {
		try {
			mySQ = new MySQ(plugin, Host, Port, Database, Username, Password);
			connection = mySQ.openConnection();

			DatabaseMetaData dbMeta = connection.getMetaData();
			ResultSet tables = dbMeta.getTables(null, null, "players", null);
			if (tables.next()) {
				Common.log("Database is online");
			} else {
				String playertable = "CREATE TABLE `players` ("
						+ "`ID` INT NOT NULL AUTO_INCREMENT COMMENT '',"
						+ "`o_UUID` VARCHAR(45) NOT NULL COMMENT '',"
						+ "`h_Name` VARCHAR(255) NOT NULL COMMENT '',"
						+ "`Rented` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '',"
						+ "`h_Color` VARCHAR(45) NULL COMMENT '',"
						+ "`h_Variant` VARCHAR(45) NULL COMMENT '',"
						+ "`h_Style` VARCHAR(45) NULL DEFAULT NULL COMMENT '',"
						+ "`h_Gender` VARCHAR(45) NULL COMMENT '',"
						+ "`h_Breed` VARCHAR(45) NULL COMMENT '',"
						+ "`h_Armor` VARCHAR(45) NULL COMMENT '',"
						+ "`h_Saddle` TINYINT(1) NULL DEFAULT 0 COMMENT '',"
						+ "`h_LastBreed` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '',"
						+ "`travel_dist` DOUBLE NOT NULL DEFAULT 0 COMMENT '',"
						+ "`s_Agility` DOUBLE NOT NULL DEFAULT 0 COMMENT '',"
						+ "`sw_Lvl`  TINYINT(1) NULL DEFAULT 0 COMMENT '',"
						+ "`ag_Lvl`  TINYINT(1) NULL DEFAULT 0 COMMENT '',"
						+ "`s_Energy` TINYINT(3) NOT NULL DEFAULT 100 COMMENT '',"
						+ "`s_Swiftness` DOUBLE NOT NULL DEFAULT 0 COMMENT '',"
						+ "PRIMARY KEY (`ID`)  COMMENT '',"
						+ "UNIQUE INDEX `ID_UNIQUE` (`ID` ASC)  COMMENT '');";
				try {
					Statement statement = connection.createStatement();
					statement.executeQuery(playertable);
					Common.log("Player table created");
				} catch (SQLException e) {
					Common.log("*****************************************");
					Common.log("SQL Create Table Error!!!");
					Common.log("*****************************************");
					e.printStackTrace();
				}
			}
			connection.close();
		} catch (ClassNotFoundException e) {
			Common.log("*****************************************");
			Common.log("MySQL Connection Error!!!");
			Common.log("*****************************************");
			e.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(plugin);
			Bukkit.getLogger().info("HorseRPGv2 Disabled");
			Bukkit.getLogger().info("---Team TMU---");
		} catch (SQLException e) {
			Common.log("*****************************************");
			Common.log("SQL Connection Error!!!");
			Common.log("*****************************************");
			e.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(plugin);
			Bukkit.getLogger().info("HorseRPGv2 Disabled");
			Bukkit.getLogger().info("---Team TMU---");
		}

	}

	public HashMap<String, HashMap<String, Object>> getInfo (String player) {
		HashMap<String, HashMap<String, Object>> horses = new HashMap<String, HashMap<String, Object>>();
		try {
			mySQ = new MySQ(plugin, Host, Port, Database, Username, Password);
			connection = mySQ.openConnection();

			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM horse_new WHERE o_UUID = '" + player + "';");

			while (resultSet.next()) {
				HashMap<String, Object> horse = new HashMap<String, Object>();
				String hName = resultSet.getString("h_Name");
				horse.put("id", resultSet.getInt("ID"));
				horse.put("name", hName);
				horse.put("color", resultSet.getString("h_Color"));
				horse.put("variant", resultSet.getString("h_Variant"));
				horse.put("style", resultSet.getString("h_Style"));
				horse.put("gender", resultSet.getString("h_Gender"));
				horse.put("breed", resultSet.getString("h_Breed"));
				horse.put("armor", resultSet.getString("h_Armor"));
				horse.put("saddle", resultSet.getInt("h_Saddle"));
				horse.put("tDistance", (resultSet.getDouble("travel_dist")));
				horse.put("agility", resultSet.getDouble("s_Agility"));
				horse.put("switfness", resultSet.getDouble("s_Swiftness"));
				horse.put("agLvl", resultSet.getInt("ag_Lvl"));
				horse.put("swLvl", resultSet.getInt("sw_Lvl"));
				horse.put("energy", resultSet.getInt("s_Energy"));
				horses.put(hName, horse);

			}
			resultSet.close();
			connection.close();
			return horses;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	public boolean killHorse (int ID) {
		try {
			connection = mySQ.openConnection();
			Statement statement = connection.createStatement();
			statement.executeUpdate("DELETE from players WHERE ID = '" + ID + "';");
			connection.close();
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public HashMap<String, Object> getHorseFromID (int ID) {
		HashMap<String, Object> horse = new HashMap<String, Object>();
		try {
			connection = mySQ.openConnection();
			Statement stmnt = connection.createStatement();
			ResultSet rset = stmnt.executeQuery("SELECT * FROM players WHERE ID = '" + ID + "';");

			while (rset.next()) {
				String hName = rset.getString("h_Name");
				horse.put("owneruuid", rset.getString("o_UUID"));
				horse.put("id", Integer.valueOf(rset.getInt("ID")));
				horse.put("name", hName);
				horse.put("rental", rset.getBoolean("Rented"));
				horse.put("color", rset.getString("h_Color"));
				horse.put("variant", rset.getString("h_Variant"));
				horse.put("style", rset.getString("h_Style"));
				horse.put("gender", rset.getString("h_Gender"));
				horse.put("breed", rset.getString("h_Breed"));
				horse.put("armor", rset.getString("h_Armor"));
				horse.put("saddle", rset.getInt("h_Saddle"));
				horse.put("lBreed", Long.valueOf(rset.getLong("h_LastBreed")));
				horse.put("tDistance", Double.valueOf(rset.getDouble("travel_dist")));
				horse.put("agility", Double.valueOf(rset.getDouble("s_Agility")));
				horse.put("switfness", Double.valueOf(rset.getDouble("s_Swiftness")));
				horse.put("agLvl", rset.getInt("ag_Lvl"));
				horse.put("swLvl", rset.getInt("sw_Lvl"));
				horse.put("energy", Integer.valueOf(rset.getInt("s_Energy")));
				if (rset.getBoolean("Rented")) {
					horse.put("r_Time", getTime(Integer.valueOf(rset.getInt("ID"))));
				}
				break;
			}
			rset.close();
			connection.close();
			return horse;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public HashMap<String, HashMap<String, Object>> getAllHorses (String player) {
		HashMap<String, HashMap<String, Object>> horses = getInfo(player);
//		HashMap<Integer, String> rental = getRentals(player);
//		if (!rental.isEmpty()) {
//			HashMap<String, HashMap<String, Object>> rentals = getRentalInfo(rental);
//			if (rentals != null) {
//				for (Map.Entry<String, HashMap<String, Object>> entry : rentals.entrySet()) {
//					horses.put(entry.getKey(), entry.getValue());
//				}
//			}
//		}
		if (horses != null) {
			return horses;
		}
		return null;
	}

	public long getTime (int horseID) {
		try {
			long timeleft = -1;
			connection = mySQ.openConnection();
			Statement stmnt = connection.createStatement();
			ResultSet rset = stmnt.executeQuery("SELECT r_Time FROM rental WHERE h_ID = '" + horseID + "';");

			while (rset.next()) {
				timeleft = rset.getLong("r_Time");
			}
			rset.close();
			connection.close();
			return timeleft;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
}
