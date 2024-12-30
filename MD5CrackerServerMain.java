import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MD5CrackerServerMain {

    private static final int MAX_CLIENTS = 10;
    private static ExecutorService executorService;

    private static String serverAddress;
    private static int serverPort;
    private static String hashToCrack;
    private static int passwordLength;
    private static int numberOfThreads;

    public static void main(String[] args) {

        MD5CrackerServerMain serverMain = new MD5CrackerServerMain();
        serverMain.receiveClientInput(hashToCrack, passwordLength, numberOfThreads, serverAddress, serverPort);

    }

    public void receiveClientInput(String hashToCrack, int passwordLength, int numberOfThreads, String serverAddress, int serverPort) {

        // Set received input values
        this.hashToCrack = hashToCrack;
        this.passwordLength = passwordLength;
        this.numberOfThreads = numberOfThreads;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;

        // Start the server with the received input values
        startServer(hashToCrack, passwordLength, numberOfThreads);
    }

    public void startServer(String hashToCrack, int passwordLength, int numberOfThreads) {

        executorService = Executors.newFixedThreadPool(MAX_CLIENTS);

        try {
            // Look up the remote object in the RMI registry using the provided server address and port
            MD5CrackerServerImpl server = new MD5CrackerServerImpl(hashToCrack, passwordLength, numberOfThreads);

            // Attempt to create the registry
            LocateRegistry.createRegistry(serverPort);
            System.out.println("Server started. Listening for connections...");

            // Bind the server to the RMI registry
            Naming.rebind("MD5CrackerServerMain", server);

            executorService.submit(server);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {

                // Clean up resources when the server is shutting down
                executorService.shutdown();

                try {
                    executorService.awaitTermination(5, TimeUnit.SECONDS);
                }
                catch (InterruptedException e) {
                    System.out.println("\n++++++++++++++++ERROR: Termination failure++++++++++++++++++++");
                    System.exit(1); //Exit program
                }

            }));

        }
        catch (IOException e) {
            System.out.println("\n++++++++++++++++ERROR: Starting server failure++++++++++++++++++++");
            System.exit(1); //Exit program
        }
    }
}
