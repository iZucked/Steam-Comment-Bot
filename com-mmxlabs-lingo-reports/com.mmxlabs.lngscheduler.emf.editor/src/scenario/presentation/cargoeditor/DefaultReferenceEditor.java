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
	private EClass refType;
	private EAttribute nameAttribute;
	private ComboBoxCellEditor myEditor;
	private final IReferenceValueProvider valueProvider;
	private final EditingDomain editingDomain;

	public interface IReferenceValueProvider {
		public Iterable<? extends EObject> getAllowedValues(
				final EObject target, final EStructuralFeature field);
	}

	public DefaultReferenceEditor(final EClass refType,
			final EAttribute nameAttribute,
			final IReferenceValueProvider valueProvider,
			final EditingDomain editingDomain) {
		this.refType = refType;
		this.nameAttribute = nameAttribute;
		this.valueProvider = valueProvider;
		this.editingDomain = editingDomain;
	}

	@Override
	public IFeatureManipulator getFeatureManipulator(
			final List<EReference> path, final EStructuralFeature field) {

		assert (field.getEType().equals(refType));

		return new IFeatureManipulator() {
			private EObject getTarget(EObject modelObject) {
				if (modelObject == null)
					return null;
				for (final EReference ref : path) {
					modelObject = (EObject) modelObject.eGet(ref);
					if (modelObject == null)
						return null;
				}
				return modelObject;
			}

			@Override
			public String getStringValue(EObject o) {
				o = getTarget(o);
				final EObject entity = (EObject) o.eGet(field);
				if (entity == null)
					return "";
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
				} else {
					System.err.println("Set to " + value);
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
				valueMap.clear();
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

				final String[] stringValues = valueMap.keySet().toArray(
						new String[] {});

				valueList.clear();
				reverseValueList.clear();

				for (final Map.Entry<String, EObject> e : valueMap.entrySet()) {
					reverseValueList.put(e.getValue(), valueList.size());
					valueList.add(e.getValue());
				}

				myEditor.setItems(stringValues);

				return true;
			}

			@Override
			public CellEditor createCellEditor(Composite parent) {
				return myEditor = new ComboBoxCellEditor(parent,
						new String[] {});
			}

			@Override
			public Object getEditorValue(final EObject row) {
				final EObject currentValue = (EObject) getTarget(row).eGet(
						field);

				return reverseValueList.get(currentValue);
			}
		};
	}
}
