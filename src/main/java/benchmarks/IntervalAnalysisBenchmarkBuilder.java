package benchmarks;

import abstractdomain.interval.value.Interval;
import analysis.common.Analysis;
import analysis.common.AnalysisContext;
import analysis.common.condition.Condition;
import analysis.common.condition.EqualTo;
import analysis.common.condition.LessThan;
import analysis.common.condition.UnequalTo;
import analysis.common.controlstructure.Block;
import analysis.common.controlstructure.IfThenElse;
import analysis.common.controlstructure.While;
import analysis.common.expression.Assignable;
import analysis.common.expression.Expression;
import analysis.common.expression.associative.Addition;
import analysis.common.expression.associative.Multiplication;
import analysis.common.expression.atom.ArrayElement;
import analysis.common.expression.atom.Constant;
import analysis.common.expression.atom.Variable;
import analysis.common.expression.nonassociative.Division;
import analysis.common.expression.nonassociative.Modulo;
import analysis.common.expression.nonassociative.Subtraction;
import analysis.common.statement.Assign;
import analysis.interval.IntervalAnalysisContext;
import java.util.List;
import java.util.Set;

public class IntervalAnalysisBenchmarkBuilder
        implements BenchmarkProgram<
        Expression<Interval, Interval>,
        Condition<Interval, Interval>,
        Analysis<Interval, Interval>,
        Assignable<Interval, Interval>
        > {

  public IntervalAnalysisBenchmarkBuilder() {
  }

  private static final AnalysisContext<Interval, Interval> CONTEXT = IntervalAnalysisContext.INSTANCE;

  @Override
  public Analysis<Interval, Interval> while_(Condition<Interval, Interval> condition, Analysis<Interval, Interval> body) {
    return new While<>(condition, body, CONTEXT);
  }

  @Override
  public Analysis<Interval, Interval> if_(Condition<Interval, Interval> condition, Analysis<Interval, Interval> ifBody, Analysis<Interval, Interval> elseBody) {
    return new IfThenElse<>(condition, ifBody, elseBody, CONTEXT);
  }

  @Override
  public Analysis<Interval, Interval> block(List<Analysis<Interval, Interval>> statements) {
    return new Block<>(statements);
  }

  @Override
  public Analysis<Interval, Interval> assign(Assignable<Interval, Interval> assignee, Expression<Interval, Interval> value) {
    return new Assign<>(value, assignee);
  }

  @Override
  public Analysis<Interval, Interval> havoc(Assignable<Interval, Interval> assignee) {
    //TODO
    return null;
  }

  @Override
  public Analysis<Interval, Interval> assume(Condition<Interval, Interval> condition) {
    //TODO
    return null;
  }

  @Override
  public Analysis<Interval, Interval> assert_(Condition<Interval, Interval> condition) {
    //TODO
    return null;
  }

  @Override
  public Expression<Interval, Interval> constant(int value) {
    return new Constant<>(value, CONTEXT);
  }

  @Override
  public Expression<Interval, Interval> addition(Set<Expression<Interval, Interval>> summands) {
    return new Addition<>(summands, CONTEXT);
  }

  @Override
  public Expression<Interval, Interval> multiplication(Set<Expression<Interval, Interval>> factors) {
    return new Multiplication<>(factors, CONTEXT);
  }

  @Override
  public Expression<Interval, Interval> subtraction(Expression<Interval, Interval> minuend, Expression<Interval, Interval> subtrahend) {
    return new Subtraction<>(minuend, subtrahend, CONTEXT);
  }

  @Override
  public Expression<Interval, Interval> division(Expression<Interval, Interval> dividend, Expression<Interval, Interval> divisor) {
    return new Division<>(dividend, divisor, CONTEXT);
  }

  @Override
  public Expression<Interval, Interval> modulo(Expression<Interval, Interval> dividend, Expression<Interval, Interval> divisor) {
    return new Modulo<>(dividend, divisor, CONTEXT);
  }

  @Override
  public Assignable<Interval, Interval> variable(String varName) {
    return new Variable<>(varName);
  }

  @Override
  public Assignable<Interval, Interval> arrayElement(String arrayRef, Expression<Interval, Interval> index) {
    return new ArrayElement<>(arrayRef, index, CONTEXT);
  }

  @Override
  public Condition<Interval, Interval> lessThan(Expression<Interval, Interval> left, Expression<Interval, Interval> right) {
    return new LessThan<>(left, right, CONTEXT);
  }

  @Override
  public Condition<Interval, Interval> lessEqualThan(Expression<Interval, Interval> left, Expression<Interval, Interval> right) {
    return new LessThan<>(left, right, CONTEXT);
  }

  @Override
  public Condition<Interval, Interval> greaterThan(Expression<Interval, Interval> left, Expression<Interval, Interval> right) {
    //TODO
    return null;
  }

  @Override
  public Condition<Interval, Interval> greaterEqualThan(Expression<Interval, Interval> left, Expression<Interval, Interval> right) {
    //TODO
    return null;
  }

  @Override
  public Condition<Interval, Interval> equalTo(Expression<Interval, Interval> left, Expression<Interval, Interval> right) {
    return new EqualTo<>(left, right, CONTEXT);
  }

  @Override
  public Condition<Interval, Interval> unequalTo(Expression<Interval, Interval> left, Expression<Interval, Interval> right) {
    return new UnequalTo<>(left, right, CONTEXT);
  }

  @Override
  public Condition<Interval, Interval> true_() {
    //TODO
    return null;
  }

  @Override
  public Condition<Interval, Interval> false_() {
    //TODO
    return null;
  }
}
