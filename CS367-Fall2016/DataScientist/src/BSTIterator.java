
///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  SetTesterMain.java
// File:             BSTIterator.java
// Semester:         (CS367) Spring 2016
//
// Author:           Utkarsh Jain, ujain6@wisc.edu
// CS Login:         utkarsh
// Lecturer's Name:  Jim Skrentny
//
//
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * The Iterator for Binary Search Tree (BST) that is built using Java's Stack
 * class. This iterator steps through the items BST using an INORDER traversal.
 *
 * @author CS367
 */
public class BSTIterator<K> implements Iterator<K> {

	/** Stack to track where the iterator is in the BST */
	Stack<BSTNode<K>> stack;

	/**
	 * Constructs the iterator so that it is initially at the smallest value in
	 * the set. Hint: Go to farthest left node and push each node onto the stack
	 * while stepping down the BST to get there.
	 *
	 * @param n
	 *            the root node of the BST
	 */
	public BSTIterator(BSTNode<K> n) {

		// Initialize the stack
		stack = new Stack<BSTNode<K>>();

		// Go down the BST until it reaches the smallest leaf node, i.e., the
		// height of the node should be 1
		while (n != null) {

			// push the node to the stack
			stack.push(n);

			// update the pointer n to point to the next left child
			n = n.getLeftChild();

		}

	}

	/**
	 * Returns true iff the iterator has more items.
	 *
	 * @return true iff the iterator has more items
	 */
	public boolean hasNext() {
		return stack.size() == 0;
	}

	/**
	 * Returns the next item in the iteration.
	 *
	 * @return the next item in the iteration
	 * @throws NoSuchElementException
	 *             if the iterator has no more items
	 */
	public K next() {

		if (!hasNext()) {

			throw new NoSuchElementException();

		}

		BSTNode<K> nodeToBeReturned = stack.pop();
		BSTNode<K> n = nodeToBeReturned.getRightChild();
		while (n != null) {
			stack.push(n);
			n = n.getLeftChild();
			while (n != null) {
				stack.push(n);
				n = n.getLeftChild();
			}
		}
		return nodeToBeReturned.getKey();
	}

	/**
	 * Not Supported
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
