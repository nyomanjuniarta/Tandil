
package similarity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import java.math.BigDecimal;

import java.util.*;
import org.jblas.DoubleMatrix; 
import java.math.BigInteger;
import java.math.RoundingMode;

import org.apache.commons.math3.complex.Complex;

import similarity.Powerset;

/**
 *
 * @author eegho
 */
public class ACS {
    
    
    
    
    
    
    
    
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
  public static BigDecimal[][] prepare_matrix(int n,int m) 
    {
        BigDecimal[][] d=new BigDecimal[n+1][m+1];
        
        for(int i=0;i<d.length;i++)
            for(int j=0;j<d[i].length;j++)
                
                if(i==0 || j==0)
                    d[i][j]=new BigDecimal("1");
                else
                    d[i][j]=new BigDecimal("0");
        return d;
        
    }
  public static BigDecimal inclusion_exclusion_all_common_sequence_s1(int position,int m, int level,Set temp,List<Vector> L,ArrayList<Integer> L_position,BigDecimal[][] d) 
    {
        BigDecimal x=new BigDecimal("0"); 
        if(level==0)
        {
          x= new BigDecimal("2").pow(temp.size()).subtract(new BigDecimal(1)).multiply(d[L_position.get(position)][m]);
        }
        else 
        {
            for(int j=position-1;j>=0;j--) 
            {
                Set temp1=new HashSet(L.get(j));
                temp1.retainAll(temp);
                if(temp1.size()!=0)
                {
                    level--;
                    x=x.add(inclusion_exclusion_all_common_sequence_s1(j,m,level,temp1,L,L_position,d));
                    level++;
                }
            }  
        }
        return x;
    }
  
public static BigDecimal inclusion_exclusion_all_common_sequence_s1_s2(int position, int level,Set temp,List<Vector> Lx,List<Vector> Ly,ArrayList<Integer> Lx_position,ArrayList<Integer> Ly_position,BigDecimal[][] d) 
  {
      BigDecimal item_remove=new BigDecimal("0");
      if(level==0)
      {
          BigDecimal item_remove1=new BigDecimal("0");
          if(temp.size()==0)
              return new BigDecimal("0");
          BigDecimal oneL=new BigDecimal("1");
          for(int levelL=0;levelL<Ly.size();levelL++) 
          {
              for(int jL=Ly.size()-1;jL>=0;jL--) 
              {
                  Set item=new HashSet(Ly.get(jL));
                  item.retainAll(temp);
                  if(item.size()!=0)
                  {
                      //item_remove1=item_remove1+(oneL*inclusion_exclusion_all_common_sequence_s1(jL,G.get(position).get_position(),levelL,item,L,d));
                      item_remove1=item_remove1.add(oneL.multiply(inclusion_exclusion_all_common_sequence_s1(jL,Lx_position.get(position),levelL,item,Ly,Ly_position,d)));
                  }
              } 
              oneL=oneL.multiply(new BigDecimal("-1"));
          }
          return item_remove1;
      }
      else
      {
          for(int j=position-1;j>=0;j--)
          {
              Set temp1=new HashSet(Lx.get(j));
              temp1.retainAll(temp);
              if(temp1.size()!=0)
              {
                  level--;
                  item_remove=item_remove.add(inclusion_exclusion_all_common_sequence_s1_s2(j,level,temp1,Lx,Ly,Lx_position,Ly_position,d));
                  level++;
              }
          }            
      }
      return item_remove;
  } 
  
  
  public static BigDecimal  ACS_method(List<Vector> seq_list1,List<Vector> seq_list2) 
  {
      if(seq_list1.size()==0 || seq_list2.size()==0)
          return new BigDecimal("1");
    
      int seq_length1=seq_list1.size();
      int seq_length2=seq_list2.size();
      BigDecimal[][] d=prepare_matrix(seq_length1, seq_length2);     
    long start_time;
    long elapsedTimeMillis;
    float elapsedTimeSec;
  
    float time_total_Ly=0;
    float time_total_Lx=0;
    float time_total_inclusion_Ly=0;
    float time_total_inclusion_Ly_Lx=0;


      for(int i=0;i<seq_length1;i++) 
      {
          for(int j=0;j<seq_length2;j++)
          {
              Vector x=seq_list2.get(j);
              List<Vector> Ly=new ArrayList();
              ArrayList<Integer> Ly_position=new ArrayList();
              List<Vector> Lx=new ArrayList();
              ArrayList<Integer> Lx_position=new ArrayList();
              
               start_time= System.currentTimeMillis();
              Ly=Generate_L(i,seq_list1,x,Ly_position);
              //System.out.println(Ly_position);
              elapsedTimeMillis = System.currentTimeMillis()-start_time;
               elapsedTimeSec= elapsedTimeMillis/1000F;
            time_total_Ly=time_total_Ly+elapsedTimeSec;
       //    System.out.println("Time to calculate L_S2(length of L is "+Ly.size()+ " ) :"+elapsedTimeSec);

              
              BigDecimal item_remove=new BigDecimal("0");
              BigDecimal one=new BigDecimal("1");
              BigDecimal A=new BigDecimal("0");
              BigDecimal R=new BigDecimal("0");
              
              //d[i+1][j+1]=d[i+1][j];
              
              start_time = System.currentTimeMillis();
              
              for(int levely=0;levely<Ly.size();levely++) 
              {
                  for(int jj=Ly.size()-1;jj>=0;jj--) 
                  {
                      Set item=new HashSet(Ly.get(jj));
                      item_remove=item_remove.add(inclusion_exclusion_all_common_sequence_s1(jj,j,levely,item,Ly,Ly_position,d));
                      
                  }
             
                  //d[i+1][j+1]=d[i+1][j+1]+(one*item_remove); 
                  A=A.add(one.multiply(item_remove));
                 // System.out.println("A="+A);
                  //d[i+1][j+1]=d[i+1][j+1].add(one.multiply(item_remove));
                  item_remove=new BigDecimal("0");
                  //one=one*-1; 
                  one=one.multiply(new BigDecimal("-1"));
              }
              
              
            elapsedTimeMillis = System.currentTimeMillis()-start_time;
            elapsedTimeSec = elapsedTimeMillis/1000F;
          //      System.out.println("Time to calculate  Inclusion execlusion on L_S2 :"+elapsedTimeSec);
            time_total_inclusion_Ly=time_total_inclusion_Ly+elapsedTimeSec;
              
              
              if(Ly.size()!=0)
              {
                  
                 start_time = System.currentTimeMillis();  
                  
                Lx=Generate_L(j-1,seq_list2,x,Lx_position);
                 elapsedTimeMillis = System.currentTimeMillis()-start_time;
                 elapsedTimeSec = elapsedTimeMillis/1000F;
                 
                time_total_Lx=time_total_Lx+elapsedTimeSec; 
               // System.out.println("Time to calculate L_S1(length of L is "+Lx.size()+ " ) :"+elapsedTimeSec);
                 
                 

                //System.out.println("Lx="+Lx); 
                BigDecimal one_x_y=new BigDecimal("1");
                item_remove=new BigDecimal("0");
                
                
                
                start_time = System.currentTimeMillis();
                for(int levelx=0;levelx<Lx.size();levelx++) 
                {
                    for(int jG=Lx.size()-1;jG>=0;jG--) 
                    {
                        Set itemG=new HashSet(Lx.get(jG));
                        item_remove =item_remove.add(inclusion_exclusion_all_common_sequence_s1_s2(jG,levelx,itemG,Lx,Ly,Lx_position,Ly_position,d));
                        
                    }
                    //d[i+1][j+1]=d[i+1][j+1]-(one_x_y*item_remove);
                    R=R.add(one_x_y.multiply(item_remove));
                //  System.out.println("R="+R);
                    //d[i+1][j+1]=d[i+1][j+1].subtract(one_x_y.multiply(item_remove));
                    item_remove=new BigDecimal("0");
                    one_x_y=one_x_y.multiply(new BigDecimal("-1")); 
                }
                
                
                
                elapsedTimeMillis = System.currentTimeMillis()-start_time;
                 elapsedTimeSec = elapsedTimeMillis/1000F;
                 time_total_inclusion_Ly_Lx=time_total_inclusion_Ly_Lx+elapsedTimeSec;
              //  System.out.println("Time to calculate  Inclusion execlusion on L_S2 and L_S1 :"+elapsedTimeSec);
              }
            
           
              
            d[i+1][j+1]=d[i+1][j].add(A).subtract(R); 
              
            
          }
      }
  //  for(int i=0;i<d.length;i++) {
  //    System.out.println("\n");
  //      for(int j=0;j<d[i].length;j++)
   //         System.out.print(d[i][j]+" ");
   // }
 //    System.out.println("time to claculate Ly : "+time_total_Ly);
 //   System.out.println("time to claculate Lx : "+time_total_Lx);


   // System.out.println("time to claculate inclusion Ly : "+time_total_inclusion_Ly);


   // System.out.println("time to claculate Lx and Ly : "+time_total_inclusion_Ly_Lx);
   
   /*for(int i=0;i<d.length;i++)
     for (int j=0;j<d[i].length;j++)
         System.out.println("d["+i+"]["+j+"]="+d[i][j]);*/
      
      return d[d.length-1][d[0].length-1]; 
  }
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
  public static BigDecimal  ACS_approx_elias(List<Vector> seq_list1,List<Vector> seq_list2) 
  {
      if(seq_list1.size()==0 || seq_list2.size()==0)
          return new BigDecimal("1");
    
      int seq_length1=seq_list1.size();
      int seq_length2=seq_list2.size();
      BigDecimal[][] d=prepare_matrix(seq_length1, seq_length2);     
    long start_time;
    long elapsedTimeMillis;
    float elapsedTimeSec;
  
    float time_total_Ly=0;
    float time_total_Lx=0;
    float time_total_inclusion_Ly=0;
    float time_total_inclusion_Ly_Lx=0;


      for(int i=0;i<seq_length1;i++) 
      {
          for(int j=0;j<seq_length2;j++)
          {
              Vector x=seq_list2.get(j);
              int Ly;
              int Lx;
              Ly=get_first_intersection(i, seq_list1, x);
            
              BigDecimal item_remove=new BigDecimal("0");
              BigDecimal one=new BigDecimal("1");
              BigDecimal A=new BigDecimal("0");
              BigDecimal R=new BigDecimal("0");
              
              if(Ly!=-1) 
              {
                Set intersection = new HashSet(x);
                intersection.retainAll(seq_list1.get(Ly));  
                A= ((new BigDecimal("2").pow(intersection.size())).subtract(new BigDecimal(1))).multiply(d[Ly][j]); 
                
                Lx=get_first_intersection(j-1,seq_list2,x);
                if(Lx!=-1) 
                {
                  intersection = new HashSet(x);
                  intersection.retainAll(seq_list1.get(Ly));
                  intersection.retainAll(seq_list2.get(Lx));
                  R=((new BigDecimal("2").pow(intersection.size())).subtract(new BigDecimal(1))).multiply(d[Ly][Lx]); 
                }
                  
              }

              d[i+1][j+1]=d[i+1][j].add(A).subtract(R);     
          }
      }   
      return d[d.length-1][d[0].length-1]; 
  }
   
   
   
   
   //Toon
   
   
  public static List<Set> Generate_Power_Set(List<Vector> seq) {
      ArrayList<Set> output = new ArrayList<Set>();
      for (int i = 0; i < seq.size(); i++) {
          Powerset p = new Powerset(new HashSet(seq.get(i)));
          output.add(p.solve());
      }
      return output;
  }
   
public static BigDecimal ACS_Toon_Version1(List<Vector> seq1,List<Vector> seq2) 
{
  List<Set> power_set_seq1=Generate_Power_Set(seq1);
  List<Set> power_set_seq2=Generate_Power_Set(seq2);
  System.out.println("S1="+seq1);
  System.out.println("S2="+seq2);

  for(int n=0;n<seq1.size();n++)
      for(int m=0; m<seq2.size();m++) 
      {
          BigDecimal u[][]=new BigDecimal[n+1][m+1];
          Set itemset1=new HashSet(seq1.get(n));
          Set itemset2=new HashSet(seq2.get(m));
          itemset2.retainAll(itemset1);
          Powerset p = new Powerset(new HashSet(itemset2));
          Set intersection_set=p.solve();
          u[n][m]=new BigDecimal(intersection_set.size()+"");
          System.out.println("u["+n+"]["+m+"]="+u[n][m]);
          for(int i=n;i>=0;i--)
          {
            Set empty = new HashSet(0);
            Set union_set_seq1=new HashSet();
            union_set_seq1.add(empty); 
            for(int ii=i;ii<=n-1;ii++) 
              union_set_seq1.addAll(power_set_seq1.get(ii));
            for(int j=m;j>=0;j--) 
            {
              Set union_set_seq2=new HashSet();
              union_set_seq2.add(empty); 
              for(int jj=j;jj<=m-1;jj++) 
                union_set_seq2.addAll(power_set_seq2.get(jj));
              if(j==m)
              {
                if(i!=n) 
                {
                  Set temp_intersection_set=new HashSet(intersection_set);
                  temp_intersection_set.removeAll(union_set_seq1);
                  temp_intersection_set.removeAll(union_set_seq2);
                  u[i][j]= new BigDecimal(temp_intersection_set.size());
                  System.out.println("      u["+i+"]["+j+"]="+u[i][j]); 
                }
              }
              else
              {
                Set temp_intersection_set=new HashSet(intersection_set);
                temp_intersection_set.removeAll(union_set_seq1);
                temp_intersection_set.removeAll(union_set_seq2);
                u[i][j]= new BigDecimal(temp_intersection_set.size());
                System.out.println("      u["+i+"]["+j+"]="+u[i][j]); 
              }
            }
          }
        
      }                                
  return new BigDecimal("0");
}
   
   
   
