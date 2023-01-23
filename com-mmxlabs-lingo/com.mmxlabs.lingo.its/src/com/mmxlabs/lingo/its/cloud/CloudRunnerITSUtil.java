/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.cloud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.SecretKey;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIConverter.Cipher;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.google.common.io.ByteStreams;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationConstants;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.CloudOptimisationDataService;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.ScenarioServicePushToCloudAction.KeyData;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.impl.CloudOptimisationSharedCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileLoader;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileUtil;
import com.mmxlabs.scenario.service.model.util.encryption.impl.keyfiles.KeyFileV2;

public class CloudRunnerITSUtil {

//	private static final String SANDBOX_APPLICATION = "com.mmxlabs.lingo.app.headless.HeadlessSandboxOneshotApplication";
//	private static final String OPTIMISER_APPLICATION = "com.mmxlabs.lingo.app.headless.HeadlessOptimiserOneshotApplication";

	private static final String CLOUD_APPLICATION = "com.mmxlabs.lingo.app.headless.HeadlessCloudApplication";

	public static File runScenario(final File target, final String jobType, final File scenario, final File params) throws Exception {

		// Now generate all the certificates to simulate the gateway.
		// We need a keypair for the gateway. The public key is used on the client to
		// encrypt the symmetric key used for the scenario during transit
		// The secret key is used on the optimisation node to decrypt the symmetric key

		RSAPublicKey optiServerPubKey;
		final File optiServerPrivateKey = new File(target, "optiServer.key");
		{

			final KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			final KeyPair kp = kpg.generateKeyPair();
			optiServerPubKey = (RSAPublicKey) kp.getPublic();
			final Key pvt = kp.getPrivate();

			final Base64.Encoder encoder = Base64.getEncoder();
			try (FileWriter out = new FileWriter(optiServerPrivateKey)) {
				out.write("-----BEGIN PRIVATE KEY-----\n");
				out.write(encoder.encodeToString(pvt.getEncoded()));
				out.write("\n-----END PRIVATE KEY-----\n");
			}
		}

		final KeyData keyData = generateKeyPairs(target, "abc", optiServerPubKey);

		final CloudOptimisationSharedCipherProvider cipher = new CloudOptimisationSharedCipherProvider(keyData.keyfile());
		final File encryptedZip = new File(target, "scenario.lingo");
		try (FileInputStream fis = new FileInputStream(scenario)) {
			try (FileOutputStream fout = new FileOutputStream(encryptedZip)) {
				ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, p -> reencryptScenario(p.getSharedCipher(), cipher.getSharedCipher(), fis, fout));
			}
		}

		final File runDir = new File(target, "LiNGO");
		runDir.mkdirs();
		final File encryptedResult = new File(target, "result.enc.xmi");

		final String jre;
		final String os = System.getProperty("osgi.os");
		if ("win32".equals(os)) {
			jre = System.getProperty("java.home") + "\\bin\\javaw.exe";
		} else {
			jre = System.getProperty("java.home") + "/bin/java";
		}
		final List<String> commands = new LinkedList<>();
		final Consumer<String> copyProp = p -> commands.add(String.format("-D%s=%s", p, System.getProperty(p)));

		final BiConsumer<String, String> copyArg = (arg, prop) -> {
			commands.add(arg);
			commands.add(System.getProperty(prop));
		};

		final Consumer<String> copyPropIfSet = p -> {
			if (System.getProperty(p) != null) {
				commands.add(String.format("-D%s=%s", p, System.getProperty(p)));
			}
		};

		commands.add(jre); // Java exe

		// Runtime configuration
		copyProp.accept("osgi.splashPath");
		copyProp.accept("osgi.bundles");
		copyProp.accept("osgi.install.area");
		copyProp.accept("org.eclipse.equinox.simpleconfigurator.configUrl");
		copyProp.accept("eclipse.p2.data.area");
		copyProp.accept("osgi.bundles.defaultStartLevel");
		copyProp.accept("osgi.framework");
		copyProp.accept("osgi.configuration.cascaded");

		// Build server / ITS launch configs may pass in these variables
		copyPropIfSet.accept("lingo.license.file");
		copyPropIfSet.accept("lingo.enc.keyfile");

		commands.add("-Dlingo.cloud.key.uuid=" + Base64.getEncoder().encodeToString(keyData.keyUUID())); //
		commands.add("-Dlingo.cloud.enc.keyfile=" + keyData.encryptedSymmetricKey().getAbsolutePath()); //
		commands.add("-Dlingo.cloud.priv.keyfile=" + optiServerPrivateKey.getAbsolutePath()); //

		commands.add("org.eclipse.equinox.launcher.Main"); // Entry point
		copyArg.accept("-dev", "osgi.dev"); // Copy classpath

		// LiNGO args
		commands.add("-product");
		commands.add("com.mmxlabs.lingo.app.product");
		commands.add("-data");
		commands.add(runDir.getAbsolutePath());

		commands.add("-debug");

		commands.add("-noSplash");
		commands.add("-console");
		commands.add("-consoleLog");

		commands.add("-application");
		commands.add(CLOUD_APPLICATION);
		commands.add("-jobtype");
		commands.add(jobType);

		commands.add("-params");
		commands.add(params.getAbsolutePath()); //
		commands.add("-scenario");
		commands.add(encryptedZip.getAbsolutePath()); //

		commands.add("-outputScenario");
		commands.add(encryptedResult.getAbsolutePath());

		final ProcessBuilder pb = new ProcessBuilder(commands);

		// Set this as environment rather than command line arg as the command lines gets too big
		pb.environment().put("CLASSPATH", System.getProperty("java.class.path"));
		
		pb.directory(runDir);

		// Start the lingo process and copy std out/err to this process' output.
		final Process p = pb.start();
		while (p.isAlive()) {
			System.out.println("Running lingo process...");
			Thread.sleep(20_000);
//			{
//				String line;
//				final BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//				while ((line = br.readLine()) != null) {
//					System.out.println(line);
//				}
//			}
//			{
//				String line;
//				final BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//				while ((line = br.readLine()) != null) {
//					System.err.println(line);
//				}
//			}
		}

