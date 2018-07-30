package main.view;

import main.model.EmployeeHierarchy;

import java.util.List;

public class HierarchyView {

    public void printHierarchy(List<EmployeeHierarchy> employees) {

        System.out.println("Employee Hierarchy");

        for(EmployeeHierarchy em : employees) {
            System.out.println();
            indent(em.getIndent());
            System.out.print(em.getName());
        }

        System.out.println();
    }

    private void indent(int indent) {
        for(int i=0; i<indent; i++) {
            System.out.print('\t');
        }
    }
}
