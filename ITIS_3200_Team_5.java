import java.nio.charset.StandardCharsets;
import java.util.Scanner;
public class ITIS_3200_Team_5 {
    public static void main(String[] args) {
        Vault accounts = new Vault();
        String scan = "";
        String scan2 = "";
        String site = "";
        String pw;
        Scanner keyboard = new Scanner(System.in);
        byte deviceKey = 0;
        Packet packet = new Packet();

        boolean secure = true;
        do {
            System.out.print("Choose your Security Level:\n" + 
                "1: Insecure\n" +
                "2: Secure\n" +
                "> "
            );
            scan = keyboard.nextLine();
            switch (scan) {
                case "1":
                    secure = false;
                    System.out.println("Insecure version in use.");
                    break;
                case "2":
                    secure = true;
                    System.out.println("Secure version in use.");
                    packet = accounts.makeDeviceKey();
                    System.out.println("--------------------\n" +
                        "Receiving packet...\n" +
                        packet + 
                        "\n--------------------"
                    );
                    deviceKey = packet.decrypt()[0];
                    System.out.println("Device key set.");
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        }
        while (!(scan.equals("1") || scan.equals("2")));

        do {
            System.out.print("\n0: Quit\n" +
                "1: Log In\n" +
                "2: Hack Vault\n" +
                "> "
            );
            scan = keyboard.nextLine();
            switch (scan) {
                case "1": 
                    do {
                        System.out.print("\n0: Log Out\n" +
                            "1: Add Account\n" +
                            "2: Remove Account\n" +
                            "3: See Password\n" +
                            "4: Show Vault\n" +
                            "> "
                        );
                        scan2 = keyboard.nextLine();
                        switch (scan2) {
                            case "1":
                                System.out.print("Enter a website:\n> ");
                                site = keyboard.nextLine();

                                System.out.printf("Enter your password for %s:\n> ", site);
                                pw = keyboard.nextLine();
                                byte[] pwBytes = pw.getBytes();
                                if (secure) pwBytes = Encryptor.encrypt(pwBytes, deviceKey);
                                packet = Encryptor.encryptToPacket(pwBytes);
                                System.out.println("--------------------\n" +
                                    "Sending packet...\n" +
                                    packet + 
                                    "\n--------------------"
                                );
                                accounts.addPassword(site, packet);
                                System.out.println("Account added.");
                                break;
                            case "2":
                                System.out.print("Enter the index to remove:\n> ");
                                site = keyboard.nextLine();
                                try {
                                    int index = Integer.parseInt(site);
                                    if (accounts.removePassword(index-1)) {
                                        System.out.print("Account removed.");
                                    }
                                    else System.out.print("Index out of range.");
                                }
                                catch (Exception e) {
                                    System.out.print("Invalid index.");
                                }
                                break;
                            case "3":
                                System.out.print("Enter website to check:\n> ");
                                site = keyboard.nextLine();
                                packet = accounts.getPassword(site);
                                System.out.println("--------------------\n" +
                                    "Receiving packet...\n" +
                                    packet + 
                                    "\n--------------------"
                                    );
                                if (packet == null) {
                                    System.out.println("Password not found.");
                                    break;
                                }
                                pwBytes = packet.decrypt();
                                if (secure) pwBytes = Encryptor.decrypt(pwBytes, deviceKey);
                                pw = new String(pwBytes, StandardCharsets.UTF_8);
                                System.out.println("Password: " + pw);
                                break;
                            case "4":
                                System.out.println("--------------------\n" +
                                    "Receiving " + accounts.size() + " packets...\n" +
                                    "--------------------\n"
                                    );
                                System.out.printf("#|%-10s|%-10s\n", 
                                "Website", "Password");
                                System.out.println("-----------------------");
                                for (int i = 0; i < accounts.size(); ++i) {
                                    packet = accounts.getPassword(i);
                                    pwBytes = packet.decrypt();
                                    if (secure) pwBytes = Encryptor.decrypt(pwBytes, deviceKey);
                                    pw = new String(pwBytes, StandardCharsets.UTF_8);
                                    System.out.printf("%d|%-10s|%-10s\n",
                                        i+1, accounts.getWebsite(i), pw);
                                }
                                break;
                            case "0":
                                System.out.println("Closing.");
                                break;
                            default:
                                System.out.println("Invalid selection.");
                            }
                    }
                    while (!scan2.equals("0"));
                    break;
                case "2":
                    accounts.hackVault();
                    break;
                case "0":
                    System.out.println("Closing.");
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        }
        while (!scan.equals("0"));
        keyboard.close();
    }
}