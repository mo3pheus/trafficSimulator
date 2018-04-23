package util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtil {
    private static TrackedLogger logger = new TrackedLogger(ImageUtil.class);

    public static void writeImageToFile(BufferedImage bufferedImage, String dataArchivePath) {
        if (bufferedImage == null) {
            throw new RuntimeException("BufferedImage was null");
        }

        String fileName   = dataArchivePath;
        File   outputFile = new File(fileName);
        logger.debug("CameraUtil trying to write the following fileName = " + fileName);
        try {
            ImageIO.write(bufferedImage, "jpg", outputFile);
        } catch (IOException io) {
            logger.error("Cant write the following file =>" + fileName, io);
        }
    }

    public static byte[] convertImageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(10000);
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        byteArrayOutputStream.flush();

        String base64String = Base64.encode(byteArrayOutputStream.toByteArray());
        byteArrayOutputStream.close();

        return Base64.decode(base64String);
    }
}
