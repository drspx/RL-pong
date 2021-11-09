package org.neuralnetwork;

public class ActivationSoftmaxLossCategoricalCrossentropy extends Loss{

    public ActivationSoftmax activationSoftmax = new ActivationSoftmax();
    CategoricalCrossEntropyLoss loss = new CategoricalCrossEntropyLoss();
    public double[][] output;
    public double[][] dInputs;


    public double forward(double[][] inputs, double[] ys) {
        this.activationSoftmax.forward(inputs);
        this.output = this.activationSoftmax.output;
        return calculate(loss, output, ys);
    }

    public double forward(double[][] inputs, double[][] ys) {
        this.activationSoftmax.forward(inputs);
        this.output = this.activationSoftmax.output;
        return calculate(loss, output, ys);
    }

    public double[][] backward(double[][] dvalues, double[][] ys) {
        int samples = dvalues.length;
        dInputs = Matrix.addForEach(dvalues, Matrix.applyFunction(n -> -n, ys));
        dInputs = Matrix.applyFunction(d -> (d / (double) samples), dInputs);
        return dInputs;
    }

    public double[][] backward(double[][] dvalues, double[] ys) {
        return backward(dvalues, Tools.convertToOneHot(ys, dvalues[0].length));
    }

}
