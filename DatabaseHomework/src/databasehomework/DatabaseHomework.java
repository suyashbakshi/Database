/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasehomework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

/**
 *
 * @author suyas
 */
public class DatabaseHomework {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        Class.forName("com.mysql.jdbc.Driver");

        Connection m_connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "suyash");
        String schemaFile = "S:\\University\\Database systems\\test_schema.txt";
        BufferedReader reader = new BufferedReader(new FileReader(schemaFile));

        String schema;

        while ((schema = reader.readLine()) != null) {

            //Loop for each given table schema in the input file.
            TableSchema mTable = new TableSchema();
            System.out.println("\nInput Schema : " + schema);

            //Parse each line to retrieve tablename and columns
            int idx = schema.indexOf('(');
            String tablename = schema.substring(0, idx);
            System.out.println("Table Name : " + tablename);
            mTable.setTableName(tablename);

            String sub = schema.substring(++idx, schema.length() - 1);
            String[] columns = sub.split(",");
            System.out.print("Columns : ");
            for (String s : columns) {
                System.out.print(s + " ");
                mTable.addColumn(s);
            }

            System.out.println();
            if (Verification.verify1NF(mTable, m_connection)) {
                System.out.println("1 NF is Verified for table " + tablename);
            } else {
                System.out.println("1 NF failed for table " + tablename);
            }
        }

        reader.close();
    }

}

class TableSchema {

    String tablename;
    ArrayList<String> columns = new ArrayList();

    public void setTableName(String name) {
        tablename = name;
    }

    public void addColumn(String columnName) {
        columns.add(columnName);
    }

    public String getTableName() {
        return this.tablename;
    }

    public ArrayList getColumns() {
        return this.columns;
    }
}
