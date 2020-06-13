class Chart {
    loaded = false;
    scale = 1;

    constructor(number) {
        this.number = number;
        this.extents = {};
        fetch("/chart/" + number)
            .then(checkResponse)
            .then(function (info) {
                this.load(info)
            }.bind(this));
    }

    load(info) {
        this.info = info;
        this.getInitialScale();

        let sectionOptions = ["All"];
        let colorOptions = Object.keys(this.info.kapHeaders[0].colors);

        for (let kapHeader of this.info.kapHeaders) {
            this.extents[kapHeader.paneInfo.number] = new Extent(this, kapHeader);
            sectionOptions.push(kapHeader.paneInfo.number);
        }

        l("section").innerHTML = getSelectOptions(sectionOptions);
        l("palette").innerHTML = getSelectOptions(colorOptions);
    }

    getInitialScale() {
        for (let extentHeader of this.info.kapHeaders) {
            if (Math.floor(extentHeader.paneInfo.dimensions.width * this.scale) > l("canvas").width) {
                this.scale = l("canvas").width / extentHeader.paneInfo.dimensions.width;
            }
            if (Math.floor(extentHeader.paneInfo.dimensions.height * this.scale) > l("canvas").height) {
                this.scale = l("canvas").height / extentHeader.paneInfo.dimensions.height;
            }
        }
        l("scale").innerText = (Math.round(scale * 1000) / 10) + "%";
    }

    extentLoaded() {
        for (let extent of Object.values(this.extents)) {
            if (!extent.loaded) return;
        }
        this.loaded = true;
        stage.update();
        chartLoaded();
    }

    showExtent(extentId) {
        let shown;
        if (extentId === 1) shown = Object.keys(this.extents);
        else if (extentId == null) shown = [];
        else shown = [extentId];

        for (let extent in this.extents) {
            if (shown.indexOf(extent) !== -1) this.extents[extent].show();
            else this.extents[extent].hide();
        }
    }

    moveBy(x, y) {
        for (let extent of Object.values(this.extents)) {
            extent.container.x = extent.container.x + x;
            extent.container.y = extent.container.y + y;
        }
    }

    zoom(zoom) {
        this.scale = this.scale * zoom;
        l("scale").innerText = (Math.round(this.scale * 1000) / 10) + "%";
        for (let extent of Object.values(this.extents)) {
            extent.container.scale = extent.container.scale * zoom;
        }

        setTimeout(function () {
            if (lastScroll + 1000 > Date.now()) {
                console.log("Skipping render, to soon");
                return;
            }
            console.log("Rendering");
            for (let extent of Object.values(charts[activeChart].extents)) {
                extent.updateSize();
            }
        }, 1000);
    }
}