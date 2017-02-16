/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasehomework;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author suyas
 */
public class Utils {
    
    public static ResultSet executeQuery(Connection connection, String query) {
        
        try {
            System.out.println("QUERY FIRED : " + query);
            PreparedStatement ps = connection.prepareStatement(query);
            return ps.executeQuery();
            
        } catch (SQLException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static boolean execute(Connection connection, String query) {
        
        try {
            System.out.println("QUERY FIRED : " + query);
            PreparedStatement ps = connection.prepareStatement(query);
            return ps.execute();
            
        } catch (SQLException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public static ArrayList<Dependency> generateDeps(TableSchema schema, Connection connection) {
        
        ArrayList<String> columns = schema.getColumns();
        String tablename = schema.getTableName();
        StringBuffer primes = new StringBuffer();
        ArrayList<String> nonPrime = new ArrayList();
        ArrayList<Dependency> deps = new ArrayList();
        String s = null;

//        loop to separate prime and non-prime attributes
        for (int i = 0; i < columns.size(); i++) {
            s = columns.get(i);
            if (s.contains("(k)")) {
                primes.append(s.replace("(k)", "") + ",");
            } else {
                nonPrime.add(s);
            }
            
        }
        primes.deleteCharAt(primes.lastIndexOf(","));
        System.out.println("PRIMES IS " + primes);

        //get prime attributes combination as: k1 k2 k3 k1,k2 k1,k2,k3
        ArrayList<String> primeCombinations = getKeyCombinations(primes);
        
        String mPrime = null;
        String mNprime = null;
        
        for (int i = 0; i < primeCombinations.size(); i++) {
            
            mPrime = primeCombinations.get(i);
            for (int j = 0; j < nonPrime.size(); j++) {
                
                mNprime = nonPrime.get(j);
                String viewOneQuery = "create view one as select " + mPrime + "," + mNprime + " from " + tablename + ";";
                String viewTwoQuery = "create view two as select " + mPrime + "," + mNprime + " from " + tablename + ";";
                Utils.execute(connection, viewOneQuery);
                Utils.execute(connection, viewTwoQuery);

                //get view combinations as
                //one.k1=two.k1 and one.k2=two.k2 and so on...
                String viewCombination = getViewCombination(mPrime);
                
                String query = "select count(*) from one,two where " + viewCombination + " one." + mNprime + "<>two." + mNprime + ";";
                
                ResultSet rs = Utils.executeQuery(connection, query);
                
                try {
                    rs.next();
                    if (Integer.parseInt(rs.getString("count(*)")) > 0) {
                        System.out.println("No FD between " + mPrime + " and " + mNprime);
                    } else {
                        deps.add(new Dependency(mPrime, mNprime));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
                }
                String dropView = "drop view one;";
                String dropViewTwo = "drop view two;";
                Utils.execute(connection, dropView);
                Utils.execute(connection, dropViewTwo);
            }
            
        }
        return deps;
    }
    
    private static ArrayList<String> getKeyCombinations(StringBuffer primes) {
        
        String split[] = primes.toString().split(",");
        ArrayList<String> combinations = new ArrayList();
        
        for (int i = 0; i < (1 << split.length); i++) {
            // Print current subset
            String s = "";
            for (int j = 0; j < split.length; j++) {
                if ((i & (1 << j)) > 0) {
                    //System.out.print(split[j] + "-"+j);
                    s += "," + split[j];
                }
            }
            if (!s.equals("")) {
                combinations.add(s.replaceFirst(",", ""));
                
            }
        }
        return combinations;
    }
    
    private static String getViewCombination(String prime) {
        
        String split[] = prime.split(",");
        String s = "";
        for (int i = 0; i < split.length; i++) {
            s += "one." + split[i] + "=two." + split[i] + " and ";
        }
        return s;
        
    }
    
}
