function l(element) {
    return document.getElementById(element);
}

function searchChart() {
    let chart = l('search').value;
    console.log("Searching for chart", chart);
    const userAction = async () => {
        const response = await fetch("/catalog/" + chart);
        const json = await response.json();
        console.log(json);
    }
}