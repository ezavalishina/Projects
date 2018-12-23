package ru.nsu.fit.g15204.zavalishina.lab2;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public final class MD2 {
    private static int POS = 0;
    private static final int[] X = new int[48];
    private static byte[] message;

    private static int[] S = new int[]{
            41, 46, 67, 201, 162, 216, 124, 1, 61, 54, 84, 161, 236, 240, 6,
            19, 98, 167, 5, 243, 192, 199, 115, 140, 152, 147, 43, 217, 188,
            76, 130, 202, 30, 155, 87, 60, 253, 212, 224, 22, 103, 66, 111, 24,
            138, 23, 229, 18, 190, 78, 196, 214, 218, 158, 222, 73, 160, 251,
            245, 142, 187, 47, 238, 122, 169, 104, 121, 145, 21, 178, 7, 63,
            148, 194, 16, 137, 11, 34, 95, 33, 128, 127, 93, 154, 90, 144, 50,
            39, 53, 62, 204, 231, 191, 247, 151, 3, 255, 25, 48, 179, 72, 165,
            181, 209, 215, 94, 146, 42, 172, 86, 170, 198, 79, 184, 56, 210,
            150, 164, 125, 182, 118, 252, 107, 226, 156, 116, 4, 241, 69, 157,
            112, 89, 100, 113, 135, 32, 134, 91, 207, 101, 230, 45, 168, 2, 27,
            96, 37, 173, 174, 176, 185, 246, 28, 70, 97, 105, 52, 64, 126, 15,
            85, 71, 163, 35, 221, 81, 175, 58, 195, 92, 249, 206, 186, 197,
            234, 38, 44, 83, 13, 110, 133, 40, 132, 9, 211, 223, 205, 244, 65,
            129, 77, 82, 106, 220, 55, 200, 108, 193, 171, 250, 36, 225, 123,
            8, 12, 189, 177, 74, 120, 136, 149, 139, 227, 99, 232, 109, 233,
            203, 213, 254, 59, 0, 29, 57, 242, 239, 183, 14, 102, 88, 208, 228,
            166, 119, 114, 248, 235, 117, 75, 10, 49, 68, 80, 180, 143, 237,
            31, 26, 219, 153, 141, 51, 159, 17, 131, 20
    };

    public static void main(String[] args) throws IOException {
        final String folder = "2";
        message = Files.readAllBytes(Paths.get(folder, "picture.jpg"));

        calculate();

        System.out.print("HASH: ");
        for (int i = 0; i < 16; i++) {
            final BigInteger bigInteger = new BigInteger(Integer.toString(X[i]));
            System.out.print(bigInteger.toString(16));
        }
    }

    private static void calculate() {
        addBits();
        addCheckSum();
        blocksHandling();
    }

    private static void addBits() {
        if ((message.length % 16) != 0) {
            final int a = (16 - message.length % 16);
            final byte[] array = new byte[a];
            Arrays.fill(array, (byte) a);
            message = concatenate(message, array);
        } else {
            final byte[] array = new byte[16];
            Arrays.fill(array, (byte) 16);
            message = concatenate(message, array);
        }
    }

    private static void addCheckSum() {
        final int[] C = new int[16];
        int L = 0;
        int c;
        for (int i = 0; i < message.length / 16; i++) {
            final byte[] array = readBlock(message);
            for (int j = 0; j < 16; j++) {
                c = array[j] & 0xFF;
                C[j] = (S[c ^ L] ^ C[j]);
                L = C[j];
            }
        }
        message = writeBlock(message, C);
    }

    private static void blocksHandling() {
        for (int i = 0; i < message.length / 16; i++) {
            for (int j = 0; j < 16; j++) {
                X[16 + j] = message[i * 16 + j];
                X[32 + j] = X[16 + j] ^ X[j];
            }

            int t = 0;

            for (int j = 0; j < 18; j++) {
                for (int k = 0; k < 48; k++) {
                    X[k] = X[k & 0xFF] ^ S[t & 0xFF];
                    t = X[k & 0xFF] ^ S[t & 0xFF];
                }
                t = (t + j) % 256;
            }
        }
    }

    private static byte[] readBlock(final byte[] message) {
        final byte[] block = Arrays.copyOfRange(message, POS, POS + 16);
        POS += 16;

        return block;
    }

    private static byte[] writeBlock(final byte[] message, final int[] checkSum) {
        final byte[] byteCheckSum = new byte[16];
        for (int i = 0; i < 16; i++) {
            byteCheckSum[i] = (byte) checkSum[i];
        }
        return concatenate(message, byteCheckSum);
    }

    private static byte[] concatenate(final byte[] a, final byte[] b) {
        final int aLen = a.length;
        final int bLen = b.length;

        @SuppressWarnings("unchecked")
        final byte[] c = (byte[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }
}
