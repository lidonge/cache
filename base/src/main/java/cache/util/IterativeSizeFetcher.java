package cache.util;
import org.openjdk.jol.vm.VM;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * @author lidong@date 2024-07-04@version 1.0
 */

public class IterativeSizeFetcher {

    public static int getDeepObjectSize(Object obj) {
        if (obj == null) {
            return 0;
        }

        Set<Object> visited = new HashSet<>();
        Stack<Object> stack = new Stack<>();
        stack.push(obj);
        int totalSize = 0;

        while (!stack.isEmpty()) {
            Object current = stack.pop();

            if (current == null || visited.contains(current)) {
                continue;
            }
            visited.add(current);

            // Calculate the size of the current object
            totalSize += VM.current().sizeOf(current);

            Class<?> clazz = current.getClass();

            // Process arrays
            if (clazz.isArray()) {
                int length = Array.getLength(current);
                for (int i = 0; i < length; i++) {
                    Object arrayElement = Array.get(current, i);
                    if (arrayElement != null && !visited.contains(arrayElement)) {
                        stack.push(arrayElement);
                    }
                }
            } else {
                // Process fields of the object
                while (clazz != null) {
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field : fields) {
                        if (!Modifier.isStatic(field.getModifiers())) {
//                            System.out.println("========"+field.getName() + ","+field.getType().getName());
                            field.setAccessible(true);
                            try {
                                Object fieldValue = field.get(current);
                                if (fieldValue != null && !visited.contains(fieldValue)) {
                                    stack.push(fieldValue);
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    clazz = clazz.getSuperclass();
                }
            }
        }

        return totalSize;
    }

}
