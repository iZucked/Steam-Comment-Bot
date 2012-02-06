package com.mmxlabs.models.ui;

import java.util.HashMap;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;

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
public class EditorFactoryRegistry {
	private static final String EDITOR_FACTORY_EXTENSION_POINT_ID = "com.mmxlabs.models.ui.editorfactories";

	private final WeakHashMap<EClass, WeakHashMap<EStructuralFeature, IInlineEditorFactory>> inlineEditorFactoryCache = new WeakHashMap<EClass, WeakHashMap<EStructuralFeature, IInlineEditorFactory>>();
	// TODO make weaker
	private final HashMap<String, IInlineEditorFactory> cacheById = new HashMap<String, IInlineEditorFactory>();

	public EditorFactoryRegistry() {

	}

	private class EditorFactoryMatcher {
		private IConfigurationElement element;

		private boolean matchesFeatureName;
		private boolean matchesDataType;
		private boolean badMatch;

		private int referenceTypeMinGenerations = Integer.MAX_VALUE;

		private int ownerTypeMinGenerations = Integer.MAX_VALUE;

		public EditorFactoryMatcher(final IConfigurationElement element,
				final EClass owner, final EStructuralFeature feature) {
			this.element = element;
			final String featureNameEquals = element
					.getAttribute("featureNameEquals");
			if (featureNameEquals != null) {
				matchesFeatureName = true;
				badMatch = badMatch
						|| !featureNameEquals.equals(feature.getName());
			}

			final EClassifier featureType = feature.getEType();
			final boolean dataType = featureType instanceof EDataType;
			final EClass featureEClass = featureType instanceof EClass ? (EClass) featureType
					: null;

			for (final IConfigurationElement child : element.getChildren()) {
				if (child.getName().equals("FeatureAnyOf")) {
					if (dataType) {
						matchesDataType = false;
						boolean needsMatchDataType = false;
						for (final IConfigurationElement dtMatch : element
								.getChildren("EDataTypeIs")) {
							needsMatchDataType = true;
							if (dtMatch.getAttribute("EDataTypeURI").equals(
									featureType.getEPackage().getNsURI())
									&& dtMatch.getAttribute("EDataTypeName")
											.equals(featureType.getName())) {
								matchesDataType = true;
								break;
							}
						}
						badMatch = badMatch
								|| (needsMatchDataType && !matchesDataType);
					} else {
						boolean needsMatchReferenceType = false;
						for (final IConfigurationElement eceMatch : element
								.getChildren("EClassExtends")) {
							needsMatchReferenceType = true;
							final String eClassName = eceMatch
									.getAttribute("EClass");
							referenceTypeMinGenerations = Math.min(
									referenceTypeMinGenerations,
									getMinimumGenerations(featureEClass,
											eClassName));
						}
						badMatch = badMatch
								|| (needsMatchReferenceType && referenceTypeMinGenerations == Integer.MAX_VALUE);
					}
				} else if (child.getName().equals("OwnerAnyOf")) {
					boolean needsMatchOwner = false;
					for (final IConfigurationElement eceMatch : element
							.getChildren("EClassExtends")) {
						needsMatchOwner = true;
						final String eClassName = eceMatch
								.getAttribute("EClass");
						ownerTypeMinGenerations = Math.min(
								ownerTypeMinGenerations,
								getMinimumGenerations(owner, eClassName));
					}
					badMatch = badMatch
							|| (needsMatchOwner && ownerTypeMinGenerations == Integer.MAX_VALUE);
				}
			}
		}

		private int getMinimumGenerations(final EClass eClass,
				final String canonicalNameOfSuper) {
			if (eClass.getInstanceClass().getCanonicalName()
					.equals(canonicalNameOfSuper)) {
				return 0;
			}
			int result = Integer.MAX_VALUE;
			for (final EClass superClass : eClass.getESuperTypes()) {
				final int d = getMinimumGenerations(superClass,
						canonicalNameOfSuper);
				if (d - 1 < result - 1) {
					result = d + 1;
				}
			}
			return result;
		}

		public EditorFactoryMatcher getBetterMatch(
				final EditorFactoryMatcher other) {
			if (other == null)
				return this;
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
				return this;

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
			final String id = element.getAttribute("id");
			if (cacheById.containsKey(id)) {
				return cacheById.get(id);
			} else {
				IInlineEditorFactory result = null;
				try {
					result = (IInlineEditorFactory) element
							.createExecutableExtension("factoryClass");
				} catch (CoreException e) {

				}
				
				cacheById.put(id, result);
				return result;
			}
		}
	}

	/**
	 * Search for an editor factory in the extensions list
	 * 
	 * @param owner
	 * @param feature
	 * @return
	 */
	public synchronized IInlineEditorFactory getEditorFactory(
			final EClass owner, final EStructuralFeature feature) {
		WeakHashMap<EStructuralFeature, IInlineEditorFactory> cache2 = inlineEditorFactoryCache
				.get(owner);
		if (cache2 == null) {
			cache2 = new WeakHashMap<EStructuralFeature, IInlineEditorFactory>();
			inlineEditorFactoryCache.put(owner, cache2);
		}
		if (cache2.containsKey(feature)) {
			return cache2.get(feature);
		} else {
			IInlineEditorFactory cachedFactory = null;
			EditorFactoryMatcher matcher = null;
			for (final IConfigurationElement element : Platform
					.getExtensionRegistry().getConfigurationElementsFor(
							EDITOR_FACTORY_EXTENSION_POINT_ID)) {
				// check whether element matches
				for (final IConfigurationElement factoryElement : element
						.getChildren("editorFactory")) {
					matcher = new EditorFactoryMatcher(element, owner, feature)
							.getBetterMatch(matcher);
				}
			}

			if (matcher != null) {
				cachedFactory = matcher.instantiate();
			}

			cache2.put(feature, cachedFactory);
			return cachedFactory;
		}
	}
}
