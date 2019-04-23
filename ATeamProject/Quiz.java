package ATeamProject;

import javafx.application.Application;
import javafx.stage.Stage;

public class Quiz extends Application implements QuizADT, QuizGUI {

	@Override
	public void mainScreen(Stage primaryStage) {
		// TODO ALEX DO DO THIS ONE
		
	}

	@Override
	public void topicChoosingPage(Stage primaryStage) {
		// TODO ALEX MOON DO THIS ONE
		
	}

	@Override
	public void takingQuizPage(Stage primaryStage) {
		// TODO DANIEL PORTNOV DO THIS ONE
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			mainScreen(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void addQuestion(String questionText, String answer, String[] options, String topic) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadQuestions(String JSONfilePath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public Question[] generateQuizQuestions(Topic[] topics, int numQuestions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] generateQuiz(Question[] quizQuestions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String grade(Question[] quizQuestions, String[] answers) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Simply launches the program, see {@link Quiz#start(Stage)}
	 * for more interesting main-method type shenanigans
	 * @param args The command line args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
