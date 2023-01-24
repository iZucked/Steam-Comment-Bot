/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui;

import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.MMXObject.DelegateInformation;
import com.mmxlabs.models.ui.editors.util.CommandUtil;

/**
 * WIP: Util class for use in editors for the various override schemes.
 * 
 * Do not use as this is not finished. More work is needed to clean up the override mechanism to work reliably in all caes.
 * 
 * @author Simon Goodall
 *
 */
@Deprecated
public class FeatureOveridesUtil {
	private FeatureOveridesUtil() {

	}

	public static class OverrideStatus {

		public EObject input;

		public EStructuralFeature field;

		public boolean isOverridable;
		public boolean isUnsettable;
		public boolean isOverridableWithButton;

		public EStructuralFeature overrideToggleFeature;
		public EStructuralFeature classOverrideFeature;

		public boolean canOverride() {
			if (input instanceof MMXObject) {
				final MMXObject mmxinput = (MMXObject) input;
				final DelegateInformation di = mmxinput.getUnsetValueOrDelegate(field);
				if (di != null) {
					if (classOverrideFeature != null) {
						if (input.eGet(classOverrideFeature) == null) {
							return false;
						}
					}

					return true;
				}

			}
			return false;
		}

		public boolean isOverridden() {
			if (!canOverride()) {
				return true;
			}
			if (overrideToggleFeature != null) {
				return (Boolean) input.eGet(overrideToggleFeature);
			}
			return input.eIsSet(field);
		}

		public Command createSetCommand(EditingDomain domain, Object value) {
			if (value == SetCommand.UNSET_VALUE) {
				final CompoundCommand cmd = new CompoundCommand();

				cmd.append(SetCommand.create(domain, input, field, value));
				if (overrideToggleFeature != null) {
					cmd.append(SetCommand.create(domain, input, overrideToggleFeature, Boolean.FALSE));
				}
				return cmd;
			} else {
				if (field.isMany()) {
					return CommandUtil.createMultipleAttributeSetter(domain, input, field, (Collection<?>) value);
				} else {
					return SetCommand.create(domain, input, field, value);
				}
			}
		}

		public Object getValue() {

			if (input == null) {
				return null;
			}
			if (isOverridableWithButton) {
				if (!canOverride()) {
					return input.eGet(field);

				} else if (input.eIsSet(field)) {
					return input.eGet(field);

				} else {
					if (input instanceof MMXObject) {
						return ((MMXObject) input).getUnsetValue(field);
					} else {
						return null;
					}
				}
			} else if (isOverridable) {
				// For e.g. numeric inline editor
				if (!field.isUnsettable() || input.eIsSet(field)) {
					return input.eGet(field);

				} else {
					return null;
				}
			} else {
				// Original code path
				if (!field.isUnsettable() || input.eIsSet(field)) {
					return input.eGet(field);
				} else {
					if (input instanceof MMXObject) {
						return ((MMXObject) input).getUnsetValue(field);
					} else {
						return null;
					}
				}
			}
		}
	}

	public static @NonNull OverrideStatus getOverrideStatus(EObject input, EStructuralFeature field) {
		OverrideStatus status = new OverrideStatus();
		if (field == null) {
			return status;
		}
		status.input = input;
		status.field = field;
		status.isUnsettable = field.isUnsettable();

		EAnnotation overrideAnnotation = field.getEAnnotation("http://www.mmxlabs.com/models/overrideFeature");
		if (overrideAnnotation == null) {
			overrideAnnotation = field.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverride");
		}
		if (overrideAnnotation == null) {
			overrideAnnotation = field.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverrideByContainer");
		}

		boolean doNotUpdateButtonCheck = false;
		if (overrideAnnotation != null) {
			if (!overrideAnnotation.getReferences().isEmpty()) {
				status.isOverridable = true;
				status.classOverrideFeature = (EStructuralFeature) overrideAnnotation.getReferences().get(0);
			} else {
				// TODO: Extract out!
				if (field.isMany()) {
					for (EStructuralFeature f : field.getEContainingClass().getEAllAttributes()) {
						if (f.getName().equals(field.getName() + "Override")) {
							status.isOverridable = true;
							status.overrideToggleFeature = f;
							break;
						}
					}
				}
			}
			String value = overrideAnnotation.getDetails().get("showButton");
			if (value != null && value.equalsIgnoreCase("false")) {
				doNotUpdateButtonCheck = true;
				status.isOverridableWithButton = false;
			}

		}
		if (!status.isOverridable) {
			if (field.isUnsettable()) {
				status.isOverridable = true;
			} else {
				if (field.isMany()) {
					for (EStructuralFeature f : field.getEContainingClass().getEAllAttributes()) {
						if (f.getName().equals(field.getName() + "Override")) {
							status.isOverridable = true;
							status.overrideToggleFeature = f;
							break;
						}
					}
				}
			}
		}
		EAnnotation annotation = field.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverride");
		if (annotation != null) {
			for (EObject o : annotation.getReferences()) {
				if (o instanceof EStructuralFeature) {
					status.classOverrideFeature = (EStructuralFeature) o;
					break;
				}
			}
		}

		if (!doNotUpdateButtonCheck && status.isOverridable) {
			status.isOverridableWithButton = true;
		}

		return status;
	}
}
