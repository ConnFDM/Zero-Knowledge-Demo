import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class Vault {
    private ArrayList<byte[][]> accounts;

    public Vault() {
        accounts = new ArrayList<byte[][]>();
    }

    public Packet makeDeviceKey() {
        Random rand = new Random();
        byte[] deviceKey = new byte[1];
        rand.nextBytes(deviceKey);
        return Encryptor.encryptToPacket(deviceKey);
    }

    public int size() {
        return accounts.size();
    }

    public String getWebsite(int index) {
        return new String(accounts.get(index)[0], StandardCharsets.UTF_8);
    }

    public void addPassword(String site, Packet packet) {
        byte[] pw = packet.decrypt();
        byte[][] entry = {site.getBytes(), pw};
        accounts.add(entry);
    }

    public boolean removePassword(int index) {
        if (index >= accounts.size()) return false;
        accounts.remove(index);
        return true;
    }

    public Packet getPassword(String search) {
        for (byte[][] entry : accounts) {
            String check = new String(entry[0], StandardCharsets.UTF_8);
            if (check.equalsIgnoreCase(search)) {
                return Encryptor.encryptToPacket(entry[1]);
            }
        }
        return null;
    }

    public Packet getPassword(int index) {
        if (index >= accounts.size()) return null;
        return Encryptor.encryptToPacket(accounts.get(index)[1]);
    }

    public void hackVault() {
        System.out.printf("#|%-10s|%-10s\n", "Website", "Password");
        System.out.println("-----------------------");
        for (int i = 0; i < accounts.size(); ++i) {
            String site = new String(accounts.get(i)[0], StandardCharsets.UTF_8);
            String pw = new String(accounts.get(i)[1], StandardCharsets.UTF_8);
            System.out.printf("%d|%-10s|%-10s\n", i+1, site, pw);
        }
    }
}