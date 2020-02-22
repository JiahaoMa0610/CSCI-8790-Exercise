package target;

@Table(name = "Annotated_Point", id = 1)
@Author(name = "JK", year = 1998)
public class AnnotatedPoint {

   @Column(name = "X", id = 2)
   @Author(name = "MJ", year = 1995)
   int x;
   int y;
}
