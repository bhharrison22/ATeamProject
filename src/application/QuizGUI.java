package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.parser.ParseException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class QuizGUI extends Application implements QuizGUIADT {

  private Quiz quiz;
  private String counter = "0";

  @Override
  public void mainScreen(Stage primaryStage) {
    Label welcome = new Label("WELCOME TO QUIZ GENERATOR");
    Label numQues = new Label("Number of Questions in Quiz: ");
    Label questions = new Label(this.counter);
    
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10,10,10,10));
    grid.setMinSize(300,300);
    grid.setVgap(5);
    grid.setHgap(5);
    grid.setAlignment(Pos.CENTER);
    
    Button add = new Button("Add Question");
    Button load = new Button("Load Question");
    Button save = new Button("Save Question");
    Button next = new Button("Next");
    Button test = new Button("Test");
    add.setOnAction(e-> addQuestionPage(primaryStage));
    load.setOnAction(e-> loadQuestionPage(primaryStage));
    save.setOnAction(e-> this.quiz.save());
    next.setOnAction(e-> topicChoosingPage(primaryStage));
    
    grid.add(add, 0, 2);
    grid.add(load, 1, 2);
    grid.add(save, 0, 3);
    grid.add(next, 1, 3);
 
    HBox welcomeLabel = new HBox();
    welcomeLabel.setPadding(new Insets(50,0,0,0));
    welcomeLabel.setAlignment(Pos.CENTER);
    welcomeLabel.getChildren().add(welcome);
    
    VBox root = new VBox();
    root.getChildren().addAll(welcomeLabel, grid);
    HBox questionCounter = new HBox();
    questionCounter.getChildren().addAll(numQues, questions, test);
    
    root.getChildren().add(questionCounter);
    
    test.setOnAction(e-> questions.setText(Integer.parseInt(questions.getText()) + 1 + ""));
    
    Scene scene = new Scene(root); 
    primaryStage.setScene(scene);
    primaryStage.show();    
  }

  @Override
  public void topicChoosingPage(Stage primaryStage) {
    VBox mainBox = new VBox(); // This layout will be vertical, so VBox
    mainBox.setPadding(new Insets(50, 20, 50, 20));
    mainBox.setSpacing(50);
    mainBox.setPrefSize(400, 600);
    ObservableList<Topic> selectedTopics = FXCollections.observableArrayList();

    // The stuff at the top, a drop down menu w a list of topics and a label
    HBox topBox = new HBox();
    topBox.setSpacing(10);
    topBox.setPadding(new Insets(10, 40, 10, 20));
    Label lbl1 = new Label("Choose topic: ");
    ObservableList<Topic> topic = FXCollections.observableArrayList();
    // Obtains list of all topics and displays them:
    ArrayList<Topic> currentTopics = quiz.getTopics();
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

    // See end of midbox for action listener
    topBox.getChildren().addAll(lbl1, dropDown, selectCurrentTopic);
    mainBox.getChildren().add(topBox);

    // The stuff in the middle, a list of selected topics
    HBox midBox = new HBox();
    midBox.setSpacing(10);
    midBox.setPadding(new Insets(10, 40, 10, 20));

    if (selectedTopics.isEmpty()) {
      selectedTopics.add(new Topic("No topics selected"));
    }
    Label lbl2 = new Label("Selected topics: ");
    String tops = "";
    for (Topic t : selectedTopics) {
      tops += t.toString() + "\n";
    }
    Label selTops = new Label(tops);
    midBox.getChildren().addAll(lbl2, selTops);
    mainBox.getChildren().add(midBox);
    // Allows user to add topics to their selected list
    selectCurrentTopic.setOnAction(e -> {
      Topic selected = dropDown.getValue();
      // If some topic is selected...
      if (!selected.toString().equals("No topics selected")) {
        // Removes default text
        if (selectedTopics.toString().contains("No topics selected")) {
          selectedTopics.remove(0);
        }
        // Avoids duplicates
        if (!selectedTopics.contains(selected)) {
          selectedTopics.add(selected);
        }
        String tops1 = "";
        int numQuestions = 0;
        for (Topic t : selectedTopics) {
          tops1 += "" + t.getQuestions().size() + " " + t.toString() + "\n";
          numQuestions += t.getQuestions().size();
        }
        tops1 += "" + numQuestions + " total questions selected";
        // Updates the list of selected topics
        selTops.setText(tops1);
      }
    });

    // The section that lets you choose how many quiz questions you want
    HBox thirdBox = new HBox();
    thirdBox.setSpacing(10);
    thirdBox.setPadding(new Insets(10, 40, 10, 20));
    Label numQQ = new Label("Number of quiz questions:");
    // Creates a field that only accepts numbers
    TextField numQuizQuestions = new TextField("0");
    numQuizQuestions.setPrefWidth(40);
    numQuizQuestions.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
    numQuizQuestions.setText("0");
    thirdBox.getChildren().addAll(numQQ, numQuizQuestions);
    mainBox.getChildren().add(thirdBox);

    // The stuff at the bottom, a forward and back button
    HBox bottomBox = new HBox();
    bottomBox.setPadding(new Insets(10, 20, 10, 20));
    bottomBox.setSpacing(20);

    Button back = new Button("Back");
    // Runs mainScreen method when user wants to go back
    back.setOnAction(e -> mainScreen(primaryStage));

    Button forward = new Button("Take Quiz");
    // Runs takingQuizPage when user is ready to take quiz
    forward.setOnAction(e -> takingQuizPage(primaryStage, this.quiz
        .generateQuizQuestions(selectedTopics, Integer.parseInt(numQuizQuestions.getText()))));

    // Adds functionality to remove the latest topic selected
    Button removeLatest = new Button("Remove last topic");
    removeLatest.setOnAction(e -> {
      // If a topic has been selected...
      if (!selTops.toString().contains("No topics selected")) {
        // Remove latest selection
        selectedTopics.remove(selectedTopics.size() - 1);
        // Rebuild selected topics list
        String tops1 = "";
        for (Topic t : selectedTopics) {
          tops1 += t.toString() + "\n";
        }
        // Avoids having an empty selection list by adding placeholder text
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

	  String[] choices = {"A is a subset of B, but not a proper subset of B", "A is a proper subset of B", "A is a superset of B, but not a proper superset of B",
			  "A is a proper superset of B", "A is the complement of B"};
	  
	  Question A = new Question("Which statement is TRUE regarding sets A and B?", "A is a proper superset of B", null , choices, "set");
	  
	  String[] choices1 = {"when the problem size is small", "when the problem size is large"};
	  
	  Question B = new Question("When two algorithms have different big-O time complexity, the constants and low-order terms only matter ________. ",
			  "when the problem size is small", "application/bucky.png", choices1, "performance");
	  
	  Question[] questions1 = {A,B};	  
	  int numCorrect = 0;

	  for (Question q: questions1) {
	    if (q == null) {
	      break;
	    } else {
	      if (renderQuestion( q)) {
	        numCorrect++;
	      }
	    }
	  }

	  System.out.println(numCorrect);
}
  
  private Boolean renderQuestion(Question q) {
	  Stage secondary = new Stage();
	  boolean result = false;

      VBox layout = new VBox();
      Scene scene = new Scene(layout, 700, 700);
      layout.setPadding(new Insets(10, 50, 50, 50));
      layout.setSpacing(20);
      
      
      Label title = new Label("Quiz");
      title.setFont(Font.font(50));
      title.setLineSpacing(15);
      layout.getChildren().add(title);
      layout.setAlignment(Pos.BASELINE_CENTER);
      
      Label question = new Label(q.getText()); 
      question.setFont(Font.font(20));
      layout.getChildren().add(question);
//      question.setWrapText(true);
      layout.setAlignment(Pos.BASELINE_CENTER);
      
      try {
        if(q.getImagePath() != null) {
          ImageView img = new ImageView(q.getImagePath()); 
          img.setFitHeight(100);
          img.setFitWidth(100);
          layout.getChildren().add(img);
          layout.setAlignment(Pos.BASELINE_CENTER);
        }
      } catch (IllegalArgumentException e) {
        System.out.println("Bad image path");
      }
      
      for(String s: q.getChoiceArray()) {
    	  Label choice = new Label(s); 
          choice.setFont(Font.font(10));
          layout.getChildren().add(choice);
          layout.setAlignment(Pos.BASELINE_CENTER);
      }
      
      TextField answer = new TextField();
      
      answer.setOnAction( e -> {
      boolean correct = checkAnswer(answer.getText(), q.getAnswer());
      
      if(correct) {
    	  layout.getChildren().add(new Label("Correct!"));
      } else {
    	  layout.getChildren().add(new Label("Incorrect!"));
      }	 
      });
      
      layout.getChildren().add(answer);
      
      Button next = new Button("Next");
      next.setOnAction(e -> {
    	  secondary.close();
      });
      
      layout.getChildren().add(next);
      
      secondary.setScene(scene);
      secondary.showAndWait();
      
	  result = checkAnswer(answer.getText(), q.getAnswer());
      
      return result;
}

  
  

private boolean checkAnswer(String input, String answer) {
	if(input.equals(answer)) {
		return true;
	} else {
		return false;
	}	
}

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
   * Simply launches the program, see {@link Quiz#start(Stage)} for more interesting main-method
   * type shenanigans.
   * 
   * @param args The command line args
   */
  public static void main(String[] args) {
    launch(args);

  }


  public void addQuestionPage(Stage primaryStage) {
	  VBox mainBox = new VBox();
	  VBox choices = new VBox();
	  VBox radioButtons = new VBox(10);
	  BorderPane prompt = new BorderPane();
	  BorderPane choice = new BorderPane();
	  BorderPane buttons = new BorderPane();
	  mainBox.setPadding(new Insets(50, 20, 50, 20));
      mainBox.setSpacing(50);
      mainBox.setPrefSize(400, 400);
      Label choicePrompt = new Label("Select the correct choice");
      prompt.setRight(choicePrompt);
      TextField topic = new TextField("Enter the topic here");
      TextArea content = new TextArea("Enter the question here");
      content.setMinHeight(50);
      content.setWrapText(true);
      TextField choiceA = new TextField("Enter choice A here");
      TextField choiceB = new TextField("Enter choice B here");
      TextField choiceC = new TextField("Enter choice C here");
      TextField choiceD = new TextField("Enter choice D here");
      TextField choiceE = new TextField("Enter choice E here");
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
      Button backButton = new Button("Back");
      backButton.setOnAction(e -> mainScreen(primaryStage));
      Button addButton = new Button("Add");
      addButton.setOnAction(e -> mainScreen(primaryStage) 
      );
      choices.getChildren().add(choiceA);
      choices.getChildren().add(choiceB);
      choices.getChildren().add(choiceC);
      choices.getChildren().add(choiceD);
      choices.getChildren().add(choiceE);
      choice.setLeft(choices);
      choice.setRight(radioButtons);
      buttons.setLeft(backButton);
      buttons.setRight(addButton);
      mainBox.getChildren().add(topic);
      mainBox.getChildren().add(content);
      
      mainBox.getChildren().add(prompt);
      mainBox.getChildren().add(choice);
      mainBox.getChildren().add(buttons);
      Scene scene = new Scene(mainBox);
      primaryStage.setScene(scene);
      primaryStage.show();
	}

  @Override
  public void loadQuestionPage(Stage primaryStage) {
    // Layouts:
    VBox mainBox = new VBox();
    HBox instuctBox = new HBox();
    HBox textBox = new HBox();
    HBox resultBox = new HBox();
    HBox buttonBox = new HBox();
    // Fields:
    Label instrutLabel = new Label("Load Question: ");
    Label resultLabel = new Label("");
    TextField JSONFile = new TextField("Enter Relative JSON FilePath (No .JSON)");
    Button back = new Button("Back");
    Button load = new Button("Load Question");
    load.setOnAction(e -> {
      if (loadQuestion((JSONFile.getText() + ".json"))) {
        resultLabel.setTextFill(Color.web("#0000FF"));
        resultLabel.setText("Questions Added!");
      }
      else {
        resultLabel.setTextFill(Color.web("#FF0000"));
        resultLabel.setText("Addition failed");
      }
    });
    back.setOnAction(e -> mainScreen(primaryStage));
    JSONFile.setPrefWidth(300);
    // Adding elements:
    mainBox.setSpacing(10);
    mainBox.setPrefSize(400, 400);
    instuctBox.getChildren().add(instrutLabel);
    instuctBox.setPadding(new Insets(10, 0, 0, 20));
    textBox.setPadding(new Insets(0, 20, 20, 20));
    textBox.getChildren().addAll(JSONFile);
    buttonBox.setPadding(new Insets(20, 20, 20, 20));
    buttonBox.setSpacing(20);
    buttonBox.getChildren().addAll(back, load);
    resultBox.getChildren().add(resultLabel);
    resultBox.setPadding(new Insets(10, 0, 0, 20));
    mainBox.getChildren().addAll(instuctBox, textBox, buttonBox, resultBox);
    Scene scene = new Scene(mainBox);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  
  private boolean loadQuestion(String JSONfilePath) {
    try {
      quiz.loadQuestions(JSONfilePath);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
