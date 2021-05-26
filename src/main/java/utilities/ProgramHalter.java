package utilities;

public class ProgramHalter {

    public void haltProgram(int seconds) throws InterruptedException {

        /*
        * This code pauses the execution for one minute after to give you the developer time to switch IP addresses via your VPN.
        * Given the 51 vulnerability fixes for Tomcat 7, this pause would happen five times.
        * This mechanism seeks to avoid bombarding SVN servers with too many requests in a short while, which would make the servers automatically ban your IP address.
        */

        int pauseTime = seconds * 1000;
        System.out.println("Pausing for " + pauseTime + " seconds." + " Consider switching to another IP address via your VPN is there is enough time.");
        Thread.sleep(seconds * 1000);
        System.out.println("Program execution continuing...");
    }
}
