
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamApi {

    record Employee(String name, int age, String gender, double salary, String designation, String department) {

    }

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
                new Employee("emp1", 25, "Male", 50000, "Developer", "IT"),
                new Employee("emp2", 45, "Female", 95000, "Manager", "HR"),
                new Employee("emp3", 30, "Male", 60000, "Developer", "IT"),
                new Employee("emp4", 55, "Male", 120000, "Director", "Sales"),
                new Employee("emp5", 28, "Female", 55000, "Analyst", "Finance"),
                new Employee("emp6", 40, "Male", 85000, "Manager", "IT"),
                new Employee("emp7", 22, "Female", 45000, "Intern", "HR"),
                new Employee("emp8", 35, "Male", 75000, "Developer", "IT"),
                new Employee("emp9", 50, "Female", 110000, "Manager", "Finance"),
                new Employee("emp10", 32, "Male", 65000, "Analyst", "Sales"),
                new Employee("emp11", 29, "Female", 58000, "Developer", "IT"),
                new Employee("emp12", 48, "Male", 92000, "Manager", "Sales"),
                new Employee("emp13", 26, "Female", 52000, "Developer", "IT"),
                new Employee("emp14", 38, "Male", 80000, "Lead", "Finance"),
                new Employee("emp15", 42, "Female", 88000, "Manager", "IT"),
                new Employee("emp16", 31, "Male", 62000, "Developer", "Sales"),
                new Employee("emp17", 52, "Female", 105000, "Director", "HR"),
                new Employee("emp18", 27, "Male", 54000, "Analyst", "IT"),
                new Employee("emp19", 44, "Female", 98000, "Manager", "Sales"),
                new Employee("emp20", 36, "Male", 77000, "Lead", "Finance")
        );

        //Find highest salary paid employee
        System.out.println("--- Highest Salary Employee ---");
        employees.stream()
                .max(Comparator.comparingDouble(Employee::salary))
                .ifPresent(System.out::println);

        // Find how many male and female employees
        System.out.println("\n--- Gender Count ---");
        Map<Boolean, Long> genderCount = employees.stream()
                .collect(Collectors.partitioningBy(
                        emp -> "Male".equalsIgnoreCase(emp.gender()),
                        Collectors.counting()
                ));

        System.out.println("Male count : " + genderCount.get(true));
        System.out.println("Female count : " + genderCount.get(false));

        // Total expense department wise
        System.out.println("\n--- Total Expense Department Wise ---");
        Map<String, Double> deptExpense = employees.stream()
                .collect(Collectors.groupingBy(Employee::department,
                        Collectors.summingDouble(Employee::salary)));
        deptExpense.forEach((dept, total) -> System.out.println(dept + ": " + total));

        // Who are top 5 elder most employees
        System.out.println("\n--- Top 5 Eldest Employees ---");
        employees.stream()
                .sorted((e1, e2) -> Integer.compare(e2.age(), e1.age()))
                .limit(5)
                .forEach(System.out::println);

        //Only display names of managers
        System.out.println("\n--- Manager Names ---");
        employees.stream()
                .filter(e -> "Manager".equalsIgnoreCase(e.designation()))
                .map(Employee::name)
                .forEach(System.out::println);

        //Hike salary by 20% for all employees except managers
        System.out.println("\n--- Salaries After 20% Hike (Non-Managers) ---");
        List<Employee> hikedEmployees = employees.stream()
                .map(e -> !"Manager".equalsIgnoreCase(e.designation())
                ? new Employee(e.name(), e.age(), e.gender(), e.salary() * 1.2, e.designation(), e.department())
                : e)
                .collect(Collectors.toList());
        hikedEmployees.forEach(e -> System.out.println(e.name() + ": " + e.salary()));

        // Find total number of employees
        System.out.println("\n--- Total Number of Employees ---");
        System.out.println("Total: " + employees.size());
    }
}
