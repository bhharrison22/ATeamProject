package ATeamProject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Question {
  // Fields
  private String text;
  private String topic;
  private String metadata; // purpose unkown. Delete?
  private String imagePath;
  private String[] choiceArray;
  private int answerIndex; // index of choiceArray the the correct answer
  
  public Question(String text, String topic, String metadata, String imagePath, String[] choiceArray, int answerIndex) {
    this.text = text;
    this.topic = topic;
    this.metadata = metadata;
    this.imagePath = imagePath;
    this.choiceArray = choiceArray;
    this.answerIndex = answerIndex;
  }
  
  /**
   * 
   * @return correct answer of the question
   */
  public String getCorrectAnswer() {
    return choiceArray[answerIndex];
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

  public String[] getChoiceArray() {
    return choiceArray;
  }
  
}
