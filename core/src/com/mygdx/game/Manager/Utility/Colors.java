package com.mygdx.game.Manager.Utility;

import com.badlogic.gdx.graphics.Color;

public class Colors {
    // UI Colors - using dimmer white as default
    public static final Color UI_WHITE = new Color(0.8f, 0.8f, 0.8f, 1f);
    
    // Compass Colors
    public static final Color COMPASS_BACKGROUND = new Color(60f/255f, 60f/255f, 60f/255f, 1f);
    public static final Color COMPASS_GRID = new Color(45f/255f, 45f/255f, 45f/255f, 1f);
    public static final Color COMPASS_BORDER = UI_WHITE;
    
    // Throttle Colors
    public static final Color THROTTLE_INDICATOR = new Color(255f/255f, 131f/255f, 0f/255f, 1f);
    public static final Color THROTTLE_TICKS = UI_WHITE;
    
    // Text Colors
    public static final Color TEXT_DEFAULT = UI_WHITE;
    public static final Color TEXT_WARNING = new Color(1f, 0.8f, 0f, 1f);
    public static final Color TEXT_ERROR = new Color(1f, 0.2f, 0.2f, 1f);

    
    // Helper method to get transparent version of any color
    public static Color getTransparent(Color color, float alpha) {
        return new Color(color.r, color.g, color.b, alpha);
    }

    // Orbit Path Colors
    public static final Color PATH_DANGEROUS = new Color(1f, 0f, 0f, 1f);  // Red for dangerous orbits
    public static final Color PATH_ESCAPE = new Color(255f/256f, 170f/256f, 3f/256f, 1f);  // Orange for escape trajectories
    public static final Color PATH_NORMAL = new Color(0f, 1f, 0f, 1f);  // Green for normal orbits


    
    // PathKlob Colors
    public static final Color PATHKLOB_CIRCLE = new Color(1f, 0f, 0f, 1f);  // Red circle
    public static final Color PATHKLOB_PATH = new Color(133f/256f, 36f/256f, 201f/256f, 1f);  // Purple path

    // Celestial Body Colors and Fade Values
    public static final Color CBODY_PATH = new Color(0.8f, 0.8f, 0.8f, 1f);  // Dimmed white to match UI
    public static final Color CBODY_DEFAULT_CIRCLE = new Color(1f, 0f, 0f, 1f);  // Default red circle
    
    // Fade constants
    public static final float CBODY_PATH_MAX_FADE = 0.8f;  // fPathMax
    public static final float CBODY_CIRCLE_MAX_FADE = 0.75f;  // fCirleMax
    
    // Planet Circle Colors
    public static final Color FIJI_CIRCLE = new Color(82f/255f, 9f/255f, 117f/255f, 1f);    // Purple
    public static final Color KERBIN_CIRCLE = new Color(0f/255f, 30f/255f, 255f/255f, 1f);  // Blue
    public static final Color MINMUS_CIRCLE = new Color(119f/255f, 130f/255f, 118f/255f, 1f); // Grey-Green
    public static final Color MUN_CIRCLE = new Color(107f/255f, 107f/255f, 107f/255f, 1f);   // Grey
    public static final Color HACOBO_CIRCLE = new Color(56f/255f, 207f/255f, 33f/255f, 1f);  // Green
    public static final Color NARS_CIRCLE = new Color(173f/255f, 74f/255f, 11f/255f, 1f);    // Orange-Red
    public static final Color SUN_CIRCLE = new Color(219f/255f, 216f/255f, 42f/255f, 1f);    // Yellow
    
    // Orbital HUD Colors
    public static final Color ORBITAL_PATH = new Color(1f, 0f, 0f, 1f);  // Red orbital path
    
    // Background Colors
    public static final Color BACKGROUND_STAR = new Color(1f, 1f, 1f, 0.5f);  // Semi-transparent white for stars
    public static final Color BACKGROUND_EMPTY = new Color(0f, 0f, 0f, 0f);   // Transparent black for space
    
    // Helper method to convert Color to int array for legacy support
    public static int[] colorToIntArray(Color color) {
        return new int[] {
            (int)(color.r * 255),
            (int)(color.g * 255),
            (int)(color.b * 255)
        };
    }

} 