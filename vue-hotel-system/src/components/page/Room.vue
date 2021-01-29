<template>

    <div>
        <!--  客户列表文字  -->
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-school"></i> 房间列表
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <!--  头部容器      -->
        <div class="container">
            <div class="handle-box">
                <el-button plain round type="primary" icon="el-icon-s-promotion" @click="getAllRooms">所有房间</el-button>
                &nbsp;
                <el-dropdown>
                    <el-button
                        type="primary"
                        round
                        plain
                    >
                        更多操作<i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item @click.native="getHasBookedRoom">已预定房间</el-dropdown-item>
                        <el-dropdown-item @click.native="getHasCheckedRoom">已入住房间</el-dropdown-item>
                        <el-dropdown-item @click.native="getNUllRooms">剩余空房间</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>


                &nbsp;&nbsp;
                <el-input
                    round
                    v-model="roomMsgs.roomId"
                    placeholder="请输入房间号搜索,直接回车即可"
                    class="handle-input mr10"
                    clearable
                    prefix-icon="el-icon-search"
                    @clear="handleSearch"
                    @keydown.enter.native="handleSearch"
                >
                </el-input>
                <el-button plain round style="float: right" type="primary" icon="el-icon-plus" @click="handBook">添加房间</el-button>
            </div>

            <!-- 主列表 -->
            <el-table
                :data="tableData"
                style="width: 100%"
                border
            >
                <el-table-column prop="roomId" label="房间号" align="center" width="150"></el-table-column>
                <el-table-column prop="rank" label="房间级别" align="center" width="150"></el-table-column>
                <el-table-column prop="size" label="房间大小" align="center" width="150"></el-table-column>
                <el-table-column prop="maxNum" label="容纳人数" align="center" width="150"></el-table-column>
                <el-table-column prop="rent" label="房间单价" align="center" width="150"></el-table-column>
                <el-table-column prop="earnest" label="房间定金" align="center" width="150"></el-table-column>
                <el-table-column prop="position" label="房间位置" align="center" width="150"></el-table-column>

                <el-table-column label="房间状态" align="center" width="100">
                    <template slot-scope="scope">
                        <el-tag
                            v-if="scope.row.state===1"
                            :type="scope.row.state=== 1?'success':(scope.row.state===0?'danger':'')"
                        >已入住
                        </el-tag>
                        <el-tag
                            v-if="scope.row.state===-1"
                            :type="scope.row.state=== 11?'success':(scope.row.state===-1?'danger':'')"
                        >空房
                        </el-tag>
                        <el-tag
                            v-if="scope.row.state===0"
                            :type="scope.row.state=== 11?'success':(scope.row.state===9?'danger':'')"
                        >已预订
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                    label="时间"
                    width="180"
                    align="center"
                >
                    <template slot-scope="scope">
                        <i class="el-icon-time"></i>
                        <span style="margin-left: 10px">{{ scope.row.time }}</span>
                    </template>
                </el-table-column>


                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            icon="el-icon-edit"
                            v-if="name==='admin'"
                            @click="handleEdit(scope.$index, scope.row)">编辑
                        </el-button>
                        <el-button
                            plain
                            size="mini"
                            type="danger"
                            icon="el-icon-delete"
                            v-if="name==='admin'"
                            @click="handleDelete(scope.$index, scope.row,scope.row.roomId)">删除
                        </el-button>
                        <el-button
                            plain
                            size="mini"
                            icon="el-icon-coffee-cup"
                            v-if="name!=='admin'"
                            @click="handleService(scope.row.roomId)">消费
                        </el-button>
                        <el-button
                            plain
                            size="mini"
                            type="success"
                            icon="el-icon-chat-round"
                            v-if="name!=='admin'"
                            @click="inspect(scope.row.roomId)">结算
                        </el-button>
                        <el-button
                            plain
                            icon="el-icon-lx-exit"
                            type="primary"
                            v-if="name!=='admin'"
                            @click="checkOut(scope.row.roomId)">退房
                        </el-button>
                    </template>
                </el-table-column>

            </el-table>


            <!-- 编辑弹出框 -->
            <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
                <el-form ref="form" :model="form" label-width="80px" :rules="rules">
                    <el-form-item label="房间号" prop="roomId">
                        <el-input v-model="form.roomId"></el-input>
                    </el-form-item>
                    <el-form-item label="房间级别">
                        <el-input v-model="form.rank"></el-input>
                    </el-form-item>
                    <el-form-item label="房间大小" prop="size">
                        <el-input v-model="form.size" placeholder="请输入正整数"></el-input>
                    </el-form-item>
                    <el-form-item label="容纳人数" prop="maxNum">
                        <el-input v-model="form.maxNum" placeholder="请输入正整数"></el-input>
                    </el-form-item>
                    <el-form-item label="房间单价" prop="rent">
                        <el-input v-model="form.rent" placeholder="请输入正整数"></el-input>
                    </el-form-item>
                    <el-form-item label="房间定金" prop="earnest">
                        <el-input v-model="form.earnest" placeholder="请输入正整数"></el-input>
                    </el-form-item>
                    <el-form-item label="房间位置">
                        <el-input v-model="form.position"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveRoomEdit">确 定</el-button>
                </span>
            </el-dialog>

            <!-- 添加弹出框 -->
            <el-dialog title="添加" :visible.sync="addVisible" width="30%">
                <el-form ref="form" :model="form" label-width="80px" :rules="rules">
                    <el-form-item label="房间号" prop="roomId">
                        <el-input v-model="form.roomId"></el-input>
                    </el-form-item>
                    <el-form-item label="房间级别">
                        <el-input v-model="form.rank"></el-input>
                    </el-form-item>
                    <el-form-item label="房间大小" prop="size">
                        <el-input v-model="form.size" placeholder="请输入正整数"></el-input>
                    </el-form-item>
                    <el-form-item label="容纳人数" prop="maxNum">
                        <el-input v-model="form.maxNum" placeholder="请输入正整数"></el-input>
                    </el-form-item>
                    <el-form-item label="房间单价" prop="rent">
                        <el-input v-model="form.rent" placeholder="请输入正整数"></el-input>
                    </el-form-item>
                    <el-form-item label="房间定金" prop="earnest">
                        <el-input v-model="form.earnest" placeholder="请输入正整数"></el-input>
                    </el-form-item>
                    <el-form-item label="房间位置">
                        <el-input v-model="form.position"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="addVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveRoom">确 定</el-button>
                </span>
            </el-dialog>

            <!--结算处理-->
            <el-dialog title="消费结算" :visible.sync="inspectVisible" width="30%">
                <el-form ref="form" :model="form" label-width="70px">
                    <el-form-item label="总消费">
                        <el-input v-model="form.toll"></el-input>
                    </el-form-item>
                    <el-form-item label="已支付">
                        <el-input v-model="form.hasSettle"></el-input>
                    </el-form-item>
                    <el-form-item label="待支付">
                        <el-input v-model="form.needSettle"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="inspectVisible = false">取消支付</el-button>
                <el-button type="primary" @click="surePay">确认支付</el-button>
                </span>
            </el-dialog>

            <!-- 服务弹出框-->
            <el-dialog title="服务列表" :visible.sync="serviceVisible" width="30%">
                <el-table
                    :data="serviceData"
                    style="width: 100%"
                    border
                >
                    <el-table-column prop="id" label="消费编号" align="center"></el-table-column>
                    <el-table-column prop="name" label="消费名称" align="center"></el-table-column>
                    <el-table-column prop="money" label="消费金额" align="center"></el-table-column>
                    <el-table-column label="操作" align="center">
                        <template slot-scope="scope">
                            <el-button
                                plain
                                size="mini"
                                icon="el-icon-coffee-cup"
                                @click="sureCost(scope.row.id)">订购
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="serviceVisible = false">订购完成</el-button>
                </span>
            </el-dialog>


            <!--  分页角标设置   -->
            <div class="pagination">
                <el-pagination
                    background
                    layout="total, prev, pager, next"
                    :current-page="roomMsgs.pageIndex"
                    :page-size="roomMsgs.pageSize"
                    :total="pageTotal"
                    @current-change="handlePageChange"
                ></el-pagination>
            </div>
        </div>
    </div>
