package ru.nsu.fit.g15204.zavalishina.lab3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class Steganography{
    private static int[] rgb;

    public static void main(String[] args) throws IOException {
        String folder = "3";

        final BufferedImage image = ImageIO.read(Files.newInputStream(Paths.get(folder, "picture.bmp")));
        rgb = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        byte[] message = Files.readAllBytes(Paths.get(folder, "key"));

        injectMessage(message);

        image.setRGB(0, 0, image.getWidth(), image.getHeight(), rgb, 0, image.getWidth());
        ImageIO.write(image, "bmp", Files.newOutputStream(Paths.get(folder, "injected.bmp")));

        message = extract();
        final OutputStream outputStream = Files.newOutputStream(Paths.get(folder, "extracted"));
        outputStream.write(message);
        outputStream.close();
    }

    private static void injectMessage(final byte[] message) {
        int count = 0;
        for (byte symbol : message) {
            for (int bitPos = 0; bitPos < 8; bitPos++) {
                injectBit(getBit(symbol, bitPos), count);
                count++;
            }
        }
    }

    private static void injectBit(final boolean bit, final int bitNumber) {
        final int pixelNumber = bitNumber / 3;
        final int channelOffset = (bitNumber % 3) * 8;

        int pixelValue = rgb[pixelNumber];
        int channelValue = pixelValue >>> channelOffset & 0xFF;
        if (bit) {
            channelValue |= 1;
        } else {
            channelValue &= ~1;
        }
        pixelValue = pixelValue & ~(0xFF << channelOffset) | channelValue << channelOffset;

        rgb[pixelNumber] = pixelValue;
    }

    private static byte[] extract() {
        final byte[] message = new byte[rgb.length * 3 / 8];
        int pixelNumber = 0;
        int channelNumber = 0;
        for (int i = 0; i < message.length; i++) {
            byte messageByte = 0;
            for (int j = 0; j < 8; j++) {
                final boolean bit = getBit(rgb[pixelNumber], channelNumber * 8);
                messageByte = pasteBit(messageByte, bit, j);
                channelNumber++;
                if (channelNumber == 3) {
                    channelNumber = 0;
                    pixelNumber++;
                }
            }
            message[i] = messageByte;
        }
        return message;
    }

    private static byte pasteBit(byte messageByte, final boolean bit, final int pos) {
        if (bit) {
            messageByte |= 1 << pos;
        } else {
            messageByte &= ~(1 << pos);
        }
        return messageByte;
    }

    private static boolean getBit(final int b, final int pos) {
        return (b >>> pos & 1) != 0;
    }
}
