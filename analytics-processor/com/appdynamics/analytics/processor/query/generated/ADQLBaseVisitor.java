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
/*     */ public class ADQLBaseVisitor<T>
/*     */   extends AbstractParseTreeVisitor<T>
/*     */   implements ADQLVisitor<T>
/*     */ {
/*     */   public T visitExpression(@NotNull ADQLParser.ExpressionContext ctx)
/*     */   {
/*  21 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQueryNoWith(@NotNull ADQLParser.QueryNoWithContext ctx)
/*     */   {
/*  29 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitColumnPredicated(@NotNull ADQLParser.ColumnPredicatedContext ctx)
/*     */   {
/*  37 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitUnquotedIdentifier(@NotNull ADQLParser.UnquotedIdentifierContext ctx)
/*     */   {
/*  45 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQuery(@NotNull ADQLParser.QueryContext ctx)
/*     */   {
/*  53 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitStringLiteral(@NotNull ADQLParser.StringLiteralContext ctx)
/*     */   {
/*  61 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQuerySpecification(@NotNull ADQLParser.QuerySpecificationContext ctx)
/*     */   {
/*  69 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitBackQuotedIdentifier(@NotNull ADQLParser.BackQuotedIdentifierContext ctx)
/*     */   {
/*  77 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitIntegerLiteral(@NotNull ADQLParser.IntegerLiteralContext ctx)
/*     */   {
/*  85 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitBooleanValue(@NotNull ADQLParser.BooleanValueContext ctx)
/*     */   {
/*  93 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQuotedIdentifier(@NotNull ADQLParser.QuotedIdentifierContext ctx)
/*     */   {
/* 101 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitFunctionCall(@NotNull ADQLParser.FunctionCallContext ctx)
/*     */   {
/* 109 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQueryTermDefault(@NotNull ADQLParser.QueryTermDefaultContext ctx)
/*     */   {
/* 117 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitTableName(@NotNull ADQLParser.TableNameContext ctx)
/*     */   {
/* 125 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitDigitIdentifier(@NotNull ADQLParser.DigitIdentifierContext ctx)
/*     */   {
/* 133 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQuotedIdentifierAlternative(@NotNull ADQLParser.QuotedIdentifierAlternativeContext ctx)
/*     */   {
/* 141 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitComparisonOperator(@NotNull ADQLParser.ComparisonOperatorContext ctx)
/*     */   {
/* 149 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitNumericLiteral(@NotNull ADQLParser.NumericLiteralContext ctx)
/*     */   {
/* 157 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSingleStatement(@NotNull ADQLParser.SingleStatementContext ctx)
/*     */   {
/* 165 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitBetween(@NotNull ADQLParser.BetweenContext ctx)
/*     */   {
/* 173 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQueryPrimaryDefault(@NotNull ADQLParser.QueryPrimaryDefaultContext ctx)
/*     */   {
/* 181 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitValueExpressionDefault(@NotNull ADQLParser.ValueExpressionDefaultContext ctx)
/*     */   {
/* 189 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitMatchStringReference(@NotNull ADQLParser.MatchStringReferenceContext ctx)
/*     */   {
/* 197 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitColumnReference(@NotNull ADQLParser.ColumnReferenceContext ctx)
/*     */   {
/* 205 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitQualifiedName(@NotNull ADQLParser.QualifiedNameContext ctx)
/*     */   {
/* 213 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitBooleanDefault(@NotNull ADQLParser.BooleanDefaultContext ctx)
/*     */   {
/* 221 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitBooleanLiteral(@NotNull ADQLParser.BooleanLiteralContext ctx)
/*     */   {
/* 229 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSelectSingle(@NotNull ADQLParser.SelectSingleContext ctx)
/*     */   {
/* 237 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitDecimalLiteral(@NotNull ADQLParser.DecimalLiteralContext ctx)
/*     */   {
/* 245 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSelectAll(@NotNull ADQLParser.SelectAllContext ctx)
/*     */   {
/* 253 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitComparison(@NotNull ADQLParser.ComparisonContext ctx)
/*     */   {
/* 261 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitParenthesizedExpression(@NotNull ADQLParser.ParenthesizedExpressionContext ctx)
/*     */   {
/* 269 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitStatementDefault(@NotNull ADQLParser.StatementDefaultContext ctx)
/*     */   {
/* 277 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitNullLiteral(@NotNull ADQLParser.NullLiteralContext ctx)
/*     */   {
/* 285 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitPrimaryMatchString(@NotNull ADQLParser.PrimaryMatchStringContext ctx)
/*     */   {
/* 293 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitNonReserved(@NotNull ADQLParser.NonReservedContext ctx)
/*     */   {
/* 301 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitLogicalBinary(@NotNull ADQLParser.LogicalBinaryContext ctx)
/*     */   {
/* 309 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitMatch(@NotNull ADQLParser.MatchContext ctx)
/*     */   {
/* 317 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitRelationDefault(@NotNull ADQLParser.RelationDefaultContext ctx)
/*     */   {
/* 325 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitLogicalNot(@NotNull ADQLParser.LogicalNotContext ctx)
/*     */   {
/* 333 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSingleBooleanLiteral(@NotNull ADQLParser.SingleBooleanLiteralContext ctx)
/*     */   {
/* 341 */     return (T)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public T visitSortItem(@NotNull ADQLParser.SortItemContext ctx)
/*     */   {
/* 349 */     return (T)visitChildren(ctx);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/generated/ADQLBaseVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */