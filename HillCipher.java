public class HillCipher {
    private int[][] key;
    private String plaintText;

    public HillCipher(int[][] key) {
        this.key = key;
    }

    public int[][] getKey() {
        return this.key;
    }

    public String getPlaintText() {
        return this.plaintText;
    }

    public void setPlaintText(String plaintText) {
        if (plaintText.length() % 2 != 0) {
            plaintText += "x";
        }
        this.plaintText = plaintText;
    }

    public String encrypt(String plaintText) {
        int[] plaintTextVector = new int[plaintText.length()];
        for (int i = 0; i < plaintText.length(); i++) {
            plaintTextVector[i] = plaintText.charAt(i);
        }
        int[] cipherTextVector = new int[plaintText.length()];
        int[][] letters = new int[2][1];
        for (int i = 0; i < plaintText.length() - 1; i+=2) {
            letters[0][0] = plaintTextVector[i];
            letters[1][0] = plaintTextVector[i + 1];
            int[][] result = multiply(key, letters);
            cipherTextVector[i] = result[0][0] % 128;
            cipherTextVector[i + 1] = result[1][0] % 128;
        }
        StringBuilder cipherText = new StringBuilder();
        for (int j : cipherTextVector) {
            cipherText.append((char) (j));
        }
        return cipherText.toString();
    }

    public int[][] multiply(int[][] a, int[][] b) {
        int[][] result = new int[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < a[0].length; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    public String decrypt(String cipher) {
        int determinant = getDeterminant(key);
        int determinantInverse = extendedEuclidean(determinant, 128);
        int[][] keyInverse = new int[2][2];
        keyInverse[0][0] = (key[1][1] * determinantInverse);
        keyInverse[1][1] = (key[0][0] * determinantInverse);
        keyInverse[0][1] = (-key[0][1] * determinantInverse);
        keyInverse[1][0] = (-key[1][0] * determinantInverse);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                keyInverse[i][j] = keyInverse[i][j] % 128;
                if (keyInverse[i][j] < 0) {
                    keyInverse[i][j] += 128;
                }
            }
        }
        int[] cipherTextVector = new int[cipher.length()];
        for (int i = 0; i < cipher.length(); i++) {
            cipherTextVector[i] = cipher.charAt(i);
        }
        int[] plaintTextVector = new int[cipher.length()];
        int[][] letters = new int[2][1];
        for (int i = 0; i < cipher.length() - 1; i+=2) {
            letters[0][0] = cipherTextVector[i];
            letters[1][0] = cipherTextVector[i + 1];
            int[][] result = multiply(keyInverse, letters);
            plaintTextVector[i] = result[0][0] % 128;
            plaintTextVector[i + 1] = result[1][0] % 128;
        }
        StringBuilder plaintText = new StringBuilder();
        for (int j : plaintTextVector) {
            plaintText.append((char) (j));
        }
        return plaintText.toString();
    }

    public static int getDeterminant(int[][] key) {
        return key[0][0] * key[1][1] - key[0][1] * key[1][0];
    }

    public int extendedEuclidean(int a, int b) {
        int x = 0, y = 1, lastX = 1, lastY = 0, temp;
        while (b != 0) {
            int quotient = a / b;
            int remainder = a % b;
            a = b;
            b = remainder;
            temp = x;
            x = lastX - quotient * x;
            lastX = temp;
            temp = y;
            y = lastY - quotient * y;
            lastY = temp;
        }
        return lastX;
    }

    public static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    public static boolean isValidKey(int[][] a) {
        int determinant = getDeterminant(a);
        return determinant != 0 && gcd(determinant, 128) == 1;
    }
}
