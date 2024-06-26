import java.util.ArrayList;

public class TitanicSimulator extends TitanicSimulatorBase {
    // Constructor(s)
    public TitanicSimulator() {
    }


    // Sorts students by the requested method, the returned list should contain the
    //  number of students equal to 'studentCount' and ordered by the requested method.
    public ArrayList<Student> pickStudents(Skyward skyward, int studentCount, SortMethod sortMethod) {
        final ArrayList<Student> initial = skyward.getStudents();
        final ArrayList<Student> result = new ArrayList<>(initial.size());
        
        while (result.size() < studentCount /* initial.size() > 0 */) {
            int nextToRemoveIdx = 0;
            for (int i = 0; i < initial.size(); i++) {
                final int comparason = compareStudents(initial.get(i), initial.get(nextToRemoveIdx), sortMethod);
                if (comparason < 0) {
                    nextToRemoveIdx = i;
                }
            }
            result.add(initial.remove(nextToRemoveIdx));
        }

        // while (result.size() > studentCount) {
        //     result.remove(studentCount);
        // }
        return result;
    }

    // TODO: You can add you own methods here (like a sort method, mayhaps)
    
    // This is a helper method you can use. It compares two students using your the provided comparison method. 
    private int compareStudents(Student a, Student b, SortMethod sortMethod) {
        if (sortMethod == SortMethod.FIRST_NAME) {
            return a.getFirstName().toUpperCase().compareTo(b.getFirstName().toUpperCase());
        }
        else if (sortMethod == SortMethod.LAST_NAME) {
            return a.getLastName().toUpperCase().compareTo(b.getLastName().toUpperCase());
        }
        else if (sortMethod == SortMethod.STUDENT_ID) {
            return Integer.compare(a.getStudentId(), b.getStudentId());
        }
        else { // sortMethod == SortMethod.GRADE
            return Double.compare(b.getGrade(), a.getGrade());
        }
    }

    // Here's another helper method for you. It copies the first 'count' students into a new ArrayList for you.
    private ArrayList<Student> copyFirstN(ArrayList<Student> students, int count) {
        ArrayList<Student> copy = new ArrayList<Student>();
        for (int i = 0; (i < count) && (i < students.size()); i++) {
            copy.add( students.get(i) );
        }
        return copy;
    }
}
