package org.neuralnetwork;

import java.util.Arrays;

import static org.neuralnetwork.Matrix.*;


public class ActivationSoftmax {

    public double[][] output;
    public double[][] dInputs;

    public double[][] forward(double[][] inputs) {
        output = getNormValues(inputs);
        return output;
    }


    public double[][] getNormValues(double[][] input) {
        double[][] normalized = new double[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            normalized[i] = getNormValues(input[i]);
        }
        return normalized;
    }

    public double[] getNormValues(double[] input) {
        double[] exp = Arrays.stream(input).map(Math::exp).toArray();
        double normBase = Arrays.stream(exp).sum();
        return Arrays.stream(exp).map(d -> d / normBase).toArray();
    }

    public double[][] backward(double[][] dValues) {
        dInputs = zeroesLike(dValues);
        for (int i = 0; i < this.output.length; i++) {
            double[][] singleOutput = reshape(output[i], output[i].length, 1);
            double[][] jacobianMatrix = applyFunction((n1, n2) -> (n1 - n2), diagFlat(singleOutput), dot(singleOutput, transpose(singleOutput)));
            this.dInputs[i] = dot(jacobianMatrix, dValues[i]);
        }
        return dInputs;
    }

}
