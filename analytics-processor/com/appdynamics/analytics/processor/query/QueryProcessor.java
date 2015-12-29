/*     */ package com.appdynamics.analytics.processor.query;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.event.query.EventSchema;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLLexer;
/*     */ import com.appdynamics.analytics.processor.query.generated.ADQLParser;
/*     */ import com.appdynamics.analytics.processor.query.generated.SqlBaseBaseVisitor;
/*     */ import com.appdynamics.analytics.processor.query.generated.SqlBaseLexer;
/*     */ import com.appdynamics.analytics.processor.query.generated.SqlBaseParser;
/*     */ import com.appdynamics.analytics.processor.query.node.EsQuery;
/*     */ import com.google.common.base.Function;
/*     */ import org.antlr.v4.runtime.ANTLRInputStream;
/*     */ import org.antlr.v4.runtime.BaseErrorListener;
/*     */ import org.antlr.v4.runtime.CommonTokenStream;
/*     */ import org.antlr.v4.runtime.RecognitionException;
/*     */ import org.antlr.v4.runtime.Recognizer;
/*     */ import org.antlr.v4.runtime.atn.ParserATNSimulator;
/*     */ import org.antlr.v4.runtime.atn.PredictionMode;
/*     */ import org.antlr.v4.runtime.misc.NotNull;
/*     */ import org.antlr.v4.runtime.misc.ParseCancellationException;
/*     */ import org.antlr.v4.runtime.tree.ParseTree;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QueryProcessor
/*     */ {
/*  33 */   private static final Logger log = LoggerFactory.getLogger(QueryProcessor.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  39 */   private static final BaseErrorListener ERROR_LISTENER = new BaseErrorListener()
/*     */   {
/*     */ 
/*     */ 
/*     */ 
/*     */     public void syntaxError(@NotNull Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, @NotNull String message, RecognitionException e)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  49 */       throw new ParsingException(message, e, line, charPositionInLine);
/*     */     }
/*     */   };
/*     */   
/*     */   public static void validateSqlQuery(String query) {
/*  54 */     ANTLRInputStream input = new ANTLRInputStream(query);
/*  55 */     SqlBaseLexer lexer = new SqlBaseLexer(new CaseInsensitiveStream(input));
/*  56 */     CommonTokenStream tokens = new CommonTokenStream(lexer);
/*     */     
/*  58 */     lexer.removeErrorListeners();
/*  59 */     lexer.addErrorListener(ERROR_LISTENER);
/*     */     
/*  61 */     SqlBaseParser parser = new SqlBaseParser(tokens);
/*  62 */     parser.removeErrorListeners();
/*  63 */     parser.addErrorListener(ERROR_LISTENER);
/*     */     
/*  65 */     ParseTree tree = parser.query();
/*     */     
/*  67 */     SqlBaseBaseVisitor visitor = new SqlBaseBaseVisitor();
/*  68 */     visitor.visit(tree);
/*     */   }
/*     */   
/*     */   public static EsQuery ADQLtoESQuery(String adql) {
/*  72 */     return ADQLtoESQuery(adql, null);
/*     */   }
/*     */   
/*     */   public static EsQuery ADQLtoESQuery(String adql, Function<String, EventSchema> getEventSchemaForEvent) {
/*  76 */     return ADQLtoESQuery(adql, getEventSchemaForEvent, false);
/*     */   }
/*     */   
/*     */   public static EsQuery ADQLtoESQuery(String adql, Function<String, EventSchema> getEventSchemaForEvent, boolean trace)
/*     */   {
/*  81 */     ParseTree tree = parseADQLintoParseTree(adql, trace);
/*  82 */     ADQLtoESQueryVisitor visitor = new ADQLtoESQueryVisitor(getEventSchemaForEvent);
/*  83 */     return (EsQuery)visitor.visit(tree);
/*     */   }
/*     */   
/*     */   public static String ADQLtoSQLQuery(String adql) {
/*  87 */     return ADQLtoSQLQuery(adql, null, false);
/*     */   }
/*     */   
/*     */   public static String ADQLtoSQLQuery(String adql, Function<String, EventSchema> getEventSchemaForEvent) {
/*  91 */     return ADQLtoSQLQuery(adql, getEventSchemaForEvent, false);
/*     */   }
/*     */   
/*     */   public static String ADQLtoSQLQuery(String adql, Function<String, EventSchema> getEventSchemaForEvent, boolean trace)
/*     */   {
/*  96 */     ParseTree tree = parseADQLintoParseTree(adql, trace);
/*  97 */     ADQLtoSQLQueryVisitor visitor = new ADQLtoSQLQueryVisitor(getEventSchemaForEvent);
/*  98 */     return (String)visitor.visit(tree);
/*     */   }
/*     */   
/*     */   public static ParseTree parseADQLintoParseTree(String adql) {
/* 102 */     return parseADQLintoParseTree(adql, false);
/*     */   }
/*     */   
/*     */   private static ParseTree parseADQLintoParseTree(String adql, boolean trace) {
/* 106 */     ANTLRInputStream input = new ANTLRInputStream(adql);
/* 107 */     ADQLLexer lexer = new ADQLLexer(new CaseInsensitiveStream(input));
/* 108 */     CommonTokenStream tokens = new CommonTokenStream(lexer);
/*     */     
/* 110 */     lexer.removeErrorListeners();
/* 111 */     lexer.addErrorListener(ERROR_LISTENER);
/*     */     
/* 113 */     ADQLParser parser = new ADQLParser(tokens);
/* 114 */     parser.removeErrorListeners();
/* 115 */     parser.addErrorListener(ERROR_LISTENER);
/*     */     
/* 117 */     parser.setTrace(trace);
/*     */     ParseTree tree;
/*     */     try
/*     */     {
/* 121 */       ((ParserATNSimulator)parser.getInterpreter()).setPredictionMode(PredictionMode.SLL);
/* 122 */       tree = parser.query();
/*     */     } catch (ParseCancellationException ex) {
/* 124 */       tokens.reset();
/* 125 */       parser.reset();
/*     */       
/* 127 */       ((ParserATNSimulator)parser.getInterpreter()).setPredictionMode(PredictionMode.LL);
/* 128 */       tree = parser.query();
/*     */     }
/*     */     
/* 131 */     return tree;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/QueryProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */