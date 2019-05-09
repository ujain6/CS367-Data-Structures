
///////////////////////////////////////////////////////////////////////////////
//   
// Main Class File:  Game.java
// File:             SpyGraph.java
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
import java.lang.reflect.Array;
import java.util.*;

/**
 * Stores all vertexes as a list of GraphNodes. Provides necessary graph
 * operations as need by the SpyGame class.
 * 
 * @author strominger
 *
 */
public class SpyGraph implements Iterable<GraphNode> {

	/** DO NOT EDIT -- USE THIS LIST TO STORE ALL GRAPHNODES */
	private List<GraphNode> vlist;

	/**
	 * Initializes an empty list of GraphNode objects
	 */
	public SpyGraph() {

		vlist = new ArrayList<GraphNode>();
	}

	/**
	 * Add a vertex with this label to the list of vertexes. No duplicate 
	 * vertex names are allowed.
	 * 
	 * @param name
	 *            The name of the new GraphNode to create and add to the list.
	 */
	public void addGraphNode(String name) {

		// throws IllegalArgumentException if the name is null
		if(name == null)
			throw new IllegalArgumentException();
		
		// flag to check if the node with the provided name is already present
		// in the list or not
		boolean flag = false;
		for (GraphNode g : vlist) {
			if (g.getNodeName().equals(name))
				flag = true;
		}

		// Adds a vertex with the label name if not already present in the list
		if (!flag) {
			vlist.add(new GraphNode(name));
		}
	}

	/**
	 * Adds v2 as a neighbor of v1 and adds v1 as a neighbor of v2. Also sets
	 * the cost for each neighbor pair.
	 * 
	 * @param v1name
	 *            The name of the first vertex of this edge
	 * @param v2name
	 *            The name of second vertex of this edge
	 * @param cost
	 *            The cost of traveling to this edge
	 * @throws IllegalArgumentException
	 *             if the names are the same
	 */
	public void addEdge(String v1name, String v2name, int cost) 
			throws IllegalArgumentException {

		// throws IllegalArgumentException if the v1name or v2name is null
		// or v1 = v2 
		if (v1name == null || v2name == null || v1name.equals(v2name))
			throw new IllegalArgumentException();

		// Getting the corresponding graph nodes from their names
		GraphNode v1 = getNodeFromName(v1name);
		GraphNode v2 = getNodeFromName(v2name);

		if (v1 != null && v2 != null) {

			// Add v2 as neighbor of v1
			v1.addNeighbor(v2, cost);

			// Vice-versa, add v1 as neighbor of v2
			v2.addNeighbor(v1, cost);
		}

	}

	/**
	 * Return an iterator through all nodes in the SpyGraph
	 * 
	 * @return iterator through all nodes in alphabetical order.
	 */
	public Iterator<GraphNode> iterator() {
		return vlist.iterator();
	}

	/**
	 * Return Breadth First Search list of nodes on path from one Node to
	 * another. Uses a companion method.
	 * 
	 * @param start
	 *            First node in BFS traversal
	 * @param end
	 *            Last node (match node) in BFS traversal
	 * @return The BFS traversal from start to end node.
	 */
	public List<Neighbor> BFS(String start, String end) {
		
		
		// throws IllegalArgumentException if either of start or end is null
		if(start == null || end == null)
			throw new IllegalArgumentException();

		// Creating a HashMap to keep track of visited and unvisited nodes
		// (maps a GraphNode to its boolean value)
		HashMap<GraphNode, Boolean> visitedNodes = 
				new HashMap<GraphNode, Boolean>();

		// Creating a HashMap to keep track of parent nodes (maps child node to
		// parent node)
		HashMap<GraphNode, GraphNode> parents = 
				new HashMap<GraphNode, GraphNode>();

		// creating a HashMap with all nodes unvisited
		for (GraphNode g : this.vlist)
			visitedNodes.put(g, false);

		Queue<GraphNode> queue = new LinkedList<GraphNode>();

		// Getting the start node
		GraphNode node = getNodeFromName(start);

		// marking the first node of vlist as visited
		visitedNodes.put(node, true);

		// adding the first node to queue
		queue.add(node);

		while (!queue.isEmpty()) {
			GraphNode nodeToCheck = queue.remove();

			for (Neighbor n : nodeToCheck.getNeighbors()) {
				if (!visitedNodes.get(n.getNeighborNode())) {

					visitedNodes.put(n.getNeighborNode(), true);
					queue.add(n.getNeighborNode());

					parents.put(n.getNeighborNode(), nodeToCheck);

				}
			}
		}

		return Helper(parents, start, end);

	}


