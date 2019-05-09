
///////////////////////////////////////////////////////////////////////////////
//                  
// Main Class File:  WhatsApp.java
// File:             CommandProcessor.java
// Semester:         CS367, Spring 2016
//
// Author:           Utkarsh Jain, ujain6@wisc.edu
// CS Login:         utkarsh
// Lecturer's Name:  Jim Skrentny
//
import java.util.Date;
import java.util.Iterator;

/**
 * The most important class. This processes all the commands issued by the users
 *
 * @author jmishra
 */
public class CommandProcessor {

	// session added for saving some typing overhead and slight performance
	// benefit
	private static final Config CONFIG = Config.getInstance();

	/**
	 * A method to do login. Should show LOGIN_PROMPT for the nickname,
	 * PASSWORD_PROMPT for the password. Says SUCCESSFULLY_LOGGED_IN is
	 * successfully logs in someone. Must set the logged in user in the Config
	 * instance here
	 *
	 * @throws WhatsAppException
	 *             if the credentials supplied by the user are invalid, throw
	 *             this exception with INVALID_CREDENTIALS as the message
	 */

	public static void doLogin() throws WhatsAppException {
		CONFIG.getConsoleOutput().printf(Config.LOGIN_PROMPT);
		String nickname = CONFIG.getConsoleInput().nextLine();
		CONFIG.getConsoleOutput().printf(Config.PASSWORD_PROMPT);
		String password = CONFIG.getConsoleInput().nextLine();

		Iterator<User> userIterator = CONFIG.getAllUsers().iterator();
		while (userIterator.hasNext()) {
			User user = userIterator.next();
			if (user.getNickname().equals(nickname)
					&& user.getPassword().equals(password)) {
				CONFIG.setCurrentUser(user);
				CONFIG.getConsoleOutput().printf(Config.SUCCESSFULLY_LOGGED_IN);
				return;
			}

		}
		throw new WhatsAppException(String.format(Config.INVALID_CREDENTIALS));
	}

	/**
	 * A method to logout the user. Should print SUCCESSFULLY_LOGGED_OUT when
	 * done.
	 */
	public static void doLogout() {

		CONFIG.setCurrentUser(null);
		CONFIG.getConsoleOutput().printf(Config.SUCCESSFULLY_LOGGED_OUT);

	}

	/**
	 * A method to send a message. Handles both one to one and broadcasts
	 * MESSAGE_SENT_SUCCESSFULLY if sent successfully.
	 *
	 * @param nickname
	 *            - can be a friend or broadcast list nickname
	 * @param message
	 *            - message to send
	 * @throws WhatsAppRuntimeException
	 *             simply pass this untouched from the constructor of the
	 *             Message class
	 * @throws WhatsAppException
	 *             throw this with one of CANT_SEND_YOURSELF,
	 *             NICKNAME_DOES_NOT_EXIST messages
	 */
	public static void sendMessage(String nickname, String message)
			throws WhatsAppRuntimeException, WhatsAppException {

		// Date object to record the time at which the message was sent
		Date date = new Date();

		if (nickname
				.equals(Config.getInstance().getCurrentUser().getNickname())) {
			throw new WhatsAppException(Config.CANT_SEND_YOURSELF);
		}

		// if the nickname is of a broadcast list, it sends message to all the
		// members of the broadcast list.
		if (Config.getInstance().getCurrentUser().isBroadcastList(nickname)) {

			// Created a message object and instantiated using the proper values
			Message sendMessageSender = new Message(
					Config.getInstance().getCurrentUser().getNickname(), null,
					nickname, date, message, true);

			// Add the message object to the message list of the SENDER
			Config.getInstance().getCurrentUser().getMessages()
			.add(sendMessageSender);

			// Iterator over all the list of members in that particular
			// broadcast
			// list
			Iterator<String> itr = Helper
					.getBroadcastListFromNickname(Config.getInstance()
							.getCurrentUser().getBroadcastLists(), nickname)
					.getMembers().iterator();

			// Creates message objects for each member in the particular
			// broadcast list
			while (itr.hasNext()) {

				String name = itr.next();

				// get the user object whose nickname matches
				User bcastListMember = Helper.getUserFromNickname(
						Config.getInstance().getAllUsers(), name);

				// Create a message object for the receiver
				Message sendMessageBcastMembers = new Message(
						Config.getInstance().getCurrentUser().getNickname(),
						name, null, date, message, false);

				// Add the message object to the message list of the RECEIVER
				bcastListMember.getMessages().add(sendMessageBcastMembers);

			}
			CONFIG.getConsoleOutput().printf(Config.MESSAGE_SENT_SUCCESSFULLY);

		}

		// if the nickname matches the nickname of a friend
		else if (Config.getInstance().getCurrentUser().isFriend(nickname)) {

			User friend = Helper.getUserFromNickname(
					Config.getInstance().getCurrentUser().getFriends(),
					nickname);

			Message sendMessage = new Message(
					Config.getInstance().getCurrentUser().getNickname(),
					nickname, null, date, message, true);

			Message sendMessageReceiver = new Message(
					Config.getInstance().getCurrentUser().getNickname(),
					nickname, null, date, message, false);

			// Adds the message object to the message list of SENDER
			Config.getInstance().getCurrentUser().getMessages()
			.add(sendMessage);
			// Adds the message object to the message list of RECEIVER
			friend.getMessages().add(sendMessageReceiver);

			// Message sent confirmation
			CONFIG.getConsoleOutput().printf(Config.MESSAGE_SENT_SUCCESSFULLY);

		}

		else {
			throw new WhatsAppException(
					String.format(Config.NICKNAME_DOES_NOT_EXIST, nickname));
		}

	}

