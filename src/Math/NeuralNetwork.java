package Math;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

// Neural network class for bird brain
// Note based heavily off of Daniel Shiffman's neural network class: https://github.com/kim-marcel/basic_neural_network/blob/master/src/main/java/basicneuralnetwork/NeuralNetwork.java
public class NeuralNetwork implements Serializable {

    // 3 layer neural network
    private Matrix inputLayer;
    private Matrix hiddenLayer;
    private Matrix outputLayer;

    // weights and biases
    private Matrix weightsIH;
    private Matrix weightsHO;

    private Matrix biasH;
    private Matrix biasO;

    // input layer -> hidden layer -> output layer
    // weightIH weightHO

    /**
     * Creates a new NeuralNetwork object with the specified number of input nodes,
     * hidden nodes, and output nodes.
     * 
     * @param inputNodes  The number of input nodes.
     * @param hiddenNodes The number of hidden nodes.
     * @param outputNodes The number of output nodes.
     * 
     */
    public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes) {
        // create layers
        this.inputLayer = new Matrix(inputNodes, 1);
        this.hiddenLayer = new Matrix(hiddenNodes, 1);
        this.outputLayer = new Matrix(outputNodes, 1);

        // Create weights and biases
        this.weightsIH = new Matrix(hiddenNodes, inputNodes);
        this.weightsHO = new Matrix(outputNodes, hiddenNodes);

        this.biasH = new Matrix(hiddenNodes, 1);
        this.biasO = new Matrix(outputNodes, 1);

        // randomize weights and biases
        this.weightsIH.randomize();
        this.weightsHO.randomize();

        this.biasH.randomize();
        this.biasO.randomize();
    }

    /**
     * Feeds the input matrix through the neural network and returns the output
     * 
     * @param input The input matrix
     * @return The output matrix
     * @throws Exception If the input matrix dimensions do not match the input layer
     *                   dimensions
     */
    public Matrix feedForward(Matrix input) throws Exception {
        // check if input matrix dimensions match input layer dimensions
        if (input.rows != this.inputLayer.rows || input.columns != this.inputLayer.columns) {
            System.out.println("INPUT ROWS: " + input.rows + " INPUT COLUMNS: " + input.columns);
            System.out.println(
                    "INPUT LAYER ROWS: " + this.inputLayer.rows + " INPUT LAYER COLUMNS: " + this.inputLayer.columns);
            throw new Exception("Input matrix dimensions must match input layer dimensions");
        }

        // calculate hidden layer
        Matrix hiddenMatrix = Matrix.matrixMultiplication(this.weightsIH, input);
        hiddenMatrix.add(this.biasH);
        // normalize (basically activation function)
        hiddenMatrix.sigmoid();

        // calculate output layer
        Matrix outpuMatrix = Matrix.matrixMultiplication(this.weightsHO, hiddenMatrix);
        outpuMatrix.add(this.biasO);
        outpuMatrix.sigmoid();

        // return output matrix
        return outpuMatrix;
    }

    /**
     * Mutates the weights and biases of the neural network
     * 
     * @param mutationRate The rate at which the neural network mutates
     */
    public void mutate(double mutationRate) {
        this.weightsIH.mutate(mutationRate);
        this.weightsHO.mutate(mutationRate);

        this.biasH.mutate(mutationRate);
        this.biasO.mutate(mutationRate);
    }

    /**
     * Creates a copy of the neural network and mutates it
     * 
     * @param mutationRate The rate at which the neural network mutates
     * @return The mutated copy of the neural network
     */
    public NeuralNetwork copyAndMutate(double mutationRate) {
        NeuralNetwork copy = new NeuralNetwork(this.inputLayer.rows, this.hiddenLayer.rows, this.outputLayer.rows);

        copy.weightsIH = this.weightsIH.copy();
        copy.weightsHO = this.weightsHO.copy();

        copy.biasH = this.biasH.copy();
        copy.biasO = this.biasO.copy();

        copy.mutate(0.1);

        return copy;
    }

    /**
     * Creates a copy of the neural network and serializes it
     * 
     * @param filename The name of the file to save the neural network to
     * 
     */
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

    /**
     * Loads a neural network from a file
     * 
     * @param filename The name of the file to load the neural network from
     * @return
     */
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
