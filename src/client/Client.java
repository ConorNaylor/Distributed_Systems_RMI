package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.ExamServer;
public class Client {

	public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ExamServer stub = (ExamServer) registry.lookup("ExamServer");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
