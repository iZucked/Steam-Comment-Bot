/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;

import com.google.common.io.ByteStreams;
import com.mmxlabs.scenario.service.model.util.encryption.impl.CachingSharedCipherProvider;

/**
 * Small util class decrypt and extract the contents of a .lingo file. Remember to ensure the keyfile is correctly linked up. E.g. by adding the following system property;
 * 
 * <pre>
 * -Dlingo.enc.keyfile=${system_property:user.home}/mmxlabs/keyfiles/<CLIENT_CODE>/lingo.pks
 * </pre>
 */
public class LiNGODecrypter {

	public static void main(final String args[]) throws Exception {

		if (args.length == 0) {
			System.err.println("No .lingo file specified");
		}

		final File lingoFile = new File(args[0]);

		// Collect file names of entries in the zip file
		final List<String> names = new LinkedList<>();
		try (FileInputStream fis = new FileInputStream(lingoFile)) {
			try (ZipInputStream zipIn = new ZipInputStream(fis)) {
				ZipEntry zipInEntry = zipIn.getNextEntry();
				while (zipInEntry != null) {
					// We could get the input stream and pass to the decryptor here, but would need to be careful to not close the underlying zip input stream.
					names.add(zipInEntry.getName());
					zipIn.closeEntry();
					zipInEntry = zipIn.getNextEntry();
				}
			}
		}

		final ExtensibleURIConverterImpl convertor = new ExtensibleURIConverterImpl();

		final Map<Object, Object> options = new HashMap<>();
		final URIConverter.Cipher cipher = new CachingSharedCipherProvider().getSharedCipher();
		options.put(Resource.OPTION_CIPHER, cipher);

		for (final String name : names) {
			final URI entryURI = URI.createURI("archive:" + URI.createFileURI(lingoFile.getAbsolutePath()) + "!/" + name);

			// Copy root object
			try (InputStream inputStream = convertor.createInputStream(entryURI, options)) {
				try (FileOutputStream fos = new FileOutputStream(args[0] + "." + name)) {
					ByteStreams.copy(cipher.decrypt(inputStream), fos);
				}
			}

		}
	}
}
