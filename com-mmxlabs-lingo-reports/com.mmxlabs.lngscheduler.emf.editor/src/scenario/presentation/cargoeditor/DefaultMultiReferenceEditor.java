package scenario.presentation.cargoeditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.ListSelectionDialog;

public class DefaultMultiReferenceEditor implements IFeatureEditor {
	private final IReferenceValueProvider valueProvider;
	private final EAttribute nameAttribute;
	private final EditingDomain editingDomain;

	public DefaultMultiReferenceEditor(
			final IReferenceValueProvider valueProvider,
			final EAttribute nameAttribute, final EditingDomain editingDomain) {
		this.valueProvider = valueProvider;
		this.nameAttribute = nameAttribute;
		this.editingDomain = editingDomain;
	}

	@Override
	public IFeatureManipulator getFeatureManipulator(
			final List<EReference> path, final EStructuralFeature field) {
		return new BaseFeatureManipulator(path, field, editingDomain) {
			private DialogCellEditor cellEditor = null;
			private final List<EObject> options = new ArrayList<EObject>();

			@Override
			public void setFromEditorValue(final EObject o, final Object value) {
				final EObject target = getTarget(o);
				final CompoundCommand cc = new CompoundCommand(
						CompoundCommand.LAST_COMMAND_ALL);
				
				// TODO: Check equals...
				
				cc.append(editingDomain.createCommand(RemoveCommand.class,
						new CommandParameter(target, field,
								(Collection<?>) target.eGet(field))));
				cc.append(editingDomain.createCommand(AddCommand.class,
						new CommandParameter(target, field, (Collection<?>)value)));
				editingDomain.getCommandStack().execute(cc);
			}

			@Override
			public String getStringValue(final EObject o) {
				EList<? extends EObject> selectedValues = (EList<? extends EObject>) getTarget(
						o).eGet(field);
				final StringBuilder sb = new StringBuilder();
				for (final EObject obj : selectedValues) {
					if (sb.length() > 0)
						sb.append(", ");
					sb.append(obj.eGet(nameAttribute).toString());
				}
				return sb.toString();
			}

			@Override
			public Object getEditorValue(final EObject row) {
				final HashSet<EObject> set = new HashSet<EObject>();
				final EList<EObject> elements = (EList<EObject>) getTarget(row)
						.eGet(field);
				for (final EObject element : elements)
					set.add(element);
				return set;
			}

			@Override
			public CellEditor createCellEditor(final Composite parent) {
				assert cellEditor == null;

				cellEditor = new DialogCellEditor(parent) {
					@Override
					protected Object openDialogBox(final Control box) {
//						final PickerDialog picker = new PickerDialog(
//								box.getShell(), nameAttribute);
//						
//						picker.setSelectableItems(options);
//						picker.setSelectedItems((Collection<EObject>) getValue());
//						Object newItems = picker.open();
//						return newItems;
						ListSelectionDialog dlg = new ListSelectionDialog(box.getShell(), options.toArray(), new ArrayContentProvider(), new LabelProvider() {

							@Override
							public String getText(Object element) {
								return ((EObject) element).eGet(nameAttribute).toString();
								
							}}, "Select values:");
						dlg.setTitle("Value Selection");
						dlg.setInitialSelections(((Collection<?>)getValue()).toArray());
						dlg.setBlockOnOpen(true);
						dlg.open();
						Object[] result = dlg.getResult();
						if (result == null) return null;
						else return Arrays.asList(result);
					}

					@Override
					protected boolean dependsOnExternalFocusListener() {
						return false;
					}
				};

				return cellEditor;
			}

			@Override
			public boolean canModify(final EObject row) {
				options.clear();

				for (final EObject value : valueProvider.getAllowedValues(
						getTarget(row), field)) {
					options.add(value);
				}

				return true;
			}
		};
	}

//	private class PickerDialog extends Dialog {
//		Set<EObject> result = new HashSet<EObject>();
//		final List<EObject> dialogOptions = new ArrayList<EObject>();
//		final EAttribute nameAttribute;
//
//		public PickerDialog(final Shell parent, final EAttribute nameAttribute) {
//			super(parent);
//			this.nameAttribute = nameAttribute;
//			
//		}
//
//		public Object open() {
//			final Shell parent = getParentShell();
//			final Shell shell = new Shell(parent, SWT.DIALOG_TRIM
//					| SWT.APPLICATION_MODAL);
//			
//
//			shell.setLayout(new FillLayout());
//
//			final Composite top = new Composite(shell, SWT.NONE);
//
//			top.setLayout(new GridLayout(2, true));
//
//			GridData gd = new GridData(SWT.LEFT, SWT.TOP, true, true, 2, 1);
//
//			final Table checkTable = new Table(top, SWT.MULTI | SWT.CHECK);
//			checkTable.setLayoutData(gd);
//			for (final EObject option : dialogOptions) {
//				final TableItem item = new TableItem(checkTable, SWT.NONE);
//				item.setText(option.eGet(nameAttribute).toString());
//				item.setData(option);
//				item.setChecked(result.contains(option));
//			}
//
//			checkTable.pack();
//
//			gd = new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 1, 1);
//
//			final Button cancelButton = new Button(top, SWT.NONE);
//			cancelButton.setLayoutData(gd);
//			final Button okButton = new Button(top, SWT.NONE);
//			okButton.setLayoutData(gd);
//			cancelButton.setText("Cancel");
//			okButton.setText("OK");
//
//			final Listener buttonListener = new Listener() {
//				@Override
//				public void handleEvent(final Event event) {
//					if (event.widget == okButton) {
//						result.clear();
//						for (final TableItem item : checkTable.getItems()) {
//							if (item.getChecked()) {
//								result.add((EObject) item.getData());
//							}
//						}
//					} else {
//						result = null;
//					}
//					shell.close();
//				}
//			};
//
//			okButton.addListener(SWT.Selection, buttonListener);
//			cancelButton.addListener(SWT.Selection, buttonListener);
//			shell.setSize(640, 480);
////			shell.pack();
//			shell.open();
//
//			// TODO some kind of layout
//			Display display = parent.getDisplay();
//			while (!shell.isDisposed()) {
//				if (!display.readAndDispatch())
//					display.sleep();
//			}
//
//			return result;
//		}
//		
//		
//
//		public void setSelectedItems(final Collection<EObject> items) {
//			result.clear();
//			result.addAll(items);
//		}
//
//		public void setSelectableItems(final List<EObject> dialogOptions) {
//			this.dialogOptions.clear();
//			this.dialogOptions.addAll(dialogOptions);
//		}
//	}
}
