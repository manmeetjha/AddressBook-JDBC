import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookJDBCServices{

    public static List<Address> getContactsForDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = String.format("select * from addressbook where Date >= '%s' AND Date <= '%s'", startDate,endDate);
        return getContactList(sql);
    }

    public static List<Address> getContactsByCity(String city) {
        String sql = String.format("select * from addressbook where City = '%s'",city);
        return getContactList(sql);
    }

    public static void insertIntoDB(String firstName, String lastName, String address, String city, String state, String zip, String phone_no, String email, String date) {
        String sql = String.format("insert into addressbook (firstName, lastName, Address, City, State, Zip, Phone_No, Email, Date) values ('%s','%s','%s','%s','%s',%s,%s,'%s','%s')",
                firstName,lastName,address,city,state,zip,phone_no,email,date);
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Address> readData() {
        String sql = String.format("select * from addressbook");
        return getContactList(sql);
    }

    private static List<Address> getContactList(String sql) {
        List<Address> contactList = new ArrayList<>();
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                contactList.add(new Address(resultSet.getString("firstName"), resultSet.getString("lastName"),
                        resultSet.getString("Address"), resultSet.getString("City"), resultSet.getString("State"),
                        resultSet.getString("Zip"), resultSet.getString("Phone_No"), resultSet.getString("Email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }

    private static Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/addressbook?useSSL=false";
        String userName = "root";
        String password = "Monu@12783";
        return DriverManager.getConnection(jdbcURL, userName, password);
    }

    public static List<Address> getContacts(String firstName) {
        String sql = String.format("select * from addressbook where firstName = '%s'", firstName);
        return getContactList(sql);
    }

    public int updateContactUsingSQL(String firstName, String column, String columnValue) {
        String sql = String.format("UPDATE addressbook SET %s = '%s' WHERE firstName = '%s';", column, columnValue,
                firstName);
        System.out.println("sql: "+sql);
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
