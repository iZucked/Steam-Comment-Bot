/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.ui.editorpart;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory.ISetting;

/**
 */
public class ActualsEditingCommands {

	private final EditingDomain editingDomain;

	private final LNGScenarioModel rootObject;

	/**
	 */
	public ActualsEditingCommands(final EditingDomain editingDomain, final LNGScenarioModel rootObject) {
		this.editingDomain = editingDomain;
		this.rootObject = rootObject;
	}

	@SuppressWarnings("unchecked")
	public <@NonNull T> T createObject(final EClass clz, final EReference reference, final EObject container) {
		final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(clz);

		// TODO: Pre-generate and link to UI
		// TODO: Add FOB/DES etc as explicit slot types.
		final IModelFactory factory = factories.get(0);
		final Collection<? extends ISetting> settings = factory.createInstance(rootObject, container, reference, null);
		if (settings.isEmpty() == false) {

			for (final ISetting setting : settings) {

				return (T) setting.getInstance();
			}
		}
		throw new IllegalStateException("Unable to create requested instance");
	}

	public CargoActuals createNewCargoActuals(final List<Command> setCommands, final ActualsModel actualsModel) {
		// Create a cargo
		final CargoActuals newCargoActuals = createObject(ActualsPackage.eINSTANCE.getCargoActuals(), ActualsPackage.eINSTANCE.getActualsModel_CargoActuals(), actualsModel);
		setCommands.add(AddCommand.create(editingDomain, actualsModel, ActualsPackage.eINSTANCE.getActualsModel_CargoActuals(), newCargoActuals));

		final LoadActuals newLoadActuals = createObject(ActualsPackage.eINSTANCE.getLoadActuals(), ActualsPackage.eINSTANCE.getCargoActuals_Actuals(), newCargoActuals);
		setCommands.add(AddCommand.create(editingDomain, newCargoActuals, ActualsPackage.eINSTANCE.getCargoActuals_Actuals(), newLoadActuals));

		final DischargeActuals newDischargeActuals = createObject(ActualsPackage.eINSTANCE.getDischargeActuals(), ActualsPackage.eINSTANCE.getCargoActuals_Actuals(), newCargoActuals);
		setCommands.add(AddCommand.create(editingDomain, newCargoActuals, ActualsPackage.eINSTANCE.getCargoActuals_Actuals(), newDischargeActuals));
		return newCargoActuals;
	}

	// public LoadActuals createNewLoad(final List<Command> setCommands, final ActualsModel actualsModel, final boolean isDESPurchase) {
	//
	// final LoadActuals newLoadActuals = createObject(ActualsPackage.eINSTANCE.getLoadActuals(), ActualsPackage.eINSTANCE.getActualsModel_LoadActuals(), actualsModel);
	// setCommands.add(AddCommand.create(editingDomain, actualsModel, ActualsPackage.eINSTANCE.getActualsModel_LoadActuals(), newLoadActuals));
	//
	// return newLoadActuals;
	// }
	//
	// public DischargeActuals createNewDischarge(final List<Command> setCommands, final ActualsModel actualsModel, final boolean isFOBSale) {
	//
	// final DischargeActuals newDischargeActuals = createObject(ActualsPackage.eINSTANCE.getDischargeActuals(), ActualsPackage.eINSTANCE.getActualsModel_DischargeActuals(), actualsModel);
	// setCommands.add(AddCommand.create(editingDomain, actualsModel, ActualsPackage.eINSTANCE.getActualsModel_DischargeActuals(), newDischargeActuals));
	// return newDischargeActuals;
	// }
}
