import java.lang.reflect.Array;

public class Arr {
    @SuppressWarnings("unchecked")
    public static <T> T[] removeAt(T[] array, int index) {
        if (array == null) {
            throw new NullPointerException();
        }
        if (index < 0 | index >= array.length) {
            throw new IndexOutOfBoundsException();
        }

        // ignore unchecked cast because we know newInstance returns the type we want
        T[] result = (T[])Array.newInstance(array.getClass().getComponentType(), array.length - 1);

        for (int i = 0; i < array.length; i++) {
            if (i < index) {
                result[i] = array[i];
            }
            else if (i > index) {
                result[i] = array[i - 1];
            }
        }
        
        return result;
    }

    public static <T> T[][] removeColumn(T[][] array, int index) {
        return removeAt(array, index);
    }
}