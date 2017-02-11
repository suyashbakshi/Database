/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasehomework;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author suyas
 */
public class Verification {

    public static boolean verify1NF(TableSchema schema, Connection mConnection) {

        String tablename = schema.getTableName();
        ArrayList<String> columns = schema.getColumns();

//        System.out.println(columns.size());
        for (int i = 0; i < columns.size(); i++) {

            String mColumn = columns.get(i);
            if (mColumn.contains("(k)")) {
                System.out.println("\nChecking 1 NF for Key Column : " + mColumn.replace("(k)", ""));

                if (checkUnique(mConnection, tablename, mColumn) && checkNull(mConnection, tablename, mColumn)) {
                    System.out.println("Key Column " + mColumn + " verifies 1 NF.");
                } else {
                    return false;
                }

            }
        }
        return true;
    }

    private static boolean checkUnique(Connection connection, String tablename, String column) {

//      call Utils.executeQuery from here for given column. Query is the one that checks duplicates.
//      if query returns 1, return false since duplicates are present.
        String mColumn = column.replace("(k)", "");
        String query = "select " + mColumn + " from " + tablename + " group by " + mColumn + " having count(" + mColumn + ")>1 ;";
        System.out.println("USING QUERY : " + query);
        ResultSet result = Utils.executeQuery(connection, query);
        try {
            if (result.next()) {
                System.out.println("COLUMN " + mColumn + " HAS DUPLICATES.");
                result.close();
                return false;
            }
            result.close();
        } catch (SQLException ex) {
            Logger.getLogger(Verification.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private static boolean checkNull(Connection connection, String tablename, String column) {
        String mColumn = column.replace("(k)", "");
        String query = "select count(*) from " + tablename + " where " + mColumn + " is NULL;";
        System.out.println("USING QUERY : " + query);
        ResultSet result = Utils.executeQuery(connection, query);
        
        try {
            while(result.next()){
                String r = result.getString("count(*)");
                if(Integer.parseInt(r)>0){
                    System.out.println("COLUMN "+mColumn + " HAS NULLS.");
                    return false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Verification.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
