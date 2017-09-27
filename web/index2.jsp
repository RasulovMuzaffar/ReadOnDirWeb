
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
        <link href="resources/css/style2.css" rel="stylesheet">

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
            <div class="row">
                <div class="col-md-2 sprsMenu">
                    <p>Запросы</p>
                    <ul class="sprM">
                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf0">Свободный запрос</li>

                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf1">Груженные Вагоны</li>
                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf2">Порожные Вагоны</li>
                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf3">Поезда назначением и находящиеся на
                            ст.
                        </li>
                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf4">Передача ваг. по стыку Истыклол</li>
                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf5">Передача ваг. по стыку Кудукли</li>
                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf6">Принятые и сданные поезда по
                            Учкурган
                        </li>
                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf7">Принятые и сданые поезда по
                            Ходжадавлет
                        </li>
                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf8">Чужие ваг. на ст. по времени нахож-ия
                            на дор.
                        </li>
                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf9">Подход и работа с поездами по ст.
                            Сарыагач
                        </li>
                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf10">Подход к ст. Асака</li>
                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf11">Подход наличие поездов по ст.Арысь
                        </li>
                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf12">Подход поездов к стыку Бекабад</li>
                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf13">Пономерное наличие чужих вг.</li>
                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf14">Работа и налич поездов по ст.
                            Джала-Абад
                        </li>
                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf15">Работа ст. с поездами</li>

                        <li tabIndex="0" class="spr" onclick="sendPopup(this)" value="pf25">Расширенная справка о вагонах в
                            поезде
                        </li>
                    </ul>
                </div>


                <div class="col-md-8 sprTables">
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
                <div class="col-md-2 sprsHist">
                    <p>История</p>
                    <ul id="sprHid" class="sprH">
                        <c:forEach items="${hl}" var="h">
                            <li tabIndex="0" class="hist" data-idmess="${h.id}" onclick="getHist(this);">${h.header}</li>
                            </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
        <div class="row">
            <footer class="footer">
                <div class="container-footer">
                    <p class="text-muted text-center">Developed by Muzaffar Rasulov</p>
                </div>
            </footer>
        </div>


        <!-- Наше модальное всплывающее окно -->
        <div id="popupWin" class="modalwinCom">
            <div id="popup" class="row pt"></div>
        </div>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="resources/js/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="resources/js/bootstrap.js"></script>
        <script src="resources/js/expToExcel.js"></script>


        <!--<script src="resources/js/modalwin.js"></script>-->

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

