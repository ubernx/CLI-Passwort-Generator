import java.util.Scanner;
import java.util.Random;

public class Main {

    public static final String CMD_EXIT = "/exit";
    public static final String CMD_GENERATE = "/generate";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String INVALID_CMD = "Unbekannte Anweisung";
    public static final String ERROR_POSITIVE_INTEGER = "Sie dürfen nur positive ganze Zahlen verwenden";
    public static final String ERROR_LENGTH = "Sie haben den Rahmen von 4-16 Zeichen pro Passwort überschritten";
    public static final String ERROR_MODE = "Ungültiger Modus ausgewählt.";
    public static final String WELCOME = "\nWillkommen zum Passwort-Generator.";
    public static final String ASK_LENGTH = "Bitte geben Sie die gewünschte Länge des Passworts an: ";
    public static final String ASK_COUNT = "Bitte geben Sie die gewünschte Anzahl der Passwörter an: ";
    public static final String ASK_MODE = "Modus (1 oder 2): ";
    public static final String MENU_HELP = """
            Folgende Commands sind verfügbar:
            '/generate' (Generiert eine gewählte Anzahl von Passwörtern, mit der gewählten Länge und der gewählten Zeichenart)
            '/exit'     (Beendet und schließt das Programm)
            """;

    public static final String MODE_SELECTION = """
            Bitte wählen Sie den Modus:
            1: Groß- und Kleinbuchstaben + Zahlen
            2: Groß- und Kleinbuchstaben + Zahlen + Sonderzeichen
            """;

    public static long PW_GENERATED_TIME = 0;
    public static long PW_TOSTRING_TIME = 0;

    public static void main(String[] args) {

        Scanner commandInput = new Scanner(System.in);
        System.out.print(WELCOME);

        int i = 0;
        do {
            System.out.println(MENU_HELP);

            switch (commandInput.next().toLowerCase()) {
                case CMD_GENERATE -> configurePasswords();
                case CMD_EXIT -> i = 1;
                default -> System.out.println(INVALID_CMD);
            }
        } while (i != 1);

        commandInput.close();
    }



    private static final char[] UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] LOWER = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] DIGITS = "0123456789".toCharArray();
    private static final char[] SPECIAL = "#*+-_!$&=?".toCharArray();


    static int validatePositiveInteger(String receivedInteger) {

        int trueInteger;
        try {
            trueInteger = Integer.parseInt(receivedInteger);
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + ERROR_POSITIVE_INTEGER + ANSI_RESET);
            return -1;
        }
        if (trueInteger <= 0) {
            System.out.println(ANSI_RED + ERROR_POSITIVE_INTEGER + ANSI_RESET);
            return -1;
        }
        return trueInteger;
    }



    static void configurePasswords() {
        int pwLength, pwCount, mode;
        Scanner input = new Scanner(System.in);

        System.out.print(ASK_LENGTH);
        pwLength = validatePositiveInteger(input.next());
        if (pwLength < 4 || pwLength > 16) {
            System.out.println(ANSI_RED + ERROR_LENGTH + ANSI_RESET);
            return;
        }

        System.out.print(ASK_COUNT);
        pwCount = validatePositiveInteger(input.next());
        if (pwCount <= 0) return;

        System.out.println(MODE_SELECTION);
        System.out.print(ASK_MODE);
        mode = validatePositiveInteger(input.next());
        if (mode != 1 && mode != 2) {
            System.out.println(ANSI_RED + ERROR_MODE + ANSI_RESET);
            return;
        }


        StringBuilder selectedChars = new StringBuilder();
        for (char c : UPPER) selectedChars.append(c);
        for (char c : LOWER) selectedChars.append(c);
        for (char c : SPECIAL) if (mode == 2) selectedChars.append(c);

        char[] charSet = selectedChars.toString().toCharArray();


        int requiredDigits = switch (pwLength) {
            case 4, 5, 6 -> 2;
            case 7, 8, 9, 10 -> 3;
            default -> 4;
        };

        System.out.println(passwordsToString(
                generatePasswords(pwLength, pwCount, charSet, requiredDigits)
        ));


        System.out.println("Es wurden " + pwCount + " Passwörter, jeweils " + pwLength + " Zeichen lang, generiert.\n"
                + "Dauer der Generierung: " + PW_GENERATED_TIME + " ms, Dauer der Konvertierung: " + PW_TOSTRING_TIME + " ms");
    }

    static char[][] generatePasswords(int pwLength, int pwCount, char[] charSet, int requiredDigits) {


        long start = System.currentTimeMillis();

        char[][] generatedPasswords = new char[pwLength][pwCount];
        Random rng = new Random();

        for (int i = 0; i < pwCount; i++) {
            int digitsPlaced = 0;
            int[] digitPositions = new int[requiredDigits];

            while (digitsPlaced < requiredDigits) {
                int pos = rng.nextInt(pwLength);
                boolean unique = true;
                for (int j = 0; j < digitsPlaced; j++) {
                    if (digitPositions[j] == pos) {
                        unique = false;
                        break;
                    }
                }
                if (unique) {
                    digitPositions[digitsPlaced++] = pos;
                }
            }

            for (int j = 0; j < pwLength; j++) {
                boolean isDigitPos = false;
                for (int d : digitPositions) {
                    if (d == j) {
                        isDigitPos = true;
                        break;
                    }
                }
                if (isDigitPos) {
                    generatedPasswords[j][i] = DIGITS[rng.nextInt(DIGITS.length)];
                } else {
                    generatedPasswords[j][i] = charSet[rng.nextInt(charSet.length)];
                }
            }
        }

        long end = System.currentTimeMillis();
        PW_GENERATED_TIME = end - start;
        return generatedPasswords;
    }

    static String passwordsToString(char[][] generatedPasswords) {

        long start = System.currentTimeMillis();
        int pwLength = generatedPasswords.length;
        int pwCount = generatedPasswords[0].length;
        StringBuilder sb = new StringBuilder(pwCount * (pwLength + 10));
        for (int i = 0; i < pwCount; i++) {
            sb.append((i + 1)).append(". Passwort: ");
            for (int j = 0; j < pwLength; j++) {
                sb.append(generatedPasswords[j][i]);
            }
            sb.append('\n');
        }
        long end = System.currentTimeMillis();
        PW_TOSTRING_TIME = end - start;
        return sb.toString();
    }
}