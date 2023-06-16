package Math;

import java.util.Random;

public class NeuralNetwork {

    private Matrix inputLayer;
    private Matrix hiddenLayer;
    private Matrix outputLayer;

    private Matrix weightsIH;
    private Matrix weightsHO;

    private Matrix biasH;
    private Matrix biasO;

    public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes) {
        this.inputLayer = new Matrix(inputNodes, 1);
        this.hiddenLayer = new Matrix(hiddenNodes, 1);
        this.outputLayer = new Matrix(outputNodes, 1);

        this.weightsIH = new Matrix(hiddenNodes, inputNodes);
        this.weightsHO = new Matrix(outputNodes, hiddenNodes);

        this.biasH = new Matrix(hiddenNodes, 1);
        this.biasO = new Matrix(outputNodes, 1);

        this.weightsIH.randomize();
        this.weightsHO.randomize();

        this.biasH.randomize();
        this.biasO.randomize();
    }

    public Matrix feedForward(Matrix input) throws Exception {
        if (input.rows != this.inputLayer.rows || input.columns != this.inputLayer.columns) {
            System.out.println("INPUT ROWS: " + input.rows + " INPUT COLUMNS: " + input.columns);
            System.out.println(
                    "INPUT LAYER ROWS: " + this.inputLayer.rows + " INPUT LAYER COLUMNS: " + this.inputLayer.columns);
            throw new Exception("Input matrix dimensions must match input layer dimensions");
        }

        this.hiddenLayer = Matrix.matrixMultiplication(this.weightsIH, this.inputLayer);
        this.hiddenLayer.add(this.biasH);
        this.hiddenLayer.sigmoid();

        this.outputLayer = Matrix.matrixMultiplication(this.weightsHO, this.hiddenLayer);
        this.outputLayer.add(this.biasO);
        this.outputLayer.sigmoid();

        return this.outputLayer;
    }

    public void mutate(double mutationRate) {
        this.weightsIH.mutate(mutationRate);
        this.weightsHO.mutate(mutationRate);

        this.biasH.mutate(mutationRate);
        this.biasO.mutate(mutationRate);
    }

    public static NeuralNetwork crossOver(NeuralNetwork brain1, NeuralNetwork brain2) {
        NeuralNetwork offspring = new NeuralNetwork(brain1.inputLayer.rows, brain1.hiddenLayer.rows,
                brain1.outputLayer.rows);

        Random random = new Random();

        // Determine crossover points
        int crossoverPoint1 = random.nextInt(offspring.weightsIH.rows);
        int crossoverPoint2 = random.nextInt(offspring.weightsHO.rows);

        // Copy weights from brain1 to offspring
        for (int i = 0; i < crossoverPoint1; i++) {
            for (int j = 0; j < offspring.weightsIH.columns; j++) {
                offspring.weightsIH.matrix[i][j] = brain1.weightsIH.matrix[i][j];
            }
        }

        for (int i = 0; i < crossoverPoint2; i++) {
            for (int j = 0; j < offspring.weightsHO.columns; j++) {
                offspring.weightsHO.matrix[i][j] = brain1.weightsHO.matrix[i][j];
            }
        }

        // Copy weights from brain2 to offspring
        for (int i = crossoverPoint1; i < brain2.weightsIH.rows; i++) {
            for (int j = 0; j < offspring.weightsIH.columns; j++) {
                offspring.weightsIH.matrix[i][j] = brain2.weightsIH.matrix[i][j];
            }
        }

        for (int i = crossoverPoint2; i < brain2.weightsHO.rows; i++) {
            for (int j = 0; j < offspring.weightsHO.columns; j++) {
                offspring.weightsHO.matrix[i][j] = brain2.weightsHO.matrix[i][j];
            }
        }

        return offspring;
    }

}
