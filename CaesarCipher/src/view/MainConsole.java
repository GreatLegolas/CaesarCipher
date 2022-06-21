package view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class MainConsole {

    public static String alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя .,\":-!?";
    public static char[] alphabetInChars = alphabet.toCharArray();

    String message = "";
    char ch;
    int key;

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        int choice = 0;
        do {
            System.out.println("1: Зашифровать файл\n2: Расшифровать файл\n3: Расшифровать файл (Brute force)\n4: Выход");
            choice = console.nextInt();
            switch (choice) {
                case 1: new MainConsole().EncryptTextFile(); break;
                case 2: new MainConsole().DecryptTextFile(); break;
                case 3: new MainConsole().BruteForceCryptoAnalysis(); break;
                case 4: break;
            }
        } while (choice != 4);
    }

        // first method to encrypt the file

    void EncryptTextFile() { //  throws FileNotFoundException {
            // System.setProperty("file.encoding", "UTF-8");
            String encryptedMessage = "";
            char ch;
            Scanner sc = new Scanner(System.in);
            String fileToEncrypt = "src/module/toEncrypt.txt";
            // need to check the existence of the file to encrypt
            if (Files.notExists(Path.of(fileToEncrypt))) {
                // throw new FileNotFoundException("Файл для шифрования не найден");
                System.out.println("Файл для шифрования не найден");
                System.exit(-1);
            }
            Path path = Path.of(fileToEncrypt);
            System.out.println("Введите ключ (число сдвига/ов): ");
            key = sc.nextInt();
            try {
                message = Files.readString(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Файл " + "src/module/EncryptedText.txt" + " получен для шифрации с ключом " + key);

            for (int i = 0; i < message.length(); i++) {
                ch = message.charAt(i);
                // String stringToSearch = ch + "";
                if (indexOfNeededChar(ch) != -1) {
                    int indexOfNewChar = indexOfNeededChar(ch) + key;
                    if (indexOfNewChar >= alphabetInChars.length) {
                        int fromStart = indexOfNewChar - alphabetInChars.length;
                        ch = alphabetInChars[fromStart];
                    } else {
                        ch = alphabetInChars[indexOfNewChar];
                    }
                    encryptedMessage += ch;
                } else {
                    encryptedMessage += ch;
                    // System.out.println("changeless" + ch);
                }
            }
            Path fileWithEncryptedText = Path.of("src/module/EncryptedText.txt");

            try {
                Files.writeString(fileWithEncryptedText, encryptedMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Зашифрованный текст записан в файл " + fileWithEncryptedText);

    }

    // second method to decrypt the file

    void DecryptTextFile() {
            String decryptedMessage = "";
            Scanner sc = new Scanner(System.in);
            System.out.println("Введите ключ (число сдвига/ов): ");
            key = sc.nextInt();
            Path fileWithEncryptedText = Path.of("src/module/EncryptedText.txt");
            try {
                message = Files.readString(fileWithEncryptedText);
            } catch (FileNotFoundException e) {
                e.getMessage();
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < message.length(); i++) {
                ch = message.charAt(i);
                // String stringToSearch = ch + "";
                if (indexOfNeededChar(ch) != -1) {
                    int indexOfNewChar = indexOfNeededChar(ch) - key;
                    if (indexOfNewChar < 0) {
                        int fromEnd = indexOfNewChar + alphabetInChars.length;
                        ch = alphabetInChars[fromEnd];
                    } else {
                        ch = alphabetInChars[indexOfNewChar];
                    }
                    decryptedMessage += ch;
                } else {
                    decryptedMessage += ch;
                }
            }
            String fileWithDecipheredText = "src/module/DecipheredText.txt";
            try {
                Files.writeString(Path.of(fileWithDecipheredText), decryptedMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Расшифрованный текст записан в файл " + fileWithDecipheredText);
        }


        // third method to brute force the encrypted file


        void BruteForceCryptoAnalysis() {
                String bruteForcedMessage = "";
                Path fileWithEncryptedText = Path.of("src/module/EncryptedText.txt");
                try {
                    message = Files.readString(fileWithEncryptedText);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int key = 1; key < alphabet.length(); key++) {
                    // System.out.print("Key is " + key + " ");
                    for (int i = 0; i < message.length(); i++) {
                        ch = message.charAt(i);

                        if (indexOfNeededChar(ch) != -1) {
                            int indexOfNewChar = indexOfNeededChar(ch) - key;
                            if (indexOfNewChar < 0) {
                                int fromEnd = indexOfNewChar + alphabetInChars.length;
                                ch = alphabetInChars[fromEnd];
                            } else {
                                ch = alphabetInChars[indexOfNewChar];
                            }
                            bruteForcedMessage += ch;
                        } else {
                            bruteForcedMessage += ch;
                        }
                    }
                    if (isCorrect(bruteForcedMessage)) {
                        System.out.println("Определен ключ шифрации, который равен: " + key);
                        break;
                    } else {
                        bruteForcedMessage = "";
                    }
                }
            }

            public static boolean isCorrect(String bruteForcedMessage) {
                boolean result = false;
                if ((countMatches(bruteForcedMessage, " ,") > 0) ||
                        (countMatches(bruteForcedMessage, " .") > 0)) {
                    // System.out.println("Correct");
                    return false;
                }
                if ((countMatches(bruteForcedMessage, ", ") > 1) &&
                        (countMatches(bruteForcedMessage, ". ") > 0)) {
                    // System.out.println("Correct");
                    result = true;
                    // System.out.println(bruteForcedMessage);
                    String fileWithBruteForcedText = "src/module/BruteForcedText.txt";
                    try {
                        Files.writeString(Path.of(fileWithBruteForcedText), bruteForcedMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Расшифрованный (brute force) текст записан в файл " + fileWithBruteForcedText);
                }
                return result;
            }

            int indexOfNeededChar(char ch) {
                int index = -1;
                for (int i = 0; i < alphabetInChars.length; i++) {
                    if (alphabetInChars[i] == ch) {
                        index = i;
                        break;
                    }
                }
                return index;
            }
    /* Checks if a string is empty ("") or null. */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /* Counts how many times the substring appears in the larger string. */
    public static int countMatches(String text, String str)
    {
        if (isEmpty(text) || isEmpty(str)) {
            return 0;
        }

        int index = 0, count = 0;
        while (true)
        {
            index = text.indexOf(str, index);
            if (index != -1)
            {
                count ++;
                index += str.length();
            }
            else {
                break;
            }
        }

        return count;
    }
}
