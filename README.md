## Hi there 👋
This is Aina Aisya! I shared and took credit of this project as I hugely contributed to the overall code. The success of this project entirely relied on the help of my teammates, Jacyntha Eleanor, Anrianih Yusof, Nurul Fatihah, and Nur Izzatul Natasha of Universiti Malaysia Sarawak (UNIMAS)

# MD5 Cracker Application

This guide will help you set up and run the MD5 Cracker application. The app uses distributed computing and multithreading to crack MD5 hashes.

## What You Need

Before starting, make sure your computer has:

- **Java Development Kit (JDK)** version 8 or later.
- **RMI Registry** installed.
- Some experience using the command line.

## Files in the Application

The application has four main files:

1. **MD5CrackerClient.java**: Lets users input settings and communicate with the servers.
2. **MD5CrackerServerImpl.java**: Handles the main logic for cracking the MD5 hashes.
3. **MD5CrackerServerMain.java**: Starts the server program.
4. **RemoteCracker.java**: Defines the methods that the client can use on the server.

## How to Set It Up

### 1. Compile the Code

Compile all the Java files to check for errors. Run this command in your terminal:

```bash
javac *.java
```

### 2. Start the RMI Registry

Start the RMI registry in the background. Use this command:

```bash
rmiregistry &
```

### 3. Launch the Servers

Run one or more servers to handle hash-cracking tasks. Use this command to start the server, replacing `<port>` with a port number (like `1099`):

```bash
java MD5CrackerServerMain
```

When the server starts, you will need to enter:
- The hash you want to crack.
- The length of the password.
- The number of threads to use.
- The server's address.
- The server's port.

### 4. Run the Client

Run the client program to connect to the servers and start cracking the hash. Use this command:

```bash
java MD5CrackerClient
```

The client will ask you to enter:
- **MD5 hash**: The hash you want to crack.
- **Password length**: The length of the password (2 to 5 characters).
- **Number of threads**: The number of threads to use (1 to 10).
- **Number of servers**: How many servers to connect to (1 or 2).

For each server, you also need to provide:
- **Server address**: For example, `localhost`.
- **Server port**: The port number the server is using.

### 5. See the Results

The client will show you the results from the servers, including:
- The cracked password (if found).
- The time it took to crack the hash.

### 6. Stop the Application

Once the client finishes, it will stop on its own. If you want to stop the servers, press `Ctrl+C` in the terminal.

## Troubleshooting Tips

- Make sure the RMI registry is running before starting the servers.
- Check that the server address and port match what you entered in the client.
- If you have network issues, ensure that firewalls aren’t blocking RMI communication.

## Notes

-The application supports up to two servers. If additional servers are required, the code can be modified accordingly.
-Ensure that the input MD5 hash and other parameters are valid to avoid unexpected errors.

## Acknowledgment

-This project was developed by Aina Aisya in collaboration with an incredible team. I took the lead and contributed significantly to the overall code, ensuring the success of this application. Special thanks to my amazing teammates Jacyntha Eleanor, Anrianih Yusof, Nurul Fatihah, and Nur Izzatul Natasha from Universiti Malaysia Sarawak (UNIMAS) for their invaluable support and contributions.

## License

-This project is open-source and free to use. Contributions and improvements are welcome.
