// Philipp Lehmann, followscontent, 2015

import processing.pdf.*;
import controlP5.*;

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
float vortex_rotation = 5.0;
float moving_speed = 2.0;
float line_noise_magnitude = 0.0;
float point_noise_magnitude = 0.0;
int line_iterations = 10;
int line_points = 400;

void setup() {
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
    
void draw() {
    drawCreature();
    if(vortex_effect) {
        drawVortex();
    }
    drawHelp();
    drawControls();
}

void keyPressed() {
    // Key pressed 0-9
    if (key >= '0' && key <= '9') {
        for (int i = 0; i <= 9; i++) {
            if (i == int(str(key))) {
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

Creature newCreature() {
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


