/*
 * PrimeHandler.java
 *
 * Created on June 1, 2008, 1:38 PM
 *
 * This class compute the number of shared prime factors between two
 * numbers. The process of finding it is as follows:
 *   
 *   - first generates list of prime numbers (default 20).
 *   - check if each prime number in the list is factor of two numbers
 *   - if prime numbers in the list is not large enough, expand the list
 *     with larger prime numbers
 *
 *
 * Warning: This method of computing the shared prime factors is time
 *          efficient but may become inefficient in terms of space
 *          complexity. Suppose one problem instance of two numbers are
 *          very large numbers and the rest of the instances are small
 *          numbers. Due to the first instance, the list of prime numbers
 *          holds large number of prime numbers. However, the rest of
 *          problem instances do not use all the prime numbers in the list.
 *
 */

package illegal_wiretaps;

import java.util.Vector;

/**
 *
 * @author tashiro
 */
public class PrimeHandler {
  private static int maxNumForPrime = 0;
  private static Vector<Integer> listPrimeNumbers = new Vector<Integer>();
  
  public PrimeHandler () { 
    generateListPrimeNumbers(20);
  }
  
  /**
   * Creates a new instance of PrimeHandler
   */
  public PrimeHandler(int max_num) {
    generateListPrimeNumbers(max_num);
  }
  
  private void generateListPrimeNumbers(int arg_num) {  // Sieve of Eratosthenes
    if (maxNumForPrime > arg_num)  // check wheather the prime numbers are generated.
      return;
    
    maxNumForPrime = 2 * maxNumForPrime ;
    maxNumForPrime = (maxNumForPrime > arg_num)? maxNumForPrime: arg_num;
    
    listPrimeNumbers.clear();
    boolean[] is_prime = new boolean[(maxNumForPrime + 1)];
    for (int i = 2; i < maxNumForPrime; i++)  // initial values for numbers larger than or equal to 2 are true.
      is_prime[i] = true;
    
    for (int i = 2; i * i <= maxNumForPrime; i++) {
      if (is_prime[i]) {
        // eliminate multiples of each prime, starting with its square
        for (int j = 0; j * i <= maxNumForPrime; j++) {
          if ( (i * i + j * i) <= maxNumForPrime)
            is_prime[(i * i + j * i)] = false;
        }
      }
    }
    
    for (int i = 2; i < maxNumForPrime; i++)
      if (is_prime[i])
        listPrimeNumbers.add(i);
  }
  
  public int numSharedPrimeFactor(int x, int y) {
    int counter = 0;
    int min = (x > y)? y: x;
    
    generateListPrimeNumbers(min);
    
    for (int prime_num: listPrimeNumbers) {
      if (x % prime_num == 0 && y % prime_num == 0)
        counter++;
      if (min < prime_num)
        break;
    }
    return counter;
  }  

}
