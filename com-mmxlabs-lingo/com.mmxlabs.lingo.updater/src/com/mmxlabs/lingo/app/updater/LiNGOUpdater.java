/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.updater;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.internal.p2.artifact.repository.ArtifactRepositoryManager;
import org.eclipse.equinox.internal.p2.metadata.repository.MetadataRepositoryManager;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.engine.PhaseSetFactory;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.operations.ProfileModificationJob;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.SynchronizeOperation;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.json.JSONObject;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.hub.common.http.ProgressResponseBody;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.lingo.app.updater.model.UpdateVersion;
import com.mmxlabs.lingo.app.updater.model.Version;
import com.mmxlabs.lingo.app.updater.util.SignatureByteProcessor;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSource;

public class LiNGOUpdater {

	public static RSAPublicKey readPublicKey(String resource) throws Exception {
		byte[] keyBytes;
		try (InputStream is = LiNGOUpdater.class.getResourceAsStream(resource)) {
			keyBytes = ByteStreams.toByteArray(is);
		}
		String key = new String(keyBytes, StandardCharsets.UTF_8);
		// Strip the header/footer from the encoded content
		String publicKeyPEM = key //
				.replace("-----BEGIN PUBLIC KEY-----", "") //
				.replaceAll(System.lineSeparator(), "") // Maybe be \r\n
				.replaceAll("\n", "") //
				.replace("-----END PUBLIC KEY-----", "");

		byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
		return (RSAPublicKey) keyFactory.generatePublic(keySpec);

	}

//	Digital signatures
// https://www.baeldung.com/java-digital-signature

	private static final String FILE_VERSION_JSON = "LiNGO/version.json";
	private static final String FILE_VERSION_JSON_ALT = "version.json";
	private static final String FILE_VERSIONS_JSON = "versions.json";

//	private static final String UPDATE_FOLDER = "updates";

	private String user = null;
	private String pw = null;

	private void withAuthHeader(Request.Builder b) {
		if (user != null && pw != null) {
			b.header("Authorization", Credentials.basic(user, pw));
		}
	}

	public void updateWithDialog(final String path) throws Exception {
		final ProgressMonitorDialog d = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
		d.run(true, false, monitor -> {
			update(path, monitor);
		});
	}

	public @Nullable Version getLiNGOVersion() {
		// TODO: This method should be a service.
		final String userHome = System.getProperty("eclipse.home.location");
		if (userHome != null) {
			try {
				final URL url = new URL(userHome + "/version.json");
				byte[] versionBytes;
				try (InputStream is = url.openStream()) {
					versionBytes = ByteStreams.toByteArray(is);
				}
				String jsonText = new String(versionBytes, StandardCharsets.UTF_8);
				JSONObject json = new JSONObject(jsonText);

				String string = json.getString("version");
				return Version.fromString(string);
			} catch (final IOException e) {
				// Ignore
				// Maybe better to catch some of these exception types and feedback to user?
			}
		}
		return null;

	}

	/**
	 * Returns the updates folder. Creates it if it does not exist;
	 * 
	 * @return
	 */
	public File getUpdatesFolder() {

		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		IPath updatesPath = workspaceLocation.append("updates/");
		File file = updatesPath.toFile();
		file.mkdirs();
		return file;
	}

