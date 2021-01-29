<template>

    <div>
        <!--  客户列表文字  -->
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-addressbook"></i> 预定列表
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <!--  头部容器      -->
        <div class="container">
            <div class="handle-box">
                <el-button
                    round
                    plain
                    type="primary"
                    icon="el-icon-delete"
                    class="handle-del mr10"
                    @click="delAllSelection"
                >批量删除
                </el-button>
                <el-button plain round type="primary" icon="el-icon-s-home" @click="handCheckIn">直接入住</el-button>

                &nbsp;&nbsp;
                <el-input
                    round
                    v-model="bookMsgs.guestIdCard"
                    placeholder="请输入身份证号搜索,直接回车即可"
                    class="handle-input mr10"
                    clearable
                    prefix-icon="el-icon-search"
                    @clear="handleSearch"
                    @keydown.enter.native="handleSearch"
                >
                </el-input>
                <el-button plain round style="float: right" type="primary" icon="el-icon-plus" @click="handBook">添加预定</el-button>
            </div>

            <!-- 主列表 -->
            <el-table
                :data="tableData"
                style="width: 100%">

                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="guestIdCard" label="身份证号" align="center" width="200"></el-table-column>
                <el-table-column prop="resultRoom" label="房间号" align="center" width="200"></el-table-column>
                <el-table-column prop="rank" label="预定房间级别" align="center" width="200"></el-table-column>
                <el-table-column
                    label="预计入住时间"
                    width="180"
                    align="center"
                >
                    <template slot-scope="scope">
                        <i class="el-icon-time"></i>
                        <span style="margin-left: 10px">{{ scope.row.fromTime }}</span>
                    </template>
                </el-table-column>

                <el-table-column
                    label="预计退房时间"
                    width="180"
                    align="center"
                >
                    <template slot-scope="scope">
                        <i class="el-icon-time"></i>
                        <span style="margin-left: 10px">{{ scope.row.toTime }}</span>
                    </template>
                </el-table-column>

                <el-table-column label="状态" align="center" width="200">
                    <template slot-scope="scope">
                        <!--已处理1 蓝色,未处理0 红色,已入住11 绿色-->
                        <el-tag
                            v-if="scope.row.state===11"
                            :type="scope.row.state=== 11?'success':(scope.row.state===0?'danger':'')"
                        >已入住
                        </el-tag>
                        <el-tag
                            v-if="scope.row.state===1"
                            :type="scope.row.state=== 11?'success':(scope.row.state===0?'danger':'')"
                        >已处理
                        </el-tag>
                        <el-tag
                            v-if="scope.row.state===0"
                            :type="scope.row.state=== 11?'success':(scope.row.state===0?'danger':'')"
                        >未处理
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            icon="el-icon-edit"
                            @click="handleEdit(scope.$index, scope.row)">编辑
                        </el-button>
                        <el-button
                            plain
                            size="mini"
                            type="danger"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.$index, scope.row,scope.row.id)">删除
                        </el-button>
                        <el-button
                            size="mini"
                            type="primary"
                            icon="el-icon-bell"
                            plain
                            @click="handleSolve(scope.$index, scope.row,scope.row.rank)">处理
                        </el-button>
                        <el-button
                            plain
                            type="success"
                            size="mini"
                            icon="el-icon-key"
                            @click="handleIn(scope.row.id)">入住
                        </el-button>
                    </template>
                </el-table-column>

            </el-table>

            <!-- 添加弹出框 -->
            <el-dialog title="添加" :visible.sync="addVisible" width="30%">
                <el-form ref="form" :model="form" label-width="70px">
                    <el-form-item label="用户名">
                        <el-input v-model="form.name"></el-input>
                    </el-form-item>
                    <el-form-item label="身份证号">
                        <el-input v-model="form.idCard"></el-input>
                    </el-form-item>
                    <el-form-item label="联系电话">
                        <el-input v-model="form.contact"></el-input>
                    </el-form-item>
                    <el-form-item label="房间级别">
                        <el-select v-model="form.rank" placeholder="请选择">
                            <el-option
                                v-for="item in types"
                                :key="item"
                                :label="item"
                                :value="item">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="入住时间">
                        <el-date-picker
                            value-format="yyyy-MM-dd HH:mm:ss"
                            v-model=" form.fromTime"
                            type="date"
                            placeholder="选择日期时间">
                        </el-date-picker>


                    </el-form-item>
                    <el-form-item label="退房时间">
                        <el-date-picker
                            value-format="yyyy-MM-dd HH:mm:ss"
                            v-model="form.toTime"
                            type="date"
                            placeholder="选择日期时间">
                        </el-date-picker>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="addVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveBook">确 定</el-button>
                </span>
            </el-dialog>


            <!-- 编辑弹出框 -->
            <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
                <el-form ref="form" :model="form" label-width="70px">
                    <el-form-item label="身份证号">
                        <el-input v-model="form.guestIdCard" :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item label="房间级别">
                        <el-select v-model="form.rank" placeholder="请选择">
                            <el-option
                                v-for="item in types"
                                :key="item"
                                :label="item"
                                :value="item">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="入住时间">
                        <el-date-picker
                            value-format="yyyy-MM-dd HH:mm:ss"
                            v-model=" form.fromTime"
                            type="date"
                            placeholder="选择日期时间">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="退房时间">
                        <el-date-picker
                            value-format="yyyy-MM-dd HH:mm:ss"
                            v-model="form.toTime"
                            type="date"
                            placeholder="选择日期时间">
                        </el-date-picker>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
                </span>
            </el-dialog>

            <!--    处理弹出框    -->
            <el-dialog title="处理" :visible.sync="solveVisible" width="30%">
                <el-form ref="form" :model="form" label-width="70px">
                    <el-table
                        :data="roomTypeData"
                        style="width: 100%">
                        <el-table-column prop="roomId" label="房型编号" align="center"></el-table-column>
                        <el-table-column prop="rank" label="房间级别" align="center"></el-table-column>
                        <el-table-column prop="size" label="房间大小" align="center"></el-table-column>
                        <el-table-column prop="maxNum" label="容纳人数" align="center"></el-table-column>
                        <el-table-column prop="rent" label="房间单价" align="center"></el-table-column>
                        <el-table-column prop="earnest" label="房间定金" align="center"></el-table-column>
                    </el-table>
                    <br>
                    <br>
                    <el-form-item label="房间级别">
                        <el-input v-model="form.rank" :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item label="空房间号">
                        <el-select v-model="form.resultRoom" placeholder="请选择">
                            <el-option
                                v-for="item in optionsByClass"
                                :key="item.roomId"
                                :label="item.roomId"
                                :value="item.roomId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="solveVisible = false">取 消</el-button>
                <el-button type="primary" @click="solveRoomId">确 定</el-button>
                </span>
            </el-dialog>

            <!-- 入住弹出框 -->
            <el-dialog title="办理入住" :visible.sync="checkVisible" width="32%">
                <el-form ref="form" :model="form" label-width="70px">
                    <el-table
                        :data="roomTypeData"
                        style="width: 100%">
                        <el-table-column prop="roomId" label="房型号" align="center"></el-table-column>
                        <el-table-column prop="rank" label="房间级别" align="center"></el-table-column>
                        <el-table-column prop="size" label="房间大小" align="center"></el-table-column>
                        <el-table-column prop="maxNum" label="容纳人数" align="center"></el-table-column>
                        <el-table-column prop="rent" label="房间单价" align="center"></el-table-column>
                        <el-table-column prop="earnest" label="房间定金" align="center"></el-table-column>
                    </el-table>
                    <br>

                    <el-form-item label="用户名">
                        <el-input v-model="form.name"></el-input>
                    </el-form-item>
                    <el-form-item label="身份证号">
                        <el-input v-model="form.idCard"></el-input>
                    </el-form-item>
                    <el-form-item label="联系电话">
                        <el-input v-model="form.contact"></el-input>
                    </el-form-item>
                    <el-form-item label="空房间号">
                        <el-select v-model="form.resultRoom" placeholder="请选择">
                            <el-option
                                v-for="item in options"
                                :key="item.roomId"
                                :label="item.roomId"
                                :value="item.roomId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="入住时间">
                        <el-date-picker
                            value-format="yyyy-MM-dd"
                            v-model=" form.fromTime"
                            type="date"
                            placeholder="选择日期时间">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="退房时间">
                        <el-date-picker
                            value-format="yyyy-MM-dd"
                            v-model="form.toTime"
                            type="date"
                            placeholder="选择日期时间">
                        </el-date-picker>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="checkVisible = false">取 消</el-button>
                <el-button type="primary" @click="CheckIn">确 定</el-button>
                </span>
            </el-dialog>

            <!--  分页角标设置   -->
            <div class="pagination">
                <el-pagination
                    background
                    layout="total, prev, pager, next"
                    :current-page="bookMsgs.pageIndex"
                    :page-size="bookMsgs.pageSize"
                    :total="pageTotal"
                    @current-change="handlePageChange"
                ></el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
