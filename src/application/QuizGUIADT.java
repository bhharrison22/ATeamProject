package application;

import javafx.stage.Stage;

/**
 * Interface blueprinting the Quiz Generator GUI
 * @author AlexDo
 *
 */
public interface QuizGUIADT {

	/**
	 * The start page of the application.
	 * Questions can be created here and JSON Files can be loaded
	 * @param primaryStage The primary stage
	 */
	public void mainScreen(Stage primaryStage);
	
	/**
	 * The page after user presses "Add Question" in the main screen,
	 * where user can fill out text fields and forms manually.
	 * @param primaryStage
	 */
	public void addQuestionPage(Stage primaryStage);
	
	/**
	 * The page after user presses "Load Question" in the main screen,
	 * where user can enter in a file path to load questions into the Quiz
	 * from a JSON file.
	 * @param primaryStage
	 */
	public void loadQuestionPage(Stage primaryStage);
	
	/**
	 * The page after user presses next in the main screen,
	 * where topics are chosen from the list of available 
	 * topics to start generating a quiz.
	 * @param primaryStage The primary stage
	 */
	public void topicChoosingPage(Stage primaryStage);
	
	/**
	 * The page where the quiz is actually taken.
	 * @param primaryStage The primary stage
	 */
	public void takingQuizPage(Stage primaryStage, Question[] questions);
	
	/**
	 * Exit page prompting user to exit saving questions in quiz or exit without saving.
	 */
	public void exitPage();
	
}
