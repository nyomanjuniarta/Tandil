package similarity;

/**
 *
 * @author eegho
 */

import java.util.Set;

public class  intersection 
{
    private int position;
    private Set intersection;
    public boolean used=true;
    
    
    public intersection() {
        
    }
    
    public intersection(int position,Set intersection) {
      super();

        this.position=position;
        this.intersection=intersection;
        
    }
    
    
    
    
    public int get_position() {
      return position;
    }
    
    
    
  public Set get_Set() {
    return intersection;
  }
  
  public void not_used() {
    used=false;
  }
}