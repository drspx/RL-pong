package org.neuralnetwork;


public class Main {

    public void main(String[] args) {
        new Main().launch(Tools.spiralDataX(), Tools.spiralDatay());
    }


    private void launch(double[][] dataX, double[] datay) {

        int epochs = 10000;

        DenseLayer dense1 = new DenseLayer(2, 64);
        ActivationReLU activation1 = new ActivationReLU();
        DenseLayer dense2 = new DenseLayer(64, 3);
        ActivationSoftmaxLossCategoricalCrossentropy lossActivation = new ActivationSoftmaxLossCategoricalCrossentropy();
        OptimizerAdam optimizerAdam = new OptimizerAdam();

        double loss = 0;
        for (int i = 0; i < epochs; i++) {

            dense1.forward(dataX);
            activation1.forward(dense1.output);
            dense2.forward(activation1.output);
            loss = lossActivation.forward(dense2.output, datay);
            loss += lossActivation.regularizationLoss(dense1) + lossActivation.regularizationLoss(dense2);

            lossActivation.backward(lossActivation.output, datay);
            dense2.backward(lossActivation.dInputs);
            activation1.backward(dense2.dInputs);
            dense1.backward(activation1.dInputs);

            optimizerAdam.preUpdate();
            optimizerAdam.update(dense1);
            optimizerAdam.update(dense2);
            optimizerAdam.postUpdate();

            if (i % 100 == 0) {
                System.out.print("epoch:" + i);
                System.out.format("  acc:%.4f ", Tools.accuracy(lossActivation.output, datay));
                System.out.format(" loss:%.4f ", loss);
                System.out.println();
            }

        }
        System.out.println("---------------------result--------------------");
        System.out.println("acc:" + Tools.accuracy(lossActivation.output, datay));
        System.out.println("loss:" + loss);


    }

}
