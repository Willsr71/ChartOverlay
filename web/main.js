const stage = new createjs.Stage("canvas");
const resizer = window.pica({features: ['js', 'wasm', 'cib', 'ww']});
const RESIZER_ARGS = {
    unsharpAmount: 100,
    unsharpRadius: 0.6,
    unsharpThreshold: 2
};
let chartList = [];
let charts = {};
let activeChart = 0;
let lastScroll = 0;

let options = {
    showBounds: false,
    section: "All"
};

function l(element) {
    return document.getElementById(element);
}

function searchChart() {
    let chart = l("search").value;
    if (chart.length === 0) {
        alert("No chart specified!");
        return;
    }
    if (chartList.indexOf(chart) === -1) {
        alert("Chart does not exist");
        return;
    }

    setLoading(true);
    for (let chart of Object.values(charts)) {
        chart.showExtent(null);
    }
    stage.update();

    charts[chart] = new Chart(chart);
    activeChart = chart;
}

function updateOptions() {
    options.showBounds = l("bounds").checked;
    options.section = l("section").value;

    if (activeChart == null) return;
    charts[activeChart].showExtent(options.section === "All" ? 1 : options.section);

    stage.update();
    console.log("Options updated");
}

function chartLoaded() {
    for (let chart of Object.values(charts)) {
        if (!chart.loaded) return;
    }

    setLoading(false);
}

function setLoading(loading) {
    l("loadingCircle").hidden = !loading;
}

function checkResponse(response) {
    if (response.status !== 200) {
        alert("Error: " + response.statusText);
        return null;
    }
    return response.json();
}

function getSelectOptions(options) {
    let str = "";
    for (let option of options) {
        str += "<option>" + option + "</option>";
    }
    return str;
}

function onResize() {
    l("canvas").width = window.innerWidth;
    l("canvas").height = window.innerHeight;
}

function onMouseWheel(e) {
    lastScroll = Date.now();
    let zoom;
    if (Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail))) > 0)
        zoom = 1.1;
    else
        zoom = 1 / 1.1;
    console.log(stage.mouseX, stage.mouseY);
    charts[activeChart].zoom(zoom);

    stage.update();
}

window.onresize = onResize;

window.onload = function () {
    fetch("/catalog/list")
        .then(checkResponse)
        .then(json => {
            chartList = json;
            setLoading(false);
        });
    onResize();

    //createjs.Touch().enable(stage);

    stage.on("stagemousedown", function (e) {
        let initialPos = {x: e.stageX, y: e.stageY};
        stage.addEventListener("stagemousemove", function (ev) {
            charts[activeChart].moveBy(ev.stageX - initialPos.x, ev.stageY - initialPos.y);
            initialPos.x = ev.stageX;
            initialPos.y = ev.stageY;
            stage.update();
        });
    });

    stage.on("stagemouseup", function () {
        stage.removeAllEventListeners("stagemousemove");
    });

    l("canvas").addEventListener("mousewheel", onMouseWheel, false);
    l("canvas").addEventListener("DOMMouseScroll", onMouseWheel, false);
};