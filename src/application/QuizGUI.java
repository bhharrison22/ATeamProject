package application;

import javafx.stage.Stage;

public interface QuizGUI {

	/**
	 * The start page of the application.
	 * Questions can be created here and JSON Files can be loaded
	 * @param primaryStage The primary stage
	 */
	public void mainScreen(Stage primaryStage);
	
	/**
	 * The second page, where topics are chosen from the list
	 * of available topics to start generating a quiz.
	 * @param primaryStage The primary stage
	 */
	public void topicChoosingPage(Stage primaryStage);
	
	/**
	 * The page where the quiz is actually taken.
	 * @param primaryStage The primary stage
	 */
	public void takingQuizPage(Stage primaryStage, Question[] questions);
	
}