	/**
	 * Displays messages from the message list of the user logged in. Prints the
	 * messages in the format specified by MESSAGE_FORMAT. Says NO_MESSAGES if
	 * no messages can be displayed at the present time
	 *
	 * @param nickname
	 *            - send a null in this if you want to display messages related
	 *            to everyone. This can be a broadcast nickname also.
	 * @param enforceUnread
	 *            - send true if you want to display only unread messages.
	 */
	public static void readMessage(String nickname, boolean enforceUnread) {

		int numMessages = 0;

		// If there are no messages in the list
		if (Config.getInstance().getCurrentUser().getMessages().size() == 0)
			System.out.println(Config.NO_MESSAGES);

		else {

			//Iterator over the messages list of the current logged in user
			Iterator<Message> iterator = Config.getInstance().getCurrentUser()
					.getMessages().iterator();


			while (iterator.hasNext()) {

				//Create a message object and assign it to  the iterator
				Message message = iterator.next();

				//nickname is null when we need to show messages related to
				//everyone
				if (nickname == null) {

					//to show only unread messages
					if (enforceUnread) {

						//if isRead message returns false
						if (!message.isRead()) {

							Config.getInstance().getConsoleOutput()
							.printf(String.format(Config.MESSAGE_FORMAT,
									message.getFromNickname(),
									message.getToNickname(),
									message.getMessage(),
									message.getSentTime()));

							//set the read status to true, i.e., the 
							//message has been read
							message.setRead(true);

							numMessages++;
						}
					} else {
						String toNickname = message.getToNickname();

						if (toNickname == null)
							toNickname = message.getBroadcastNickname();


						Config.getInstance().getConsoleOutput()
						.printf(String.format(Config.MESSAGE_FORMAT,
								message.getFromNickname(), toNickname,
								message.getMessage(),
								message.getSentTime()));


						//set the read status to true, i.e., the 
						//message has been read
						message.setRead(true);



						numMessages++;
					}
				} 

				//To show messages assosiated with the nickname
				else if (message.getToNickname() != null
						|| message.getFromNickname() != null) {

					//for both sent and received messages. Checks if the 
					//fromNickname(received messages) OR toNickname(sent 
					//messages) matches or not.
					if (message.getFromNickname().equals(nickname)
							|| message.getToNickname().equals(nickname)) {

						//only unread messages
						if (enforceUnread) {
							if (!message.isRead()) {

								Config.getInstance().getConsoleOutput()
								.printf(String.format(
										Config.MESSAGE_FORMAT,
										message.getFromNickname(),
										message.getToNickname(),
										message.getMessage(),
										message.getSentTime()));

								//set the read status to true, i.e., the 
								//message has been read
								message.setRead(true);

								numMessages++;
							}
						} 
						//both read and unread messages
						else {
							String nameToPrint = message.getToNickname();
							if (nameToPrint == null)
								nameToPrint = message.getBroadcastNickname();


							Config.getInstance().getConsoleOutput()
							.printf(String.format(Config.MESSAGE_FORMAT,
									message.getFromNickname(),
									nameToPrint, message.getMessage(),
									message.getSentTime()));

							//set the read status to true, i.e., the 
							//message has been read
							message.setRead(true);
							numMessages++;
						}
					}
				} 
				else if (message.getBroadcastNickname() != null) {

					if (message.getBroadcastNickname().equals(nickname)) {

						//only unread messages
						if (enforceUnread) {
							if (!message.isRead()) {


								Config.getInstance().getConsoleOutput()
								.printf(String.format(
										Config.MESSAGE_FORMAT,
										message.getFromNickname(),
										message.getToNickname(),
										message.getMessage(),
										message.getSentTime()));

								//set the read status to true, i.e., the 
								//message has been read
								message.setRead(true);
								numMessages++;
							}
						} 
						else {

							String nameToPrint = message.getToNickname();
							if (nameToPrint == null)
								nameToPrint = message.getBroadcastNickname();


							Config.getInstance().getConsoleOutput()
							.printf(Config.MESSAGE_FORMAT,
									message.getFromNickname(), nameToPrint,
									message.getMessage(),
									message.getSentTime());

							//set the read status to true, i.e., the 
							//message has been read
							message.setRead(true);
							numMessages++;
						}
					}
				}
			}
		}

		//This shows no messages when there were no messages found assosiated
		//with the provided nickname. Doesn't mean that there are no messages
		//in the user's message list
		if (numMessages == 0)
			CONFIG.getConsoleOutput().printf(Config.NO_MESSAGES);


	}

