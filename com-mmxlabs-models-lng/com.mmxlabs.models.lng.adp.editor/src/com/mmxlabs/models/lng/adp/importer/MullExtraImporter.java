package com.mmxlabs.models.lng.adp.importer;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter.ImportResults;

public class MullExtraImporter implements IExtraModelImporter {
	
	public static final @NonNull String MULL_KEY = "MULL";
	public static final @NonNull String MULL_NAME = "Mull";
	
	public static final @NonNull String KEY_ENTITY = "entity";
	
	final static @NonNull Map<String, String> inputs = new LinkedHashMap<>();
	static {
		inputs.put(MULL_KEY, MULL_NAME);
	}
	
	@Override
	public @NonNull Map<String, String> getRequiredInputs() {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INVENTORY_MODEL) && LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
			return inputs;
		}
		return Collections.emptyMap();
	}

	@Override
	public void importModel(@NonNull MMXRootObject rootObject, @NonNull Map<String, CSVReader> inputs, @NonNull IMMXImportContext context) {
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INVENTORY_MODEL) || !LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
			return;
		}
	}

	@Override
	public void exportModel(@NonNull Map<String, Collection<Map<String, String>>> output, @NonNull IMMXExportContext context) {
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INVENTORY_MODEL) || !LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
			return;
		}
	}

	public List<Pair<String, MullEntityRow>> doImportMullEntityRows(@NonNull final CSVReader reader, @NonNull final IMMXImportContext context) throws IOException {
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INVENTORY_MODEL) || !LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MULL_SLOT_GENERATION)) {
			return null;
		}
		
		final List<Pair<String, MullEntityRow>> results = new LinkedList<>();
		final MullImporter importer = new MullImporter();
		Map<String, String> row;
		while ((row = reader.readRow(true)) != null) {
			final ImportResults importObject = importer.importEntityRow(null, EcorePackage.eINSTANCE.getEObject(), row, context);
			final String entity = row.get(KEY_ENTITY);
			results.add(Pair.of(entity, (MullEntityRow) importObject.importedObject));
		}
		return results;
	}
}
