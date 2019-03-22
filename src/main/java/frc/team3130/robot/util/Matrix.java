package frc.team3130.robot.util;

public class Matrix {
    Double[][] body;
    int nRows, nCols;

    public Matrix(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.body = new Double[nRows][nCols];
    }

    public Matrix(Double[][] values) {
        this.nRows = values.length;
        this.nCols = values[0].length;
        for (int i = 0; i < this.nRows; ++i) {
            if (values[i].length != nCols) {
                throw new IllegalArgumentException("Row " + i + " has " + values[i].length + ". Should be " + nCols);
            }
        }
        this.body = values;
    }

    public Double get(int i, int j) {
        return this.body[i][j];
    }

    public void put(int i, int j, Double a) {
        this.body[i][j] = a;
    }

    public Matrix multiply(Matrix other) {
        if (other.nRows == 1) { // Other is just a vector
            if (this.nCols != other.nCols) {
                throw new IllegalArgumentException("A:nCols: " + this.nCols + " did not match B-vector size " + other.nCols + ".");
            }
            Matrix C = new Matrix(1, this.nRows);
            for (int i = 0; i < this.nRows; i++) {
                Double sum = 0.0;
                for (int k = 0; k < this.nCols; k++) {
                    sum += this.body[i][k] * other.body[0][k];
                }
                C.body[0][i] = sum;
            }
            return C;
        }

        if (this.nCols != other.nRows) {
            throw new IllegalArgumentException("A:nCols: " + this.nCols + " did not match B:nRows " + other.nRows + ".");
        }
        Matrix C = new Matrix(this.nRows, other.nCols);
        for (int i = 0; i < this.nRows; i++) { // aRow
            for (int j = 0; j < other.nCols; j++) { // bColumn
                Double sum = 0.0;
                for (int k = 0; k < this.nCols; k++) { // aColumn
                    sum += this.body[i][k] * other.body[k][j];
                }
                C.body[i][j] = sum;
            }
        }
        return C;
    }

    // This main() is just a unit test. Should print out an identity matrix.
    public static void main(String[] args) {

        Double[][] A = { { 4.00, 3.00 }, { 2.00, 1.00 } };
        Double[][] B = { { -0.500, 1.500 }, { 1.000, -2.0000 } };
        Matrix matrixA = new Matrix(A);
        Matrix matrixB = new Matrix(B);
        Matrix result = matrixA.multiply(matrixB);

        for (int i = 0; i < result.nRows; i++) {
            for (int j = 0; j < result.nCols; j++)
                System.out.print(result.get(i, j) + " ");
            System.out.println();
        }

        Double[][] V = {{1.0, 2.0}};
        Matrix matrixV = new Matrix(V);
        System.out.format("Rows %d, Cols %d%n", matrixV.nRows, matrixV.nCols);
        Matrix result1 = matrixA.multiply(matrixV);
        for (int i = 0; i < result1.nRows; i++) {
            for (int j = 0; j < result1.nCols; j++)
                System.out.print(result1.get(i, j) + " ");
            System.out.println();
        }
    }
}
