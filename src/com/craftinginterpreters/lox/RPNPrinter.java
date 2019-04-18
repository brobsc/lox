package com.craftinginterpreters.lox;

public class RPNPrinter implements Expr.Visitor<String> {
    public static void main(String[] args) {
//        Expr expression = new Expr.Binary(
//                new Expr.Unary(
//                        new Token(TokenType.MINUS, "-", null, 1),
//                        new Expr.Literal(123)),
//                new Token(TokenType.STAR, "*", null, 1),
//                new Expr.Grouping(
//                        new Expr.Literal(45.67)));

        Expr expression = new Expr.Binary(
                new Expr.Binary(
                        new Expr.Literal(1),
                        new Token(TokenType.PLUS, "+", null, 1),
                        new Expr.Literal(2)
                ),
                new Token(TokenType.STAR, "*", null, 1),

                new Expr.Binary(
                        new Expr.Grouping(
                                new Expr.Binary(
                                        new Expr.Literal(3),
                                        new Token(TokenType.SLASH, "/", null, 1),
                                        new Expr.Literal(0)
                                )
                        ),
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(3)
                )
        );

        // (1 + 2) * (4 - 3)
        System.out.println(new RPNPrinter().print(expression));
    }

    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return rpn(expr.operator.lexeme, false, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return rpn("", true, expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return rpn(expr.operator.lexeme, false, expr.right);
    }

    private String rpn(String name, boolean parens, Expr... exprs) {
        StringBuilder stringBuilder = new StringBuilder();

        if (parens) {
            stringBuilder.append("(");
        }

        for (Expr expr : exprs) {
            stringBuilder.append(expr.accept(this));
            if (!parens) stringBuilder.append(" ");
        }

        stringBuilder.append(name);

        if (parens) {
            stringBuilder.append(")");
        }

        return stringBuilder.toString();
    }
}
