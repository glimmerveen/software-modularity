package accelerate.modularity.calculator;

import accelerate.modularity.api.Calculator;
import accelerate.modularity.api.Operations;
import org.osgi.annotation.bundle.Header;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

//@Header(name = Constants.BUNDLE_ACTIVATOR, value = "${@class}")
public class Activator implements BundleActivator {

    private final Object mutex = new Object();

    private ServiceReference<Operations> usedOperations;
    private ServiceRegistration<Calculator> calculatorRegistration;
    private CalculatorComponent calculatorComponent;
    private ServiceTracker<Operations, Operations> operationsTracker;

    @Override
    public void start(BundleContext context) throws Exception {
        operationsTracker = new ServiceTracker<>(context, Operations.class, new ServiceTrackerCustomizer<>() {
            @Override
            public Operations addingService(ServiceReference<Operations> reference) {
                Operations service = context.getService(reference);
                synchronized (mutex) {
                    if (calculatorRegistration == null) {
                        usedOperations = reference;
                        calculatorComponent = new CalculatorComponent(service);
                        calculatorRegistration = context.registerService(
                                Calculator.class,
                                calculatorComponent,
                                null
                        );
                    }
                }
                return service;
            }

            @Override
            public void modifiedService(ServiceReference<Operations> reference, Operations service) {

            }

            @Override
            public void removedService(ServiceReference<Operations> reference, Operations service) {
                synchronized (mutex) {
                    if (calculatorRegistration != null && reference.equals(usedOperations)) {
                        // Unregister the service, ensuring no-one is using it anymore
                        calculatorRegistration.unregister();
                        calculatorRegistration = null;
                        // Deactivate callback into component, such that it cleanup its resources.
                        calculatorComponent.stop();
                        calculatorComponent = null;
                        usedOperations = null;
                    }
                }
            }
        });

        operationsTracker.open();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        operationsTracker.close();
        synchronized (mutex) {
            if (calculatorRegistration != null) {
                // Unregister the service, ensuring no-one is using it anymore
                calculatorRegistration.unregister();
                calculatorRegistration = null;
                // Deactivate callback into component, such that it cleanup its resources.
                calculatorComponent.stop();
                calculatorComponent = null;
                usedOperations = null;
            }
        }
    }
}
