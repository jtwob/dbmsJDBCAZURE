import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AzureSQLJDBC {
    // JDBC URL for Azure SQL Database
    private static final String url = "jdbc:sqlserver://dbms-james.database.windows.net:1433;database=DBMS;user=james@dbms-james;password=<PASSWORD>;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    public static void main(String[] args) {
        // Declare connection objects
        Connection connection = null;
        Statement statement = null;

        try {
            // Establish the connection
            connection = DriverManager.getConnection(url);
            System.out.println("Connected to Azure SQL Database!");

            // Create a statement
            statement = connection.createStatement();

            // Execute a simple query
            String sql = "SELECT TOP 10 * FROM Faculty";

            String insertion = "INSERT INTO Faculty (fid, fname, deptid, salary)\n" + //
                                "VALUES (" + //
                                //"    -- Insert faculty ID" + //
                                "    501, " + //
                                "" + //
                               // "    -- Insert faculty name" + //
                                "    'Auld', " + //
                                "" + //
                                //"    -- Insert department ID" + //
                                "    10," + //
                                "" + //
                                //"    -- Calculate salary based on average faculty salary in the same department" + //
                                "    CASE" + //
                                "        WHEN (SELECT AVG(salary) FROM Faculty WHERE deptid = 10) > 50000" + //
                                "            THEN 0.9 * (SELECT AVG(salary) FROM Faculty WHERE deptid = 10)" + //
                                "        WHEN (SELECT AVG(salary) FROM Faculty WHERE deptid = 10) < 30000" + //
                                "            THEN (SELECT AVG(salary) FROM Faculty WHERE deptid = 10)" + //
                                "        ELSE" + //
                                "            0.8 * (SELECT AVG(salary) FROM Faculty WHERE deptid = 10)" + //
                                "    END" + //
                                ");";

            ResultSet resultSet = statement.executeQuery(sql);

            // Process the result set
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("fid") + ", Name: " + resultSet.getString("fname") + ", Dept: " + resultSet.getString("deptid") + ", Salary: " + resultSet.getString("salary"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
