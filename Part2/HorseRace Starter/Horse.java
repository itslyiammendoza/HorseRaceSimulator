
/**
 * Write a description of class Horse here.
 * 
 * @author Lyiam Jeremy Mendoza
 * @version 1.0
 */

class CustomException extends Exception {
    CustomException(String message) {
        super(message);
}

    public String getMessage() {
        return "Custom Exception: " + super.getMessage();
    }
}

public class Horse
{
    //Fields of class Horse
    
    private String horseName;
    private char horseSymbol;
    private double horseConfidence;
    private int distanceTravelled;
    private boolean hasFallen;

    private String breed;
    private String coatColour;
    private String equipment;
    private String accessories;

    private int lane;

    private double avgSpeed;
    private int finishingTime;
    private double winRatio;
    private int numberOfRaces;
    private int numberOfWins;

      
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence, String breed, String coatColour, String equipment, String accessories)
    {
        this.horseSymbol = horseSymbol;
        this.horseName = horseName;
        try{
            if(horseConfidence < 0 || horseConfidence > 1) {
                throw new CustomException("Horse confidence must be a decimal between 0 and 1.");
            }
            this.horseConfidence = horseConfidence;
        }catch(CustomException e) {
            System.out.println(e.getMessage());
            this.horseConfidence = 0.5;
        }
        this.distanceTravelled = 0;
        this.hasFallen = false;

        this.breed = breed;
        this.coatColour = coatColour;
        this.equipment = equipment;
        this.accessories = accessories;

        this.avgSpeed = 0.0;
        this.finishingTime = 0;
        this.winRatio = 0.0;
        this.numberOfRaces = 0;
        this.numberOfWins = 0;
    }
    
    //Other methods of class Horse
    public void fall()
    {
        this.horseConfidence = (((this.horseConfidence * 10) -1 ) / 10);
        this.hasFallen = true;
        
        // Ensure confidence doesn't go below 0
        if (this.horseConfidence < 0) {
            this.horseConfidence = 0;
        }
    }
    
    public double getConfidence()
    {
        return this.horseConfidence;
    }
    
    public int getDistanceTravelled()
    {
        return this.distanceTravelled;
    }
    
    public String getName()
    {
        return this.horseName;
    }
    
    public char getSymbol()
    {
        return this.horseSymbol;
    }
    
    public void goBackToStart()
    {
        this.distanceTravelled = 0;
    }
    
    public boolean hasFallen()
    {
        if (this.hasFallen == true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void moveForward()
    {
        this.distanceTravelled++;
    }

    public void setConfidence(double newConfidence)
    {
        this.horseConfidence = newConfidence;
    }
    
    public void setSymbol(char newSymbol)
    {
        this.horseSymbol = newSymbol;
    }

    public String getBreed() {
        return breed;
    }

    public String getCoatColour() {
        return coatColour;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getAccessories() {
        return accessories;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setCoatColour(String coatColour) {
        this.coatColour = coatColour;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setAccessories(String accessories) {
        this.accessories = accessories;
    }
    
    public int getLane() {
        return this.lane;
    }

    public double getAvgSpeed() {
        return this.avgSpeed;
    }

    public int getFinishingTime() {
        return this.finishingTime;
    }

    public double getWinRatio() {
        return this.winRatio;
    }

    public int getNumberOfRaces() {
        return this.numberOfRaces;
    }

    public int getNumberOfWins() {
        return this.numberOfWins;
    }

    public double setAvgSpeed(double avgSpeed) {
        return this.avgSpeed = avgSpeed;
    }

    public int setFinishingTime(int finishingTime) {
        return this.finishingTime = finishingTime;
    }

    public double setWinRatio(double winRatio) {
        this.winRatio = winRatio;
        return this.winRatio;
    }

    public int setNumberOfRaces(int numberOfRaces) {
        return this.numberOfRaces = numberOfRaces;
    }

    public int setNumberOfWins(int numberOfWins) {
        return this.numberOfWins = numberOfWins;
    }
}