	/**
	 * Helper method to generate neighbor list from HashMap
	 * 
	 * @param list
	 *            contains key, value pair of child, parent
	 * @param start
	 *            start node name of the path
	 * @param stop
	 *            stop node name of the path
	 * @return list containing neighbors for the required path
	 */
	private List<Neighbor> Helper(HashMap<GraphNode, GraphNode> list,
			String start, String stop) {

		// boolean variable to find the start node
		boolean found = false;

		// List of type GraphNode to save all GraphNodes from start to end
		List<GraphNode> path = new ArrayList<GraphNode>();

		// List of neighbor type to return the path in
		List<Neighbor> neighbors = new ArrayList<Neighbor>();

		// Getting the end GraphNode
		GraphNode end = getNodeFromName(stop); 

		// Getting the start GraphNode
		GraphNode begin = getNodeFromName(start);

		// throwing Illegal Argument exception when node does not exists
		if(end == null || begin == null)
			throw new IllegalArgumentException();

		// adding the last node in the path if the path is
		path.add(end);

		// GraphNode variable to save a node
		GraphNode node;

		// finding the path (including start and end node)
		if(!start.equals(stop)){
			while (!found) {
				node = list.get(end);
				path.add(node);
				end = node;

				// throwing Illegal argument exception when node is unreachable
				if(end == null)
					throw new IllegalArgumentException();
				if (end.getNodeName().equals(start)) {
					found = true;
				}
			}

			// reversing the list to find path from start --> end
			Collections.reverse(path);

			for (int i = 0; i < path.size() - 1; i++) {
				for (Neighbor n : path.get(i).getNeighbors()) {
					if (n.getNeighborNode().getNodeName().equals
							(path.get(i + 1).getNodeName())) {
						neighbors.add(n);
					}
				}
			}
		}
		return neighbors;
	}

	/**
	 * @param name
	 *            Name corresponding to node to be returned
	 * @return GraphNode associated with name, null if no such node exists
	 */
	public GraphNode getNodeFromName(String name) {
		
		// throws IllegalArgumentException if name is null
		if(name == null)
			throw new IllegalArgumentException();
		
		for (GraphNode n : vlist) {
			if (n.getNodeName().equalsIgnoreCase(name))
				return n;
		}
		return null;
	}

	/**
	 * Return Depth First Search list of nodes on path from one Node to another.
	 * Uses a companion method.
	 * 
	 * @param start
	 *            First node in DFS traversal
	 * @param end
	 *            Last node (match node) in DFS traversal
	 * @return The DFS traversal from start to end node.
	 */
	public List<Neighbor> DFS(String start, String end) {

		// throws IllegalArgumentException if either of start or end is null
		if(start == null || end == null)
			throw new IllegalArgumentException();
		
		// Creating a HashMap to keep track of visited and unvisited nodes
		// (maps a GraphNode to its boolean value)
		HashMap<GraphNode, Boolean> visitedNodes =
				new HashMap<GraphNode, Boolean>();

		// Creating a HashMap to keep track of parent nodes (maps child node to
		// parent node)
		HashMap<GraphNode, GraphNode> parents =
				new HashMap<GraphNode, GraphNode>();

		// creating a HashMap with all nodes unvisited
		for (GraphNode g : this.vlist)
			visitedNodes.put(g, false);

		// Getting the startNode
		GraphNode begin = getNodeFromName(start);

		// Getting the endNode
		GraphNode stop = getNodeFromName(end);

		// calling the helper method to recursively find the path
		DFSHelper(parents, visitedNodes, begin, start, end);

		while (!parents.containsKey(stop) && !parents.containsValue(begin)) {

			if (!parents.containsKey(stop)) {
				for (GraphNode g : this.vlist) {
					if (!visitedNodes.get(g)) {
						DFSHelper(parents, visitedNodes, begin, start, end);
					}
				}
			}
		}

		return Helper(parents, start, end);
	}

	/**
	 * 
	 * Helper/Companion method that goes through all the nodes from staring node
	 * to create a hash map with child as key and parent as value
	 * 
	 */
	private void DFSHelper(HashMap<GraphNode, GraphNode> list, 
			HashMap<GraphNode, Boolean> visitedNodes,
			GraphNode nodeToCheck, String start, String end) {

		// marking the node as visited
		visitedNodes.put(nodeToCheck, true);

		for (Neighbor n : nodeToCheck.getNeighbors()) {

			// checking the neighbor only if it is not visited
			if (!visitedNodes.get(n.getNeighborNode())) {
				DFSHelper(list, visitedNodes, n.getNeighborNode(), start, end);
				list.put(n.getNeighborNode(), nodeToCheck);
			}
		}
	}

	/**
	 * OPTIONAL: Students are not required to implement Dijkstra's ALGORITHM
	 *
	 * Return Dijkstra's shortest path list of nodes on path from one Node to
	 * another.
	 * 
	 * @param start
	 *            First node in path
	 * @param end
	 *            Last node (match node) in path
	 * @return The shortest cost path from start to end node.
	 */
	public List<Neighbor> Dijkstra(String start, String end) {

		// returns an empty list
		return new ArrayList<Neighbor>();
	}

	/**
	 * DO NOT EDIT THIS METHOD
	 * 
	 * @return a random node from this graph
	 */
	public GraphNode getRandomNode() {
		if (vlist.size() <= 0) {
			System.out.println("Must have nodes in the graph before randomly choosing one.");
			return null;
		}
		int randomNodeIndex = Game.RNG.nextInt(vlist.size());
		return vlist.get(randomNodeIndex);
	}

}
