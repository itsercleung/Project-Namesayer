package namesayer.util;

import javafx.collections.ObservableList;
import namesayer.PractiseController;
import org.controlsfx.control.Rating;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * RatingManager - Allows for updating and getting current values of ratings from calling methods with appropriate values
 * such as retrieving the int position of the current name.
 * It must be that each RatingManager object must have such a selectedList and rating component.
 */
public class RatingManager {
    private ObservableList<Name> selectedList;
    private PractiseController practiseController;
    private Rating audioRating;

    public RatingManager(ObservableList<Name> selectedList, PractiseController practiseController, Rating audioRating) {
        this.selectedList = selectedList;
        this.practiseController = practiseController;
        this.audioRating = audioRating;
    }

    //Update editable rating component
    public ObservableList<Name> ratingUpdate() {
        //If txt doesnt exist then make one and append TITLE
        File pqFile = new File("./data/ratingAudio.txt");
        if (!pqFile.exists()) {
            try {
                pqFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Reading ratingAudio.txt to get values of ratings
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("./data/ratingAudio.txt"));
            String currentAudio = new String(bytes);

            //Once current audio is found in txt, extract its rating and update for audioRating component.
            for (int i = 0; i < selectedList.size(); i++) {
                if (currentAudio.contains(practiseController.getNamePlaylist().get(i).toString() + ".wav")) {
                    Scanner scanner = new Scanner(currentAudio);
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if (line.substring(0, line.length() - 4).equals(practiseController.getNamePlaylist().get(i).toString() + ".wav")) {
                            double rating = Double.parseDouble(line.substring(line.length() - 3));
                            selectedList.get(i).getRating().setRating(rating); //Set column rating
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return selectedList;
    }

    //Update rating if user makes rate of specific audio row
    public String ratingPressed(String rating, int currentNameNum) {
        //Append the listed poorQualityAudio if the audio hasn't been rated already
        try {
            FileWriter writer = new FileWriter("./data/ratingAudio.txt", true);
            byte[] bytes = Files.readAllBytes(Paths.get("./data/ratingAudio.txt"));
            String currentAudio = new String(bytes);
            String currSelectedName = practiseController.getNamePlaylist().get(currentNameNum).toString() + ".wav";

            //Makes new line if audio has no existing rating otherwise overwrite rating
            if (!currentAudio.contains(currSelectedName)) {
                writer.write(currSelectedName + "-" + rating + "\n");
                writer.close();
            } else if (currentAudio.contains(currSelectedName)) {
                String newName = currSelectedName + "-" + rating;
                Scanner scanner = new Scanner(currentAudio);
                int lineCount = 0;

                //Creating list to append to specific line
                Path path = Paths.get("./data/ratingAudio.txt");
                List<String> lines = Files.readAllLines(path);

                //Find line of audio and replace it with different rating
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.length() == newName.length()) {
                        if (line.substring(0, line.length() - 3).equals(newName.substring(0, newName.length() - 3))) {
                            lines.set(lineCount, newName);
                            Files.write(path, lines);
                        }
                    }
                    lineCount++;
                }
                scanner.close();

                return currSelectedName;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Update rating change on rateable component
    public void updateRatingComponent(int currentNameNum) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("./data/ratingAudio.txt"));
            String currentAudio = new String(bytes);
            String currentPlay = practiseController.getNamePlaylist().get(currentNameNum).toString() + ".wav";

            if (currentAudio.contains(currentPlay)) {
                Scanner scanner = new Scanner(currentAudio);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.substring(0, line.length() - 4).equals(currentPlay)) {
                        double rating = Double.parseDouble(line.substring(line.length() - 3));
                        audioRating.setRating(rating); //Set adjustable rating
                    }
                }
            }
            else {
                audioRating.setRating(0.0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
