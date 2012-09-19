/*
 * FibonacciHeapNode.java
 *
 * Created on June 4, 2008, 2:09 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package illegal_wiretaps;

/**
 *
 * @author tashiro
 */
public class FibonacciHeapNode {
  
  public FibonacciHeapNode parent, child, sibling_left, sibling_right;
  public boolean mark;
  public int id, degree;
  public double priority;
  public FibonacciHeapNode prev_node;  // previous node in augument path.
  
  
  /** Creates a new instance of FibonacciHeapNode */
  public FibonacciHeapNode(int id, double priority) {  // assume id >= 0
    this.id = id;
    this.priority = priority;
    initializeNodeForInsert();
    this.prev_node = null;
  }
  
  public FibonacciHeapNode (int id, double priority, FibonacciHeapNode prev_node) {
    this.id = id;
    this.priority = priority;
    initializeNodeForInsert();
    this.prev_node = prev_node;
  }
  
  public void initializeNodeForInsert() {
    parent = null;
    child = null;
    sibling_left = this;
    sibling_right = this;
    mark = false;
    degree = 0;
  }
  
  public String toString() {
    int prev_id = (prev_node == null)? -1: prev_node.id;
    String str = "id:" + id + " pri:" + priority + " prev_node:" + prev_id + "\n";
    return str;
  }  
}
