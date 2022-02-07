/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

public class DefaultComponentHelper implements IComponentHelper {

	protected final List<IComponentHelper> superClassesHelpers = new ArrayList<>();

	/**
	 * The class for this component helper. Separate to the topClass in the method
	 * parameters which are the top level class whe are querying.
	 */
	protected final EClass targetClass;

	protected final Set<EStructuralFeature> ignoreFeatures = new HashSet<>();

	protected final Map<EStructuralFeature, Function<EClass, IInlineEditor>> editorFactories = new HashMap<>();

	public DefaultComponentHelper(final EClass targetClass) {
		this.targetClass = targetClass;

		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		for (final EClass parent : targetClass.getESuperTypes()) {
			superClassesHelpers.addAll(registry.getComponentHelpers(parent));
		}
		// Always ignore this feature. Subclass can always remove this declaration.
		ignoreFeatures.add(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid());
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
				final IInlineEditor editor = editorFactories.get(feature).apply(topClass);
				if (editor != null) {
					detailComposite.addInlineEditor(editor);
				}
			} else {

				final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, feature);
				if (editor != null) {
					detailComposite.addInlineEditor(editor);
				}
			}
		}
	}

	protected void sortEditors(final List<IInlineEditor> editors) {
		// Left empty for sub-classes
	}
}
