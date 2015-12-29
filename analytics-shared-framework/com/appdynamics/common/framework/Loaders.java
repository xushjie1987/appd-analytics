/*     */ package com.appdynamics.common.framework;
/*     */ 
/*     */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.appdynamics.common.util.lifecycle.Stoppable;
/*     */ import com.appdynamics.common.util.misc.Pair;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.inject.Binding;
/*     */ import com.google.inject.Guice;
/*     */ import com.google.inject.Injector;
/*     */ import com.google.inject.Module;
/*     */ import com.google.inject.spi.DefaultBindingTargetVisitor;
/*     */ import com.google.inject.spi.InstanceBinding;
/*     */ import io.dropwizard.Bundle;
/*     */ import io.dropwizard.ConfiguredBundle;
/*     */ import io.dropwizard.setup.Bootstrap;
/*     */ import io.dropwizard.setup.Environment;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
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
/*     */ public abstract class Loaders
/*     */ {
/*  42 */   private static final Logger log = LoggerFactory.getLogger(Loaders.class);
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
/*     */   static void loadAndAddBundles(Bootstrap<? extends AppConfiguration> bootstrap)
/*     */   {
/*  58 */     String csv = System.getProperty("ad.dw.bundle.classNames.csv");
/*  59 */     if ((csv == null) || ((csv = csv.trim()).length() <= 0)) {
/*  60 */       log.debug("No bundles to load");
/*     */       
/*  62 */       return;
/*     */     }
/*     */     
/*  65 */     log.debug("Attempting to load bundles [{}]", csv);
/*  66 */     String[] classNames = csv.split(",");
/*  67 */     ClassLoader cl = AppConfiguration.class.getClassLoader();
/*  68 */     for (int i = 0; i < classNames.length; i++) {
/*  69 */       classNames[i] = classNames[i].trim();
/*  70 */       if (classNames[i].length() > 0) {
/*  71 */         log.debug("Attempting to load bundle class [{}]", classNames[i]);
/*     */         try {
/*  73 */           Class klass = cl.loadClass(classNames[i]);
/*  74 */           if (Bundle.class.isAssignableFrom(klass)) {
/*  75 */             Bundle bundle = (Bundle)klass.newInstance();
/*  76 */             bootstrap.addBundle(bundle);
/*  77 */             log.debug("Loaded bundle class [{}]", classNames[i]);
/*  78 */           } else if (ConfiguredBundle.class.isAssignableFrom(klass)) {
/*  79 */             ConfiguredBundle bundle = (ConfiguredBundle)klass.newInstance();
/*  80 */             bootstrap.addBundle(bundle);
/*  81 */             log.debug("Loaded bundle class [{}]", classNames[i]);
/*     */           } else {
/*  83 */             throw new IllegalArgumentException("Class [" + classNames[i] + "]" + " does not implement [" + Bundle.class.getName() + "]");
/*     */           }
/*     */         }
/*     */         catch (Throwable t) {
/*  87 */           throw new ConfigurationException("Error occurred while attempting to load bundle class [" + classNames[i] + "]", t);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static Injector loadAndInitializeModules(AppConfiguration conf, Environment env)
/*     */   {
/* 101 */     List<Module> modules = new LinkedList();
/*     */     
/*     */ 
/* 104 */     List<ModuleConfiguration> moduleConfigurations = conf.getModules();
/* 105 */     if (moduleConfigurations == null) {
/* 106 */       log.info("No modules to load");
/*     */     } else {
/* 108 */       Map<ModuleConfiguration, Module> moduleInstances = loadModules(moduleConfigurations, Module.class);
/*     */       
/* 110 */       applyConfigurationsIfPossible(moduleInstances);
/*     */       
/* 112 */       log.debug("Loaded and configured modules [\n  - {}\n]", Joiner.on("\n  - ").join(moduleInstances.values()));
/*     */       
/* 114 */       for (Module module : moduleInstances.values()) {
/* 115 */         modules.add(module);
/*     */       }
/* 117 */       moduleInstances.clear();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 122 */     Pair<AppLifecycle, Injector> pair = internalPrepareAndPreStart(conf, env, modules);
/* 123 */     ((AppLifecycle)pair.getLeft()).manage(env.lifecycle());
/* 124 */     log.debug("Started modules");
/* 125 */     return (Injector)pair.getRight();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   static Pair<AppLifecycle, Injector> internalPrepareAndPreStart(AppConfiguration conf, Environment env, Iterable<? extends Module> modules)
/*     */   {
/* 142 */     List<Module> allModules = new LinkedList();
/* 143 */     for (Module module : modules) {
/* 144 */       Objects.requireNonNull(module, "List of modules cannot contain null elements");
/* 145 */       allModules.add(module);
/*     */     }
/*     */     
/* 148 */     AppLifecycle appLifecycle = new AppLifecycle();
/* 149 */     BootstrapModule bootstrapModule = new BootstrapModule(conf, env, appLifecycle);
/* 150 */     allModules.add(bootstrapModule);
/* 151 */     Injector injector = Guice.createInjector(allModules);
/*     */     
/*     */ 
/* 154 */     Set<Object> instances = new HashSet();
/* 155 */     for (Binding<?> binding : injector.getAllBindings().values()) {
/* 156 */       binding.acceptTargetVisitor(new DefaultBindingTargetVisitor()
/*     */       {
/*     */         public Void visit(InstanceBinding<?> instanceBinding) {
/* 159 */           Object instance = instanceBinding.getInstance();
/* 160 */           this.val$instances.add(instance);
/* 161 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 166 */     for (Module module : allModules)
/*     */     {
/*     */ 
/* 169 */       if (instances.contains(module)) {
/* 170 */         log.trace("Module [{}] will not be explicitly injected", module);
/*     */       }
/*     */       else {
/* 173 */         log.trace("Injecting module [{}]", module);
/* 174 */         injector.injectMembers(module);
/*     */       }
/*     */     }
/* 177 */     return new Pair(appLifecycle, injector);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Stoppable prepareAndPreStart(AppConfiguration conf, Environment env, Iterable<? extends Module> modules)
/*     */   {
/* 195 */     return (Stoppable)internalPrepareAndPreStart(conf, env, modules).getLeft();
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
/*     */   public static <T> T loadClass(String className, Class<T> castTo, ClassLoader optionalClassLoader)
/*     */     throws ClassNotFoundException, IllegalAccessException, InstantiationException
/*     */   {
/* 210 */     if (optionalClassLoader == null) {
/* 211 */       optionalClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     }
/*     */     
/* 214 */     Class klass = optionalClassLoader.loadClass(className);
/* 215 */     Object o = klass.newInstance();
/*     */     
/* 217 */     return (T)castTo.cast(o);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <MC extends ModuleConfiguration, M> LinkedHashMap<MC, M> loadModules(List<MC> moduleConfigurations, Class<M> moduleType)
/*     */   {
/* 234 */     log.debug("Attempting to load [{}] modules", Integer.valueOf(moduleConfigurations.size()));
/*     */     
/* 236 */     LinkedHashMap<MC, M> moduleInstances = new LinkedHashMap(moduleConfigurations.size());
/* 237 */     for (MC moduleConfiguration : moduleConfigurations) {
/* 238 */       String className = moduleConfiguration.getClassName();
/* 239 */       ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/* 240 */       if (classLoader == null) {
/* 241 */         classLoader = Loaders.class.getClassLoader();
/*     */       }
/*     */       try
/*     */       {
/* 245 */         String[] classPath = moduleConfiguration.getClassPath();
/* 246 */         if ((classPath != null) && (classPath.length > 0)) {
/* 247 */           URL[] urls = new URL[classPath.length];
/* 248 */           for (int i = 0; i < classPath.length; i++) {
/* 249 */             urls[i] = new URL(classPath[i]);
/*     */           }
/*     */           
/* 252 */           classLoader = new URLClassLoader(urls, classLoader);
/*     */         } else {
/* 254 */           classPath = new String[0];
/*     */         }
/*     */         
/* 257 */         log.debug("Attempting to load module class [{}]. Custom class path: {}", className, Arrays.asList(classPath));
/*     */         
/* 259 */         M instance = loadClass(className, moduleType, classLoader);
/* 260 */         moduleInstances.put(moduleConfiguration, instance);
/* 261 */         log.debug("Loaded module class [{}]", className);
/*     */       } catch (Throwable t) {
/* 263 */         String msg = "Error occurred while attempting to load module class [" + className + "]";
/* 264 */         log.error(msg, t);
/* 265 */         throw new ConfigurationException(msg + ". Module loading has been halted", t);
/*     */       }
/*     */     }
/*     */     
/* 269 */     log.debug("Completed loading");
/* 270 */     return moduleInstances;
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
/*     */   public static <MC extends ModuleConfiguration, M> void applyConfigurationsIfPossible(Map<MC, M> moduleInstances)
/*     */   {
/* 283 */     Map<MC, Configurable> configurableTargets = new LinkedHashMap(moduleInstances.size());
/*     */     
/* 285 */     for (Map.Entry<MC, M> entry : moduleInstances.entrySet()) {
/* 286 */       MC mc = (ModuleConfiguration)entry.getKey();
/* 287 */       M m = entry.getValue();
/* 288 */       String uri = mc.getUri();
/* 289 */       Object properties = mc.getProperties();
/*     */       
/* 291 */       if ((m instanceof Configurable)) {
/* 292 */         configurableTargets.put(mc, (Configurable)m);
/*     */       } else {
/* 294 */         if ((properties != null) && (
/* 295 */           (((properties instanceof Collection)) && (!((Collection)properties).isEmpty())) || (((properties instanceof Map)) && (!((Map)properties).isEmpty()))))
/*     */         {
/* 297 */           log.warn("Module [{}] has properties [{}] specified but they could not be set as it is not an instance of [{}]", new Object[] { m.getClass().getName(), properties, Configurable.class.getName() });
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 303 */         if (uri != null) {
/* 304 */           log.warn("Module [{}] has a uri [{}] specified but it could not be set as it is not an instance of [{}]", new Object[] { m.getClass().getName(), uri, Configurable.class.getName() });
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 310 */     if (configurableTargets.size() > 0) {
/* 311 */       applyConfigurations(configurableTargets);
/*     */     } else {
/* 313 */       log.warn("There were no [{}] instances", Configurable.class.getName());
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
/*     */ 
/*     */   public static <MC extends ModuleConfiguration, M extends Configurable> void applyConfigurations(Map<MC, M> configsAndTargets)
/*     */   {
/* 329 */     log.debug("Attempting to configure [{}] modules", Integer.valueOf(configsAndTargets.size()));
/*     */     
/* 331 */     for (Map.Entry<MC, M> entry : configsAndTargets.entrySet()) {
/* 332 */       MC mc = (ModuleConfiguration)entry.getKey();
/* 333 */       M m = (Configurable)entry.getValue();
/*     */       try {
/* 335 */         String uri = mc.getUri();
/* 336 */         m.setUri(uri);
/*     */         
/* 338 */         Object properties = mc.getProperties();
/* 339 */         Object configuration = Reader.readFrom(m.getConfigurationType(), properties);
/* 340 */         m.setConfiguration(configuration);
/*     */       } catch (Throwable t) {
/* 342 */         throw new ConfigurationException("Error occurred while configuring module [" + m.getClass().getName() + "]", t);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 347 */     log.debug("Configuration completed");
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/Loaders.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */