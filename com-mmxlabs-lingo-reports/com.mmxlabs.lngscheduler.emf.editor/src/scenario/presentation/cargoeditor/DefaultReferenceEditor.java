/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 */

package scenario.presentation.cargoeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;

public class DefaultReferenceEditor implements IFeatureEditor {
	protected static final String NULL_NAME = "(none)";
	private EClass refType;
	private EAttribute nameAttribute;
	private final IReferenceValueProvider valueProvider;
	private final EditingDomain editingDomain;
	private final boolean allowNullValues;

	public DefaultReferenceEditor(final EClass refType,
			final EAttribute nameAttribute,
			final IReferenceValueProvider valueProvider,
			final EditingDomain editingDomain, final boolean allowNullValues) {
		this.refType = refType;
		this.nameAttribute = nameAttribute;
		this.valueProvider = valueProvider;
		this.editingDomain = editingDomain;
		this.allowNullValues = allowNullValues;
	}

	@Override
	public IFeatureManipulator getFeatureManipulator(
			final List<EReference> path, final EStructuralFeature field) {

		
		assert (field.getEType().equals(refType));

		return new BaseFeatureManipulator(path, field, editingDomain) {
			private ComboBoxCellEditor myEditor = null;

			@Override
			public String getStringValue(EObject o) {
				o = getTarget(o);
				final EObject entity = (EObject) o.eGet(field);
				if (entity == null)
					return NULL_NAME;
				return entity.eGet(nameAttribute).toString();
			}

			@Override
			public void setFromEditorValue(final EObject o, final Object value) {
				if (value instanceof Integer) {
					final int index = (Integer) value;
					if (index >= 0 && index < valueList.size()) {
						final EObject modelValue = valueList
								.get((Integer) value);

						final EObject target = getTarget(o);
						editingDomain.getCommandStack().execute(
								editingDomain.createCommand(SetCommand.class,
										new CommandParameter(target, field,
												modelValue)));
					}
				}
			}

			final List<EObject> valueList = new ArrayList<EObject>();
			final Map<EObject, Integer> reverseValueList = new HashMap<EObject, Integer>();

			@Override
			public boolean canModify(final EObject row) {
				// update cell editor
				final EObject target = getTarget(row);
				final Iterable<? extends EObject> values = valueProvider
						.getAllowedValues(target, field);

				final TreeMap<String, EObject> valueMap = new TreeMap<String, EObject>();

				for (final EObject value : values) {
					String valueName = value.eGet(nameAttribute).toString();
					if (valueMap.containsKey(valueName)) {
						int counter = 1;
						while (valueMap.containsKey(valueName + " (" + counter
								+ ")")) {
							counter++;
						}
						valueName = valueName + " (" + counter + ")";
					}
					valueMap.put(valueName, value);
				}

				valueList.clear();
				reverseValueList.clear();
				final String[] names = new String[valueMap.size()
						+ (allowNullValues ? 1 : 0)];

				int nameIndex = 0;
				if (allowNullValues) {
					valueList.add(null);
					reverseValueList.put(null, 0);
					names[nameIndex++] = NULL_NAME;
				}

				for (final Map.Entry<String, EObject> e : valueMap.entrySet()) {
					reverseValueList.put(e.getValue(), valueList.size());
					valueList.add(e.getValue());
					names[nameIndex++] = e.getKey();
				}

				myEditor.setItems(names);

				return true;
			}

			@Override
			public CellEditor createCellEditor(Composite parent) {
				assert myEditor == null;
				myEditor = new ComboBoxCellEditor(parent,
						new String[] {});
				return myEditor;
			}

			@Override
			public Object getEditorValue(final EObject row) {
				final EObject currentValue = (EObject) getTarget(row).eGet(
						field);
				Integer result = reverseValueList.get(currentValue);
				return result;
			}
		};
	}
}
