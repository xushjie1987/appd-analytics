/*     */ package com.appdynamics.common.framework;
/*     */ 
/*     */ import com.appdynamics.common.util.exception.PermanentException;
/*     */ import com.appdynamics.common.util.lifecycle.Stoppable;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.util.concurrent.UncheckedExecutionException;
/*     */ import com.google.inject.Binder;
/*     */ import com.google.inject.Provider;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.matcher.AbstractMatcher;
/*     */ import com.google.inject.matcher.Matchers;
/*     */ import com.google.inject.spi.InjectionListener;
/*     */ import com.google.inject.spi.TypeEncounter;
/*     */ import com.google.inject.spi.TypeListener;
/*     */ import io.dropwizard.lifecycle.Managed;
/*     */ import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.annotation.PreDestroy;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.reflections.ReflectionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AppLifecycle
/*     */   implements Stoppable
/*     */ {
/*  53 */   private static final Logger log = LoggerFactory.getLogger(AppLifecycle.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  58 */   private static final List<Class<? extends Annotation>> LIFECYCLE_ANNOTATIONS = ImmutableList.of(PreDestroy.class, PostConstruct.class);
/*     */   
/*     */ 
/*     */ 
/*     */   private final LoadingCache<Class<?>, Multimap<Class<? extends Annotation>, Method>> annotatedMethodsCache;
/*     */   
/*     */ 
/*     */ 
/*     */   private final Deque<Object> preDestroyObjects;
/*     */   
/*     */ 
/*     */   private volatile boolean stopped;
/*     */   
/*     */ 
/*     */ 
/*     */   AppLifecycle()
/*     */   {
/*  75 */     this.stopped = false;
/*  76 */     this.annotatedMethodsCache = CacheBuilder.newBuilder().softValues().build(new CacheLoader()
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       public Multimap<Class<? extends Annotation>, Method> load(Class<?> key)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*  86 */         HashMultimap<Class<? extends Annotation>, Method> map = HashMultimap.create(AppLifecycle.LIFECYCLE_ANNOTATIONS.size(), 1);
/*     */         
/*     */ 
/*     */ 
/*  90 */         for (Class<? extends Annotation> annotation : AppLifecycle.LIFECYCLE_ANNOTATIONS) {
/*  91 */           Set<Method> methods = ReflectionUtils.getAllMethods(key, ReflectionUtils.withAnnotation(annotation));
/*     */           
/*  93 */           if (methods.size() > 0) {
/*  94 */             map.putAll(annotation, methods);
/*     */           }
/*     */         }
/*     */         
/*  98 */         return map;
/*     */       }
/*     */       
/* 101 */     });
/* 102 */     this.preDestroyObjects = new LinkedList();
/*     */   }
/*     */   
/*     */   private synchronized void processInjectee(Object injectee) {
/* 106 */     if (this.stopped) {
/* 107 */       throw new PermanentException("Lifecycle manager is stopping. Cannot create any more objects.");
/*     */     }
/*     */     
/* 110 */     log.trace("Injecting object {}", injectee);
/*     */     
/* 112 */     Multimap<Class<? extends Annotation>, Method> lifecycleMethods = (Multimap)this.annotatedMethodsCache.getUnchecked(injectee.getClass());
/*     */     
/*     */ 
/* 115 */     callAnnotatedMethods(lifecycleMethods, PostConstruct.class, injectee);
/*     */     
/* 117 */     Collection<Method> stopMethods = lifecycleMethods.get(PreDestroy.class);
/* 118 */     if (stopMethods.size() > 0)
/*     */     {
/* 120 */       this.preDestroyObjects.addFirst(injectee);
/*     */     }
/*     */   }
/*     */   
/*     */   private void processTypeEncounter(TypeLiteral<?> type, TypeEncounter<?> encounter)
/*     */   {
/*     */     try {
/* 127 */       this.annotatedMethodsCache.getUnchecked(type.getRawType());
/*     */     } catch (UncheckedExecutionException e) {
/* 129 */       encounter.addError(e.getCause());
/*     */     }
/*     */     
/* 132 */     encounter.register(new InjectionListener()
/*     */     {
/*     */       public void afterInjection(Object injectee) {
/* 135 */         AppLifecycle.this.processInjectee(injectee);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void stop()
/*     */   {
/* 146 */     for (Object obj : this.preDestroyObjects) {
/* 147 */       Multimap<Class<? extends Annotation>, Method> lifecycleMethods = (Multimap)this.annotatedMethodsCache.getUnchecked(obj.getClass());
/*     */       
/* 149 */       callAnnotatedMethods(lifecycleMethods, PreDestroy.class, obj);
/*     */     }
/*     */     
/* 152 */     this.preDestroyObjects.clear();
/* 153 */     this.stopped = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void callAnnotatedMethods(Multimap<Class<? extends Annotation>, Method> lifecycleMethods, Class<? extends Annotation> annotation, Object target)
/*     */   {
/* 161 */     Collection<Method> methods = lifecycleMethods.get(annotation);
/* 162 */     Set<Method> calledMethods = new HashSet();
/* 163 */     for (Method method : methods) {
/*     */       try {
/* 165 */         Method targetMethod = findTargetMethod(target.getClass(), method.getName(), method.getParameterTypes());
/* 166 */         if (!calledMethods.contains(targetMethod)) {
/* 167 */           targetMethod.setAccessible(true);
/* 168 */           targetMethod.invoke(target, new Object[0]);
/* 169 */           calledMethods.add(targetMethod);
/*     */         } else {
/* 171 */           log.debug("Did not call [{}] since it has already been called.", targetMethod.toGenericString());
/*     */         }
/*     */       }
/*     */       catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
/* 175 */         String msg = "Could not invoke " + annotation.getSimpleName() + "-annotated method on " + target;
/* 176 */         log.error(msg, e);
/* 177 */         throw new PermanentException(msg, e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private Method findTargetMethod(Class<?> cls, String name, Class<?>[] parameterTypes) throws NoSuchMethodException {
/*     */     try {
/* 184 */       return cls.getDeclaredMethod(name, parameterTypes);
/*     */     }
/*     */     catch (NoSuchMethodException e) {
/* 187 */       Class<?> parent = cls.getSuperclass();
/* 188 */       if (parent.equals(Object.class)) {
/* 189 */         throw e;
/*     */       }
/* 191 */       return findTargetMethod(parent, name, parameterTypes);
/*     */     }
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
/*     */ 
/*     */   void bind(Binder binder)
/*     */   {
/* 206 */     binder.bindListener(Matchers.any(), new TypeListener()
/*     */     {
/*     */       public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
/* 209 */         AppLifecycle.this.processTypeEncounter(type, encounter);
/*     */       }
/*     */       
/*     */ 
/* 213 */     });
/* 214 */     binder.bindInterceptor(Matchers.subclassesOf(Provider.class), new AbstractMatcher()
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 220 */       new MethodInterceptor
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         public boolean matches(Method method) {
/* 220 */           return ("get".equals(method.getName())) && (method.getParameterTypes().length == 0) && (!method.isSynthetic()) && (!method.isBridge()); } }, new MethodInterceptor[] { new MethodInterceptor()
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         public Object invoke(MethodInvocation invocation)
/*     */           throws Throwable
/*     */         {
/*     */ 
/*     */ 
/* 230 */           Object obj = invocation.proceed();
/* 231 */           AppLifecycle.this.processInjectee(obj);
/* 232 */           return obj;
/*     */         }
/*     */       } });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void manage(LifecycleEnvironment lifecycle)
/*     */   {
/* 244 */     lifecycle.manage(new Managed()
/*     */     {
/*     */       public void start() {}
/*     */       
/*     */ 
/*     */       public void stop()
/*     */       {
/*     */         try
/*     */         {
/* 253 */           AppLifecycle.this.stop();
/*     */         } catch (Throwable e) {
/* 255 */           AppLifecycle.log.error("Error occurred during shut down", e);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/AppLifecycle.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */