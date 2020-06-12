//上传文件地址
// var uploadSingleUrl="http://localhost:8080/AccountBook/testing/upload";//测试
var uploadSingleUrl = "https://www.5aichang.cn/ac_android_test/checkup.php";//线上

// var uploadSliceUrl = "http://localhost/sliceuploader/upload_slice.php";//测试
var uploadSliceUrl = "https://www.5aichang.cn/ac_android_test/upload_slice.php";//线上
/**
 * 文件分片上传
 * @param {切割模式} mode
 */
class SliceUploader {

    constructor(mode, size) {
        this.mode = mode
        this.size = size

    }
    useFixCount(file, count) {
        this.COUNT = count;
        this.LENGTH = Math.ceil(file.size / count);
    }
    useFixLength(file, length) {
        this.LENGTH = length;
        this.COUNT = Math.ceil(file.size / length);
    }
    upload(file, md5, callback) {
        var that = this
        switch (this.mode) {
            case "fix_count":
                this.useFixCount(file, this.size);
                break;
            case "fix_length":
                this.useFixLength(file, this.size);
                break;
        }
        var form_data = new FormData();
        //发送文件
        function sendFile(originIndex) {
            var index = originIndex < 0 ? 0 : originIndex;
            var start = index * that.LENGTH;
            var end = (index + 1) * that.LENGTH > file.size ? file.size : (index + 1) * that.LENGTH;
            var lastSliceSize = file.size - (that.COUNT - 1) * that.LENGTH; //最后一个片段的大小

            console.log("sendFile originIndex=" + originIndex);


            if (originIndex >= 0) {
                var file_slice = file.slice(start, end);
                // console.log("blob size=" + file_slice.size);
                // console.log("last blob size=" + lastSliceSize);
                // console.log("slice:start=" + start + ",end=" + end);
                // console.log("file name=" + file.name);
                form_data.append('file', file_slice);
                form_data.append('index', index);
            } else
                form_data.append('index', originIndex);

            form_data.append('total_blob_num', that.COUNT);
            form_data.append('file_name', file.name);
            form_data.append('file_slice_size', that.LENGTH);
            form_data.append('file_last_slice_size', lastSliceSize);
            form_data.append('md5', md5);


            $.ajax({
                type: "POST",
                url: uploadSliceUrl,
                data: form_data,
                processData: false,
                contentType: false,
                //这里我们先拿到jQuery产生的XMLHttpRequest对象，为其增加 progress 事件绑定，然后再返回交给ajax使用
                xhr: function () {
                    var xhr = $.ajaxSettings.xhr();
                    if (onprogress && xhr.upload) {
                        xhr.upload.addEventListener("progress", onprogress, false);
                        return xhr;
                    }
                },
                //上传成功后回调
                success: function (data) {
                    console.log(data);
                    if (data.completed == 0)
                        sendFile(data.indexNext);
                    else {
                        callback.progress(1)
                        callback.success(data);

                    }

                },
                //上传失败后回调
                error: function (e) {
                    callback.error(e);
                }
            });
            //侦查附件上传情况 ,这个方法大概0.05-0.1秒执行一次
            function onprogress(evt) {
                if (originIndex < 0)
                    return

                var loaded = evt.loaded;	//已经上传大小情况  
                var tot = evt.total;		//附件总大小  
                var slicePer = loaded / tot;  //已经上传的百分比

                var totalPer = Math.min(1, (index + slicePer) / that.COUNT)

                console.log("slicePer=" + slicePer + ",totalPer=" + totalPer)
                callback.progress(totalPer)
            }
        }
        sendFile(-1);//小于0时不传文件,只是让服务器检测是否需要断点上传
    };
}

/**
 * 上传整个文件
 */
class SingleUploader {
    constructor() {

    }
    upload(file, callback) {
        var formData = new FormData();

        // 从当前项中获取上传文件，放到 formData对象里面，formData参数以key name的方式
        formData.append("file", file);
        $.ajax({
            type: "POST",
            url: uploadSingleUrl,
            data: formData,			//这里上传的数据使用了formData 对象
            processData: false, 	//必须false才会自动加上正确的Content-Type
            contentType: false,

            //这里我们先拿到jQuery产生的XMLHttpRequest对象，为其增加 progress 事件绑定，然后再返回交给ajax使用
            xhr: function () {
                var xhr = $.ajaxSettings.xhr();
                if (onprogress && xhr.upload) {
                    xhr.upload.addEventListener("progress", onprogress, false);
                    return xhr;
                }
            },

            //上传成功后回调
            success: function (data) {
                callback.success(data);
            },

            //上传失败后回调
            error: function (e) {
                callback.error(e);
            }

        });
        //侦查附件上传情况 ,这个方法大概0.05-0.1秒执行一次
        function onprogress(evt) {
            var loaded = evt.loaded;	//已经上传大小情况  
            var tot = evt.total;		//附件总大小  
            var per = loaded / tot;  //已经上传的百分比

            console.log("onprogress:loaded=" + loaded + ",tot=" + tot + ",per=" + per)
            callback.progress(per)
        }
    }
}
