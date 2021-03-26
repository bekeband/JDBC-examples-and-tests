import java.sql.*;

public class CustomerRepository {

    private String DB_URL =
            "jdbc:mysql://localhost/jdbc_demo?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private String USER = "root";
    private String PASSWORD = "KHVvPuDVauw8qn";
    private String DB_NAME = "customer";

    public CustomerRepository(String DB_URL, String USER, String PASSWORD, String DB_NAME) {
        this.DB_URL = DB_URL;
        this.USER = USER;
        this.PASSWORD = PASSWORD;
        this.DB_NAME = DB_NAME;
    }

    public void createTable() {

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String createCustomerSQL = "CREATE TABLE IF NOT EXISTS " + DB_NAME +
                    "(ID INT(10) PRIMARY KEY, " +
                    "email VARCHAR(50) UNIQUE, " +
                    "name VARCHAR(30) )";

            Statement statement = connection.createStatement();
            statement.execute(createCustomerSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void createCustomer(int id, String email, String name) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String createCustomerSQL =
                    "INSERT INTO " + DB_NAME + " (ID, email, name) VALUES (" +
                    + id + ",'" + email + "','" + name + "')";

            Statement statement = connection.createStatement();
            statement.execute(createCustomerSQL);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void updateEmail(int id, String email) {

        /*
        UPDATE `jdbc_demo`.`customer` SET `email` = 'maril@mail.hu' WHERE (`ID` = '103');
         */

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String createCustomerSQL =
                    "UPDATE " + DB_NAME + " SET `email` = '" + email + "' WHERE ('ID' = '" + id + "');";

            Statement statement = connection.createStatement();
            statement.execute(createCustomerSQL);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public String printCustomer(int id) {
        String result = "";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String createCustomerSQL =
                    "SELECT id, name, email from " + DB_NAME + " WHERE (id = '" + id + "');";

            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(createCustomerSQL);

            while (results.next()) {
                String name = results.getString("name");
                int ID = results.getInt("id");
                String email = results.getString("email");
                System.out.println("Id = " + ID + ", Name = " + name + ", Email = " + email);
                result = "Id = " + ID + ", Name = " + name + ", Email = " + email;
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }

    public void listAllCustomers() {
        // TODO írd ki a konzolra az összes felhasználót
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String createCustomerSQL =
                    "SELECT name, age from " + DB_NAME + ";";

            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(createCustomerSQL);

            while (results.next()) {
                String name = results.getString("name");
                int age = results.getInt("age");
                System.out.println("Name = " + name + ", Age = " + age);
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
