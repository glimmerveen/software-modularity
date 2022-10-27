package accelerate.modularity.calculator;

import accelerate.modularity.api.CalculationListener;
import accelerate.modularity.api.Calculator;
import accelerate.modularity.api.Operations;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

//@Component(
//        service = Calculator.class
//)
public class CalculatorComponent implements Calculator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculatorComponent.class);

    private final Operations operations;
    private final List<CalculationListener> calculationListeners = new CopyOnWriteArrayList<>();

//    @Activate
    public CalculatorComponent(@Reference Operations operations) {
        this.operations = operations;
        LOGGER.info("Activated: {}", operations);
    }

    @Override
    public double calculate(String term) {
        List<Object> items = parseTerm(term);

        double value = Double.NaN;
        Iterator<Object> iterator = items.iterator();
        while (iterator.hasNext()) {
            double val1;
            if (Double.isNaN(value)) {
                val1 = (Double) iterator.next();
            } else {
                val1 = value;
            }
            Operator operator = (Operator) iterator.next();
            double val2 = (Double) iterator.next();
            value = operator.apply(operations, val1, val2);
        }

        final double result = value;

        calculationListeners.forEach(listener -> listener.calculationPerformed(term, result));

        return result;
    }

    private List<Object> parseTerm(String input) {
        List<Object> items = new ArrayList<>();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        tokenizer.ordinaryChar('-');
        tokenizer.ordinaryChar('/');

        try {
            while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
                if (tokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                    items.add(tokenizer.nval);
                } else {
                    items.add(Operator.parse((char) tokenizer.ttype));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Whoops", e);
        }
        return items;
    }

//    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE, unbind = "calculationListenerRemoved")
    public void calculationListenerAdded(CalculationListener calculationListener) {
        calculationListeners.add(calculationListener);
    }

    public void calculationListenerRemoved(CalculationListener calculationListener) {
        calculationListeners.remove(calculationListener);
    }

//    @Deactivate
    public void stop() {
        LOGGER.info("Deactivate");
    }
}
