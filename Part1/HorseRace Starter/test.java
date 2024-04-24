public class test {
  public static void main(String[] args) {
      Horse horse1 = new Horse('\u2658', "PIPPI LONGSTOCKING", 0.6);
      Horse horse2 = new Horse('\u265E', "KOKOMO", 0.5);
      Horse horse3 = new Horse('\u2654', "EL JEFE", 0.4);

      Race race = new Race(10);
      race.addHorse(horse1, 1);
      race.addHorse(horse2, 2);
      race.addHorse(horse3, 3);
      race.startRace();

      //horse 1 tests
      // System.out.println(horse1.getName());
      // System.out.println(horse1.getSymbol());
      // System.out.println(horse1.getConfidence());
      // System.out.println(horse1.getDistanceTravelled());
      // System.out.println(horse1.hasFallen());
      // System.out.println("\n");

      // //horse 2 tests
      // System.out.println(horse2.getName());
      // System.out.println(horse2.getSymbol());
      // System.out.println(horse2.getConfidence());
      // System.out.println(horse2.getDistanceTravelled());
      // System.out.println(horse2.hasFallen());
      // System.out.println("\n");

      // //horse 3 tests
      // System.out.println(horse3.getName());
      // System.out.println(horse3.getSymbol());
      // System.out.println(horse3.getConfidence());
      // System.out.println(horse3.getDistanceTravelled());
      // System.out.println(horse3.hasFallen());
      // System.out.println("\n");

      // Horse horse4 = new Horse('H', "horse4", 0.5);

      // //horse 4 tests
      // horse4.fall();
      // horse4.moveForward();
      // System.out.println(horse4.getDistanceTravelled());
      // horse4.goBackToStart();
      // horse4.setConfidence(0.7);
      // horse4.setSymbol('h');

      // System.out.println(horse4.getName());
      // System.out.println(horse4.getSymbol());
      // System.out.println(horse4.getConfidence());
      // System.out.println(horse4.getDistanceTravelled());
      // System.out.println(horse4.hasFallen());
      // System.out.println("\n");

      // Horse horse5 = new Horse('T', "horse5", 2);
  }
}
