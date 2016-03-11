/*     */ package com.appdynamics.common.util.standalone;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.FileVisitor;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSource;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Manifest;
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
/*     */ 
/*     */ 
/*     */ public final class ProxyMain
/*     */ {
/*     */   public static final String FILE_MANIFEST = "META-INF/MANIFEST.MF";
/*     */   public static final String ATTRIBUTE_ACTUAL_MAIN_CLASS = "Actual-Main-Class";
/*     */   public static final String ATTRIBUTE_ACTUAL_CLASS_PATH = "Actual-Class-Path";
/*     */   public static final String DEFAULT_CLASS_PATH = ".";
/*     */   public static final String METHOD_NAME = "main";
/*     */   public static final String FILE_EXTENSION_JAR = ".jar";
/*     */   public static final String FILE_EXTENSION_ZIP = ".zip";
/*     */   public static final String PROP_LOG_LEVEL_VERBOSE = "log.level";
/*     */   public static final String LOG_LEVEL_DEFAULT = "INFO";
/*     */   public static final String LOG_LEVEL_VERBOSE = "DEBUG";
/*  63 */   private static final boolean VERBOSE_LOGGING = ;
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isLogLevelVerbose()
/*     */   {
/*  69 */     String level = System.getProperty("log.level", "INFO");
/*  70 */     return level.trim().equalsIgnoreCase("DEBUG");
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/*  75 */       callActualMain(args);
/*     */     } catch (Throwable t) {
/*  77 */       t.printStackTrace(System.out);
/*  78 */       System.exit(-1);
/*     */     }
/*     */   }
/*     */   
/*     */   static void callActualMain(String[] args)
/*     */     throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
/*     */   {
/*  85 */     String actualMainClassName = null;
/*  86 */     String classPath = ".";
/*  87 */     ClassLoader topClassLoader = ProxyMain.class.getClassLoader();
/*     */     
/*  89 */     Enumeration<URL> manifestUrls = topClassLoader.getResources("META-INF/MANIFEST.MF");
/*  90 */     while (manifestUrls.hasMoreElements()) {
/*  91 */       URL url = (URL)manifestUrls.nextElement();
/*  92 */       InputStream is = url.openStream();Throwable localThrowable2 = null;
/*  93 */       try { Manifest manifest = new Manifest(is);
/*  94 */         Attributes attributes = manifest.getMainAttributes();
/*     */         
/*  96 */         actualMainClassName = attributes.getValue("Actual-Main-Class");
/*  97 */         if (actualMainClassName == null)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */           if (is != null) if (localThrowable2 != null) { try { is.close(); } catch (Throwable x2) {} localThrowable2.addSuppressed(x2); } else { is.close();
/*     */             }
/*     */         }
/*     */         else {
/* 101 */           classPath = attributes.getValue("Actual-Class-Path");
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/*  92 */         localThrowable2 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 103 */         if (is != null) if (localThrowable2 != null) try { is.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else is.close();
/*     */       } }
/* 105 */     logVerbose("The class name that will be called is [" + actualMainClassName + "] using the " + "classpath [" + classPath + "]");
/*     */     
/* 107 */     if (actualMainClassName == null) {
/* 108 */       throw new IllegalArgumentException("The manifest does not contain the attribute [Actual-Main-Class]");
/*     */     }
/*     */     
/*     */ 
/* 112 */     String cp = classPath;
/* 113 */     final ClassLoader topCL = topClassLoader;
/* 114 */     URLClassLoader urlClassLoader = (URLClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public URLClassLoader run() {
/* 117 */         URL[] jarUrls = ProxyMain.findJarUrls(this.val$cp);
/* 118 */         URLClassLoader urlClassLoader = new URLClassLoader(jarUrls, topCL);
/* 119 */         Thread.currentThread().setContextClassLoader(urlClassLoader);
/* 120 */         return urlClassLoader;
/*     */       }
/*     */       
/* 123 */     });
/* 124 */     log("Calling [" + actualMainClassName + "] with arguments " + Arrays.asList(args));
/* 125 */     Class<?> actualMainClass = Class.forName(actualMainClassName, true, urlClassLoader);
/* 126 */     Method actualMainMethod = actualMainClass.getMethod("main", new Class[] { String[].class });
/* 127 */     actualMainMethod.invoke(null, new Object[] { args });
/*     */   }
/*     */   
/*     */   static URL[] findJarUrls(String classPath) {
/* 131 */     CodeSource codeSource = ProxyMain.class.getProtectionDomain().getCodeSource();
/*     */     
/* 133 */     logVerbose("This JAR is [" + codeSource.getLocation() + "]");
/* 134 */     Path codeSourcePath = new File(codeSource.getLocation().getFile()).toPath();
/* 135 */     logVerbose("This JAR is located at [" + codeSourcePath + "]");
/* 136 */     Path codeSourceParentPath = codeSourcePath.getParent();
/* 137 */     if (codeSourceParentPath == null) {
/* 138 */       throw new IllegalArgumentException("The directory containing the code source path was null.");
/*     */     }
/* 140 */     codeSourceParentPath = codeSourceParentPath.toAbsolutePath();
/* 141 */     Path resolvedClassPath = codeSourceParentPath.resolve(new File(classPath).toPath());
/* 142 */     log("The path that will be used to load classes is [" + resolvedClassPath + "]");
/* 143 */     Path actualClassPath = resolvedClassPath.toAbsolutePath();
/* 144 */     File actualClassDir = actualClassPath.toFile();
/* 145 */     if ((!actualClassDir.exists()) || (!actualClassDir.isDirectory())) {
/* 146 */       throw new IllegalArgumentException("The directory that is supposed to contain the libraries is invalid [" + actualClassDir.getAbsolutePath() + ']');
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 152 */       List<Path> jarPaths = new ArrayList();
/* 153 */       FileVisitor<Path> matcherVisitor = new SimpleFileVisitor()
/*     */       {
/*     */         public FileVisitResult visitFile(Path file, BasicFileAttributes baf) {
/* 156 */           String string = file.toString();
/* 157 */           if ((string.endsWith(".jar")) || (string.endsWith(".zip"))) {
/* 158 */             ProxyMain.logVerbose("Adding [" + string + "] to the classpath");
/* 159 */             this.val$jarPaths.add(file);
/*     */           }
/* 161 */           return FileVisitResult.CONTINUE;
/*     */         }
/* 163 */       };
/* 164 */       Files.walkFileTree(actualClassPath, matcherVisitor);
/* 165 */       URL[] jarUrls = new URL[jarPaths.size()];
/* 166 */       int i = 0;
/* 167 */       for (Path jarPath : jarPaths) {
/* 168 */         Path p = jarPath.toRealPath(new LinkOption[0]);
/* 169 */         jarUrls[(i++)] = new URL(p.toUri().toString());
/* 170 */         logVerbose("URL [" + jarUrls[(i - 1)] + "]");
/*     */       }
/* 172 */       return jarUrls;
/*     */     } catch (IOException e) {
/* 174 */       throw new IllegalArgumentException("The operation could not be performed because the library classpath is invalid or the expected directory structure is incorrect", e);
/*     */     }
/*     */   }
/*     */   
/*     */   static void log(String message)
/*     */   {
/* 180 */     System.out.println(message);
/*     */   }
/*     */   
/*     */   static void logVerbose(String message) {
/* 184 */     if (VERBOSE_LOGGING) {
/* 185 */       System.out.println(message);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/standalone/ProxyMain.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */