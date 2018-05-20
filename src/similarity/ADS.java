package similarity;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import java.util.*;
import org.jblas.DoubleMatrix;  
import similarity.Powerset;
import java.math.BigInteger;
import java.math.BigDecimal;

// For fast matrix  


/**
 *
 * @author eegho
 */
public class ADS {
    public ADS() {

    }   
  
  
  private static BigDecimal inclusion_exclusion_all_sub_sequence(int position,int level,Set temp,List<Vector> L,List<Integer> L_position,BigDecimal[] d) {
      BigDecimal x = new BigDecimal("0");

      if (level == 0) 
      {
          x= new BigDecimal("2").pow(temp.size()).subtract(new BigDecimal("1")).multiply(d[L_position.get(position)]);
      } 
      else 
      {
        for (int j = position - 1; j >= 0; j--)
        {
          Set temp1 = new HashSet(L.get(j));
          temp1.retainAll(temp);
          if (temp1.size() != 0) 
          {
            level--;
            x =x.add(inclusion_exclusion_all_sub_sequence(j, level, temp1, L,L_position, d));
            level++;
          }
        }
      }
      return x;
  }
    
    
  private static BigDecimal inclusion_exclusion_all_sub_sequence(int position,int level,Set temp,List<Vector> L,int end) 
  {
      BigDecimal x = new BigDecimal("0");
      if (level == 0) 
          x=new BigDecimal("2").pow(temp.size());
      else 
      {
        for (int j = position-1; j >end; j--)
        {
          Set temp1 = new HashSet(L.get(j));
          temp1.retainAll(temp);
          level--;
          x=x.add(inclusion_exclusion_all_sub_sequence(j, level, temp1, L,end));
          level++;
        }
      }
      return x;
  }
    
    
    
    
  private static double inclusion_exclusion(int position, int n, int level,
                                            Set itemset, List<Vector> seq) {
      double x = 0;

      if (level == 0) {
          Set itemsetn = new HashSet(seq.get(n - 1));
          itemsetn.retainAll(itemset);
          x = Math.pow(2, itemsetn.size());
      } else {
          for (int j = position + 1; j < n; j++) {
              Set tempitemset = new HashSet(seq.get(j - 1));
              tempitemset.retainAll(itemset);
              level--;
              x = x + inclusion_exclusion(j, n, level, tempitemset, seq);
              level++;
          }
      }

      return x;
  } 

 
 
 
  
  public static BigDecimal Number_Subsequence(List<Vector> seq_list) 
  {
      
      
    if(seq_list.size()==0)
        return new BigDecimal("1");
    int seq_length=seq_list.size();
    BigDecimal[] d=new BigDecimal[seq_length+1];
    for(int i=1;i<d.length;i++)
      d[i]=new BigDecimal("0");
    d[0]=new BigDecimal("1");            
    for(int i=0;i<seq_length;i++) 
    { 
       // System.out.println("i="+i);
      List<Vector> L=new ArrayList(); 
      ArrayList<Integer> L_position=new ArrayList();
      //System.out.println("L="+L.toString());
      Vector item_n=seq_list.get(i);
      L=Generate_L(i-1, seq_list,item_n,L_position);
        //System.out.println(L.size() + " L = " + L);
        //System.out.println("L_position " + L_position);
      d[i+1]=new BigDecimal("2").pow(item_n.size()).multiply(d[i]);
      BigDecimal item_remove=new BigDecimal("0");
      double one=-1;
      double temp_one=1;
      BigDecimal R=new BigDecimal("0");
      for(int level=0;level<L.size();level++) 
      {
    	//System.out.println("  level " + level);
        for(int j=L.size()-1;j>=0;j--) 
        {
          Set item=new HashSet(L.get(j));
          item_remove=item_remove.add(inclusion_exclusion_all_sub_sequence(j,level,item,L,L_position,d));
        	//System.out.println("    j " + j + ", item " + item + ", item_remove " + item_remove);
        }
      //    System.out.println(item_remove);
        d[i+1]=d[i+1].add(item_remove.multiply(new BigDecimal(one+"")));
        R=R.add(item_remove.multiply(new BigDecimal(temp_one+"")));
        item_remove=new BigDecimal("0");
        one=one*-1; 
        temp_one=temp_one*-1;
      }
   //   System.out.println("R="+R);
   //   System.out.println("phi:"+d[i+1]);

      
    }
    return d[d.length-1];
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  public static BigDecimal approxElias(List<Vector> seq_list) 
  {
      
      
    if(seq_list.size()==0)
        return new BigDecimal("1");
    int seq_length=seq_list.size();
    BigDecimal[] d=new BigDecimal[seq_length+1];
    for(int i=1;i<d.length;i++)
      d[i]=new BigDecimal("0");
    d[0]=new BigDecimal("1");            
    for(int i=0;i<seq_length;i++) 
    { 
     

      Vector item_n=seq_list.get(i);
      int L=get_first_intersection(i-1, seq_list,item_n);
      //System.out.println("L="+L+"");
      d[i+1]=new BigDecimal("2").pow(item_n.size()).multiply(d[i]);
      if(L!=-1)
      {
       
        Set intersection = new HashSet(item_n);
        intersection.retainAll(seq_list.get(L));
        BigDecimal remove_value=((new BigDecimal("2").pow(intersection.size())).subtract(new BigDecimal("1")).multiply(d[L]));
        d[i+1]=d[i+1].subtract(remove_value);
        System.out.println("R="+remove_value);
          System.out.println("Phi("+i+")="+d[i+1]);
      }
     

    }
    return d[d.length-1];
  }


  public static BigDecimal approxNumberOfSequences(List<Vector> seq_list, int threshold) 
  {
      int seq_length=seq_list.size();
      boolean doApprox = false;
      double currentCoefficient =0;
      BigDecimal item_remove=new BigDecimal("0");
      int one=-1;
      BigDecimal[] d=new BigDecimal[seq_length+1];
      for(int i=1;i<d.length;i++)
        d[i]=new BigDecimal("0");
      d[0]=new BigDecimal("1");
      for(int i=0;i<seq_length;i++) 
      { 
        doApprox =false;
        List<Vector> L=new ArrayList(); 
        ArrayList<Integer> L_position=new ArrayList();
        Vector item_n=seq_list.get(i);
        L=Generate_L(i-1, seq_list,item_n,L_position);  
        d[i+1]=new BigDecimal("2").pow(item_n.size()).multiply(d[i]);          
        item_remove=new BigDecimal("0");
        one=-1;
        if(L.size() > threshold)
          doApprox = true;
        if(doApprox)
        {
            int k = (int)(Math.ceil(Math.sqrt(L.size())));
            int N;
            if(L.size()<=1)
                N=2;
            else
                N=L.size();
            IEApprox iea = new IEApprox(k,N);
            BigDecimal R=new BigDecimal("0");
            DoubleMatrix coefficients = iea.getCoefficients();
     //       for(int ii=0;ii<k;ii++)
     //           System.out.println(coefficients.get(0,ii)+"");
            for(int level=0;level<k;level++) 
            {
                
                currentCoefficient = coefficients.get(0,level);
                for(int j=L.size()-1;j>=0;j--) 
                {
                    Set item=new HashSet(L.get(j));
                    item_remove=item_remove.add(inclusion_exclusion_all_sub_sequence(j,level,item,L,L_position,d));
                }
             //   System.out.print(currentCoefficient+"*"+item_remove+" ");
                d[i+1]=d[i+1].subtract(item_remove.multiply(new BigDecimal(currentCoefficient+"")));
              //System.out.println("ads[i+1]="+d[i+1]);

                R=R.add(item_remove.multiply(new BigDecimal(currentCoefficient+"")));
              //  item_remove=new BigDecimal("0");
            }
            //System.out.println("");
           // System.out.println("ads["+(i+1)+"]="+d[i+1]+"\n");
          }
          else
          {  
            BigDecimal R=new BigDecimal("0");
            int temp_one=1;
            for(int level=0;level<L.size();level++) 
            {
              for(int j=L.size()-1;j>=0;j--) 
              {
                Set item=new HashSet(L.get(j));
                item_remove=item_remove.add(inclusion_exclusion_all_sub_sequence(j,level,item,L,L_position,d));
              }
                d[i+1]=d[i+1].add(item_remove.multiply(new BigDecimal(one+"")));
                R=R.add(item_remove.multiply(new BigDecimal(temp_one+"")));
                item_remove=new BigDecimal("0");
                one=one*-1; 
                temp_one=temp_one*-1; 
            }
          }
      }
      return d[d.length-1];
  }
  
  
  
  
 
 
 
 
 
 
 
  public static BigDecimal approxNumberOfSequences(List<Vector> seq_list) 
  {
      int seq_length=seq_list.size();
     System.out.println("length:"+seq_length);
      double currentCoefficient =0;
      BigDecimal item_remove=new BigDecimal("0");
      int one=-1;
      BigDecimal[] d=new BigDecimal[seq_length+1];
      for(int i=1;i<d.length;i++)
        d[i]=new BigDecimal("0");
      d[0]=new BigDecimal("1");
      
      for(int i=0;i<seq_length;i++) 
      { 
          
        List<Vector> L=new ArrayList(); 
        ArrayList<Integer> L_position=new ArrayList();
        Vector item_n=seq_list.get(i);
        L=Generate_L(i-1, seq_list,item_n,L_position);
        d[i+1]=new BigDecimal("2").pow(item_n.size()).multiply(d[i]);          
        item_remove=new BigDecimal("0");
        one=-1;
          if(L.size()>1)
          {
              int k = (int)(Math.ceil(Math.sqrt(L.size())));
              if(k==0)
                  k++;
              IEApprox iea = new IEApprox(k,L.size());
              DoubleMatrix coefficients = iea.getCoefficients();
              for(int level=0;level<k;level++) 
              {
                  currentCoefficient = coefficients.get(0,level);
                  for(int j=L.size()-1;j>=0;j--) 
                  {
                      Set item=new HashSet(L.get(j));
                      item_remove=item_remove.add(inclusion_exclusion_all_sub_sequence(j,level,item,L,L_position,d));
                  }
                  
                  
                  d[i+1]=d[i+1].subtract(item_remove.multiply(new BigDecimal(currentCoefficient+"")));
                  item_remove=new BigDecimal("0");
              }
          }
        else
        {  
          for(int level=0;level<L.size();level++) 
          {
            for(int j=L.size()-1;j>=0;j--) 
            {
              Set item=new HashSet(L.get(j));
              item_remove=item_remove.add(inclusion_exclusion_all_sub_sequence(j,level,item,L,L_position,d));
            }
            d[i+1]=d[i+1].add(item_remove.multiply(new BigDecimal(one+"")));
            item_remove=new BigDecimal("0");
            one=one*-1; 
          }
        }
      }
      return d[d.length-1];
  }
 
 
 
 
 
 
 
  

  private static void add_element(Vector new_item,int position,List<Vector> L,ArrayList<Integer> position_set) 
  {
      int i=0;
      int size=L.size();
      while(i<size)
      {
        if (new_item.containsAll(L.get(i))) 
        {
          position_set.remove(i);
          L.remove(i);
          size--;
        }
        else 
          i++;
      }
      L.add(new_item);
      position_set.add(position);

  }
  
 
  private static List<Vector> Generate_L(int end_seq,List<Vector> S,Vector item_n,ArrayList<Integer> position_set) 
  {
     List<Vector> L = new ArrayList();
      for (int k = 0; k <= end_seq; k++) 
      {
        Set intersection = new HashSet(item_n);
        intersection.retainAll(S.get(k));
        if (intersection.size() != 0) 
        {
          Set a = new HashSet(intersection);
          add_element(new Vector(a),k,L,position_set);
        }
      }
      return L;
  }
  
  
  
  
  
  
  private static int get_first_intersection(int end_seq,List<Vector> S,Vector item_n) 
  {
    
    int postion=-1;
      for (int k = end_seq; k >= 0; k--) 
      {
        Set intersection = new HashSet(item_n);
        intersection.retainAll(S.get(k));
        if (intersection.size() != 0) 
        {
            postion=k;
            break;
        }
      }
      return postion;
  }

  
  
 

}
