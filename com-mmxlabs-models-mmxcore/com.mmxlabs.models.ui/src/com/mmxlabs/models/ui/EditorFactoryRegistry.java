package com.mmxlabs.models.ui;

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
 * Handles the extension point for editor stuff.
 * @author hinton
 *
 */
public class EditorFactoryRegistry {
	private static final String EDITOR_FACTORY_EXTENSION_POINT_ID = "com.mmxlabs.models.ui.editorfactories";

	private final IInlineEditorFactory nullEditorFactory = new IInlineEditorFactory() {
		@Override
		public IInlineEditor createEditor(EClass owner,
				EStructuralFeature feature) {
			return null;
		}
	};

	private final WeakHashMap<EClass, WeakHashMap<EStructuralFeature, IInlineEditorFactory>> inlineEditorFactoryCache = new WeakHashMap<EClass, WeakHashMap<EStructuralFeature,IInlineEditorFactory>>();

	public EditorFactoryRegistry() {

	}
	
	private class EditorFactoryMatcher {
		private IConfigurationElement element;
		
		private boolean matchesFeatureName;
		private boolean matchesDataType;
		private boolean badMatch;
		
		private int referenceTypeMinGenerations = Integer.MAX_VALUE;
		
		private int ownerTypeMinGenerations = Integer.MAX_VALUE;
		
		public EditorFactoryMatcher(final IConfigurationElement element, final EClass owner, final EStructuralFeature feature) {
			this.element = element;
			final String featureNameEquals = element.getAttribute("featureNameEquals");
			if (featureNameEquals != null) {
				matchesFeatureName = true;
				badMatch = badMatch || !featureNameEquals.equals(feature.getName());
			}
			
			final EClassifier featureType = feature.getEType();
			final boolean dataType = featureType instanceof EDataType;
			final EClass featureEClass = featureType instanceof EClass ? (EClass) featureType : null;
			
			for (final IConfigurationElement child : element.getChildren()) {
				if (child.getName().equals("FeatureAnyOf")) {
					if (dataType) {
						matchesDataType = false;
						boolean needsMatchDataType = false;
						for (final IConfigurationElement dtMatch : element.getChildren("EDataTypeIs")) {
							needsMatchDataType = true;
							if (dtMatch.getAttribute("EDataTypeURI").equals(featureType.getEPackage().getNsURI())
									&& dtMatch.getAttribute("EDataTypeName").equals(featureType.getName())) {
								matchesDataType = true;
								break;
							}
						}
						badMatch = badMatch || (needsMatchDataType && !matchesDataType);
					} else {
						boolean needsMatchReferenceType = false;
						for (final IConfigurationElement eceMatch : element.getChildren("EClassExtends")) {
							needsMatchReferenceType = true;
							final String eClassName = eceMatch.getAttribute("EClass");
							referenceTypeMinGenerations = Math.min(referenceTypeMinGenerations, getMinimumGenerations(featureEClass, eClassName));
						}
						badMatch = badMatch || (needsMatchReferenceType && referenceTypeMinGenerations == Integer.MAX_VALUE);
					}
				} else if (child.getName().equals("OwnerAnyOf")) {
					boolean needsMatchOwner = false;
					for (final IConfigurationElement eceMatch : element.getChildren("EClassExtends")) {
						needsMatchOwner = true;
						final String eClassName = eceMatch.getAttribute("EClass");
						ownerTypeMinGenerations = Math.min(ownerTypeMinGenerations, getMinimumGenerations(owner, eClassName));
					}
					badMatch = badMatch || (needsMatchOwner && ownerTypeMinGenerations == Integer.MAX_VALUE);
				}
			}
		}
		
		private int getMinimumGenerations(final EClass eClass, final String canonicalNameOfSuper) {
			if (eClass.getInstanceClass().getCanonicalName().equals(canonicalNameOfSuper)) {
				return 0;
			}
			int result = Integer.MAX_VALUE;
			for (final EClass superClass : eClass.getESuperTypes()) {
				final int d = getMinimumGenerations(superClass, canonicalNameOfSuper);
				if (d-1 < result-1) {
					result = d+1;
				}
			}
			return result;
		}
		
		public EditorFactoryMatcher getBetterMatch(final EditorFactoryMatcher other) {
			if (other == null) return this;
			if (badMatch) {
				if (other.badMatch) return null;
				return other;
			}
			
			if (other.badMatch) return this;
			
			if (matchesFeatureName && !other.matchesFeatureName) return this;
			if (other.matchesFeatureName && !matchesFeatureName) return other;
			
			if (matchesDataType && !other.matchesDataType) return this;
			if (other.matchesDataType && !matchesDataType) return this;
			
			if (referenceTypeMinGenerations < other.referenceTypeMinGenerations) return this;
			if (other.referenceTypeMinGenerations < referenceTypeMinGenerations) return other;
			
			if (ownerTypeMinGenerations < other.ownerTypeMinGenerations) return this;
			if (other.ownerTypeMinGenerations < ownerTypeMinGenerations) return other;
			
			return this;
		}

		public IInlineEditorFactory instantiate() {
			try {
				return (IInlineEditorFactory) element.createExecutableExtension("factoryClass");
			} catch (CoreException e) {
				return null;
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
		WeakHashMap<EStructuralFeature, IInlineEditorFactory> cache2 = inlineEditorFactoryCache.get(owner);
		if (cache2 == null) {
			cache2 = new WeakHashMap<EStructuralFeature, IInlineEditorFactory>();
			inlineEditorFactoryCache.put(owner, cache2);
		}
		IInlineEditorFactory cachedFactory = cache2.get(feature);
		if (cachedFactory == null) {
			EditorFactoryMatcher matcher = null;
			for (final IConfigurationElement element : Platform.getExtensionRegistry().getConfigurationElementsFor(EDITOR_FACTORY_EXTENSION_POINT_ID)) {
				// check whether element matches
				for (final IConfigurationElement factoryElement : element.getChildren("editorFactory")) {
					matcher = new EditorFactoryMatcher(element, owner, feature).getBetterMatch(matcher);
				}
			}

			if (matcher != null) {
				cachedFactory = matcher.instantiate();
				if (cachedFactory == null) cachedFactory = nullEditorFactory;
			} else {
				cachedFactory = nullEditorFactory;
			}
			
			cache2.put(feature, cachedFactory);
		}
		if (cachedFactory == nullEditorFactory) {
			return null;
		} else {
			return cachedFactory;
		}
	}
}
