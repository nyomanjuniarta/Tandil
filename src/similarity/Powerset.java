package similarity;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
/**
 * @author eegho
 *
 */
public class Powerset 
{
	//private vars
	private Set s;  // inputed set
	private Set ps; // result powerset
	
	public Powerset(Set set){this.s = set;}

	
	public Set solve()
	{
		if(s.isEmpty()) 
		{
			Set empty = new HashSet(0);
			Set set = new HashSet();
			set.add(empty);
			ps = set;
			return set;
		}
		else 
		{
			Iterator i = s.iterator();
			Object obj = i.next();
			s.remove(obj);
			Set set = solve();
			Set set1 = solve();
			for(Iterator j = set1.iterator(); j.hasNext();) 
			{
				Set tmp = new HashSet((Set) j.next());
				tmp.add(obj);
				set.add(tmp);
  			}
			s.add(obj);
			ps = set;
			return set;
		}
	
	}
	
	public Set getPowerset(){return ps;}
	public Set getSet(){return s;}

}
