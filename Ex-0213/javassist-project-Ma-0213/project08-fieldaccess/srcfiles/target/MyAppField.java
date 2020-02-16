package target;

public class MyAppField {
   int x = 11, y = 17;

   public void move(int dx, int dy) {
      setX(dx);
      setY(dy);
   }

   int setX(int dx) {
      this.x = dx;
      return x;
   }

   int setY(int dy) {
      this.y = dy;
      return y;
   }

   public static void main(String[] args) throws Exception {
      System.out.println("[MyAppField] Run...");
      MyAppField a = new MyAppField();
      a.move(11, 14);
      System.out.println("[MyAppField] Done. ");
   }
}
