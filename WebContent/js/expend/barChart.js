
var chart;
var chartData;
var catesStats;
var basePath = '/' + window.location.pathname.split('/')[1];

$(function(){
    // set date picker
    $( "#datepicker" ).datepicker({dateFormat: "yy-mm", changeMonth: true});

    $.ajax({
        url : basePath + '/ExpendData',
        success : function(data) {
            catesStats = JSON.parse(data);
        }
    });
    $('#showButton').click(function(){
        var monthStr = $('#datepicker').val();
        resetChartData(monthStr);
        for (var i = 0; i < 0; i++) chartData.pop();
        writeChart();
    });
    // 回车修复
    $('#datepicker').keyup(function(e){
        if (e.keyCode == 13) {
            $('#showButton').click();
        }
    });
});

function resetChartData(monthStr) {
    chartData = new Array;
    var chartDataTemp = new Array;
    var cateNameArray = new Array;
    for (var cate in catesStats) {
        cateNameArray.push(cate);
        var cateStats = catesStats[cate];
        var dateStats = cateStats;
        for (var date in dateStats) {
            if (date === monthStr) {
                var dateStat = dateStats[date];
                chartDataTemp[cate] = dateStat;
            }
        }
    }
    cateNameArray.sort(function(a,b){
        return a.localeCompare(b);
    });
    for (var i in cateNameArray) {
        var statDataStr = chartDataTemp[cateNameArray[i]];
        if (statDataStr) {
            var statData = JSON.parse(statDataStr);
            // Special treatment for cateName which is too long.
            if (statData['cateName'].length > 5) {
                var cateName = statData['cateName'];
                cateName = cateName.substring(0, 3) + '\n' + cateName.substring(3);
                statData['cateName'] = cateName;
            }
            chartData.push(statData);
        }
    }
}

function writeChart() {
    $('#barChartDiv').empty();
    // SERIAL CHART
    chart = new AmCharts.AmSerialChart();
    chart.dataProvider = chartData;
    chart.categoryField = "cateName";
    chart.color = "#444444";
    chart.fontSize = 14;
    chart.fontFamily = '楷体';
    chart.startDuration = 1;
    chart.plotAreaFillAlphas = 0.2;
    // the following two lines makes chart 3D
    chart.angle = 30;
    chart.depth3D = 30;

    // AXES
    // category
    var categoryAxis = chart.categoryAxis;
    categoryAxis.gridAlpha = 0.2;
    categoryAxis.gridPosition = "start";
    categoryAxis.gridColor = "#444444";
    categoryAxis.axisColor = "#444444";
    categoryAxis.axisAlpha = 0.5;
    categoryAxis.dashLength = 5;
    categoryAxis.fontSize = 8;

    // value
    var valueAxis = new AmCharts.ValueAxis();
    valueAxis.stackType = "3d"; // This line makes chart 3D stacked (columns are placed one behind another)
    valueAxis.gridAlpha = 0.2;
    valueAxis.gridColor = "#444444";
    valueAxis.axisColor = "#444444";
    valueAxis.axisAlpha = 0.5;
    valueAxis.dashLength = 5;
    valueAxis.title = "Category Expend"
    valueAxis.titleColor = "#444444";
    valueAxis.unit = "RMB";
    chart.addValueAxis(valueAxis);

    // GRAPHS         
    var graph = new AmCharts.AmGraph();
    graph.title = "Monthly Category Expend";
    graph.valueField = "howMuch";
    graph.type = "column";
    graph.lineAlpha = 0;
    graph.lineColor = "#D2F400";
    graph.fillAlphas = 1;
    graph.balloonText = "[[category]]: [[value]]";
    chart.addGraph(graph);

    chart.write("barChartDiv");
}

