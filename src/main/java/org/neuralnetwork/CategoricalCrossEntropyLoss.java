package org.neuralnetwork;

import java.util.Arrays;

import static org.neuralnetwork.Matrix.applyFunction;


public class CategoricalCrossEntropyLoss extends Loss {

    public double[] output;
    public double[][] dInputs;

    public double[] forward(double[][] input, double[][] target) {
        output = getEntropyLoss(input, target);
        return output;
    }

    public double[] forward(double[][] input, double[] target) {
        output = getEntropyLoss(input, target);
        return output;
    }

    public double[][] backward(double[][] dValue, double[][] yTrue) {
        double samples = Arrays.stream(dValue).count();
        this.dInputs = applyFunction((n1, n2) -> (-n1 / n2) / samples, yTrue, dValue);
        return dInputs;
    }


    public double[][] backward(double[][] dValue, double[] yTrue) {
        return backward(dValue, Tools.convertToOneHot(yTrue, dValue[0].length));
    }

    private double[] getTargetForEntropyLossCalculation(double[][] output, double[] target) {
        double[] actual = new double[target.length];
        for (int i = 0; i < target.length; i++) {
            actual[i] = output[i][(int) target[i]];
        }
        return actual;
    }

    public double[] getEntropyLoss(double[][] output, double[] target) { //returning negative log likelihood
        return Arrays.stream(getTargetForEntropyLossCalculation(output, target))
                .map(n -> -findApproximateLog(n)).toArray();
    }

    public double findApproximateLog(double n) {
        double smallNumber = 1e-7;
        if (n >= 1.0) {
            n = 1.0 - smallNumber;
        } else if (n <= smallNumber) {
            n = smallNumber;
        }
        return Math.log(n);
    }

    public double getEntropyLoss(double[] output, double[] target) {
        double result = 0;
        for (int i = 0; i < target.length; i++) {
            result += findApproximateLog(output[i]) * target[i];
        }
        return -result;
    }

    public double[] getEntropyLoss(double[][] output, double[][] target) {
        double[] result = new double[target.length];
        for (int i = 0; i < target.length; i++) {
            result[i] = getEntropyLoss(output[i], target[i]);
        }
        return result;
    }

}
