/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.importer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IExportContext;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

/**
 * @since 2.0
 */
public class CommercialModelImporter implements ISubmodelImporter {
	public static final String ENTITIES_KEY = "ENTITIES";
	public static final String SALES_CON_KEY = "SALES";
	public static final String PURCHASE_CON_KEY = "PURCHASE";
	final static Map<String, String> inputs = new LinkedHashMap<String, String>();

	@Inject
	private IImporterRegistry importerRegistry;

	private LegalEntityImporter entityImporter;
	private IClassImporter purchaseImporter;
	private IClassImporter salesImporter;

	static {
		inputs.put(ENTITIES_KEY, "Entities");
		inputs.put(PURCHASE_CON_KEY, "Purchase Contracts");
		inputs.put(SALES_CON_KEY, "Sales Contracts");
	}

	/**
	 * @since 2.0
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

			entityImporter = (LegalEntityImporter) importerRegistry.getClassImporter(CommercialPackage.eINSTANCE.getLegalEntity());
			purchaseImporter = importerRegistry.getClassImporter(CommercialPackage.eINSTANCE.getPurchaseContract());
			salesImporter = importerRegistry.getClassImporter(CommercialPackage.eINSTANCE.getSalesContract());

		}
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IImportContext context) {
		final CommercialModel commercial = CommercialFactory.eINSTANCE.createCommercialModel();
		if (inputs.containsKey(ENTITIES_KEY)) {
			commercial.getEntities().addAll((Collection<? extends LegalEntity>) entityImporter.importObjects(CommercialPackage.eINSTANCE.getLegalEntity(), inputs.get(ENTITIES_KEY), context));
			/*
			 * LegalEntity shippingEntity = entityImporter.getShippingEntity(); if (commercial.getEntities().isEmpty() == false && shippingEntity == null) {
			 * commercial.setShippingEntity(commercial.getEntities().get(0)); }
			 */
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
	public void exportModel(final UUIDObject model, final Map<String, Collection<Map<String, String>>> output, final IExportContext context) {
		final CommercialModel cm = (CommercialModel) model;
		output.put(ENTITIES_KEY, entityImporter.exportObjects(cm.getEntities(), context));
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
