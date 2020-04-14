$(function(){
    var myChart = echarts.init(document.getElementById('chart'));
    window.addEventListener("resize",function(){
        myChart.resize();
    });

    setTimeout(function () {

        option = {
            legend: {},
            tooltip: {
                trigger: 'axis',
                showContent: false
            },
            toolbox: {
                show: true,
                feature: {
                    mark: {show: true},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            dataZoom: [{
                start: 80,
                end:100
            }, {
                type: 'inside'
            }],
            dataset: {
                source:dataSetSource
            },
            xAxis: {type: 'category'},
            yAxis: {gridIndex: 0},
            grid: {top: '55%'},
            series: [
                {type: 'line', smooth: true, seriesLayoutBy: 'row'},
                {type: 'line', smooth: true, seriesLayoutBy: 'row'},
                {type: 'line', smooth: true, seriesLayoutBy: 'row'},
                {type: 'line', smooth: true, seriesLayoutBy: 'row'},
                {type: 'line', smooth: true, seriesLayoutBy: 'row'},
                {
                    type: 'pie',
                    id: 'pie',
                    radius: '30%',
                    center: ['50%', '25%'],
                    label: {
                        formatter: '{b}: {@2012} ({d}%)'
                    },
                    encode: {
                        itemName: 'product',
                        value: '2012',
                        tooltip: '2012'
                    }
                }
            ]
        };

        myChart.on('updateAxisPointer', function (event) {
            var xAxisInfo = event.axesInfo[0];
            if (xAxisInfo) {
                var dimension = xAxisInfo.value + 1;
                myChart.setOption({
                    series: {
                        id: 'pie',
                        label: {
                            formatter: '{b}: {@[' + dimension + ']} ({d}%)'
                        },
                        encode: {
                            value: dimension,
                            tooltip: dimension
                        }
                    }
                });
            }
        });

        myChart.setOption(option);

    });
});