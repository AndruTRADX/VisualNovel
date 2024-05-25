import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class VisualNovel {

    private static ArrayList<String> dialogs = new ArrayList<>();
    private static int currentDialogIndex = 0;
    private static boolean waitForEnter = true;
    private static List<Boolean> triviaResults = new ArrayList<>();
    private static Random random = new Random();
    private static int characterChoice = 0; // Para recordar qué personaje fue seleccionado
    private static List<Integer> availableCharacters = new ArrayList<>(); // Lista de personajes disponibles

    public static void main(String[] args) {
        loadDialogsFromTextFile("timelines/beginning.txt");

        Scanner scanner = new Scanner(System.in);

        while (currentDialogIndex < dialogs.size()) {
            String dialog = dialogs.get(currentDialogIndex);
            displayDialog(dialog, scanner);
            currentDialogIndex++;
        }

        // Crear trivia para cada personaje
        TriviaQuestions trivia1 = new TriviaQuestions();
        trivia1.addTrivia("What is Valeria's major?", new String[]{"Literature", "Economics", "Biology", "Computer Science"}, 1);
        trivia1.addTrivia("Who is Valeria's favorite author?", new String[]{"Shakespeare", "Tolstoy", "Hemingway", "Austen"}, 4);

        TriviaQuestions trivia2 = new TriviaQuestions();
        trivia2.addTrivia("What is Santiago's job?", new String[]{"Doctor", "Financial Advisor", "Engineer", "Artist"}, 2);
        trivia2.addTrivia("What is the stock market?", new String[]{"A place to buy stocks", "A place to buy groceries", "A place to buy clothes", "A place to buy furniture"}, 1);

        TriviaQuestions trivia3 = new TriviaQuestions();
        trivia3.addTrivia("What is Isabela's field?", new String[]{"Physics", "Chemistry", "Biology", "Mathematics"}, 3);
        trivia3.addTrivia("What does a biologist study?", new String[]{"Stars", "Chemicals", "Living organisms", "Numbers"}, 3);

        TriviaQuestions trivia4 = new TriviaQuestions();
        trivia4.addTrivia("What is Max studying?", new String[]{"Art", "Literature", "Computer Science", "History"}, 3);
        trivia4.addTrivia("What is a compiler?", new String[]{"A person who compiles data", "A tool that converts code to machine language", "A type of software bug", "A programming language"}, 2);

        // Inicializar lista de personajes disponibles
        for (int i = 1; i <= 4; i++) {
            availableCharacters.add(i);
        }

        boolean gameEnded = false;

        while (!gameEnded && !availableCharacters.isEmpty()) {
            // Seleccionar personaje y presentar trivia
            selectCharacterAndPresentTrivia(scanner, trivia1, trivia2, trivia3, trivia4);

            // Mostrar resultados de las trivias
            gameEnded = displayTriviaResults(scanner);

            // Eliminar el personaje seleccionado de las opciones disponibles
            availableCharacters.remove(Integer.valueOf(characterChoice));
        }

        // Verificar si el jugador perdió todas las historias
        if (!gameEnded && availableCharacters.isEmpty()) {
            loadDialogsFromTextFile("badending/finalending.txt");
            while (currentDialogIndex < dialogs.size()) {
                String dialog = dialogs.get(currentDialogIndex);
                displayDialog(dialog, scanner);
                currentDialogIndex++;
            }
        }

        scanner.close();
    }

    private static void loadDialogsFromTextFile(String fileName) {
        dialogs.clear();
        currentDialogIndex = 0;
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

    private static void selectCharacterAndPresentTrivia(Scanner scanner, TriviaQuestions trivia1, TriviaQuestions trivia2, TriviaQuestions trivia3, TriviaQuestions trivia4) {
        boolean validChoice = false;
        TriviaQuestions selectedTrivia = null;

        do {
            System.out.println("Select your story:");
            for (int character : availableCharacters) {
                switch (character) {
                    case 1:
                        System.out.println("1. Valeria Montenegro (Liberal Arts Student)");
                        break;
                    case 2:
                        System.out.println("2. Santiago Rivera (Financial Advisor)");
                        break;
                    case 3:
                        System.out.println("3. Isabela Vargas (Conservation biologist)");
                        break;
                    case 4:
                        System.out.println("4. Max García (Software engineering student)");
                        break;
                }
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (availableCharacters.contains(choice)) {
                switch (choice) {
                    case 1:
                        loadDialogsFromTextFile("characters/character1.txt");
                        selectedTrivia = trivia1;
                        characterChoice = 1;
                        validChoice = true;
                        break;
                    case 2:
                        loadDialogsFromTextFile("characters/character2.txt");
                        selectedTrivia = trivia2;
                        characterChoice = 2;
                        validChoice = true;
                        break;
                    case 3:
                        loadDialogsFromTextFile("characters/character3.txt");
                        selectedTrivia = trivia3;
                        characterChoice = 3;
                        validChoice = true;
                        break;
                    case 4:
                        loadDialogsFromTextFile("characters/character4.txt");
                        selectedTrivia = trivia4;
                        characterChoice = 4;
                        validChoice = true;
                        break;
                    default:
                        break;
                }
            } else {
                clearConsole();
                System.out.println("Invalid choice, please try again");
            }
        } while (!validChoice);

        // Presentar diálogos del personaje seleccionado
        while (currentDialogIndex < dialogs.size()) {
            String dialog = dialogs.get(currentDialogIndex);
            displayDialog(dialog, scanner);
            currentDialogIndex++;
        }

        // Presentar trivia seleccionada
        presentTrivia(selectedTrivia, scanner);
    }

    private static void presentTrivia(TriviaQuestions triviaQuestions, Scanner scanner) {
        for (TriviaQuestions.Trivia trivia : triviaQuestions.getTriviaList()) {
            presentTriviaQuestion(trivia.getQuestion(), trivia.getOptions(), trivia.getCorrectAnswer(), scanner);
        }
    }

    private static void presentTriviaQuestion(String question, String[] options, int correctAnswer, Scanner scanner) {
        System.out.println(question);
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.print("Enter your answer (1-" + options.length + "): ");
        int playerAnswer = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea después del número

        clearConsole();

        if (playerAnswer == correctAnswer) {
            System.out.println("Correct!");
            triviaResults.add(true); // Registrar respuesta correcta
        } else {
            System.out.println("Incorrect. The correct answer is: " + options[correctAnswer - 1]);
            triviaResults.add(false); // Registrar respuesta incorrecta
        }
    }

    private static boolean displayTriviaResults(Scanner scanner) {
        System.out.println("\nTrivia Results:");

        boolean selectedResult = selectRandomResult();
        System.out.println("Randomly selected result: " + (selectedResult ? "Correct" : "Incorrect"));

        if (selectedResult) {
            loadDialogsFromTextFile("goodending/character" + characterChoice + ".txt");
        } else {
            loadDialogsFromTextFile("badending/character" + characterChoice + ".txt");
        }

        // Mostrar el diálogo del final seleccionado
        while (currentDialogIndex < dialogs.size()) {
            String dialog = dialogs.get(currentDialogIndex);
            displayDialog(dialog, scanner);
            currentDialogIndex++;
        }

        if (selectedResult) {
            System.out.println("The game has ended. Thank you for playing!");
            return true;
        } else {
            System.out.println("You have reached a bad ending. Try again with another character.");
            return false;
        }
    }

    private static boolean selectRandomResult() {
        int totalWeight = 0;
        for (boolean result : triviaResults) {
            totalWeight += result ? 1 : 3;
        }

        int randomIndex = random.nextInt(totalWeight);
        int currentWeight = 0;

        for (boolean result : triviaResults) {
            currentWeight += result ? 1 : 3;
            if (randomIndex < currentWeight) {
                return result;
            }
        }
        return false; // Default case, should not be reached
    }

    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
