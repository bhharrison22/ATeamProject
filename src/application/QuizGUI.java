package application;

import java.io.File;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class QuizGUI extends Application implements QuizGUIADT {

	private Quiz quiz;
	private Label counter = new Label("0");
	private final String SAVED_QUESTION_FILE_PATH = "Saved_Questions.json";
	private int questionsAnswered = 0;

	/**
	 * The start page of the application. Questions can be created here and JSON
	 * Files can be loaded
	 * 
	 * @param primaryStage The primary stage
	 */
	@Override
	public void mainScreen(Stage primaryStage) {
		// Labels for the page
		Label welcome = new Label("WELCOME TO QUIZ GENERATOR");
		Label numQues = new Label("Number of Questions in Quiz: ");
		Label questionsAdded = new Label("");
		// counter will update every time user adds questions to the database
		// either via addQuestionPage or the loadQuestionPage so that
		// counter will always display the correct number of questions in the database
		this.counter.setText(this.quiz.numQuestions() + "");

		// the HBox containing the welcome label
		HBox welcomeLabel = new HBox();
		welcomeLabel.setPadding(new Insets(50));
		welcomeLabel.setAlignment(Pos.CENTER);
		welcomeLabel.getChildren().add(welcome);

		// the GridPane containing the buttons for the different landing pages
		// plus setup and configurations
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setMinSize(200, 200);
		grid.setVgap(20);
		grid.setHgap(20);
		grid.setAlignment(Pos.CENTER);

		// HBox indicating questions have been saved
		HBox saved = new HBox();
		saved.setPadding(new Insets(10, 10, 10, 10));
		saved.setAlignment(Pos.CENTER);
		saved.getChildren().add(questionsAdded);

		// Buttons for the page with standard widths
		Button add = new Button("Add Question");
		add.setMaxWidth(150);
		Button load = new Button("Load Question");
		load.setMaxWidth(150);
		Button save = new Button("Save Questions");
		save.setMaxWidth(150);
		Button next = new Button("Make Quiz");
		next.setMaxWidth(150);

		// button actions events upon pressing
		add.setOnAction(e -> addQuestionPage(primaryStage));
		load.setOnAction(e -> loadQuestionPage(primaryStage));
		save.setOnAction(e -> {
			quiz.save(SAVED_QUESTION_FILE_PATH);
			questionsAdded.setTextFill(Color.web("#FF0000"));
			questionsAdded.setText("All questions (" + this.quiz.numQuestions() + ") saved to a JSON file");
		});
		next.setOnAction(e -> topicChoosingPage(primaryStage));

		// add to GridPane
		grid.add(add, 0, 2);
		grid.add(load, 1, 2);
		grid.add(save, 0, 3);
		grid.add(next, 1, 3);

		// image pane for style
		BorderPane picture = new BorderPane();
		Image image = new Image("file:bucky.png", 150, 150, false, false);
		ImageView imageView = new ImageView(image);
		picture.setCenter(imageView);

		// the HBox containing the questions counter in the quiz
		HBox questionCounter = new HBox();
		questionCounter.setPadding(new Insets(10, 10, 10, 10));
		questionCounter.setAlignment(Pos.CENTER);
		questionCounter.getChildren().addAll(numQues, this.counter);

		// the VBox containing all layouts for this page
		VBox root = new VBox();
		root.getChildren().addAll(welcomeLabel, picture, grid, saved, questionCounter);

		// setup scene of this page
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * The page after user presses next in the main screen, where topics are chosen
	 * from the list of available topics to start generating a quiz.
	 * 
	 * @param primaryStage The primary stage
	 */
	@Override
	public void topicChoosingPage(Stage primaryStage) {
		// The main VBox, layout will be oriented vertically
		VBox mainBox = new VBox();
		mainBox.setPadding(new Insets(50, 20, 50, 20));
		mainBox.setSpacing(50);
		mainBox.setPrefSize(400, 600);

		// The stuff at the top, a drop down menu w a list of topics and a label
		HBox topBox = new HBox();
		Label lbl1 = new Label("Choose topic: ");
		topBox.setSpacing(10);
		topBox.setPadding(new Insets(10, 10, 10, 20));

		// The list of selected topics
		ObservableList<Topic> topic = FXCollections.observableArrayList();

		// The list of topics available for selection, sorted by alpha
		ArrayList<Topic> currentTopics = quiz.getTopics();
		currentTopics.sort((a, b) -> a.toString().compareTo(b.toString()));
		if (currentTopics.isEmpty()) {
			topic.add(new Topic("No topics loaded"));
		} else {
			topic.addAll(currentTopics);
		}

		// Dropdown list of available topics
		ComboBox<Topic> dropDown = new ComboBox<Topic>(topic);
		// Allows user to select current topic. see below midBox for listener
		Button selectCurrentTopic = new Button("Select");
		selectCurrentTopic.setMaxWidth(150);

		// Adds to main box
		topBox.getChildren().addAll(lbl1, dropDown, selectCurrentTopic);
		mainBox.getChildren().add(topBox);

		// The stuff in the middle, a list of selected topics
		HBox midBox = new HBox();
		midBox.setSpacing(10);
		midBox.setPadding(new Insets(10, 20, 10, 20));

		// The list of topics that the user has selected
		ObservableList<Topic> selectedTopics = FXCollections.observableArrayList();
		// Default behavior, tells the user that they haven't selected any topics yet
		if (selectedTopics.isEmpty()) {
			selectedTopics.add(new Topic("No topics selected"));
		}

		// Creates labels which represent the list of selected topics
		Label lbl2 = new Label("Selected topics: ");
		String tops = "";
		for (Topic t : selectedTopics) {
			tops += t.toString() + "\n";
		}
		Label selTops = new Label(tops);

		// Adds to main box
		midBox.getChildren().addAll(lbl2, selTops);
		mainBox.getChildren().add(midBox);

		// Allows user to add topics to their selected list
		selectCurrentTopic.setOnAction(e -> {
			// Takes the current selected topic
			Topic selected = dropDown.getValue();
			// If some topic has been selected prior to this call, then...
			if (!selected.toString().equals("No topics selected")) {
				// Removes default text
				if (selectedTopics.toString().contains("No topics selected")) {
					selectedTopics.remove(0);
				}

				// Avoids duplicates
				if (!selectedTopics.contains(selected)) {
					selectedTopics.add(selected);
				}

				// Constructs a string representation for the label
				String tops1 = "";
				int numQuestions = 0;
				for (Topic t : selectedTopics) {
					tops1 += "" + t.getQuestions().size() + " " + t.toString() + "\n";
					numQuestions += t.getQuestions().size();
				}
				tops1 += "" + numQuestions + " total questions selected";

				// Updates the label to show the new list of selected topics
				selTops.setText(tops1);
			}
		});

		// The section that lets you choose how many quiz questions you want
		HBox thirdBox = new HBox();
		thirdBox.setSpacing(10);
		thirdBox.setPadding(new Insets(10, 20, 10, 20));

		// Instructor label
		Label numQQ = new Label("Number of quiz questions:");

		// Creates a field that only accepts numbers
		TextField numQuizQuestions = new TextField();
		numQuizQuestions.setPrefWidth(40);
		numQuizQuestions.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
		// Sets default text so error isn't thrown when no questions are selected
		numQuizQuestions.setText("0");

		// Adds to main box
		thirdBox.getChildren().addAll(numQQ, numQuizQuestions);
		mainBox.getChildren().add(thirdBox);

		// The stuff at the bottom, a forward and back button
		HBox bottomBox = new HBox();
		bottomBox.setPadding(new Insets(10, 20, 10, 20));
		bottomBox.setSpacing(20);

		// Back button that takes user back to main screen
		Button back = new Button("Back");
		back.setMaxWidth(150);
		back.setOnAction(e -> mainScreen(primaryStage));

		// Forward button that creates a list of questions and opens up the taking quiz
		// page
		Button forward = new Button("Take Quiz");
		forward.setMaxWidth(150);
		forward.setOnAction(e -> takingQuizPage(primaryStage,
				this.quiz.generateQuizQuestions(selectedTopics, Integer.parseInt(numQuizQuestions.getText()))));

		// Allows the user to remove the last topic that they added
		Button removeLatest = new Button("Remove last topic");
		removeLatest.setMaxWidth(150);
		removeLatest.setOnAction(e -> {
			// If the user has selected a topic prior to this method call
			if (!selTops.toString().contains("No topics selected")) {
				// Remove latest selection
				selectedTopics.remove(selectedTopics.size() - 1);

				// Rebuild selected topics list
				String tops1 = "";
				int numQuestions = 0;
				for (Topic t : selectedTopics) {
					tops1 += "" + t.getQuestions().size() + " " + t.toString() + "\n";
					numQuestions += t.getQuestions().size();
				}
				tops1 += "" + numQuestions + " total questions selected";

				// Avoids having an empty selection list by adding placeholder text
				if (!selectedTopics.isEmpty()) {
					selTops.setText(tops1);
				} else {
					selTops.setText("No topics selected");
				}
			}
		});

		// Adds to main box
		bottomBox.getChildren().addAll(back, removeLatest, forward);
		mainBox.getChildren().add(bottomBox);

		// Shows main screen
		Scene scene = new Scene(mainBox);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * The page where the quiz is actually taken.
	 * 
	 * @param primaryStage The primary stage
	 */
	@Override
	public void takingQuizPage(Stage primaryStage, Question[] questions) {
		int numAnswered = 0;
		int numCorrect = 0;

		//Used to show what question you're on
		int i = 1;
		for (Question q : questions) {
			if (q == null) {
				break;
			} else {
			  firstTimeChecked = true;
				if (renderQuestion(q, i, questions.length)) {
					numCorrect++;
				}
				i++;
			}
		}

		renderSummary(numCorrect, questions.length);
		questionsAnswered = 0;
	}

	private void renderSummary(int correct, int numQuestions) {
		Stage stage = new Stage();

		//Main VBox layout
		VBox layout = new VBox();
		Scene scene = new Scene(layout, 700, 700);
		layout.setPadding(new Insets(10, 50, 50, 50));
		layout.setSpacing(20);

		//The title, big font, centered at top
		Label title = new Label("Summary");
		title.setFont(Font.font(50));
		title.setLineSpacing(15);
		layout.getChildren().add(title);
		layout.setAlignment(Pos.BASELINE_CENTER);

		Label label = new Label("You answered " + questionsAnswered + " questions and got " + correct + " correct.");
		Label label1 = new Label("There were " + numQuestions + " questions total.");

		label.setFont(Font.font(20));
		label1.setFont(Font.font(20));
		layout.getChildren().add(label);
		layout.getChildren().add(label1);
		layout.setAlignment(Pos.BASELINE_CENTER);

		//Exit button, returns to quiz page
		Button next = new Button("Return");
		next.setOnAction(e -> {
			stage.close();
		});
		
		Button exit = new Button("Exit");
		exit.setOnAction(e -> {
			exitPage(stage);
		});

		//Renders screen
		layout.getChildren().add(next);
		layout.getChildren().add(exit);
		stage.setScene(scene);
		stage.showAndWait();
	}
	
	boolean firstTimeChecked;

	/**
	 * Renders a question and lets the user answer
	 * @param q The Question object
	 * @param questionNum The question that the user is on
	 * @param totalNumQuestions The total number of questions in the quiz
	 * @return Whether the question was answered correctly
	 */
	private Boolean renderQuestion(Question q, int questionNum, int totalNumQuestions) {
		Stage secondary = new Stage();
		boolean result = false;

		//The main VBox
		VBox layout = new VBox();
		Scene scene = new Scene(layout, 700, 800);
		layout.setPadding(new Insets(10, 50, 50, 50));
		layout.setSpacing(20);

		//The title, bold, centered at the top. Shows what question the user is on
		Label title = new Label("Quiz (" + questionNum + "/" + totalNumQuestions + ")");
		title.setFont(Font.font(50));
		title.setLineSpacing(15);
		layout.getChildren().add(title);
		layout.setAlignment(Pos.BASELINE_CENTER);

		//The text of the question
		int numSpaces = 0;
		String questionText = "";
		for (int i = 0; i < q.getText().length(); i++) {
		  questionText += q.getText().charAt(i);
		  if (q.getText().charAt(i) == ' ') {
		    numSpaces++;
		    if (numSpaces % 10 == 0) {
		      questionText += "\n";
		    }
		  }
		}
		
		Label question = new Label(questionText);
		HBox container = new HBox();
		question.setFont(Font.font(20));
		question.setMinHeight(numSpaces * 4 + 20);
	    question.setPrefHeight(numSpaces * 4 + 20);
		container.getChildren().add(question);
		container.setAlignment(Pos.TOP_CENTER);
		layout.getChildren().add(container);
		layout.setAlignment(Pos.BASELINE_CENTER);
	

		//Trys to display the image, doesn't have to
		try {
			if (q.getImagePath() != null) {
				ImageView img = new ImageView("file:" + q.getImagePath());
				img.setFitHeight(100);
				img.setFitWidth(100);
				layout.getChildren().add(img);
				layout.setAlignment(Pos.BASELINE_CENTER);
			}
		} catch (IllegalArgumentException e) {
			System.out.println("Bad image path");
		}

		//Displays the options
		for (String s : q.getChoiceArray()) {
			Label choice = new Label(s);
			choice.setFont(Font.font(15));
			layout.getChildren().add(choice);
			layout.setAlignment(Pos.BASELINE_CENTER);
		}	

		//The place where the user will answer
		TextField answer = new TextField();
		Label label = new Label("");
		Button checkAnswer = new Button("Check Answer");
		
		int count = 0;

		checkAnswer.setOnAction(e -> {
			boolean correct = checkAnswer(answer.getText(), q.getAnswer());
			layout.getChildren().remove(label);

			if (correct) {
				label.setText("Correct!");
			} else {
				label.setText("Incorrect!");
			}

			layout.getChildren().add(label);

			if (firstTimeChecked) {
			  questionsAnswered++;
			}
			firstTimeChecked = false;
		});
		

		layout.getChildren().add(answer);
		layout.getChildren().add(checkAnswer);

		//Moving on to the next question regardless of whether it was saved
		Button next = new Button("Next");
		next.setOnAction(e -> {
			secondary.close();
		});

		//Rendering everything
		layout.getChildren().add(next);
		secondary.setScene(scene);
		secondary.showAndWait();
		result = checkAnswer(answer.getText(), q.getAnswer());

		//Whether the answer was right
		return result;
	}

	/**
	 * Checks whether an answer was correct
	 * @param input The user answer
	 * @param answer The correct answer
	 * @return True if question correct
	 */
	private boolean checkAnswer(String input, String answer) {
		if (input.equals(answer)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * The method to setup the stage for the QuizGUI.
	 * 
	 * @param primaryStage - the stage of the QuizGUI.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.quiz = new Quiz();
		try {
			primaryStage.setTitle("ATeam 79 Group Quiz Project");
			mainScreen(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * The page after user presses "Add Question" in the main screen, where user can
	 * fill out text fields and forms manually.
	 * 
	 * @param primaryStage
	 */
	@Override
	public void addQuestionPage(Stage primaryStage) {
		// the main pane
		VBox mainBox = new VBox();
		// pane displaying
		VBox choices = new VBox();
		// pane containing radio buttons
		VBox radioButtons = new VBox(10);
		// pane containing error message
		VBox error = new VBox();
		// pane containing the radio
		BorderPane prompt = new BorderPane();
		// pane containing
		BorderPane choice = new BorderPane();
		// pane containing the buttons
		BorderPane buttons = new BorderPane();
		// set the VBox
		mainBox.setPadding(new Insets(50, 20, 50, 20));
		mainBox.setSpacing(20);
		mainBox.setPrefSize(400, 400);
		// set labels above the text fields to prompt the user
		Label topicPrompt = new Label("Enter question topic");
		Label questionPrompt = new Label("Enter question");
		Label imagePrompt = new Label("Enter path of question of image (optional)");
		Label choicePrompt = new Label("Select the correct choice");
		Label errorPrompt = new Label("");
		errorPrompt.setTextFill(Color.web("#FF0000"));

		prompt.setRight(choicePrompt);
		// create text areas/fields to gather the info for the question
		TextField topic = new TextField();
		topic.setPromptText("Enter the topic here");
		TextArea content = new TextArea();
		content.setPromptText("Enter the question here");
		// make it pretty
		content.setMinHeight(50);
		content.setWrapText(true);
		// prompt the user for the image
		TextField image = new TextField();
		image.setPromptText("Enter the path for the image");
		TextField choiceA = new TextField();
		// gather the choice options
		choiceA.setPromptText("Enter choice A here");
		TextField choiceB = new TextField();
		choiceB.setPromptText("Enter choice B here");
		TextField choiceC = new TextField();
		choiceC.setPromptText("Enter choice C here");
		TextField choiceD = new TextField();
		choiceD.setPromptText("Enter choice D here");
		TextField choiceE = new TextField();
		choiceE.setPromptText("Enter choice E here");
		// use a radio button group to let the user to select the correct choice
		RadioButton rb1 = new RadioButton("A");
		RadioButton rb2 = new RadioButton("B");
		RadioButton rb3 = new RadioButton("C");
		RadioButton rb4 = new RadioButton("D");
		RadioButton rb5 = new RadioButton("E");
		final ToggleGroup group = new ToggleGroup();
		rb1.setToggleGroup(group);
		rb2.setToggleGroup(group);
		rb3.setToggleGroup(group);
		rb4.setToggleGroup(group);
		rb5.setToggleGroup(group);
		radioButtons.getChildren().add(rb1);
		rb1.setSelected(true);
		radioButtons.getChildren().add(rb2);
		radioButtons.getChildren().add(rb3);
		radioButtons.getChildren().add(rb4);
		radioButtons.getChildren().add(rb5);
		// add buttons for the users to switch between pages
		Button backButton = new Button("Back");
		backButton.setOnAction(e -> mainScreen(primaryStage));
		Button addButton = new Button("Add");
		// add event handler for the add button
		EventHandler<MouseEvent> addEventHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				String answer = null;
				if (rb1.isSelected() == true) {
					answer = choiceA.getText();
				}
				if (rb2.isSelected() == true) {
					answer = choiceB.getText();
				}
				if (rb3.isSelected() == true) {
					answer = choiceC.getText();
				}
				if (rb4.isSelected() == true) {
					answer = choiceD.getText();
				}
				if (rb5.isSelected() == true) {
					answer = choiceE.getText();
				}
				String[] options = new String[5];
				options[0] = choiceA.getText();
				options[1] = choiceB.getText();
				options[2] = choiceC.getText();
				options[3] = choiceD.getText();
				options[4] = choiceE.getText();
				String topicText = topic.getText();
				String imageText = image.getText();
				File file = new File(imageText);
				if (!file.exists() && !imageText.isEmpty()) {
					errorPrompt.setText("Please enter a valid location or leave box empty");
				} else {
					quiz.addQuestion(content.getText(), answer, options, topicText, imageText);
					mainScreen(primaryStage);
				}
			}
		};
		addButton.addEventFilter(MouseEvent.MOUSE_CLICKED, addEventHandler);
		// add everything to the choices pane
		choices.getChildren().add(choiceA);
		choices.getChildren().add(choiceB);
		choices.getChildren().add(choiceC);
		choices.getChildren().add(choiceD);
		choices.getChildren().add(choiceE);
		choice.setLeft(choices);
		choice.setRight(radioButtons);
		buttons.setLeft(backButton);
		buttons.setRight(addButton);
		error.getChildren().add(errorPrompt);
		// add everything to the main pane
		mainBox.getChildren().addAll(topicPrompt, topic, questionPrompt, content, imagePrompt, image, prompt, choice,
				buttons, error);
		Scene scene = new Scene(mainBox);
		primaryStage.setScene(scene);
		// go back to the primary stage
		primaryStage.show();
	}

	/**
	 * The page after user presses "Load Question" in the main screen, where user
	 * can enter in a file path to load questions into the Quiz from a JSON file.
	 * 
	 * @param primaryStage
	 */
	@Override
	public void loadQuestionPage(Stage primaryStage) {
		// Layouts:
		VBox mainBox = new VBox(); // Main Container
		HBox instuctBox = new HBox();
		HBox textBox = new HBox();
		HBox resultBox = new HBox();
		HBox buttonBox = new HBox();

		// Fields:
		Label instrutLabel = new Label("Enter Relative JSON FilePath");
		Label resultLabel = new Label("");
		TextField JSONFile = new TextField();
		Button back = new Button("Back");
		Button load = new Button("Load Questions");

		// Setting field behavior:
		JSONFile.setPromptText("JSON FilePath");
		JSONFile.setPrefWidth(300);
		back.setMaxWidth(150);
		load.setMaxWidth(150);

		// Loads files from inputed filepath and displays message to indicate success
		load.setOnAction(e -> {
			if (loadQuestion((JSONFile.getText()))) {
				resultLabel.setTextFill(Color.web("#0000FF"));
				resultLabel.setText("Questions Added!");
			} else {
				resultLabel.setTextFill(Color.web("#FF0000"));
				resultLabel.setText("Addition failed");
			}
		});
		back.setOnAction(e -> mainScreen(primaryStage)); // Has back button return user to main screen

		// Setting style for layouts:
		mainBox.setSpacing(10);
		mainBox.setPrefSize(400, 400);
		instuctBox.setPadding(new Insets(10, 20, 0, 20));
		textBox.setPadding(new Insets(0, 20, 20, 20));
		buttonBox.setPadding(new Insets(20, 20, 20, 20));
		buttonBox.setSpacing(20);
		resultBox.setPadding(new Insets(10, 20, 0, 20));

		// Adding elements to layouts
		textBox.getChildren().addAll(JSONFile);
		instuctBox.getChildren().add(instrutLabel);
		buttonBox.getChildren().addAll(back, load);
		resultBox.getChildren().add(resultLabel);
		mainBox.getChildren().addAll(instuctBox, textBox, buttonBox, resultBox);

		// Setting Scene:
		Scene scene = new Scene(mainBox);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Private helper method that takes questions from a JSON file and parses it
	 * into the Quiz's storage
	 * 
	 * @param JSONfilePath is the file path of the JSON file
	 * @return true if retrieval successful, false otherwise
	 */
	private boolean loadQuestion(String JSONfilePath) {
		try {
			quiz.loadQuestions(JSONfilePath); // loads questions from JSON file into quiz
			return true;
		} catch (Exception e) { // Catches Exception if file does not exist, other IO error
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Exit page prompting user to exit saving questions in quiz or exit without
	 * saving, will provide a goodbye message.
	 */
	@Override
	public void exitPage(Stage primaryStage) {
		// main container
		VBox root = new VBox();
		root.setPadding(new Insets(30, 30, 30, 30));
		root.setPrefSize(400, 300);

		// various labels for the exit page
		Label thanks = new Label("THANKS FOR TAKING THE QUIZ");
		Label savePrompt = new Label("Enter file name to save all questions to json file:");
		savePrompt.setPadding(new Insets(10, 0, 10, 0));
		Label goodBye = new Label(""); // will change depending on button chosen
		TextField fileName = new TextField();
		fileName.setPromptText("Enter file name");

		// HBox containing thank you message
		HBox thankYou = new HBox();
		thankYou.setPadding(new Insets(10, 10, 10, 10));
		thankYou.setAlignment(Pos.CENTER);
		thankYou.getChildren().add(thanks);

		// buttons for either save or exit without saving
		Button save = new Button("Save");
		save.setMaxWidth(150);
		Button exitNoSave = new Button("Exit without Save");
		save.setMaxWidth(150);
		save.setOnAction(e -> {
			quiz.save(fileName.getText());
			goodBye.setText(
					"Saved all questions (" + quiz.numQuestions() + ") to " + fileName.getText() + ", goodbye!");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Platform.exit();
		});
		exitNoSave.setOnAction(e -> {
			goodBye.setText("Exiting quiz without saving, goodbye!");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Platform.exit();
		});

		// the grid containing the buttons
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setMinSize(200, 200);
		grid.setVgap(20);
		grid.setHgap(20);
		grid.setAlignment(Pos.CENTER);
		grid.add(save, 0, 0);
		grid.add(exitNoSave, 1, 0);

		// add all the components of this page in a VBox in order
		root.getChildren().addAll(thankYou, savePrompt, fileName, grid, goodBye);

		// setup scene and show
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Simply launches the program, see {@link Quiz#start(Stage)} for more
	 * interesting main-method type shenanigans.
	 * 
	 * @param args The command line args
	 */
	public static void main(String[] args) {
		launch(args);

	}
}
