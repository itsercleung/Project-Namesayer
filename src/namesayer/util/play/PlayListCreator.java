package namesayer.util.play;

import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import namesayer.util.Name;
import org.controlsfx.control.Rating;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * PlayListCreator: creates a list of all selected names from txt input and search input from users Practice Controller panel.
 * Deals with making appropriate single and concat names as well as dealing with making the audio files
 */
public class PlayListCreator {
    private ObservableList<String> selectedNameList;
    private List<Name> namePlaylist = new ArrayList<>();
    private List<PlayAudio> playAudioList = new ArrayList<>();

    public PlayListCreator(ObservableList<String> selectedNameList) {
        this.selectedNameList = selectedNameList;
    }

    public List<Name> getNamePlaylist() {
        return namePlaylist;
    }

    public List<PlayAudio> getPlayAudioList() {
        return playAudioList;
    }

    /**
     * makeRating: When user sets rating on a name then makeRating set
     *
     * @param rate : default rate to setup (existing and new rate)
     * @return returns Rating component to display on PlayController
     */
    private Rating makeRating(double rate) {
        Rating rating = new Rating();
        rating.setOrientation(Orientation.HORIZONTAL);
        rating.setUpdateOnHover(false);
        rating.setPartialRating(false);
        rating.setRating(rate);
        rating.setDisable(true);
        return rating;
    }

