import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteCracker extends Remote {
    String crackPasswords(String hashToCrack, int passwordLength, int numberOfThreads) throws RemoteException;
}
