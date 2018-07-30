package test;

import main.domain.EmployeeStore;
import main.model.Employee;
import main.model.EmployeeHierarchy;
import main.transformer.EmployeeTransformer;
import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;

public class MomentonTest {

    @Test
    public void testAddingEmployeeToStore() {
        Employee employee = new Employee("John",100,200);
        EmployeeStore employeeStore = new EmployeeStore();
        employeeStore.addEmployee(employee);

        assertThat(employeeStore.getEmployeeById(100).getName(), is("John"));
        assertThat(employeeStore.getEmployeeById(100).getId(), is(100));
        assertThat(employeeStore.getEmployeeById(100).getManagerId(), is(200));
    }

    @Test
    public void testAddingCEOToStore() {
        Employee ceo = new Employee("fred",100);
        EmployeeStore employeeStore = new EmployeeStore();
        employeeStore.addEmployee(ceo);

        assertThat(employeeStore.getEmployeeById(100).getName(), is("fred"));
        assertThat(employeeStore.getEmployeeById(100).getId(), is(100));
        assertThat(employeeStore.getEmployeeById(100).getManagerId(), is(0));
    }

    @Test
    public void testAddingDuplicateEmployeeIdThrowsException() throws Error {
        Employee employee1 = new Employee("fred",100, 150);
        Employee employee2 = new Employee("jennifer",100, 150);
        EmployeeStore employeeStore = new EmployeeStore();

        try {
            employeeStore.addEmployee(employee1);
            employeeStore.addEmployee(employee2);
            fail();
        } catch (Error e) {
            assertThat(e.getMessage(),containsString("Error: Duplicate employee id"));
        }
    }

    @Test
    public void testGettingChildren() {
        Employee child1 = new Employee("alice", 200, 100);
        Employee child2 = new Employee("jake", 300, 100);
        Employee ceo = new Employee("fred",100);

        EmployeeStore employeeStore = new EmployeeStore();
        employeeStore.addEmployee(ceo);
        employeeStore.addEmployee(child1);
        employeeStore.addEmployee(child2);

        List<Employee> children = employeeStore.getChildren(ceo);
        assertThat(children.size(), is(2));
        assertThat(children.get(0).getName(), is("alice"));
        assertThat(children.get(1).getName(), is("jake"));
    }

    @Test
    public void testGettingCEO() {
        Employee child1 = new Employee("alice", 200, 100);
        Employee child2 = new Employee("jake", 300, 100);
        Employee ceo = new Employee("fred",100);

        EmployeeStore employeeStore = new EmployeeStore();
        employeeStore.addEmployee(ceo);
        employeeStore.addEmployee(child1);
        employeeStore.addEmployee(child2);

        Employee found = employeeStore.getCEO().get();
        assertThat(found.getName(), is("fred"));
        assertThat(found.getId(), is(100));
        assertThat(found.isCEO(), is(true));
    }

    @Test
    public void testGetEmployeeById() {
        Employee employee1 = new Employee("one", 100, 150);
        Employee employee2 = new Employee("two", 200, 150);
        Employee employee3 = new Employee("three", 300, 150);
        EmployeeStore employeeStore = new EmployeeStore();
        employeeStore.addEmployee(employee1);
        employeeStore.addEmployee(employee2);
        employeeStore.addEmployee(employee3);
        Employee found = employeeStore.getEmployeeById(200);
        assertThat(found.getName(), is("two"));
        assertThat(found.getId(), is(200));
    }

    @Test
    public void testGetEmployeeByUnknownIdReturnsNull() {
        Employee employee1 = new Employee("one", 100, 150);
        Employee employee2 = new Employee("two", 200, 150);
        Employee employee3 = new Employee("three", 300, 150);
        EmployeeStore employeeStore = new EmployeeStore();
        employeeStore.addEmployee(employee1);
        employeeStore.addEmployee(employee2);
        employeeStore.addEmployee(employee3);

        Employee found = employeeStore.getEmployeeById(400);
        assertThat(found, nullValue());
    }

    @Test
    public void testBeingOwnManagerThrowsError() throws Error {
        Employee employee1 = new Employee("one", 100, 100);
        EmployeeStore employeeStore = new EmployeeStore();
        employeeStore.addEmployee(employee1);
        EmployeeTransformer employeeTransformer = new EmployeeTransformer();
        try {
            employeeTransformer.transform(employeeStore);
        } catch (Error e) {
            assertThat(e.getMessage(), containsString("cannot be his/her own manager"));
        }
    }

