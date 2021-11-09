package org.neuralnetwork;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class Matrix {


    public static void printMatrix(double[][] input) {
        for (double[] doubles : input) {
            for (int j = 0; j < doubles.length; j++) {
                if (doubles[j] > 0) {
                    System.out.format(" %.4f ", doubles[j]);
                } else {
                    System.out.format("%.4f ", doubles[j]);
                }
            }
            System.out.println();
        }
    }

    public static void printMatrix(double[] input) {
        for (double d : input) {
            System.out.format(" %.4f ", d);
        }
        System.out.println();
    }

    public static double[][] dot(double[][] m1, double[][] m2) {
        double[][] products = new double[m1.length][m2[0].length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                for (int k = 0; k < m2.length; k++) {
                    products[i][j] = products[i][j] + m1[i][k] * m2[k][j];
                }
            }
        }
        return products;
    }

    public static double[] dot(double[] inputs, double[][] weights) {
        double[] products = new double[weights.length];
        for (int i = 0; i < weights.length; i++) {
            double product = 0;
            for (int j = 0; j < inputs.length; j++) {
                product += inputs[j] * weights[i][j];
            }
            products[i] = product;
        }
        return products;
    }

    public static double[] dot(double[][] m, double[] v) {
        double[] products = new double[v.length];
        for (int i = 0; i < m.length; i++) {
            double product = 0;
            for (int j = 0; j < v.length; j++) {
                product += m[i][j] * v[j];
            }
            products[i] = product;
        }
        return products;
    }

    public static double[][] addForEach(double[][] m1, double[][] m2) {
        if (m2.length == 1) {
            return addForEach(m1, m2[0]);
        }
        double[][] m = new double[m1.length][m1[0].length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                m[i][j] = m1[i][j] + m2[i][j];
            }
        }
        return m;
    }

    public static double[][] addForEach(double[][] matrix, double[] addMatrix) {
        double[][] m = zeroesLike(matrix);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                m[i][j] = matrix[i][j] + addMatrix[j];
            }
        }
        return m;
    }

    public static double[] addForEach(double[] matrix, double[] addMatrix) {
        double[] m = new double[matrix.length];
        for (int i = 0; i < addMatrix.length; i++) {
            m[i] = matrix[i] + addMatrix[i];
        }
        return m;
    }

    public static double[][] transpose(double[][] input) {
        double[][] transpose = new double[input[0].length][input.length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                transpose[j][i] = input[i][j];
            }
        }
        return transpose;
    }

    public static double[][] reshape(double[] input, int dim1, int dim2) {
        double[][] reshaped = new double[dim1][dim2];
        int index = 0;
        for (int i = 0; i < reshaped[0].length; i++) {
            for (int j = 0; j < reshaped.length; j++) {
                reshaped[j][i] = input[index++];
            }
        }
        return reshaped;
    }

    public static double[][] zeros(int x, int y) {
        return new double[x][y];
    }

    public static double[][] random(int inputSize, int neurons) {
        double[][] weights = new double[inputSize][neurons];
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < neurons; j++) {
                weights[i][j] = 1 * (Math.random() - 0.5);
            }
        }
        return weights;
    }

    public static double[][] applyFunction(Function<Double, Double> f, double[][] doubles) {
        double[][] clone = copyOf(doubles);
        for (int i = 0; i < doubles.length; i++) {
            for (int j = 0; j < doubles[0].length; j++) {
                double v = doubles[i][j];
                clone[i][j] = f.apply(v);
            }
        }
        return clone;
    }

    public static double[][] applyFunction(BiFunction<Double, Double, Double> f, double[][] d1, double[][] d2) {
        double[][] clone = zeroesLike(d1);
        for (int i = 0; i < d1.length; i++) {
            for (int j = 0; j < d1[0].length; j++) {
                clone[i][j] = f.apply(d1[i][j], d2[i][j]);
            }
        }
        return clone;
    }

    public static double[][] applyOnIndexWherePredicateApplyFunction(Predicate<Double> p, Function<Double, Double> f, double[][] doubles) {
        double[][] clone = copyOf(doubles);
        for (int i = 0; i < doubles.length; i++) {
            for (int j = 0; j < doubles[0].length; j++) {
                clone[i][j] = p.test(doubles[i][j]) ? f.apply(doubles[i][j]) : doubles[i][j];
            }
        }
        return clone;
    }

    public static double sumd(double[][] input) {
        return Arrays.stream(input).flatMapToDouble(Arrays::stream).sum();
    }

    public static double[] suml(double[][] input) {
        return Arrays.stream(input).map(arr -> Arrays.stream(arr).sum()).mapToDouble(e -> e).toArray();
    }

    public static double[][] sumAxis1(double[][] input) {
        double[][] sum = new double[input.length][1];
        for (int i = 0; i < input.length; i++) {
            sum[i] = new double[]{Arrays.stream(input[i]).sum()};
        }
        return sum;
    }

    public static double[][] sumAxis0(double[][] input) {
        return Matrix.transpose(sumAxis1(Matrix.transpose(input)));
    }

    public static void print(double[][] obs) {
        Arrays.stream(obs).forEach(l -> {
                    Arrays.stream(l).forEach(n -> System.out.print((Math.round(n * 100.0) / 100.0) + " "));
                    System.out.println();
                }
        );
    }

    public static void print(double[][] obs, int decimalPoints) {
        for (int i = 0; i < obs.length; i++) {
            Arrays.stream(obs[i]).forEach(val -> System.out.printf(" %." + decimalPoints + "f", val));
            System.out.println();
        }
    }

    public static double[][] zeroesLike(double[][] like) {
        return new double[like.length][like[0].length];
    }

    public static double[][] copyOf(double[][] original) {
        double[][] copy = zeroesLike(original);
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[0].length; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }

    public static double[] copyOf(double[] original) {
        double[] copy = new double[original.length];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i];
        }
        return copy;
    }

    public static double[][] diagFlat(double[][] input) {
        double[][] doubles = zeros(input.length, input.length);
        for (int i = 0; i < doubles.length; i++) {
            doubles[i][i] = input[i][0];
        }
        return doubles;
    }


    public static boolean areEqual(double[][] expected, double[][] dValues2) {
        return Arrays.stream(applyFunction((n1, n2) -> n1 - n2, expected, dValues2))
                .flatMapToDouble(l -> Arrays.stream(l.clone())).allMatch(n -> n == 0.0);
    }

    public static void print5Lines(double[][] output) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < output[0].length; j++) {
                System.out.print(output[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static double[][] scalar(double scalar, double[][] matrix) {
        double[][] matrix2 = zeroesLike(matrix);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix2[i][j] = matrix[i][j] * scalar;
            }
        }
        return matrix2;
    }
}
