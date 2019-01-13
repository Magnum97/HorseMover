package me.magnum.horsemover.sql;

import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Dbase {

	protected Connection connection;

	protected Plugin plugin;

	protected Dbase (Plugin plugin) {
		this.plugin = plugin;
		this.connection = null;
	}

	public abstract Connection openConnection () throws SQLException, ClassNotFoundException;

	public boolean checkConnection () throws SQLException {
		return connection != null && !connection.isClosed();
	}

	public boolean closeConnection() throws SQLException	{
		if (connection == null)
			return false;
		connection.close();
		return true;
	}
	public ResultSet querySQL(String query) throws SQLException,ClassNotFoundException{
		if (!checkConnection())
			openConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		return resultSet;
	}


	public int updateSQL(String query) throws SQLException, ClassNotFoundException {
		if (!checkConnection())
			openConnection();
		Statement statement = connection.createStatement();
		int result = statement.executeUpdate(query);
		return result;
	}
}

