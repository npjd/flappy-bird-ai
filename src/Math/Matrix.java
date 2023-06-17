package Math;

public class Matrix {
    int rows, columns;
    public double[][] matrix;

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.matrix = new double[rows][columns];
    }

    public void randomize() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++)
                this.matrix[i][j] = Math.random();
        }
    }

    public void add(Matrix m) {
        if (this.rows == m.rows && this.columns == m.columns) {
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.columns; j++)
                    this.matrix[i][j] += m.matrix[i][j];
            }
        } else {
            System.out.println("Matrix dimensions must match");
        }
    }

    public void sigmoid() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++)
                this.matrix[i][j] = 1 / (1 + Math.exp(-this.matrix[i][j]));
        }
    }

    public void mutate(double mutationRate) {

        if (mutationRate >= 1 || mutationRate <= 0) {
            System.out.println("Mutation rate must be between 0 and 1");
            return;
        }
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (Math.random() < mutationRate) {
                    this.matrix[i][j] += Math.random() * 0.1 - 0.05;
                }
            }
        }

    }

    public static Matrix fromArray(double[][] arr) {
        int rows = arr.length;
        int columns = arr[0].length;

        Matrix m = new Matrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                m.matrix[i][j] = arr[i][j];
            }
        }
        return m;
    }

    public static Matrix matrixMultiplication(Matrix m1, Matrix m2) throws Exception {
        if (m1.columns == m2.rows) {
            Matrix matrixProduct = new Matrix(m1.rows, m2.columns);
            for (int i = 0; i < m1.rows; i++) {
                for (int j = 0; j < m2.columns; j++) {
                    for (int k = 0; k < m1.columns; k++) {
                        matrixProduct.matrix[i][j] += m1.matrix[i][k] * m2.matrix[k][j];
                    }
                }
            }

            return matrixProduct;
        } else {
            throw new Exception("m1.rows must be equal to m2.columns");
        }
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < this.rows; i++) {
            s += "[";
            for (int j = 0; j < this.columns; j++) {
                s += this.matrix[i][j] + ", ";
            }
            s += "]\n";
        }
        return s;
    }

}
