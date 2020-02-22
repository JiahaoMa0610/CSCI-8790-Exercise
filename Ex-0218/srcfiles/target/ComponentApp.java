package target;

@Table(name = "COMPONENT_APP", id = 101)
@Author(name = "CA", year = 1991)
public class ComponentApp {

   @Column(name = "COMPONENT_NAME", id = 102)
   @Author(name = "CB", year = 1992)
   String componentName;
   
   @Column(name = "COMPONENT_PROVIDER", id = 103)
   @Author(name = "CC", year = 1993)
   String componentProvider;
   
   @Row(name = "COMPONENT_LOCATION", id = 104)
   @Author(name = "CD", year = 1994)
   String componentLocation;

}
