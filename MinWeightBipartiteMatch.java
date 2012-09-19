/*
 * MinCostBipartiteMatchWFHeap.java
 *
 * Created on June 1, 2008, 3:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package illegal_wiretaps;

import java.util.Stack;
import java.util.LinkedList;

/**
 * MinWeightBipartiteMatchWMinHeap
 * 
 * This is an implementation of Minimum Weight Bipartite Matching using augument 
 * path and dijkstra's shortest path.
 * The details of how the algorithm works are explained in the following link: 
 *  http://valis.cs.uiuc.edu/~sariel/teach/courses/473/notes/27_matchings_notes.pdf
 * 
 * 
 * @author tashiro
 */
public class MinWeightBipartiteMatch {
  
  private double[][] table_weight;
  private int[] map_match_left_to_right, map_match_right_to_left;
  
  // handy when finding neighbors for exposed left category nodes
  private int[] list_all_node_ids_right_category;  
  
  /** Creates a new instance of MinCostBipartiteMatch */
  public MinWeightBipartiteMatch(double[][] arg_weight_table) throws Exception {
    
    if (arg_weight_table.length != arg_weight_table[0].length)
      throw new Exception 
            ("Weight table shoule be square. If not, just add extra row or " +
             "column to the table and assign infinity weights for the extra " +
             "row or column. ");
    
    table_weight = arg_weight_table;
    map_match_left_to_right = new int[table_weight.length];
    map_match_right_to_left = new int[table_weight.length];
    list_all_node_ids_right_category = new int[table_weight.length];  
    
    for (int i = 0; i < table_weight.length; i++) {
      map_match_left_to_right[i] = -1;  // indicate no matching
      map_match_right_to_left[i] = -1;  // indicate no matching
      list_all_node_ids_right_category[i] = global_node_id(i, false);
    }
  }
  
  public int[] findMatch() throws Exception {
    Stack<Integer> aug_path;

    while ( !( aug_path = findMinAugumentPath() ).empty() ) {
      //System.out.println("aug_path: " + aug_path);
      /*
        This part compute new match as follows:
          new_match = match (exclusive or) path = (match \ path) or (path \ match)
                where path is a augumenting path of the corresponding match. 
         After the update, the following condition satisfies: 
          |new_match| = |match| + 1.
       */
      // augumenting path starts from an exposed left category node
      boolean flag_left_category_node = true;  
      int id_prev_node = -1;
      while (!aug_path.empty()) {
        int id_node = aug_path.pop();

        assert( ( flag_left_category_node  && is_node_left_category(id_node) ) ||
                ( !flag_left_category_node  && !is_node_left_category(id_node) ) );
        
        if (id_prev_node != -1) {
          if (flag_left_category_node) {  // left category node
            assert (map_match_left_to_right[id_node] == id_prev_node);

            // the matched edge in augumenting path should be removed.
            map_match_left_to_right[id_node] = -1;  
            
          } else {  // right category node, meaning the edge goes to new match
            map_match_left_to_right[id_prev_node] = id_node;
          }
        }

        id_prev_node = id_node;
        flag_left_category_node  = !flag_left_category_node ;
      }
      
      // update map_match_right_left according to new map_match_left_to_right
      for (int i = 0; i < map_match_right_to_left.length; i++)  // initialize 
        map_match_right_to_left[i] = -1;  
      for (int i = 0; i < map_match_left_to_right.length; i++)
        if (map_match_left_to_right[i] != -1)
          map_match_right_to_left[local_node_id(map_match_left_to_right[i])] = i;
    }
    
    // return a solution match table with local ids
    int[] solution = new int[table_weight.length];
    for (int i = 0; i < table_weight.length; i++) 
        solution[i] = local_node_id(map_match_left_to_right[i]);
    return solution;
  }
  
  /*
   * Dijkstra's shortest path to find the minimum weight augument path given match.
   */
  private Stack<Integer> findMinAugumentPath() throws Exception {  
    Stack<Integer> path = new Stack<Integer>();
    FibonacciHeap fheap = new FibonacciHeap();
    
    setupHeap(fheap);
    
    while (!fheap.empty()) {
      //System.out.println("heap status:");
      //fheap.showStatus();
      FibonacciHeapNode node_entry = fheap.extractMinimum();
      //System.out.println("fheap min: " + node_entry.id + "(" + node_entry.priority + ")");
      
      if ( is_node_exposed_right_category(node_entry.id) ) {
        FibonacciHeapNode tmp_node_entry = node_entry;
        do {
          path.push(tmp_node_entry.id);
          tmp_node_entry = tmp_node_entry.prev_node;
        } while (tmp_node_entry != null);
        return path;
      }
            
      int[] list_neighbor_node_id = get_neighbors_aug_path(node_entry.id, fheap);  
      
      int local_id_entry_node = local_node_id(node_entry.id);
      double alt;
      int local_id_neighbor_node;
      for (int i = 0; i < list_neighbor_node_id.length; i++) {
        local_id_neighbor_node = local_node_id(list_neighbor_node_id[i]);
        
        if (is_node_left_category(node_entry.id))
          alt = node_entry.priority + table_weight[local_id_entry_node][local_id_neighbor_node];
        else
          alt = node_entry.priority - table_weight[local_id_neighbor_node][local_id_entry_node];

        if ( alt < fheap.getPriority(list_neighbor_node_id[i]) ) {
          fheap.decreasePriority( list_neighbor_node_id[i], alt);
          fheap.setPrevNode(list_neighbor_node_id[i], node_entry);
        }
      }
    }
    assert( path.empty() );
    return path;  // No augument path. should be empty list.
  }
  
