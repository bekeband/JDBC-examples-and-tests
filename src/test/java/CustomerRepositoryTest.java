import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerRepositoryTest {

    private static final String CATALOG = "TEST";
    private static final String PUBLIC_SCHEMA = "PUBLIC";
    private static final String DB_URL = "jdbc:h2:mem:" + CATALOG + ";DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private static final String DB_TABLE_NAME = "customer";

    private CustomerRepository customerRepository;

    @BeforeEach
    void init()  {
        customerRepository = new CustomerRepository(DB_URL, DB_USER, DB_PASSWORD, DB_TABLE_NAME);
        customerRepository.createTable();
    }

    @AfterEach
    void destruct() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String dropTable = "DROP TABLE IF EXISTS " + DB_TABLE_NAME;
            Statement statement = connection.createStatement();
            statement.execute(dropTable);
        }
    }

    @Test
    void createTable() {
        boolean result = false;
        customerRepository.createTable();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            ResultSet resultSet = connection.getMetaData().getCatalogs();

            System.out.println("Catalogs -----------------------------");
            while (resultSet.next()) {
                System.out.println("Catalog " + resultSet.getString("TABLE_CAT"));
            }

            resultSet = connection.getMetaData().getSchemas();
            System.out.println("Schemas -----------------------------");
            while (resultSet.next()) {
                System.out.print("Catalog " + resultSet.getString("TABLE_CATALOG"));
                System.out.println(", Schema " + resultSet.getString("TABLE_SCHEM"));
            }

            System.out.println("Tables -----------------------------");

            resultSet = connection.getMetaData().getTables(CATALOG, PUBLIC_SCHEMA,
                    null, null);

            while (resultSet.next()) {
                System.out.print("Catalog:" + resultSet.getString("TABLE_CAT"));
                System.out.print(", Schema:" + resultSet.getString("TABLE_SCHEM"));
                System.out.println(", Tablename:" + resultSet.getString("TABLE_NAME"));
            }
            resultSet = connection.getMetaData().getTables(CATALOG, PUBLIC_SCHEMA,
                    null, null);
            while (resultSet.next()) {
                if((DB_TABLE_NAME.equalsIgnoreCase(resultSet.getString("TABLE_NAME")))) {
                    result = true;
                }
            }
            resultSet.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        assertTrue(result);
    }

    @Test
    void createCustomer() {
        customerRepository.createCustomer(100, "email@email.mail", "Mail Elek");
        assertTrue(getCustomerID("Mail Elek") != -1);
    }

    @Test
    void updateEmail() {
        customerRepository.createCustomer(200, "uemail@email.mail", "Change Elek");
        customerRepository.updateEmail(200, "changed@mail.hu");
        assertFalse(getEmail(200).equals("no mail address"));
    }

    @Test
    void printCustomer() {
        customerRepository.createCustomer(334, "gmail@mail.hu", "Gémail Zénó");
        assertTrue(customerRepository.printCustomer(334).
                equalsIgnoreCase("Id = 334, Name = Gémail Zénó, Email = gmail@mail.hu"));
    }


    public String getEmail(int id) {
        String result = "no mail address";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sqlSelectQuery  = "SELECT email FROM customer WHERE id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectQuery);

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result = resultSet.getString("email");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }

    public int getCustomerID(String customerName) {
        int result = -1;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sqlSelectQuery  = "SELECT ID FROM customer WHERE name=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlSelectQuery);

            preparedStatement.setString(1, customerName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result = resultSet.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}