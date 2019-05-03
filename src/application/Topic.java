package application;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which contains all the info for a given topic including all Questions that fall under that
 * topic.
 *
 */
public class Topic {

  private String name;
  private List<Question> questionArray;

  /**
   * Main constructor for Topic. Takes a name parameter and initializes Question storage.
   * 
   * @param name is the name of the Topic
   */
  public Topic(String name) {
    this.name = name;
    this.questionArray = new ArrayList<>();
  }

  /**
   * Adds a Question to be stored under the Topic
   * 
   * @param newQuestion
   */
  public void addQuestion(Question newQuestion) {
    questionArray.add(newQuestion);
  }

  /**
   * @return a List of all Questions under a Topic
   */
  public List<Question> getQuestions() {
    return questionArray;
  }

  /**
   * @return The name of the Topic
   */
  @Override
  public String toString() {
    return name;
  }
}
