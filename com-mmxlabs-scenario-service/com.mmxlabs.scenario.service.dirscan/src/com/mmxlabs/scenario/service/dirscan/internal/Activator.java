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

	private DirScanScenarioService dirScanService;

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

			try {
				dirScanService = new DirScanScenarioService(serviceName);
				// Various ways of passing properties which do not work...
				dirScanRegistration = getBundle().getBundleContext().registerService(IScenarioService.class, dirScanService, props);
				dirScanRegistration.setProperties(props);
				// .. the way I've found to make it work...
				dirScanService.start(props);
			} catch (final IOException e) {
				log.error("Error starting DirScan Scenario Service: " + e.getMessage(), e);
			}
		}

	}

	private void disableService() {
		if (dirScanRegistration != null) {

			dirScanRegistration.unregister();
			dirScanRegistration = null;

			dirScanService.stop();
			dirScanService = null;
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
		// The code does not work correctly yet
		if (!b || b) {
			return;
		}
//		try {

//			// get the p2 agent
//			final BundleContext bundleContext = getBundle().getBundleContext();
//			final ServiceReference<IProvisioningAgentProvider> sr = bundleContext.getServiceReference(IProvisioningAgentProvider.class);
//			if (sr == null) {
//				return;
//			}
//			final IProvisioningAgentProvider agentProvider = bundleContext.getService(sr);
//			final IProvisioningAgent agent = agentProvider.createAgent(null);
////////////////////// Way 1
//			final IMatchExpression<IInstallableUnit> filter = null;
//
//			// Create fragment descriptor
//			final InstallableUnitFragmentDescription descriptor = new MetadataFactory.InstallableUnitFragmentDescription();
//			descriptor.setId("configure." + PLUGIN_ID);
//			descriptor.setVersion(Version.createOSGi(1, 0, 0, Long.toString(new Date().getTime())));
//			descriptor.setArtifacts(new IArtifactKey[] { ArtifactKey.parse("osgi.bundle," + descriptor.getId() + ",0.0.0") });
//
//			// Add requirement on self
//			// String exactVersion = "[" + descriptor.getVersion().toString() + "," + descriptor.getVersion().toString() + "]";
//			// final IRequirement metaRequirement = MetadataFactory.createRequirement("org.eclipse.equinox.p2.iu", descriptor.getId(), new VersionRange(exactVersion), filter, true, true);
//			// descriptor.setMetaRequirements(new IRequirement[] { metaRequirement });
//
//			// Add in the mark started instructions
//			final Map<String, String> instructions = new HashMap<String, String>();
//			// instructions.put("configure", "org.eclipse.equinox.p2.touchpoint.eclipse.markStarted(started:true);");
//			// instructions.put("unconfigure", "org.eclipse.equinox.p2.touchpoint.eclipse.markStarted(started:false);");
//			instructions.put("configure", "org.eclipse.equinox.p2.touchpoint.eclipse.setStartLevel(startLevel:4); org.eclipse.equinox.p2.touchpoint.eclipse.markStarted(started:true);");
//			instructions.put("unconfigure", "org.eclipse.equinox.p2.touchpoint.eclipse.setStartLevel(startLevel:-1); org.eclipse.equinox.p2.touchpoint.eclipse.markStarted(started:false);");
//
//			// Example p2.inf has ${artifact} - but not sure if needed
//			instructions.put("install", "org.eclipse.equinox.p2.touchpoint.eclipse.installBundle(bundle:" + descriptor.getId() + ");");
//			instructions.put("uninstall", "org.eclipse.equinox.p2.touchpoint.eclipse.uninstallBundle(bundle:" + descriptor.getId() + ");");
//
//			// Add the touchpoint data to the descriptor
//			final ITouchpointData touchpointData = MetadataFactory.createTouchpointData(instructions);
//			descriptor.addTouchpointData(touchpointData);
//
//			// Set requirements on the host bundle (already installed)
//			final IRequirement hostRequirement1 = MetadataFactory.createRequirement("osgi.bundle", PLUGIN_ID, new VersionRange("1.0.0"), filter, false, true);
//			final IRequirement hostRequirement2 = MetadataFactory.createRequirement("org.eclipse.equinox.p2.eclipse.type", "bundle", new VersionRange("[1.0.0,2.0.0)"), filter, false, true);
//			descriptor.setHost(new IRequirement[] { hostRequirement1, hostRequirement2 });
//
//			// Add a requirement on the host bundle
//			final IRequirement requirement = MetadataFactory.createRequirement("osgi.bundle", PLUGIN_ID, new VersionRange("1.0.0"), filter, false, true);
//			descriptor.setRequirements(new IRequirement[] { requirement });
//
//			// Set the provided capability
//			final IProvidedCapability provides = MetadataFactory.createProvidedCapability("org.eclipse.equinox.p2.iu", descriptor.getId(), descriptor.getVersion());
//			descriptor.setCapabilities(new IProvidedCapability[] { provides });
//
//			// Generate the IU
//			final IInstallableUnitFragment fragment = MetadataFactory.createInstallableUnitFragment(descriptor);
//			//
//			IQuery<IInstallableUnit> query = QueryUtil.createIUQuery(PLUGIN_ID);
//
//			IProfileRegistry installerRegistry = (IProfileRegistry) ((IProvisioningAgent) agent.getService(IProvisioningAgent.INSTALLER_AGENT)).getService(IProfileRegistry.SERVICE_NAME);
//			IProfile installerProfile = installerRegistry.getProfile((String) agent.getService(IProvisioningAgent.INSTALLER_PROFILEID));
//
//			IQueryResult<IInstallableUnit> c = installerProfile.query(query, new NullProgressMonitor());
//			Iterator<IInstallableUnit> iterator = c.iterator();
//			Set<IInstallableUnit> unmodifiableSet = c.toUnmodifiableSet();
//
//			IInstallableUnit unit = MetadataFactory.createResolvedInstallableUnit(unmodifiableSet.iterator().next(), new IInstallableUnitFragment[] { fragment });

			
////////////////////Way 2
			
//			Map<String, String> advice = new HashMap<>();
//
//			// #create a requirement on the fragment we are creating
//			advice.put("requires.0.namespace", "org.eclipse.equinox.p2.iu");
//			advice.put("requires.0.name", "configure." + PLUGIN_ID);
//			advice.put("requires.0.greedy", "true");
//
//			// #create a IU fragment named configure.net.certiv.bent.core");
//			advice.put("units.0.id", "configure." + PLUGIN_ID);
//			advice.put("units.0.version", "1.0.0");
//			advice.put("units.0.provides.1.namespace", "org.eclipse.equinox.p2.iu");
//			advice.put("units.0.provides.1.name", "configure." + PLUGIN_ID);
//			advice.put("units.0.provides.1.version", "1.0.0");
//			
//			advice.put("units.0.instructions.install", "org.eclipse.equinox.p2.touchpoint.eclipse.installBundle(bundle:${artifact});");
//			advice.put("units.0.instructions.uninstall", "org.eclipse.equinox.p2.touchpoint.eclipse.uninstallBundle(bundle:${artifact});");
//			advice.put("units.0.instructions.unconfigure",
//					"org.eclipse.equinox.p2.touchpoint.eclipse.setStartLevel(startLevel:-1); org.eclipse.equinox.p2.touchpoint.eclipse.markStarted(started:false);");
//			advice.put("units.0.instructions.configure", "org.eclipse.equinox.p2.touchpoint.eclipse.setStartLevel(startLevel:4);org.eclipse.equinox.p2.touchpoint.eclipse.markStarted(started:true);");
//
//			// # add multiple sets of host requirements to ensure this p2.inf is selected
//			advice.put("units.0.hostRequirements.1.namespace", "osgi.bundle");
//			advice.put("units.0.hostRequirements.1.name", PLUGIN_ID);
//			advice.put("units.0.hostRequirements.1.greedy", "false");
//
//			advice.put("units.0.hostRequirements.2.namespace", "org.eclipse.equinox.p2.eclipse.type");
//			advice.put("units.0.hostRequirements.2.name", "bundle");
//			advice.put("units.0.hostRequirements.2.range", "[1.0.0,2.0.0)");
//			advice.put("units.0.hostRequirements.2.greedy", "false");
//
//			advice.put("units.0.requires.1.namespace", "osgi.bundle");
//			advice.put("units.0.requires.1.name", PLUGIN_ID);
//			advice.put("units.0.requires.1.greedy", "false");
//
//			AdviceFileParser parser = new AdviceFileParser("advice." + PLUGIN_ID, Version.createOSGi(1, 0, 0, Long.toString(new Date().getTime())), advice);
//
//			parser.parse();
//
//			InstallableUnitDescription[] unitDescriptions = parser.getAdditionalInstallableUnitDescriptions();
//
//			List<IInstallableUnit> ius = new ArrayList<IInstallableUnit>(unitDescriptions.length);
//			for (InstallableUnitDescription d : unitDescriptions) {
//				IInstallableUnit iu = MetadataFactory.createInstallableUnit(d);
//				ius.add(iu);
//			}
//			// ((InstallableUnit)unit.
//			// unit.getAsetArtifacts(new IArtifactKey[] { ArtifactKey.parse("osgi.bundle," + descriptor.getId() + ",0.0.0")});
//			// MetadataFactory.createResolvedInstallableUnit(unit, fragments)

			//
			// IInstallableUnit unit = fragment;

//			// Create and execute the install operation
//			// final InstallOperation op = new InstallOperation(new ProvisioningSession(agent), Collections.single ton(unit));
//			final InstallOperation op = new InstallOperation(new ProvisioningSession(agent), ius);
//			// UpdateOperation op = new UpdateOperation(new ProvisioningSession(agent), Collections.singleton(unit));
//			final IStatus status = op.resolveModal(new NullProgressMonitor());
//			if (status.isOK()) {
//				final Job job = op.getProvisioningJob(new NullProgressMonitor());
//				job.addJobChangeListener(new JobChangeAdapter() {
//					public void done(final IJobChangeEvent event) {
//						// Clean up!
//						agent.stop();
//					}
//				});
//				job.schedule();
//			} else {
//				int ii = 0;
//				log.error(status.toString());
//			}
			//

			// Platform.getPlugin(PLUGIN_ID).start(context);
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
			// InstallableUnitFragmentDescription descriptor = new MetadataFactory.InstallableUnitFragmentDescription();
			// descriptor.setId(PLUGIN_ID + ".enabler");
			// descriptor.setVersion(Version.createOSGi(1, 0, 0, Long.toString(new Date().getTime())));
			// // descriptor.setTouchpointType(type);
			// // descriptor.setTouchpointType(type);Type(type);
			// ;
			//
			// IRequirement requirement = MetadataFactory.createRequirement("osgi.bundle", PLUGIN_ID, new VersionRange("1.0.0"), filter, 0, 1, true, "");
			// descriptor.setRequirements(new IRequirement[] { requirement});
			// MetadataFactory.createRequirement("org.eclipse.equinox.p2.iu", "configure." +PLUGIN_ID, new VersionRange("[1.0.0,2.0.0)"), filter, minCard, maxCard, greedy, description)
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
//		} catch (final ProvisionException e) {
//			log.error(e.getMessage(), e);
//		}
	}
}
