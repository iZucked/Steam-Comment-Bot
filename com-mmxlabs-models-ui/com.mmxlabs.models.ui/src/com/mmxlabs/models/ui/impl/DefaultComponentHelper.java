/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;
import com.mmxlabs.models.ui.editors.impl.ReadOnlyInlineEditorWrapper;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

public class DefaultComponentHelper implements IComponentHelper {

	protected final List<IComponentHelper> superClassesHelpers = new ArrayList<>();

	/**
	 * The class for this component helper. Separate to the topClass in the method
	 * parameters which are the top level class whe are querying.
	 */
	protected final EClass targetClass;

	protected final Set<EStructuralFeature> ignoreFeatures = new HashSet<>();

	protected final Map<EStructuralFeature, Function<EClass, List<IInlineEditor>>> editorFactories = new HashMap<>();

	public DefaultComponentHelper(final EClass targetClass) {
		this.targetClass = targetClass;

		addSuperClassHelpers(targetClass);
		// Always ignore this feature. Subclass can always remove this declaration.
		ignoreFeatures.add(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid());
	}

	/**
	 * This is called from the constructor so it can be overridden.
	 * 
	 * @param targetClass
	 */
	protected void addSuperClassHelpers(final EClass targetClass) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		for (final EClass parent : targetClass.getESuperTypes()) {
			superClassesHelpers.addAll(registry.getComponentHelpers(parent));
		}
	}

	protected void addEditors(final EStructuralFeature feature, final Function<EClass, List<IInlineEditor>> factory) {
		editorFactories.put(feature, factory);
	}

	protected void addEditor(final EStructuralFeature feature, final Function<EClass, IInlineEditor> factory) {
		editorFactories.put(feature, topClass -> {
			final IInlineEditor editor = factory.apply(topClass);
			if (editor != null) {
				return Collections.singletonList(editor);
			}
			return null;
		});
	}

	protected void addDefaultReadonlyEditor(final EStructuralFeature feature) {
		addDefaultEditorWithWrapper(feature, ReadOnlyInlineEditorWrapper::new);
	}

	protected void addDefaultEditorWithWrapper(final EStructuralFeature feature, Function<IInlineEditor, IInlineEditorEnablementWrapper> wrapperFactory) {
		editorFactories.put(feature, topClass -> {
			final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, feature);
			if (editor != null) {
				return Collections.singletonList(wrapperFactory.apply(editor));
			}
			return null;
		});
	}

	protected void addDefaultEditorForLicenseFeature(final String featureName, final EStructuralFeature feature) {
		editorFactories.put(feature, topClass -> {
			if (LicenseFeatures.isPermitted(featureName)) {
				final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, feature);
				if (editor != null) {
					return Collections.singletonList(editor);
				}
			}
			return null;
		});
	}

	protected void addEditorForLicenseFeature(final String featureName, final EStructuralFeature feature, final Function<EClass, IInlineEditor> factory) {
		editorFactories.put(feature, topClass -> {
			if (LicenseFeatures.isPermitted(featureName)) {
				final IInlineEditor editor = factory.apply(topClass);
				if (editor != null) {
					return Collections.singletonList(editor);
				}
			}
			return null;
		});
	}

	/**
	 * Entry point called from a display composite.
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		final List<IInlineEditor> editors = new LinkedList<>();
		// Create a dummy implementation to gather the editors allowing changes to the
		// order before callign the real class.
		final IInlineEditorContainer container = editor -> {
			editors.add(editor);
			return editor;
		};
		addEditorsToComposite(container, targetClass);

		sortEditors(editors);

		editors.forEach(detailComposite::addInlineEditor);
	}

	/**
	 * Typically called from {@link #addEditorsToComposite(IInlineEditorContainer)}
	 * as we recurse through type hierarchy
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// Add super class fields before we add this class fields
		for (final IComponentHelper helper : superClassesHelpers) {
			helper.addEditorsToComposite(detailComposite, topClass);
		}

		// Add fields from this class
		for (final EStructuralFeature feature : targetClass.getEStructuralFeatures()) {
			// Check the ignore list.
			if (ignoreFeatures.contains(feature)) {
				continue;
			}

			if (editorFactories.containsKey(feature)) {
				final List<IInlineEditor> editors = editorFactories.get(feature).apply(topClass);
				if (editors != null) {
					editors.forEach(detailComposite::addInlineEditor);
				}
			} else {
				final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, feature);
				if (editor != null) {
					detailComposite.addInlineEditor(editor);
				}
			}
		}
	}

	/**
	 * Move the beforeFeature to before the afterfeature
	 * 
	 * @param editors
	 * @param beforeFeature
	 * @param afterFeature
	 */
	protected void sortEditorBeforeOtherEditor(final List<IInlineEditor> editors, EStructuralFeature beforeFeature, EStructuralFeature afterFeature) {

		// There may be multiple editors for the same featire, so gather them here...
		final List<IInlineEditor> editorsForFeature = new LinkedList<>();
		IInlineEditor afterEditor = null;
		for (final var editor : editors) {
			if (editor.getFeature() == beforeFeature) {
				editorsForFeature.add(editor);
			}
			if (afterEditor == null && editor.getFeature() == afterFeature) {
				afterEditor = editor;
			}
		}

		if (afterEditor != null && !editorsForFeature.isEmpty()) {
			editors.removeAll(editorsForFeature);
			int idx = editors.indexOf(afterEditor);
			editors.addAll(idx, editorsForFeature);
		}
	}

	/**
	 * Sorts the editors list so that editors are ordered first by the
	 * orderedFeatures list then by anything else. Multiple editors for the same
	 * feature should still be in the same order.
	 * 
	 * @param editors
	 * @param orderedFeatures
	 */
	protected void sortEditors(final List<IInlineEditor> editors, final List<EStructuralFeature> orderedFeatures) {

		// Reverse the list so that we can move the editors to the head of the list
		Collections.reverse(orderedFeatures);
		for (final var feature : orderedFeatures) {
			// There may be multiple editors for the same featire, so gather them here...
			final List<IInlineEditor> editorsForFeature = new LinkedList<>();
			for (final var editor : editors) {
				if (editor.getFeature() == feature) {
					editorsForFeature.add(editor);
				}
			}
			// Then move them to the start of the editors list in order.
			if (!editorsForFeature.isEmpty()) {
				editors.removeAll(editorsForFeature);
			}
			editors.addAll(0, editorsForFeature);
		}
	}

	protected void sortEditors(final List<IInlineEditor> editors) {
		// Left empty for sub-classes
	}
}
