package me.magnum.horsemover.sql;

import lombok.Data;
import me.magnum.lib.Common;
import me.vagdedes.mysql.basic.Config;
import me.vagdedes.mysql.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static me.magnum.horsemover.HorseMover.getPlugin;
import static me.magnum.horsemover.HorseMover.pre;
import static me.vagdedes.mysql.database.SQL.createTable;
import static me.vagdedes.mysql.database.SQL.tableExists;

public @Data
class Database {

    public static String dBa;
    public static String dBb;
    public static String tableFrom;
    public static String tableTo;

    String playerId;
    String ign;
    String horsename;

    PreparedStatement update;
    PreparedStatement get;
    PreparedStatement insert;
    PreparedStatement delete;

    private void dbSet(char database) {
//        String noSSL = "?useSSL=false&characterEncoding=UTF-8";
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

    @SuppressWarnings("deprication")
    public void getHorse(CommandSender sender, String user, String horse) {
        //
        BukkitRunnable get = new BukkitRunnable() {
            @Override
            public void run() {
                dbSet('a');
                MySQL.reconnect();
                OfflinePlayer offp = Bukkit.getOfflinePlayer(user);
                playerId = offp.getUniqueId().toString();

                if (horse.equalsIgnoreCase("all")) {
                    try {
                        ResultSet rs = MySQL.query("SELECT * FROM " + tableFrom + " WHERE o_UUID='" + playerId + "' ORDER BY h_name;");

                        if (rs.first()) {
                            rs.first();
                            Common.tell(sender, pre + "&aHorses belonging to&e " + user);
                            do {
                                String hname = rs.getString("h_Name");
                                String color = rs.getString("h_Color");
                                String breed = rs.getString("h_Breed");
                                String gender = rs.getString("h_Gender");
                                String result = "\n&fName: " + hname + " is a " + color + " " + breed + " " + gender;
                                Common.tell(sender, result);
                            } while (rs.next());
                        } else Common.tell(sender, pre + "&eNo horses found for owner &f" + user);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (NullPointerException npe) {
                    }
                } else
                    try {
                        ResultSet rs = MySQL.query("SELECT * FROM " + tableFrom + " WHERE o_UUID='" + playerId + "' AND h_name='" + horse + "' ORDER BY h_name;");


                        if (rs.first()) {
                            rs.first();
                            do {
                                String hname = rs.getString("h_Name");
                                String color = rs.getString("h_Color");
                                String breed = rs.getString("h_Breed");
                                String gender =  rs.getString("h_Gender");
                                String result = "\n&fName: " + hname + " is a " + color + " " + breed + " " + gender;
                                Common.tell(sender, pre + result);
                            } while (rs.next());
                        } else
                            Common.tell(sender, pre + "&eNo horse named &f" + horse + "&e found for owner &f" + user);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (NullPointerException npe) {
                    }
            }
        };

        //
        get.runTaskAsynchronously(getPlugin());
    }

    @SuppressWarnings("deprication")
    public void copyHorse(CommandSender sender, String user, String horse) {
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
                String hname = rs.getString("h_Name");
                String color = rs.getString("h_Color");
                String breed = rs.getString("h_Breed");
                String gender = rs.getString("h_Gender");
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

    @SuppressWarnings("deprication")
    public void removeHorse(CommandSender sender, String user, String horse) {
        MySQL.reconnect();
        OfflinePlayer offp = Bukkit.getOfflinePlayer(user);
        playerId = offp.getUniqueId().toString();
        try {
            dbSet('a');
            MySQL.reconnect();
            ResultSet rs = MySQL.query("SELECT * FROM " + tableTo + " WHERE o_UUID='" + playerId + "' AND h_name='" + horse + "' ORDER BY h_name;");

            if (rs.first()) {
                rs.first();
                String hname = rs.getString("h_Name");
                String color = rs.getString("h_Color");
                String breed = rs.getString("h_Breed");
                String gender = rs.getString("h_Gender");
                String result = "\n&fName: " + hname + " is a " + color + " " + breed + " " + gender;
                Common.tell(sender,// pre + result,
                        pre + "&cRemoving &e" + horse + " &cfrom" + tableTo);
                // DELETE FROM table_name WHERE condition;
                MySQL.update("DELETE FROM " + tableTo + " WHERE o_UUID='" + playerId + "' AND h_Name='" + horse + "';");
//                MySQL.update("INSERT INTO " + tableTo + " SELECT * from " + tableFrom + " WHERE o_UUID='" + playerId + "' AND h_Name='" + horse + "';");
            } else Common.tell(sender, pre + "&eNo horse named &f" + horse + "&e found for owner &f" + user);

        } catch (SQLException e) {
            e.printStackTrace();
        }// catch (NullPointerException npe) {}

    }


    public void showStatus(CommandSender sender) {
        if (MySQL.isConnected()) {
            Common.tell(sender,
                    pre + "Status: &aConnected to " + Config.getDatabase());
        } else {
            Common.tell(sender, pre + "Status: &cDatabase not connected.");
        }
    }

    // --Commented out by Inspection START (12/13/2018 1:04 AM):

    public void Setup() {
        dbSet('a');
        if (!MySQL.isConnected()) {
            MySQL.connect();
        } else MySQL.reconnect();

//        dbSet('a');
        if (!(tableExists(tableFrom))) {
            createTable(tableFrom, "`ID` INT NOT NULL AUTO_INCREMENT COMMENT '',`o_UUID` VARCHAR(36) NOT NULL COMMENT '',`h_Name` VARCHAR(255) NOT NULL COMMENT '',`Rented` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '',`h_Color` VARCHAR(45) NULL COMMENT '',`h_Variant` VARCHAR(45) NULL COMMENT '',`h_Style` VARCHAR(45) NULL DEFAULT NULL COMMENT '',`h_Gender` VARCHAR(45) NULL COMMENT '',`h_Breed` VARCHAR(45) NULL COMMENT '',`h_Armor` VARCHAR(45) NULL COMMENT '',`h_Saddle` TINYINT(1) NULL DEFAULT 0 COMMENT '',`h_LastBreed` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '',`travel_dist` DOUBLE NOT NULL DEFAULT 0 COMMENT '',`s_Agility` DOUBLE NOT NULL DEFAULT 0 COMMENT '',`sw_Lvl`  TINYINT(1) NULL DEFAULT 0 COMMENT '',`ag_Lvl`  TINYINT(1) NULL DEFAULT 0 COMMENT '',`s_Energy` TINYINT(3) NOT NULL DEFAULT 100 COMMENT '',`s_Swiftness` DOUBLE NOT NULL DEFAULT 0 COMMENT '',PRIMARY KEY (`ID`)  COMMENT '',UNIQUE INDEX `ID_UNIQUE` (`ID` ASC)  COMMENT ''");
        }
//        dbSet('b');
//        MySQL.reconnect();
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

    public static String getdBa() {
        return dBa;
    }

    public static String getdBb() {
        return dBb;
    }

    public static String getTableFrom() {
        return tableFrom;
    }

    public static String getTableTo() {
        return tableTo;
    }
}