    /**
     * Makes playlist from selectedList names:
     * (1) Use name to find all audio files associating with name
     * (2) Get list of all audio files associating with name
     * (3) Create a Name object for each element in list
     * (4) Put into namePlayList to PLAY
     */
    public void makePlayList() {
        //Getting names of all name files in data/names
        File namesFolder = new File("./data/names");
        File[] listOfNames = namesFolder.listFiles();

        //(1) Get all name files with same name as selectedName
        for (String name : selectedNameList) {
            if (name.contains("[COMBINE]: ")) {
                name = name.replace("[COMBINE]: ", "");
            }

            List<String> listOfSameName = new ArrayList<>(); //Duplicate names for each entry
            for (File file : listOfNames) {
                String fileName = file.getName();
                String[] parts = fileName.split("_");
                int extIndex = parts[3].indexOf(".");

                //If equal add to current List of same name
                if (name.equals(parts[3].substring(0, extIndex).toLowerCase())) {
                    listOfSameName.add(fileName);
                }
                //If name is combination split into parts to compare
                else if (name.contains(" ")) {
                    String[] names = name.split(" ");
                    for (String combName : names) {
                        if (combName.toLowerCase().equals(parts[3].substring(0, extIndex).toLowerCase())) {
                            listOfSameName.add(fileName);
                        }
                    }
                }
            }

            //(2) Look at existing ranks and pick the better ranks of names to play
            List<String> listOfHRNames = new ArrayList<>();
            try {
                byte[] bytes = Files.readAllBytes(Paths.get("./data/ratingAudio.txt"));
                String currentAudio = new String(bytes);
                if (!listOfSameName.isEmpty() && listOfSameName.size() > 1) {

                    //Go through current listOfSameName and find ranks from 3-5 or unranked (0) to filter
                    for (String currName : listOfSameName) {
                        if (currentAudio.contains(currName)) {
                            Scanner scanner = new Scanner(currentAudio);
                            while (scanner.hasNextLine()) {
                                String line = scanner.nextLine();
                                if (line.substring(0, line.length() - 4).equals(currName)) {
                                    double rating = Double.parseDouble(line.substring(line.length() - 3));
                                    //If 3,4,5 OR 0 then accept as high rank
                                    if (rating >= 3 || rating == 0) {
                                        listOfHRNames.add(currName);
                                    }
                                }
                            }
                        }
                        //Name audio isn't in rating system, thus treat as UNRANKED
                        else {
                            listOfHRNames.add(currName);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //(3a) If list has two different names (randomly select one for each name)
            String[] names = name.split(" ");
            if (names.length > 1) {
                List<List<String>> allHRNameList = new ArrayList<>(); //Stores lists of each name with high rank (HR) OR if no HR then add all
                List<String> chosenCombNames = new ArrayList<>(); //Randomly chosen list
                for (String combName : names) {

                    //Get all HR names associated with the name
                    List<String> HRNameList = new ArrayList<>();
                    for (String rankNames : listOfHRNames) {
                        if (combName.toLowerCase().equals(rankNames.substring(rankNames.lastIndexOf("_") + 1, rankNames.lastIndexOf(".")).toLowerCase())) {
                            HRNameList.add(rankNames);
                        }
                    }

                    //If name has no HR associated then get any of its duplicates
                    if (HRNameList.isEmpty()) {
                        int indexLR = 0;
                        for (String string : listOfSameName) {
                            if (string.toLowerCase().contains(combName.toLowerCase())) {
                                HRNameList.add(listOfSameName.get(indexLR));
                                break;
                            }
                            indexLR++;
                        }
                    }
                    allHRNameList.add(HRNameList);
                }

                //Combine the two names into one list (chosen randomly if a name has duplicates)
                for (List<String> list : allHRNameList) {
                    Random random = new Random();
                    String chosenName = list.get(random.nextInt(list.size()));
                    chosenCombNames.add(chosenName);
                }

                //Make Name object and add to selectionList (combination)
                String chosenName = chosenCombNames.get(0);
                String[] parts = chosenName.split("_");
                parts[3] = parts[3].substring(0, parts[3].lastIndexOf("."));
                for (int i = 1; i < chosenCombNames.size(); i++) {
                    chosenName = chosenCombNames.get(i);
                    String[] addParts = chosenName.split("_");
                    parts[0] = parts[0] + " " + addParts[0];
                    parts[1] = parts[1] + " " + addParts[1];
                    parts[2] = parts[2] + " " + addParts[2];
                    parts[3] = parts[3] + " " + addParts[3].substring(0, addParts[3].lastIndexOf("."));
                }
                namePlaylist.add(new Name(parts[3], parts[0], parts[1], parts[2], makeRating(0)));
            }
            //(3b) - (4) If listOfSameName is not empty or has an item, select a random file according to ranking
            else {
                if (!listOfHRNames.isEmpty()) {
                    Random random = new Random();
                    String chosenName = listOfHRNames.get(random.nextInt(listOfHRNames.size()));
                    String[] parts = chosenName.split("_");
                    int extIndex = parts[3].indexOf(".");
                    namePlaylist.add(new Name(parts[3].substring(0, extIndex), parts[0], parts[1], parts[2], makeRating(0))); //Make new Name object according to selected name file
                }
                //Else there is no high rank for following name, thus get any name in previous list
                else {
                    String chosenName = listOfSameName.get(0);
                    String[] parts = chosenName.split("_");
                    int extIndex = parts[3].indexOf(".");
                    namePlaylist.add(new Name(parts[3].substring(0, extIndex), parts[0], parts[1], parts[2], makeRating(0)));
                }
            }
        }

        //Sort namePlayList (can be randomized if user toggles)
        if (namePlaylist.size() > 0) {
            Collections.sort(namePlaylist, new Comparator<Name>() {
                @Override
                public int compare(final Name object1, final Name object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
        }
    }

    /**
     * filterSelectedVideos: Gets all filteredVideos and creates selected videos to be filtered through PlayAudio class
     */
    public void filterSelectedVideos() {
        for (Name playName : namePlaylist) {
            String nameAudio = playName.toString() + ".wav";

            //If current name isn't combination
            if (nameAudio.contains(" ")) {
                List<String> combineNameList = new ArrayList<>();
                String[] hypParts = nameAudio.split("_");
                String[] spcParts = hypParts[3].split(" ");
                int nameCount = spcParts.length;

                //Reformat and place into combineNameList
                for (int i = 0; i < nameCount; i++) {
                    StringBuilder name = new StringBuilder();
                    for (String part : hypParts) {
                        String[] spaceParts = part.split(" ");
                        name.append("_").append(spaceParts[i]);
                    }
                    if (name.toString().contains(".wav")) {
                        name = new StringBuilder(name.substring(0, name.lastIndexOf(".")));
                    }
                    combineNameList.add(name.substring(1));
                }

                //Run appropriate methods with given name list and play
                PlayAudio playAudio = new PlayAudio(combineNameList, nameAudio);
                playAudio.filterCombinedAudio();
                playAudioList.add(playAudio);
            }
        }
    }
}