	/**
	 * Method to do a user search. Does a case insensitive "contains" search on
	 * either first name or last name. Displays user information as specified by
	 * the USER_DISPLAY_FOR_SEARCH format. Says NO_RESULTS_FOUND is nothing
	 * found.
	 *
	 * @param word
	 *            - word to search for
	 * @param searchByFirstName
	 *            - true if searching for first name. false for last name
	 */
	public static void search(String word, boolean searchByFirstName) {

		if (searchByFirstName) {

			Iterator<User> search = Config.getInstance().getAllUsers()
					.iterator();

			// A counter that increments by one whenever a match is found
			int results = 0;

			while (search.hasNext()) {
				User friend = search.next();

				// checks if the FIRST NAME contains the "word"
				if (friend.getFirstName().contains(word)) {

					// increments by one as a match is found
					results++;

					// checks if the user found is a friend or not
					if (Config.getInstance().getCurrentUser()
							.isFriend(friend.getNickname())) {

						// is a friend
						CONFIG.getConsoleOutput().printf(String.format(
								Config.USER_DISPLAY_FOR_SEARCH,
								friend.getLastName(), friend.getFirstName(),
								friend.getNickname(), "Yes"));
					}

					else {
						// not a friend
						CONFIG.getConsoleOutput().printf(String.format(
								Config.USER_DISPLAY_FOR_SEARCH,
								friend.getLastName(), friend.getFirstName(),
								friend.getNickname(), "No"));
					}
				} // end of if that checks friend or not

			} // end of while loop

			// if the results equals 0, this means no match was found
			if (results == 0) {
				CONFIG.getConsoleOutput().print(Config.NO_RESULTS_FOUND);
			}

		}

		// search by last name
		else {
			Iterator<User> search = Config.getInstance().getAllUsers()
					.iterator();

			int results = 0;
			while (search.hasNext()) {
				User friend = search.next();

				if (friend.getLastName().contains(word)) {
					results++;
					if (Config.getInstance().getCurrentUser()
							.isFriend(friend.getNickname())) {
						CONFIG.getConsoleOutput().printf(String.format(
								Config.USER_DISPLAY_FOR_SEARCH,
								friend.getLastName(), friend.getFirstName(),
								friend.getNickname(), "Yes"));
					} else {
						CONFIG.getConsoleOutput().printf(String.format(
								Config.USER_DISPLAY_FOR_SEARCH,
								friend.getLastName(), friend.getFirstName(),
								friend.getNickname(), "No"));
					}

				} // end of main if

			} // end of while loop
			if (results == 0) {
				CONFIG.getConsoleOutput().print(Config.NO_RESULTS_FOUND);
			}
		}
	}

	/**
	 * Adds a new friend. Says SUCCESSFULLY_ADDED if added. Hint: use the
	 * addFriend method of the User class.
	 *
	 * @param nickname
	 *            - nickname of the user to add as a friend
	 * @throws WhatsAppException
	 *             simply pass the exception thrown from the addFriend method of
	 *             the User class
	 */
	public static void addFriend(String nickname) throws WhatsAppException {

		// calls the addFriend function of the User class
		Config.getInstance().getCurrentUser().addFriend(nickname);

	}

