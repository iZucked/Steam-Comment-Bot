package com.mmxlabs.scenario.service.dirscan.internal;

import java.io.IOException;
import java.util.Hashtable;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.dirscan.DirScanScenarioService;
import com.mmxlabs.scenario.service.dirscan.preferences.PreferenceConstants;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	
	private static final Logger log = LoggerFactory.getLogger(Activator.class);

	// The plug-in ID
	public static final String PLUGIN_ID = "com.mmxlabs.scenario.service.dirscan"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private IPropertyChangeListener propertyChangeListener;

	private ServiceRegistration<IScenarioService> dirScanRegistration;

	// private BundleContext context;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		propertyChangeListener = new IPropertyChangeListener() {
			@Override
			public void propertyChange(final PropertyChangeEvent event) {
				if (event.getProperty().equals(PreferenceConstants.P_ENABLED_KEY)) {
					if (Boolean.TRUE.equals(event.getNewValue())) {
						markAutoStart(true);
						enableService();
					} else {
						markAutoStart(false);
						disableService();
					}
				} else {
					updateService();
				}
			}
		};
		getPreferenceStore().addPropertyChangeListener(propertyChangeListener);

		// Attempt to start service immediately
		enableService();
	}

	private void enableService() {
		final String enabled = getPreferenceStore().getString(PreferenceConstants.P_ENABLED_KEY);
		if ("true".equalsIgnoreCase(enabled)) {

			final String serviceName = getPreferenceStore().getString(PreferenceConstants.P_NAME_KEY);
			final String path = getPreferenceStore().getString(PreferenceConstants.P_PATH_KEY);

			final Hashtable<String, String> props = new Hashtable<String, String>();
			props.put(PreferenceConstants.P_NAME_KEY, serviceName);
			props.put(PreferenceConstants.P_PATH_KEY, path);
			// used internally
			props.put("component.id", serviceName);

			DirScanScenarioService service;
			try {
				service = new DirScanScenarioService(serviceName);
				// Various ways of passing properties which do not work...
				dirScanRegistration = getBundle().getBundleContext().registerService(IScenarioService.class, service, props);
				dirScanRegistration.setProperties(props);
				// .. the way I've found to make it work...
				service.updated(props);
			} catch (IOException e) {
				log.error("Error starting DirScan Scenario Service: " + e.getMessage(), e);
			}
		}

	}

	private void disableService() {
		if (dirScanRegistration != null) {

			dirScanRegistration.unregister();
			dirScanRegistration = null;
		}
	}

	private void updateService() {
		if (dirScanRegistration != null) {
			disableService();
			enableService();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {

		disableService();

		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	private void markAutoStart(final boolean b) {
		if (true) {
			return;
		}
		// IPublisherInfo publisherInfo = ...;
		// IPublisherAction[] actions = ...;
		// Publisher publisher = new Publisher( publisherInfo );
		// publisher.publish( actions, progressMonitor );

		// IInstallableUnit iu;
		// iu.getTouchpointData()
		//
		// Touchpoint tp;
		// tp.
		// TODO Auto-generated method stub
		// PlatformAdmin admin;
		// admin.getState(true).getBundle(0).get
		// ServiceReference<?> packageAdminRef = getBundle().getBundleContext().getServiceReference(PackageAdmin.class.getName());
		// PackageAdmin packageAdmin =(PackageAdmin) getBundle().getBundleContext().getService(packageAdminRef)

		// Profile p;
		// Engine engine;
		// Profile
		// IInstallableUnit iu;
		// p.setInstallableUnitProperty(iu, key, value);

		// ProvisioningAction action;
		// Touchpoint touchpoint =
		// Map<String, Object> parameters = new HashMap<String, Object>();
		// parameters.put(ActionConstants.PARM_PROFILE, profile);
		// EclipseTouchpoint touchpoint = new EclipseTouchpoint();
		// touchpoint.initializePhase(null, profile, "test", parameters);
		// InstallableUnitOperand operand = new InstallableUnitOperand(null, iu);
		// parameters.put("iu", operand.second());
		// touchpoint.initializeOperand(profile, operand, parameters);
		//
		// parameters.put(ActionConstants.PARM_STARTED, Boolean.TRUE.toString());
		// parameters = Collections.unmodifiableMap(parameters);
		//
		// Manipulator manipulator = (Manipulator) parameters.get(EclipseTouchpoint.PARM_MANIPULATOR);
		// assertNotNull(manipulator);
		//
		// BundleInfo bundleInfo = Util.createBundleInfo(osgiTarget, Util.getManifest(iu.getTouchpointData()));
		// manipulator.getConfigData().addBundle(bundleInfo);
		//
		// MarkStartedAction markStarted = new MarkStartedAction();
		// markStarted.execute(parameters);
		// IProvisioningAgent agent;
		// ProvisioningContext provisingContext = new ProvisioningContext(agent);
		//
		// // provisingContext.
		// Map<String, Object> instructions = new HashMap<String, Object>();
		// //
		// instructions.put("configure", "org.eclipse.equinox.p2.touchpoint.eclipse.markStarted(started:true);");
		// ITouchpointData touchpointData = MetadataFactory.createTouchpointData(instructions);
		//
		//
		// InstallableUnitDescription descriptor = new MetadataFactory.InstallableUnitDescription();
		// root.setSingleton(true);
		// root.setId(id);
		// root.setVersion(version);
		// root.setProperty(IInstallableUnit.PROP_NAME, name);
		//
		// Collection<IRequirement> requiredCapabilities = createIURequirements(children);
		// if (requires != null)
		// requiredCapabilities.addAll(requires);
		// root.setRequirements(requiredCapabilities.toArray(new IRequirement[requiredCapabilities.size()]));
		// root.setArtifacts(new IArtifactKey[0]);
		//
		//		root.setProperty("lineUp", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		// root.setUpdateDescriptor(MetadataFactory.createUpdateDescriptor(id, VersionRange.emptyRange, IUpdateDescriptor.NORMAL, null));
		// root.setProperty(InstallableUnitDescription.PROP_TYPE_GROUP, Boolean.TRUE.toString());
		// root.setCapabilities(new IProvidedCapability[] { createSelfCapability(id, version) });
		// // TODO why is the type OSGI?
		// root.setTouchpointType(PublisherHelper.TOUCHPOINT_OSGI);
		// return root;
		// IInstallableUnit rootIU = MetadataFactory.createInstallableUnit(descriptor);
		// try {
		// ProvisioningUI provisioningUI = ProvisioningUI.getDefaultUI();
		//
		// if (null == provisioningUI) {
		// return;
		// }
		//
		// String profileId = provisioningUI.getProfileId();
		//
		// ProvisioningSession provisioningSession = provisioningUI.getSession();
		//
		// if (null == provisioningSession) {
		// return;
		// }
		//
		// IQueryable<IInstallableUnit> queryable = ((IProfileRegistry) provisioningSession.getProvisioningAgent().getService(IProfileRegistry.SERVICE_NAME)).getProfile(profileId);
		//
		// if (null == queryable) {
		// return;
		// }
		//
		// // to get the product ID
		// // String pId = Platform.getProduct().getId();
		//
		// String pId = Activator.PLUGIN_ID;
		//
		// if (null != queryable) {
		// IQueryResult<IInstallableUnit> iqr = queryable.query(QueryUtil.createIUQuery(pId), null);
		//
		// if (null != iqr) {
		// Iterator<IInstallableUnit> ius = iqr.iterator();
		// if (ius.hasNext()) {
		// IInstallableUnit iu = ius.next();
		// Version v = iu.getVersion();
		//
		// if (null != v) {
		// System.out.println("ID: " + iu.getId() + " | IU: " + iu.toString() + " | Version: " + v.toString());
		// }
		// //Engine engine;
		// //ProvisioningContext context;
		// //context.
		// //engine.createPlan(profile, context);
		//
		// TouchpointManager mgr;
		// mgr.getTouchpoint(typeId, versionRange)
		// }
		// }
		// }
		// } catch (Exception e) {
		// System.out.println(e.getStackTrace());
		// return;
		// }
	}
}
