package com.mmxlabs.lngdataserver.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.UUID;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.DownloadConfigBuilder;
import de.flapdoodle.embed.mongo.config.ExtractedArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.MongoCmdOptionsBuilder;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.runtime.Mongod;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.extract.ITempNaming;
import de.flapdoodle.embed.process.io.directories.FixedPath;
import de.flapdoodle.embed.process.io.directories.IDirectory;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * @author Robert Erdin
 */
public class MongoDBService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBService.class);

	private String embeddedBinariesLocation;

	private String embeddedDataLocation;
	//
	private static MongodStarter MONGOD_STARTER;
	private MongodExecutable mongodExecutable;
	private MongodProcess mongodProcess;

	public int start() throws IOException, ClassNotFoundException {

		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		final IPath mongoPath = workspaceLocation.append("mongo");
		mongoPath.toFile().mkdirs();

		final String mongoPortFile = mongoPath.append("mongodb.port").toOSString();
		try {
			final int port = readInt(mongoPortFile);
			if (port > 0) {
				if (false) {
					// Attempt to reuse the existing connection. Note, this means we have no control over the process
					final MongoClient mongoClient = new MongoClient("127.0.0.1", port);
					mongoClient.close();
					LOGGER.info("Connected to existing MongoDB on port " + port);
					return port;
				} else {
					// Terminate existing server
					Mongod.sendShutdown(InetAddress.getByName("127.0.0.1"), port);
					// Sleep a bit to let it shutdown
					Thread.sleep(500);
				}
			}
		} catch (final Exception e) {
			// Ignore
		}

		if (MONGOD_STARTER == null) {
			LOGGER.debug("Starting embedded Mongo DB");// " + getPort());
			if (embeddedBinariesLocation != null) {
				final IDirectory artifactStorePath = new FixedPath(embeddedBinariesLocation);
				final ITempNaming executableNaming = new ITempNaming() {
					@Override
					public String nameFor(final String arg0, final String arg1) {
						return mongoPath.append(String.format("%s-%s-%s", arg0, UUID.randomUUID().toString(), arg1)).toOSString();
					}
				};

				final Command command = Command.MongoD;

				final IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()//
						.defaults(command)

						.artifactStore(new ExtractedArtifactStoreBuilder()//
								.defaults(command)//
								.download(new DownloadConfigBuilder() //
										.defaultsForCommand(command)//
										.artifactStorePath(artifactStorePath)//
										.build())//
								.executableNaming(executableNaming))
						.daemonProcess(true)

						.build();

				MONGOD_STARTER = MongodStarter.getInstance(runtimeConfig);
			} else {
				MONGOD_STARTER = MongodStarter.getDefaultInstance();
			}

		}

		final File file = new File(embeddedDataLocation);
		file.mkdirs();
		// Net n = new Net(bindIp, port, ipv6)
		mongodExecutable = MONGOD_STARTER.prepare(new MongodConfigBuilder() //
				.version(Version.Main.PRODUCTION) //
				.replication(new Storage(embeddedDataLocation, null, 0)) //
				// .net(new Net(host, getPort(), false))
				.net(new Net("127.0.0.1", Network.getFreeServerPort(), false))
				// .setParameter("bind_ip","127.0.0.1")
				.cmdOptions(new MongoCmdOptionsBuilder().syncDelay(10).build()).build());

		mongodProcess = mongodExecutable.start();
		final int port = mongodProcess.getConfig().net().getPort();
		try (PrintWriter pw = new PrintWriter(mongoPortFile)) {
			pw.println(port);
		}
		LOGGER.debug("Started embedded Mongo DB on port " + port);// " + getPort());

		return port;
	}

	private int readInt(final String file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			final String line = reader.readLine();
			return Integer.parseInt(line);
		} catch (final Exception e) {
			// Ignore
		}

		return 0;
	}

	public void stop() {
		LOGGER.info("shutting down embedded database...");
		mongodProcess.stop();
		mongodExecutable.stop();
		LOGGER.info("... embedded mongodb gracefully shut down");
		MONGOD_STARTER = null;
	}

	public String getEmbeddedBinariesLocation() {
		return embeddedBinariesLocation;
	}

	public void setEmbeddedBinariesLocation(final String embeddedBinariesLocation) {
		this.embeddedBinariesLocation = embeddedBinariesLocation;
	}

	public String getEmbeddedDataLocation() {
		return embeddedDataLocation;
	}

	public void setEmbeddedDataLocation(final String embeddedDataLocation) {
		this.embeddedDataLocation = embeddedDataLocation;
	}
}
