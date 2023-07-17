/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.Collection;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryFacilityType;
import com.mmxlabs.models.lng.cargo.InventoryFeedRow;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.tabular.manipulators.IdentityInlineEditorWrapper;

/**
 * A component helper for InventoryEventRow instances
 *
 * @generated NOT
 */
public class InventoryFeedRowComponentHelper extends InventoryEventRowComponentHelper {

	public InventoryFeedRowComponentHelper() {
		super(CargoPackage.Literals.INVENTORY_FEED_ROW);
		final @NonNull IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(CargoPackage.Literals.INVENTORY_FEED_ROW, CargoPackage.Literals.INVENTORY_FEED_ROW__CV);
		final @NonNull IdentityInlineEditorWrapper newEditor = new IdentityInlineEditorWrapper(editor) {
			boolean canBeVisible = true;

			@Override
			public boolean isEditorVisible() {
				return canBeVisible && super.isEditorVisible();
			}

			@Override
			public void display(IDialogEditingContext dialogContext, MMXRootObject scenario, EObject object, Collection<EObject> range) {
				final Optional<@NonNull Inventory> inventory = range.stream().filter(Inventory.class::isInstance).map(Inventory.class::cast).findAny();
				if (editor.getEditorTarget() instanceof InventoryFeedRow && inventory.isPresent()) {
					canBeVisible = inventory.get().getFacilityType() != InventoryFacilityType.UPSTREAM;
				}
				super.display(dialogContext, scenario, object, range);
			}
		};
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INVENTORY_CV_MODEL)) {
			addEditor(CargoPackage.Literals.INVENTORY_FEED_ROW__CV, topClass -> newEditor);
		} else {
			ignoreFeatures.add(CargoPackage.Literals.INVENTORY_FEED_ROW__CV);
		}
	}
}