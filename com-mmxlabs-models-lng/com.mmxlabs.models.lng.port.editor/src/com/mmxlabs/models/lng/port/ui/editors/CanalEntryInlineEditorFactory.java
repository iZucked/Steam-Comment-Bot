/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editors;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;

public class CanalEntryInlineEditorFactory implements IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {

		ArrayList<Object> objectsList = new ArrayList<>();
		for (final CanalEntry canalEntry : CanalEntry.values()) {
			final String name = PortModelLabeller.getName(canalEntry);

			objectsList.add(name);
			objectsList.add(canalEntry);
		}
		return new EENumInlineEditor((EAttribute) feature, objectsList.toArray()) {
			@Override
			protected boolean updateOnChangeToFeature(Object changedFeature) {
				// update names
				if (changedFeature == CargoPackage.Literals.CANAL_BOOKING_SLOT__ROUTE_OPTION) {
					updateNames();
					return true;
				}
				return super.updateOnChangeToFeature(changedFeature);
			}
			
			@Override
			protected void updateControl() {
				super.updateControl();
				updateNames();
			}

			private void updateNames() {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
				PortModel portModel = scenarioModel.getReferenceModel().getPortModel();

				if (input instanceof CanalBookingSlot) {
					CanalBookingSlot canalBookingSlot = (CanalBookingSlot) input;
					names.clear();
					for (final CanalEntry literal : CanalEntry.values()) {
						names.add(ModelDistanceProvider.getCanalEntranceName(portModel, canalBookingSlot.getRouteOption(), literal));
					}
				}
				combo.setItems(names.toArray(new String[names.size()]));
			}
		};
	}

}