	public void update(final String path, final IProgressMonitor monitor) {

		SubMonitor m = SubMonitor.convert(monitor);

		m.beginTask("Update", 100);
		try {

			URL url = null;
			UpdateVersion updateVersion = null;
			if (path != null) {
				url = new URL(path);
				updateVersion = getVersion(url);
			} else {
				List<URL> updateSites = getUpdateSites();
				for (URL u : updateSites) {
					final UpdateVersion version = getVersion(u);
					if (version != null) {
						if (updateVersion == null || version.isBetter(updateVersion)) {
							updateVersion = version;
							url = u;
						}
					}
				}

			}
			if (updateVersion != null) {
				final UpdateVersion pUpdateVersion = updateVersion;
				{
					Version v = getLiNGOVersion();
					if (v != null) {
						if (updateVersion.isBetter(v)) {
							boolean[] proceed = new boolean[1];
							Display.getDefault().syncExec(() -> {
								String msg = String.format("An update to LiNGO %s has been found.\nCurrent version is %s.\nProceed?", pUpdateVersion, v);
								if (MessageDialog.openQuestion(Display.getDefault().getActiveShell(), "Update found", msg)) {
									proceed[0] = true;
								}
							});
							if (!proceed[0]) {
								return;
							}
//							
//							
//							dialog. Update to lingo xxxx found.
//							Current version yyyy.
//							Update?()
						} else {
							Display.getDefault().syncExec(() -> {
								String msg = String.format("An update to LiNGO %s has been found. Current version is %s. No upgrade needed", pUpdateVersion, v);
								MessageDialog.openInformation(Display.getDefault().getActiveShell(), "No update found", msg);
							});
							return;
						}

					}

				}

				if (!versionExists(updateVersion)) {
					m.subTask("Downloading files");
					if (!downloadVersion(url, updateVersion, m.split(41))) {
						System.out.println("Downloaded file did not match expected checksum");
						return;
					}
					m.subTask("Downloading checksums");

					downloadVersionSignature(url, updateVersion, m.split(2));
					downloadVersionChecksum(url, updateVersion, m.split(2));

				} else {
				}

				m.subTask("Verifying download");

				if (!verifyDownload(updateVersion, m.split(5))) {
					return;
				}
				m.setWorkRemaining(50);
				File repo = new File(getUpdatesFolder(), updateVersion.getVersion() + ".zip");

				System.out.println("Updating to " + updateVersion.getVersion());
				m.subTask("Running updating");
				if (updateFromRepo(repo, m.split(50))) {
					// Trigger restart
					// TODO: Prompt user!
					Display.getDefault().asyncExec(() -> {
						if (MessageDialog.openQuestion(Display.getDefault().getActiveShell(), "Restart needed", "A restart is needed to complete update. Would you like to restart now?")) {
							// Note: JVM *may* need to somehow relaunch rather than restart
							PlatformUI.getWorkbench().restart();
						}
					});
				}
			}

		} catch (

		final Exception e) {
			// Message dialog!
			e.printStackTrace();

			Display.getDefault().asyncExec(() -> {
				MessageDialog.openError(Display.getDefault().getActiveShell(), "Error updating", e.getMessage());

			});
		}

	}

	public boolean versionExists(UpdateVersion uv) {

		File repo = new File(getUpdatesFolder(), uv.getVersion() + ".zip");

		return repo.exists();
	}

	public boolean verifyDownload(UpdateVersion uv, IProgressMonitor monitor) throws Exception {

		File repo = new File(getUpdatesFolder(), uv.getVersion() + ".zip");

		{
			File file = new File(getUpdatesFolder(), uv.getVersion() + ".sha256.sig");
			if (file.exists()) {
				// Read in signature
				byte[] encryptedMessageHash = Files.readAllBytes(file.toPath());
				RSAPublicKey publicKey = readPublicKey("/certs/update-cert.pem");

				Security.addProvider(new BouncyCastleProvider());

				Signature signature = Signature.getInstance("SHA256WithRSA", "BC");
				signature.initVerify(publicKey);

				try (FileInputStream fin = new FileInputStream(repo)) {
					try (BufferedInputStream bufin = new BufferedInputStream(fin)) {
						ByteStreams.readBytes(bufin, new SignatureByteProcessor(signature));
					}
				}

				boolean validSig = signature.verify(encryptedMessageHash);

				System.out.println("Valid sig? " + validSig);

				return validSig;
			}
		}

		{

			File file = new File(getUpdatesFolder(), uv.getVersion() + ".sha256");
			if (file.exists()) {

				MessageDigest md = MessageDigest.getInstance("SHA-256");
				try (FileInputStream fout = new FileInputStream(repo)) {
					try (DigestInputStream out = new DigestInputStream(fout, md)) {
						ByteStreams.copy(out, ByteStreams.nullOutputStream());
					}
				}

				// Expected hash
				byte[] actualHashBytes = md.digest();

				String expected = Files.readString(file.toPath());

				// If we have a valid download, then move tmp file to real location
				if (expected != null && actualHashBytes != null)

				{
					final StringBuilder sb = new StringBuilder();
					for (final byte b : actualHashBytes) {
						sb.append(String.format("%02X", b));
					}
					String str = sb.toString().toLowerCase();
					// Ass
					String expectedSum = expected.split(" ")[0].toLowerCase();

					boolean validChecksum = str.equals(expectedSum);
					System.out.println("Checksum match? " + validChecksum);
					return validChecksum;
				}
			}
		}
		return false;
	}

	public boolean downloadVersion(URL baseUrl, UpdateVersion uv, IProgressMonitor monitor) throws Exception {
		URL url = expandURL(baseUrl);
		SubMonitor progress = SubMonitor.convert(monitor);
		progress.beginTask("Download", 100);
//		try {

		// TODO: Sanitise version
		File file = new File(getUpdatesFolder(), uv.getVersion() + ".zip");
		byte[] messageHash = null;

		// Download to a temp file.
		if (true) {

			final IProgressListener progressListener = WrappedProgressMonitor.wrapMonitor(progress.split(99));
			OkHttpClient.Builder clientBuilder = HttpClientUtil.basicBuilder();
			if (progressListener != null) {
				clientBuilder = clientBuilder.addNetworkInterceptor(new Interceptor() {
					@Override
					public Response intercept(final Chain chain) throws IOException {
						final Response originalResponse = chain.proceed(chain.request());
						return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), progressListener)).build();
					}
				});
			}
			final OkHttpClient localHttpClient = clientBuilder //
					.build();

			final Request.Builder requestBuilder = new Request.Builder() //
					.url(url) //
			;
			withAuthHeader(requestBuilder);

			final Request request = requestBuilder //
					.build();
			try (Response response = localHttpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					throw new IOException("" + response);
				}

				MessageDigest md = MessageDigest.getInstance("SHA-256");
				try (FileOutputStream fout = new FileOutputStream(file)) {
					try (DigestOutputStream out = new DigestOutputStream(fout, md)) {

						try (BufferedSource bufferedSource = response.body().source()) {
							ByteStreams.copy(bufferedSource.inputStream(), out);
						}
					}

				}

				// Expected hash
				messageHash = md.digest();
			}
		}
		return true;

	}

	public void downloadVersionChecksum(URL baseUrl, UpdateVersion uv, IProgressMonitor monitor) throws Exception {
		URL url = new URL(expandURL(baseUrl).toString() + ".sha256");
		SubMonitor progress = SubMonitor.convert(monitor);
		progress.beginTask("Download", 100);
//		try {

		// TODO: Sanitise version
		File file = new File(getUpdatesFolder(), uv.getVersion() + ".sha256");

		final IProgressListener progressListener = WrappedProgressMonitor.wrapMonitor(progress.split(99));
		OkHttpClient.Builder clientBuilder = HttpClientUtil.basicBuilder();
		if (progressListener != null) {
			clientBuilder = clientBuilder.addNetworkInterceptor(new Interceptor() {
				@Override
				public Response intercept(final Chain chain) throws IOException {
					final Response originalResponse = chain.proceed(chain.request());
					return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), progressListener)).build();
				}
			});
		}
		final OkHttpClient localHttpClient = clientBuilder //
				.build();

		final Request.Builder requestBuilder = new Request.Builder() //
				.url(url) //
		;
		withAuthHeader(requestBuilder);

		final Request request = requestBuilder //
				.build();
		try (Response response = localHttpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("" + response);
			}

			try (FileOutputStream fout = new FileOutputStream(file)) {
				try (BufferedSource bufferedSource = response.body().source()) {
					ByteStreams.copy(bufferedSource.inputStream(), fout);
				}
			}
		}
