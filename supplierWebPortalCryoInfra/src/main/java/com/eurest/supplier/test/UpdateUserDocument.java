package com.eurest.supplier.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class UpdateUserDocument {
	
	static String userMySQL = "smartechdbtst";
	static String passMySQL = "smart$dbT5t";
	static String hostMysQL = "jdbc:mysql://sm-db-test.crvhin3ktmx2.us-east-1.rds.amazonaws.com:3306/portalcryo";
	
	private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    
    static {
        config.setJdbcUrl( hostMysQL);
        config.setUsername(userMySQL);
        config.setPassword( passMySQL);
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource( config );
    }
    
    public static java.sql.Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
       

	public static void main(String[] args) throws SQLException, IOException {
		// TODO Auto-generated method stub
		
		String path = "C:\\Users\\SMDEVELOPER\\Desktop\\";
		Connection connection = UpdateUserDocument.getConnection();
		 java.sql.PreparedStatement statement = connection.prepareStatement("SELECT id,addressBook,content,name FROM portalcryo.userdocument where uuid <> ''");
	        ResultSet resultSet = statement.executeQuery();
	        while(resultSet.next()){
	        	File file = new File(path + resultSet.getString("name"));
	        	
	        	 final String empUpdateSql = "UPDATE portalcryo.userdocument set content = ? where id = ?";

	    		 java.sql.PreparedStatement pstmt = connection.prepareStatement(empUpdateSql);
	    		 byte[] fileContent = Files.readAllBytes(file.toPath());
	    		 pstmt.setBytes(1, fileContent);
	    		 pstmt.setString(2, resultSet.getString("id"));
	    		    		 
	    		 pstmt.addBatch();
	    		 pstmt.clearParameters();
	    		 pstmt.executeBatch();
	    		
	    		 System.out.println("FIN - " + resultSet.getString("name"));
	        	
	        }
	        connection.close();
	        System.out.println("FIN");

	}

}
