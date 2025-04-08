module.exports = {
    //开启代理服务器（方式一）
    // devServer:{
    //     proxy : 'http://localhost:8000'      //为后端服务器
    // }
    devServer: {
        proxy: {
            //这里的/heng为前缀，这样的话会直接把/heng带到请求地址上
            '/heng': {
                target: 'http://192.168.10.109:8000',
                //下面对象的key是正在表达式，意思是把/heng开头的替换为空字符串
                pathRewrite: {'^/heng':''}
            }
        }
    }
}