//		return file;
	}

	public void downloadVersionSignature(URL baseUrl, UpdateVersion uv, IProgressMonitor monitor) throws Exception {
		URL url = new URL(expandURL(baseUrl).toString() + ".sha256.sig");
		SubMonitor progress = SubMonitor.convert(monitor);
		progress.beginTask("Download", 100);
//		try {

		// TODO: Sanitise version
		File file = new File(getUpdatesFolder(), uv.getVersion() + ".sha256.sig");

		final IProgressListener progressListener = WrappedProgressMonitor.wrapMonitor(progress.split(99));
		OkHttpClient.Builder clientBuilder = HttpClientUtil.basicBuilder();
		if (progressListener != null) {
			clientBuilder = clientBuilder.addNetworkInterceptor(new Interceptor() {
				@Override
				public Response intercept(final Chain chain) throws IOException {
					final Response originalResponse = chain.proceed(chain.request());
					return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), progressListener)).build();
				}
			});
		}
		final OkHttpClient localHttpClient = clientBuilder //
				.build();

		final Request.Builder requestBuilder = new Request.Builder() //
				.url(url) //
		;
		withAuthHeader(requestBuilder);

		final Request request = requestBuilder //
				.build();
		try (Response response = localHttpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("" + response);
			}

			try (FileOutputStream fout = new FileOutputStream(file)) {
				try (BufferedSource bufferedSource = response.body().source()) {
					ByteStreams.copy(bufferedSource.inputStream(), fout);
				}
			}
		}
