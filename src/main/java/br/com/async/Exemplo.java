package br.com.async;

public class Exemplo {
    public static int sum(int n) {
        if (n >= 1) {
            return sum(n - 1) + n;
        }
        return n;
    }

    public static int iterativeSum(int n) {
        int sum = 0;
        if (n < 0) {
            return -1;
        }
        for (int i = 0; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

    public static void main(String[] args) {
        int resultadoRecursao = sum(2);
        int resultadoIteracao = sum(2);

        System.out.println(resultadoRecursao);
        System.out.println(resultadoIteracao);
    }
}
