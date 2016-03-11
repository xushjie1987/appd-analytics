/*     */ package com.appdynamics.analytics.processor.query.generated;
/*     */ 
/*     */ import org.antlr.v4.runtime.misc.NotNull;
/*     */ import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SqlBaseBaseVisitor<T>
/*     */   extends AbstractParseTreeVisitor<T>
/*     */   implements SqlBaseVisitor<T>
/*     */ {
/*     */   public T visitShowSchemas(@NotNull SqlBaseParser.ShowSchemasContext ctx)
/*     */   {
/*  21 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitCreateView(@NotNull SqlBaseParser.CreateViewContext ctx)
/*     */   {
/*  29 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitUnquotedIdentifier(@NotNull SqlBaseParser.UnquotedIdentifierContext ctx)
/*     */   {
/*  37 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitRenameTable(@NotNull SqlBaseParser.RenameTableContext ctx)
/*     */   {
/*  45 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitTypeConstructor(@NotNull SqlBaseParser.TypeConstructorContext ctx)
/*     */   {
/*  53 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQuerySpecification(@NotNull SqlBaseParser.QuerySpecificationContext ctx)
/*     */   {
/*  61 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitStringLiteral(@NotNull SqlBaseParser.StringLiteralContext ctx)
/*     */   {
/*  69 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitShowColumns(@NotNull SqlBaseParser.ShowColumnsContext ctx)
/*     */   {
/*  77 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitWhenClause(@NotNull SqlBaseParser.WhenClauseContext ctx)
/*     */   {
/*  85 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitOver(@NotNull SqlBaseParser.OverContext ctx)
/*     */   {
/*  93 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQueryTermDefault(@NotNull SqlBaseParser.QueryTermDefaultContext ctx)
/*     */   {
/* 101 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitParenthesizedRelation(@NotNull SqlBaseParser.ParenthesizedRelationContext ctx)
/*     */   {
/* 109 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSubstring(@NotNull SqlBaseParser.SubstringContext ctx)
/*     */   {
/* 117 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitJoinCriteria(@NotNull SqlBaseParser.JoinCriteriaContext ctx)
/*     */   {
/* 125 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitInsertInto(@NotNull SqlBaseParser.InsertIntoContext ctx)
/*     */   {
/* 133 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitExplainType(@NotNull SqlBaseParser.ExplainTypeContext ctx)
/*     */   {
/* 141 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitColumnReference(@NotNull SqlBaseParser.ColumnReferenceContext ctx)
/*     */   {
/* 149 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitBooleanDefault(@NotNull SqlBaseParser.BooleanDefaultContext ctx)
/*     */   {
/* 157 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitTable(@NotNull SqlBaseParser.TableContext ctx)
/*     */   {
/* 165 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitLike(@NotNull SqlBaseParser.LikeContext ctx)
/*     */   {
/* 173 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitDecimalLiteral(@NotNull SqlBaseParser.DecimalLiteralContext ctx)
/*     */   {
/* 181 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitExtract(@NotNull SqlBaseParser.ExtractContext ctx)
/*     */   {
/* 189 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitExplainFormat(@NotNull SqlBaseParser.ExplainFormatContext ctx)
/*     */   {
/* 197 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSearchedCase(@NotNull SqlBaseParser.SearchedCaseContext ctx)
/*     */   {
/* 205 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSingleExpression(@NotNull SqlBaseParser.SingleExpressionContext ctx)
/*     */   {
/* 213 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitPredicated(@NotNull SqlBaseParser.PredicatedContext ctx)
/*     */   {
/* 221 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitArrayConstructor(@NotNull SqlBaseParser.ArrayConstructorContext ctx)
/*     */   {
/* 229 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSubscript(@NotNull SqlBaseParser.SubscriptContext ctx)
/*     */   {
/* 237 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitLogicalBinary(@NotNull SqlBaseParser.LogicalBinaryContext ctx)
/*     */   {
/* 245 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitExplain(@NotNull SqlBaseParser.ExplainContext ctx)
/*     */   {
/* 253 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSampledRelation(@NotNull SqlBaseParser.SampledRelationContext ctx)
/*     */   {
/* 261 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSubqueryRelation(@NotNull SqlBaseParser.SubqueryRelationContext ctx)
/*     */   {
/* 269 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitJoinRelation(@NotNull SqlBaseParser.JoinRelationContext ctx)
/*     */   {
/* 277 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitInterval(@NotNull SqlBaseParser.IntervalContext ctx)
/*     */   {
/* 285 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitShowSession(@NotNull SqlBaseParser.ShowSessionContext ctx)
/*     */   {
/* 293 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQuery(@NotNull SqlBaseParser.QueryContext ctx)
/*     */   {
/* 301 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitUse(@NotNull SqlBaseParser.UseContext ctx)
/*     */   {
/* 309 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitBackQuotedIdentifier(@NotNull SqlBaseParser.BackQuotedIdentifierContext ctx)
/*     */   {
/* 317 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitShowTables(@NotNull SqlBaseParser.ShowTablesContext ctx)
/*     */   {
/* 325 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSetQuantifier(@NotNull SqlBaseParser.SetQuantifierContext ctx)
/*     */   {
/* 333 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitTableName(@NotNull SqlBaseParser.TableNameContext ctx)
/*     */   {
/* 341 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitDigitIdentifier(@NotNull SqlBaseParser.DigitIdentifierContext ctx)
/*     */   {
/* 349 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitComparisonOperator(@NotNull SqlBaseParser.ComparisonOperatorContext ctx)
/*     */   {
/* 357 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitNumericLiteral(@NotNull SqlBaseParser.NumericLiteralContext ctx)
/*     */   {
/* 365 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQueryPrimaryDefault(@NotNull SqlBaseParser.QueryPrimaryDefaultContext ctx)
/*     */   {
/* 373 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitAtTimeZone(@NotNull SqlBaseParser.AtTimeZoneContext ctx)
/*     */   {
/* 381 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitNamedQuery(@NotNull SqlBaseParser.NamedQueryContext ctx)
/*     */   {
/* 389 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitShowCatalogs(@NotNull SqlBaseParser.ShowCatalogsContext ctx)
/*     */   {
/* 397 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQualifiedName(@NotNull SqlBaseParser.QualifiedNameContext ctx)
/*     */   {
/* 405 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitDistinctFrom(@NotNull SqlBaseParser.DistinctFromContext ctx)
/*     */   {
/* 413 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitBooleanLiteral(@NotNull SqlBaseParser.BooleanLiteralContext ctx)
/*     */   {
/* 421 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitParenthesizedExpression(@NotNull SqlBaseParser.ParenthesizedExpressionContext ctx)
/*     */   {
/* 429 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitStatementDefault(@NotNull SqlBaseParser.StatementDefaultContext ctx)
/*     */   {
/* 437 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitNullLiteral(@NotNull SqlBaseParser.NullLiteralContext ctx)
/*     */   {
/* 445 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitRowConstructor(@NotNull SqlBaseParser.RowConstructorContext ctx)
/*     */   {
/* 453 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitExists(@NotNull SqlBaseParser.ExistsContext ctx)
/*     */   {
/* 461 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitRelationDefault(@NotNull SqlBaseParser.RelationDefaultContext ctx)
/*     */   {
/* 469 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitBoundedFrame(@NotNull SqlBaseParser.BoundedFrameContext ctx)
/*     */   {
/* 477 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitLogicalNot(@NotNull SqlBaseParser.LogicalNotContext ctx)
/*     */   {
/* 485 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitExpression(@NotNull SqlBaseParser.ExpressionContext ctx)
/*     */   {
/* 493 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitTimeZoneInterval(@NotNull SqlBaseParser.TimeZoneIntervalContext ctx)
/*     */   {
/* 501 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitInList(@NotNull SqlBaseParser.InListContext ctx)
/*     */   {
/* 509 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitShowPartitions(@NotNull SqlBaseParser.ShowPartitionsContext ctx)
/*     */   {
/* 517 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitType(@NotNull SqlBaseParser.TypeContext ctx)
/*     */   {
/* 525 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitBooleanValue(@NotNull SqlBaseParser.BooleanValueContext ctx)
/*     */   {
/* 533 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQuotedIdentifier(@NotNull SqlBaseParser.QuotedIdentifierContext ctx)
/*     */   {
/* 541 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitInlineTable(@NotNull SqlBaseParser.InlineTableContext ctx)
/*     */   {
/* 549 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitFunctionCall(@NotNull SqlBaseParser.FunctionCallContext ctx)
/*     */   {
/* 557 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitNullPredicate(@NotNull SqlBaseParser.NullPredicateContext ctx)
/*     */   {
/* 565 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitUnboundedFrame(@NotNull SqlBaseParser.UnboundedFrameContext ctx)
/*     */   {
/* 573 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitConcatenation(@NotNull SqlBaseParser.ConcatenationContext ctx)
/*     */   {
/* 581 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitDropView(@NotNull SqlBaseParser.DropViewContext ctx)
/*     */   {
/* 589 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitColumnAliases(@NotNull SqlBaseParser.ColumnAliasesContext ctx)
/*     */   {
/* 597 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitCurrentRowBound(@NotNull SqlBaseParser.CurrentRowBoundContext ctx)
/*     */   {
/* 605 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitUnnest(@NotNull SqlBaseParser.UnnestContext ctx)
/*     */   {
/* 613 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSetSession(@NotNull SqlBaseParser.SetSessionContext ctx)
/*     */   {
/* 621 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSelectSingle(@NotNull SqlBaseParser.SelectSingleContext ctx)
/*     */   {
/* 629 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitComparison(@NotNull SqlBaseParser.ComparisonContext ctx)
/*     */   {
/* 637 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSimpleCase(@NotNull SqlBaseParser.SimpleCaseContext ctx)
/*     */   {
/* 645 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitDropTable(@NotNull SqlBaseParser.DropTableContext ctx)
/*     */   {
/* 653 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitAliasedRelation(@NotNull SqlBaseParser.AliasedRelationContext ctx)
/*     */   {
/* 661 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitArithmeticBinary(@NotNull SqlBaseParser.ArithmeticBinaryContext ctx)
/*     */   {
/* 669 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitCast(@NotNull SqlBaseParser.CastContext ctx)
/*     */   {
/* 677 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitIntervalField(@NotNull SqlBaseParser.IntervalFieldContext ctx)
/*     */   {
/* 685 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSpecialDateTimeFunction(@NotNull SqlBaseParser.SpecialDateTimeFunctionContext ctx)
/*     */   {
/* 693 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQueryNoWith(@NotNull SqlBaseParser.QueryNoWithContext ctx)
/*     */   {
/* 701 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSubqueryExpression(@NotNull SqlBaseParser.SubqueryExpressionContext ctx)
/*     */   {
/* 709 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitIntegerLiteral(@NotNull SqlBaseParser.IntegerLiteralContext ctx)
/*     */   {
/* 717 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitTimeZoneString(@NotNull SqlBaseParser.TimeZoneStringContext ctx)
/*     */   {
/* 725 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitResetSession(@NotNull SqlBaseParser.ResetSessionContext ctx)
/*     */   {
/* 733 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitIntervalLiteral(@NotNull SqlBaseParser.IntervalLiteralContext ctx)
/*     */   {
/* 741 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitArithmeticUnary(@NotNull SqlBaseParser.ArithmeticUnaryContext ctx)
/*     */   {
/* 749 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSetOperation(@NotNull SqlBaseParser.SetOperationContext ctx)
/*     */   {
/* 757 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQuotedIdentifierAlternative(@NotNull SqlBaseParser.QuotedIdentifierAlternativeContext ctx)
/*     */   {
/* 765 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSingleStatement(@NotNull SqlBaseParser.SingleStatementContext ctx)
/*     */   {
/* 773 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitBetween(@NotNull SqlBaseParser.BetweenContext ctx)
/*     */   {
/* 781 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitValueExpressionDefault(@NotNull SqlBaseParser.ValueExpressionDefaultContext ctx)
/*     */   {
/* 789 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitWith(@NotNull SqlBaseParser.WithContext ctx)
/*     */   {
/* 797 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitCreateTableAsSelect(@NotNull SqlBaseParser.CreateTableAsSelectContext ctx)
/*     */   {
/* 805 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSelectAll(@NotNull SqlBaseParser.SelectAllContext ctx)
/*     */   {
/* 813 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitWindowFrame(@NotNull SqlBaseParser.WindowFrameContext ctx)
/*     */   {
/* 821 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSubquery(@NotNull SqlBaseParser.SubqueryContext ctx)
/*     */   {
/* 829 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitNonReserved(@NotNull SqlBaseParser.NonReservedContext ctx)
/*     */   {
/* 837 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSimpleType(@NotNull SqlBaseParser.SimpleTypeContext ctx)
/*     */   {
/* 845 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitInSubquery(@NotNull SqlBaseParser.InSubqueryContext ctx)
/*     */   {
/* 853 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitJoinType(@NotNull SqlBaseParser.JoinTypeContext ctx)
/*     */   {
/* 861 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitShowFunctions(@NotNull SqlBaseParser.ShowFunctionsContext ctx)
/*     */   {
/* 869 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSampleType(@NotNull SqlBaseParser.SampleTypeContext ctx)
/*     */   {
/* 877 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSortItem(@NotNull SqlBaseParser.SortItemContext ctx)
/*     */   {
/* 885 */     return (T)visitChildren(ctx);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/generated/SqlBaseBaseVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */