## Hi there!

# MD5 Cracker Application

This README provides step-by-step instructions to set up and run the MD5 Cracker application. The application is designed to crack MD5 hashes using distributed computing and multithreading.

## Prerequisites

Before you begin, ensure that you have the following installed on your machine:

- **Java Development Kit (JDK)** (version 8 or later)
- **RMI Registry**
- A basic understanding of the command line/terminal

## Application Components

The application consists of the following Java files:

1. **MD5CrackerClient.java**: Client-side application to input parameters and interact with servers.
2. **MD5CrackerServerImpl.java**: Implementation of the server logic for cracking MD5 hashes.
3. **MD5CrackerServerMain.java**: Entry point for starting the server.
4. **RemoteCracker.java**: Interface for defining server methods accessible by the client.

## Setup Instructions

### 1. Compile the Java Files

Compile all Java files to ensure there are no syntax or compilation errors.

```bash
javac *.java
```

### 2. Start the RMI Registry

Run the RMI Registry in the background. Open a terminal and execute the following command:

```bash
rmiregistry &
```

### 3. Launch the Servers

Start one or more servers using the `MD5CrackerServerMain` class. Each server will listen for client requests and perform hash cracking tasks.

Run the server with the following command, replacing `<port>` with the desired port number (e.g., 1099):

```bash
java MD5CrackerServerMain
```

When prompted, provide the following details for each server:
- **Hash to crack**
- **Password length**
- **Number of threads**
- **Server address**
- **Server port**

### 4. Run the Client

Execute the client application to connect to the servers and initiate the hash-cracking process.

Run the client with:

```bash
java MD5CrackerClient
```

When prompted, provide the following inputs:
- **MD5 hash to crack**: The hash you want to decrypt.
- **Password length**: Length of the password (2 to 5 characters).
- **Number of threads**: Number of threads for computation (1 to 10).
- **Number of servers**: Total servers to connect to (1 or 2).

For each server, input:
- **Server address**: The server's address (e.g., `localhost`).
- **Server port**: The port the server is listening on.

### 5. View the Results

The client will display results from each connected server, including the cracked password (if found) and the time taken for the computation.

### 6. Stop the Application

Once the client displays results, the application terminates automatically. If needed, stop the servers by interrupting the terminal process (e.g., `Ctrl+C`).

## Troubleshooting

- Ensure that the RMI registry is running before starting the servers.
- Verify that the server address and port match the details entered in the client.
- Check for firewall or network issues that may block RMI communication.

## Notes

- The application supports up to two servers. If additional servers are required, the code can be modified accordingly.
- Ensure that the input MD5 hash and other parameters are valid to avoid unexpected errors.

## Acknowledgment

This project was developed by Aina Aisya in collaboration with an incredible team. I took the lead and contributed significantly to the overall code, ensuring the success of this application. Special thanks to my amazing teammates Jacyntha Eleanor, Anrianih Yusof, Nurul Fatihah, and Nur Izzatul Natasha from Universiti Malaysia Sarawak (UNIMAS) for their invaluable support and contributions.

## License

This project is open-source and free to use. Contributions and improvements are welcome.

