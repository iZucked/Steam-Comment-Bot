/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortCountryGroup;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.provider.PortItemProviderAdapterFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.BasicAttributeInlineEditor;
import com.mmxlabs.models.ui.editors.util.CommandUtil;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.dialogs.GroupedElementProvider;

/**
 * Editor for port groups which displays as a ticklist (maybe with categories?)
 * 
 * @author hinton
 * 
 */
public class PortGroupContentsEditor extends BasicAttributeInlineEditor {
	private CheckboxTreeViewer viewer;
	private PortModel portModel = null;
	private GroupedElementProvider gep;

	public PortGroupContentsEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {

		final Tree table = new Tree(parent, SWT.CHECK | SWT.V_SCROLL | SWT.BORDER);
		viewer = new CheckboxTreeViewer(table);

		final AdapterFactoryContentProvider baseContentProvider = new AdapterFactoryContentProvider(new PortItemProviderAdapterFactory()) {

			@Override
			public Object[] getElements(final Object object) {
				final ArrayList<Pair<String, EObject>> result = new ArrayList<>();
				final Set<String> seenCountryGroup = new HashSet<>();
				final Set<String> seenCountries = new HashSet<>();

				for (final Object o : super.getElements(object)) {
					if (o instanceof Port) {
						final Port port = (Port) o;
						result.add(Pair.of(port.getName(), port));
						if (port.getLocation() != null) {
							seenCountries.add(port.getLocation().getCountry());
						}
					}
					if (o instanceof PortCountryGroup) {
						final PortCountryGroup portCountryGroup = (PortCountryGroup) o;
						result.add(Pair.of(portCountryGroup.getName(), portCountryGroup));
						seenCountryGroup.add(portCountryGroup.getName());
					}
				}

				seenCountries.removeAll(seenCountryGroup);
				seenCountries.remove(null);
				seenCountries.remove("");

				for (String countryName : seenCountries) {
					final PortCountryGroup pcg = PortFactory.eINSTANCE.createPortCountryGroup();
					pcg.setName(countryName);
					result.add(new Pair<String, EObject>(countryName + "", pcg));
				}

				Collections.sort(result, (arg0, arg1) -> {
					if (arg0 == null) {
						if (arg1 == null) {
							return 0;
						} else {
							return 1;
						}
					} else if (arg1 == null) {
						return -1;
					}
					return arg0.getFirst().compareTo(arg1.getFirst());
				});

				return result.toArray();
			}
		};

		gep = new GroupedElementProvider(baseContentProvider);

		viewer.setContentProvider(gep);

		final LabelProvider baseLabelProvider = new LabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Pair<?, ?>) element).getFirst().toString();
			}
		};
		viewer.setLabelProvider(gep.wrapLabelProvider(baseLabelProvider));

		gep.groupers.add(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				final EObject second = ((Pair<?, EObject>) element).getSecond();
				if (second instanceof PortCountryGroup) {
					return "Country Group";
				} else if (second instanceof PortGroup) {
					return "Port Group";
				} else if (second instanceof CapabilityGroup) {
					return "Capability Group";
				} else if (second instanceof Port) {
					return "By port";
				}
				return second.eClass().getName();
			}
		});

		viewer.setCheckStateProvider(new ICheckStateProvider() {
			@Override
			public boolean isGrayed(final Object element) {
				return element == input;
			}

			@Override
			public boolean isChecked(Object element) {
				final Object value = getValue();
				if (GroupedElementProvider.E.class.isInstance(element)) {
					element = GroupedElementProvider.E.class.cast(element).value;
				}
				if (element instanceof Pair) {
					element = ((Pair) element).getSecond();
				}
				if (value instanceof Collection) {
					return ((Collection<?>) value).contains(element);
				}
				return false;
			}
		});

		viewer.addCheckStateListener(event -> {
			Object element = event.getElement();
			if (GroupedElementProvider.G.class.isInstance(element)) {
				return;
			}
			if (GroupedElementProvider.E.class.isInstance(element)) {
				element = GroupedElementProvider.E.class.cast(element).value;
			}
			if (element instanceof Pair) {
				element = ((Pair) element).getSecond();
			}
			if (element == null) {
				return;
			}

			final ArrayList<Object> c = new ArrayList<>((Collection<?>) getValue());

			if (event.getChecked()) {
				c.add(element);
			} else {
				c.remove(element);
			}

			doSetValue(c, false);
		});

		viewer.setAutoExpandLevel(2);

		return wrapControl(viewer.getControl());
	}

	@Override
	protected Command createSetCommand(final Object value) {
		return CommandUtil.createMultipleAttributeSetter(commandHandler.getEditingDomain(), input, feature, (Collection<?>) value);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject rootObject, final EObject input, final Collection<EObject> range) {
		if (rootObject instanceof LNGScenarioModel) {
			portModel = ((LNGScenarioModel) rootObject).getReferenceModel().getPortModel();
		}
		super.display(dialogContext, rootObject, input, range);
	}

	@Override
	protected void updateDisplay(final Object value) {
		ViewerHelper.setInput(viewer, true, portModel);
	}

	@Override
	protected void setControlsEnabled(final boolean enabled) {

		viewer.getControl().setEnabled(enabled);

		super.setControlsEnabled(enabled);
	}

	@Override
	protected void setControlsVisible(final boolean visible) {

		viewer.getControl().setVisible(visible);

		super.setControlsVisible(visible);
	}
}
