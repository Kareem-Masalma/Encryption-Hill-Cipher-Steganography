import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class LSB {
    private BufferedImage image;
    private String message;
    private int width, height;
    private int i, j, k, l, index;
    private boolean flag;
    private String bits;
    private int exp;
    private int length;
    private StringBuilder decryptedText;
    private File file;

    public LSB(File file) {
        try {
            this.image = ImageIO.read(file);
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.i = 0;
            this.j = width - 1;
            this.k = 0;
            this.l = height - 1;
            this.flag = true;
            this.index = 0;
            this.exp = 0;
            this.file = file;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LSB(File file, String message) {
        try {
            this.image = ImageIO.read(file);
            this.message = message;
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.i = 0;
            this.j = width - 1;
            this.k = 0;
            this.l = height - 1;
            this.flag = true;
            this.index = 0;
            this.exp = 0;
            this.file = file;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void encrypt() {
        char[] messageChars = message.toCharArray();
        StringBuilder bitsBuilder = new StringBuilder();

        for (char c : messageChars) {
            String binary = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
            bitsBuilder.append(binary);
        }

        this.bits = bitsBuilder.toString();

        for (int i = 0; i < bits.length(); i += 3) {
            int bit1 = bits.charAt(i) - '0';
            int bit2 = (i + 1 < bits.length()) ? bits.charAt(i + 1) - '0' : 0;
            int bit3 = (i + 2 < bits.length()) ? bits.charAt(i + 2) - '0' : 0;
            encryptBit(bit1, bit2, bit3);
        }
        saveEncryptedImage();
    }

    private void encryptBit(int bit1, int bit2, int bit3) {
        if (flag) {
            int rgb = image.getRGB(i, k);
            int newRGB = changeBits(rgb, bit1, bit2, bit3);
            image.setRGB(i, k, newRGB);
            index += 3;
            i++;
            if (i == width) {
                i = 0;
                k++;
            }
        } else {
            int rgb = image.getRGB(j, l);
            int newRGB = changeBits(rgb, bit1, bit2, bit3);
            image.setRGB(j, l, newRGB);
            index += 3;
            j--;
            if (j == -1) {
                j = width - 1;
                l--;
            }
        }
        flag = !flag;
    }

    private void saveEncryptedImage() {
        try {
            String fileName = file.getName();
            int dotIndex = fileName.lastIndexOf(".");
            String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
            String extension = (dotIndex == -1) ? "png" : fileName.substring(dotIndex + 1);
            File output = new File(file.getParent(), baseName + "-encrypted." + extension);
            ImageIO.write(image, extension, output);
            System.out.println("Encrypted image saved as: " + output.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int changeBits(int rgb, int bit1, int bit2, int bit3) {
        int red = (rgb >> 16) & 0xFF;
        red = (red & ~1) | bit1;
        int green = (rgb >> 8) & 0xFF;
        green = (green & ~1) | bit2;
        int blue = rgb & 0xFF;
        blue = (blue & ~1) | bit3;
        return (red << 16) | (green << 8) | blue;
    }

    public String decrypt(int length) {
        this.i = 0;
        this.j = width - 1;
        this.k = 0;
        this.l = height - 1;
        this.flag = true;
        this.index = 0;
        this.exp = 0;
        this.length = length;

        decryptedText = new StringBuilder();

        for (int i = 0; i < length * 8; i += 3) {
            decryptBit();
        }

        String decryptedBinary = decryptedText.toString();
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < decryptedBinary.length(); i += 8) {
            if (i + 8 <= decryptedBinary.length()) {
                messageBuilder.append((char) Integer.parseInt(decryptedBinary.substring(i, i + 8), 2));
            }
        }
        return messageBuilder.toString();
    }

    private void decryptBit() {
        if (flag) {
            int rgb = image.getRGB(i, k);
            int bit1 = (rgb >> 16) & 1;
            int bit2 = (rgb >> 8) & 1;
            int bit3 = rgb & 1;
            decryptedText.append(bit1).append(bit2).append(bit3);
            index += 3;
            i++;
            if (i == width) {
                i = 0;
                k++;
            }
        } else {
            int rgb = image.getRGB(j, l);
            int bit1 = (rgb >> 16) & 1;
            int bit2 = (rgb >> 8) & 1;
            int bit3 = rgb & 1;
            decryptedText.append(bit1).append(bit2).append(bit3);
            index += 3;
            j--;
            if (j == -1) {
                j = width - 1;
                l--;
            }
        }
        flag = !flag;
    }
}
