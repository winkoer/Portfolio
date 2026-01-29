import java.util.Arrays;

public class Mathe_main {
    public static void main(String[] args){
        double[][] matrix1 = {
                {4.0, 3.0, 5.0},
                {5.0, -2.0, 1.0},
                {7.0, 3.0, -9.0}
        };
        double[][] matrix2 = {
                {4.0, 3.0,4.0},
                {5.0, 2.0,4.0},
                {5.0, 2.0,4.0}
        };
        double[][] matrix3 = {
                {4.0, 3.0, 5.0},
                {5.0, -2.0, 1.0},
                {7.0, 3.0, -9.0}
        };
        double[][] matrix4 = {
                {4.0, 3.0, 5.0},
                {5.0, -2.0, 1.0},
                {7.0, 3.0, -9.0}
        };
        double[] vektor_mul  = {-3.0, 1.0, -5.0, 2.0};
        double test = -7.0;
        double[][][] matricies = {matrix1,matrix2,matrix3, matrix4};
        double[][] verktor_fin = Matrizen.matrix_ketten_multiplikation(matricies);

        for(double[] i: verktor_fin){
            System.out.println(Arrays.toString(i));
        }
        //System.out.println(Arrays.toString(verktor_fin));
    }
}
