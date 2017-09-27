function expToExcel() {
    var t = document.getElementsByClassName('mytable').length;
    var template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" \n\
xmlns:x="urn:schemas-microsoft-com:office:excel" \n\
xmlns="http://www.w3.org/TR/REC-html40">\n\
<head><!--[if gte mso 9]><xml>\n\
<x:ExcelWorkbook><x:ExcelWorksheets>\n\
<x:ExcelWorksheet><x:Name>{worksheet1}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet>\n\
</x:ExcelWorksheets></x:ExcelWorkbook></xml>\n\
<![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/>\n\
<style>td{mso-number-format:"\@"; border:1px solid #b3b3ff;}</style></head><body>';

    for (var k = 0; k < t; k++) {
        template += '<label>{label' + k + '}</label><table>{table' + k + '}</table><br/><br/>';
    }
    template += '</body></html>';

    var tables = [];
    for (var i = 0; i < t; i++)
        tables[i] = "table" + i;
    console.log(tables);

    var labels = [];
    for (var i = 0; i < t; i++)
        labels[i] = "label" + i;
    console.log(labels);

    var ctx = {};
    ctx.worksheet1 = 'Отчет';
    for (var k = 0; k < t; k++) {
        console.log(tables[k]);
        ctx[tables[k]] = document.getElementsByClassName('mytable')[k].innerHTML;
        ctx[labels[k]] = document.getElementsByClassName('tTitle')[k].innerHTML;
    }


    console.log(ctx);

    var v = JSON.stringify(ctx);
    console.log('-------------------');
    console.log(v);

    tableToExcel(template);
    function tableToExcel(template) {
        var uri = 'data:application/vnd.ms-excel;base64,';

        var d = JSON.parse(v);

        function format(s, c) {
            return s.replace(/{(\w+)}/g,
                    function (m, p) {
                        return c[p];
                    });
        }

        function base64(s) {
            return window.btoa(unescape(encodeURIComponent(s)));
        }
        ;
        var currDate = new Date();
        var dd = currDate.getDate();
        var MM = currDate.getMonth() + 1;
        var yyyy = currDate.getFullYear();
        var HH = currDate.getHours();
        var mm = currDate.getMinutes();
        var ss = currDate.getSeconds();

        if (dd < 10)
            dd = '0' + dd;
        if (MM < 10)
            MM = '0' + MM;
        if (HH < 10)
            HH = '0' + HH;
        if (mm < 10)
            mm = '0' + mm;
        if (ss < 10)
            ss = '0' + ss;


//        var currD = dd + '.' + MM + '.' + yyyy + '_' + HH + ':' + mm + ':' + ss;
        var currD = dd + MM + yyyy + HH + mm + ss;
        // window.location.href = uri + base64(format(template, d));
            document.getElementById('dlink').href = uri + base64(format(template, d));
            document.getElementById('dlink').download = "report" + currD + ".xls";
            document.getElementById('dlink').click();
        
//        window.location.href = uri + base64(format(template, d));
//        window.location.download = 'qwe.xls';
    }
}

