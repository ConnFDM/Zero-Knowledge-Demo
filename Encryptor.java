import java.util.Date;
import java.util.Random;

public final class Encryptor {
    public static Packet encryptToPacket(String input) {
        byte[] in = input.getBytes();
        return encryptToPacket(in);
    }

    public static Packet encryptToPacket(byte[] input) {
        long seed = new Date().getTime();
        Random rand = new Random(seed);
        int iv = rand.nextInt();
        byte[] out = encrypt(input, iv);
        return new Packet(out, seed);
    }

    public static byte[] encrypt(String input, long key) {
        byte[] in = input.getBytes();
        return encrypt(in, key);
    }

    public static byte[] encrypt(byte[] input, long key) {
        int len = input.length;
        byte[] out = new byte[len];
        out[0] = (byte)(input[0] ^ key);
        for (int i = 1; i < len; ++i) {
            byte next = (byte)(input[i] ^ out[i-1]);
            out[i] = next;
        }
        return out;
    }

    public static byte[] decrypt(byte[] input, long key) {
        int len = input.length;
        byte[] out = new byte[len];
        out[0] = (byte)(input[0] ^ key);
        for (int i = 1; i < len; ++i) {
            out[i] = (byte)(input[i] ^ input[i-1]);
        }
        return out;
    }
}