/*
 * Main.java
 *
 * Created on June 1, 2008, 10:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package illegal_wiretaps;

/**
 *
 * @author tashiro
 */
public class Main {
  
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws Exception {
    String[] listFirstNames = null;
    
    InputHandler inHand = new InputHandler();
    try {
      inHand.handleCommand(args);
      listFirstNames = inHand.handleFileInput();
    } catch (Exception e) {
      System.out.println("problem handling command line or input file.");
      System.out.println("\t" + e);
      return;
    }
    
    WiretapsProblem prob = new WiretapsProblem();   
    prob.solveProblem(listFirstNames);
    prob.printSolution();
  }
  
}
