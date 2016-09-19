package org.itat.index;

/**
*
* @author Peter Karich, info@jetsli.de
*/
public class StopWatch {

   private long lastTime;
   private long time;
   private String name = "";

   public StopWatch(String name) {
       this.name = name;
   }
   
   public StopWatch() {        
   }

   public StopWatch setName(String name) {
       this.name = name;
       return this;
   }

   public StopWatch start() {
       lastTime = System.currentTimeMillis();
       return this;
   }

   public StopWatch stop() {
       if (lastTime < 0)
           return this;
       time += System.currentTimeMillis() - lastTime;
       lastTime = -1;
       return this;
   }

   /**
    * @return the delta time in milliseconds
    */
   public long getTime() {
       return time;
   }

   @Override
   public String toString() {
       String str = "";
       if(!name.isEmpty())
           str += name + " ";
       
       return str + "time:" + getSeconds();
   }

   public float getSeconds() {
       return time / 1000f;
   }
}