import java.util.ArrayList;

public class TriviaQuestions {

    private ArrayList<Trivia> triviaList;

    public TriviaQuestions() {
        this.triviaList = new ArrayList<>();
    }

    public void addTrivia(String question, String[] options, int correctAnswer) {
        Trivia trivia = new Trivia(question, options, correctAnswer);
        triviaList.add(trivia);
    }

    public ArrayList<Trivia> getTriviaList() {
        return triviaList;
    }

    public static class Trivia {
        private String question;
        private String[] options;
        private int correctAnswer;

        public Trivia(String question, String[] options, int correctAnswer) {
            this.question = question;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }

        public String getQuestion() {
            return question;
        }

        public String[] getOptions() {
            return options;
        }

        public int getCorrectAnswer() {
            return correctAnswer;
        }
    }
}