  private static BigDecimal inclusion_exclusion_all_sub_sequence(int position,int level,Set temp,List<Vector> seq,int end) 
  {
     // System.out.println("Enter "+level+" Position:"+position+" end:"+end);
      BigDecimal x = new BigDecimal("0");
      if (level == 0) 
      {
          x=new BigDecimal("2").pow(temp.size());
         // System.out.print(" "+x);
          
      }
      else 
      {
        for (int j = position-1; j >=end; j--)
        {
          Set temp1 = new HashSet(seq.get(j));
          temp1.retainAll(temp);
          level--;
          BigDecimal v=new BigDecimal("1");
          v=inclusion_exclusion_all_sub_sequence(j, level, temp1, seq,end);
          x=x.add(v);
          level++;
        }
      }
      return x;
  }
  private static BigDecimal inclusion_exclusion_all_sub_sequence(int position,int level,Set temp,List<Vector> seq1,
                                                                      List<Vector> seq2,int sequence2_length,int j,int end) 
  {
      BigDecimal x = new BigDecimal("0");
      if (level == 0) 
      {     
          BigDecimal item_remove1=new BigDecimal("0");
          BigDecimal one=new BigDecimal("1");
          int end_j=sequence2_length-j-1;
          for(int levelj=0;levelj<end_j;levelj++) 
          {
              for (int z = sequence2_length-2 ; z >=j ; z--) 
              {
                  Set item=new HashSet(seq2.get(z));
                  item.retainAll(temp);
                  BigDecimal value=new BigDecimal("1");
                  value=inclusion_exclusion_all_sub_sequence(z,levelj,item,seq2,j);
                  item_remove1=item_remove1.add(one.multiply(value));
                  
              } 
              one=one.multiply(new BigDecimal("-1"));
          }
          return item_remove1;
      }
      else 
      {
        for (int z = position-1; z >=end; z--)
        {
          Set temp1 = new HashSet(seq1.get(z));
          temp1.retainAll(temp);
          level--;
          x=x.add(inclusion_exclusion_all_sub_sequence(z, level, temp1, seq1,seq2,sequence2_length,j,end));
          level++;
        }
      }
      return x;
  }
   
   
   
