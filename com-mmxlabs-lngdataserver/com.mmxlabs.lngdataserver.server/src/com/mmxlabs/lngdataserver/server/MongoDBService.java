package com.mmxlabs.lngdataserver.server;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.extract.ITempNaming;
import de.flapdoodle.embed.process.extract.UUIDTempNaming;
import de.flapdoodle.embed.process.io.directories.FixedPath;
import de.flapdoodle.embed.process.io.directories.IDirectory;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * @author Robert Erdin
 */
public class MongoDBService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBService.class);

	// @Value("${db.embedded:true}")
	// private boolean embedded;
	// @Value("${db.embeddedBinaries:#{null}}")
	private String embeddedBinariesLocation;
	// @Value("${db.host:localhost}")
	// private String host;
	// @Value("${db.port:27017}")
	// private int providedPort; // this is only a hint in the embedded setup
	// @Value("${db.user:#{null}}")
	// private String dbUser;
	// @Value("${db.pw:#{null}}")
	// private String dbPw;

	// // only needed for embedded mode
	// @Value("${db.embeddedDataLocation:mongo_data}")
	private String embeddedDataLocation;
	//
	private static MongodStarter MONGOD_STARTER;
	private MongodExecutable mongodExecutable;
	private MongodProcess mongodProcess;
	// private int randomPort;

	public int start() throws IOException, ClassNotFoundException {
		if (MONGOD_STARTER == null) {
			LOGGER.debug("Starting embedded Mongo DB");// " + getPort());
			if (embeddedBinariesLocation != null) {
				IDirectory artifactStorePath = new FixedPath(embeddedBinariesLocation);
				ITempNaming executableNaming = new UUIDTempNaming();

				Command command = Command.MongoD;

				IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()
						.defaults(command)
						.artifactStore(new ExtractedArtifactStoreBuilder()
								.defaults(command)
						.download(new DownloadConfigBuilder()
								.defaultsForCommand(command)
								.artifactStorePath(artifactStorePath)
								.build())
						.executableNaming(executableNaming))
						.build();

				MONGOD_STARTER = MongodStarter.getInstance(runtimeConfig);
			} else {
				MONGOD_STARTER = MongodStarter.getDefaultInstance();
			}

		}

		File file = new File(embeddedDataLocation);
		file.mkdirs();
		// Net n = new Net(bindIp, port, ipv6)
		mongodExecutable = MONGOD_STARTER.prepare(new MongodConfigBuilder().version(Version.Main.PRODUCTION).replication(new Storage(embeddedDataLocation, null, 0))
				// .net(new Net(host, getPort(), false))
				// .net(new Net(host, getPort(), false))
				.net(new Net("127.0.0.1", Network.getFreeServerPort(), false))
				// .setParameter("bind_ip","127.0.0.1")

				.cmdOptions(new MongoCmdOptionsBuilder().syncDelay(10).build()).build());

		mongodProcess = mongodExecutable.start();

		int port = mongodProcess.getConfig().net().getPort();
		System.out.println(port);
		int ii = 0;
		LOGGER.debug("Started embedded Mongo DB on port " + port);// " + getPort());

		return port;
	}
	//
	// public int getPort() throws IOException {
	// if (randomPort == 0 && portAvailable(providedPort)) {
	// // try to set the random port to the one provided by the user
	// // this is slightly dangerous since it could be taken in the meantime
	// randomPort = providedPort;
	// return randomPort;
	// }
	// if (randomPort == 0) {
	// try {
	// randomPort = Network.getFreeServerPort();
	// } catch (IOException e) {
	// LOGGER.warn("Error getting free port for embedded mongodb", e);
	// throw e;
	// }
	// }
	// return randomPort;
	// }

	// private static boolean portAvailable(int port) {
	// LOGGER.info("--------------Testing port " + port);
	// Socket s = null;
	// try {
	// s = new Socket("localhost", port);
	//
	// // If the code makes it this far without an exception it means
	// // something is using the port and has responded.
	// LOGGER.info("--------------Port " + port + " is not available");
	// return false;
	// } catch (IOException e) {
	// LOGGER.info("--------------Port " + port + " is available");
	// return true;
	// } finally {
	// if (s != null) {
	// try {
	// s.close();
	// } catch (IOException e) {
	// throw new RuntimeException("Error closing probing socket", e);
	// }
	// }
	// }
	// }

	public void stop() {
		LOGGER.info("shutting down embedded database...");
		mongodProcess.stop();
		mongodExecutable.stop();
		LOGGER.info("... embedded mongodb gracefully shut down");
	}

	public String getEmbeddedBinariesLocation() {
		return embeddedBinariesLocation;
	}

	public void setEmbeddedBinariesLocation(String embeddedBinariesLocation) {
		this.embeddedBinariesLocation = embeddedBinariesLocation;
	}

	public String getEmbeddedDataLocation() {
		return embeddedDataLocation;
	}

	public void setEmbeddedDataLocation(String embeddedDataLocation) {
		this.embeddedDataLocation = embeddedDataLocation;
	}
}
