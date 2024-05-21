import java.util.List;
import java.util.Scanner;

public class VisualNovel {

    private static final List<String> dialogs = List.of(
            "Hello, welcome to our visual novel.",
            "I hope you enjoy the story.",
            "Let's begin the adventure!");

    private static int currentDialogIndex = 0;
    private static boolean waitForEnter = true;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (currentDialogIndex < dialogs.size()) {
            String dialog = dialogs.get(currentDialogIndex);
            displayDialog(dialog, scanner);
            currentDialogIndex++;
        }

        scanner.close();
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
                Thread.sleep(50);
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

    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