//		return file;
	}

	public UpdateVersion getVersion(final URL baseUrl) throws Exception {
		// If URL ends in .zip - then assume file.
		URL url = expandURL(baseUrl);
		final AtomicInteger bytesRead2 = new AtomicInteger();

		if (url.toString().startsWith("http")) {

			OkHttpClient.Builder clientBuilder = HttpClientUtil.basicBuilder();

			if (true) {
				// Debugging to check we only download part of the file
				clientBuilder = clientBuilder.addNetworkInterceptor(new Interceptor() {
					@Override
					public Response intercept(final Chain chain) throws IOException {
						final Response originalResponse = chain.proceed(chain.request());
						return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), new IProgressListener() {
							@Override
							public void update(final long bytesRead, final long contentLength, final boolean done) {
								bytesRead2.addAndGet((int) bytesRead);

							}
						})).build();
					}
				});
			}

			final OkHttpClient localHttpClient = clientBuilder //
					.build();
			final Request.Builder requestBuilder = new Request.Builder() //
					.url(url) //
			;
			withAuthHeader(requestBuilder);

			final Request request = requestBuilder //
					.build();
			try (Response response = localHttpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					System.out.println("Server returned " + response.code());
					return null;
				}

				final AtomicInteger bytesRead = new AtomicInteger();

				// Note: Downloading from Jenkins (via nginx) would grab 8k (inc headers)
				// Downloading from s3 (via apache) downloads the whole file
				// (Because it was an old file with the version.json at the end rather than the
				// beginning)

				// Read up to 8k bytes of the upstream file. This sets a hard limit on the
				// amount downloaded. We will alway grab 8k (unless the file is smaller).
				try (InputStream is = response.peekBody(1024L * 8L).byteStream()) {

					// 512 byte buffer. we don't expect version.json to be very big. This is a soft
					// limit - we download 512k at a time, but can still download the whole file if
					// needed
					try (BufferedInputStream bis = new BufferedInputStream(is, 512)) {

						try (ZipInputStream zis = new ZipInputStream(bis)) {
							ZipEntry entry = zis.getNextEntry();
							while (entry != null && (entry.isDirectory() || !entry.getName().contains("version"))) {
								entry = zis.getNextEntry();
							}

							if (FILE_VERSION_JSON.equals(entry.getName()) || FILE_VERSION_JSON_ALT.equals(entry.getName())) {
								final ByteArrayOutputStream out = new ByteArrayOutputStream();
								ByteStreams.copy(zis, out);
								final String str = out.toString();

								final ObjectMapper mapper = new ObjectMapper();
								final UpdateVersion uv = mapper.readValue(str, UpdateVersion.class);
								return uv;
							}
						}
					}
				} catch (EOFException e) {
					// If we hit here, then we were unable to find version.json at the start of the
					// zip file.

				} finally {
					System.out.println(bytesRead + " -- " + bytesRead2);
				}
			}
		}

		return null;
	}

	private URL expandURL(final URL baseUrl) throws MalformedURLException {
		URL url = null;
		if (baseUrl.toString().endsWith(".zip")) {
			// Actual repo file
			url = baseUrl;
		} else {
			// Assume a directory - look for update.zip
			final String sep = baseUrl.toString().endsWith("/") ? "" : "/";
			url = new URL(baseUrl.toString() + sep + "update.zip");
		}
		return url;
	}

	/** Get the list of currently enabled update sites */
	public List<URL> getUpdateSites() {

		final BundleContext context = FrameworkUtil.getBundle(LiNGOUpdater.class).getBundleContext();
		final ServiceReference<IProvisioningAgentProvider> providerRef = context.getServiceReference(IProvisioningAgentProvider.class);
		if (providerRef == null) {
			throw new RuntimeException("No provisioning agent provider is available"); //$NON-NLS-1$
		}
		try {
			final IProvisioningAgentProvider provider = context.getService(providerRef);
			if (provider == null) {
				throw new RuntimeException("No provisioning agent provider is available"); //$NON-NLS-1$
			}

			final IProvisioningAgent agent = provider.createAgent(null); // null = location for running system
			if (agent == null) {
				throw new RuntimeException("Location was not provisioned by p2");
			}

			IMetadataRepositoryManager mgr = (IMetadataRepositoryManager) agent.getService(IMetadataRepositoryManager.class.getName());
			URI[] knownRepositories = mgr.getKnownRepositories(IMetadataRepositoryManager.REPOSITORIES_ALL);
			List<URL> urls = new LinkedList<>();
			for (var u : knownRepositories) {
				urls.add(u.toURL());
			}
			return urls;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public boolean updateFromRepo(final File file, IProgressMonitor monitor) throws Exception {

		final String UPDATE_SITE_URL = "jar:" + file.toURI().toString() + "!/LiNGO";

		final SubMonitor progress = SubMonitor.convert(monitor, 100);

		final BundleContext context = FrameworkUtil.getBundle(LiNGOUpdater.class).getBundleContext();
		final ServiceReference<IProvisioningAgentProvider> providerRef = context.getServiceReference(IProvisioningAgentProvider.class);
		if (providerRef == null) {
			throw new RuntimeException("No provisioning agent provider is available"); //$NON-NLS-1$
		}
		try {
			final IProvisioningAgentProvider provider = context.getService(providerRef);
			if (provider == null) {
				throw new RuntimeException("No provisioning agent provider is available"); //$NON-NLS-1$
			}

			// See
			// http://wiki.eclipse.org/Equinox/p2/FAQ#But_why_aren.27t_uninstalled_bundles.2Ffeatures_immediately_removed.3F
			// IProvisioningAgentProvider provider = // obtain the
			// IProvisioningAgentProvider using OSGi services
			final IProvisioningAgent agent = provider.createAgent(null); // null = location for running system
			if (agent == null) {
				throw new RuntimeException("Location was not provisioned by p2");
			}
			final ProvisioningSession session = new ProvisioningSession(agent);

//					   //get the repository managers and define our repositories
			final IMetadataRepositoryManager manager = new MetadataRepositoryManager(agent);
			final IArtifactRepositoryManager artifactManager = new ArtifactRepositoryManager(agent);
			manager.addRepository(new URI(UPDATE_SITE_URL));
			artifactManager.addRepository(new URI(UPDATE_SITE_URL));

			final UpdateOperation operation = new UpdateOperation(session);

			final IProvisioningPlan provisioningPlan = operation.getProvisioningPlan();

			operation.getProvisioningContext().setArtifactRepositories(new URI[] { new URI(UPDATE_SITE_URL) });
			operation.getProvisioningContext().setMetadataRepositories(new URI[] { new URI(UPDATE_SITE_URL) });

			final IStatus status = operation.resolveModal(progress.split(10));
			System.out.println(status.getMessage());

			final ProvisioningJob provisioningJob = operation.getProvisioningJob(progress.split(10));

			// Skip the bundle verification step as we will assume content is validated from
			// zip
			if (provisioningJob instanceof ProfileModificationJob) {
				final ProfileModificationJob profileModificationJob = (ProfileModificationJob) provisioningJob;
				profileModificationJob.setPhaseSet(PhaseSetFactory.createDefaultPhaseSetExcluding(new String[] { PhaseSetFactory.PHASE_CHECK_TRUST }));
			}

			final IStatus st = provisioningJob.runModal(progress.split(30));

			final int ii = 0;
			System.out.println(st.getMessage());

			if (st != null && st.isOK()) {
				return true;
			}

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// When you're done, make sure you 'unget' the service.
			context.ungetService(providerRef);
		}

		return false;
	}

	public boolean installFromRepo(final File file, IProgressMonitor monitor) throws Exception {

		final String UPDATE_SITE_URL = "jar:" + file.toURI().toString() + "!/LiNGO";

		final SubMonitor progress = SubMonitor.convert(monitor, 100);

		final BundleContext context = FrameworkUtil.getBundle(LiNGOUpdater.class).getBundleContext();
		final ServiceReference<IProvisioningAgentProvider> providerRef = context.getServiceReference(IProvisioningAgentProvider.class);
		if (providerRef == null) {
			throw new RuntimeException("No provisioning agent provider is available"); //$NON-NLS-1$
		}
		try {
			final IProvisioningAgentProvider provider = context.getService(providerRef);
			if (provider == null) {
				throw new RuntimeException("No provisioning agent provider is available"); //$NON-NLS-1$
			}

			// See
			// http://wiki.eclipse.org/Equinox/p2/FAQ#But_why_aren.27t_uninstalled_bundles.2Ffeatures_immediately_removed.3F
			// IProvisioningAgentProvider provider = // obtain the
			// IProvisioningAgentProvider using OSGi services
			final IProvisioningAgent agent = provider.createAgent(null); // null = location for running system
			if (agent == null)
				throw new RuntimeException("Location was not provisioned by p2");
//					String UPDATE_SITE_URL = String.format("jar:%s!/LiNGO", file.toURI());
			final ProvisioningSession session = new ProvisioningSession(agent);

//					   //get the repository managers and define our repositories
			final IMetadataRepositoryManager manager = new MetadataRepositoryManager(agent);
//					   IMetadataRepositoryManager manager = 
			final IArtifactRepositoryManager artifactManager = new ArtifactRepositoryManager(agent);
			manager.addRepository(new URI(UPDATE_SITE_URL));
			artifactManager.addRepository(new URI(UPDATE_SITE_URL));

//					// Load and query the metadata
			IMetadataRepository metadataRepo = manager.loadRepository(new URI(UPDATE_SITE_URL), progress.split(25));
			Collection toInstall = metadataRepo.query(QueryUtil.createIUQuery("com.mmxlabs.lingo.feature.feature.group"), progress.split(25)).toUnmodifiableSet();
//					Collection toInstall2 = metadataRepo.query(QueryUtil.createIUQuery("com.mmxlabs.lingo.?.feature.feature.group"),progress.split(25)).toUnmodifiableSet();
//					Collection toInstall3 = metadataRepo.query(QueryUtil.createIUQuery("com.mmxlabs.lingo.app.product.id"),progress.split(25)).toUnmodifiableSet();
//
			InstallOperation operation = new InstallOperation(session, toInstall);
//agent.

			SynchronizeOperation operation2 = new SynchronizeOperation(session, toInstall);

//			final UpdateOperation operation = new UpdateOperation(session);

			final IProvisioningPlan provisioningPlan = operation.getProvisioningPlan();
//					provisioningPlan.
//					op
//					operation.setSelectedUpdates(null);
			operation.getProvisioningContext().setArtifactRepositories(new URI[] { new URI(UPDATE_SITE_URL) });
			operation.getProvisioningContext().setMetadataRepositories(new URI[] { new URI(UPDATE_SITE_URL) });

			final IStatus status = operation.resolveModal(progress.split(10));
			System.out.println(status.getMessage());

			final ProvisioningJob provisioningJob = operation.getProvisioningJob(progress.split(10));

			// Skip the bundle verification step as we will assume content is validated from
			// zip
			if (provisioningJob instanceof ProfileModificationJob profileModificationJob) {
				profileModificationJob.setPhaseSet(PhaseSetFactory.createDefaultPhaseSetExcluding(new String[] { PhaseSetFactory.PHASE_CHECK_TRUST }));
			}

			final IStatus st = provisioningJob.runModal(progress.split(30));

			final int ii = 0;
			System.out.println(st.getMessage());

			if (st != null && st.isOK()) {
				return true;
			}

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// When you're done, make sure you 'unget' the service.
			context.ungetService(providerRef);
		}

		return false;
	}

	public void setAuth(String user, String pw) {
		this.user = user;
		this.pw = pw;

	}
}
