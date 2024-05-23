import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class VisualNovel {

    private static ArrayList<String> dialogs = new ArrayList<>();
    private static int currentDialogIndex = 0;
    private static boolean waitForEnter = true;

    public static void main(String[] args) {
        loadDialogsFromTextFile("timelines/beginning.txt");

        Scanner scanner = new Scanner(System.in);

        while (currentDialogIndex < dialogs.size()) {
            String dialog = dialogs.get(currentDialogIndex);
            displayDialog(dialog, scanner);
            currentDialogIndex++;
        }

        selectCharacter(scanner);

        scanner.close();
    }

    private static void loadDialogsFromTextFile(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                dialogs.add(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayDialog(String dialog, Scanner scanner) {
        waitForEnter = true;

        Thread typingThread = new Thread(() -> typeText(dialog));
        typingThread.start();

        while (typingThread.isAlive()) {
            if (scanner.hasNextLine() && waitForEnter) {
                scanner.nextLine();
                typingThread.interrupt();
                break;
            }
        }

        waitForEnter = true;

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        clearConsole();
    }

    private static void typeText(String text) {
        try {
            for (char c : text.toCharArray()) {
                System.out.print(c);
                Thread.sleep(40);
            }
            System.out.println();
            waitForEnter = false;
            System.out.println("Press enter to continue");
        } catch (InterruptedException e) {
            clearConsole();
            System.out.println(text);
            waitForEnter = false;
            System.out.println("Press enter to continue");
        }
    }

    private static void selectCharacter(Scanner scanner) {
        boolean validChoice = false;

        do {
            System.out.println("Select your story:");
            System.out.println("1. Valeria Montenegro (Liberal Arts Student)");
            System.out.println("2. Santiago Rivera (Financial Advisor) ");
            System.out.println("3. Isabela Vargas (Conservation biologist)");
            System.out.println("4. Max García (Software engineering student)");
    
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            switch (choice) {
                case 1:
                    loadDialogsFromTextFile("characters/character1.txt");
                    validChoice = true;
                    break;
                case 2:
                    loadDialogsFromTextFile("characters/character2.txt");
                    validChoice = true;
                    break;
                case 3:
                    loadDialogsFromTextFile("characters/character3.txt");
                    validChoice = true;
                    break;
                case 4:
                    loadDialogsFromTextFile("characters/character4.txt");
                    validChoice = true;
                    break;
                default:
                    clearConsole();
                    System.out.println("Invalid choice, please try again");
                    break;
            }
        } while (!validChoice);

        if (currentDialogIndex < dialogs.size()) {
            String dialog = dialogs.get(currentDialogIndex);
            displayDialog(dialog, scanner);
            currentDialogIndex++;
        }
    }
    

    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