  public static BigDecimal insert_values_to_u_i(int i,Set intersection,List<Vector> seq,int sequence_length)
    {
      //System.out.println("seq[n]="+intersection);
        BigDecimal output =new BigDecimal("0");
        int one = 1;
        int end=sequence_length-i-1;
        for (int level = 0; level <end ; level++) 
        {
            //System.out.println("level:"+level);
          for (int z = sequence_length-2 ; z >=i ; z--) 
          {
              Set temp_item = new HashSet(seq.get(z));
              temp_item.retainAll(intersection);
              BigDecimal v=new BigDecimal("1");
              v=inclusion_exclusion_all_sub_sequence(z,level,temp_item,seq,i);
              output=output.add(new BigDecimal(one+"").multiply(v));
          }
            one=one*-1;
        }
        return output;

    } 
  
  
  public static BigDecimal insert_values_to_u_i_j(int i,int j,Set intersection,List<Vector> seq1,List<Vector> seq2,int sequence1_length,int sequence2_length)
    {
        BigDecimal output =new BigDecimal("0");
        int one =1;
        int end=sequence1_length-i-1;
        for (int level = 0; level <end ; level++) 
        {
         // System.out.print(one+"*(");
          for (int z = sequence1_length-2 ; z >=i ; z--) 
          {
              Set temp_item = new HashSet(seq1.get(z));
              temp_item.retainAll(intersection);
              BigDecimal t=new BigDecimal("1");
             // if(temp_item.size()!=0)
                t=inclusion_exclusion_all_sub_sequence(z,level,temp_item,seq1,seq2,sequence2_length,j,i);
              output=output.add(new BigDecimal(one+"").multiply(t));
           // System.out.print(")\n"+one+"*(");
              
          }
            one=one*-1;
        //System.out.println("");
        }
        return output;

    } 
   
   public static BigDecimal u(int i, int j,Set intersection,List<Vector> seq1,List<Vector> seq2,int seq1_length,int seq2_length) 
   {
  //   return new BigDecimal("2").pow(intersection.size()).subtract(insert_values_to_u_i_j(i,j,intersection,seq1,seq2,seq1_length,seq2_length)).add(new BigDecimal("1"));

     return new BigDecimal("2").pow(intersection.size()).subtract(insert_values_to_u_i(i,intersection,seq1,seq1_length)).subtract(insert_values_to_u_i(j,intersection,seq2,seq2_length)).add(insert_values_to_u_i_j(i,j,intersection,seq1,seq2,seq1_length,seq2_length)).add(new BigDecimal("1"));
   }
   
   
  public static BigDecimal u_new(int i, int j,Set intersection,List<Vector> seq1,List<Vector> seq2,int seq1_length,int seq2_length) 
  {
    Set item_i=new HashSet(seq1.get(i));
    Set item_j=new HashSet(seq1.get(j));
    Set item_i_j_1=new HashSet(seq1.get(i));
    Set item_i_j_2=new HashSet(seq1.get(j));
    
    item_i.retainAll(intersection);
    item_j.retainAll(intersection);
    item_i_j_1.retainAll(intersection);
    item_i_j_2.retainAll(item_i_j_1);
    BigDecimal value1=(new BigDecimal("-1").multiply(new BigDecimal("2").pow(item_i.size()))).add(insert_values_to_u_i(i+1,item_i,seq1,seq1_length-1));
    BigDecimal value2=(new BigDecimal("-1").multiply(new BigDecimal("2").pow(item_j.size()))).add(insert_values_to_u_i(j+1,item_j,seq1,seq2_length-1));
    
    BigDecimal value3=(new BigDecimal("-1").multiply(new BigDecimal("2").pow(item_i_j_2.size()))).add(insert_values_to_u_i_j(i+1,j+1,item_i_j_2,seq1,seq2,seq1_length-1,seq2_length-1));
    return value1.add(value2).subtract(value3);
  }
 
