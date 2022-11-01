/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.KeyFactory;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.internal.p2.operations.IStatusCodes;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.engine.PhaseSetFactory;
import org.eclipse.equinox.p2.operations.ProfileModificationJob;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.hub.common.http.IProgressListener;
import com.mmxlabs.hub.common.http.ProgressResponseBody;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.lingo.app.updater.auth.IUpdateAuthenticationProvider;
import com.mmxlabs.lingo.app.updater.model.UpdateVersion;
import com.mmxlabs.lingo.app.updater.model.Version;
import com.mmxlabs.lingo.app.updater.util.SignatureByteProcessor;
import com.mmxlabs.rcp.common.ServiceHelper;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSource;

/**
 * 
 * openssl dgst -sha256 -verify my-pub.pem -signature in.txt.sha256 in.txt //
 * 
 * Digital signatures https://www.baeldung.com/java-digital-signature
 *
 */
public class LiNGOUpdater {

	private static final Logger LOGGER = LoggerFactory.getLogger(LiNGOUpdater.class);

	public static RSAPublicKey readPublicKey(final String resource) throws Exception {
		byte[] keyBytes;
		try (InputStream is = LiNGOUpdater.class.getResourceAsStream(resource)) {
			keyBytes = ByteStreams.toByteArray(is);
		}
		final String key = new String(keyBytes, StandardCharsets.UTF_8);
		// Strip the header/footer from the encoded content
		final String publicKeyPEM = key //
				.replace("-----BEGIN PUBLIC KEY-----", "") //
				.replaceAll(System.lineSeparator(), "") // Maybe be \r\n
				.replaceAll("\n", "") //
				.replace("-----END PUBLIC KEY-----", "");

		final byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

		final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
		return (RSAPublicKey) keyFactory.generatePublic(keySpec);

	}

	private static final String FILE_VERSION_JSON = "LiNGO/version.json";
	private static final String FILE_VERSION_JSON_ALT = "version.json";
	private static final String FILE_VERSIONS_JSON = "versions.json";

//	private static final String UPDATE_FOLDER = "updates";

	private String user = null;
	private String pw = null;

	private boolean performSigCheck = true;

	private void withAuthHeader(final URL url, final Request.Builder b) {
		if (user != null && pw != null) {
			b.header("Authorization", Credentials.basic(user, pw, StandardCharsets.UTF_8));
		} else {

			ServiceHelper.withOptionalServiceConsumer(IUpdateAuthenticationProvider.class, p -> {
				if (p != null) {
					try {
						final Pair<String, String> header = p.provideAuthenticationHeader(url);
						if (header != null) {
							b.addHeader(header.getFirst(), header.getSecond());
						}
					} catch (final Exception exception) {
						// Error generating the token
						exception.printStackTrace();
					}
				}
			});
		}
	}

	public void updateWithDialog(final @Nullable String path) throws Exception {
		final ProgressMonitorDialog d = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
		d.run(true, false, monitor -> update(path, monitor));
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
				final String jsonText = new String(versionBytes, StandardCharsets.UTF_8);
				final JSONObject json = new JSONObject(jsonText);

				final String string = json.getString("version");
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
		final IPath updatesPath = workspaceLocation.append("updates/");
		final File file = updatesPath.toFile();
		file.mkdirs();
		return file;
	}

