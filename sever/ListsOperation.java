import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ListsOperation {
    public String[][] lists; //Storing ID lists
    public static File logFile = null;
    public static Writer out = null;

    public ListsOperation(int lists_number, int members_number) {
        lists = new String[lists_number][members_number]; //initiate the lists for storing the name

        logFile = new File("log.txt"); //initiate the logFile
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException exp) {
                System.err.println("failed to creat logFile: " + exp);
                System.exit(-1);
            }
        }

        try {
            out = new FileWriter(logFile, false); //initiate the FileWriter
        } catch (IOException exp) {
            System.err.println("Error" + exp);
            System.exit(-1);
        }

    }

    //Writing log file
    public void write_logfile(InetAddress inet, String request) {
        String output;
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd|hh:mm:ss");
        output = ft.format(date) + '|' + inet.getHostAddress() + '|' + request + ".\n"; //correct the format
        try {
            out.write(output);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
         }
    }

    public String[][] getLists() {
        return lists;
    }
}
