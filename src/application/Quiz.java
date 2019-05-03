package application;

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

/**
 * Contains logic and functions for a Quiz. Can add and store questions from user input or a JSON
 * file and can save all questions to a JSON file.
 *
 */
public class Quiz implements QuizADT {

  private ArrayList<Topic> currentTopics; // The list of current available topics
  int numQuestions;

  /*
   * Main constructor for Quiz. Takes no parameters.
   */
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
        JSONObject choiceObj = (JSONObject) choiceArray.get(j);
        String choice = (String) choiceObj.get("choice");
        options[j] = choice;
        if (choiceObj.get("isCorrect").equals("T")) {
          answer = choice;
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
  public void save(String JSONfilePath) {
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
          if (q != null || q.getAnswer().equals(choice)) {
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
    writeToFile(questionList, JSONfilePath);
  }

  /**
   * Writes information from a given JSONArray to a JSON file.
   * 
   * @param questions is a JSONArray which will be written to the file
   */
  private void writeToFile(JSONArray questions, String JSONfilePath) {
    try {
      JSONObject masterObj = new JSONObject();
      masterObj.put("questionArray", questions);
      FileWriter file = new FileWriter(JSONfilePath, false);
      file.write(masterObj.toJSONString()); // writes sent questions in in JSON form o 
      file.flush();
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
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
    for (Topic t : topics) { // loops through all stored questions
      for (Question q : t.getQuestions()) {
        allQuestions.add(q);
      }
    }
    for (int i = 0; i < numQuestions; i++) {
      if (!allQuestions.isEmpty()) {
        int randIndex = (int) (Math.random() * allQuestions.size()); // gets random index
        quizQuestions[i] = allQuestions.get(randIndex);
        allQuestions.remove(randIndex);
      } else {
        quizQuestions[i] = null;
      }
    }
    return quizQuestions;
  }

  /**
   * 
   * @return number of questions currently stored
   */
  public int numQuestions() {
    return numQuestions;
  }

  /**
   * @return ArrayList containing all the topics (and therefore questions) currently being stored
   */
  public ArrayList<Topic> getTopics() {
    return currentTopics;
  }

}