	public void update(final @Nullable String path, final IProgressMonitor monitor) {

		final SubMonitor m = SubMonitor.convert(monitor);

		m.beginTask("Update", 100);
		try {

			URL url = null;
			UpdateVersion updateVersion = null;
			if (path != null) {
				url = new URL(path);
				updateVersion = getVersion(url);
			} else {
				final List<URL> updateSites = getUpdateSites();
				if (updateSites.isEmpty()) {
					Display.getDefault().asyncExec(() -> MessageDialog.openError(Display.getDefault().getActiveShell(), "Error updating", "No enabled update sites found"));
					return;
				}
				for (final URL u : updateSites) {
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
					final Version v = getLiNGOVersion();
					if (v == null || updateVersion.isBetter(v)) {
						final boolean[] proceed = new boolean[1];
						Display.getDefault().syncExec(() -> {
							final String msg;
							if (v == null) {
								msg = String.format("An update to LiNGO %s has been found.\nProceed?", pUpdateVersion);
							} else {
								msg = String.format("An update to LiNGO %s has been found.\nCurrent version is %s.\nProceed?", pUpdateVersion, v);
							}
							if (MessageDialog.openQuestion(Display.getDefault().getActiveShell(), "Update found", msg)) {
								proceed[0] = true;
							}
						});
						if (!proceed[0]) {
							return;
						}

					} else {
						Display.getDefault().syncExec(() -> {
							final String msg = String.format("LiNGO %s has been found. Current version is %s. No upgrade needed", pUpdateVersion, v);
							MessageDialog.openInformation(Display.getDefault().getActiveShell(), "No update found", msg);
						});
						return;
					}
				}

				m.setTaskName(String.format("Updating to %s", updateVersion.getVersion()));

				if (!versionExists(updateVersion)) {
					if (performSigCheck) {
						m.subTask("Downloading update signature");
						downloadVersionSignature(url, updateVersion, m.split(2));
					}
					// TODO: Move sig check into the download loop
					m.subTask("Downloading update file");
					downloadVersion(url, updateVersion, m.split(41));
				}

				m.subTask("Verifying download");

				if (performSigCheck && !verifyDownload(updateVersion, m.split(5))) {
					// Downloaded file signature failed
					MessageDialog.openError(Display.getDefault().getActiveShell(), "Download failure", "Download signature check failed. Please retry.");
					// Cleanup downloads so we are clean for a retry
					cleanUpdatesFolder();
					return;
				}

				m.setWorkRemaining(50);

				final File repo = new File(getUpdatesFolder(), updateVersion.getVersion() + ".zip");

				m.subTask("Running updating");
				if (updateFromRepo(repo, m.split(50))) {
					Display.getDefault().asyncExec(() -> {
						if (MessageDialog.openQuestion(Display.getDefault().getActiveShell(), "Restart needed", "A restart is needed to complete update. Would you like to restart now?")) {
							// Note: JVM *may* need to somehow relaunch rather than restart
							PlatformUI.getWorkbench().restart();
						}
					});
				}
			}
		} catch (final Exception e) {
			LOGGER.error("Error updating " + e.getMessage(), e);
			Display.getDefault().asyncExec(() -> MessageDialog.openError(Display.getDefault().getActiveShell(), "Error updating", e.getMessage() + "\nPlease try again"));
		} finally {
			// Always try to clean up download folder
			cleanUpdatesFolder();
		}
	}

	/**
	 * Remove all files in the updates folder
	 */
	private void cleanUpdatesFolder() {

		final File updatesFolder = getUpdatesFolder();
		if (updatesFolder != null && updatesFolder.exists() && updatesFolder.isDirectory()) {
			try {
				final Path tempDir = updatesFolder.toPath();
				Files.walkFileTree(tempDir, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
						Files.delete(dir);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
						Files.delete(file);
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (final IOException e) {
				LOGGER.error("Error cleaning updates folder " + e.getMessage(), e);
			}
		}
	}

	private boolean versionExists(final UpdateVersion uv) {

		final File repo = new File(getUpdatesFolder(), uv.getVersion() + ".zip");
		final File sig = new File(getUpdatesFolder(), uv.getVersion() + ".sha256.sig");

		return repo.exists() && (!performSigCheck || sig.exists());
	}

	private boolean verifyDownload(final UpdateVersion uv, final IProgressMonitor monitor) throws Exception {

		final File repo = new File(getUpdatesFolder(), uv.getVersion() + ".zip");

		{
			final File file = new File(getUpdatesFolder(), uv.getVersion() + ".sha256.sig");
			if (file.exists()) {
				// Read in signature
				final byte[] encryptedMessageHash = Files.readAllBytes(file.toPath());
				final RSAPublicKey publicKey = readPublicKey("/certs/update-cert.pem");

				// TODO: We probably should not do this on every call...
				Security.addProvider(new BouncyCastleProvider());

				final Signature signature = Signature.getInstance("SHA256WithRSA", "BC");
				signature.initVerify(publicKey);

				try (FileInputStream fin = new FileInputStream(repo)) {
					try (BufferedInputStream bufin = new BufferedInputStream(fin)) {
						ByteStreams.readBytes(bufin, new SignatureByteProcessor(signature));
					}
				}

				return signature.verify(encryptedMessageHash);
			}
		}
		return false;
	}

	private void downloadVersion(final URL baseUrl, final UpdateVersion uv, final IProgressMonitor monitor) throws Exception {
		final URL url = expandURL(baseUrl);
		final SubMonitor progress = SubMonitor.convert(monitor);
		progress.beginTask("Download", 100);

		final File file = new File(getUpdatesFolder(), uv.getVersion() + ".zip");

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
		withAuthHeader(url, requestBuilder);

		final Request request = requestBuilder //
				.build();
		try (Response response = localHttpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				throw new IOException("" + response);
			}

			try (FileOutputStream out = new FileOutputStream(file)) {
				try (BufferedSource bufferedSource = response.body().source()) {
					ByteStreams.copy(bufferedSource.inputStream(), out);
				}
			}
		}
	}

	private void downloadVersionSignature(final URL baseUrl, final UpdateVersion uv, final IProgressMonitor monitor) throws Exception {
		final URL url = new URL(expandURL(baseUrl).toString() + ".sha256.sig");
		final SubMonitor progress = SubMonitor.convert(monitor);
		progress.beginTask("Download", 100);

		final File file = new File(getUpdatesFolder(), uv.getVersion() + ".sha256.sig");

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
		withAuthHeader(url, requestBuilder);

		final Request request = requestBuilder //
				.build();
		try (Response response = localHttpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				if (response.code() == 404) {
					throw new RuntimeException("No signature file found at " + url);
				} else {
					throw new RuntimeException("Unable to find signature file at " + url + " Code " + response.code());
				}
			}

			try (FileOutputStream fout = new FileOutputStream(file)) {
				try (BufferedSource bufferedSource = response.body().source()) {
					ByteStreams.copy(bufferedSource.inputStream(), fout);
				}
			}
		}
	}

