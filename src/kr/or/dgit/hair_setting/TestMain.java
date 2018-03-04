package kr.or.dgit.hair_setting;

import java.sql.SQLException;
import java.util.Properties;

import erp_application.jdbc.DBCon;
import erp_application.jdbc.LoadProperties;

import java.util.Map.Entry;


public class TestMain {
	public static void main(String[] args) throws SQLException, InterruptedException {
		testDBConnection();
	}
	
	private static void testDBConnection() {
		DBCon dbCon = DBCon.getInstance();
		System.out.println(dbCon);

		LoadProperties lp = new LoadProperties();
		Properties pro = lp.getProperties();
		
		for(Entry<Object,Object> e: pro.entrySet()) {
			System.out.printf("%s : %s%n", e.getKey(), e.getValue());
		}
		
		
		String[] values = pro.get("tables").toString().split(",");
		for(String v : values) {
			System.out.println(v);
		}
	}
}
