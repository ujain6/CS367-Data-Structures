///////////////////////////////////////////////////////////////////////////////
//   
// Main Class File:  Game.java
// File:             Player.java
// Semester:         CS367, Spring 2016
//
// Author:           Pranav Mehendiratta
// Email:            mehendiratta@wisc.edu
// CS Login:         mehendiratta
// Lecturer's Name:  Jim Skrentny
//
///////////////////////////////////////////////////////////////////////////////
// Pair Partner:     Utkarsh Jain
// Email:            ujain6@wisc.edu
// CS Login:         utkarsh
// Lecturer's Name:  Jim Skrentny
//
import java.util.ArrayList;

/**
 * 
 * This class stores information about the player like name, budget, spycams,
 * location, etc.
 * 
 */
public class Player extends Object {

	private String playerName;
	private int budget;
	private int spycams;
	private GraphNode location;
	private ArrayList<String> spycamNames;

	/**
	 * Constructs a new Player object
	 * 
	 * @param name		contains the name of the player
	 * @param budget	contains the budget that player has
	 * @param spycams	contains the number of spycams the player has
	 * @param startnode	contains the reference to GraphNode(startnode) object
	 */
	public Player(String name, int budget, int spycams, GraphNode startnode){
		
		// throws IllegalArgumentException if the name or startNode object
		// is null
		if(name == null || startnode == null)
			throw new IllegalArgumentException();
		
		this.playerName = name;
		this.budget = budget;
		this.spycams = spycams;
		this.location = startnode;
		this.spycamNames = new ArrayList<String>();
	}

	/**
	 * Decreases the player budget by a specific amount
	 * 
	 * @param dec	integer number by which to decrease player budget
	 */
	public void decreaseBudget(int dec) {
		this.budget -= dec;
	}

	/**
	 * Drops a spycam to a GraphNode
	 * 
	 * @return	true if the spycam is successfully dropped at the current 
	 * 			location
	 * 			false if the spycam cannot be dropped at the current location	
	 */	
	public boolean dropSpycam(){	
		
		//If there are no remaining spycams
		if(spycams == 0)
			System.out.println("Not enough spycams");
		
		else if(location.getSpycam())
			System.out.println("Already a Spy Cam there");
		
		//If there is no spycam at the player's current location, spycam is 
		//dropped there.
		else if(!location.getSpycam()){
			location.setSpycam(true);
			spycamNames.add(location.getNodeName());
			
			//decrement the remaining spycam count
			spycams--;
			
			//to be corrected
			System.out.println("Dropped a Spy Cam at " 
					+ location.getNodeName());

			return true;
		}	
		return false;
	}

	/**
	 * Getter method to retrieve the player's budget
	 * 
	 * @return	player's budget
	 */
	public int getBudget(){
		return this.budget;
	}

	/**
	 * Getter method to retrieve the player's location
	 * 
	 * @return	returns the node where the player is currently located.
	 */
	public GraphNode getLocation(){
		return location;
	}

	/**
	 * Getter method to retrieve location name for player's location
	 * 
	 * @return	location name for player's current location
	 */
	public String getLocationName(){
		return this.location.getNodeName();
	}

	/**
	 * Getter method to retrieve player's name
	 * 
	 * @return	player's name
	 */
	public String getName(){
		return this.playerName;
	}

	/**
	 * 
	 * Gets the Spycam back
	 * 
	 */
	public void getSpycamBack(boolean pickupSpyCam){
		
		//if true, spycam is picked up. Otherwise false means there was no spy
		//cam
		if(pickupSpyCam)
			//increment the number of spycams remaining
			spycams++;
	}

	/**
	 * Getter method to retrieve the number of spycams left
	 * 
	 * @return	number of spycams remaining
	 */
	public int 	getSpycams() {
		return this.spycams;
	}

	/**
	 * moves player to a GraphNode
	 * 
	 * @param name - Neighboring node to move to
	 * @return	true if successfully moves to the GraphNode
	 * 			false if cannot move to the GraphNode
	 */
	public boolean move(String name){
		
		// throws IllegalArgumentException if the name is null
		if(name == null)
			throw new IllegalArgumentException();

		boolean flag = false;
		try{
			int costRequired = this.location.getCostTo(name);

			// checking if the cost is to move is 1
			if(this.location.getCostTo(name) == 1){
				GraphNode toMoveTo = location.getNeighbor(name);
				location = toMoveTo;
				flag = true;
			}
			
			// checks if there is enough budget left to move to the location
			else if(this.budget >= costRequired){
				
				//Decrement budget by that amount
				decreaseBudget(costRequired);
				GraphNode toMoveTo = location.getNeighbor(name);
				location = toMoveTo;
				flag = true;
			}
			else{
				System.out.println("Not enough money cost is " + costRequired 
						+ " budget is " + this.budget); 
			}
		}catch(NotNeighborException e){
			System.out.println(name + " is not a neighbor of your"
					+ " current location");
		}
		return flag;
	}

	/**
	 * 
	 * Picks up spycam from the given GraphNode
	 * 
	 * @param node - The node the player asked to remove a spy cam from
	 * @return	true if a spycam is retrieved, false otherwise
	 * 			
	 * 
	 */
	public boolean 	pickupSpycam(GraphNode node){
		
		// throws IllegalArgumentException if the node object is null
		if(node == null)
			throw new IllegalArgumentException();
		
		//Check to see if there is a spycam
		if(node.getSpycam()){
			
			//remove the spycam from that node
			getSpycamBack(node.getSpycam());
			
			//remove the spycam from the Player's list of spy cam names
			spycamNames.remove(node.getNodeName());
			node.setSpycam(false);
			return true;
		}
		return false;
	}

	/**
	 * Display the names of the locations where Spy Cams were dropped 
	 * (and are still there).
	 * 
	 */
	public void printSpyCamLocations(){
		for(String name: spycamNames)
			System.out.println("Spy cam at " + name);
	}
}
