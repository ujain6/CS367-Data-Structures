///////////////////////////////////////////////////////////////////////////////
//   
// Main Class File:  Game.java
// File:             Neighbor.java
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

/**
 * 
 * This class constructs/represents an edge between two neighbors. Holds the 
 * reference to neighbor node and cost required to reach that node. 
 * 
 */
public class Neighbor extends Object implements Comparable<Neighbor> {

	private GraphNode neighbor;
	private int cost;

	/**
	 * A neighbor is added to an existing GraphNode by creating an instance of
	 * Neighbor that stores the neighbor and the cost to reach that neighbor
	 * 
	 * @param cost - cost to reach this neighbor
	 * @param neighbor - the neighbor node being reached by this edge
	 * 
	 */
	public Neighbor(int cost, GraphNode neighbor) {
		
		// throws IllegalArgumentException if the neighbor object is null
		if(neighbor == null)
			throw new IllegalArgumentException();
		
		this.neighbor = neighbor;
		this.cost = cost;

	}

	/**
	 * Compares the Compares the node names of this node and the otherNode. 
	 * Returns the results of comparing this node's name to the otherNode's 
	 * name. Allows Lists of Neighbors to be sorted using built-in sort 
	 * methods.
	 * 
	 * @param otherNode - neighbor to be compared
	 * 
	 * @return an integer depending on the comparison of name of the GraphNodes
	 * 
	 */
	public int compareTo(Neighbor otherNode) {
		
		// throws IllegalArgumentException if the otherName object is null
		if(otherNode == null)
			throw new IllegalArgumentException();
		
		return neighbor.getNodeName().compareTo
				(otherNode.getNeighborNode().getNodeName());
	}

	/**
	 * 
	 * Returns the cost of traveling this edge to get to the Neighbor at the 
	 * other end of this edge
	 * 
	 * @return the cost of the edge to get to this neighbor
	 * 
	 */
	public int getCost() {
		return this.cost;
	}

	/**
	 * Returns the Neighbor(node) that is at the other end of "this" 
	 * node's edge.
	 * 
	 * @return the neighbor node itself.
	 * 
	 */
	public GraphNode getNeighborNode() {
		return this.neighbor;
	}

	/**
	 * 
	 * Returns a string representation of this Neighbor. The String that is 
	 * returned shows an arrow(with the cost in the middle) and then the 
	 * Neighbor node's name.
	 * 
	 * Overrides: toString in class java.lang.Object
	 * 
	 * @return A string with the cost and the destination node
	 */
	public String toString() {
		return "--" + this.cost + "-->" + this.neighbor.getNodeName();
	}
}
