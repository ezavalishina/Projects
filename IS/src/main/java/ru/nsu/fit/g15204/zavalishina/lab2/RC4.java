package ru.nsu.fit.g15204.zavalishina.lab2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class RC4 {
    private static final int S_LENGTH = 256;
    private static final byte[] S = new byte[S_LENGTH];

    public static void main(String[] args) throws IOException {
        String folder = "2";

        final byte[] toEncrypt = Files.readAllBytes(Paths.get(folder, "picture.jpg"));
        final byte[] key = Files.readAllBytes(Paths.get(folder, "key"));

        Files.write(Paths.get(folder, "encrypted"),
                PRGAcrypt(key, toEncrypt));

        final byte[] toDecrypt = Files.readAllBytes(Paths.get(folder, "encrypted"));

        Files.write(Paths.get(folder, "decrypted"),
                PRGAcrypt(key, toDecrypt));

    }

    private static byte[] PRGAcrypt(final byte[] key, final byte[] message) {
        KSA(key);
        int i = 0;
        int j = 0;
        for (int k = 0; k < message.length; k++) {
            i = (i + 1) & 0xFF;
            j = (j + S[i]) & 0xFF;
            swap(i, j);
            final int t = (S[i] + S[j]) & 0xFF;
            message[k] ^= S[t];
        }
        return message;
    }

    private static void KSA(final byte[] key) {
        final int L = key.length;
        for (int i = 0; i < S_LENGTH; i++) {
            S[i] = (byte) i;
        }
        int j = 0;
        for (int i = 0; i < S_LENGTH; i++) {
            j = (j + S[i] + key[i % L]) & 0xFF;
            swap(i, j);
        }
    }

    private static void swap(final int i, final int j) {
        byte tmp = S[i];
        S[i] = S[j];
        S[j] = tmp;
    }
}
