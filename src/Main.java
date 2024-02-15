import java.text.DecimalFormat;
import java.util.Scanner;

import static java.lang.Math.abs;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat(" 00.000;-00.000");

        //info
        System.out.println("\nThis program solves linear programming problems with the primal simplex method.");
        System.out.println("The problem is given in the form of a standard linear program.");
        System.out.println("\nmax c1*x1 + c2*x2 + ... + cn*xn");
        System.out.println("st a11*x1 + a12*x2 + ... + a1n*xn <= b1");
        System.out.println("   a21*x1 + a22*x2 + ... + a2n*xn <= b2");
        System.out.println("   ...");
        System.out.println("   am1*x1 + am2*x2 + ... + amn*xn <= bm");
        System.out.println("   x1, x2, ..., xn >= 0");

        // input
        System.out.print("\nnumber of variables (n) = ");
        int xlength = scanner.nextInt();

        System.out.print("number of constraints (m) = ");
        int stlength = scanner.nextInt();

        System.out.println("enter the coefficients of the objective function (c1, c2, ..., cn):");
        double[] c = new double[xlength];
        for(int i = 0; i < xlength; i++) {
            System.out.print("c"+(i+1)+" = ");
            c[i] = scanner.nextDouble();
        }
        System.out.print("\nmax ");
        for (int i = 0; i < xlength; i++) {
            if(c[i] > 0 && i != 0)
                System.out.print(" + " + abs(c[i]) + "*x" + (i+1));
            else if(c[i] < 0)
                System.out.print(" - " + abs(c[i]) + "*x" + (i+1));
            else if (c[i] > 0)
                System.out.print(c[i] + "*x" + (i+1));
        }
        System.out.println();

        int[] x = new int[xlength];
        for(int i = 0; i < xlength; i++){
            x[i] = i+1;
        }

        int[] st = new int[stlength];
        for(int i = 0; i < stlength; i++){
            st[i] = i + xlength +1;
        }

        System.out.println("\nenter matrix A:");
        double[][] a = new double[stlength][xlength];
        for(int i = 0; i < stlength; i++){
            for(int j = 0; j < xlength; j++){
                System.out.println();
                for(int v = 0; v < stlength; v++){
                    System.out.print("|");
                    for(int w = 0; w < xlength; w++) {
                        if(a[v][w] > 0)
                            System.out.print(df.format(a[v][w]));
                        else if(v == i && w == j)
                            System.out.print("   x   ");
                        else
                            System.out.print(" ----- ");
                    }
                    System.out.print("|");
                    System.out.println();
                }

                System.out.print("a"+(i+1)+(j+1)+" = ");
                a[i][j] = scanner.nextDouble();
            }
        }

        // output matrix A
        System.out.println();
        for (int i = 0; i < stlength; i++) {
            System.out.print("|");
            for (int j = 0; j < xlength; j++) {
                if(a[i][j] > 0)
                    System.out.print(df.format(a[i][j]));
                else
                    System.out.print(" ----- ");
            }
            System.out.print("|");
            System.out.println();
        }

        System.out.println("\nenter vector b:");
        double[] b = new double[stlength];
        for(int i = 0; i < stlength; i++){
            System.out.print("b" + (i+1) + " = ");
            b[i] = scanner.nextDouble();
        }

        // calculation and output
        Simplex.primal(x, st, a, b, c);
        System.out.println();

    }
}