	/**
	 * removes an existing friend. Says NOT_A_FRIEND if not a friend to start
	 * with, SUCCESSFULLY_REMOVED if removed. Additionally removes the friend
	 * from any broadcast list she is a part of
	 *
	 * @param nickname
	 *            nickname of the user to remove from the friend list
	 * @throws WhatsAppException
	 *             simply pass the exception from the removeFriend method of the
	 *             User class
	 */
	public static void removeFriend(String nickname) throws WhatsAppException {

		Config.getInstance().getCurrentUser().removeFriend(nickname);
		Config.getInstance().getConsoleOutput().printf(Config.SUCCESSFULLY_REMOVED);

	}

	/**
	 * adds a friend to a broadcast list. Says SUCCESSFULLY_ADDED if added
	 *
	 * @param friendNickname
	 *            the nickname of the friend to add to the list
	 * @param bcastNickname
	 *            the nickname of the list to add the friend to
	 * @throws WhatsAppException
	 *             throws a new instance of this exception with one of
	 *             NOT_A_FRIEND (if friendNickname is not a friend),
	 *             BCAST_LIST_DOES_NOT_EXIST (if the broadcast list does not
	 *             exist), ALREADY_PRESENT (if the friend is already a member of
	 *             the list), CANT_ADD_YOURSELF_TO_BCAST (if attempting to add
	 *             the user to one of his own lists
	 */
	public static void addFriendToBcast(String friendNickname,
			String bcastNickname) throws WhatsAppException {

		// To check we're not adding ourself to the broadcast list
		if (friendNickname
				.equals(Config.getInstance().getCurrentUser().getNickname())) {
			throw new WhatsAppException(Config.CANT_ADD_YOURSELF_TO_BCAST);
		}

		// to check we're adding only a person whose a friend
		if (!Config.getInstance().getCurrentUser().isFriend(friendNickname)) {
			throw new WhatsAppException(Config.NOT_A_FRIEND);
		}

		// To check if the broadcast list even exists or not
		if (!Config.getInstance().getCurrentUser()
				.isBroadcastList(bcastNickname)) {
			throw new WhatsAppException(String
					.format(Config.BCAST_LIST_DOES_NOT_EXIST, bcastNickname));
		}

		// To check if the person we're adding is not already in the blist
		if (Config.getInstance().getCurrentUser()
				.isMemberOfBroadcastList(friendNickname, bcastNickname)) {
			throw new WhatsAppException(Config.ALREADY_PRESENT);
		}

		// If we pass all the tests above, get the broadcast list assosiated
		// with
		// the nickname given
		Helper.getBroadcastListFromNickname(
				Config.getInstance().getCurrentUser().getBroadcastLists(),
				bcastNickname).getMembers().add(friendNickname);

		CONFIG.getConsoleOutput().printf(Config.SUCCESSFULLY_ADDED);
	}

	/**
	 * removes a friend from a broadcast list. Says SUCCESSFULLY_REMOVED if
	 * removed
	 *
	 * @param friendNickname
	 *            the friend nickname to remove from the list
	 * @param bcastNickname
	 *            the nickname of the list from which to remove the friend
	 * @throws WhatsAppException
	 *             throw a new instance of this with one of these messages:
	 *             NOT_A_FRIEND (if friendNickname is not a friend),
	 *             BCAST_LIST_DOES_NOT_EXIST (if the broadcast list does not
	 *             exist), NOT_PART_OF_BCAST_LIST (if the friend is not a part
	 *             of the list)
	 */
	public static void removeFriendFromBcast(String friendNickname,
			String bcastNickname) throws WhatsAppException {

		// To check if the person is a friend or not
		if (!Config.getInstance().getCurrentUser().isFriend(friendNickname)) {
			throw new WhatsAppException(Config.NOT_A_FRIEND);
		}

		// To check if the broadcast list with the provided nickname exists or
		// not.
		if (!Config.getInstance().getCurrentUser()
				.isBroadcastList(bcastNickname)) {
			throw new WhatsAppException(String
					.format(Config.BCAST_LIST_DOES_NOT_EXIST, bcastNickname));
		}

		// To check if the person is part of that particular broadcast list or
		// not
		if (Config.getInstance().getCurrentUser().getBroadcastLists()
				.contains(bcastNickname)) {
			throw new WhatsAppException(Config.NOT_PART_OF_BCAST_LIST);
		}

		// If we pass all the tests above, removes the particular person from
		// the broadcast list.
		Helper.getBroadcastListFromNickname(
				Config.getInstance().getCurrentUser().getBroadcastLists(),
				bcastNickname).getMembers().remove(friendNickname);
		CONFIG.getConsoleOutput().printf(Config.SUCCESSFULLY_REMOVED);
	}

