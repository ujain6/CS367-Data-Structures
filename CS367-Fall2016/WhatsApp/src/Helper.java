
///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  WhatsApp.java
// File:             Helper.java
// Semester:         CS367, Spring 2016
//
// Author:           Utkarsh Jain, ujain6@wisc.edu
// CS Login:         utkarsh
// Lecturer's Name:  Jim Skrentny
//
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * A few helper static methods
 *
 * @author jmishra
 */
public class Helper {

	/**
	 * Given a nickname as a String return the User object from the List of
	 * Users
	 *
	 * @param users
	 *            the list of users from which a user will be returned
	 * @param nickname
	 *            the nickname of the user to be returned
	 * @return the User object whose nickname is nickname and this object is
	 *         searched from the users List
	 */
	public static User getUserFromNickname(List<User> users, String nickname) {

		Iterator<User> userIterator = users.iterator();

		while (userIterator.hasNext()) {
			User user = userIterator.next();

			if (user.getNickname().equals(nickname)) {
				return user;
			}

		}
		return null;

	}

	/**
	 * Given a nickname as a String return the BroadcastList from the List of
	 * BroadcastLists
	 *
	 * @param lists
	 *            the list of lists from which one list object is to be returned
	 * @param nickname
	 *            the nickname of the broadcast list to be returned
	 * @return the BroadcastList object whose nickname is nickname. This object
	 *         is searched from the lists List
	 */
	public static BroadcastList getBroadcastListFromNickname(
			List<BroadcastList> lists, String nickname) {

		Iterator<BroadcastList> BroadcastListIterator = lists.iterator();

		while (BroadcastListIterator.hasNext()) {
			BroadcastList bcast = BroadcastListIterator.next();

			if (bcast.getNickname().equals(nickname)) {
				return bcast;
			}

		}
		return null;
	}

	/**
	 * Given a nickname determine whether a User exists globally with that
	 * nickname
	 *
	 * @param nickname
	 *            the nickname to check for
	 * @return true if a user exists globally with the provided nickname
	 */
	public static boolean isExistingGlobalContact(String nickname) {

		// Iterator over the list containing all users
		Iterator<User> isExistingGlobalContact = Config.getInstance()
				.getAllUsers().iterator();

		while (isExistingGlobalContact.hasNext()) {

			User user = isExistingGlobalContact.next();
			if (user.getNickname().equals(nickname)) {

				return true;
			}
		}
		return false;

	}

	/**
	 * Populate data from the file with the given path
	 *
	 * @param filename
	 *            the path to the input file
	 * @throws FileNotFoundException
	 *             this is thrown if the file is not found
	 * @throws IOException
	 *             this is thrown if some error occurs while reading the input
	 *             file
	 * @throws WhatsAppRuntimeException
	 *             this is thrown if any invalid data is used to construct any
	 *             of the WhatsApp objects or a line is encountered that does
	 *             not begin with any of the four words mentioned in the
	 *             specification
	 * @throws java.text.ParseException
	 *             this exception is thrown if some issue occurs while parsing
	 *             the date string
	 */
	public static void populateData(String filename)
			throws FileNotFoundException, IOException, WhatsAppRuntimeException,
			ParseException {
		Scanner scanner = new Scanner(new File(filename));
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] parts = line.split(",");

			switch (parts[0]) {
			case "user":
				User user = new User(parts[1], parts[2], parts[3], parts[4],
						new ArrayList<>(), new ArrayList<>(),
						new ArrayList<>());
				Config.getInstance().getAllUsers().add(user);

				break;
			case "flist":
				user = getUserFromNickname(Config.getInstance().getAllUsers(),
						parts[1]);
				for (int i = 2; i < parts.length; ++i) {
					user.getFriends().add(getUserFromNickname(
							Config.getInstance().getAllUsers(), parts[i]));
				}

				break;
			case "bcast":
				user = getUserFromNickname(Config.getInstance().getAllUsers(),
						parts[1]);
				BroadcastList newList = new BroadcastList(parts[3],
						new ArrayList<>());
				for (int i = 4; i < parts.length; ++i) {
					newList.getMembers().add(parts[i]);
				}
				user.getBroadcastLists().add(newList);

				break;
			case "message":
				SimpleDateFormat sdf = new SimpleDateFormat(
						"MM/dd/yyyy HH:mm:ss");

				if ((getUserFromNickname(Config.getInstance().getAllUsers(),
						parts[1])).isBroadcastList(parts[2])) {
					Message sentMessage = new Message(parts[1], null, parts[2],
							sdf.parse(parts[3]),
							parts[4].substring(1, parts[4].length() - 1), true);

					getUserFromNickname(Config.getInstance().getAllUsers(),
							parts[1]).getMessages().add(sentMessage);
					Iterator<String> memberIterator = getBroadcastListFromNickname(
							getUserFromNickname(
									Config.getInstance().getAllUsers(),
									parts[1]).getBroadcastLists(),
							parts[2]).getMembers().iterator();
					for (int i = 5; i < parts.length; ++i) {
						String member = memberIterator.next();
						Message receivedMessage = new Message(parts[1], member,
								null, sdf.parse(parts[3]),
								parts[4].substring(1, parts[4].length() - 1),
								parts[i].equals("read"));
						getUserFromNickname(Config.getInstance().getAllUsers(),
								member).getMessages().add(receivedMessage);
					}
				} else {
					Message sentMessage = new Message(parts[1], parts[2], null,
							sdf.parse(parts[3]),
							parts[4].substring(1, parts[4].length() - 1), true);
					getUserFromNickname(Config.getInstance().getAllUsers(),
							parts[1]).getMessages().add(sentMessage);
					Message receivedMessage = new Message(parts[1], parts[2],
							null, sdf.parse(parts[3]),
							parts[4].substring(1, parts[4].length() - 1),
							parts[5].equals("read"));
					getUserFromNickname(Config.getInstance().getAllUsers(),
							parts[2]).getMessages().add(receivedMessage);
				}

				break;
			default:
				throw new WhatsAppRuntimeException();
			}
		}
	}

}