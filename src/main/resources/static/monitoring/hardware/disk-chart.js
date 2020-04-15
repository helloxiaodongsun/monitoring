$(function() {
    var myChart = echarts.init(document.getElementById('chart'));
    window.addEventListener("resize",function(){
        myChart.resize();
    });


        myChart.setOption(option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            toolbox: {
                show: true,
                feature: {
                    mark: {show: true},
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            legend: {
                data: legendData
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            yAxis: {
                type: 'value'
            },
            xAxis: {
                type: 'category',
                data: xAxisData
            },
            dataZoom: [{
                start: 80,
                end:100
            }, {
                type: 'inside'
            }],
            series: dealseries(legendData,rawData)
        }, true);



});
function dealseries(legendData,rawData){
    var series = [];
    for(i in legendData){
        var serie = {
            name : legendData[i],
            type : 'bar',
            stack : '总量',
            data : rawData[legendData[i]]
        }
        series.push(serie);
    }
    return series;
}