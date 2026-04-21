import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Packet {
    private byte[] data;
    private long time;

    public Packet() {
        data = new byte[0];
        time = 0;
    }

    public Packet(byte[] d, long t) {
        data = d;
        time = t;
    }

    public void setData(byte[] d) {
        data = d;
    }

    public void setTime(long t) {
        time = t;
    }

    public byte[] getData() {
        return data;
    }

    public long getTime() {
        return time;
    }

    public byte[] decrypt() {
        Random rand = new Random(time);
        int iv = rand.nextInt();
        return Encryptor.decrypt(data, iv);
    }

    public String toString() {
        String dataStr = new String(data, StandardCharsets.UTF_8);
        return "Data: " + dataStr +
        "\nTime: " + time;
    }
}