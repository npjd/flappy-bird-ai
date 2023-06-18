package Math;

import java.io.Serializable;

// matrix class for neural network 
public class Matrix implements Serializable {
    int rows, columns;
    public double[][] matrix;

    /**
     * Creates a new Matrix object with the specified number of rows and columns.
     *
     * @param rows    The number of rows in the matrix.
     * @param columns The number of columns in the matrix.
     */
    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.matrix = new double[rows][columns];
    }

    /*
     * Randomizes the values in the matrix.
     */
    public void randomize() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++)
                this.matrix[i][j] = Math.random();
        }
    }

    /**
     * Adds a matrix to this matrix.
     * 
     * @param m the matrix to add
     */
    public void add(Matrix m) {
        // check if matrix dimensions match
        if (this.rows == m.rows && this.columns == m.columns) {
            // add each value in the matrix
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.columns; j++)
                    this.matrix[i][j] += m.matrix[i][j];
            }
        } else {
            System.out.println("Matrix dimensions must match");
        }
    }

    /**
     * Takes sigmoid of each value in the matrix.
     * 
     */
    public void sigmoid() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++)
                // F(x) = 1 / (1 + e^-x)
                this.matrix[i][j] = 1 / (1 + Math.exp(-this.matrix[i][j]));
        }
    }

    /**
     * 
     * Mutation function for genetic algorithm. Basically adds a random value to
     * each value in the matrix.
     * 
     * @param mutationRate the rate at which the matrix mutates
     */
    public void mutate(double mutationRate) {
        // check if mutation rate is valid
        if (mutationRate >= 1 || mutationRate <= 0) {
            System.out.println("Mutation rate must be between 0 and 1");
            return;
        }
        // mutate each value in the matrix
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                // if random number is less than mutation rate, mutate
                if (Math.random() < mutationRate) {
                    this.matrix[i][j] += Math.random() * 0.1 - 0.05;
                }
            }
        }

    }

    /**
     * Returns a matrix based on a 2D array.
     * 
     * @param arr the 2D array to convert to a matrix
     * 
     * @return a matrix based on a 2D array
     */
    public static Matrix fromArray(double[][] arr) {
        int rows = arr.length;
        int columns = arr[0].length;
        // create a new matrix with the same dimensions as the array
        Matrix m = new Matrix(rows, columns);

        // set the values of the matrix to the values of the array
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                m.matrix[i][j] = arr[i][j];
            }
        }
        return m;
    }

    /**
     * Does matrix multiplication on two matrices. Returns a new matrix.
     * 
     * @param m1 the first matrix
     * 
     * @param m2 the second matrix
     * 
     * @return the product of the two matrices
     */
    public static Matrix matrixMultiplication(Matrix m1, Matrix m2) throws Exception {
        // check if matrix dimensions match
        if (m1.columns == m2.rows) {
            // create a new matrix with the correct dimensions
            Matrix matrixProduct = new Matrix(m1.rows, m2.columns);
            for (int i = 0; i < m1.rows; i++) {
                for (int j = 0; j < m2.columns; j++) {
                    for (int k = 0; k < m1.columns; k++) {
                        // multiply each value in the matrix and add it to the product matrix
                        matrixProduct.matrix[i][j] += m1.matrix[i][k] * m2.matrix[k][j];
                    }
                }
            }

            return matrixProduct;
        } else {
            throw new Exception("m1.rows must be equal to m2.columns");
        }
    }

    /**
     * Returns a copy of the matrix
     * .
     * 
     * @return a copy of the matrix
     */
    public Matrix copy() {
        Matrix m = new Matrix(this.rows, this.columns);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++)
                m.matrix[i][j] = this.matrix[i][j];
        }
        return m;
    }

}
