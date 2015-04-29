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
    float line_interpolation = 1 / float(line_iterations);
    float line_noise_magnitude = 0.0;
    float point_noise_magnitude = 0.0;
    float noise_progress = 0.0;
    float moving_speed = 3.0;
    float decrease_rate = 0.98;
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
        line_interpolation = 1 / float(line_iterations);

        // Transfer vectors to local variables
        vs1 = ts1; hs1 = ths1; he1 = the1; ve1 = te1;
        vs2 = ts2; hs2 = ths2; he2 = the2; ve2 = te2;

        // Set random movement vectors
        m1 = new PVector(randomPlusMinus(moving_speed), randomPlusMinus(moving_speed));
        m2 = new PVector(randomPlusMinus(moving_speed), randomPlusMinus(moving_speed));
        m3 = new PVector(randomPlusMinus(moving_speed), randomPlusMinus(moving_speed));
        m4 = new PVector(randomPlusMinus(moving_speed), randomPlusMinus(moving_speed));
    } 

    void update() { 
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
    void draw() {
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
                    float point_interpolation = j / float(line_points);
                    float r  = sin(point_interpolation*3) * stroke_weight;
                    float x = bezierPoint(p1x, h1x, h2x, p2x, point_interpolation);
                    float y = bezierPoint(p1y, h1y, h2y, p2y, point_interpolation);

                    y += noisePlusMinus(line_noise_magnitude, i, j, 0.01);
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
    float noisePlusMinus(float amp, float x, float y, float f) {
        float n = amp*noise(y*f, x)-amp*0.5;
        return(n);
    }


    // Random value positive or negative ()
    float randomPlusMinus(float i) {
        // Random value that can be positive or negative
        float r = random(i)-i*0.5;
        return r;
    }
} 