package com.mmxlabs.lngdataserver.integration.repo.generic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.mmxlabs.common.util.CheckedBiConsumer;
import com.mmxlabs.common.util.CheckedFunction;
import com.mmxlabs.lngdataserver.commons.DataVersion;
import com.mmxlabs.lngdataserver.commons.IDataRepository;
import com.mmxlabs.lngdataserver.integration.repo.general.GeneralDataRepository;
import com.mmxlabs.scenario.service.model.util.encryption.EncryptionUtils;

import okhttp3.MediaType;

public abstract class AbstractGenericDataRepository<T> implements IDataRepository<T> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractGenericDataRepository.class);

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	private final String type;

	protected boolean listenForNewLocalVersions;
	protected final List<Runnable> newLocalVersionCallbacks = new LinkedList<>();

	public AbstractGenericDataRepository(final String type) {
		this.type = type;
	}

	private GenericDataRepository.IUpdateListener updateListener = () -> newLocalVersionCallbacks.forEach(Runnable::run);

	public void startListenForNewLocalVersions() {
		GenericDataRepository.INSTANCE.registerType(type);

		listenForNewLocalVersions = true;
		if (!canWaitForNewLocalVersion()) {
			return;
		}
		GenericDataRepository.INSTANCE.addListener(type, updateListener);

	}

	public void stopListeningForNewLocalVersions() {
		GenericDataRepository.INSTANCE.removeListener(type, updateListener);
		listenForNewLocalVersions = false;
	}

	public void registerLocalVersionListener(final Runnable versionConsumer) {
		newLocalVersionCallbacks.add(versionConsumer);
	}

	protected boolean canWaitForNewLocalVersion() {
		return true;
	}

	public abstract boolean publishVersion(final T version) throws Exception;

	protected boolean doPublishOutputStream(T version, String uuid, CheckedBiConsumer<T, OutputStream, IOException> writeAction) throws Exception {

		Path p = Files.createTempFile("hub-", ".data");
		try {

			// Write data to temp file
			try (FileOutputStream os = new FileOutputStream(p.toFile())) {
				EncryptionUtils.encrypt(os, eos -> writeAction.accept(version, eos));
			}
			// Upload it
			GenericDataRepository.INSTANCE.importData(type, uuid, "application/octet-stream", p.toFile(), null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			Files.delete(p);
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

	public List<DataVersion> getLocalVersions() {

		ImmutableList<GenericDataRecord> records = GenericDataRepository.INSTANCE.getRecords(type);
		return records.stream() //
				.filter(v -> v.getUuid() != null) //
				.filter(v -> v.getCreationDate() != null) //
				.sorted((v1, v2) -> v2.getCreationDate().compareTo(v1.getCreationDate())) //
				.map(v -> new DataVersion(v.getUuid(), v.getCreationDate(), true, false)) //
				.collect(Collectors.toList());
	}

	public abstract T getLocalVersion(final String versionTag) throws Exception;

	protected T doGetLocalVersion(final String versionTag, CheckedFunction<InputStream, T, Exception> readAction) throws Exception {

		File f = GenericDataRepository.INSTANCE.getDataFile(type, versionTag);
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
}
