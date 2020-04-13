$(function(){
    var serverId = $("#servers").val();
    load(serverId);

    setInterval(function () {
        count++;
        serverId = $("#servers").val();
        load(serverId);
    }, 10000);

    $("#servers").change(function(){
        var val = $(this).val();
        load(val);
    });




});

/**
 * 加载
 * @param serverId
 */
function load(serverId){
    getBasicInfo(serverId);
    load_disk(serverId);
    load_cpu(serverId);
    load_IO(serverId);
    load_mem(serverId);
}

/**
 * 获取服务器基本信息
 * @param id
 */
function getBasicInfo(id) {
    $.ajax({
        url: portal.bp() + '/serverInfo/summary',
        type: "get",
        async: true,
        cache: false,
        data: {"ip":id},
        dataType: "json",
        success: function (o) {
            var code = o.code;
            if (code == 0) {
                $("#ipAddress").text(o.data.serviceIp);
                $("#system").text(o.data.serviceVersion);
                if(o.data.serviceActive=='1'){
                    //正常
                    $("#status").css("color","green");
                    $("#status").text("正常");
                }else if(o.data.statusCode=='0'){
                    //宕机
                    $("#status").css("color","#dada0b");
                    $("#status").text("宕机");
                }
                $("#os").text(o.data.serviceCoreVersion);
            } else {
                layer.msg(o.msg, {icon: 2});
            }
        },
        beforeSend:function(XMLHttpRequest){},
        complete:function(XMLHttpRequest){}
    });
}
//需要删除
var count=1;

/**
 * 加载硬盘图表
 * 已用、空闲
 * @param id
 */
