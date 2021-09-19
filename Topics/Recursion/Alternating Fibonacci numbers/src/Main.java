import java.util.Scanner;

public class Main {

    public static int fib(int n) {
        if (n <= 1) {
            return n;
        }
        return fib(n - 2) - fib(n - 1);
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        System.out.println(fib(n));
    }
}