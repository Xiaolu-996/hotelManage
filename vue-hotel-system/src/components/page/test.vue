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
                    v-model="rooms.roomId"
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
                style="width: 100%">

                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="roomId" label="房间号" align="center" width="100"></el-table-column>
                <el-table-column prop="position" label="房间位置" align="center"></el-table-column>
                <el-table-column prop="typeId" label="房间级别" align="center"></el-table-column>
                <el-table-column prop="typeId" label="房间类型" align="center"></el-table-column>


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
                            @click="handleDelete(scope.$index, scope.row,scope.row.roomId)">删除
                        </el-button>
                        <el-button
                            plain
                            size="mini"
                            type="success"
                            icon="el-icon-chat-round"
                            @click="handleDelete(scope.$index, scope.row,scope.row.roomId)">查看
                        </el-button>
                        <el-button
                            plain
                            type="primary"
                            size="mini"
                            icon="el-icon-lx-exit"
                            @click="handleEdit(scope.$index, scope.row)">退房
                        </el-button>
                    </template>
                </el-table-column>

            </el-table>


            <!-- 编辑弹出框 -->
            <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
                <el-form ref="form" :model="form" label-width="70px">
                    <el-form-item label="房间号">
                        <el-input v-model="form.roomId"></el-input>
                    </el-form-item>
                    <el-form-item label="房间位置">
                        <el-input v-model="form.position"></el-input>
                    </el-form-item>
                    <el-form-item label="房间级别">
                        <el-input v-model="form.typeId"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveRoomEdit">确 定</el-button>
                </span>
            </el-dialog>

            <!-- 添加弹出框 -->
            <el-dialog title="添加" :visible.sync="addVisible" width="30%">
                <el-form ref="form" :model="form" label-width="70px">
                    <el-form-item label="房间号">
                        <el-input v-model="form.roomId"></el-input>
                    </el-form-item>
                    <el-form-item label="房间位置">
                        <el-input v-model="form.position"></el-input>
                    </el-form-item>
                    <el-form-item label="房间级别">
                        <el-input v-model="form.typeId"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="addVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveRoom">确 定</el-button>
                </span>
            </el-dialog>

            <!--  分页角标设置   -->
            <div class="pagination">
                <el-pagination
                    background
                    layout="total, prev, pager, next"
                    :current-page="rooms.pageIndex"
                    :page-size="rooms.pageSize"
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
    name: 'Room',
    data () {
        return {
            rooms: {
                roomId: '',
                typeId: '',
                position: '',
                pageIndex: 1,       //当前在第几页
                pageSize: 10        //每页展示多少条数据
            },

            value: '',
            tableData: [],
            multipleSelection: [],
            delList: [],
            switchValue: true,
            editVisible: false,
            addVisible: false,
            pageTotal: 50,      //总共有多少条数据
            form: {},
            idx: -1,
            id: -1
        };
    },
    created () {
        this.getAllRooms();
    },
    methods: {

        //获取所有预定信息
        getAllRooms () {
            this.$http.get('http://localhost/getAllRooms').then((res) => {
                //console.log(res);
                this.tableData = res.data.data.rooms;
            });
        },

        // 编辑
        saveRoomEdit () {
            //console.log(this.form);
            this.$http.post('http://localhost/addRoom?position=' + this.form.position + '&roomId=' + this.form.roomId + '&typeId=' + this.form.typeId).then(res => {
                console.log(res);
                if (res.data.code === 200) {
                    //1.提示成功
                    this.$message.success(`修改成功`);
                    //2.关闭对话框
                    this.editVisible = false;
                    //3.更新视图
                    this.getAllRoomType();
                    //4.清空输入文本框
                    this.form = {};
                } else {
                    this.$message.warning('修改失败');
                }
            });
        },


        // 添加
        saveRoom () {
            // console.log(this.form);
            this.$http.post('http://localhost/addRoom?position=' + this.form.position + '&roomId=' + this.form.roomId + '&typeId=' + this.form.typeId).then(res => {
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
            this.$http.get('http://localhost/getRoomById?id=' + this.rooms.roomId).then(res => {
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
                    this.tableData = res.data.data.rooms;
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
                    this.tableData = res.data.data.rooms;
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
            this.$set(this.rooms, 'pageIndex', val);
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
