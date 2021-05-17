import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Pair<U, V> {
	public final U first; // the first field of a pair
	public final V second; // the second field of a pair

	// Constructs a new pair with specified values
	Pair(U first, V second) {
		this.first = first;
		this.second = second;
	}
}

public class LamportOTP {


	//KeyPair Generation
	public static Pair<BigInteger[][], BigInteger[][]> LamportKey() throws NoSuchAlgorithmException {
		BigInteger[][] privateKey = new BigInteger[2][256];
		BigInteger[][] publicKey = new BigInteger[2][256];

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 256; j++) {
				privateKey[i][j] = new BigInteger(256, new Random());
				publicKey[i][j] = new BigInteger(getSHA(privateKey[i][j].toString()));
			}
		}
		return new Pair(privateKey, publicKey);
	}


	//SHA-256 Hash Function to create Message Digest
	public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
		// Static getInstance method is called with hashing SHA
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		// digest() method called
		// to calculate message digest of an input
		// and return array of byte
		return md.digest(input.getBytes(StandardCharsets.UTF_8));
	}

	// To sign signature

	public static BigInteger[] getSignature(String message, BigInteger[][] privateKey) throws NoSuchAlgorithmException {
		byte[] b = getSHA(message);
		BigInteger digest = new BigInteger(b);

		BigInteger[] signature = new BigInteger[256];
		BigInteger index = new BigInteger("1");

		for (int i = 0; i < 256; i++) {
			int choice = digest.and(index).equals(new BigInteger("0")) ? 0 : 1;

			signature[i] = privateKey[choice][i];
			index = index.multiply(new BigInteger("2"));
		}

		return signature;
	}

	//verify Signature

	public static boolean verifySignature(String message, BigInteger[] signature, BigInteger[][] publicKey)
			throws NoSuchAlgorithmException {
		byte[] b = getSHA(message);
		BigInteger digest = new BigInteger(b);

		BigInteger[] newSignature = getSignature(message, publicKey);
		BigInteger[] hashedSignature = new BigInteger[256];

		for (int i = 0; i < 256; i++) {
			hashedSignature[i] = new BigInteger(getSHA(signature[i].toString()));
		}

		return compareSignature(newSignature, hashedSignature);
	}

	// Signature comparision

	public static boolean compareSignature(BigInteger[] newSignature, BigInteger[] oldSignature)
			throws NoSuchAlgorithmException {
		for (int i = 0; i < 256; i++) {
			if (!newSignature[i].equals(oldSignature[i])) {
				return false;
			}
		}

		return true;
	}


	//Main Function Execution

	public static void main(String[] args) throws NoSuchAlgorithmException {

		Scanner sc = new Scanner(System.in);

		System.out.println("Key Generation in Progress : ");		
		// KeyGeneration
		Pair<BigInteger[][], BigInteger[][]> keyPair = LamportKey();
		BigInteger[][] privateKey = keyPair.first;
		BigInteger[][] publicKey = keyPair.second;
		
		System.out.println("Key Generated Successfully. ");		
		

		// Message Input
		System.out.println("Enter message by Alice : ");
		String message = sc.nextLine();

		// Salt addition for enhancement
		System.out.println("Add salt to enhance Scheme? [Y/N] ");
		String choice = sc.nextLine();
		choice = choice.toUpperCase();

		String salt = new BigInteger(32, new Random()).toString();

		if (choice.equals("Y")) {
			message += salt;
			System.out.println("Message + salt = " + message);
		} else
			System.out.println("Your Message= " + message);

		System.out.println("Generating Signature ...");
		BigInteger[] sign = getSignature(message, privateKey);
		System.out.println("Signature generated successfully");

		System.out.println();
		System.out.println();
		System.out.print("Signature Verification status: ");
		boolean result = verifySignature(message, sign, publicKey);

		if (result) {
			System.out.println("Authorised");
		} else {
			System.out.println("Not Authorised");
		}
	}

}
