package frc.team3130.robot.util;

public class Matrix {
    double[][] body;
    int nRows, nCols;

    public Matrix(int nRows, int nCols) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.body = new double[nRows][nCols];
    }

    public Matrix(double[][] values) {
        this.nRows = values.length;
        this.nCols = values[0].length;
        for (int i = 0; i < this.nRows; ++i) {
            if (values[i].length != nCols) {
                throw new IllegalArgumentException("Row " + i + " has " + values[i].length + ". Should be " + nCols);
            }
        }
        this.body = values;
    }

    public Matrix(double x, double y, double z) {
        this.nRows = 1;
        this.nCols = 3;
        this.body = new double[][]{{x, y, z}};
    }

    public double get(int i, int j) {
        return this.body[i][j];
    }

    public void put(int i, int j, double a) {
        this.body[i][j] = a;
    }

    public Matrix multiply(double m) {
        Matrix result = new Matrix(this.nRows, this.nCols);
        for (int i = 0; i < this.nRows; i++) {
            for (int j = 0; j < this.nCols; j++) {
                result.body[i][j] = m * this.body[i][j];
            }
        }
        return result;
    }

    public Matrix multiply(Matrix other) {
        if (other.nRows == 1) { // Other is just a vector
            if (this.nCols != other.nCols) {
                throw new IllegalArgumentException("A:nCols: " + this.nCols + " did not match B-vector size " + other.nCols + ".");
            }
            Matrix C = new Matrix(1, this.nRows);
            for (int i = 0; i < this.nRows; i++) {
                double sum = 0.0;
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
                double sum = 0.0;
                for (int k = 0; k < this.nCols; k++) { // aColumn
                    sum += this.body[i][k] * other.body[k][j];
                }
                C.body[i][j] = sum;
            }
        }
        return C;
    }

    public Matrix add(Matrix other) {
        Matrix sum = new Matrix(this.nRows, this.nCols);
        for (int i = 0; i < this.nRows; i++) {
            for (int j = 0; j < this.nCols; j++) {
                sum.body[i][j] = this.body[i][j] + (i < other.nRows && j < other.nCols ? other.body[i][j] : 0.0);
            }
        }
        return sum;
    }

    public double norm() {
        double sum = 0.0;
        for (int i = 0; i < this.nRows; i++) {
            for (int j = 0; j < this.nCols; j++) {
                sum += this.body[i][j] * this.body[i][j];
            }
        }
        return Math.sqrt(sum);
    }

    public static Matrix Identity(int d) {
        Matrix I = new Matrix(d, d);
        for (int i = 0; i < I.nRows; i++) {
            for (int j = 0; j < I.nCols; j++) {
                I.body[i][j] = (i == j ? 1.0 : 0.0);
            }
        }
        return I;
    }

    public static Matrix Rodrigues(Matrix rvec) {
        if (rvec.nRows != 1 || rvec.nCols != 3) {
            throw new IllegalArgumentException("Rotattion vector must be 3-dimentional but nRows = " + rvec.nRows + ", nCols = " + rvec.nCols);
        }
        double theta = rvec.norm();
        if (theta == 0.0) return Identity(3);

        Matrix k = rvec.multiply(1/theta);
        Matrix K = new Matrix(3,3);
        K.put(0, 0, 0.0);
        K.put(0, 1, -k.get(0, 2));
        K.put(0, 2,  k.get(0, 1));

        K.put(1, 0,  k.get(0, 2));
        K.put(1, 1, 0.0);
        K.put(1, 2, -k.get(0, 0));

        K.put(2, 0, -k.get(0, 1));
        K.put(2, 1,  k.get(0, 0));
        K.put(2, 2, 0.0);

        Matrix K2 = K.multiply(K);
        Matrix R = Identity(3).add(K.multiply(Math.sin(theta))).add(K2.multiply(1 - Math.cos(theta)));
        return R;
    }

    // This main() is just a unit test. Should print out an identity matrix.
    public static void main(String[] args) {
        double[][] k = {{3.1416/6, 3.1416/18, 0.0}};
        double[][] t = {{0.0, 0.0, -1.0}};
        Matrix R = Matrix.Rodrigues(new Matrix(k));
        Matrix e = R.multiply(new Matrix(t));

        System.out.format("Rotation matrix rows:%d, cols:%d %n", R.nRows, R.nCols);
        for (int i = 0; i < R.nRows; i++) {
            for (int j = 0; j < R.nCols; j++)
                System.out.format("%9.4f ", R.get(i, j));
            System.out.println();
        }
        System.out.println("Rotated vector");
        for (int i = 0; i < e.nCols; i++) System.out.format("%9.4f ", e.get(0, i));
        System.out.println();
    }
}
