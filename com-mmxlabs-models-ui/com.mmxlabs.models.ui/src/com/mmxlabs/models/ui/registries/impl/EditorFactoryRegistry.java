/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.registries.impl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.extensions.IInlineEditorFactoryExtension;
import com.mmxlabs.models.ui.extensions.IInlineEditorFactoryExtension.IEClassMatcher;
import com.mmxlabs.models.ui.registries.IEditorFactoryRegistry;
import com.mmxlabs.models.util.importer.registry.impl.AbstractRegistry;

/**
 * The editor factory registry matches {@link IInlineEditorFactory}s to
 * combinations of {@link EClass} and {@link EStructuralFeature}.
 * 
 * When a UI is being constructed by a typical {@link IComponentHelper}, the
 * {@link IComponentHelper} will use
 * {@link ComponentHelperUtils#createDefaultEditor(EClass, EStructuralFeature)}
 * to create the {@link IInlineEditor} for each field. This method asks this
 * registry to look up a suitable {@link IInlineEditorFactory} for the
 * combination of {@link EClass} and {@link EStructuralFeature} being displayed.
 * 
 * {@link IInlineEditorFactory}s are registered on the
 * com.mmxlabs.models.ui.editorfactories extension point. The extension point
 * can specify a feature name, an owner EClass, and a list of feature classes or
 * EDataTypes.
 * 
 * The registry will find a match for a class and feature thus:
 * <ol>
 * <li>For each extension
 * <ol>
 * <li>If the extension specifies a feature name, the name must match exactly</li>
 * <li>If the extension specifies an owner class, the EClass passed in must be a
 * subclass of that owner class</li>
 * <li>If the extension specifies some types, the feature must have a type
 * assignable to that type</li>
 * </ol>
 * </li>
 * <li>If two extensions match, the winner is the more specific one, according
 * to:
 * <ol>
 * <li>If one matching extension specifies a feature name and the other doesn't,
 * the one with the name wins</li>
 * <li>If one extension specifies a datatype and it matches, and the other
 * doesn't, the first wins</li>
 * <li>If one extension's feature reference type is more specific, it wins</li>
 * <li>If one extension's feature owner type is more specific, it wins</li>
 * </ol>
 * </li>
 * </ol>
 * 
 * @author hinton
 * 
 */
public class EditorFactoryRegistry extends AbstractRegistry<Pair<EClass, EStructuralFeature>, IInlineEditorFactory> implements IEditorFactoryRegistry {
	
	@Inject Iterable<IInlineEditorFactoryExtension> extensions;
	
	public EditorFactoryRegistry() {

	}

	private class EditorFactoryMatcher {
		private IInlineEditorFactoryExtension extension;

		private boolean matchesFeatureName;
		private boolean matchesDataType;
		private boolean badMatch = false;

		private int referenceTypeMinGenerations = Integer.MAX_VALUE;

		private int ownerTypeMinGenerations = Integer.MAX_VALUE;

