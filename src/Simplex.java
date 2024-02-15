import java.text.DecimalFormat;
import java.util.Arrays;

public class Simplex {
    public static void primal(int[] x, int[] st, double[][] a, double[] b, double[] c){
        DecimalFormat df = new DecimalFormat(" 00.000;-00.000");

        // tableau
        double[][] tab = new double[st.length+2][x.length+2];
        System.out.println("\ninitial tableau:");

        // X
        for(int i = 0; i<x.length; i++){
            tab[0][i+1] = x[i];
        }
        // objektive function
        for(int i = 0; i<c.length; i++){
            tab[1][i+1] = -c[i];
        }
        // b
        for(int i = 0; i<b.length; i++){
            tab[i+2][tab[i+2].length-1] = b[i];
        }
        // st
        for(int i = 0; i<st.length; i++){
            tab[i+2][0] = st[i];
        }
        // coefficients
        for(int i = 0; i < a.length; i++){
            System.arraycopy(a[i], 0, tab[i + 2], 1, a[i].length);
        }

        // simplex
        // output tableau
        for(int i = 0; i < tab.length; i++){
            for(int j = 0; j < tab[i].length; j++){
                if((j == 0 && i == 0) || (i == 1 && j == 0)) {
                    System.out.print("        | ");
                    continue;
                }
                if(i == 0 && j == tab[0].length-1){
                    System.out.print("          ");
                    continue;
                }

                if(j == 0 || j == tab[i].length-2)
                    System.out.print(df.format(tab[i][j]) + " | ");
                else
                    System.out.print(df.format(tab[i][j]) + "  ");
            }
            System.out.println();
            if(i <= 1) {
                for(int e = 0; e < x.length+2; e++)
                    System.out.print("---------");
                System.out.println();
            }
        }

        int s = -1;
        int r = -1;
        double[] xz = new double[x.length+1];

        int iteration = 1;
        // calculation
        while(true) {
            // pivot column
            double preS = 1000000000000d;
            for (int i = 0; i < tab[1].length; i++) {
                if (tab[1][i] < 0 && tab[1][i] < preS) {
                    s = i;
                    preS = tab[1][i];
                }
            }
            if(s == -1) {
                System.out.println();
                System.out.println("optimal solution:");
                System.out.print("x* = (");
                for(int i = 0; i < xz.length-1; i++) {
                    if(i != xz.length-2)
                        System.out.print(df.format(xz[i])+ "; ");
                    else
                        System.out.print(df.format(xz[i]));
                }
                System.out.print(" )  |  f(x*) = " + xz[xz.length-1]+"\n");
                break;
            }
            //pivot row
            double pre = 1000000000000d;
            for (int j = 0; j < tab.length - 2; j++) {
                double ais = tab[j + 2][s];
                double bi = tab[j + 2][tab[j + 2].length - 1];
                double newpre = bi / ais;
                if (ais > 0 && newpre < pre) {
                    pre = newpre;
                    r = j + 2;
                }
            }
            // new tableau
            if(r == -1){
                System.out.println("\nunrestricted problem\n");
                break;
            }
            double[][] newtab = new double[st.length + 2][x.length + 2];
            // switch variables
            System.arraycopy(tab[0], 0, newtab[0], 0, tab[0].length);

            for (int i = 0; i < tab.length; i++) {
                newtab[i][0] = tab[i][0];
            }
            newtab[r][0] = tab[0][s];
            newtab[0][s] = tab[r][0];

            // pivot element
            newtab[r][s] = 1 / tab[r][s];
            // pivot row
            for (int i = 1; i < tab[r].length; i++) {
                if (i != s)
                    newtab[r][i] = tab[r][i] / tab[r][s];
            }
            // pivot column
            for (int i = 1; i < tab.length; i++) {
                if (i != r)
                    newtab[i][s] = tab[i][s] / -tab[r][s];
            }
            // Dreiecksregel
            for (int i = 1; i < r; i++) {
                for (int j = 1; j < s; j++) {
                    newtab[i][j] = tab[i][j] - ((tab[r][j] * tab[i][s]) / tab[r][s]);
                }
            }
            for (int i = 1; i < r; i++) {
                for (int j = s + 1; j < tab[i].length; j++) {
                    newtab[i][j] = tab[i][j] - ((tab[r][j] * tab[i][s]) / tab[r][s]);
                }
            }
            for (int i = r + 1; i < tab.length; i++) {
                for (int j = 1; j < s; j++) {
                    newtab[i][j] = tab[i][j] - ((tab[r][j] * tab[i][s]) / tab[r][s]);
                }
            }
            for (int i = r + 1; i < tab.length; i++) {
                for (int j = s + 1; j < tab[i].length; j++) {
                    newtab[i][j] = tab[i][j] - ((tab[r][j] * tab[i][s]) / tab[r][s]);
                }
            }
            //
            Arrays.fill(xz, 0);
            for(int i = 0; i < tab.length; i++){
                if(newtab[i][0] <= x.length && newtab[i][0] != 0)
                    xz[((int) newtab[i][0])-1] = newtab[i][tab[i].length-1];
            }
            xz[xz.length-1] = newtab[1][newtab[1].length-1];

            // output
            System.out.println("\npivot column: "+s +", pivot row: "+(r-1)+", pivot element: "+df.format(tab[r][s]));
            System.out.println();
            System.out.println("iteration "+iteration +":");
            for(int i = 0; i < newtab.length; i++){
                for(int j = 0; j < newtab[i].length; j++){
                    if((j == 0 && i == 0) || (i == 1 && j == 0)) {
                        System.out.print("        | ");
                        continue;
                    }
                    if(i == 0 && j == tab[0].length-1){
                        System.out.print("          ");
                        continue;
                    }
                    if(j == 0 || j == newtab[i].length-2)
                        System.out.print(df.format(newtab[i][j]) + " | ");
                    else
                        System.out.print(df.format(newtab[i][j]) + "  ");
                }
                System.out.println();
                if(i <= 1) {
                    for (int e = 0; e < x.length + 2; e++)
                        System.out.print("---------");
                    System.out.println();
                }
            }

            // new iteration
            s = -1;
            r = -1;
            iteration++;
            tab = newtab;
        }
    }
}