package com.appdynamics.analytics.processor.query.generated;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

public abstract interface SqlBaseListener
  extends ParseTreeListener
{
  public abstract void enterShowSchemas(@NotNull SqlBaseParser.ShowSchemasContext paramShowSchemasContext);
  
  public abstract void exitShowSchemas(@NotNull SqlBaseParser.ShowSchemasContext paramShowSchemasContext);
  
  public abstract void enterCreateView(@NotNull SqlBaseParser.CreateViewContext paramCreateViewContext);
  
  public abstract void exitCreateView(@NotNull SqlBaseParser.CreateViewContext paramCreateViewContext);
  
  public abstract void enterUnquotedIdentifier(@NotNull SqlBaseParser.UnquotedIdentifierContext paramUnquotedIdentifierContext);
  
  public abstract void exitUnquotedIdentifier(@NotNull SqlBaseParser.UnquotedIdentifierContext paramUnquotedIdentifierContext);
  
  public abstract void enterRenameTable(@NotNull SqlBaseParser.RenameTableContext paramRenameTableContext);
  
  public abstract void exitRenameTable(@NotNull SqlBaseParser.RenameTableContext paramRenameTableContext);
  
  public abstract void enterTypeConstructor(@NotNull SqlBaseParser.TypeConstructorContext paramTypeConstructorContext);
  
  public abstract void exitTypeConstructor(@NotNull SqlBaseParser.TypeConstructorContext paramTypeConstructorContext);
  
  public abstract void enterQuerySpecification(@NotNull SqlBaseParser.QuerySpecificationContext paramQuerySpecificationContext);
  
  public abstract void exitQuerySpecification(@NotNull SqlBaseParser.QuerySpecificationContext paramQuerySpecificationContext);
  
  public abstract void enterStringLiteral(@NotNull SqlBaseParser.StringLiteralContext paramStringLiteralContext);
  
  public abstract void exitStringLiteral(@NotNull SqlBaseParser.StringLiteralContext paramStringLiteralContext);
  
  public abstract void enterShowColumns(@NotNull SqlBaseParser.ShowColumnsContext paramShowColumnsContext);
  
  public abstract void exitShowColumns(@NotNull SqlBaseParser.ShowColumnsContext paramShowColumnsContext);
  
  public abstract void enterWhenClause(@NotNull SqlBaseParser.WhenClauseContext paramWhenClauseContext);
  
  public abstract void exitWhenClause(@NotNull SqlBaseParser.WhenClauseContext paramWhenClauseContext);
  
  public abstract void enterOver(@NotNull SqlBaseParser.OverContext paramOverContext);
  
  public abstract void exitOver(@NotNull SqlBaseParser.OverContext paramOverContext);
  
  public abstract void enterQueryTermDefault(@NotNull SqlBaseParser.QueryTermDefaultContext paramQueryTermDefaultContext);
  
  public abstract void exitQueryTermDefault(@NotNull SqlBaseParser.QueryTermDefaultContext paramQueryTermDefaultContext);
  
  public abstract void enterParenthesizedRelation(@NotNull SqlBaseParser.ParenthesizedRelationContext paramParenthesizedRelationContext);
  
  public abstract void exitParenthesizedRelation(@NotNull SqlBaseParser.ParenthesizedRelationContext paramParenthesizedRelationContext);
  
  public abstract void enterSubstring(@NotNull SqlBaseParser.SubstringContext paramSubstringContext);
  
  public abstract void exitSubstring(@NotNull SqlBaseParser.SubstringContext paramSubstringContext);
  
  public abstract void enterJoinCriteria(@NotNull SqlBaseParser.JoinCriteriaContext paramJoinCriteriaContext);
  
  public abstract void exitJoinCriteria(@NotNull SqlBaseParser.JoinCriteriaContext paramJoinCriteriaContext);
  
  public abstract void enterInsertInto(@NotNull SqlBaseParser.InsertIntoContext paramInsertIntoContext);
  
  public abstract void exitInsertInto(@NotNull SqlBaseParser.InsertIntoContext paramInsertIntoContext);
  
  public abstract void enterExplainType(@NotNull SqlBaseParser.ExplainTypeContext paramExplainTypeContext);
  
  public abstract void exitExplainType(@NotNull SqlBaseParser.ExplainTypeContext paramExplainTypeContext);
  
  public abstract void enterColumnReference(@NotNull SqlBaseParser.ColumnReferenceContext paramColumnReferenceContext);
  
  public abstract void exitColumnReference(@NotNull SqlBaseParser.ColumnReferenceContext paramColumnReferenceContext);
  
  public abstract void enterBooleanDefault(@NotNull SqlBaseParser.BooleanDefaultContext paramBooleanDefaultContext);
  
  public abstract void exitBooleanDefault(@NotNull SqlBaseParser.BooleanDefaultContext paramBooleanDefaultContext);
  
  public abstract void enterTable(@NotNull SqlBaseParser.TableContext paramTableContext);
  
  public abstract void exitTable(@NotNull SqlBaseParser.TableContext paramTableContext);
  
  public abstract void enterLike(@NotNull SqlBaseParser.LikeContext paramLikeContext);
  
  public abstract void exitLike(@NotNull SqlBaseParser.LikeContext paramLikeContext);
  
  public abstract void enterDecimalLiteral(@NotNull SqlBaseParser.DecimalLiteralContext paramDecimalLiteralContext);
  
  public abstract void exitDecimalLiteral(@NotNull SqlBaseParser.DecimalLiteralContext paramDecimalLiteralContext);
  
  public abstract void enterExtract(@NotNull SqlBaseParser.ExtractContext paramExtractContext);
  
  public abstract void exitExtract(@NotNull SqlBaseParser.ExtractContext paramExtractContext);
  
  public abstract void enterExplainFormat(@NotNull SqlBaseParser.ExplainFormatContext paramExplainFormatContext);
  
  public abstract void exitExplainFormat(@NotNull SqlBaseParser.ExplainFormatContext paramExplainFormatContext);
  
  public abstract void enterSearchedCase(@NotNull SqlBaseParser.SearchedCaseContext paramSearchedCaseContext);
  
  public abstract void exitSearchedCase(@NotNull SqlBaseParser.SearchedCaseContext paramSearchedCaseContext);
  
  public abstract void enterSingleExpression(@NotNull SqlBaseParser.SingleExpressionContext paramSingleExpressionContext);
  
  public abstract void exitSingleExpression(@NotNull SqlBaseParser.SingleExpressionContext paramSingleExpressionContext);
  
  public abstract void enterPredicated(@NotNull SqlBaseParser.PredicatedContext paramPredicatedContext);
  
  public abstract void exitPredicated(@NotNull SqlBaseParser.PredicatedContext paramPredicatedContext);
  
  public abstract void enterArrayConstructor(@NotNull SqlBaseParser.ArrayConstructorContext paramArrayConstructorContext);
  
  public abstract void exitArrayConstructor(@NotNull SqlBaseParser.ArrayConstructorContext paramArrayConstructorContext);
  
  public abstract void enterSubscript(@NotNull SqlBaseParser.SubscriptContext paramSubscriptContext);
  
  public abstract void exitSubscript(@NotNull SqlBaseParser.SubscriptContext paramSubscriptContext);
  
  public abstract void enterLogicalBinary(@NotNull SqlBaseParser.LogicalBinaryContext paramLogicalBinaryContext);
  
  public abstract void exitLogicalBinary(@NotNull SqlBaseParser.LogicalBinaryContext paramLogicalBinaryContext);
  
  public abstract void enterExplain(@NotNull SqlBaseParser.ExplainContext paramExplainContext);
  
  public abstract void exitExplain(@NotNull SqlBaseParser.ExplainContext paramExplainContext);
  
  public abstract void enterSampledRelation(@NotNull SqlBaseParser.SampledRelationContext paramSampledRelationContext);
  
  public abstract void exitSampledRelation(@NotNull SqlBaseParser.SampledRelationContext paramSampledRelationContext);
  
  public abstract void enterSubqueryRelation(@NotNull SqlBaseParser.SubqueryRelationContext paramSubqueryRelationContext);
  
  public abstract void exitSubqueryRelation(@NotNull SqlBaseParser.SubqueryRelationContext paramSubqueryRelationContext);
  
  public abstract void enterJoinRelation(@NotNull SqlBaseParser.JoinRelationContext paramJoinRelationContext);
  
  public abstract void exitJoinRelation(@NotNull SqlBaseParser.JoinRelationContext paramJoinRelationContext);
  
  public abstract void enterInterval(@NotNull SqlBaseParser.IntervalContext paramIntervalContext);
  
  public abstract void exitInterval(@NotNull SqlBaseParser.IntervalContext paramIntervalContext);
  
  public abstract void enterShowSession(@NotNull SqlBaseParser.ShowSessionContext paramShowSessionContext);
  
  public abstract void exitShowSession(@NotNull SqlBaseParser.ShowSessionContext paramShowSessionContext);
  
  public abstract void enterQuery(@NotNull SqlBaseParser.QueryContext paramQueryContext);
  
  public abstract void exitQuery(@NotNull SqlBaseParser.QueryContext paramQueryContext);
  
  public abstract void enterUse(@NotNull SqlBaseParser.UseContext paramUseContext);
  
  public abstract void exitUse(@NotNull SqlBaseParser.UseContext paramUseContext);
  
  public abstract void enterBackQuotedIdentifier(@NotNull SqlBaseParser.BackQuotedIdentifierContext paramBackQuotedIdentifierContext);
  
  public abstract void exitBackQuotedIdentifier(@NotNull SqlBaseParser.BackQuotedIdentifierContext paramBackQuotedIdentifierContext);
  
  public abstract void enterShowTables(@NotNull SqlBaseParser.ShowTablesContext paramShowTablesContext);
  
  public abstract void exitShowTables(@NotNull SqlBaseParser.ShowTablesContext paramShowTablesContext);
  
  public abstract void enterSetQuantifier(@NotNull SqlBaseParser.SetQuantifierContext paramSetQuantifierContext);
  
  public abstract void exitSetQuantifier(@NotNull SqlBaseParser.SetQuantifierContext paramSetQuantifierContext);
  
  public abstract void enterTableName(@NotNull SqlBaseParser.TableNameContext paramTableNameContext);
  
  public abstract void exitTableName(@NotNull SqlBaseParser.TableNameContext paramTableNameContext);
  
  public abstract void enterDigitIdentifier(@NotNull SqlBaseParser.DigitIdentifierContext paramDigitIdentifierContext);
  
  public abstract void exitDigitIdentifier(@NotNull SqlBaseParser.DigitIdentifierContext paramDigitIdentifierContext);
  
  public abstract void enterComparisonOperator(@NotNull SqlBaseParser.ComparisonOperatorContext paramComparisonOperatorContext);
  
  public abstract void exitComparisonOperator(@NotNull SqlBaseParser.ComparisonOperatorContext paramComparisonOperatorContext);
  
  public abstract void enterNumericLiteral(@NotNull SqlBaseParser.NumericLiteralContext paramNumericLiteralContext);
  
  public abstract void exitNumericLiteral(@NotNull SqlBaseParser.NumericLiteralContext paramNumericLiteralContext);
  
  public abstract void enterQueryPrimaryDefault(@NotNull SqlBaseParser.QueryPrimaryDefaultContext paramQueryPrimaryDefaultContext);
  
  public abstract void exitQueryPrimaryDefault(@NotNull SqlBaseParser.QueryPrimaryDefaultContext paramQueryPrimaryDefaultContext);
  
  public abstract void enterAtTimeZone(@NotNull SqlBaseParser.AtTimeZoneContext paramAtTimeZoneContext);
  
  public abstract void exitAtTimeZone(@NotNull SqlBaseParser.AtTimeZoneContext paramAtTimeZoneContext);
  
  public abstract void enterNamedQuery(@NotNull SqlBaseParser.NamedQueryContext paramNamedQueryContext);
  
  public abstract void exitNamedQuery(@NotNull SqlBaseParser.NamedQueryContext paramNamedQueryContext);
  
  public abstract void enterShowCatalogs(@NotNull SqlBaseParser.ShowCatalogsContext paramShowCatalogsContext);
  
  public abstract void exitShowCatalogs(@NotNull SqlBaseParser.ShowCatalogsContext paramShowCatalogsContext);
  
  public abstract void enterQualifiedName(@NotNull SqlBaseParser.QualifiedNameContext paramQualifiedNameContext);
  
  public abstract void exitQualifiedName(@NotNull SqlBaseParser.QualifiedNameContext paramQualifiedNameContext);
  
  public abstract void enterDistinctFrom(@NotNull SqlBaseParser.DistinctFromContext paramDistinctFromContext);
  
  public abstract void exitDistinctFrom(@NotNull SqlBaseParser.DistinctFromContext paramDistinctFromContext);
  
  public abstract void enterBooleanLiteral(@NotNull SqlBaseParser.BooleanLiteralContext paramBooleanLiteralContext);
  
  public abstract void exitBooleanLiteral(@NotNull SqlBaseParser.BooleanLiteralContext paramBooleanLiteralContext);
  
  public abstract void enterParenthesizedExpression(@NotNull SqlBaseParser.ParenthesizedExpressionContext paramParenthesizedExpressionContext);
  
  public abstract void exitParenthesizedExpression(@NotNull SqlBaseParser.ParenthesizedExpressionContext paramParenthesizedExpressionContext);
  
  public abstract void enterStatementDefault(@NotNull SqlBaseParser.StatementDefaultContext paramStatementDefaultContext);
  
  public abstract void exitStatementDefault(@NotNull SqlBaseParser.StatementDefaultContext paramStatementDefaultContext);
  
  public abstract void enterNullLiteral(@NotNull SqlBaseParser.NullLiteralContext paramNullLiteralContext);
  
  public abstract void exitNullLiteral(@NotNull SqlBaseParser.NullLiteralContext paramNullLiteralContext);
  
  public abstract void enterRowConstructor(@NotNull SqlBaseParser.RowConstructorContext paramRowConstructorContext);
  
  public abstract void exitRowConstructor(@NotNull SqlBaseParser.RowConstructorContext paramRowConstructorContext);
  
  public abstract void enterExists(@NotNull SqlBaseParser.ExistsContext paramExistsContext);
  
  public abstract void exitExists(@NotNull SqlBaseParser.ExistsContext paramExistsContext);
  
  public abstract void enterRelationDefault(@NotNull SqlBaseParser.RelationDefaultContext paramRelationDefaultContext);
  
  public abstract void exitRelationDefault(@NotNull SqlBaseParser.RelationDefaultContext paramRelationDefaultContext);
  
  public abstract void enterBoundedFrame(@NotNull SqlBaseParser.BoundedFrameContext paramBoundedFrameContext);
  
  public abstract void exitBoundedFrame(@NotNull SqlBaseParser.BoundedFrameContext paramBoundedFrameContext);
  
  public abstract void enterLogicalNot(@NotNull SqlBaseParser.LogicalNotContext paramLogicalNotContext);
  
  public abstract void exitLogicalNot(@NotNull SqlBaseParser.LogicalNotContext paramLogicalNotContext);
  
  public abstract void enterExpression(@NotNull SqlBaseParser.ExpressionContext paramExpressionContext);
  
  public abstract void exitExpression(@NotNull SqlBaseParser.ExpressionContext paramExpressionContext);
  
  public abstract void enterTimeZoneInterval(@NotNull SqlBaseParser.TimeZoneIntervalContext paramTimeZoneIntervalContext);
  
  public abstract void exitTimeZoneInterval(@NotNull SqlBaseParser.TimeZoneIntervalContext paramTimeZoneIntervalContext);
  
  public abstract void enterInList(@NotNull SqlBaseParser.InListContext paramInListContext);
  
  public abstract void exitInList(@NotNull SqlBaseParser.InListContext paramInListContext);
  
  public abstract void enterShowPartitions(@NotNull SqlBaseParser.ShowPartitionsContext paramShowPartitionsContext);
  
  public abstract void exitShowPartitions(@NotNull SqlBaseParser.ShowPartitionsContext paramShowPartitionsContext);
  
  public abstract void enterType(@NotNull SqlBaseParser.TypeContext paramTypeContext);
  
  public abstract void exitType(@NotNull SqlBaseParser.TypeContext paramTypeContext);
  
  public abstract void enterBooleanValue(@NotNull SqlBaseParser.BooleanValueContext paramBooleanValueContext);
  
  public abstract void exitBooleanValue(@NotNull SqlBaseParser.BooleanValueContext paramBooleanValueContext);
  
  public abstract void enterQuotedIdentifier(@NotNull SqlBaseParser.QuotedIdentifierContext paramQuotedIdentifierContext);
  
  public abstract void exitQuotedIdentifier(@NotNull SqlBaseParser.QuotedIdentifierContext paramQuotedIdentifierContext);
  
  public abstract void enterInlineTable(@NotNull SqlBaseParser.InlineTableContext paramInlineTableContext);
  
  public abstract void exitInlineTable(@NotNull SqlBaseParser.InlineTableContext paramInlineTableContext);
  
  public abstract void enterFunctionCall(@NotNull SqlBaseParser.FunctionCallContext paramFunctionCallContext);
  
  public abstract void exitFunctionCall(@NotNull SqlBaseParser.FunctionCallContext paramFunctionCallContext);
  
  public abstract void enterNullPredicate(@NotNull SqlBaseParser.NullPredicateContext paramNullPredicateContext);
  
  public abstract void exitNullPredicate(@NotNull SqlBaseParser.NullPredicateContext paramNullPredicateContext);
  
  public abstract void enterUnboundedFrame(@NotNull SqlBaseParser.UnboundedFrameContext paramUnboundedFrameContext);
  
  public abstract void exitUnboundedFrame(@NotNull SqlBaseParser.UnboundedFrameContext paramUnboundedFrameContext);
  
  public abstract void enterConcatenation(@NotNull SqlBaseParser.ConcatenationContext paramConcatenationContext);
  
  public abstract void exitConcatenation(@NotNull SqlBaseParser.ConcatenationContext paramConcatenationContext);
  
  public abstract void enterDropView(@NotNull SqlBaseParser.DropViewContext paramDropViewContext);
  
  public abstract void exitDropView(@NotNull SqlBaseParser.DropViewContext paramDropViewContext);
  
  public abstract void enterColumnAliases(@NotNull SqlBaseParser.ColumnAliasesContext paramColumnAliasesContext);
  
  public abstract void exitColumnAliases(@NotNull SqlBaseParser.ColumnAliasesContext paramColumnAliasesContext);
  
  public abstract void enterCurrentRowBound(@NotNull SqlBaseParser.CurrentRowBoundContext paramCurrentRowBoundContext);
  
  public abstract void exitCurrentRowBound(@NotNull SqlBaseParser.CurrentRowBoundContext paramCurrentRowBoundContext);
  
  public abstract void enterUnnest(@NotNull SqlBaseParser.UnnestContext paramUnnestContext);
  
  public abstract void exitUnnest(@NotNull SqlBaseParser.UnnestContext paramUnnestContext);
  
  public abstract void enterSetSession(@NotNull SqlBaseParser.SetSessionContext paramSetSessionContext);
  
  public abstract void exitSetSession(@NotNull SqlBaseParser.SetSessionContext paramSetSessionContext);
  
  public abstract void enterSelectSingle(@NotNull SqlBaseParser.SelectSingleContext paramSelectSingleContext);
  
  public abstract void exitSelectSingle(@NotNull SqlBaseParser.SelectSingleContext paramSelectSingleContext);
  
  public abstract void enterComparison(@NotNull SqlBaseParser.ComparisonContext paramComparisonContext);
  
  public abstract void exitComparison(@NotNull SqlBaseParser.ComparisonContext paramComparisonContext);
  
  public abstract void enterSimpleCase(@NotNull SqlBaseParser.SimpleCaseContext paramSimpleCaseContext);
  
  public abstract void exitSimpleCase(@NotNull SqlBaseParser.SimpleCaseContext paramSimpleCaseContext);
  
  public abstract void enterDropTable(@NotNull SqlBaseParser.DropTableContext paramDropTableContext);
  
  public abstract void exitDropTable(@NotNull SqlBaseParser.DropTableContext paramDropTableContext);
  
  public abstract void enterAliasedRelation(@NotNull SqlBaseParser.AliasedRelationContext paramAliasedRelationContext);
  
  public abstract void exitAliasedRelation(@NotNull SqlBaseParser.AliasedRelationContext paramAliasedRelationContext);
  
  public abstract void enterArithmeticBinary(@NotNull SqlBaseParser.ArithmeticBinaryContext paramArithmeticBinaryContext);
  
  public abstract void exitArithmeticBinary(@NotNull SqlBaseParser.ArithmeticBinaryContext paramArithmeticBinaryContext);
  
  public abstract void enterCast(@NotNull SqlBaseParser.CastContext paramCastContext);
  
  public abstract void exitCast(@NotNull SqlBaseParser.CastContext paramCastContext);
  
  public abstract void enterIntervalField(@NotNull SqlBaseParser.IntervalFieldContext paramIntervalFieldContext);
  
  public abstract void exitIntervalField(@NotNull SqlBaseParser.IntervalFieldContext paramIntervalFieldContext);
  
  public abstract void enterSpecialDateTimeFunction(@NotNull SqlBaseParser.SpecialDateTimeFunctionContext paramSpecialDateTimeFunctionContext);
  
  public abstract void exitSpecialDateTimeFunction(@NotNull SqlBaseParser.SpecialDateTimeFunctionContext paramSpecialDateTimeFunctionContext);
  
  public abstract void enterQueryNoWith(@NotNull SqlBaseParser.QueryNoWithContext paramQueryNoWithContext);
  
  public abstract void exitQueryNoWith(@NotNull SqlBaseParser.QueryNoWithContext paramQueryNoWithContext);
  
  public abstract void enterSubqueryExpression(@NotNull SqlBaseParser.SubqueryExpressionContext paramSubqueryExpressionContext);
  
  public abstract void exitSubqueryExpression(@NotNull SqlBaseParser.SubqueryExpressionContext paramSubqueryExpressionContext);
  
  public abstract void enterIntegerLiteral(@NotNull SqlBaseParser.IntegerLiteralContext paramIntegerLiteralContext);
  
  public abstract void exitIntegerLiteral(@NotNull SqlBaseParser.IntegerLiteralContext paramIntegerLiteralContext);
  
  public abstract void enterTimeZoneString(@NotNull SqlBaseParser.TimeZoneStringContext paramTimeZoneStringContext);
  
  public abstract void exitTimeZoneString(@NotNull SqlBaseParser.TimeZoneStringContext paramTimeZoneStringContext);
  
  public abstract void enterResetSession(@NotNull SqlBaseParser.ResetSessionContext paramResetSessionContext);
  
  public abstract void exitResetSession(@NotNull SqlBaseParser.ResetSessionContext paramResetSessionContext);
  
  public abstract void enterIntervalLiteral(@NotNull SqlBaseParser.IntervalLiteralContext paramIntervalLiteralContext);
  
  public abstract void exitIntervalLiteral(@NotNull SqlBaseParser.IntervalLiteralContext paramIntervalLiteralContext);
  
  public abstract void enterArithmeticUnary(@NotNull SqlBaseParser.ArithmeticUnaryContext paramArithmeticUnaryContext);
  
  public abstract void exitArithmeticUnary(@NotNull SqlBaseParser.ArithmeticUnaryContext paramArithmeticUnaryContext);
  
  public abstract void enterSetOperation(@NotNull SqlBaseParser.SetOperationContext paramSetOperationContext);
  
  public abstract void exitSetOperation(@NotNull SqlBaseParser.SetOperationContext paramSetOperationContext);
  
  public abstract void enterQuotedIdentifierAlternative(@NotNull SqlBaseParser.QuotedIdentifierAlternativeContext paramQuotedIdentifierAlternativeContext);
  
  public abstract void exitQuotedIdentifierAlternative(@NotNull SqlBaseParser.QuotedIdentifierAlternativeContext paramQuotedIdentifierAlternativeContext);
  
  public abstract void enterSingleStatement(@NotNull SqlBaseParser.SingleStatementContext paramSingleStatementContext);
  
  public abstract void exitSingleStatement(@NotNull SqlBaseParser.SingleStatementContext paramSingleStatementContext);
  
  public abstract void enterBetween(@NotNull SqlBaseParser.BetweenContext paramBetweenContext);
  
  public abstract void exitBetween(@NotNull SqlBaseParser.BetweenContext paramBetweenContext);
  
  public abstract void enterValueExpressionDefault(@NotNull SqlBaseParser.ValueExpressionDefaultContext paramValueExpressionDefaultContext);
  
  public abstract void exitValueExpressionDefault(@NotNull SqlBaseParser.ValueExpressionDefaultContext paramValueExpressionDefaultContext);
  
  public abstract void enterWith(@NotNull SqlBaseParser.WithContext paramWithContext);
  
  public abstract void exitWith(@NotNull SqlBaseParser.WithContext paramWithContext);
  
  public abstract void enterCreateTableAsSelect(@NotNull SqlBaseParser.CreateTableAsSelectContext paramCreateTableAsSelectContext);
  
  public abstract void exitCreateTableAsSelect(@NotNull SqlBaseParser.CreateTableAsSelectContext paramCreateTableAsSelectContext);
  
  public abstract void enterSelectAll(@NotNull SqlBaseParser.SelectAllContext paramSelectAllContext);
  
  public abstract void exitSelectAll(@NotNull SqlBaseParser.SelectAllContext paramSelectAllContext);
  
  public abstract void enterWindowFrame(@NotNull SqlBaseParser.WindowFrameContext paramWindowFrameContext);
  
  public abstract void exitWindowFrame(@NotNull SqlBaseParser.WindowFrameContext paramWindowFrameContext);
  
  public abstract void enterSubquery(@NotNull SqlBaseParser.SubqueryContext paramSubqueryContext);
  
  public abstract void exitSubquery(@NotNull SqlBaseParser.SubqueryContext paramSubqueryContext);
  
  public abstract void enterNonReserved(@NotNull SqlBaseParser.NonReservedContext paramNonReservedContext);
  
  public abstract void exitNonReserved(@NotNull SqlBaseParser.NonReservedContext paramNonReservedContext);
  
  public abstract void enterSimpleType(@NotNull SqlBaseParser.SimpleTypeContext paramSimpleTypeContext);
  
  public abstract void exitSimpleType(@NotNull SqlBaseParser.SimpleTypeContext paramSimpleTypeContext);
  
  public abstract void enterInSubquery(@NotNull SqlBaseParser.InSubqueryContext paramInSubqueryContext);
  
  public abstract void exitInSubquery(@NotNull SqlBaseParser.InSubqueryContext paramInSubqueryContext);
  
  public abstract void enterJoinType(@NotNull SqlBaseParser.JoinTypeContext paramJoinTypeContext);
  
  public abstract void exitJoinType(@NotNull SqlBaseParser.JoinTypeContext paramJoinTypeContext);
  
  public abstract void enterShowFunctions(@NotNull SqlBaseParser.ShowFunctionsContext paramShowFunctionsContext);
  
  public abstract void exitShowFunctions(@NotNull SqlBaseParser.ShowFunctionsContext paramShowFunctionsContext);
  
  public abstract void enterSampleType(@NotNull SqlBaseParser.SampleTypeContext paramSampleTypeContext);
  
  public abstract void exitSampleType(@NotNull SqlBaseParser.SampleTypeContext paramSampleTypeContext);
  
  public abstract void enterSortItem(@NotNull SqlBaseParser.SortItemContext paramSortItemContext);
  
  public abstract void exitSortItem(@NotNull SqlBaseParser.SortItemContext paramSortItemContext);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/generated/SqlBaseListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */