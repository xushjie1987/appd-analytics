package com.appdynamics.analytics.processor.query.generated;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public abstract interface ADQLVisitor<T>
  extends ParseTreeVisitor<T>
{
  public abstract T visitExpression(@NotNull ADQLParser.ExpressionContext paramExpressionContext);
  
  public abstract T visitQueryNoWith(@NotNull ADQLParser.QueryNoWithContext paramQueryNoWithContext);
  
  public abstract T visitColumnPredicated(@NotNull ADQLParser.ColumnPredicatedContext paramColumnPredicatedContext);
  
  public abstract T visitUnquotedIdentifier(@NotNull ADQLParser.UnquotedIdentifierContext paramUnquotedIdentifierContext);
  
  public abstract T visitQuery(@NotNull ADQLParser.QueryContext paramQueryContext);
  
  public abstract T visitStringLiteral(@NotNull ADQLParser.StringLiteralContext paramStringLiteralContext);
  
  public abstract T visitQuerySpecification(@NotNull ADQLParser.QuerySpecificationContext paramQuerySpecificationContext);
  
  public abstract T visitBackQuotedIdentifier(@NotNull ADQLParser.BackQuotedIdentifierContext paramBackQuotedIdentifierContext);
  
  public abstract T visitIntegerLiteral(@NotNull ADQLParser.IntegerLiteralContext paramIntegerLiteralContext);
  
  public abstract T visitBooleanValue(@NotNull ADQLParser.BooleanValueContext paramBooleanValueContext);
  
  public abstract T visitQuotedIdentifier(@NotNull ADQLParser.QuotedIdentifierContext paramQuotedIdentifierContext);
  
  public abstract T visitFunctionCall(@NotNull ADQLParser.FunctionCallContext paramFunctionCallContext);
  
  public abstract T visitQueryTermDefault(@NotNull ADQLParser.QueryTermDefaultContext paramQueryTermDefaultContext);
  
  public abstract T visitTableName(@NotNull ADQLParser.TableNameContext paramTableNameContext);
  
  public abstract T visitDigitIdentifier(@NotNull ADQLParser.DigitIdentifierContext paramDigitIdentifierContext);
  
  public abstract T visitQuotedIdentifierAlternative(@NotNull ADQLParser.QuotedIdentifierAlternativeContext paramQuotedIdentifierAlternativeContext);
  
  public abstract T visitComparisonOperator(@NotNull ADQLParser.ComparisonOperatorContext paramComparisonOperatorContext);
  
  public abstract T visitNumericLiteral(@NotNull ADQLParser.NumericLiteralContext paramNumericLiteralContext);
  
  public abstract T visitSingleStatement(@NotNull ADQLParser.SingleStatementContext paramSingleStatementContext);
  
  public abstract T visitBetween(@NotNull ADQLParser.BetweenContext paramBetweenContext);
  
  public abstract T visitQueryPrimaryDefault(@NotNull ADQLParser.QueryPrimaryDefaultContext paramQueryPrimaryDefaultContext);
  
  public abstract T visitValueExpressionDefault(@NotNull ADQLParser.ValueExpressionDefaultContext paramValueExpressionDefaultContext);
  
  public abstract T visitMatchStringReference(@NotNull ADQLParser.MatchStringReferenceContext paramMatchStringReferenceContext);
  
  public abstract T visitColumnReference(@NotNull ADQLParser.ColumnReferenceContext paramColumnReferenceContext);
  
  public abstract T visitQualifiedName(@NotNull ADQLParser.QualifiedNameContext paramQualifiedNameContext);
  
  public abstract T visitBooleanDefault(@NotNull ADQLParser.BooleanDefaultContext paramBooleanDefaultContext);
  
  public abstract T visitBooleanLiteral(@NotNull ADQLParser.BooleanLiteralContext paramBooleanLiteralContext);
  
  public abstract T visitSelectSingle(@NotNull ADQLParser.SelectSingleContext paramSelectSingleContext);
  
  public abstract T visitDecimalLiteral(@NotNull ADQLParser.DecimalLiteralContext paramDecimalLiteralContext);
  
  public abstract T visitSelectAll(@NotNull ADQLParser.SelectAllContext paramSelectAllContext);
  
  public abstract T visitComparison(@NotNull ADQLParser.ComparisonContext paramComparisonContext);
  
  public abstract T visitParenthesizedExpression(@NotNull ADQLParser.ParenthesizedExpressionContext paramParenthesizedExpressionContext);
  
  public abstract T visitStatementDefault(@NotNull ADQLParser.StatementDefaultContext paramStatementDefaultContext);
  
  public abstract T visitNullLiteral(@NotNull ADQLParser.NullLiteralContext paramNullLiteralContext);
  
  public abstract T visitPrimaryMatchString(@NotNull ADQLParser.PrimaryMatchStringContext paramPrimaryMatchStringContext);
  
  public abstract T visitNonReserved(@NotNull ADQLParser.NonReservedContext paramNonReservedContext);
  
  public abstract T visitLogicalBinary(@NotNull ADQLParser.LogicalBinaryContext paramLogicalBinaryContext);
  
  public abstract T visitMatch(@NotNull ADQLParser.MatchContext paramMatchContext);
  
  public abstract T visitRelationDefault(@NotNull ADQLParser.RelationDefaultContext paramRelationDefaultContext);
  
  public abstract T visitLogicalNot(@NotNull ADQLParser.LogicalNotContext paramLogicalNotContext);
  
  public abstract T visitSingleBooleanLiteral(@NotNull ADQLParser.SingleBooleanLiteralContext paramSingleBooleanLiteralContext);
  
  public abstract T visitSortItem(@NotNull ADQLParser.SortItemContext paramSortItemContext);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/generated/ADQLVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */