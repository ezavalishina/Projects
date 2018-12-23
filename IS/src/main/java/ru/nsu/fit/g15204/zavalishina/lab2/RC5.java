package ru.nsu.fit.g15204.zavalishina.lab2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public final class RC5 {
    private static final int W = 64;
    private static final int R = 5;
    private static final long P = 0xB7E151628AED2A6BL;
    private static final long Q = 0x9E3779B97F4A7C15L;
    private static long[] S;
    private static long[] L;
    private static int c;

    public static void main(String[] args) throws IOException {
        final String folder = "2";

        final byte[] key = Files.readAllBytes(Paths.get(folder, "key"));
        init(key);

        byte[] bytesToEncrypt = Files.readAllBytes(Paths.get(folder, "picture.jpg"));
        bytesToEncrypt = Arrays.copyOf(bytesToEncrypt, bytesToEncrypt.length + 15);
        final long[] toEncrypt = new long[bytesToEncrypt.length / 16 * 2];
        final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytesToEncrypt));
        for (int i = 0; i < toEncrypt.length; i++) {
            toEncrypt[i] = dataInputStream.readLong();
        }
        dataInputStream.close();

        final long[] toDecrypt = encrypt(toEncrypt);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(toDecrypt.length * 8);
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        for (int i = 0; i < toDecrypt.length; i++) {
            dataOutputStream.writeLong(toDecrypt[i]);
        }
        Files.write(Paths.get(folder, "encrypted"),byteArrayOutputStream.toByteArray());

        final long[] decrypted = decrypt(toDecrypt);
        byteArrayOutputStream = new ByteArrayOutputStream(decrypted.length * 8);
        dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        for (int i = 0; i < decrypted.length; i++) {
            dataOutputStream.writeLong(decrypted[i]);
        }
        Files.write(Paths.get(folder, "decrypted"),byteArrayOutputStream.toByteArray());

        dataOutputStream.close();
        byteArrayOutputStream.close();
    }

    private static void init(final byte[] key) {
        keySplitting(key);
        tableConstruction();
        mixing();
    }

    private static long[] encrypt(final long[] message) {
        for (int i = 0; i < message.length / 2; i++) {
            long A = message[i * 2] + S[0];
            long B = message[i * 2 + 1] + S[1];
            for (int j = 1; j <= R; j++) {
                A = Long.rotateLeft(A ^ B, (int) B) + S[j * 2];
                B = Long.rotateLeft(A ^ B, (int) A) + S[j * 2 + 1];
            }
            message[i * 2] = A;
            message[i * 2 + 1] = B;
        }
        return message;
    }

    private static long[] decrypt(final long[] message) {
        for (int i = 0; i < message.length / 2; i++) {
            long A = message[i * 2];
            long B = message[i * 2 + 1];
            for (int j = R; j >= 1; j--) {
                B = Long.rotateRight(B - S[j * 2 + 1], (int) A) ^ A;
                A = Long.rotateRight(A - S[j * 2], (int) B) ^ B;
            }
            message[i * 2 + 1] = B - S[1];
            message[i * 2] = A - S[0];
        }
        return message;
    }

    private static void keySplitting(final byte[] key) {
        final int b = key.length;

        c = (b + 7) / (W / 8);
        L = new long[c];

        if (c == b && b == 0) {
            c = 1;
            L = new long[1];
            L[0] = 0;
            return;
        }

        for (int i = c - 1; i >= 0; i--) {
            L[i / 8] = (L[i / 8] << 8) + key[i];
        }
    }

    private static void tableConstruction() {
        S = new long[2 * (R + 1)];
        S[0] = P;
        for (int i = 1; i < S.length; i++) {
            S[i] = S[i - 1] + Q;
        }
    }

    private static void mixing() {
        int i = 0;
        int j = 0;
        long G = 0;
        long H = 0;
        int N = Math.max(3 * c, 3 * 2 * (R + 1));

        for (int k = 0; k < N; k++) {
            G = S[i] = Long.rotateLeft(S[i] + G + H, 3);
            H = L[j] = Long.rotateLeft(L[j] + G + H, (int)(G + H));
            i = (i + 1) % (2 * (R + 1));
            j = (j + 1) % c;
        }
    }
}
