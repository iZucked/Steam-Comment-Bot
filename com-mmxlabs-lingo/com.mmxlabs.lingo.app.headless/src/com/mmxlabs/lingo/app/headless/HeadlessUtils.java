/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.json.simple.JSONObject;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.license.features.pluginxml.PluginRegistryHook;
import com.mmxlabs.license.ssl.LicenseChecker;
import com.mmxlabs.license.ssl.LicenseState;
import com.mmxlabs.lingo.app.headless.HeadlessGenericApplication.InvalidCommandLineException;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationConstants;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.rcp.common.viewfactory.ReplaceableViewManager;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;

public class HeadlessUtils {

	/**
	 * Returns an {@link Options} object with the default required options for an OSGI application.
	 * 
	 * @return
	 */
	public static Options getRequiredOsgiOptions() {
		// create the Options
		final Options options = new Options();

		// Options for OSGi/Eclipse compat - not used, but needs to be specified
		options.addOption("application", true, "(OSGi) Application ID");
		options.addOption("arch", true, "(OSGi) Set arch");
		options.addOption("clean", false, "(OSGi) Clean any framework cached data");
		options.addOption("configuration", true, "(OSGi) OSGi configuration area");
		options.addOption(OptionBuilder.withLongOpt("console").withDescription("[port] (OSGi) Enable OSGi Console").hasOptionalArg().create());
		options.addOption("consoleLog", false, "(OSGi) Enable logging to console");
		options.addOption("data", true, "(OSGi) OSGi instance area");
		options.addOption(OptionBuilder.withLongOpt("debug").withDescription("[options file] (OSGi) debug mode").hasOptionalArg().create());
		options.addOption(OptionBuilder.withLongOpt("dev").withDescription("[entires] (OSGi) dev mode").hasOptionalArg().create());
		// options.addOption("eclipse.keyring", true, "(Equinox) Set to override
		// location of the default secure storage");
		// options.addOption("eclipse.password", true,
		// "(Equinox) If specified, the secure storage treats contents of the file as a
		// default password. When not set, password providers are used to obtain a
		// password.");
		options.addOption("feature", true, "(Equinox) equivalent to setting eclipse.product to <feature id>");
		options.addOption("framework", true, "(Equinox) equivalent to setting osgi.framework to <location>");
		options.addOption("initialize", false,
				"(OSGi) initializes the configuration being run. All runtime related data structures and caches are refreshed. Any user/plug-in defined configuration data is not purged. No application is run, any product specifications are ignored and no UI is presented (e.g., the splash screen is not drawn)");
		options.addOption("install", true, "(OSGi) OSGi install area");
		options.addOption("keyring", true, "(OSGi) the location of the authorization database on disk. This argument has to be used together with the -password argument.");
		// various --launcher options

		options.addOption("name", true,
				"(OSGi) The name to be displayed in the task bar item for the splash screen when the application starts up (not applicable on Windows). Also used as the title of error dialogs opened by the launcher. When not set, the name is the name of the executable.");

		options.addOption("nl", true, "(OSGi) equivalent to setting osgi.nl to <locale>");
		options.addOption("noExit", false, "(OSGi) equivalent to setting osgi.noShutdown to \"true\"");
		options.addOption("noLazyRegistryCacheLoading", false, "(OSGi) equivalent to setting eclipse.noLazyRegistryCacheLoading to \"true\"");
		options.addOption("noRegistryCache", false, "(OSGi)equivalent to setting eclipse.noRegistryCache to \"true\"");
		options.addOption("nosplash", false, "(OSGi) Disable splash screen");
		options.addOption("os", true, "(OSGi) equivalent to setting osgi.os to <operating system>");
		options.addOption("password", true, "(OSGi) the password for the authorization database");
		options.addOption("pluginCustomization", true, "(OSGi) equivalent to setting eclipse.pluginCustomization to <location>");
		options.addOption("product", true, "(OSGi) Product ID");

		options.addOption("registryMultiLanguage", false, "(OSGi) equivalent to setting eclipse.registry.MultiLanguage to \"true\"");
		options.addOption("showSplash", true,
				"(OSGi) The location of jar used to startup eclipse. The jar referred to should have the Main-Class attribute set to org.eclipse.equinox.launcher.Main. If this parameter is not set, the executable will look in the plugins directory for theorg.eclipse.equinox.launcher bundle with the highest version.");

		options.addOption("startup", true, "(OSGi) Set location of startup jar");
		options.addOption("user", true, "(OSGi) OSGi user area");
		options.addOption("vm", true, "(OSGi) used to locate the Java VM");
		options.addOption(OptionBuilder.withLongOpt("vmargs").withDescription("(OSGi) Java VM arguments").hasArgs().create());
		options.addOption("ws", true, "(OSGi) Set window system");

		// Memory command line args (Not used by headless, but added to maintain compat
		// with main laucher arg set.
		options.addOption("automem", false, "(LiNGO) Automatically determine upper bound for heap size");
		options.addOption("noautomem", false, "(LiNGO) Do not automatically determine upper bound for heap size");

		return options;

	}

