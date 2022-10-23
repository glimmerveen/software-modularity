package accelerate.modularity.basic;

import accelerate.modularity.api.Operations;
import org.osgi.annotation.bundle.Header;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

import static java.util.Map.of;
import static org.osgi.framework.FrameworkUtil.asDictionary;

@Header(name = Constants.BUNDLE_ACTIVATOR, value = "${@class}")
public class Activator implements BundleActivator {

    private ServiceRegistration<Operations> calculatorServiceRegistration;

    @Override
    public void start(BundleContext bundleContext) {
        calculatorServiceRegistration = bundleContext.registerService(
                Operations.class,
                new BasicOperations(),
                asDictionary(of("operations", "basic"))
        );
    }

    @Override
    public void stop(BundleContext bundleContext) {
        // Note that services are tied to the lifecycle of the BundleContext they are registered with: when a bundle is
        // stopped, the BundleContext is invalidated and all services registered using that context are invalidated and
        // removed. As such explicitly unregistering in the way done here is not strictly needed.
        calculatorServiceRegistration.unregister();
    }
}
