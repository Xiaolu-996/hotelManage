<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card shadow="hover" class="mgb20" style="height:252px;">
                    <div class="user-info">
                        <img src="../../assets/img/people.gif" class="user-avator" alt />
                        <div class="user-info-cont">
                            <div class="user-info-name">{{ name }}</div>
                            <div>{{ role }}</div>
                        </div>
                    </div>
                    <div class="user-info-list">
                        登录地点：
                        <span>    成都</span>
                    </div>
                    <div class="user-info-list">
                        上次登录时间：
                        <span>2021-1-12</span>
                    </div>
                </el-card>
                <!--
            <el-card shadow="hover" style="height:252px;">
                <div slot="header" class="clearfix">
                    <span>房间总量详情</span>
                </div>
                剩余房间量
                <el-progress :percentage="36" color="#42b983"></el-progress>
                清扫房间量
                <el-progress :percentage="9" color="#f1e05a"></el-progress>
                预定房间量
                <el-progress :percentage="23" color="#f56c6c"></el-progress>
                已住房间量
                <el-progress :percentage="32"></el-progress>
            </el-card>
        </el-col>
             <el-col :span="16">
                        <el-row :gutter="20" class="mgb20">
                            <el-col :span="8">
                                <el-card shadow="hover" :body-style="{padding: '0px'}">
                                    <div class="grid-content grid-con-1">
                                        <i class="el-icon-lx-people grid-con-icon"></i>
                                        <div class="grid-cont-right">
                                            <div class="grid-num">123</div>
                                            <div>用户入住总量</div>
                                        </div>
                                    </div>
                                </el-card>
                            </el-col>
                            <el-col :span="8">
                                <el-card shadow="hover" :body-style="{padding: '0px'}">
                                    <div class="grid-content grid-con-2">
                                        <i class="el-icon-lx-notice grid-con-icon"></i>
                                        <div class="grid-cont-right">
                                            <div class="grid-num">321</div>
                                            <div>房间剩余总量</div>
                                        </div>
                                    </div>
                                </el-card>
                            </el-col>
                            <el-col :span="8">
                                <el-card shadow="hover" :body-style="{padding: '0px'}">
                                    <div class="grid-content grid-con-3">
                                        <i class="el-icon-lx-goods grid-con-icon"></i>
                                        <div class="grid-cont-right">
                                            <div class="grid-num">65</div>
                                            <div>房间预定总量</div>
                                        </div>
                                    </div>
                                </el-card>
                            </el-col>
                        </el-row>
                        <el-card shadow="hover" style="height:403px;">
                            <div slot="header" class="clearfix">
                                <span>待办事项</span>
                                <el-button style="float: right; padding: 3px 0" type="text">添加</el-button>
                            </div>
                            <el-table :show-header="false" :data="todoList" style="width:100%;">
                                <el-table-column width="40">
                                    <template slot-scope="scope">
                                        <el-checkbox v-model="scope.row.status"></el-checkbox>
                                    </template>
                                </el-table-column>
                                <el-table-column>
                                    <template slot-scope="scope">
                                        <div
                                            class="todo-item"
                                            :class="{'todo-item-del': scope.row.status}"
                                        >{{ scope.row.title }}
                                        </div>
                                    </template>
                                </el-table-column>
                                <el-table-column width="60">
                                    <template>
                                        <i class="el-icon-edit" style="color: cornflowerblue"></i>
                                        <i class="el-icon-delete" style="color: red"></i>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </el-card>

                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-card shadow="hover">
                            <schart ref="bar" class="schart" canvasId="bar" :options="options"></schart>
                        </el-card>
                    </el-col>
                    <el-col :span="12">
                        <el-card shadow="hover">
                            <schart ref="line" class="schart" canvasId="line" :options="options2"></schart>
                        </el-card>-->
            </el-col>
        </el-row>
    </div>
</template>

<script>
import Schart from 'vue-schart';
import bus from '../common/bus';

