import java.io.*;
import java.net.*;

public class Client {
    private Socket socket = null;
    private PrintWriter socketOutput = null;
    private BufferedReader socketInput = null;

    public String wholeRequest; //the whole request typed in the cml

    public void runClient(String[] args) {

        try {
            socket = new Socket("localhost", 9101); //Creat socket. Severs running on the same machine
            socketOutput = new PrintWriter(socket.getOutputStream(), true); //Chain a writing stream
            socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host.\n");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to host.\n");
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(
                new InputStreamReader(System.in));

        String fromServer;

        socketOutput.println(wholeRequest);

        socketOutput.println(args[0]);

        //Communicating with the server
        try {
            while ((fromServer = socketInput.readLine()) != null) {
                // Echo server string.
                if (fromServer.equals("finish")) break;
                if (fromServer.equals("invalid command")) {
                    System.out.println("Invalid command.");
                    break;
                }
                if (fromServer.equals("inValid")) {
                    System.out.println("The command is invalid.");
                    break;
                }

                if (fromServer.equals("totals begin")) {
                    if (args.length == 1) {
                        socketOutput.println("send list number");
                        fromServer = socketInput.readLine();
                        printTotals(fromServer);
                        socketOutput.println("success");
                    } else {
                        socketOutput.println("wrong command");
                        System.out.println("The command is invalid.");
                    }
                } else if (fromServer.equals("join begin")) {
                    if (isNumeric(args[1]) && args.length == 3) {
                        socketOutput.println(args[1]);
                        fromServer = socketInput.readLine();
                        if (fromServer.equals("valid number")) {
                            socketOutput.println(args[2]);
                            System.out.println("Success.");
                        } else {
                            System.out.println("Failed.");
                            socketOutput.println("wrong command");
                        }
                    } else {
                        socketOutput.println("wrong command");
                        System.out.println("The list number is not numeric.");
                    }
                } else if (fromServer.equals("list begin")) {
                    if (isNumeric(args[1]) && args.length == 2) {
                        socketOutput.println(args[1]);
                        fromServer = socketInput.readLine();
                        if (fromServer.equals("valid list number")) {
                            socketOutput.println("send data");
                            fromServer = socketInput.readLine();
                            printList(fromServer);
                            socketOutput.println("success");
                        } else {
                            System.out.println("The number of list is invalid.");
                            socketOutput.println("wrong command");
                        }
                    } else {
                        socketOutput.println("wrong command");
                        System.out.println("The list number is not numeric");
                    }
                }


            }
            socketOutput.close();
            socketInput.close();
            stdIn.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("I/O exception during execution\n");
            System.exit(1);
        }
    }

    //print the total lists
    private void printTotals(String totals) {
        String[] numbersOfTotals = totals.split("\\s+");
        String list_number = numbersOfTotals[0];
        String member_number = numbersOfTotals[1];
        System.out.println("There are " + list_number + " list(s), each with a maximum size of " + member_number + '.');
        for (int i = 0; i < Integer.parseInt(list_number); i++) {
            System.out.println("List " + (i + 1) + " has " + numbersOfTotals[i + 2] + " member(s).");
        }
    }

    private boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //print the member in the list
    private void printList(String list) {
        String[] name = list.split("\\t+");
        for (int i = 0; i < name.length; i++) {
            System.out.println(name[i]);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.wholeRequest = args[0];
        for (int i = 1; i < args.length; i++)
            client.wholeRequest = client.wholeRequest + ' ' + args[i];
        client.runClient(args);
    }
}