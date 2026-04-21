Command line interface (CLI) that demonstrates the zero-knowledge architecture used by password managers such as DashLane.

Run with any program that can run Java source code e.g. VSCode.

For failure case: 
- Select "1: Insecure" on boot. This version does not use zero-knowledge architecture. 
- Log in and add accounts. Websites will be sent to a vault in plaintext, while their corresponding passwords will first be encrypted in a packet using rudimentary cipher block chaining (CBC). 
- Retrieve your passwords while logged in. The vault will again encrypt your passwords with CBC before sending them back to you.
- Log out, then select "2: Hack Vault". This will show what the vault looks like on the password manager's end, and what a hacker would see if they breached its security. In this version, they will have full access to your passwords.

For success case:
- Select "2: Secure" on boot. This version uses zero-knowledge architecture.
- Log in and add accounts. In this version, passwords will first be encrypted using a device key, which is only ever known by the user's device. That ciphertext will then be encrypted again into a packet to be sent to the vault.
- Retrieve your passwords. Your device will need to decrypt the output twice; once out of its packet, then again with the device key.
- Log out, then select "2: Hack Vault". Because of zero-knowledge architecture, even someone who has breached security will only see ciphertext, encrypted by your device key, which isn't stored anywhere by the password manager once it's generated. A hacker in this case would also need to access your device to learn the device key.
