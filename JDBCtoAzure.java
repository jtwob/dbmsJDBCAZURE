import java.util.Scanner;

public class JDBCtoAzure {


    
    public static void main(String[] args) {
        // Create a Scanner object to read input from the command line (System.in)
        Scanner scanner = new Scanner(System.in);

        // Output a message asking the user for input
        System.out.println("Enter your input (type 'exit' to quit):");

        // Read input in a loop until the user types 'exit'
        while (true) {
            // Read the next line of input
            String input = scanner.nextLine();

            // Check if the user wants to exit
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            // Output the user's input
            System.out.println("You entered: " + input);
        }

        scanner.close();
    }
}