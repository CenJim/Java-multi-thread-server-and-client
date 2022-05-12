/*
 * This class is a subclass of class Thread, which is a thread
 * used to handle the client.
 */

import java.net.*;
import java.io.*;
import java.util.*;

public class ClientHandler extends Thread{
    private Socket socket = null;

    public ClientHandler(Socket socket){
        super("ClientHandler");
        this.socket = socket;
    }

    public void run(){
        try {

            // Input and output streams to/from the client.
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            // Logging.
            InetAddress inet = socket.getInetAddress();
            String wholeRequest = in.readLine();
            Server.listsOperation.write_logfile(inet, wholeRequest);

            // Initialise a protocol object for this client.
            String inputLine, outputLine;
            ListProtocol lp = new ListProtocol();

            // Sequential protocol.
            while( (inputLine = in.readLine())!=null ) {
                outputLine = lp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("finish"))
                    break;
            }

            // Free up resources for this connection.
            out.close();
            in.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
