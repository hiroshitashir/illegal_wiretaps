/*
 * FibonacciHeap.java
 *
 * Created on June 4, 2008, 2:09 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package illegal_wiretaps;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author tashiro
 */
public class FibonacciHeap {
  
  private HashMap<Integer,FibonacciHeapNode>  handle = new HashMap<Integer,FibonacciHeapNode>();
  private FibonacciHeapNode min_node = null;
  private int num_nodes = 0;
  
  
  /** Creates a new instance of FibonacciHeap */
  public FibonacciHeap() {  // make heap
  }
  
  
  // assume arg_node is properly initialize, meaning parent and child are set to
  // null, and sibling_left and sibling_right are set to itself. In addition,
  // mark is false and degree is 0.
  public void insert(FibonacciHeapNode arg_node) {
    
    // concatenate the root list containing arg_node with root list H
    if (this.min_node == null)
      min_node = arg_node;
    else
      mergeSiblingList(this.min_node, arg_node);
    
    if (min_node == null || min_node.priority > arg_node.priority)
      min_node = arg_node;
    num_nodes++;
    handle.put(arg_node.id, arg_node);
  }
  
  
  public FibonacciHeapNode minimum() {
    return min_node;
  }
  
  
  // assume nodes in arg_heap have different ids from those in this heap.
  public void union(FibonacciHeap arg_heap) {
    if (arg_heap.empty())
      return;
    
    if (this.empty()) {
      this.min_node = arg_heap.min_node;
      this.handle = arg_heap.handle;
      this.num_nodes = arg_heap.num_nodes;
      return;
    }
    
    // concatenate the root list of arg_heap with the root list of H
    mergeSiblingList(this.min_node, arg_heap.min_node);
    
    if (this.min_node.priority > arg_heap.min_node.priority)
      this.min_node = arg_heap.min_node;
    
    this.num_nodes += arg_heap.num_nodes;
    
    this.handle.putAll(arg_heap.handle);
  }
  
  
  public FibonacciHeapNode extractMinimum() {
    FibonacciHeapNode node_return = this.min_node;
    FibonacciHeapNode child_begin, child_each;
    
    if (node_return != null) {
      
      if (node_return.child != null) {
        // update parent pointers of minimum node children
        child_begin = node_return.child;
        child_each  = node_return.child;
        do {
          child_each.parent = null;
        } while ( (child_each = child_each.sibling_right) != child_begin );  // pointer comparison
        
        // add the children to the root list
        mergeSiblingList(node_return, child_begin);
      }
      
      // remove minimum node from the root list
      node_return.sibling_right.sibling_left = node_return.sibling_left;
      node_return.sibling_left.sibling_right = node_return.sibling_right;
      
      handle.remove(node_return.id);
      num_nodes--;
      
      if (node_return.sibling_right == node_return) {
        assert(num_nodes == 0);
        min_node = null;
      } else {
        min_node = node_return.sibling_right;
        consolidate();
      }
    }
    return node_return;
  }
  
  
  private void consolidate() {
    FibonacciHeapNode[] pnode_degree = new FibonacciHeapNode[this.num_nodes];
    for (int i = 0; i < pnode_degree.length; i++)
      pnode_degree[i] = null;
    
    LinkedList<FibonacciHeapNode> root_list = new LinkedList<FibonacciHeapNode>();
    FibonacciHeapNode node_start = min_node;
    FibonacciHeapNode node_each = min_node;
    do {
      root_list.add(node_each);
    } while ( (node_each = node_each.sibling_right) != node_start );
    
    // for each node in the root list
    while ( !root_list.isEmpty() ) {
      FibonacciHeapNode node_x = root_list.removeFirst();
      int degree = node_x.degree;
      while (pnode_degree[degree] != null) {
        FibonacciHeapNode node_y = pnode_degree[degree];
        if (node_x.priority > node_y.priority) {
          // exchange node_each <-> node_y
          FibonacciHeapNode tmp = node_x;
          node_x = node_y;
          node_y = tmp;
        }
        fibHeapLink(node_x, node_y);
        pnode_degree[degree] = null;
        degree++;
      }
      pnode_degree[degree] = node_x;
    }
    
    this.min_node = null;
    
    for (int i = 0; i < pnode_degree.length; i++) {
      if (pnode_degree[i] != null) {
        pnode_degree[i].sibling_left = pnode_degree[i];
        pnode_degree[i].sibling_right = pnode_degree[i];
        
        if (min_node != null)
          mergeSiblingList(min_node, pnode_degree[i]);
        
        if (min_node == null || min_node.priority > pnode_degree[i].priority)
          min_node = pnode_degree[i];
        
      }
    }
  }
  
  
  // assume node_x and node_y are non-null so that there are at lease two nodes
  // in the root list.
  private static void fibHeapLink(FibonacciHeapNode node_x, FibonacciHeapNode node_y) {
    // remove node_y from the root list
    node_y.sibling_right.sibling_left = node_y.sibling_left;
    node_y.sibling_left.sibling_right = node_y.sibling_right;
    
    // make node_y a child of x, icrement degree[x]
    node_y.parent = node_x;
    node_y.sibling_right = node_y;
    node_y.sibling_left = node_y;
    
    if (node_x.child == null) {
      node_x.child = node_y;
    } else
      mergeSiblingList(node_x.child, node_y);
    
    node_x.degree++;
    node_y.mark = false;
  }
  
