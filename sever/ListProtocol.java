

public class ListProtocol {
    //specify some states
    private static final int WAITING = 0;
    private static final int TOTALS = 1;
    private static final int LIST = 2;
    private static final int JOIN = 3;
    private static final int INVALID = 4;

    private int state = WAITING; //initiate the state

    private int list_num = 0; //storing the number of lists

    public String processInput(String theInput) {
        String theOutput = null;

        //determine which command is being chosen
        if (state == WAITING) {
            if (theInput.equals("totals")) {
                state = TOTALS;
            } else if (theInput.equals("join")) {
                state = JOIN;
            } else if (theInput.equals("list")) {
                state = LIST;
            } else {
                state = INVALID;
            }
        }

        //Execute the total command
        if (state == TOTALS) {
            if (theInput.equals("totals")) {
                theOutput = "totals begin";
            } else if (theInput.equals("send list number")) {
                theOutput = Integer.toString(Server.lists_number) + ' ' + Integer.toString(Server.members_number_max);
                for (int i = 0; i < Server.lists_number; i++) {
                    int members_number = 0;
                    for (int j = 0; j < Server.members_number_max; j++) {
                        if (Server.listsOperation.lists[i][j] != null)
                            members_number++;
                    }
                    theOutput = theOutput + ' ' + Integer.toString(members_number);
                }
            } else if (theInput.equals("wrong command")) {
                theOutput = "finish";
                state = WAITING;
            } else if (theInput.equals("success")) {
                theOutput = "finish";
                state = WAITING;
            }
        }


        //Execute the join command
        if (state == JOIN) {
            if (theInput.equals("join")) {
                theOutput = "join begin";
            } else if (isNumeric(theInput)) {
                if (joinNumIsValid(theInput)) {
                    list_num = Integer.parseInt(theInput);
                    theOutput = "valid number";
                } else {
                    theOutput = "invalid number";
                }
            } else if (theInput.equals("wrong command")) {
                theOutput = "finish";
                state = WAITING;
            } else {
                int members_number = 0;
                for (int j = 0; j < Server.members_number_max; j++) {
                    if (Server.listsOperation.lists[list_num - 1][j] != null)
                        members_number++;
                }
                Server.listsOperation.lists[list_num - 1][members_number] = theInput;
                theOutput = "finish";
                state = WAITING;
            }
        }

        //Execute the list command
        if (state == LIST) {
            if (theInput.equals("list")) {
                theOutput = "list begin";
            } else if (isNumeric(theInput)) {
                if (listNumIsValid(theInput)) {
                    list_num = Integer.parseInt(theInput);
                    theOutput = "valid list number";
                } else {
                    theOutput = "invalid list number";
                }
            } else if (theInput.equals("send data")) {
                theOutput = Server.listsOperation.lists[list_num - 1][0];
                int members_number = 0;
                for (int j = 0; j < Server.members_number_max; j++) {
                    if (Server.listsOperation.lists[list_num - 1][j] != null)
                        members_number++;
                }
                for (int i = 1; i < members_number; i++){
                    theOutput = theOutput + '\t' + Server.listsOperation.lists[list_num - 1][i];
                }
            } else {
                theOutput = "finish";
                state = WAITING;
            }
        }

        if (state == INVALID) {
            theOutput = "invalid command";
            state = WAITING;
        }

        return theOutput;
    }

    //determine if the String is numeric
    private boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //determine if the join number is valid
    private boolean joinNumIsValid(String joinNum) {
        if (Integer.parseInt(joinNum) <= Server.lists_number && Integer.parseInt(joinNum) > 0) {
            int members_number = 0;
            for (int j = 0; j < Server.members_number_max; j++) {
                if (Server.listsOperation.lists[Integer.parseInt(joinNum) - 1][j] != null)
                    members_number++;
            }
            return members_number < Server.members_number_max;
        } else
            return false;
    }

    //determine if the list number is valid
    private boolean listNumIsValid(String listNum) {
        return Integer.parseInt(listNum) <= Server.lists_number && Integer.parseInt(listNum) > 0;
    }


}
