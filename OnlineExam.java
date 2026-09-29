import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class OnlineExam {
    JFrame frame;
    JPanel mainPanel;

    ArrayList<Student> students = new ArrayList<>();
    ArrayList<Question> questions = new ArrayList<>();
    ArrayList<Integer> selectedAnswers = new ArrayList<>();

    Student currentStudent;

    int currentQuestion = 0;
    int timeLeft = 60;

    Timer timer;

    JLabel questionNumberLabel;
    JLabel questionLabel;
    JLabel timerLabel;

    JRadioButton optionA;
    JRadioButton optionB;
    JRadioButton optionC;
    JRadioButton optionD;

    ButtonGroup optionGroup;

    JButton previousButton;
    JButton nextButton;
    JButton submitButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OnlineExam());
    }

    public OnlineExam() {
        frame = new JFrame("Online Examination System");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(new Color(245, 245, 245));

        frame.add(mainPanel);

        loadStudents();
        loadQuestions();

        showWelcomeScreen();

        frame.setVisible(true);
    }

    private void showWelcomeScreen() {
        stopTimer();

        mainPanel.removeAll();

        JPanel panel = new JPanel(
                new BorderLayout(20, 20));

        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel title = new JLabel("ONLINE EXAMINATION SYSTEM",SwingConstants.CENTER);

        title.setFont(new Font("Arial", Font.BOLD, 26));

        title.setForeground(new Color(30, 60, 100));

        JLabel instructions = new JLabel(
                "<html><center>" +
                        "Welcome to the Online Examination System<br><br>" +
                        "Total Questions: 10<br>" +
                        "Total Time: 60 Seconds<br>" +
                        "Please register before starting the examination." +
                        "</center></html>",
                SwingConstants.CENTER);

        instructions.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton startButton =new JButton("Student Registration");

        JButton exitButton =new JButton("Exit");

        startButton.setFont(new Font("Arial", Font.BOLD, 15));

        exitButton.setFont(new Font("Arial", Font.BOLD, 15));

        startButton.setPreferredSize(new Dimension(220, 45));

        exitButton.setPreferredSize(new Dimension(220, 45));

        JPanel buttons = new JPanel(new GridLayout(2, 1, 10, 10));

        buttons.setBackground(Color.WHITE);

        buttons.add(startButton);
        buttons.add(exitButton);

        panel.add(title, BorderLayout.NORTH);
        panel.add(instructions, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        mainPanel.add(panel);

        refresh();

        startButton.addActionListener(e -> showRegistrationScreen());

        exitButton.addActionListener(e -> System.exit(0));
    }

    private void showRegistrationScreen() {
        stopTimer();

        mainPanel.removeAll();

        JPanel panel = new JPanel(
                new BorderLayout(20, 20));

        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        JLabel title = new JLabel("STUDENT REGISTRATION",SwingConstants.CENTER);

        title.setFont(new Font("Arial", Font.BOLD, 26));

        title.setForeground(new Color(30, 60, 100));

        JPanel form = new JPanel(new GridBagLayout());

        form.setBackground(Color.WHITE);

        GridBagConstraints gbc =new GridBagConstraints();

        gbc.insets =new Insets(8, 8, 8, 8);

        gbc.anchor =GridBagConstraints.CENTER;

        JLabel nameLabel =new JLabel("Student Name:");

        JLabel idLabel =new JLabel("Student ID:");

        JLabel emailLabel =new JLabel("Email:");

        JTextField nameField =new JTextField(25);

        JTextField idField =new JTextField(25);

        JTextField emailField =new JTextField(25);

        nameField.setFont(new Font("Arial", Font.PLAIN, 15));

        idField.setFont(new Font("Arial", Font.PLAIN, 15));

        emailField.setFont(new Font("Arial", Font.PLAIN, 15));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;

        form.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        form.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;

        form.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        form.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;

        form.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        form.add(emailField, gbc);

        JButton registerButton =new JButton("Register & Start");

        JButton backButton =new JButton("Back");

        JPanel buttons = new JPanel();

        buttons.setBackground(Color.WHITE);

        buttons.add(backButton);
        buttons.add(registerButton);

        panel.add(title,BorderLayout.NORTH);
        panel.add(form,BorderLayout.CENTER);
        panel.add(buttons,BorderLayout.SOUTH);
        mainPanel.add(panel);

        refresh();

        registerButton.addActionListener(e -> {

            String name =nameField.getText().trim();
            String studentId =idField.getText().trim();
            String email =emailField.getText().trim();
            if (name.isEmpty() ||studentId.isEmpty() ||email.isEmpty()) {
                JOptionPane.showMessageDialog(frame,"Please fill all the fields.");
                return;
            }
            if (studentIdExists(studentId)) {

                JOptionPane.showMessageDialog(frame,"Student ID already exists!");

                return;
            }

            currentStudent =new Student(name,studentId,email);
            students.add(currentStudent);
            saveStudents();

            JOptionPane.showMessageDialog(frame,"Registration successful!");

            startExam();
        });

        backButton.addActionListener(e -> showWelcomeScreen());
    }

    private boolean studentIdExists(String studentId) {

        for (Student student : students) {

            if (student.getStudentId().equalsIgnoreCase(studentId)) {

                return true;
            }
        }

        return false;
    }

    private void loadStudents() {

        File file =new File("students.txt");

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =new BufferedReader(new FileReader(file))) {

            String line;

            while ((line =reader.readLine()) != null) {

                String[] data =line.split("\\|");

                if (data.length == 3) {

                    Student student = new Student(data[0],data[1],data[2]);

                    students.add(student);
                }
            }

        } catch (IOException e) {

            JOptionPane.showMessageDialog(frame,"Error loading student data.");
        }
    }

    private void saveStudents() {

        try (PrintWriter writer =new PrintWriter(new FileWriter("students.txt"))) {

            for (Student student : students) {

                writer.println(
                        student.getName() +
                                "|" +
                                student.getStudentId() +
                                "|" +
                                student.getEmail());
            }

        } catch (IOException e) {

            JOptionPane.showMessageDialog(frame,"Error saving student data.");
        }
    }

    private void loadQuestions() {

        questions.clear();

        questions.add(new Question(
                "Which language is commonly used for Android application development?",
                new String[]{
                        "Python",
                        "Java",
                        "HTML",
                        "SQL"
                },
                1));

        questions.add(new Question(
                "Which keyword is used to create a class in Java?",
                new String[]{
                        "class",
                        "struct",
                        "define",
                        "object"
                },
                0));

        questions.add(new Question(
                "Which collection is used to create a dynamic list in Java?",
                new String[]{
                        "ArrayList",
                        "Stack",
                        "Queue",
                        "Tree"
                },
                0));

        questions.add(new Question(
                "Which Swing component is used for single-line text input?",
                new String[]{
                        "JTextArea",
                        "JTextField",
                        "JLabel",
                        "JButton"
                },
                1));

        questions.add(new Question(
                "Which method is the entry point of a Java program?",
                new String[]{
                        "start()",
                        "run()",
                        "main()",
                        "execute()"
                },
                2));

        questions.add(new Question(
                "Which Swing component represents a button?",
                new String[]{
                        "JButton",
                        "JLabel",
                        "JTextField",
                        "JPanel"
                },
                0));

        questions.add(new Question(
                "Which keyword is used for inheritance in Java?",
                new String[]{
                        "implements",
                        "extends",
                        "inherits",
                        "super"
                },
                1));

        questions.add(new Question(
                "Which data type stores true or false values?",
                new String[]{
                        "int",
                        "String",
                        "boolean",
                        "double"
                },
                2));

        questions.add(new Question(
                "Which keyword is used to create an object in Java?",
                new String[]{
                        "class",
                        "new",
                        "object",
                        "create"
                },
                1));

        questions.add(new Question(
                "Which class is commonly used to display a message dialog in Swing?",
                new String[]{
                        "JDialog",
                        "JOptionPane",
                        "JMessage",
                        "JFrame"
                },
                1));
    }

    private void startExam() {

        selectedAnswers.clear();

        for (int i = 0;i < questions.size();i++) {

            selectedAnswers.add(-1);
        }

        currentQuestion = 0;
        timeLeft = 60;

        createExamGUI();
        displayQuestion();
        startTimer();
    }

    private void createExamGUI() {

        stopTimer();

        mainPanel.removeAll();

        JPanel examPanel =new JPanel(new BorderLayout( 15, 15));

        examPanel.setBackground(Color.WHITE);

        examPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel top =new JPanel(new BorderLayout());

        top.setBackground(Color.WHITE);

        questionNumberLabel =new JLabel();

        questionNumberLabel.setFont(new Font("Arial",Font.BOLD,18));

        questionNumberLabel.setForeground(new Color(  30, 60, 100));

        timerLabel =new JLabel("Time: 60",SwingConstants.RIGHT);

        timerLabel.setFont(new Font("Arial",Font.BOLD,18));

        top.add(questionNumberLabel,BorderLayout.WEST);

        top.add(timerLabel,BorderLayout.EAST);

        JPanel questionPanel =new JPanel(new BorderLayout(10, 20));

        questionPanel.setBackground( Color.WHITE);

        questionPanel.setBorder(BorderFactory.createLineBorder(new Color(  210, 210, 210)));

        questionLabel =new JLabel();

        questionLabel.setFont(new Font("Arial",Font.BOLD,17));

        questionLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        questionPanel.add(questionLabel,BorderLayout.NORTH);

        optionA = createRadioButton();

        optionB = createRadioButton();

        optionC = createRadioButton();

        optionD = createRadioButton();

        optionGroup = new ButtonGroup();
 
        optionGroup.add(optionA);
        optionGroup.add(optionB);
        optionGroup.add(optionC);
        optionGroup.add(optionD);

        JPanel options =new JPanel(new GridLayout(4, 1, 8, 8));
        options.setBackground(Color.WHITE);
        options.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        options.add(optionA);
        options.add(optionB);
        options.add(optionC);
        options.add(optionD);

        questionPanel.add(options,BorderLayout.CENTER);
        previousButton =new JButton("Previous");
        nextButton =new JButton("Next");
        submitButton =new JButton("Submit");
        JPanel buttons =new JPanel(new FlowLayout(FlowLayout.CENTER,15,5));

        buttons.setBackground(Color.WHITE);

        buttons.add(previousButton);
        buttons.add(nextButton);
        buttons.add(submitButton);

        examPanel.add(top,BorderLayout.NORTH);
        examPanel.add(questionPanel,BorderLayout.CENTER);
        examPanel.add(buttons,BorderLayout.SOUTH);
        mainPanel.add(examPanel);

        refresh();

        previousButton.addActionListener( e -> previousQuestion());
        nextButton.addActionListener( e -> nextQuestion());
        submitButton.addActionListener(e -> {

            int choice =
                    JOptionPane.showConfirmDialog(
                            frame,
                            "Are you sure you want to submit?",
                            "Confirm Submission",
                            JOptionPane.YES_NO_OPTION);

            if (choice ==JOptionPane.YES_OPTION) 
                {
                        submitExam();
                }
        });
    }

    private JRadioButton createRadioButton() {

        JRadioButton radioButton =new JRadioButton();

        radioButton.setFont(new Font("Arial",Font.PLAIN,15));

        radioButton.setBackground(Color.WHITE);

        return radioButton;
    }

    private void displayQuestion() {

        Question question =questions.get( currentQuestion);
        questionNumberLabel.setText("Question " +(currentQuestion + 1) +" of " +questions.size());
        questionLabel.setText(
                "<html>" +
                        question.getQuestion() +
                        "</html>");

        String[] options = question.getOptions();

        optionA.setText("A. " + options[0]);

        optionB.setText("B. " + options[1]);

        optionC.setText("C. " + options[2]);

        optionD.setText("D. " + options[3]);

        optionGroup.clearSelection();

        int selected =selectedAnswers.get(currentQuestion);

        if (selected == 0) {

            optionA.setSelected(true);

        } else if (selected == 1) {

            optionB.setSelected(true);

        } else if (selected == 2) {

            optionC.setSelected(true);

        } else if (selected == 3) {

            optionD.setSelected(true);
        }

        previousButton.setEnabled(currentQuestion > 0);
        nextButton.setEnabled(currentQuestion < questions.size() - 1);

        refresh();
    }

    private void saveCurrentAnswer() {

        int selected = -1;

        if (optionA.isSelected()) {

            selected = 0;

        } else if (optionB.isSelected()) {

            selected = 1;

        } else if (optionC.isSelected()) {

            selected = 2;

        } else if (optionD.isSelected()) {

            selected = 3;
        }

        selectedAnswers.set(currentQuestion,selected);
    }

    private void nextQuestion() {

        saveCurrentAnswer();

        if (currentQuestion <
                questions.size() - 1) {

            currentQuestion++;

            displayQuestion();
        }
    }

    private void previousQuestion() {

        saveCurrentAnswer();

        if (currentQuestion > 0) {

            currentQuestion--;

            displayQuestion();
        }
    }

    private void startTimer() {

        stopTimer();

        timeLeft = 60;

        timerLabel.setText("Time: " + timeLeft);

        timer =
                new Timer(
                        1000,
                        e -> {

                            timeLeft--;

                            timerLabel.setText("Time: " +timeLeft);

                            if (timeLeft <= 0) {

                                stopTimer();

                                saveCurrentAnswer();

                                JOptionPane.showMessageDialog(
                                        frame,
                                        "Time is over!\n" +
                                                "The exam will be submitted.",
                                        "Time Over",
                                        JOptionPane.INFORMATION_MESSAGE);

                                submitExam();
                            }
                        });

        timer.start();
    }

    private void stopTimer() {

        if (timer != null) {

            timer.stop();

            timer = null;
        }
    }

    private void submitExam() {

        stopTimer();

        saveCurrentAnswer();

        int correct = 0;
        int wrong = 0;

        for (int i = 0;i < questions.size();i++) {

            int selected =
                    selectedAnswers.get(i);

            int correctAnswer =
                    questions.get(i)
                            .getCorrectAnswer();

            if (selected == -1) {

                continue;

            } else if (selected ==
                    correctAnswer) {

                correct++;

            } else {

                wrong++;
            }
        }

        int total =questions.size();

        int unanswered =total - correct - wrong;

        double percentage =((double) correct /total) * 100;

        String result;

        if (percentage >= 40) {

            result = "PASS";

        } else {

            result = "FAIL";
        }

        showResult(
                total,
                correct,
                wrong,
                unanswered,
                percentage,
                result);
    }

    private void showResult(
            int total,
            int correct,
            int wrong,
            int unanswered,
            double percentage,
            String result) {

        String message =
                "EXAMINATION RESULT\n\n" +

                "Student Name    : " +
                currentStudent.getName() +
                "\n" +

                "Student ID      : " +
                currentStudent.getStudentId() +
                "\n" +

                "Email           : " +
                currentStudent.getEmail() +
                "\n\n" +

                "Total Questions : " +
                total +
                "\n" +

                "Correct Answers : " +
                correct +
                "\n" +

                "Wrong Answers   : " +
                wrong +
                "\n" +

                "Unanswered      : " +
                unanswered +
                "\n" +

                String.format(
                        "Percentage      : %.2f%%\n",
                        percentage) +

                "Result          : " +
                result;

        JOptionPane.showMessageDialog(
                frame,
                message,
                "Exam Result",
                JOptionPane.INFORMATION_MESSAGE);

        int choice =
                JOptionPane.showConfirmDialog(
                        frame,
                        "Do you want to take the exam again?",
                        "Online Examination System",
                        JOptionPane.YES_NO_OPTION);

        if (choice ==JOptionPane.YES_OPTION) {

            startExam();

        } else {

            showWelcomeScreen();
        }
    }

    private void refresh() {

        mainPanel.revalidate();
        mainPanel.repaint();
    }
}