package com.mygdx.game.Manager.Physics.orbital;

public class OrbitalUtils {
    public static double calculateEscapeVelocity(double centralMass, double distance) {
        return Math.sqrt((2 * OrbitalConstants.GRAV_CONSTANT * centralMass) / distance);
    }

    public static double findFocus(double semiMajor, double semiMinor) {
        return Math.sqrt(Math.pow(semiMajor, 2) - Math.pow(semiMinor, 2));
    }

    // Add other utility methods currently scattered in classes
} 