package main.transformer;

import main.model.Employee;
import main.model.EmployeeHierarchy;
import main.domain.EmployeeStore;

import java.util.ArrayList;
import java.util.List;

public class EmployeeTransformer {

    List<EmployeeHierarchy> hierarchy = new ArrayList<>();

    public List<EmployeeHierarchy> transform(EmployeeStore employeeStore) {

        preValidate(employeeStore);
        Employee employee = employeeStore.getCEO().get();
        transformChildren(employeeStore, employee, 0);
        postValidate(employeeStore);

        return hierarchy;
    }

    private void transformChildren(EmployeeStore employeeStore, Employee employee, int indent) {
        EmployeeHierarchy employeeHierarchy = new EmployeeHierarchy(employee.getName(), indent);
        hierarchy.add(employeeHierarchy);
        employee.setVisited(true);

        List<Employee> children = employeeStore.getChildren(employee);
        for (Employee em : children) {
            transformChildren(employeeStore, em, indent + 1);
        }
    }

    private void preValidate(EmployeeStore employeeStore) {
        int ceo = 0;

        List<Employee> employees = employeeStore.getEmployeeList();
        for (Employee em : employees) {
            em.setVisited(false);
            if(em.getManagerId() == em.getId()) {
                throw new Error("Error: employee " + em.getId() + " ("+em.getName() + ") cannot be his/her own manager");
            }
            if(em.getManagerId() != 0 && employeeStore.getEmployeeById(em.getManagerId()) == null) {
                throw new Error("Error: employee " + em.getId() + " (" + em.getName() + ") has unknown manager id " + em.getManagerId());
            }
            if(em.isCEO()) {
                ceo++;
            }
        }
        if(ceo == 0) {
            throw new Error("Error: no CEO found");
        } else if (ceo != 1) {
            throw new Error("Error: more than one CEO found");
        }
    }

    private void postValidate(EmployeeStore employeeStore) {
        StringBuilder missingEmployees = new StringBuilder();
        List<Employee> employees = employeeStore.getEmployeeList();
        for (Employee em : employees) {
            if(!em.isVisited()) {
                missingEmployees.append(em.getId() + " (" + em.getName() + "), ");
            }
        }
        if(missingEmployees.length() > 0) {
            throw new Error("Error: employees " + missingEmployees + "are not in the hierarchy");
        }
    }
}