		public EditorFactoryMatcher(final IInlineEditorFactoryExtension extension,
				final EClass owner, final EStructuralFeature feature) {
			this.extension = extension;
			final String featureNameEquals = extension.getFeatureName();
			if (featureNameEquals != null && !featureNameEquals.isEmpty()) {
				matchesFeatureName = true;
				badMatch = badMatch
						|| !featureNameEquals.equals(feature.getName());
			}

			final EClassifier featureType = feature.getEType();
			final boolean dataType = featureType instanceof EDataType;
			final EClass featureEClass = featureType instanceof EClass ? (EClass) featureType
					: null;

			final String extensionID = extension.getID();
			
			for (final IInlineEditorFactoryExtension.IFeatureMatcher featureMatcher : extension.getFeatureMatchers()) {
				if (dataType) {
					matchesDataType = false;
					boolean needsMatchDataType = false;
					for (final IInlineEditorFactoryExtension.IDataTypeMatcher dataTypeMatcher : featureMatcher.getDataTypeMatchers()) {
						needsMatchDataType = true;
						if (dataTypeMatcher.getDataTypeURI().equals(featureType.getEPackage().getNsURI())
								&& dataTypeMatcher.getDataTypeName().equals(featureType.getName())) {
							matchesDataType = true;
							break;
						}
					}
					badMatch = badMatch || (needsMatchDataType && !matchesDataType) || (!needsMatchDataType && featureMatcher.getEClassMatchers().length > 0);
				} else {
					boolean needsMatchReferenceType = false;
					for (final IInlineEditorFactoryExtension.IEClassMatcher classMatcher : featureMatcher.getEClassMatchers()) {
						needsMatchReferenceType = true;
						referenceTypeMinGenerations = Math.min(referenceTypeMinGenerations,
								getMinimumGenerations(featureEClass, classMatcher.getEClassName()));
					}
					badMatch = badMatch
							|| (needsMatchReferenceType && referenceTypeMinGenerations == Integer.MAX_VALUE)
							|| (!needsMatchReferenceType && featureMatcher.getDataTypeMatchers().length > 0);
				}
			}
			
			for (final IInlineEditorFactoryExtension.IOwnerMatcher ownerMatcher : extension.getOwnerMatchers()) {
				boolean needsMatchOwner = false;
				for (final IEClassMatcher classMatcher : ownerMatcher.getEClassMatchers()) {
					needsMatchOwner = true;
					ownerTypeMinGenerations = Math.min(ownerTypeMinGenerations,
							getMinimumGenerations(owner, classMatcher.getEClassName()));
				}
				badMatch = badMatch
						|| (needsMatchOwner && ownerTypeMinGenerations == Integer.MAX_VALUE);
			}
		}
	

	public EditorFactoryMatcher getBetterMatch(
				final EditorFactoryMatcher other) {
			if (other == null)
				return this.badMatch ? null : this;
			
			if (badMatch) {
				if (other.badMatch)
					return null;
				return other;
			}

			if (other.badMatch)
				return this;

			if (matchesFeatureName && !other.matchesFeatureName)
				return this;
			if (other.matchesFeatureName && !matchesFeatureName)
				return other;

			if (matchesDataType && !other.matchesDataType)
				return this;
			if (other.matchesDataType && !matchesDataType)
				return other;

			if (referenceTypeMinGenerations < other.referenceTypeMinGenerations)
				return this;
			if (other.referenceTypeMinGenerations < referenceTypeMinGenerations)
				return other;

			if (ownerTypeMinGenerations < other.ownerTypeMinGenerations)
				return this;
			if (other.ownerTypeMinGenerations < ownerTypeMinGenerations)
				return other;

			return this;
		}

		public IInlineEditorFactory instantiate() {
			if (factoryExistsForID(extension.getID())) return getFactoryForID(extension.getID());
			else return cacheFactoryForID(extension.getID(), extension.instantiate());
		}
	}

	final Map<EClass, Map<EStructuralFeature, IInlineEditorFactory>> fasterCache = new HashMap<EClass, Map<EStructuralFeature, IInlineEditorFactory>>();
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.models.ui.registries.impl.IEditorFactoryRegistry#getEditorFactory(org.eclipse.emf.ecore.EClass, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	@Override
	public IInlineEditorFactory getEditorFactory(
			final EClass owner, final EStructuralFeature feature) {
		Map<EStructuralFeature, IInlineEditorFactory> byClass = fasterCache.get(owner);
		if (byClass == null) {
			byClass = new HashMap<EStructuralFeature, IInlineEditorFactory>();
			fasterCache.put(owner, byClass);
		}
		if (byClass.containsKey(feature)) return byClass.get(feature);
		
		EditorFactoryMatcher matcher = null;
		for (final IInlineEditorFactoryExtension extension : extensions) {
			matcher = new EditorFactoryMatcher(extension, owner, feature).getBetterMatch(matcher);
		}
		if (matcher == null) return null;
		
		IInlineEditorFactory factory = matcher.instantiate();
		
		byClass.put(feature, factory);
		
		return factory;
	}

	@Override
	protected IInlineEditorFactory match(final Pair<EClass, EStructuralFeature> key) {
		EditorFactoryMatcher matcher = null;
		for (final IInlineEditorFactoryExtension extension : extensions) {
			matcher = new EditorFactoryMatcher(extension, key.getFirst(), key.getSecond()).getBetterMatch(matcher);
		}
		if (matcher == null) return null;
		
		return matcher.instantiate();
	}
}
