package accelerate.modularity.calculator;

import accelerate.modularity.api.Operations;

import java.util.Arrays;

public enum Operator {
    PLUS('+') {
        @Override
        public double apply(Operations operations, double one, double two) {
            return operations.add(one, two);
        }
    },
    MINUS('-') {
        @Override
        public double apply(Operations operations, double one, double two) {
            return operations.subtract(one, two);
        }
    },
    MULTIPLY('*') {
        @Override
        public double apply(Operations operations, double one, double two) {
            return operations.multiply(one, two);
        }
    },
    DIVIDE('/') {
        @Override
        public double apply(Operations operations, double one, double two) {
            return operations.divide(one, two);
        }
    };

    private final char character;

    Operator(char c) {
        this.character = c;
    }

    public abstract double apply(Operations operations, double one, double two);

    public static Operator parse(char c) {
        return Arrays.stream(Operator.values()).filter(operator -> c == operator.character).findFirst().orElseThrow();
    }
}
