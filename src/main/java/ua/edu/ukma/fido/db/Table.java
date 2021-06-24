package ua.edu.ukma.fido.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Table {
    public static void create() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " +
                Main.tableName +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE, price NUMBER, amount NUMBER)";

        try (Statement statement = DB.connection.createStatement()) {
            statement.execute(sqlQuery);

            System.out.println("Table \"" + Main.tableName + "\" was created!\n");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static Integer insert(String name,double price, int amount) {
        String sqlQuery = "INSERT INTO " + Main.tableName + " (name, price,amount) VALUES (?,?,?)";

        try (PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, price);
            preparedStatement.setInt(3, amount);

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                Integer id = resultSet.getInt(1);

                System.out.println("Inserted: id(" + id + ")  name: " + name+ "  price: " + price + "  amount: " + amount+"\n");

                return id;
            } else {
                System.err.println("Can't insert :(");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    public static void insert(int id, String name, double price, int amount) {
        String sqlQuery = "INSERT INTO " + Main.tableName + " (id, name, price,amount) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setDouble(3, price);
            preparedStatement.setDouble(4, amount);

            preparedStatement.executeUpdate();

            System.out.println("Inserted: id(" + id + ")  name: " + name+ "  price: " + price + "  amount: " + amount+"\n");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void update_name(int id, String name) {
        String sqlQuery = "UPDATE " + Main.tableName + " SET name = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();

            System.out.println("Updated: id(" + id + ")  (new)name: " + name+"\n");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void update_price(int id, double price) {
        String sqlQuery = "UPDATE " + Main.tableName + " SET price = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery)) {
            preparedStatement.setDouble(1, price);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();

            System.out.println("Updated: id(" + id + ")  (new)price: " + price+"\n");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void update_amount(String productName, int amount) {
        String sqlQuery = "UPDATE " + Main.tableName + " SET amount = ? WHERE name = ?";

        try (PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, amount);
            preparedStatement.setString(2, productName);
            preparedStatement.executeUpdate();
            System.out.println("Updated: product(" + productName + ")  (new)amount: " + amount+"\n");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static Product selectByName(String name) {
        Product product = null;
        String sqlQuery = "SELECT * FROM " + Main.tableName + " WHERE name = ?";

        try (PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet != null && resultSet.next()) {
                product = new Product(
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("amount")
                );
                resultSet.close();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return product;
    }

    public static ResultSet selectWhereAmountBiggerThan(int amount) {
        String sqlQuery = "SELECT * FROM " + Main.tableName + " WHERE amount > ?";

        try (PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, amount);
            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public static ResultSet selectWhereAmountSmallerThan(int amount) {
        String sqlQuery = "SELECT * FROM " + Main.tableName + " WHERE amount < ?";

        try (PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, amount);
            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public static ResultSet selectWherePriceBiggerThan(double price) {
        String sqlQuery = "SELECT * FROM " + Main.tableName + " WHERE price > ?";

        try (PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery)) {
            preparedStatement.setDouble(1, price);
            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    public static ResultSet selectWherePriceSmallerThan(double price) {
        String sqlQuery = "SELECT * FROM " + Main.tableName + " WHERE price < ?";
        try (PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery)) {
            preparedStatement.setDouble(1, price);
            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    public static ResultSet selectOneLimitOffset(int limit, int offset) {
        String sqlQuery = "SELECT * FROM " + Main.tableName + " LIMIT ?, ?";
        try (PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, limit);
            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    public static ResultSet selectAll() {
        String sqlQuery = "SELECT * FROM " + Main.tableName;
        try (Statement statement = DB.connection.createStatement();) {
            return statement.executeQuery(sqlQuery);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    public static void cleanDatabase() {
        String sqlQuery = "DELETE FROM " + Main.tableName;
        try (PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteByName(String name) {
        String sqlQuery = "DELETE FROM " + Main.tableName + " WHERE name = ?";

        try (PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);) {

            preparedStatement.setString(1, name);

            preparedStatement.executeUpdate();

            System.out.println("Deleted " + name);
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void delete(int id) {
        String sqlQuery = "DELETE FROM " + Main.tableName + " WHERE id = ?";

        try (PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);) {
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

            System.out.println("Deleted " + id);
            System.out.println();
            preparedStatement.closeOnCompletion();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void dropTable() {
        String sqlQuery = "DROP TABLE " + Main.tableName;

        try (Statement statement = DB.connection.createStatement();) {
            statement.execute(sqlQuery);
            System.out.println("Table \"" + Main.tableName + "\" was truncated");
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
