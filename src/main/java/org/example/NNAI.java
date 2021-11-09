package org.example;

import org.neuralnetwork.*;

public class NNAI {

    private final GamePanel gamePanel;
    DenseLayer dense1 = new DenseLayer(6, 6);
    ActivationReLU activation1 = new ActivationReLU();
    DenseLayer dense2 = new DenseLayer(6, 3);
    ActivationSoftmaxLossCategoricalCrossentropy lossActivation = new ActivationSoftmaxLossCategoricalCrossentropy();
    OptimizerAdam optimizerAdam = new OptimizerAdam();
    OptimizerSGD sgd = new OptimizerSGD();

    double[][] dense1dWeights;
    double[][] dense1dBiases;

    double[][] dense2dWeights;
    double[][] dense2dBiases;

    private int experienceLoops = 0;

    public NNAI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void tick(Ball ball, Player player) {
        double[][] input = {{
                ball.x / (double) GamePanel.GAME_WIDTH,
                ball.y / (double) GamePanel.GAME_HEIGHT,
                ball.vX / 10.0,
                ball.vY / 10.0,
                player.x / (double) GamePanel.GAME_WIDTH,
                player.y / (double) GamePanel.GAME_HEIGHT}};

        dense1.forward(input);
        activation1.forward(dense1.output);
        dense2.forward(activation1.output);
        double[][] normValues = lossActivation.activationSoftmax.getNormValues(dense2.output);

        lossActivation.forward(dense2.output, normValues);

        //double forward = lossActivation.forward(dense2.output, normValues);

        //System.out.println("loss:" + forward);

        double[][] output = lossActivation.output;
        int argmax = Tools.argmax(output[0]);
        if (argmax == 0) {
            player.goUp();
            player.dontGoDown();
            output = new double[][]{{1, 0, 0}};
        }
        if (argmax == 1) {
            player.dontGoUp();
            player.dontGoDown();
            output = new double[][]{{0, 1, 0}};

        }
        if (argmax == 2) {
            player.goDown();
            player.dontGoUp();
            output = new double[][]{{0, 0, 1}};

        }

        lossActivation.forward(dense2.output, output);
        lossActivation.backward(lossActivation.output, output);
        dense2.backward(lossActivation.dInputs);
        activation1.backward(dense2.dInputs);
        dense1.backward(activation1.dInputs);
        experienceLoops++;

        //momentum
        if (dense1dWeights == null) {
            dense1dWeights = Matrix.zeroesLike(dense1.dWeights);
            dense1dBiases = Matrix.zeroesLike(dense1.dBiases);
            dense2dWeights = Matrix.zeroesLike(dense2.dWeights);
            dense2dBiases = Matrix.zeroesLike(dense2.dBiases);
        }
        dense2dWeights = Matrix.addForEach(dense2dWeights, dense2.dWeights);
        dense2dBiases = Matrix.addForEach(dense2dBiases, dense2.dBiases);
        dense1dWeights = Matrix.addForEach(dense1dWeights, dense1.dWeights);
        dense1dBiases = Matrix.addForEach(dense1dBiases, dense1.dBiases);


        if (gamePanel.stats.didPlayer2JustHitBall()) {
//            double[][] weights1 = Matrix.copyOf(dense1.weights);
//            double[][] weights2 = dense2.weights;
            carrot();
            //SGDCarrot();
//            System.out.println("---start---");
//            Matrix.printMatrix(Matrix.addForEach(Matrix.scalar(-1, dense1.weights), weights1));
//            Matrix.printMatrix(Matrix.addForEach(Matrix.scalar(-1, dense2.weights), weights2));
//            System.out.println(Matrix.sumd(Matrix.addForEach(Matrix.scalar(-1, dense1.weights), weights1)));
//            System.out.println(Matrix.sumd(Matrix.addForEach(Matrix.scalar(-1, dense2.weights), weights2)));
//            System.out.println("---end-----");

        } else if (gamePanel.stats.player1WonLastRound) {
            stick();
        }

    }

    private void SGDStick() {
        System.out.println("---------stick------------");
        double scale = -1;
        //normalizeMomentum();
        dense1.dWeights = Matrix.scalar(scale, dense1.dWeights);
        dense1.dBiases = Matrix.scalar(scale, dense1.dBiases);
        sgd.updateParams(dense1);
        dense2.dWeights = Matrix.scalar(scale, dense2.dWeights);
        dense2.dBiases = Matrix.scalar(scale, dense2.dBiases);
        sgd.updateParams(dense2);
        resetMomentum();
    }

    private void SGDCarrot() {
        System.out.println("---------carrot------------");
        double scale = 1;
        //normalizeMomentum();
        dense1.dWeights = Matrix.scalar(scale, dense1.dWeights);
        dense1.dBiases = Matrix.scalar(scale, dense1.dBiases);
        sgd.updateParams(dense1);
        dense2.dWeights = Matrix.scalar(scale, dense2.dWeights);
        dense2.dBiases = Matrix.scalar(scale, dense2.dBiases);
        sgd.updateParams(dense2);
        resetMomentum();
    }


    private void resetMomentum() {
        experienceLoops = 0;
        dense1dWeights = null;
        dense1dBiases = null;
        dense2dWeights = null;
        dense2dBiases = null;
    }

    private void normalizeMomentum() {
        dense1.dWeights = Matrix.applyFunction(d -> d / experienceLoops, dense1dWeights);
        dense1.dBiases = Matrix.applyFunction(d -> d / experienceLoops, dense1dBiases);
        dense2.dWeights = Matrix.applyFunction(d -> d / experienceLoops, dense2dWeights);
        dense2.dBiases = Matrix.applyFunction(d -> d / experienceLoops, dense2dBiases);
    }

    private void carrot() {
        System.out.println("---------carrot------------");
        double scale = 1;
        normalizeMomentum();
        optimizerAdam.preUpdate();
        dense1.dWeights = Matrix.scalar(scale, dense1.dWeights);
        dense1.dBiases = Matrix.scalar(scale, dense1.dBiases);
        optimizerAdam.update(dense1);
        dense2.dWeights = Matrix.scalar(scale, dense2.dWeights);
        dense2.dBiases = Matrix.scalar(scale, dense2.dBiases);
        optimizerAdam.update(dense2);
        optimizerAdam.postUpdate();
        resetMomentum();
    }

    private void stick() {
        System.out.println("---------stick------------");
        double scale = -1;
        normalizeMomentum();
        optimizerAdam.preUpdate();
        dense1.dWeights = Matrix.scalar(scale, dense1.dWeights);
        dense1.dBiases = Matrix.scalar(scale, dense1.dBiases);
        optimizerAdam.update(dense1);
        dense2.dWeights = Matrix.scalar(scale, dense2.dWeights);
        dense2.dBiases = Matrix.scalar(scale, dense2.dBiases);
        optimizerAdam.update(dense2);
        optimizerAdam.postUpdate();
        resetMomentum();
    }

}
