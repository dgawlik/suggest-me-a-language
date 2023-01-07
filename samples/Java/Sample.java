/*
    == Suggest me a language, 2023 ==
*/


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Sample {
    public static void main(String[] args) {
        System.out.println(isValid("{([])}"));
        System.out.println(isValid("{)[}"));
    }

    private static boolean isValid(String braces) {
        char[] tokens = braces.toCharArray();
        LinkedList<Character> stack = new LinkedList<>();
        Map<Character, Character> opposites = Map.of(']', '[', '}', '{', ')', '(');

        for (char t : tokens) {
            if ("{[(".contains("" + t)) {
                stack.offerLast(t);
            } else if (!stack.isEmpty() && "}])".contains("" + t)) {
                char top = stack.peekLast();
                if (top == opposites.get(t)) {
                    stack.pollLast();
                }
            } else {
                return false;
            }
        }

        return stack.isEmpty();
    }
}

// Running => java Sample.java