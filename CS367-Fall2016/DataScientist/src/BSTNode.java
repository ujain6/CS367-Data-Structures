/**
 * Node class for Binary Search Tree (BST).
 *
 * DO NOT MODIFY THIS CLASS.
 *
 * @author CS367
 */
public class BSTNode<K> {

	/** Key value */
	private K key;

	/** Left child */
	private BSTNode<K> left;

	/** Right child */
	private BSTNode<K> right;

	/**
	 * The height of a node is the length of the path from that node to the
	 * farthest leaf. For Example, a leaf node has height 1, its parent has
	 * height 2 and so on.
	 */
	private int height;

	/**
	 * Balance factor for this node, which is equal to the height of its left
	 * subtree minus the height of its right subtree.
	 */
	private int balanceFactor;

	/**
	 * Constructs a new leaf node containing k.
	 *
	 * @param k
	 *            the key
	 */
	public BSTNode(K k) {

		key = k;
		left = right = null;
		height = 1;
		balanceFactor = 0;
	}

	/**
	 * Get this node's key.
	 *
	 * @return the key
	 */
	public K getKey() {
		return key;
	}

	/**
	 * Get this node's left child.
	 *
	 * @return the left child
	 */
	public BSTNode<K> getLeftChild() {
		return left;
	}

	/**
	 * Get this node's right child.
	 *
	 * @return the right child
	 */
	public BSTNode<K> getRightChild() {
		return right;
	}

	/**
	 * Get this node's height.
	 *
	 * @return the height
	 */
	public int getHeight() {

		return height;
	}

	/**
	 * Get this node's balance factor.
	 *
	 * @return the balance factor
	 */
	public int getBalanceFactor() {

		return balanceFactor;
	}

	/**
	 * Set this node's left child.
	 *
	 * @param l
	 *            the left child
	 */
	public void setLeftChild(BSTNode<K> l) {
		left = l;
	}

	/**
	 * Set this node's right child.
	 * 
	 * @param r
	 *            the right child
	 */
	public void setRightChild(BSTNode<K> r) {
		right = r;
	}

	/**
	 * Set this node's height.
	 *
	 * @param h
	 *            the height
	 */
	public void setHeight(int h) {
		height = h;
	}

	/**
	 * Set this node's balance factor.
	 * 
	 * @param bf
	 *            the balance factor
	 */
	public void setBalanceFactor(int bf) {
		balanceFactor = bf;
	}
}