  public static BigDecimal ACS_Toon_Version2(List<Vector> seq1,List<Vector> seq2) 
  {
  //  List<Set> power_set_seq1=Generate_Power_Set(seq1);
  //  List<Set> power_set_seq2=Generate_Power_Set(seq2);
    System.out.println("S1="+seq1);
    System.out.println("S2="+seq2);
    for(int n=0;n<seq1.size();n++)
        for(int m=0; m<seq2.size();m++) 
        {
            BigDecimal u[][]=new BigDecimal[n+1][m+1];
            Set itemset1=new HashSet(seq1.get(n));
          
            Set itemset2=new HashSet(seq2.get(m));
           
            itemset2.retainAll(itemset1);
          
            u[n][m]=new BigDecimal("2").pow(itemset2.size());
            System.out.println("u["+n+"]["+m+"]="+u[n][m]);
            for(int i=n;i>=0;i--)
            {
              for(int j=m;j>=0;j--) 
              {
                if(j==m)
                {
                  if(i!=n) 
                  {
                    u[i][j]=u(i,j,itemset2,seq1,seq2,n+1,m+1);
                    System.out.println("      u["+i+"]["+j+"]="+u[i][j]);
                  }
                }
                else
                {
                  u[i][j]=u(i,j,itemset2,seq1,seq2,n+1,m+1);
                  System.out.println("      u["+i+"]["+j+"]="+u[i][j]); 
                }
              }
            }
          
        }                                
    return new BigDecimal("0");
  } 
   public static BigDecimal ACS_Toon_Version3(List<Vector> seq1,List<Vector> seq2) 
  {
  //  List<Set> power_set_seq1=Generate_Power_Set(seq1);
  //  List<Set> power_set_seq2=Generate_Power_Set(seq2);
    System.out.println("S1="+seq1);
    System.out.println("S2="+seq2);
    for(int n=0;n<seq1.size();n++)
        for(int m=0; m<seq2.size();m++) 
        {
            BigDecimal u[][]=new BigDecimal[n+1][m+1];
            Set itemset1=new HashSet(seq1.get(n));
          
            Set itemset2=new HashSet(seq2.get(m));
           
            itemset2.retainAll(itemset1);
          
            u[n][m]=new BigDecimal("2").pow(itemset2.size());
            System.out.println("u["+n+"]["+m+"]="+u[n][m]);
            for(int i=n;i>=0;i--)
            {
              for(int j=m;j>=0;j--) 
              {
                if(j==m)
                {
                  if(i!=n) 
                  {
                    //u[i][j]=u(i,j,itemset2,seq1,seq2,n+1,m+1);
                    u[i][j]=u[i+1][j].add(u[i][j+1]).subtract(u[i+1][j+1]).subtract(u_new(i,j,itemset2,seq1,seq2,n+1,m+1));
                    System.out.println("      u["+i+"]["+j+"]="+u[i][j]);
                  }
                }
                else
                {
              //    BigDecimal one=u[i][j];
                  BigDecimal two=u[i][j+1];
               //   BigDecimal three=u[i+1][j+1];
                  u[i][j]=u[i+1][j].add(u[i][j+1]).subtract(u[i+1][j+1]).subtract(u_new(i,j,itemset2,seq1,seq2,n+1,m+1));
                  System.out.println("      u["+i+"]["+j+"]="+u[i][j]); 
                }
              }
            }
          
        }                                
    return new BigDecimal("0");
  } 
   
   
   
   
   
   
   
   
    public static BigDecimal Approxinclusion_exclusion_all_common_sequence_s1_s2(int position, int level,Set temp,List<Vector> Lx,List<Vector> Ly,ArrayList<Integer> Lx_position,ArrayList<Integer> Ly_position,BigDecimal[][] d,int threshold)
    {
        
        BigDecimal item_remove=new BigDecimal("0");
        boolean doApproxL = false;
        double currentCoefficient = 0;
        if(level==0) 
        {
          BigDecimal item_remove1=new BigDecimal("0");
            if(temp.size()==0)
                return new BigDecimal ("0");
            double oneL=1;
            int N;
            if(Ly.size()==1)
                N=2;
            else
                N=Ly.size();
            int k = (int)(Math.ceil(Math.sqrt(Ly.size())));    
            IEApprox iea = new IEApprox(k,N);
            DoubleMatrix coefficients = iea.getCoefficients();
            for(int levelL=0;levelL<k;levelL++) 
            {
                currentCoefficient = coefficients.get(0,levelL);
                for(int jL=Ly.size()-1;jL>=0;jL--) 
                {
                    Set item=new HashSet(Ly.get(jL));
                    item.retainAll(temp);
                    if(item.size()!=0)
                        item_remove1=item_remove1.add(inclusion_exclusion_all_common_sequence_s1(jL,Lx_position.get(position),levelL,item,Ly,Ly_position,d).multiply(new BigDecimal(currentCoefficient)));
                } 
            }
            return item_remove1;
        }
        else 
        {
            
            for(int j=position-1;j>=0;j--) 
            {
                Set temp1=new HashSet(Lx.get(j));
                temp1.retainAll(temp);
                if(temp1.size()!=0)
                {
                    level--;
                    item_remove=item_remove.add(Approxinclusion_exclusion_all_common_sequence_s1_s2(j,level,temp1,Lx,Ly,Lx_position,Ly_position,d,threshold));
                    level++;
                }
            }

        }
        return item_remove;
    }
    	
