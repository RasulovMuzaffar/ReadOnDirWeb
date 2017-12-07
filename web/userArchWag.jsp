<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!--<meta charset="utf-8">-->
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Справочник АСОУП</title>

        <!-- Bootstrap -->
        <link href="resources/css/bootstrap.css" rel="stylesheet">
        <link href="resources/css/styleSingleArch.css" rel="stylesheet">

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="resources/js/html5shiv.min.js"></script>
        <script src="resources/js/respond.min.js"></script>
    <![endif]-->
        <link rel="stylesheet" href="resources/css/font-awesome.min.css">
    </head>
    <body>
        <div class="container-fluid">
            <div class="row header">
                <div class="col-md-4 hleft">
                    <h2>Справочник АСОУП</h2>
                </div>
                <div id="xxxx" class="col-md-8 hright">
                    <a href="logOff">Выход</a>
                    <br/>
                    <h4><strong>Здравствуйте, ${user.fName} ${user.lName}!</strong></h4>
                </div>
            </div>
            <!--<div class="row">-->
            <div class="well form-inline col-md-12 archMessBar" role="form">
                <div class="form-group">
                    <div class="form-group">
                        <input type="text" class="input-small" placeholder="Номер вагона">
                    </div>
                    <div class="form-group">
                        <input type="text" class="input-small" placeholder="Дата1">
                    </div>
                    <div class="form-group">
                        <input type="text" class="input-small" placeholder="Дата2">
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-info" onclick="findWag();" id="btnFind">Поиск</button>
                    </div>

                    <!--/////////////////////////////-->
                    <!--                    <div id="o-wrapper" class="o-wrapper col-md-2">
                                            <div class="c-buttons">
                                                <button id="c-button--slide-right" class="c-button fa fa-history"> История</button>
                                            </div>
                                        </div>-->
                    <!-- /o-wrapper -->
                    <!--/////////////////////////////-->

                </div>
            </div>
            <!--</div>-->
            <div class="col-md-10 sprTables">
                <div class="row">
                    <p class="progressInfo"></p>
                </div>
                <div class="tbl">
                    <a id="dlink" style="display: none;"></a>
                    <button type="button" class="btn btn-info fa fa-download" id="expToExc" style="display: none;" onclick="expToExcel('myTable');"></button>
                    <div class="row" id="tbl">
                    </div>
                </div>
            </div>
            <div class="col-md-2 sprsHist archHist">
                <p>История</p>
                <ul id="sprHid" class="sprH">
                    <c:forEach items="${hl}" var="h">
                        <li tabIndex="0" class="hist" data-idmess="${h.id}" onclick="getHist(this);">${h.header}</li>
                        </c:forEach>
                </ul>
            </div>

        </div>
        <div class="row">
            <footer class="footer">
                <div class="container-footer">
                    <p class="text-muted text-center">Developed by Muzaffar Rasulov</p>
                </div>
            </footer>
        </div>

        <!--БОКОВОЕ МЕНЮ-->
        <!--        <nav id="c-menu--slide-right" class="c-menu c-menu--slide-right">
                    <button class="c-menu__close">Закрыть историю &rarr;</button>
                    <ul class="c-menu__items" id="history">
                    </ul>
                </nav>-->
        <!-- /c-menu slide-right -->

        <!--<div id="c-mask" class="c-mask"></div>-->
        <!-- /c-mask -->


        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src = "resources/js/jquery-3.2.0.min.js" ></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="resources/js/bootstrap.js"></script>
        <script src="resources/js/expToExcel.js"></script>

        <!-- menus script -->
        <!--<script src="resources/js/dist/menu.js"></script>-->

        <script>
                    var webSocket = new WebSocket("ws://${pageContext.request.localAddr}:8080/armASOUP//ws");
                    webSocket.onopen = function (message) {
                        processOpen(message);
                        console.log(message);
//                                console.log("sessions\u0003${pageContext.session.id}");

                        webSocket.send("sessions\u0003${pageContext.session.id}");

                    };
                    webSocket.onmessage = function (message) {
                        console.log(message.data);
                        var m = message.data.split("\u0003");
                        window[m[0]](m[1]);
                    };
                    function processOpen(message) {
                        console.log(message);
                    }
                    function processMessage(message) {
                        console.log(message.valueOf());
                    }


                    function findWag() {
                        var v = document.getElementsByClassName('input-small')[0].value;
                        var d1 = document.getElementsByClassName('input-small')[1].value;
                        var d2 = document.getElementsByClassName('input-small')[2].value;
                        var m = "(:2790 " + v + " dt-" + d1 + "-" + d2 + ":)";
                        console.log("spr\u0003" + m);
                        webSocket.send("spr\u0003" + m);
                    }

            <%-- получаем натурлий лист поезда  --%>
                    function getTGNL(p) {
                        var x = p.replace("  ", " ");
                        console.log(x);
                        webSocket.send("getTGNL\u0003" + x);
                    }

            <%-- получаем Расширенную справку поезда  --%>
                    function getRS(p) {
                        var x = p.replace("  ", " ");
                        console.log(x);
                        webSocket.send("getRS\u0003" + x);
                    }

                    function getHist(p) {
                        var x = p.dataset.idmess;

                        console.log(x);
                        console.log(p.dataset.idmess);
                        console.log(p.dataset['idmess']);
                        webSocket.send("getHist\u0003" + x);
                    }

                    function checking() {
                        console.log($(".mytable tr").length);
                        console.log($(".mytable tr:last td:first").html());
                        var rowsCount = document.getElementsByClassName("mytable")[0].getElementsByTagName('tr').length;
                        var endRowFCellVal = document.getElementsByClassName("mytable")[0].rows[rowsCount - 1].cells[0].innerHTML;
//                            document.getElementsByClassName("progressInfo")[0].innerHTML = endRowFCellVal;

                        if ((rowsCount - 1) != endRowFCellVal) {
                            document.getElementsByClassName("progressInfo")[0].innerHTML = "Таблица неполная!";
                            console.log("Таблица неполная!");
                        }
                    }


                    function sprDefault(p) {
                        document.getElementById("expToExc").style.display = "block";
                        document.getElementById("tbl").innerHTML = p;
                    }

//    Формируем лист истории
                    function histList(p) {
                        if (document.getElementById('sprHid') !== null) {
                            document.getElementById('sprHid').innerHTML = p;
                        } else {
                            document.getElementById('history').innerHTML = p;
                        }
                    }

                    function warning(p) {
                        console.log(p);
                        document.getElementById('xxxx').insertAdjacentHTML('beforeEnd', '<br/><span id="warning" class="warning">' + p + '</span>');
                        setTimeout('document.getElementById("warning").remove()', 5000);
                    }
                    function logoff(p) {
                        location.href = p;
                    }

                    /**
                     * Slide right instantiation and action.
                     */
                    var slideRight = new Menu({
                        wrapper: '#o-wrapper',
                        type: 'slide-right',
                        menuOpenerClass: '.c-button',
                        maskId: '#c-mask'
                    });

                    var slideRightBtn = document.querySelector('#c-button--slide-right');

                    slideRightBtn.addEventListener('click', function (e) {
                        e.preventDefault;
                        slideRight.open();
                    });

        </script>
    </body>

</html>