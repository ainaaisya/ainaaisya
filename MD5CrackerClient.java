import java.rmi.Naming;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

public class MD5CrackerClient {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input validation for MD5 hash
        String hashToCrack;
        do {
            System.out.print("Enter the MD5 hash to crack: ");
            hashToCrack = scanner.nextLine().trim();
            if (hashToCrack.isEmpty()) {
                System.out.println("MD5 hash cannot be empty. Please enter a valid hash.");
                System.out.println("++++++++++++++++++++++++++++++++++++");
            }
        } while (hashToCrack.isEmpty());

        // Input validation for password length
        int passwordLength;
        do {
            System.out.print("Enter the password length (2 to 5): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                System.out.println("++++++++++++++++++++++++++++++++++++");
                scanner.next(); // consume the invalid input
            }
            passwordLength = scanner.nextInt();
            scanner.nextLine(); // consume the newline character
            if (passwordLength < 2 || passwordLength > 5) {
                System.out.println("Password length must be between 2 and 5. Please enter a valid length.");
                System.out.println("++++++++++++++++++++++++++++++++++++");
            }
        } while (passwordLength < 2 || passwordLength > 5);

        // Input validation for number of threads
        int numberOfThreads;
        do {
            System.out.print("Enter the number of threads (1 to 10): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                System.out.println("++++++++++++++++++++++++++++++++++++");
                scanner.next(); // consume the invalid input
            }
            numberOfThreads = scanner.nextInt();
            scanner.nextLine(); // consume the newline character
            if (numberOfThreads < 1 || numberOfThreads > 10) {
                System.out.println("Number of threads must be between 1 and 10. Please enter a valid number.");
                System.out.println("++++++++++++++++++++++++++++++++++++");
            }
        } while (numberOfThreads < 1 || numberOfThreads > 10);

        // Input validation for number of servers
        int numberOfServers;
        do {
            System.out.print("Enter the number of servers: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                System.out.println("++++++++++++++++++++++++++++++++++++");
                scanner.next(); // consume the invalid input
            }
            numberOfServers = scanner.nextInt();
            scanner.nextLine(); // consume the newline character
            if (numberOfServers < 1 || numberOfServers > 2) {
                System.out.println("Two servers are available. Please enter a valid number.");
                System.out.println("++++++++++++++++++++++++++++++++++++");
            }
        } while (numberOfServers < 1 || numberOfServers > 2);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfServers);
        List<ServerDetails> serverDetailsList = new ArrayList<>();

        for (int i = 1; i <= numberOfServers; i++) {
            System.out.println("++++++++++++++++++++++++++++++++++++");
            System.out.println("Enter details for server #" + i + ":");

            // Input validation for server address
            String serverAddress;
            do {
                System.out.print("Server address: ");
                serverAddress = scanner.nextLine().trim();
                if (serverAddress.isEmpty()) {
                    System.out.println("Server address cannot be empty. Please enter a valid address.");
                    System.out.println("++++++++++++++++++++++++++++++++++++");
                }
            } while (serverAddress.isEmpty());

            // Input validation for server port
            int serverPort;
            do {
                System.out.print("Server port: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a valid port number.");
                    System.out.println("++++++++++++++++++++++++++++++++++++");
                    scanner.next(); // consume the invalid input
                }
                serverPort = scanner.nextInt();
                scanner.nextLine(); // consume the newline character
                if (serverPort < 1 || serverPort > 8888) {
                    System.out.println("Invalid port number. Please enter a valid port number.");
                    System.out.println("++++++++++++++++++++++++++++++++++++");
                }
            } while (serverPort < 1 || serverPort > 8888);

            // Store server details
            serverDetailsList.add(new ServerDetails(serverAddress, serverPort));
        }

        // Connect to RMI and crack password concurrently
        for (ServerDetails serverDetails : serverDetailsList) {
            int finalNumberOfThreads = numberOfThreads;

            String finalHashToCrack = hashToCrack;
            int finalPasswordLength = passwordLength;

            executorService.submit(() -> {
                connectAndCrack(serverDetails, finalHashToCrack, finalPasswordLength, finalNumberOfThreads);
            });
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println("\n++++++++++++++++ERROR: Please try again later++++++++++++++++++++");
            System.exit(1); //Exit program
        }

        scanner.close();
    }

    //Connect to RMI
    private static void connectAndCrack(ServerDetails serverDetails, String hashToCrack, int passwordLength, int numberOfThreads) {
        try {
            RemoteCracker server = (RemoteCracker) Naming.lookup("rmi://" + serverDetails.getAddress() + ":" + serverDetails.getPort() + "/MD5CrackerServerMain");

            // Create a client and initiate the cracking process
            String result = server.crackPasswords(hashToCrack, passwordLength, numberOfThreads);

            //Display result from server
            System.out.println("Result from server " + serverDetails.getAddress() + " is " + result);

        } catch (Exception e) {
            System.out.println("\n++++++++++++++++ERROR: Retrieving result failure++++++++++++++++++++");
            System.exit(1); //Exit program
        }
    }

    //Store server details
    private static class ServerDetails {
        private final String address;
        private final int port;

        public ServerDetails(String address, int port) {
            this.address = address;
            this.port = port;
        }

        public String getAddress() {
            return address;
        }

        public int getPort() {
            return port;
        }
    }
}