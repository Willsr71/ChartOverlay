class Extent {
    loaded = false;

    constructor(chart, extentHeader) {
        this.chart = chart;
        this.header = extentHeader;

        this.container = new createjs.Container();
        stage.addChild(this.container);
        this.image = new Image();
        this.image.onload = function () {
            this.updateSize();
        }.bind(this);
        this.image.src = "/chart/png/" + this.header.paneInfo.number;
    }

    updateSize() {
        setLoading(true);
        let offscreenCanvas = document.createElement("canvas");
        offscreenCanvas.width = this.header.paneInfo.dimensions.width * this.chart.scale;
        offscreenCanvas.height = this.header.paneInfo.dimensions.height * this.chart.scale;
        resizer.resize(this.image, offscreenCanvas, RESIZER_ARGS)
            .then(function () {
                this.resizedImage = new createjs.Bitmap(offscreenCanvas);
                this.container.scale = 1;
                this.container.removeAllChildren();
                this.container.addChild(this.resizedImage);
                this.loaded = true;
                this.chart.extentLoaded();
            }.bind(this));
    }

    show() {
        this.resizedImage.visible = true;
    }

    hide() {
        this.resizedImage.visible = false;
    }
}