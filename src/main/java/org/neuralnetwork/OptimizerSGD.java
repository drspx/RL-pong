package org.neuralnetwork;

public class OptimizerSGD {

    public double learningRate = 0.1;


    public void updateParams(DenseLayer layer) {

        layer.weights = Matrix.applyFunction((n, m) -> -learningRate * n+m, layer.dWeights, layer.weights);
        layer.bias = (Matrix.applyFunction((n, m) -> -learningRate * n+m, layer.dBiases, layer.bias));
    }
}
