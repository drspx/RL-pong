package org.neuralnetwork;


import static org.neuralnetwork.Matrix.applyFunction;

public class ActivationReLU {

    public double[][] output;
    public double[][] input;
    public double[][] dInputs;

    public double[][] forward(double[][] input) {
        this.input = input;
        output = applyFunction(n -> Math.max(0, n), input);
        return output;
    }

    public double[][] backward(double[][] dValues) {
        this.dInputs = applyFunction((n, m) -> n<=0 ? 0 : m, input, dValues);
        return this.dInputs;
    }

}
