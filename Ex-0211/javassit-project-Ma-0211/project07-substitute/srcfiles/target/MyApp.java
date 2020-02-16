package target;

public class MyApp {
   int x = 10, y = 10;

   public void draw() {
      move(1, 1);
   }

   public void move(int dx, int dy) {
      x = dx;
      y = dy;
      System.out.println("\t[MyApp] x: " + x + ", y: " + y);
      System.out.println("\t[MyApp] dx: " + dx + ", dy: " + dy);
   }

   public static void main(String[] args) throws Exception {
      System.out.println("[MyApp] Run...");
      MyApp a = new MyApp();
      a.draw();
      System.out.println("[MyApp] Done.");
   }
}
