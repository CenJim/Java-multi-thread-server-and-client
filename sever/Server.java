import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class Server {
    public static ListsOperation listsOperation;  //Instantiating a ListsOperation class
    public static int lists_number;   //the number of lists
    public static int members_number_max;    //the max number of the list's number

    public static void main(String[] args) throws IOException {
        if (!Server.commandIsValid(args)) {  //Judging whether command line arguments are valid
            System.out.println("The command arguments are invalid. It should be two positive numbers.");
            return;
        }
        lists_number = Integer.parseInt(args[0]);
        members_number_max = Integer.parseInt(args[1]);
        listsOperation = new ListsOperation(lists_number, members_number_max);

        ServerSocket server = null;
        ExecutorService service = null;

        try {
            server = new ServerSocket(9101);  //Choosing the port 9101
        } catch (IOException e) {
            System.err.println("Could not listen on port: 9101.");
            System.exit(-1);
        }

        service = Executors.newFixedThreadPool(25);  //Use an Executor to manage a fixed thread-pool with 25 connections.

        while (true) {
            Socket client = server.accept();  //Waiting for a link from the client
            service.submit(new ClientHandler(client));
        }
    }

    //Judging whether command line arguments are valid
    public static boolean commandIsValid(String[] command) {
        if (command.length == 2) {
            return isNumeric(command[0]) && isNumeric(command[1]) && Integer.parseInt(command[0]) > 0 && Integer.parseInt(command[1]) > 0;
        } else
            return false;
    }

    //Determining if a String is numeric
    private static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}