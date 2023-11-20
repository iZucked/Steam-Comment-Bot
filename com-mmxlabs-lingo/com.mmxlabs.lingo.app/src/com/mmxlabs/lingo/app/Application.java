/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.app;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.internal.p2.garbagecollector.GarbageCollector;
import org.eclipse.equinox.internal.p2.metadata.repository.MetadataRepositoryManager;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.io.FileDeleter;
import com.mmxlabs.common.time.Days;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.auth.AuthenticationManager;
import com.mmxlabs.hub.license.HubLicenseManager;
import com.mmxlabs.hub.services.permissions.UserPermissionsService;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.license.features.pluginxml.PluginRegistryHook;
import com.mmxlabs.license.ssl.LicenseChecker;
import com.mmxlabs.license.ssl.LicenseManager;
import com.mmxlabs.license.ssl.LicenseState;
import com.mmxlabs.lingo.reports.customizable.CustomReportsRegistryHook;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.application.DelayedOpenFileProcessor;
import com.mmxlabs.rcp.common.application.WorkbenchStateManager;
import com.mmxlabs.rcp.common.locking.WellKnownTriggers;
import com.mmxlabs.rcp.common.viewfactory.ReplaceableViewManager;
import com.mmxlabs.scenario.service.model.manager.ISharedDataModelType;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.WorkspaceReencrypter;
import com.mmxlabs.scenario.service.model.util.encryption.impl.DelegatingEMFCipher;

/**
 * This class controls all aspects of the application's execution
 */
@SuppressWarnings("restriction")
public class Application implements IApplication {

	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	private final AuthenticationManager authenticationManager = AuthenticationManager.getInstance();

