import org.junit.Test;
import static org.junit.Assert.*;

public class OrbitalUtilsTest {
    private static final double DELTA = 1e-10;  // For floating-point comparisons
    
    @Test
    public void testCircularOrbit() {
        // Test a simple circular orbit
        double mass = 1.0e24;  // Small planet mass
        double radius = 1.0e7; // 10,000 km orbit
        
        double velocity = OrbitalUtils.calculateCircularOrbitVelocity(mass, radius);
        double period = OrbitalUtils.calculateOrbitalPeriod(mass, radius);
        
        // Verify orbit is stable (centripetal force equals gravitational force)
        double centripetalForce = mass * (velocity * velocity) / radius;
        double gravForce = OrbitalConstants.GRAV_CONSTANT * mass * mass / (radius * radius);
        
        assertEquals(centripetalForce, gravForce, DELTA);
    }
    
    @Test
    public void testEscapeVelocity() {
        double mass = 1.0e24;
        double radius = 1.0e7;
        
        double escapeVel = OrbitalUtils.calculateEscapeVelocity(mass, radius);
        double circularVel = OrbitalUtils.calculateCircularOrbitVelocity(mass, radius);
        
        // Escape velocity should be sqrt(2) times circular orbit velocity
        assertEquals(escapeVel, circularVel * Math.sqrt(2), DELTA);
    }
} 