		// Decrypt the result using the symmetric key generated earlier and re-encrypt
		// with application key.
		final CloudOptimisationSharedCipherProvider scenarioCipherProvider = new CloudOptimisationSharedCipherProvider(keyData.keyfile());
		final Cipher cloudCipher = scenarioCipherProvider.getSharedCipher();
		final File solutionFile = new File(target, "result.xmi.plain");
		try (FileOutputStream fout = new FileOutputStream(solutionFile)) {
			try (FileInputStream fin = new FileInputStream(encryptedResult)) {
				try (InputStream is = cloudCipher.decrypt(fin)) {
					ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, provider -> {
						try (OutputStream eOut = provider.getSharedCipher().encrypt(fout)) {
							ByteStreams.copy(is, eOut);
						}
					});
				}
			}
		}

		return solutionFile;
	}

	public static KeyData generateKeyPairs(final File target, final String scenarioUUID, final RSAPublicKey optiServerPubKey) throws Exception {

		final byte[] keyUUID = new byte[KeyFileUtil.UUID_LENGTH];
		EcoreUtil.generateUUID(keyUUID);

		final SecretKey tmpKey = KeyFileV2.generateKey(256);
		final KeyFileV2 keyfile = new KeyFileV2(keyUUID, tmpKey);

		File encryptedSymmetricKey = null;
		File keyStore = null;
		try {

			// save encrypted symmetric key to a temp file
			encryptedSymmetricKey = new File(target, "shared.key");

			// encrypt symmetric key with public key (to send to opti server)
			CloudOptimisationDataService.INSTANCE.encryptSymmetricKey(optiServerPubKey, tmpKey, encryptedSymmetricKey);

			keyStore = new File(target, "keystore.p12");
			KeyFileLoader.initalise(keyStore);
			KeyFileLoader.addToStore(keyStore, keyUUID, tmpKey, KeyFileV2.KEYFILE_TYPE);

			return new KeyData(keyUUID, keyfile, tmpKey, encryptedSymmetricKey, keyStore);
		} catch (Exception e) {
			if (encryptedSymmetricKey != null) {
				encryptedSymmetricKey.delete();
			}
			if (keyStore != null) {
				keyStore.delete();
			}
			throw e;
		}
	}

	/**
	 * Copies an {@link InputStream} of scenario data to the output stream through a
	 * re-encryption step. Returns a map of the decrypted digests for each entry in
	 * the zip file.
	 * 
	 * @param cipher
	 * @param in
	 * @param out
	 * @return
	 * @throws Exception
	 */
	private static void reencryptScenario(final Cipher sourceCipher, final Cipher cipher, final InputStream in, final OutputStream out) throws Exception {

		// LiNGO scenario is a zip file, so we need to unzip and decrypt.
		try (ZipOutputStream zipOut = new ZipOutputStream(out)) {
			try (ZipInputStream zipIn = new ZipInputStream(in)) {

				ZipEntry zipInEntry = zipIn.getNextEntry();
				final NonClosingInputStream filterIn = new NonClosingInputStream(zipIn);

				while (zipInEntry != null) {
					final ZipEntry zipOutEntry = new ZipEntry(zipInEntry);

					zipOut.putNextEntry(zipOutEntry);

					final NonClosingOutputStream filterOut = new NonClosingOutputStream(zipOut);

					try (PushbackInputStream pin = new PushbackInputStream(filterIn, KeyFileUtil.UUID_LENGTH)) {
						{
							try (InputStream dis = sourceCipher.decrypt(pin)) {
								try (OutputStream os = cipher.encrypt(filterOut)) {
									ByteStreams.copy(dis, os);
								}
							}
						}
						zipOut.closeEntry();

						zipIn.closeEntry();

						zipInEntry = zipIn.getNextEntry();
					}
				}
			}
		}
	}

	private static class NonClosingInputStream extends FilterInputStream {
		protected NonClosingInputStream(final InputStream in) {
			super(in);
		}

		@Override
		public void close() throws IOException {
			// Do not close the stream

		}
	}

	private static class NonClosingOutputStream extends FilterOutputStream {
		protected NonClosingOutputStream(final OutputStream out) {
			super(out);
		}

		@Override
		public void close() throws IOException {
			// Do not close the stream.
			flush();
		}
	}

	public static AbstractSolutionSet solutionReady(final IScenarioDataProvider sdp, final File solutionFile) throws Exception {

		final EditingDomain ed = sdp.getEditingDomain();

		Resource rr = null;
		final URI rootModelURI = URI.createURI(CloudOptimisationConstants.ROOT_MODEL_URI);
		try {

			// Find the root object and create a URI mapping to the real URI so the result
			// xmi can resolve
			for (final var r : ed.getResourceSet().getResources()) {
				if (!r.getContents().isEmpty() && r.getContents().get(0) instanceof LNGScenarioModel) {
					ed.getResourceSet().getURIConverter().getURIMap().put(rootModelURI, r.getURI());
				}
			}

			// Create a resource to load the result into
			rr = ed.getResourceSet().createResource(URI.createFileURI(solutionFile.getAbsolutePath()));
			final Resource pRR = rr;
			// Load in the result.
			ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, cipherProvider -> {
				final Map<String, URIConverter.Cipher> options = new HashMap<>();
				options.put(Resource.OPTION_CIPHER, cipherProvider.getSharedCipher());
				pRR.load(options);
			});

			final AbstractSolutionSet res = (AbstractSolutionSet) rr.getContents().get(0);
			assert res != null;

			// "Resolve" the references. The result references to the main scenario may be
			// "proxy" objects
			EcoreUtil.resolveAll(ed.getResourceSet());

			// Remove result from it's resource
			rr.getContents().clear();

			return res;
		} finally {
			if (rr != null) {
				// Remove the obsolete resource
				ed.getResourceSet().getResources().remove(rr);
			}
			// Clear the mapping
			ed.getResourceSet().getURIConverter().getURIMap().remove(rootModelURI);
		}
	}
}
