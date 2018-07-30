package main.model;

public class EmployeeHierarchy {
    private String name;
    private int indent;

    public EmployeeHierarchy(String name, int indent) {
        this.name = name;
        this.indent = indent;
    }

    public String getName() {
        return name;
    }

    public int getIndent() {
        return indent;
    }
}
