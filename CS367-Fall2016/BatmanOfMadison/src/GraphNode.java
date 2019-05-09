
///////////////////////////////////////////////////////////////////////////////
//   
// Main Class File:  Game.java
// File:             GraphNode.java
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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * Represents a vertex(node) of the game space where the Player and the Spy can
 * be located.
 * 
 */
public class GraphNode extends Object implements Comparable<GraphNode> {

	// for Dijkstra's algorithm(not used)
	private static int NOT_NEIGHBOR;
	private String name;
	private boolean spycam;
	private ArrayList<Neighbor> neighbors;

	/**
	 * Constructs/represents a GraphNode object with a valid location. There 
	 * can be a player, a spycam, or a spy at any graph node.
	 * 
	 * @param nodeName
	 *            contains the name for the GraphNode
	 */
	public GraphNode(String name) {
		
		// throws IllegalArgumentException if the name is null
		if(name == null)
			throw new IllegalArgumentException();
		this.name = name;
		this.neighbors = new ArrayList<Neighbor>();
		this.spycam = false;
	}

	/**
	 * Adds the neighbor to list of neighbors and maintains sorted order of
	 * neighbors by neighbor name
	 * 
	 * @param neighbor
	 *            reference to neighbor GraphNode
	 * @param cost
	 *            cost required to move to that GraphNode
	 */
	public void addNeighbor(GraphNode neighbor, int cost) {
		
		// throws IllegalArgumentException if the neighbor object is null
		if(neighbor == null)
			throw new IllegalArgumentException();
		neighbors.add(new Neighbor(cost, neighbor));

		// Following "367 convention"
		Collections.sort(neighbors);
	}

	/**
	 * Return the results of comparing this node's name to the other node's 
	 * name. Allows collections of nodes to be sorted using the built-in 
	 * sort methods.
	 * 
	 * @param otherNode - reference to other GraphNode
	 * 
	 * @return the result of comapreTo on the node names only 
	 */
	public int compareTo(GraphNode otherNode) {
		
		// throws IllegalArgumentException if the otherNode is null
		if(otherNode == null)
			throw new IllegalArgumentException();
		
		return this.name.compareTo(otherNode.getNodeName());
	}

	/**
	 * Prints a list of neighbors of this GraphNode and the cost of the edge to
	 * them
	 */
	public void displayCostToEachNeighbor() {
		for (Neighbor neighbor : neighbors)
			System.out.println(neighbor.getCost() + " " + 
					neighbor.getNeighborNode().getNodeName());
	}

	/**
	 * returns the cost to a given neighbor
	 * 
	 * @param neighborName - name of the potential neighbor
	 * 
	 * @return cost to the neighborName
	 * 
	 * @throws NotNeighborException
	 *             if neighbor with the given name is not a neighbor
	 */
	public int getCostTo(String neighborName) throws NotNeighborException{
		
		// throws IllegalArgumentException if the neighborName is null
		if(neighborName == null)
			throw new IllegalArgumentException();

		// checks if their is a neighbor with given name
		for (Neighbor neighbor : neighbors) {
			if (neighbor.getNeighborNode().getNodeName().equals(neighborName)){
				return neighbor.getCost();
			}
		}
		throw new NotNeighborException();
	}

	/**
	 * 
	 * @param name - name of potential neighbor
	 * 
	 * @return the GraphNode associated with name that is a neighbor of the
	 *         current node
	 *         
	 * @throws NotNeighborException
	 *             if name is not a neighbor of the GraphNode
	 */
	public GraphNode getNeighbor(String name) throws NotNeighborException {
		
		// throws IllegalArgumentException if the name is null
		if(name == null)
			throw new IllegalArgumentException();
		
		for (Neighbor neighbor : neighbors) {
			if (neighbor.getNeighborNode().getNodeName().equals(name)) {
				return neighbor.getNeighborNode();
			}
		}
		throw new NotNeighborException();
	}

	/**
	 * Returns and iterator that can be used to find neighbors of this
	 * GraphNode.
	 * 
	 * @return an iterator of String node labels
	 */
	public Iterator<String> getNeighborNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (Neighbor neighbor : neighbors)
			names.add(neighbor.getNeighborNode().getNodeName());

		return names.iterator();
	}

	/**
	 * Returns a list of the neighbors of this GraphNode instance.
	 * 
	 * @return reference to the list object holding the neighbor objects
	 */
	public List<Neighbor> getNeighbors() {

		return neighbors;
	}

	/**
	 * Return the name of this GraphNode.
	 * 
	 * @return name of the GraphNode
	 */
	public String getNodeName() {
		return this.name;
	}

	/**
	 * Return the boolean value if there is a spycam at the current location
	 * 
	 * @return true if the GraphNode has a spycam
	 */
	public boolean getSpycam() {
		return this.spycam;
	}

	/**
	 * Returns true if this node name is a neighbor of current node.
	 * 
	 * @param neighborName
	 *            name of the neighbor to look for
	 * @return true if the node is an adjacent neighbor.
	 */
	public boolean isNeighbor(String neighborName) {
		
		// throws IllegalArgumentException if the neighborName is null
		if(neighborName == null)
			throw new IllegalArgumentException();
		
		for (Neighbor neighbor : neighbors) {
			if (neighbor.getNeighborNode().getNodeName().equals(neighborName)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Display's the node name followed by a list of neighbors to this node.
	 * 
	 */
	public void printNeighborNames() {
		for (Neighbor neighbor : neighbors)
			System.out.println(neighbor.getCost() + " " + 
					neighbor.getNeighborNode().getNodeName());
	}

	/**
	 * Sets whether the node has a spycam or not
	 * 
	 * @param cam
	 *            indicates whether the node now has a spycam
	 */
	public void setSpycam(boolean cam) {
		this.spycam = cam;
	}

	/**
	 * Returns the name of the node
	 */
	public String toString() {
		return this.name;
	}

}