    @Test
    public void testEmployeeHasUnknownMangerThrowsError() throws Error {
        Employee ceo = new Employee("fred",100);
        Employee employee1 = new Employee("one", 200, 110);
        EmployeeStore employeeStore = new EmployeeStore();
        employeeStore.addEmployee(employee1);
        employeeStore.addEmployee(ceo);
        EmployeeTransformer employeeTransformer = new EmployeeTransformer();
        try {
            employeeTransformer.transform(employeeStore);
            fail();
        } catch (Error e) {
            assertThat(e.getMessage(), containsString("has unknown manager id"));
        }
    }


    @Test
    public void testNoCeoThrowsError() throws Error {
        EmployeeStore employees = new EmployeeStore();
        employees.addEmployee(new Employee("Alan",100,150));
        employees.addEmployee(new Employee("Martin",220,100));
        employees.addEmployee(new Employee("Jamie",150, 100));
        employees.addEmployee(new Employee("Alex",275,100));
        employees.addEmployee(new Employee("Steve",400,150));
        employees.addEmployee(new Employee("David",190,400));

        EmployeeTransformer employeeTransformer = new EmployeeTransformer();
        try {
            employeeTransformer.transform(employees);
            fail();
        } catch (Error e) {
            assertThat(e.getMessage(), containsString("Error: no CEO found"));
        }
    }

    @Test
    public void testMoreThanOneCeoThrowsError() throws Error {
        EmployeeStore employees = new EmployeeStore();
        employees.addEmployee(new Employee("Alan",100,150));
        employees.addEmployee(new Employee("Martin",220,100));
        employees.addEmployee(new Employee("Jamie",150));
        employees.addEmployee(new Employee("Alex",275,100));
        employees.addEmployee(new Employee("Steve",400));
        employees.addEmployee(new Employee("David",190,400));

        EmployeeTransformer employeeTransformer = new EmployeeTransformer();
        try {
            employeeTransformer.transform(employees);
            fail();
        } catch (Error e) {
            assertThat(e.getMessage(), containsString("Error: more than one CEO found"));
        }
    }

    @Test
    public void testOrphanedEmployeesTrowsError() throws Error {
        EmployeeStore employees = new EmployeeStore();
        employees.addEmployee(new Employee("Alan",100,150));
        employees.addEmployee(new Employee("Martin",220,100));
        employees.addEmployee(new Employee("Jamie",150));
        employees.addEmployee(new Employee("Alex",275,400));
        employees.addEmployee(new Employee("Steve",400, 275));
        employees.addEmployee(new Employee("David",190,275));

        EmployeeTransformer employeeTransformer = new EmployeeTransformer();
        try {
            employeeTransformer.transform(employees);
            fail();
        } catch (Error e) {
            assertThat(e.getMessage(), containsString("are not in the hierarchy"));
        }
    }

    @Test
    public void testHierarchy() {
        EmployeeStore employees = new EmployeeStore();

        employees.addEmployee(new Employee("Alan", 100, 150));
        employees.addEmployee(new Employee("Martin", 220, 100));
        employees.addEmployee(new Employee("Jamie", 150));
        employees.addEmployee(new Employee("Alex", 275, 100));
        employees.addEmployee(new Employee("Steve", 400, 150));
        employees.addEmployee(new Employee("David", 190, 400));

        EmployeeTransformer employeeTransformer = new EmployeeTransformer();
        List<EmployeeHierarchy> employeeHierarchy = employeeTransformer.transform(employees);

        assertThat(employeeHierarchy.size(), is(6));
        assertThat(employeeHierarchy.get(0).getName(), is("Jamie"));
        assertThat(employeeHierarchy.get(0).getIndent(), is(0));
        assertThat(employeeHierarchy.get(1).getName(), is("Alan"));
        assertThat(employeeHierarchy.get(1).getIndent(), is(1));
        assertThat(employeeHierarchy.get(2).getName(), is("Martin"));
        assertThat(employeeHierarchy.get(2).getIndent(), is(2));
        assertThat(employeeHierarchy.get(3).getName(), is("Alex"));
        assertThat(employeeHierarchy.get(3).getIndent(), is(2));
        assertThat(employeeHierarchy.get(4).getName(), is("Steve"));
        assertThat(employeeHierarchy.get(4).getIndent(), is(1));
        assertThat(employeeHierarchy.get(5).getName(), is("David"));
        assertThat(employeeHierarchy.get(5).getIndent(), is(2));
    }
}
