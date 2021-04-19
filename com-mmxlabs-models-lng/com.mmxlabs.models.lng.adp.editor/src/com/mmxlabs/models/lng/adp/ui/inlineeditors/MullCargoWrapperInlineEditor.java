package com.mmxlabs.models.lng.adp.ui.inlineeditors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.MullCargoWrapper;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.MultiReferenceInlineEditor;
import com.mmxlabs.rcp.common.dialogs.ListSelectionDialog;

public class MullCargoWrapperInlineEditor extends MultiReferenceInlineEditor {

	public MullCargoWrapperInlineEditor(EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject context, final EObject input, final Collection<EObject> range) {
		super.display(dialogContext, context, input, range);
		label.setText("Cargoes to Keep");
	}

	@Override
	public void createColumns(final ListSelectionDialog dlg) {
		dlg.addColumn("Load ID", new ColumnLabelProvider() {
			@SuppressWarnings("unchecked")
			@Override
			public String getText(final Object element) {
				return ((LoadSlot) ((Pair<?, EObject>) element).getSecond()).getName();
			}
		});
		dlg.addColumn("Entity", new ColumnLabelProvider() {
			@SuppressWarnings("unchecked")
			@Override
			public String getText(final Object element) {
				final BaseLegalEntity entity = ((LoadSlot) ((Pair<?, EObject>) element).getSecond()).getEntity();
				return entity == null ? "<Not specified>" : entity.getName();
			}
		});
		dlg.addColumn("Load Date", new ColumnLabelProvider() {
			@SuppressWarnings("unchecked")
			@Override
			public String getText(final Object element) {
				final LocalDate loadDate = ((LoadSlot) ((Pair<?, EObject>) element).getSecond()).getWindowStart();
				return loadDate.toString();
			}
		});
		dlg.addColumn("Discharge ID", new ColumnLabelProvider() {
			@SuppressWarnings("unchecked")
			@Override
			public String getText(final Object element) {
				return ((LoadSlot) ((Pair<?, EObject>) element).getSecond()).getCargo().getSlots().get(1).getName();
			}
		});
	}

	@Override
	protected List<EObject> openDialogBox(final Control cellEditorWindow) {
		final List<Pair<String, EObject>> options = valueProvider.getAllowedValues(input, feature);

		if (options.size() > 0 && options.get(0).getSecond() == null)
			options.remove(0);

		final ListSelectionDialog dlg = new ListSelectionDialog(cellEditorWindow.getShell(), options.toArray(), new ArrayContentProvider(), new LabelProvider() {

			@Override
			public String getText(final Object element) {
				return ((Pair<String, ?>) element).getFirst();
			}
		});
		dlg.setTitle("Value Selection");

		final ArrayList<Pair<String, EObject>> selectedOptions = new ArrayList<>();
		final Collection<EObject> sel = (Collection<EObject>) getValue();
		if (sel != null) {
			final Collection<LoadSlot> selectedLoadSlots = sel.stream().map(MullCargoWrapper.class::cast).map(MullCargoWrapper::getLoadSlot).collect(Collectors.toSet());
			for (final Pair<String, EObject> p : options) {
				if (selectedLoadSlots.contains(p.getSecond())) {
					selectedOptions.add(p);
				}
			}
		}

		dlg.setInitialSelections(selectedOptions.toArray());

		createColumns(dlg);

		dlg.groupBy(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return ((Pair<?, EObject>) element).getSecond().eClass().getName();
			}
		});

		if (dlg.open() == Window.OK) {
			final Object[] result = dlg.getResult();

			final ArrayList<EObject> mullCargoWrappers = new ArrayList<>(result.length);
			for (final Object object : result) {
				final MullCargoWrapper mullCargoWrapper = ADPFactory.eINSTANCE.createMullCargoWrapper();
				final LoadSlot loadSlot = ((Pair<String, LoadSlot>) object).getSecond();
				mullCargoWrapper.setLoadSlot(loadSlot);
				mullCargoWrapper.setDischargeSlot((DischargeSlot) loadSlot.getCargo().getSlots().get(1));
				mullCargoWrappers.add(mullCargoWrapper);
			}
			return mullCargoWrappers;
		}

		return null;
	}

//	@Override
//	protected Object getValue() {
//		if (input == null) {
//			return null;
//		}
//		final List<MullCargoWrapper> mullCargoWrappers = (List<MullCargoWrapper>) input.eGet(feature);
//		if (mullCargoWrappers.isEmpty()) {
//			return mullCargoWrappers;
//		}
//		final ArrayList<EObject> selectedLoadSlots = new ArrayList<>(mullCargoWrappers.size());
//		for (final MullCargoWrapper mullCargoWrapper : mullCargoWrappers) {
//			selectedLoadSlots.add(mullCargoWrapper.getLoadSlot());
//		}
//		return selectedLoadSlots;
//	}
}
