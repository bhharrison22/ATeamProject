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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class Quiz extends Application implements QuizADT, QuizGUI {
	
	private ArrayList<Topic> currentTopics; //The list of current available topics
	int numQuestions;
	final String SAVED_QUESTION_FILE_PATH = "Saved_Questions.json";
	
	public Quiz() {
      currentTopics = new ArrayList<>();
      numQuestions = 0;
    }
	
	@Override
	public void mainScreen(Stage primaryStage) {
	  // labels for respective sections of main page
	  Label enter = new Label("Enter Question");
	  Label load = new Label("Load Question");
	  Label numQ = new Label("Number of Current Questions");
	  
	  // textfields for user to enter questions and topic 
	  TextField topic = new TextField();
	  TextField question = new TextField();
	  TextField path = new TextField(); // path for JSON file
	  
	  // auxillary buttons for GUI
	  Button getImage = new Button("^");
	  Button saveQuestion = new Button("SAVE");
	  Button loadQ = new Button("LOAD");
	  Button next = new Button("SAVE AND NEXT");
	  next.setOnAction(e -> topicChoosingPage(primaryStage));
	  
	  // table containing questions (for now hardcoded)
	  TableView<String> table = new TableView<String>();
	  table.setEditable(true);
	  TableColumn<String, String> qCol = new TableColumn<String, String>("Questions");
	  table.getColumns().addAll(qCol);
	  
	  // root pane for the scene
	  VBox root = new VBox();
	  root.setPadding(new Insets(50, 20, 50, 20));
	  root.setSpacing(50);
	  root.setPrefSize(900, 300);
	  
	  // enter question
	  HBox enterQuestion = new HBox();
	  enterQuestion.setSpacing(10);
      enterQuestion.setPadding(new Insets(10, 20, 10, 20));
      enterQuestion.getChildren().addAll(enter, topic, question, getImage, saveQuestion, table);
      
      root.getChildren().addAll(enterQuestion);
      
      // load question
      HBox loadQuestion = new HBox();
      loadQuestion.setSpacing(10);
      loadQuestion.setPadding(new Insets(10, 20, 10, 20));
      loadQuestion.getChildren().addAll(load, path, loadQ);
      
      root.getChildren().addAll(loadQuestion);
      
      // bottom container
      HBox save = new HBox();
      save.setSpacing(10);
      save.setPadding(new Insets(10, 20, 10, 20));
      save.getChildren().addAll(numQ, next);
      
      root.getChildren().addAll(save);
      
      // set scene of this page
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.show();
      
	}

	@Override
	public void topicChoosingPage(Stage primaryStage) {
		VBox mainBox = new VBox(); //This layout will be vertical, so VBox
		mainBox.setPadding(new Insets(50, 20, 50, 20));
		mainBox.setSpacing(50);
		mainBox.setPrefSize(400, 600);
		ObservableList<Topic> selectedTopics = FXCollections.observableArrayList();
		
		//The stuff at the top, a drop down menu w a list of topics and a label
		HBox topBox = new HBox();
		topBox.setSpacing(10);
		topBox.setPadding(new Insets(10, 40, 10, 20));
		Label lbl1 = new Label("Choose topic: ");
		ObservableList<Topic> topic = FXCollections.observableArrayList();
		currentTopics.add(new Topic("Linux")); //Hard coded topic list, remove later
		currentTopics.add(new Topic("Hash Table"));
		//Sorts topics in alphabetical order
		currentTopics.sort((a, b) -> {
		  return a.toString().compareTo(b.toString());
		});
		if (currentTopics.isEmpty()) {
			topic.add(new Topic("No topics loaded"));
		} else {
			topic.addAll(currentTopics);
		}
		ComboBox<Topic> dropDown = new ComboBox<Topic>(topic);
		Button selectCurrentTopic = new Button("Select");
		
		//See end of midbox for action listener
		topBox.getChildren().addAll(lbl1, dropDown, selectCurrentTopic);
		mainBox.getChildren().add(topBox);
		
		//The stuff in the middle, a list of selected topics
		HBox midBox = new HBox();
		midBox.setSpacing(10);
		midBox.setPadding(new Insets(10, 40, 10, 20));
		
		if (selectedTopics.isEmpty()) {
			selectedTopics.add(new Topic("No topics selected"));
		}
		Label lbl2 = new Label("Selected topics: ");
		String tops = "";
		for (Topic t: selectedTopics) {
			tops += t.toString() + "\n";
		}
		Label selTops = new Label(tops);
		midBox.getChildren().addAll(lbl2, selTops);
		mainBox.getChildren().add(midBox);
		//Allows user to add topics to their selected list
		selectCurrentTopic.setOnAction(e -> {
          Topic selected = dropDown.getValue();
          //If some topic is selected...
          if (!selected.toString().equals("No topics selected")) {
            //Removes default text
            if (selectedTopics.toString().contains("No topics selected")) {
              selectedTopics.remove(0);
            }
            //Avoids duplicates
            if (!selectedTopics.contains(selected)) {
              selectedTopics.add(selected);
            }
            String tops1 = "";
            int numQuestions = 0;
            for (Topic t: selectedTopics) {
              tops1 += "" + t.getQuestions().size() + " " + t.toString() + "\n";
              numQuestions += t.getQuestions().size();
            }
            tops1 += "" + numQuestions + " total questions selected";
            //Updates the list of selected topics
            selTops.setText(tops1);
          }
        });
		
		//The section that lets you choose how many quiz questions you want
		HBox thirdBox = new HBox();
        thirdBox.setSpacing(10);
        thirdBox.setPadding(new Insets(10, 40, 10, 20));
        Label numQQ = new Label("Number of quiz questions:");
        //Creates a field that only accepts numbers
        TextField numQuizQuestions = new TextField();
        numQuizQuestions.setPrefWidth(40);
        numQuizQuestions.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        thirdBox.getChildren().addAll(numQQ, numQuizQuestions);
        mainBox.getChildren().add(thirdBox);
		
		//The stuff at the bottom, a forward and back button
		HBox bottomBox = new HBox();
		bottomBox.setPadding(new Insets(10, 20, 10, 20));
		bottomBox.setSpacing(20);
		
		Button back = new Button("Back");
		//Runs mainScreen method when user wants to go back
		back.setOnAction(e -> mainScreen(primaryStage)); 
		
		Button forward = new Button("Take Quiz");
		//Runs takingQuizPage when user is ready to take quiz
		forward.setOnAction(e -> takingQuizPage(primaryStage, 
		    generateQuizQuestions(selectedTopics, Integer.parseInt(numQuizQuestions.getText()))));
		
		//Adds functionality to remove the latest topic selected
		Button removeLatest = new Button("Remove last topic");
		removeLatest.setOnAction(e -> {
		  //If a topic has been selected...
		  if (!selTops.toString().contains("No topics selected")) {
		    //Remove latest selection
		    selectedTopics.remove(selectedTopics.size() - 1);
		    //Rebuild selected topics list
		    String tops1 = "";
		    for (Topic t: selectedTopics) {
		      tops1 += t.toString() + "\n";
		    }
		    //Avoids having an empty selection list by adding placeholder text
		    if (!tops1.equals("")) {
		      selTops.setText(tops1);
		    } else {
		      selTops.setText("No topics selected");
		    }
		    
		  }
		  });
		
		bottomBox.getChildren().addAll(back, forward, removeLatest);
		mainBox.getChildren().add(bottomBox);
		
		Scene scene = new Scene(mainBox);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void takingQuizPage(Stage primaryStage, Question[] questions) {
		VBox root = new VBox();
		root.setPadding(new Insets(50, 20, 50, 20));
		root.setSpacing(50);
		root.setPrefSize(700, 700);
		root.setAlignment(Pos.TOP_CENTER);
		
		Label lbl = new Label("Quiz");
	    lbl.setFont(Font.font("Helvetica", FontWeight.BOLD, 24));
	    
	    Label lbl1 = new Label("Who is this?");
	    
	    Image img = new Image("application/bucky.png");
	    
	    ImageView imageview = new ImageView();
        imageview.setImage(img);
        imageview.setFitWidth(100);
        imageview.setPreserveRatio(true);
        imageview.setSmooth(true);
        imageview.setCache(true);
	    
	    Label lbl2 = new Label("a) Bucky\nb) Deb\nc) Jerry Seinfeld\nd) IDK");
	    TextField txt = new TextField();
	    Label lbl3 = new Label("Correct!");
	    
	    txt.setOnAction(e -> {
	    	ObservableList<Node> children = root.getChildren();
			children.remove(lbl3);
	    	if (txt.getText() != null && txt.getText().equalsIgnoreCase("a")) {
	    		   children.add(lbl3);
	    	}
	    });
	    
	    root.getChildren().add(lbl);
	    root.getChildren().add(lbl1);
	    root.getChildren().add(imageview);
	    root.getChildren().add(lbl2);
	    root.getChildren().add(txt);
		
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
    currentTopics = new ArrayList<Topic>();
    try {
      primaryStage.setTitle("ATeam 79 Group Quiz Project");
      mainScreen(primaryStage);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
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
    JSONArray questionArray = (JSONArray) jo.get("questionArray"); // JSON array of questions in file
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
   * Writes all Questions currently stored into JSON file.
   */
  @SuppressWarnings("unchecked")
  @Override
  public void save() {
    JSONArray questionList = new JSONArray(); // List of Questions stored as JSONObject
    // Loops through all Questions being storage: 
    for(Topic t : currentTopics) {
      for(Question q : t.getQuestions()) {
        JSONObject newQuestion = new JSONObject(); // JSON Object that stores all Question Fields
        newQuestion.put("questionText", q.getText());
        newQuestion.put("topic", q.getTopic());
        newQuestion.put("image", q.getImagePath());
        JSONArray choices = new JSONArray();
        for(String choice : q.getChoiceArray()) { // adds all choices to JSONArray
          if(q.getAnswer().equals(choice)) {
            JSONObject correct =new JSONObject();
            correct.put("isCorrect", "T");
            correct.put("choice", choice);
            choices.add(correct);
          }
          else {
            JSONObject inncorrect =new JSONObject();
            inncorrect.put("isCorrect", "F");
            inncorrect.put("choice", choice);
            choices.add(inncorrect);
          }
        }
        newQuestion.put("choiceArray", choices);
        questionList.add(newQuestion); //adds Question to overall Array of Question
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
    // TODO: see if there's a more efficient way to do this
    Question[] quizQuestions = new Question[numQuestions];
    ArrayList<Question> allQuestions = new ArrayList<>();
    for (Topic t : topics) {
      for (Question q : t.getQuestions()) {
        allQuestions.add(q);
      }
    }
    for (int i = 0; i < numQuestions; i++) {
      if (!allQuestions.isEmpty()) {
        int randIndex = (int) Math.random() * allQuestions.size();
        quizQuestions[i] = allQuestions.get(randIndex);
        allQuestions.remove(randIndex);
      } else {
        quizQuestions[i] = null;
      }
    }
    return quizQuestions;
  }

  @Override
  public String[] generateQuiz(Question[] quizQuestions) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Takes in a list of a quiz's questions and the user's answers and generates a result
   */
  @Override
  public String grade(Question[] quizQuestions, String[] answers) {
    int correct = 0;
    for(int i=0;i<quizQuestions.length;i++) {
      if(quizQuestions[i].getAnswer().equals(answers[i])) {
        correct++;
      }
    }
    return ("You Answered " + correct + " out of " + quizQuestions.length
        + " Questions, giving you a percentage of " + Math.round(correct / quizQuestions.length));
  }
  
  public int numQuestions() {
    return numQuestions;
  }

  /**
   * Simply launches the program, see {@link Quiz#start(Stage)} for more interesting main-method
   * type shenanigans.
   * 
   * @param args The command line args
   */
  public static void main(String[] args) {
    launch(args);
    // TODO: remove testing before submitting.
    // Testing: 
    Quiz q1 = new Quiz();
    String[] options = {"answer", "Wrong1", "Wrong2", "Wrong3"};
    q1.addQuestion("Q1", "answer", options, "Test Questions", "");
    q1.addQuestion("Q2", "Wrong1", options, "Test Questions", "");
    Question[] qs = q1.generateQuizQuestions(q1.currentTopics, 3);
    for (Question q : qs) {
      if (q != null)
        System.out.println(q.getText());
      else
        System.out.println(q);
    }
    q1.save();
  }

}
