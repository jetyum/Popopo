function timeToString(second) {
    if (second < 60) {
        return Math.round(second) + "s";
    } else if (second < 3600) {
        return Math.round(second / 60) + "min";
    } else if (second < 86400) {
        return Math.round(second / 3600) + "h";
    } else {
        return Math.round(second / 86400) + "day";
    }
}

function memoryToString(memory) {
    const unit = ["", "k", "M", "G", "T"];
    let i = 0;

    while (memory > 1024) {
        memory /= 1024;
        i++;
    }

    if (i < 5) {
        return Math.round(memory * 10) / 10 + unit[i];
    } else {
        return "error";
    }
}

function panel(name, decorator) {
    let html = "";

    if (name.length > 13) name = name.substring(0, 13) + "...";

    html += `<div class='panel'>`;
    html += `<div class='name'>${name}</div>`;
    html += decorator() + `</div>`;

    return html;
}

function progress(style, decorator) {
    const circle = `r='41.333' cx='50' cy='50'`;
    let html = "";

    html += `<div class='circle_bar'>`;
    html += `<svg viewBox='0 0 100 100'>`;
    html += `<circle class='base' ${circle}/>`;

    for (const s of style) {
        const stroke = 259.6 * s[0];

        html += `<circle class='circle' ${circle} style='`;
        html += `stroke-dasharray: ${stroke}, 259.6;`;
        html += `stroke: ${s[1]};'/>`;
    }

    html += `</svg><div class='bar_info'>`;
    html += decorator() + `</div></div>`;

    return html;
}

function playerPanel(player) {
    const name = player.name;

    return panel(name, function () {
        const imgUrl = "https://minotar.net/avatar/";
        let html = "";

        html += `<img src='${imgUrl + name}'><br>`;

        let time;

        if (player.online) {
            html += `<div class='circle online'></div>`;
            time = "now"
        } else {
            html += `<div class='circle offline'></div>`;
            time = timeToString(player.time);
        }

        html += `<div class='info time'>${time}</div>`;

        return html;
    })
}

function ramPanel(ram) {
    const style = [
        [ram.reserve / ram.max, "#b5e291"],
        [ram.use / ram.max, "#eda981"]
    ];
    const useStr = memoryToString(ram.use);
    const reserveStr = memoryToString(ram.reserve);
    const maxStr = memoryToString(ram.max);
    const useRate = Math.round(100 * ram.use / ram.max);

    return panel("Memory", function () {
        let html = "";

        html += progress(style, function () {
            let html = "";

            html += `<div>${useRate}%</div>`;
            html += `<div class='info ram_use'>${useStr}</div>`;

            return html;
        });

        html += `<div class='info ram_max'>${maxStr}</div>`;
        html += `<div class='info ram_reserve'>${reserveStr}</div>`;

        return html;
    })
}

function cpuPanel(cpu) {
    const style = [[cpu.rate, "#eda981"]];
    const cpuRate = Math.round(100 * cpu.rate);
    const tps = Math.round(cpu.tps * 100) / 100
    const tpsRate = Math.round(cpu.tps * 50) / 10;

    return panel("Processing", function () {
        let html = "";

        html += progress(style, function () {
            let html = "";

            html += `<div>${cpuRate}%</div>`;
            html += `<div class='info ram_use'>${cpu.core}Core</div>`;

            return html;
        });

        html += `<div class='rectangle_bar'>`;
        html += `<div class='rectangle' style='width: ${tpsRate}%'></div>`;
        html += `<div class='bar_info'>${tps}tps</div>`;
        html += `</div>`;

        return html;
    })
}

function loadPanels(className, value) {
    const elm = document.getElementsByClassName(className);

    for (const e of elm) {
        e.innerHTML += cpuPanel(value.cpu);
        e.innerHTML += ramPanel(value.ram);

        for (const player of value.players) {
            e.innerHTML += playerPanel(player);
        }
    }
}
