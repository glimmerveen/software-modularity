package accelerate.modularity.endpoint;

import accelerate.modularity.api.Calculator;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Component(
        service = Endpoint.class,
        scope = ServiceScope.PROTOTYPE
)
@JaxrsResource
public class Endpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(Endpoint.class);

    private final Calculator calculator;

    @Activate
    public Endpoint(@Reference Calculator calculator) {
        this.calculator = calculator;
    }

    @GET
    @Path("calculate/{terms}")
    @Produces("text/plain")
    public String calculate(@PathParam("terms") String terms) {
        LOGGER.info("calculate({})", terms);
        return "%s = %f".formatted(terms, calculator.calculate(terms));
    }
}
