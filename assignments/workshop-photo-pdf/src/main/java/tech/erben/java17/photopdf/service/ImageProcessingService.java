package tech.erben.java17.photopdf.service;

import org.springframework.stereotype.Service;
import tech.erben.java17.photopdf.model.ColorMode;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

@Service
public class ImageProcessingService {

    public BufferedImage applyColorMode(BufferedImage sourceImage, ColorMode colorMode) {
        BufferedImage workingCopy = cloneImage(sourceImage);
        BufferedImage processedImage;

        switch (colorMode) {
            case GRAYSCALE:
                processedImage = toGrayscale(workingCopy);
                break;
            case INVERTED:
                processedImage = invertColors(workingCopy);
                break;
            case COLOR:
            default:
                processedImage = workingCopy;
                break;
        }

        return processedImage;
    }

    private BufferedImage cloneImage(BufferedImage sourceImage) {
        BufferedImage copy = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());
        copy.setData(sourceImage.getData());
        return copy;
    }

    private BufferedImage toGrayscale(BufferedImage input) {
        ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        BufferedImage destination = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        return colorConvert.filter(input, destination);
    }

    private BufferedImage invertColors(BufferedImage input) {
        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage inverted = new BufferedImage(width, height, input.getType());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int argb = input.getRGB(x, y);
                Color color = new Color(argb, true);
                Color invertedColor = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue(), color.getAlpha());
                inverted.setRGB(x, y, invertedColor.getRGB());
            }
        }

        return inverted;
    }
}
