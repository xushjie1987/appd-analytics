package com.appdynamics.analytics.processor.query.generated;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

public abstract interface ADQLListener
  extends ParseTreeListener
{
  public abstract void enterExpression(@NotNull ADQLParser.ExpressionContext paramExpressionContext);
  
  public abstract void exitExpression(@NotNull ADQLParser.ExpressionContext paramExpressionContext);
  
  public abstract void enterQueryNoWith(@NotNull ADQLParser.QueryNoWithContext paramQueryNoWithContext);
  
  public abstract void exitQueryNoWith(@NotNull ADQLParser.QueryNoWithContext paramQueryNoWithContext);
  
  public abstract void enterColumnPredicated(@NotNull ADQLParser.ColumnPredicatedContext paramColumnPredicatedContext);
  
  public abstract void exitColumnPredicated(@NotNull ADQLParser.ColumnPredicatedContext paramColumnPredicatedContext);
  
  public abstract void enterUnquotedIdentifier(@NotNull ADQLParser.UnquotedIdentifierContext paramUnquotedIdentifierContext);
  
  public abstract void exitUnquotedIdentifier(@NotNull ADQLParser.UnquotedIdentifierContext paramUnquotedIdentifierContext);
  
  public abstract void enterQuery(@NotNull ADQLParser.QueryContext paramQueryContext);
  
  public abstract void exitQuery(@NotNull ADQLParser.QueryContext paramQueryContext);
  
  public abstract void enterStringLiteral(@NotNull ADQLParser.StringLiteralContext paramStringLiteralContext);
  
  public abstract void exitStringLiteral(@NotNull ADQLParser.StringLiteralContext paramStringLiteralContext);
  
  public abstract void enterQuerySpecification(@NotNull ADQLParser.QuerySpecificationContext paramQuerySpecificationContext);
  
  public abstract void exitQuerySpecification(@NotNull ADQLParser.QuerySpecificationContext paramQuerySpecificationContext);
  
  public abstract void enterBackQuotedIdentifier(@NotNull ADQLParser.BackQuotedIdentifierContext paramBackQuotedIdentifierContext);
  
  public abstract void exitBackQuotedIdentifier(@NotNull ADQLParser.BackQuotedIdentifierContext paramBackQuotedIdentifierContext);
  
  public abstract void enterIntegerLiteral(@NotNull ADQLParser.IntegerLiteralContext paramIntegerLiteralContext);
  
  public abstract void exitIntegerLiteral(@NotNull ADQLParser.IntegerLiteralContext paramIntegerLiteralContext);
  
  public abstract void enterBooleanValue(@NotNull ADQLParser.BooleanValueContext paramBooleanValueContext);
  
  public abstract void exitBooleanValue(@NotNull ADQLParser.BooleanValueContext paramBooleanValueContext);
  
  public abstract void enterQuotedIdentifier(@NotNull ADQLParser.QuotedIdentifierContext paramQuotedIdentifierContext);
  
  public abstract void exitQuotedIdentifier(@NotNull ADQLParser.QuotedIdentifierContext paramQuotedIdentifierContext);
  
  public abstract void enterFunctionCall(@NotNull ADQLParser.FunctionCallContext paramFunctionCallContext);
  
  public abstract void exitFunctionCall(@NotNull ADQLParser.FunctionCallContext paramFunctionCallContext);
  
  public abstract void enterQueryTermDefault(@NotNull ADQLParser.QueryTermDefaultContext paramQueryTermDefaultContext);
  
  public abstract void exitQueryTermDefault(@NotNull ADQLParser.QueryTermDefaultContext paramQueryTermDefaultContext);
  
  public abstract void enterTableName(@NotNull ADQLParser.TableNameContext paramTableNameContext);
  
  public abstract void exitTableName(@NotNull ADQLParser.TableNameContext paramTableNameContext);
  
  public abstract void enterDigitIdentifier(@NotNull ADQLParser.DigitIdentifierContext paramDigitIdentifierContext);
  
  public abstract void exitDigitIdentifier(@NotNull ADQLParser.DigitIdentifierContext paramDigitIdentifierContext);
  
  public abstract void enterQuotedIdentifierAlternative(@NotNull ADQLParser.QuotedIdentifierAlternativeContext paramQuotedIdentifierAlternativeContext);
  
  public abstract void exitQuotedIdentifierAlternative(@NotNull ADQLParser.QuotedIdentifierAlternativeContext paramQuotedIdentifierAlternativeContext);
  
  public abstract void enterComparisonOperator(@NotNull ADQLParser.ComparisonOperatorContext paramComparisonOperatorContext);
  
  public abstract void exitComparisonOperator(@NotNull ADQLParser.ComparisonOperatorContext paramComparisonOperatorContext);
  
  public abstract void enterNumericLiteral(@NotNull ADQLParser.NumericLiteralContext paramNumericLiteralContext);
  
  public abstract void exitNumericLiteral(@NotNull ADQLParser.NumericLiteralContext paramNumericLiteralContext);
  
  public abstract void enterSingleStatement(@NotNull ADQLParser.SingleStatementContext paramSingleStatementContext);
  
  public abstract void exitSingleStatement(@NotNull ADQLParser.SingleStatementContext paramSingleStatementContext);
  
  public abstract void enterBetween(@NotNull ADQLParser.BetweenContext paramBetweenContext);
  
  public abstract void exitBetween(@NotNull ADQLParser.BetweenContext paramBetweenContext);
  
  public abstract void enterQueryPrimaryDefault(@NotNull ADQLParser.QueryPrimaryDefaultContext paramQueryPrimaryDefaultContext);
  
  public abstract void exitQueryPrimaryDefault(@NotNull ADQLParser.QueryPrimaryDefaultContext paramQueryPrimaryDefaultContext);
  
  public abstract void enterValueExpressionDefault(@NotNull ADQLParser.ValueExpressionDefaultContext paramValueExpressionDefaultContext);
  
  public abstract void exitValueExpressionDefault(@NotNull ADQLParser.ValueExpressionDefaultContext paramValueExpressionDefaultContext);
  
  public abstract void enterMatchStringReference(@NotNull ADQLParser.MatchStringReferenceContext paramMatchStringReferenceContext);
  
  public abstract void exitMatchStringReference(@NotNull ADQLParser.MatchStringReferenceContext paramMatchStringReferenceContext);
  
  public abstract void enterColumnReference(@NotNull ADQLParser.ColumnReferenceContext paramColumnReferenceContext);
  
  public abstract void exitColumnReference(@NotNull ADQLParser.ColumnReferenceContext paramColumnReferenceContext);
  
  public abstract void enterQualifiedName(@NotNull ADQLParser.QualifiedNameContext paramQualifiedNameContext);
  
  public abstract void exitQualifiedName(@NotNull ADQLParser.QualifiedNameContext paramQualifiedNameContext);
  
  public abstract void enterBooleanDefault(@NotNull ADQLParser.BooleanDefaultContext paramBooleanDefaultContext);
  
  public abstract void exitBooleanDefault(@NotNull ADQLParser.BooleanDefaultContext paramBooleanDefaultContext);
  
  public abstract void enterBooleanLiteral(@NotNull ADQLParser.BooleanLiteralContext paramBooleanLiteralContext);
  
  public abstract void exitBooleanLiteral(@NotNull ADQLParser.BooleanLiteralContext paramBooleanLiteralContext);
  
  public abstract void enterSelectSingle(@NotNull ADQLParser.SelectSingleContext paramSelectSingleContext);
  
  public abstract void exitSelectSingle(@NotNull ADQLParser.SelectSingleContext paramSelectSingleContext);
  
  public abstract void enterDecimalLiteral(@NotNull ADQLParser.DecimalLiteralContext paramDecimalLiteralContext);
  
  public abstract void exitDecimalLiteral(@NotNull ADQLParser.DecimalLiteralContext paramDecimalLiteralContext);
  
  public abstract void enterSelectAll(@NotNull ADQLParser.SelectAllContext paramSelectAllContext);
  
  public abstract void exitSelectAll(@NotNull ADQLParser.SelectAllContext paramSelectAllContext);
  
  public abstract void enterComparison(@NotNull ADQLParser.ComparisonContext paramComparisonContext);
  
  public abstract void exitComparison(@NotNull ADQLParser.ComparisonContext paramComparisonContext);
  
  public abstract void enterParenthesizedExpression(@NotNull ADQLParser.ParenthesizedExpressionContext paramParenthesizedExpressionContext);
  
  public abstract void exitParenthesizedExpression(@NotNull ADQLParser.ParenthesizedExpressionContext paramParenthesizedExpressionContext);
  
  public abstract void enterStatementDefault(@NotNull ADQLParser.StatementDefaultContext paramStatementDefaultContext);
  
  public abstract void exitStatementDefault(@NotNull ADQLParser.StatementDefaultContext paramStatementDefaultContext);
  
  public abstract void enterNullLiteral(@NotNull ADQLParser.NullLiteralContext paramNullLiteralContext);
  
  public abstract void exitNullLiteral(@NotNull ADQLParser.NullLiteralContext paramNullLiteralContext);
  
  public abstract void enterPrimaryMatchString(@NotNull ADQLParser.PrimaryMatchStringContext paramPrimaryMatchStringContext);
  
  public abstract void exitPrimaryMatchString(@NotNull ADQLParser.PrimaryMatchStringContext paramPrimaryMatchStringContext);
  
  public abstract void enterNonReserved(@NotNull ADQLParser.NonReservedContext paramNonReservedContext);
  
  public abstract void exitNonReserved(@NotNull ADQLParser.NonReservedContext paramNonReservedContext);
  
  public abstract void enterLogicalBinary(@NotNull ADQLParser.LogicalBinaryContext paramLogicalBinaryContext);
  
  public abstract void exitLogicalBinary(@NotNull ADQLParser.LogicalBinaryContext paramLogicalBinaryContext);
  
  public abstract void enterMatch(@NotNull ADQLParser.MatchContext paramMatchContext);
  
  public abstract void exitMatch(@NotNull ADQLParser.MatchContext paramMatchContext);
  
  public abstract void enterRelationDefault(@NotNull ADQLParser.RelationDefaultContext paramRelationDefaultContext);
  
  public abstract void exitRelationDefault(@NotNull ADQLParser.RelationDefaultContext paramRelationDefaultContext);
  
  public abstract void enterLogicalNot(@NotNull ADQLParser.LogicalNotContext paramLogicalNotContext);
  
  public abstract void exitLogicalNot(@NotNull ADQLParser.LogicalNotContext paramLogicalNotContext);
  
  public abstract void enterSingleBooleanLiteral(@NotNull ADQLParser.SingleBooleanLiteralContext paramSingleBooleanLiteralContext);
  
  public abstract void exitSingleBooleanLiteral(@NotNull ADQLParser.SingleBooleanLiteralContext paramSingleBooleanLiteralContext);
  
  public abstract void enterSortItem(@NotNull ADQLParser.SortItemContext paramSortItemContext);
  
  public abstract void exitSortItem(@NotNull ADQLParser.SortItemContext paramSortItemContext);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/generated/ADQLListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */