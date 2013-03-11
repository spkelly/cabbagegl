import java.util.*;

public class HitData {
   private List<HitPoint> hitpoints;

   public HitData() {
      hitpoints = new ArrayList<HitPoint>();
   }

   public void addHitpoint(HitPoint toAdd) {
      hitpoints.add(toAdd);
   }

   public List<HitPoint> getHitpoints() {
      return Collections.unmodifiableList(hitpoints);
   }

   public void sort() {
      Collections.sort(hitpoints);
   }

   public int size() {
      return hitpoints.size();
   }
   
   public boolean isEmpty() {
      return hitpoints.isEmpty();
   }

}