	public static BigDecimal  approxNumberOfCommonSequences(List<Vector> seq_list1,List<Vector> seq_list2,int threshold) 
    {
        
        boolean doApproxLy = false;
        boolean doApproxLx = false;
        double currentCoefficient = 0;
        int seq_length1=seq_list1.size();
        int seq_length2=seq_list2.size();  
        BigDecimal[][] d=prepare_matrix(seq_length1, seq_length2); 
        int callz = 0;
        
        for(int i=0;i<seq_length1;i++) 
        {
            for(int j=0;j<seq_length2;j++)
            {
                BigDecimal A=new BigDecimal("0");
                BigDecimal R=new BigDecimal("0");
                Vector x=seq_list2.get(j);
                doApproxLx = false;
                doApproxLy =false;
                List<Vector> Ly=new ArrayList();
                ArrayList<Integer> Ly_position=new ArrayList();
                List<Vector> Lx=new ArrayList();
                ArrayList<Integer> Lx_position=new ArrayList();
                Ly=Generate_L(i,seq_list1,x,Ly_position);
                BigDecimal item_remove=new BigDecimal("0");
                double one=1;
                if(Ly.size() > threshold)
                    doApproxLy = true;                
                if(doApproxLy)
                {
                    int N;
                    if(Ly.size()==1)
                        N=2;
                    else
                        N=Ly.size();
                    int k= (int)(Math.ceil(Math.sqrt(Ly.size())));
                    IEApprox iea = new IEApprox(k,N);
                    DoubleMatrix coefficients = iea.getCoefficients();
                    for(int levely=0;levely<k;levely++) 
                    {
                        currentCoefficient = coefficients.get(0,levely);
                        for(int jj=Ly.size()-1;jj>=0;jj--) 
                        {
                            Set item=new HashSet(Ly.get(jj));
                            item_remove=item_remove.add(inclusion_exclusion_all_common_sequence_s1(jj,j,levely,item,Ly,Ly_position,d));
                        }
                        A=A.add(item_remove.multiply(new BigDecimal(currentCoefficient)));
                        item_remove=new BigDecimal("0");
                    }
                }
                else 
                {
                    for(int levely=0;levely<Ly.size();levely++) 
                    {
                        for(int jj=Ly.size()-1;jj>=0;jj--) 
                        {
                            Set item=new HashSet(Ly.get(jj));
                          item_remove=item_remove.add(inclusion_exclusion_all_common_sequence_s1(jj,j,levely,item,Ly,Ly_position,d));
                        }
                        A=A.add(item_remove.multiply(new BigDecimal(one)));
                        item_remove=new BigDecimal("0");
                        one=one*-1; 
                    }
                    
                } 
                if(Ly.size()!=0)
                {  
                  Lx=Generate_L(j-1,seq_list2,x,Lx_position);             
                  int one_x_y=1;
                  item_remove=new BigDecimal("0");
                  
                  if(Lx.size()==0)
                      R=new BigDecimal("0");
                else
                {
                  if(doApproxLy)
                  {      
             
                    int N;
                    if(Lx.size()==1)
                        N=2;
                    else
                        N=Lx.size();
                    int k= (int)(Math.ceil(Math.sqrt(Lx.size())));  
                    IEApprox iea = new IEApprox(k,N);
                    DoubleMatrix coefficients = iea.getCoefficients();
                    for(int levelx=0;levelx<k;levelx++) 
                    { 
                        currentCoefficient = coefficients.get(0,levelx);
                        for(int jG=Lx.size()-1;jG>=0;jG--) 
                        {
                            Set itemG=new HashSet(Lx.get(jG));
                            item_remove =item_remove.add(Approxinclusion_exclusion_all_common_sequence_s1_s2(jG,levelx,itemG,Lx,Ly,Lx_position,Ly_position,d,threshold));
                        }
                        R=R.add(item_remove.multiply(new BigDecimal(currentCoefficient)));
                        item_remove=new BigDecimal("0"); 
                    }
                  }
                  else 
                  {
                      for(int levelx=0;levelx<Lx.size();levelx++) 
                      {  
                          for(int jG=Lx.size()-1;jG>=0;jG--) 
                          {  
                            Set itemG=new HashSet(Lx.get(jG));
                            item_remove =item_remove.add(inclusion_exclusion_all_common_sequence_s1_s2(jG,levelx,itemG,Lx,Ly,Lx_position,Ly_position,d));
                              
                          }
                          R=R.add(item_remove.multiply(new BigDecimal(one_x_y)));
                          item_remove=new BigDecimal("0");
                          one_x_y=one_x_y*-1; 
                      }
                      
                  }
                }
                }
              //  System.out.println("A="+A+"------------R="+R);
               d[i+1][j+1]=d[i+1][j].add(A).subtract(R);
            }
        }
      		return d[d.length-1][d[0].length-1];
		
    }

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public static BigDecimal Approxinclusion_exclusion_all_common_sequence_s1_s2(int position, int level,Set temp,List<Vector> Lx,List<Vector> Ly,ArrayList<Integer> Lx_position,ArrayList<Integer> Ly_position,BigDecimal[][] d)
  {
      
      BigDecimal item_remove=new BigDecimal("0");
      boolean doApproxL = false;
      double currentCoefficient = 0;
      if(level==0) 
      {
          BigDecimal item_remove1=new BigDecimal("0");
          if(temp.size()==0)
              return new BigDecimal ("0");
          double oneL=1;
          if(Ly.size()>1)
          {
            int k = (int)(Math.ceil(Math.sqrt(Ly.size())));
            if(k==0)
                k++;
            IEApprox iea = new IEApprox(k,Ly.size());
            DoubleMatrix coefficients = iea.getCoefficients();
            for(int levelL=0;levelL<k;levelL++) 
            {
              currentCoefficient = coefficients.get(0,levelL);
              for(int jL=Ly.size()-1;jL>=0;jL--) 
              {
                Set item=new HashSet(Ly.get(jL));
                item.retainAll(temp);
                if(item.size()!=0)
                item_remove1=item_remove1.add(inclusion_exclusion_all_common_sequence_s1(jL,Lx_position.get(position),levelL,item,Ly,Ly_position,d).multiply(new BigDecimal(currentCoefficient)));
              } 
            }
          }
        else
        {
            for(int levelL=0;levelL<Ly.size();levelL++) 
            {
                for(int jL=Ly.size()-1;jL>=0;jL--) 
                {
                    Set item=new HashSet(Ly.get(jL));
                    item.retainAll(temp);
                    if(item.size()!=0)
                      item_remove1=item_remove1.add(inclusion_exclusion_all_common_sequence_s1(jL,Lx_position.get(position),levelL,item,Ly,Ly_position,d).multiply(new BigDecimal(oneL)));
                } 
                oneL=oneL*-1;
            }
        }
          return item_remove1;
      }
      else 
      {
          
          for(int j=position-1;j>=0;j--) 
          {
              Set temp1=new HashSet(Lx.get(j));
              temp1.retainAll(temp);
              if(temp1.size()!=0)
              {
                  level--;
                  item_remove=item_remove.add(Approxinclusion_exclusion_all_common_sequence_s1_s2(j,level,temp1,Lx,Ly,Lx_position,Ly_position,d));
                  level++;
              }
          }

      }
      return item_remove;
  }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
  public static BigDecimal  approxNumberOfCommonSequences(List<Vector> seq_list1,List<Vector> seq_list2)
  {
     
      double currentCoefficient = 0;
      int seq_length1=seq_list1.size();
      int seq_length2=seq_list2.size();

      BigDecimal[][] d=prepare_matrix(seq_length1, seq_length2); 
      int callz = 0;
      
      for(int i=0;i<seq_length1;i++) 
      {
          for(int j=0;j<seq_length2;j++)
          {
              BigDecimal A=new BigDecimal("0");
              BigDecimal R=new BigDecimal("0");
              Vector x=seq_list2.get(j);
             
              List<Vector> Ly=new ArrayList();
              ArrayList<Integer> Ly_position=new ArrayList();
              List<Vector> Lx=new ArrayList();
              ArrayList<Integer> Lx_position=new ArrayList();
              Ly=Generate_L(i,seq_list1,x,Ly_position);
              //System.out.println("Ly="+Ly);
              BigDecimal item_remove=new BigDecimal("0");
              double one=1;
              //d[i+1][j+1]=d[i+1][j];
              if(Ly.size()>1)
              {
                  int k = (int)(Math.ceil(Math.sqrt(Ly.size())));
                  IEApprox iea = new IEApprox(k,Ly.size());
              
                  DoubleMatrix coefficients = iea.getCoefficients();
          
                  for(int levely=0;levely<k;levely++) 
                  {
                      
                      currentCoefficient = coefficients.get(0,levely);
                    
                     // if(levely==0)
                      //  currentCoefficient=1;
                      for(int jj=Ly.size()-1;jj>=0;jj--) 
                      {
                          Set item=new HashSet(Ly.get(jj));
                          //item_remove=item_remove.add(inclusion_exclusion_all_common_sequence_s1(jj,j,levely,item,Ly,d));
                          item_remove=item_remove.add(inclusion_exclusion_all_common_sequence_s1(jj,j,levely,item,Ly,Ly_position,d));
                      }
                      //System.out.println("item_remove="+item_remove+" currentCoefficient="+currentCoefficient);
                      //d[i+1][j+1]=d[i+1][j+1].add(item_remove.multiply(currentCoefficient));  
                      //System.out.print(currentCoefficient+"*"+item_remove+" ");
                      A=A.add(item_remove.multiply(new BigDecimal(currentCoefficient)));
                      item_remove=new BigDecimal("0");
                  }
              }
            else 
            {
                for(int levely=0;levely<Ly.size();levely++) 
                {
                    for(int jj=Ly.size()-1;jj>=0;jj--) 
                    {
                        Set item=new HashSet(Ly.get(jj));
                      item_remove=item_remove.add(inclusion_exclusion_all_common_sequence_s1(jj,j,levely,item,Ly,Ly_position,d));
                    }
                    //d[i+1][j+1]=d[i+1][j+1]+(one*item_remove);  
                   // System.out.print(one+"*"+item_remove+" ");
                    A=A.add(item_remove.multiply(new BigDecimal(one)));
                    item_remove=new BigDecimal("0");
                    one=one*-1; 
                }
              //System.out.println("");
                
            }

                  //  System.out.println("k="+k+" N="+Ly.size());
                 // System.out.println("cccccc");
              
              if(Ly.size()!=0)
              {
              
                Lx=Generate_L(j-1,seq_list2,x,Lx_position);
                //System.out.println("Lx="+Lx);
                int one_x_y=1;
                item_remove=new BigDecimal("0");
                if(Lx.size()>1)
                {
                  int  k = (int)(Math.ceil(Math.sqrt(Lx.size())));
                 
                  IEApprox  iea = new IEApprox(k,Lx.size());
                DoubleMatrix    coefficients = iea.getCoefficients();
                    for(int levelx=0;levelx<k;levelx++) 
                    { 
                        currentCoefficient = coefficients.get(0,levelx);
                        for(int jG=Lx.size()-1;jG>=0;jG--) 
                        {
                            Set itemG=new HashSet(Lx.get(jG));
                           item_remove =item_remove.add(Approxinclusion_exclusion_all_common_sequence_s1_s2(jG,levelx,itemG,Lx,Ly,Lx_position,Ly_position,d));
                        }
                        //d[i+1][j+1]=d[i+1][j+1]-(currentCoefficient*item_remove);  
                        R=R.add(item_remove.multiply(new BigDecimal(currentCoefficient)));
                       // System.out.print(currentCoefficient+"*"+item_remove+" ");
                        item_remove=new BigDecimal("0"); 
                    }
                }
                else 
                {
                    for(int levelx=0;levelx<Lx.size();levelx++) 
                    {  
                        for(int jG=Lx.size()-1;jG>=0;jG--) 
                        {  
                          Set itemG=new HashSet(Lx.get(jG));
                          item_remove =item_remove.add(inclusion_exclusion_all_common_sequence_s1_s2(jG,levelx,itemG,Lx,Ly,Lx_position,Ly_position,d));
                            
                        }
                        //d[i+1][j+1]=d[i+1][j+1]-(one_x_y*item_remove);  
                        R=R.add(item_remove.multiply(new BigDecimal(one_x_y)));
                        item_remove=new BigDecimal("0");
                        one_x_y=one_x_y*-1; 
                    }
                    
                }
                   // System.out.println("");

            //System.out.println("d="+d[i+1][j].setScale(0, RoundingMode.UP)+" A="+A.setScale(0, RoundingMode.UP)+" R="+R.setScale(0, RoundingMode.UP));
              }
              
              
            d[i+1][j+1]=d[i+1][j].add(A).subtract(R);
          }
  }
    
  return d[d.length-1][d[0].length-1];
  
  }
  
   
  public static BigDecimal inclusion_exclusion_all_common_sequence_s1(int position,int m, int level,Set temp,List<Vector> L) 
  {
      BigDecimal x=new BigDecimal("0"); 
      if(level==0)
      {
        x= new BigDecimal(temp.size());
      }
      else 
      {
          for(int j=position-1;j>=0;j--) 
          {
              Set temp1=new HashSet(L.get(j));
              temp1.retainAll(temp);
              if(temp1.size()!=0)
              {
                  level--;
                  x=x.add(inclusion_exclusion_all_common_sequence_s1(j,m,level,temp1,L));
                  level++;
              }
          }  
      }
      return x;
  }
  
  
  
  
  public static List<Vector> pares_set(String set) 
  {
      List<Vector> L=new ArrayList();
      try 
      {
          String[] A=set.trim().split("-1");
          for(int i=0;i<A.length;i++)
          {
              String s = A[i];
              String[] items=s.trim().split(" ");
              
             // List list = Arrays.asList(items);
              Vector itemset =new Vector() ;
              for(int j=0;j<items.length;j++)
              {
                  itemset.addElement(items[j].trim());
              }
              
              L.add(itemset);
          }
          
      }
      catch(Exception e) {
          
      }
      return L;
  }
  
  
  public static void main(String args[]) {
   // System.out.println("Toon Solution 2222222222");  
    //System.out.println("--------------------------------------------------");
  
  try
  {
  
      List<Vector> sequence1 = Pars.pars_sequence_integer(args[0]);
      List<Vector> sequence2 = Pars.pars_sequence_integer(args[1]);
  
    System.out.println( "ACS Normal: "+ACS_method(sequence1,sequence2));
 
    }
  
  catch(Exception ee) {
    
  }

    
  }
  
  
  
