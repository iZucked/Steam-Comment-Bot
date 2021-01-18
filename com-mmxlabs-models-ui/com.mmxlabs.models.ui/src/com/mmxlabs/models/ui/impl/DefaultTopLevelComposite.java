/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;

/**
 * The default composite used to display an EObject
 * 
 * @author hinton
 * 
 */
public class DefaultTopLevelComposite extends Composite implements IDisplayComposite {

	public static class ChildCompositeContainer {
		public List<EReference> childReferences = new LinkedList<>();
		public List<IDisplayComposite> childComposites = new LinkedList<>();
		public List<EObject> childObjects = new LinkedList<>();
	}

	/**
	 * Adapter factory instance. This contains all factories registered in the global registry.
	 */
	protected final ComposedAdapterFactory FACTORY = createAdapterFactory();

	protected IDisplayComposite topLevel = null;
	protected List<ChildCompositeContainer> childCompositeContainers = new LinkedList<>();
	protected ICommandHandler commandHandler;
	protected IDisplayCompositeLayoutProvider layoutProvider = new DefaultDisplayCompositeLayoutProvider();
	protected IInlineEditorWrapper editorWrapper = IInlineEditorWrapper.IDENTITY;
	protected IDialogEditingContext dialogContext;
	protected FormToolkit toolkit;

