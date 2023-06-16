package Math;

public class NeuralNetwork {

    private Matrix inputLayer;
    private Matrix hiddenLayer;
    private Matrix outputLayer;

    private Matrix weightsIH;
    private Matrix weightsHO;

    public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes) {
        this.inputLayer = new Matrix(inputNodes, 1);
        this.hiddenLayer = new Matrix(hiddenNodes, 1);
        this.outputLayer = new Matrix(outputNodes, 1);

        this.weightsIH = new Matrix(hiddenNodes, inputNodes);
        this.weightsHO = new Matrix(outputNodes, hiddenNodes);

        this.weightsIH.randomize();
        this.weightsHO.randomize();
    }

    public Matrix feedForward(Matrix input) throws Exception {
        if (input.rows != this.inputLayer.rows || input.columns != this.inputLayer.columns) {
            System.out.println("INPUT ROWS: " + input.rows + " INPUT COLUMNS: " + input.columns);
            System.out.println("INPUT LAYER ROWS: " + this.inputLayer.rows + " INPUT LAYER COLUMNS: " + this.inputLayer.columns);
            throw new Exception("Input matrix dimensions must match input layer dimensions");
        }


        this.hiddenLayer = Matrix.matrixMultiplication(this.weightsIH, this.inputLayer);
        this.hiddenLayer.sigmoid();

        this.outputLayer = Matrix.matrixMultiplication(this.weightsHO, this.hiddenLayer);
        this.outputLayer.sigmoid();

        return this.outputLayer;
    }

}
