package util;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DatabaseUtil {
	private static Properties properties = PropertiesUtil.getInstance().getDbProp();
	private static Properties progProp = PropertiesUtil.getInstance().getProgProp();
	private static Logger log = LogUtil.getLogger(DatabaseUtil.class.getName());
	
	private static Connection con(int env) {

		Connection con = null;
		try {
			if(env == 1 ){// VIT
				log.info("RUn as VIT");
				Class.forName(properties.getProperty("driver"));
				con = DriverManager.getConnection(properties.getProperty("url"),
				properties.getProperty("user"),
				properties.getProperty("password"));
			}
			else if (env==2 ) {// Staging OR selain 1 
				log.info("Run As Staging");
				Class.forName(properties.getProperty("driver"));
				con = DriverManager.getConnection(properties.getProperty("urlStaging"),
				properties.getProperty("user"),
				properties.getProperty("password"));
			}
			else{
				return null ;
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e);
		} 		
		return con;
	}
	
	public static ArrayList<String> getMainQuery(String query, String[] model, int env) {
		Connection con = con(env);
	    Statement stmt = null;
	    ArrayList<String> data = new ArrayList<String>();
	    try {
	    	stmt = con.createStatement();
			  ResultSet rset = stmt.executeQuery(query);
			  while (rset.next()) {		
				  String record = "";
				  for(int i = 0; i < model.length ; i++) {
					  String x  = rset.getString(model[i]) ;
					  if(i == model.length - 1) {
						  record = record+x;
					  }else {
						  record = record+x+ progProp.getProperty("delimiter");
					  }
				  }
				  data.add(record);
		      }
			  
		} catch (Exception e) {
			log.error("Error " + e.getMessage());
//			e.printStackTrace();
			// TODO: handle exception
		}finally{
	        try {
	            stmt.close();
	            con.close();
	        } catch (SQLException f) {
	            f.printStackTrace();
				log.error(f);
	        }
	    }
		return data;
	}

	public static int deletedQuery(String query,String table_name, int env) {
		Connection con = con(env);
	    Statement stmt = null;
		int rset=0;
	    log.info("Running Query: "+query);
	    try {
	    	stmt = con.createStatement();
			rset = stmt.executeUpdate(query);
			log.info("Berhasil menghapus Data dari table :"+table_name);

		} catch (Exception e) {
			log.error("Error " + e.getMessage());
//			e.printStackTrace();
			// TODO: handle exception
		}finally{
	        try {
	            stmt.close();
	            con.close();
	        } catch (SQLException f) {
	            f.printStackTrace();
				log.error(f);
	        }
	    }
		return rset;
	}
	
	


	
	public static String selectData(String query, String model, int env) {
		Connection con = con(env);
	    Statement stmt = null;
	    String data = "";
		log.info(query);
	    try {
	    	stmt = con.createStatement();
			  ResultSet rset = stmt.executeQuery(query);
			  while (rset.next()) {
				  data = rset.getString(model);
		      }			  
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
	        try {
	            stmt.close();
	            con.close();
	        } catch (SQLException f) {
	            log.error(f);
	        }
	    }
		return data;
	}
	
	public static int executeUpdate(String queryInsertUpdate, int env) throws SQLException {
		Connection con = con(env);
	    PreparedStatement stmt = null;
		log.info(queryInsertUpdate);
	    int id = 0;
//	    try {
			  stmt = con.prepareStatement(queryInsertUpdate);
			  stmt.executeUpdate();
			  id  = stmt.executeUpdate();
			  log.info("success Update");
//		} catch (Exception e) {
//			e.printStackTrace();
			// TODO: handle exception
//		}finally{
	        try {
	            stmt.close();
	            con.close();
	        } catch (SQLException f) {
	            log.error(f);
	        }
//	    }
		return id;
	}

	public static int Backup2000(String query, Integer env, String date){
		Connection con = con(env);
	    Statement stmt = null;
	    String data = "";
		log.info(query);
		String envName = env==1?"VIT":"Staging Dev";

	    try {
	    	stmt = con.createStatement();
			  ResultSet rset = stmt.executeQuery(query);
			  FileWriter fileWriter = new FileWriter("BackupSource2000/"+envName+"_Source2000_"+date+".sql");
			  Integer columnCount = rset.getMetaData().getColumnCount();

			  while (rset.next()) {
				StringBuilder insertQuery = new StringBuilder("INSERT INTO ");

                insertQuery.append("`source_data`").append(" (");

                // Append column names
                for (int i = 1; i <= columnCount; i++) {
                    insertQuery.append(rset.getMetaData().getColumnName(i));
                    if (i < columnCount) {
                        insertQuery.append(", ");
                    }
                }
                insertQuery.append(") VALUES (");

                // Append column values
                for (int i = 1; i <= columnCount; i++) {
                    String value = rset.getString(i);
                    if (value != null) {
                        value = value.replace("'", "''"); // Escape single quotes
                        insertQuery.append("'").append(value).append("'");
                    } else {
                        insertQuery.append("NULL");
                    }
                    if (i < columnCount) {
                        insertQuery.append(", ");
                    }
                }
                insertQuery.append(");\n");
				

                // Write the insert query to the file
                fileWriter.write(insertQuery.toString());
				fileWriter.flush();
				
		      }			  
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
	        try {
	            stmt.close();
	            con.close();
	        } catch (SQLException f) {
	            log.error(f);
	        }
	    }
		return 0;
	}
}



