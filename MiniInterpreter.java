import java.util.*;

public class MiniInterpreter {

    private final Map<String, Integer> variables = new HashMap<>();

    public void execute(String input) {
        String[] lines = input.split("\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("let ")) {
                handleLet(line);
            } else if (line.startsWith("if ")) {
                System.out.println(handleIf(line));
            } else if (!line.isEmpty()) {
                System.out.println(evaluate(line));
            }
        }
    }

    private void handleLet(String line) {
        String[] parts = line.substring(4).split("=");
        String name = parts[0].trim();
        String expr = parts[1].trim();
        int value = evaluate(expr);
        variables.put(name, value);
    }

    private int handleIf(String line) {
        int thenIdx = line.indexOf("then");
        int elseIdx = line.indexOf("else");
        String condition = line.substring(3, thenIdx).trim();
        String thenExpr = line.substring(thenIdx + 4, elseIdx).trim();
        String elseExpr = line.substring(elseIdx + 4).trim();

        int result = evaluate(condition);
        return result != 0 ? evaluate(thenExpr) : evaluate(elseExpr);
    }

    private int evaluate(String expr) {
        expr = replaceVars(expr);

        if (expr.contains("==")) return compare(expr, "==");
        if (expr.contains(">")) return compare(expr, ">");
        if (expr.contains("<")) return compare(expr, "<");
        if (expr.contains("+")) return calc(expr, "+");
        if (expr.contains("-")) return calc(expr, "-");
        if (expr.contains("*")) return calc(expr, "*");
        if (expr.contains("/")) return calc(expr, "/");

        return Integer.parseInt(expr.trim());
    }

    private int compare(String expr, String op) {
        String[] parts = expr.split(op);
        int a = Integer.parseInt(parts[0].trim());
        int b = Integer.parseInt(parts[1].trim());
        return switch (op) {
            case "==" -> a == b ? 1 : 0;
            case ">" -> a > b ? 1 : 0;
            case "<" -> a < b ? 1 : 0;
            default -> 0;
        };
    }

    private int calc(String expr, String op) {
        String[] parts = expr.split("\\" + op);
        int a = Integer.parseInt(parts[0].trim());
        int b = Integer.parseInt(parts[1].trim());
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            default -> 0;
        };
    }

    private String replaceVars(String expr) {
        for (String var : variables.keySet()) {
            expr = expr.replaceAll("\\b" + var + "\\b", String.valueOf(variables.get(var)));
        }
        return expr;
    }

    public static void main(String[] args) {
        String input = """
                let x = 10
                let y = 20
                if x < y then y else x
                let sum = x + y
                sum
                """;

        MiniInterpreter interpreter = new MiniInterpreter();
        interpreter.execute(input);
    }
}
