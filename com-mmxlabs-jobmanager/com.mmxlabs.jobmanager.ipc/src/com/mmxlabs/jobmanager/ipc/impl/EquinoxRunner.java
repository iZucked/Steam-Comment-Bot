/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.jobmanager.ipc.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Locates a JRE and an equinox stack and then communicates with that stack over its console
 * 
 * TODO either parse output or handle this better using JMX or similar for a nicer interface.
 * 
 * @author hinton
 * 
 */
public class EquinoxRunner {

	/**
	 * This is a runnable designed to be executed in another thread. It reads the equinox console standard out and looks for the osgi> prompt. The text following each osgi> prompt is then pushed onto
	 * a blocking queue.
	 * 
	 * When a command is executed, the executor waits on the blocking queue for an answer, so this runner is not really thread safe.
	 * 
	 * @author hinton
	 * 
	 */
	private class EquinoxOutputParser implements Runnable {
		private static final String PROMPT = "osgi>";

		private final BufferedReader reader;
		private final BlockingQueue<String> messages;
		private StringBuffer buffer = null;
		private StringBuffer lineBuffer = new StringBuffer();
		private final char[] smallBuffer = new char[PROMPT.length()];
		private int ring = 0;
		private boolean running = true;

		public EquinoxOutputParser(BufferedReader reader, BlockingQueue<String> messages) {
			super();
			this.reader = reader;
			this.messages = messages;
		}

		public boolean isRunning() {
			return running;
		}

		@Override
		public void run() {
			running = true;
			read();
			while (read()) {
				try {
					messages.put(buffer.toString());
				} catch (final InterruptedException e) {
					log.error("Output parser interrupted", e);
					return;
				}
			}
			running = false;
		}

		/*
		 * this is not efficient
		 */
		private boolean read() {
			buffer = new StringBuffer();

			int x;
			try {
				while ((x = reader.read()) != -1) {
					final char c = (char) x;
					buffer.append(smallBuffer[ring]);
					if (c == '\n') {
						log.debug("OSGI Output: " + lineBuffer.toString());
						lineBuffer = new StringBuffer();
					} else {
						lineBuffer.append(c);
					}
					smallBuffer[ring] = c;
					ring++;
					if (ring >= smallBuffer.length)
						ring = 0;
					boolean equal = true;
					for (int i = 0; i < smallBuffer.length; i++) {
						if (PROMPT.charAt(i) != smallBuffer[(i + ring) % smallBuffer.length]) {
							equal = false;
							break;
						}
					}

					if (equal) {
						return true;
					}
				}
			} catch (final IOException e) {
				log.error("Output parser IO exception", e);
				return false;
			}

			return true;
		}
	}

	private class Copier implements Runnable {
		private final BufferedReader reader;
		private final String name;
		private final PrintStream out;

		public Copier(final String name, final InputStream stream, final PrintStream err) {
			this.reader = new BufferedReader(new InputStreamReader(stream));
			this.name = name;
			this.out = err;
		}

		@Override
		public void run() {
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					out.println(name + ": " + line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			out.println(name + " closed and finished");
		}
	}

	private Process process = null;
	private BufferedWriter outputWriter;
	private BufferedReader errorReader;
	private BufferedReader inputReader;
	private EquinoxOutputParser outputParser;

	/**
	 * This queue has strings pushed onto it that are produced by equinox in between osgi> prompts.
	 */
	private final BlockingQueue<String> outputQueue = new LinkedBlockingQueue<String>();

	private static final Logger log = LoggerFactory.getLogger(EquinoxRunner.class);

	/**
	 * Holds a list of args which are passed to java, i.e. before the -jar argument
	 */
	private static final List<String> extraJavaArgs = new LinkedList<String>();
	/**
	 * Holds a list of args which are passed to equinox, after the -jar argument, and the default arguments which are always passed (-clean, -data, -configuration, -console)
	 */
	private static final List<String> extraEquinoxArgs = new LinkedList<String>();
	/**
	 * How many milliseconds to wait before sending a command; if you talk to equinox too quickly it has problems, for unknown reasons, and fails to load bundles which it's otherwise fine with.
	 */
	private static final long COMMAND_DELAY = 50;

	/**
	 * How many milliseconds to give equinox to finish starting up; for some reason it presents the osgi> prompt when it's not really ready, and if you ask it to load a bundle immediately it will blow
	 * up.
	 * 
	 * Good work there equinox.
	 */
	private static final long STARTUP_DELAY = 2000;

	public EquinoxRunner() {

	}

	/**
	 * Set a list of extra args which will be passed to the VM, rather than equinox
	 * 
	 * @param extraArgs
	 */
	public void setExtraJavaArgs(final List<String> extraArgs) {
		extraJavaArgs.clear();
		extraJavaArgs.addAll(extraArgs);
	}

	/**
	 * Set a list of extra args which will be passed to equinox, on top of the defaults.
	 * 
	 * @param extraArgs
	 */
	public void setExtraEquinoxArgs(final List<String> extraArgs) {
		extraEquinoxArgs.clear();
		extraEquinoxArgs.addAll(extraArgs);
	}

