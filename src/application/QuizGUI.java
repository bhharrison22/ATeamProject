package application;

import java.util.ArrayList;
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
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class QuizGUI extends Application implements QuizGUIADT {

  private Quiz quiz;

  @Override
  public void mainScreen(Stage primaryStage) {
    Label welcome = new Label("WELCOME TO QUIZ GENERATOR");
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
    TextField numQuizQuestions = new TextField();
    numQuizQuestions.setPrefWidth(40);
    numQuizQuestions.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
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
    // TODO: remove testing before submitting.
    // // Testing:
    // Quiz q1 = new Quiz();
    // String[] options = {"answer", "Wrong1", "Wrong2", "Wrong3"};
    // q1.addQuestion("Q1", "answer", options, "Test Questions", "");
    // q1.addQuestion("Q2", "Wrong1", options, "Test Questions", "");
    // q1.save();
    // Quiz q2 = new Quiz();
    // try {
    // q2.loadQuestions("Saved_Questions.json");
    // q2.addQuestion("After Load Q", "Wrong3", options, "loading", "");
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // Question[] qs = q2.generateQuizQuestions(q2.currentTopics, 4);
    // for (Question q : qs) {
    // if (q != null)
    // System.out.println(q.getText());
    // else
    // System.out.println(q);
  }


  @Override
  public void addQuestionPage(Stage primaryStage) {
    // TODO YUANYE DO THIS

  }

  @Override
  public void loadQuestionPage(Stage primaryStage) {
    // TODO BEN DO THIS

  }
}
