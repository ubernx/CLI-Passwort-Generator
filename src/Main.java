import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

public class Main {

    private static final int SKIP_INTEGERS = 10;
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    private static final String COMMAND_GENERATE = "/generate";
    private static final String COMMAND_EXIT = "/exit";
    private static final String COMMAND_ERROR = RED + " Befehl existiert nicht, geben Sie erneut den Befehl ein:" + RESET;
    private static final String REQUEST_PASSWORD_COUNT = "Bitte geben Sie die gewünschte Anzahl der Passworter an: ";
    private static final String REQUEST_PASSWORD_LENGTH = "Bitte geben Sie die gewünschte Passwort länge an: ";
    private static final String ERROR_NOT_AN_POSITIVE_INTEGER = RED + "Sie dürfen nur positive ganze Zahlen verwenden" + RESET;
    private static final String ERROR_OUT_OF_BOUND_PASSWORD_LENGTH = RED + "Sie haben den Zeichenlänge bereich <4-16> nicht eingehalten" + RESET;
    private static final String ERROR_MODE_TYPE = RED + "1 und 2 sind die einzigen Gültigen Modi" + RESET;
    private static final String PRINT_OR_SAVE_PASSWORDS = "Passwörter in Datei speichern? (J/N): ";
    private static final String WRITE_SUCCEEDED = "Passwörter befinden sich in: ";
    private static final String WRITE_FAILED = RED + "Failed to write config file:" + RESET;
    private static final String PASSWORDS_FILENAME = "pw.txt";
    private static final String LINEBREAK = "\n";
    private static final String J = "J";
    private static final String EMPTY_SPACEHOLDER = "";
    private static final String QUOTES = "''";
    private static final String NUMMERATOR = ". ";
    private static final String ALPHANUMMERICAL = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String SPECIAL = "#$%&'()*+,-./";
    private static final String REQUEST_PASSWORD_MODE = """
            Bitte wählen Sie den Modus:
            1: Groß- und Kleinbuchstaben + Zahlen
            2: Groß- und Kleinbuchstaben + Zahlen + Sonderzeichen
            """;
    private static final String MAIN_MENU = """
        Folgende Commands sind verfügbar:
        '/generate' (Generiert eine gewählte Anzahl von Passwörtern, mit der gewählten Länge und der gewählten Zeichenart)
        '/exit'     (Beendet und schließt das Programm)
        """;


    static Scanner parameters = new Scanner(System.in);
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {

            System.out.println(MAIN_MENU);
            String userInput = input.nextLine().toLowerCase();

            switch (userInput) {

                case COMMAND_GENERATE -> configurePasswordList();
                case COMMAND_EXIT     -> { input.close(); parameters.close(); System.exit(0); }
                default               -> System.out.println(QUOTES + userInput + QUOTES + COMMAND_ERROR);

            }

        }

    }

    static void exportPasswordList(String passwordList) {

        File file = new File(PASSWORDS_FILENAME);

        try (FileWriter writer = new FileWriter(file)) {

            writer.write(passwordList);
            System.out.println(WRITE_SUCCEEDED + file.getAbsolutePath());

        } catch (IOException e) {

            System.out.println(WRITE_FAILED + e.getMessage());

        }

    }

    static void configurePasswordList() {

        int pwMode, pwLength, pwCount, pwDigitCount = -1;
        String saveToFile, passwordList = EMPTY_SPACEHOLDER;

        System.out.print(REQUEST_PASSWORD_COUNT);
        pwCount = validatePositiveInteger(parameters.next());
        if (pwCount == -1) return;

        System.out.print(REQUEST_PASSWORD_LENGTH);
        pwLength = validatePositiveInteger(parameters.next());
        if (pwLength == -1) return;
        if (pwLength > 16 || pwLength < 4) {

            System.out.println(ERROR_OUT_OF_BOUND_PASSWORD_LENGTH);
            return;

        }

        switch (pwLength) {

            case 4, 5, 6                -> pwDigitCount = 2;
            case 7, 8, 9, 10            -> pwDigitCount = 3;
            case 11, 12, 13, 14, 15, 16 -> pwDigitCount = 4;

        }

        System.out.print(REQUEST_PASSWORD_MODE);
        pwMode = validatePositiveInteger(parameters.next());
        if (pwMode == -1) return;
        if (pwMode > 2 || pwMode < 1) {

            System.out.println(ERROR_MODE_TYPE);
            return;

        }

        for (int i = 0; i < pwCount; i++) {
            passwordList += (i + 1) + NUMMERATOR;
            passwordList += generator(pwDigitCount, pwLength, pwMode) + LINEBREAK;
        }


        System.out.print(PRINT_OR_SAVE_PASSWORDS);
        saveToFile = parameters.next().toUpperCase();

        if (saveToFile.equals(J)) {

            exportPasswordList(passwordList);

        } else {

            System.out.println(LINEBREAK + passwordList);

        }

    }

    static String generator(int pwDigitCount, int pwLength, int pwMode) {

        Random rng = new Random();

        String charSet = EMPTY_SPACEHOLDER, password = EMPTY_SPACEHOLDER;
        String isInteger;

        if (pwMode == 1) charSet = ALPHANUMMERICAL;
        if (pwMode == 2) charSet = ALPHANUMMERICAL + SPECIAL;

        for (int i = 0; i < pwLength; i++) {

            if (pwDigitCount > 0) {

                isInteger = EMPTY_SPACEHOLDER;
                isInteger += charSet.charAt(rng.nextInt(charSet.length()));

                if (isInteger.matches("\\d")) {

                    password += isInteger;
                    pwDigitCount--;

                } else {

                    password += isInteger;

                }

            } else {

                password += charSet.charAt(rng.nextInt(SKIP_INTEGERS, charSet.length()));

            }

        }

        return password;

    }

    static int validatePositiveInteger(String receivedInteger){

        int trueInteger;

        if (receivedInteger.matches("\\d+")) {

            trueInteger = Integer.parseInt(receivedInteger);

        } else {

            System.out.println(ERROR_NOT_AN_POSITIVE_INTEGER);
            return -1;

        }

        if (trueInteger <= 0) {

            System.out.println(ERROR_NOT_AN_POSITIVE_INTEGER);
            return -1;

        }

        return trueInteger;

    }


}