function load_disk(id){
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('div-disk'));
    window.addEventListener("resize",function(){
        myChart.resize();
    });

    $.ajax({
        url:  portal.bp() + '/diskInfo/summary',
        type: "get",
        async: true,
        cache: false,
        data: {"ip":id},
        dataType: "json",
        success: function (o) {
            var code = o.code;
            if (code == 0) {
                var data = o.data;
                var data_disk = [];
                data_disk.push({value:(parseFloat(data.diskUsedSize)/1024/1024).toFixed(3),name:'已用'}); //已用
                data_disk.push({value:(parseFloat(data.diskAvailSize)/1024/1024).toFixed(3),name:'空闲'}); //空闲

                var colors = [];


                //需要删除开始
                if(count%2==0||id==2){
                    data_disk = [
                        {value: 71, name: '已用'},
                        {value: 30, name: '空闲'},
                    ];
                }
                //需要删除结束


                var total = data_disk[0].value + data_disk[1].value;
                if(data_disk[0].value/total > 0.7){
                    //超过70%显示红色
                    colors = ['red','#E0E0E0'];
                }else{
                    colors = ['#1B8CD4', '#E0E0E0'];
                }

                option = {
                    color: colors,
                    title: {
                        text: '硬盘使用情况',
                        subtext: '',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b} : {c} GB ({d}%)'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: ['已用', '空闲']
                    },
                    series: [
                        {
                            name: '使用情况',
                            type: 'pie',
                            radius: '55%',
                            center: ['50%', '60%'],
                            data: data_disk,
                            emphasis: {
                                itemStyle: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                };


                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            } else {
                layer.msg(o.msg, {icon: 2});
            }
        },
        beforeSend:function(XMLHttpRequest){},
        complete:function(XMLHttpRequest){}
    });
}

/**
 * 加载cpu图表
 * 用户空间占用、内核空间占用、优化的进程占用、IO等待占用
 * @param id
 */
function load_cpu(id){
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('div-cpu'));
    window.addEventListener("resize",function(){
        myChart.resize();
    });
    $.ajax({
        url: portal.bp() + '/cpuInfo/detail',
        type: "get",
        async: true,
        cache: false,
        data: {"ip": id},
        dataType: "json",
        success: function (o) {
            var code = o.code;
            if (code == 0) {
                var data = o.data;
                var usCpuRate = typeof(data.usCpuRate) == 'string'?(parseFloat(data.usCpuRate)).toFixed(2):(data.usCpuRate).toFixed(2);
                var syCpuRate = typeof(data.syCpuRate) == 'string'?(parseFloat(data.syCpuRate)).toFixed(2):(data.syCpuRate).toFixed(2);
                var niCpuRate = typeof(data.niCpuRate) == 'string'?(parseFloat(data.niCpuRate)).toFixed(2):(data.niCpuRate).toFixed(2);
                var waCpuRate = typeof(data.waCpuRate) == 'string'?(parseFloat(data.waCpuRate)).toFixed(2):(data.waCpuRate).toFixed(2);
                var totalCpuRate = (parseFloat(usCpuRate) + parseFloat(syCpuRate) + parseFloat(niCpuRate) + parseFloat(waCpuRate)).toFixed(2);
                var data_cpu = [];
                var data_cpu_1 = [parseFloat(totalCpuRate),parseFloat(usCpuRate),parseFloat(syCpuRate),parseFloat(niCpuRate),parseFloat(waCpuRate)];
                var data_cpu_0 = [0,data_cpu_1[2]+data_cpu_1[3]+data_cpu_1[4],data_cpu_1[3]+data_cpu_1[4],data_cpu_1[4],0];

                data_cpu.push(data_cpu_0);
                data_cpu.push(data_cpu_1);

                var colors = '#0590eb';


                //需要删除开始
                if(count%2==0){
                    data_cpu = [
                        [0, 80, 60, 30, 0],
                        [90, 10, 20, 30, 30]
                    ];
                }
                //需要删除结束

                if(data_cpu[1][0]>=90){
                    colors = 'red';
                }

                option = {
                    title:{
                        show:false,
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        },
                        formatter: function (params) {
                            var tar = params[1];
                            return tar.name + '<br/>' + tar.seriesName + ' : ' + tar.value + ' %';
                        }
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        top: '16%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        splitLine: {show: false,},
                        axisLabel:{ //轴标签属性
                            interval :0, //标签全部显示
                        },
                        data: ['总占比', '用户空间', '内核空间', '改变优先级进程', 'IO等待']
                    },
                    yAxis: {
                        type: 'value',
                        name: '占比（%）',
                        nameLocation:'end',
                        max:100,
                        axisLabel:{ //轴标签属性
                            interval :0, //标签全部显示
                        },
                    },
                    series: [
                        {
                            name: '辅助',
                            type: 'bar',
                            stack: '总量',
                            itemStyle: {
                                barBorderColor: 'rgba(0,0,0,0)',
                                color: 'rgba(0,0,0,0)'
                            },
                            emphasis: {
                                itemStyle: {
                                    barBorderColor: 'rgba(0,0,0,0)',
                                    color: 'rgba(0,0,0,0)'
                                }
                            },
                            data: data_cpu[0]
                        },
                        {
                            name: '占比',
                            type: 'bar',
                            stack: '总量',
                            label: {
                                show: true,
                                position: 'inside'
                            },
                            itemStyle:{
                                normal: {
                                    color:function(params){
                                        // var colorlist = ['#0590eb','#0590eb','#0590eb'];
                                        // return colorlist[params.dataIndex];
                                        return colors;
                                    },
                                }
                            },
                            data: data_cpu[1]
                        }
                    ]
                };



                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            } else {
                layer.msg(o.msg, {icon: 2});
            }
        },
        beforeSend: function (XMLHttpRequest) {
        },
        complete: function (XMLHttpRequest) {
        }
    });
}

/**
 * 加载IO图表
 * 每秒传输速度、每秒读取数、每秒写入数
 * @param id
 */
