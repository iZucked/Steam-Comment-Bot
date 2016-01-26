/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.importer.CargoImporter;
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
 * 
 */
public final class CargoImportAction extends SimpleImportAction {

	private static final Logger log = LoggerFactory.getLogger(CargoImportAction.class);

	public CargoImportAction(final IScenarioEditingLocation part, final ScenarioTableViewer viewer) {
		super(part, viewer);
	}
	
	public CargoImportAction(final ImportHooksProvider iph, final EObject container, final EReference containment) {
		super(iph, container, containment);
	}

	/**
	 * Merge the imported list of objects with the existing {@link Cargo}es and {@link Slot}s in the {@link CargoModel}.
	 */
	@Override
	public Command mergeImports(final EObject container, final EReference containment, final Collection<EObject> imports) {

		final CargoModel tmpCargoModel = CargoFactory.eINSTANCE.createCargoModel();

		for (final EObject o : imports) {
			if (o instanceof Cargo) {
				final Cargo cargo = (Cargo) o;
				// Filter out broken cargoes - those with less than two slots
				for (final Slot slot : cargo.getSlots()) {
					if (slot instanceof LoadSlot) {
						final LoadSlot loadSlot = (LoadSlot) slot;
						tmpCargoModel.getLoadSlots().add(loadSlot);
					} else if (slot instanceof DischargeSlot) {
						final DischargeSlot dischargeSlot = (DischargeSlot) slot;
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

		final EditingDomain destEditingDomain = importHooksProvider.getEditingDomain();

		final List<IMappingDescriptor> descriptors = new LinkedList<IMappingDescriptor>();
		final LNGScenarioModel importModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
		importModel.setCargoModel(tmpCargoModel);

		final LNGScenarioModel destScenarioModel = (LNGScenarioModel) importHooksProvider.getRootObject();

		descriptors.add(EMFModelMergeTools.generateMappingDescriptor(tmpCargoModel, destScenarioModel.getCargoModel(), CargoPackage.eINSTANCE.getCargoModel_Cargoes()));
		descriptors.add(EMFModelMergeTools.generateMappingDescriptor(tmpCargoModel, destScenarioModel.getCargoModel(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots()));
		descriptors.add(EMFModelMergeTools.generateMappingDescriptor(tmpCargoModel, destScenarioModel.getCargoModel(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots()));

		// Fix up the descriptor data references to point to destination objects in the cases where the source is not being transferred across
		EMFModelMergeTools.rewriteMappingDescriptors(descriptors, importModel, destScenarioModel);

		final CompoundCommand cmd = new CompoundCommand("Import caroges");

		cmd.append(EMFModelMergeTools.applyMappingDescriptors(destEditingDomain, destScenarioModel, descriptors));
		cmd.append(new IdentityCommand() {
			@Override
			public boolean canUndo() {
				return false;
			}
		});

		return cmd;
	}
}