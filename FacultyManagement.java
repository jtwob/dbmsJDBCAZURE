import java.sql.*;
import java.util.Scanner;

public class FacultyManagement {
    private static final String url = "jdbc:sqlserver://dbms-james.database.windows.net:1433;database=DBMS;user=james@dbms-james;password=121319Aa!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;

        try {
            // connect to database
            connection = DriverManager.getConnection(url);
            System.out.println("Connected to Azure SQL Database!");

            boolean keepRunning = true;

            while (keepRunning) {
                // print menu
                System.out.println("\nMENU");
                System.out.println("1. Insert Faculty with Department Salary Logic");
                System.out.println("2. Insert Faculty with Average Salary Excluding Department");
                System.out.println("3. Get All Faculty");
                System.out.println("4. Quit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        // insert faculty with department salary logic
                        insertFacultyWithDeptSalary(connection, scanner);
                        break;
                    case 2:
                        // insert faculty with average salary excluding a department
                        insertFacultyWithAvgExcludingDept(connection, scanner);
                        break;
                    case 3:
                        // get all faculty members
                        getAllFaculty(connection);
                        break;
                    case 4:
                        // quit
                        keepRunning = false;
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            scanner.close();
        }
    }

    // Func to insert faculty with department salary logic
    private static void insertFacultyWithDeptSalary(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter faculty ID: ");
            int fid = scanner.nextInt();
            scanner.nextLine(); 

            System.out.print("Enter faculty name: ");
            String fname = scanner.nextLine();

            System.out.print("Enter department ID: ");
            int deptid = scanner.nextInt();
            scanner.nextLine();

            // call T-SQL stored procedure for department-specific salary logic
            CallableStatement callableStatement = connection.prepareCall("{CALL InsertFacultyWithDeptSalary(?, ?, ?)}");
            callableStatement.setInt(1, fid);
            callableStatement.setString(2, fname);
            callableStatement.setInt(3, deptid);

            callableStatement.execute();
            System.out.println("Faculty inserted successfully with department-specific salary logic!");

            callableStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Func to insert faculty with average salary excluding a department
    private static void insertFacultyWithAvgExcludingDept(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter faculty ID: ");
            int fid = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter faculty name: ");
            String fname = scanner.nextLine();

            System.out.print("Enter department ID: ");
            int deptid = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter department ID to exclude from average salary: ");
            int exclude_deptid = scanner.nextInt();
            scanner.nextLine();

            // call T-SQL stored procedure for average salary excluding a department
            CallableStatement callableStatement = connection
                    .prepareCall("{CALL InsertFacultyWithAvgSalaryExcludingDept(?, ?, ?, ?)}");
            callableStatement.setInt(1, fid);
            callableStatement.setString(2, fname);
            callableStatement.setInt(3, deptid);
            callableStatement.setInt(4, exclude_deptid);

            callableStatement.execute();
            System.out.println("Faculty inserted successfully with average salary excluding specified department!");

            callableStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Func to get all faculty members
    private static void getAllFaculty(Connection connection) {
        try {
            // call T-SQL stored procedure to get all faculty members
            CallableStatement callableStatement = connection.prepareCall("{CALL GetAllFaculty}");

            ResultSet resultSet = callableStatement.executeQuery();

            // print faculty table nicely
            System.out.println("\nFaculty List:");
            System.out.println("FID\tFName\tDeptID\tSalary");

            while (resultSet.next()) {
                int fid = resultSet.getInt("fid");
                String fname = resultSet.getString("fname");
                int deptid = resultSet.getInt("deptid");
                double salary = resultSet.getDouble("salary");

                System.out.println(fid + "\t" + fname + "\t" + deptid + "\t" + salary);
            }

            resultSet.close();
            callableStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