	public DefaultTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		super(parent, style);
		this.dialogContext = dialogContext;
		this.toolkit = toolkit;
		toolkit.adapt(this);
	}

	/**
	 */
	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		final EClass eClass = object.eClass();
		final Group g = new Group(this, SWT.NONE);
		toolkit.adapt(g);
		g.setText(EditorUtils.unmangle(object));
		g.setLayout(new FillLayout());
		g.setLayoutData(layoutProvider.createTopLayoutData(root, object, object));
		topLevel = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(eClass).createSublevelComposite(g, eClass, dialogContext, toolkit);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);

		topLevel.display(dialogContext, root, object, range, dbc);

		final int numChildren = createDefaultChildCompositeSection(dialogContext, root, object, range, dbc, eClass, this);
		setLayout(layoutProvider.createTopLevelLayout(root, object, numChildren + 1));
	}

	protected int createDefaultChildCompositeSection(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range,
			final EMFDataBindingContext dbc, final EClass eClass, final Composite parent) {
		final ChildCompositeContainer childContainer = createChildComposites(root, object, eClass, parent);

		final Iterator<IDisplayComposite> children = childContainer.childComposites.iterator();
		final Iterator<EObject> childObjectsItr = childContainer.childObjects.iterator();

		while (childObjectsItr.hasNext()) {
			final IDisplayComposite next = children.next();
			GridData gridData = (GridData) next.getComposite().getLayoutData();
			if (gridData == null) {
				gridData = GridDataFactory.swtDefaults().create();
			}
			gridData.verticalAlignment = GridData.BEGINNING;
			next.getComposite().setLayoutData(gridData);

			next.display(dialogContext, root, childObjectsItr.next(), range, dbc);
		}

		childCompositeContainers.add(childContainer);
		return childContainer.childComposites.size();
	}

	/**
	 * SPECULATIVE DOCUMENTATION
	 *
	 * Creates a series of display components for all of the "reference" subcomponents of an object, for the purpose of a GUI editor.
	 * 
	 * Silently populates the following fields: childReferences childComposites childObjects
	 * 
	 * @param root
	 *            The root object for the entire data model
	 * @param object
	 *            The object being edited
	 * @param eClass
	 *            The object's class
	 * @param parent
	 *            The GUI component to add sub-components to
	 */
	protected ChildCompositeContainer createChildComposites(final MMXRootObject root, final EObject object, final EClass eClass, final Composite parent) {
		final ChildCompositeContainer childReferences = new ChildCompositeContainer();
		for (final EReference ref : eClass.getEAllReferences()) {
			if (shouldDisplay(ref)) {
				if (ref.isMany()) {
					final List values = (List) object.eGet(ref);
					for (final Object o : values) {
						if (o instanceof EObject) {
							createChildArea(childReferences, root, object, parent, ref, (EObject) o);
						}
					}
				} else {
					final EObject value = (EObject) object.eGet(ref);

					createChildArea(childReferences, root, object, parent, ref, value);
				}
			}
		}
		return childReferences;
	}

	protected IDisplayComposite createChildArea(final ChildCompositeContainer childCompositeContainer, final MMXRootObject root, final EObject object, final Composite parent, final EReference ref,
			final EObject value) {
		final String label = EditorUtils.unmangleFeature(ref, object);
		return createChildArea(childCompositeContainer, root, object, parent, ref, label, value);

	}

	/**
	 * SPECULATIVE DOCUMENTATION
	 * 
	 * Creates a "sub level composite" display component for a GUI editor on the object "object" which allows editing of one of its subcomponents.
	 * 
	 * Silently modifies the following fields: childReferences childComposites childObjects
	 * 
	 * @param root
	 *            The root object for the entire data model
	 * @param object
	 *            The parent data object being edited
	 * @param parent
	 *            The display component to add relevant new visual sub-components to
	 * @param ref
	 *            The EReference representing the field metadata
	 * @param value
	 *            The object's sub-component value (which may be one of many, if the field is a list)
	 * @return
	 */
	protected IDisplayComposite createChildArea(final ChildCompositeContainer childCompositeContainer, final MMXRootObject root, final EObject object, final Composite parent, final EReference ref,
			final String groupLabel, final EObject value) {
		final BiFunction<EObject, Composite, IDisplayComposite> factory = (v, g) -> Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(v.eClass())
				.createSublevelComposite(g, v.eClass(), dialogContext, toolkit);
		return createChildArea(childCompositeContainer, root, object, parent, ref, groupLabel, value, factory, null);
	}

	protected IDisplayComposite createChildArea(final ChildCompositeContainer childCompositeContainer, final MMXRootObject root, final EObject object, final Composite parent, final EReference ref,
			final String groupLabel, final EObject value, final Consumer<Composite> compositeAction) {
		final BiFunction<EObject, Composite, IDisplayComposite> factory = (v, g) -> Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(v.eClass())
				.createSublevelComposite(g, v.eClass(), dialogContext, toolkit);
		return createChildArea(childCompositeContainer, root, object, parent, ref, groupLabel, value, factory, compositeAction);

	}

	protected IDisplayComposite createChildArea(final ChildCompositeContainer childCompositeContainer, final MMXRootObject root, final EObject object, final Composite parent, final EReference ref,
			final String groupLabel, final EObject value, final BiFunction<EObject, Composite, IDisplayComposite> factory, final Consumer<Composite> compositeAction) {
		if (value != null) {
			Composite p = parent;
			if (groupLabel != null) {
				final Group g2 = new Group(parent, SWT.NONE);
				toolkit.adapt(g2);
				g2.setText(groupLabel);
				g2.setLayout(new GridLayout(1, true));
				g2.setLayoutData(layoutProvider.createTopLayoutData(root, object, value));

				// Tooltips
				if (object != null) {
					// Set to blank by default - and replace below if the feature is
					// found
					String toolTip = "";
					// This will fetch the property source of the input object
					final IItemPropertySource inputPropertySource = (IItemPropertySource) FACTORY.adapt(object, IItemPropertySource.class);

					// Iterate through the property descriptors to find a matching
					// descriptor for the feature
					for (final IItemPropertyDescriptor descriptor : inputPropertySource.getPropertyDescriptors(object)) {

						final Object feature = descriptor.getFeature(object);
						if (ref.equals(feature)) {
							// Found match
							toolTip = descriptor.getDescription(value).replace("{0}", EditorUtils.unmangle(object.eClass().getName()).toLowerCase());
							break;
						}
					}

					g2.setToolTipText(toolTip);
				}
				p = g2;
			}

			final IDisplayComposite sub = factory.apply(value, p);
			sub.setCommandHandler(commandHandler);
			sub.setEditorWrapper(editorWrapper);
			childCompositeContainer.childReferences.add(ref);
			childCompositeContainer.childComposites.add(sub);
			childCompositeContainer.childObjects.add(value);

			if (compositeAction != null) {
				compositeAction.accept(p);
			}

			return sub;
		}
		return null;
	}

	protected boolean shouldDisplay(final EReference ref) {
		return ref.isContainment() && !ref.isMany();
	}

	@Override
	public Composite getComposite() {
		return this;
	}

	@Override
	public void setCommandHandler(final ICommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	@Override
	public void displayValidationStatus(final IStatus status) {
		if (topLevel != null) {
			topLevel.displayValidationStatus(status);
		}
		for (final ChildCompositeContainer childContainer : childCompositeContainers) {
			for (final IDisplayComposite child : childContainer.childComposites) {
				child.displayValidationStatus(status);
			}
		}
	}

	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {
		this.editorWrapper = wrapper;
		if (topLevel != null) {
			topLevel.setEditorWrapper(editorWrapper);
		}
		for (final ChildCompositeContainer childContainer : childCompositeContainers) {
			for (final IDisplayComposite child : childContainer.childComposites) {
				child.setEditorWrapper(editorWrapper);
			}
		}
	}

	public ICommandHandler getCommandHandler() {
		return commandHandler;
	}

	public void setLayoutProvider(final IDisplayCompositeLayoutProvider layoutProvider) {
		this.layoutProvider = layoutProvider;
	}

	@Override
	public boolean checkVisibility(final IDialogEditingContext context) {

		boolean changed = false;
		if (topLevel != null) {
			topLevel.checkVisibility(context);
		}
		for (final ChildCompositeContainer childContainer : childCompositeContainers) {
			for (final IDisplayComposite child : childContainer.childComposites) {
				changed |= child.checkVisibility(context);
			}
		}
		return changed;
	}

	/**
	 * Utility method to create a {@link ComposedAdapterFactory}. Taken from org.eclipse.emf.compare.util.AdapterUtils.
	 * 
	 * @return
	 */
	public static ComposedAdapterFactory createAdapterFactory() {
		final List<AdapterFactory> factories = new ArrayList<>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return new ComposedAdapterFactory(factories);
	}
}
