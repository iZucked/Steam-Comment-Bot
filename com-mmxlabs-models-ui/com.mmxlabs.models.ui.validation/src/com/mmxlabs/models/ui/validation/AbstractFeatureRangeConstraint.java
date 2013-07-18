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

public abstract class AbstractFeatureRangeConstraint extends AbstractModelMultiConstraint {
	protected Map<EStructuralFeature, Double> minValues = new HashMap<EStructuralFeature, Double>();
	protected Map<EStructuralFeature, Double> maxValues = new HashMap<EStructuralFeature, Double>();
	
	protected void setMin(EStructuralFeature feature, double value) {
		minValues.put(feature, value);
	}
	
	protected void setMax(EStructuralFeature feature, double value) {
		maxValues.put(feature, value);
	}
	
	protected void setRange(EStructuralFeature feature, double min, double max) {
		setMin(feature, min);
		setMax(feature, max);
	}
	
	protected Collection<EStructuralFeature> getConstrainedFeatures() {
		HashSet<EStructuralFeature> result = new HashSet<EStructuralFeature>(minValues.keySet());
		result.addAll(maxValues.keySet());
		return result;
	}
	
	protected boolean shouldValidateFeature(EObject object, EStructuralFeature feature) {
		return true;
	}	

	protected String validate(final IValidationContext ctx, List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		for (EStructuralFeature feature: getConstrainedFeatures()) {
			if (shouldValidateFeature(target,feature)) {
				final String className = target.eClass().getName();
				final String featureName = feature.getName();
			
				final Object value = target.eGet(feature);
				
				if (value instanceof Number) {
					final Double number = ((Number) value).doubleValue();
					
					String comparator = null;
					Double comparison = null;
					
					final Double minValue = minValues.get(feature);
					final Double maxValue = maxValues.get(feature);
					if (minValue != null && number < minValue) {
						comparator = "more";
						comparison = minValue;
					}
					else if (minValue != null && number > maxValue) {
						comparator = "less";
						comparison = maxValue;
					}
					
					if (comparison != null) {
						final String message = String.format("%s %s is %.2f (should be %s than %.2f)", className, featureName, number, comparator, comparison);
						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dcsd.addEObjectAndFeature(target, feature);
						statuses.add(dcsd);
					}					
				}			
				else if (value != null) {
					final String message = String.format("%s %s has non-numeric value", className, featureName);
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dcsd.addEObjectAndFeature(target, feature);
					statuses.add(dcsd);
					
				}

			}
		}

		return getPluginId();
		
	}

	protected abstract String getPluginId();
}
