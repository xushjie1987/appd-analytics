/*      */ package com.appdynamics.analytics.processor.query.generated;
/*      */ 
/*      */ import org.antlr.v4.runtime.tree.ParseTreeListener;
/*      */ 
/*      */ public class ADQLParser extends org.antlr.v4.runtime.Parser { protected static final org.antlr.v4.runtime.dfa.DFA[] _decisionToDFA;
/*      */   protected static final org.antlr.v4.runtime.atn.PredictionContextCache _sharedContextCache;
/*      */   public static final int T__3 = 1;
/*      */   public static final int T__2 = 2;
/*      */   public static final int T__1 = 3;
/*      */   public static final int T__0 = 4;
/*      */   public static final int SELECT = 5;
/*      */   public static final int FROM = 6;
/*      */   public static final int AS = 7;
/*      */   public static final int ALL = 8;
/*      */   public static final int SOME = 9;
/*      */   public static final int ANY = 10;
/*      */   public static final int DISTINCT = 11;
/*      */   public static final int WHERE = 12;
/*      */   public static final int GROUP = 13;
/*      */   public static final int BY = 14;
/*      */   public static final int ORDER = 15;
/*      */   public static final int HAVING = 16;
/*      */   public static final int LIMIT = 17;
/*      */   public static final int APPROXIMATE = 18;
/*      */   public static final int AT = 19;
/*      */   public static final int CONFIDENCE = 20;
/*      */   public static final int OR = 21;
/*      */   public static final int AND = 22;
/*      */   public static final int IN = 23;
/*      */   public static final int NOT = 24;
/*      */   public static final int EXISTS = 25;
/*      */   public static final int BETWEEN = 26;
/*      */   public static final int LIKE = 27;
/*      */   public static final int IS = 28;
/*      */   public static final int NULL = 29;
/*      */   public static final int TRUE = 30;
/*      */   public static final int FALSE = 31;
/*      */   public static final int NULLS = 32;
/*      */   public static final int FIRST = 33;
/*      */   public static final int LAST = 34;
/*      */   public static final int ESCAPE = 35;
/*      */   public static final int ASC = 36;
/*      */   public static final int DESC = 37;
/*      */   public static final int SUBSTRING = 38;
/*      */   public static final int FOR = 39;
/*      */   public static final int DATE = 40;
/*      */   public static final int TIME = 41;
/*      */   public static final int TIMESTAMP = 42;
/*      */   public static final int INTERVAL = 43;
/*      */   public static final int YEAR = 44;
/*      */   public static final int MONTH = 45;
/*      */   public static final int DAY = 46;
/*      */   public static final int HOUR = 47;
/*      */   public static final int MINUTE = 48;
/*      */   public static final int SECOND = 49;
/*      */   public static final int ZONE = 50;
/*      */   public static final int CURRENT_DATE = 51;
/*      */   public static final int CURRENT_TIME = 52;
/*      */   public static final int CURRENT_TIMESTAMP = 53;
/*      */   public static final int LOCALTIME = 54;
/*      */   public static final int LOCALTIMESTAMP = 55;
/*      */   public static final int EXTRACT = 56;
/*      */   public static final int CASE = 57;
/*      */   public static final int WHEN = 58;
/*      */   public static final int THEN = 59;
/*      */   public static final int ELSE = 60;
/*      */   public static final int END = 61;
/*      */   public static final int JOIN = 62;
/*      */   public static final int CROSS = 63;
/*      */   public static final int OUTER = 64;
/*      */   public static final int INNER = 65;
/*      */   public static final int LEFT = 66;
/*      */   public static final int RIGHT = 67;
/*      */   public static final int FULL = 68;
/*      */   public static final int NATURAL = 69;
/*      */   public static final int USING = 70;
/*      */   public static final int ON = 71;
/*      */   public static final int OVER = 72;
/*      */   public static final int PARTITION = 73;
/*      */   public static final int RANGE = 74;
/*      */   public static final int ROWS = 75;
/*      */   public static final int UNBOUNDED = 76;
/*      */   public static final int PRECEDING = 77;
/*      */   public static final int FOLLOWING = 78;
/*      */   public static final int CURRENT = 79;
/*      */   public static final int ROW = 80;
/*      */   public static final int WITH = 81;
/*      */   public static final int RECURSIVE = 82;
/*      */   public static final int VALUES = 83;
/*      */   public static final int CREATE = 84; public static final int TABLE = 85; public static final int VIEW = 86; public static final int REPLACE = 87; public static final int INSERT = 88; public static final int INTO = 89; public static final int CONSTRAINT = 90; public static final int DESCRIBE = 91; public static final int EXPLAIN = 92; public static final int FORMAT = 93; public static final int TYPE = 94; public static final int TEXT = 95; public static final int GRAPHVIZ = 96; public static final int JSON = 97; public static final int LOGICAL = 98; public static final int DISTRIBUTED = 99; public static final int CAST = 100; public static final int TRY_CAST = 101; public static final int SHOW = 102; public static final int TABLES = 103; public static final int SCHEMAS = 104; public static final int CATALOGS = 105;
/*   91 */   public String getGrammarFileName() { return "ADQL.g4"; }
/*      */   
/*      */   public static final int COLUMNS = 106; public static final int USE = 107; public static final int PARTITIONS = 108; public static final int FUNCTIONS = 109; public static final int DROP = 110; public static final int UNION = 111; public static final int EXCEPT = 112; public static final int INTERSECT = 113; public static final int TO = 114; public static final int SYSTEM = 115; public static final int BERNOULLI = 116; public static final int POISSONIZED = 117; public static final int TABLESAMPLE = 118; public static final int RESCALED = 119; public static final int STRATIFY = 120; public static final int ALTER = 121; public static final int RENAME = 122;
/*   94 */   public String[] getTokenNames() { return tokenNames; }
/*      */   
/*      */   public static final int UNNEST = 123; public static final int ARRAY = 124; public static final int SET = 125; public static final int RESET = 126; public static final int SESSION = 127; public static final int IF = 128; public static final int NULLIF = 129; public static final int COALESCE = 130; public static final int EQ = 131; public static final int NEQ = 132; public static final int LT = 133; public static final int LTE = 134; public static final int GT = 135; public static final int GTE = 136;
/*   97 */   public String[] getRuleNames() { return ruleNames; }
/*      */   
/*      */   public static final int PLUS = 137; public static final int MINUS = 138; public static final int ASTERISK = 139; public static final int SLASH = 140; public static final int PERCENT = 141; public static final int CONCAT = 142; public static final int QUESTIONMARK = 143; public static final int STRING = 144; public static final int INTEGER_VALUE = 145; public static final int DECIMAL_VALUE = 146; public static final int WILDCARD = 147;
/*  100 */   public String getSerializedATN() { return "\003а훑舆괭䐗껱趀ꫝ\003 Ú\004\002\t\002\004\003\t\003\004\004\t\004\004\005\t\005\004\006\t\006\004\007\t\007\004\b\t\b\004\t\t\t\004\n\t\n\004\013\t\013\004\f\t\f\004\r\t\r\004\016\t\016\004\017\t\017\004\020\t\020\004\021\t\021\004\022\t\022\004\023\t\023\004\024\t\024\004\025\t\025\004\026\t\026\004\027\t\027\004\030\t\030\004\031\t\031\004\032\t\032\004\033\t\033\004\034\t\034\004\035\t\035\003\002\003\002\003\002\003\003\003\003\003\004\003\004\003\005\003\005\003\005\003\005\005\005F\n\005\003\005\003\005\005\005J\n\005\003\006\003\006\003\007\003\007\003\b\003\b\005\bR\n\b\003\t\003\t\003\t\003\t\007\tX\n\t\f\t\016\t[\013\t\003\t\003\t\005\t_\n\t\003\t\003\t\005\tc\n\t\003\n\003\n\003\n\005\nh\n\n\003\n\003\n\003\n\005\nm\n\n\003\n\003\n\003\n\003\n\003\n\005\nt\n\n\003\013\003\013\003\f\003\f\003\r\003\r\003\016\003\016\003\016\003\016\003\016\003\016\003\016\003\016\005\016\n\016\003\016\003\016\003\016\003\016\003\016\003\016\007\016\n\016\f\016\016\016\013\016\003\017\003\017\003\017\003\017\003\017\005\017\n\017\003\020\003\020\003\020\003\020\005\020\n\020\003\020\003\020\003\020\003\020\003\020\005\020£\n\020\003\021\003\021\003\022\003\022\003\022\003\022\005\022«\n\022\003\023\003\023\003\023\003\023\003\023\003\023\003\023\003\023\003\023\003\023\005\023·\n\023\003\024\003\024\003\025\003\025\003\026\003\026\003\027\003\027\003\030\003\030\003\031\003\031\003\031\007\031Æ\n\031\f\031\016\031É\013\031\003\032\003\032\003\032\003\032\003\032\005\032Ð\n\032\003\033\003\033\003\034\003\034\005\034Ö\n\034\003\035\003\035\003\035\002\003\032\036\002\004\006\b\n\f\016\020\022\024\026\030\032\034\036 \"$&(*,.02468\002\007\003\002&'\005\002\003\002\003\002 !\013\002\024\026*3JMORXY^ehotyÚ\002:\003\002\002\002\004=\003\002\002\002\006?\003\002\002\002\bA\003\002\002\002\nK\003\002\002\002\fM\003\002\002\002\016O\003\002\002\002\020S\003\002\002\002\022s\003\002\002\002\024u\003\002\002\002\026w\003\002\002\002\030y\003\002\002\002\032\003\002\002\002\034\003\002\002\002\036¢\003\002\002\002 ¤\003\002\002\002\"ª\003\002\002\002$¶\003\002\002\002&¸\003\002\002\002(º\003\002\002\002*¼\003\002\002\002,¾\003\002\002\002.À\003\002\002\0020Â\003\002\002\0022Ï\003\002\002\0024Ñ\003\002\002\0026Õ\003\002\002\0028×\003\002\002\002:;\005\004\003\002;<\007\002\002\003<\003\003\002\002\002=>\005\006\004\002>\005\003\002\002\002?@\005\b\005\002@\007\003\002\002\002AE\005\n\006\002BC\007\021\002\002CD\007\020\002\002DF\005\016\b\002EB\003\002\002\002EF\003\002\002\002FI\003\002\002\002GH\007\023\002\002HJ\007\002\002IG\003\002\002\002IJ\003\002\002\002J\t\003\002\002\002KL\005\f\007\002L\013\003\002\002\002MN\005\020\t\002N\r\003\002\002\002OQ\005\030\r\002PR\t\002\002\002QP\003\002\002\002QR\003\002\002\002R\017\003\002\002\002ST\007\007\002\002TY\005\022\n\002UV\007\005\002\002VX\005\022\n\002WU\003\002\002\002X[\003\002\002\002YW\003\002\002\002YZ\003\002\002\002Z^\003\002\002\002[Y\003\002\002\002\\]\007\b\002\002]_\005\024\013\002^\\\003\002\002\002^_\003\002\002\002_b\003\002\002\002`a\007\016\002\002ac\005\032\016\002b`\003\002\002\002bc\003\002\002\002c\021\003\002\002\002dg\0050\031\002ef\007\t\002\002fh\0052\032\002ge\003\002\002\002gh\003\002\002\002ht\003\002\002\002il\005$\023\002jk\007\t\002\002km\0052\032\002lj\003\002\002\002lm\003\002\002\002mt\003\002\002\002no\0050\031\002op\007\003\002\002pq\007\002\002qt\003\002\002\002rt\007\002\002sd\003\002\002\002si\003\002\002\002sn\003\002\002\002sr\003\002\002\002t\023\003\002\002\002uv\005\026\f\002v\025\003\002\002\002wx\0050\031\002x\027\003\002\002\002yz\005\032\016\002z\031\003\002\002\002{|\b\016\001\002|}\007\032\002\002}\005\032\016\006~\005\034\017\002\007\006\002\002\005\030\r\002\007\004\002\002\003\002\002\002{\003\002\002\002~\003\002\002\002\003\002\002\002\003\002\002\002\f\005\002\002\007\030\002\002\005\032\016\006\f\004\002\002\007\027\002\002\005\032\016\005\003\002\002\002\003\002\002\002\003\002\002\002\003\002\002\002\003\002\002\002\033\003\002\002\002\003\002\002\002\005&\024\002\005\036\020\002\003\002\002\002\005(\025\002\005.\030\002\003\002\002\002\003\002\002\002\003\002\002\002\035\003\002\002\002\005,\027\002\005 \021\002£\003\002\002\002\007\032\002\002\003\002\002\002\003\002\002\002\003\002\002\002\007\034\002\002\005 \021\002 \007\030\002\002 ¡\005 \021\002¡£\003\002\002\002¢\003\002\002\002¢\003\002\002\002£\037\003\002\002\002¤¥\005\"\022\002¥!\003\002\002\002¦«\007\037\002\002§«\0056\034\002¨«\005.\030\002©«\007\002\002ª¦\003\002\002\002ª§\003\002\002\002ª¨\003\002\002\002ª©\003\002\002\002«#\003\002\002\002¬­\0050\031\002­®\007\006\002\002®¯\007\002\002¯°\007\004\002\002°·\003\002\002\002±²\0050\031\002²³\007\006\002\002³´\005&\024\002´µ\007\004\002\002µ·\003\002\002\002¶¬\003\002\002\002¶±\003\002\002\002·%\003\002\002\002¸¹\0050\031\002¹'\003\002\002\002º»\005*\026\002»)\003\002\002\002¼½\t\003\002\002½+\003\002\002\002¾¿\t\004\002\002¿-\003\002\002\002ÀÁ\t\005\002\002Á/\003\002\002\002ÂÇ\0052\032\002ÃÄ\007\003\002\002ÄÆ\0052\032\002ÅÃ\003\002\002\002ÆÉ\003\002\002\002ÇÅ\003\002\002\002ÇÈ\003\002\002\002È1\003\002\002\002ÉÇ\003\002\002\002ÊÐ\007\002\002ËÐ\0054\033\002ÌÐ\0058\035\002ÍÐ\007\002\002ÎÐ\007\002\002ÏÊ\003\002\002\002ÏË\003\002\002\002ÏÌ\003\002\002\002ÏÍ\003\002\002\002ÏÎ\003\002\002\002Ð3\003\002\002\002ÑÒ\007\002\002Ò5\003\002\002\002ÓÖ\007\002\002ÔÖ\007\002\002ÕÓ\003\002\002\002ÕÔ\003\002\002\002Ö7\003\002\002\002×Ø\t\006\002\002Ø9\003\002\002\002\026EIQY^bgls¢ª¶ÇÏÕ"; }
/*      */   
/*      */   public static final int IDENTIFIER = 148; public static final int DIGIT_IDENTIFIER = 149; public static final int QUOTED_IDENTIFIER = 150; public static final int BACKQUOTED_IDENTIFIER = 151; public static final int TIME_WITH_TIME_ZONE = 152; public static final int TIMESTAMP_WITH_TIME_ZONE = 153; public static final int SIMPLE_COMMENT = 154; public static final int BRACKETED_COMMENT = 155;
/*  103 */   public org.antlr.v4.runtime.atn.ATN getATN() { return _ATN; }
/*      */   
/*      */   public static final int WS = 156; public static final int UNRECOGNIZED = 157; public static final int DELIMITER = 158; public static final String[] tokenNames; public static final int RULE_singleStatement = 0; public static final int RULE_statement = 1;
/*  106 */   public ADQLParser(org.antlr.v4.runtime.TokenStream input) { super(input);
/*  107 */     this._interp = new org.antlr.v4.runtime.atn.ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache); }
/*      */   
/*      */   public static final int RULE_query = 2;
/*      */   public static final int RULE_queryNoWith = 3;
/*  111 */   public static class SingleStatementContext extends org.antlr.v4.runtime.ParserRuleContext { public ADQLParser.StatementContext statement() { return (ADQLParser.StatementContext)getRuleContext(ADQLParser.StatementContext.class, 0); }
/*      */     
/*  113 */     public org.antlr.v4.runtime.tree.TerminalNode EOF() { return getToken(-1, 0); }
/*      */     
/*  115 */     public SingleStatementContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/*  117 */     public int getRuleIndex() { return 0; }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  120 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterSingleStatement(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  124 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitSingleStatement(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  128 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitSingleStatement(this);
/*  129 */       return (T)visitor.visitChildren(this); } }
/*      */   
/*      */   public static final int RULE_queryTerm = 4;
/*      */   public static final int RULE_queryPrimary = 5;
/*      */   public static final int RULE_sortItem = 6; public static final int RULE_querySpecification = 7; public static final int RULE_selectItem = 8; public static final int RULE_relation = 9; public static final int RULE_relationPrimary = 10; public static final int RULE_expression = 11; public static final int RULE_booleanExpression = 12; public static final int RULE_predicated = 13; public static final int RULE_predicate = 14; public static final int RULE_valueExpression = 15; public static final int RULE_primaryExpression = 16;
/*  134 */   public final SingleStatementContext singleStatement() throws org.antlr.v4.runtime.RecognitionException { SingleStatementContext _localctx = new SingleStatementContext(this._ctx, getState());
/*  135 */     enterRule(_localctx, 0, 0);
/*      */     try {
/*  137 */       enterOuterAlt(_localctx, 1);
/*      */       
/*  139 */       setState(56);statement();
/*  140 */       setState(57);match(-1);
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/*  144 */       _localctx.exception = re;
/*  145 */       this._errHandler.reportError(this, re);
/*  146 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/*  149 */       exitRule();
/*      */     }
/*  151 */     return _localctx; }
/*      */   
/*      */   public static final int RULE_function = 17;
/*      */   public static final int RULE_columnName = 18;
/*      */   public static final int RULE_matchString = 19;
/*  156 */   public static final int RULE_primaryMatchString = 20; public static final int RULE_comparisonOperator = 21; public static final int RULE_booleanValue = 22; public static final int RULE_qualifiedName = 23; public static final int RULE_identifier = 24; public static final int RULE_quotedIdentifier = 25; public static final int RULE_number = 26; public static final int RULE_nonReserved = 27; public static final String[] ruleNames; public static final String _serializedATN = "\003а훑舆괭䐗껱趀ꫝ\003 Ú\004\002\t\002\004\003\t\003\004\004\t\004\004\005\t\005\004\006\t\006\004\007\t\007\004\b\t\b\004\t\t\t\004\n\t\n\004\013\t\013\004\f\t\f\004\r\t\r\004\016\t\016\004\017\t\017\004\020\t\020\004\021\t\021\004\022\t\022\004\023\t\023\004\024\t\024\004\025\t\025\004\026\t\026\004\027\t\027\004\030\t\030\004\031\t\031\004\032\t\032\004\033\t\033\004\034\t\034\004\035\t\035\003\002\003\002\003\002\003\003\003\003\003\004\003\004\003\005\003\005\003\005\003\005\005\005F\n\005\003\005\003\005\005\005J\n\005\003\006\003\006\003\007\003\007\003\b\003\b\005\bR\n\b\003\t\003\t\003\t\003\t\007\tX\n\t\f\t\016\t[\013\t\003\t\003\t\005\t_\n\t\003\t\003\t\005\tc\n\t\003\n\003\n\003\n\005\nh\n\n\003\n\003\n\003\n\005\nm\n\n\003\n\003\n\003\n\003\n\003\n\005\nt\n\n\003\013\003\013\003\f\003\f\003\r\003\r\003\016\003\016\003\016\003\016\003\016\003\016\003\016\003\016\005\016\n\016\003\016\003\016\003\016\003\016\003\016\003\016\007\016\n\016\f\016\016\016\013\016\003\017\003\017\003\017\003\017\003\017\005\017\n\017\003\020\003\020\003\020\003\020\005\020\n\020\003\020\003\020\003\020\003\020\003\020\005\020£\n\020\003\021\003\021\003\022\003\022\003\022\003\022\005\022«\n\022\003\023\003\023\003\023\003\023\003\023\003\023\003\023\003\023\003\023\003\023\005\023·\n\023\003\024\003\024\003\025\003\025\003\026\003\026\003\027\003\027\003\030\003\030\003\031\003\031\003\031\007\031Æ\n\031\f\031\016\031É\013\031\003\032\003\032\003\032\003\032\003\032\005\032Ð\n\032\003\033\003\033\003\034\003\034\005\034Ö\n\034\003\035\003\035\003\035\002\003\032\036\002\004\006\b\n\f\016\020\022\024\026\030\032\034\036 \"$&(*,.02468\002\007\003\002&'\005\002\003\002\003\002 !\013\002\024\026*3JMORXY^ehotyÚ\002:\003\002\002\002\004=\003\002\002\002\006?\003\002\002\002\bA\003\002\002\002\nK\003\002\002\002\fM\003\002\002\002\016O\003\002\002\002\020S\003\002\002\002\022s\003\002\002\002\024u\003\002\002\002\026w\003\002\002\002\030y\003\002\002\002\032\003\002\002\002\034\003\002\002\002\036¢\003\002\002\002 ¤\003\002\002\002\"ª\003\002\002\002$¶\003\002\002\002&¸\003\002\002\002(º\003\002\002\002*¼\003\002\002\002,¾\003\002\002\002.À\003\002\002\0020Â\003\002\002\0022Ï\003\002\002\0024Ñ\003\002\002\0026Õ\003\002\002\0028×\003\002\002\002:;\005\004\003\002;<\007\002\002\003<\003\003\002\002\002=>\005\006\004\002>\005\003\002\002\002?@\005\b\005\002@\007\003\002\002\002AE\005\n\006\002BC\007\021\002\002CD\007\020\002\002DF\005\016\b\002EB\003\002\002\002EF\003\002\002\002FI\003\002\002\002GH\007\023\002\002HJ\007\002\002IG\003\002\002\002IJ\003\002\002\002J\t\003\002\002\002KL\005\f\007\002L\013\003\002\002\002MN\005\020\t\002N\r\003\002\002\002OQ\005\030\r\002PR\t\002\002\002QP\003\002\002\002QR\003\002\002\002R\017\003\002\002\002ST\007\007\002\002TY\005\022\n\002UV\007\005\002\002VX\005\022\n\002WU\003\002\002\002X[\003\002\002\002YW\003\002\002\002YZ\003\002\002\002Z^\003\002\002\002[Y\003\002\002\002\\]\007\b\002\002]_\005\024\013\002^\\\003\002\002\002^_\003\002\002\002_b\003\002\002\002`a\007\016\002\002ac\005\032\016\002b`\003\002\002\002bc\003\002\002\002c\021\003\002\002\002dg\0050\031\002ef\007\t\002\002fh\0052\032\002ge\003\002\002\002gh\003\002\002\002ht\003\002\002\002il\005$\023\002jk\007\t\002\002km\0052\032\002lj\003\002\002\002lm\003\002\002\002mt\003\002\002\002no\0050\031\002op\007\003\002\002pq\007\002\002qt\003\002\002\002rt\007\002\002sd\003\002\002\002si\003\002\002\002sn\003\002\002\002sr\003\002\002\002t\023\003\002\002\002uv\005\026\f\002v\025\003\002\002\002wx\0050\031\002x\027\003\002\002\002yz\005\032\016\002z\031\003\002\002\002{|\b\016\001\002|}\007\032\002\002}\005\032\016\006~\005\034\017\002\007\006\002\002\005\030\r\002\007\004\002\002\003\002\002\002{\003\002\002\002~\003\002\002\002\003\002\002\002\003\002\002\002\f\005\002\002\007\030\002\002\005\032\016\006\f\004\002\002\007\027\002\002\005\032\016\005\003\002\002\002\003\002\002\002\003\002\002\002\003\002\002\002\003\002\002\002\033\003\002\002\002\003\002\002\002\005&\024\002\005\036\020\002\003\002\002\002\005(\025\002\005.\030\002\003\002\002\002\003\002\002\002\003\002\002\002\035\003\002\002\002\005,\027\002\005 \021\002£\003\002\002\002\007\032\002\002\003\002\002\002\003\002\002\002\003\002\002\002\007\034\002\002\005 \021\002 \007\030\002\002 ¡\005 \021\002¡£\003\002\002\002¢\003\002\002\002¢\003\002\002\002£\037\003\002\002\002¤¥\005\"\022\002¥!\003\002\002\002¦«\007\037\002\002§«\0056\034\002¨«\005.\030\002©«\007\002\002ª¦\003\002\002\002ª§\003\002\002\002ª¨\003\002\002\002ª©\003\002\002\002«#\003\002\002\002¬­\0050\031\002­®\007\006\002\002®¯\007\002\002¯°\007\004\002\002°·\003\002\002\002±²\0050\031\002²³\007\006\002\002³´\005&\024\002´µ\007\004\002\002µ·\003\002\002\002¶¬\003\002\002\002¶±\003\002\002\002·%\003\002\002\002¸¹\0050\031\002¹'\003\002\002\002º»\005*\026\002»)\003\002\002\002¼½\t\003\002\002½+\003\002\002\002¾¿\t\004\002\002¿-\003\002\002\002ÀÁ\t\005\002\002Á/\003\002\002\002ÂÇ\0052\032\002ÃÄ\007\003\002\002ÄÆ\0052\032\002ÅÃ\003\002\002\002ÆÉ\003\002\002\002ÇÅ\003\002\002\002ÇÈ\003\002\002\002È1\003\002\002\002ÉÇ\003\002\002\002ÊÐ\007\002\002ËÐ\0054\033\002ÌÐ\0058\035\002ÍÐ\007\002\002ÎÐ\007\002\002ÏÊ\003\002\002\002ÏË\003\002\002\002ÏÌ\003\002\002\002ÏÍ\003\002\002\002ÏÎ\003\002\002\002Ð3\003\002\002\002ÑÒ\007\002\002Ò5\003\002\002\002ÓÖ\007\002\002ÔÖ\007\002\002ÕÓ\003\002\002\002ÕÔ\003\002\002\002Ö7\003\002\002\002×Ø\t\006\002\002Ø9\003\002\002\002\026EIQY^bgls¢ª¶ÇÏÕ"; public static final org.antlr.v4.runtime.atn.ATN _ATN; public static class StatementContext extends org.antlr.v4.runtime.ParserRuleContext { public StatementContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/*  158 */     public int getRuleIndex() { return 1; }
/*      */     
/*      */     public StatementContext() {}
/*      */     
/*  162 */     public void copyFrom(StatementContext ctx) { super.copyFrom(ctx); }
/*      */   }
/*      */   
/*      */   public static class StatementDefaultContext
/*      */     extends ADQLParser.StatementContext {
/*  167 */     public ADQLParser.QueryContext query() { return (ADQLParser.QueryContext)getRuleContext(ADQLParser.QueryContext.class, 0); }
/*      */     
/*  169 */     public StatementDefaultContext(ADQLParser.StatementContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  172 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterStatementDefault(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  176 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitStatementDefault(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  180 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitStatementDefault(this);
/*  181 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final StatementContext statement() throws org.antlr.v4.runtime.RecognitionException {
/*  186 */     StatementContext _localctx = new StatementContext(this._ctx, getState());
/*  187 */     enterRule(_localctx, 2, 1);
/*      */     try {
/*  189 */       _localctx = new StatementDefaultContext(_localctx);
/*  190 */       enterOuterAlt(_localctx, 1);
/*      */       
/*  192 */       setState(59);query();
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/*  196 */       _localctx.exception = re;
/*  197 */       this._errHandler.reportError(this, re);
/*  198 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/*  201 */       exitRule();
/*      */     }
/*  203 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class QueryContext extends org.antlr.v4.runtime.ParserRuleContext {
/*      */     public ADQLParser.QueryNoWithContext queryNoWith() {
/*  208 */       return (ADQLParser.QueryNoWithContext)getRuleContext(ADQLParser.QueryNoWithContext.class, 0);
/*      */     }
/*      */     
/*  211 */     public QueryContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/*  213 */     public int getRuleIndex() { return 2; }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  216 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterQuery(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  220 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitQuery(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  224 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitQuery(this);
/*  225 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final QueryContext query() throws org.antlr.v4.runtime.RecognitionException {
/*  230 */     QueryContext _localctx = new QueryContext(this._ctx, getState());
/*  231 */     enterRule(_localctx, 4, 2);
/*      */     try {
/*  233 */       enterOuterAlt(_localctx, 1);
/*      */       
/*  235 */       setState(61);queryNoWith();
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/*  239 */       _localctx.exception = re;
/*  240 */       this._errHandler.reportError(this, re);
/*  241 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/*  244 */       exitRule();
/*      */     }
/*  246 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class QueryNoWithContext extends org.antlr.v4.runtime.ParserRuleContext { public org.antlr.v4.runtime.Token limit;
/*      */     
/*  251 */     public org.antlr.v4.runtime.tree.TerminalNode INTEGER_VALUE() { return getToken(145, 0); }
/*  252 */     public org.antlr.v4.runtime.tree.TerminalNode LIMIT() { return getToken(17, 0); }
/*      */     
/*  254 */     public ADQLParser.SortItemContext sortItem() { return (ADQLParser.SortItemContext)getRuleContext(ADQLParser.SortItemContext.class, 0); }
/*      */     
/*  256 */     public org.antlr.v4.runtime.tree.TerminalNode ORDER() { return getToken(15, 0); }
/*  257 */     public org.antlr.v4.runtime.tree.TerminalNode BY() { return getToken(14, 0); }
/*      */     
/*  259 */     public ADQLParser.QueryTermContext queryTerm() { return (ADQLParser.QueryTermContext)getRuleContext(ADQLParser.QueryTermContext.class, 0); }
/*      */     
/*      */ 
/*  262 */     public QueryNoWithContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/*  264 */     public int getRuleIndex() { return 3; }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  267 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterQueryNoWith(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  271 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitQueryNoWith(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  275 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitQueryNoWith(this);
/*  276 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final QueryNoWithContext queryNoWith() throws org.antlr.v4.runtime.RecognitionException {
/*  281 */     QueryNoWithContext _localctx = new QueryNoWithContext(this._ctx, getState());
/*  282 */     enterRule(_localctx, 6, 3);
/*      */     try
/*      */     {
/*  285 */       enterOuterAlt(_localctx, 1);
/*      */       
/*  287 */       setState(63);queryTerm();
/*  288 */       setState(67);
/*  289 */       int _la = this._input.LA(1);
/*  290 */       if (_la == 15)
/*      */       {
/*  292 */         setState(64);match(15);
/*  293 */         setState(65);match(14);
/*  294 */         setState(66);sortItem();
/*      */       }
/*      */       
/*      */ 
/*  298 */       setState(71);
/*  299 */       _la = this._input.LA(1);
/*  300 */       if (_la == 17)
/*      */       {
/*  302 */         setState(69);match(17);
/*  303 */         setState(70);_localctx.limit = match(145);
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/*  310 */       _localctx.exception = re;
/*  311 */       this._errHandler.reportError(this, re);
/*  312 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/*  315 */       exitRule();
/*      */     }
/*  317 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class QueryTermContext
/*      */     extends org.antlr.v4.runtime.ParserRuleContext {
/*  322 */     public QueryTermContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/*  324 */     public int getRuleIndex() { return 4; }
/*      */     
/*      */     public QueryTermContext() {}
/*      */     
/*  328 */     public void copyFrom(QueryTermContext ctx) { super.copyFrom(ctx); }
/*      */   }
/*      */   
/*      */   public static class QueryTermDefaultContext
/*      */     extends ADQLParser.QueryTermContext {
/*  333 */     public ADQLParser.QueryPrimaryContext queryPrimary() { return (ADQLParser.QueryPrimaryContext)getRuleContext(ADQLParser.QueryPrimaryContext.class, 0); }
/*      */     
/*  335 */     public QueryTermDefaultContext(ADQLParser.QueryTermContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  338 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterQueryTermDefault(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  342 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitQueryTermDefault(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  346 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitQueryTermDefault(this);
/*  347 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final QueryTermContext queryTerm() throws org.antlr.v4.runtime.RecognitionException {
/*  352 */     QueryTermContext _localctx = new QueryTermContext(this._ctx, getState());
/*  353 */     enterRule(_localctx, 8, 4);
/*      */     try {
/*  355 */       _localctx = new QueryTermDefaultContext(_localctx);
/*  356 */       enterOuterAlt(_localctx, 1);
/*      */       
/*  358 */       setState(73);queryPrimary();
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/*  362 */       _localctx.exception = re;
/*  363 */       this._errHandler.reportError(this, re);
/*  364 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/*  367 */       exitRule();
/*      */     }
/*  369 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class QueryPrimaryContext
/*      */     extends org.antlr.v4.runtime.ParserRuleContext {
/*  374 */     public QueryPrimaryContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/*  376 */     public int getRuleIndex() { return 5; }
/*      */     
/*      */     public QueryPrimaryContext() {}
/*      */     
/*  380 */     public void copyFrom(QueryPrimaryContext ctx) { super.copyFrom(ctx); }
/*      */   }
/*      */   
/*      */   public static class QueryPrimaryDefaultContext
/*      */     extends ADQLParser.QueryPrimaryContext {
/*  385 */     public ADQLParser.QuerySpecificationContext querySpecification() { return (ADQLParser.QuerySpecificationContext)getRuleContext(ADQLParser.QuerySpecificationContext.class, 0); }
/*      */     
/*  387 */     public QueryPrimaryDefaultContext(ADQLParser.QueryPrimaryContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  390 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterQueryPrimaryDefault(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  394 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitQueryPrimaryDefault(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  398 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitQueryPrimaryDefault(this);
/*  399 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final QueryPrimaryContext queryPrimary() throws org.antlr.v4.runtime.RecognitionException {
/*  404 */     QueryPrimaryContext _localctx = new QueryPrimaryContext(this._ctx, getState());
/*  405 */     enterRule(_localctx, 10, 5);
/*      */     try {
/*  407 */       _localctx = new QueryPrimaryDefaultContext(_localctx);
/*  408 */       enterOuterAlt(_localctx, 1);
/*      */       
/*  410 */       setState(75);querySpecification();
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/*  414 */       _localctx.exception = re;
/*  415 */       this._errHandler.reportError(this, re);
/*  416 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/*  419 */       exitRule();
/*      */     }
/*  421 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class SortItemContext extends org.antlr.v4.runtime.ParserRuleContext { public org.antlr.v4.runtime.Token ordering;
/*      */     
/*  426 */     public org.antlr.v4.runtime.tree.TerminalNode DESC() { return getToken(37, 0); }
/*  427 */     public org.antlr.v4.runtime.tree.TerminalNode ASC() { return getToken(36, 0); }
/*      */     
/*  429 */     public ADQLParser.ExpressionContext expression() { return (ADQLParser.ExpressionContext)getRuleContext(ADQLParser.ExpressionContext.class, 0); }
/*      */     
/*      */ 
/*  432 */     public SortItemContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/*  434 */     public int getRuleIndex() { return 6; }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  437 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterSortItem(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  441 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitSortItem(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  445 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitSortItem(this);
/*  446 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final SortItemContext sortItem() throws org.antlr.v4.runtime.RecognitionException {
/*  451 */     SortItemContext _localctx = new SortItemContext(this._ctx, getState());
/*  452 */     enterRule(_localctx, 12, 6);
/*      */     try
/*      */     {
/*  455 */       enterOuterAlt(_localctx, 1);
/*      */       
/*  457 */       setState(77);expression();
/*  458 */       setState(79);
/*  459 */       int _la = this._input.LA(1);
/*  460 */       if ((_la == 36) || (_la == 37))
/*      */       {
/*  462 */         setState(78);
/*  463 */         _localctx.ordering = this._input.LT(1);
/*  464 */         _la = this._input.LA(1);
/*  465 */         if ((_la != 36) && (_la != 37)) {
/*  466 */           _localctx.ordering = this._errHandler.recoverInline(this);
/*      */         }
/*  468 */         consume();
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/*  475 */       _localctx.exception = re;
/*  476 */       this._errHandler.reportError(this, re);
/*  477 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/*  480 */       exitRule();
/*      */     }
/*  482 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class QuerySpecificationContext extends org.antlr.v4.runtime.ParserRuleContext { public ADQLParser.BooleanExpressionContext where;
/*      */     
/*  487 */     public org.antlr.v4.runtime.tree.TerminalNode WHERE() { return getToken(12, 0); }
/*      */     
/*  489 */     public ADQLParser.RelationContext relation() { return (ADQLParser.RelationContext)getRuleContext(ADQLParser.RelationContext.class, 0); }
/*      */     
/*      */     public java.util.List<ADQLParser.SelectItemContext> selectItem() {
/*  492 */       return getRuleContexts(ADQLParser.SelectItemContext.class);
/*      */     }
/*      */     
/*  495 */     public ADQLParser.BooleanExpressionContext booleanExpression() { return (ADQLParser.BooleanExpressionContext)getRuleContext(ADQLParser.BooleanExpressionContext.class, 0); }
/*      */     
/*      */ 
/*  498 */     public ADQLParser.SelectItemContext selectItem(int i) { return (ADQLParser.SelectItemContext)getRuleContext(ADQLParser.SelectItemContext.class, i); }
/*      */     
/*  500 */     public org.antlr.v4.runtime.tree.TerminalNode SELECT() { return getToken(5, 0); }
/*  501 */     public org.antlr.v4.runtime.tree.TerminalNode FROM() { return getToken(6, 0); }
/*      */     
/*  503 */     public QuerySpecificationContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/*  505 */     public int getRuleIndex() { return 7; }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  508 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterQuerySpecification(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  512 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitQuerySpecification(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  516 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitQuerySpecification(this);
/*  517 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final QuerySpecificationContext querySpecification() throws org.antlr.v4.runtime.RecognitionException {
/*  522 */     QuerySpecificationContext _localctx = new QuerySpecificationContext(this._ctx, getState());
/*  523 */     enterRule(_localctx, 14, 7);
/*      */     try
/*      */     {
/*  526 */       enterOuterAlt(_localctx, 1);
/*      */       
/*  528 */       setState(81);match(5);
/*  529 */       setState(82);selectItem();
/*  530 */       setState(87);
/*  531 */       this._errHandler.sync(this);
/*  532 */       int _la = this._input.LA(1);
/*  533 */       while (_la == 3)
/*      */       {
/*      */ 
/*  536 */         setState(83);match(3);
/*  537 */         setState(84);selectItem();
/*      */         
/*      */ 
/*  540 */         setState(89);
/*  541 */         this._errHandler.sync(this);
/*  542 */         _la = this._input.LA(1);
/*      */       }
/*  544 */       setState(92);
/*  545 */       _la = this._input.LA(1);
/*  546 */       if (_la == 6)
/*      */       {
/*  548 */         setState(90);match(6);
/*  549 */         setState(91);relation();
/*      */       }
/*      */       
/*      */ 
/*  553 */       setState(96);
/*  554 */       _la = this._input.LA(1);
/*  555 */       if (_la == 12)
/*      */       {
/*  557 */         setState(94);match(12);
/*  558 */         setState(95);_localctx.where = booleanExpression(0);
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/*  565 */       _localctx.exception = re;
/*  566 */       this._errHandler.reportError(this, re);
/*  567 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/*  570 */       exitRule();
/*      */     }
/*  572 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class SelectItemContext
/*      */     extends org.antlr.v4.runtime.ParserRuleContext {
/*  577 */     public SelectItemContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/*  579 */     public int getRuleIndex() { return 8; }
/*      */     
/*      */     public SelectItemContext() {}
/*      */     
/*  583 */     public void copyFrom(SelectItemContext ctx) { super.copyFrom(ctx); }
/*      */   }
/*      */   
/*      */   public static class SelectSingleContext extends ADQLParser.SelectItemContext {
/*  587 */     public org.antlr.v4.runtime.tree.TerminalNode AS() { return getToken(7, 0); }
/*      */     
/*  589 */     public ADQLParser.FunctionContext function() { return (ADQLParser.FunctionContext)getRuleContext(ADQLParser.FunctionContext.class, 0); }
/*      */     
/*      */     public ADQLParser.QualifiedNameContext qualifiedName() {
/*  592 */       return (ADQLParser.QualifiedNameContext)getRuleContext(ADQLParser.QualifiedNameContext.class, 0);
/*      */     }
/*      */     
/*  595 */     public ADQLParser.IdentifierContext identifier() { return (ADQLParser.IdentifierContext)getRuleContext(ADQLParser.IdentifierContext.class, 0); }
/*      */     
/*  597 */     public SelectSingleContext(ADQLParser.SelectItemContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  600 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterSelectSingle(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  604 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitSelectSingle(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  608 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitSelectSingle(this);
/*  609 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*  613 */   public static class SelectAllContext extends ADQLParser.SelectItemContext { public org.antlr.v4.runtime.tree.TerminalNode ASTERISK() { return getToken(139, 0); }
/*      */     
/*  615 */     public ADQLParser.QualifiedNameContext qualifiedName() { return (ADQLParser.QualifiedNameContext)getRuleContext(ADQLParser.QualifiedNameContext.class, 0); }
/*      */     
/*  617 */     public SelectAllContext(ADQLParser.SelectItemContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  620 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterSelectAll(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  624 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitSelectAll(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  628 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitSelectAll(this);
/*  629 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final SelectItemContext selectItem() throws org.antlr.v4.runtime.RecognitionException {
/*  634 */     SelectItemContext _localctx = new SelectItemContext(this._ctx, getState());
/*  635 */     enterRule(_localctx, 16, 8);
/*      */     try
/*      */     {
/*  638 */       setState(113);
/*  639 */       int _la; switch (((org.antlr.v4.runtime.atn.ParserATNSimulator)getInterpreter()).adaptivePredict(this._input, 8, this._ctx)) {
/*      */       case 1: 
/*  641 */         _localctx = new SelectSingleContext(_localctx);
/*  642 */         enterOuterAlt(_localctx, 1);
/*      */         
/*  644 */         setState(98);qualifiedName();
/*  645 */         setState(101);
/*  646 */         _la = this._input.LA(1);
/*  647 */         if (_la == 7)
/*      */         {
/*  649 */           setState(99);match(7);
/*  650 */           setState(100);identifier();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */         break;
/*      */       case 2: 
/*  658 */         _localctx = new SelectSingleContext(_localctx);
/*  659 */         enterOuterAlt(_localctx, 2);
/*      */         
/*  661 */         setState(103);function();
/*  662 */         setState(106);
/*  663 */         _la = this._input.LA(1);
/*  664 */         if (_la == 7)
/*      */         {
/*  666 */           setState(104);match(7);
/*  667 */           setState(105);identifier();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */         break;
/*      */       case 3: 
/*  675 */         _localctx = new SelectAllContext(_localctx);
/*  676 */         enterOuterAlt(_localctx, 3);
/*      */         
/*  678 */         setState(108);qualifiedName();
/*  679 */         setState(109);match(1);
/*  680 */         setState(110);match(139);
/*      */         
/*  682 */         break;
/*      */       
/*      */       case 4: 
/*  685 */         _localctx = new SelectAllContext(_localctx);
/*  686 */         enterOuterAlt(_localctx, 4);
/*      */         
/*  688 */         setState(112);match(139);
/*      */       }
/*      */       
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/*  694 */       _localctx.exception = re;
/*  695 */       this._errHandler.reportError(this, re);
/*  696 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/*  699 */       exitRule();
/*      */     }
/*  701 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class RelationContext
/*      */     extends org.antlr.v4.runtime.ParserRuleContext {
/*  706 */     public RelationContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/*  708 */     public int getRuleIndex() { return 9; }
/*      */     
/*      */     public RelationContext() {}
/*      */     
/*  712 */     public void copyFrom(RelationContext ctx) { super.copyFrom(ctx); }
/*      */   }
/*      */   
/*      */   public static class RelationDefaultContext
/*      */     extends ADQLParser.RelationContext {
/*  717 */     public ADQLParser.RelationPrimaryContext relationPrimary() { return (ADQLParser.RelationPrimaryContext)getRuleContext(ADQLParser.RelationPrimaryContext.class, 0); }
/*      */     
/*  719 */     public RelationDefaultContext(ADQLParser.RelationContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  722 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterRelationDefault(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  726 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitRelationDefault(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  730 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitRelationDefault(this);
/*  731 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final RelationContext relation() throws org.antlr.v4.runtime.RecognitionException {
/*  736 */     RelationContext _localctx = new RelationContext(this._ctx, getState());
/*  737 */     enterRule(_localctx, 18, 9);
/*      */     try {
/*  739 */       _localctx = new RelationDefaultContext(_localctx);
/*  740 */       enterOuterAlt(_localctx, 1);
/*      */       
/*  742 */       setState(115);relationPrimary();
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/*  746 */       _localctx.exception = re;
/*  747 */       this._errHandler.reportError(this, re);
/*  748 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/*  751 */       exitRule();
/*      */     }
/*  753 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class RelationPrimaryContext
/*      */     extends org.antlr.v4.runtime.ParserRuleContext {
/*  758 */     public RelationPrimaryContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/*  760 */     public int getRuleIndex() { return 10; }
/*      */     
/*      */     public RelationPrimaryContext() {}
/*      */     
/*  764 */     public void copyFrom(RelationPrimaryContext ctx) { super.copyFrom(ctx); }
/*      */   }
/*      */   
/*      */   public static class TableNameContext
/*      */     extends ADQLParser.RelationPrimaryContext {
/*  769 */     public ADQLParser.QualifiedNameContext qualifiedName() { return (ADQLParser.QualifiedNameContext)getRuleContext(ADQLParser.QualifiedNameContext.class, 0); }
/*      */     
/*  771 */     public TableNameContext(ADQLParser.RelationPrimaryContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  774 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterTableName(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  778 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitTableName(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  782 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitTableName(this);
/*  783 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final RelationPrimaryContext relationPrimary() throws org.antlr.v4.runtime.RecognitionException {
/*  788 */     RelationPrimaryContext _localctx = new RelationPrimaryContext(this._ctx, getState());
/*  789 */     enterRule(_localctx, 20, 10);
/*      */     try {
/*  791 */       _localctx = new TableNameContext(_localctx);
/*  792 */       enterOuterAlt(_localctx, 1);
/*      */       
/*  794 */       setState(117);qualifiedName();
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/*  798 */       _localctx.exception = re;
/*  799 */       this._errHandler.reportError(this, re);
/*  800 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/*  803 */       exitRule();
/*      */     }
/*  805 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class ExpressionContext extends org.antlr.v4.runtime.ParserRuleContext {
/*      */     public ADQLParser.BooleanExpressionContext booleanExpression() {
/*  810 */       return (ADQLParser.BooleanExpressionContext)getRuleContext(ADQLParser.BooleanExpressionContext.class, 0);
/*      */     }
/*      */     
/*  813 */     public ExpressionContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/*  815 */     public int getRuleIndex() { return 11; }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  818 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterExpression(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  822 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitExpression(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  826 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitExpression(this);
/*  827 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final ExpressionContext expression() throws org.antlr.v4.runtime.RecognitionException {
/*  832 */     ExpressionContext _localctx = new ExpressionContext(this._ctx, getState());
/*  833 */     enterRule(_localctx, 22, 11);
/*      */     try {
/*  835 */       enterOuterAlt(_localctx, 1);
/*      */       
/*  837 */       setState(119);booleanExpression(0);
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/*  841 */       _localctx.exception = re;
/*  842 */       this._errHandler.reportError(this, re);
/*  843 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/*  846 */       exitRule();
/*      */     }
/*  848 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class BooleanExpressionContext
/*      */     extends org.antlr.v4.runtime.ParserRuleContext {
/*  853 */     public BooleanExpressionContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/*  855 */     public int getRuleIndex() { return 12; }
/*      */     
/*      */     public BooleanExpressionContext() {}
/*      */     
/*  859 */     public void copyFrom(BooleanExpressionContext ctx) { super.copyFrom(ctx); }
/*      */   }
/*      */   
/*      */   public static class LogicalBinaryContext extends ADQLParser.BooleanExpressionContext {
/*      */     public ADQLParser.BooleanExpressionContext left;
/*      */     public org.antlr.v4.runtime.Token operator;
/*      */     public ADQLParser.BooleanExpressionContext right;
/*      */     
/*  867 */     public ADQLParser.BooleanExpressionContext booleanExpression(int i) { return (ADQLParser.BooleanExpressionContext)getRuleContext(ADQLParser.BooleanExpressionContext.class, i); }
/*      */     
/*  869 */     public org.antlr.v4.runtime.tree.TerminalNode AND() { return getToken(22, 0); }
/*  870 */     public org.antlr.v4.runtime.tree.TerminalNode OR() { return getToken(21, 0); }
/*      */     
/*  872 */     public java.util.List<ADQLParser.BooleanExpressionContext> booleanExpression() { return getRuleContexts(ADQLParser.BooleanExpressionContext.class); }
/*      */     
/*  874 */     public LogicalBinaryContext(ADQLParser.BooleanExpressionContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  877 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterLogicalBinary(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  881 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitLogicalBinary(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  885 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitLogicalBinary(this);
/*  886 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class BooleanDefaultContext extends ADQLParser.BooleanExpressionContext {
/*  891 */     public ADQLParser.PredicatedContext predicated() { return (ADQLParser.PredicatedContext)getRuleContext(ADQLParser.PredicatedContext.class, 0); }
/*      */     
/*  893 */     public BooleanDefaultContext(ADQLParser.BooleanExpressionContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  896 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterBooleanDefault(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  900 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitBooleanDefault(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  904 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitBooleanDefault(this);
/*  905 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class LogicalNotContext extends ADQLParser.BooleanExpressionContext {
/*  910 */     public ADQLParser.BooleanExpressionContext booleanExpression() { return (ADQLParser.BooleanExpressionContext)getRuleContext(ADQLParser.BooleanExpressionContext.class, 0); }
/*      */     
/*  912 */     public org.antlr.v4.runtime.tree.TerminalNode NOT() { return getToken(24, 0); }
/*  913 */     public LogicalNotContext(ADQLParser.BooleanExpressionContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  916 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterLogicalNot(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  920 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitLogicalNot(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  924 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitLogicalNot(this);
/*  925 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ParenthesizedExpressionContext extends ADQLParser.BooleanExpressionContext {
/*  930 */     public ADQLParser.ExpressionContext expression() { return (ADQLParser.ExpressionContext)getRuleContext(ADQLParser.ExpressionContext.class, 0); }
/*      */     
/*  932 */     public ParenthesizedExpressionContext(ADQLParser.BooleanExpressionContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/*  935 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterParenthesizedExpression(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/*  939 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitParenthesizedExpression(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/*  943 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitParenthesizedExpression(this);
/*  944 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final BooleanExpressionContext booleanExpression() throws org.antlr.v4.runtime.RecognitionException {
/*  949 */     return booleanExpression(0);
/*      */   }
/*      */   
/*      */   private BooleanExpressionContext booleanExpression(int _p) throws org.antlr.v4.runtime.RecognitionException {
/*  953 */     org.antlr.v4.runtime.ParserRuleContext _parentctx = this._ctx;
/*  954 */     int _parentState = getState();
/*  955 */     BooleanExpressionContext _localctx = new BooleanExpressionContext(this._ctx, _parentState);
/*  956 */     BooleanExpressionContext _prevctx = _localctx;
/*  957 */     int _startState = 24;
/*  958 */     enterRecursionRule(_localctx, 24, 12, _p);
/*      */     try
/*      */     {
/*  961 */       enterOuterAlt(_localctx, 1);
/*      */       
/*  963 */       setState(129);
/*  964 */       switch (this._input.LA(1))
/*      */       {
/*      */       case 24: 
/*  967 */         _localctx = new LogicalNotContext(_localctx);
/*  968 */         this._ctx = _localctx;
/*  969 */         _prevctx = _localctx;
/*      */         
/*  971 */         setState(122);match(24);
/*  972 */         setState(123);booleanExpression(4);
/*      */         
/*  974 */         break;
/*      */       
/*      */       case 18: 
/*      */       case 19: 
/*      */       case 20: 
/*      */       case 30: 
/*      */       case 31: 
/*      */       case 40: 
/*      */       case 41: 
/*      */       case 42: 
/*      */       case 43: 
/*      */       case 44: 
/*      */       case 45: 
/*      */       case 46: 
/*      */       case 47: 
/*      */       case 48: 
/*      */       case 49: 
/*      */       case 72: 
/*      */       case 73: 
/*      */       case 74: 
/*      */       case 75: 
/*      */       case 77: 
/*      */       case 78: 
/*      */       case 79: 
/*      */       case 80: 
/*      */       case 86: 
/*      */       case 87: 
/*      */       case 92: 
/*      */       case 93: 
/*      */       case 94: 
/*      */       case 95: 
/*      */       case 96: 
/*      */       case 97: 
/*      */       case 98: 
/*      */       case 99: 
/*      */       case 102: 
/*      */       case 103: 
/*      */       case 104: 
/*      */       case 105: 
/*      */       case 106: 
/*      */       case 107: 
/*      */       case 108: 
/*      */       case 109: 
/*      */       case 114: 
/*      */       case 115: 
/*      */       case 116: 
/*      */       case 117: 
/*      */       case 118: 
/*      */       case 119: 
/*      */       case 125: 
/*      */       case 126: 
/*      */       case 127: 
/*      */       case 128: 
/*      */       case 129: 
/*      */       case 130: 
/*      */       case 139: 
/*      */       case 143: 
/*      */       case 144: 
/*      */       case 147: 
/*      */       case 148: 
/*      */       case 149: 
/*      */       case 150: 
/*      */       case 151: 
/* 1037 */         _localctx = new BooleanDefaultContext(_localctx);
/* 1038 */         this._ctx = _localctx;
/* 1039 */         _prevctx = _localctx;
/* 1040 */         setState(124);predicated();
/*      */         
/* 1042 */         break;
/*      */       
/*      */       case 4: 
/* 1045 */         _localctx = new ParenthesizedExpressionContext(_localctx);
/* 1046 */         this._ctx = _localctx;
/* 1047 */         _prevctx = _localctx;
/* 1048 */         setState(125);match(4);
/* 1049 */         setState(126);expression();
/* 1050 */         setState(127);match(2);
/*      */         
/* 1052 */         break;
/*      */       case 5: case 6: case 7: case 8: case 9: case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17: case 21: case 22: case 23: case 25: case 26: case 27: case 28: case 29: case 32: case 33: case 34: case 35: case 36: case 37: case 38: case 39: case 50: case 51: case 52: case 53: case 54: case 55: case 56: case 57: case 58: case 59: case 60: case 61: case 62: case 63: case 64: case 65: case 66: case 67: case 68: case 69: case 70: case 71: case 76: case 81: case 82: case 83: case 84: case 85: case 88: case 89: case 90: case 91: case 100: case 101: case 110: case 111: case 112: case 113: case 120: case 121: case 122: case 123: case 124: case 131: case 132: case 133: case 134: case 135: case 136: case 137: case 138: case 140: case 141: case 142: case 145: case 146: default: 
/* 1054 */         throw new org.antlr.v4.runtime.NoViableAltException(this);
/*      */       }
/* 1056 */       this._ctx.stop = this._input.LT(-1);
/* 1057 */       setState(139);
/* 1058 */       this._errHandler.sync(this);
/* 1059 */       int _alt = ((org.antlr.v4.runtime.atn.ParserATNSimulator)getInterpreter()).adaptivePredict(this._input, 11, this._ctx);
/* 1060 */       while ((_alt != 2) && (_alt != 0)) {
/* 1061 */         if (_alt == 1) {
/* 1062 */           if (this._parseListeners != null) triggerExitRuleEvent();
/* 1063 */           _prevctx = _localctx;
/*      */           
/* 1065 */           setState(137);
/* 1066 */           switch (((org.antlr.v4.runtime.atn.ParserATNSimulator)getInterpreter()).adaptivePredict(this._input, 10, this._ctx))
/*      */           {
/*      */           case 1: 
/* 1069 */             _localctx = new LogicalBinaryContext(new BooleanExpressionContext(_parentctx, _parentState));
/* 1070 */             ((LogicalBinaryContext)_localctx).left = _prevctx;
/* 1071 */             pushNewRecursionContext(_localctx, _startState, 12);
/* 1072 */             setState(131);
/* 1073 */             if (!precpred(this._ctx, 3)) throw new org.antlr.v4.runtime.FailedPredicateException(this, "precpred(_ctx, 3)");
/* 1074 */             setState(132);((LogicalBinaryContext)_localctx).operator = match(22);
/* 1075 */             setState(133);((LogicalBinaryContext)_localctx).right = booleanExpression(4);
/*      */             
/* 1077 */             break;
/*      */           
/*      */ 
/*      */           case 2: 
/* 1081 */             _localctx = new LogicalBinaryContext(new BooleanExpressionContext(_parentctx, _parentState));
/* 1082 */             ((LogicalBinaryContext)_localctx).left = _prevctx;
/* 1083 */             pushNewRecursionContext(_localctx, _startState, 12);
/* 1084 */             setState(134);
/* 1085 */             if (!precpred(this._ctx, 2)) throw new org.antlr.v4.runtime.FailedPredicateException(this, "precpred(_ctx, 2)");
/* 1086 */             setState(135);((LogicalBinaryContext)_localctx).operator = match(21);
/* 1087 */             setState(136);((LogicalBinaryContext)_localctx).right = booleanExpression(3);
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */ 
/* 1093 */         setState(141);
/* 1094 */         this._errHandler.sync(this);
/* 1095 */         _alt = ((org.antlr.v4.runtime.atn.ParserATNSimulator)getInterpreter()).adaptivePredict(this._input, 11, this._ctx);
/*      */       }
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/* 1100 */       _localctx.exception = re;
/* 1101 */       this._errHandler.reportError(this, re);
/* 1102 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 1105 */       unrollRecursionContexts(_parentctx);
/*      */     }
/* 1107 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class PredicatedContext
/*      */     extends org.antlr.v4.runtime.ParserRuleContext {
/* 1112 */     public PredicatedContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/* 1114 */     public int getRuleIndex() { return 13; }
/*      */     
/*      */     public PredicatedContext() {}
/*      */     
/* 1118 */     public void copyFrom(PredicatedContext ctx) { super.copyFrom(ctx); }
/*      */   }
/*      */   
/*      */   public static class ColumnPredicatedContext extends ADQLParser.PredicatedContext {
/*      */     public ADQLParser.ColumnNameContext columnName;
/*      */     
/* 1124 */     public ADQLParser.ColumnNameContext columnName() { return (ADQLParser.ColumnNameContext)getRuleContext(ADQLParser.ColumnNameContext.class, 0); }
/*      */     
/*      */ 
/* 1127 */     public ADQLParser.PredicateContext predicate() { return (ADQLParser.PredicateContext)getRuleContext(ADQLParser.PredicateContext.class, 0); }
/*      */     
/* 1129 */     public ColumnPredicatedContext(ADQLParser.PredicatedContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1132 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterColumnPredicated(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1136 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitColumnPredicated(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1140 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitColumnPredicated(this);
/* 1141 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class MatchContext extends ADQLParser.PredicatedContext {
/* 1146 */     public ADQLParser.MatchStringContext matchString() { return (ADQLParser.MatchStringContext)getRuleContext(ADQLParser.MatchStringContext.class, 0); }
/*      */     
/* 1148 */     public MatchContext(ADQLParser.PredicatedContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1151 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterMatch(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1155 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitMatch(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1159 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitMatch(this);
/* 1160 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class SingleBooleanLiteralContext extends ADQLParser.PredicatedContext {
/* 1165 */     public ADQLParser.BooleanValueContext booleanValue() { return (ADQLParser.BooleanValueContext)getRuleContext(ADQLParser.BooleanValueContext.class, 0); }
/*      */     
/* 1167 */     public SingleBooleanLiteralContext(ADQLParser.PredicatedContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1170 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterSingleBooleanLiteral(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1174 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitSingleBooleanLiteral(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1178 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitSingleBooleanLiteral(this);
/* 1179 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final PredicatedContext predicated() throws org.antlr.v4.runtime.RecognitionException {
/* 1184 */     PredicatedContext _localctx = new PredicatedContext(this._ctx, getState());
/* 1185 */     enterRule(_localctx, 26, 13);
/*      */     try {
/* 1187 */       setState(147);
/* 1188 */       switch (((org.antlr.v4.runtime.atn.ParserATNSimulator)getInterpreter()).adaptivePredict(this._input, 12, this._ctx)) {
/*      */       case 1: 
/* 1190 */         _localctx = new ColumnPredicatedContext(_localctx);
/* 1191 */         enterOuterAlt(_localctx, 1);
/*      */         
/* 1193 */         setState(142);((ColumnPredicatedContext)_localctx).columnName = columnName();
/* 1194 */         setState(143);predicate(((ColumnPredicatedContext)_localctx).columnName);
/*      */         
/* 1196 */         break;
/*      */       
/*      */       case 2: 
/* 1199 */         _localctx = new MatchContext(_localctx);
/* 1200 */         enterOuterAlt(_localctx, 2);
/*      */         
/* 1202 */         setState(145);matchString();
/*      */         
/* 1204 */         break;
/*      */       
/*      */       case 3: 
/* 1207 */         _localctx = new SingleBooleanLiteralContext(_localctx);
/* 1208 */         enterOuterAlt(_localctx, 3);
/*      */         
/* 1210 */         setState(146);booleanValue();
/*      */       }
/*      */       
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/* 1216 */       _localctx.exception = re;
/* 1217 */       this._errHandler.reportError(this, re);
/* 1218 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 1221 */       exitRule();
/*      */     }
/* 1223 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class PredicateContext extends org.antlr.v4.runtime.ParserRuleContext { public org.antlr.v4.runtime.ParserRuleContext value;
/*      */     
/* 1228 */     public PredicateContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/* 1230 */     public PredicateContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState, org.antlr.v4.runtime.ParserRuleContext value) { super(invokingState);
/* 1231 */       this.value = value; }
/*      */     
/* 1233 */     public int getRuleIndex() { return 14; }
/*      */     
/*      */     public PredicateContext() {}
/*      */     
/* 1237 */     public void copyFrom(PredicateContext ctx) { super.copyFrom(ctx);
/* 1238 */       this.value = ctx.value;
/*      */     } }
/*      */   
/*      */   public static class BetweenContext extends ADQLParser.PredicateContext { public ADQLParser.ValueExpressionContext lower;
/*      */     public ADQLParser.ValueExpressionContext upper;
/*      */     
/* 1244 */     public org.antlr.v4.runtime.tree.TerminalNode BETWEEN() { return getToken(26, 0); }
/* 1245 */     public org.antlr.v4.runtime.tree.TerminalNode AND() { return getToken(22, 0); }
/* 1246 */     public org.antlr.v4.runtime.tree.TerminalNode NOT() { return getToken(24, 0); }
/*      */     
/* 1248 */     public ADQLParser.ValueExpressionContext valueExpression(int i) { return (ADQLParser.ValueExpressionContext)getRuleContext(ADQLParser.ValueExpressionContext.class, i); }
/*      */     
/*      */ 
/* 1251 */     public java.util.List<ADQLParser.ValueExpressionContext> valueExpression() { return getRuleContexts(ADQLParser.ValueExpressionContext.class); }
/*      */     
/* 1253 */     public BetweenContext(ADQLParser.PredicateContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1256 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterBetween(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1260 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitBetween(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1264 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitBetween(this);
/* 1265 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ComparisonContext extends ADQLParser.PredicateContext { public ADQLParser.ValueExpressionContext right;
/*      */     
/* 1271 */     public ADQLParser.ValueExpressionContext valueExpression() { return (ADQLParser.ValueExpressionContext)getRuleContext(ADQLParser.ValueExpressionContext.class, 0); }
/*      */     
/*      */ 
/* 1274 */     public ADQLParser.ComparisonOperatorContext comparisonOperator() { return (ADQLParser.ComparisonOperatorContext)getRuleContext(ADQLParser.ComparisonOperatorContext.class, 0); }
/*      */     
/* 1276 */     public ComparisonContext(ADQLParser.PredicateContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1279 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterComparison(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1283 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitComparison(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1287 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitComparison(this);
/* 1288 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final PredicateContext predicate(org.antlr.v4.runtime.ParserRuleContext value) throws org.antlr.v4.runtime.RecognitionException {
/* 1293 */     PredicateContext _localctx = new PredicateContext(this._ctx, getState(), value);
/* 1294 */     enterRule(_localctx, 28, 14);
/*      */     try
/*      */     {
/* 1297 */       setState(160);
/* 1298 */       switch (this._input.LA(1)) {
/*      */       case 131: 
/*      */       case 132: 
/*      */       case 133: 
/*      */       case 134: 
/*      */       case 135: 
/*      */       case 136: 
/* 1305 */         _localctx = new ComparisonContext(_localctx);
/* 1306 */         enterOuterAlt(_localctx, 1);
/*      */         
/* 1308 */         setState(149);comparisonOperator();
/* 1309 */         setState(150);((ComparisonContext)_localctx).right = valueExpression();
/*      */         
/* 1311 */         break;
/*      */       case 24: 
/*      */       case 26: 
/* 1314 */         _localctx = new BetweenContext(_localctx);
/* 1315 */         enterOuterAlt(_localctx, 2);
/*      */         
/* 1317 */         setState(153);
/* 1318 */         int _la = this._input.LA(1);
/* 1319 */         if (_la == 24)
/*      */         {
/* 1321 */           setState(152);match(24);
/*      */         }
/*      */         
/*      */ 
/* 1325 */         setState(155);match(26);
/* 1326 */         setState(156);((BetweenContext)_localctx).lower = valueExpression();
/* 1327 */         setState(157);match(22);
/* 1328 */         setState(158);((BetweenContext)_localctx).upper = valueExpression();
/*      */         
/* 1330 */         break;
/*      */       default: 
/* 1332 */         throw new org.antlr.v4.runtime.NoViableAltException(this);
/*      */       }
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re) {
/* 1336 */       _localctx.exception = re;
/* 1337 */       this._errHandler.reportError(this, re);
/* 1338 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 1341 */       exitRule();
/*      */     }
/* 1343 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class ValueExpressionContext
/*      */     extends org.antlr.v4.runtime.ParserRuleContext {
/* 1348 */     public ValueExpressionContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/* 1350 */     public int getRuleIndex() { return 15; }
/*      */     
/*      */     public ValueExpressionContext() {}
/*      */     
/* 1354 */     public void copyFrom(ValueExpressionContext ctx) { super.copyFrom(ctx); }
/*      */   }
/*      */   
/*      */   public static class ValueExpressionDefaultContext
/*      */     extends ADQLParser.ValueExpressionContext {
/* 1359 */     public ADQLParser.PrimaryExpressionContext primaryExpression() { return (ADQLParser.PrimaryExpressionContext)getRuleContext(ADQLParser.PrimaryExpressionContext.class, 0); }
/*      */     
/* 1361 */     public ValueExpressionDefaultContext(ADQLParser.ValueExpressionContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1364 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterValueExpressionDefault(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1368 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitValueExpressionDefault(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1372 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitValueExpressionDefault(this);
/* 1373 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final ValueExpressionContext valueExpression() throws org.antlr.v4.runtime.RecognitionException {
/* 1378 */     ValueExpressionContext _localctx = new ValueExpressionContext(this._ctx, getState());
/* 1379 */     enterRule(_localctx, 30, 15);
/*      */     try {
/* 1381 */       _localctx = new ValueExpressionDefaultContext(_localctx);
/* 1382 */       enterOuterAlt(_localctx, 1);
/*      */       
/* 1384 */       setState(162);primaryExpression();
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/* 1388 */       _localctx.exception = re;
/* 1389 */       this._errHandler.reportError(this, re);
/* 1390 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 1393 */       exitRule();
/*      */     }
/* 1395 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class PrimaryExpressionContext
/*      */     extends org.antlr.v4.runtime.ParserRuleContext {
/* 1400 */     public PrimaryExpressionContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/* 1402 */     public int getRuleIndex() { return 16; }
/*      */     
/*      */     public PrimaryExpressionContext() {}
/*      */     
/* 1406 */     public void copyFrom(PrimaryExpressionContext ctx) { super.copyFrom(ctx); }
/*      */   }
/*      */   
/*      */   public static class NullLiteralContext extends ADQLParser.PrimaryExpressionContext {
/* 1410 */     public org.antlr.v4.runtime.tree.TerminalNode NULL() { return getToken(29, 0); }
/* 1411 */     public NullLiteralContext(ADQLParser.PrimaryExpressionContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1414 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterNullLiteral(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1418 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitNullLiteral(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1422 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitNullLiteral(this);
/* 1423 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/* 1427 */   public static class StringLiteralContext extends ADQLParser.PrimaryExpressionContext { public org.antlr.v4.runtime.tree.TerminalNode STRING() { return getToken(144, 0); }
/* 1428 */     public StringLiteralContext(ADQLParser.PrimaryExpressionContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1431 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterStringLiteral(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1435 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitStringLiteral(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1439 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitStringLiteral(this);
/* 1440 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class BooleanLiteralContext extends ADQLParser.PrimaryExpressionContext {
/* 1445 */     public ADQLParser.BooleanValueContext booleanValue() { return (ADQLParser.BooleanValueContext)getRuleContext(ADQLParser.BooleanValueContext.class, 0); }
/*      */     
/* 1447 */     public BooleanLiteralContext(ADQLParser.PrimaryExpressionContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1450 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterBooleanLiteral(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1454 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitBooleanLiteral(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1458 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitBooleanLiteral(this);
/* 1459 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class NumericLiteralContext extends ADQLParser.PrimaryExpressionContext {
/* 1464 */     public ADQLParser.NumberContext number() { return (ADQLParser.NumberContext)getRuleContext(ADQLParser.NumberContext.class, 0); }
/*      */     
/* 1466 */     public NumericLiteralContext(ADQLParser.PrimaryExpressionContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1469 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterNumericLiteral(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1473 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitNumericLiteral(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1477 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitNumericLiteral(this);
/* 1478 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final PrimaryExpressionContext primaryExpression() throws org.antlr.v4.runtime.RecognitionException {
/* 1483 */     PrimaryExpressionContext _localctx = new PrimaryExpressionContext(this._ctx, getState());
/* 1484 */     enterRule(_localctx, 32, 16);
/*      */     try {
/* 1486 */       setState(168);
/* 1487 */       switch (this._input.LA(1)) {
/*      */       case 29: 
/* 1489 */         _localctx = new NullLiteralContext(_localctx);
/* 1490 */         enterOuterAlt(_localctx, 1);
/*      */         
/* 1492 */         setState(164);match(29);
/*      */         
/* 1494 */         break;
/*      */       case 145: 
/*      */       case 146: 
/* 1497 */         _localctx = new NumericLiteralContext(_localctx);
/* 1498 */         enterOuterAlt(_localctx, 2);
/*      */         
/* 1500 */         setState(165);number();
/*      */         
/* 1502 */         break;
/*      */       case 30: 
/*      */       case 31: 
/* 1505 */         _localctx = new BooleanLiteralContext(_localctx);
/* 1506 */         enterOuterAlt(_localctx, 3);
/*      */         
/* 1508 */         setState(166);booleanValue();
/*      */         
/* 1510 */         break;
/*      */       case 144: 
/* 1512 */         _localctx = new StringLiteralContext(_localctx);
/* 1513 */         enterOuterAlt(_localctx, 4);
/*      */         
/* 1515 */         setState(167);match(144);
/*      */         
/* 1517 */         break;
/*      */       default: 
/* 1519 */         throw new org.antlr.v4.runtime.NoViableAltException(this);
/*      */       }
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re) {
/* 1523 */       _localctx.exception = re;
/* 1524 */       this._errHandler.reportError(this, re);
/* 1525 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 1528 */       exitRule();
/*      */     }
/* 1530 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class FunctionContext
/*      */     extends org.antlr.v4.runtime.ParserRuleContext {
/* 1535 */     public FunctionContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/* 1537 */     public int getRuleIndex() { return 17; }
/*      */     
/*      */     public FunctionContext() {}
/*      */     
/* 1541 */     public void copyFrom(FunctionContext ctx) { super.copyFrom(ctx); }
/*      */   }
/*      */   
/*      */   public static class FunctionCallContext extends ADQLParser.FunctionContext {
/* 1545 */     public org.antlr.v4.runtime.tree.TerminalNode ASTERISK() { return getToken(139, 0); }
/*      */     
/* 1547 */     public ADQLParser.QualifiedNameContext qualifiedName() { return (ADQLParser.QualifiedNameContext)getRuleContext(ADQLParser.QualifiedNameContext.class, 0); }
/*      */     
/*      */ 
/* 1550 */     public ADQLParser.ColumnNameContext columnName() { return (ADQLParser.ColumnNameContext)getRuleContext(ADQLParser.ColumnNameContext.class, 0); }
/*      */     
/* 1552 */     public FunctionCallContext(ADQLParser.FunctionContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1555 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterFunctionCall(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1559 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitFunctionCall(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1563 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitFunctionCall(this);
/* 1564 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final FunctionContext function() throws org.antlr.v4.runtime.RecognitionException {
/* 1569 */     FunctionContext _localctx = new FunctionContext(this._ctx, getState());
/* 1570 */     enterRule(_localctx, 34, 17);
/*      */     try {
/* 1572 */       setState(180);
/* 1573 */       switch (((org.antlr.v4.runtime.atn.ParserATNSimulator)getInterpreter()).adaptivePredict(this._input, 16, this._ctx)) {
/*      */       case 1: 
/* 1575 */         _localctx = new FunctionCallContext(_localctx);
/* 1576 */         enterOuterAlt(_localctx, 1);
/*      */         
/* 1578 */         setState(170);qualifiedName();
/* 1579 */         setState(171);match(4);
/* 1580 */         setState(172);match(139);
/* 1581 */         setState(173);match(2);
/*      */         
/* 1583 */         break;
/*      */       
/*      */       case 2: 
/* 1586 */         _localctx = new FunctionCallContext(_localctx);
/* 1587 */         enterOuterAlt(_localctx, 2);
/*      */         
/* 1589 */         setState(175);qualifiedName();
/* 1590 */         setState(176);match(4);
/* 1591 */         setState(177);columnName();
/* 1592 */         setState(178);match(2);
/*      */       }
/*      */       
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/* 1598 */       _localctx.exception = re;
/* 1599 */       this._errHandler.reportError(this, re);
/* 1600 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 1603 */       exitRule();
/*      */     }
/* 1605 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class ColumnNameContext
/*      */     extends org.antlr.v4.runtime.ParserRuleContext {
/* 1610 */     public ColumnNameContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/* 1612 */     public int getRuleIndex() { return 18; }
/*      */     
/*      */     public ColumnNameContext() {}
/*      */     
/* 1616 */     public void copyFrom(ColumnNameContext ctx) { super.copyFrom(ctx); }
/*      */   }
/*      */   
/*      */   public static class ColumnReferenceContext
/*      */     extends ADQLParser.ColumnNameContext {
/* 1621 */     public ADQLParser.QualifiedNameContext qualifiedName() { return (ADQLParser.QualifiedNameContext)getRuleContext(ADQLParser.QualifiedNameContext.class, 0); }
/*      */     
/* 1623 */     public ColumnReferenceContext(ADQLParser.ColumnNameContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1626 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterColumnReference(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1630 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitColumnReference(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1634 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitColumnReference(this);
/* 1635 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final ColumnNameContext columnName() throws org.antlr.v4.runtime.RecognitionException {
/* 1640 */     ColumnNameContext _localctx = new ColumnNameContext(this._ctx, getState());
/* 1641 */     enterRule(_localctx, 36, 18);
/*      */     try {
/* 1643 */       _localctx = new ColumnReferenceContext(_localctx);
/* 1644 */       enterOuterAlt(_localctx, 1);
/*      */       
/* 1646 */       setState(182);qualifiedName();
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/* 1650 */       _localctx.exception = re;
/* 1651 */       this._errHandler.reportError(this, re);
/* 1652 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 1655 */       exitRule();
/*      */     }
/* 1657 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class MatchStringContext
/*      */     extends org.antlr.v4.runtime.ParserRuleContext {
/* 1662 */     public MatchStringContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/* 1664 */     public int getRuleIndex() { return 19; }
/*      */     
/*      */     public MatchStringContext() {}
/*      */     
/* 1668 */     public void copyFrom(MatchStringContext ctx) { super.copyFrom(ctx); }
/*      */   }
/*      */   
/*      */   public static class MatchStringReferenceContext
/*      */     extends ADQLParser.MatchStringContext {
/* 1673 */     public ADQLParser.PrimaryMatchStringContext primaryMatchString() { return (ADQLParser.PrimaryMatchStringContext)getRuleContext(ADQLParser.PrimaryMatchStringContext.class, 0); }
/*      */     
/* 1675 */     public MatchStringReferenceContext(ADQLParser.MatchStringContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1678 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterMatchStringReference(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1682 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitMatchStringReference(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1686 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitMatchStringReference(this);
/* 1687 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final MatchStringContext matchString() throws org.antlr.v4.runtime.RecognitionException {
/* 1692 */     MatchStringContext _localctx = new MatchStringContext(this._ctx, getState());
/* 1693 */     enterRule(_localctx, 38, 19);
/*      */     try {
/* 1695 */       _localctx = new MatchStringReferenceContext(_localctx);
/* 1696 */       enterOuterAlt(_localctx, 1);
/*      */       
/* 1698 */       setState(184);primaryMatchString();
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/* 1702 */       _localctx.exception = re;
/* 1703 */       this._errHandler.reportError(this, re);
/* 1704 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 1707 */       exitRule();
/*      */     }
/* 1709 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class PrimaryMatchStringContext extends org.antlr.v4.runtime.ParserRuleContext {
/* 1713 */     public org.antlr.v4.runtime.tree.TerminalNode ASTERISK() { return getToken(139, 0); }
/* 1714 */     public org.antlr.v4.runtime.tree.TerminalNode IDENTIFIER() { return getToken(148, 0); }
/* 1715 */     public org.antlr.v4.runtime.tree.TerminalNode WILDCARD() { return getToken(147, 0); }
/* 1716 */     public org.antlr.v4.runtime.tree.TerminalNode STRING() { return getToken(144, 0); }
/* 1717 */     public org.antlr.v4.runtime.tree.TerminalNode QUESTIONMARK() { return getToken(143, 0); }
/*      */     
/* 1719 */     public PrimaryMatchStringContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/* 1721 */     public int getRuleIndex() { return 20; }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1724 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterPrimaryMatchString(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1728 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitPrimaryMatchString(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1732 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitPrimaryMatchString(this);
/* 1733 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final PrimaryMatchStringContext primaryMatchString() throws org.antlr.v4.runtime.RecognitionException {
/* 1738 */     PrimaryMatchStringContext _localctx = new PrimaryMatchStringContext(this._ctx, getState());
/* 1739 */     enterRule(_localctx, 40, 20);
/*      */     try
/*      */     {
/* 1742 */       enterOuterAlt(_localctx, 1);
/*      */       
/* 1744 */       setState(186);
/* 1745 */       int _la = this._input.LA(1);
/* 1746 */       if (((_la - 139 & 0xFFFFFFC0) != 0) || ((1L << _la - 139 & 0x331) == 0L)) {
/* 1747 */         this._errHandler.recoverInline(this);
/*      */       }
/* 1749 */       consume();
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/* 1753 */       _localctx.exception = re;
/* 1754 */       this._errHandler.reportError(this, re);
/* 1755 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 1758 */       exitRule();
/*      */     }
/* 1760 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class ComparisonOperatorContext extends org.antlr.v4.runtime.ParserRuleContext {
/* 1764 */     public org.antlr.v4.runtime.tree.TerminalNode NEQ() { return getToken(132, 0); }
/* 1765 */     public org.antlr.v4.runtime.tree.TerminalNode GTE() { return getToken(136, 0); }
/* 1766 */     public org.antlr.v4.runtime.tree.TerminalNode LT() { return getToken(133, 0); }
/* 1767 */     public org.antlr.v4.runtime.tree.TerminalNode LTE() { return getToken(134, 0); }
/* 1768 */     public org.antlr.v4.runtime.tree.TerminalNode GT() { return getToken(135, 0); }
/* 1769 */     public org.antlr.v4.runtime.tree.TerminalNode EQ() { return getToken(131, 0); }
/*      */     
/* 1771 */     public ComparisonOperatorContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/* 1773 */     public int getRuleIndex() { return 21; }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1776 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterComparisonOperator(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1780 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitComparisonOperator(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1784 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitComparisonOperator(this);
/* 1785 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final ComparisonOperatorContext comparisonOperator() throws org.antlr.v4.runtime.RecognitionException {
/* 1790 */     ComparisonOperatorContext _localctx = new ComparisonOperatorContext(this._ctx, getState());
/* 1791 */     enterRule(_localctx, 42, 21);
/*      */     try
/*      */     {
/* 1794 */       enterOuterAlt(_localctx, 1);
/*      */       
/* 1796 */       setState(188);
/* 1797 */       int _la = this._input.LA(1);
/* 1798 */       if (((_la - 131 & 0xFFFFFFC0) != 0) || ((1L << _la - 131 & 0x3F) == 0L)) {
/* 1799 */         this._errHandler.recoverInline(this);
/*      */       }
/* 1801 */       consume();
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/* 1805 */       _localctx.exception = re;
/* 1806 */       this._errHandler.reportError(this, re);
/* 1807 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 1810 */       exitRule();
/*      */     }
/* 1812 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class BooleanValueContext extends org.antlr.v4.runtime.ParserRuleContext {
/* 1816 */     public org.antlr.v4.runtime.tree.TerminalNode TRUE() { return getToken(30, 0); }
/* 1817 */     public org.antlr.v4.runtime.tree.TerminalNode FALSE() { return getToken(31, 0); }
/*      */     
/* 1819 */     public BooleanValueContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/* 1821 */     public int getRuleIndex() { return 22; }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1824 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterBooleanValue(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1828 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitBooleanValue(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1832 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitBooleanValue(this);
/* 1833 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final BooleanValueContext booleanValue() throws org.antlr.v4.runtime.RecognitionException {
/* 1838 */     BooleanValueContext _localctx = new BooleanValueContext(this._ctx, getState());
/* 1839 */     enterRule(_localctx, 44, 22);
/*      */     try
/*      */     {
/* 1842 */       enterOuterAlt(_localctx, 1);
/*      */       
/* 1844 */       setState(190);
/* 1845 */       int _la = this._input.LA(1);
/* 1846 */       if ((_la != 30) && (_la != 31)) {
/* 1847 */         this._errHandler.recoverInline(this);
/*      */       }
/* 1849 */       consume();
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/* 1853 */       _localctx.exception = re;
/* 1854 */       this._errHandler.reportError(this, re);
/* 1855 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 1858 */       exitRule();
/*      */     }
/* 1860 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class QualifiedNameContext extends org.antlr.v4.runtime.ParserRuleContext {
/*      */     public ADQLParser.IdentifierContext identifier(int i) {
/* 1865 */       return (ADQLParser.IdentifierContext)getRuleContext(ADQLParser.IdentifierContext.class, i);
/*      */     }
/*      */     
/* 1868 */     public java.util.List<ADQLParser.IdentifierContext> identifier() { return getRuleContexts(ADQLParser.IdentifierContext.class); }
/*      */     
/*      */ 
/* 1871 */     public QualifiedNameContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/* 1873 */     public int getRuleIndex() { return 23; }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1876 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterQualifiedName(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1880 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitQualifiedName(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1884 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitQualifiedName(this);
/* 1885 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final QualifiedNameContext qualifiedName() throws org.antlr.v4.runtime.RecognitionException {
/* 1890 */     QualifiedNameContext _localctx = new QualifiedNameContext(this._ctx, getState());
/* 1891 */     enterRule(_localctx, 46, 23);
/*      */     try
/*      */     {
/* 1894 */       enterOuterAlt(_localctx, 1);
/*      */       
/* 1896 */       setState(192);identifier();
/* 1897 */       setState(197);
/* 1898 */       this._errHandler.sync(this);
/* 1899 */       int _alt = ((org.antlr.v4.runtime.atn.ParserATNSimulator)getInterpreter()).adaptivePredict(this._input, 17, this._ctx);
/* 1900 */       while ((_alt != 2) && (_alt != 0)) {
/* 1901 */         if (_alt == 1)
/*      */         {
/*      */ 
/* 1904 */           setState(193);match(1);
/* 1905 */           setState(194);identifier();
/*      */         }
/*      */         
/*      */ 
/* 1909 */         setState(199);
/* 1910 */         this._errHandler.sync(this);
/* 1911 */         _alt = ((org.antlr.v4.runtime.atn.ParserATNSimulator)getInterpreter()).adaptivePredict(this._input, 17, this._ctx);
/*      */       }
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/* 1916 */       _localctx.exception = re;
/* 1917 */       this._errHandler.reportError(this, re);
/* 1918 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 1921 */       exitRule();
/*      */     }
/* 1923 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class IdentifierContext
/*      */     extends org.antlr.v4.runtime.ParserRuleContext {
/* 1928 */     public IdentifierContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/* 1930 */     public int getRuleIndex() { return 24; }
/*      */     
/*      */     public IdentifierContext() {}
/*      */     
/* 1934 */     public void copyFrom(IdentifierContext ctx) { super.copyFrom(ctx); }
/*      */   }
/*      */   
/*      */   public static class UnquotedIdentifierContext
/*      */     extends ADQLParser.IdentifierContext {
/* 1939 */     public ADQLParser.NonReservedContext nonReserved() { return (ADQLParser.NonReservedContext)getRuleContext(ADQLParser.NonReservedContext.class, 0); }
/*      */     
/* 1941 */     public org.antlr.v4.runtime.tree.TerminalNode IDENTIFIER() { return getToken(148, 0); }
/* 1942 */     public UnquotedIdentifierContext(ADQLParser.IdentifierContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1945 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterUnquotedIdentifier(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1949 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitUnquotedIdentifier(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1953 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitUnquotedIdentifier(this);
/* 1954 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/* 1958 */   public static class DigitIdentifierContext extends ADQLParser.IdentifierContext { public org.antlr.v4.runtime.tree.TerminalNode DIGIT_IDENTIFIER() { return getToken(149, 0); }
/* 1959 */     public DigitIdentifierContext(ADQLParser.IdentifierContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1962 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterDigitIdentifier(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1966 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitDigitIdentifier(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1970 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitDigitIdentifier(this);
/* 1971 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class QuotedIdentifierAlternativeContext extends ADQLParser.IdentifierContext {
/* 1976 */     public ADQLParser.QuotedIdentifierContext quotedIdentifier() { return (ADQLParser.QuotedIdentifierContext)getRuleContext(ADQLParser.QuotedIdentifierContext.class, 0); }
/*      */     
/* 1978 */     public QuotedIdentifierAlternativeContext(ADQLParser.IdentifierContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1981 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterQuotedIdentifierAlternative(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 1985 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitQuotedIdentifierAlternative(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 1989 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitQuotedIdentifierAlternative(this);
/* 1990 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/* 1994 */   public static class BackQuotedIdentifierContext extends ADQLParser.IdentifierContext { public org.antlr.v4.runtime.tree.TerminalNode BACKQUOTED_IDENTIFIER() { return getToken(151, 0); }
/* 1995 */     public BackQuotedIdentifierContext(ADQLParser.IdentifierContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 1998 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterBackQuotedIdentifier(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 2002 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitBackQuotedIdentifier(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 2006 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitBackQuotedIdentifier(this);
/* 2007 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final IdentifierContext identifier() throws org.antlr.v4.runtime.RecognitionException {
/* 2012 */     IdentifierContext _localctx = new IdentifierContext(this._ctx, getState());
/* 2013 */     enterRule(_localctx, 48, 24);
/*      */     try {
/* 2015 */       setState(205);
/* 2016 */       switch (this._input.LA(1)) {
/*      */       case 148: 
/* 2018 */         _localctx = new UnquotedIdentifierContext(_localctx);
/* 2019 */         enterOuterAlt(_localctx, 1);
/*      */         
/* 2021 */         setState(200);match(148);
/*      */         
/* 2023 */         break;
/*      */       case 150: 
/* 2025 */         _localctx = new QuotedIdentifierAlternativeContext(_localctx);
/* 2026 */         enterOuterAlt(_localctx, 2);
/*      */         
/* 2028 */         setState(201);quotedIdentifier();
/*      */         
/* 2030 */         break;
/*      */       case 18: 
/*      */       case 19: 
/*      */       case 20: 
/*      */       case 40: 
/*      */       case 41: 
/*      */       case 42: 
/*      */       case 43: 
/*      */       case 44: 
/*      */       case 45: 
/*      */       case 46: 
/*      */       case 47: 
/*      */       case 48: 
/*      */       case 49: 
/*      */       case 72: 
/*      */       case 73: 
/*      */       case 74: 
/*      */       case 75: 
/*      */       case 77: 
/*      */       case 78: 
/*      */       case 79: 
/*      */       case 80: 
/*      */       case 86: 
/*      */       case 87: 
/*      */       case 92: 
/*      */       case 93: 
/*      */       case 94: 
/*      */       case 95: 
/*      */       case 96: 
/*      */       case 97: 
/*      */       case 98: 
/*      */       case 99: 
/*      */       case 102: 
/*      */       case 103: 
/*      */       case 104: 
/*      */       case 105: 
/*      */       case 106: 
/*      */       case 107: 
/*      */       case 108: 
/*      */       case 109: 
/*      */       case 114: 
/*      */       case 115: 
/*      */       case 116: 
/*      */       case 117: 
/*      */       case 118: 
/*      */       case 119: 
/*      */       case 125: 
/*      */       case 126: 
/*      */       case 127: 
/*      */       case 128: 
/*      */       case 129: 
/*      */       case 130: 
/* 2082 */         _localctx = new UnquotedIdentifierContext(_localctx);
/* 2083 */         enterOuterAlt(_localctx, 3);
/*      */         
/* 2085 */         setState(202);nonReserved();
/*      */         
/* 2087 */         break;
/*      */       case 151: 
/* 2089 */         _localctx = new BackQuotedIdentifierContext(_localctx);
/* 2090 */         enterOuterAlt(_localctx, 4);
/*      */         
/* 2092 */         setState(203);match(151);
/*      */         
/* 2094 */         break;
/*      */       case 149: 
/* 2096 */         _localctx = new DigitIdentifierContext(_localctx);
/* 2097 */         enterOuterAlt(_localctx, 5);
/*      */         
/* 2099 */         setState(204);match(149);
/*      */         
/* 2101 */         break;
/*      */       case 21: case 22: case 23: case 24: case 25: case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33: case 34: case 35: case 36: case 37: case 38: case 39: case 50: case 51: case 52: case 53: case 54: case 55: case 56: case 57: case 58: case 59: case 60: case 61: case 62: case 63: case 64: case 65: case 66: case 67: case 68: case 69: case 70: case 71: case 76: case 81: case 82: case 83: case 84: case 85: case 88: case 89: case 90: case 91: case 100: case 101: case 110: case 111: case 112: case 113: case 120: case 121: case 122: case 123: case 124: case 131: case 132: case 133: case 134: case 135: case 136: case 137: case 138: case 139: case 140: case 141: case 142: case 143: case 144: case 145: case 146: case 147: default: 
/* 2103 */         throw new org.antlr.v4.runtime.NoViableAltException(this);
/*      */       }
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re) {
/* 2107 */       _localctx.exception = re;
/* 2108 */       this._errHandler.reportError(this, re);
/* 2109 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 2112 */       exitRule();
/*      */     }
/* 2114 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class QuotedIdentifierContext extends org.antlr.v4.runtime.ParserRuleContext {
/* 2118 */     public org.antlr.v4.runtime.tree.TerminalNode QUOTED_IDENTIFIER() { return getToken(150, 0); }
/*      */     
/* 2120 */     public QuotedIdentifierContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/* 2122 */     public int getRuleIndex() { return 25; }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 2125 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterQuotedIdentifier(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 2129 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitQuotedIdentifier(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 2133 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitQuotedIdentifier(this);
/* 2134 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final QuotedIdentifierContext quotedIdentifier() throws org.antlr.v4.runtime.RecognitionException {
/* 2139 */     QuotedIdentifierContext _localctx = new QuotedIdentifierContext(this._ctx, getState());
/* 2140 */     enterRule(_localctx, 50, 25);
/*      */     try {
/* 2142 */       enterOuterAlt(_localctx, 1);
/*      */       
/* 2144 */       setState(207);match(150);
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/* 2148 */       _localctx.exception = re;
/* 2149 */       this._errHandler.reportError(this, re);
/* 2150 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 2153 */       exitRule();
/*      */     }
/* 2155 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class NumberContext
/*      */     extends org.antlr.v4.runtime.ParserRuleContext {
/* 2160 */     public NumberContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/* 2162 */     public int getRuleIndex() { return 26; }
/*      */     
/*      */     public NumberContext() {}
/*      */     
/* 2166 */     public void copyFrom(NumberContext ctx) { super.copyFrom(ctx); }
/*      */   }
/*      */   
/*      */   public static class DecimalLiteralContext extends ADQLParser.NumberContext {
/* 2170 */     public org.antlr.v4.runtime.tree.TerminalNode DECIMAL_VALUE() { return getToken(146, 0); }
/* 2171 */     public DecimalLiteralContext(ADQLParser.NumberContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 2174 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterDecimalLiteral(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 2178 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitDecimalLiteral(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 2182 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitDecimalLiteral(this);
/* 2183 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/* 2187 */   public static class IntegerLiteralContext extends ADQLParser.NumberContext { public org.antlr.v4.runtime.tree.TerminalNode INTEGER_VALUE() { return getToken(145, 0); }
/* 2188 */     public IntegerLiteralContext(ADQLParser.NumberContext ctx) { copyFrom(ctx); }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 2191 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterIntegerLiteral(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 2195 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitIntegerLiteral(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 2199 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitIntegerLiteral(this);
/* 2200 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final NumberContext number() throws org.antlr.v4.runtime.RecognitionException {
/* 2205 */     NumberContext _localctx = new NumberContext(this._ctx, getState());
/* 2206 */     enterRule(_localctx, 52, 26);
/*      */     try {
/* 2208 */       setState(211);
/* 2209 */       switch (this._input.LA(1)) {
/*      */       case 146: 
/* 2211 */         _localctx = new DecimalLiteralContext(_localctx);
/* 2212 */         enterOuterAlt(_localctx, 1);
/*      */         
/* 2214 */         setState(209);match(146);
/*      */         
/* 2216 */         break;
/*      */       case 145: 
/* 2218 */         _localctx = new IntegerLiteralContext(_localctx);
/* 2219 */         enterOuterAlt(_localctx, 2);
/*      */         
/* 2221 */         setState(210);match(145);
/*      */         
/* 2223 */         break;
/*      */       default: 
/* 2225 */         throw new org.antlr.v4.runtime.NoViableAltException(this);
/*      */       }
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re) {
/* 2229 */       _localctx.exception = re;
/* 2230 */       this._errHandler.reportError(this, re);
/* 2231 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 2234 */       exitRule();
/*      */     }
/* 2236 */     return _localctx;
/*      */   }
/*      */   
/*      */   public static class NonReservedContext extends org.antlr.v4.runtime.ParserRuleContext {
/* 2240 */     public org.antlr.v4.runtime.tree.TerminalNode TIMESTAMP() { return getToken(42, 0); }
/* 2241 */     public org.antlr.v4.runtime.tree.TerminalNode SYSTEM() { return getToken(115, 0); }
/* 2242 */     public org.antlr.v4.runtime.tree.TerminalNode RESET() { return getToken(126, 0); }
/* 2243 */     public org.antlr.v4.runtime.tree.TerminalNode COALESCE() { return getToken(130, 0); }
/* 2244 */     public org.antlr.v4.runtime.tree.TerminalNode VIEW() { return getToken(86, 0); }
/* 2245 */     public org.antlr.v4.runtime.tree.TerminalNode NULLIF() { return getToken(129, 0); }
/* 2246 */     public org.antlr.v4.runtime.tree.TerminalNode BERNOULLI() { return getToken(116, 0); }
/* 2247 */     public org.antlr.v4.runtime.tree.TerminalNode TIME() { return getToken(41, 0); }
/* 2248 */     public org.antlr.v4.runtime.tree.TerminalNode TABLESAMPLE() { return getToken(118, 0); }
/* 2249 */     public org.antlr.v4.runtime.tree.TerminalNode SECOND() { return getToken(49, 0); }
/* 2250 */     public org.antlr.v4.runtime.tree.TerminalNode LOGICAL() { return getToken(98, 0); }
/* 2251 */     public org.antlr.v4.runtime.tree.TerminalNode FUNCTIONS() { return getToken(109, 0); }
/* 2252 */     public org.antlr.v4.runtime.tree.TerminalNode IF() { return getToken(128, 0); }
/* 2253 */     public org.antlr.v4.runtime.tree.TerminalNode YEAR() { return getToken(44, 0); }
/* 2254 */     public org.antlr.v4.runtime.tree.TerminalNode OVER() { return getToken(72, 0); }
/* 2255 */     public org.antlr.v4.runtime.tree.TerminalNode USE() { return getToken(107, 0); }
/* 2256 */     public org.antlr.v4.runtime.tree.TerminalNode ROW() { return getToken(80, 0); }
/* 2257 */     public org.antlr.v4.runtime.tree.TerminalNode CONFIDENCE() { return getToken(20, 0); }
/* 2258 */     public org.antlr.v4.runtime.tree.TerminalNode PARTITION() { return getToken(73, 0); }
/* 2259 */     public org.antlr.v4.runtime.tree.TerminalNode TO() { return getToken(114, 0); }
/* 2260 */     public org.antlr.v4.runtime.tree.TerminalNode INTERVAL() { return getToken(43, 0); }
/* 2261 */     public org.antlr.v4.runtime.tree.TerminalNode POISSONIZED() { return getToken(117, 0); }
/* 2262 */     public org.antlr.v4.runtime.tree.TerminalNode TYPE() { return getToken(94, 0); }
/* 2263 */     public org.antlr.v4.runtime.tree.TerminalNode FORMAT() { return getToken(93, 0); }
/* 2264 */     public org.antlr.v4.runtime.tree.TerminalNode JSON() { return getToken(97, 0); }
/* 2265 */     public org.antlr.v4.runtime.tree.TerminalNode SCHEMAS() { return getToken(104, 0); }
/* 2266 */     public org.antlr.v4.runtime.tree.TerminalNode CURRENT() { return getToken(79, 0); }
/* 2267 */     public org.antlr.v4.runtime.tree.TerminalNode AT() { return getToken(19, 0); }
/* 2268 */     public org.antlr.v4.runtime.tree.TerminalNode GRAPHVIZ() { return getToken(96, 0); }
/* 2269 */     public org.antlr.v4.runtime.tree.TerminalNode MINUTE() { return getToken(48, 0); }
/* 2270 */     public org.antlr.v4.runtime.tree.TerminalNode FOLLOWING() { return getToken(78, 0); }
/* 2271 */     public org.antlr.v4.runtime.tree.TerminalNode SET() { return getToken(125, 0); }
/* 2272 */     public org.antlr.v4.runtime.tree.TerminalNode MONTH() { return getToken(45, 0); }
/* 2273 */     public org.antlr.v4.runtime.tree.TerminalNode TEXT() { return getToken(95, 0); }
/* 2274 */     public org.antlr.v4.runtime.tree.TerminalNode EXPLAIN() { return getToken(92, 0); }
/* 2275 */     public org.antlr.v4.runtime.tree.TerminalNode PRECEDING() { return getToken(77, 0); }
/* 2276 */     public org.antlr.v4.runtime.tree.TerminalNode DISTRIBUTED() { return getToken(99, 0); }
/* 2277 */     public org.antlr.v4.runtime.tree.TerminalNode CATALOGS() { return getToken(105, 0); }
/* 2278 */     public org.antlr.v4.runtime.tree.TerminalNode TABLES() { return getToken(103, 0); }
/* 2279 */     public org.antlr.v4.runtime.tree.TerminalNode SHOW() { return getToken(102, 0); }
/* 2280 */     public org.antlr.v4.runtime.tree.TerminalNode RESCALED() { return getToken(119, 0); }
/* 2281 */     public org.antlr.v4.runtime.tree.TerminalNode DAY() { return getToken(46, 0); }
/* 2282 */     public org.antlr.v4.runtime.tree.TerminalNode SESSION() { return getToken(127, 0); }
/* 2283 */     public org.antlr.v4.runtime.tree.TerminalNode COLUMNS() { return getToken(106, 0); }
/* 2284 */     public org.antlr.v4.runtime.tree.TerminalNode RANGE() { return getToken(74, 0); }
/* 2285 */     public org.antlr.v4.runtime.tree.TerminalNode DATE() { return getToken(40, 0); }
/* 2286 */     public org.antlr.v4.runtime.tree.TerminalNode ROWS() { return getToken(75, 0); }
/* 2287 */     public org.antlr.v4.runtime.tree.TerminalNode HOUR() { return getToken(47, 0); }
/* 2288 */     public org.antlr.v4.runtime.tree.TerminalNode APPROXIMATE() { return getToken(18, 0); }
/* 2289 */     public org.antlr.v4.runtime.tree.TerminalNode REPLACE() { return getToken(87, 0); }
/* 2290 */     public org.antlr.v4.runtime.tree.TerminalNode PARTITIONS() { return getToken(108, 0); }
/*      */     
/* 2292 */     public NonReservedContext(org.antlr.v4.runtime.ParserRuleContext parent, int invokingState) { super(invokingState); }
/*      */     
/* 2294 */     public int getRuleIndex() { return 27; }
/*      */     
/*      */     public void enterRule(ParseTreeListener listener) {
/* 2297 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).enterNonReserved(this);
/*      */     }
/*      */     
/*      */     public void exitRule(ParseTreeListener listener) {
/* 2301 */       if ((listener instanceof ADQLListener)) ((ADQLListener)listener).exitNonReserved(this);
/*      */     }
/*      */     
/*      */     public <T> T accept(org.antlr.v4.runtime.tree.ParseTreeVisitor<? extends T> visitor) {
/* 2305 */       if ((visitor instanceof ADQLVisitor)) return (T)((ADQLVisitor)visitor).visitNonReserved(this);
/* 2306 */       return (T)visitor.visitChildren(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final NonReservedContext nonReserved() throws org.antlr.v4.runtime.RecognitionException {
/* 2311 */     NonReservedContext _localctx = new NonReservedContext(this._ctx, getState());
/* 2312 */     enterRule(_localctx, 54, 27);
/*      */     try
/*      */     {
/* 2315 */       enterOuterAlt(_localctx, 1);
/*      */       
/* 2317 */       setState(213);
/* 2318 */       int _la = this._input.LA(1);
/* 2319 */       if ((((_la & 0xFFFFFFC0) != 0) || ((1L << _la & 0x3FF00001C0000) == 0L)) && (((_la - 72 & 0xFFFFFFC0) != 0) || ((1L << _la - 72 & 0x7E0FC3FCFF0C1EF) == 0L))) {
/* 2320 */         this._errHandler.recoverInline(this);
/*      */       }
/* 2322 */       consume();
/*      */     }
/*      */     catch (org.antlr.v4.runtime.RecognitionException re)
/*      */     {
/* 2326 */       _localctx.exception = re;
/* 2327 */       this._errHandler.reportError(this, re);
/* 2328 */       this._errHandler.recover(this, re);
/*      */     }
/*      */     finally {
/* 2331 */       exitRule();
/*      */     }
/* 2333 */     return _localctx;
/*      */   }
/*      */   
/*      */   public boolean sempred(org.antlr.v4.runtime.RuleContext _localctx, int ruleIndex, int predIndex) {
/* 2337 */     switch (ruleIndex) {
/* 2338 */     case 12:  return booleanExpression_sempred((BooleanExpressionContext)_localctx, predIndex);
/*      */     }
/* 2340 */     return true;
/*      */   }
/*      */   
/* 2343 */   private boolean booleanExpression_sempred(BooleanExpressionContext _localctx, int predIndex) { switch (predIndex) {
/* 2344 */     case 0:  return precpred(this._ctx, 3);
/*      */     case 1: 
/* 2346 */       return precpred(this._ctx, 2);
/*      */     }
/* 2348 */     return true;
/*      */   }
/*      */   
/*      */   static
/*      */   {
/*   14 */     org.antlr.v4.runtime.RuntimeMetaData.checkVersion("4.3", "4.3");
/*      */     
/*      */ 
/*   17 */     _sharedContextCache = new org.antlr.v4.runtime.atn.PredictionContextCache();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   45 */     tokenNames = new String[] { "<INVALID>", "'.'", "')'", "','", "'('", "'SELECT'", "'FROM'", "'AS'", "'ALL'", "'SOME'", "'ANY'", "'DISTINCT'", "'WHERE'", "'GROUP'", "'BY'", "'ORDER'", "'HAVING'", "'LIMIT'", "'APPROXIMATE'", "'AT'", "'CONFIDENCE'", "'OR'", "'AND'", "'IN'", "'NOT'", "'EXISTS'", "'BETWEEN'", "'LIKE'", "'IS'", "'NULL'", "'TRUE'", "'FALSE'", "'NULLS'", "'FIRST'", "'LAST'", "'ESCAPE'", "'ASC'", "'DESC'", "'SUBSTRING'", "'FOR'", "'DATE'", "'TIME'", "'TIMESTAMP'", "'INTERVAL'", "'YEAR'", "'MONTH'", "'DAY'", "'HOUR'", "'MINUTE'", "'SECOND'", "'ZONE'", "'CURRENT_DATE'", "'CURRENT_TIME'", "'CURRENT_TIMESTAMP'", "'LOCALTIME'", "'LOCALTIMESTAMP'", "'EXTRACT'", "'CASE'", "'WHEN'", "'THEN'", "'ELSE'", "'END'", "'JOIN'", "'CROSS'", "'OUTER'", "'INNER'", "'LEFT'", "'RIGHT'", "'FULL'", "'NATURAL'", "'USING'", "'ON'", "'OVER'", "'PARTITION'", "'RANGE'", "'ROWS'", "'UNBOUNDED'", "'PRECEDING'", "'FOLLOWING'", "'CURRENT'", "'ROW'", "'WITH'", "'RECURSIVE'", "'VALUES'", "'CREATE'", "'TABLE'", "'VIEW'", "'REPLACE'", "'INSERT'", "'INTO'", "'CONSTRAINT'", "'DESCRIBE'", "'EXPLAIN'", "'FORMAT'", "'TYPE'", "'TEXT'", "'GRAPHVIZ'", "'JSON'", "'LOGICAL'", "'DISTRIBUTED'", "'CAST'", "'TRY_CAST'", "'SHOW'", "'TABLES'", "'SCHEMAS'", "'CATALOGS'", "'COLUMNS'", "'USE'", "'PARTITIONS'", "'FUNCTIONS'", "'DROP'", "'UNION'", "'EXCEPT'", "'INTERSECT'", "'TO'", "'SYSTEM'", "'BERNOULLI'", "'POISSONIZED'", "'TABLESAMPLE'", "'RESCALED'", "'STRATIFY'", "'ALTER'", "'RENAME'", "'UNNEST'", "'ARRAY'", "'SET'", "'RESET'", "'SESSION'", "'IF'", "'NULLIF'", "'COALESCE'", "'='", "NEQ", "'<'", "'<='", "'>'", "'>='", "'+'", "'-'", "'*'", "'/'", "'%'", "'||'", "'?'", "STRING", "INTEGER_VALUE", "DECIMAL_VALUE", "WILDCARD", "IDENTIFIER", "DIGIT_IDENTIFIER", "QUOTED_IDENTIFIER", "BACKQUOTED_IDENTIFIER", "TIME_WITH_TIME_ZONE", "TIMESTAMP_WITH_TIME_ZONE", "SIMPLE_COMMENT", "BRACKETED_COMMENT", "WS", "UNRECOGNIZED", "DELIMITER" };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   81 */     ruleNames = new String[] { "singleStatement", "statement", "query", "queryNoWith", "queryTerm", "queryPrimary", "sortItem", "querySpecification", "selectItem", "relation", "relationPrimary", "expression", "booleanExpression", "predicated", "predicate", "valueExpression", "primaryExpression", "function", "columnName", "matchString", "primaryMatchString", "comparisonOperator", "booleanValue", "qualifiedName", "identifier", "quotedIdentifier", "number", "nonReserved" };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2421 */     _ATN = new org.antlr.v4.runtime.atn.ATNDeserializer().deserialize("\003а훑舆괭䐗껱趀ꫝ\003 Ú\004\002\t\002\004\003\t\003\004\004\t\004\004\005\t\005\004\006\t\006\004\007\t\007\004\b\t\b\004\t\t\t\004\n\t\n\004\013\t\013\004\f\t\f\004\r\t\r\004\016\t\016\004\017\t\017\004\020\t\020\004\021\t\021\004\022\t\022\004\023\t\023\004\024\t\024\004\025\t\025\004\026\t\026\004\027\t\027\004\030\t\030\004\031\t\031\004\032\t\032\004\033\t\033\004\034\t\034\004\035\t\035\003\002\003\002\003\002\003\003\003\003\003\004\003\004\003\005\003\005\003\005\003\005\005\005F\n\005\003\005\003\005\005\005J\n\005\003\006\003\006\003\007\003\007\003\b\003\b\005\bR\n\b\003\t\003\t\003\t\003\t\007\tX\n\t\f\t\016\t[\013\t\003\t\003\t\005\t_\n\t\003\t\003\t\005\tc\n\t\003\n\003\n\003\n\005\nh\n\n\003\n\003\n\003\n\005\nm\n\n\003\n\003\n\003\n\003\n\003\n\005\nt\n\n\003\013\003\013\003\f\003\f\003\r\003\r\003\016\003\016\003\016\003\016\003\016\003\016\003\016\003\016\005\016\n\016\003\016\003\016\003\016\003\016\003\016\003\016\007\016\n\016\f\016\016\016\013\016\003\017\003\017\003\017\003\017\003\017\005\017\n\017\003\020\003\020\003\020\003\020\005\020\n\020\003\020\003\020\003\020\003\020\003\020\005\020£\n\020\003\021\003\021\003\022\003\022\003\022\003\022\005\022«\n\022\003\023\003\023\003\023\003\023\003\023\003\023\003\023\003\023\003\023\003\023\005\023·\n\023\003\024\003\024\003\025\003\025\003\026\003\026\003\027\003\027\003\030\003\030\003\031\003\031\003\031\007\031Æ\n\031\f\031\016\031É\013\031\003\032\003\032\003\032\003\032\003\032\005\032Ð\n\032\003\033\003\033\003\034\003\034\005\034Ö\n\034\003\035\003\035\003\035\002\003\032\036\002\004\006\b\n\f\016\020\022\024\026\030\032\034\036 \"$&(*,.02468\002\007\003\002&'\005\002\003\002\003\002 !\013\002\024\026*3JMORXY^ehotyÚ\002:\003\002\002\002\004=\003\002\002\002\006?\003\002\002\002\bA\003\002\002\002\nK\003\002\002\002\fM\003\002\002\002\016O\003\002\002\002\020S\003\002\002\002\022s\003\002\002\002\024u\003\002\002\002\026w\003\002\002\002\030y\003\002\002\002\032\003\002\002\002\034\003\002\002\002\036¢\003\002\002\002 ¤\003\002\002\002\"ª\003\002\002\002$¶\003\002\002\002&¸\003\002\002\002(º\003\002\002\002*¼\003\002\002\002,¾\003\002\002\002.À\003\002\002\0020Â\003\002\002\0022Ï\003\002\002\0024Ñ\003\002\002\0026Õ\003\002\002\0028×\003\002\002\002:;\005\004\003\002;<\007\002\002\003<\003\003\002\002\002=>\005\006\004\002>\005\003\002\002\002?@\005\b\005\002@\007\003\002\002\002AE\005\n\006\002BC\007\021\002\002CD\007\020\002\002DF\005\016\b\002EB\003\002\002\002EF\003\002\002\002FI\003\002\002\002GH\007\023\002\002HJ\007\002\002IG\003\002\002\002IJ\003\002\002\002J\t\003\002\002\002KL\005\f\007\002L\013\003\002\002\002MN\005\020\t\002N\r\003\002\002\002OQ\005\030\r\002PR\t\002\002\002QP\003\002\002\002QR\003\002\002\002R\017\003\002\002\002ST\007\007\002\002TY\005\022\n\002UV\007\005\002\002VX\005\022\n\002WU\003\002\002\002X[\003\002\002\002YW\003\002\002\002YZ\003\002\002\002Z^\003\002\002\002[Y\003\002\002\002\\]\007\b\002\002]_\005\024\013\002^\\\003\002\002\002^_\003\002\002\002_b\003\002\002\002`a\007\016\002\002ac\005\032\016\002b`\003\002\002\002bc\003\002\002\002c\021\003\002\002\002dg\0050\031\002ef\007\t\002\002fh\0052\032\002ge\003\002\002\002gh\003\002\002\002ht\003\002\002\002il\005$\023\002jk\007\t\002\002km\0052\032\002lj\003\002\002\002lm\003\002\002\002mt\003\002\002\002no\0050\031\002op\007\003\002\002pq\007\002\002qt\003\002\002\002rt\007\002\002sd\003\002\002\002si\003\002\002\002sn\003\002\002\002sr\003\002\002\002t\023\003\002\002\002uv\005\026\f\002v\025\003\002\002\002wx\0050\031\002x\027\003\002\002\002yz\005\032\016\002z\031\003\002\002\002{|\b\016\001\002|}\007\032\002\002}\005\032\016\006~\005\034\017\002\007\006\002\002\005\030\r\002\007\004\002\002\003\002\002\002{\003\002\002\002~\003\002\002\002\003\002\002\002\003\002\002\002\f\005\002\002\007\030\002\002\005\032\016\006\f\004\002\002\007\027\002\002\005\032\016\005\003\002\002\002\003\002\002\002\003\002\002\002\003\002\002\002\003\002\002\002\033\003\002\002\002\003\002\002\002\005&\024\002\005\036\020\002\003\002\002\002\005(\025\002\005.\030\002\003\002\002\002\003\002\002\002\003\002\002\002\035\003\002\002\002\005,\027\002\005 \021\002£\003\002\002\002\007\032\002\002\003\002\002\002\003\002\002\002\003\002\002\002\007\034\002\002\005 \021\002 \007\030\002\002 ¡\005 \021\002¡£\003\002\002\002¢\003\002\002\002¢\003\002\002\002£\037\003\002\002\002¤¥\005\"\022\002¥!\003\002\002\002¦«\007\037\002\002§«\0056\034\002¨«\005.\030\002©«\007\002\002ª¦\003\002\002\002ª§\003\002\002\002ª¨\003\002\002\002ª©\003\002\002\002«#\003\002\002\002¬­\0050\031\002­®\007\006\002\002®¯\007\002\002¯°\007\004\002\002°·\003\002\002\002±²\0050\031\002²³\007\006\002\002³´\005&\024\002´µ\007\004\002\002µ·\003\002\002\002¶¬\003\002\002\002¶±\003\002\002\002·%\003\002\002\002¸¹\0050\031\002¹'\003\002\002\002º»\005*\026\002»)\003\002\002\002¼½\t\003\002\002½+\003\002\002\002¾¿\t\004\002\002¿-\003\002\002\002ÀÁ\t\005\002\002Á/\003\002\002\002ÂÇ\0052\032\002ÃÄ\007\003\002\002ÄÆ\0052\032\002ÅÃ\003\002\002\002ÆÉ\003\002\002\002ÇÅ\003\002\002\002ÇÈ\003\002\002\002È1\003\002\002\002ÉÇ\003\002\002\002ÊÐ\007\002\002ËÐ\0054\033\002ÌÐ\0058\035\002ÍÐ\007\002\002ÎÐ\007\002\002ÏÊ\003\002\002\002ÏË\003\002\002\002ÏÌ\003\002\002\002ÏÍ\003\002\002\002ÏÎ\003\002\002\002Ð3\003\002\002\002ÑÒ\007\002\002Ò5\003\002\002\002ÓÖ\007\002\002ÔÖ\007\002\002ÕÓ\003\002\002\002ÕÔ\003\002\002\002Ö7\003\002\002\002×Ø\t\006\002\002Ø9\003\002\002\002\026EIQY^bgls¢ª¶ÇÏÕ".toCharArray());
/*      */     
/*      */ 
/* 2424 */     _decisionToDFA = new org.antlr.v4.runtime.dfa.DFA[_ATN.getNumberOfDecisions()];
/* 2425 */     for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
/* 2426 */       _decisionToDFA[i] = new org.antlr.v4.runtime.dfa.DFA(_ATN.getDecisionState(i), i);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/generated/ADQLParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */