/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.importer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

/**
 */
public class CommercialModelImporter implements ISubmodelImporter {
	public static final String ENTITIES_KEY = "ENTITIES";
	public static final String ENTITY_BOOKS_KEY = "ENTITYBOOKS";
	public static final String SALES_CON_KEY = "SALES";
	public static final String PURCHASE_CON_KEY = "PURCHASE";
	final static Map<String, String> inputs = new LinkedHashMap<String, String>();

	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter entityImporter;
	private LegalEntityBookImporter entityBookImporter;
	private IClassImporter purchaseImporter;
	private IClassImporter salesImporter;

	static {
		inputs.put(ENTITIES_KEY, "Entities");
		inputs.put(ENTITY_BOOKS_KEY, "Entity Books");
		inputs.put(PURCHASE_CON_KEY, "Purchase Contracts");
		inputs.put(SALES_CON_KEY, "Sales Contracts");
	}

	/**
	 */
	public CommercialModelImporter() {
		final Activator activator = Activator.getDefault();
		if (activator != null) {

			importerRegistry = activator.getImporterRegistry();
			registryInit();
		}
	}

	@Inject
	private void registryInit() {
		if (importerRegistry != null) {

			entityBookImporter = new LegalEntityBookImporter();// (LegalEntityBookImporter) importerRegistry.getClassImporter(CommercialPackage.eINSTANCE.getLegalEntity());
			entityImporter = importerRegistry.getClassImporter(CommercialPackage.eINSTANCE.getLegalEntity());
			purchaseImporter = importerRegistry.getClassImporter(CommercialPackage.eINSTANCE.getPurchaseContract());
			salesImporter = importerRegistry.getClassImporter(CommercialPackage.eINSTANCE.getSalesContract());
		}
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		final CommercialModel commercial = CommercialFactory.eINSTANCE.createCommercialModel();
		if (inputs.containsKey(ENTITIES_KEY)) {
			final Collection<EObject> entities = entityImporter.importObjects(CommercialPackage.eINSTANCE.getBaseLegalEntity(), inputs.get(ENTITIES_KEY), context);
			for (final EObject entity : entities) {
				if (entity instanceof BaseLegalEntity) {
					final BaseLegalEntity baseLegalEntity = (BaseLegalEntity) entity;
					commercial.getEntities().add(baseLegalEntity);
					// Ensure a book exists. Note the LegalEntityBookImporter will overwrite this data if needed.
					// A post model importer may be a more robust way of ensuring books exist should implementations change.
					if (baseLegalEntity.getShippingBook() == null) {
						baseLegalEntity.setShippingBook(CommercialFactory.eINSTANCE.createSimpleEntityBook());
					}
					if (baseLegalEntity.getTradingBook() == null) {
						baseLegalEntity.setTradingBook(CommercialFactory.eINSTANCE.createSimpleEntityBook());
					}
					if (baseLegalEntity.getUpstreamBook() == null) {
						baseLegalEntity.setUpstreamBook(CommercialFactory.eINSTANCE.createSimpleEntityBook());
					}
				}

			}
		}
		if (inputs.containsKey(ENTITY_BOOKS_KEY)) {
			entityBookImporter.importObjects(CommercialPackage.eINSTANCE.getBaseEntityBook(), inputs.get(ENTITY_BOOKS_KEY), context);
		}
		if (inputs.containsKey(SALES_CON_KEY)) {
			commercial.getSalesContracts()
					.addAll((Collection<? extends SalesContract>) salesImporter.importObjects(CommercialPackage.eINSTANCE.getSalesContract(), inputs.get(SALES_CON_KEY), context));
		}
		if (inputs.containsKey(PURCHASE_CON_KEY)) {
			commercial.getPurchaseContracts().addAll(
					(Collection<? extends PurchaseContract>) purchaseImporter.importObjects(CommercialPackage.eINSTANCE.getPurchaseContract(), inputs.get(PURCHASE_CON_KEY), context));
		}
		return commercial;
	}

	@Override
	public void exportModel(final EObject model, final Map<String, Collection<Map<String, String>>> output, final IMMXExportContext context) {
		final CommercialModel cm = (CommercialModel) model;
		output.put(ENTITIES_KEY, entityImporter.exportObjects(cm.getEntities(), context));
		output.put(ENTITY_BOOKS_KEY, entityBookImporter.exportObjects(cm.getEntities(), context));

		final LinkedList<PurchaseContract> purchase = new LinkedList<PurchaseContract>();
		final LinkedList<SalesContract> sales = new LinkedList<SalesContract>();
		for (final SalesContract c : cm.getSalesContracts()) {
			sales.add(c);
		}
		for (final PurchaseContract c : cm.getPurchaseContracts()) {
			purchase.add(c);
		}
		output.put(SALES_CON_KEY, salesImporter.exportObjects(sales, context));
		output.put(PURCHASE_CON_KEY, purchaseImporter.exportObjects(purchase, context));
	}

	@Override
	public EClass getEClass() {
		return CommercialPackage.eINSTANCE.getCommercialModel();
	}
}
