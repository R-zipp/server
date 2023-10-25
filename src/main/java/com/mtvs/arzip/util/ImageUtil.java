package com.mtvs.arzip.util;


import com.amazonaws.util.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

    public static BufferedImage convertStreamToBufferedImage(InputStream stream) throws IOException {
        byte[] bytes = IOUtils.toByteArray(stream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        return ImageIO.read(inputStream);
    }

    public static ByteArrayInputStream getByteArrayInputStream(InputStream stream) throws IOException {
        byte[] bytes = IOUtils.toByteArray(stream);
        return new ByteArrayInputStream(bytes);
    }
}
