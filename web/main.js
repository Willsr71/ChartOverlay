let chartList = [];
const chartOverlay = new ChartOverlay();

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

window.onload = function () {
    fetch("/catalog/list")
        .then(checkResponse)
        .then(json => {
            chartList = json;
        });
    chartOverlay.setup();
    setLoading(false);
};