</template>

<script>

export default {
    name: 'Room',
    data () {
        return {
            name: localStorage.getItem('ms_username'),
            roomMsgs: {
                roomId: '',
                size: '',
                rank: '',
                rent: '',
                earnest: '',
                maxNum: '',
                position: '',
                time: '',
                pageIndex: 1,       //当前在第几页
                pageSize: 10        //每页展示多少条数据
            },



            costTypes: {
                id: '',
                name: '',
                money: '',
                serviceNeedRooId: ''
            },

            value: '',
            serviceData: [],    //服务数据
            tableData: [],
            multipleSelection: [],
            delList: [],
            flag: false,
            switchValue: true,
            editVisible: false,
            addVisible: false,
            inspectVisible: false,
            serviceVisible: false,
            pageTotal: 50,      //总共有多少条数据
            form: {
                roomIdPay: '',
                hasSettle: '',      //已支付
                toll: '',           //总消费
                needSettle: ''      //尚需
            },
            idx: -1,
            id: -1,

            rules: {
                size: [
                    { required: true, message: '请输入正整数', trigger: 'blur' }
                ],
                maxNum: [
                    { required: true, message: '请输入正整数', trigger: 'blur' }
                ],
                earnest: [
                    { required: true, message: '请输入正整数', trigger: 'blur' }
                ],
                rent: [
                    { required: true, message: '请输入正整数', trigger: 'blur' }
                ]
            }
        };
    },
    created () {
        this.getAllRooms();
    },
    methods: {

        //订购消费
        sureCost (costTypesId) {
            console.log('costTypesId: ' + costTypesId);
            console.log('serviceNeedRooId: ' + this.costTypes.serviceNeedRooId);
            this.$http.post('http://localhost/addCost?costTypeId=' + costTypesId + '&id=0&roomId=' + this.costTypes.serviceNeedRooId).then(res => {
                console.log(res);
                if (res.data.code === 200) {
                    //1.提示成功
                    this.$message.success(`订购成功！`);
                } else {
                    this.$message.error('订购失败');
                }
            });

        },

        //获取所有消费信息
        getAllCostType () {
            this.$http.get('http://localhost/getAllCostType').then((res) => {
                //console.log(res);
                this.serviceData = res.data.data.costTypes;
            });
        },

        //服务添加
        handleService (serviceNeedRooId) {
            this.costTypes.serviceNeedRooId = serviceNeedRooId;
            this.serviceVisible = true;
            this.getAllCostType();
        },

        //确认支付
        surePay () {
            console.log(this.form.roomIdPay);
            this.$http.post('http://localhost/settleCostByRoomId?roomId=' + this.form.roomIdPay).then(res => {
                console.log(res);
                if (res.data.code === 200) {
                    //1.提示成功
                    this.$message.success(`结算成功！`);
                    this.inspectVisible = false;
                    this.form = {};
                } else {
                    this.$message.error('结算失败');
                }
            });
        },

        //查看当前消费
        inspect (roomId) {
            this.form.roomIdPay = roomId;
            this.inspectVisible = true;
            this.$http.post('http://localhost/getCostByRoomId?roomId=' + roomId).then(res => {
                //console.log(res);
                this.form.hasSettle = res.data.data.hasSettle;
                this.form.toll = res.data.data.toll;
                this.form.needSettle = res.data.data.needSettle;
            });
        },

        //退房
        checkOut (checkroomId) {
            //console.log(this.form);
            this.$http.post('http://localhost/checkOut?roomId=' + checkroomId).then(res => {
                console.log(res);
                if (res.data.code === 200) {
                    //1.提示成功
                    this.$message.success(`您已退房成功！`);
                    //2.关闭对话框
                    //3.更新视图
                    this.getAllRooms();
                    //4.清空输入文本框
                    this.form = {};
                } else {
                    this.$message.error('消费未结清退房失败');
                }
            });
        },

        getAllRooms () {
            this.$http.get('http://localhost/getAllRooms').then((res) => {
                //console.log(res);
                this.tableData = res.data.data.roomMsgs;
            });
        },

        // 编辑
        saveRoomEdit () {
            //console.log(this.form);
            if (this.form.maxNum > 0 && this.form.earnest > 0 && this.form.rent > 0 && this.form.size > 0) {
                this.$http.post('http://localhost/addRoom?earnest=' + this.form.earnest + '&roomId=' + this.form.roomId + '&maxNum=' + this.form.maxNum + '&rank=' + this.form.rank + '&rent=' + this.form.rent + '&size=' + this.form.size + '&position=' + this.form.position).then(res => {
                    //console.log(res);
                    if (res.data.code === 200) {
                        //1.提示成功
                        this.$message.success(`修改成功`);
                        //2.关闭对话框
                        this.editVisible = false;
                        //3.更新视图
                        this.getAllRooms();
                        //4.清空输入文本框
                        this.form = {};
                    } else {
                        this.$message.warning('修改失败');
                    }
                });
            } else {
                this.$message.warning('请检查输入数值是否正确');
            }
        },


        // 添加
        saveRoom () {
            if (this.form.maxNum > 0 && this.form.earnest > 0 && this.form.rent > 0 && this.form.size > 0) {
                //console.log(this.form);
                this.$http.post('http://localhost/addRoom?earnest=' + this.form.earnest + '&roomId=' + this.form.roomId + '&maxNum=' + this.form.maxNum + '&rank=' + this.form.rank + '&rent=' + this.form.rent + '&size=' + this.form.size + '&position=' + this.form.position).then(res => {
                    //console.log(res);
                    if (res.data.code === 200) {
                        //1.提示成功
                        this.$message.success(`添加成功`);
                        //2.关闭对话框
                        this.addVisible = false;
                        //3.更新视图
                        this.getAllRooms();
                        //4.清空输入文本框
                        this.form = {};
                    } else {
                        this.$message.warning('添加失败');
                    }
                });
            } else {
                this.$message.warning('请检查输入数值是否正确');
            }
        },


        //删除预定信息
        handleDelete (index, row, roomId) {
            if (localStorage.getItem('ms_username') === 'admin') {
                // 二次确认删除
                this.$confirm('确定要删除吗？', '提示', {
                    type: 'warning'
                })
                    .then(() => {
                        this.$http.delete('http://localhost//deleteRoom?id=' + roomId).then((res) => {
                            if (res.data.code === 200) {
                                this.$message.success('删除成功');
                                this.tableData.splice(index, 1);
                                this.getAllRooms();
                            } else {
                                this.$message.warning('删除失败');
                            }
                        });
                    })
                    .catch(() => {
                    });
            } else {
                this.$message.error('抱歉您没有该权限');
            }

        },


        //添加预定信息框
        handBook () {
            if (localStorage.getItem('ms_username') === 'admin') {
                this.addVisible = true;
                this.form = {};
            } else {
                this.$message.error('抱歉您没有该权限');
            }
        },


        //添加预定信息    待处理！！！！！！！！！！！！！！！
        saveBook () {
            //console.log(this.form);
            this.$http.post('http://localhost/addGuest?contact=' + this.form.contact + '&idCard=' + this.form.idCard + '&name=' + this.form.name).then(res => {
                //console.log(res);
                if (res.data.code === 200) {
                    //1.提示成功
                    this.$message.success(`添加成功`);
                    //2.关闭对话框
                    this.addVisible = false;
                    //3.更新视图
                    this.getAllRooms();
                    //4.清空输入文本框
                    this.form = {};
                } else {
                    this.$message.warning('添加失败');
                }
            });
        },

        // 编辑操作
        handleEdit (index, row) {
            if (localStorage.getItem('ms_username') === 'admin') {
                this.idx = index;
                this.form = row;
                this.editVisible = true;
            } else {
                this.$message.error('抱歉您没有该权限');
            }
        },

        // 搜索功能
        handleSearch () {
            this.$http.get('http://localhost/getRoomById?id=' + this.roomMsgs.roomId).then(res => {
                //console.log(res.data);
                if (res.data.code === 200) {
                    let array = [];
                    array.push(res.data.data.room);
                    this.tableData = array;
                } else {
                    this.$message.error('抱歉没有该数据');
                }
            });
        },

        //获取空房间
        getNUllRooms () {
            this.$http.get('http://localhost/getNullRooms').then(res => {
                //  console.log(res);
                if (res.data.data === null) {
                    this.$message.info('没有剩余房间了');
                } else {
                    console.log(res);
                    this.tableData = res.data.data.nullRooms;
                }
            });
        },

        //获取已经预定的房间
        getHasBookedRoom () {
            this.$http.get('http://localhost/getHasBookedRoom').then(res => {
                if (res.data.data === null) {
                    this.$message.info('已预订房间为空');
                } else {
                    console.log(res);
                    this.tableData = res.data.data.roomMsgs;
                }
            });
        },

        //获取已经入住的房间
        getHasCheckedRoom () {
            this.$http.get('http://localhost/getHasCheckedRoom').then(res => {
                if (res.data.data === null) {
                    this.$message.info('已入住房间为空');
                } else {
                    console.log(res);
                    this.tableData = res.data.data.roomMsgs;
                }
            });
        },

        // 多选操作
        handleSelectionChange (val) {
            this.multipleSelection = val;
        },

        delAllSelection () {
            const length = this.multipleSelection.length;
            let str = '';
            this.delList = this.delList.concat(this.multipleSelection);
            for (let i = 0; i < length; i++) {
                str += this.multipleSelection[i].roomId + ' ';
            }
            this.$message.error(`删除了${str}`);
            this.multipleSelection = [];
        },

        // 分页导航
        handlePageChange (val) {
            this.$set(this.roomMsgs, 'pageIndex', val);
            this.getAllRooms();
        }
    }
};
</script>


<style scoped>
.handle-box {
    margin-bottom: 20px;
}

.handle-select {
    width: 120px;
}

.handle-input {
    width: 300px;
    display: inline-block;
}

.table {
    width: 100%;
    font-size: 14px;
}

.red {
    color: #ff0000;
}

.mr10 {
    margin-right: 2px;
}
</style>
