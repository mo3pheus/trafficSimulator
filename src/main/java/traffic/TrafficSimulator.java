package traffic;

import kafka.Transmitter;
import util.ImageUtil;
import util.TrackedLogger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

public class TrafficSimulator {
    private String              imageSource = null;
    private TrackedLogger       logger      = new TrackedLogger(TrafficSimulator.class);
    private List<BufferedImage> imageList   = new ArrayList<>();
    private Transmitter         transmitter = null;

    public TrafficSimulator(String imageSource, Properties kafkaProperties) {
        this.imageSource = imageSource;
        initiateImageList();
        transmitter = new Transmitter(kafkaProperties);
    }

    private void initiateImageList() {
        try (Stream<Path> paths = Files.walk(Paths.get(imageSource))) {
            paths.filter(Files::isRegularFile).forEach(this::process);
        } catch (IOException io) {
            logger.error("Error while walking image source.", io);
        }
    }

    private void process(Path path) {
        try {
            BufferedImage image = ImageIO.read(path.toFile());
            imageList.add(image);
        } catch (IOException e) {
            logger.error("Can't read image." + path.toString(), e);
        }
    }

    public void simulateTraffic() {
        for (BufferedImage image : imageList) {
            try {
                transmitter.transmitMessage(ImageUtil.convertImageToByteArray(image));
                logger.info("Transmitted image = " + image);
            } catch (IOException e) {
                logger.error("Error while simulating traffic", e);
            }
        }
    }
}
