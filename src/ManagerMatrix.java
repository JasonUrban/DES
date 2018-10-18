import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class ManagerMatrix {
    private static int[][] getInitialTranspositionMatrix() {
        int[][] matrix = new int[8][8];
        for (int i = 0, start = 58; i < 8; i++, start += i != 4 ? 2 : -7) {
            for (int j = 0; j < 8; j++) {
                matrix[i][j] = start - j * 8;
            }
        }
        return matrix;
    }

    private static int[][] getFinalTranspositionMatrix() {
        int[][] matrix = new int[8][8];
        for (int i = 0, start = 40; i < matrix.length; i++, start--) {
            for (int j = 0, startInner = start; j < matrix[i].length; startInner += j % 2 == 0 ? -32 : 40, j++) {
                matrix[i][j] = startInner;
            }
        }
        return matrix;
    }

    private static int[][] getCompressedKeyMatrix() {
        int[][] matrix = new int[4][14];
        int start = 57, startInner = start;
        for (int i = 0; i < matrix.length / 2; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = startInner;
                start = startInner <= 8 ? start + 1 : start;
                startInner = startInner <= 8 ? start : startInner - 8;
            }
        }
        start = 63;
        startInner = start;
        for (int i = 0; i < matrix.length / 2; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i + 2][j] = startInner;
                start = startInner <= 8 ? start - 1 : start;
                startInner = startInner <= 8 ? start : startInner - 8;
                if (startInner == 60) {
                    startInner = 28;
                }
            }
        }
        return matrix;
    }

    private static int[][] getPermutedKeyMatrix() {
        return new int[][]{
                {14, 17, 11, 24, 1, 5, 3, 28},
                {15, 6, 21, 10, 23, 19, 12, 4},
                {26, 8, 16, 7, 27, 20, 13, 2},
                {41, 52, 31, 37, 47, 55, 30, 40},
                {51, 45, 33, 48, 44, 49, 39, 56},
                {34, 53, 46, 42, 50, 36, 29, 32}
        };
    }

    private static String binaryToText(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length() - 15; i += 16) {
            String item = input.substring(i, i + 16);
            int decimal = Integer.parseInt(item, 2);
            output.append((char) decimal);
        }
        return output.toString();
    }

    private static String textToBinary(String input) {
        StringBuilder bin = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            bin.append(getOlderBitsBinary(Integer.toString((int) input.charAt(i), 2), 16));
        }
        return bin.toString();
    }

    private static String getOlderBitsBinary(String bits, int preferredLength) {
        int length = bits.length();
        if (length != preferredLength) {
            StringBuilder fourBits = new StringBuilder();
            for (int j = 0; j < preferredLength - length; j++) {
                fourBits.append("0");
            }
            fourBits.append(bits);
            bits = fourBits.toString();
        }
        return bits;
    }

    private static String decimalToBinary(int input) {
        return getOlderBitsBinary(Integer.toString(input, 2), 4);
    }

    private static String getPermuted(int[][] matrix, String input) {
        StringBuilder _final = new StringBuilder();
        for (int[] aMatrix : matrix) {
            for (int anAMatrix : aMatrix) {
                _final.append(input.charAt(anAMatrix - 1));
            }
        }
        return _final.toString();
    }

    private static String[] getShiftedSubKeys(String permutedKey) {
        String[] subKeysShifted = new String[16];
        String left = permutedKey.substring(0, permutedKey.length() / 2),
                right = permutedKey.substring(permutedKey.length() / 2);
        for (int i = 0; i < subKeysShifted.length; i++) {
            int num;
            if (i == 0 || i == 1 || i == 8 || i == 15) {
                num = 1;
            } else {
                num = 2;
            }
            for (int j = 0; j < num; j++) {
                subKeysShifted[i] = left.substring(1) + left.substring(0, 1);
                left = subKeysShifted[i];
                subKeysShifted[i] += right.substring(1) + right.substring(0, 1);
                right = subKeysShifted[i].substring(subKeysShifted[i].length() / 2);
            }
        }
        return subKeysShifted;
    }

    private static String[] getRounds(String input, String[] subKeys) {
        String prevLeft = input.substring(0, input.length() / 2), left,
                prevRight = input.substring(input.length() / 2), right;
        String[] rounds = new String[16];
        for (int i = 0; i < rounds.length; i++) {
            left = prevRight;
            right = getXORRound(prevLeft, getModifiedRound(prevRight, subKeys[i]));
            if (i != 15) {
                rounds[i] = left + right;
            } else {
                rounds[i] = right + left;
            }
            prevLeft = left;
            prevRight = right;
        }
        return rounds;
    }

    private static String getModifiedRound(String prevRight, String subKey) {
        String permutedRight = getPermuted(getExpansionPBox(), prevRight);
        String intermediateResult = getXORRound(permutedRight, subKey);
        StringBuilder finalResult = new StringBuilder();
        int[][][] sBoxes = getSBoxes();
        for (int i = 0, j = 0; i < intermediateResult.length(); i += 6, j++) {
            finalResult.append(reduceToFour(sBoxes, intermediateResult.substring(i, i + 6), j));
        }
        return getPermuted(getSimplePBox(), finalResult.toString());
    }

    private static String reduceToFour(int[][][] sBoxes, String sixBits, int number) {
        int row = Integer.parseInt(sixBits.substring(0, 1) + sixBits.substring(sixBits.length() - 1), 2),
                col = Integer.parseInt(sixBits.substring(1, sixBits.length() - 1), 2);
        return decimalToBinary(sBoxes[number][row][col]);
    }

    private static String getXORRound(String operand1, String operand2) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < operand1.length(); i++) {
            int first = Integer.parseInt(operand1.substring(i, i + 1), 2),
                    second = Integer.parseInt(operand2.substring(i, i + 1), 2);
            result.append(Integer.toString(first ^ second));
        }
        return result.toString();
    }

    private static int[][][] getSBoxes() {
        return new int[][][]{
                {
                        {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                        {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                        {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                        {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
                },
                {
                        {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                        {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                        {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                        {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
                },
                {
                        {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                        {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                        {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                        {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
                },
                {
                        {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                        {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                        {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                        {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
                },
                {
                        {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                        {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                        {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                        {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
                },
                {
                        {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                        {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                        {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                        {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
                },
                {
                        {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                        {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                        {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                        {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
                },
                {
                        {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                        {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                        {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                        {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
                }
        };
    }

    private static int[][] getSimplePBox() {
        return new int[][]{
                {16, 7, 20, 21},
                {29, 12, 28, 17},
                {1, 15, 23, 26},
                {5, 18, 31, 10},
                {2, 8, 24, 14},
                {32, 27, 3, 9},
                {19, 13, 30, 6},
                {22, 11, 4, 25}
        };
    }

    private static int[][] getExpansionPBox() {
        int[][] matrix = new int[8][6];
        int start = 1;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i == 0 ? 1 : 0; j < (i == matrix.length - 1 ? matrix[i].length - 1 : matrix[i].length); j++, start++) {
                matrix[i][j] = start;
            }
            start -= 2;
        }
        matrix[0][0] = 32;
        matrix[matrix.length - 1][matrix[matrix.length - 1].length - 1] = 1;
        return matrix;
    }

    static String DESAlgorithm(String text, String key, boolean isDecrypt) {
        String binaryText = textToBinary(text), binaryKey = textToBinary(key), initial;
        String[] _final = new String[binaryText.length() / 64];
        String[][] subKeys = new String[binaryText.length() / 64][16], rounds = new String[binaryText.length() / 64][16];
        for (int i = 0; i < binaryText.length() / 64; i++) {
            int[][] initialTranspositionMatrix = getInitialTranspositionMatrix();
            initial = getPermuted(initialTranspositionMatrix, binaryText.substring(i * 64, i * 64 + 64));
            int[][] compressedKeyMatrix = getCompressedKeyMatrix();
            String keyPermuted = getPermuted(compressedKeyMatrix, binaryKey.substring(i * 64, i * 64 + 64));
            String[] keysShifted = getShiftedSubKeys(keyPermuted);
            int[][] permutedKeyMatrix = getPermutedKeyMatrix();
            for (int j = 0; j < keysShifted.length; j++) {
                subKeys[i][j] = getPermuted(permutedKeyMatrix, keysShifted[j]);
            }
            if (isDecrypt) {
                List<Object> list = Arrays.asList(subKeys[i]);
                Collections.reverse(list);
                subKeys[i] = list.toArray(new String[0]);
            }
            rounds[i] = getRounds(initial, subKeys[i]);
            _final[i] = getPermuted(getFinalTranspositionMatrix(), rounds[i][15]);
        }
        StringBuilder output = new StringBuilder();
        for (String result : _final) {
            output.append(binaryToText(result));
        }
        return output.toString();
    }
}
