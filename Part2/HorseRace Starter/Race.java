import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author Lyiam Jeremy Mendoza
 * @version 1.0
 */
public class Race
{
    private int raceLength;
    public List<Horse> tracks;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance)
    {
        // initialise instance variables
        raceLength = distance;
        tracks = new ArrayList<>();
    }
    
    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber)
    {
        tracks.add(theHorse);
    }
    
    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace()
    {
        readFinishingTimesFromFile();
        //declare a local variable to tell us when the race is finished
        boolean finished = false;
        
        //reset all the lanes (all horses not fallen and back to 0). 
        for (Horse horse : tracks) {
            horse.goBackToStart();
            horse.setNumberOfRaces(horse.getNumberOfRaces() + 1);
        }

        double time = 0.0;
                    
        while (!finished)
        {
            time++;
            //move each horse
            for (Horse horse : tracks) {
                moveHorse(horse);
            }
                        
            //print the race positions
            printRace();
            
            //if any of the three horses has won the race is finished
            for (Horse horse : tracks) {
                if (raceWonBy(horse)) {
                    finished = true;
                    break;
                }
            }

            // if all horses have fallen, the race is finished
            boolean allHorsesFallen = true;
            for (Horse horse : tracks) {
                if (!horse.hasFallen()) {
                    allHorsesFallen = false;
                    break;
                }
            }
            if (allHorsesFallen) {
                finished = true;
            }

            //wait for 100 milliseconds
            try{ 
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(Exception e){}
        }

        Horse winner = null;
        for (Horse horse : tracks) {
            if (raceWonBy(horse)) {
                winner = horse;
                break;
            }
        }

        for (Horse horse : tracks) {
            horse.setAvgSpeed(horse.getDistanceTravelled()/time);
        }
        

        if (winner != null) {
            if (winner.getConfidence() >= 1.0) {
                winner.setConfidence(1.0);
            } else {
                winner.setConfidence(((winner.getConfidence() * 10) + 1) / 10);
            }
            winner.setNumberOfWins(winner.getNumberOfWins() + 1);
            
            // Corrected win ratio calculation using floating-point division
            winner.setWinRatio(((double) winner.getNumberOfWins() / winner.getNumberOfRaces()) * 100);
            
            for (Horse horse : tracks) {
                if (raceWonBy(horse)) {
                    horse.setFinishingTime((int)time);
                    winner = horse;
                    break;
                }
            }
            printRace();
            System.out.println("And the winner is " + winner.getName() + " with a time of : " + time + ". The horse is " + winner.getBreed() + " with a " + winner.getCoatColour() + " coat. It is equipped with " + winner.getEquipment() + " and has " + winner.getAccessories() + " as accessories.");
            displayRaceStatistics();
        }
    }
    
    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse)
    {
        //if the horse has fallen it cannot move, 
        //so only run if it has not fallen
        
        if  (!theHorse.hasFallen())
        {
            //the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence())
            {
               theHorse.moveForward();
            }
            
            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence 
            //so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
            {
                theHorse.fall();
            }
        }
    }
        
    /** 
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    public boolean raceWonBy(Horse theHorse)
    {
        if (theHorse.getDistanceTravelled() == raceLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
        System.out.print('\u000C');  //clear the terminal window
        
        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();
        
        for (Horse horse : tracks) {
            printLane(horse);
            System.out.println();
        }
        
        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();
        
    }
    
    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
        
        //print a | for the beginning of the lane
        System.out.print('|');
        
        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            System.out.print('X');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }
        
        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);
        
        //print the | for the end of the track
        System.out.print('|');
        
        System.out.print(" " + theHorse.getName() + " (Current confidence " + theHorse.getConfidence() + ")");

    }
        
    
    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }

    public void displayRaceStatistics() {
        System.out.println("\n");
        System.out.println("Race Statistics:");
        for (Horse horse : tracks) {
            System.out.println("Lane " + (tracks.indexOf(horse) + 1) + ": " + horse.getName() + " - Distance travelled: " + horse.getDistanceTravelled() + "m.");
            System.out.println("Average Speed: " + horse.getAvgSpeed() + "m/s");
            System.out.println("Best finishing Time: " + horse.getFinishingTime() + "s");
            System.out.println("Win ratio: " + horse.getWinRatio() + "%");
            System.out.println("\n");
        }
    }

    public void readFinishingTimesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("horseInfo/Horses.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] horseDetails = line.split(",");
                String horseNameFromFile = horseDetails[1];
                int finishingTimeFromFile = Integer.parseInt(horseDetails[8]);
                
                for (Horse horse : tracks) {
                    if (horse.getName().equals(horseNameFromFile)) {
                        horse.setFinishingTime(finishingTimeFromFile);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Horse getWinner(){
        Horse winner = null;
        for (Horse horse : tracks) {
            if (raceWonBy(horse)) {
                winner = horse;
                break;
            }
        }
        return winner;
    }

}
