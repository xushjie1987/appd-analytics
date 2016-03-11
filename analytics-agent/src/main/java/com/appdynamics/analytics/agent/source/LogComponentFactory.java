package com.appdynamics.analytics.agent.source;

import com.appdynamics.analytics.agent.input.tail.FileSignature;
import com.appdynamics.analytics.agent.input.tail.TailFileState;
import com.appdynamics.analytics.agent.source.tail.DirectoryPoller;
import com.appdynamics.analytics.agent.source.tail.FileInputScanner;
import com.appdynamics.analytics.pipeline.framework.Pipeline;
import com.appdynamics.analytics.pipeline.framework.PipelineConfiguration;
import com.appdynamics.common.io.file.FilePathConfiguration;
import com.appdynamics.common.util.lifecycle.RunningState;
import com.google.common.base.Function;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public abstract interface LogComponentFactory
{
  public abstract RunningState createRunningState(boolean paramBoolean);
  
  public abstract Pipeline createPipeline(PipelineConfiguration paramPipelineConfiguration);
  
  public abstract DirectoryPoller createDirectoryPoller(FilePathConfiguration paramFilePathConfiguration);
  
  public abstract FileInputScanner createFileInputScanner(DirectoryPoller paramDirectoryPoller, Map<FileSignature, TailFileState> paramMap1, Map<FileSignature, TailFileState> paramMap2, Function<TailFileState, Boolean> paramFunction);
  
  public abstract LogSource createLogSource(LogSourceConfiguration paramLogSourceConfiguration, ExecutorService paramExecutorService)
    throws IllegalArgumentException;
}


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/source/LogComponentFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */