function Utility() {
    var def = {
        isDoubleTapBug: function(evt, target) {
            if ($.browser.android) {
                if ("touchstart" != evt.type && "touchstart" == $(target).data("lastTouch")) return evt.stopImmediatePropagation(), evt.preventDefault(), !0; - 1 != evt.type.indexOf("touch") && $(target).data("lastTouch", !0)
            }
            return !1
        },
        getEventNames: function(name) {
            switch (name) {
                case "start":
                    return $.browser.ios || $.browser.android ? "touchstart" : "touchstart mousedown";
                case "end":
                    return $.browser.ios || $.browser.android ? "touchend" : "touchend mouseup";
                default:
                    return name
            }
        },
        touchEnded: function() {
            touchEndedSinceTap = !0
        },
        isTouch: function() {
            return "ontouchstart" in document.documentElement
        },
        padLeft: function(nr, n, str) {
            return Array(n - String(nr).length + 1).join(str || "0") + nr
        },
        trim: function(s) {
            return s.replace(/^\s*|\s*$/gi, "")
        },
        between: function(min, max, decimals) {
            return decimals ? 1 * (Math.random() * (max - min) + min).toFixed(decimals) : Math.floor(Math.random() * (max - min + 1) + min)
        },
        shuffleSimple: function(sourceArray) {
            return sourceArray.sort(function() {
                return .5 - Math.random()
            }), sourceArray
        },
        shuffle: function(sourceArray) {
            for (var n = 0; n < sourceArray.length - 1; n++) {
                var k = n + Math.floor(Math.random() * (sourceArray.length - n)),
                    temp = sourceArray[k];
                sourceArray[k] = sourceArray[n], sourceArray[n] = temp
            }
            return sourceArray
        },
        index: function(obj, i) {
            var j = 0;
            for (var name in obj) {
                if (j == i) return obj[name];
                j++
            }
        },
        areArraysEqual: function(arr1, arr2) {
            return !(!arr1 || !arr2) && arr1.join("|") === arr2.join("|")
        },
        count: function(obj) {
            var count = 0;
            for (var name in obj) count++;
            return count
        },
        eat: function(e) {
            return e.preventDefault(), e.stopPropagation(), !1
        },
        pick: function(arr) {
            var drawFromArr = arr;
            if (arr.constructor == Object) {
                drawFromArr = [];
                for (var id in arr) drawFromArr.push(id)
            }
            var drawIndex = Utils.between(0, drawFromArr.length - 1);
            return 0 == drawFromArr.length ? null : drawFromArr[drawIndex]
        },
        draw: function(arr, optionalValueToMatch) {
            var drawFromArr = arr;
            if (arr.constructor == Object) {
                drawFromArr = [];
                for (var id in arr) drawFromArr.push(id)
            }
            if (0 == drawFromArr.length) return null;
            var drawIndex = Utils.between(0, drawFromArr.length - 1);
            if (void 0 != optionalValueToMatch) {
                for (var foundMatch = !1, i = 0; i < drawFromArr.length; i++)
                    if (drawFromArr[i] == optionalValueToMatch) {
                        drawIndex = i, foundMatch = !0;
                        break
                    } if (!foundMatch) return null
            }
            var value = drawFromArr[drawIndex];
            return drawFromArr.splice(drawIndex, 1), value
        },
        removeFromArray: function(arr, val) {
            if (0 == arr.length) return null;
            for (var foundMatch = !1, drawIndex = -1, i = 0; i < arr.length; i++)
                if (arr[i] == val) {
                    drawIndex = i, foundMatch = !0;
                    break
                } if (!foundMatch) return null;
            var value = arr[drawIndex];
            return arr.splice(drawIndex, 1), value
        },
        toArray: function(obj) {
            var arr = [];
            for (var id in obj) arr.push(id);
            return arr
        },
        fillArray: function(min, max, repeatEachValue) {
            repeatEachValue || (repeatEachValue = 1);
            for (var arr = new Array, repeat = 0; repeat < repeatEachValue; repeat++)
                for (var i = min; i <= max; i++) arr.push(i);
            return arr
        },
        contains: function(arr, item) {
            for (var i = 0; i < arr.length; i++)
                if (arr[i] == item) return !0;
            return !1
        },
        setCookie: function(name, value, days) {
            if (days) {
                var date = new Date;
                date.setTime(date.getTime() + 24 * days * 60 * 60 * 1e3);
                expires = "; expires=" + date.toGMTString()
            } else var expires = "";
            document.cookie = name + "=" + value + expires + "; path=/"
        },
        getCookie: function(name) {
            for (var nameEQ = name + "=", ca = document.cookie.split(";"), i = 0; i < ca.length; i++) {
                for (var c = ca[i];
                     " " == c.charAt(0);) c = c.substring(1, c.length);
                if (0 == c.indexOf(nameEQ)) return c.substring(nameEQ.length, c.length)
            }
            return null
        },
        clearCookie: function(name) {
            this.setCookie(name, "", -1)
        },
        cssVendor: function($el, prop, value) {
            switch (prop) {
                case "opacity":
                    $.browser.ie ? $el.css("-ms-filter", '"progid:DXImageTransform.Microsoft.Alpha(Opacity=' + Math.round(100 * value) + ')"') : $el.css(prop, value);
                    break;
                default:
                    for (var prefixes = ["", "-webkit-", "-moz-", "-o-", "-ms-"], i = 0; i < prefixes.length; i++) $el.css(prefixes[i] + prop, value)
            }
        },
        createCSS: function(s, id) {
            id = id || "tempcss", $("#" + id).remove();
            var style = '<style id="' + id + '">' + s + "</style>";
            !window.isWebApp && window.MSApp && window.MSApp.execUnsafeLocalFunction ? MSApp.execUnsafeLocalFunction(function() {
                $("head").first().append($(style))
            }) : $("head").first().append($(style))
        },
        setColorScheme: function(c1, c2) {
            var c2 = c2 || Colors.getComplementary(c1),
                css = (Colors.luminateHex(c1, .05), Colors.luminateHex(c2, .05), ".odd  .tile-1 .inner { background-color: " + c1 + "; }.even .tile-1 .inner { background-color: " + c1 + "; }.odd  .tile-2 .inner { background-color: " + c2 + "; }.even .tile-2 .inner { background-color: " + c2 + "; }");
            Utils.createCSS(css)
        }
    };
    for (var o in def) this[o] = def[o]
}

function Grid(size, height, id) {
    function each(handler) {
        for (var i = 0; i < tiles.length; i++) {
            var x = i % width,
                y = Math.floor(i / width),
                tile = tiles[i];
            if (handler.call(tile, x, y, i, tile)) break
        }
        return self
    }

    function getIndex(x, y) {
        return x < 0 || x >= width || y < 0 || y >= height ? -1 : y * width + x
    }

    function render() {}

    function domRenderer(direct) {
        if (!noRender) {
            if (clearTimeout(renderTOH), direct) {
                Game.debug && console.log("rendering..."), rendered = !1;
                for (var html = '<table data-grid="' + id + '" id="grid" cellpadding=0 cellspacing=0>', y = 0; y < height; y++) {
                    html += "<tr>";
                    for (var x = 0; x < width; x++) {
                        var index = getIndex(x, y),
                            tile = tiles[index],
                            label = "",
                            value = "";
                        switch (tile.type) {
                            case TileType.Value:
                                label = 2, value = tile.value;
                                break;
                            case TileType.Wall:
                                label = 1;
                                break;
                            case TileType.Dot:
                                label = 2
                        }
                        html += '<td data-x="' + x + '" data-y="' + y + '" class="' + ((x + y % 2) % 2 ? "even" : "odd") + '"><div id="tile-' + x + "-" + y + '" class="tile tile-' + label + '"><div class="inner">' + value + "</div></div></td>"
                    }
                    html += "</tr>"
                }
                return html += "</table>", $("#" + id).html(html), Game.resize(), rendered = !0, self
            }
            renderTOH = setTimeout(function() {
                domRenderer(!0)
            }, 0)
        }
    }

    function isDone(allowDots) {
        for (var i = 0; i < tiles.length; i++)
            if (tiles[i].type == TileType.Unknown || !allowDots && tiles[i].type == TileType.Dot) return !1;
        return !0
    }

    function fillDotsAround(aroundTile) {
        overwriteNumbers = !0;
        for (var i = 0; i < size; i++) {
            var tile = tiles[i * size + aroundTile.x];
            tile.type == TileType.Unknown && tile.dot(), tile.type == TileType.Value && overwriteNumbers && tile.dot(), (tile = tiles[aroundTile.y * size + i]).type == TileType.Unknown && tile.dot(), tile.type == TileType.Value && overwriteNumbers && tile.dot()
        }
        return render(), self
    }

    function tile(x, y) {
        return x < 0 || x >= width || y < 0 || y >= height ? null : tiles[getIndex(x, y)]
    }

    function solve(silent, hintMode) {
        var tryAgain = !0,
            attempts = 0,
            hintTile = null,
            pool = tiles;
        for (hintMode && (pool = tiles.concat(), Utils.shuffle(pool)); tryAgain && attempts++ < 99;) {
            if (tryAgain = !1, isDone()) return silent && clearTimeout(renderTOH), !0;
            for (i = 0; i < pool.length; i++) pool[i].info = pool[i].collect();
            for (var tile, info, i = 0; i < pool.length; i++) {
                if (tile = pool[i], info = tile.collect(tile.info), tile.isDot() && !info.unknownsAround && !hintMode) {
                    tile.number(info.numberCount, !0), tryAgain = HintType.NumberCanBeEntered;
                    break
                }
                if (tile.isNumber() && info.unknownsAround) {
                    if (info.numberReached) {
                        hintMode ? hintTile = tile : tile.close(), tryAgain = HintType.ValueReached;
                        break
                    }
                    if (info.singlePossibleDirection) {
                        hintMode ? hintTile = tile : tile.closeDirection(info.singlePossibleDirection, !0, 1), tryAgain = HintType.OneDirectionLeft;
                        break
                    }
                    for (var dir in Directions) {
                        var curDir = info.direction[dir];
                        if (curDir.wouldBeTooMuch) {
                            hintMode ? hintTile = tile : tile.closeDirection(dir), tryAgain = HintType.WouldExceed;
                            break
                        }
                        if (curDir.unknownCount && curDir.numberWhenDottingFirstUnknown + curDir.maxPossibleCountInOtherDirections <= tile.value) {
                            hintMode ? hintTile = tile : tile.closeDirection(dir, !0, 1), tryAgain = HintType.OneDirectionRequired;
                            break
                        }
                    }
                    if (tryAgain) break
                }
                if (tile.isUnknown() && !info.unknownsAround && !info.completedNumbersAround && 0 == info.numberCount) {
                    hintMode ? hintTile = tile : tile.wall(!0), tryAgain = HintType.MustBeWall;
                    break
                }
            }
            if (tileToSolve && tileToSolve.allowQuickSolve && tile && tileToSolve.tile.x == tile.x && tileToSolve.tile.y == tile.y && tileToSolve.exportValue == tile.getExportValue()) return !0;
            if (hintMode) return hint.mark(hintTile, tryAgain), tryAgain = !1, !1
        }
        return render(), silent && clearTimeout(renderTOH), !1
    }

    function save(slot) {
        slot = slot || 1, saveSlots[slot] = {
            values: []
        };
        for (var i = 0; i < tiles.length; i++) saveSlots[slot].values.push(tiles[i].value);
        return self
    }

    function restore(slot) {
        slot = slot || 1;
        var saveSlot = saveSlots[slot];
        if (!saveSlot) return console.log("Cannot restore save slot ", slot), self;
        for (var i = 0; i < saveSlot.values.length; i++) tiles[i].value = saveSlot.values[i];
        return render(), self
    }

    function breakDownWithQuickSolveOrNot(quickSolve) {
        var tile, attempts = 0,
            walls = 0,
            pool = tiles.concat();
        tileToSolve = null, save("full");
        for (i = 0; i < pool.length; i++) tiles[i].isWall() && walls++;
        Utils.shuffle(pool);
        for (var i = 0; i < pool.length && attempts++ < 6;) {
            if (tileToSove = null, (tile = pool[i++]).isWall()) {
                if (1 == walls) continue;
                walls--
            }
            tileToSolve = {
                tile: tile,
                exportValue: tile.getExportValue(),
                allowQuickSolve: quickSolve
            }, tile.clear(), save("breakdown"), solve(!0) ? (restore("breakdown"), attempts = 0) : (restore("breakdown"), tileToSolve.tile.setExportValue(tileToSolve.exportValue), tile.isWall() && walls++)
        }
        tileToSolve = null, save("empty")
    }

    function isValidByCalculation() {
        for (var i = 0; i < tiles.length; i++) {
            var tile = tiles[i];
            if (tile.isEmpty) return !1;
            if (tile.isNumber()) {
                var info = tile.collect();
                if (tile.value != info.numberCount) return console.log(tile.x, tile.y, tile.value, info.numberCount), !1
            }
        }
        return !0
    }

    function getEmptyTiles() {
        var emptyTiles = [];
        return each(function() {
            this.isEmpty && emptyTiles.push(this)
        }), emptyTiles
    }

    function getQuality() {
        return Math.round(getEmptyTiles().length / (width * height) * 100)
    }
    var self = this,
        id = id || "board";
    width = size, height = height || size, tiles = [], saveSlots = {}, renderTOH = 0, rendered = !1, noRender = !1, emptyTile = new Tile(-99, self, -99), currentPuzzle = null, tileToSolve = null;
    var hint = self.hint = new Hint(this);
    this.activateDomRenderer = function() {
        render = this.render = domRenderer, noRender = !1
    }, this.each = each, this.fillDots = function(overwriteNumbers) {
        for (var i = 0; i < tiles.length; i++) {
            var tile = tiles[i];
            tile.type == TileType.Unknown && tile.dot(), tile.type == TileType.Value && overwriteNumbers && tile.dot()
        }
        return render(), self
    }, this.render = render, this.solve = solve, this.number = function(x, y, n) {
        return tile(x, y).number(n), render(), self
    }, this.wall = function(x, y) {
        return tile(x, y).wall(), render(), self
    }, this.dot = function(x, y) {
        return tile(x, y).dot(), render(), self
    }, this.getIndex = getIndex, this.tile = tile, this.load = function(puzzle) {
        currentPuzzle = puzzle, width = puzzle.size, height = puzzle.size, tiles = [];
        for (var i = 0; i < puzzle.empty.length; i++) {
            var tile = new Tile(puzzle.empty[i], self, i, !0);
            tiles.push(tile)
        }
    }, this.generate = function(size) {
        var len = size * size;
        width = height = size;
        for (var i = 0; i < len; i++) {
            var tile = new Tile(2 * (size - 1), self, i);
            tiles.push(tile)
        }
    },
        this.maxify = function(maxAllowed) {
            for (var tile, tryAgain = !0, attempts = 0, maxAllowed = maxAllowed || width, maxTiles = [], i = 0; i < tiles.length; i++)(tile = tiles[i]).value > maxAllowed && maxTiles.push(tile);
            Utils.shuffle(maxTiles);
            for (; tryAgain && attempts++ < 99;)
                for (tryAgain = !1, i = 0; i < maxTiles.length; i++) {
                    var x = maxTiles[i].x,
                        y = maxTiles[i].y;
                    if ((tile = tiles[y * size + x]).value > maxAllowed) {
                        var max = maxAllowed,
                            cuts = tile.getTilesInRange(1, max),
                            cut = null,
                            firstCut = null;
                        for (Utils.shuffle(cuts); !cut && cuts.length;) cut = cuts.pop(), firstCut || (firstCut = cut);
                        cut || (cut = firstCut), cut ? (cut.wall(!0), fillDotsAround(cut), solve(), tryAgain = !0) : console.log("no cut found for", tile.x, tile.y, tile.value, cuts, 1, max);
                        break
                    }
                }
            render()
        }, this.save = save, this.restore = restore, this.breakDown = function() {
        breakDownWithQuickSolveOrNot(!0), solve() || (restore("full"), breakDownWithQuickSolveOrNot(!1)), restore("empty")
    }, this.clear = function() {
        each(function(x, y, i, tile) {
            tile.clear()
        })
    }, this.getNextLockedInTile = function() {
        var lockedInTiles = [];
        return each(function(x, y, i, tile) {
            tile.isLockedIn() && lockedInTiles.push(tile)
        }), !!lockedInTiles.length && Utils.pick(lockedInTiles)
    }, this.getValues = function() {
        var values = [];
        return each(function() {
            values.push(this.getExportValue())
        }), values
    }, this.isValid = function(hard) {
        if (hard) return isValidByCalculation();
        for (var i = 0; i < tiles.length; i++) {
            var tile = tiles[i];
            if (!tile.isEmpty)
                if (1 == currentPuzzle.full[i]) {
                    if (!tile.isWall()) return !1
                } else if (currentPuzzle.full[i] > 1 && !tile.isNumberOrDot()) return !1
        }
        return !0
    }, this.mark = function(x, y) {
        return tile(x, y).mark(), self
    }, this.unmark = function(x, y) {
        if ("number" == typeof x && "number" == typeof y) return tile(x, y).unmark(), self;
        for (var y = 0; y < height; y++)
            for (var x = 0; x < width; x++) tile(x, y).unmark();
        return self
    }, this.getClosedWrongTiles = function() {
        for (var pool = tiles, wrongTiles = [], i = 0; i < pool.length; i++) pool[i].info = pool[i].collect();
        for (i = 0; i < pool.length; i++) {
            var tile = pool[i],
                info = tile.collect(tile.info);
            tile.isNumber() && info.numberCount != tile.value && 0 == info.unknownsAround && wrongTiles.push(tile)
        }
        return wrongTiles
    }, this.__defineGetter__("tiles", function() {
        return tiles
    }), this.__defineGetter__("width", function() {
        return width
    }), this.__defineGetter__("height", function() {
        return height
    }), this.__defineGetter__("rendered", function() {
        return rendered
    }), this.__defineGetter__("quality", function() {
        return getQuality()
    }), this.__defineGetter__("emptyTileCount", function() {
        return getEmptyTiles().length
    }), this.__defineGetter__("emptyTiles", function() {
        return getEmptyTiles()
    }), this.__defineGetter__("hint", function() {
        return hint
    })
}

function Tile(tileValue, grid, index, isExportValue) {
    function setValue(tileValue) {
        return -2 == tileValue ? (value = tileValue, type = TileType.Dot) : isNaN(tileValue) || tileValue < 0 || tileValue > 90 ? (value = -1, type = TileType.Unknown) : (value = tileValue, type = 0 == tileValue ? TileType.Wall : TileType.Value), render(), value
    }

    function render() {
        if (grid.render) {
            if (grid.rendered) {
                var $tile = $("#tile-" + x + "-" + y),
                    label = "",
                    value = "";
                switch (type) {
                    case TileType.Value:
                        label = 2, value = tile.value;
                        break;
                    case TileType.Wall:
                        label = 1;
                        break;
                    case TileType.Dot:
                        label = 2
                }
                $tile.removeClass().addClass("tile tile-" + label), $tile.find(".inner").text(value)
            } else grid.render();
            return self
        }
    }

    function setType(tileType) {
        switch (tileType) {
            case TileType.Unknown:
                type = tileType, value = -1;
                break;
            case TileType.Wall:
                type = tileType, value = 0;
                break;
            case TileType.Dot:
                type = tileType, value = -2;
                break;
            case TileType.Value:
                console.log("Error. Don't set tile type directly to TileType.Value.")
        }
        render()
    }

    function isDot() {
        return type == TileType.Dot
    }

    function isNumber() {
        return type == TileType.Value
    }

    function dot() {
        setType(TileType.Dot);
        return self
    }

    function wall() {
        setType(TileType.Wall);
        return self
    }

    function unknown() {
        setType(TileType.Unknown);
        return self
    }

    function traverse(hor, ver) {
        var newX = x + hor,
            newY = y + ver;
        return grid.tile(newX, newY)
    }

    function closeDirection(dir, withDots, amount) {
        for (var t = self.move(dir), count = 0; t && !t.isWall(); t = t.move(dir)) {
            if (t.isUnknown()) {
                if (count++, !withDots) {
                    t.wall(!0);
                    break
                }
                t.dot(!0)
            }
            if (count >= amount) break
        }
    }

    function setExportValue(value) {
        switch (value) {
            case 0:
                unknown();
                break;
            case 1:
                wall();
                break;
            case 2:
                dot();
                break;
            default:
                setValue(value - 2)
        }
    }

    var self = this,
        value = -1,
        type = TileType.Unknown,
        x = this.x = index % grid.width,
        y = this.y = Math.floor(index / grid.width);
    this.id = x + "," + y;

    isExportValue ? setExportValue(tileValue) : setValue(tileValue),

        this.mark = function() {
            return $("#tile-" + x + "-" + y).addClass("marked"), self
        },
        this.unmark = function() {
            return $("#tile-" + x + "-" + y).removeClass("marked"), self
        },
        this.dot = dot, this.wall = wall, this.number = function(n) {
        setValue(n);
        return self
    },
        this.unknown = unknown, this.isDot = isDot, this.isWall = function() {
        return type == TileType.Wall
    },
        this.isNumber = isNumber, this.isNumberOrDot = function() {
        return isNumber() || isDot()
    },
        this.isUnknown = function() {
            return type == TileType.Unknown
        },
        this.isLockedIn = function() {
            if (!isDot()) return !1;
            for (var dir in Directions)
                if (self.move(dir) && !self.move(dir).isWall()) return !1;
            return !0
        },
        this.collect = function(info) {
            if (info)
                for (var dir in Directions) {
                    for (var curDir = info.direction[dir], t = self.move(dir); t && !t.isWall(); t = t.move(dir)) t.isNumber() && t.info.numberReached && (info.completedNumbersAround = !0);
                    if (isNumber() && !info.numberReached && curDir.unknownCount) {
                        curDir.maxPossibleCountInOtherDirections = 0;
                        for (var otherDir in Directions) otherDir != dir && (curDir.maxPossibleCountInOtherDirections += info.direction[otherDir].maxPossibleCount)
                    }
                } else {
                info = {
                    unknownsAround: 0,
                    numberCount: 0,
                    numberReached: !1,
                    canBeCompletedWithUnknowns: !1,
                    completedNumbersAround: !1,
                    singlePossibleDirection: null,
                    direction: {}
                };
                for (var dir in Directions) info.direction[dir] = {
                    unknownCount: 0,
                    numberCountAfterUnknown: 0,
                    wouldBeTooMuch: !1,
                    maxPossibleCount: 0,
                    maxPossibleCountInOtherDirections: 0,
                    numberWhenDottingFirstUnknown: 0
                };
                var lastPossibleDirection = null,
                    possibleDirCount = 0;
                for (var dir in Directions)
                    for (t = self.move(dir); t && !t.isWall(); t = t.move(dir)) curDir = info.direction[dir], t.isUnknown() ? (curDir.unknownCount || curDir.numberWhenDottingFirstUnknown++, curDir.unknownCount++, curDir.maxPossibleCount++, info.unknownsAround++, isNumber() && lastPossibleDirection != dir && (possibleDirCount++, lastPossibleDirection = dir)) : (t.isNumber() || t.isDot()) && (curDir.maxPossibleCount++, curDir.unknownCount ? isNumber() && 1 == curDir.unknownCount && (curDir.numberCountAfterUnknown++, curDir.numberWhenDottingFirstUnknown++, curDir.numberCountAfterUnknown + 1 > value && (curDir.wouldBeTooMuch = !0)) : (info.numberCount++, curDir.numberWhenDottingFirstUnknown++));
                1 == possibleDirCount && (info.singlePossibleDirection = lastPossibleDirection), isNumber() && value == info.numberCount ? info.numberReached = !0 : isNumber() && value == info.numberCount + info.unknownsAround && (info.canBeCompletedWithUnknowns = !0)
            }
            return 1 == possibleDirCount && (info.singlePossibleDirection = lastPossibleDirection), isNumber() && value == info.numberCount ? info.numberReached = !0 : isNumber() && value == info.numberCount + info.unknownsAround && (info.canBeCompletedWithUnknowns = !0), info
        },
        this.traverse = traverse, this.right = function() {
        return traverse(1, 0)
    },
        this.left = function() {
            return traverse(-1, 0)
        },
        this.up = function() {
            return traverse(0, -1)
        },
        this.down = function() {
            return traverse(0, 1)
        },
        this.move = function(dir) {
            switch (dir) {
                case Directions.Right:
                    return traverse(1, 0);
                case Directions.Left:
                    return traverse(-1, 0);
                case Directions.Up:
                    return traverse(0, -1);
                case Directions.Down:
                    return traverse(0, 1)
            }
        },
        this.close = function(withDots) {
            for (var dir in Directions) closeDirection(dir, withDots)
        },
        this.clear = function() {
            unknown()
        },
        this.render = render,
        this.closeDirection = closeDirection,
        this.getTilesInRange = function(min, max) {
            var self = this,
                result = [],
                max = max || min;
            for (var dir in Directions)
                for (var distance = 0, t = self.move(dir); t && !t.isWall(); t = t.move(dir)) ++distance >= min && distance <= max && result.push(t);
            return result
        },
        this.getExportValue = function() {
            switch (type) {
                case TileType.Unknown:
                    return 0;
                case TileType.Wall:
                    return 1;
                case TileType.Dot:
                    return 2;
                case TileType.Value:
                    return value + 2
            }
        },
        this.setExportValue = setExportValue, this.__defineGetter__("value", function() {
        return value
    }),
        this.__defineSetter__("value", function(v) {
            return setValue(v)
        }),
        this.__defineGetter__("type", function() {
            return type
        }),
        this.__defineSetter__("type", function(v) {
            return setValue(v)
        }),
        this.__defineGetter__("isEmpty", function() {
            return -1 == value
        })
}

