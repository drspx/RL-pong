package org.neuralnetwork;

import java.util.Arrays;

public class Loss {

    public static double calculate(CategoricalCrossEntropyLoss loss, double[][] input, double[] y) {
        double[] losses = loss.forward(input, y);
        return Arrays.stream(losses).average().orElse(-1);
    }

    public static double calculate(CategoricalCrossEntropyLoss loss, double[][] input, double[][] y) {
        double[] losses = loss.forward(input, y);
        return Arrays.stream(losses).average().orElse(-1);
    }

    public static double L1(double lambda, double[][] weights) {
        return Matrix.sumd(Matrix.applyFunction(n -> Math.abs(n) * lambda, weights));
    }

    public static double L2(double lambda, double[][] weights) {
        return Matrix.sumd(Matrix.applyFunction(n -> Math.sqrt(n) * lambda, weights));
    }

    public double regularizationLoss(DenseLayer layer) {
        double regularizationLoss = 0;
        if (layer.weightRegularizerL1 > 0) {
            regularizationLoss += L1(layer.weightRegularizerL1, layer.weights);
        }
        if (layer.weightRegularizerL2 > 0) {
            regularizationLoss += L2(layer.weightRegularizerL2, layer.weights);
        }
        if (layer.biasRegularizerL1>0){
            regularizationLoss += L1(layer.biasRegularizerL1, layer.bias);
        }
        if (layer.biasRegularizerL2>0){
            regularizationLoss+= L2(layer.biasRegularizerL2, layer.bias);
        }
        return regularizationLoss;
    }

}
