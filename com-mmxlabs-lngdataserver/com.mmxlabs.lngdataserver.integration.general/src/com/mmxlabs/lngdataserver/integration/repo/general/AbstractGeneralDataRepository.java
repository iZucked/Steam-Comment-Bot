/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.repo.general;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.common.util.CheckedFunction;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.IDataRepository;
import com.mmxlabs.scenario.service.model.util.encryption.EncryptionUtils;

import okhttp3.MediaType;

public abstract class AbstractGeneralDataRepository<T> implements IDataRepository<T> {

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	private final @NonNull TypeRecord type;

	protected boolean listenForNewLocalVersions;
	protected final Collection<Runnable> newLocalVersionCallbacks = new ConcurrentLinkedQueue<>();

	protected AbstractGeneralDataRepository(final @NonNull TypeRecord type) {
		this.type = type;
	}

	// Note: ConcurrentModificationException seen here
	private final GeneralDataRepository.IUpdateListener updateListener = () -> newLocalVersionCallbacks.forEach(Runnable::run);

	protected void startListenForNewLocalVersions() {
		GeneralDataRepository.INSTANCE.registerType(type);

		listenForNewLocalVersions = true;
		if (!canWaitForNewLocalVersion()) {
			return;
		}
		GeneralDataRepository.INSTANCE.addListener(type, updateListener);

	}

	public void stopListeningForNewLocalVersions() {
		GeneralDataRepository.INSTANCE.removeListener(type, updateListener);
		listenForNewLocalVersions = false;
	}

	@Override
	public void registerLocalVersionListener(final Runnable versionConsumer) {
		newLocalVersionCallbacks.add(versionConsumer);
	}
	
	@Override
	public void deregisterLocalVersionListener(final Runnable versionConsumer) {
		newLocalVersionCallbacks.remove(versionConsumer);
	}

	protected boolean canWaitForNewLocalVersion() {
		return true;
	}

	protected boolean uploadData(final String json) throws Exception {
		try {
			// Upload it
			GeneralDataRepository.INSTANCE.uploadData(type, json, null);
			return true;
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean hasLocalVersion(final String version) {
		if (version == null || version.isEmpty()) {
			return false;
		}

		final List<DataVersion> versions = getLocalVersions();

		if (versions != null) {
			for (final DataVersion v : versions) {
				if (version.equals(v.getIdentifier())) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public List<DataVersion> getLocalVersions() {

		final ImmutableList<GeneralDataRecord> records = GeneralDataRepository.INSTANCE.getRecords(type);
		return records.stream() //
				.filter(v -> v.getUuid() != null) //
				.filter(v -> v.getCreationDate() != null) //
				.sorted((v1, v2) -> v2.getCreationDate().compareTo(v1.getCreationDate())) //
				.map(v -> new DataVersion(v.getUuid(), v.getCreationDate(), true, false)) //
				.collect(Collectors.toList());
	}

	public abstract T getLocalVersion(final String versionTag) throws Exception;

	protected @Nullable T doGetLocalVersion(final String versionTag, final CheckedFunction<InputStream, T, Exception> readAction) throws Exception {

		final File f = GeneralDataRepository.INSTANCE.getDataFile(type.getType(), versionTag);
		if (f != null) {
			try (FileInputStream fis = new FileInputStream(f)) {
				return EncryptionUtils.decrypt(fis, readAction::apply);
			}
		}
		return null;
	}

	@Override
	public boolean hasUpstreamVersion(final String uuid) {
		return getLocalVersions().stream() //
				.filter(v -> Objects.equals(uuid, v.getIdentifier())) //
				.findFirst().isPresent();
	}

	@Override
	public @Nullable String getCurrentVersion() {
		return GeneralDataRepository.INSTANCE.getCurrentVersion(type);
	}
}
