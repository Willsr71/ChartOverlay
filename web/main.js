const stage = new createjs.Stage("canvas");
const resizer = window.pica({features: ['js', 'wasm', 'cib', 'ww']});
const chartOverlay = new ChartOverlay();
let chartList = [];

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
    fetch("/chart/" + chart)
        .then(checkResponse)
        .then(chartOverlay.loadChart)
}

function updateOptions() {
    chartOverlay.options.showBounds = l("bounds").checked;
    chartOverlay.options.section = l("section").value;
    console.log("Options updated");
}

function setLoading(loading) {
    l("loadingCircle").hidden = !loading;
    l("loadingText").hidden = !loading;
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
    l("canvas").width = window.visualViewport.width;
    l("canvas").height = window.visualViewport.height;
}

window.onresize = onResize;

window.onload = function () {
    fetch("/catalog/list")
        .then(checkResponse)
        .then(json => {
            chartList = json;
        });
    onResize();
    setLoading(false);
};