import { fetchData2 } from '../../api/index';

export default {
    name: 'ReserveTable',
    data () {
        return {
            time: 'hello world',
            bookMsgs: {
                id: '',
                guestIdCard: '',
                fromTime: '',
                toTime: '',
                rank: '',
                state: '',
                resultRoom: '',
                pageIndex: 1,       //当前在第几页
                pageSize: 10        //每页展示多少条数据
            },

            roomTypeData: [],
            types: [],
            tableData: [],
            options: [],         //供选择的所有空房间号
            optionsByClass: [],     //获取当前等级的空房间号
            multipleSelection: [],
            delList: [],
            nullRooms: '',
            editVisible: false,
            addVisible: false,
            solveVisible: false,
            checkVisible: false,
            pageTotal: 50,      //总共有多少条数据
            form: {},
            idx: -1
        };
    },
    created () {
        this.getAllBookMsgs();
        this.getNUllRooms();
        this.getAllClass();
        this.gerAllRooms();
    },

    methods: {


        //获取空房间
        getNUllRooms () {
            this.$http.get('http://localhost/getNullRooms').then(res => {
                //  console.log(res);
                // console.log(res);
                this.options = res.data.data.nullRooms;
                //  console.log(this.options);
            });
        },

        //获取所有预定信息
        getAllBookMsgs () {
            this.$http.get('http://localhost/getAllBookMsgs').then((res) => {
                // console.log(res);
                this.tableData = res.data.data.bookMsgs;
            });
        },

        gerAllRooms () {
            this.$http.get('http://localhost/getAllRooms').then(res => {
                //console.log(res);
                this.roomTypeData = res.data.data.roomMsgs;
            });
        },

        //获取所有级别信息
        getAllClass () {
            this.$http.get('http://localhost/getAllRooms').then(res => {
                //利用set去重
                const result = new Set();
                for (let i = 0; i < res.data.data.roomMsgs.length; i++) {
                    result.add(res.data.data.roomMsgs[i].rank);
                }
                this.types = result;
                //  console.log(this.types);
            });
        },


        handBook () {
            this.addVisible = true;
            this.form = {};
        },

        //删除预定信息
        handleDelete (index, row, bookid) {
            // 二次确认删除
            this.$confirm('确定要删除吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    this.$http.delete('http://localhost//deleteBookMsg?id=' + bookid).then((res) => {
                        if (res.data.code === 200) {
                            this.$message.success('删除成功');
                            this.tableData.splice(index, 1);
                            this.getAllBookMsgs();
                        } else {
                            this.$message.warning('抱歉您没有该权限');
                        }
                    });
                })
                .catch(() => {
                });
        },




        //添加预定信息
        saveBook () {
            let fromTime = new Date(this.form.fromTime.replace(new RegExp('-', 'gm'), '/')).getTime();
            let toTime = new Date(this.form.toTime.replace(new RegExp('-', 'gm'), '/')).getTime();
            fromTime = fromTime + 43200000;
            toTime = toTime + 43200000;
            const days = Math.floor((toTime - fromTime) / (24 * 3600 * 1000));
            console.log(days);
            console.log('formTime: ' + fromTime);
            console.log('toTime: ' + toTime);
            if (days >= 1) {
                this.$http.post('http://localhost/addBookMsg?contact=' + this.form.contact + '&fromTime=' + fromTime + '&id=0&idCard=' + this.form.idCard + '&name=' + this.form.name + '&toTime=' + toTime + '&rank=' + this.form.rank).then(res => {
                    //console.log(res);
                    if (res.data.code === 200) {
                        //1.提示成功
                        this.$message.success(`添加成功`);
                        //2.关闭对话框
                        this.addVisible = false;
                        //3.更新视图
                        this.getAllBookMsgs();
                        //4.清空输入文本框
                        this.form = {};
                    } else {
                        this.$message.warning('添加失败');
                    }
                });
            } else {
                this.$message.warning('入住时间至少大于1天');
            }
        },

        // 编辑操作
        handleEdit (index, row) {
            this.idx = index;
            this.form = row;
            this.editVisible = true;
        },

        //编辑预定信息
        saveEdit () {
            let fromTime = new Date(this.form.fromTime.replace(new RegExp('-', 'gm'), '/')).getTime();
            let toTime = new Date(this.form.toTime.replace(new RegExp('-', 'gm'), '/')).getTime();
            fromTime = fromTime + 43200000;
            toTime = toTime + 43200000;
            const days = Math.floor((toTime - fromTime) / (24 * 3600 * 1000));
            if (days >= 1) {
                this.$http.put('http://localhost//updateBookMsg?fromTime=' + fromTime + '&id=' + this.form.id + '&rank=' + this.form.rank + '&toTime=' + toTime).then(res => {
                    //console.log(res);
                    if (res.data.code === 200) {
                        //1.提示成功
                        this.$message.success(`修改成功`);
                        //2.关闭对话框
                        this.editVisible = false;
                        //3.更新视图
                        this.getAllBookMsgs();
                        //4.清空输入文本框
                        this.form = {};
                    } else {
                        this.$message.warning('修改失败');
                    }
                });
            } else {
                this.$message.warning('入住时间至少大于1天');
            }
        },

        //直接入住弹出框
        handCheckIn () {
            this.checkVisible = true;
            this.form = {};
        },

        CheckIn () {
            let fromTime = new Date(this.form.fromTime.replace(new RegExp('-', 'gm'), '/')).getTime();
            let toTime = new Date(this.form.toTime.replace(new RegExp('-', 'gm'), '/')).getTime();
            fromTime = fromTime + 43200000;
            toTime = toTime + 43200000;
            const days = Math.floor((toTime - fromTime) / (24 * 3600 * 1000));
            if (days >= 1) {
                this.$http.post('http://localhost/checkIn?contact=' + this.form.contact + '&fromTime=' + fromTime + '&idCard=' + this.form.idCard + '&name=' + this.form.name + '&toTime=' + toTime + '&roomId=' + this.form.resultRoom).then(res => {
                    //console.log(res);
                    if (res.data.code === 200) {
                        //1.提示成功
                        this.$message.success(`您已入住成功!`);
                        //2.关闭对话框
                        this.checkVisible = false;
                        //3.更新视图
                        this.getAllBookMsgs();
                        //4.清空输入文本框
                        this.form = {};
                    } else {
                        this.$message.warning('抱歉,入住失败');
                    }
                });
            } else {
                this.$message.warning('入住时间至少大于1天');
            }
        },

        //处理弹出框
        handleSolve (index, row) {
            this.idx = index;
            this.form = row;
            const fromTime = new Date(this.form.fromTime.replace(new RegExp('-', 'gm'), '/')).getTime();
            const toTime = new Date(this.form.toTime.replace(new RegExp('-', 'gm'), '/')).getTime();
            console.log('formTime: ' + fromTime);
            console.log('toTime: ' + toTime);
            this.$http.get('http://localhost//getNullRoomsByRank?fromTime=' + fromTime + '&rank=' + this.form.rank + '&toTime=' + toTime).then(res => {
                //console.log(res.data);
                this.optionsByClass = res.data.data.nullRooms;
            });
            this.solveVisible = true;
        },

        //处理分配房间问题
        solveRoomId () {
            //resultRoom
            this.$http.put('http://localhost//assignment?bookMsgId=' + this.form.id + '&roomId=' + this.form.resultRoom).then(res => {
                console.log(res);
                if (res.data.code === 200) {
                    //1.提示成功
                    this.$message.success(`分配房间成功`);
                    //2.关闭对话框
                    this.solveVisible = false;
                    //3.更新视图
                    this.getAllBookMsgs();
                    //4.清空输入文本框
                    this.form = {};
                } else {
                    this.$message.error('房间类型不匹配，房间失败');
                    this.solveVisible = false;
                }
            });
        },

        //入住
        handleIn (bookId) {
            this.$http.post('http://localhost/bookCheckIn?bookMsgId=' + bookId).then(res => {
                //console.log(res);
                if (res.data.code === 200) {
                    //1.提示成功
                    this.$message.success(`您已入住成功!`);
                    //2.关闭对话框
                    this.editVisible = false;
                    //3.更新视图
                    this.getAllBookMsgs();
                    //4.清空输入文本框
                    this.form = {};
                } else {
                    this.$message.warning('抱歉,未处理无法入住');
                }
            });
        },

        // 搜索功能
        handleSearch () {
            //console.log(this.bookMsgs.guestIdCard);
            this.$http.get('http://localhost/getBookMsgByIdCard?idCard=' + this.bookMsgs.guestIdCard).then(res => {
                console.log(res.data);
                if (res.data.code === 200) {
                    console.log(res.data.data.bookMsgs);
                    this.tableData = res.data.data.bookMsgs;
                } else {
                    this.$message.error('抱歉没有该数据');
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
                str += this.multipleSelection[i].name + ' ';
            }
            this.$message.error(`删除了${str}`);
            this.multipleSelection = [];
        },

        // 分页导航
        handlePageChange (val) {
            this.$set(this.bookMsgs, 'pageIndex', val);
            this.getAllBookMsgs();
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
    margin-right: 10px;
}
</style>
