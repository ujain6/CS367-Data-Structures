
///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  SetTesterMain.java
// File:             BSTreeSetTester.java
// Semester:         (CS367) Spring 2016
//
// Author:           Utkarsh Jain, ujain6@wisc.edu
// CS Login:         utkarsh
// Lecturer's Name:  Jim Skrentny
//
//
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * SetTesterADT implementation using a Binary Search Tree (BST) as the data
 * structure.
 *
 * <p>
 * The BST rebalances if a specified threshold is exceeded (explained below). If
 * rebalanceThreshold is non-positive (>=0) then the BST DOES NOT rebalance. It
 * is a basic BStree. If the rebalanceThreshold is positive then the BST does
 * rebalance. It is a BSTreeB where the last B means the tree is balanced.
 * </p>
 *
 * <p>
 * Rebalancing is triggered if the absolute value of the balancedFfactor in any
 * BSTNode is >= to the rebalanceThreshold in its BSTreeSetTester. Rebalancing
 * requires the BST to be completely rebuilt.
 * </p>
 *
 * @author CS367
 */
public class BSTreeSetTester<K extends Comparable<K>> implements SetTesterADT<K> {

	/** Root of this tree */
	BSTNode<K> root;

	/** Number of items in this tree */
	int numKeys;

	/**
	 * rebalanceThreshold >= 0 DOES NOT REBALANCE (BSTree).<br>
	 * rebalanceThreshold <0 rebalances the tree (BSTreeB).
	 */
	int rebalanceThreshold;

	/**
	 * True iff tree is balanced, i.e., if rebalanceThreshold is NOT exceeded by
	 * absolute value of balanceFactor in any of the tree's BSTnodes.Note if
	 * rebalanceThreshold is non-positive, isBalanced must be true.
	 */
	boolean isBalanced;

	/**
	 * Constructs an empty BSTreeSetTester with a given rebalanceThreshold.
	 *
	 * @param rbt
	 *            the rebalance threshold
	 */
	public BSTreeSetTester(int rbt) {

		root = null;
		rebalanceThreshold = rbt;
		numKeys = 0;
		isBalanced = true;

	}

	/**
	 * Add node to binary search tree. Remember to update the height and
	 * balancedFactor of each node. Also rebalance as needed based on
	 * rebalanceThreshold.
	 *
	 * @param key
	 *            the key to add into the BST
	 * @throws IllegalArgumentException
	 *             if the key is null
	 * @throws DuplicateKeyException
	 *             if the key is a duplicate
	 */
	public void add(K key) {

		if (key == null) {

			throw new IllegalArgumentException();
		}

		// Calls the recursive method
		root = add(root, key);

		// Increment the number of nodes
		numKeys++;

		// If the rebalnce threshold is positive, the tree is not balanced and
		// the rebalance function is called
		if (!isBalanced && rebalanceThreshold > 0) {
			rebalance();
		}

	}

	/**
	 * Companion method for the add operation
	 * 
	 */
	public BSTNode<K> add(BSTNode<K> n, K key) throws DuplicateKeyException {
		if (n == null) {
			return new BSTNode<K>(key);
		}

		// For duplicate key

		if (key.compareTo(n.getKey()) == 0) {
			throw new DuplicateKeyException();

		}

		// If the value is less than the root
		else if (key.compareTo(n.getKey()) < 0) {
			// add key to the left subtree
			n.setLeftChild(add(n.getLeftChild(), key));

			// Updating the height and balance factor of each node

			calculateHeightAndBalanceFactor(n);
			return n;

		}

		// If the value is more than the root
		else {
			// add key to the right subtree
			n.setRightChild(add(n.getRightChild(), key));

			// Updating the height and balance factor of each node

			calculateHeightAndBalanceFactor(n);
			return n;

		}

	}

