/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package com.mmxlabs.scheduleview.views;

import java.util.ArrayList;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

import com.mmxlabs.common.Pair;

/**
 * @author hinton
 * 
 */
public class ScenarioPropertySourceProvider implements IPropertySourceProvider {
	private final AdapterFactoryContentProvider afcp;

	public ScenarioPropertySourceProvider(final AdapterFactory adapterFactory) {
		super();
		this.afcp = new AdapterFactoryContentProvider(adapterFactory) {

			@Override
			protected IPropertySource createPropertySource(Object object,
					IItemPropertySource itemPropertySource) {
				return new PropertySource(object, itemPropertySource) {
					// TODO really we need to override the
					// itemPropertyDescriptor to do this properly.
					@Override
					protected IPropertyDescriptor createPropertyDescriptor(
							final IItemPropertyDescriptor itemPropertyDescriptor) {
						return new PropertyDescriptor(object,
								itemPropertyDescriptor) {
							@Override
							public CellEditor createPropertyEditor(
									final Composite composite) {
								return super.createPropertyEditor(composite);
							}

							@Override
							protected CellEditor createEDataTypeCellEditor(
									EDataType eDataType, Composite composite) {
								// if (eDataType.equals(ecorePackage.getEInt())
								// || eDataType.equals(ecorePackage
								// .getELong())
								// || eDataType.equals(ecorePackage
								// .getEFloat())) {
								// final SpinnerCellEditor editor = new
								// SpinnerCellEditor(
								// composite);
								// editor.setDigits(eDataType
								// .equals(ecorePackage.getEFloat()) ? 3
								// : 0);
								// editor.setMaximumValue(10000000);
								// return editor;
								// } else {
								// return super.createEDataTypeCellEditor(
								// eDataType, composite);
								// }
								return null;
							}
						};
					}
				};
			}
		};
	}

	@Override
	public IPropertySource getPropertySource(Object object) {
		final IPropertySource defaultSource = afcp.getPropertySource(object);
		if (defaultSource == null)
			return null;
		final ArrayList<Pair<EReference, IPropertySource>> subSources = new ArrayList<Pair<EReference, IPropertySource>>();
		subSources.add(new Pair<EReference, IPropertySource>(null,
				defaultSource));
		final String topName;

		if (object instanceof EObject) {
			// flatten out containment
			final EObject eObject = (EObject) object;
			topName = ((EObject) object).eClass().getName();
			for (final EReference reference : eObject.eClass()
					.getEAllContainments()) {
				// if (reference.isMany())
				// continue;
				final Object value = eObject.eGet(reference);
				// get a source for the contained object
				
				if (reference.isMany()) {
					for (final Object o : ((Iterable) value)) {
						final IPropertySource subSource = getPropertySource(o);

						subSources.add(new Pair<EReference, IPropertySource>(reference,
								subSource));
					}
				} else {
					final IPropertySource subSource = getPropertySource(value);

					subSources.add(new Pair<EReference, IPropertySource>(reference,
							subSource));
				}
				
			}
		} else {
			topName = "";
		}

		final IPropertySource wrapper = new IPropertySource() {
			IPropertyDescriptor[] descriptors = null;

			// Map<Object, IPropertySource> idToSourceMap = new HashMap<Object,
			// IPropertySource>();

			@Override
			public void setPropertyValue(Object id, Object value) {
				assert id instanceof Pair;
				Pair<IPropertySource, Object> pair = (Pair) id;
				pair.getFirst().setPropertyValue(pair.getSecond(), value);
			}

			@Override
			public void resetPropertyValue(Object id) {
				assert id instanceof Pair;
				Pair<IPropertySource, Object> pair = (Pair) id;
				pair.getFirst().resetPropertyValue(pair.getSecond());
			}

			@Override
			public boolean isPropertySet(Object id) {
				assert id instanceof Pair;
				Pair<IPropertySource, Object> pair = (Pair) id;
				return pair.getFirst().isPropertySet(pair.getSecond());
			}

			@Override
			public Object getPropertyValue(Object id) {
				assert id instanceof Pair;
				Pair<IPropertySource, Object> pair = (Pair) id;
				return pair.getFirst().getPropertyValue(pair.getSecond());
			}

			@Override
			public IPropertyDescriptor[] getPropertyDescriptors() {
				if (descriptors == null) {
					final ArrayList<IPropertyDescriptor> subDescriptors = new ArrayList<IPropertyDescriptor>();

					for (final Pair<EReference, IPropertySource> refAndSource : subSources) {
						final IPropertySource subSource = refAndSource
								.getSecond();
						if (subSource == null)
							continue;
						final String prefixName = refAndSource.getFirst() == null ? topName
								: refAndSource.getFirst().getName();

						final IPropertyDescriptor[] subDescriptorList = subSource
								.getPropertyDescriptors();

						for (final IPropertyDescriptor descriptor : subDescriptorList) {
							final IPropertyDescriptor wrappedDescriptor = new IPropertyDescriptor() {
								final Pair<Object, Object> id = new Pair<Object, Object>(
										subSource, descriptor.getId());

								@Override
								public CellEditor createPropertyEditor(
										Composite parent) {
									return descriptor
											.createPropertyEditor(parent);
								}

								@Override
								public String getCategory() {
									return prefixName;
								}

								@Override
								public String getDescription() {
									return descriptor.getDescription();
								}

								@Override
								public String getDisplayName() {
									return descriptor.getDisplayName();
								}

								@Override
								public String[] getFilterFlags() {
									return descriptor.getFilterFlags();
								}

								@Override
								public Object getHelpContextIds() {
									return descriptor.getHelpContextIds();
								}

								@Override
								public Object getId() {
									return id;
								}

								@Override
								public ILabelProvider getLabelProvider() {
									return descriptor.getLabelProvider();
								}

								@Override
								public boolean isCompatibleWith(
										IPropertyDescriptor anotherProperty) {
									return descriptor
											.isCompatibleWith(anotherProperty);
								}

							};

							// idToSourceMap.put(wrappedDescriptor.getId(),
							// subSource);
							subDescriptors.add(wrappedDescriptor);
						}
					}

					descriptors = subDescriptors
							.toArray(new IPropertyDescriptor[0]);
				}
				return descriptors;
			}

			@Override
			public Object getEditableValue() {
				// System.err.println("Uh oh");
				// TODO what is this?
				return defaultSource.getEditableValue();
				// return null;// idToSourceMap.get(id).getEditableValue(id,
				// value);
			}
		};

		return wrapper;
	}

}