  /*
   * Initial setup of minheap. We may have several sources (exposed nodes in left category) and want to find the 
   * shortest path from a source to an exposed node in right category.  Since Dijkstra's algorithm assumes single 
   * source, we have to pre-compute which source node has the shortest edge to each next node in right category. 
   * After the pre-compute, insert all the nodes except source nodes to minheap. From this point, Dijkstra's algorithm 
   * starts its own job.
   */
  private void setupHeap (FibonacciHeap fheap) throws Exception {
    double[] min_weight_right_nodes = new double[table_weight.length];
    FibonacciHeapNode[] prev_of_right_nodes = new FibonacciHeapNode[table_weight.length];
    
    // initialize 
    for (int i = 0; i < table_weight.length; i++) {
      min_weight_right_nodes[i] = Double.MAX_VALUE;
      prev_of_right_nodes[i] = null;
    }
    
    FibonacciHeapNode left_exposed_node;
    int num_left_exposed_nodes = 0;
    
    // insert nodes in left category 
    for (int i = 0; i < map_match_left_to_right.length; i++) {
      
      if (map_match_left_to_right[i] == -1) {  // exposed node (source nodes) 
        num_left_exposed_nodes++;
        left_exposed_node = new FibonacciHeapNode(global_node_id(i, true), 0);
        // no need to insert source node to the heap since we are already computing the 
        // shortest edge from the source node(s) to the next nodes in right category.
        // Just register the source node as a previous node of the next node.
        
        for (int j = 0; j < table_weight.length; j++) {    
          // keep track minimum edge between the right node and left exposed nodes
          if (min_weight_right_nodes[j] > table_weight[i][j]) {
            min_weight_right_nodes[j] = table_weight[i][j];
            prev_of_right_nodes[j] = left_exposed_node;
          }
        }
      } else   // non-exposed node
        fheap.insert(new FibonacciHeapNode(global_node_id(i, true), Double.MAX_VALUE));
    }
    
    if (num_left_exposed_nodes == 0) {
      fheap.clear();
      return;
    }
    
    // insert nodes in right category 
    for (int j = 0; j < map_match_right_to_left.length; j++) 
      fheap.insert(new FibonacciHeapNode(global_node_id(j, false), min_weight_right_nodes[j], prev_of_right_nodes[j]));    
  }

  // convert local node id to global 
  private int global_node_id (int local_id, boolean flag_left_category) {
    return (flag_left_category)? local_id: (table_weight.length + local_id);
  }
  
  // convert global node id to local
  private int local_node_id (int global_id_node) {
    assert ( global_id_node >= 0 && global_id_node < (table_weight.length * 2) );
    return (is_node_left_category(global_id_node))? global_id_node: global_id_node - table_weight.length;
  }
  
  // check if the node is left category in 
  private boolean is_node_left_category (int global_id_node) {
    assert ( global_id_node >= 0 && global_id_node < (table_weight.length * 2) );
    return (global_id_node < table_weight.length);
  }
  
  private boolean is_node_exposed_right_category(int global_id_node) {
    assert ( global_id_node >= 0 && global_id_node < (table_weight.length * 2) );
    int local_id_node = local_node_id(global_id_node);
    return (global_id_node >= table_weight.length && map_match_right_to_left[local_id_node] == -1);
  }
  
  private int[] get_neighbors_aug_path (int global_id_node, FibonacciHeap fheap) {  // neighbors in alternate path.
    assert ( global_id_node >= 0 && global_id_node < (table_weight.length * 2) );
    //if (global_id_node == 5)
    //  System.out.print("error point");
    if (is_node_left_category(global_id_node)) {
      
      if (map_match_left_to_right[global_id_node] == -1) // exposed node
        return list_all_node_ids_right_category;
      else {  // node with match
        LinkedList<Integer> list = new LinkedList<Integer>();
        for (int i = 0; i < list_all_node_ids_right_category.length; i++) {
          int global_id_right_node = list_all_node_ids_right_category[i];
          if ( global_id_right_node != map_match_left_to_right[global_id_node] && 
                fheap.contain(global_id_right_node) ) {
            list.add(global_id_right_node);
          }
        }
        
        int[] list_node_ids_right_category = new int[list.size()];
        for (int i = 0; i < list.size(); i++)
          list_node_ids_right_category[i] = list.get(i);
        return list_node_ids_right_category;        
      }

    } else {  // node is in right category
      
      // return the node in left category which can be reached through matched edge
      int[] list = new int[1];
      int idx = local_node_id(global_id_node);
      
      // no point finding neighbor if the node is an exposed right category node
      assert (map_match_right_to_left[idx] != -1);  
      
      list[0] = map_match_right_to_left[idx];
      return list;
    
    }
  }

   public void showStatus() {
    for (int i = 0; i < map_match_left_to_right.length; i++) {
      if (map_match_left_to_right[i] != -1)
        System.out.print(i + " => " + map_match_left_to_right[i] + "\n");        
    }
    System.out.println();
  }
}
