package com.mmxlabs.models.lng.scenario.actions.anonymisation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.mmxlabs.common.csv.CSVWriter;
import com.mmxlabs.common.csv.FileCSVReader;
import com.mmxlabs.scenario.service.model.util.encryption.EncryptionUtils;

public class AnonymisationMapIO {

	private static final String KEY_OLD_NAME = "oldname";
	private static final String KEY_NEW_NAME = "newname";
	private static final String KEY_AR_TYPE = "type";
	
	private static final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();		
	public static final File anonyMapFile = new File(workspaceLocation.toOSString() + IPath.SEPARATOR + "anonyMap.data");

	public static @NonNull ObjectMapper createObjectMapper() {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jdk8Module());
		return objectMapper;
	}

	public static String write(final List<AnonymisationRecord> records, final File file) {
		final ObjectMapper objectMapper = createObjectMapper();
		String message = "Anonymisation map import successful";
		try {
			FileOutputStream fos = new FileOutputStream(file);
			EncryptionUtils.encrypt(fos, os -> objectMapper.writeValue(os, records));
		} catch (final Exception e) {
			message = "Anonymisation map write error:\n" + e.getMessage();
		}
		return message;
	}
	
	public static List<AnonymisationRecord> read(final File file) {

		try (InputStream inputStream = new FileInputStream(file)){
			return EncryptionUtils.decrypt(inputStream, AnonymisationMapIO::readRecords);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<AnonymisationRecord>();
		}
	}
	
	private static List<AnonymisationRecord> readRecords(final InputStream is) throws IOException {
		final ObjectMapper mapper = createObjectMapper();
		return mapper.readValue(is, new TypeReference<List<AnonymisationRecord>>() {});
	}

	public static List<AnonymisationRecord> readCSV(final String fileName) throws Exception{
		final List<AnonymisationRecord> records = new ArrayList<>();
		try(FileCSVReader reader = new FileCSVReader(new File(fileName))){
			Map<String, String> row;
			while ((row = reader.readRow(true)) != null) {
				final String oldName = row.get(KEY_OLD_NAME);
				final String newName = row.get(KEY_NEW_NAME);
				final String type = row.get(KEY_AR_TYPE);
				records.add(new AnonymisationRecord(oldName, newName, type));
			}
		}

		return records;
	}

	public static String writeCSV(final List<AnonymisationRecord> records, final String fileName) {
		String message = "Anonymisation map export successful";
		try (final FileOutputStream outputStream = new FileOutputStream(fileName)) {
			try(final Writer writer = new OutputStreamWriter(outputStream)){
				final CSVWriter cw = new CSVWriter(writer, ',');

				for (final Field f : AnonymisationRecord.class.getFields()) {
					cw.addValue(f.getName());
				}
				cw.endRow();

				for (final AnonymisationRecord record : records) {
					for (final Field f : AnonymisationRecord.class.getFields()) {
						final Object value = f.get(record);
						String strValue = "";
						if (value != null)
							strValue = value.toString();
						cw.addValue(strValue);
					}
					cw.endRow();
				}
			}
		} catch (Exception e) {
			message = "Anonymisation map write error:\n" + e.getMessage();
		}
		return message;
	}
	
	public static String getFile(final Shell shell) {

        FileDialog dialog = new FileDialog(shell, SWT.SAVE);
        String [] filterNames = new String [] {"CSV files", "All Files (*)"};
    	String [] filterExtensions = new String [] {"*.csv", "*"};
        dialog.setFilterNames(filterNames);
        dialog.setFilterExtensions(filterExtensions);
        String file = dialog.open();
        if (file != null) {
            file = file.trim();
            if (file.length() > 0) {
				return file;
			}
        }
        return "";
    }
}
