//自定义函数区
function time(timestp) {
	var myDate = new Date(timestp)
	return myDate;
}

function hour(timestp) {
	var myDate = new Date(timestp)
	var hour = parseInt(myDate.getHours());
	return hour;
}

//数据区
var data = {
	"place": "19-20栋"
};
var timestp = {
	"out": [1554852949000, 1554872726000, 1554946828000, 1554958548000],
	"in": [1554869437000, 1554903753000, 1554956455000, 1554991855000]
};
//图表坐标数据
var outData = [
	[timestp.out[0], hour(timestp.out[0])],
	[timestp.out[1], hour(timestp.out[1])],
	[timestp.out[2], hour(timestp.out[2])],
	[timestp.out[3], hour(timestp.out[3])],
];
var inData = [
	[timestp.in[0], hour(timestp.in[0])],
	[timestp.in[1], hour(timestp.in[1])],
	[timestp.in[2], hour(timestp.in[2])],
	[timestp.in[3], hour(timestp.in[3])],
];

//通行记录
var chartAcc = Highcharts.chart('container', {
	chart: {
		type: 'scatter',
		zoomType: 'xy'
	},
	title: {
		text: '最近通行记录'
	},
	subtitle: {
		text: '数据来源: nchu'
	},
	credits: {
		enabled: false
	},
	responsive: {
		rules: [{ // 在图表小于 500px 的情况下关闭图例
			condition: { // 响应条件
				maxWidth: 500
			},
			chartOptions: { // 响应内容
				legend: {
					enabled: false
				}
			}
		}]
	},
	xAxis: {
		title: {
			enabled: true,
			text: '日期'
		},
		type: 'datetime',
		dateTimeLabelFormats: {
			day: '%e of %b'
		},
		/*floor: 1,
		ceiling: 31,*/
		startOnTick: true,
		endOnTick: true,
		showLastLabel: true,
		tickInterval: 24 * 3600 * 1000,
		/*labels: {
			formatter: function() {
				return time(this.value).getDate();
			},
			step: 1 //坐标刻度显示间隔
		},*/
	},
	yAxis: {
		title: {
			text: '时间'
		},
		floor: 0,
		ceiling: 24,
		max: 24,
		min: 0,
		tickInterval: 4,
		/*tickAmount: 6,*/
		/*minTickInterval:1,*/
		/*tickInterval: 2,
		tickAmount: 6,
		*/
		/*labels: {
			formatter: function() {
				if(this.value < 23) {
					return this.value;
				} else if(this.value >= 23 && this.value <= 24) {
					return this.value + "(晚归)";
				} else {
					return '';
				}
			},
			step: 1 //坐标刻度显示间隔
		}*/
	},
	legend: {
		align: 'right',
		verticalAlign: 'top',
		floating: true,
		/*layout: 'vertical',*/
		x: -40,
		/*y: 70,*/
		/*backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF',*/
		/*borderWidth: 1*/
	},
	plotOptions: {
		scatter: {
			marker: {
				radius: 5,
				states: {
					hover: {
						enabled: true,
						lineColor: 'rgb(100,100,100)'
					}
				}
			},
			states: {
				hover: {
					marker: {
						enabled: false
					}
				}
			},
			tooltip: {
				/*headerFormat: '<b>{series.name}</b><br>',*/
				/*pointFormat: '时间：' + '{point.x}号, {point.y}<br>',*/
				pointFormatter: function() {
					return '时间：<b>' + (time(this.x).getMonth() + 1) + '月' +
						time(this.x).getDate() + '日 ' +
						time(this.x).getHours() + ':' +
						time(this.x).getMinutes() + '</b><br/>'
				},
				footerFormat: '地点：' + data.place
			}
		}
	},
	series: [{
		name: '外出',
		color: 'rgba(180,238,180,1)',
		data: outData
	}, {
		name: '归寝',
		color: 'rgba(164,211,238,1)',
		data: inData
	}]

});

//异常数据图表
var chartYic = Highcharts.chart('yichang', {
	chart: {
		type: 'bar' //指定图表的类型，默认是折线图（line）
	},
	title: {
		text: '异常通行数据' // 标题
	},
	credits: {
		enabled: false
	},
	responsive: {
		rules: [{ // 在图表小于 500px 的情况下关闭图例
			condition: { // 响应条件
				maxWidth: 500
			},
			chartOptions: { // 响应内容
				legend: {
					enabled: false
				}
			}
		}]
	},
	xAxis: {
		categories: ['外出', '归寝', '其它'] // x 轴分类
	},
	yAxis: {
		title: {
			text: '数量' // y 轴标题
		},
		tickInterval: 1,
	},
	legend: {
		align: 'right',
		verticalAlign: 'top',
		floating: true,
		/*layout: 'vertical',*/
		x: -40,
		/*y: 70,*/
		/*backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF',*/
		/*borderWidth: 1*/
	},
	series: [{ // 数据列
		name: '异常数量', // 数据列名
		data: [2, 1, 4], // 数据
		color: 'rgba(255,100,97,1)'
	}]
});

//访客数据图表
var total = 10;
var last = 8;
var chartVisitor = Highcharts.chart('visitor', {
	chart: {
		plotBackgroundColor: null,
		plotBorderWidth: null,
		plotShadow: false,
		type: 'pie'
	},
	title: {
		text: '本月录入访客情况'
	},
	credits: {
		enabled: false
	},
	tooltip: {
		/*headerFormat: '{series.name}<br>',*/
		/*pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>',*/
		/*footerFormat: ' (',*/
		pointFormatter: function() {
			return this.percentage + '%' + ' (' + (this.percentage / 100) * total + ')';
		},
	},
	plotOptions: {
		pie: {
			allowPointSelect: true,
			cursor: 'pointer',
			dataLabels: {
				enabled: true,
				format: '<b>{point.name}</b>: {point.percentage:.1f} %',
				style: {
					color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
				}
			}
		}
	},
	series: [{
		name: '占比',
		colorByPoint: true,
		data: [{
			name: '剩余额度',
			y: (last / total) * 100,
			sliced: true,
			selected: true
		}, {
			name: '已用额度',
			y: ((total - last) / total) * 100,
			color: 'rgba(223, 83, 83, .5)'
		}]
	}]
});