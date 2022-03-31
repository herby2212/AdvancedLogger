package de.Herbystar.AVL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.file.YamlConfiguration;

public class MySQL {
	
	private String host;
	private int port;
	private String user;
	private String password;
	private String database;
	
	private Connection con;
	
	private File f1 = new File("plugins/AdvancedLogger", "MySQL.yml");
	private YamlConfiguration config = YamlConfiguration.loadConfiguration(f1);
	
	public MySQL() throws Exception {
		this.host = config.getString("AVL.MySQL.host");
		this.port = config.getInt("AVL.MySQL.port");
		this.user = config.getString("AVL.MySQL.user");
		this.password = config.getString("AVL.MySQL.password");
		this.database = config.getString("AVL.MySQL.database");
		this.OpenConnection();
	}
	
	public Connection OpenConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
		this.con = con;
		return con;
	}
	
	public Connection getConnection() {
		return this.con;
	}
	
	public boolean hasConnection() {
		try {
			return this.con != null || this.con.isValid(1);
		} catch (SQLException ex) {
			return false;
		}
	}
	
	public void closeRessources(ResultSet rs, PreparedStatement st) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
			}
		}
		if(st != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
			}
		}
	}
	
	public void closeConnetion() {
		try {
			this.con.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		this.con = null;
	}
	
	public void queryUpdate(String query) {
		Connection con = this.con;
		PreparedStatement st = null;
		try {
			st = con.prepareStatement(query);
			st.executeUpdate();
		} catch (SQLException ex) {
			System.err.println("Failed to send update '" + query + "'!");
		}
	}

}
