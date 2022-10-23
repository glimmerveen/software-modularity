package accelerate.modularity.cli;

import accelerate.modularity.api.Calculator;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        service = CalculatorCli.class,
        property = {
                "osgi.command.scope=accelerate",
                "osgi.command.function=calculate"
        }
)
public class CalculatorCli {
    private final Calculator calculator;

    @Activate
    public CalculatorCli(@Reference Calculator calculator) {
        this.calculator = calculator;
    }

    public void calculate(String input) {
        System.out.printf("%s = %f%n", input, calculator.calculate(input));
    }
}
