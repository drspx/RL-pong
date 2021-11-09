package org.neuralnetwork;

public class OptimizerSGDWithMomentum {

    public double learningRate = 1;
    public double currentLearningRate = learningRate;
    public double decay = 8e-8;
    public double iterations = 0;

    public double momentum = 0.9;
    private double[] weightMomentum;
    private double[][] biasMomentum;

    public void preUpdateParameters() {
        if (decay != 0) {
            currentLearningRate = learningRate * (1 / (1 + decay * iterations));
        }
    }

    public void postUpdateParameters() {
        ++iterations;
    }


    public void updateParamsWithMomentum(DenseLayer layer) {
        if (layer.weightMomentums == null) {
            layer.weightMomentums = Matrix.zeroesLike(layer.weights);
            layer.biasMomentums = Matrix.zeroesLike(layer.bias);
        }

        double[][] scalar = Matrix.scalar(momentum, layer.weightMomentums);
        double[][] scalar1 = Matrix.scalar(currentLearningRate, layer.dWeights);

        layer.weightMomentums = Matrix.applyFunction((a, b) -> (a - b), scalar, scalar1);

        layer.weights = Matrix.applyFunction((n, m) -> -currentLearningRate * n + m, layer.dWeights, layer.weights);
        layer.bias = (Matrix.applyFunction((n, m) -> -currentLearningRate * n + m, layer.dBiases, layer.bias));
    }
}
