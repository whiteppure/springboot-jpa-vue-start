/**
 * 前端导出excel，不使用后端接口；效率高,秒下载
 *
 * 你完全可以不用在Vue里使用，哪怕是Jquery、React等其他前端框架，你只需要将封装好的方法引入即可；
 * 该方法参考{@link https://blog.csdn.net/weixin_46683455/article/details/108828806}，有改动
 *
 * "对于一些特殊类型的item，比如服务器返回0/1字段，一般我们需要转换为是/否，自己定义一个函数，用函数转换，不要用三目运算符；
 * 除此之外一些特殊类型的格式转换，参考该链接{@link https://blog.csdn.net/weixin_34167043/article/details/86132311}"
 *
 */
let idTmr;

/**
 * 自己定义一个函数transform，在里面写我们的业务逻辑
 *
 * 注意：表头和表中数据对应关系，要按照顺序对应好
 * 例如：['name','age','gender'] 对应 ['姓名','年龄','性别'] 这样导出的才是正确的
 *
 * @param headerData 表头，例如：['姓名','年龄','性别']
 * @param table 表格数据
 * @param tableColumn 必须为表格数据中的字段属性名称；例如：['name','age','gender']
 * @param name 导出excel文件名称; 传入文件名称不带后缀
 * @param title excel 标题 如果为空，默认为{@param name}
 * @param callback 回调函数；导出完毕回调,方便你知道导出完成了(可根据自己需求决定是否需要)
 */
function transform(headerData, table, tableColumn, name, title, callback) {

    if (tableColumn === [] || headerData === [] || table === {} || name === null) return

    if (title == null) title = name

    let tableInnerHTML = ''

    //这里对应是表格数据，我们只需要传过来即可
    let bodyData = table

    //拼接完全使用thead、tbody、tr、td、th，并且相应的tr、th、td里可以写一些类似colspan(决定占几列)
    //rowspan(决定占几行)的属性、可以用作合并行、合并列等高级操作
    tableInnerHTML += '<thead><tr>';    //头部部分开始拼接！
    tableInnerHTML += `<th colspan=${headerData.length} 
    				style='font-size: 18px; font-weight: 500'>` + title + "</th></tr>"
    tableInnerHTML += '<tr>'
    headerData.forEach(item => {
        tableInnerHTML += "<th rowspan='1' style='border:solid; font-size: 16px;'>"
            + item + "</th>"
    })
    tableInnerHTML += '</tr></thead>';     //头部部分结束
    tableInnerHTML += '<tbody>'           //身体部分开始

    for (const item of bodyData) {
        tableInnerHTML += "<tr>"
        tableColumn.forEach(column => {
            // tableInnerHTML += "<td  align='center' style='border:solid'>" + item[column] + "</td>"
            tableInnerHTML += "<td style= " +
                "'" +
                "mso-number-format:\"\@\"; " +
                "border:solid;" +
                "text-align:center;" +
                "width: 300px;" +
                "height: 30px;"+
                "'"+
                "  class = 'tdRight' >" + item[column] + "</td>"
        })
        tableInnerHTML += "</tr>"
    }
    tableInnerHTML += '</tbody>';    //身体结束
//------------OK，到此为止拼接工作做完，也就是基本的数据已经被拼接成表格了--------------------
//tip开始(下面还有个tip结束的位置)
    /*-------从tip开始到tip结束的过程是判断浏览器类型步骤，做兼容性处理！对于你来说你完全可以不用
             深入理解这里面的逻辑，直接复制到自己的项目里去，不会存在任何浏览器兼容性的问题！*/
    function getExplorer() {
        var explorer = window.navigator.userAgent;
        if (explorer.indexOf('MSIE') >= 0) {
            return 'ie';        // ie
        } else if (explorer.indexOf('Firefox') >= 0) {
            return 'Firefox';   // firefox
        } else if (explorer.indexOf('Chrome') >= 0) {
            return 'Chrome';    // Chrome
        } else if (explorer.indexOf('Opera') >= 0) {
            return 'Opera';     // Opera
        } else if (explorer.indexOf('Safari') >= 0) {
            return 'Safari';    // Safari
        }
    }

    if (getExplorer() !== 'Safari' && name.substr(-1, 4) !== '.xls') {
        name += '.xls';
    }
    if (getExplorer() === 'ie') {
        var curTbl = table;
        var oXL = new ActiveXObject('Excel.Application');
        var oWB = oXL.Workbooks.Add();
        var xlsheet = oWB.Worksheets(1);
        var sel = document.body.createTextRange();
        sel.moveToElementText(curTbl);
        sel.select();
        sel.execCommand('Copy');
        xlsheet.Paste();
        oXL.Visible = true;
        try {
            var fname = oXL.Application.GetSaveAsFilename('Excel.xls', 'Excel Spreadsheets (*.xls), *.xls');
        } catch (e) {
            print('Nested catch caught ' + e);
        } finally {
            oWB.SaveAs(fname);
            // oWB.Close(savechanges = false);
            oXL.Quit();
            oXL = null;
            idTmr = setInterval(Cleanup(), 1);
        }
    } else {
        // 在这调用下面的一个方法，传入拼接完成的表格，文件名，回调函数。该方法是干嘛的请往下看
        tableToExcel(tableInnerHTML, name, callback);
    }
    //tip结束
}   //此括号结束，我们自己封装的transform方法也结束了！90%的逻辑完成了！


/*下面的两个函数对于你来说你也完全不用深入理解里面的逻辑，你只要知道，他是在帮助你做转换，帮助你
  将拼接好的HTML字符串模板真正地转换并且输出到Excel里面去，直接当成固定书写方法，直接拿来用即可*/
function Cleanup() {
    window.clearInterval(idTmr);
}

let tableToExcel = (function () {
    let template = '<html><head><meta charset="UTF-8"></head><body><table>{table}</table></body></html>';
    let format = function (s, c) {
        return s.replace(/{(\w+)}/g, function (m, p) {
            return c[p];
        });
    };
    return function (table, name, callback) {
        let ctx = {worksheet: name || 'Worksheet', table: table};
        let blob = new Blob([format(template, ctx)]);
        let a = document.createElement('a');
        a.href = URL.createObjectURL(blob);
        a.download = name;     //这里这个name就是对应的文件名！
        a.click();
        a.remove();
        callback('success');    /*这里调用我们自己传入的回调方法，这样导出Excel完成后你就能
        						 在外面知道导出完毕，并且再往下做自己其他的逻辑*/
    };
})();

//导出自己封装的transform方法
export default transform;