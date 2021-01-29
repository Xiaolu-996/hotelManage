<template>

    <div>
        <!--  客户列表文字  -->
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-friend"></i> 客户列表
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
                <el-button type="primary" round plain icon="el-icon-plus" @click="handGuest">添加客户</el-button>
                &nbsp; &nbsp;
                <el-input
                    round
                    v-model="search"
                    placeholder="可根据姓名,身份证号,联系方式查询"
                    class="handle-input mr10"
                    clearable
                    prefix-icon="el-icon-search"
                    @clear="handleSearch"
                    @keydown.enter.native="handleSearch"
                >
                </el-input>

            </div>


            <!--   主列表    -->
            <el-table
                :data="tableData"
                border
                class="table"
                header-cell-class-name="table-header"
                @selection-change="handleSelectionChange"
                style="width: 100%">

                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="name" label="用户名" align="center"></el-table-column>
                <el-table-column prop="roomId" label="房间号" align="center"></el-table-column>
                <el-table-column prop="idCard" label="身份证号" align="center"></el-table-column>
                <el-table-column prop="contact" label="联系方式" align="center"></el-table-column>
                <el-table-column label="状态" align="center" width="200">
                    <template slot-scope="scope">
                        <!--已入住1 绿色,未处理0 红色,已退房-1 蓝色-->
                        <el-tag
                            v-if="scope.row.state===1"
                            :type="scope.row.state=== 1?'success':(scope.row.state===0?'danger':'')"
                        >已入住
                        </el-tag>
                        <el-tag
                            v-if="scope.row.state===-1"
                            :type="scope.row.state=== 11?'success':(scope.row.state===0?'danger':'')"
                        >已退房
                        </el-tag>
                        <el-tag
                            v-if="scope.row.state===0"
                            :type="scope.row.state=== 11?'success':(scope.row.state===0?'danger':'')"
                        >未处理
                        </el-tag>
                    </template>
                </el-table-column>

                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            icon="el-icon-edit"
                            @click="handleEdit(scope.$index, scope.row,scope.row.name, scope.row.idCard,scope.row.contact)"
                        >编辑
                        </el-button>

                        <el-button
                            plain
                            size="mini"
                            type="danger"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.$index, scope.row,scope.row.idCard)"
                        >删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!--  分页角标设置   -->
            <div class="pagination">
                <el-pagination
                    background
                    layout="total, prev, pager, next"
                    :current-page="guestMsgs.pageIndex"
                    :page-size="guestMsgs.pageSize"
                    :total="pageTotal"
                    @current-change="handlePageChange"
                ></el-pagination>
            </div>
        </div>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
            <el-form ref="form" :model="form" label-width="70px">
                <el-form-item label="身份证号">
                    <el-input v-model="form.idCard" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="用户名">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="联系电话">
                    <el-input v-model="form.contact"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 添加弹出框 -->
        <el-dialog title="添加用户" :visible.sync="addVisible" width="30%">
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
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveGuest">确 定</el-button>
            </span>
        </el-dialog>

    </div>


</template>

<script>
import { fetchData } from '../../api/index';

export default {
    name: 'BaseTable',
    data () {
        return {
            guestMsgs: {
                idCard: '',
                name: '',
                contact: '',
                state: '',
                roomId: '',
                pageIndex: 1,       //当前在第几页
                pageSize: 10        //每页展示多少条数据
            },

            search: '',
            tableData: [],
            multipleSelection: [],
            delList: [],
            pageTotal: 50,      //总共有多少条数据
            idx: -1,
            id: -1,

            editVisible: false,         //编辑框显示控制
            addVisible: false,          //添加框显示控制
            form: {
                idCard: '',
                name: '',
                contact: ''
            }
        };
    },
    created () {
        this.getAllGuest();
        //this.getData();
    },
    methods: {
        // 获取模拟数据
        /*   getData () {
               fetchData(this.guestMsgs).then(res => {
                   this.tableData = res.data.guestMsgs;
                   console.log(res.data.guestMsgs);
                   this.pageTotal = res.pageTotal || 50;
               });
           },*/
        // 搜索功能
        handleSearch () {
            console.log(this.search);
            this.$http.get('http://localhost//getGuestByContact?contact=' + this.search).then(res => {
                if (res.data.code === 200) {
                    this.tableData = res.data.data.guestMsgs;
                } else {
                    this.$http.get('http://localhost//getGuestByIdCard?idCard=' + this.search).then(res => {
                        if (res.data.code === 200) {
                            this.tableData = res.data.data.guestMsgs;
                        } else {
                            this.$http.get('http://localhost//getGuestByName?name=' + this.search).then(res => {
                                if (res.data.code === 200) {
                                    this.tableData = res.data.data.guestMsgs;
                                } else {
                                    this.$message.error('抱歉没有该数据');
                                }
                            });
                        }
                    });
                }
            });
        },


        //获取全部用户操作
        getAllGuest () {
            //console.log('token: ' + localStorage.getItem('token'));
            this.$http.get('http://localhost/getAllGuest').then((res) => {
                this.tableData = res.data.data.guestMsgs;
                // console.log(res.data.data.guestMsgs);
                // console.log('getAllGuest方法执行完毕');
            });
        },

        //添加用户框
        handGuest () {
            this.addVisible = true;
            this.form = {};
        },

        //添加用户
        saveGuest () {
            //console.log(this.form);
            this.$http.post('http://localhost/addGuest?contact=' + this.form.contact + '&idCard=' + this.form.idCard + '&name=' + this.form.name).then(res => {
                //console.log(res);
                if (res.data.code === 200) {
                    //1.提示成功
                    this.$message.success(`添加成功`);
                    //2.关闭对话框
                    this.addVisible = false;
                    //3.更新视图
                    this.getAllGuest();
                    //4.清空输入文本框
                    this.form = {};
                } else {
                    this.$message.warning('添加失败');
                }
            });
        },

        // 编辑用户框
        handleEdit (index, row) {
            this.idx = index;
            this.form = row;
            this.editVisible = true;
        },

        // 保存编辑
        saveEdit () {
            //console.log(this.form);
            this.$http.post('http://localhost/addGuest?contact=' + this.form.contact + '&idCard=' + this.form.idCard + '&name=' + this.form.name).then(res => {
                //console.log(res);
                if (res.data.code === 200) {
                    //1.提示成功
                    this.$message.success(`修改成功`);
                    //2.关闭对话框
                    this.editVisible = false;
                    //3.更新视图
                    this.getAllGuest();
                    //4.清空输入文本框
                    this.form = {};
                } else {
                    this.$message.warning('修改失败');
                }
            });
        },


        // 删除用户
        handleDelete (index, row, userid) {
            // 二次确认删除
            this.$confirm('确定要删除吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    //console.log(userid);
                    this.$http.delete('http://localhost//deleteGuest?idCard=' + userid).then((res) => {
                        if (res.data.code === 200) {
                            this.$message.success('删除成功');
                            this.tableData.splice(index, 1);
                            this.getAllGuest();
                        } else {
                            this.$message.warning('删除失败');
                        }
                    });
                })
                .catch(() => {
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
            this.$message.error(`删除了 ${str}`);
            this.multipleSelection = [];
        },

        // 分页导航
        handlePageChange (val) {
            this.$set(this.guestMsgs, 'pageIndex', val);
            this.getData();
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

.table-td-thumb {
    display: block;
    margin: auto;
    width: 40px;
    height: 40px;
}
</style>
