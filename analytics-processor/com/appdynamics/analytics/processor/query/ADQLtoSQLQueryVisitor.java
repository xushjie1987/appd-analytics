/*     */ package com.appdynamics.analytics.processor.query;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.BetweenContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.ComparisonContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.ComparisonOperatorContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.DecimalLiteralContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.FunctionCallContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.IntegerLiteralContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.LogicalBinaryContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.MatchStringReferenceContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.QuerySpecificationContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.StringLiteralContext;
/*     */ import org.antlr.v4.runtime.misc.NotNull;
/*     */ import org.antlr.v4.runtime.tree.TerminalNode;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ 
/*     */ public class ADQLtoSQLQueryVisitor extends com.appdynamics.analytics.processor.query.generated.ADQLBaseVisitor<String>
/*     */ {
/*  19 */   private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ADQLtoSQLQueryVisitor.class);
/*     */   
/*     */ 
/*     */ 
/*     */   private ADQLParsingHelper parsingHelper;
/*     */   
/*     */ 
/*     */ 
/*     */   private StringBuilder stringBuilder;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ADQLtoSQLQueryVisitor(com.google.common.base.Function<String, com.appdynamics.analytics.processor.event.query.EventSchema> getEventSchemaFunction)
/*     */   {
/*  34 */     this.parsingHelper = new ADQLParsingHelper(getEventSchemaFunction);
/*  35 */     this.stringBuilder = new StringBuilder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitDecimalLiteral(@NotNull ADQLParser.DecimalLiteralContext ctx)
/*     */   {
/*  45 */     String doubleLiteral = ctx.DECIMAL_VALUE().getText();
/*  46 */     return Double.toString(this.parsingHelper.parseDoubleLiteral(doubleLiteral));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitQuotedIdentifierAlternative(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.QuotedIdentifierAlternativeContext ctx)
/*     */   {
/*  58 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitDigitIdentifier(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.DigitIdentifierContext ctx)
/*     */   {
/*  69 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitQuotedIdentifier(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.QuotedIdentifierContext ctx)
/*     */   {
/*  79 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitTableName(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.TableNameContext ctx)
/*     */   {
/*  89 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitValueExpressionDefault(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.ValueExpressionDefaultContext ctx)
/*     */   {
/*  99 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitBackQuotedIdentifier(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.BackQuotedIdentifierContext ctx)
/*     */   {
/* 109 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitStatementDefault(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.StatementDefaultContext ctx)
/*     */   {
/* 119 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitBooleanDefault(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.BooleanDefaultContext ctx)
/*     */   {
/* 129 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitSortItem(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.SortItemContext ctx)
/*     */   {
/* 139 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitNumericLiteral(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.NumericLiteralContext ctx)
/*     */   {
/* 149 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitBooleanLiteral(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.BooleanLiteralContext ctx)
/*     */   {
/* 159 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitBetween(@NotNull ADQLParser.BetweenContext ctx)
/*     */   {
/* 169 */     String identifier = ctx.value.getText();
/*     */     
/* 171 */     String lower = (String)visit(ctx.lower);
/* 172 */     String upper = (String)visit(ctx.upper);
/*     */     
/* 174 */     this.parsingHelper.parseDoubleLiteral(lower);
/* 175 */     this.parsingHelper.parseDoubleLiteral(upper);
/*     */     
/* 177 */     this.parsingHelper.validateEventFieldTypes(identifier, ADQLParsingHelper.NUMERIC_FIELD_TYPES);
/*     */     
/* 179 */     this.stringBuilder.setLength(0);
/* 180 */     this.stringBuilder.append(identifier);
/*     */     
/* 182 */     if (ctx.NOT() != null) {
/* 183 */       this.stringBuilder.append(" NOT");
/*     */     }
/*     */     
/* 186 */     this.stringBuilder.append(String.format(" BETWEEN %s AND %s", new Object[] { lower, upper }));
/*     */     
/* 188 */     return this.stringBuilder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitLogicalBinary(@NotNull ADQLParser.LogicalBinaryContext ctx)
/*     */   {
/* 199 */     String left = (String)visit(ctx.left);
/* 200 */     String right = (String)visit(ctx.right);
/*     */     
/* 202 */     this.stringBuilder.setLength(0);
/*     */     
/* 204 */     if (ctx.operator.getType() == 22) {
/* 205 */       this.stringBuilder.append(String.format("(%s AND %s)", new Object[] { left, right }));
/* 206 */     } else if (ctx.operator.getType() == 21) {
/* 207 */       this.stringBuilder.append(String.format("(%s OR %s)", new Object[] { left, right }));
/*     */     } else {
/* 209 */       throw new ParsingException(String.format("Unknown binary operator: %s", new Object[] { ctx.getText() }));
/*     */     }
/*     */     
/* 212 */     return this.stringBuilder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitLogicalNot(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.LogicalNotContext ctx)
/*     */   {
/* 222 */     String inner = (String)visitChildren(ctx);
/* 223 */     return String.format("NOT %s", new Object[] { inner });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitColumnReference(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.ColumnReferenceContext ctx)
/*     */   {
/* 233 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitSelectSingle(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.SelectSingleContext ctx)
/*     */   {
/* 243 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitRelationDefault(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.RelationDefaultContext ctx)
/*     */   {
/* 253 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitExpression(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.ExpressionContext ctx)
/*     */   {
/* 263 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitComparison(@NotNull ADQLParser.ComparisonContext ctx)
/*     */   {
/* 273 */     String identifier = ctx.value.getText();
/* 274 */     String right = (String)visit(ctx.right);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 279 */     this.parsingHelper.validateIdentifierTypeMatchesValueAsString(identifier, right, true);
/*     */     
/* 281 */     this.stringBuilder.setLength(0);
/* 282 */     if (ctx.comparisonOperator().GT() != null) {
/* 283 */       this.stringBuilder.append(String.format("%s > %s", new Object[] { identifier, right }));
/* 284 */     } else if (ctx.comparisonOperator().GTE() != null) {
/* 285 */       this.stringBuilder.append(String.format("%s >= %s", new Object[] { identifier, right }));
/* 286 */     } else if (ctx.comparisonOperator().LT() != null) {
/* 287 */       this.stringBuilder.append(String.format("%s < %s", new Object[] { identifier, right }));
/* 288 */     } else if (ctx.comparisonOperator().LTE() != null) {
/* 289 */       this.stringBuilder.append(String.format("%s <= %s", new Object[] { identifier, right }));
/* 290 */     } else if (ctx.comparisonOperator().EQ() != null) {
/* 291 */       if ((right.startsWith("'")) && (StringUtils.contains(right, '*'))) {
/* 292 */         String remapWildcard = remapADQLWildcardCharactersToSQL(right);
/* 293 */         this.stringBuilder.append(String.format("%s LIKE %s", new Object[] { identifier, remapWildcard }));
/*     */       } else {
/* 295 */         this.stringBuilder.append(String.format("%s = %s", new Object[] { identifier, right }));
/*     */       }
/* 297 */     } else if (ctx.comparisonOperator().NEQ() != null) {
/* 298 */       this.stringBuilder.append(String.format("%s != %s", new Object[] { identifier, right }));
/*     */     } else {
/* 300 */       throw new ParsingException(String.format("Unknown comparison operation: %s", new Object[] { ctx.getText() }));
/*     */     }
/*     */     
/* 303 */     return this.stringBuilder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitNullLiteral(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.NullLiteralContext ctx)
/*     */   {
/* 313 */     return "null";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitMatchStringReference(@NotNull ADQLParser.MatchStringReferenceContext ctx)
/*     */   {
/* 323 */     String matchString = ctx.primaryMatchString().getText();
/* 324 */     matchString = StringUtils.stripStart(matchString, "'");
/* 325 */     matchString = StringUtils.stripEnd(matchString, "'");
/*     */     
/* 327 */     return matchString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitMatch(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.MatchContext ctx)
/*     */   {
/* 338 */     String matchString = remapADQLWildcardCharactersToSQL((String)visitChildren(ctx));
/*     */     
/* 340 */     this.stringBuilder.setLength(0);
/* 341 */     if (StringUtils.contains(matchString, '*'))
/*     */     {
/* 343 */       this.stringBuilder.append(String.format("%s LIKE '%s'", new Object[] { this.parsingHelper.getAllField(), matchString }));
/* 344 */     } else if (StringUtils.contains(matchString, ' '))
/*     */     {
/*     */ 
/* 347 */       this.stringBuilder.append(String.format("MULTIMATCH(%s, '%s')", new Object[] { this.parsingHelper.getAllField(), matchString }));
/*     */     }
/*     */     else
/*     */     {
/* 351 */       this.stringBuilder.append(String.format("MATCH(%s, '%s')", new Object[] { this.parsingHelper.getAllField(), matchString }));
/*     */     }
/*     */     
/* 354 */     return this.stringBuilder.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitComparisonOperator(@NotNull ADQLParser.ComparisonOperatorContext ctx)
/*     */   {
/* 364 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitQualifiedName(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.QualifiedNameContext ctx)
/*     */   {
/* 374 */     return ctx.getText();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitQuery(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.QueryContext ctx)
/*     */   {
/* 384 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitQueryTermDefault(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.QueryTermDefaultContext ctx)
/*     */   {
/* 394 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitNonReserved(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.NonReservedContext ctx)
/*     */   {
/* 404 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitSingleStatement(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.SingleStatementContext ctx)
/*     */   {
/* 414 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitUnquotedIdentifier(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.UnquotedIdentifierContext ctx)
/*     */   {
/* 424 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitSelectAll(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.SelectAllContext ctx)
/*     */   {
/* 434 */     return "*";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitQueryNoWith(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.QueryNoWithContext ctx)
/*     */   {
/* 444 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitStringLiteral(@NotNull ADQLParser.StringLiteralContext ctx)
/*     */   {
/* 455 */     return ctx.STRING().getText();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitIntegerLiteral(@NotNull ADQLParser.IntegerLiteralContext ctx)
/*     */   {
/* 465 */     String value = ctx.INTEGER_VALUE().getText();
/* 466 */     return Long.toString(this.parsingHelper.parseLongLiteral(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitBooleanValue(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.BooleanValueContext ctx)
/*     */   {
/* 476 */     String value = ctx.getText();
/* 477 */     return Boolean.toString(this.parsingHelper.parseBooleanLiteral(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitQueryPrimaryDefault(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.QueryPrimaryDefaultContext ctx)
/*     */   {
/* 487 */     return (String)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitQuerySpecification(@NotNull ADQLParser.QuerySpecificationContext ctx)
/*     */   {
/* 498 */     String relation = (String)visit(ctx.relation());
/*     */     
/* 500 */     relation = ADQLParsingHelper.normalizeEventType(relation);
/*     */     
/* 502 */     this.parsingHelper.addEventSchema(relation);
/*     */     
/* 504 */     String where = "";
/* 505 */     if (ctx.booleanExpression() != null) {
/* 506 */       String whereFilter = (String)visit(ctx.booleanExpression());
/* 507 */       if (StringUtils.isNotBlank(whereFilter)) {
/* 508 */         where = String.format("WHERE %s", new Object[] { whereFilter });
/*     */       }
/*     */     }
/*     */     
/* 512 */     StringBuilder selectItemsBuilder = new StringBuilder();
/* 513 */     boolean firstItem = true;
/*     */     
/* 515 */     for (com.appdynamics.analytics.processor.query.generated.ADQLParser.SelectItemContext selectItemContext : ctx.selectItem()) {
/* 516 */       String item = (String)visit(selectItemContext);
/* 517 */       if (!firstItem) {
/* 518 */         selectItemsBuilder.append(", ");
/*     */       }
/* 520 */       firstItem = false;
/* 521 */       selectItemsBuilder.append(item);
/*     */     }
/* 523 */     String items = selectItemsBuilder.toString();
/*     */     
/* 525 */     String result = String.format("SELECT %s FROM %s %s", new Object[] { items, relation, where });
/* 526 */     return StringUtils.stripEnd(result, "; ");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitParenthesizedExpression(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.ParenthesizedExpressionContext ctx)
/*     */   {
/* 538 */     return (String)visit(ctx.getChild(1));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String visitFunctionCall(@NotNull ADQLParser.FunctionCallContext ctx)
/*     */   {
/* 548 */     String function = ((String)visit(ctx.qualifiedName())).toLowerCase();
/*     */     
/* 550 */     String field = "*";
/* 551 */     if (ctx.ASTERISK() == null) {
/* 552 */       field = (String)visit(ctx.columnName());
/*     */     }
/*     */     
/* 555 */     this.parsingHelper.validateAggegrateFunctionUsage(function, field);
/*     */     
/* 557 */     return String.format("%s(%s)", new Object[] { function, field });
/*     */   }
/*     */   
/*     */   private String remapADQLWildcardCharactersToSQL(String right) {
/* 561 */     return right.replace('*', '%').replace('?', '_');
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/ADQLtoSQLQueryVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */