package me.magnum.horsemover.sql;

import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQ extends Dbase{

	private final String user;
	private final String db;
	private final String pass;
	private final String port;
	private final String host;

	public MySQ (Plugin plugin,String host,String port,String database,String user,String pass){
		super(plugin);
		this.host=host;
		this.port=port;
		this.db=database;
		this.user=user;
		this.pass = pass;
	}

	@Override
	public Connection openConnection() throws SQLException, ClassNotFoundException
	{
		if (checkConnection())
		{
			return connection;
		}
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.db + "?useSSL=false" , this.user, this.pass);
		return connection;
	}


}
