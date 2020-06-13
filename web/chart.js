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
        this.getInitialScale()

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
        console.log("Initial scale: ", this.scale);
    }

    extentLoaded() {
        for (let extent of Object.values(this.extents)) {
            if (!extent.loaded) return;
        }
        this.loaded = true;
        stage.update();
        chartLoaded();
    }
}