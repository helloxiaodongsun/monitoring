$(function(){
    var myChart = echarts.init(document.getElementById('chart'));
    window.addEventListener("resize",function(){
        myChart.resize();
    });

    setTimeout(function () {

        option = {
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['内存已使用大小','空闲内存大小','缓存使用大小']
            },
            toolbox: {
                feature: {
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            xAxis: {
                type: 'category',
                data: xAxisData,
                axisPointer: {
                    type: 'shadow'
                }
            },
            yAxis: {
                type: 'value',
                name: '大小',
                axisLabel: {
                    formatter: '{value} GB'
                }
            },
            dataZoom: [{
                start: 80,
                end:100
            }, {
                type: 'inside'
            }],
            series: [
                {
                    name: '内存已使用大小',
                    type: 'line',
                    data: seriesData["memUseTotal"]
                },
                {
                    name: '空闲内存大小',
                    type: 'line',
                    data: seriesData["freeMemTotal"]
                },
                {
                    name: '缓存使用大小',
                    type: 'line',
                    data: seriesData["bufferCacheUseMemTotal"]
                }
            ]
        };



        myChart.setOption(option);

    });
});