package com.mmxlabs.models.lng.cargo.importer;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.csv.FieldMap;
import com.mmxlabs.common.csv.IFieldMap;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class InventoryImporter extends DefaultClassImporter {
	
	private static final String KEY_TYPE = "type";
	private static final String KEY_FLOW_TYPE_FEED = "feed";
	private static final String KEY_FLOW_TYPE_OFFTAKE = "offtake";
	private static final String KEY_CAPACITY = "capacity";
	
	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {
		final String rowType = row.get(KEY_TYPE);
		final EClass rowClass;
		if (KEY_FLOW_TYPE_FEED.equalsIgnoreCase(rowType) || KEY_FLOW_TYPE_OFFTAKE.equalsIgnoreCase(rowType)) {
			rowClass = getTrueOutputClass(eClass, "InventoryEventRow");
		} else if (KEY_CAPACITY.equalsIgnoreCase(rowType)) {
			rowClass = getTrueOutputClass(eClass, "InventoryCapacityRow");
		} else {
			context.addProblem(context.createProblem(String.format("%s is not a valid inventory CSV row type", rowType), true, true, true));
			return new ImportResults(null);
		}
		
		try {
			final EObject instance = rowClass.getEPackage().getEFactoryInstance().create(rowClass);
			assert instance != null;
			final ImportResults results = new ImportResults(instance);
			importAttributes(row, context, rowClass, instance);
			final IFieldMap iFieldMap = row instanceof IFieldMap ? (IFieldMap) row : new FieldMap(row);
			importReferences(iFieldMap, context, rowClass, instance);
			return results;
		} catch (IllegalArgumentException illegal) {
			context.addProblem(context.createProblem("Ill-formed inventory CSV row", true, true, true));
			return new ImportResults(null);
		}
	}
	
	public ImportResults importEventRow(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {
		final String rowType = row.get(KEY_TYPE);
		final EClass rowClass;
		if (KEY_FLOW_TYPE_FEED.equalsIgnoreCase(rowType) || KEY_FLOW_TYPE_OFFTAKE.equalsIgnoreCase(rowType)) {
			rowClass = getTrueOutputClass(eClass, "InventoryEventRow");
		} else {
			context.addProblem(context.createProblem(String.format("%s is not a valid inventory event row type", rowType), true, true, true));
			return new ImportResults(null);
		}
		
		try {
			final EObject instance = rowClass.getEPackage().getEFactoryInstance().create(rowClass);
			assert instance != null;
			final ImportResults results = new ImportResults(instance);
			importAttributes(row, context, rowClass, instance);
			final IFieldMap iFieldMap = row instanceof IFieldMap ? (IFieldMap) row : new FieldMap(row);
			importReferences(iFieldMap, context, rowClass, instance);
			return results;
		} catch (IllegalArgumentException illegal) {
			context.addProblem(context.createProblem("Ill-formed inventory CSV row", true, true, true));
			return new ImportResults(null);
		}
	}
}
