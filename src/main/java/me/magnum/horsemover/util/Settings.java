package me.magnum.horsemover.util;

import me.magnum.horsemover.HorseMover;
import me.magnum.horsemover.sql.Database;
import me.vagdedes.mysql.basic.Config;


public class Settings extends SimpleConfig {


	private Settings (String fileName) {
		super(fileName);

		setHeader(new String[]{
				"--------------------------------------------------------",
				" Your configuration file got updated automatically!",
				" ",
				" Unfortunately, due to how Bukkit saves .yml files,",
				" all comments in your file were lost. Please open",
				" " + fileName + " from jar to browse the default values.",
				"--------------------------------------------------------"
		});
	}


	private void onLoad () {

		String host = (getString("sql.host"));
		String port = (getString("sql.port"));
		String username = (getString("sql.user"));
		String password = (getString("sql.password"));
		String database = (getString("sql.dba"));
		Config.setHost(host);
		Config.setPassword(port);
		Config.setDatabase(database);
		Config.setUser(username);
		Config.setPassword(password);
		Database.dba = getString("sql.dba");
		Database.dbb = getString("sql.dbb");
		Database.tableFrom = getString("sql.fromtable");
		Database.tableTo = getString("sql.totable");
		HorseMover.setPre(getString("plugin.prefix"));
		new Database().setup();
	}

	public static void init () {
		new Settings("settings.yml").onLoad();
	}
}
