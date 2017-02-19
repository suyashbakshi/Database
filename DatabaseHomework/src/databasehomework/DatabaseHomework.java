/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasehomework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.Properties;
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

        Class.forName("com.vertica.jdbc.Driver");

        Connection m_connection = DriverManager.getConnection("jdbc:vertica://129.7.243.243:5433/cosc6340s17", "team04", "XCQyntKe");
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
                System.out.println(tablename + " satisfies 1 Normal Form");

                //if 1 NF is satisfied, then check for 2 NF.
                if (Verification.verify2NF(mTable, m_connection)) {
                    //verify2NF() returns true if table satisfies 2NF.
                    System.out.println(tablename + " satisfies 2 Normal Form");
                    
                    if(Verification.verify3NF(mTable, m_connection)){
                        System.out.println(tablename + " satisfies 3 Normal Form");
                    }
                    else{
                        System.out.println("3 NF Failed for " + tablename);
                    }
                }
                else
                    System.out.println("2 NF Failed for " + tablename);

            } else {
                System.out.println("1 NF failed for table " + tablename);
            }
        }

        reader.close();
    }

}

class TableSchema {

    private String tablename;
    private ArrayList<String> columns = new ArrayList();
    
    public TableSchema(){
    }
    
    public TableSchema(String tablename,ArrayList<String> columns){
        this.tablename = tablename;
        this.columns = columns;
    }

    public void setTableName(String name) {
        this.tablename = name;
    }

    public void addColumn(String columnName) {
        this.columns.add(columnName);
    }

    public String getTableName() {
        return this.tablename;
    }

    public ArrayList getColumns() {
        return this.columns;
    }
    
    public void showTable(){
        System.out.print(this.tablename + "( " );
        for (int i = 0; i < this.columns.size(); i++) {
            System.out.print(this.columns.get(i) + " ");
        }
        System.out.print(")");
        System.out.println();
    }
}
