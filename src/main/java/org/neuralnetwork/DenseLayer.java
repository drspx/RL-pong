package org.neuralnetwork;


import static org.neuralnetwork.Matrix.*;

public class DenseLayer {

    public final int inputSize;
    public final int neurons;

    public double[][] weights;
    public double[][] bias;
    public double[][] output;
    public double[][] input;
    public double[][] dWeights;
    public double[][] dBiases;
    public double[][] dInputs;

    public double[][] weightMomentums;
    public double[][] biasMomentums;
    public double[][] weightCache;
    public double[][] biasCache;

    public double weightRegularizerL1 = 0;
    public double weightRegularizerL2 = 0;
    public double biasRegularizerL1 = 0;
    public double biasRegularizerL2 = 0;


    public DenseLayer(int inputSize, int neurons) {
        weights = random(inputSize, neurons);
        bias = zeros(1, neurons);
        this.inputSize = inputSize;
        this.neurons = neurons;
    }

    public double[][] forward(double[][] input) {
        this.input = input;
        output = addForEach(dot(input, weights), bias);
        return output;
    }

    public void backward(double[][] dValues) {
        this.dWeights = dot(transpose(this.input), dValues);
        this.dBiases = sumAxis0(dValues);
        this.dInputs = dot(dValues, transpose(this.weights));
    }

    public void optimizeWeights(double[][] doubles) {
        this.weights = Matrix.applyFunction((n, m) -> n * m, doubles, weights);
    }

    public void optimizeBiases(double[][] doubles) {
        this.bias = Matrix.applyFunction((n, m) -> n * m, doubles, bias);
    }

}
