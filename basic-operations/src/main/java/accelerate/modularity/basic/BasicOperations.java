package accelerate.modularity.basic;

import accelerate.modularity.api.Operations;

public class BasicOperations implements Operations {
    @Override
    public double add(double lhs, double rhs) {
        return lhs+rhs;
    }

    @Override
    public double subtract(double lhs, double rhs) {
        return lhs-rhs;
    }

    @Override
    public double multiply(double lhs, double rhs) {
        return lhs*rhs;
    }

    @Override
    public double divide(double lhs, double rhs) {
        return lhs/rhs;
    }
}
