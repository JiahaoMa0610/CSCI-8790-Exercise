package target;

@Table(name = "SERVICE_APP", id = 201)
@Author(name = "SA", year = 2001)
public class ServiceApp {

   @Row(name = "SERVICE_NAME", id = 202)
   @Author(name = "SB", year = 2002)
   String serviceName;
   
   @Row(name = "SERVICE_PROVIDER", id = 203)
   @Author(name = "SC", year = 2003)
   String serviceProvider;
   
   @Column(name = "SERVICE_LOCATION", id = 204)
   @Author(name = "SD", year = 2004)
   String serviceLocation;

}
