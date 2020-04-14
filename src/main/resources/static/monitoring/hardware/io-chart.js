$(function(){
    var myChart = echarts.init(document.getElementById('chart'));
    window.addEventListener("resize",function(){
        myChart.resize();
    });

    setTimeout(function () {

        option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            toolbox: {
                feature: {
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            legend: {
                data: ['每秒传输速度', '每秒读取数', '每秒写入数','使用率','平均相应时间']
            },
            dataZoom: [{
                start: 80,
                end:100
            }, {
                type: 'inside'
            }],
            xAxis: [
                {
                    type: 'category',
                    data: xAxisData,
                    axisPointer: {
                        type: 'shadow'
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '传输数',
                    min: 0,
                    interval: 50,
                    axisLabel: {
                        formatter: '{value} kb/s'
                    }
                },
                {
                    type: 'value',
                    name: '使用率',
                    min: 0,
                    max: 100,
                    interval: 10,
                    axisLabel: {
                        formatter: '{value} %'
                    }
                },
                {
                    type: 'value',
                    offset : 50,
                    name: '时间',
                    interval: 5,
                    axisLabel: {
                        formatter: '{value} ms'
                    }
                }
            ],
            series: [
                {
                    name: '每秒传输速度',
                    type: 'bar',
                    data: seriesData["diskTrans"]
                },
                {
                    name: '每秒读取数',
                    type: 'bar',
                    data: seriesData["diskRead"]
                },
                {
                    name: '每秒写入数',
                    type: 'bar',
                    data: seriesData["diskWrite"]
                },
                {
                    name: '使用率',
                    type: 'line',
                    yAxisIndex: 1,
                    data: seriesData["diskUseRate"]
                },
                {
                    name: '平均相应时间',
                    type: 'line',
                    yAxisIndex: 2,
                    data: seriesData["diskAvgRespond"]
                }
            ]
        };


        myChart.setOption(option);

    });
});