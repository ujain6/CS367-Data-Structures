
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A class implementing the singleton pattern (only one instance of this class
 * can every exist) You cannot instantiate this class from outside by using the
 * new operator. The only way to get the only instance is to use
 * Config.getInstance() This class stores session wide information like the list
 * of all users, the currently logged in user and the console I/O objects. This
 * class contains ALL the strings that you may print ever in this project. This
 * is done in an effort to avoid typos that you can make and to ease testing
 * effort. Some of these strings have %s embedded in them. You must use the
 * String.format method to substitute appropriate strings in these parent
 * strings. For being exactly sure of the locations where these constants ought
 * to be used, thoroughly read ALL the javadocs
 *
 * DO NOT CHANGE THIS CLASS IN ANY WAY
 *
 * @author jmishra
 */
public class Config
{

    private static Config instance = null;

    private User currentUser;
    private List<User> allUsers;
    private Scanner consoleInput;
    private PrintStream consoleOutput;

    /**
     * This is shown if the nickname is already a friend
     */
    public static final String ALREADY_A_FRIEND = "Already a friend\n";
    /**
     * This is shown if the supplied nickname is already present in the
     * broadcast list
     */
    public static final String ALREADY_PRESENT = "Already present\n";
    /**
     * This is shown if the broadcast list on which some operation is to be
     * performed does not exist
     */
    public static final String BCAST_LIST_DOES_NOT_EXIST = "Broadcast list %s does not exist\n";
    /**
     * This is shown if trying to add the user to one of his own broadcast lists
     */
    public static final String CANT_ADD_YOURSELF_TO_BCAST = "You can't add yourself to a broadcast list\n";
    /**
     * Encapsulate this string inside most instances of WhatsAppRuntimeException
     */
    public static final String CANT_BE_EMPTY_OR_NULL = "The supplied field cannot be null or empty\n";
    /**
     * This is shown if trying to add the user as his own friend
     */
    public static final String CANT_BE_OWN_FRIEND = "You can't be your own friend\n";
    /**
     * This is shown if you cannot locate the nickname supplied for various
     * operations. For places where you must use this string, see the individual
     * javadoc descriptions of methods, specifically of the CommandProcessor
     * class
     */
    public static final String CANT_LOCATE = "Can't locate %s\n";
    /**
     * This is shown if trying to send a message to the same user
     */
    public static final String CANT_SEND_YOURSELF = "Can't send yourself a message\n";
    /**
     * This is the prompt for the logged in user to enter a command
     */
    public static final String ENTER_COMMAND = "Enter command:\n";
    /**
     * This is shown if some error occurs while reading the input file
     */
    public static final String ERROR_IO = "Some error while reading the input file--Exiting\n";
    /**
     * Show this just before exiting the program when the exit: command is
     * issued
     */
    public static final String EXITING = "Exiting\n";
    /**
     * This is shown if the input file does not exist
     */
    public static final String FILE_DOES_NOT_EXIST = "The input file does not exist\n";
    /**
     * This is shown when the entered command is not valid
     */
    public static final String INVALID_COMMAND = "Invalid command\n";

    /**
     * This is shown if the nickname/password pair is incorrect
     */
    public static final String INVALID_CREDENTIALS = "Invalid nickname/password combination--Try again\n";
    /**
     * This is shown to the logged in user for getting a command
     */
    public static final String LOGIN_PROMPT = "Enter nickname:\n";
    /**
     * This is the message template to be used in read message commands
     */
    public static final String MESSAGE_FORMAT = "From: %s To: %s Message: %s Time Sent: %s\n";
    /**
     * This is shown when a message is sent successfully to the intended
     * receiver(s)
     */
    public static final String MESSAGE_SENT_SUCCESSFULLY = "Sent successfully\n";
    /**
     * This is shown if the nickname is not an existing global contact
     */
    public static final String NICKNAME_DOES_NOT_EXIST = "I don't know %s\n";
    /**
     * This is shown if the supplied nickname is not a friend
     */
    public static final String NOT_A_FRIEND = "Not a friend\n";
    /**
     * This is shown if the user nickname is not a member of the broadcast list
     * nickname
     */
    public static final String NOT_PART_OF_BCAST_LIST = "%s not part of %s\n";
    /**
     * This is shown if there are no messages to be shown as a result of any of
     * the read messages commands
     */
    public static final String NO_MESSAGES = "No messages\n";
    /**
     * This is shown for search commands when they show no users that match the
     * search criteria
     */
    public static final String NO_RESULTS_FOUND = "Your search returned no results\n";
    /**
     * This is shown to get the password while login
     */
    public static final String PASSWORD_PROMPT = "Enter password:\n";

