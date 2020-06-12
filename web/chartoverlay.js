class ChartOverlay {
    charts = {};
    loading = [];
    scale = 1;

    options = {
        showBounds: false,
    };

    constructor() {
    }

    loadChart(chartInfo) {
        chartOverlay.getInitialScale(chartInfo);
        chartOverlay.charts[chartInfo.header.chartInfo.number] = chartInfo;

        let sectionOptions = ["All"];
        let colorOptions = Object.keys(chartInfo.kapHeaders[0].colors);

        for (let kapHeader of chartInfo.kapHeaders) {
            chartOverlay.loadExtent(kapHeader);
            sectionOptions.push(kapHeader.paneInfo.number);
        }

        l("section").innerHTML = getSelectOptions(sectionOptions);
        l("palette").innerHTML = getSelectOptions(colorOptions);
    }

    getInitialScale(chartInfo) {
        for (let i in chartInfo.kapHeaders) {
            if (Math.floor(chartInfo.kapHeaders[i].paneInfo.dimensions.width * chartOverlay.scale) > window.visualViewport.width) {
                chartOverlay.scale = window.visualViewport.width / chartInfo.kapHeaders[i].paneInfo.dimensions.width;
            }
            if (Math.floor(chartInfo.kapHeaders[i].paneInfo.dimensions.height * chartOverlay.scale) > window.visualViewport.height) {
                chartOverlay.scale = window.visualViewport.height / chartInfo.kapHeaders[i].paneInfo.dimensions.height;
            }
        }
        console.log("Initial scale: ", chartOverlay.scale);
    }

    loadExtent(extent) {
        let img = new Image();
        let offscreenCanvas = document.createElement("canvas");
        offscreenCanvas.width = extent.paneInfo.dimensions.width * chartOverlay.scale;
        offscreenCanvas.height = extent.paneInfo.dimensions.height * chartOverlay.scale;
        console.log(offscreenCanvas.width, offscreenCanvas.height);
        img.onload = function (event) {
            resizer.resize(event.target, offscreenCanvas, {
                unsharpAmount: 100,
                unsharpRadius: 0.6,
                unsharpThreshold: 2
            }).then(function () {
                let img = new createjs.Bitmap(offscreenCanvas);
                stage.addChild(img);
                stage.update();
                setLoading(false);
            });

        };
        img.src = "/chart/png/" + extent.paneInfo.number;
    }
}