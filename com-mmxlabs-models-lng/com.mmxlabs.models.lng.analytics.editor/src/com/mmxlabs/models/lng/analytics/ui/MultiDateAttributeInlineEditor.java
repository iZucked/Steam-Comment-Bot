/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.ListDialog;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.editors.impl.DialogInlineEditor;

public class MultiDateAttributeInlineEditor extends DialogInlineEditor {
	private Function<Object, String> labelProvider;
	private Supplier<List<Object>> valuesSupplier;

	private static final int MAX_DISPLAY_LENGTH = 32;
	private static final int MIN_DISPLAY_NAMES = 2;

	public MultiDateAttributeInlineEditor(final EStructuralFeature feature, Function<Object, String> labelProvider, Supplier<List<Object>> valuesSupplier) {
		super(feature);
		this.labelProvider = labelProvider;
		this.valuesSupplier = valuesSupplier;
	}

	@Override
	protected Object displayDialog(Object currentValue) {
//		final List<Pair<String, Object>> options = new LinkedList<>();
//		
//				for (Object o : valuesSupplier.get()) {
//					options.add(new Pair<>(labelProvider.apply(o), o));
//				}
//		
//				if ((options.size() > 0) && (options.get(0).getSecond() == null)) {
//					options.remove(0);
//				}
//			final ArrayList<Pair<String, Object>> selectedOptions = new ArrayList<>();
		Object value = currentValue;
		if (value == SetCommand.UNSET_VALUE) {
			return null;
		}
		final Collection<?> sel = (Collection<?>) value;
		final ListDialog dlg = new ListDialog(getShell()) {

			@Override
			protected Control createDialogArea(Composite container) {
				// TODO Auto-generated method stub
				Control c = super.createDialogArea(container);
				
				
				return c;
			}
			
			@Override
			protected void createButtonsForButtonBar(Composite parent) {
				// TODO Auto-generated method stub
				Button addbutton = createButton(parent, 9000, "Add", true);
				addbutton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						((Collection) currentValue).add(LocalDate.now().atStartOfDay());
						 getTableViewer().refresh();
					}
				});

				super.createButtonsForButtonBar(parent);
			}

		};
		dlg.setContentProvider(new ArrayContentProvider());
//						null);(Object) sel.toArray(),
		dlg.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(final Object element) {
				return  element.toString();
			}
		});

		dlg.setInput(currentValue);
		dlg.setTitle("Value Selection");

//				for (final Pair<String, Object> p : options) {
//					if (sel.contains(p.getSecond())) {
//						selectedOptions.add(p);
//					}
//				}

////				dlg.setInitialSelections(selectedOptions.toArray());
//				dlg.addColumn("Name", new ColumnLabelProvider() {
//					@Override
//					public String getText(final Object element) {
//						return ((Pair<String, ?>) element).getFirst();
//					}
//				});

		// dlg.groupBy(new ColumnLabelProvider() {
		// @Override
		// public String getText(final Object element) {
		// return ((Pair<?, EObject>) element).getSecond().eClass().getName();
		// }
		// });

		if (dlg.open() == Window.OK) {
//			final Object[] result = dlg.getResult();
//
//			final ArrayList<Object> resultList = new ArrayList<>();
//			for (final Object o : result) {
//				resultList.add(((Pair<String, Object>) o).getSecond());
//			}

			return currentValue;
		}
		return null;
	}

	@Override
	protected String render(Object value) {
		if (!(value instanceof List)) {
			return "";
		}
		@SuppressWarnings("unchecked")
		final List<?> selectedValues = (List<?>) value;
		final StringBuilder sb = new StringBuilder();
		int numNamesAdded = 0;
		for (final Object obj : selectedValues) {
			String name = labelProvider.apply(obj);
			if (sb.length() > 0) {
				sb.append(", ");
			}
			if (sb.length() + name.length() <= MAX_DISPLAY_LENGTH || numNamesAdded <= MIN_DISPLAY_NAMES - 1) {
				sb.append(name);
				++numNamesAdded;
			} else {
				sb.append("...");
				break;
			}
		}
		return sb.toString();
	}

	@Override
	protected Object getInitialUnsetValue() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	protected String renderValue(final Object value) {
//		if (!(value instanceof List)) {
//			return "";
//		}
//		@SuppressWarnings("unchecked")
//		final List<?> selectedValues = (List<?>) value;
//		final StringBuilder sb = new StringBuilder();
//		int numNamesAdded = 0;
//		for (final Object obj : selectedValues) {
//			String name = labelProvider.apply(obj);
//			if (sb.length() > 0) {
//				sb.append(", ");
//			}
//			if (sb.length() + name.length() <= MAX_DISPLAY_LENGTH || numNamesAdded <= MIN_DISPLAY_NAMES - 1) {
//				sb.append(name);
//				++numNamesAdded;
//			} else {
//				sb.append("...");
//				break;
//			}
//		}
//		return sb.toString();
//	}
//
//	@Override
//	public void doSetValue(final Object object, final Object value) {
//		final Object currentValue = getValue(object);
//		if (Equality.isEqual(currentValue, value)) {
//			return;
//		}
//		commandHandler.handleCommand(CommandUtil.createMultipleAttributeSetter(commandHandler.getEditingDomain(), (EObject) object, field, (Collection<?>) value), (EObject) object, field);
//	}

//	@SuppressWarnings("unchecked")
//	@Override
//	protected Object openDialogBox(final Control cellEditorWindow, final Object object) {
//
//		final List<Pair<String, Object>> options = new LinkedList<>();
//
//		for (Object o : valuesSupplier.get()) {
//			options.add(new Pair<>(labelProvider.apply(o), o));
//		}
//
//		if ((options.size() > 0) && (options.get(0).getSecond() == null)) {
//			options.remove(0);
//		}
//
//		final ListSelectionDialog dlg = new ListSelectionDialog(cellEditorWindow.getShell(), (Object) options.toArray(), new ArrayContentProvider(),
//
//				new LabelProvider() {
//
//					@Override
//					public String getText(final Object element) {
//						return ((Pair<String, ?>) element).getFirst();
//					}
//				});
//		dlg.setTitle("Value Selection");
//
//		final ArrayList<Pair<String, Object>> selectedOptions = new ArrayList<>();
//		Object value = getValue(object);
//		if (value == SetCommand.UNSET_VALUE) {
//			return null;
//		}
//		final Collection<?> sel = (Collection<?>) value;
//		for (final Pair<String, Object> p : options) {
//			if (sel.contains(p.getSecond())) {
//				selectedOptions.add(p);
//			}
//		}
//
//		dlg.setInitialSelections(selectedOptions.toArray());
//		dlg.addColumn("Name", new ColumnLabelProvider() {
//			@Override
//			public String getText(final Object element) {
//				return ((Pair<String, ?>) element).getFirst();
//			}
//		});
//
//		// dlg.groupBy(new ColumnLabelProvider() {
//		// @Override
//		// public String getText(final Object element) {
//		// return ((Pair<?, EObject>) element).getSecond().eClass().getName();
//		// }
//		// });
//
//		if (dlg.open() == Window.OK) {
//			final Object[] result = dlg.getResult();
//
//			final ArrayList<Object> resultList = new ArrayList<>();
//			for (final Object o : result) {
//				resultList.add(((Pair<String, Object>) o).getSecond());
//			}
//
//			return resultList;
//		}
//		return null;
//	}
}