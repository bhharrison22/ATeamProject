package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.parser.ParseException;

/**
 * A basic quiz ADT that uses all of the methods from the design doc
 * @author amoon
 */
public interface QuizADT {

	/**
	 * Adds a question to the question database
	 * @param question The text of the question
	 * @param answer The answer of the question
	 * @param options Question options
	 * @param topic The topic of the question
	 */
	public void addQuestion(String questionText, String answer, String[] options, String topic, String image);
	
	/**
	 * Loads questions into the program from a JSON file
	 * @param JSONfilePath The file path of the questions
	 */
	public void loadQuestions(String JSONfilePath) throws FileNotFoundException, IOException, ParseException;
	
	/**
	 * Saves all added questions into a JSON file
	 */
	public void save();
	
	/**
	 * Autogenerates a list of questions from a set of topics
	 * @param topics The set of topics
	 * @param numQuestions The number of questions to be generated
	 * @return The list of questions that have been randomly generated
	 */
	public Question[] generateQuizQuestions(Topic[] topics, int numQuestions);
	
	/**
	 * Generates a quiz from a set of questions
	 * @param quizQuestions The set of questions
	 * @return A list of strings that represent the questions
	 */
	public String[] generateQuiz(Question[] quizQuestions);
	
	/**
	 * Grades a set of answers based on a set of questions
	 * @param quizQuestions The set of questions
	 * @param answers The set of answers (pre: is same length as quizQuestions)
	 * @return A string that can be read to see the grade.
	 */
	public String grade(Question[] quizQuestions, String[] answers);
	
}
