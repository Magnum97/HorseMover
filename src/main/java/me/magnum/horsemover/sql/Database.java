package me.magnum.horsemover.sql;

import lombok.Data;
import me.magnum.lib.Common;
import me.vagdedes.mysql.basic.Config;
import me.vagdedes.mysql.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static me.magnum.horsemover.HorseMover.pre;
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
    String ign;
    String horsename;

    PreparedStatement update;
    PreparedStatement get;
    PreparedStatement insert;
    PreparedStatement delete;

    private void dbSet(char database) {
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

    public void Setup() {
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

    public void showStatus(CommandSender sender) {
        if (MySQL.isConnected()) {
            Common.tell(sender,
                    "SQL Status: &aConnected to SQL " + Config.getDatabase());
        } else {
            Common.tell(sender, "SQL Status: &cDatabase not connected.");
        }
    }


// --Commented out by Inspection START (12/13/2018 1:04 AM):
    @SuppressWarnings("deprication")
    public void getHorse(CommandSender sender, String user, String horse) {
        MySQL.disconnect();
        dbSet('a');
        MySQL.connect();
        OfflinePlayer offp = Bukkit.getOfflinePlayer(user);
        playerId = offp.getUniqueId().toString();
////            Common.tell(sender, pre + "&FUUID for " + user + " is " + uuid);
        try {
            ResultSet rs = MySQL.query("SELECT * FROM " + tableFrom + " WHERE o_UUID='" + playerId + "' AND h_name='" + horse + "' ORDER BY h_name;");


            if (rs.first()) {
                rs.first();
            do {
                String hname = ((ResultSet) rs).getNString("h_Name");
                String color = ((ResultSet) rs).getNString("h_Color");
                String breed = ((ResultSet) rs).getNString("h_Breed");
                String gender = ((ResultSet) rs).getNString("h_Gender");
                String result = "\n&fName: " + hname + " is a " + color + " " + breed + " " + gender;
                Common.tell(sender, pre + result);
            } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException npe) {}
    }
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
//            if (SQL.exists("IGN", user, tableFrom)) {
//                SQL.set("Player_UUID", playerId, "IGN", "=", user, tableFrom);
//                SQL.set("x", x, "IGN", "=", user, tableFrom);
//                SQL.set("y", y, "IGN", "=", user, tableFrom);
//                SQL.set("z", z, "IGN", "=", user, tableFrom);
//
//            } else {
//
//                SQL.insertData("Player_UUID, IGN, x, y, z", "'" + playerId + "'" + ", '" + user + "', '" + x + "', '" + y + "', '" + z + "'", tableFrom);
//            }
//
//
//        }
//*/
//
///*
//        public void purgePlayer (CommandSender sender, String user){
//            ign = user;
//            if (SQL.exists("IGN", user, tableFrom)) {
//                SQL.deleteData("IGN", "=", ign, tableFrom);
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