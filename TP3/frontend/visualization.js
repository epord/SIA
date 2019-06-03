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

    ++currentGeneration;

}
const drawGenerationCounter = () => {
    textSize(40);
    fill(0);
    text(currentGeneration, 20, 50);
};

const drawPopulationClasses = () => {
    for (let i = 0; i < 10; i++) {
        for (let j = 0; j < 10; j++) {
            const generationData = generations[currentGeneration];
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









