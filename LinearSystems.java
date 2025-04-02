/*
 * By Ray Murdorf
 * Licensed under CC-BY-NC-SA 4.0
 */

 import java.util.Scanner;

public class LinearSystems {
    public static double[][] gaussianWithPivoting(double[][] arr) throws Exception {
        int n = arr.length;
        double[] s = new double[n]; // scale vector
        double[] r = new double[n]; // ratio vector
        double temp;
        int row; // this is the pivotal row
        double[] swap = {};
        // for loop to find the scale vector
        for(int i = 0; i < n; i++) {
            temp = Math.abs(arr[i][0]);
            for(int j = 1; j < n; j++) {
                if(Math.abs(arr[i][j]) > temp) temp = Math.abs(arr[i][j]);
            }
            s[i] = temp;
        }

        // main for loop
        for(int i = 0; i < n; i++) {

            // for loop(s) to find the ratio vector
            row = i;
            for(int j = 0; j < i; j++) {
                r[j] = 0;
            }
            for(int j = i; j < n; j++) {
                r[j] = Math.abs(arr[j][i] / s[j]);
                for(int k = 0; k < j; k++) {
                    if(r[j] > r[k]) row = j;
                }
            }
            // at this point, row = the pivotal row

            // only swaps if the pivotal row is below the current row
            if(row > i) {
                swap = arr[i];
                arr[i] = arr[row];
                arr[row] = swap;
            }

            // performs gaussian elimination on all rows below the current one
            for(int j = i + 1; j < n; j++) {
                temp = arr[j][i] / arr[i][i];
                for(int k = i + 1; k <= n; k++) {
                    arr[j][k] -= temp * arr[i][k];
                }
            }

            // error checking
            if(Math.abs(r[row]) < 0.000001) {
                if(Math.abs(arr[n - 1][n]) < 0.000001) {
                    throw new Exception("The System is Singular and Consistent");
                } else {
                    throw new Exception("The System is Singular and Inconsistent");
                }
            }
        }

        // checks for singularity in the last row
        if(Math.abs(arr[n - 1][n - 1]) < 0.000001) {
            if(Math.abs(arr[n - 1][n]) < 0.000001) {
                throw new Exception("The System is Singular and Consistent");
            } else {
                throw new Exception("The System is Singular and Inconsistent");
            }
        }

        return arr;
    }

    // performs back substitution and returns a 1d array with the solutions for each variable
    public static double[] backSubstitution(double[][] arr) {
        int n = arr.length;
        double[] solution = new double[n];
        solution[n - 1] = arr[n - 1][n] / arr[n - 1][n - 1];
        for(int i = n - 2; i >= 0; i--) {
            solution[i] = arr[i][n];
            for(int j = i + 1; j < n; j++) {
                solution[i] -= arr[i][j] * solution[j];
            }
            solution[i] /= arr[i][i];
        }
        return solution;
    }

    // testing method
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        System.out.println("How many variables does the system have? ");
        int n = kb.nextInt();
        double[][] arr = new double[n][n + 1];
        for(int i = 0;  i < n; i++) {
            System.out.println("Now inputting equation " + i);
            for(int j = 0; j < n; j++) {
                System.out.print("x" + j + "? ");
                arr[i][j] = kb.nextDouble();
            }
            System.out.print("Solution? ");
            arr[i][n] = kb.nextDouble();
        }
        try {
            double[] solution = backSubstitution(gaussianWithPivoting(arr));
            for(int i = 0; i < solution.length; i++) {
                System.out.printf("x[%d] = %.2f %n", i, solution[i]);
            }
            System.out.println();
        } catch(Exception e) {
            System.out.println(e);
        }
        kb.close();
    }
}
