package main.domain;

import main.model.Employee;

import java.util.*;
import java.util.stream.Collectors;

public class EmployeeStore {
    private Map<Integer,Employee> employees = new TreeMap<>();

    public void addEmployee(Employee em) {
        if (employees.get(em.getId()) != null) {
            throw new Error("Error: Duplicate employee id " + em.getId() + " ("+em.getName()+")");
        }
        employees.put(em.getId(), em);
    }

    public Optional<Employee> getCEO() {
        return employees.values().stream().filter(Employee::isCEO).findFirst();
    }

    public List<Employee> getChildren(Employee employee) {
        return employees.values().stream().filter(e -> e.getManagerId() == employee.getId() ).collect(Collectors.toList());
    }

    public List<Employee> getEmployeeList() {
        return new ArrayList<>(employees.values());
    }

    public Employee getEmployeeById(int id) {
        return employees.get(id);
    }
}
