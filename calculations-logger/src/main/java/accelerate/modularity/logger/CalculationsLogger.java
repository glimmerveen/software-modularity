package accelerate.modularity.logger;

import accelerate.modularity.api.CalculationListener;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = CalculationListener.class)
public class CalculationsLogger implements CalculationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationsLogger.class);

    @Override
    public void calculationPerformed(String term, double result) {
        LOGGER.info("{} = {}", term, result);
    }
}
