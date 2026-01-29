import java.util.Arrays;

public class Vektoren {

    public static double[] Vektor_addition(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Vektoren müssen gleich lang sein");
        }

        double[] fin = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            fin[i] = a[i] + b[i];
        }
        return fin;
    }

    public static double[] Skalarmultiplikation(double[] a, double b){
        double multipli = b;
        double[] fin = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            fin[i] = a[i]*multipli;
        }
        return fin;
    }

    public static double[] Skalarprodukt(double[] a, double[] b){
        double[] fin = new double[a.length];
        if (a.length != b.length){
            throw new IllegalArgumentException("Vektoren sind nicht gleichgroß");
        }
        for (int i = 0; i < a.length; i++) {
            fin[i] = a[i]*b[i];
        }
        return fin;
    }
}