	public void start() throws IOException {
		final File vmFile = new File(getVMPath());
		File equinoxJarFile = null;
		try {
			equinoxJarFile = getEquinoxJar();
		} catch (URISyntaxException e) {
		}

		assert vmFile.exists() : "VM " + vmFile + " does not exist";
		assert equinoxJarFile != null : " equinox jar could not be located";
		assert equinoxJarFile.exists() : "equinox jar " + equinoxJarFile + " does not exist";

		final List<String> arguments = new LinkedList<String>();

		arguments.add(vmFile.getPath());

		arguments.addAll(extraJavaArgs);

		arguments.add("-jar");
		arguments.add(equinoxJarFile.getPath());
		arguments.add("-console");
		arguments.add("-clean");
		// get temp dir

		File tempDir = null;
		try {
			tempDir = createTempDir();
		} catch (IOException e) {
			log.error("Could not create temporary directory", e);
		}
		assert tempDir != null && tempDir.exists() : "Temp directory " + tempDir + " could not be created";

		// need one dir for runtime info and another for bundle scratch space.
		final File confDir = new File(tempDir, "conf");
		final File dataDir = new File(tempDir, "data");
		confDir.mkdir();
		dataDir.mkdir();
		assert confDir.exists();
		assert dataDir.exists();
		arguments.add("-configuration");
		arguments.add(confDir.toString());
		arguments.add("-data");
		arguments.add(dataDir.toString());

		arguments.addAll(extraEquinoxArgs);

		log.debug("Starting equinox process with " + arguments);

		final ProcessBuilder pb = new ProcessBuilder(arguments);

		setProcess(pb.start());

		if (true) {
			final Thread printError = new Thread(new Copier("OSGi Error", process.getErrorStream(), System.err));
			// final Thread printOut = new Thread(new Copier("OSGi Output", process.getInputStream(), System.out));
			printError.start();
			// printOut.start();
			outputParser = new EquinoxOutputParser(inputReader, outputQueue);
			final Thread parseOut = new Thread(outputParser);
			parseOut.start();
		}

		try {
			Thread.sleep(STARTUP_DELAY);
		} catch (final InterruptedException e) {
		}
	}

	private void setProcess(final Process process) {
		this.process = process;
		this.outputWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		this.inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		this.errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	}

	public static File createTempDir() throws IOException {
		final File temp;

		temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

		if (!(temp.delete())) {
			throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
		}

		if (!(temp.mkdir())) {
			throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
		}

		return (temp);
	}

	/**
	 * Find the path to the newest equinox framework jar. This is complicated by the fact that the system bundle's path isn't findable in the normal way.
	 * 
	 * @throws URISyntaxException
	 */
	private File getEquinoxJar() throws URISyntaxException {
		File bestOSGI = null;
		{
			/**
			 * This is the bundle containing the eclipse launcher; we actually want org.eclipse.osgi, but we can't get a path for it because it is the system bundle.
			 */
			final Bundle launcherBundle = Platform.getBundle("org.eclipse.equinox.launcher");
			if (launcherBundle == null) {
				log.error("Could not find launcher bundle!");
				return null;
			}
			final String launcherPath = launcherBundle.getLocation().substring("reference:".length());
			final File launcherFile = new File(new URI(launcherPath));
			final File launcherDirectory = launcherFile.getParentFile();
			final File[] jars = launcherDirectory.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File arg0, String arg1) {
					return arg1.endsWith(".jar");
				}
			});

			/**
			 * This matches versions of the org.eclipse.osgi bundle jar, and picks out the major / minor / subminor versiosn numbers
			 */
			final Pattern pattern = Pattern.compile("org\\.eclipse\\.osgi_(\\d+)\\.(\\d+)\\.(\\d+).*");
			final int[] versions = new int[] { 0, 0, 0 };
			for (final File f : jars) {
				final Matcher matcher = pattern.matcher(f.getName());
				// check whether the pattern matches this jar; if it does, see if it's a better version.
				if (matcher.matches()) {
					final int v0 = Integer.parseInt(matcher.group(1));
					final int v1 = Integer.parseInt(matcher.group(2));
					final int v2 = Integer.parseInt(matcher.group(3));
					boolean better = false;
					if (v0 > versions[0]) {
						better = true;
					} else if (v0 == versions[0]) {
						if (v1 > versions[1]) {
							better = true;
						} else if (v1 == versions[1]) {
							better = v2 > versions[2];
						}
					}
					if (better) {
						versions[0] = v0;
						versions[1] = v1;
						versions[2] = v2;
						bestOSGI = f;
					}
				}
			}
		}

		return bestOSGI;
	}

	/**
	 * Find the path of this VM's binary. Non-portable, and could be improved
	 */
	private String getVMPath() {
		return System.getProperty("java.home") + "/bin/java";
	}

	private String execute(final String command) {
		log.debug("execute " + command);
		try {
			Thread.sleep(COMMAND_DELAY);
			outputWriter.write(command + "\n");
			outputWriter.flush();

			String result = null;
			while (result == null && outputParser.isRunning()) {
				result = outputQueue.poll(1, TimeUnit.SECONDS);
			}
			log.debug("result=" + result);
			return result;
		} catch (final IOException ex) {
			log.error("IO Error executing " + command, ex);
		} catch (final InterruptedException e) {
			log.error("Interrupted waiting for output parser", e);
		}
		return null;
	}

	public void shutdown() {
		execute("close");
		try {
			log.debug("Equinox exit status " + process.waitFor());
		} catch (final InterruptedException e) {
			log.error("Interrupted waiting for child equinox instance to shut down", e);
		}
	}

	public void installBundle(final String name) {
		int tries = 3;
		while (tries > 0) {
			final String result = execute("install " + name);
			if (Pattern.compile("Bundle id is \\d+").matcher(result).find())
				return;
			else
				log.debug(result + " does not match success");
			tries = tries - 1;
		}
	}

	public void startBundle(final String name) {
		execute("start " + name);
	}
}
