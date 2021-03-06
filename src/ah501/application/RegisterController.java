package ah501.application;

import ah501.movies.Movie;
import ah501.movies.MovieIO;
import ah501.movies.MovieReg;
import ah501.movies.Rating;
import ah501.registration.Registry;
import ah501.registration.User;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/*
* Author: ah501
* This controller handles the FXML features, navigation and other methods
* of the MovieFinder JavaFX application.
*/

public class RegisterController {

    // Attributes
    private Registry reg;
    private MovieReg mreg;
    private static int lastId = 164979;

    // FXML attributes as required for handler methods.
    @FXML
    private PasswordField pwSignup, pwField;

    @FXML
    private Label loginError, signupError, movieError, ratingError;

    @FXML
    private Button login, signup,loginSubmit,loginCancel, signupSubmit, signupCancel,
    search, clear, randomise, back, addM, addR, all, sort, deleteM, deleteR;

    @FXML
    private TextField unameField, usernameField, nameField, emailField, searchField, mNameField, yearField, genreField;

    @FXML
    private MenuItem newMovie, newRating, about, logout;

    @FXML
    private ListView<String> movieList, searchResults;

    @FXML
    private ChoiceBox<Integer> movieRating;

    @FXML
    private ChoiceBox<String> movieChoice;


    // Handler methods for FXML elements.

    @FXML
    void onLogin(ActionEvent event) throws IOException {
        Stage stage;
        if(event.getSource()==login) {
            stage = (Stage) login.getScene().getWindow();
            stage.setScene(SceneManager.createFXMLScene("LoginForm.fxml"));
            stage.show();
        }
    }

    @FXML
    void onSubmitLogin(ActionEvent event) throws IOException {
        Stage stage;
        if(event.getSource() == loginSubmit) {
            reg = new Registry();

            for(User u : reg.getRegister()) {
                if (usernameField.getText().equals(u.getUsername())) {
                    if (pwField.getText().equals(u.getPassword())) {
                        stage = (Stage) loginSubmit.getScene().getWindow();
                        stage.setScene(SceneManager.createFXMLScene("MainView.fxml"));
                        stage.show();
                    }
                }

            }

            loginError.setText("Invalid username or password.");
            if(usernameField.getText().equals("")){
                loginError.setText("Please enter a username");
            }
            if(pwField.getText().equals("")) {
                loginError.setText("Please enter a password");
            }

        }
    }

    @FXML
    void onSignup(ActionEvent event) throws IOException {
        Stage stage;
        if(event.getSource() == signup) {
            stage = (Stage) signup.getScene().getWindow();
            stage.setScene(SceneManager.createFXMLScene("SignupForm.fxml"));
            stage.show();
        }
    }

    @FXML
    void onSignupSubmit(ActionEvent event) throws IOException {
        Stage stage;
        if (event.getSource() == signupSubmit) {
            if (nameField.getText().isEmpty() || unameField.getText().isEmpty() || emailField.getText().isEmpty() || pwSignup.getText().isEmpty()) {
                signupError.setText("Please enter all the required information.");
            } else if (!emailField.getText().contains("@")){
                signupError.setText("Please enter a valid email address.");
            } else {
                reg = new Registry();

                boolean exists = false;
                for (User u : reg.getRegister()) {
                    if (u.getUsername().equals(unameField.getText())) {
                        signupError.setText("Username already exists.");
                        exists = true;
                    }

                }

                if (!exists) {
                    User n = new User(unameField.getText(), nameField.getText(), emailField.getText(), pwSignup.getText());
                    reg.addUser(n);
                    stage = (Stage) signupSubmit.getScene().getWindow();
                    stage.setScene(SceneManager.createFXMLScene("LoginForm.fxml"));
                    stage.show();
                }

            }

        }
    }

    // End methods for registration, begin methods for main functionality

    @FXML
    void returnHome(ActionEvent event) throws IOException {
        Stage stage;
        if(event.getSource()==signupCancel) {
            stage = (Stage) signupCancel.getScene().getWindow();
            stage.setScene(SceneManager.createFXMLScene("StartScreen.fxml"));
            stage.show();
        }

        if(event.getSource()==loginCancel) {
            stage = (Stage) loginCancel.getScene().getWindow();
            stage.setScene(SceneManager.createFXMLScene("StartScreen.fxml"));
            stage.show();
        }

    }

    @FXML
    void onSave() {
        ObservableList<String> saveResult = FXCollections.observableArrayList();
        MovieIO.saveRegistry();
        if(MovieIO.saveRatings()) {
            saveResult.add("Your database was saved succesfully.");
        } else {
            saveResult.add("Something went wrong while saving! Please try again.");
        }

        movieList.setItems(saveResult);
    }

