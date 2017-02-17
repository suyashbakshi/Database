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
import java.util.function.Predicate;
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
            if (!s.contains("(k)")) {
                nonPrime.add(s);
            }
        }
        primes = Utils.getCommaSeparatedPrimes(schema);

        System.out.println("PRIMES IS " + primes);

        //get prime attributes combination as: 
        //k1 
        //k2 
        //k3
        //k1,k2
        //k1,k3
        //k2,k3
        //k1,k2,k3
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
                //for k1        the statement as one.k1=two.k1
                //for k1,k2 get the statement as one.k1=two.k1 and one.k2=two.k2 and so on...
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
        return Utils.removeFullyFd(primes, deps);
    }

    public static ArrayList<String> getKeyCombinations(StringBuffer primes) {

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

    private static ArrayList<Dependency> removeFullyFd(StringBuffer primes, ArrayList<Dependency> deps) {

        //remove all the fd's that have the whole candidate key on the left. Since they do not violate 2 NF.
        //keep only those fd's that have partial candidate key on the left.
        for (int i = 0; i < deps.size(); i++) {

            if (deps.get(i).getLeft().equalsIgnoreCase(primes.toString())) {
                deps.remove(i);
                i--;
            }
        }
        return deps;
    }

    public static StringBuffer getCommaSeparatedPrimes(TableSchema schema) {

        ArrayList<String> columns = schema.getColumns();
        StringBuffer primes = new StringBuffer();
        String s;
        for (int i = 0; i < columns.size(); i++) {
            s = columns.get(i);
            if (s.contains("(k)")) {
                primes.append(s.replace("(k)", "")).append(",");
            }
        }
        primes.deleteCharAt(primes.lastIndexOf(","));
        return primes;
    }

    static ArrayList<TableSchema> generateDecomp(TableSchema schema, ArrayList<Dependency> fdList) {

        ArrayList<String> primeComb = Utils.getKeyCombinations(Utils.getCommaSeparatedPrimes(schema));
        ArrayList<TableSchema> decompositions = new ArrayList();
        
        //flags for creating decomposition for all nonPrimes that do not show up on right side of any FD
        //and hence are fully functional dependent on candidate key and should stay in original table
        ArrayList<String> nonPrimes = Utils.getNonPrimes(schema);
        boolean flags[] = new boolean[nonPrimes.size()];

        for (int i = 0; i < primeComb.size() - 1; i++) {

            String prime = primeComb.get(i);
            ArrayList<String> dCol = new ArrayList();
            dCol.add(prime);
//            System.out.println(prime);
            for (int j = 0; j < fdList.size(); j++) {
//                System.out.println(fdList.get(j).showDep());
                if (fdList.get(j).getLeft().equals(prime)) {
                    dCol.add(fdList.get(j).getRight());

                    //mark the flag for non Prime as false, which appears on right side in a partial dependency
                    //since it cannot be a part of main table
                    int idx = nonPrimes.indexOf(fdList.get(j).getRight());
                    flags[idx] = false;
                    
                }
            }
            if (!(dCol.size() == 1)) {
                decompositions.add(new TableSchema(schema.getTableName() + "" + ++i, dCol));
            }
        }
        TableSchema firstDec = new TableSchema();
        firstDec.setTableName(schema.getTableName());
        //add column that is a combination of complete candidate key
        firstDec.addColumn(primeComb.get(primeComb.size()-1));
        for (int i = 0; i < nonPrimes.size(); i++) {
            if(flags[i]){
                firstDec.addColumn(nonPrimes.get(i));
            }
        }
        decompositions.add(firstDec);
        

        return decompositions;
    }
    
    public static ArrayList<String> getNonPrimes(TableSchema schema){
        ArrayList<String> nonPrimes = new ArrayList();
        
        for (int i = 0; i < schema.getColumns().size(); i++) {
            if(!(schema.getColumns().get(i).toString().contains("(k)")))
                nonPrimes.add(schema.getColumns().get(i).toString());
        }
        return nonPrimes;
    }

}