    /**
     * This is shown if another user with the same nickname already exists
     */
    public static final String SOMETHING_EXISTS = "Something with the same nickname already exists\n";
    /**
     * This is shown as part of various add operations that may be performed as
     * part of commands
     */
    public static final String SUCCESSFULLY_ADDED = "Successfully added\n";
    /**
     * This is shown if the user provides valid credentials
     */
    public static final String SUCCESSFULLY_LOGGED_IN = "Successfully logged in\n";
    /**
     * This is shown after a logout: command is issued by a logged in user
     */
    public static final String SUCCESSFULLY_LOGGED_OUT = "Logged out\n";
    /**
     * This is shown as part of various removal operations that may be performed
     * as part of commands
     */
    public static final String SUCCESSFULLY_REMOVED = "Successfully removed\n";

    /**
     * This is the usage string to be shown if the number of command line
     * arguments is not exactly one
     */
    public static final String USAGE = "Usage: java WhatsApp <path to the pre-population file>\n";
    /**
     * This is used as a template while showing user information for search
     * commands
     */
    public static final String USER_DISPLAY_FOR_SEARCH = "Last name: %s First name: %s Nickname: %s Friend: %s\n";

    private Config()
    {
        allUsers = new ArrayList<>();
        consoleInput = new Scanner(System.in);
        consoleOutput = System.out;
    }

    /**
     * This static method is used to return the only instance existing for the
     * Config class
     *
     * @return the instance of Config
     */
    public static Config getInstance()
    {
        if (instance == null)
        {
            instance = new Config();
        }

        return instance;
    }

    /**
     * returns the currently logged in user object
     *
     * @return the currently logged in user object
     */
    public User getCurrentUser()
    {
        return currentUser;
    }

    /**
     * sets the currently logged in user object. Use this in the
     * doLogin/doLogout methods of the CommandProcessor class
     *
     * @param currentUser the currently logged in user object. set this to null
     * while logging out
     */
    public void setCurrentUser(User currentUser)
    {
        this.currentUser = currentUser;
    }

    /**
     * returns the list of the global contacts
     *
     * @return the list of all users in WhatsApp
     */
    public List<User> getAllUsers()
    {
        return allUsers;
    }

    /**
     * sets the list of global users
     *
     * @param allUsers the list of all users in WhatsApp
     */
    public void setAllUsers(List<User> allUsers)
    {
        this.allUsers = allUsers;
    }

    /**
     * returns the only way you can obtain input from the keyboard (its a
     * Scanner)
     *
     * @return the Scanner which you should use to get input from the keyboard
     */
    public Scanner getConsoleInput()
    {
        return consoleInput;
    }

    /**
     * a setter for the Scanner object that should be used to obtain input from
     * the keyboard
     *
     * @param consoleInput the Scanner to be used to obtain input
     */
    public void setConsoleInput(Scanner consoleInput)
    {
        this.consoleInput = consoleInput;
    }

    /**
     * returns the only way you can write output to screen (its a PrintStream)
     *
     * @return the PrintStream which you should use to write output to screen
     */
    public PrintStream getConsoleOutput()
    {
        return consoleOutput;
    }

    /**
     * a setter for the PrintStream object that should be used to write to the
     * screen
     *
     * @param consoleOutput the PrintStream to be used to write the screen
     */
    public void setConsoleOutput(PrintStream consoleOutput)
    {
        this.consoleOutput = consoleOutput;
    }

}