	/**
	 * Initialises the application custom features
	 */
	public static void initAccessControl() {
		// Initialise feature enablements
		LicenseFeatures.initialiseFeatureEnablements();

		PluginRegistryHook.initialisePluginXMLEnablements();

		ReplaceableViewManager.initialiseReplaceableViews();
	}

	@NonNullByDefault
	public static void saveResult(EObject result, IScenarioDataProvider scenarioDataProvider, File resultOutput, IScenarioCipherProvider cipher) throws IOException {

		final EditingDomain editingDomain = scenarioDataProvider.getEditingDomain();

		// Set the place holder root model URI prior to saving
		for (final var r : editingDomain.getResourceSet().getResources()) {
			if (!r.getContents().isEmpty() && r.getContents().get(0) instanceof LNGScenarioModel) {
				r.setURI(URI.createURI(CloudOptimisationConstants.ROOT_MODEL_URI));
			}
		}

		// Convert to a URI for EMF
		final URI resultURI = URI.createFileURI(resultOutput.getAbsolutePath());
		// Create a resource in the resource set. This should allow cross resource
		// referencing and hook in the encryption code path
		final Resource r = editingDomain.getResourceSet().createResource(resultURI);
		r.getContents().add(result);

		Map<String, URIConverter.Cipher> options = new HashMap<>();
		if (cipher != null) {
			options.put(Resource.OPTION_CIPHER, cipher.getSharedCipher());
		}
		r.save(options);
	}

	public static JSONObject getDefaultMachineInfo() {
		JSONObject machInfo = new JSONObject();
		machInfo.put("AvailableProcessors", Runtime.getRuntime().availableProcessors());
		machInfo.put("os", System.getProperty("os.name"));
		machInfo.put("JavaVersion", System.getProperty("java.version"));

		{
			// If we are running on EC2, then we can try to grab the instance type
			try {
				Process process = Runtime.getRuntime().exec("ec2-metadata -t");
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
					String line;
					while ((line = reader.readLine()) != null) {
						int idx = line.indexOf(':');
						if (idx > 0) {
							machInfo.put(line.substring(0, idx).trim(), line.substring(idx + 1).trim());
						}
					}
				}
			} catch (Exception e) {
				// Ignore any exceptions
			}
		}

		machInfo.put("AvailableProcessors", Runtime.getRuntime().availableProcessors());
		return machInfo;
	}

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String mD5Checksum(File input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			// If source is a directory, collect MD5 sum of all contents.
			if (input.isDirectory()) {
				for (File f : input.listFiles()) {
					if (f.isFile()) {
						try (InputStream in = new FileInputStream(f)) {
							byte[] block = new byte[4096];
							int length;
							while ((length = in.read(block)) > 0) {
								digest.update(block, 0, length);
							}
						}
					}
				}
			} else {

				try (InputStream in = new FileInputStream(input)) {
					byte[] block = new byte[4096];
					int length;
					while ((length = in.read(block)) > 0) {
						digest.update(block, 0, length);
					}
				}
			}
			String result = bytesToHex(digest.digest());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Filter out invalid command line items that getopt cannot work with
	 * 
	 * @param commandLineArgs
	 * @return
	 */
	public static String[] filterCommandLineArgs(final String[] commandLineArgs) {
		final List<String> commandLine = new ArrayList<>(commandLineArgs.length);
		int skip = 0;
		for (final String arg : commandLineArgs) {
			if (skip != 0) {
				--skip;
				continue;
			}
			if (arg.equals("-eclipse.keyring")) {
				skip = 1;
				continue;
			}
			if (arg.equals("-eclipse.password")) {
				skip = 1;
				continue;
			}
			commandLine.add(arg);

		}
		return commandLine.toArray(new String[commandLine.size()]);
	}

	/**
	 * Returns the value of a command line parameter, or a specified existing value. If the existing value is not null, and the command line parameter is present, a warning is printed to stderr.
	 */
	public static String commandLineParameterOrValue(CommandLine commandLine, String commandLineOptionName, String oldValue) {
		if (commandLine.hasOption(commandLineOptionName)) {
			String newValue = commandLine.getOptionValue(commandLineOptionName);

			if (oldValue != null && oldValue.equals(newValue) == false) {
				String overrideWarning = "Overriding existing value with command line option %s: '%s' -> '%s'";
				System.err.println(String.format(overrideWarning, commandLineOptionName, oldValue, newValue));
			}

			return newValue;

		}

		return oldValue;
	}
	
	/**
	 * Checks the user license, throwing an exception if there is a problem.
	 * 
	 * @throws LicenseChecker.InvalidLicenseException
	 */
	public static void doCheckLicense() throws LicenseChecker.InvalidLicenseException {
		// check to see if the user has a valid license
		final LicenseState validity = LicenseChecker.checkLicense();
		if (validity != LicenseState.Valid) {
			System.err.println("License is invalid");
			throw new LicenseChecker.InvalidLicenseException();
		}
	}
}
