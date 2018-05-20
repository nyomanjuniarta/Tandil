package similarity;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;
import org.jblas.DoubleMatrix;                  // For fast matrix  


/**
 *
 * @author eegho
 */
public class Pars 
{
    
    public Pars() {

    }
    
  public static  List<Vector> pars_sequence_integer(String seq) 
  { 
     // System.out.println(seq);
      List<Vector> MS=new ArrayList(); 
      if (seq.trim().equals("-2"))
      {
          return MS;
      }
      seq=seq.replace("-2", "").trim();
      try {
          
          String[] seqs=seq.trim().split("-1");
          for(int i=0;i<seqs.length;i++)
          {
              String set = seqs[i];
              String[] items=set.trim().split(" ");
              
             // List list = Arrays.asList(items);
              Vector itemset =new Vector() ;
              for(int j=0;j<items.length;j++)
              {
                  itemset.addElement(items[j].trim());
              }
              
              MS.add(itemset);
          }
      } catch (Exception e) {
          System.out.println("Error in pars " +
                             e.toString());
      }
      return MS;    
  }
  
  public  static List<Vector> pars_sequence_string(String seq) 
  {  
      List<Vector> MS=new ArrayList(); 
      
      
      try {
          seq=seq.substring(2, seq.length()-2);
          //int nbSets = 0;
          StringTokenizer stSet =
          new StringTokenizer(seq, ")("); //Tokenizer des itemsets
          
          while (stSet.hasMoreTokens()) 
          {
              String set = stSet.nextToken();
              Vector itemset = new Vector();
              char[] ch = set.toCharArray();
              for (char c : ch)
                  itemset.addElement(c);  
              MS.add(itemset);
          } // End of reading itemsets
          
          //System.out.println(MS);
          
      } catch (Exception e) {
          System.out.println("Lors d'identifier le nombre des itemSet : " +
                             e.toString());
      }
      return MS;   
  }
  
  public static ArrayList<List<Vector>> pars_data(String data) 
  {
    ArrayList<List<Vector>> sequences=new ArrayList<List<Vector>>();
    try
    {
    
      BufferedReader reader = new BufferedReader(new FileReader(data));
      String line;
      int i=0;
      while((line=reader.readLine())!=null)
      {
          System.out.println("Seq"+(i++));
        sequences.add(pars_sequence_integer(line));
       
      }
       
    }
    catch(Exception ee) {
      System.out.println(ee.toString());
    }
    return sequences;
    
  }
  
  
  
  public  static int pars_data(String data,int length) 
  {
    int i=0;
    try
    {
    
      BufferedReader reader = new BufferedReader(new FileReader(data));
      String line;

      while((line=reader.readLine())!=null)
      {
        
        String[] seqs=line.trim().split("-1");
        if(seqs.length==length)
        {
            int x=1;
            i++;
        }
       
      }
       
    }
    catch(Exception ee) {
      
    }
    return i;
    
  }
  
  
  
  
  public static ArrayList<List<Vector>> pars_data(String data,int Start,int End) 
  {
    ArrayList<List<Vector>> sequences=new ArrayList<List<Vector>>();
    try
    {
    
      BufferedReader reader = new BufferedReader(new FileReader(data));
      String line;
      for(int i=0;i<End;i++)
      {
        line=reader.readLine();
        //  System.out.println(line);
        if(i>=Start)
          sequences.add(pars_sequence_integer(line));
      }
    }
    catch(Exception ee) {
      
    }
    return sequences;
    
  }
  
  
  
  
  
  public static void main(String args[]) 
  {
    
    
    String input_data= args[0];
    pars_data(input_data);
    System.gc();
    
  }

}