function load_IO(id){
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('div-io'));
    window.addEventListener("resize",function(){
        myChart.resize();
    });
    $.ajax({
        url: portal.bp() + '/ioInfo/summary',
        type: "get",
        async: true,
        cache: false,
        data: {"ip": id},
        dataType: "json",
        success: function (o) {
            var code = o.code;
            if (code == 0) {
                var data = o.data;
                var data_io = [data.diskTrans,data.diskRead,data.diskWrite];

                //需要删除开始
                if(count%2==0){
                    data_io = [60,40,20];
                }
               //需要删除结束

                option = {
                    title: {
                        text: 'I/O使用情况',
                        subtext: '',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        },
                        formatter: '{b} : {c} KB'
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '5%',
                        top: '16%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'value',
                        name: '单位（KB）',
                        nameLocation:'middle',
                    },
                    yAxis: {
                        type: 'category',
                        inverse: true, //反向坐标轴
                        data: ['每秒传输速度','每秒读取数','每秒写入数']
                    },
                    series: [{
                        data: data_io,
                        type: 'bar',
                        itemStyle:{
                            normal:{
                                /*color:function(params){
                                    var colorlist = ['red','black','green'];
                                    return colorlist[params.dataIndex];
                                },*/
                                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
                                    offset: 0,
                                    color: '#0590eb' // 0% 处的颜色
                                }, {
                                    offset: 1,
                                    color: '#08e3f1' // 100% 处的颜色
                                }], false),
                            }
                        },
                        showBackground: true,
                        backgroundStyle: {
                            color: 'rgba(220, 220, 220, 0.8)'
                        }
                    }]
                };


                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            } else {
                layer.msg(o.msg, {icon: 2});
            }
        },
        beforeSend: function (XMLHttpRequest) {
        },
        complete: function (XMLHttpRequest) {
        }
    });
}


/**
 * 加载内存图表
 * 内存总大小、内存已使用大小、空闲内存大小、进程共享内存总额、缓存使用大小、缓存空闲大小、交换区总大小、交换区使用大小、交换区空闲大小
 * @param id
 */
function load_mem(id){
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('div-mem'));
    window.addEventListener("resize",function(){
        myChart.resize();
    });
    $.ajax({
        url: portal.bp() + '/memInfo/summary',
        type: "get",
        async: true,
        cache: false,
        data: {"ip": id},
        dataType: "json",
        success: function (o) {
            var code = o.code;
            if (code == 0) {
                var data = o.data;
                var data_mem = [data.memTotal,data.memUseTotal,data.freeMemTotal,data.sharedMemTotal,data.bufferCacheUseMemTotal,data.swapMemTotal,data.swapUseMemTotal,data.swapFreeMemTotal];


                //需要删除开始
                if(count%2==0){
                    data_mem = [60, 55, 5,20, 45, 25,15, 41, 28];
                }
                //需要删除结束




                option = {
                    title: {
                        text: '内存使用情况',
                        subtext: '',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        },
                        formatter: '{b} : {c} GB',
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '5%',
                        top: '16%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'value',
                        name: '单位（GB）',
                        nameLocation:'middle',
                    },
                    yAxis: {
                        type: 'category',
                        inverse: true, //反向坐标轴
                        data: ['内存总大小','内存已使用大小','空闲内存大小','进程共享内存总额','缓存使用大小','缓存空闲大小','交换区总大小','交换区使用大小','交换区空闲大小'],
                        axisLabel:{ //轴标签属性
                            interval :0, //标签全部显示
                        },
                    },
                    series: [{
                        data: data_mem,
                        type: 'bar',
                        itemStyle:{
                            normal:{
                                /*color:function(params){
                                    var colorlist = ['red','black','green'];
                                    return colorlist[params.dataIndex];
                                },*/
                                color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{
                                    offset: 0,
                                    color: '#0590eb' // 0% 处的颜色
                                }, {
                                    offset: 1,
                                    color: '#08e3f1' // 100% 处的颜色
                                }], false),
                            }
                        },
                        showBackground: true,
                        backgroundStyle: {
                            color: 'rgba(220, 220, 220, 0.8)'
                        }
                    }]
                };




                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            } else {
                layer.msg(o.msg, {icon: 2});
            }
        },
        beforeSend: function (XMLHttpRequest) {
        },
        complete: function (XMLHttpRequest) {
        }
    });
}

