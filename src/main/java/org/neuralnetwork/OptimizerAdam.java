package org.neuralnetwork;

public class OptimizerAdam {

    public double learningRate = 0.005;
    public double decay = 5e-7;
    public double epsilon = 1e-7;
    public double beta1 = 0.9;
    public double beta2 = 0.999;

    public double currentLearningRate = learningRate;
    public double iterations = 0;


    public void preUpdate() {
        if (this.decay == 0) {
            currentLearningRate = this.learningRate * (1 / (1 + decay * iterations));
        }
    }

    public void update(DenseLayer layer) {

        if (layer.weightMomentums == null) {
            layer.weightMomentums = Matrix.zeroesLike(layer.weights);
            layer.biasMomentums = Matrix.zeroesLike(layer.bias);
            layer.weightCache = Matrix.zeroesLike(layer.weights);
            layer.biasCache = Matrix.zeroesLike(layer.bias);
        }

        layer.weightMomentums = Matrix.addForEach(
                Matrix.scalar(this.beta1, layer.weightMomentums),
                Matrix.scalar((1 - this.beta1), layer.dWeights));

        layer.biasMomentums = Matrix.addForEach(
                Matrix.scalar(this.beta1, layer.biasMomentums),
                Matrix.scalar((1 - this.beta1), layer.dBiases));

        double v = 1 - Math.pow(this.beta1, this.iterations + 1);
        double[][] weightMomentumsCorrected = Matrix.applyFunction(a -> a / v, layer.weightMomentums);
        double[][] biasMomentumsCorrected = Matrix.applyFunction(a -> a / v, layer.biasMomentums);

        layer.weightCache = Matrix.addForEach(
                Matrix.scalar(this.beta2, layer.weightCache),
                Matrix.scalar(1 - beta2, Matrix.applyFunction(a -> Math.pow(a, 2), layer.dWeights)));
        layer.biasCache = Matrix.addForEach(
                Matrix.scalar(this.beta2, layer.biasCache),
                Matrix.scalar(1 - beta2, Matrix.applyFunction(a -> Math.pow(a, 2), layer.dBiases)));

        double v1 = 1 - Math.pow(beta2, this.iterations + 1);
        double[][] weightCacheCorrected = Matrix.applyFunction(a -> a / v1, layer.weightCache);
        double[][] biasCacheCorrected = Matrix.applyFunction(a -> a / v1, layer.biasCache);

        double[][] calcRes1 = Matrix.scalar(-this.currentLearningRate, weightMomentumsCorrected);
        double[][] calcRes2 = Matrix.applyFunction(a -> Math.sqrt(a) + this.epsilon, weightCacheCorrected);
        double[][] calcRes3 = Matrix.applyFunction((a, b) -> a / b, calcRes1, calcRes2);
        layer.weights = Matrix.addForEach(layer.weights, calcRes3);

        double[][] calcRes4 = Matrix.scalar(-this.currentLearningRate, biasMomentumsCorrected);
        double[][] calcRes5 = Matrix.applyFunction(a -> Math.sqrt(a) + this.epsilon, biasCacheCorrected);
        double[][] calcRes6 = Matrix.applyFunction((a, b) -> a / b, calcRes4, calcRes5);
        layer.bias = Matrix.addForEach(layer.bias, calcRes6);


    }

    public void postUpdate() {
        ++iterations;
    }


}
