import java.io.Serializable;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MD5CrackerServerImpl extends UnicastRemoteObject implements RemoteCracker, Runnable, Serializable {

    private static final int MIN_ASCII = 33;
    private static final int MAX_ASCII = 126;
    private static final int FIXED_THREAD_POOL_SIZE = 10; // Fixed thread pool size
    private volatile String foundPassword = null;

    private String hashToCrack;
    private int passwordLength;
    private int nnumberOfThreads;

    public MD5CrackerServerImpl(String hashToCrack, int passwordLength, int numberOfThreads) throws RemoteException {
        this.hashToCrack = hashToCrack;
        this.passwordLength = passwordLength;
        this.nnumberOfThreads = numberOfThreads;
    }

    @Override
    public void run() {
        try {
            System.out.println("Ready...");
        } catch (Exception e) {
            System.out.println("\n++++++++++++++++ERROR: Starting server failure++++++++++++++++++++");
            System.exit(1); //Exit program
        }
    }

    @Override
    public String crackPasswords(String hashToCrack, int passwordLength, int numberOfThreads) {

        // Determine the total password space based on the ASCII range
        int totalPasswordSpace = MAX_ASCII - MIN_ASCII + 1;

        long timeStart = System.currentTimeMillis();

        // Use a fixed thread pool with a predefined number of threads
        ExecutorService executorService = Executors.newFixedThreadPool(FIXED_THREAD_POOL_SIZE);

        // Determine the password space per thread
        int passwordSpacePerThread = totalPasswordSpace / numberOfThreads;
        int extraPasswordSpace = totalPasswordSpace % numberOfThreads;

        // Calculate the number of threads to use from the fixed pool
        int threadsToUse = Math.min(numberOfThreads, FIXED_THREAD_POOL_SIZE);

        for (int threadId = 0; threadId < threadsToUse; threadId++) {

            // Determine the range for the current thread
            int startSpace = threadId * passwordSpacePerThread;
            int endSpace = startSpace + passwordSpacePerThread - 1 + (extraPasswordSpace > 0 ? 1 : 0);

            // Decrement extraPasswordSpace after utilizing it
            if (extraPasswordSpace > 0) {
                extraPasswordSpace--;
            }

            //Start password searching
            executorService.execute(new MD5CrackThread(threadId, hashToCrack, passwordLength, numberOfThreads, startSpace, endSpace));
        }

        // Shutdown the executorService to prevent new tasks
        executorService.shutdown();

        // Wait for all threads to finish
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("\n++++++++++++++++ERROR: Termination failure++++++++++++++++++++");
                System.exit(1); //Exit program
            }
        }

        long timeEnd = System.currentTimeMillis();
        double timeSpent = (timeEnd - timeStart) / 60000.0; // Convert millis to minutes

        long threadId = Thread.currentThread().getId();
        System.out.println("Password found by thread ID #" + threadId + ": " + foundPassword);

        // Return the found password and time taken as part of the result
        return foundPassword + "\nTime taken: " + timeSpent + " minutes";
    }

    public class MD5CrackThread implements Runnable {
        private final int threadId;
        private final String hashToCrack;
        private final int passwordLength;
        private final int totalThreads;
        private final int startSpace;
        private final int endSpace;

        public MD5CrackThread(int threadId, String hashToCrack, int passwordLength, int totalThreads, int startSpace, int endSpace) {
            this.threadId = threadId;
            this.hashToCrack = hashToCrack;
            this.passwordLength = passwordLength;
            this.totalThreads = totalThreads;
            this.startSpace = startSpace;
            this.endSpace = endSpace;
        }

        @Override
        public void run() {

            String threadFoundPassword = generatePasswords(passwordLength);

            if (threadFoundPassword != null) {
                synchronized (MD5CrackerServerImpl.this) {
                    if (foundPassword == null || threadFoundPassword.length() < foundPassword.length()) {
                        foundPassword = threadFoundPassword;
                    }
                }
            }

        }

        private String generatePasswords(int length) {
            return generatePassword("", length);
        }

        private String generatePassword(String prefix, int length) {
            if (length == 0) {
                // Check the generated password against the hash
                if (getMD5(prefix).equals(hashToCrack)) {
                    return prefix;
                }
                return null;
            } else {

                //Iterates through all possible combinations within its assigned range and for the given password length
                for (int j = MIN_ASCII; j <= MAX_ASCII;  j++) {
                    String password = generatePassword(prefix + (char) j, length - 1);
                    if (password != null) {
                        return password;  // Password found, propagate it up
                    }
                }
                return null;
            }
        }
    }

    private String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (Exception e) {
            System.out.println("\n++++++++++++++++ERROR: Password cracking process fail++++++++++++++++++++");
            throw new RuntimeException(e);
        }
    }
}