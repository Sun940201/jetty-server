import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
public class APIServlet {

    @GET
    @Path("hello/{name}")
    @Produces(MediaType.TEXT_PLAIN + ";charset=UTF8")
    public String sayHello(@PathParam("name") String name) {
        return "Hello, " + name;
    }

}