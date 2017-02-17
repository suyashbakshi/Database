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
        if (checkUnique(mConnection, tablename, columns) && checkNull(mConnection, tablename, columns)) {
            System.out.println("Table " + tablename + " verifies 1 NF.");
        } else {
            return false;
        }

        return true;
    }

    public static boolean verify2NF(TableSchema schema, Connection m_connection) {

        System.out.println("\nChecking 2 NF for table :" + schema.getTableName());
        ArrayList<Dependency> fdList = Utils.generateDeps(schema, m_connection);
        ArrayList<TableSchema> decomp = null;

        if (fdList.isEmpty()) {
            return true;
        } else {
            for (int i = 0; i < fdList.size(); i++) {

                System.out.println("VIOLATING FD : " + fdList.get(i).showDep());
                decomp = Utils.generateDecomp(schema, fdList);
                
            }
            for (int j = 0; j < decomp.size(); j++) {
                    decomp.get(j).showTable();
                }
            return false;
        }
    }

    private static boolean checkUnique(Connection connection, String tablename, ArrayList<String> columns) {

//      call Utils.executeQuery from here for given column. Query is the one that checks duplicates.
//      if query returns 1, return false since duplicates are present.
        String mColumn = columns.get(0).replace("(k)", "");
        StringBuilder joinColumn = new StringBuilder();

        //looping to generate the comma separated string of key column names as : k1,k2,k3
        for (int i = 0; i < columns.size(); i++) {
            String mCol = columns.get(i);

            if (i > 0 && mCol.contains("(k)")) {
                joinColumn.append(",");
            }
            if (mCol.contains("(k)")) {
                joinColumn.append(mCol.replace("(k)", ""));
            }
        }
        System.out.println("JOIN : " + joinColumn);

        String query = "select " + joinColumn + " from " + tablename + " group by " + joinColumn + " having count(*)>1 ;";
        ResultSet result = Utils.executeQuery(connection, query);
        try {
            if (result.next()) {
                System.out.println("COLUMN COMBINATION " + joinColumn + " HAS DUPLICATES.");
                result.close();
                return false;
            }
            result.close();

        } catch (SQLException ex) {
            Logger.getLogger(Verification.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private static boolean checkNull(Connection connection, String tablename, ArrayList<String> column) {

        for (int i = 0; i < column.size(); i++) {

            String mColumn = column.get(i);

            if (mColumn.contains("(k)")) {
                String query = "select count(*) from " + tablename + " where " + mColumn.replace("(k)", "") + " is NULL;";
                ResultSet result = Utils.executeQuery(connection, query);

                try {
                    while (result.next()) {
                        String r = result.getString("count(*)");
                        if (Integer.parseInt(r) > 0) {
                            System.out.println("COLUMN " + mColumn + " HAS NULLS.");
                            return false;

                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Verification.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }

}