	/*
   
    public static void main(String args[]) 
    {
        
        
        //      String seq1=args[0];
        //      String seq2=args[1];
        
        
        
        String seq1=args[0];
        String seq2=args[1];
        
        List<Vector> sequence1 = Pars.pars_sequence_integer(seq1);
        List<Vector> sequence2 = Pars.pars_sequence_integer(seq2);
        System.out.println("SIZE OF THE SEQUENCE1 IN ITEMSETS IS " + sequence1.size());
        System.out.println("SIZE OF THE SEQUENCE2 IN ITEMSETS IS " + sequence2.size());

        
        try 
        {
            // Get current time
            long start = System.currentTimeMillis();
        
            BigDecimal allsubsequence = ACS_method(sequence1,sequence2);
            // Get elapsed time in milliseconds
            long elapsedTimeMillis = System.currentTimeMillis()-start;
            
            // Get elapsed time in seconds
            float elapsedTimeSec = elapsedTimeMillis/1000F;
            System.out.println("Number of common subsequences between "+seq1+" and "+seq2+" = "+allsubsequence);
            System.out.println("Computation took " + elapsedTimeSec + " seconds " ); 
            System.out.println("----------------------------------------------------------------------");
            
            ACS_Toon_Version1(sequence1,sequence2);
          
            start = System.currentTimeMillis();
            
            
            //allsubsequence=ACS_method(pars_sequence_string(seq1),pars_sequence_string(seq2)); // To get sequences as strings like "<(abc)(a)>" 
            allsubsequence=approxNumberOfCommonSequences(sequence1,sequence2,8);
            System.out.println("Approximate number of common subsequences between "+seq1+" and "+seq2+" = "+allsubsequence);
            
            // Get elapsed time in milliseconds
            elapsedTimeMillis = System.currentTimeMillis()-start;
            
            // Get elapsed time in seconds
            elapsedTimeSec = elapsedTimeMillis/1000F;
            System.out.println("Computation took " + elapsedTimeSec + " seconds " ); 

            
        }
        catch(Exception eee) {
            System.out.println("error");
            
            
        }
        
    }
*/
  
  
  
 
  public static void inclusion_execlusion_value(List<Vector> L) 
  {
      BigDecimal item_remove=new BigDecimal("0");
      BigDecimal A=new BigDecimal("0");
      double one=1;
      for(int level=0;level<L.size();level++) 
      {
         // System.out.println("level"+level);
          for(int jj=L.size()-1;jj>=0;jj--) 
          {
            Set item=new HashSet(L.get(jj));
            item_remove=item_remove.add(inclusion_exclusion_all_common_sequence_s1(jj,level,item,L));
          }
          //d[i+1][j+1]=d[i+1][j+1]+(one*item_remove);  
         // System.out.print(one+"*"+item_remove+" ");
          A=A.add(item_remove.multiply(new BigDecimal(one)));
          item_remove=new BigDecimal("0");
          one=one*-1; 
      }
      System.out.println("A="+A);
  }
  
  
  
