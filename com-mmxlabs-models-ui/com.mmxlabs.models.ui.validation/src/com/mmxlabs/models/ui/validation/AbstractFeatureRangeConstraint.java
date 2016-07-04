/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

/**
 */
public abstract class AbstractFeatureRangeConstraint extends AbstractModelMultiConstraint {
	protected Map<EStructuralFeature, Double> minValues = new HashMap<EStructuralFeature, Double>();
	protected Map<EStructuralFeature, Double> maxValues = new HashMap<EStructuralFeature, Double>();
	protected Map<EStructuralFeature, String> featureLabels = new HashMap<EStructuralFeature, String>();

	public AbstractFeatureRangeConstraint() {
		createConstraints();
	}

	/**
	 * Sets the required range for a particular feature. The min and max values may be null for "any". A label string to use when displaying the feature may also be supplied.
	 * 
	 * @param feature
	 * @param min
	 * @param max
	 * @param label
	 */
	protected void setRange(final EStructuralFeature feature, final Double min, final Double max, final String label) {
		if (min != null) {
			minValues.put(feature, min);
		}
		if (max != null) {
			maxValues.put(feature, max);
		}
		if (label != null) {
			featureLabels.put(feature, label);
		}
	}

	/**
	 * Returns a collection of the features which this constraint applies to.
	 * 
	 * @return
	 */
	private Collection<EStructuralFeature> getConstrainedFeatures() {
		final HashSet<EStructuralFeature> result = new HashSet<EStructuralFeature>(minValues.keySet());
		result.addAll(maxValues.keySet());
		return result;
	}

	/**
	 * Returns the display label to use for a particular feature.
	 * 
	 * @param feature
	 * @return
	 */
	private String getLabel(final EStructuralFeature feature) {
		if (featureLabels.containsKey(feature)) {
			return featureLabels.get(feature);
		}

		return feature.getEContainingClass().getName() + " " + feature.getName();
	}

	/**
	 * Implement this method to indicate whether or not a particular object's feature should be validated.
	 * 
	 * @param object
	 * @param feature
	 * @return
	 */
	abstract protected boolean shouldValidateFeature(EObject object, EStructuralFeature feature);

	/**
	 * Implementing classes should set their constraints here, via setRange() calls.
	 */
	abstract protected void createConstraints();

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		for (final EStructuralFeature feature : getConstrainedFeatures()) {
			if (target.eIsSet(feature) && shouldValidateFeature(target, feature)) {
				final String className = target.eClass().getName();
				final String featureName = feature.getName();

				final Object value = target.eGet(feature);

				if (value instanceof Number) {
					final Double number = ((Number) value).doubleValue();

					String comparator = null;
					Double comparison = null;

					final Double minValue = minValues.get(feature);
					final Double maxValue = maxValues.get(feature);
					if (minValue != null && number <= minValue) {
						comparator = "more";
						comparison = minValue;
					} else if (maxValue != null && number >= maxValue) {
						comparator = "less";
						comparison = maxValue;
					}

					if (comparison != null) {
						final String message = String.format("%s is %.2f (should be %s than %.2f)", getLabel(feature), number, comparator, comparison);
						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dcsd.addEObjectAndFeature(target, feature);
						statuses.add(dcsd);
					}
				} else if (value != null) {
					final String message = String.format("%s %s has non-numeric value", className, featureName);
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dcsd.addEObjectAndFeature(target, feature);
					statuses.add(dcsd);

				}

			}
		}

		return getPluginId();

	}

	/**
	 * Implementing sub-classes should implement this with the body "return Activator.PLUGIN_ID;" (assuming there is a local Activator class to import).
	 * 
	 * @return
	 */
	protected abstract String getPluginId();
}
