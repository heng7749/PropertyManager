<template>
  <div style="padding:16px;">
      <!-- 面包屑导航 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/' }">发布管理</el-breadcrumb-item>
      <el-breadcrumb-item>配件发布</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 表单 -->
    <el-form ref="form" :model="formInfo" label-width="80px" class="formStyle">
        <el-form-item label="配件名">
            <el-input v-model="formInfo.name" style="width:300px" placeholder="请输入配件名"></el-input>
        </el-form-item>
        <el-form-item label="配件分类">
            <el-select v-model="formInfo.type" placeholder="请选择配件">
                <el-option label="车架" value="fram"></el-option>
                <el-option label="轮胎" value="tyre"></el-option>
                <el-option label="脚踏" value="pedal"></el-option>
                <el-option label="刹车" value="brake"></el-option>
                <el-option label="链条" value="chain"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="价格">
            <el-input v-model="formInfo.value"  suffix-icon="el-icon-wallet" style="width:100px"></el-input>
        </el-form-item>
   
        <el-form-item label="详细描述">
            <el-input type="textarea" v-model="formInfo.desc" :autosize="{minRows: 2, maxRows: 8}" style="width:500px"></el-input>
        </el-form-item>

        <el-form-item label="上传图片">
            <el-upload
                action="https://jsonplaceholder.typicode.com/posts/"
                list-type="picture-card"
                :on-preview="handlePictureCardPreview"
                :on-remove="handleRemove"
                :limit='5'>
                <i class="el-icon-position"></i>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible">
                <img width="100%" :src="dialogImageUrl" alt="">
            </el-dialog>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" @click="onSubmit">发布</el-button>
        </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
    name:'FittingPublish',
    data(){
        return {
            dialogImageUrl: '',
            dialogVisible: false,
            formInfo: {
                name: '',
                type:1,
                value:80,
                desc: ''
            }
        }
    },
    methods:{
        handleRemove(file, fileList) {
            console.log(file, fileList);
        },
        handlePictureCardPreview(file) {
            this.dialogImageUrl = file.url;
            this.dialogVisible = true;
        },
        onSubmit() {
            console.log('submit!');
        }
    }
}
</script>

<style>
    .formStyle {
        margin-top: 30px;
    }
</style>