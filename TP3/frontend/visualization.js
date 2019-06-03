function setup() {
    createCanvas(1280, 720);
    frameRate(3);

}


let currentGeneration = 0;
function draw() {
    background(200);
    textSize(40);
    fill(0);
    text(currentGeneration++, 20, 50);
    for (var i = 0; i < 10; i++) {
        for (var j = 0; j < 10; j++) {
            fill(255, 0, 0);
            ellipse(20 + 30 * i, 100 + 30 * j, 20, 20)
        }
    }
}