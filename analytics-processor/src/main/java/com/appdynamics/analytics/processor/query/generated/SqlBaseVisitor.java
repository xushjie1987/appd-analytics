package com.appdynamics.analytics.processor.query.generated;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public abstract interface SqlBaseVisitor<T>
  extends ParseTreeVisitor<T>
{
  public abstract T visitShowSchemas(@NotNull SqlBaseParser.ShowSchemasContext paramShowSchemasContext);
  
  public abstract T visitCreateView(@NotNull SqlBaseParser.CreateViewContext paramCreateViewContext);
  
  public abstract T visitUnquotedIdentifier(@NotNull SqlBaseParser.UnquotedIdentifierContext paramUnquotedIdentifierContext);
  
  public abstract T visitRenameTable(@NotNull SqlBaseParser.RenameTableContext paramRenameTableContext);
  
  public abstract T visitTypeConstructor(@NotNull SqlBaseParser.TypeConstructorContext paramTypeConstructorContext);
  
  public abstract T visitQuerySpecification(@NotNull SqlBaseParser.QuerySpecificationContext paramQuerySpecificationContext);
  
  public abstract T visitStringLiteral(@NotNull SqlBaseParser.StringLiteralContext paramStringLiteralContext);
  
  public abstract T visitShowColumns(@NotNull SqlBaseParser.ShowColumnsContext paramShowColumnsContext);
  
  public abstract T visitWhenClause(@NotNull SqlBaseParser.WhenClauseContext paramWhenClauseContext);
  
  public abstract T visitOver(@NotNull SqlBaseParser.OverContext paramOverContext);
  
  public abstract T visitQueryTermDefault(@NotNull SqlBaseParser.QueryTermDefaultContext paramQueryTermDefaultContext);
  
  public abstract T visitParenthesizedRelation(@NotNull SqlBaseParser.ParenthesizedRelationContext paramParenthesizedRelationContext);
  
  public abstract T visitSubstring(@NotNull SqlBaseParser.SubstringContext paramSubstringContext);
  
  public abstract T visitJoinCriteria(@NotNull SqlBaseParser.JoinCriteriaContext paramJoinCriteriaContext);
  
  public abstract T visitInsertInto(@NotNull SqlBaseParser.InsertIntoContext paramInsertIntoContext);
  
  public abstract T visitExplainType(@NotNull SqlBaseParser.ExplainTypeContext paramExplainTypeContext);
  
  public abstract T visitColumnReference(@NotNull SqlBaseParser.ColumnReferenceContext paramColumnReferenceContext);
  
  public abstract T visitBooleanDefault(@NotNull SqlBaseParser.BooleanDefaultContext paramBooleanDefaultContext);
  
  public abstract T visitTable(@NotNull SqlBaseParser.TableContext paramTableContext);
  
  public abstract T visitLike(@NotNull SqlBaseParser.LikeContext paramLikeContext);
  
  public abstract T visitDecimalLiteral(@NotNull SqlBaseParser.DecimalLiteralContext paramDecimalLiteralContext);
  
  public abstract T visitExtract(@NotNull SqlBaseParser.ExtractContext paramExtractContext);
  
  public abstract T visitExplainFormat(@NotNull SqlBaseParser.ExplainFormatContext paramExplainFormatContext);
  
  public abstract T visitSearchedCase(@NotNull SqlBaseParser.SearchedCaseContext paramSearchedCaseContext);
  
  public abstract T visitSingleExpression(@NotNull SqlBaseParser.SingleExpressionContext paramSingleExpressionContext);
  
  public abstract T visitPredicated(@NotNull SqlBaseParser.PredicatedContext paramPredicatedContext);
  
  public abstract T visitArrayConstructor(@NotNull SqlBaseParser.ArrayConstructorContext paramArrayConstructorContext);
  
  public abstract T visitSubscript(@NotNull SqlBaseParser.SubscriptContext paramSubscriptContext);
  
  public abstract T visitLogicalBinary(@NotNull SqlBaseParser.LogicalBinaryContext paramLogicalBinaryContext);
  
  public abstract T visitExplain(@NotNull SqlBaseParser.ExplainContext paramExplainContext);
  
  public abstract T visitSampledRelation(@NotNull SqlBaseParser.SampledRelationContext paramSampledRelationContext);
  
  public abstract T visitSubqueryRelation(@NotNull SqlBaseParser.SubqueryRelationContext paramSubqueryRelationContext);
  
  public abstract T visitJoinRelation(@NotNull SqlBaseParser.JoinRelationContext paramJoinRelationContext);
  
  public abstract T visitInterval(@NotNull SqlBaseParser.IntervalContext paramIntervalContext);
  
  public abstract T visitShowSession(@NotNull SqlBaseParser.ShowSessionContext paramShowSessionContext);
  
  public abstract T visitQuery(@NotNull SqlBaseParser.QueryContext paramQueryContext);
  
  public abstract T visitUse(@NotNull SqlBaseParser.UseContext paramUseContext);
  
  public abstract T visitBackQuotedIdentifier(@NotNull SqlBaseParser.BackQuotedIdentifierContext paramBackQuotedIdentifierContext);
  
  public abstract T visitShowTables(@NotNull SqlBaseParser.ShowTablesContext paramShowTablesContext);
  
  public abstract T visitSetQuantifier(@NotNull SqlBaseParser.SetQuantifierContext paramSetQuantifierContext);
  
  public abstract T visitTableName(@NotNull SqlBaseParser.TableNameContext paramTableNameContext);
  
  public abstract T visitDigitIdentifier(@NotNull SqlBaseParser.DigitIdentifierContext paramDigitIdentifierContext);
  
  public abstract T visitComparisonOperator(@NotNull SqlBaseParser.ComparisonOperatorContext paramComparisonOperatorContext);
  
  public abstract T visitNumericLiteral(@NotNull SqlBaseParser.NumericLiteralContext paramNumericLiteralContext);
  
  public abstract T visitQueryPrimaryDefault(@NotNull SqlBaseParser.QueryPrimaryDefaultContext paramQueryPrimaryDefaultContext);
  
  public abstract T visitAtTimeZone(@NotNull SqlBaseParser.AtTimeZoneContext paramAtTimeZoneContext);
  
  public abstract T visitNamedQuery(@NotNull SqlBaseParser.NamedQueryContext paramNamedQueryContext);
  
  public abstract T visitShowCatalogs(@NotNull SqlBaseParser.ShowCatalogsContext paramShowCatalogsContext);
  
  public abstract T visitQualifiedName(@NotNull SqlBaseParser.QualifiedNameContext paramQualifiedNameContext);
  
  public abstract T visitDistinctFrom(@NotNull SqlBaseParser.DistinctFromContext paramDistinctFromContext);
  
  public abstract T visitBooleanLiteral(@NotNull SqlBaseParser.BooleanLiteralContext paramBooleanLiteralContext);
  
  public abstract T visitParenthesizedExpression(@NotNull SqlBaseParser.ParenthesizedExpressionContext paramParenthesizedExpressionContext);
  
  public abstract T visitStatementDefault(@NotNull SqlBaseParser.StatementDefaultContext paramStatementDefaultContext);
  
  public abstract T visitNullLiteral(@NotNull SqlBaseParser.NullLiteralContext paramNullLiteralContext);
  
  public abstract T visitRowConstructor(@NotNull SqlBaseParser.RowConstructorContext paramRowConstructorContext);
  
  public abstract T visitExists(@NotNull SqlBaseParser.ExistsContext paramExistsContext);
  
  public abstract T visitRelationDefault(@NotNull SqlBaseParser.RelationDefaultContext paramRelationDefaultContext);
  
  public abstract T visitBoundedFrame(@NotNull SqlBaseParser.BoundedFrameContext paramBoundedFrameContext);
  
  public abstract T visitLogicalNot(@NotNull SqlBaseParser.LogicalNotContext paramLogicalNotContext);
  
  public abstract T visitExpression(@NotNull SqlBaseParser.ExpressionContext paramExpressionContext);
  
  public abstract T visitTimeZoneInterval(@NotNull SqlBaseParser.TimeZoneIntervalContext paramTimeZoneIntervalContext);
  
  public abstract T visitInList(@NotNull SqlBaseParser.InListContext paramInListContext);
  
  public abstract T visitShowPartitions(@NotNull SqlBaseParser.ShowPartitionsContext paramShowPartitionsContext);
  
  public abstract T visitType(@NotNull SqlBaseParser.TypeContext paramTypeContext);
  
  public abstract T visitBooleanValue(@NotNull SqlBaseParser.BooleanValueContext paramBooleanValueContext);
  
  public abstract T visitQuotedIdentifier(@NotNull SqlBaseParser.QuotedIdentifierContext paramQuotedIdentifierContext);
  
  public abstract T visitInlineTable(@NotNull SqlBaseParser.InlineTableContext paramInlineTableContext);
  
  public abstract T visitFunctionCall(@NotNull SqlBaseParser.FunctionCallContext paramFunctionCallContext);
  
  public abstract T visitNullPredicate(@NotNull SqlBaseParser.NullPredicateContext paramNullPredicateContext);
  
  public abstract T visitUnboundedFrame(@NotNull SqlBaseParser.UnboundedFrameContext paramUnboundedFrameContext);
  
  public abstract T visitConcatenation(@NotNull SqlBaseParser.ConcatenationContext paramConcatenationContext);
  
  public abstract T visitDropView(@NotNull SqlBaseParser.DropViewContext paramDropViewContext);
  
  public abstract T visitColumnAliases(@NotNull SqlBaseParser.ColumnAliasesContext paramColumnAliasesContext);
  
  public abstract T visitCurrentRowBound(@NotNull SqlBaseParser.CurrentRowBoundContext paramCurrentRowBoundContext);
  
  public abstract T visitUnnest(@NotNull SqlBaseParser.UnnestContext paramUnnestContext);
  
  public abstract T visitSetSession(@NotNull SqlBaseParser.SetSessionContext paramSetSessionContext);
  
  public abstract T visitSelectSingle(@NotNull SqlBaseParser.SelectSingleContext paramSelectSingleContext);
  
  public abstract T visitComparison(@NotNull SqlBaseParser.ComparisonContext paramComparisonContext);
  
  public abstract T visitSimpleCase(@NotNull SqlBaseParser.SimpleCaseContext paramSimpleCaseContext);
  
  public abstract T visitDropTable(@NotNull SqlBaseParser.DropTableContext paramDropTableContext);
  
  public abstract T visitAliasedRelation(@NotNull SqlBaseParser.AliasedRelationContext paramAliasedRelationContext);
  
  public abstract T visitArithmeticBinary(@NotNull SqlBaseParser.ArithmeticBinaryContext paramArithmeticBinaryContext);
  
  public abstract T visitCast(@NotNull SqlBaseParser.CastContext paramCastContext);
  
  public abstract T visitIntervalField(@NotNull SqlBaseParser.IntervalFieldContext paramIntervalFieldContext);
  
  public abstract T visitSpecialDateTimeFunction(@NotNull SqlBaseParser.SpecialDateTimeFunctionContext paramSpecialDateTimeFunctionContext);
  
  public abstract T visitQueryNoWith(@NotNull SqlBaseParser.QueryNoWithContext paramQueryNoWithContext);
  
  public abstract T visitSubqueryExpression(@NotNull SqlBaseParser.SubqueryExpressionContext paramSubqueryExpressionContext);
  
  public abstract T visitIntegerLiteral(@NotNull SqlBaseParser.IntegerLiteralContext paramIntegerLiteralContext);
  
  public abstract T visitTimeZoneString(@NotNull SqlBaseParser.TimeZoneStringContext paramTimeZoneStringContext);
  
  public abstract T visitResetSession(@NotNull SqlBaseParser.ResetSessionContext paramResetSessionContext);
  
  public abstract T visitIntervalLiteral(@NotNull SqlBaseParser.IntervalLiteralContext paramIntervalLiteralContext);
  
  public abstract T visitArithmeticUnary(@NotNull SqlBaseParser.ArithmeticUnaryContext paramArithmeticUnaryContext);
  
  public abstract T visitSetOperation(@NotNull SqlBaseParser.SetOperationContext paramSetOperationContext);
  
  public abstract T visitQuotedIdentifierAlternative(@NotNull SqlBaseParser.QuotedIdentifierAlternativeContext paramQuotedIdentifierAlternativeContext);
  
  public abstract T visitSingleStatement(@NotNull SqlBaseParser.SingleStatementContext paramSingleStatementContext);
  
  public abstract T visitBetween(@NotNull SqlBaseParser.BetweenContext paramBetweenContext);
  
  public abstract T visitValueExpressionDefault(@NotNull SqlBaseParser.ValueExpressionDefaultContext paramValueExpressionDefaultContext);
  
  public abstract T visitWith(@NotNull SqlBaseParser.WithContext paramWithContext);
  
  public abstract T visitCreateTableAsSelect(@NotNull SqlBaseParser.CreateTableAsSelectContext paramCreateTableAsSelectContext);
  
  public abstract T visitSelectAll(@NotNull SqlBaseParser.SelectAllContext paramSelectAllContext);
  
  public abstract T visitWindowFrame(@NotNull SqlBaseParser.WindowFrameContext paramWindowFrameContext);
  
  public abstract T visitSubquery(@NotNull SqlBaseParser.SubqueryContext paramSubqueryContext);
  
  public abstract T visitNonReserved(@NotNull SqlBaseParser.NonReservedContext paramNonReservedContext);
  
  public abstract T visitSimpleType(@NotNull SqlBaseParser.SimpleTypeContext paramSimpleTypeContext);
  
  public abstract T visitInSubquery(@NotNull SqlBaseParser.InSubqueryContext paramInSubqueryContext);
  
  public abstract T visitJoinType(@NotNull SqlBaseParser.JoinTypeContext paramJoinTypeContext);
  
  public abstract T visitShowFunctions(@NotNull SqlBaseParser.ShowFunctionsContext paramShowFunctionsContext);
  
  public abstract T visitSampleType(@NotNull SqlBaseParser.SampleTypeContext paramSampleTypeContext);
  
  public abstract T visitSortItem(@NotNull SqlBaseParser.SortItemContext paramSortItemContext);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/generated/SqlBaseVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */