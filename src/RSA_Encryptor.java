import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class RSA_Encryptor {

    private long p;
    private long q;
    private long n;
    private long e;
    private long d;
    private long originalMessage;
    private long encryptedMessage;
    private long decryptedMessage;

    public RSA_Encryptor(long originalMessage, int upperBound) {
        this.originalMessage = originalMessage;
        PositivePrimePair pair = getPrimePair(upperBound);
        this.p = pair.getFirstPrime();
        this.q = pair.getSecondPrime();
        encrypt();
    }

    public RSA_Encryptor(long p, long q, long originalMessage) {
        this.p = p;
        this.q = q;
        this.originalMessage = originalMessage;
        encrypt();
    }

    public long getP() {
        return this.p;
    }

    public long getQ() {
        return this.q;
    }

    public long getN() {
        return this.n;
    }

    public long getE() {
        return this.e;
    }

    public long getD() {
        return this.d;
    }

    public long getOriginalMessage() {
        return this.originalMessage;
    }

    public long getEncryptedMessage() {
        return this.encryptedMessage;
    }

    public long getDecryptedMessage() {
        return this.decryptedMessage;
    }

    public void setOriginalMessage(long originalMessage) {
        this.originalMessage = originalMessage;
    }

    public void setEncryptedMessage(long encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

    public void setDecryptedMessage(long decryptedMessage) {
        this.decryptedMessage = decryptedMessage;
    }

    public void setP(int p) {
        if (!isPositivePrime(p)) {
            throw new IllegalArgumentException();
        }
        this.p = p;
    }

    public void setQ(int q) {
        if (!isPositivePrime(q)) {
            throw new IllegalArgumentException();
        }
        this.q = q;
    }

    private void calculateN() {
        this.n = this.p * this.q;
    }

    public int eulerPhiN() {
        return (int) ((this.p - 1) * (this.q - 1));
    }

    private void calculateE() {
        boolean found = false;
        int i = 3;
        while (!found) {
            if (i % 2 == 1 && arePositiveRelativePrimes(i, eulerPhiN())) {
                this.e = i;
                return;
            }
            i += 2;
        }
    }

    private void calculateD() {
        boolean found = false;
        int i = 1;
        while (!found) {
            if ((i * this.e) % eulerPhiN() == 1) {
                this.d = i;
                return;
            }
            i++;
        }
    }

    public String getPublicKey() {
        return String.format("(%d, %d)", this.e, this.n);
    }

    public String getPrivateKey() {
        return String.format("(%d, %d)", this.d, this.n);
    }

    public static boolean isPositivePrime(int n) {
        if (n < 2) {
            return false;
        }
        if (n > 2 && n % 2 == 0) {
            return false;
        }
        int numberOfDivisors = 1;
        for (int i = 1; i < (int) Math.sqrt(n) + 1; i++) {
            if (n % i == 0) {
                numberOfDivisors++;
            }
        }
        if (numberOfDivisors == 2) {
            return true;
        }
        return false;
    }

    public static boolean arePositiveRelativePrimes(int a, int b) {
        if (a < 1 || b < 1) {
            throw new IllegalArgumentException();
        }
        if (a == b && a != 1) {
            return false;
        }
        if (isPositivePrime(a) && isPositivePrime(b)) {
            return true;
        }
        int numberOfCommonDivisors = 0;
        int upperBound = Math.min(a, b);
        for (int i = 1; i <= upperBound; i++) {
            if (a % i == 0 && b % i == 0) {
                numberOfCommonDivisors++;
            }
            if (numberOfCommonDivisors > 1) {
                return false;
            }
        }
        if (numberOfCommonDivisors == 1) {
            return true;
        }
        return false;
    }

    public static PositivePrimePair getPrimePair(int upperBound) {
        PositivePrimePair primePair;
        int p = 0, q = 0; // Primes
        Random random = new Random();
        int firstRandom = random.nextInt(upperBound) + 1;
        int secondRandom = firstRandom;
        while (secondRandom == firstRandom) {
            secondRandom = random.nextInt(upperBound) + 1;
        }
        int chosenPrimeCounter = 0;
        int primeCounter = 0;
        int i = 2;
        while (chosenPrimeCounter < 2) {
            if (isPositivePrime(i)) {
                primeCounter++;
                if (primeCounter == firstRandom) {
                    p = i;
                    chosenPrimeCounter++;
                }
                if (primeCounter == secondRandom) {
                    q = i;
                    chosenPrimeCounter++;
                }
            }
            i++;
        }
        primePair = new PositivePrimePair(p, q);
        return primePair;
    }

    public void encrypt() {
        calculateN();
        calculateE();
        calculateD();
        //this.encryptedMessage = /*new BigDecimal(String.valueOf(*/(int) Math.pow(this.originalMessage, this.e) % this.n;
        this.encryptedMessage = fastComputationOfPowers(this.originalMessage, this.e, this.n);
        decrypt();
    }

    public void decrypt() {
        /*BigDecimal encryptedMessage = new BigDecimal(String.valueOf(this.encryptedMessage));
        BigDecimal modulus = new BigDecimal(String.valueOf(this.n));
        BigDecimal result = encryptedMessage.pow(this.d).remainder(modulus);
        this.decryptedMessage = result;*/
        this.decryptedMessage = fastComputationOfPowers(this.encryptedMessage, this.d, this.n);
    }

    /*public BigDecimal decrypt(int encryptedMessage) {
        BigDecimal encryptedDecimal = new BigDecimal(String.valueOf(encryptedMessage));
        BigDecimal modulus = new BigDecimal(String.valueOf(this.n));
        BigDecimal decryptedMessage = encryptedDecimal.pow(this.d).remainder(modulus);
        return decryptedMessage;
    }*/

    public long decryptWithChineseRemainderTheorem() {
        long result;
        long c1 = fastComputationOfPowers(this.encryptedMessage, fastComputationOfPowers(this.d, 1, this.p - 1), this.p);
        long c2 = fastComputationOfPowers(this.encryptedMessage, fastComputationOfPowers(this.d, 1, this.q - 1), this.q);
        long M = this.p * this.q;
        long M1 = this.q;
        long M2 = this.p;
        long[] values = extendedEuclideanAlgorithm(this.p, this.q);
        long x = values[1];
        long y = values[2];
        result = fastComputationOfPowers(c1 * x * M1 + c2 * y * M2, 1, M);
        return result;
    }

    public long[] extendedEuclideanAlgorithm(long p, long q) {
        long[] results = new long[3];
        long a = Math.max(p, q);
        long b = Math.min(p, q);
        long r = a % b;
        long quotient = a / b;
        ArrayList<Long> xValues = new ArrayList<Long>();
        xValues.add((long) 1);
        xValues.add((long) 0);
        ArrayList<Long> yValues = new ArrayList<Long>();
        yValues.add((long) 0);
        yValues.add((long) 1);
        long x = xValues.get(1) * quotient + xValues.get(0);
        long y = yValues.get(1) * quotient + yValues.get(0);
        xValues.add(x);
        yValues.add(y);
        int counter = 3;
        while(r != 0) {
            a = b;
            b = r;
            quotient = a / b;
            r = a % b;
            if (r != 0) {
                x = xValues.get(counter - 1) * quotient + xValues.get(counter - 2);
                y = yValues.get(counter - 1) * quotient + yValues.get(counter - 2);
                xValues.add(x);
                yValues.add(y);
                counter++;
            }
        }
        x = (int) Math.pow(-1, counter - 1) * x;
        y = (int) Math.pow(-1, counter) * y;
        results[0] = b;
        results[1] = x;
        results[2] = y;
        return results;
    }

    public long fastComputationOfPowers(long base, long exponent, long modulus) {
        if (exponent == 0) {
            if (modulus == 1 || modulus == -1) {
                return 0;
            } else {
                return 1;
            }
        }
        long exponentCopy = exponent;
        ArrayList<Long> exponentsOfTwo = new ArrayList<Long>();
        ArrayList<Long> remnantsFromCongruences = new ArrayList<Long>();
        long power = 0;
        while (Math.pow(2, power) <= exponent) { //Determining the biggest exponent of two that's necessary
            power++;
        }
        power--;
        while (power >= 0) { //Writing the exponent as the sum of powers of 2
            if (Math.pow(2, power) <= exponentCopy) {
                exponentsOfTwo.add(power);
                exponentCopy -= Math.pow(2, power);
            }
            power--;
        }
        for (int i = 0; i <= exponentsOfTwo.get(0); i++) { //Solving the congruences
            if (i == 0) {
                remnantsFromCongruences.add((int) Math.pow(base, Math.pow(2, 0)) % modulus);
            } else if (i == 1) {
                remnantsFromCongruences.add((int) Math.pow(base, Math.pow(2, 1)) % modulus);
            }
            else {
                remnantsFromCongruences.add((int) Math.pow(remnantsFromCongruences.get(i - 1), 2) % modulus);
            }
        }
        long previousRemnant = 1;
        for (int i = 0; i < exponentsOfTwo.size(); i++) {
            previousRemnant = (previousRemnant * remnantsFromCongruences.get(exponentsOfTwo.get(i).intValue())) % modulus;
        }
        return previousRemnant;
    }

}
