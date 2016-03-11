/*     */ package com.appdynamics.analytics.processor.query;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.BetweenContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.BooleanValueContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.ColumnReferenceContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.ComparisonContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.ComparisonOperatorContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.DecimalLiteralContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.FunctionCallContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.IntegerLiteralContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.LogicalBinaryContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.MatchStringReferenceContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.ParenthesizedExpressionContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.QualifiedNameContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.QuerySpecificationContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.SelectSingleContext;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser.StringLiteralContext;
/*     */ import com.appdynamics.analytics.processor.query.node.EsAggs;
/*     */ import com.appdynamics.analytics.processor.query.node.EsFilter;
/*     */ import com.appdynamics.analytics.processor.query.node.EsIdentifier;
/*     */ import com.appdynamics.analytics.processor.query.node.EsItem;
/*     */ import com.appdynamics.analytics.processor.query.node.EsNode;
/*     */ import com.appdynamics.analytics.processor.query.node.EsValue;
/*     */ import com.appdynamics.common.util.misc.Pair;
/*     */ import java.util.ArrayList;
/*     */ import org.antlr.v4.runtime.misc.NotNull;
/*     */ import org.antlr.v4.runtime.tree.TerminalNode;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.elasticsearch.index.query.BoolFilterBuilder;
/*     */ import org.elasticsearch.index.query.FilterBuilder;
/*     */ import org.elasticsearch.index.query.FilterBuilders;
/*     */ import org.elasticsearch.index.query.QueryBuilder;
/*     */ import org.elasticsearch.index.query.QueryBuilders;
/*     */ import org.elasticsearch.index.query.RangeFilterBuilder;
/*     */ import org.elasticsearch.search.aggregations.AggregationBuilders;
/*     */ 
/*     */ public class ADQLtoESQueryVisitor extends com.appdynamics.analytics.processor.query.generated.ADQLBaseVisitor<EsNode>
/*     */ {
/*  39 */   private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ADQLtoESQueryVisitor.class);
/*     */   
/*     */ 
/*  42 */   private static final EsValue TRUE_ES_VALUE = new EsValue(Boolean.valueOf(true));
/*  43 */   private static final EsValue FALSE_ES_VALUE = new EsValue(Boolean.valueOf(false));
/*     */   
/*     */   private ADQLParsingHelper parsingHelper;
/*     */   
/*  47 */   private static java.util.HashMap<java.lang.reflect.Type, com.appdynamics.analytics.processor.event.query.EventFieldType> mapObjectTypeToFieldType = null;
/*     */   
/*     */   public ADQLtoESQueryVisitor(com.google.common.base.Function<String, com.appdynamics.analytics.processor.event.query.EventSchema> getEventSchemaFunction) {
/*  50 */     this.parsingHelper = new ADQLParsingHelper(getEventSchemaFunction);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitDecimalLiteral(@NotNull ADQLParser.DecimalLiteralContext ctx)
/*     */   {
/*  61 */     String doubleLiteral = ctx.DECIMAL_VALUE().getText();
/*  62 */     return new EsValue(Double.valueOf(this.parsingHelper.parseDoubleLiteral(doubleLiteral)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitQuotedIdentifierAlternative(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.QuotedIdentifierAlternativeContext ctx)
/*     */   {
/*  73 */     return (EsNode)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitDigitIdentifier(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.DigitIdentifierContext ctx)
/*     */   {
/*  84 */     return (EsNode)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitQuotedIdentifier(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.QuotedIdentifierContext ctx)
/*     */   {
/*  95 */     return (EsNode)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitTableName(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.TableNameContext ctx)
/*     */   {
/* 106 */     return (EsNode)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitValueExpressionDefault(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.ValueExpressionDefaultContext ctx)
/*     */   {
/* 117 */     return (EsNode)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitBackQuotedIdentifier(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.BackQuotedIdentifierContext ctx)
/*     */   {
/* 128 */     return (EsNode)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitStatementDefault(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.StatementDefaultContext ctx)
/*     */   {
/* 139 */     return (EsNode)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitBooleanDefault(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.BooleanDefaultContext ctx)
/*     */   {
/* 150 */     return (EsNode)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitSortItem(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.SortItemContext ctx)
/*     */   {
/* 161 */     return (EsNode)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitBetween(@NotNull ADQLParser.BetweenContext ctx)
/*     */   {
/* 172 */     String identifier = ctx.value.getText();
/* 173 */     EsValue lower = (EsValue)visit(ctx.lower);
/* 174 */     EsValue upper = (EsValue)visit(ctx.upper);
/*     */     
/* 176 */     this.parsingHelper.validateEventFieldTypes(identifier, ADQLParsingHelper.NUMERIC_FIELD_TYPES);
/*     */     
/* 178 */     FilterBuilder fb = FilterBuilders.rangeFilter(identifier).from(lower.value).to(upper.value);
/*     */     
/* 180 */     if (ctx.NOT() != null) {
/* 181 */       fb = FilterBuilders.notFilter(fb);
/*     */     }
/*     */     
/* 184 */     return new EsFilter(fb);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitLogicalBinary(@NotNull ADQLParser.LogicalBinaryContext ctx)
/*     */   {
/* 196 */     EsFilter left = (EsFilter)visit(ctx.left);
/* 197 */     EsFilter right = (EsFilter)visit(ctx.right);
/*     */     
/* 199 */     FilterBuilder fb = null;
/*     */     
/* 201 */     if (ctx.operator.getType() == 22) {
/* 202 */       fb = FilterBuilders.boolFilter().must(left.getFilterBuilder()).must(right.getFilterBuilder());
/*     */ 
/*     */ 
/*     */     }
/* 206 */     else if (ctx.operator.getType() == 21) {
/* 207 */       fb = FilterBuilders.boolFilter().should(left.getFilterBuilder()).should(right.getFilterBuilder());
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 212 */       throw new ParsingException(String.format("Unknown binary operator: %s", new Object[] { ctx.getText() }));
/*     */     }
/*     */     
/* 215 */     return new EsFilter(fb);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitLogicalNot(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.LogicalNotContext ctx)
/*     */   {
/* 226 */     EsFilter inner = (EsFilter)visitChildren(ctx);
/* 227 */     FilterBuilder fb = FilterBuilders.notFilter(inner.getFilterBuilder());
/* 228 */     return new EsFilter(fb);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitColumnReference(@NotNull ADQLParser.ColumnReferenceContext ctx)
/*     */   {
/* 239 */     return (EsNode)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitSelectSingle(@NotNull ADQLParser.SelectSingleContext ctx)
/*     */   {
/* 250 */     String label = null;
/* 251 */     EsNode value = (EsNode)visit(ctx.getChild(0));
/*     */     
/* 253 */     if (ctx.identifier() != null) {
/* 254 */       label = ctx.identifier().getText();
/*     */     }
/*     */     
/* 257 */     return new EsItem(label, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitRelationDefault(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.RelationDefaultContext ctx)
/*     */   {
/* 268 */     return (EsNode)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitExpression(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.ExpressionContext ctx)
/*     */   {
/* 279 */     return (EsNode)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitComparison(@NotNull ADQLParser.ComparisonContext ctx)
/*     */   {
/* 290 */     String identifier = ctx.value.getText();
/* 291 */     identifier = this.parsingHelper.normalizeIdentifierForEventType(identifier);
/* 292 */     EsValue rightValue = (EsValue)visit(ctx.right);
/*     */     
/* 294 */     this.parsingHelper.validateIdentifierTypeMatchesValueTypeAsObject(identifier, rightValue.value);
/*     */     
/* 296 */     FilterBuilder fb = null;
/* 297 */     if (ctx.comparisonOperator().GT() != null) {
/* 298 */       fb = FilterBuilders.rangeFilter(identifier).gt(rightValue.value);
/* 299 */     } else if (ctx.comparisonOperator().GTE() != null) {
/* 300 */       fb = FilterBuilders.rangeFilter(identifier).gte(rightValue.value);
/* 301 */     } else if (ctx.comparisonOperator().LT() != null) {
/* 302 */       fb = FilterBuilders.rangeFilter(identifier).lt(rightValue.value);
/* 303 */     } else if (ctx.comparisonOperator().LTE() != null) {
/* 304 */       fb = FilterBuilders.rangeFilter(identifier).lte(rightValue.value);
/* 305 */     } else if (ctx.comparisonOperator().EQ() != null) {
/* 306 */       if (this.parsingHelper.getEventFieldType(identifier) == com.appdynamics.analytics.processor.event.query.EventFieldType.STRING) {
/* 307 */         String value = (String)rightValue.value;
/* 308 */         if (StringUtils.contains(value, '*')) {
/* 309 */           QueryBuilder qb = QueryBuilders.wildcardQuery(identifier, value);
/* 310 */           fb = FilterBuilders.queryFilter(qb);
/*     */         }
/*     */       }
/* 313 */       if (fb == null) {
/* 314 */         fb = FilterBuilders.termFilter(identifier, rightValue.value);
/*     */       }
/* 316 */     } else if (ctx.comparisonOperator().NEQ() != null) {
/* 317 */       fb = FilterBuilders.notFilter(FilterBuilders.termFilter(identifier, rightValue.value));
/*     */     }
/*     */     
/* 320 */     if (fb == null) {
/* 321 */       throw new ParsingException(String.format("Unknown comparison operation [%s]", new Object[] { ctx.getText() }));
/*     */     }
/*     */     
/* 324 */     return new EsFilter(fb);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitNullLiteral(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.NullLiteralContext ctx)
/*     */   {
/* 335 */     return new EsValue(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitMatchStringReference(@NotNull ADQLParser.MatchStringReferenceContext ctx)
/*     */   {
/* 345 */     String matchString = ctx.primaryMatchString().getText();
/* 346 */     matchString = StringUtils.stripStart(matchString, "'");
/* 347 */     matchString = StringUtils.stripEnd(matchString, "'");
/*     */     
/* 349 */     return new EsValue(matchString);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitMatch(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.MatchContext ctx)
/*     */   {
/* 360 */     String matchString = (String)((EsValue)visitChildren(ctx)).value;
/*     */     
/*     */ 
/*     */     QueryBuilder qb;
/*     */     
/*     */     QueryBuilder qb;
/*     */     
/* 367 */     if (StringUtils.contains(matchString, '*'))
/*     */     {
/* 369 */       qb = QueryBuilders.wildcardQuery(this.parsingHelper.getAllField(), matchString.toLowerCase()); } else { QueryBuilder qb;
/* 370 */       if (StringUtils.contains(matchString, ' '))
/*     */       {
/* 372 */         qb = QueryBuilders.matchPhraseQuery(this.parsingHelper.getAllField(), matchString);
/*     */       }
/*     */       else {
/* 375 */         qb = QueryBuilders.matchQuery(this.parsingHelper.getAllField(), matchString);
/*     */       }
/*     */     }
/* 378 */     FilterBuilder fb = FilterBuilders.queryFilter(qb);
/*     */     
/* 380 */     return new EsFilter(fb);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitQualifiedName(@NotNull ADQLParser.QualifiedNameContext ctx)
/*     */   {
/* 391 */     return new EsIdentifier(ctx.getText());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitQuery(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.QueryContext ctx)
/*     */   {
/* 402 */     return (EsNode)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitSingleStatement(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.SingleStatementContext ctx)
/*     */   {
/* 413 */     return (EsNode)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitUnquotedIdentifier(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.UnquotedIdentifierContext ctx)
/*     */   {
/* 424 */     return (EsNode)visitChildren(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitSelectAll(@NotNull com.appdynamics.analytics.processor.query.generated.ADQLParser.SelectAllContext ctx)
/*     */   {
/* 435 */     return new EsIdentifier("*");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitStringLiteral(@NotNull ADQLParser.StringLiteralContext ctx)
/*     */   {
/* 446 */     String value = ctx.STRING().getText();
/* 447 */     return new EsValue(value.substring(1, value.length() - 1));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitIntegerLiteral(@NotNull ADQLParser.IntegerLiteralContext ctx)
/*     */   {
/* 458 */     String value = ctx.INTEGER_VALUE().getText();
/* 459 */     return new EsValue(Long.valueOf(this.parsingHelper.parseLongLiteral(value)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitBooleanValue(@NotNull ADQLParser.BooleanValueContext ctx)
/*     */   {
/* 470 */     if (ctx.TRUE() != null)
/* 471 */       return TRUE_ES_VALUE;
/* 472 */     if (ctx.FALSE() != null) {
/* 473 */       return FALSE_ES_VALUE;
/*     */     }
/* 475 */     throw new ParsingException(String.format("Unknown boolean value [%s].", new Object[] { ctx.getText() }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitQuerySpecification(@NotNull ADQLParser.QuerySpecificationContext ctx)
/*     */   {
/* 488 */     EsIdentifier relationIdentifier = (EsIdentifier)visit(ctx.relation());
/* 489 */     String relation = relationIdentifier.getIdentifier();
/*     */     
/* 491 */     relation = ADQLParsingHelper.normalizeEventType(relation);
/*     */     
/* 493 */     this.parsingHelper.addEventSchema(relation);
/*     */     
/* 495 */     FilterBuilder whereFilter = null;
/* 496 */     ArrayList<Pair<String, String>> fieldLabels = new ArrayList();
/* 497 */     ArrayList<org.elasticsearch.search.aggregations.AbstractAggregationBuilder> aggsBuilders = null;
/*     */     
/* 499 */     if (ctx.booleanExpression() != null) {
/* 500 */       EsFilter filter = (EsFilter)visit(ctx.booleanExpression());
/* 501 */       whereFilter = filter.getFilterBuilder();
/*     */     }
/*     */     
/* 504 */     boolean allFields = false;
/* 505 */     for (com.appdynamics.analytics.processor.query.generated.ADQLParser.SelectItemContext selectItemContext : ctx.selectItem()) {
/* 506 */       String label = null;
/* 507 */       EsNode item = (EsNode)visit(selectItemContext);
/* 508 */       if ((item instanceof EsItem)) {
/* 509 */         EsItem singleItem = (EsItem)item;
/* 510 */         label = singleItem.getLabel();
/* 511 */         item = singleItem.getValue();
/*     */       }
/* 513 */       if ((item instanceof EsIdentifier)) {
/* 514 */         EsIdentifier value = (EsIdentifier)item;
/* 515 */         String field = value.getIdentifier();
/* 516 */         if (field == "*") {
/* 517 */           allFields = true;
/*     */         } else {
/* 519 */           field = this.parsingHelper.normalizeIdentifierForEventType(field);
/* 520 */           fieldLabels.add(new Pair(field, label));
/*     */         }
/* 522 */       } else if ((item instanceof EsAggs)) {
/* 523 */         EsAggs value = (EsAggs)item;
/* 524 */         String name = value.getName();
/* 525 */         if (aggsBuilders == null) {
/* 526 */           aggsBuilders = new ArrayList();
/*     */         }
/* 528 */         if (value.getAggsBuilder() != null) {
/* 529 */           aggsBuilders.add(value.getAggsBuilder());
/*     */         }
/* 531 */         fieldLabels.add(new Pair(name, label));
/*     */       }
/*     */     }
/*     */     
/* 535 */     return new com.appdynamics.analytics.processor.query.node.EsQuery(fieldLabels, allFields, aggsBuilders, relation, whereFilter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitParenthesizedExpression(@NotNull ADQLParser.ParenthesizedExpressionContext ctx)
/*     */   {
/* 546 */     return (EsNode)visit(ctx.getChild(1));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EsNode visitFunctionCall(@NotNull ADQLParser.FunctionCallContext ctx)
/*     */   {
/* 556 */     EsIdentifier function = (EsIdentifier)visit(ctx.qualifiedName());
/* 557 */     String functionIdentifier = function.getIdentifier();
/*     */     
/* 559 */     String fieldName = "*";
/* 560 */     String field = "_id";
/* 561 */     if (ctx.ASTERISK() == null) {
/* 562 */       EsIdentifier identifier = (EsIdentifier)visit(ctx.columnName());
/* 563 */       field = this.parsingHelper.normalizeIdentifierForEventType(identifier.getIdentifier());
/* 564 */       fieldName = field.toLowerCase();
/*     */     }
/*     */     
/* 567 */     this.parsingHelper.validateAggegrateFunctionUsage(functionIdentifier, field);
/*     */     
/* 569 */     String name = null;
/* 570 */     org.elasticsearch.search.aggregations.AbstractAggregationBuilder aggsBuilder = null;
/* 571 */     switch (functionIdentifier.toLowerCase()) {
/*     */     case "avg": 
/* 573 */       name = String.format("avg(%s)", new Object[] { fieldName });
/* 574 */       aggsBuilder = AggregationBuilders.avg(name).field(field);
/* 575 */       break;
/*     */     case "min": 
/* 577 */       name = String.format("min(%s)", new Object[] { fieldName });
/* 578 */       aggsBuilder = AggregationBuilders.min(name).field(field);
/* 579 */       break;
/*     */     case "max": 
/* 581 */       name = String.format("max(%s)", new Object[] { fieldName });
/* 582 */       aggsBuilder = AggregationBuilders.max(name).field(field);
/* 583 */       break;
/*     */     case "sum": 
/* 585 */       name = String.format("sum(%s)", new Object[] { fieldName });
/* 586 */       aggsBuilder = AggregationBuilders.sum(name).field(field);
/* 587 */       break;
/*     */     case "count": 
/* 589 */       name = String.format("count(%s)", new Object[] { fieldName });
/* 590 */       aggsBuilder = null;
/*     */       
/*     */ 
/*     */ 
/* 594 */       break;
/*     */     default: 
/* 596 */       throw new ParsingException(String.format("Unknown aggregate function [%s] on field [%s]", new Object[] { functionIdentifier, field }));
/*     */     }
/*     */     
/*     */     
/* 600 */     return new EsAggs(name, aggsBuilder);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/ADQLtoESQueryVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */