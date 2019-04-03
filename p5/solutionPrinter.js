const windowSize = 800; // px
const fps = 1;
const states = [];
let boardSize;
let file;

function preload() {
    file = loadStrings('solution.out');
}

function setup() {
    createCanvas(windowSize, windowSize);
    noStroke();
    frameRate(fps);
    boardSize = parseInt(file[0].trim());
    file.filter((content, line) => content && line !== 0)
        .forEach((content, line) => {
            if ((line % boardSize) === 0) {
                states.push([])
            }
            states[Math.floor(line/boardSize)].push(content);
        });
    console.log(states)
}

const drawCell = (row, col, color, number = '') => {
    const cellDiameter = windowSize/boardSize;
    const x = cellDiameter * (row + .5);
    const y = cellDiameter * (col + .5);
    fill(color);
    circle(y, x, cellDiameter * .9 / 2);
    if (number){
        const tSize = cellDiameter * 0.5;
        fill(255);
        textAlign(CENTER);
        textSize(tSize);
        text(number, y, x + tSize/4);
    }
};

const renderCell = (row, col, value) => {
    if (/^\d*$/.test(value)) {
        return drawCell(row, col, color(37, 178, 221), value);
    }
    switch (value) {
        case '.':
            drawCell(row, col, color(234));
            break;
        case 'X':
            drawCell(row, col, color(249, 31, 55));
            break;
        case 'O':
            drawCell(row, col, color(37, 178, 221));
            break;
    }
};

let currentBoardIdx = 0;
function draw () {
    background(250);
    currentBoardIdx = Math.min(currentBoardIdx+1, states.length - 1);
    const currentBoard = (states[(currentBoardIdx)]);
    currentBoard.forEach((line, i) => {
        const row = line.trim().split(' ');
        row.forEach((cell, j) => {
            renderCell(i, j, cell);
        })
    });
}