	private Object applicationExitCode;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app. IApplicationContext)
	 */
	@Override
	public Object start(final IApplicationContext context) throws Exception {

		// Various attempts to disable dark mode. However the eclipse theme takes over later.
		// OS.SetPreferredAppMode(3); // Force light mode
		// System.setProperty("org.eclipse.swt.internal.win32.disableCustomThemeTweaks", "true");
		// System.setProperty("org.eclipse.swt.internal.win32.useDarkModeExplorerTheme", "false");

		cleanUpTemporaryFolder();

		// Hacky code to ensure initialised
		final ISharedDataModelType<PortModel> distances = LNGScenarioSharedModelTypes.DISTANCES;

		// Start peaberry activation - only for ITS runs inside eclipse.
		final String[] bundlesToStart = { "org.eclipse.equinox.common", //
				"org.eclipse.equinox.event", //
				"org.ops4j.peaberry.activation", //
		};
		// The auto-start the embedded webserver, include the following in the bundlesToStart array.
		// "org.eclipse.equinox.http.jetty", //
		// "org.eclipse.equinox.http.registry", //

		for (final String bundleName : bundlesToStart) {
			try {
				Platform.getBundle(bundleName).start();
			} catch (final BundleException e1) {
				e1.printStackTrace();
			}
		}
		final var display = PlatformUI.createDisplay();

		final Location instanceLoc = Platform.getInstanceLocation();
		try {
			if (instanceLoc == null) {
				// Need a workspace
				return IApplication.EXIT_OK;
			}
			if (instanceLoc.isSet()) {
				// Attempt to lock workspace
				if (!instanceLoc.lock()) {
					// Tell user about lock
					MessageDialog.openError(display.getActiveShell(), "Startup Error", "The workspace is in use by another instance of LiNGO.");
					display.dispose();
					return IApplication.EXIT_OK;
				}

				initAccessControl();
				WorkbenchStateManager.cleanupWorkbenchState();

				displayProgressMonitor(display);

				if (applicationExitCode == ApplicationCode.CONTINUE) {
					// Old Experimental code to handle .lingo file associations in windows and
					// automatically import into LiNGO. This pre-dates the current scenario service
					// and does not currently do
					// anything.
					final var processor = new DelayedOpenFileProcessor(display);
					final int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor(processor));
					if (returnCode == PlatformUI.RETURN_RESTART) {
						return IApplication.EXIT_RESTART;
					}
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (instanceLoc != null) {
				instanceLoc.release();
			}
			// clean exit
			display.dispose();
		}
		return IApplication.EXIT_OK;
	}

	/**
	 * Runs the 5 following startup tasks and monitors their progress with the IProgressMonitor: login, permission check, license check, p2 cleanup, (optional) re-encryption
	 *
	 * @param display
	 * @param monitor
	 * @return an CONTINUE {@see com.mmxlabs.lingo.app.intro.ApplicationCode} or IApplication.EXIT_OK
	 * @throws Exception
	 */
	public Object startupTasks(final Display display, final IProgressMonitor monitor) throws Exception {
		final var subMonitor = SubMonitor.convert(monitor, 5);

		datahubLogin(subMonitor.split(1));
		if (!datahubPermissionCheck(display, subMonitor.split(1))) {
			// Method call will fire up a dialog if needed.
			return IApplication.EXIT_OK;
		}

		if (!licenseCheck(subMonitor.split(1))) {
			if (System.getProperty("lingo.suppress.dialogs") == null) {
				MessageDialog.openError(display.getActiveShell(), "License Error", "Unable to validate license");
			} else {
				System.err.println( "Unable to validate license");
			}
			return IApplication.EXIT_OK;
		}
		if (System.getProperty("lingo.suppress.dialogs") == null) {
			Date c = null;
			if ((c = licenseCheckExpiresSoon(14, subMonitor.split(1))) != null) {
				final LocalDate d = LocalDate.ofInstant(c.toInstant(), ZoneId.systemDefault());
				MessageDialog.openWarning(display.getActiveShell(), "License expiring", String.format("LiNGO license expires soon (%s - %d days). Please check for updates or contact Minimax Labs.",
						d.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)), Days.between(LocalDate.now(), d)));
			}
		}

		// Don't abort LiNGO if p2 garbage collect fails.
		// For some reason this started to happen ~14 Dec 2017
		try {
			cleanupP2(subMonitor.split(1));
		} catch (final Exception e) {
			LOG.error("Error during P2 cleanup: " + e.getMessage(), e);
		}

		// Defer the background polling a little
		UpstreamUrlProvider.INSTANCE.start();

		try {
			reencryptWorkspace(display, monitor);
			return ApplicationCode.CONTINUE;
		} catch (final IOException e) {
			LOG.error("Error during workspace re-encryption: " + e.getMessage(), e);
		}
		return IApplication.EXIT_OK;
	}

	/**
	 * Extend IApplication with CONTINUE code.
	 */
	public interface ApplicationCode extends IApplication {
		public static final Integer CONTINUE = Integer.valueOf(100);
	}

	/**
	 * Re-encrypt the workspace if the re-encryption feature is enabled
	 *
	 * @param display
	 * @param monitor
	 * @throws Exception
	 */
	private void reencryptWorkspace(final Display display, final IProgressMonitor monitor) throws Exception {
		final var encryptionMonitor = SubMonitor.convert(monitor, 1);
		encryptionMonitor.setTaskName("Checking if encryption migration is needed");

		{
			ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, p -> {
				if (p != null) {
					final var sharedCipher = p.getSharedCipher();
					if (sharedCipher instanceof final DelegatingEMFCipher cipher) {
						final var reencrypter = new WorkspaceReencrypter();
						// Add in standard paths.
						reencrypter.addDefaultPaths();

						reencrypter.migrateWorkspaceEncryption(display.getActiveShell(), cipher);
					}
				}
			});
		}

		// Data file manipulation complete, allow startup to continue.
		WellKnownTriggers.WORKSPACE_DATA_ENCRYPTION_CHECK.fireTrigger();
	}

	/**
	 * Checks whether a valid license keystore exists in one of the pre-defined locations. See {@link LicenseChecker#doCheckLicense(KeyStore)} for more info
	 *
	 * @return true if there is a valid license, false other
	 * @throws IOException
	 */
	private boolean licenseCheck(final IProgressMonitor monitor) throws IOException {
		final var licenseMonitor = SubMonitor.convert(monitor, 1);
		licenseMonitor.setTaskName("Validating the license");

		HubLicenseManager.refreshHubLicense();

		// check if the other licenses are valid
		final LicenseState validity = LicenseChecker.checkLicense();
		return validity == LicenseState.Valid;
	}

	private Date licenseCheckExpiresSoon(final int withinDays, final IProgressMonitor monitor) throws IOException {
		final var licenseMonitor = SubMonitor.convert(monitor, 1);
		licenseMonitor.setTaskName("Checking license expiry");

		try {
			final KeyStore licenseKeystore = LicenseManager.getLicenseKeystore();
			final X509Certificate cert = LicenseManager.getClientLicense(licenseKeystore);
			final Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, withinDays);
			if (cert.getNotAfter().before(c.getTime())) {
				return cert.getNotAfter();
			}
		} catch (final Exception e) {
			LOG.error("Unable to load license: " + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Check if the DataHub is online. If it is, start the authentication prompt then re-check the online status
	 */
	private void datahubLogin(final IProgressMonitor monitor) {
		final var loginMonitor = SubMonitor.convert(monitor, 1);
		loginMonitor.setTaskName("Logging into the DataHub");
		UpstreamUrlProvider.INSTANCE.updateOnlineStatus();

		// Trigger early startup prompt for Data Hub
		if (DataHubServiceProvider.getInstance().isHubOnline()) {
			authenticationManager.run(null);

			// Re-test online status
			UpstreamUrlProvider.INSTANCE.isUpstreamAvailable();
		}
	}

	/**
	 * Check DataHub to see if user is authorised to use LiNGO
	 *
	 * @param display
	 * @return true if the user has the correct permission, false otherwise
	 */
	private boolean datahubPermissionCheck(final Display display, final IProgressMonitor monitor) {
		final var permissionMonitor = SubMonitor.convert(monitor, 1);
		permissionMonitor.setTaskName("Checking the DataHub permissions");
		var success = false;

		final boolean datahubStartupCheck = LicenseFeatures.isPermitted(KnownFeatures.FEATURE_DATAHUB_STARTUP_CHECK);
		if (datahubStartupCheck) {

			// use the progress monitor for all startup tasks
			waitForHub();

			// check if datahub is available
			if (DataHubServiceProvider.getInstance().isHubOnline()) {

				// refresh permissions
				try {
					UserPermissionsService.INSTANCE.updateUserPermissions();
				} catch (final Exception e) {
					MessageDialog.openError(display.getActiveShell(), "", "Error getting user permissions from Data Hub. Please try again later.");
				}

				if (UserPermissionsService.INSTANCE.hasUserPermissions() && UserPermissionsService.INSTANCE.isPermitted("lingo", "read")) {
					success = true;
				} else {
					MessageDialog.openError(display.getActiveShell(), "", "User is not authorised to use LiNGO");
					display.dispose();
				}
			} else {
				MessageDialog.openError(display.getActiveShell(), "", "Unable to connect to DataHub. Please try again later.");
			}
		} else {
			// don't block startup if the permission check feature is disabled
			success = true;
		}

		return success;
	}

	/**
	 * Remove the temporary folders in the workspace
	 */
	private void cleanUpTemporaryFolder() {
		// Clean up temp folder on start up.
		final File tempDirectory = ScenarioStorageUtil.getTempDirectory();
		if (tempDirectory.exists() && tempDirectory.isDirectory()) {
			final boolean secureDelete = LicenseFeatures.isPermitted(FileDeleter.LICENSE_FEATURE__SECURE_DELETE);
			try {
				final Path tempDir = tempDirectory.toPath();
				Files.walkFileTree(tempDir, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
						if (!tempDir.equals(dir)) {
							dir.toFile().delete();
						}
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
						FileDeleter.delete(file.toFile(), secureDelete);
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		// Sometimes dir disappears? Perhaps the tree-walker check does not work?
		tempDirectory.mkdirs();
	}

	/**
	 * Show the progress monitor while waiting for the hub to come online
	 *
	 * @param display
	 */
	private void displayProgressMonitor(final Display display) {
		final var progressDialog = new ProgressMonitorDialog(display.getActiveShell());
		try {
			progressDialog.run(false, false, monitor -> {
				try {
					applicationExitCode = startupTasks(display, monitor);
				} catch (final Exception e) {
					LOG.error("failed to start LiNGO: " + e.getMessage());
				}
				monitor.done();
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wait up to 30 seconds for the hub to come online. Tries to connect once every second
	 *
	 * @param display
	 */
	private void waitForHub() {
		final var timeoutInSeconds = 30;
		final var msInASecond = 1000;

		for (int i = 0; i < timeoutInSeconds; ++i) {
			if (DataHubServiceProvider.getInstance().isHubOnline()) {
				break;
			}
			try {
				Thread.sleep(msInASecond);
			} catch (final InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 */
	private void cleanupP2(final IProgressMonitor monitor) {
		final var cleanupMonitor = SubMonitor.convert(monitor, 1);
		cleanupMonitor.setTaskName("Cleaning up the eclipse p2 files");

		final var context = FrameworkUtil.getBundle(Application.class).getBundleContext();
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
			final IProfileRegistry profileRegistry = (IProfileRegistry) agent.getService(IProfileRegistry.SERVICE_NAME);
			if (profileRegistry == null) {
				throw new RuntimeException("Unable to acquire the profile registry service.");
			}

			// can also use IProfileRegistry.SELF for the current profile
			final IProfile profile = profileRegistry.getProfile(IProfileRegistry.SELF);
			// Profile can be null if we have not enabled p2 support in the runtime config
			// in eclipse. Generated product should be fine.
			if (profile != null) {
				final GarbageCollector gc = (GarbageCollector) agent.getService(GarbageCollector.SERVICE_NAME);
				gc.runGC(profile);
			}

			{
				// Try and clean up old update sites from updater code
				final List<URI> toRemove = new LinkedList<>();
				final IMetadataRepositoryManager manager = new MetadataRepositoryManager(agent);
				for (final URI uri : manager.getKnownRepositories(IMetadataRepositoryManager.REPOSITORIES_ALL)) {
					final String str = uri.toString();
					if (str.matches("jar:.*/workspace/updates/.*!/LiNGO")) {
						toRemove.add(uri);
					}
				}
				toRemove.forEach(manager::removeRepository);
			}

		} catch (final ProvisionException e) {
			e.printStackTrace();
		} finally {
			// When you're done, make sure you 'unget' the service.
			context.ungetService(providerRef);
		}
	}

	private void initAccessControl() {
		PluginRegistryHook.initialisePluginXMLEnablements();

		CustomReportsRegistryHook.initialisePluginXMLEnablements();

		ReplaceableViewManager.initialiseReplaceableViews();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning()) {
			return;
		}
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final var display = workbench.getDisplay();
		display.syncExec(() -> {
			if (!display.isDisposed()) {
				workbench.close();
			}
		});
	}
}