	private UpdateVersion getVersion(final URL baseUrl) throws Exception {
		// If URL ends in .zip - then assume file.
		final URL url = expandURL(baseUrl);

		if (url.toString().startsWith("http")) {

			final OkHttpClient.Builder clientBuilder = HttpClientUtil.basicBuilder();

			final OkHttpClient localHttpClient = clientBuilder //
					.build();
			final Request.Builder requestBuilder = new Request.Builder() //
					.url(url) //
			;
			withAuthHeader(url, requestBuilder);

			final Request request = requestBuilder //
					.build();
			try (Response response = localHttpClient.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					Display.getDefault().asyncExec(() -> {
						final String msg = String.format("Failed to connect to update server - HTTP Error %d", response.code());
						MessageDialog.openInformation(Display.getDefault().getActiveShell(), "No update found", msg);
					});
					return null;
				}

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
							if (entry != null) {
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
					}
				} catch (final EOFException e) {
					// If we hit here, then we were unable to find version.json at the start of the
					// zip file.
					Display.getDefault().asyncExec(() -> {
						final String msg = String.format("Malformed update file found");
						MessageDialog.openInformation(Display.getDefault().getActiveShell(), "No update found", msg);
					});
					return null;
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
	private List<URL> getUpdateSites() {

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

			final IMetadataRepositoryManager mgr = (IMetadataRepositoryManager) agent.getService(IMetadataRepositoryManager.class.getName());
			final URI[] knownRepositories = mgr.getKnownRepositories(IRepositoryManager.REPOSITORIES_ALL);
			final List<URL> urls = new LinkedList<>();
			for (final var u : knownRepositories) {
				urls.add(u.toURL());
			}
			return urls;

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

	}

	private boolean updateFromRepo(final File file, final IProgressMonitor monitor) throws Exception {

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
			// final IMetadataRepositoryManager manager = new
			// MetadataRepositoryManager(agent);
			// final IArtifactRepositoryManager artifactManager = new
			// ArtifactRepositoryManager(agent);
			// manager.addRepository(new URI(UPDATE_SITE_URL));
			// artifactManager.addRepository(new URI(UPDATE_SITE_URL));

			final UpdateOperation operation = new UpdateOperation(session);

//			final IProvisioningPlan provisioningPlan = operation.getProvisioningPlan();

			final URI updateSiteURI = new URI(UPDATE_SITE_URL);
			operation.getProvisioningContext().setArtifactRepositories(updateSiteURI);
			operation.getProvisioningContext().setMetadataRepositories(updateSiteURI);

			final IStatus status = operation.resolveModal(progress.split(10));

			if (!status.isOK()) {

				if (status.getCode() == IStatusCodes.NOTHING_TO_UPDATE) {
					Display.getDefault().asyncExec(() -> {
						MessageDialog.openError(Display.getDefault().getActiveShell(), "Update failed", "No updates found in update file");
					});
				} else {
					Display.getDefault().asyncExec(() -> {
						MessageDialog.openError(Display.getDefault().getActiveShell(), "Update failed", "Update was unable to complete " + status.getMessage());
					});
				}
				return false;
			}

			final ProvisioningJob provisioningJob = operation.getProvisioningJob(progress.split(10));

			// Skip the bundle verification step as we will assume content is validated from
			// zip
			if (provisioningJob instanceof final ProfileModificationJob profileModificationJob) {
				profileModificationJob.setPhaseSet(PhaseSetFactory.createDefaultPhaseSetExcluding(new String[] { PhaseSetFactory.PHASE_CHECK_TRUST }));
			}

			final IStatus st = provisioningJob.runModal(progress.split(30));
			if (st.isOK()) {
				return true;
			} else {
				Display.getDefault().asyncExec(() -> {
					MessageDialog.openError(Display.getDefault().getActiveShell(), "Update failed", "Update was unable to complete " + st.getMessage());
				});
			}

		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
			Display.getDefault().asyncExec(() -> {
				MessageDialog.openError(Display.getDefault().getActiveShell(), "Update failed", "Exception during update " + e.getMessage());
			});
		} finally {
			// When you're done, make sure you 'unget' the service.
			context.ungetService(providerRef);
		}

		return false;
	}

	public void setAuth(final String user, final String pw) {
		this.user = user;
		this.pw = pw;
	}

	public void disableSigCheck(final boolean b) {
		this.performSigCheck = !b;

	}

}
