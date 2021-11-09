package org.neuralnetwork;

public class BinaryLogisticRegression {

    public double[][] applySigmoid(double[][] d) {
        return Matrix.applyFunction(this::sigmoid, d);
    }

    public double sigmoid(double d) {
        return 1.0 / (1.0 + Math.exp(-d));
    }

    public double sigmoidDerivative(double d) {
        return sigmoid(d) * (1 - sigmoid(d));
    }

}
