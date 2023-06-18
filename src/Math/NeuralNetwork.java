package Math;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

        Matrix hiddenMatrix = Matrix.matrixMultiplication(this.weightsIH, input);
        hiddenMatrix.add(this.biasH);
        hiddenMatrix.sigmoid();

        Matrix outpuMatrix = Matrix.matrixMultiplication(this.weightsHO, hiddenMatrix);
        outpuMatrix.add(this.biasO);
        outpuMatrix.sigmoid();

        return outpuMatrix;
    }

    public void mutate(double mutationRate) {
        this.weightsIH.mutate(mutationRate);
        this.weightsHO.mutate(mutationRate);

        this.biasH.mutate(mutationRate);
        this.biasO.mutate(mutationRate);
    }

    public NeuralNetwork copyAndMutate(double mutationRate) {
        NeuralNetwork copy = new NeuralNetwork(this.inputLayer.rows, this.hiddenLayer.rows, this.outputLayer.rows);

        copy.weightsIH = this.weightsIH.copy();
        copy.weightsHO = this.weightsHO.copy();

        copy.biasH = this.biasH.copy();
        copy.biasO = this.biasO.copy();

        copy.mutate(0.1);

        return copy;
    }

    public void save(String filename) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Neural network saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NeuralNetwork load(String filename) {
        NeuralNetwork network = null;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            network = (NeuralNetwork) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Neural network loaded from " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return network;
    }

}
