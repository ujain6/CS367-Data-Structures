import java.io.File;
import java.io.IOException;
import java.text.ParseException;

/**
 * The main class.It's a minimal class which hosts only the main method. Most of
 * the major work is delegated to the CommandProcessor and Helper classes
 *
 * @author jmishra
 */
public class WhatsApp
{

    /**
     * @param args the command line arguments. args[1] must be the path to the
     * prepopulation file. Print the USAGE message if exactly one command line
     * argument is not supplied. Print FILE_DOES_NOT_EXIST if the file path
     * given by the user on the command line does not exist. Prints EXITING and
     * exits when the exit: command is encountered
     */
    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            Config.getInstance().getConsoleOutput().
                    printf(Config.USAGE);
            System.exit(1);
        }
        if (!(new File(args[0])).exists())
        {
            Config.getInstance().getConsoleOutput().
                    printf(Config.FILE_DOES_NOT_EXIST);
            System.exit(1);
        }
        // try to populate data from the input file here
        try
        {
            Helper.populateData(args[0]);
        } catch (IOException ex)
        {
            Config.getInstance().getConsoleOutput().
                    printf(Config.ERROR_IO);
            System.exit(1);
        } catch (WhatsAppRuntimeException | ParseException ex)
        {
            Config.getInstance().getConsoleOutput().printf(ex.getMessage());
            System.exit(1);
        }
        
        String command = "";
        // do this till exit is entered as a command
        while (!command.equals("exit:"))
        {
            boolean successfulLogin = false;
            // do this till a successful login
            while (!successfulLogin)
            {
                try
                {
                    CommandProcessor.doLogin();
                } catch (WhatsAppException ex)
                {
                    Config.getInstance().getConsoleOutput().printf(ex.
                            getMessage());
                    continue;
                }
                successfulLogin = true;
            }
            // run this loop till someone is still logged in
            while (Config.getInstance().getCurrentUser() != null)
            {
                Config.getInstance().getConsoleOutput().
                        printf(Config.ENTER_COMMAND);
                command = Config.getInstance().getConsoleInput().
                        nextLine();
                if (!command.equals("exit:"))
                {
                    CommandProcessor.processCommand(command);
                } else {
                    Config.getInstance().setCurrentUser(null);
                }
            }
        }
        Config.getInstance().getConsoleOutput().printf(Config.EXITING);
    }

}
