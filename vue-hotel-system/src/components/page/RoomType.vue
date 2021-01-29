<template>

    <div>
        <!--  客户列表文字  -->
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-calendar"></i> 房间管理
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
                <el-button plain round type="primary" icon="el-icon-plus" @click="handBook">添加房间</el-button>
            </div>

            <!-- 主列表 -->
            <el-table
                :data="tableData"
                border
                style="width: 100%">

                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="roomId" label="房型编号" align="center"></el-table-column>
                <el-table-column prop="rank" label="房间级别" align="center"></el-table-column>
                <el-table-column prop="size" label="房间大小" align="center"></el-table-column>
                <el-table-column prop="maxNum" label="容纳人数" align="center"></el-table-column>
                <el-table-column prop="rent" label="房间单价" align="center"></el-table-column>
                <el-table-column prop="earnest" label="房间定金" align="center"></el-table-column>
                <el-table-column prop="position" label="房间位置" align="center"></el-table-column>


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
                    </template>
                </el-table-column>

            </el-table>


            <!-- 编辑弹出框 -->
            <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
                <el-form ref="form" :model="form" label-width="70px">
                    <el-form-item label="房间级别">
                        <el-input v-model="form.rank"></el-input>
                    </el-form-item>
                    <el-form-item label="房间大小">
                        <el-input v-model="form.size"></el-input>
                    </el-form-item>
                    <el-form-item label="容纳人数">
                        <el-input v-model="form.maxNum"></el-input>
                    </el-form-item>
                    <el-form-item label="房间单价">
                        <el-input v-model="form.rent"></el-input>
                    </el-form-item>
                    <el-form-item label="房间定金">
                        <el-input v-model="form.earnest"></el-input>
                    </el-form-item>
                    <el-form-item label="房间位置">
                        <el-input v-model="form.position"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
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
                <el-button type="primary" @click="saveRoomType">确 定</el-button>
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
    name: 'RoomType',
    data () {
        return {
            rooms: {
                roomId: '',
                size: '',
                rank: '',
                rent: '',
                earnest: '',
                maxNum: '',
                position: '',
                pageIndex: 1,       //当前在第几页
                pageSize: 10        //每页展示多少条数据
            },

            tableData: [],
            multipleSelection: [],
            delList: [],
            editVisible: false,
            addVisible: false,
            pageTotal: 50,      //总共有多少条数据
            form: {},
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

        getAllRooms () {
            this.$http.get('http://localhost/getAllRooms').then((res) => {
                //console.log(res);
                this.tableData = res.data.data.rooms;
            });
        },

        handleDelete (index, row, roomId) {
            if (localStorage.getItem('ms_username') === 'admin') {
                // 二次确认删除
                this.$confirm('确定要删除吗？', '提示', {
                    type: 'warning'
                })
                    .then(() => {
                        this.$http.delete('  http://localhost//deleteRoom?id=' + roomId).then((res) => {
                            // console.log(res);
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

        //添加房间类型
        saveRoomType () {
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

        //编辑房间级别信息
        saveEdit () {
            //console.log(this.form);
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
    margin-right: 10px;
}
</style>