const generateGridAndSolution = (size) => {
    for (var puzzle, grid = null, attempts = 0; attempts++ < 10;) {
        grid = new Grid(size), puzzle = {
            size: size,
            full: [],
            empty: [],
            quality: 0,
            ms: 0
        };
        var d = new Date;
        if (grid.clear(), grid.generate(size), grid.maxify(size), grid.isValid(!0)) {
            puzzle.full = grid.getValues(), grid.breakDown(), puzzle.empty = grid.getValues(), puzzle.ms = new Date - d, puzzle.quality = grid.quality;
            break
        }
        grid = null
    }
    return grid;
}
var HintType = {
    "None": "None",
    "NumberCanBeEntered": "NumberCanBeEntered",
    "OneDirectionLeft": "OneDirectionLeft",
    "ValueReached": "ValueReached",
    "WouldExceed": "WouldExceed",
    "OneDirectionRequired": "OneDirectionRequired",
    "MustBeWall": "MustBeWall",
    "ErrorClosedTooEarly": "ErrorClosedTooEarly",
    "ErrorClosedTooLate": "ErrorClosedTooLate",
    "Error": "Error",
    "Errors": "Errors",
    "LockedIn": "LockedIn",
    "GameContinued": "GameContinued",
    "TimeTrialShown": "TimeTrialShown"
};
var TileType = {
    Unknown: 'Unknown',
    Dot: 'Dot',
    Wall: 'Wall',
    Value: 'Value'
};
var Directions = {
    Left: 'Left',
    Right: 'Right',
    Up: 'Up',
    Down: 'Down'
};
var Utils = new Utility();

function Hint() {
    this.active = false;
}

// ==========================
// ||                      ||
// ||    Read from here    ||
// ||                      ||
// ==========================
const fs = require('fs');

const [nodeLoc, runLoc, size, fileName] = process.argv;
if (!size || isNaN(size)) return console.log("Size must be a number.");
if (!fileName) return console.log('No output filename provided.');

const grid = generateGridAndSolution(size);

const outputGridFormat = `${size}\n`+ grid.tiles.map(tile => {
    let value;
    if (tile.type === TileType.Value) value = tile.value;
    else if (tile.type === TileType.Wall) value = 'X';
    else value = '.';
    return value;
}).reduce((total, tileValue, i) => {
    if (i % parseInt(size) === 0) return total + '\n' + tileValue;
    return total + ' ' + tileValue
});

fs.writeFile(fileName, outputGridFormat, (err) => {
    if (err) throw err;
});