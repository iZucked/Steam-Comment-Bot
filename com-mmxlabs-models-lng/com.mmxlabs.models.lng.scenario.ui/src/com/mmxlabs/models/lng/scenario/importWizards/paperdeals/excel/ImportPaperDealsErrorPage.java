package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.excel;

import java.util.List;
import java.util.Optional;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.excel.util.ExcelImportResultDescriptor;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.excel.util.ExcelImportResultDescriptor.MessageContext;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.excel.util.ExcelImportResultDescriptor.MessageType;

public class ImportPaperDealsErrorPage extends WizardPage {
	
	private TableViewer viewer;


	public ImportPaperDealsErrorPage(String pageName) {
		super(pageName);
		setTitle("Import Errors");
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			List<ExcelImportResultDescriptor> messages = ((ImportPaperDealsFromExcelWizard) getWizard()).getMessages();
			Optional<ExcelImportResultDescriptor> message = messages.stream().filter(m -> m.getType().equals(MessageType.INFO)).findFirst();
			
			if(message.isPresent())
				setDescription(message.get().getMessage());
			
			viewer.setInput(messages);
			viewer.refresh();
			int errors = viewer.getTable().getItemCount();
			setMessage(errors == 0 ? null : errors + " problems during import");
			// Pack columns, but skip filename as this can be very long
			for (int i = 1; i < viewer.getTable().getColumnCount(); ++i) {
				viewer.getTable().getColumn(i).pack();
			}
		}
	}
	
	private TableViewerColumn addViewerColumn(final String name, final ColumnLabelProvider lp) {
		final TableViewerColumn c = new TableViewerColumn(viewer, SWT.NONE);

		c.getColumn().setResizable(true);
		c.getColumn().setText(name);
		c.getColumn().setMoveable(true);
		c.getColumn().pack();
		c.setLabelProvider(lp);

		return c;
	}
	
	@Override
	public void createControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		// set up table, and load inputs
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);

		addViewerColumn("Context", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ExcelImportResultDescriptor message  = getElement(element);
				
				if(message == null)
					return "";
								
				if(message.getContext().equals(MessageContext.PAPER_DEAL)){
					return "" + MessageContext.PAPER_DEAL + " : " + message.getPaperDealName();
				}
				
				return message.getContext().toString();
			}
		});

		addViewerColumn("Row", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ExcelImportResultDescriptor message  = getElement(element);
				
				if(message == null)
					return "";
				
				return message.getRowNumber() == -1 ? "" : "" + ((ExcelImportResultDescriptor) element).getRowNumber();
			}
		});

		addViewerColumn("Column", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ExcelImportResultDescriptor message  = getElement(element);
				
				if(message == null)
					return "";
								
				return message.getColumnNumber() == -1 ? "" : "" + ((ExcelImportResultDescriptor) element).getColumnNumber();
			}
		});

		addViewerColumn("Problem", new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ExcelImportResultDescriptor message  = getElement(element);
				
				if(message == null)
					return "";
								
				return message.getMessage();
			}
		});

		viewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public Object[] getElements(Object inputElement) {
				return (((List<ExcelImportResultDescriptor>) inputElement).stream().filter(message -> message.getType().equals(MessageType.ERROR))).toArray();
			}
		});

		setControl(viewer.getControl());
	}
	
	private ExcelImportResultDescriptor getElement(final Object element) {
		if(element != null && element instanceof ExcelImportResultDescriptor realElement )
			return realElement;
		return null;
	}
}
