package main.model;

public class Employee {
    private String name;
    private int id;
    private int managerId;
    private boolean visited;

    public Employee(String name, int id, int managerId) {
        this.name = name;
        this.id = id;
        this.managerId = managerId;
        this.visited = false;
    }

    public Employee(String name, int id) {
        this.name = name;
        this.id = id;
        this.managerId = 0;
        this.visited = false;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getManagerId() {
        return managerId;
    }

    public boolean isCEO() {
        return  (managerId == 0);
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
