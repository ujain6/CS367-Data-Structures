///////////////////////////////////////////////////////////////////////////////
//                   
// Main Class File:  StudentCenter.java
// File:             Listnode.java
// Semester:         CS367, Spring 2016
//
// Author:           Utkarsh Jain (ujain6@wisc.edu)
// CS Login:         utkarsh
// Lecturer's Name:  James Skrentny
// 
//
/**
 * The internal node structure of {@link PacketLinkedList}.
 *
 * @param <E> the generic type of the data content stored in the list
 */
public class Listnode<E> {
    private E data;                // data to be stored 
    private Listnode<E> next;   // connnection to next node

    /**
     * Constructs a new list nodes with no links to neighboring nodes.
     * @param data the data to be stored in this node
     */
    Listnode(E data) {
        this(data, null);
    }
    
    /**
     * Constructs a new list node with links to neighboring nodes.
     * @param data the data to be stored in this node
     * @param next the node after this one
     */
    Listnode(E data, Listnode<E> next) {
        this.data = data;
        this.next = next;
    }

    /**
     * Returns the current data.
     * @return the current data
     */
    E getData() {
        return data;
    }

    /**
     * Returns the current next node.
     * @return the current next node
     */
    Listnode<E> getNext() {
        return next;
    }

    /**
     * Sets the data to the given new value.
     * @param data the new data
     */
    void setData(E data) {
        this.data = data;
    }

    /**
     * Sets the next node to the given new value.
     * @param next the new next node
     */
    void setNext(Listnode<E> next) {
        this.next = next;
    }
}
