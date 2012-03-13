/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.edapt.migration.MigrationException;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailablility;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.optimiser.OptimiserFactory;
import com.mmxlabs.models.lng.optimiser.OptimiserPackage;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.jointmodel.EmptyJointModelRelease;
import com.mmxlabs.models.mmxcore.jointmodel.IJointModelRelease;
import com.mmxlabs.models.mmxcore.jointmodel.JointModel;
import com.mmxlabs.shiplingo.platform.models.manifest.manifest.Entry;
import com.mmxlabs.shiplingo.platform.models.manifest.manifest.Manifest;
import com.mmxlabs.shiplingo.platform.models.manifest.manifest.ManifestFactory;
import com.mmxlabs.shiplingo.platform.models.manifest.manifest.ManifestPackage;

/**
 * This is an example LNG joint model; each client will have a different implementation of this.
 * 
 * In this simple example, we have a very simple ecore model which is placed in a zip archive, called manifest.xmi.
 * 
 * This in turn contains relative references to each submodel part. It is not all that LNG specific really.
 * 
 * @author hinton
 *
 */
public class ManifestJointModel extends JointModel {
	private static final List<IJointModelRelease> releases = 
			new LinkedList<IJointModelRelease>();
	private static final String PORT_MODEL_KEY = "port-model";
	private static final String FLEET_MODEL_KEY = "fleet-model";
	private static final String CARGO_MODEL_KEY = "cargo-model";
	private static final String PRICING_MODEL_KEY = "pricing-model";
	private static final String INPUT_MODEL_KEY = "input-model";
	private static final String SCHEDULE_MODEL_KEY = "schedule-model";
	private static final String COMMERCIAL_MODEL_KEY = "commercial-model";
	private static final String ROOT_MODEL_KEY = "root-model";
	private static final String OPTIMISER_MODEL_KEY = "optimiser-model";
	
	/**
	 * This map lets us know what kind of model class has what key.
	 */
	private static final Map<EClass, String> modelClassKeys = new LinkedHashMap<EClass, String>();
	
	static {
		releases.add(new EmptyJointModelRelease(
				PORT_MODEL_KEY, 0,
				FLEET_MODEL_KEY, 0,
				CARGO_MODEL_KEY, 0,
				PRICING_MODEL_KEY, 0,
				INPUT_MODEL_KEY, 0,
				SCHEDULE_MODEL_KEY, 0,
				COMMERCIAL_MODEL_KEY, 0,
				OPTIMISER_MODEL_KEY, 0));
		
		modelClassKeys.put(PortPackage.eINSTANCE.getPortModel(), PORT_MODEL_KEY);
		modelClassKeys.put(FleetPackage.eINSTANCE.getFleetModel(), FLEET_MODEL_KEY);
		modelClassKeys.put(CargoPackage.eINSTANCE.getCargoModel(), CARGO_MODEL_KEY);
		modelClassKeys.put(CommercialPackage.eINSTANCE.getCommercialModel(), COMMERCIAL_MODEL_KEY);
		modelClassKeys.put(PricingPackage.eINSTANCE.getPricingModel(), PRICING_MODEL_KEY);
		modelClassKeys.put(InputPackage.eINSTANCE.getInputModel(), INPUT_MODEL_KEY);
		modelClassKeys.put(SchedulePackage.eINSTANCE.getScheduleModel(), SCHEDULE_MODEL_KEY);
		modelClassKeys.put(OptimiserPackage.eINSTANCE.getOptimiserModel(), OPTIMISER_MODEL_KEY);
		
		/*
		 * There is no migration history for MMXCore, but this is not a problem; the joint model
		 * will ignore submodels which have no release history and leave them out of the upgrade process. 
		 */
		modelClassKeys.put(MMXCorePackage.eINSTANCE.getMMXRootObject(), ROOT_MODEL_KEY);
	}

	private URI rootURI;
	
	private Manifest manifest;
	
	
	protected void setFile(final URI rootURI) throws IOException {
		this.rootURI = rootURI;
	}
	
	protected Resource createManifestResource() {
		ManifestPackage.eINSTANCE.getEntry(); // trigger load of package?
		return getResourceSet().createResource(getArchiveURI("MANIFEST.xmi"));
	}
	
	protected URI getArchiveURI(final String relativePath) {
		return URI.createURI("archive:" + rootURI.toString() + "!/" + relativePath);
	}
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.models.mmxcore.jointmodel.JointModel#needsUpgrade()
	 */
	@Override
	protected boolean needsUpgrade() {
		return false;
	}

