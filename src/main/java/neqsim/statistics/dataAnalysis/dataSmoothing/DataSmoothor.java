/*
 * DataSmoothor.java
 *
 * Created on 31. januar 2001, 21:27
 */

package neqsim.statistics.dataAnalysis.dataSmoothing;

import Jama.*;

/**
 *
 * @author Even Solbraa
 * @version
 */
public class DataSmoothor extends Object {

    private static final long serialVersionUID = 1000;

    double[] nonSmoothedNumbers, smoothedNumbers, cCoef;
    int nl = 0, nr = 0, ld = 0, m = 0, mm = 0, imj = 0, kk = 0;
    int[] index;
    double[][] a;
    double[] b;
    double sum = 0, fac = 0;

    /** Creates new DataSmoothor */
    public DataSmoothor() {
    }

    public DataSmoothor(double[] nonSmoothedNumbers, int nl, int nr, int ld, int m) {
        this.nonSmoothedNumbers = new double[nonSmoothedNumbers.length];
        this.smoothedNumbers = new double[nonSmoothedNumbers.length];
        this.cCoef = new double[nonSmoothedNumbers.length];
        this.nonSmoothedNumbers = nonSmoothedNumbers;
        System.arraycopy(nonSmoothedNumbers, 0, smoothedNumbers, 0, nonSmoothedNumbers.length);
        this.nl = nl;
        this.nr = nr;
        this.ld = ld;
        this.m = m;
    }

    public void runSmoothing() {
        findCoefs();
        setSmoothedNumbers();
    }

    public void findCoefs() {
        if (nonSmoothedNumbers.length < (nl + nr + 1) || nl < 0 || nr < 0 || ld > m || nl + nr < m) {
            System.err.println("Wrong input to DataSmoothor!");
        }

        index = new int[m + 1];
        a = new double[m + 1][m + 1];
        b = new double[m + 1];

        for (int i = 0; i < m + 1; i++) {
            index[i] = 1;
            b[i] = 1.0;
            for (int j = 0; j < m + 1; j++) {
                a[j][j] = 1.0;
            }
        }

        for (int ipj = 0; ipj <= (m << 1); ipj++) {
            sum = !(ipj == 0) ? 0.0 : 1.0;
            for (int k = 1; k <= nr; k++) {
                sum += Math.pow(k, ipj);
            }
            for (int k = 1; k <= nl; k++) {
                sum += Math.pow(-k, ipj);
            }
            mm = Math.min(ipj, 2 * m - ipj);
            for (imj = -mm; imj <= mm; imj += 2) {
                a[(ipj + imj) / 2][(ipj - imj) / 2] = sum;
            }
        }

        for (int j = 0; j < m + 1; j++) {
            b[j] = 0.0;
        }
        b[ld] = 1.0;

        Matrix amatrix = new Matrix(a);
        // amatrix.print(10,2);
        Matrix bmatrix = new Matrix(b, 1);
        bmatrix = amatrix.solve(bmatrix.transpose());
        // bmatrix.print(10,2);
        b = bmatrix.transpose().getArray()[0];

        for (int kk = 0; kk < nonSmoothedNumbers.length; kk++) {
            cCoef[kk] = 0.0;
        }

        for (int k = -nl; k <= nr; k++) {
            sum = b[0];
            fac = 1.0;
            for (mm = 0; mm < m; mm++) {
                sum += b[mm + 1] * (fac *= k);
            }
            kk = ((nonSmoothedNumbers.length - k) % nonSmoothedNumbers.length);
            cCoef[kk] = sum;
        }
        // new Matrix(cCoef,1).print(10,2);

    }

    public void setSmoothedNumbers() {
        for (int i = nl; i < nonSmoothedNumbers.length - nr; i++) {
            smoothedNumbers[i] = 0;
            smoothedNumbers[i] = cCoef[0] * nonSmoothedNumbers[i];
            for (int j = 0; j < nl; j++) {
                smoothedNumbers[i] += cCoef[nonSmoothedNumbers.length - 1 - j] * nonSmoothedNumbers[i - j - 1];
            }
            for (int j = 0; j < nr; j++) {
                smoothedNumbers[i] += cCoef[j + 1] * nonSmoothedNumbers[i + j - 1];
            }
        }
    }

    public double[] getSmoothedNumbers() {
        return smoothedNumbers;
    }

    public static void main(String args[]) {
        double[] numbers = { 10, 11, 12, 13, 14, 15, 15.5, 15, 19, 14, 14, 13, 12, 12, 11, 10, 9, 8 };
        DataSmoothor test = new DataSmoothor(numbers, 3, 3, 0, 4);
        Matrix data = new Matrix(test.getSmoothedNumbers(), 1);
        data.print(10, 2);
        test.runSmoothing();
        data = new Matrix(test.getSmoothedNumbers(), 1);
        data.print(10, 2);
    }
}
