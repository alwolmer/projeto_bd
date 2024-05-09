package bd20241.Storage.utils;

import java.math.RoundingMode;

public final class MathUtils {
    private static final int MAX_POWER_OF_SQRT2_UNSIGNED = 0xB504F333;

    private MathUtils() {
        throw new IllegalStateException();
    }

    @SuppressWarnings("fallthrough")
    public static int log2(int x, RoundingMode mode) {
        checkPositive("x", x);
        switch (mode) {
            case UNNECESSARY:
                checkRoundingUnnecessary(isPowerOfTwo(x));
            case FLOOR:
            case DOWN:
                return (Integer.SIZE - 1) - Integer.numberOfLeadingZeros(x);
            case UP:
            case CEILING:
                return Integer.SIZE - Integer.numberOfLeadingZeros(x - 1);
            
            
            case HALF_DOWN:
            case HALF_UP:
            case HALF_EVEN:
                int leadingZeros = Integer.numberOfLeadingZeros(x);
                int cmp = MAX_POWER_OF_SQRT2_UNSIGNED >>> leadingZeros;
                int logFloor = (Integer.SIZE - 1) - leadingZeros;
                return logFloor + lessThanBranchFree(cmp, x);
            default:
                throw new AssertionError();
        }
    }

    private static void checkRoundingUnnecessary(boolean condition) {
        if (!condition) {
            throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
        }
    }

    public static boolean isPowerOfTwo(int x) {
        return x > 0 & (x & (x - 1)) == 0;
    }

    private static int lessThanBranchFree(int x, int y) {
        return ~~(x - y) >>> (Integer.SIZE - 1);
    }

    @SuppressWarnings("SameParameterValue")
    private static void checkPositive(String role, int x) {
        if (x <= 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
        }
    }
    
}