  // merge two root lists. argument nodes are nodes in each root lists.
  private static void mergeSiblingList(FibonacciHeapNode node1, FibonacciHeapNode node2) {
    FibonacciHeapNode right_node_node1 = node1.sibling_right;
    FibonacciHeapNode left_node_node2 = node2.sibling_left;
    
    node1.sibling_right = node2;
    node2.sibling_left = node1;
    
    left_node_node2.sibling_right = right_node_node1;
    right_node_node1.sibling_left = left_node_node2;
  }
  
  
  public void decreasePriority(int id, double priority_new) throws Exception {
    if (id < 0 || !handle.containsKey(id) )
      throw new Exception("id "+id+" issue for decrease_priority");
    
    decreasePriority(this.handle.get(id), priority_new);
  }
  
  public void decreasePriority(FibonacciHeapNode node, double priority_new) throws Exception {
    if (priority_new > node.priority)
      throw new Exception("new key is greater than current key");
    
    node.priority = priority_new;
    if (node.parent != null && node.priority < node.parent.priority) {
      FibonacciHeapNode parent = node.parent;
      cut(node, parent);
      cascadingCut(parent);
    }
    
    if (node.priority < min_node.priority)
      min_node = node;
  }
  
  
  public void setPrevNode(int id, FibonacciHeapNode node_prev) throws Exception {
    if (id < 0 || !handle.containsKey(id) )
      throw new Exception("id "+id+" issue for set_prev_node");
    
    setPrevNode(handle.get(id), node_prev);
  }
  
  public void setPrevNode(FibonacciHeapNode node_target, FibonacciHeapNode node_new) throws Exception {
    node_target.prev_node = node_new;
  }
  
  
  private void cut(FibonacciHeapNode node, FibonacciHeapNode parent_node) {
    // remove node from the child list of parent_node, decrement degree[parent_node]
    if (parent_node.degree == 1)
      parent_node.child = null;
    else {
      parent_node.child = node.sibling_left;
      node.sibling_left.sibling_right = node.sibling_right;
      node.sibling_right.sibling_left = node.sibling_left;
    }
    parent_node.degree--;
    
    // add node to the root list
    node.sibling_left = node;
    node.sibling_right = node;
    mergeSiblingList(node, min_node);
    
    node.parent = null;
    node.mark = false;
  }
  
  private void cascadingCut(FibonacciHeapNode node) {
    FibonacciHeapNode parent_node = node.parent;
    if (parent_node != null) {
      if (node.mark = false)
        node.mark = true;
      else {
        cut(node, parent_node);
        cascadingCut(parent_node);
      }
    }
  }
  
  
  public void delete(int id) throws Exception {
    delete( this.handle.get(id) );
  }
  
  public void delete(FibonacciHeapNode node) throws Exception {
    decreasePriority(node, Double.MIN_VALUE);
    extractMinimum();
  }
  
  public FibonacciHeapNode get_node(int id) {
    return handle.get(id);
  }
  
  
  public double getPriority(int id) throws Exception {
    if (id < 0 || !handle.containsKey(id) )
      throw new Exception("id "+id+" issue for get_priority");
    
    FibonacciHeapNode node = handle.get(id);
    return node.priority;
  }
  
  public boolean contain(int id) {
    return (handle.containsKey(id));
  }
  
  public boolean empty() {
    return this.num_nodes == 0;
  }
  
  public void clear() {
    this.min_node = null;
    this.num_nodes = 0;
    this.handle.clear();
  }
  
  public void showStatus() {
    System.out.println(handle);
  }
}