	/**
	 * A method to remove a broadcast list. Says BCAST_LIST_DOES_NOT_EXIST if
	 * there is no such list to begin with and SUCCESSFULLY_REMOVED if removed.
	 * Hint: use the removeBroadcastList method of the User class
	 *
	 * @param nickname
	 *            the nickname of the broadcast list which is to be removed from
	 *            the currently logged in user
	 * @throws WhatsAppException
	 *             Simply pass the exception returned from the
	 *             removeBroadcastList method of the User class
	 */
	public static void removeBroadcastcast(String nickname)
			throws WhatsAppException {

		if (Config.getInstance().getCurrentUser().isBroadcastList(nickname)) {

			// Calls the removeBroadcastList function of the User class
			Config.getInstance().getCurrentUser().removeBroadcastList(nickname);

			// Confirmation message
			CONFIG.getConsoleOutput().printf(Config.SUCCESSFULLY_REMOVED);
		}

		else {
			CONFIG.getConsoleOutput().printf(Config.BCAST_LIST_DOES_NOT_EXIST);

		}
		


	}

	/**
	 * Processes commands issued by the logged in user. Says INVALID_COMMAND for
	 * anything not conforming to the syntax. This basically uses the rest of
	 * the methods in this class. These methods throw either or both an instance
	 * of WhatsAppException/WhatsAppRuntimeException. You ought to catch such
	 * exceptions here and print the messages in them. Note that this method
	 * does not throw any exceptions. Handle all exceptions by catch them here!
	 *
	 * @param command
	 *            the command string issued by the user
	 */
	public static void processCommand(String command) {
		try {
			switch (command.split(":")[0]) {
			case "logout":
				doLogout();
				break;
			case "send message":
				String nickname = command.substring(command.indexOf(":") + 1,
						command.indexOf(",")).trim();
				String message = command.substring(command.indexOf("\"") + 1,
						command.trim().length() - 1);
				sendMessage(nickname, message);
				break;
			case "read messages unread from":
				nickname = command.substring(command.indexOf(":") + 1,
						command.trim().length()).trim();
				readMessage(nickname, true);
				break;
			case "read messages all from":
				nickname = command.substring(command.indexOf(":") + 1,
						command.trim().length()).trim();
				readMessage(nickname, false);
				break;
			case "read messages all":
				readMessage(null, false);
				break;
			case "read messages unread":
				readMessage(null, true);
				break;
			case "search fn":
				String word = command.substring(command.indexOf(":") + 1,
						command.trim().length()).trim();
				search(word, true);
				break;
			case "search ln":
				word = command.substring(command.indexOf(":") + 1,
						command.trim().length()).trim();
				search(word, false);
				break;
			case "add friend":
				nickname = command.substring(command.indexOf(":") + 1,
						command.trim().length()).trim();
				addFriend(nickname);
				break;
			case "remove friend":
				nickname = command.substring(command.indexOf(":") + 1,
						command.trim().length()).trim();
				removeFriend(nickname);
				break;
			case "add to bcast":
				String nickname0 = command.substring(command.indexOf(":") + 1,
						command.indexOf(",")).trim();
				String nickname1 = command.substring(command.indexOf(",") + 1,
						command.trim().length()).trim();
				addFriendToBcast(nickname0, nickname1);
				break;
			case "remove from bcast":
				nickname0 = command.substring(command.indexOf(":") + 1,
						command.indexOf(",")).trim();
				nickname1 = command.substring(command.indexOf(",") + 1,
						command.trim().length()).trim();
				removeFriendFromBcast(nickname0, nickname1);
				break;
			case "remove bcast":
				nickname = command.substring(command.indexOf(":") + 1,
						command.trim().length()).trim();
				removeBroadcastcast(nickname);
				break;
			default:
				CONFIG.getConsoleOutput().printf(Config.INVALID_COMMAND);
			}
		} catch (StringIndexOutOfBoundsException ex) {
			CONFIG.getConsoleOutput().printf(Config.INVALID_COMMAND);
		} catch (WhatsAppException | WhatsAppRuntimeException ex) {
			CONFIG.getConsoleOutput().printf(ex.getMessage());
		}
	}

}