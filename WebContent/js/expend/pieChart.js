
var catesStats,
    chartData,
    // year array
    yearArray;

AmCharts.ready(function() {
    var basePath = '/' + window.location.pathname.split('/')[1];
    $.ajax({
        url : basePath + '/ExpendData',
        success : function(data) {
            catesStats = JSON.parse(data);
            main();
        }
    });
    
    
    
});

function main() {
    // Separate data by year
    yearArray = getYearData(catesStats);
    // calculate and statistics Chart Data
    chartData = calculateChartData(yearArray);
    // create chart div and draw chart
    for (var year in chartData) {
        var chartName = 'chart' + year;
        $('#charts').append('<div id="' + chartName + '" class="chartDiv"></div>');
        writeChart(year);
    }
    resizeOuterDivHeight();
}

// convert original data to yearData -> year -> cate -> cate year total expend number
function getYearData(catesStats) {
    var yearArray = [];
    for (var cate in catesStats) {
        for (var dateStr in catesStats[cate]) {
            var expend = JSON.parse(catesStats[cate][dateStr]);
            var year = dateStr.substring(0,4);
            if (yearArray[year]) {
                if (yearArray[year][cate]) {
                    yearArray[year][cate] = yearArray[year][cate] + expend.howMuch;
                } else {
                    yearArray[year][cate] = expend.howMuch;
                } 
            } else {
                yearArray[year] = new Array();
                yearArray[year][cate] = expend.howMuch;
            }
            
        }
    }
    return yearArray;
}

function calculateChartData(yearArray) {
    var chartData = [];
    for (var year in yearArray) {
        var i = 0;
        var food = {};
        food.cateName = "食品";
        food.howMuch = 0;
        for (var cate in yearArray[year]) {
            // 将必要食品和非必要食品合并。
            if (cate.lastIndexOf('食品') > 0) {
                food.howMuch += yearArray[year][cate];
                i--;
            } else if (i == 0) {
                chartData[year] = new Array();
                chartData[year][0] = {cateName:cate, howMuch:yearArray[year][cate]};
            } else {
                chartData[year][i] = {cateName:cate, howMuch:yearArray[year][cate]};
            }
            i++;
        }
        chartData[year][i] = food; 
    }
    return chartData;
}

function writeChart(year) {
    // PIE CHART
    chart = new AmCharts.AmPieChart();
    chart.fontFamily = '楷体';
    chart.fontSize = 14;
    chart.addTitle(year, 14);
    chart.dataProvider = chartData[year];
    chart.titleField = "cateName";
    chart.valueField = "howMuch";
    chart.outlineColor = "#FFFFFF";
    chart.outlineAlpha = 0.8;
    chart.outlineThickness = 2;
    // this makes the chart 3D
    chart.depth3D = 15;
    chart.angle = 30;
    // WRITE
    chart.write('chart' + year);
}