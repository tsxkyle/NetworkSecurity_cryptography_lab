package cryptography;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Nguyen Duy Tiep on 23-Oct-17.
 */
public class RSA {

    private BigInteger modulus;
    private BigInteger privateKey;
    private BigInteger publicKey;

    public RSA(int bits) {
        generateKeys(bits);
    }

    /**
     * @return encrypted message
     */
    public synchronized String encrypt(String message) {
        return (new BigInteger(message.getBytes())).modPow(publicKey, modulus).toString();
    }

    /**
     * @return encrypted message as big integer
     */
    public synchronized BigInteger encrypt(BigInteger message) {
        return message.modPow(publicKey, modulus);
    }

    /**
     * @return plain message
     */
    public synchronized String decrypt(String encryptedMessage) {
        return new String((new BigInteger(encryptedMessage)).modPow(privateKey, modulus).toByteArray());
    }

    /**
     * @return plain message as big integer
     */
    public synchronized BigInteger decrypt(BigInteger encryptedMessage) {
        return encryptedMessage.modPow(privateKey, modulus);
    }

    /**
     * Generate a new public and private key set.
     */
    public synchronized void generateKeys(int bits) {
        SecureRandom r = new SecureRandom();
        BigInteger p = new BigInteger(bits / 2, 100, r);
        BigInteger q = new BigInteger(bits / 2, 100, r);
        modulus = p.multiply(q);

        BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        publicKey = BigInteger.valueOf(3L);

        while (m.gcd(publicKey).intValue() > 1) {
            publicKey = publicKey.add(BigInteger.TWO);
        }

        privateKey = publicKey.modInverse(m);
    }

	//  Driver Code
	public static void main(String[] args) throws IOException {
		
		RSA rsa = new RSA(1024);
		DataInputStream input = new DataInputStream(System.in);
		
		// Input Message
		System.out.print("Enter the Message: ");
		@SuppressWarnings("deprecation")
		String textToEncrypt = input.readLine();

		// Encrypting the Message
        String cipherText = rsa.encrypt(textToEncrypt);
		System.out.println("Encoded Message: " + cipherText);

		// Decrypting the Message
		String decryptedText = rsa.decrypt(cipherText);
		System.out.println("Decoded Message: " + decryptedText);
	}
}