	/**
	 * Rebalances the tree by: 1. Copying all keys in the BST in sorted order
	 * into an array. Hint: Use your BSTIterator. 2. Rebuilding the tree from
	 * the sorted array of keys.
	 */
	public void rebalance() {
		K[] keys = (K[]) new Comparable[numKeys];

		// Rebalancing is triggered if the absolute value of the balancedFfactor
		// in any
		// BSTNode is to the rebalanceThreshold in its BSTreeSetTester.
		// Rebalancing requires the BST to be completely rebuilt.

		// Inititalize an iterator at the root
		BSTIterator<K> itr = new BSTIterator(root);
		int i = 0;
		while (itr.hasNext()) {

			keys[i] = itr.next();
			i++;

		}

		// Build the tree again from the array formed by the while loop
		root = sortedArrayToBST(keys, 0, numKeys - 1);

		isBalanced = true;

	}

	/**
	 * Recursively rebuilds a binary search tree from a sorted array.
	 * Reconstruct the tree in a way similar to how binary search would explore
	 * elements in the sorted array. The middle value in the sorted array will
	 * become the root, the middles of the two remaining halves become the left
	 * and right children of the root, and so on. This can be done recursively.
	 * Remember to update the height and balanceFactor of each node.
	 *
	 * @param keys
	 *            the sorted array of keys
	 * @param start
	 *            the first index of the part of the array used
	 * @param stop
	 *            the last index of the part of the array used
	 * @return root of the new balanced binary search tree
	 */
	private BSTNode<K> sortedArrayToBST(K[] keys, int start, int stop) {

		if (start > stop)
			return null;

		int mid = (start + stop) / 2;

		// The middle value in the array becomes the node
		BSTNode<K> newRoot = new BSTNode<K>(keys[mid]);

		// Call the fucntion recursively on the first half of the array
		newRoot.setLeftChild(sortedArrayToBST(keys, start, mid - 1));

		// Call the function recursively on the second half of the array
		newRoot.setRightChild(sortedArrayToBST(keys, mid + 1, stop));

		// Updating the height and balance factor of each node
		calculateHeightAndBalanceFactor(newRoot);

		return newRoot;
	}

	/**
	 * Returns true iff the key is in the binary search tree.
	 *
	 * @param key
	 *            the key to search
	 * @return true if the key is in the binary search tree
	 * @throws IllegalArgumentException
	 *             if key is null
	 */
	public boolean contains(K key) {
		if (key == null) {
			throw new IllegalArgumentException();
		}

		return contains(root, key);
	}

	/**
	 * Companion method for the contains menthod
	 * 
	 */
	private boolean contains(BSTNode<K> n, K key) {

		if (n == null) {
			return false;
		}

		// If contains
		if (key.compareTo(n.getKey()) == 0) {
			return true;
		}

		else if (key.compareTo(n.getKey()) < 0) {
			// key < this node's key; look in left subtree
			return contains(n.getLeftChild(), key);
		}

		else {
			// key > this node's key; look in right subtree
			return contains(n.getRightChild(), key);
		}

	}

	/**
	 * Returns the sorted list of keys in the tree that are in the specified
	 * range (inclusive of minValue, exclusive of maxValue). This can be done
	 * recursively.
	 *
	 * @param minValue
	 *            the minimum value of the desired range (inclusive)
	 * @param maxValue
	 *            the maximum value of the desired range (exclusive)
	 * @return the sorted list of keys in the specified range
	 * @throws IllegalArgumentException
	 *             if either minValue or maxValue is null, or minValue is larger
	 *             than maxValue
	 */
	public List<K> subSet(K minValue, K maxValue) {
		List<K> bstList = new ArrayList<K>();

		if ((minValue.compareTo(maxValue) > 0) || minValue == null || maxValue == null) {
			throw new IllegalArgumentException();
		}

		subSet(root, bstList, minValue, maxValue);

		return bstList;
	}

