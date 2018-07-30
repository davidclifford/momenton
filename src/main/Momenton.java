package main;

import main.model.Employee;
import main.model.EmployeeHierarchy;
import main.domain.EmployeeStore;
import main.transformer.EmployeeTransformer;
import main.view.HierarchyView;

import java.util.List;

public class Momenton {
    public static void main(String[] args) {

        EmployeeStore employees = new EmployeeStore();

        try {
            employees.addEmployee(new Employee("Alan",100,150));
            employees.addEmployee(new Employee("Martin",220,100));
            employees.addEmployee(new Employee("Jamie",150));
            employees.addEmployee(new Employee("Alex",275,100));
            employees.addEmployee(new Employee("Steve",400,150));
            employees.addEmployee(new Employee("David",190,400));

            EmployeeTransformer employeeTransformer = new EmployeeTransformer();
            List<EmployeeHierarchy> employeeHierarchy = employeeTransformer.transform(employees);

            HierarchyView view = new HierarchyView();
            view.printHierarchy(employeeHierarchy);

        } catch (Error e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