    public static void inclusion_execlusion_value_approx(List<Vector> L) 
    {
        BigDecimal item_remove=new BigDecimal("0");
        BigDecimal A=new BigDecimal("0");
        double currentCoefficient = 0;
        int k = (int)(Math.ceil(Math.sqrt(L.size())));
        IEApprox iea = new IEApprox(k,L.size());
        DoubleMatrix coefficients = iea.getCoefficients();
        for(int level=0;level<k;level++) 
        {
            currentCoefficient = coefficients.get(0,level);
            for(int jj=L.size()-1;jj>=0;jj--) 
            {
                Set item=new HashSet(L.get(jj));
                item_remove=item_remove.add(inclusion_exclusion_all_common_sequence_s1(jj,level,item,L));
            }
              //  System.out.print(currentCoefficient+"*"+item_remove+" ");
                A=A.add(item_remove.multiply(new BigDecimal(currentCoefficient)));
                item_remove=new BigDecimal("0");
            }
        System.out.println("A="+A);
    }
  
  
  
  
  
  
  
  
  
  public static BigDecimal Approxinclusion_exclusion_all_common_sequence_s1_s2(int position, int level,Set temp,List<Vector> Lx,List<Vector> Ly)
  {
      
      BigDecimal item_remove=new BigDecimal("0");
      boolean doApproxL = false;
      double currentCoefficient = 0;
      if(level==0) 
      {
        BigDecimal item_remove1=new BigDecimal("0");
          if(temp.size()==0)
              return new BigDecimal ("0");
          double oneL=1;
          if(Ly.size()>1)
          {
          int k = (int)(Math.ceil(Math.sqrt(Ly.size())));
          if(k==0)
              k++;
          IEApprox iea = new IEApprox(k,Ly.size());
          DoubleMatrix coefficients = iea.getCoefficients();
          for(int levelL=0;levelL<k;levelL++) 
          {
            currentCoefficient = coefficients.get(0,levelL);
            for(int jL=Ly.size()-1;jL>=0;jL--) 
            {
              Set item=new HashSet(Ly.get(jL));
              item.retainAll(temp);
              if(item.size()!=0)
              item_remove1=item_remove1.add(inclusion_exclusion_all_common_sequence_s1(jL,levelL,item,Ly).multiply(new BigDecimal(currentCoefficient)));
            } 
          }
          }
        else
        {
            for(int levelL=0;levelL<Ly.size();levelL++) 
            {
                for(int jL=Ly.size()-1;jL>=0;jL--) 
                {
                    Set item=new HashSet(Ly.get(jL));
                    item.retainAll(temp);
                    if(item.size()!=0)
                      item_remove1=item_remove1.add(inclusion_exclusion_all_common_sequence_s1(jL,levelL,item,Ly).multiply(new BigDecimal(oneL)));
                } 
                oneL=oneL*-1;
            }
        }
          return item_remove1;
      }
      else 
      {
          
          for(int j=position-1;j>=0;j--) 
          {
              Set temp1=new HashSet(Lx.get(j));
              temp1.retainAll(temp);
              if(temp1.size()!=0)
              {
                  level--;
                  item_remove=item_remove.add(Approxinclusion_exclusion_all_common_sequence_s1_s2(j,level,temp1,Lx,Ly));
                  level++;
              }
          }

      }
      return item_remove;
  }
  
  
  
  
  
  
  
  
  
