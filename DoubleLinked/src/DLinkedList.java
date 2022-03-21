import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * provided file for DLinkedList Assignment 
 *
 * @author lkfritz
 */

//MAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAINMAIN
public class DLinkedList<T extends Comparable<T>> {

    public static void main(String[] args) throws FileNotFoundException {

        DLinkedList<String> lst1 = new DLinkedList<>();
        DLinkedList<String> lst2 = new DLinkedList<>();        

        Scanner fin = new Scanner(new File("text1.in"));
        String str;

        while (fin.hasNext()) {
            str = fin.next();
            str = cleanUp(str);           
            lst1.insertOrderUnique(str);           
        }
        fin.close();

        fin = new Scanner(new File("text2.in"));
        while (fin.hasNext()) {
            str = fin.next();
            str = cleanUp(str);
            lst2.insertOrderUnique(str);           
        }

        System.out.println("List 1:  " + lst1);
        System.out.println("List 2:  " + lst2);
        
        
        DLinkedList combined = lst1.merge(lst2);
        
        System.out.println("\nAFTER MERGE");
        System.out.println("List 1:  " + lst1);
        System.out.println("List 2:  " + lst2);
        System.out.println("\n" + combined);
    }
//ENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAINENDOFMAIN
    //-------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * ASSIGNED
     * @param str
     * @return str in all lower case with LEADING and TRAILING non-alpha
     * chars removed
     * Complexity: O(2n)
     */
    public static String cleanUp(String str) {
    	
    	//System.out.println("Before: '" + str + "'");

    	// remove all alpha numeric characters (leading and trailing)
    	//   ^\P{Alnum}+     : any 1 or more chars other than alphanumeric at the beginning of a string
    	//   \\P{Alnum}+$    : any 1 or more chars other than alphanumeric at the end of a string
    	// regex to replace all matching items with "",  complexity O(n)
    	String s = str.replaceAll("^\\P{Alnum}+|\\P{Alnum}+$", "");
    	
    	// now make all lowercase, complexity O(n)
    	String cleaned = s.toLowerCase();
    	
    	//System.out.println("After: '" + cleaned + "'");
    	
        return cleaned;
    }

    //inner DNode class:  PROVIDED
    private class DNode {

        private DNode next, prev;
        private T data;

        private DNode(T val) {
            this.data = val;
            next = prev = this;
        }
    }

    //DLinkedList fields:  PROVIDED
    private DNode header;

    //create an empty list:  PROVIDED
    public DLinkedList() {
        header = new DNode(null);
        
    //-----------------------------------------------------------------------------------------------------------------------------------------
    }

    /**
     * PROVIDED add
     *
     * @param item return ref to newly inserted node
     */
    public DNode add(T item) {
        //make a new node
        DNode newNode = new DNode(item);
        //update newNode
        newNode.prev = header;
        newNode.next = header.next;
        //update surrounding nodes
        header.next.prev = newNode;
        header.next = newNode;
        return newNode;
    }

    //PROVIDED
    public String toString() {
        String str = "[";
        DNode curr = header.next;
        while (curr != header) {
            str += curr.data + " ";
            curr = curr.next;
        }
        str = str.substring(0, str.length() - 1);
        return str + "]";
    }

    /**
     * ASSIGNED
     * remove val from the list
     *
     * @param val
     * @return true if successful, false otherwise
     */
    
    
    
    
    //-------------------------------------------------------------------------------------------------------------------------------------------
    public boolean remove(T val) {
    	
    	// Find the value in the list, remove and fix prev/next pointers for
    	// the previous and next nodes.
    	// Algorithm: sequential search, iterate through list, looking for value to remove
    	// Big O: O(n) - worst case if value at end of list
    	boolean itemRemoved = false;
    	
    	DNode node = header.next;  // initialize to first real value
    	
    	while (node.data != null)   // interate until we get back to header (header.data == null)
    	{
    		// check if a match was found
    		if (node.data.equals(val))
    		{
    		//	System.out.println("remove("+ val + ") found match: "+ node.data);
    			
    			// found a match, fix surrounding nodes to remove this node
    			node.prev.next = node.next;  // set previous node to point to next node
    			node.next.prev = node.prev;  // set next node to point to prev node
    			itemRemoved = true;
    			node = node.next;
    		}
    		else
    		{
        		node = node.next;   // Move to next node
    		}
    	}
    	
        return itemRemoved;
    }
    