	public static ManifestJointModel createEmptyModel(final URI target) throws IOException {
		final MMXRootObject rootObject = MMXCoreFactory.eINSTANCE.createMMXRootObject();
		rootObject.setVersion(releases.size());
		//TODO sort out how to create blank models; should there be an extension for this?
		rootObject.addSubModel(PortFactory.eINSTANCE.createPortModel());
		rootObject.addSubModel(FleetFactory.eINSTANCE.createFleetModel());
		rootObject.addSubModel(CargoFactory.eINSTANCE.createCargoModel());
		rootObject.addSubModel(PricingFactory.eINSTANCE.createPricingModel());
		rootObject.addSubModel(InputFactory.eINSTANCE.createInputModel());
		rootObject.addSubModel(ScheduleFactory.eINSTANCE.createScheduleModel());
		rootObject.addSubModel(CommercialFactory.eINSTANCE.createCommercialModel());
		rootObject.addSubModel(OptimiserFactory.eINSTANCE.createOptimiserModel());
		
		{
		final CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
		final LoadSlot ls = CargoFactory.eINSTANCE.createLoadSlot();
		final DischargeSlot ds = CargoFactory.eINSTANCE.createDischargeSlot();
		final Cargo c = CargoFactory.eINSTANCE.createCargo();
		c.setLoadSlot(ls);
		c.setDischargeSlot(ds);
		
		cargoModel.getCargos().add(c);
		cargoModel.getLoadSlots().add(ls);
		cargoModel.getDischargeSlots().add(ds);
		}
		
		{
		final PortModel portModel = rootObject.getSubModel(PortModel.class);
		final Port p = PortFactory.eINSTANCE.createPort();
		p.setName("my fav. port");
		portModel.getPorts().add(p);
		}
		
		{
			final FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);
			final VesselClass vc = FleetFactory.eINSTANCE.createVesselClass();
			vc.setName("VC 1");
			final Vessel v = FleetFactory.eINSTANCE.createVessel();
			v.setVesselClass(vc);
			v.setName("V1");
			fleetModel.getVessels().add(v);
			fleetModel.getVesselClasses().add(vc);
			final VesselAvailablility a = FleetFactory.eINSTANCE.createVesselAvailablility();
			v.setAvailability(a);
		}
		
		final ManifestJointModel result = new ManifestJointModel(rootObject, target);
		return result;
	}
	
	public ManifestJointModel(final MMXRootObject rootObject, final URI file_) throws IOException {
		setFile(file_);
		final Resource resource = createManifestResource();
		manifest = ManifestFactory.eINSTANCE.createManifest();
		resource.getContents().add(manifest);
		setRootObject(rootObject);
	}
	
	public ManifestJointModel(final URI file_) throws FileNotFoundException, IOException, MigrationException {
		setFile(file_);
		final Resource resource = createManifestResource();
		resource.load(null);
		manifest = (Manifest) resource.getContents().iterator().next();
		
		for (final Entry e : manifest.getEntries()) {
			addSubModel(e.getSubModelKey(), getArchiveURI(e.getRelativePath()));
		}
		
		load();
	}
	
	@Override
	public void save() throws IOException {
//		if (!file.exists()) {
//			// create empty zipfile
//			file.getParentFile().mkdirs();
//			final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
//			zos.setComment("See MANIFEST.xmi for more information");
//			zos.flush();
//			zos.close();
//		}
		super.save();
		manifest.getEntries().clear();
		for (final Map.Entry<String, URI> keyAndURI : subModels.entrySet()) {
			final Entry e = ManifestFactory.eINSTANCE.createEntry();
			e.setSubModelKey(keyAndURI.getKey());
			e.setRelativePath(getRelativePath(keyAndURI.getValue()));
			manifest.getEntries().add(e);
		}
		manifest.eResource().save(null);
	}
	
	/**
	 * Find the path within archive of the given URI.
	 * 
	 * @param value
	 * @return
	 */
	private String getRelativePath(final URI value) {
		final String[] parts = value.toString().split("!");
		return parts[parts.length-1].substring(1);//strip off leading slash
	}

	@Override
	protected URI createURI(final UUIDObject object) {
		final String modelKey = modelClassKeys.get(object.eClass());
		final URI result = getArchiveURI(object.eClass().getName() + "-" + object.getUuid() + ".xmi");
		
		if (modelKey != null) 
			addSubModel(modelKey, result);
		
		return result;
	}

	@Override
	protected List<IJointModelRelease> getReleases() {
		return releases;
	}
	
	@Override
	public MMXRootObject getRootObject() {
		return super.getRootObject();
	}

	public static Iterable<EClass> getSubmodelClasses() {
		return modelClassKeys.keySet();
	}
}
