<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="Access-Control-Allow-Origin" content="*" />
    <title>爱唱发布测试包</title>
    <!-- <script src="http://www.jq22.com/jquery/1.11.1/jquery.min.js"></script> -->
    <script src="/AccountBook/testing/js/jquery.min.js"></script>
    <script src="/AccountBook/testing/js/app-info-parser.min.js"></script>
    <script src="/AccountBook/testing/js/Uploader.js"></script>
    <script src="/AccountBook/testing/js/SparkMd5.js"></script>

    <style>
        body {
            background-color: #F7F9F7;
            margin: 0;
        }

        .title {
            width: 100%;
            height: 150px;
            color: white;
            font-size: 36px;
            line-height: 150px;
            text-align: center;
            background-color: #708090;
        }


        .content {
            width: 800px;
            margin: 0 auto;
            display: flex;
            padding-top: 50px;
        }

        .content_l {
            width: 60%;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .fileBox {
            width: 100%;
        }

        .fileDragArea {
            width: 100%;
            height: 100px;
            box-sizing: border-box;
            border: 2px dashed #ccc;
            text-align: center;
            line-height: 50px;
            position: relative;
            border-radius: 15px;

        }

        .fileInput {
            width: 100%;
            height: 100%;
            position: absolute;
            left: 0;
            top: 0;
            right: 0;
            bottom: 0;
            opacity: 0;
            cursor: pointer;

        }

        .progressView {
            width: 0%;
            height: 100%;
            background-color: #EAA625;
            position: absolute;
            left: 0;
            top: 0;
            border-radius: 15px;

        }

        .progressTextView {
            width: 100%;
            height: 100%;
            font-size: 12px;
            line-height: 100px;
            position: absolute;
            color: #444;
            left: 0;
            top: 0;
        }

        .develop_type {
            width: 100%;
            height: 50px;
        }

        .comment {
            width: 100%;
            height: 190px;
            display: block;
            box-sizing: border-box;
            resize: none;
        }



        .uploadBtn {
            width: 100%;
            height: 50px;

            background-color: #708090;
            position: relative;
            font-size: 12px;
            color: #fff;
            border-radius: 0px 0px 15px 15px;
            border: none;
            cursor: pointer;
        }




        .content_r {
            margin-left: 10px;
            display: flex;
            flex-direction: column;


        }

        .qrcode {
            width: 410px;
            height: 480px;
            background-color: white;
            border-radius: 15px;

        }





        input {
            margin: 0;
        }

        .item_name {

            width: 100%;
            margin-top: 20px;
            margin-bottom: 5px;
            color: #ccc;
            font-size: 15px;
            font-weight: 500;

        }

        .copyright {
            width: 100%;
            text-align: center;
            font-size: 12px;
            position: absolute;
            bottom: 0;
        }
    </style>

</head>


<body>

    <div class="root">
        <div class="title">
            爱唱-测试包发布平台
        </div>


        <div class="content">

            <div class="content_l">

                <div class="fileBox">
                    <div class="fileDragArea">

                        <span class="progressView"></span>

                        <span class="progressTextView">点击选取文件或者将文件拖到此处</span>

                        <input type="file" multiple class="fileInput" />


                    </div>
                </div>

                <span class="item_name">包类型:</span>
                <select class="develop_type">
                    <option value="测试版">debug</option>
                    <option value="正式版">release</option>
                </select>

                <span class="item_name">更新内容:</span>
                <textarea placeholder="请输入更新内容" class="comment"></textarea>

                <input type="button" value="上传" class="uploadBtn" onfocus="this.blur();" />




            </div>

            <div class="content_r">

                <img class="qrcode"></img>

            </div>

        </div>

        <div class="copyright">Copyright © 2019 www.xzbenben.cn All rights reserved.
            <a href="http://www.beian.miit.gov.cn/" target="ii">京ICP备17061398号-1</a>
        </div>

    </div>





</body>



<script>

    var transparentImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAACXBIWXMAAAsTAAALEwEAmpwYAAAABGdBTUEAALGOfPtRkwAAACBjSFJNAAB6JQAAgIMAAPn/AACA6QAAdTAAAOpgAAA6mAAAF2+SX8VGAAAAP0lEQVR42mL8//8/AzEAIICYGIgEAAFEtEKAACJaIUAAEa0QIICIVggQQEQrBAggohUCBBDRCgECiGiFAAEGANUSAxEZNm5vAAAAAElFTkSuQmCC"

    var uploadBtnNormalColor = "#708090"
    var uploadBtnSuccessColor = "#3CB371"
    var uploadBtnFaildColor = "#FF6A6A"

    //生成二维码base64
    // var qrCodeUrlB64="http://192.168.10.133:8080/AccountBook/testing/qrcode_b64";//测试
    var qrCodeUrlB64 = "/AccountBook/testing/qrcode_b64"//线上

    //生成二维码,二进制流
    // var qrCodeUrl="http://localhost:8080/AccountBook/testing/qrcode";//测试
    var qrCodeUrl = "/AccountBook/testing/qrcode"//线上
</script>

<script>



    $(function () {
        //copyright显示
        window.onresize = function () {
            onSizeChanged()
        }
        onSizeChanged()


        //元素
        var oFileBox = $(".fileBox");					//选择文件父级盒子
        var fileInput = $(".fileInput");				//选择文件按钮

        var uploadBtn = $(".uploadBtn");					//上传按钮

        var progressView = $(".progressView");					//进度
        var progressTextView = $(".progressTextView");					//进度文本




        var developSelect = $(".develop_type");//包类型

        var qrCode = $(".qrcode")[0];




        var selectFile;
        var appInfo;


        function initView() {

            qrCode.src = transparentImage

        }
        initView()
        addUploadEvent()


        developSelect.change(function () {
            //获取当前select选择的值
            var selectVal = $(".develop_type option:selected").val();
        });





        //拖拽外部文件，进入目标元素触发
        fileInput.on("dragenter", function () {
            $(this).text("可以释放鼠标了！");
        });

        //拖拽外部文件，进入目标、离开目标之间，连续触发
        fileInput.on("dragover", function () {
            return false;
        });

        //拖拽外部文件，离开目标元素触发
        fileInput.on("dragleave", function () {
            $(this).text("或者将文件拖到此处");
        });

        //拖拽外部文件，在目标元素上释放鼠标触发
        fileInput.on("drop", function (ev) {
            var fs = ev.originalEvent.dataTransfer.files;
            analysis(fs);		//解析列表函数
            $(this).text("或者将文件拖到此处");
            return false;
        });

        //点击选择文件按钮选文件
        fileInput.on("change", function () {
            analysis(this.files);
        })

        function addUploadEvent() {
            uploadBtn.off();
            uploadBtn.on("click", function () { upload() });
        }

        //解析列表函数
        function analysis(obj) {
            //如果没有文件
            if (obj.length != 1) {
                alert("只允许一次上传一个文件")
                return;
            }



            var fileObj = obj[0];		//单个文件
            var name = fileObj.name;	//文件名
            var size = fileObj.size
            var type = fileType(name)//文件类型


            //文件类型不为这三种，就不上传
            if (("apk/ipa/txt").indexOf(type) == -1) {
                alert('“' + name + '”文件类型不对，不能上传');
                return;
            }



            progressTextView.text(name)
            uploadBtn.css("background-color", uploadBtnNormalColor);
            uploadBtn.val("上传测试包")
            addUploadEvent()
            progressView.css("width", "0%");
            qrCode.src = transparentImage

            selectFile = fileObj
            getAppInfo(fileObj, function (info) {
                if (info == undefined)
                    info = {}
                info.filename = name
                info.filesize = size
                info.filetype = type.toLowerCase()
                appInfo = info
            })



        };


        function upload() {
            if (!selectFile) {
                alert("没有选择文件")
                return
            }
            if (appInfo == undefined) {
                alert("正在读取文件信息,请稍等...")
                return
            }
            appInfo.developType = $(".develop_type option:selected").text();

            
            uploadBtn.off();

            // singleUpload(selectFile)
            sliceUpload(selectFile)
        }

        //上传整个文件
        function singleUpload(selectFile) {
            var uploader = new SingleUploader();
            uploader.upload(selectFile, {
                success: function (data) {
                    makeQrImage(data, appInfo)
                },
                error: function (e) {
                    progressView.css("width", "0%");
                    progressTextView.html("上传失败");
                    uploadBtn.css("background-color", uploadBtnFaildColor);
                    uploadBtn.val("重新上传");
                    addUploadEvent()
                },
                progress: function (p) {
                    p = Math.floor(p * 100)
                    progressView.css("width", p + "%");
                    progressTextView.html(p + "%");

                }
            });

        }

        //分片上传文件
        function sliceUpload(selectFile) {
            uploadBtn.val("正在准备上传文件必要的信息...")
            calculate(selectFile, function (md5) {
                uploadBtn.val("上传中...")
                console.info("md5:", md5); // compute hash
                var uploader = new SliceUploader("fix_count", 20);
                uploader.upload(selectFile, md5, {
                    success: function (data) {
                        makeQrImage(data.file_path, appInfo)

                    },
                    error: function (e) {
                        progressView.css("width", "0%");
                        progressTextView.html("上传失败");
                        uploadBtn.css("background-color", uploadBtnFaildColor);
                        uploadBtn.val("重新上传");
                        addUploadEvent()
                    },
                    progress: function (p) {
                        p = Math.floor(p * 100)
                        progressView.css("width", p + "%");
                        progressTextView.html(p + "%");

                    }
                })

            })

        }


    })



    function getAppInfo(file, callback) {

        try {
            const parser = new AppInfoParser(file)
            parser.parse().then(result => {
                console.log('app info ----> ', result)
                callback(result)
            }).catch(err => {
                console.log('err ----> ', err)
            })

        } catch (error) {
            //其他文件不支持
        }
        callback()

    }


    function makeQrImage(fileUrl, appInfo) {
        $(".uploadBtn").val("二维码制作中...")
        getQrImage(fileUrl, appInfo)
    }


    function getQrImage(fileUrl, appInfo) {


        var param = {};

        param.fileurl = fileUrl
        param.comment = $('.comment').val()
        param.device = appInfo.filetype == 'apk' ? 'android' : appInfo.filetype == 'ipa' ? 'ios' : 'other'
        param.appinfo = JSON.stringify(appInfo)

        $.ajax({
            type: "POST",
            url: qrCodeUrlB64,
            data: param,			//这里上传的数据使用了formData 对象
            contentType: "application/x-www-form-urlencoded",
            dataType: "json",
            success: function (data) {
                $(".qrcode")[0].src = "data:image/png;base64," + data.data
                $(".uploadBtn").css("background-color", uploadBtnSuccessColor);
                $(".uploadBtn").val("生成二维码成功")
            },
            error: function (err) {
                $(".uploadBtn").css("background-color", uploadBtnFaildColor);
                $(".uploadBtn").val("生成二维码失败,点击重新制作")
                $(".uploadBtn").off();
                $(".uploadBtn").on("click", function () { makeQrImage(fileUrl, appInfo) });
            }
        })

    }



    function getQrImageStream(fileUrl) {

        var param = {};

        param.fileurl = fileUrl
        param.comment = $('.comment').val()
        param.device = appInfo.filetype == 'apk' ? 'android' : appInfo == 'ios' ? 'ios' : 'other'

        var oReq = new XMLHttpRequest();

        var url = qrCodeUrl;
        oReq.open("POST", url, true);
        //设置请求头
        oReq.setRequestHeader("content-type", "application/x-www-form-urlencoded")

        oReq.responseType = "arraybuffer";

        oReq.onload = function (oEvent) {

            var arrayBuffer = oReq.response; // Note: not oReq.responseText
            var b64 = transformArrayBufferToBase64(arrayBuffer)

            $(".qrcode")[0].src = b64
            $(".uploadBtn").css("background-color", uploadBtnSuccessColor);
            $(".uploadBtn").val("生成二维码成功")

        };

        oReq.onerror = function (err) {
            $(".uploadBtn").css("background-color", uploadBtnFaildColor);
            $(".uploadBtn").val("生成二维码失败")
        }

        oReq.send("fileurl=" + param.fileurl + "&device=" + param.comment + "&desc=" + param.desc);

    }


    function transformArrayBufferToBase64(buffer) {
        var binary = '';
        var bytes = new Uint8Array(buffer);
        for (var len = bytes.byteLength, i = 0; i < len; i++) {
            binary += String.fromCharCode(bytes[i]);
        }

        return "data:image/png;base64," + window.btoa(binary);
    }


    //字节大小转换，参数为b
    function bytesToSize(bytes) {
        var sizes = ['Bytes', 'KB', 'MB'];
        if (bytes == 0) return 'n/a';
        var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
        return (bytes / Math.pow(1024, i)).toFixed(1) + ' ' + sizes[i];
    };

    //通过文件名，返回文件的后缀名
    function fileType(name) {
        var nameArr = name.split(".");
        return nameArr[nameArr.length - 1].toLowerCase();
    }


    function onSizeChanged() {
        var copyright = $(".copyright")
        var content = $(".content")
        var contenBottom = content[0].offsetTop + content[0].offsetHeight
        var copyrightTop = copyright[0].offsetTop
        // console.log("resize:content.bottom=" + contenBottom + ",copyright.top=" + copyrightTop)

        if (copyrightTop < contenBottom) {
            copyright.css("visibility", "hidden")
        } else
            copyright.css("visibility", "visible")
    }


</script>




</html>