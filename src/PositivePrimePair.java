public class PositivePrimePair {
    private final int firstPrime;
    private final int secondPrime;

    public PositivePrimePair(int firstPrime, int secondPrime) {
        if (!RSA_Encryptor.isPositivePrime(firstPrime) || !RSA_Encryptor.isPositivePrime(secondPrime)) {
            throw new IllegalArgumentException();
        }
        this.firstPrime = firstPrime;
        this.secondPrime = secondPrime;
    }

    public int getFirstPrime() {
        return this.firstPrime;
    }

    public int getSecondPrime() {
        return this.secondPrime;
    }
}
