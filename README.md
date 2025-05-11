# 🔐 LSB Hill Cipher Image Encryption 🖼️

**LSB Hill Cipher** is a hybrid cryptographic-steganographic system that encrypts text using the **Hill Cipher algorithm** and embeds the resulting ciphertext inside an image using the **Least Significant Bit (LSB)** technique with a **zig-zag traversal pattern** to increase obfuscation and security.


---

## 📦 What It Does

1. **Encrypts Plaintext**: Uses Hill Cipher with a random key matrix to convert a plaintext into a ciphertext.  
2. **Hides the Ciphertext**: Embeds the ciphertext into a PNG or BMP image using LSB steganography with a zig-zag pattern.  
3. **Extracts the Ciphertext**: Reads the hidden binary message from the LSBs in the image.  
4. **Decrypts the Message**: Applies Hill Cipher decryption to recover the original plaintext from the extracted ciphertext.

---

## 🧠 How It Works

### 🔢 Hill Cipher Encryption
- Generates a random invertible matrix using system time as a seed.
- Encrypts text via matrix multiplication (mod 26).

### 🖼️ LSB Steganography with Zig-Zag Pattern
- Converts the ciphertext to binary bits.
- Embeds those bits into the least significant bits of image pixels.
- Traverses pixels in a zig-zag manner to reduce detectability.

### 🔓 Decryption
- Extracts the ciphertext from the image LSBs using the same zig-zag path.
- Uses the inverse of the Hill matrix to decrypt the original message.

---

## 🛠️ Key Features

- 🔐 **Random Key Matrix Generation** – Uses time-based randomness  
- 🧵 **Zig-Zag Embedding Pattern** – Makes the hidden data harder to trace  
- 🎨 **PNG & BMP Format Support**  
- 📉 **Minimal Quality Loss**  
- 🧪 **Educational Implementation for InfoSec Students**

---

## 📸 Image Demonstration

### 🖼️ Before Embedding  
![Before](https://github.com/user-attachments/assets/514e541b-06e5-4f33-8420-768fb9e4e5ba)

### 🔒 After Embedding  
![After](https://github.com/user-attachments/assets/0b4fef1d-680f-4ebc-852c-929a52aed04f)

---

## 🛠️ Technologies Used

| Component             | Description                       |
|----------------------|-----------------------------------|
| Language             | Java                              |
| Cryptography         | Hill Cipher (Linear Algebra)      |
| Steganography        | LSB with Zig-Zag Traversal        |
| Image Formats        | PNG, BMP                          |
| Random Generator     | Java System Time + Random Class   |
| Image I/O            | Java ImageIO                      |
| Version Control      | Git and GitHub                    |

---

## 🚀 How to Run

```bash
git clone https://github.com/Kareem-Masalma/Encryption-Hill-Cipher-Steganography.git
cd Encryption-Hill-Cipher-Steganography
javac *.java
java Driver
```

> ⚠️ Make sure the input image is in `.png` or `.bmp` format and placed in the root directory. The output image will contain the hidden message.

---

## 👨‍🎓 Author

- **Name:** Kareem Masalma  
- **Student ID:** 1220535

---
