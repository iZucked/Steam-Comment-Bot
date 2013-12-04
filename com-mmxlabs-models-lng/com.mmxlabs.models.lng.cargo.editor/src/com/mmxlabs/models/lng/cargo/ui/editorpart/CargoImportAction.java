/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.importer.CargoImporter;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.merge.EMFModelMergeTools;
import com.mmxlabs.models.ui.merge.IMappingDescriptor;

/**
 * Extended {@link ImportAction} implementation for {@link Cargo}es. The Cargo import imports {@link Cargo}, {@link LoadSlot} and {@link DischargeSlot} instances. The {@link Cargo} object additionally
 * encode the wiring. This import action merges the cargoes created from the {@link CargoImporter} with the existing cargoes. The {@link CargoImportAction} checks that as slots are moved between
 * cargoes, invalid cargo objects are removed (those with less than two slots). Unlike standard {@link ImportAction} sub-classes, the original model {@link EObject} instance is reused where possible -
 * normally the newly imported objects replace their original counterpart.
 * 
 * @author Simon Goodall
 * @since 3.0
 * 
 */
public final class CargoImportAction extends SimpleImportAction {

	private static final Logger log = LoggerFactory.getLogger(CargoImportAction.class);

	public CargoImportAction(final IScenarioEditingLocation part, final ScenarioTableViewer viewer) {
		super(part, viewer);
	}

	public CargoImportAction(final ImportHooksProvider iph, final FieldInfoProvider fip) {
		super(iph, fip);
	}

	/**
	 * Merge the imported list of objects with the existing {@link Cargo}es and {@link Slot}s in the {@link CargoModel}.
	 */
	@Override
	public Command mergeImports(final EObject container, final EReference containment, final Collection<EObject> imports) {

		CargoModel tmpCargoModel = CargoFactory.eINSTANCE.createCargoModel();

		for (final EObject o : imports) {
			if (o instanceof Cargo) {
				final Cargo cargo = (Cargo) o;
				// Filter out broken cargoes - those with less than two slots
				for (final Slot slot : cargo.getSlots()) {
					if (slot instanceof LoadSlot) {
						LoadSlot loadSlot = (LoadSlot) slot;
						tmpCargoModel.getLoadSlots().add(loadSlot);
					} else if (slot instanceof DischargeSlot) {
						DischargeSlot dischargeSlot = (DischargeSlot) slot;
						tmpCargoModel.getDischargeSlots().add(dischargeSlot);
					}
				}
				if (cargo.getSlots().size() < 2) {
					for (final Slot slot : cargo.getSlots()) {
						slot.setCargo(null);
					}
					assert cargo.getSlots().isEmpty();
					cargo.getSlots().clear();

				} else {
					tmpCargoModel.getCargoes().add(cargo);
				}

			} else if (o instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) o;
				tmpCargoModel.getLoadSlots().add(loadSlot);
			} else if (o instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) o;
				tmpCargoModel.getDischargeSlots().add(dischargeSlot);
			}
		}

		// /////////////////////////////////////////////////////////////
//
		EditingDomain destEditingDomain = importHooksProvider.getEditingDomain();
//		if (destEditingDomain instanceof CommandProviderAwareEditingDomain) {
//			final CommandProviderAwareEditingDomain commandProviderAwareEditingDomain = (CommandProviderAwareEditingDomain) destEditingDomain;
//
//			// Normally we disable command providers, but in this can we will not. Specifically for canal cost maintenance. The user can not add or remove these objects should the number of vessel
//			// classes or the number of route change. These are intended to be maintained by a command provider.
//
//			// commandProviderAwareEditingDomain.setCommandProvidersDisabled(true);
//
//			commandProviderAwareEditingDomain.setAdaptersEnabled(false);
//			commandProviderAwareEditingDomain.startBatchCommand();
//		}
//
//		try {
			final List<IMappingDescriptor> descriptors = new LinkedList<IMappingDescriptor>();
			final LNGScenarioModel importModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
			final LNGPortfolioModel portfolioModel = LNGScenarioFactory.eINSTANCE.createLNGPortfolioModel();
			importModel.setPortfolioModel(portfolioModel);
			portfolioModel.setCargoModel(tmpCargoModel);

			LNGScenarioModel destScenarioModel = (LNGScenarioModel) importHooksProvider.getRootObject();

			descriptors.add(EMFModelMergeTools.generateMappingDescriptor(tmpCargoModel, destScenarioModel.getPortfolioModel().getCargoModel(), CargoPackage.eINSTANCE.getCargoModel_Cargoes()));
			descriptors.add(EMFModelMergeTools.generateMappingDescriptor(tmpCargoModel, destScenarioModel.getPortfolioModel().getCargoModel(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots()));
			descriptors.add(EMFModelMergeTools.generateMappingDescriptor(tmpCargoModel, destScenarioModel.getPortfolioModel().getCargoModel(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots()));

			// Fix up the descriptor data references to point to destination objects in the cases where the source is not being transferred across
			EMFModelMergeTools.rewriteMappingDescriptors(descriptors, importModel, destScenarioModel);

			// TODO: If multiple stages, then add into a compound command
			return  EMFModelMergeTools.applyMappingDescriptors(destEditingDomain, destScenarioModel, descriptors);
//			if (cmd != null) {
//				if (cmd.canExecute()) {
//
//					destEditingDomain.getCommandStack().execute(cmd);
//				} else {
//					log.error("Unable to execute merge command", new RuntimeException());
//				}
//			}
//		} finally {
//			if (destEditingDomain instanceof CommandProviderAwareEditingDomain) {
//				final CommandProviderAwareEditingDomain commandProviderAwareEditingDomain = (CommandProviderAwareEditingDomain) destEditingDomain;
//				// commandProviderAwareEditingDomain.setCommandProvidersDisabled(false);
//				commandProviderAwareEditingDomain.endBatchCommand();
//				commandProviderAwareEditingDomain.setAdaptersEnabled(true);
//			}
//		}
	}
}