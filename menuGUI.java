import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class menuGUI {

    static JTextField distanceField;
    
    public static void main(String[] args) {
        
        //use existing horses button
        JButton playButton = new JButton("Use existing horses");
        playButton.setSize(playButton.getPreferredSize());
        playButton.setLocation(200, 200);
        playButton.addActionListener(e -> {
            try {
                useExistingHorses();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //create a new horse button
        JButton createButton = new JButton("Create new horse");
        createButton.setSize(createButton.getPreferredSize());
        createButton.setLocation(50, 200);
        createButton.addActionListener(e -> {
            try {
                createNewHorse();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //display horse stats button
        JButton statsButton = new JButton("Display Race Statistics");
        statsButton.setSize(statsButton.getPreferredSize());
        statsButton.setLocation(400, 200);
        statsButton.addActionListener(e -> {
            try {
                displayAllRaceStatistics();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //betting button
        JButton betButton = new JButton("Place Bet");
        betButton.setSize(betButton.getPreferredSize());
        betButton.setLocation(600, 200);
        betButton.addActionListener(e -> {
            try {
                placeBet();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //distance text field
        distanceField = new JTextField();
        distanceField.setBounds(400, 150, 100, 30);
        JLabel distanceLabel = new JLabel("Enter the distance:");
        distanceLabel.setBounds(250, 150, 150, 30);

        //frame
        JFrame frame = new JFrame("Horse race");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.add(playButton);
        frame.add(createButton);
        frame.add(statsButton);
        frame.add(betButton);
        frame.add(distanceField);
        frame.add(distanceLabel);
        frame.setSize(800, 300);
        frame.setVisible(true);

        frame.getContentPane().setBackground(Color.green);
        playButton.setBackground(Color.yellow);
        createButton.setBackground(Color.yellow);
        statsButton.setBackground(Color.yellow);
        betButton.setBackground(Color.yellow);
        distanceField.setBackground(Color.yellow);
    }

    //returns number of horses in Horses.txt by counting rows
    public static int getNumberOfLanes() {
        int numberOfLanes = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("horseInfo/Horses.txt"))) {
            while ((reader.readLine()) != null) {
                numberOfLanes++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numberOfLanes;
    }

    //method for running race
    public static void useExistingHorses() throws IOException {

        int numberOfLanes = getNumberOfLanes();
        int distance = Integer.parseInt(distanceField.getText());

        Race race = new Race(distance);
    
        //reads horses and their info from Horses.txt
        try (BufferedReader reader = new BufferedReader(new FileReader("horseInfo/Horses.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] horseDetails = line.split(",");
                Horse horse = new Horse(horseDetails[0].charAt(0), horseDetails[1], Double.parseDouble(horseDetails[2]), horseDetails[3], horseDetails[4], horseDetails[5], horseDetails[6]);
    
                // Set additional statistics to java object
                horse.setAvgSpeed(Double.parseDouble(horseDetails[7]));
                horse.setFinishingTime(Integer.parseInt(horseDetails[8]));
                horse.setNumberOfRaces(Integer.parseInt(horseDetails[10]));
                horse.setNumberOfWins(Integer.parseInt(horseDetails[11]));
    
                // Calculate win ratio
                if (horse.getNumberOfRaces() > 0) {
                    horse.setWinRatio(((double) horse.getNumberOfWins() / horse.getNumberOfRaces()) * 100); // Calculate win ratio as a percentage
                } else {
                    horse.setWinRatio(0); // Set win ratio to 0 if no races have been run
                }
    
                race.addHorse(horse, numberOfLanes); //adds horse to the race
                numberOfLanes--;//allows for the horses to be placed in the correct lane
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        race.startRace();//starts race
        updateHorseInfo(race);//updates horse info at the end of the race
    }

    //method for creating a new horse
    public static void createNewHorse() throws IOException {
        int numberOfLanes = getNumberOfLanes();

        String horseDetails = JOptionPane.showInputDialog("Enter horse details in the format: symbol,name,confidence,breed,coat-colour,equipment,accessories:");
        String[] details = horseDetails.split(",");
  
        if (details.length >= 6) {//if what was entered by the user is correct it adds it to the end of the Horses.txt file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("horseInfo/Horses.txt", true))) {
                writer.write("\n" + horseDetails + "," + (numberOfLanes + 1));
            } catch (IOException e) {
                e.printStackTrace();
            }
  
            int distance = Integer.parseInt(distanceField.getText());
  
            Race race = new Race(distance);
  
            try (BufferedReader reader = new BufferedReader(new FileReader("horseInfo/Horses.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] horseDetails2 = line.split(",");
                    Horse horse = new Horse(horseDetails2[0].charAt(0), horseDetails2[1], Double.parseDouble(horseDetails2[2]), horseDetails2[3], horseDetails2[4], horseDetails2[5], horseDetails2[6]);
                    race.addHorse(horse, numberOfLanes);
                    numberOfLanes--;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
  
            race.startRace();
            updateHorseInfo(race);//it then does the same as useExistingHorses() but with the new horse in the file
  
        } else {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter all details for the horse.");
        }
    }
  
    //updates the horse info at the end of the race
    public static void updateHorseInfo(Race race) {
        List<String> lines = new ArrayList<>();
        
        Map<String, Integer> fastestTimes = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("horseInfo/Horses.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                String horseName = details[1];
                
                // Store the fastest time for each horse into the hashmap
                if (!fastestTimes.containsKey(horseName) || Integer.parseInt(details[8]) < fastestTimes.get(horseName)) {
                    fastestTimes.put(horseName, Integer.parseInt(details[8]));
                }
                
                for (Horse horse : race.tracks) {
                    if (horse.getName().equals(horseName)) {
                        details[2] = String.valueOf(horse.getConfidence());
                        details[7] = String.valueOf(horse.getAvgSpeed());
                        
                        // Only update the finishing time if it's the fastest
                        if (horse.getFinishingTime() <= fastestTimes.get(horseName)) {
                            details[8] = String.valueOf(horse.getFinishingTime());
                        }else{
                            details[8] = String.valueOf(fastestTimes.get(horseName));
                        }

                        details[9] = String.format("%.2f", horse.getWinRatio()); // Format to two decimal places
                        details[10] = String.valueOf(horse.getNumberOfRaces());
                        details[11] = String.valueOf(horse.getNumberOfWins());
                        break;
                    }
                }
                lines.add(String.join(",", details));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("horseInfo/Horses.txt"))) {
            int size = lines.size();
            for (int i = 0; i < size; i++) {
                writer.write(lines.get(i));
                if (i < size - 1) {
                    writer.newLine();
                }//adds all the updated info to the file
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //method to read horses from file
    public static List<Horse> readHorsesFromFile() throws IOException {
        List<Horse> horses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("horseInfo/Horses.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] horseDetails = line.split(",");
                char horseSymbol = horseDetails[0].charAt(0);
                String horseName = horseDetails[1];
                double horseConfidence = Double.parseDouble(horseDetails[2]);
                String breed = horseDetails[3];
                String coatColour = horseDetails[4];
                String equipment = horseDetails[5];
                String accessories = horseDetails[6];

                Horse horse = new Horse(horseSymbol, horseName, horseConfidence, breed, coatColour, equipment, accessories);
                
                // Set additional statistics
                horse.setAvgSpeed(Double.parseDouble(horseDetails[7]));
                horse.setFinishingTime(Integer.parseInt(horseDetails[8]));
                horse.setNumberOfRaces(Integer.parseInt(horseDetails[10]));
                horse.setNumberOfWins(Integer.parseInt(horseDetails[11]));

                // Calculate win ratio
                if (horse.getNumberOfRaces() > 0) {
                    horse.setWinRatio(((double) horse.getNumberOfWins() / horse.getNumberOfRaces()) * 100); // Calculate win ratio as a percentage
                } else {
                    horse.setWinRatio(0); // Set win ratio to 0 if no races have been run
                }

                horses.add(horse);
                //reads horses from file and sets the info to the horses object 
            }
        }

        return horses;//returns the horses from the file
    }

    //method to display the horses stats
    public static void displayAllRaceStatistics() throws IOException {
        List<Horse> horses = readHorsesFromFile();

        //creates frame
        JFrame statsFrame = new JFrame("Race Statistics");
        statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statsFrame.setLayout(new GridLayout(horses.size() + 1, 1));

        // Add labels for column headers
        JLabel header1 = new JLabel("Horse Name");
        JLabel header3 = new JLabel("Average Speed (m/s)");
        JLabel header4 = new JLabel("Best Finishing Time (s)");
        JLabel header5 = new JLabel("Win Ratio (%)");

        statsFrame.add(header1);
        statsFrame.add(header3);
        statsFrame.add(header4);
        statsFrame.add(header5);

        for (Horse horse : horses) {
            JLabel horseLabel = new JLabel(horse.getName());
            JLabel avgSpeedLabel = new JLabel(String.format("%.2f", horse.getAvgSpeed()));
            JLabel finishingTimeLabel = new JLabel(String.valueOf(horse.getFinishingTime()));
            JLabel winRatioLabel = new JLabel(String.format("%.2f", horse.getWinRatio()));

            statsFrame.add(horseLabel);
            statsFrame.add(avgSpeedLabel);
            statsFrame.add(finishingTimeLabel);
            statsFrame.add(winRatioLabel);//adds the info for every horse to the frame
        }

        statsFrame.pack();//sets the frame to wrap the contents
        statsFrame.setVisible(true);

        statsFrame.getContentPane().setBackground(Color.green);
    }

    //method to create bet frame
    public static void placeBet() throws IOException {
        
        //creates the frame
        JFrame betFrame = new JFrame("Place Bet");
        betFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        betFrame.setLayout(new GridLayout(6, 2));
    
        //creates dropdown menu for horses
        JLabel nameLabel = new JLabel("Select Horse:");
        JComboBox<String> horseDropdown = new JComboBox<>();
    
        List<Horse> horses = readHorsesFromFile();
        for (Horse horse : horses) {
            horseDropdown.addItem(horse.getName());//adds the horses to the dropdown menu
        }
    
        JLabel winsLabel = new JLabel("Wins:");
        JTextField winsField = new JTextField();
        winsField.setEditable(false);
    
        JLabel lossesLabel = new JLabel("Losses:");
        JTextField lossesField = new JTextField();
        lossesField.setEditable(false);
    
        JLabel moneyLabel = new JLabel("Available Money:");
        JTextField moneyField = new JTextField();
        moneyField.setEditable(false);
        //shows user bet history
    
        JLabel betAmountLabel = new JLabel("Enter Bet Amount:");
        JTextField betAmountField = new JTextField();
    
        JButton betButton = new JButton("Place Bet");
    
        // Read user's betting history from Bets.txt
        try (BufferedReader reader = new BufferedReader(new FileReader("horseInfo/Bets.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                String[] betDetails = line.split(",");
                winsField.setText(betDetails[0]);
                lossesField.setText(betDetails[1]);
                moneyField.setText(betDetails[2]);
            }
        }
    
        betFrame.add(nameLabel);
        betFrame.add(horseDropdown);
        betFrame.add(winsLabel);
        betFrame.add(winsField);
        betFrame.add(lossesLabel);
        betFrame.add(lossesField);
        betFrame.add(moneyLabel);
        betFrame.add(moneyField);
        betFrame.add(betAmountLabel);
        betFrame.add(betAmountField);
        betFrame.add(betButton);
    
        betFrame.pack();
        betFrame.setVisible(true);

        betFrame.getContentPane().setBackground(Color.green);
        betButton.setBackground(Color.yellow);
        horseDropdown.setBackground(Color.yellow);
        winsField.setBackground(Color.yellow);
        lossesField.setBackground(Color.yellow);
        moneyField.setBackground(Color.yellow);
        betAmountField.setBackground(Color.yellow);
    
        betButton.addActionListener(e -> {
            String selectedHorse = (String) horseDropdown.getSelectedItem();
    
            try {
                int betAmount = Integer.parseInt(betAmountField.getText());
                int availableMoney = Integer.parseInt(moneyField.getText());
                
                // Check if the bet amount is valid
                if (betAmount <= 0) {
                    JOptionPane.showMessageDialog(null, "Invalid bet amount. Please enter a positive number.");
                    return;
                }
    
                // Check if the bet amount exceeds available money
                if (betAmount > availableMoney) {
                    JOptionPane.showMessageDialog(null, "You don't have enough money to place this bet.");
                    return;
                }
    
                placeBet(selectedHorse, betAmount, winsField, lossesField, moneyField);//runs the actual method to create the bet
                
                // Close the bet frame
                betFrame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid bet amount. Please enter a valid number.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error placing bet. Please try again.");
            }
        });
    }
    
    //method to place the bet
    public static void placeBet(String selectedHorse, int betAmount, JTextField winsField, JTextField lossesField, JTextField moneyField) throws IOException {
        // Determine the winning horse
        List<Horse> horses = readHorsesFromFile();
        Race race = new Race(Integer.parseInt(distanceField.getText()));
        for (Horse horse : horses) {
            race.addHorse(horse, Integer.parseInt(distanceField.getText()));
        }
        race.startRace();
        updateHorseInfo(race);
    
        Horse winningHorse = race.getWinner();
    
        int wins = Integer.parseInt(winsField.getText());
        int losses = Integer.parseInt(lossesField.getText());
        int money = Integer.parseInt(moneyField.getText());
    
        if (winningHorse != null && winningHorse.getName().equals(selectedHorse)) {
            wins++;
            money += betAmount;
        } else {
            losses++;
            money -= betAmount;
        }
    
        // Write updated stats to Bets.txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("horseInfo/Bets.txt"))) {
            writer.write(wins + "," + losses + "," + money);
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Update the text fields with the new stats
        winsField.setText(String.valueOf(wins));
        lossesField.setText(String.valueOf(losses));
        moneyField.setText(String.valueOf(money));
    }
}