
var chart = {};
var chartData = [];
var chartCursor;
var catesStats;
var cateArray = [];
var cateTotalArray = [];

$(function() {
    var basePath = '/' + window.location.pathname.split('/')[1];
    $.ajax({
        url : basePath + '/ExpendData',
        success : function(data) {
            catesStats = JSON.parse(data);
            main();
        }
    });

    $('#checkAll').click(function(){
        if (this.checked) {
            $('#cateCheckboxDiv .cateCheckbox').prop('checked', true);
        } else {
            $('#cateCheckboxDiv .cateCheckbox').prop('checked', false);
        }
    });

});

function main() {

    // Write categories
    var cateCheckBoxDiv = $('#cateCheckboxDiv table');
    calculateCatePercent();
    // Category names 中文排序
    var cateNameArray = new Array();
    for (var cate in catesStats) {
        cateNameArray.push(cate);
    }
    cateNameArray.sort(function(a,b){
        return a.localeCompare(b);
    });
    // Create check boxes
    for (var i in cateNameArray) {
        var cate = cateNameArray[i];
        cateArray.push(cate);
        checked = "checked";
        var catePercent = Math.round(cateTotalArray[cate]/cateTotalArray['total'] * 100);
        var checkBoxStr = '<tr><td><input type="checkbox" '+ checked +' class="cateCheckbox" id="' + cate + '">';
        var percentStr = ':</td><td>' + catePercent + '%</td></tr>';
        var wholeStr = checkBoxStr + cate + percentStr;
        cateCheckBoxDiv.append($(wholeStr));
    }
    $('#cateCheckboxDiv input[type="checkbox"]').click(function(){
        cateArray = [];
        $.each($('#cateCheckboxDiv .cateCheckbox'), function(){
            if (this.checked) {
                cateArray.push(this.id);
            }
        });
        calculateChartData();
        writeChart();
    });

    calculateChartData();

    writeChart();
}

// Get category accruing expend, including total expend
function calculateCatePercent() {
    var total = 0;
    for (var cate in catesStats) {
        var cateTotal = 0;
        for (var dateStr in catesStats[cate]) {
            var expend = JSON.parse(catesStats[cate][dateStr]);
            total += expend.howMuch;
            cateTotal += expend.howMuch;
        }
        cateTotalArray[cate] = cateTotal
    }
    cateTotalArray['total'] = total;
}

// Monthly Expenditure Statistics
function calculateChartData() {
    chartData = new Array;
    var resultArray = new Array;
    for (var cate in catesStats) {
        // inArray() is a fabulous method that return the position of the element !
        if ($.inArray(cate, cateArray)>=0) {
            for (var dateStr in catesStats[cate]) {
                var expend = JSON.parse(catesStats[cate][dateStr]);
                if (resultArray[dateStr]) {
                    resultArray[dateStr].howMuch += expend.howMuch;
                } else {
                    var statExpend = {date : new Date(dateStr), howMuch : expend.howMuch};
                    resultArray[dateStr] = statExpend;
                }
            }
        }
    }

    // create a resultArray that all elements are 0.
    var size = getArraySize(resultArray);
    
    if (size == 0) {
        for (var dateStr in catesStats['香烟']) {
            var statExpend = {date : new Date(dateStr), howMuch : 0};
            resultArray[dateStr] = statExpend;
        }
    }

    for (var dateStr in resultArray) {
        chartData.push(resultArray[dateStr]);
    }
}

function writeChart() {
    $('#lineChartDiv').empty();
    // SERIAL CHART
    chart = new AmCharts.AmSerialChart();
    chart.pathToImages = "../../js/images/";
    chart.zoomOutButton = {
        backgroundColor: '#000000',
        backgroundAlpha: 0.15
    };
    chart.dataProvider = chartData;
    chart.categoryField = "date";

    // listen for "dataUpdated" event (fired when chart is rendered) and call zoomChart method when it happens
    chart.addListener("dataUpdated", zoomChart);

    // AXES
    // category
    var categoryAxis = chart.categoryAxis;
    categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
    categoryAxis.minPeriod = "MM"; // our data is monthly, so we set minPeriod to MM
    categoryAxis.gridAlpha = 0.15;
    categoryAxis.dashLength = 5;
    categoryAxis.axisColor = "#DADADA";

    // value                
    var valueAxis = new AmCharts.ValueAxis();
    valueAxis.axisAlpha = 0.2;
    valueAxis.minimum = 0;
    valueAxis.dashLength = 5;
    valueAxis.unit = 'RMB';
    chart.addValueAxis(valueAxis);

    // GRAPH
    var graph = new AmCharts.AmGraph();
    graph.title = "red line";
    graph.valueField = "howMuch";
    graph.bullet = "round";
    graph.bulletBorderColor = "#FFFFFF";
    graph.bulletBorderThickness = 2;
    graph.lineThickness = 2;
    graph.lineColor = "#D2F400";
    graph.negativeLineColor = "#0352b5";
    graph.hideBulletsCount = 50; // this makes the chart to hide bullets when there are more than 50 series in selection
    chart.addGraph(graph);

    // CURSOR
    chartCursor = new AmCharts.ChartCursor();
    chartCursor.cursorPosition = "mouse";
    // our data is monthly, so we set minPeriod to MMM
    chartCursor.categoryBalloonDateFormat = "MMM";
    chart.addChartCursor(chartCursor);

    // SCROLLBAR
    var chartScrollbar = new AmCharts.ChartScrollbar();
    chartScrollbar.graph = graph;
    chartScrollbar.scrollbarHeight = 40;
    chartScrollbar.color = "#FFFFFF";
    chartScrollbar.autoGridCount = true;
    chart.addChartScrollbar(chartScrollbar);

    // WRITE
    chart.write("lineChartDiv");
}

// this method is called when chart is first inited as we listen for "dataUpdated" event
function zoomChart() {
    // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
    chart.zoomToIndexes(chartData.length - 40, chartData.length - 1);
}