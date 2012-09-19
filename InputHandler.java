/*
 * InputHandler.java
 *
 * Created on June 1, 2008, 1:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package illegal_wiretaps;

import java.util.Vector;
import java.io.*;

/**
 *
 * @author tashiro
 */
public class InputHandler {
  private String fileName;
  private String[] firstNames;
  
  /**
   * Creates a new instance of InputHandler
   */
  public InputHandler() {
  }
  
  public void handleCommand(String[] args) throws Exception {
    if (args.length < 1)
      throw new Exception ("filename not provided.");
    
    fileName = args[0];
  }
  
  public String[] handleFileInput() {
    BufferedReader in = null;
    String line;
    Vector<String> tmp_container = new Vector<String>();
    int num_lines = 0;
    
    try {
      in = new BufferedReader( new FileReader(fileName) );
      
      while ( ( line = in.readLine() ) != null ) {
        line = ( line.trim() ).toLowerCase();
        
        // Skip empty lines
        if (line.length() == 0)
          continue;
        else {
          String word = new String();
          for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if ('a' <= ch && ch <= 'z')  // ignore non-charactor
              word =  word + ch;
          }
          tmp_container.add(word);
          num_lines++;
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (in!= null)
          in.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    
    firstNames = new String[num_lines];
    for (int i = 0; i < num_lines; i++)
      firstNames[i] = tmp_container.get(i);

    return firstNames;
  }
  
  public String[] getFirstNames() {
    return firstNames;
  }
  
}