    @FXML
    void onLoad() {
        ObservableList<String> loadResult = FXCollections.observableArrayList();
        File movies = new File("src/files/savedMovies.csv");
        File ratings = new File("src/files/savedRatings.csv");
        if(!movies.exists() || !ratings.exists()){
            loadResult.add("Could not load database.");
            loadResult.add("Have you saved yet? If not, you cannot load anything!");
        } else {
            MovieIO.load();
            loadResult.add("Loaded your database settings succesfully.");
        }

        movieList.setItems(loadResult);

    }

    @FXML
    void onLogout(ActionEvent event) throws IOException {
        Stage stage;

            stage = (Stage) randomise.getScene().getWindow();
            stage.setScene(SceneManager.createFXMLScene("StartScreen.fxml"));
            stage.show();
    }

    @FXML
    void onAbout(ActionEvent event) throws IOException {
        Stage stage;
            stage = (Stage) randomise.getScene().getWindow();
            stage.setScene(SceneManager.createFXMLScene("About.fxml"));
            stage.show();
    }

    @FXML
    void onClear(ActionEvent event) {
        searchField.clear();
        ObservableList<String> cleared = FXCollections.observableArrayList();
        searchResults.setItems(cleared);
    }

    @FXML
    void onExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void toDelete(ActionEvent event) throws IOException {
        Stage stage;
        stage = (Stage) randomise.getScene().getWindow();
        stage.setScene(SceneManager.createFXMLScene("Delete.fxml"));
        stage.show();

    }

    @FXML
    void onNewM(ActionEvent event) throws IOException{
        Stage stage;
        if(event.getSource() == newMovie) {
            stage = (Stage) randomise.getScene().getWindow();
            stage.setScene(SceneManager.createFXMLScene("AddMovie.fxml"));
            stage.show();
        }
    }


    @FXML
    void onSubmitM(ActionEvent event) throws IOException {
        StringBuilder compositeName = new StringBuilder();
        compositeName.append(mNameField.getText() + " (" + yearField.getText() + ")");
        if(movieRating.getValue() == null) {
            movieError.setText("Please provide a rating for the new film.");
        }  else if(!Pattern.matches("[1-2][0-9][0-9][0-9]", yearField.getText())) {
             movieError.setText("Please enter a valid year.");
         } else if (mNameField.getText().isEmpty() || yearField.getText().isEmpty() || genreField.getText().isEmpty()) {
            movieError.setText("Please provide all required information.");
        }  else if (genreField.getText().contains(",")) {
            movieError.setText("Please separate your genres with the | symbol, not commas.");
        }  else if(MovieIO.existsMovie(compositeName.toString())) {
            movieError.setText("This movie is already in our database.");
        }

        else {
                mreg = new MovieReg(0);
                Movie toAdd = new Movie(lastId, compositeName.toString(), genreField.getText());
                Rating firstRating = new Rating(100006, lastId, movieRating.getValue());
                mreg.addNewMovie(toAdd);
                mreg.addNewRating(firstRating);
                lastId++;

                Stage stage;
                stage = (Stage) addM.getScene().getWindow();
                stage.setScene(SceneManager.createFXMLScene("MainView.fxml"));
                stage.show();

            }
        }


    @FXML
    void onDeleteM(ActionEvent event) throws IOException {
        if(movieChoice.getValue() == null) {
            movieError.setText("Please select a movie.");
        } else {

            String name = movieChoice.getValue();
            mreg = new MovieReg(0);
            Movie m = mreg.searchByName(name).get(0);

            if(MovieIO.existsRating(100006, m.getMovieId())) {
                mreg.deleteRating(100006,m.getMovieId());
            }

            mreg.deleteMovie(name);

            Stage stage;
            stage = (Stage) deleteM.getScene().getWindow();
            stage.setScene(SceneManager.createFXMLScene("MainView.fxml"));
            stage.show();
        }
    }



    @FXML
    void onNewR(ActionEvent event) throws IOException {
        Stage stage;
        if(event.getSource() == newRating) {
            stage = (Stage) randomise.getScene().getWindow();
            stage.setScene(SceneManager.createFXMLScene("AddRating.fxml"));
            stage.show();
        }
    }

    @FXML
    void onSubmitR(ActionEvent event) throws IOException {
        Stage stage;
        mreg = new MovieReg();
        if(movieChoice.getValue() == null) {
            ratingError.setText("Please select a movie.");
        } else if(movieRating == null) {
            ratingError.setText("Please select a rating for " + movieChoice.getValue());
        } else {
           Movie m = mreg.searchByName(movieChoice.getValue()).get(0);
           if(MovieIO.existsRating(100006, m.getMovieId())){
               ratingError.setText("You have already rated this movie, please select another.");
           } else {
               Rating r = new Rating(100006, m.getMovieId(), movieRating.getValue());
               mreg.addNewRating(r);
               stage = (Stage) addR.getScene().getWindow();
               stage.setScene(SceneManager.createFXMLScene("MainView.fxml"));
               stage.show();

           }


        }
    }

