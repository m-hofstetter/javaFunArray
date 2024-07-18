package util;

import static base.sign.Sign.SignElement.*;

import base.DomainValue;
import base.infint.*;
import base.interval.Interval;
import base.sign.Sign;
import funarray.Bound;
import funarray.Expression;
import funarray.FunArray;
import funarray.VariableReference;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import java.util.List;
import java.util.Set;
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
      InfInt integer = visitFiniteInteger(ctx.finiteInteger());
      if (ctx.MINUS() != null) {
        integer = integer.negate();
      }
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
  public FunArray visitFunArray(FunArrayParser.FunArrayContext ctx) {

    List<FunArrayParser.BoundContext> bs = ctx.bound();

    List<Bound> bounds = ctx
            .bound().stream()
            .map(this::visitBound)
            .toList();

    List<? extends DomainValue<?>> values = ctx.value().stream().map(this::visitValue).toList();
    var emptiness = ctx.emptiness().stream().map(this::visitEmptiness).toList();
    return new FunArray(bounds, values, emptiness);
  }

  @Override
  public DomainValue<?> visitValue(FunArrayParser.ValueContext ctx) {
    if (ctx.interval() != null) {
      return visitInterval(ctx.interval());
    } else if (ctx.sign() != null) {
      return visitSign(ctx.sign());
    }
    return null;
  }

  @Override
  public Sign visitSign(FunArrayParser.SignContext ctx) {
    if (ctx.UNREACHABLE() != null) {
      return new Sign(Set.of());
    }
    if (ctx.GREATER_ZERO() != null) {
      return new Sign(Set.of(POSITIVE));
    }
    if (ctx.LESS_ZERO() != null) {
      return new Sign(Set.of(NEGATIVE));
    }
    if (ctx.ZERO() != null) {
      return new Sign(Set.of(ZERO));
    }
    if (ctx.NOT_ZERO() != null) {
      return new Sign(Set.of(NEGATIVE, POSITIVE));
    }
    if (ctx.GREATER_EQUAL_ZERO() != null) {
      return new Sign(Set.of(ZERO, POSITIVE));
    }
    if (ctx.LESS_EQUAL_ZERO() != null) {
      return new Sign(Set.of(NEGATIVE, ZERO));
    }
    if (ctx.UNKNOWN() != null) {
      return new Sign(Set.of(NEGATIVE, ZERO, POSITIVE));
    }
    return null;
  }

  @Override
  public FunArray<? extends DomainValue<?>> visit(ParseTree tree) {
    return (FunArray) super.visit(tree);
  }

  private static FunArray<? extends DomainValue<?>> parseFunArray(String s) {
    FunArrayLexer lexer = new FunArrayLexer(CharStreams.fromString(s));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    FunArrayParser parser = new FunArrayParser(tokens);
    ParseTree tree = parser.funArray();
    IntervalFunArrayParser visitor = new IntervalFunArrayParser();
    return visitor.visit(tree);
  }

  public static FunArray<Interval> parseIntervalFunArray(String s) {
    return (FunArray<Interval>) parseFunArray(s);
  }

  public static FunArray<Sign> parseSignFunArray(String s) {
    return (FunArray<Sign>) parseFunArray(s);
  }

}
