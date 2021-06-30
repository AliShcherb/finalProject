package ua.edu.ukma.fido.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public final static String dbName="database.db";
    public final static String tableName="MAGAZINCHIK";
    public final static String tableNameCat="Category";

    public static void main(String[] args) {
        DB.connect();
        Table.createCategory();
        Table.cleanDatabaseCat();
        Table.createProduct();
        Table.cleanDatabase();


        Table.insertCategory(11,"dairy","molochka");

        Table.insert(1, "MOLOKO",29.19,5, 11);
        Table.insert(2, "GRECHKA",40,100,70);//❤

     /*   Integer idThree = Table.insert("MORKVA",10,20);
        Integer idFour = Table.insert("KOVBASKA",150,1);
        Integer idFive = Table.insert("POMIDORKA",11,220);*/

        List<Product> all = Table.selectAll();
       // printResultSet("All products", all);
        System.out.println("All products: "+all);

        Product productByName = Table.selectByName("MOLOKO");
        System.out.println("find moloko"+productByName);

        ResultSet byAmount_more = Table.selectWhereAmountBiggerThan(15);
        printResultSet("Find where amount of products more than 15", byAmount_more);

        ResultSet byAmount_less = Table.selectWhereAmountSmallerThan(10);
        printResultSet("Find where amount of products less than 10", byAmount_less);

        ResultSet byPrice_more = Table.selectWherePriceBiggerThan(25);
        printResultSet("Find where price of product more than 25", byPrice_more);

        ResultSet byPrice_less = Table.selectWherePriceSmallerThan(100);
        printResultSet("Find where price of product less than 100", byPrice_less);

        ResultSet oneLimitOffset = Table.selectOneLimitOffset(1, 2);
        printResultSet("oneLimitOffset", oneLimitOffset);

        Table.delete(4);

        all = Table.selectAll();
        System.out.println("All products: "+all);
        //printResultSet("All products", all);

        Table.update_name(1, "NE_MOLOKO");
        Table.update_amount("NE_MOLOKO", 50);
        Table.update_price(3,13);

        all = Table.selectAll();
        System.out.println("All products: "+all);
      //  printResultSet("All products", all);

        Table.dropTable();

        DB.close();
    }/**/

    public static void printResultSet(String resultSetName, ResultSet resultSet) {
        System.out.println(resultSetName + ":");
        try {
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") +  "\t" + resultSet.getString("name"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        System.out.println();
    }
}

