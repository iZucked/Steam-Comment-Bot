/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Simon Goodall
 * @noinstantiate This class is not intended to be instantiated by clients.
 */

public class EcoreHelper {

	/**
	 * Method to extract the package namespace URI from an XMI based ecore model
	 * 
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static String getPackageNS(final URI uri) throws Exception {

		// Use a single element array to store the result from parsing
		final String[] value = new String[1];

		// Use an URIConverter to get an InputStream from a URI
		final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();
		InputStream inputStream = null;
		try {
			inputStream = uc.createInputStream(uri, Collections.emptyMap());

			// TODO: There may well be a better way to do this. (I hope so...)

			// Use an XML parser to parse document
			final SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

			// Use handler to intercept the parsing an extract the NS URI attribute from document root.
			final DefaultHandler handler = new DefaultHandler() {

				private boolean firstElement = true;

				@Override
				public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {

					if (firstElement) {
						firstElement = false;

						if (qName.contains(":")) {
							final String[] split = qName.split(":");
							final String value2 = attributes.getValue("xmlns:" + split[0]);
							if (value2 != null) {
								value[0] = value2;
								// TODO: Break out of parsing early?
							}
						}
					}

					if (qName.equals("ecore:EPackage")) {
						// Store the result
						value[0] = attributes.getValue("nsURI");
						// TODO: Break out of parsing early?
					}

					super.startElement(uri, localName, qName, attributes);
				}

			};

			// Parse the document!
			parser.parse(inputStream, handler);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (final IOException e) {

				}
			}
		}

		return value[0];
	}
}
