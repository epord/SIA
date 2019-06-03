let generations = [];
let file;
const linesPerFrame = 11;
let generationsCount;

function preload() {
    file = loadStrings('data.p5');
}
function setup() {
    createCanvas(1280, 720);
    frameRate(20);
    generationsCount = file.filter(content => content).length / linesPerFrame;
    loadFrames()
}

const loadFrames = () => {
    for (let i = 0; i < generationsCount; i++) {
        const distributionsCount = file[linesPerFrame * i].trim().split(' ').map(n => parseInt(n));
        const commonHelmet = file[linesPerFrame * i + 1].split(' ').map((n, i) => i === 0 ? parseInt(n) : parseFloat(n));
        const commonPlatebody = file[linesPerFrame * i + 2].split(' ').map((n, i) => i === 0 ? parseInt(n) : parseFloat(n));
        const commonGloves = file[linesPerFrame * i + 3].split(' ').map((n, i) => i === 0 ? parseInt(n) : parseFloat(n));
        const commonWeapon = file[linesPerFrame * i + 4].split(' ').map((n, i) => i === 0 ? parseInt(n) : parseFloat(n));
        const commonBoots = file[linesPerFrame * i + 5].split(' ').map((n, i) => i === 0 ? parseInt(n) : parseFloat(n));
        const meanHelmet = file[linesPerFrame * i + 6].split(' ').map(n => parseFloat(n));
        const meanPlatebody = file[linesPerFrame * i + 7].split(' ').map(n => parseFloat(n));
        const meanGloves = file[linesPerFrame * i + 8].split(' ').map(n => parseFloat(n));
        const meanWeapon = file[linesPerFrame * i + 9].split(' ').map(n => parseFloat(n));
        const meanBoots = file[linesPerFrame * i + 10].split(' ').map(n => parseFloat(n));
        const generationData = {
            distributionsCount,
            commonHelmet,
            commonPlatebody,
            commonGloves,
            commonWeapon,
            commonBoots,
            meanHelmet,
            meanPlatebody,
            meanGloves,
            meanWeapon,
            meanBoots
        };
        generations.push(generationData);
    }
};


let currentGeneration = 0;
function draw() {
    background(200);

    drawGenerationCounter();
    drawPopulationClasses();
    drawCommonItems();
    drawMeanItems();

    ++currentGeneration;

}
const drawGenerationCounter = () => {
    textSize(40);
    fill(0);
    text(currentGeneration, 80, 50);
};

const drawPopulationClasses = () => {
    const generationData = generations[currentGeneration];
    for (let i = 0; i < 10; i++) {
        for (let j = 0; j < 10; j++) {
            const populationID = 10 * j + i;
            const classesCount = generationData.distributionsCount.length;
            let accumulatedPopulation = 0;
            let k = 0;
            for (; k < classesCount; k++) {
                accumulatedPopulation += generationData.distributionsCount[k];
                if (accumulatedPopulation >= populationID + 1) {
                    fill(150 + k * 105 / classesCount, 200 - k * 200 / classesCount, 255 - k * 255 / classesCount);
                    ellipse(20 + 30 * i, 100 + 30 * j, 20, 20);
                    break;
                }
            }
            if (k == classesCount) {
                ellipse(20 + 30 * i, 100 + 30 * j, 20, 20);
            }
        }
    }
};

const drawCommonItems = () => {
    const generationData = generations[currentGeneration];
    const { commonHelmet, commonPlatebody, commonGloves, commonWeapon, commonBoots } = generationData;
    textSize(20);
    textAlign(CENTER)
    fill('black');
    text("Most\ncommon", 40, 500)
    drawCommonItem(100, 460, commonHelmet, 27);
    drawCommonItem(250, 460, commonPlatebody, 37);
    drawCommonItem(400, 460, commonGloves, 9);
    drawCommonItem(550, 460, commonWeapon, 47);
    drawCommonItem(700, 460, commonBoots, 9);

};

// Max values:
// helmet -> 27
// platebody -> 37
// gloves -> 9
// weapon -> 47
// boots -> 9
const drawCommonItem = (x, y, item, maxValue) => {
    textSize(16);
    text("St", x, y);
    rect(x + 30, y - 10, item[1] * 100 / maxValue, 10);
    text("Ag", x, y + 20);
    rect(x + 30, y + 10, item[2] * 100 / maxValue, 10);
    text("Ex", x, y + 40);
    rect(x + 30, y + 30, item[3] * 100 / maxValue, 10);
    text("Re", x, y + 60);
    rect(x + 30, y + 50, item[4] * 100 / maxValue, 10);
    text("Hi", x, y + 80);
    rect(x + 30, y + 70, item[5] * 100 / maxValue, 10);
};


const drawMeanItems = () => {
    const generationData = generations[currentGeneration];
    const { meanHelmet, meanPlatebody, meanGloves, meanWeapon, meanBoots } = generationData;
    textSize(20);
    textAlign(CENTER)
    fill('black');
    text("Mean\nitems", 40, 640)
    drawMeanItem(100, 600, meanHelmet, 27);
    drawMeanItem(250, 600, meanPlatebody, 37);
    drawMeanItem(400, 600, meanGloves, 9);
    drawMeanItem(550, 600, meanWeapon, 47);
    drawMeanItem(700, 600, meanBoots, 9);
};

// Max values:
// helmet -> 27
// platebody -> 37
// gloves -> 9
// weapon -> 47
// boots -> 9
const drawMeanItem = (x, y, item, maxValue) => {
    textSize(16);
    fill('black');
    text("St", x, y);
    rect(x + 30, y - 10, item[0] * 100 / maxValue, 10);
    text("Ag", x, y + 20);
    rect(x + 30, y + 10, item[1] * 100 / maxValue, 10);
    text("Ex", x, y + 40);
    rect(x + 30, y + 30, item[2] * 100 / maxValue, 10);
    text("Re", x, y + 60);
    rect(x + 30, y + 50, item[3] * 100 / maxValue, 10);
    text("Hi", x, y + 80);
    rect(x + 30, y + 70, item[4] * 100 / maxValue, 10);
};









