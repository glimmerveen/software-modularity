package accelerate.modularity.api;

import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface Operations {
    double add(double lhs, double rhs);
    double subtract(double lhs, double rhs);
    double multiply(double lhs, double rhs);
    double divide(double lhs, double rhs);
}
