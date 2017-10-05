package models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StudentStore {
    private static StudentStore instance;
    private Map<Integer, Student> students = new HashMap<>();


    public StudentStore() {
        students.put(
                1, new Student(1, "Bobby", "Blobby", "jonny.cavell@gmail.clom", "Kt6 6QT", "01/09/2070"));
    }


    public static StudentStore getInstance() {
        if (instance == null) {
            instance = new StudentStore();
        }
        return instance;
    }

    public Student addStudent(Student student) {
        int id = students.size();
        student.setId(id);
        students.put(id, student);
        return student;
    }

    public Student getStudent(int id) {
        return students.get(id);
    }

    public Set<Student> getAllStudents() {
        return new HashSet<>(students.values());
    }

    public Student updateStudent(Student student) {
        int id = student.getId();
        if (students.containsKey(id)) {
            students.put(id, student);
            return student;
        }
        return null;
    }

    public boolean deleteStudent(int id) {
        return students.remove(id) != null;
    }
}