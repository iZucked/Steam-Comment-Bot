/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;
import com.mmxlabs.scheduler.optimiser.utils.GCStats;

//From OSGI prompt, run e.g. 
//osgi> optionise C:/dev/bm/bm.lingo C:/dev/bm/options.json
public class HeadlessOptioniserRunnerConsoleCommand implements CommandProvider {
	public static class Options {
	   int numRuns;
	   HeadlessOptioniserRunner.Options options;		
	}

	private static SimpleDateFormat sdfTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss");
	
	public static String COMMAND = "command";
		
	public void _optionise(CommandInterpreter ci) throws Exception {
				
		//Suggest to the jvm to do a gc to clean up the heap before we start off anything.
		System.gc();
		
		String scenarioFileName = null;
		String paramsFileName = null;

		String arg = null;
		while ((arg = ci.nextArgument()) != null) {

			if (scenarioFileName == null) {
				scenarioFileName = arg;
			} else if (paramsFileName == null) {
				paramsFileName = arg;
			}
		}

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.registerModule(new Jdk8Module());

		Options topOptions = mapper.readValue(new File(paramsFileName), Options.class);
		HeadlessOptioniserRunner.Options options = topOptions.options;
		final int minThreads = options.minWorkerThreads;
		final int maxThreads = options.maxWorkerThreads;
		if (options.turnPerfOptsOn) {
//			OptOptions.getInstance().setAllOnOff(options.turnPerfOptsOn);
		}
		
		File scenarioFile = new File(scenarioFileName);
		ThreadGroup tg = new ThreadGroup("Console optioniser");

		final String timestampStr = getTimestamp();
		PrintStream pst = System.out;
		if (options.filename != null && !options.filename.equals("")) {
			final String filenameStr = scenarioFile.getAbsolutePath().replace(scenarioFile.getName(), "") + options.filename + "_"+timestampStr+ ".csv";
			try {
				pst = new PrintStream(new File(filenameStr));
			} catch (FileNotFoundException e) {
				System.err.printf("Error opening options.filename, %s outputting to stdout instead:\n", filenameStr);
				e.printStackTrace(System.err);
			}
		}

		int totalIterations = options.iterations;
	
		pst.println("Run,#Threads,Time(millis),GCTime(millis)");
		for (int threads = minThreads; threads <= maxThreads; threads *= 2) {
			//options.iterations = totalIterations / threads;
			//CleanableExecutorService executorService = null;
			
			//if (threads > 1) {
			//	executorService = LNGOptimisationBuilder.createExecutorService(threads);
			//}
			
			//Do the runs with this set of parameters.
			for (int run = 1; run <= topOptions.numRuns; run++) {
				
				HeadlessOptioniserJSON json = (new HeadlessOptioniserJSONTransformer()).createJSONResultObject(getMachineInfo(), options, scenarioFile, threads);
		
				List<Future<SlotInsertionOptimiserLogger>> futures = new ArrayList<>();
				List<SlotInsertionOptimiserLogger> results = new ArrayList<>();
				
				//Do everything in N threads.
				int startTry = (run - 1) * totalIterations; //every run should start at a different point.
				SlotInsertionOptimiserLogger logger = new SlotInsertionOptimiserLogger();	
				
				CountDownLatch start = new CountDownLatch(1);
				CountDownLatch finished = new CountDownLatch(threads);
				
				for (int t = 0; t < threads; t++) {										
					final int startTryT = startTry;
					
					if (threads > 1) {
					//	futures.add(executorService.submit(() -> {
							SlotInsertionOptimiserLogger loggerT = new SlotInsertionOptimiserLogger();
							results.add(loggerT);
							Runnable r = createNewOptimiserThread(startTryT, options, scenarioFile, loggerT, start, finished);
					//		r.run();
							Thread thread = new Thread(r);
							thread.start();
							//return loggerT;
					//	}));
					}
					else {
						start.countDown();
						Runnable r = createNewOptimiserThread(startTryT, options, scenarioFile, logger, start, finished);
						r.run();
					}
					
					startTry += options.iterations;
				}
				
				//Start off all the threads.
				if (threads > 1) {
					start.countDown();
				}
				
				//Wait for all threads to complete.
				finished.await();
				
				if (threads > 1) {
					
					
				//	for (var ft : futures) {
//						results.add(ft.get());
						//Aggregate up total times.
						logger.aggregate(results);
				//	}
				}
				
				//Print out how long the run took.
				pst.printf("%d,%d,%d,%d\n",run,threads,logger.getRuntime(),GCStats.getGCTimeInMillis());
		
				if (options.outputToJSON) {
					(new HeadlessOptioniserJSONTransformer()).addRunResult(startTry, logger, json);  

					//Write out the result of all runs.
					try {
						ObjectMapper mapper1 = new ObjectMapper();
						mapper1.writerWithDefaultPrettyPrinter().writeValue(new File(scenarioFile.getAbsoluteFile() + "." + UUID.randomUUID().toString() + ".json"), json);
					} catch (Exception e) {
						System.err.println("Error writing to file:");
						e.printStackTrace();
					}
				}
			}

			//if (executorService != null) {
			//	executorService.shutdown();
			//}
		}
		
		if (pst != System.out) {
			pst.close();
		}
	}
	
	private Runnable createNewOptimiserThread(final int startTry, HeadlessOptioniserRunner.Options options, File scenarioFile, SlotInsertionOptimiserLogger logger,
			CountDownLatch start, CountDownLatch finished) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					start.await();
					//Everything from here on single threaded.
					options.maxWorkerThreads = 1;
					HeadlessOptioniserRunner runner = new HeadlessOptioniserRunner();
					runner.run(startTry, scenarioFile, logger, options, null);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					finished.countDown();
				}
			}
		};
	}
	
	private String getTimestamp() {
		return sdfTimestamp.format(new Date());
	}
	
	private String getMachineInfo() {
		String machInfo = "AvailableProcessors:"+Integer.toString(Runtime.getRuntime().availableProcessors());
		return machInfo;
	}
	
	@Override
	public String getHelp() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("optionise scenario.lingo options.json\n\t");
		return buffer.toString();
	}

}
