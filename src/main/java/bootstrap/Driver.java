package bootstrap;

import communications.protocol.KafkaConfig;
import traffic.TrafficSimulator;
import util.LoggerUtil;
import util.TrackedLogger;
import util.UUIdSingleton;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Driver {
    public static final String SEPARATOR =
            "==============================================================";

    public static Properties    projectProperties = new Properties();
    public static TrackedLogger logger            = new TrackedLogger(Driver.class);

    public static void main(String[] args) {
        try {
            Boolean debugFlag = Boolean.parseBoolean(args[0]);
            LoggerUtil.configureConsoleLogging(debugFlag, UUIdSingleton.getInstance().uuid);
            logger.info(SEPARATOR);
            logger.info("Welcome to the Traffic Simulator.");
            projectProperties = getProjectProperties(args[1]);

            TrafficSimulator trafficSimulator = new TrafficSimulator(projectProperties.getProperty("images.source"),
                                                                     KafkaConfig.getTSConfig("transmitter"));
            trafficSimulator.simulateTraffic();
            logger.info(SEPARATOR);
        } catch (IOException io) {
            logger.error("Error while reading the project properties file.", io);
        }
    }

    public static Properties getProjectProperties(String propertiesFilePath) throws IOException {
        logger.info("Properties file specified at location = " + propertiesFilePath);
        FileInputStream projFile   = new FileInputStream(propertiesFilePath);
        Properties      properties = new Properties();
        properties.load(projFile);
        return properties;
    }
}
