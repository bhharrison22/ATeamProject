package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class which contains a Question and all necessary information required to generate a quiz.
 *
 */
public class Question {
  // Fields
  private String text;
  private String topic;
  private String imagePath;
  private String[] choiceArray;
  private String answer;

  /**
   * Main constuctor for Question
   * 
   * @param text        is the text of the Question
   * @param answer      is the answer of the Question
   * @param imagePath   is the path to the image of the Question
   * @param choiceArray is an array of choices, one of which is the answer
   * @param topic       is the topic of the Question
   */
  public Question(String text, String answer, String imagePath, String[] choiceArray,
      String topic) {
    this.text = text;
    this.topic = topic;
    this.imagePath = imagePath;
    this.choiceArray = choiceArray;
    this.answer = answer;
  }

  // Getter Methods:

  public String getText() {
    return text;
  }

  public String getTopic() {
    return topic;
  }

  public String getImagePath() {
    return imagePath;
  }

  public String getAnswer() {
    return answer;
  }

  public String[] getChoiceArray() {
    return choiceArray;
  }
}
