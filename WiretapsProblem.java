/*
 * WiretapsProblem.java
 *
 * Created on June 1, 2008, 11:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package illegal_wiretaps;

/**
 *
 * @author tashiro
 */
public class WiretapsProblem {
  
  private static PrimeHandler pHandler;
  private MinWeightBipartiteMatch mwbMatchFheap;
  private double[][] costTable;
  private String[] listVName;
  private int[] solution;
  
  public final static char[] vowels = {'a', 'i', 'u', 'e', 'o'};
  public final static char[] consonants = {'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 
                                           'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z'};
  
  /**
   * Creates a new instance of WiretapsProblem
   */
  public WiretapsProblem() {
    if (pHandler == null)
      pHandler = new PrimeHandler();
  }

  /* Make costTable based on input list_victim_name */
  private void setCostTable(String[] list_victim_name) {
    solution = null;
    listVName = list_victim_name;
    costTable = new double[list_victim_name.length][list_victim_name.length];
    
    int lengh_name, num_vowel, num_consonent;
    double even_weight, odd_weight;
    boolean flag_is_even;
    
    for (int idx_victim_name = 0; idx_victim_name < list_victim_name.length; idx_victim_name++) {
      
      lengh_name = list_victim_name[idx_victim_name].length();
      num_vowel = getNumVowel(list_victim_name[idx_victim_name]);
      num_consonent = getNumConsonent(list_victim_name[idx_victim_name]);
      
      even_weight = 1.5 * num_vowel;
      odd_weight = num_consonent;
            
      flag_is_even = false;
      for (int idx_prog = 0; idx_prog < list_victim_name.length; idx_prog++) {
        
        if (flag_is_even)
          costTable[idx_victim_name][idx_prog] = even_weight + 
                2 * pHandler.numSharedPrimeFactor(get_programmer_id(idx_prog), lengh_name) + lengh_name;
        else
          costTable[idx_victim_name][idx_prog] = odd_weight + 
                2 * pHandler.numSharedPrimeFactor(get_programmer_id(idx_prog), lengh_name) + lengh_name;
        
        flag_is_even = !flag_is_even;
      }
    }
  }
  
  private static int get_programmer_id(int x) {
    return x + 1;
  }
  
  private String get_victim_name(int x) {
    return listVName[x];
  }


  public static int getNumVowel(String word) {  // assume word is lower case letters.
    int counter = 0;
    for (int i = 0; i < word.length(); i++) 
      for (int j = 0; j < WiretapsProblem.vowels.length; j++)
        if (word.charAt(i) == WiretapsProblem.vowels[j]) {
          counter++;
          j = WiretapsProblem.vowels.length;  // no need to search vowels for this character.
        }
    return counter;
  }
  
  public static int getNumConsonent(String word) {  // assume word is lower case letters.
    int counter = 0;
    for (int i = 0; i < word.length(); i++) 
      for (int j = 0; j < WiretapsProblem.consonants.length; j++)
        if (word.charAt(i) == WiretapsProblem.consonants[j]) {
          counter++;
          j = WiretapsProblem.consonants.length;  // no need to search vowels for this character.
        }
    return counter;
  }
  
  /* Solve the problem using Minimum Weight Bipartite Match algorithm */
  public int[] solveProblem(String[] list_victim_name) throws Exception {
    setCostTable(list_victim_name);
    
    //long startTime = System.currentTimeMillis(); 
    mwbMatchFheap = new MinWeightBipartiteMatch(costTable);
    //stopTime = System.currentTimeMillis(); 
    //long runTime = stopTime - startTime; 
    //System.out.println("Run time: " + runTime);
    
    return solution = mwbMatchFheap.findMatch();  
  }
  
  public double getTotalCost() {
    double total_cost = 0;
    for (int i = 0; i < solution.length; i++) {
      total_cost += costTable[i][solution[i]];
    }
    return total_cost;
  }
  
  public void printSolution () {
    System.out.println("total cost: " + getTotalCost() + "\n");
     for (int i = 0; i < solution.length; i++) {
      System.out.println( get_victim_name(i) + " => " + get_programmer_id(solution[i]) + 
                          " (" + costTable[i][solution[i]] + ")" );
    }
  }

  public void printCostTable() {
    String str = new String();
    for (int i = 0; i < costTable.length; i++) {
      for (int j = 0; j < costTable[0].length; j++)
        str += costTable[i][j] + ", ";
      str += "\n";
    }
    System.out.println(str);   
  }
}
