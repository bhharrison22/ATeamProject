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
