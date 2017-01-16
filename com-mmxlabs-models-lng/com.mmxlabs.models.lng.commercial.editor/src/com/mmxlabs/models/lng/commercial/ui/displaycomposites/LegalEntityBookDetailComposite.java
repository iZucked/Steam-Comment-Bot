/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.nebula.widgets.formattedtext.PercentFormatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.datetime.ui.formatters.LocalDateTextFormatter;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;

/**
 * Detail composite for vessel state attributes; adds an additional bit to the bottom of the composite which contains a fuel curve table.
 * 
 * @author hinton
 * 
 */
public class LegalEntityBookDetailComposite extends Composite implements IDisplayComposite {
	private DefaultDetailComposite delegate;
	private ICommandHandler commandHandler;
	private TableViewer tableViewer;

	private static EStructuralFeature editedAttribute = CommercialPackage.Literals.BASE_ENTITY_BOOK__TAX_RATES;
	private static EAttribute[] columnFeatures = { CommercialPackage.Literals.TAX_RATE__DATE, CommercialPackage.Literals.TAX_RATE__VALUE };
	private static EAttribute column1Feature = columnFeatures[0];
	private static EAttribute column2Feature = columnFeatures[1];

	private final ValidationDecorator validationDecorator;

	private EObject target;

	public LegalEntityBookDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style);
		toolkit.adapt(this);
		setLayout(new GridLayout(1, false));

		delegate = new DefaultDetailComposite(this, style, toolkit);
		delegate.getComposite().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		@SuppressWarnings("unused")
		final Label taxCurve = toolkit.createLabel(this, "Tax Rate", SWT.NONE);

		final TableViewer tableViewer = new TableViewer(this, SWT.FULL_SELECTION);
		final Table table = tableViewer.getTable();

		// set the validation decorator for the tax rate field
		validationDecorator = new ValidationDecorator(table, SWT.LEFT | SWT.TOP) {

			@Override
			protected EObject getFeature() {
				return editedAttribute;
			}

			@Override
			protected EObject getTarget() {
				return target;
			}

		};

		final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.heightHint = 100;
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayout(new TableLayout());

		final TableViewerColumn dateColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		final TableViewerColumn rateColumn = new TableViewerColumn(tableViewer, SWT.NONE);

		dateColumn.getColumn().setText("Date");
		rateColumn.getColumn().setText("Tax Rate");

		table.addListener(SWT.Resize, new Listener() {
			boolean resizing = false;

			@Override
			public void handleEvent(final Event event) {
				if (resizing)
					return;
				resizing = true;
				dateColumn.getColumn().pack();
				rateColumn.getColumn().pack();
				resizing = false;
			}
		});

		dateColumn.setEditingSupport(new EditingSupport(tableViewer) {
			final EAttribute attr = column1Feature;

			@Override
			protected void setValue(final Object element, final Object value) {
				final EditingDomain ed = commandHandler.getEditingDomain();
				commandHandler.handleCommand(SetCommand.create(ed, element, attr, value), (EObject) element, attr);
			}

			@Override
			protected Object getValue(final Object element) {
				return ((EObject) element).eGet(attr);
			}

			@Override
			protected CellEditor getCellEditor(final Object element) {
				final FormattedTextCellEditor ed = new FormattedTextCellEditor(table);
				ed.setFormatter(new LocalDateTextFormatter());
				return ed;
			}

			@Override
			protected boolean canEdit(final Object element) {
				return true;
			}
		});

		dateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				if (element != null) {
					final LocalDate date = ((TaxRate) element).getDate();
					if (date != null) {
						final LocalDateTextFormatter formatter = new LocalDateTextFormatter();
						formatter.setValue(date);
						return formatter.getDisplayString();
					}
				}
				return "";
			}
		});

		rateColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(final Object element) {
				return element == null ? "" : String.format("%.2f%%", ((TaxRate) element).getValue() * 100);
			}
		});

		rateColumn.setEditingSupport(new EditingSupport(tableViewer) {
			final EAttribute attr = column2Feature;

			@Override
			protected void setValue(final Object element, Object value) {
				// PercentFormatter returns a Double, we need a Float
				value = new Float(((Double) value).floatValue());

				final EditingDomain ed = commandHandler.getEditingDomain();
				commandHandler.handleCommand(SetCommand.create(ed, element, attr, value)

				, (EObject) element, attr);
			}

			@Override
			protected Object getValue(final Object element) {
				return ((EObject) element).eGet(attr);
			}

			@Override
			protected CellEditor getCellEditor(final Object element) {
				final FormattedTextCellEditor ed = new FormattedTextCellEditor(table);
				ed.setFormatter(new PercentFormatter("#0.00"));
				return ed;
			}

			@Override
			protected boolean canEdit(final Object element) {
				return true;
			}
		});

		LegalEntityBookDetailComposite.this.tableViewer = tableViewer;
		tableViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(final Object inputElement) {
				final TaxRate[] things = ((BaseEntityBook) inputElement).getTaxRates().toArray(new TaxRate[0]);
				Arrays.sort(things, new Comparator<TaxRate>() {

					@Override
					public int compare(final TaxRate arg0, final TaxRate arg1) {
						final LocalDate date0 = ((TaxRate) arg0).getDate();
						if (date0 == null)
							return -1;
						final LocalDate date1 = ((TaxRate) arg1).getDate();
						if (date1 == null)
							return 1;
						return date0.compareTo(date1);
					}
				});

				return things;
			}
		});

		final Composite buttons = toolkit.createComposite(this);

		buttons.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		final GridLayout buttonLayout = new GridLayout(2, true);
		buttons.setLayout(buttonLayout);
		buttonLayout.marginHeight = 0;
		buttonLayout.marginWidth = 0;
		final Button remove = toolkit.createButton(buttons, "-", SWT.NONE);
		remove.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		remove.setEnabled(false);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				remove.setEnabled(event.getSelection().isEmpty() == false);
			}
		});

		remove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final ISelection sel = tableViewer.getSelection();
				if (sel.isEmpty())
					return;
				if (sel instanceof IStructuredSelection) {
					final TaxRate taxRate = (TaxRate) ((IStructuredSelection) sel).getFirstElement();
					commandHandler.handleCommand(RemoveCommand.create(commandHandler.getEditingDomain(), taxRate.eContainer(), taxRate.eContainingFeature(), taxRate), oldValue, editedAttribute);
					tableViewer.refresh();
				}
			}
		});
		final Button add = toolkit.createButton(buttons, "+", SWT.NONE);
		add.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final ISelection sel = tableViewer.getSelection();

				TaxRate selection;
				if (!sel.isEmpty() && sel instanceof IStructuredSelection) {
					selection = (TaxRate) ((IStructuredSelection) sel).getFirstElement();
				} else {
					selection = null;
					for (final TaxRate c : oldValue.getTaxRates()) {
						if (selection == null || selection.getDate().compareTo(c.getDate()) < 0)
							selection = c;
					}
				}
				final TaxRate newTaxRate = CommercialFactory.eINSTANCE.createTaxRate();
				newTaxRate.setValue(0);
				newTaxRate.setDate(LocalDate.now());
				commandHandler.handleCommand(AddCommand.create(commandHandler.getEditingDomain(), oldValue, editedAttribute, newTaxRate), oldValue, editedAttribute);
				tableViewer.setSelection(new StructuredSelection(newTaxRate));
			}
		});
	}

	@Override
	public Composite getComposite() {
		return this;
	}

	BaseEntityBook oldValue = null;
	final Adapter adapter = new MMXContentAdapter() {

		@Override
		public void reallyNotifyChanged(final Notification notification) {
			if (!isDisposed() && isVisible()) {
				if (tableViewer != null && tableViewer.getTable().isDisposed() == false)
					tableViewer.refresh();
			} else {
				LegalEntityBookDetailComposite.this.removeAdapter();
			}
		}

	};

	void removeAdapter() {
		if (oldValue != null) {
			oldValue.eAdapters().remove(adapter);
			oldValue = null;
		}
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject value, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		target = value;
		delegate.display(dialogContext, root, value, range, dbc);
		tableViewer.setInput(value);
		removeAdapter();
		oldValue = (BaseEntityBook) value;
		value.eAdapters().add(adapter);
	}

	@Override
	public void dispose() {
		removeAdapter();
		super.dispose();
	}

	@Override
	public void setCommandHandler(final ICommandHandler commandHandler) {
		delegate.setCommandHandler(commandHandler);
		this.commandHandler = commandHandler;
	}

	@Override
	public void displayValidationStatus(final IStatus status) {
		// display validation for the tax rate field
		validationDecorator.processValidation(status);
		// display default validation information otherwise
		delegate.displayValidationStatus(status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editors.IDisplayComposite#setEditorWrapper(com.mmxlabs.models.ui.editors.IInlineEditorWrapper)
	 */
	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {
		delegate.setEditorWrapper(wrapper);
	}

	@Override
	public boolean checkVisibility(final IDialogEditingContext context) {
		return delegate.checkVisibility(context);
	}

}