    //-------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * ASSIGNED
     * inserts a value into the list in such a way that the list remains sorted in ascending order
     *
     * @param item
     */
    public void insertOrder(T item) {
    	
    	// don't allow null items
    	if (item == null)
    		return;
    	  	
    	if (header.next == header)
    	{
    		// this is an empty list, just add the item
    		add(item);
    	}    	
    	else
    	{
    		DNode node = header.next;
    		boolean found = false;
    		
    		while (node.data != null)  // header
    		{
    			int val = item.compareTo(node.data);
    			if (val <= 0)
        		{
        			DNode newNode = new DNode(item);
        			// item is to be inserted before current node
        			//    node.prev  <--- newNode ---> node
        	        //update newNode to be inserted between previous node, and this node
        			newNode.next = node;       // newNode's next will now point to this node
        	        newNode.prev = node.prev;  // newNode's prev will point to this nodes prev
        	        node.prev = newNode;       // fix this node's prev point to point to the new node
        	        newNode.prev.next = newNode;
        	        found = true;
        	        break;
        		}
    			else
        		{
        			// keep looking
     			   node = node.next;
        		}
    		}
    		
    		if (!found)
    		{
    			// add to the end of the list, update all 4 next/prev pointers
    			DNode origLastNode = header.prev;
    			DNode newLastNode = new DNode(item);
    			origLastNode.next = newLastNode;
    			newLastNode.next = header;
    			newLastNode.prev = origLastNode;
    			header.prev = newLastNode;
    		}

    	}
    }

    
    //-----------------------------------------------------------------------------------------------------------------------------------------
    /**
     * ASSIGNED
     * inserts a value into the list in such a way that the list remains sorted in ascending order,
     * but only if the item is unique
     *
     * @param item
     */
    public boolean insertOrderUnique(T item) {
    	
    	// don't allow null items
    	if (item == null)
    		return false;
    	  	
    	if (header.next == header)
    	{
    		// this is an empty list, just add the item
    		add(item);
    	}    	
    	else
    	{
    		DNode node = header.next;
    		boolean found = false;
    		
    		while (node.data != null)  // header
    		{
    			int val = item.compareTo(node.data);
    			if (val < 0)
        		{
        			DNode newNode = new DNode(item);
        			// item is to be inserted before current node
        			//    node.prev  <--- newNode ---> node
        	        //update newNode to be inserted between previous node, and this node
        			newNode.next = node;       // newNode's next will now point to this node
        	        newNode.prev = node.prev;  // newNode's prev will point to this nodes prev
        	        node.prev = newNode;       // fix this node's prev point to point to the new node
        	        newNode.prev.next = newNode;
        	        found = true;
        	        break;
        		}
    			else if (val == 0)
    			{
    				// Special case for this method, value == 0, so string is a duplicate
    				// no more processing needed, ignore and bail out of the method
    				return false;
    			}
    			else
        		{
        			// keep looking
     			   node = node.next;
        		}
    		}
    		
    		if (!found)
    		{
    			// add to the end of the list, update all 4 next/prev pointers
    			DNode origLastNode = header.prev;
    			DNode newLastNode = new DNode(item);
    			origLastNode.next = newLastNode;
    			newLastNode.next = header;
    			newLastNode.prev = origLastNode;
    			header.prev = newLastNode;
    		}

    	}
        return true;
    }

    
    //-------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * ASSIGNED
     * PRE:  this and rhs are sorted lists
     * @param rhs
     * @return list that contains this and rhs merged into a sorted list
     * POST:  returned list will not contain duplicates
     */

    public DLinkedList merge(DLinkedList rhs) {
        DLinkedList result = new DLinkedList();
        DLinkedList abc = DLinkedList.this;
        
        if (rhs == null) { 
            return rhs; 
        } 
  
        // If second linked list is empty 
        if (abc == null) { 
            return abc; 
        } 
        
        result = rhs;
        
        
//        result = rhs;
//        DNode boo = abc.header;
//        
//        result.header = boo;
//        result.header.next = result.header;
        
        
 //       
//        DNode first = abc.header; 
//        DNode second = rhs.header;
//        // Pick the smaller value 
//        if (first.data < second.data) { 
//            first.next = merge(first.next, second); 
//            first.next.prev = first; 
//            first.prev = null; 
//            return first; 
//        } else { 
//            second.next = merge(first, second.next); 
//            second.next.prev = second; 
//            second.prev = null; 
//            return second; 
//        } 
  
        return result;
        
    }

}