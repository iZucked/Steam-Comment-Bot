package com.mmxlabs.models.lng.port.importer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;

public class PortModelImporter implements ISubmodelImporter {
	public static final String PORT_KEY = "PORT";
	public static final String PORT_GROUP_KEY = "PORTGROUP";
	public static final String DISTANCES_KEY = "DISTANCES";
	public static final String SUEZ_KEY = "SUEZ";
	public static final HashMap<String, String> inputs = new HashMap<String, String>();
	static {
		inputs.put(PORT_KEY, "Ports");
		inputs.put(PORT_GROUP_KEY, "Groups");
		inputs.put(DISTANCES_KEY, "Distance Matrix");
		inputs.put(SUEZ_KEY, "Suez Distance Matrix");
	}
	
	IClassImporter portImporter = Activator.getDefault().getImporterRegistry().getClassImporter(PortPackage.eINSTANCE.getPort());
	IClassImporter portGroupImporter = Activator.getDefault().getImporterRegistry().getClassImporter(PortPackage.eINSTANCE.getPortGroup());
	
	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public UUIDObject importModel(Map<String, CSVReader> inputs,
			IImportContext context) {
		final PortModel result = PortFactory.eINSTANCE.createPortModel();
		if (inputs.containsKey(PORT_KEY)) {
			final CSVReader reader = inputs.get(PORT_KEY);
			result.getPorts().addAll((Collection<? extends Port>) portImporter.importObjects(PortPackage.eINSTANCE.getPort(), reader, context));
		}
		if (inputs.containsKey(PORT_GROUP_KEY)) {
			final CSVReader reader = inputs.get(PORT_GROUP_KEY);
			result.getPortGroups().addAll((Collection<? extends PortGroup>) portGroupImporter.importObjects(PortPackage.eINSTANCE.getPortGroup(), reader, context));
		}
		return result;
	}

	@Override
	public void exportModel(UUIDObject model,
			Map<String, Collection<Map<String, String>>> output) {
		output.put(PORT_KEY, portImporter.exportObjects(((PortModel)model).getPorts()));
		output.put(PORT_GROUP_KEY, portGroupImporter.exportObjects(((PortModel)model).getPortGroups()));
	}

}
