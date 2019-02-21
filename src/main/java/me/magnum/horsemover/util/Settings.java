package me.magnum.horsemover.util;

import me.magnum.horsemover.HorseMover;
import me.magnum.horsemover.sql.Database;
import me.vagdedes.mysql.basic.Config;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;


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

	// Added upon request of Ch.S. - parses a String list into a Material list
	private List<Material> getMaterialList (String path) {
		final List<Material> list = new ArrayList<>();

		for (final String raw : getStringList(path)) {
			// Material class is case sensitive, so make sure the name is in caps and has underscore instead of spaces
			final String correctedName = raw.toUpperCase().replace(" ", "_");

			try {
				final Material mat = Material.valueOf(correctedName);

				if (mat != null)
					list.add(mat);

			} catch (final IllegalArgumentException ex) {
				throw new RuntimeException("An item " + raw + " (" + correctedName + ") in " + path + " is not a valid Material!");
			}
		}

		return list;
	}
	// End of added

	public static void init () {
		new Settings("settings.yml").onLoad();
	}
}
