/**
 * Write a description of class Horse here.
 * 
 * This class represents a horse with methods and attributes.
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
      
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
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
    
}
