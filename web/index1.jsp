
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
        <link href="resources/css/style.css" rel="stylesheet">

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
                <div class="form-inline col-md-10" role="form">
                    <div class="form-group">
                        <label class="sr-only" for="numMess">Mess</label>
                        <select class="form-control" id="numMess" onchange="loadSpr(this);">
                            <option value="0">Выберите объект</option>
                            <option value="1">Станции и стыки</option>
                            <option value="2">Поезда</option>
                            <option value="3">Вагоны</option>
                        </select>
                        <label class="sr-only" for="numSpr">Spr</label>
                        <select class="form-control" id="numSpr" disabled="true" onchange="getForm(this);">
                            <option>-- Справочники --</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <div id="inptSt"></div>
                    </div>
                    <div class="form-group">
                        <div id="inptTime"></div>
                    </div>
                    <div class="form-group">
                        <div id="inptVg"></div>                    
                    </div>
                    <div class="form-group">
                        <div id="inptIdx"></div>
                    </div>
                    <div class="form-group">                  
                        <div>
                            <!--<input type="hidden" name="id_user" value="1" id="id_user"/>-->
                            <input type="hidden" name="id_user" value="${user.id}" id="id_user"/> 
                        </div>
                    </div>

                    <button type="button" class="btn btn-info" onclick="writing();" id="btnOK">OK</button>
                </div>
                <div id="o-wrapper" class="o-wrapper col-md-2">

                    <div class="c-buttons">
                        <button id="c-button--slide-right" class="c-button fa fa-history"> История</button>
                    </div>
                </div><!-- /o-wrapper -->
            </div>
            <div class="row">
                <p class="progressInfo"></p>
            </div>
            <div>
                <button type="button" class="btn btn-info fa fa-download" id="expToExc" style="display: none;"></button>
                <div class="row" id="tbl">
                </div>
            </div>

        </div>
        <!--БОКОВОЕ МЕНЮ-->
        <nav id="c-menu--slide-right" class="c-menu c-menu--slide-right">
            <button class="c-menu__close">Закрыть историю &rarr;</button>
            <ul class="c-menu__items">
                <li class="c-menu__item"><a href="#" class="c-menu__link">20.09 15-30 | ст. Чукурсай | Спр: 91 92 93</a></li>
                <c:forEach begin="1" end="20" step="1" var="i">
                    <li class="c-menu__item"><a href="#" class="c-menu__link">История ${i}</a></li>
                </c:forEach>

            </ul>
        </nav><!-- /c-menu slide-right -->

        <div id="c-mask" class="c-mask"></div><!-- /c-mask -->
        <!-- Наше модальное всплывающее окно -->
        <div id="popupWin" class="modalwin">
            <div id="popup" class="row pt">
            </div>
        </div>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="resources/js/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="resources/js/bootstrap.js"></script>
        <script src="resources/js/dist/menu.js"></script>
        <script src="resources/js/modalwin.js"></script>


        <script type="text/javascript">
                        var webSocket = new WebSocket("ws://${pageContext.request.localAddr}:8080/MessageToASOUP//ws");
                        webSocket.onopen = function (message) {
                            processOpen(message);
                            console.log(message);
                            console.log("sessions\u0003${pageContext.session.id}");

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
                        var rg = /^([a-z_0-9.]{1,})\|([\s\S]*)/;
                        function writing() {
//                        var kodOrg = 0; //document.getElementById('q').value;
//                        var numMess = document.getElementById('numMess').value;
//                        var numSpr = document.getElementById('numSpr').value;
//                        var id_user = document.getElementById('id_user').value;
//                        var object;
//                        if (isFinite(document.getElementById('st').value)) {
//                            object = document.getElementById('st').value;
//                        } else {
//                            object = document.getElementById('st').dataset['stcod'];
//                        }
////                            var el = document.querySelector('#stations tbody');
////                            var object = el.dataset.value;
//                        console.log("spr\u0003" + kodOrg + "," + numMess + "," + numSpr + "," + object + "," + id_user);
//                        
                            console.log("spr\u0003" + getMess());
                            webSocket.send("spr\u0003" + getMess());
//                        webSocket.send("spr\u0003" + kodOrg + "," + numMess + "," + numSpr + "," + object + "," + id_user);
                            if (document.getElementById('stations')) {
                                document.getElementById('stations').style.display = 'none';
                            }
                        }

                        function getStations() {
                            document.querySelector('#stations').addEventListener('click', function (event) {
                                console.log(event.target.closest('tr').dataset['stcod']);
                                document.querySelector('#st').value = event.target.closest('tr').querySelector('td').innerText;
                                document.querySelector('#st').dataset['stcod'] = event.target.closest('tr').dataset['stcod'];
                                document.getElementById('stations').style.display = 'none';
                            });

                            document.getElementById('st').addEventListener('focus', function (event) {
                                this.value = "";
                            });
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

                        function findSt(p) {
                            console.log(p);
                            webSocket.send("getSt\u0003" + p);
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
                        function sprPopup(p) {
                            showModalWin();
                            document.getElementById("popup").innerHTML = p;
                        }
                        function sprDefault(p) {
                            document.getElementById("expToExc").style.display = "block";
                            document.getElementById("tbl").innerHTML = p;
                        }
                        function getSt(p) {
                            document.getElementById('stations').style.display = 'table';
                            document.getElementById('stations').innerHTML = p;
                        }
                        function warning(p) {
                            console.log(p);
                            document.getElementById('xxxx').insertAdjacentHTML('beforeEnd', '<br/><span id="warning" class="warning">' + p + '</span>');
                            setTimeout('document.getElementById("warning").remove()', 5000);
                        }
                        function logoff(p) {
                            location.href = p;
                        }




                        //////////////////////////////////////////////


                        function getDataAbstaraction(val, callback) {
                            var data = {
                                0: {},
                                1: {
                                    'pf1': 'ПФ1 - 5065 груженные Вагоны',
                                    'pf2': 'ПФ2 - 5065 порожные Вагоны',
                                    'pf3': 'ПФ3 - Наличие поездов сформированных назначением и находящиеся на ст.',
//                                    'pf4': 'ПФ4 - Отчет ДО-1 по ст.Бекабад с располож. По РПС и сост.ваг-ов',
//                                    'pf5': 'ПФ5 - Отчет ДО-1 по стыку Джизак с расположением по РПС и состоянию вг.',
                                    'pf6': 'ПФ4 - Передача ваг. с разл.по стыку Истыклол',
                                    'pf7': 'ПФ5 - Передача вагонов с расположением по СНГ по стыку Кудукли',
                                    'pf8': 'ПФ6 - Перечень принятых и сданных поездов по стыкам Учкурган',
                                    'pf9': 'ПФ7 - Перечень принятых и сданых поездов по стыку Ходжадавлет',
                                    'pf10': 'ПФ8 - Перечень чужих вагонов на ст. по времени нахож-ия на дор.',
                                    'pf11': 'ПФ9 - Подход и работа с поездами по ст. Сарыагач',
                                    'pf12': 'ПФ10 - Подход к ст. Асака',
                                    'pf13': 'ПФ11 - Подход наличие поездов по ст.Арысь',
                                    'pf14': 'ПФ12 - Подход поездов к стыку Бекабад',
                                    'pf15': 'ПФ13 - Пономерное наличие чужих вг.',
                                    'pf16': 'ПФ14 - Работа и налич поездов по ст. Джала-Абад',
                                    'pf17': 'ПФ15 - Работа ст. с поездами'
                                },
                                2: {
                                    'pf21': 'ПФ21 - Арендованные вагоны по индексу поезда',
                                    'pf22': 'ПФ22 - Порожние вагоны по индексу поезда',
                                    'pf23': 'ПФ23 - Расширенная справка о ваг. В поезде по Таджикской жд',
                                    'pf24': 'ПФ24 - Расширенная справка о ваг. В поезде по Туркменской жд',
                                    'pf25': 'ПФ25 - Расширенная справка о вагонах в поезде'
                                },
                                3: {
                                    'pf31': 'ПФ31 - 215 по номеру вагона',
                                    'pf32': 'ПФ32 - Арендованные вагоны по номеру вагона',
                                    'pf33': 'ПФ33 - Карточные данные о вагоне',
                                    'pf34': 'ПФ34 - Поиск вагонов'
                                }
                            };
                            callback(data[val]);
                        }

                        function loadSpr(obj) {

                            var val = obj.options[obj.selectedIndex].value;

                            if (!val) {
                                return;
                            }

                            if (val != 0) {
                                document.querySelector('#numSpr').disabled = false;
                                document.getElementById('inptSt').innerHTML = "";
                                document.getElementById('inptTime').innerHTML = "";
                                document.getElementById('inptVg').innerHTML = "";
                                document.getElementById('inptIdx').innerHTML = "";
                            } else {
                                document.querySelector('#numSpr').disabled = true;
                                document.getElementById('inptSt').innerHTML = "";
                                document.getElementById('inptTime').innerHTML = "";
                                document.getElementById('inptVg').innerHTML = "";
                                document.getElementById('inptIdx').innerHTML = "";
                            }

                            getDataAbstaraction(val, function (data) {
                                var opts = '<option value="" hidden disabled selected>-- Справочники --</option>';
                                for (var i in data) {
                                    opts += '<option value="' + i + '">' + data[i] + '</option>';
                                }
                                document.getElementById('numSpr').innerHTML = opts;
                            });
                        }

                        function getForm(obj) {
                            var val = obj.options[obj.selectedIndex].value;
                            document.getElementById('inptSt').innerHTML = "";
                            document.getElementById('inptTime').innerHTML = "";
                            document.getElementById('inptVg').innerHTML = "";
                            document.getElementById('inptIdx').innerHTML = "";
                            console.log(val);
                            if (val === 'pf3' || val === 'pf10' || val === 'pf15' || val === 'pf17') {
                                var inpt = '<label class="sr-only" for="st">Станция</label>';
                                inpt += '<input type="text" class="form-control" id="st" placeholder="Код станции" oninput="findSt(this.value)" list="stations"/>';
                                inpt += '<table id="stations" style="display:none"></table>';

                                document.getElementById('inptSt').innerHTML = inpt;

                                getStations();

                            } else if (val === 'pf6' || val === 'pf8' || val === 'pf9' || val === 'pf7') {
                                var inpt = '<label class="sr-only" for="tm">Время</label>';
                                inpt += '<input type="text" class="form-control" id="tm" value="00" placeholder="Время"/>';

                                document.getElementById('inptTime').innerHTML = inpt;

                            } else if (val === 'pf1' || val === 'pf2') {
                                var inpt = '<label class="sr-only" for="st">Станция</label>';
                                inpt += '<input type="text" class="form-control" id="st" placeholder="Код станции" oninput="findSt(this.value);" list="stations"/>';
                                inpt += '<table id="stations" style="display:none"></table>';

                                document.getElementById('inptSt').innerHTML = inpt;

                                var inptTm = '<label class="sr-only" for="tm">Время</label>';
                                inptTm += '<input type="text" class="form-control" id="tm" value="00" placeholder="Время"/>';

                                document.getElementById('inptTime').innerHTML = inptTm;

                                getStations();

                            }

                            if (val === 'pf21' || val === 'pf22' || val === 'pf23' || val === 'pf24' || val === 'pf25') {
                                var inpt = '<label class="sr-only" for="idx">Поезд</label>';
                                inpt += '<input type="text" class="form-control" id="idx" placeholder="Индекс поезда" list="poezds"/>';

                                document.getElementById('inptVg').innerHTML = inpt;
                            }

                            if (val === 'pf31' || val === 'pf32' || val === 'pf33' || val === 'pf34') {
                                var inpt = '<label class="sr-only" for="vg">Вагон</label>';
                                inpt += '<input type="text" class="form-control" id="vg" placeholder="Номер вагона" list="vagons"/>';

                                document.getElementById('inptVg').innerHTML = inpt;
                            }
                        }

                        function getMess(object) {
                            if (document.getElementById('st') != null) {
                                if (isFinite(document.getElementById('st').value)) {
                                    object = document.getElementById('st').value;
                                } else {
                                    object = cod4(document.getElementById('st').dataset['stcod']);
                                }
                            }

//                            var textMessage;

                            var station = object;
                            var time;
                            var vagon;
                            var idx;

                            var spr = document.getElementById('numSpr').value;
//                            if (document.getElementById('st') != null) {
//                                station = document.getElementById('st').value;
//                            }
                            if (document.getElementById('tm') != null) {
                                time = document.getElementById('tm').value;
                            }
                            if (document.getElementById('vg') != null) {
                                vagon = document.getElementById('vg').value;
                            }
                            if (document.getElementById('idx') != null) {
                                idx = document.getElementById('idx').value;
                            }

                            switch (spr) {
                                case 'pf1':
                                    console.log('(:212 0 ' + station + ' 29 0:5065 ' + time + ' гр:)');
                                    return '(:212 0 ' + station + ' 29 0:5065 ' + time + ' гр:)';
                                    break;

                                case 'pf2':
                                    console.log('(:212 0 ' + station + ' 29 0:5065 ' + time + ' пор:)');
                                    return '(:212 0 ' + station + ' 29 0:5065 ' + time + ' пор:)';
                                    break;

                                case 'pf3':
                                    console.log('(:212 0 ' + station + ':92:)');
                                    return '(:212 0 ' + station + ':91:92:93:)';
//                                    return '(:212 0 ' + station + ':91:)';
                                    break;

                                case 'pf4':
                                    console.log('(:212 0 7261:1181:)');
                                    return '(:212 0 7261:1181:)';
                                    break;

                                case 'pf5':
                                    console.log('(:212 0 7269:1181:)');
                                    return '(:212 0 7269:1181:)';
                                    break;

                                case 'pf6':
                                    console.log('(:212 0 7473:3290 18 ' + time + ':)');
                                    return '(:212 0 7473:3290 18 ' + time + ':)';
                                    break;

                                case 'pf7':
                                    console.log('(:212 0 73612:3290 18 ' + time + ':)');
                                    return '(:212 0 73612:3290 18 ' + time + ':)';
                                    break;

                                case 'pf8':
                                    console.log('(:212 0 7414:1296 1 18 ' + time + ':)');
                                    return '(:212 0 7414:1296 1 18 ' + time + ':)';
                                    break;

                                case 'pf9':
                                    console.log('(:212 0 73071:1296 1 18 ' + time + ':)');
                                    return '(:212 0 73071:1296 1 18 ' + time + ':)';
                                    break;

                                case 'pf10':
                                    console.log('(:212 0 ' + station + ':5072 01:) ');
                                    return '(:212 0 ' + station + ':5072 01:) ';
                                    break;

                                case 'pf11':
                                    console.log('(:3121 68 6983:57:64:)');
                                    return '(:3121 68 6983:57:64:)';
                                    break;

                                case 'pf12':
                                    console.log('(:212 0 7434:7401:)');
                                    return '(:212 0 7434:7401:)';
                                    break;

                                case 'pf13':
                                    console.log('(:3121 68 6980:91:92:93:)');
                                    return '(:3121 68 6980:91:92:93:)';
                                    break;

                                case 'pf14':
                                    console.log('(:212 0 7261:57:)');
                                    return '(:212 0 7261:57:)';
                                    break;

                                case 'pf15':
                                    console.log('(:212 0 ' + station + ':5065:)  ');
                                    return '(:212 0 ' + station + ':5065:)  ';
                                    break;

                                case 'pf16':
                                    console.log('(:3121 70 7183:64:91:)');
                                    return '(:3121 70 7183:64:91:)';
                                    break;

                                case 'pf17':
                                    console.log('(:212 0 ' + station + ':64:)');
                                    return '(:212 0 ' + station + ':64:)';
                                    break;

                                    ////////////////////////////////////////////////
                                case 'pf21':
                                    console.log('(:216 ' + idx + ':)');
//                                    alert("на стадии разработки!");
                                    return '(:216 ' + idx + ':)';
                                    break;
                                case 'pf22':
                                    console.log('(:215*XXXX XXX XXXX*(10 7300 1)09:)');
                                    alert("на стадии разработки!");
//                                    return '(:215*XXXX XXX XXXX*(10 7300 1)09:)';
                                    break;
                                case 'pf23':
                                    console.log('(:3122 74:XXXX XXX XXXX 12 60 42 902:)');
//                                    alert("на стадии разработки!");
                                    return '(:3122 74:' + idx + '12 60 42 902:)';
                                    break;
                                case 'pf24':
                                    console.log('(:3122 75:XXXX XXX XXXX 12 60 42 902:)');
//                                    alert("на стадии разработки!");
                                    return '(:3122 75:' + idx + '12 60 42 902:)';
                                    break;
                                case 'pf25':
                                    console.log('(:213 0: XXXX XXX XXXX 12 42 60 902 104:)');
//                                    alert("на стадии разработки!");
                                    return '(:213 0: ' + idx + ' 12 42 60 902 104:)';
                                    break;

                                    ////////////////////////////////////////////////
                                case 'pf31':
                                    console.log('(:215*XXXXXXXX*(10 7300 1)09:)');
                                    alert("на стадии разработки!");
//                                    return '(:215*XXXXXXXX*(10 7300 1)09:)';
                                    break;
                                case 'pf32':
                                    console.log('(:216 XXXXXXXX:)');
//                                    alert("на стадии разработки!");
                                    return '(:216 ' + vagon + ':)';
                                    break;
                                case 'pf33':
                                    console.log('(:1367 0 2610:XXXXXXXX:)');
//                                    alert("на стадии разработки!");
                                    return '(:1367 0 2610:' + vagon + ':)';
                                    break;
                                case 'pf34':
                                    console.log('(:217 0:1839 XXXXXXXX:)');
//                                    alert("на стадии разработки!");
                                    return '(:217 0:1839 ' + vagon + ':)';
                                    break;
                            }
                        }

                        function cod4(codSt) {
                            return codSt.substring(0, 4);
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
        <script src="resources/js/findStation.js"></script>
    </body>
</html>