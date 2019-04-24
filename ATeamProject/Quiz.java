package ATeamProject;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Quiz extends Application implements QuizADT, QuizGUI {

	// The dimensions of the window, useful for standardizing size
	private final int xDim = 400;
	private final int yDim = 300;
	
	private ArrayList<Topic> currentTopics; //The list of current available topics
	
	@Override
	public void mainScreen(Stage primaryStage) {
	  // TODO ALEX DO DO THIS ONE
	  
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
	  root.setPrefSize(xDim, yDim);
	  
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
		mainBox.setPrefSize(xDim, yDim);
		
		//The stuff at the top, a drop down menu w a list of topics and a label
		HBox topBox = new HBox();
		topBox.setSpacing(10);
		topBox.setPadding(new Insets(10, 20, 10, 20));
		Label lbl1 = new Label("Choose topic: ");
		ObservableList<Topic> topic = FXCollections.observableArrayList();
		if (currentTopics.isEmpty()) {
			topic.add(new Topic("No topics loaded"));
		} else {
			topic.addAll(currentTopics);
		}
		ComboBox<Topic> dropDown = new ComboBox<Topic>(topic);
		topBox.getChildren().addAll(lbl1, dropDown);
		mainBox.getChildren().add(topBox);
		
		//The stuff in the middle, a list of selected topics
		HBox midBox = new HBox();
		midBox.setSpacing(10);
		midBox.setPadding(new Insets(10, xDim / 8, 10, xDim / 8));
		ObservableList<Topic> selectedTopics = FXCollections.observableArrayList();
		if (selectedTopics.isEmpty()) {
			selectedTopics.add(new Topic("No topics selected"));
		} else {
			//TODO add a way to select topics
		}
		Label lbl2 = new Label("Selected topics: ");
		String tops = "";
		for (Topic t: selectedTopics) {
			tops += t.toString() + "\n";
		}
		Label selTops = new Label(tops);
		midBox.getChildren().addAll(lbl2, selTops);
		mainBox.getChildren().add(midBox);
		
		//The stuff at the bottom, a forward and back button
		HBox bottomBox = new HBox();
		bottomBox.setPadding(new Insets(10, xDim / 4, 10, xDim / 4));
		bottomBox.setSpacing(20);
		Button back = new Button("Back");
		//Runs mainScreen method when user wants to go back
		back.setOnAction(e -> mainScreen(primaryStage)); 
		Button forward = new Button("Ready");
		//Runs takingQuizPage when user is ready to take quiz
		forward.setOnAction(e -> takingQuizPage(primaryStage));
		bottomBox.getChildren().addAll(back, forward);
		mainBox.getChildren().add(bottomBox);
		
		Scene scene = new Scene(mainBox);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void takingQuizPage(Stage primaryStage) {
		// TODO DANIEL PORTNOV DO THIS ONE
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

	@Override
	public void addQuestion(String questionText, String answer, String[] options, String topic) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadQuestions(String JSONfilePath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public Question[] generateQuizQuestions(Topic[] topics, int numQuestions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] generateQuiz(Question[] quizQuestions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String grade(Question[] quizQuestions, String[] answers) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Simply launches the program, see {@link Quiz#start(Stage)}
	 * for more interesting main-method type shenanigans
	 * @param args The command line args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