//                                function writing(p) {
//                                    if (document.getElementById('inptFree') != null) {
//                                        webSocket.send("freeSpr\u0003" + document.getElementById('inptFree').value);
//                                    } else {
//                                        webSocket.send("spr\u0003" + p);
//                                    }
//                                }


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
                                function sendPopup(p) {
                                    document.getElementById('popupWin').style.width = '600px';
                                    document.getElementById('popupWin').style.height = '150px';
                                    document.getElementById('popupWin').style.top = '25%';
                                    showModalWin();
                                    debugger;
                                    document.getElementById("popup").innerHTML = p;
                                }

                                function sprPopup(p) {

                                    document.getElementById('popupWin').style.width = '90%';
                                    document.getElementById('popupWin').style.height = '90%';
                                    document.getElementById('popupWin').style.top = '5%';
//                                document.getElementById('popupWin').style.overflow-y='auto';

                                    showModalWin();
                                    debugger;
                                    document.getElementById("popup").innerHTML = p;
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
                                //////////////////////////////////////////////////////

                                var darkLayer;
                                var isPopupOpen;
                                var sprli = document.getElementsByClassName('spr').item(0);
                                sprli.focus();

                                /////////////////////////////////
                                var listSprs = {
                                    'pf0': '<input type="text" id="inptFree" class="input-lg"/>' +
                                            '<span id="fullMess" hidden><span id="inptfree"></span></span>',
                                    'pf1': '<h3>(:212 0 <input type="text" id="inptSt" placeholder="Код ст."/> 29 0:5065 ' +
                                            '<input type="text" id="inptTm" placeholder="Тип вг."/> гр:)</h3>' +
                                            '<span id="fullMess" hidden>(:212 0 <span id="inptst"></span> 29 0:5065 <span id="inpttm"></span> гр:)</span>',
                                    'pf2': '<h3>(:212 0 <input type="text" id="inptSt" placeholder="Код ст."/> 29 0:5065 <input type="text" id="inptTm" placeholder="Тип вг."/> пор:)</h3>' +
                                            '<span id="fullMess" hidden>(:212 0 <span id="inptst"></span> 29 0:5065 <span id="inpttm"></span> пор:)</span>',
                                    'pf3': '<h3>(:212 0 <input type="text" id="inptSt" placeholder="Код ст."/>:91:92:93:)</h3>' +
                                            '<span id="fullMess" hidden>(:212 0 <span id="inptst"></span>:91:92:93:)</span>',
                                    'pf4': '<h3>(:212 0 7473:3290 18 <input type="text" id="inptTm" placeholder="Время."/>:)</h3>' +
                                            '<span id="fullMess" hidden>(:212 0 7473:3290 18 <span id="inpttm"></span>:91:92:93:)</span>',
                                    'pf5': '<h3>(:212 0 73612:3290 18 <input type="text" id="inptTm" placeholder="Время."/>:)</h3>' +
                                            '<span id="fullMess" hidden>(:212 0 73612:3290 18 <span id="inpttm"></span>:)</span>',
                                    'pf6': '<h3>(:212 0 7414:1296 1 18 <input type="text" id="inptTm" placeholder="Время."/>:)</h3>' +
                                            '<span id="fullMess" hidden>(:212 0 7414:1296 1 18 <span id="inpttm"></span>:)</span>',
                                    'pf7': '<h3>(:212 0 73071:1296 1 18 <input type="text" id="inptTm" placeholder="Время."/>:)</h3>' +
                                            '<span id="fullMess" hidden>(:212 0 73071:1296 1 18 <span id="inpttm"></span>:)</span>',
                                    'pf8': '<h3>(:212 0 <input type="text" id="inptSt" placeholder="Код ст."/>:5072 01:)</h3>' +
                                            '<span id="fullMess" hidden>(:212 0<span id="inptst"></span>:5072 01:)</span>',
                                    'pf9': '<h3>(:3121 68 6983:57:64:)</h3>' +
                                            '<span id="fullMess" hidden>(:3121 68 6983:57:64:)</span>',
                                    'pf10': '<h3>(:212 0 7434:7401:)</h3>' +
                                            '<span id="fullMess" hidden>(:212 0 7434:7401:)</span>',
                                    'pf11': '<h3>(:3121 68 6980:91:92:93:)</h3>' +
                                            '<span id="fullMess" hidden>(:3121 68 6980:91:92:93:)</span>',
                                    'pf12': '<h3>(:212 0 7261:57:)</h3>' +
                                            '<span id="fullMess" hidden>(:212 0 7261:57:)</span>',
                                    'pf13': '<h3>(:212 0 <input type="text" id="inptSt" placeholder="Код ст."/>:5065:)</h3>' +
                                            '<span id="fullMess" hidden>(:212 0 <span id="inptst"></span>:5065:)</span>',
                                    'pf14': '<h3>(:3121 70 7183:64:91:)</h3>' +
                                            '<span id="fullMess" hidden>(:3121 70 7183:64:91:)</span>',
                                    'pf15': '<h3>(:212 0 <input type="text" id="inptSt" placeholder="Код ст."/>:64:)</h3>' +
                                            '<span id="fullMess" hidden>(:212 0 <span id="inptst"></span>:64:)</span>',
                                    'pf25': '<h3>(:213 0: <input type="text" id="inptIdx" placeholder="Индекс поезда"/> 12 42 60 902 104:)</h3>' +
                                            '<span id="fullMess" hidden>(:213 0 <span id="inptidx"></span> 12 42 60 902 104:)</span>'


                                };


                                /////////////////////////////////

                                var arrE = [];

                                document.onkeydown = function (evt) {
                                    evt = evt || window.event;
                                    if (isPopupOpen !== true) {
                                        switch (evt.keyCode) {
                                            case 38:
                                                document.activeElement.previousElementSibling.focus();
                                                break;
                                            case 40:
                                                document.activeElement.nextElementSibling.focus();
                                                break;
                                            case 13: //enter
                                                console.log(document.activeElement.getAttribute('class'));
                                                if (document.activeElement.getAttribute('class') === "hist") {
                                                    getHist(document.activeElement);
                                                } else {
//                                                    console.log(evt.target.innerHTML);
                                                    document.getElementById('popupWin').style.width = '600px';
                                                    document.getElementById('popupWin').style.height = '150px';
                                                    document.getElementById('popupWin').style.top = '25%';
                                                    sendPopup(evt.target);
                                                }
                                                break;
                                            case 39: //right
                                                document.getElementsByClassName('hist').item(0).focus();
                                                break;
                                            case 37: //left
                                                document.getElementsByClassName('spr').item(0).focus();
                                                break;
                                        }
                                    } else {
                                        switch (evt.keyCode) {
                                            case 13: //enter
                                            case 9: //tab

                                                var elms = document.getElementById('popup').getElementsByTagName('input');
                                                var c = elms.length;
                                                if (c !== 0) {
                                                    if (document.activeElement.getAttribute('id').toLowerCase() === "inptfree") {
                                                        document.getElementById('inptfree').innerText = document.activeElement.value;

//                                                        console.log(document.getElementById('fullMess').innerText);
                                                        webSocket.send("freeSpr\u0003" + document.activeElement.value);
                                                        document.getElementById('shadow').parentNode.removeChild(darkLayer);
                                                        document.getElementById('popupWin').style.display = 'none';
                                                        isPopupOpen = false;
                                                        return false;
                                                    } else {
                                                        if (isFinite(parseInt(document.activeElement.value))) {
                                                            document.activeElement.style.border = 'none';

                                                            if (document.activeElement.value === elms[c - 1].value) {
                                                                arrE.push({
                                                                    key: document.activeElement.getAttribute('id'),
                                                                    value: document.activeElement.value
                                                                });

                                                                if (document.activeElement.getAttribute('id').toLowerCase() === "inptst") {
                                                                    document.getElementById('inptst').innerText = document.activeElement.value;
                                                                } else if (document.activeElement.getAttribute('id').toLowerCase() === "inpttm") {
                                                                    document.getElementById('inpttm').innerText = document.activeElement.value;
                                                                } else if (document.activeElement.getAttribute('id').toLowerCase() === "inptidx") {
                                                                    document.getElementById('inptidx').innerText = document.activeElement.value;
                                                                } else if (document.activeElement.getAttribute('id').toLowerCase() === "inptvg") {
                                                                    document.getElementById('inptvg').innerText = document.activeElement.value;
                                                                }
//                                                                debugger;
//                                                                writing(document.getElementById('fullMess').innerText);
                                                                webSocket.send("spr\u0003" + document.getElementById('fullMess').innerText);
//                                                            webSocket.send("getSt\u0003" + document.getElementById('fullMess').innerText);
                                                                console.log("getSt\u0003" + document.getElementById('fullMess').innerText);

                                                                document.getElementById('shadow').parentNode.removeChild(darkLayer);
                                                                document.getElementById('popupWin').style.display = 'none';
                                                                isPopupOpen = false;
                                                                return false;
                                                            } else {
//                            console.log(doc)
                                                                arrE.push({
                                                                    key: document.activeElement.getAttribute('id'),
                                                                    value: document.activeElement.value
                                                                });

                                                                if (document.activeElement.getAttribute('id').toLowerCase() === "inptst") {
                                                                    document.getElementById('inptst').innerText = document.activeElement.value;
                                                                } else if (document.activeElement.getAttribute('id').toLowerCase() === "inpttm") {
                                                                    document.getElementById('inpttm').innerText = document.activeElement.value;
                                                                } else if (document.activeElement.getAttribute('id').toLowerCase() === "inptidx") {
                                                                    document.getElementById('inptidx').innerText = document.activeElement.value;
                                                                } else if (document.activeElement.getAttribute('id').toLowerCase() === "inptvg") {
                                                                    document.getElementById('inptvg').innerText = document.activeElement.value;
                                                                }
                                                                document.activeElement.nextElementSibling.focus();
                                                            }
                                                        } else {
                                                            document.activeElement.style.border = '1px solid red';
                                                        }
                                                    }
                                                } else {
//                                                    debugger;
                                                    webSocket.send("spr\u0003" + document.getElementById('fullMess').innerText);
                                                    console.log(document.getElementById('fullMess').innerText);

                                                    document.getElementById('shadow').parentNode.removeChild(darkLayer);
                                                    document.getElementById('popupWin').style.display = 'none';
                                                    isPopupOpen = false;
                                                    return false;

                                                }
                                                break;
                                        }
                                    }
                                };

                                function sendPopup(p) {
                                    isPopupOpen = true;
                                    
                                    document.getElementById('popupWin').style.width = '600px';
                                    document.getElementById('popupWin').style.height = '150px';
                                    document.getElementById('popupWin').style.top = '25%';
                                    
                                    var s = p.getAttribute('value');
                                    console.log(p.getAttribute('value'));
                                    var m = "";
                                    m += '<h3>' + p.innerHTML + '</h3><br/>';
                                    switch (s) {
                                        case "pf0":
                                            m += listSprs.pf0;
                                            break;
                                        case "pf1":
                                            m += listSprs.pf1;
                                            break;
                                        case "pf2":
                                            m += listSprs.pf2;
                                            break;
                                        case "pf3":
                                            m += listSprs.pf3;
                                            break;
                                        case "pf4":
                                            m += listSprs.pf4;
                                            break;
                                        case "pf5":
                                            m += listSprs.pf5;
                                            break;
                                        case "pf6":
                                            m += listSprs.pf6;
                                            break;
                                        case "pf7":
                                            m += listSprs.pf7;
                                            break;
                                        case "pf8":
                                            m += listSprs.pf8;
                                            break;
                                        case "pf9":
                                            m += listSprs.pf9;
                                            break;
                                        case "pf10":
                                            m += listSprs.pf10;
                                            break;
                                        case "pf11":
                                            m += listSprs.pf11;
                                            break;
                                        case "pf12":
                                            m += listSprs.pf12;
                                            break;
                                        case "pf13":
                                            m += listSprs.pf13;
                                            break;
                                        case "pf14":
                                            m += listSprs.pf14;
                                            break;
                                        case "pf15":
                                            m += listSprs.pf15;
                                            break;
                                        case "pf25":
                                            m += listSprs.pf25;
                                            break;
                                    }
                                    showModalWin();
                                    document.getElementById("popup").innerHTML = m;
//        document.getElementById('inptSt').focus();
                                    if (document.getElementById('popup').getElementsByTagName('input').length !== 0) {
                                        document.getElementById('popup').getElementsByTagName('input')[0].focus();
                                    }

                                }


                                function showModalWin() {

                                    darkLayer = document.createElement('div'); // слой затемнения
                                    darkLayer.id = 'shadow'; // id чтобы подхватить стиль
                                    document.body.appendChild(darkLayer); // включаем затемнение

                                    var modalWin = document.getElementById('popupWin'); // находим наше "окно"
                                    modalWin.style.display = 'block'; // "включаем" его

                                    darkLayer.onclick = function () {  // при клике на слой затемнения все исчезнет
                                        darkLayer.parentNode.removeChild(darkLayer); // удаляем затемнение
                                        modalWin.style.display = 'none'; // делаем окно невидимым
                                        isPopupOpen = false;
                                        return false;
                                    };

                                    document.body.onkeydown = function (e) {
                                        if (e.keyCode === 27) {
                                            darkLayer.parentNode.removeChild(darkLayer); // удаляем затемнение
                                            modalWin.style.display = 'none'; // делаем окно невидимым
                                            isPopupOpen = false;
                                            return false;
                                        }
                                    };
                                }

        </script>
    </body>
</html>