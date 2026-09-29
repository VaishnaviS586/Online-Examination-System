/*Question  */
public class Question {

    private String question;
    private String[] options;
    private int correctAnswer;

    // Constructor
    public Question(String question, String[] options, int correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    // Getter for question
    public String getQuestion() {
        return question;
    }

    // Setter for question
    public void setQuestion(String question) {
        this.question = question;
    }

    // Getter for options
    public String[] getOptions() {
        return options;
    }

    // Setter for options
    public void setOptions(String[] options) {
        this.options = options;
    }

    // Getter for correct answer
    public int getCorrectAnswer() {
        return correctAnswer;
    }

    // Setter for correct answer
    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
