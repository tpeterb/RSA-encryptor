public class Main {

    public static void main(String[] args) {

        long originalMessage = 8;

        RSA_Encryptor rsa = new RSA_Encryptor(11, 17, originalMessage);
        Main.printData(rsa);

        originalMessage = 6;

        RSA_Encryptor rsa_2 = new RSA_Encryptor(11, 13, originalMessage);
        Main.printData(rsa_2);

        originalMessage = 15;
        RSA_Encryptor rsa_3 = new RSA_Encryptor(7, 23, originalMessage);
        Main.printData(rsa_3);

        originalMessage = 20;
        RSA_Encryptor rsa_4 = new RSA_Encryptor(101, 233, originalMessage);
        Main.printData(rsa_4);
    }

    public static void printData(RSA_Encryptor rsa) {
        System.out.println("P = " + rsa.getP());
        System.out.println("Q = " + rsa.getQ());
        System.out.println("N = " + rsa.getN());
        System.out.println("Phi(n) = " + rsa.eulerPhiN());
        System.out.println("E = " + rsa.getE());
        System.out.println("D = " + rsa.getD());
        System.out.println("Public key = " + rsa.getPublicKey());
        System.out.println("Private key = " + rsa.getPrivateKey());
        System.out.println("Encrypted message = " + rsa.getEncryptedMessage());
        System.out.println("Decrypted message = " + rsa.getDecryptedMessage());
        System.out.println("Chinese decryption = " + rsa.decryptWithChineseRemainderTheorem());
        System.out.println("-----------------------------------------");
    }

}