  public static void inclusion_execlusion_value_L1_L2(List<Vector> Lx,List<Vector> Ly) 
  {
      BigDecimal item_remove=new BigDecimal("0");
      BigDecimal A=new BigDecimal("0");
      double one=1;
      for(int level=0;level<Lx.size();level++) 
      {
          System.out.println("level Lx:"+level);
          for(int jj=Lx.size()-1;jj>=0;jj--) 
          {
            Set item=new HashSet(Lx.get(jj));
            item_remove=item_remove.add(inclusion_exclusion_all_common_sequence_s1_s2(jj,level,item,Lx,Ly));
          }
          //d[i+1][j+1]=d[i+1][j+1]+(one*item_remove);  
         // System.out.print(one+"*"+item_remove+" ");
          A=A.add(item_remove.multiply(new BigDecimal(one)));
          item_remove=new BigDecimal("0");
          one=one*-1; 
      }
      System.out.println("A="+A);
  }
  
  
  public static void inclusion_execlusion_value_L1_L2_approx(List<Vector> Lx,List<Vector> Ly) 
  {
      BigDecimal item_remove=new BigDecimal("0");
      BigDecimal A=new BigDecimal("0");
      double currentCoefficient = 0;
      int k = (int)(Math.ceil(Math.sqrt(Lx.size())));
      IEApprox iea = new IEApprox(k,Lx.size());
      DoubleMatrix coefficients = iea.getCoefficients();
      for(int level=0;level<k;level++) 
      {
          System.out.println("level:"+level);
          currentCoefficient = coefficients.get(0,level);
          for(int jj=Lx.size()-1;jj>=0;jj--) 
          {
              Set item=new HashSet(Lx.get(jj));
            item_remove=item_remove.add(Approxinclusion_exclusion_all_common_sequence_s1_s2(jj,level,item,Lx,Ly));
          }
            //  System.out.print(currentCoefficient+"*"+item_remove+" ");
              A=A.add(item_remove.multiply(new BigDecimal(currentCoefficient)));
              item_remove=new BigDecimal("0");
          }
      System.out.println("A="+A);
  }
  
  
  
  
 
  
  
  
    public static BigDecimal inclusion_exclusion_all_common_sequence_s1(int position, int level,Set temp,List<Vector> L) 
    {
        BigDecimal x=new BigDecimal("0"); 
        if(level==0)
        {
           
          x= new BigDecimal(temp.size());
        }
        else 
        {
            for(int j=position-1;j>=0;j--) 
            {
                Set temp1=new HashSet(L.get(j));
                temp1.retainAll(temp);
                if(temp1.size()!=0)
                {
                    level--;
                    x=x.add(inclusion_exclusion_all_common_sequence_s1(j,level,temp1,L));
                    level++;
                }
            }  
        }
        return x;
    }
    
  public static BigDecimal inclusion_exclusion_all_common_sequence_s1_s2(int position, int level,Set temp,List<Vector> Lx,List<Vector> Ly) 
  {
      BigDecimal item_remove=new BigDecimal("0");
      if(level==0)
      {
          BigDecimal item_remove1=new BigDecimal("0");
          if(temp.size()==0)
              return new BigDecimal("0");
          BigDecimal oneL=new BigDecimal("1");
          for(int levelL=0;levelL<Ly.size();levelL++) 
          {
            //System.out.println("level Ly:"+levelL);
              for(int jL=Ly.size()-1;jL>=0;jL--) 
              {
                  Set item=new HashSet(Ly.get(jL));
                  item.retainAll(temp);
                  if(item.size()!=0)
                  {
                      item_remove1=item_remove1.add(oneL.multiply(inclusion_exclusion_all_common_sequence_s1(jL,levelL,item,Ly)));
                  }
              } 
              oneL=oneL.multiply(new BigDecimal("-1"));
          }
          return item_remove1;
      }
      else
      {
          for(int j=position-1;j>=0;j--)
          {
              Set temp1=new HashSet(Lx.get(j));
              temp1.retainAll(temp);
              if(temp1.size()!=0)
              {
                  level--;
                  //item_remove=item_remove+inclusion_exclusion_all_common_sequence_s1_s2(j,level,temp1,G,L,d);
                  item_remove=item_remove.add(inclusion_exclusion_all_common_sequence_s1_s2(j,level,temp1,Lx,Ly));
                  level++;
              }
          }            
      }
      return item_remove;
  }
  
   
}