	/**
	 * 
	 * Companion mehtod for the subSet method
	 * 
	 * 
	 */
	public void subSet(BSTNode<K> n, List<K> bstList, K minValue, K maxValue) {

		if (n == null)
			return;

		// If more than the root, the function is called recursively on the left
		// subtree
		else if (n.getKey().compareTo(minValue) < 0)
			subSet(n.getRightChild(), bstList, maxValue, minValue);

		// If less than the root, the function is called recursively on the left
		// subtree
		else if (n.getKey().compareTo(maxValue) > 0)
			subSet(n.getLeftChild(), bstList, minValue, maxValue);

		// If it matches the range, it gets added to the list
		else if (n.getKey().compareTo(minValue) >= 0 && n.getKey().compareTo(maxValue) < 0) {
			subSet(n.getLeftChild(), bstList, minValue, maxValue);
			bstList.add(n.getKey());
			subSet(n.getRightChild(), bstList, minValue, maxValue);
		}
	}

	public void calculateHeightAndBalanceFactor(BSTNode<K> node) {

		if (node.getLeftChild() == null && node.getRightChild() == null) {
			// Height of a leaf node is 1
			node.setHeight(1);
			node.setBalanceFactor(0);
		}
		// If left child is present and right child is null
		else if (node.getLeftChild() != null && node.getRightChild() == null) {
			node.setHeight(1 + node.getLeftChild().getHeight());
			node.setBalanceFactor(node.getLeftChild().getHeight());

			// Checking if the tree is balanced or not
			//Code is causing errors
			// if (Math.abs(node.getBalanceFactor()) > rebalanceThreshold &&
			// rebalanceThreshold > 0 && isBalanced)
			// isBalanced = false;
		}

		// If left child is null and right child is present
		else if (node.getLeftChild() == null && node.getRightChild() != null) {
			node.setHeight(1 + node.getRightChild().getHeight());
			node.setBalanceFactor(-node.getRightChild().getHeight());
			
			/**
			 * code is causing errors
			 */
			// Checking if the tree is balanced or not
			// if (Math.abs(node.getBalanceFactor()) > rebalanceThreshold &&
			// rebalanceThreshold > 0 && isBalanced)
			// isBalanced = false;
		}

		else {
			node.setHeight(1 + Math.max(node.getLeftChild().getHeight(), node.getRightChild().getHeight()));
			node.setBalanceFactor(node.getLeftChild().getHeight() - node.getRightChild().getHeight());

			// Checking if the tree is balanced or not

			// if (Math.abs(node.getBalanceFactor()) > rebalanceThreshold &&
			// rebalanceThreshold > 0 && isBalanced)
			// isBalanced = false;
		}

	}

	/**
	 * Return an iterator for the binary search tree.
	 * 
	 * @return the iterator
	 */
	public Iterator<K> iterator() {

		return new BSTIterator<K>(root);
	}

	/**
	 * Clears the tree, i.e., removes all the keys in the tree.
	 */
	public void clear() {
		root = null;
		numKeys = 0;
		isBalanced = true;

	}

	/**
	 * Returns the number of keys in the tree.
	 *
	 * @return the number of keys
	 */
	public int size() {
		return numKeys;
	}

	/**
	 * Displays the top maxNumLevels of the tree. DO NOT CHANGE IT!
	 *
	 * @param maxDisplayLevels
	 *            from the top of the BST that will be displayed
	 */
	public void displayTree(int maxDisplayLevels) {
		if (rebalanceThreshold > 0) {
			System.out.println("---------------------------" + "BSTreeBSet Display-------------------------------");
		} else {
			System.out.println("---------------------------" + "BSTreeSet Display--------------------------------");
		}
		displayTreeHelper(root, 0, maxDisplayLevels);
	}

	private void displayTreeHelper(BSTNode<K> n, int curDepth, int maxDisplayLevels) {
		if (maxDisplayLevels <= curDepth)
			return;
		if (n == null)
			return;
		for (int i = 0; i < curDepth; i++) {
			System.out.print("|--");
		}
		System.out.println(n.getKey() + "[" + n.getHeight() + "]{" + n.getBalanceFactor() + "}");
		displayTreeHelper(n.getLeftChild(), curDepth + 1, maxDisplayLevels);
		displayTreeHelper(n.getRightChild(), curDepth + 1, maxDisplayLevels);
	}

}
