<template>

    <div>
        <!--  客户列表文字  -->
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-profile"></i> 前台管理
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
                <el-button plain round type="primary" icon="el-icon-plus" @click="handBook">添加前台</el-button>
            </div>

            <!-- 主列表 -->
            <el-table
                :data="tableData"
                border
                style="width: 100%">

                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="frontId" label="前台工号" align="center"></el-table-column>
                <el-table-column prop="name" label="前台姓名" align="center"></el-table-column>
                <el-table-column prop="password" label="前台密码" align="center"></el-table-column>
                <el-table-column prop="phone" label="前台电话" align="center"></el-table-column>


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
                            @click="handleDelete(scope.$index, scope.row,scope.row.frontId)">删除
                        </el-button>
                    </template>
                </el-table-column>

            </el-table>


            <!-- 编辑弹出框 -->
            <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
                <el-form ref="form" :model="form" label-width="70px">
                    <el-form-item label="前台工号">
                        <el-input v-model="form.frontId" :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item label="前台姓名">
                        <el-input v-model="form.name"></el-input>
                    </el-form-item>
                    <el-form-item label="前台密码">
                        <el-input v-model="form.password"></el-input>
                    </el-form-item>
                    <el-form-item label="联系电话">
                        <el-input v-model="form.phone"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
                </span>
            </el-dialog>

            <!-- 添加弹出框 -->
            <el-dialog title="添加" :visible.sync="addVisible" width="30%">
                <el-form ref="form" :model="form" label-width="70px">
                    <el-form-item label="前台工号">
                        <el-input v-model="form.frontId" ></el-input>
                    </el-form-item>
                    <el-form-item label="前台姓名">
                        <el-input v-model="form.name"></el-input>
                    </el-form-item>
                    <el-form-item label="前台密码">
                        <el-input v-model="form.password"></el-input>
                    </el-form-item>
                    <el-form-item label="联系电话">
                        <el-input v-model="form.phone"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                <el-button @click="addVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveFront">确 定</el-button>
                </span>
            </el-dialog>

            <!--  分页角标设置   -->
            <div class="pagination">
                <el-pagination
                    background
                    layout="total, prev, pager, next"
                    :current-page="fronts.pageIndex"
                    :page-size="fronts.pageSize"
                    :total="pageTotal"
                    @current-change="handlePageChange"
                ></el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
import { fetchData2 } from '../../api/index';


/*
http://localhost/getAllFront
fronts
*/

export default {
    name: 'RoomType',
    data () {
        return {
            fronts: {
                frontId: '',
                name: '',
                password: '',
                phone: '',
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
            id: -1
        };
    },
    created () {
        this.getAllFront();
    },
    methods: {

        //获取所有前台
        getAllFront () {
            this.$http.get('http://localhost/getAllFront').then((res) => {
                //console.log(res);
                this.tableData = res.data.data.fronts;
            });
        },

        //删除预定信息
        handleDelete (index, row, typeId) {
            if (localStorage.getItem('ms_username') === 'admin') {
                // 二次确认删除
                this.$confirm('确定要删除吗？', '提示', {
                    type: 'warning'
                })
                    .then(() => {
                        this.$http.delete('http://localhost//deleteFront?id=' + typeId).then((res) => {
                            console.log(res);
                            if (res.data.code === 200) {
                                this.$message.success('删除成功');
                                this.tableData.splice(index, 1);
                                this.getAllFront();
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

        //添加前台
        saveFront () {
            //console.log(this.form);
            this.$http.post('http://localhost/addFront?frontId=' + this.form.frontId + '&name=' + this.form.name + '&password=' + this.form.password + '&phone=' + this.form.phone).then(res => {
                //console.log(res);
                if (res.data.code === 200) {
                    //1.提示成功
                    this.$message.success(`添加成功`);
                    //2.关闭对话框
                    this.addVisible = false;
                    //3.更新视图
                    this.getAllFront();
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

        //编辑前台
        saveEdit () {
            //console.log(this.form);
            this.$http.post('http://localhost/addFront?frontId=' + this.form.frontId + '&name=' + this.form.name + '&password=' + this.form.password + '&phone=' + this.form.phone).then(res => {
                //console.log(res);
                if (res.data.code === 200) {
                    //1.提示成功
                    this.$message.success(`编辑成功`);
                    //2.关闭对话框
                    this.editVisible = false;
                    //3.更新视图
                    this.getAllFront();
                    //4.清空输入文本框
                    this.form = {};
                } else {
                    this.$message.warning('编辑失败');
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
            this.$set(this.fronts, 'pageIndex', val);
            this.getAllFront();
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
