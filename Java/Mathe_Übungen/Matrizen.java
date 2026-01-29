public class Matrizen {

    public static double[][] matrix_vektor_multiplikation(double[][]a, double[] b){
        double[][] fin = new double[a.length][a[0].length];

        if(a[0].length != b.length){
            throw new IllegalArgumentException("Der vektor muss gleich groß sein wie die spalten von a");
        }
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                fin[i][j] = a[i][j]*b[j];
            }
        }
        return fin;
    }

    public static double[][] matrix_multiplikation(double[][] a, double[][] b){
        int m = a.length;       // Reihen von A
        int n = a[0].length;    // Spalten von A
        int p = b[0].length;    // Spalten von B

        if (b.length != n) {
            throw new IllegalArgumentException("Anzahl der Spalten von A muss gleich der Anzahl der Reihen von B sein");
        }

        double[][] result = new double[m][p];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                double sum = 0;
                for (int k = 0; k < n; k++) {
                    sum += a[i][k] * b[k][j];
                }
                result[i][j] = sum;
            }
        }

        return result;
    }

    public static double betrag(double x){
        return x < 0? -x : x;
    }

    public static double[][] betrag_matrize(double[][] a){
        double[][] fin = new double[a.length][a[0].length];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] < 0){
                    fin[i][j] = betrag(a[i][j]);
                }else {
                    fin[i][j] = a[i][j];
                }
            }
        }
        return fin;
    }

    public static double[] betrag_vektor(double[] a){
        double[] fin = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            fin[i] = betrag(a[i]);
        }
        return fin;
    }

    public static double[][] transponieren_reverse(double[] a){
        double[][] fin = new double[a.length][1];
        for (int i = 0; i < a.length; i++) {
            fin[i][0] = a[i];
        }

        return fin;
    }

    public static double[][] matrix_addieren(double[][] a, double[][] b){
        double[][] fin = new double[a.length][a[0].length];

        if (!matrix_gleich(a,b)){
            throw new IllegalArgumentException("Anzahl der Reihen von A muss gleich der Anzahl der Reihen von B sein");
        }

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                fin[i][j] = a[i][j] + b[i][j];
            }
        }
        return fin;
    }

    public static double[][] matrix_subtrahieren(double[][] a, double[][] b){
        double[][] fin = new double[a.length][a[0].length];

        if (!matrix_gleich(a,b)){
            throw new IllegalArgumentException("Anzahl der Reihen von A muss gleich der Anzahl der Reihen von B sein");
        }

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                fin[i][j] = a[i][j] - b[i][j];
            }
        }
        return fin;
    }

    public static boolean matrix_gleich(double[][] a, double[][] b){
        if (a.length != b.length || a[0].length != b[0].length)return false;
        else return true;
    }


    public static double[][] matrix_ketten_multiplikation(double[][][] matrices) {
        if (matrices.length == 0) {
            throw new IllegalArgumentException("Keine Matrizen übergeben");
        }
        return matrix_ketten_multiplikation_rec(matrices, 0, matrices.length - 1);
    }

    private static double[][] matrix_ketten_multiplikation_rec(double[][][] matrices, int start, int end) {
        if (start == end) {
            return matrices[start];
        }
        if (start + 1 == end) {
            return matrix_multiplikation(matrices[start], matrices[end]);
        }

        int mid = (start + end) / 2;
        double[][] left = matrix_ketten_multiplikation_rec(matrices, start, mid);
        double[][] right = matrix_ketten_multiplikation_rec(matrices, mid + 1, end);

        return matrix_multiplikation(left, right);
    }

}
