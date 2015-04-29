void drawCreature() {
	// Update every frame
    blendMode(NORMAL);
    background(255);
    creature_one.update();
}

void drawVortex() {
	// Vortex effect
    blendMode(DARKEST);
    vortex_img = get();
    background(255);

    pushMatrix();
    translate(width * 0.5, height * 0.5);
    for(int a = 0; a < vortex_iterations; a++) {
        scale(1 - ((float) a / vortex_iterations * 0.5));
        rotate( radians(vortex_rotation) );
        image(vortex_img, -width * 0.5, -height * 0.5);
    }
    popMatrix();
}

void drawControls() {
	// Draw controls
    blendMode(NORMAL);
    cp5.draw();
}

void drawHelp() {
    if (show_help) {
        text("KEYS: (r) Refresh, (h) Help, (c) Controls, (1-5) Walkthrough", 20, height - 30); 
    }
}
