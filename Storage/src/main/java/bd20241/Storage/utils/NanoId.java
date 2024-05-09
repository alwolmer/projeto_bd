package bd20241.Storage.utils;

import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Random;


public final class NanoId {

    public static final SecureRandom DEFAULT_NUMBER_GENERATOR = new SecureRandom();

    public static final String DEFAULT_ALPHABET = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final int DEFAULT_SIZE = 21;

    private NanoId() {
        throw new IllegalStateException();
    }

    public static String randomNanoId() {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_ALPHABET, DEFAULT_SIZE);
    }

    public static String randomNanoId(int size) {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_ALPHABET, size);
    }

    public static String randomNanoId(String alphabet, int size) {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, alphabet, size);
    }

    public static String randomNanoIdForStorage() {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, "123456789ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz", 23);
    }


    public static String randomNanoId(final Random random, final String alphabet, final int size) {
        if (random == null) {
            throw new IllegalArgumentException("random cannot be null.");
        }
        if (alphabet == null) {
            throw new IllegalArgumentException("alphabet cannot be null.");
        }

        if (alphabet.length() == 0 || alphabet.length() >= 256) {
            throw new IllegalArgumentException("alphabet must contain between 1 and 255 symbols.");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater than zero.");
        }

        if (alphabet.length() == 1) {
            return repeat(alphabet.charAt(0), size);
        }

        final int mask = (2 << MathUtils.log2(alphabet.length() - 1, RoundingMode.FLOOR)) - 1;
        final int step = (int) Math.ceil(1.6 * mask * size / alphabet.length());

        final StringBuilder idBuilder = new StringBuilder(size);
        final byte[] bytes = new byte[step];

        while(true) {
            random.nextBytes(bytes);

            for (int i = 0; i < step; i++) {
                final int alphabetIndex = bytes[i] & mask;
                if (alphabetIndex < alphabet.length()) {
                    idBuilder.append(alphabet.charAt(alphabetIndex));
                    if (idBuilder.length() == size) {
                        return idBuilder.toString();
                    }
                }
            }
        }
    }

    private static String repeat(char c, int size){
        StringBuilder builder = new StringBuilder(size);

        for (int i=0; i< size; ++i) {
            builder.append(c);
        }

        return builder.toString();
    }
    
}
