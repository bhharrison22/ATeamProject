package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class Quiz implements QuizADT {
	
	private ArrayList<Topic> currentTopics; // The list of current available topics
	int numQuestions;
	final String SAVED_QUESTION_FILE_PATH = "Saved_Questions.json";
	
	
	public Quiz() {
      currentTopics = new ArrayList<>();
      numQuestions = 0;
    }
	


  /**
   * Adds a question to the Quiz. If a given topic already exists in currentTopics, the new question
   * is added to that topic. Otherwise, a new TOpic object is created and added to the Quiz.
   */
  @Override
  public void addQuestion(String questionText, String answer, String[] options, String topic,
      String image) {
    Question newQuestion = new Question(questionText, answer, image, options, topic);
    for (Topic t : currentTopics) {
      if (t.toString().equals(newQuestion.getTopic())) { // Topic exists
        t.addQuestion(newQuestion);
        numQuestions++;
        return; // exits method
      }
    }
    // If topic doesn't exist:
    Topic newTopic = new Topic(topic);
    newTopic.addQuestion(newQuestion);
    currentTopics.add(newTopic);
    numQuestions++;
  }

  /**
   * Parses a given JSON file and adds the questions from it to the quiz
   * 
   * @param JSONfilePath is the file path to the JSON file being parsed
   */
  @Override
  public void loadQuestions(String JSONfilePath)
      throws FileNotFoundException, IOException, ParseException {
    JSONObject jo = (JSONObject) new JSONParser().parse(new FileReader(JSONfilePath));
    JSONArray questionArray = (JSONArray) jo.get("questionArray"); // JSONArray of questions in file
    for (int i = 0; i < questionArray.size(); i++) {
      JSONObject questionObj = (JSONObject) questionArray.get(i);
      String questionText = (String) questionObj.get("questionText"); // Question Text
      String topic = (String) questionObj.get("topic"); // Question Topic
      String image = (String) questionObj.get("image"); // Image path
      // Get options:
      JSONArray choiceArray = (JSONArray) questionObj.get("choiceArray");
      String answer = null; // Question Answer
      String[] options = new String[choiceArray.size()]; // Question choices
      for (int j = 0; j < choiceArray.size(); j++) {
        JSONObject choiceObj = (JSONObject) choiceArray.get(i);
        options[i] = (String) choiceObj.get("choice");
        if (choiceObj.get("isCorrect") == "T") {
          answer = (String) choiceObj.get("choice");
        }
      }
      // Adds question to current Quiz with info obtained from JSON object:
      this.addQuestion(questionText, answer, options, topic, image);
    }
  }

  /**
   * Writes all Questions currently stored into a JSON file.
   */
  @SuppressWarnings("unchecked")
  @Override
  public void save() {
    JSONArray questionList = new JSONArray(); // List of Questions stored as a JSONObject
    // Loops through all Questions being storage:
    for (Topic t : currentTopics) {
      for (Question q : t.getQuestions()) {
        JSONObject newQuestion = new JSONObject(); // JSON Object that stores all Question Fields
        newQuestion.put("questionText", q.getText());
        newQuestion.put("topic", q.getTopic());
        newQuestion.put("image", q.getImagePath());
        JSONArray choices = new JSONArray();
        for (String choice : q.getChoiceArray()) { // adds all choices to a JSONArray
          if (q.getAnswer() != null || q.getAnswer().equals(choice)) {
            JSONObject correct = new JSONObject();
            correct.put("isCorrect", "T");
            correct.put("choice", choice);
            choices.add(correct);
          } else {
            JSONObject inncorrect = new JSONObject();
            inncorrect.put("isCorrect", "F");
            inncorrect.put("choice", choice);
            choices.add(inncorrect);
          }
        }
        newQuestion.put("choiceArray", choices);
        questionList.add(newQuestion); // adds this Question to overall Array of Questions
      }
    }
    writeToFile(questionList);
  }

  /**
   * Writes information from a given JSONArray to a JSON file.
   * 
   * @param questions is a JSONArray which will be written to the file
   */
  private void writeToFile(JSONArray questions) {
    try {
      JSONObject masterObj = new JSONObject();
      masterObj.put("questionArray", questions);
      FileWriter file = new FileWriter(SAVED_QUESTION_FILE_PATH, false);
      file.write(masterObj.toJSONString());
      file.flush();
      file.close();
    } catch (IOException e) {
      e.printStackTrace(); // TODO: make some kind of dialog box that tells the user of error?
    }
  }

  /**
   * Randomly generates questions from selected topics. If numQuestions > the number of Questions
   * available, the rest of the Array will be filled with null.
   * 
   * @return array of randomly Questions
   */
  @Override
  public Question[] generateQuizQuestions(List<Topic> topics, int numQuestions) {
    Question[] quizQuestions = new Question[numQuestions];
    ArrayList<Question> allQuestions = new ArrayList<>();
    for (Topic t : topics) {
      for (Question q : t.getQuestions()) {
        allQuestions.add(q);
      }
    }
    for (int i = 0; i < numQuestions; i++) {
      if (!allQuestions.isEmpty()) {
        int randIndex = (int) (Math.random() * allQuestions.size());
        quizQuestions[i] = allQuestions.get(randIndex);
        allQuestions.remove(randIndex);
      } else {
        quizQuestions[i] = null;
      }
    }
    return quizQuestions;
  }

  /**
   * Takes in a list of a quiz's questions and the user's answers and generates a result
   */
  @Override
  public String grade(Question[] quizQuestions, String[] answers) {
    int correct = 0;
    for (int i = 0; i < quizQuestions.length; i++) {
      if (quizQuestions[i].getAnswer().equals(answers[i])) {
        correct++;
      }
    }
    return ("You Answered " + correct + " out of " + quizQuestions.length
        + " Questions, giving you a percentage of " + Math.round(correct / quizQuestions.length));
  }

  public int numQuestions() {
    return numQuestions;
  }
  
  public ArrayList<Topic> getTopics(){
    return currentTopics;
  }

  /**
   * Simply launches the program, see {@link Quiz#start(Stage)} for more interesting main-method
   * type shenanigans.
   * 
   * @param args The command line args
   */
  public static void main(String[] args) {
    // TODO: remove testing before submitting.
    // Testing:
    Quiz q1 = new Quiz();
    String[] options = {"answer", "Wrong1", "Wrong2", "Wrong3"};
    q1.addQuestion("Q1", "answer", options, "Test Questions", "");
    q1.addQuestion("Q2", "Wrong1", options, "Test Questions", "");
    q1.save();
    Quiz q2 = new Quiz();
    try {
      q2.loadQuestions("Saved_Questions.json");
      q2.loadQuestions("testfile.json");
      q2.addQuestion("After Load Q", "Wrong3", options, "loading", "");
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("Just Saved Questions:");
    Question[] qs = q2.generateQuizQuestions(q1.currentTopics, 6);
    for (Question q : qs) {
      if (q != null)
        System.out.println(q.getText());
      else
        System.out.println(q);
    }
    
    System.out.println("\nAll Questions: ");
    qs = q2.generateQuizQuestions(q2.currentTopics, 6);
    for (Question q : qs) {
      if (q != null)
        System.out.println(q.getText());
      else
        System.out.println(q);
    }
  }
}
