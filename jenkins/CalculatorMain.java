public class CalculatorMain {

    public static void main(String[] args) {

        Calculator calc = new Calculator();

        int sum = calc.add(20, 5);
        int diff = calc.sub(10, 5);
        int mul = calc.mul(4, 5);

        System.out.println("Addition: " + sum);
        System.out.println("Subtraction: " + diff);
        System.out.println("Multiplication: " + mul);
    }
}