export default {
    name: 'dashboard',
    data () {
        return {
            name: localStorage.getItem('ms_username'),
            todoList: [
                {
                    title: '1907号房需要零食',
                    status: true
                },
                {
                    title: '2203号房需要矿泉水',
                    status: false
                },
                {
                    title: '3306号房需要午餐',
                    status: true
                },
                {
                    title: '5221号房需要晚餐',
                    status: false
                },
                {
                    title: '7099号房请及打扫',
                    status: false
                },
                {
                    title: '5231号房请及打扫',
                    status: true
                }
            ],
            data: [
                {
                    name: '2018/09/04',
                    value: 1083
                },
                {
                    name: '2018/09/05',
                    value: 941
                },
                {
                    name: '2018/09/06',
                    value: 1139
                },
                {
                    name: '2018/09/07',
                    value: 816
                },
                {
                    name: '2018/09/08',
                    value: 327
                },
                {
                    name: '2018/09/09',
                    value: 228
                },
                {
                    name: '2018/09/10',
                    value: 1065
                }
            ],
            options: {
                type: 'bar',
                title: {
                    text: '近一周部分住房量柱状图'
                },
                xRorate: 25,
                labels: ['周一', '周二', '周三', '周四', '周五'],
                datasets: [
                    {
                        label: '标准大床房',
                        data: [234, 278, 270, 190, 230]
                    },
                    {
                        label: '商务双床房',
                        data: [164, 178, 190, 135, 160]
                    },
                    {
                        label: '豪华大床房',
                        data: [144, 198, 150, 235, 120]
                    }
                ]
            },
            options2: {
                type: 'line',
                title: {
                    text: '近半年部分住房量趋势图'
                },
                labels: ['6月', '7月', '8月', '9月', '10月'],
                datasets: [
                    {
                        label: '标准大床房',
                        data: [234, 278, 270, 190, 230]
                    },
                    {
                        label: '商务双床房',
                        data: [164, 178, 150, 135, 160]
                    },
                    {
                        label: '豪华大床房',
                        data: [74, 118, 200, 235, 90]
                    }
                ]
            }
        };
    },
    components: {
        Schart
    },
    computed: {
        role () {
            return this.name === 'admin' ? '超级管理员' : '前台管理员';
        }
    },
    // created() {
    //     this.handleListener();
    //     this.changeDate();
    // },
    // activated() {
    //     this.handleListener();
    // },
    // deactivated() {
    //     window.removeEventListener('resize', this.renderChart);
    //     bus.$off('collapse', this.handleBus);
    // },
    methods: {
        changeDate () {
            const now = new Date().getTime();
            this.data.forEach((item, index) => {
                const date = new Date(now - (6 - index) * 86400000);
                item.name = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`;
            });
        }
        // handleListener() {
        //     bus.$on('collapse', this.handleBus);
        //     // 调用renderChart方法对图表进行重新渲染
        //     window.addEventListener('resize', this.renderChart);
        // },
        // handleBus(msg) {
        //     setTimeout(() => {
        //         this.renderChart();
        //     }, 200);
        // },
        // renderChart() {
        //     this.$refs.bar.renderChart();
        //     this.$refs.line.renderChart();
        // }
    }
};
</script>


<style scoped>
.el-row {
    margin-bottom: 20px;
}

.grid-content {
    display: flex;
    align-items: center;
    height: 100px;
}

.grid-cont-right {
    flex: 1;
    text-align: center;
    font-size: 14px;
    color: #999;
}

.grid-num {
    font-size: 30px;
    font-weight: bold;
}

.grid-con-icon {
    font-size: 50px;
    width: 100px;
    height: 100px;
    text-align: center;
    line-height: 100px;
    color: #fff;
}

.grid-con-1 .grid-con-icon {
    background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
    background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
    color: rgb(45, 140, 240);
}

.grid-con-3 .grid-con-icon {
    background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
    color: rgb(242, 94, 67);
}

.user-info {
    display: flex;
    align-items: center;
    padding-bottom: 20px;
    border-bottom: 2px solid #ccc;
    margin-bottom: 20px;
}

.user-avator {
    width: 120px;
    height: 120px;
    border-radius: 50%;
}

.user-info-cont {
    padding-left: 50px;
    flex: 1;
    font-size: 14px;
    color: #999;
}

.user-info-cont div:first-child {
    font-size: 30px;
    color: #222;
}

.user-info-list {
    font-size: 14px;
    color: #999;
    line-height: 25px;
}

.user-info-list span {
    margin-left: 70px;
}

.mgb20 {
    margin-bottom: 20px;
}

.todo-item {
    font-size: 14px;
}

.todo-item-del {
    text-decoration: line-through;
    color: #999;
}

.schart {
    width: 100%;
    height: 300px;
}
</style>
