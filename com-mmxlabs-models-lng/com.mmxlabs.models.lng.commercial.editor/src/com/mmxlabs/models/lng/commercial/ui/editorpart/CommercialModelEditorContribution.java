/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.editorpart;

import java.util.Arrays;
import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.lng.transfers.ui.editorpart.TransferAgreementsViewerPane;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CommercialModelEditorContribution extends BaseJointModelEditorContribution<CommercialModel> {
	private static final String HELP_TOPIC = "com.mmxlabs.lingo.doc.Editor_Commercial";
	private int pageNumber;
	private ContractEditorPane salesContractEditorPane;
	private ContractEditorPane purchaseContractEditorPane;
	private EntityEditorPane entityEditorPane;
	private CharterMarketEditorPane charterMarketEditorPane;
	private TransferAgreementsViewerPane transferAgreementsEditorPane;
	private boolean processTransferModel = LicenseFeatures.isPermitted(KnownFeatures.FEATURE_TRANSFER_MODEL);

	@Override
	public void addPages(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		

		purchaseContractEditorPane = new ContractEditorPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		salesContractEditorPane = new ContractEditorPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		entityEditorPane = new EntityEditorPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		charterMarketEditorPane = new CharterMarketEditorPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		

		purchaseContractEditorPane.createControl(sash);
		salesContractEditorPane.createControl(sash);
		charterMarketEditorPane.createControl(sash);
		
		final SashForm entitySash = new SashForm(sash, SWT.VERTICAL);
		entityEditorPane.createControl(entitySash);
		if (processTransferModel) {
			transferAgreementsEditorPane = new TransferAgreementsViewerPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			transferAgreementsEditorPane.createControl(entitySash);
			final EditingDomain domain = editorPart.getEditingDomain();
			if (domain != null) {
				transferAgreementsEditorPane.init(Arrays.asList(new EReference[] { LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_TransferModel(),
						TransfersPackage.eINSTANCE.getTransferModel_TransferAgreements() }), editorPart.getAdapterFactory(), editorPart.getModelReference());
				transferAgreementsEditorPane.getViewer().setInput(editorPart.getRootObject());
			}
		}
		

		entityEditorPane.init(Collections.singletonList(CommercialPackage.eINSTANCE.getCommercialModel_Entities()), editorPart.getAdapterFactory(), editorPart.getModelReference());
		purchaseContractEditorPane.init(Collections.singletonList(CommercialPackage.eINSTANCE.getCommercialModel_PurchaseContracts()), editorPart.getAdapterFactory(), editorPart.getModelReference());
		salesContractEditorPane.init(Collections.singletonList(CommercialPackage.eINSTANCE.getCommercialModel_SalesContracts()), editorPart.getAdapterFactory(), editorPart.getModelReference());
		charterMarketEditorPane.init(Collections.singletonList(CommercialPackage.eINSTANCE.getCommercialModel_CharterContracts()), editorPart.getAdapterFactory(), editorPart.getModelReference());
		
		
		entityEditorPane.getViewer().setInput(modelObject);
		purchaseContractEditorPane.getViewer().setInput(modelObject);
		salesContractEditorPane.getViewer().setInput(modelObject);
		charterMarketEditorPane.getViewer().setInput(modelObject);

		purchaseContractEditorPane.defaultSetTitle("Purchase Contracts");
		salesContractEditorPane.defaultSetTitle("Sales Contracts");

		pageNumber = editorPart.addPage(sash);
		editorPart.setPageText(pageNumber, "Commercial");

		PlatformUI.getWorkbench().getHelpSystem().setHelp(purchaseContractEditorPane.getControl(), HELP_TOPIC);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(salesContractEditorPane.getControl(), HELP_TOPIC);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(entityEditorPane.getControl(), HELP_TOPIC);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(charterMarketEditorPane.getControl(), HELP_TOPIC);

	}

	@Override
	public void setLocked(final boolean locked) {
		entityEditorPane.setLocked(locked);
		salesContractEditorPane.setLocked(locked);
		purchaseContractEditorPane.setLocked(locked);
		charterMarketEditorPane.setLocked(locked);
		if (processTransferModel) {
			transferAgreementsEditorPane.setLocked(locked);
		}
	}

	@Override
	public boolean canHandle(final IStatus status) {

		if (status instanceof final DetailConstraintStatusDecorator dcsd) {
			if (dcsd.getTarget() instanceof BaseLegalEntity) {
				return true;
			} else if (dcsd.getTarget() instanceof BaseEntityBook) {
				return true;
			} else if (dcsd.getTarget() instanceof PurchaseContract) {
				return true;
			} else if (dcsd.getTarget() instanceof SalesContract) {
				return true;
			} else if (dcsd.getTarget() instanceof GenericCharterContract) {
				return true;
			} else if (dcsd.getTarget() instanceof TransferAgreement && processTransferModel) {
				return true;
			}else if (dcsd.getTarget() instanceof final LNGPriceCalculatorParameters expressionPriceParameters) {
				if (expressionPriceParameters.eContainer() instanceof Contract) {
					return true;
				}

			}
		}

		return false;
	}

	@Override
	public void handle(final IStatus status) {
		if (status instanceof final DetailConstraintStatusDecorator dcsd) {
			editorPart.setActivePage(pageNumber);

			EObject target = dcsd.getTarget();
			if (target instanceof final LNGPriceCalculatorParameters expressionPriceParameters) {
				if (expressionPriceParameters.eContainer() instanceof Contract) {
					target = expressionPriceParameters.eContainer();
				}
			}

			if (target instanceof BaseLegalEntity) {
				entityEditorPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
			} else if (target instanceof BaseEntityBook) {
				entityEditorPane.getScenarioViewer().setSelection(new StructuredSelection(target.eContainer()), true);
			} else if (target instanceof PurchaseContract) {
				purchaseContractEditorPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
			} else if (target instanceof SalesContract) {
				salesContractEditorPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
			} else if (target instanceof GenericCharterContract) {
				salesContractEditorPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
			} else if (target instanceof TransferAgreement && processTransferModel) {
				transferAgreementsEditorPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
			}
		}
	}
}
