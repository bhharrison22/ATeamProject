package application;

import java.util.ArrayList;
import java.util.List;

public class Topic {

	private String name;
	private List<Question> questionArray;
	
	public Topic(String name) {
		this.name = name;
		this.questionArray = new ArrayList<>();
	}
	
	public void addQuestion(Question newQuestion) {
	  questionArray.add(newQuestion); 
	}
	
	public List<Question> getQuestions() {
	  return questionArray;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
