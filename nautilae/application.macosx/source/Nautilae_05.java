import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.pdf.*; 
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Nautilae_05 extends PApplet {

// Philipp Lehmann, followscontent, 2015




// Controls
ControlP5 cp5;
Slider slider_line_iterations, slider_points, slider_speed, slider_line_noise, slider_point_noise, slider_vortex_rotation, slider_vortex_iterations;
Toggle toggle_curve, toggle_vortex;
Button button_generate, button_reset;

// Interface status
boolean show_controls = true;
boolean show_help = true;
int enable_controls = 5;

// Vectors
PVector vs1, vs2, ve1, ve2, hs1, hs2, he1, he2;

// Creatures
Creature creature_one; 

// Sketch Parameters
PImage vortex_img;
boolean vortex_effect = false;
int vortex_iterations = 5;
int sketch_size = 600;
int border = 75;
int double_border = 2 * border;

// Creature Default Properties
boolean curved_line = true;
float vortex_rotation = 5.0f;
float moving_speed = 2.0f;
float line_noise_magnitude = 0.0f;
float point_noise_magnitude = 0.0f;
int line_iterations = 10;
int line_points = 400;

public void setup() {
    // Setup the stage
    smooth();
    size(sketch_size,sketch_size);

    // Create the controls
    // Controls setup
    cp5 = new ControlP5(this);
    cp5.setColorLabel(0xff000000);
    cp5.setAutoDraw(false);
    setupControls();

    // Create the first creature
    creature_one = newCreature(); 
}
    
public void draw() {
    drawCreature();
    if(vortex_effect) {
        drawVortex();
    }
    drawHelp();
    drawControls();
}

public void keyPressed() {
    // Key pressed 0-9
    if (key >= '0' && key <= '9') {
        for (int i = 0; i <= 9; i++) {
            if (i == PApplet.parseInt(str(key))) {
                setControls(i);
            }
        }
    }

    // Recreate creature
    if (key == 'r' || key == 'R') {
        createCreature();
    }

    // Show/Hide controls
    if (key == 'c' || key == 'C') {
        toggleControls();
    }

    // Show/Hide controls
    if (key == 'h' || key == 'H') {
        show_help = !show_help;
    }
}

public void createCreature() {
    updateControlValues();
    creature_one = newCreature(); 
}

public Creature newCreature() {
    // Create the random vectors (two times: startPoint, helpStartPoint, helpEndPoint, endPoint)
    PVector vs1 = new PVector(random(width-double_border)+border, random(height-double_border)+border);
    PVector hs1 = new PVector(random(width-double_border)+border, random(height-double_border)+border);
    PVector he1 = new PVector(random(width-double_border)+border, random(height-double_border)+border);
    PVector ve1 = new PVector(random(width-double_border)+border, random(height-double_border)+border);

    PVector vs2 = new PVector(random(width-double_border)+border, random(height-double_border)+border);
    PVector hs2 = new PVector(random(width-double_border)+border, random(height-double_border)+border);
    PVector he2 = new PVector(random(width-double_border)+border, random(height-double_border)+border);
    PVector ve2 = new PVector(random(width-double_border)+border, random(height-double_border)+border);

    // Create the creature with all the parameters
    Creature temp_creature = new Creature(
                                    line_iterations, 
                                    line_points, 
                                    curved_line, 
                                    moving_speed, 
                                    line_noise_magnitude, 
                                    point_noise_magnitude, 
                                    vs1, hs1, he1, ve1, vs2, hs2, he2, ve2
                                ); 
    return temp_creature;
}


public void setupControls() {
	// Slider for the number of iterations between the vectors
    slider_line_iterations = cp5.addSlider("setLineIterations")
        .setLabel("Line iterations")
        .setValue(line_iterations)
        .setRange(1,30)
        .setPosition(20,20)
        .setSize(200,10)
        .setVisible(show_controls)
        ;

    // Slider for the number of points draw for each line
    slider_speed = cp5.addSlider("setMovingSpeed")
        .setLabel("Moving speed")
        .setValue(moving_speed)
        .setRange(0,10)
        .setPosition(20,40)
        .setSize(200,10)
        .setVisible(show_controls)
        ;

    // Slider for the number of points draw for each line
    slider_points = cp5.addSlider("setPointsPerLine")
        .setLabel("Points per line")
        .setValue(line_points)
        .setRange(2,1000)
        .setPosition(20,60)
        .setSize(200,10)
        .setVisible(show_controls)
        ;

    // Toggle for the curved line effect
    toggle_curve = cp5.addToggle("setCurve")
        .setLabel("Curved line")
        .setValue(curved_line)
        .setPosition(130,80)
        .setSize(90,10)
        .setVisible(show_controls)
       ;

    // Toggle for the vortex effect
    toggle_vortex = cp5.addToggle("setVortex")
        .setLabel("Vortex")
        .setValue(vortex_effect)
        .setPosition(20,80)
        .setSize(90,10)
        .setVisible(show_controls)
       ;

    // Slider to adjust the rotation of each vortex iteration
    slider_vortex_rotation = cp5.addSlider("setVortexRotation")
        .setLabel("Vortex Rotation")
        .setValue(vortex_rotation)
        .setRange(0,90)
        .setPosition(20,120)
        .setSize(200,10)
        .setVisible(show_controls)
        ;

    // Slider to adjust the numbers of vortex iterations
    slider_vortex_iterations = cp5.addSlider("setVortexIterations")
        .setLabel("Vortex Iterations")
        .setValue(vortex_iterations)
        .setRange(1,10)
        .setPosition(20,140)
        .setSize(200,10)
        .setVisible(show_controls)
        ;

    // Slider to adjust the amount of y-noise added to the points
    slider_line_noise = cp5.addSlider("setLineNoise")
        .setLabel("Line noise")
        .setValue(line_noise_magnitude)
        .setRange(0,200)
        .setPosition(20,160)
        .setSize(200,10)
        .setVisible(show_controls)
        ;

    // Slider to adjust the amount of y-noise added to the points
    slider_point_noise = cp5.addSlider("setPointNoise")
        .setLabel("Point noise")
        .setValue(point_noise_magnitude)
        .setRange(0,25)
        .setPosition(20,180)
        .setSize(200,10)
        .setVisible(show_controls)
        ;
    // create a new button with name 'buttonA'
    button_generate = cp5.addButton("createCreature")
        .setLabel("Create")
        .setColorLabel(0xffffffff)
        .setValue(0)
        .setPosition(width-80,20)
        .setSize(60,50)
        .setVisible(show_controls)
        ;
    // create a new button with name 'buttonA'
    button_reset = cp5.addButton("resetControlButton")
        .setLabel("Reset")
        .setColorLabel(0xffffffff)
        .setValue(0)
        .setPosition(width-80,80)
        .setSize(60,20)
        .setVisible(show_controls)
        .addCallback(new CallbackListener() {
          public void controlEvent(CallbackEvent event) {
            if (event.getAction() == ControlP5.ACTION_RELEASED) {
              resetControls();
            }
          }
        })
        ;
}

// Toggle Controls

public void setControls(int i) {
    // Set the control to a specific step
    println("Set controls to: "+i);
    hideControls();
    enable_controls = i;
    showControls();
}
public void showControls() {
    hideControls();
    toggleControls(true);
}
public void hideControls() {
    toggleControls(false);
}
public void toggleControls() {
    show_controls = !show_controls;
    toggleControls(show_controls);
}
public void toggleControls(boolean show_hide) {
    show_controls = show_hide;

    // show and hide default controls
    button_generate.setVisible(show_controls);
    button_reset.setVisible(show_controls);
    slider_line_iterations.setVisible(show_controls); 

    // Only show these controls when enabled by pressing the num keys (0-5)
    if (enable_controls > 1) slider_speed.setVisible(show_controls); 
    if (enable_controls > 2) slider_points.setVisible(show_controls); 
    if (enable_controls > 2) toggle_curve.setVisible(show_controls); 
    if (enable_controls > 3) toggle_vortex.setVisible(show_controls);
    if (enable_controls > 3) slider_vortex_rotation.setVisible(show_controls);
    if (enable_controls > 3) slider_vortex_iterations.setVisible(show_controls);
    if (enable_controls > 4) slider_line_noise.setVisible(show_controls);
    if (enable_controls > 4) slider_point_noise.setVisible(show_controls);
}

// Update parameters 
public void updateControlValues() {
    vortex_effect = PApplet.parseBoolean(PApplet.parseInt(toggle_vortex.getValue()));
    vortex_rotation = slider_vortex_rotation.getValue();
    vortex_iterations = PApplet.parseInt(slider_vortex_iterations.getValue());
    curved_line = PApplet.parseBoolean(PApplet.parseInt(toggle_curve.getValue()));
    moving_speed = slider_speed.getValue();
    line_noise_magnitude = slider_line_noise.getValue();
    line_iterations = PApplet.parseInt(slider_line_iterations.getValue());
    line_points = PApplet.parseInt(slider_points.getValue());
    point_noise_magnitude = slider_point_noise.getValue();
}


// Update parameters 
public void resetControls() {
    toggle_vortex.setValue(0);
    slider_vortex_rotation.setValue(0.0f);
    slider_vortex_iterations.setValue(0.0f);
    toggle_curve.setValue(0);
    slider_speed.setValue(0);
    slider_line_noise.setValue(0);
    slider_line_iterations.setValue(1);
    slider_points.setValue(400);
    slider_point_noise.setValue(0);

    setControls(0);
    hideControls();
    createCreature();
}
class Creature { 

    // Vector 1: Start, Handler Start, Handler End, End
    PVector vs1, hs1, he1, ve1;

    // Vector 2: Start, Handler Start, Handler End, End
    PVector vs2, hs2, he2, ve2;

    // Moving Vector
    PVector m1, m2, m3, m4;

    // Line properties and boundaries
    boolean curved_line = true;
    int line_iterations = 15;
    int line_points = 400;
    int stroke_weight = 5;

    // Noise
    float line_interpolation = 1 / PApplet.parseFloat(line_iterations);
    float line_noise_magnitude = 0.0f;
    float point_noise_magnitude = 0.0f;
    float noise_progress = 0.0f;
    float moving_speed = 3.0f;
    float decrease_rate = 0.98f;
    int noise_seed = 1;
  
    Creature (int iterations, int points, boolean curved, float speed, float line_magnitude, float point_magnitude, PVector ts1, PVector ths1, PVector the1, PVector te1, PVector ts2, PVector ths2, PVector the2, PVector te2) { 
        // Creature specific noise seed
        noise_seed = floor(random(100));
        noiseDetail(1);

        // Transfer properties to local variables
        line_iterations = iterations;
        line_points = points;
        curved_line = curved;
        moving_speed = speed;
        line_noise_magnitude = line_magnitude;
        point_noise_magnitude = point_magnitude;

        // Derived properties
        line_interpolation = 1 / PApplet.parseFloat(line_iterations);

        // Transfer vectors to local variables
        vs1 = ts1; hs1 = ths1; he1 = the1; ve1 = te1;
        vs2 = ts2; hs2 = ths2; he2 = the2; ve2 = te2;

        // Set random movement vectors
        m1 = new PVector(randomPlusMinus(moving_speed), randomPlusMinus(moving_speed));
        m2 = new PVector(randomPlusMinus(moving_speed), randomPlusMinus(moving_speed));
        m3 = new PVector(randomPlusMinus(moving_speed), randomPlusMinus(moving_speed));
        m4 = new PVector(randomPlusMinus(moving_speed), randomPlusMinus(moving_speed));
    } 

    public void update() { 
        // Update the progress of the creature
        // noiseSeed(noise_seed);
        // noise_progress = noise_progress + .01;

        // Sum up vectors
        vs1.add( m1 );
        vs2.add( m2 );
        ve1.add( m3 );
        ve2.add( m4 );
        draw();

        // Decrease movement over time
        m1.mult(decrease_rate);
        m2.mult(decrease_rate);
        m3.mult(decrease_rate);
        m4.mult(decrease_rate);
    } 

       // Draw the creature
    public void draw() {
        // Draw mode
        ellipseMode(CENTER);
        if (curved_line) {
            noStroke();
            fill(0);
        } else {
            stroke(0);
            strokeWeight(stroke_weight);
            noFill();
        }


        // Create several lines by interpolating start and end vector
        for (int i = 0; i <= line_iterations; i++) {

            // Interpolate vectors for each iteration, multiply the interpolation factor
            float p1x = lerp(vs1.x, vs2.x, i * line_interpolation);
            float p1y = lerp(vs1.y, vs2.y, i * line_interpolation);
            float h1x = lerp(hs1.x, hs2.x, i * line_interpolation);
            float h1y = lerp(hs1.y, hs2.y, i * line_interpolation);

            float p2x = lerp(ve1.x, ve2.x, i * line_interpolation);
            float p2y = lerp(ve1.y, ve2.y, i * line_interpolation);
            float h2x = lerp(he1.x, he2.x, i * line_interpolation);
            float h2y = lerp(he1.y, he2.y, i * line_interpolation);

            // Draw line as circles with different sizes
            if (curved_line) {
                for (int j = 0; j <= line_points; j++) {
                    // Interpolate circle size for each point
                    float point_interpolation = j / PApplet.parseFloat(line_points);
                    float r  = sin(point_interpolation*3) * stroke_weight;
                    float x = bezierPoint(p1x, h1x, h2x, p2x, point_interpolation);
                    float y = bezierPoint(p1y, h1y, h2y, p2y, point_interpolation);

                    y += noisePlusMinus(line_noise_magnitude, i, j, 0.01f);
                    x += noisePlusMinus(point_noise_magnitude, x, y, 1);

                    ellipse(x, y, r, r);
                }
            } else {
                // bezier(x1, y1, x2, y2, x3, y3, x4, y4)
                bezier(p1x, p1y, h1x, h1y, h2x, h2y, p2x, p2y);
            }

        }
    }

    // Perlinnoise (Amplitude, X, Y, Factor)
    public float noisePlusMinus(float amp, float x, float y, float f) {
        float n = amp*noise(y*f, x)-amp*0.5f;
        return(n);
    }


    // Random value positive or negative ()
    public float randomPlusMinus(float i) {
        // Random value that can be positive or negative
        float r = random(i)-i*0.5f;
        return r;
    }
} 
public void drawCreature() {
	// Update every frame
    blendMode(NORMAL);
    background(255);
    creature_one.update();
}

public void drawVortex() {
	// Vortex effect
    blendMode(DARKEST);
    vortex_img = get();
    background(255);

    pushMatrix();
    translate(width * 0.5f, height * 0.5f);
    for(int a = 0; a < vortex_iterations; a++) {
        scale(1 - ((float) a / vortex_iterations * 0.5f));
        rotate( radians(vortex_rotation) );
        image(vortex_img, -width * 0.5f, -height * 0.5f);
    }
    popMatrix();
}

public void drawControls() {
	// Draw controls
    blendMode(NORMAL);
    cp5.draw();
}

public void drawHelp() {
    if (show_help) {
        text("KEYS: (r) Refresh, (h) Help, (c) Controls, (1-5) Walkthrough", 20, height - 30); 
    }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Nautilae_05" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
