package util;

import base.infint.*;
import base.interval.Interval;
import funarray.Bound;
import funarray.Expression;
import funarray.FunArray;
import funarray.VariableReference;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import java.util.List;
import java.util.stream.Collectors;

public class IntervalFunArrayParser extends FunArrayBaseVisitor {
  @Override
  public FiniteInteger visitFiniteInteger(FunArrayParser.FiniteIntegerContext ctx) {
    return new FiniteInteger(Integer.parseInt(ctx.getText()));
  }

  @Override
  public Infinity visitInfinity(FunArrayParser.InfinityContext ctx) {
    if (ctx.POS_INF() != null) {
      return new PositiveInfinity();
    }
    if (ctx.NEG_INF() != null) {
      return new NegativeInfinity();
    }
    return null;
  }

  @Override
  public InfInt visitInteger(FunArrayParser.IntegerContext ctx) {
    if (ctx.finiteInteger() != null) {
      return visitFiniteInteger(ctx.finiteInteger());
    }
    if (ctx.infinity() != null) {
      return visitInfinity(ctx.infinity());
    }
    return null;
  }

  @Override
  public Interval visitInterval(FunArrayParser.IntervalContext ctx) {
    if (ctx.UNREACHABLE() != null) {
      return Interval.unreachable();
    }
    return Interval.of(visitInteger(ctx.integer(0)), visitInteger(ctx.integer(1)));
  }

  @Override
  public Expression visitExpression(FunArrayParser.ExpressionContext ctx) {
    var variable = ctx.variableName() != null ? new VariableReference(ctx.variableName().getText()) : new VariableReference("0");
    if (ctx.finiteInteger() != null) {
      var integer = visitFiniteInteger(ctx.finiteInteger());
      return new Expression(variable, integer);
    }
    return new Expression(variable);
  }

  @Override
  public Bound visitBound(FunArrayParser.BoundContext ctx) {
    var expressions = ctx.expression().stream()
            .map(this::visitExpression)
            .collect(Collectors.toSet());
    return new Bound(expressions);
  }

  @Override
  public Boolean visitEmptiness(FunArrayParser.EmptinessContext ctx) {
    return ctx.getText().equals("}?");
  }

  @Override
  public FunArray<Interval> visitFunArray(FunArrayParser.FunArrayContext ctx) {

    List<FunArrayParser.BoundContext> bs = ctx.bound();

    List<Bound> bounds = ctx
            .bound().stream()
            .map(this::visitBound)
            .toList();

    var intervals = ctx.interval().stream().map(this::visitInterval).toList();
    var emptiness = ctx.emptiness().stream().map(this::visitEmptiness).toList();
    return new FunArray<>(bounds, intervals, emptiness);
  }

  @Override
  public FunArray<Interval> visit(ParseTree tree) {
    return (FunArray<Interval>) super.visit(tree);
  }

  public static FunArray<Interval> parseIntervalFunArray(String s) {
    FunArrayLexer lexer = new FunArrayLexer(CharStreams.fromString(s));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    FunArrayParser parser = new FunArrayParser(tokens);
    ParseTree tree = parser.funArray();
    IntervalFunArrayParser visitor = new IntervalFunArrayParser();
    return visitor.visit(tree);
  }


}