    @FXML
    void onDeleteR(ActionEvent event) throws IOException{
        mreg = new MovieReg();
        if(movieChoice.getValue() == null) {
            movieError.setText("Please select a movie.");
        } else {
            Movie m = mreg.searchByName(movieChoice.getValue()).get(0);
            if(!MovieIO.existsRating(100006, m.getMovieId())){
                movieError.setText("You have not yet rated this movie.");
            } else {
                Movie m2 = mreg.searchByName(movieChoice.getValue()).get(0);
                mreg.deleteRating(100006, m2.getMovieId());
                Stage stage;
                stage = (Stage) deleteR.getScene().getWindow();
                stage.setScene(SceneManager.createFXMLScene("MainView.fxml"));
                stage.show();
            }
        }

    }

    @FXML
    void onRandom(ActionEvent event) {
        ObservableList<String> random = RegisterController.randomise();
        movieList.setItems(random);

    }

    @FXML
    void onAll(ActionEvent event) {
        ObservableList<String> all = RegisterController.viewAll();
        movieList.setItems(all);
    }

    @FXML
    void onSort (ActionEvent event) {
        ObservableList<String> sorted = RegisterController.sorter();
        movieList.setItems(sorted);
    }

    @FXML
    void onSearch(ActionEvent event) throws IOException {
        Stage stage;
        stage = (Stage) search.getScene().getWindow();
        stage.setScene(SceneManager.createFXMLScene("SearchResults.fxml"));
        stage.show();

    }

    @FXML
    void performSearch(ActionEvent event) {
        ObservableList<String> results = FXCollections.observableArrayList();
        if(searchField.getText().isEmpty()){
            results.add("Please enter a search query.");
            searchResults.setItems(results);
        } else {
            String query = searchField.getText();
            results = (RegisterController.searchByName(query));
            searchResults.setItems(results);
        }
    }



    @FXML
    void goBack(ActionEvent event) throws IOException {
        Stage stage;
        if(event.getSource()==back) {
            stage = (Stage) back.getScene().getWindow();
            stage.setScene(SceneManager.createFXMLScene("MainView.fxml"));
            stage.show();
        }
    }

    // Helper methods

    private static ObservableList<String> randomise(){
        ObservableList<String> toReturn = FXCollections.<String>observableArrayList();
        toReturn.add("-----------------------------------------------");
        toReturn.add("Title (Year)   ||   Genres   ||   Average Rating");
        toReturn.add("-----------------------------------------------");
        MovieReg movies = new MovieReg();
        Collections.shuffle(movies.getMovies());

        for(int i = 0; i < 5; i++) {
            toReturn.add(movies.getMovies().get(i).toString());
            if(i < 4) {
            toReturn.add("~ * ~"); }
        }

        return toReturn;

    }

    public void generateScores() {
        ObservableList<Integer> scores = FXCollections.observableArrayList(1,2,3,4,5);
        movieRating.setItems(scores);
    }

    public void generateMovies() {
        ObservableList<String> names = FXCollections.observableArrayList();
        mreg = new MovieReg(0);
        for(int i = 0; i < mreg.getMovies().size(); i++ ) {
            names.add(mreg.getMovies().get(i).getName());
        }
        movieChoice.setItems(names);

    }


    private static ObservableList<String> searchByName(String q) {
        ObservableList<String> toReturn = FXCollections.observableArrayList();
        MovieReg movieReg = new MovieReg();
        ArrayList<Movie> results = movieReg.searchByName(q);

        if(results.isEmpty()) {
            toReturn.add("Sorry, we couldn't find any movies matching that search.");
            toReturn.add("Please note that our search system is case sensitive!");
        } else {
            toReturn.add("-----------------------------------------------");
            toReturn.add("Title (Year)   ||   Genres   ||   Average Rating");
            toReturn.add("-----------------------------------------------");

            for(Movie m : results){
                toReturn.add(m.toString());
                toReturn.add(" ");
            }
        }
        return toReturn;
    }

    private static ObservableList<String> viewAll() {
        ObservableList<String> toReturn = FXCollections.observableArrayList();
        MovieReg movieReg = new MovieReg();
        toReturn.add("-----------------------------------------------");
        toReturn.add("Title (Year)   ||   Genres   ||   Average Rating");
        toReturn.add("-----------------------------------------------");

        for(Movie m: movieReg.getMovies()) {
            toReturn.add(m.toString());
            toReturn.add("~*~");
        }

        return toReturn;
    }

    private static ObservableList<String> sorter() {
        ObservableList<String> toReturn = FXCollections.observableArrayList();
        MovieReg movieReg = new MovieReg();
        movieReg.sort();
        toReturn.add("-----------------------------------------------");
        toReturn.add("Title (Year)   ||   Genres   ||   Average Rating");
        toReturn.add("-----------------------------------------------");

        System.out.println(movieReg.getMovies().size());
        int i;
        for(i = (movieReg.getMovies().size() - 1); i > 0; i--) {
            toReturn.add(movieReg.getMovies().get(i).toString());
            toReturn.add("~*~");
        }

        return toReturn;
    }

}


