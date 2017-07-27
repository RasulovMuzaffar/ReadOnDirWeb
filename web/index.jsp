
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
    </head>
    <body>

        <div class="container-fluid">
            <div class="row header">
                <div class="col-md-4 hleft">
                    <h2>Справочник АСОУП</h2>
                </div>
                <div class="col-md-8 hright">
                    <a href="#">Выход</a>
                    <br/>
                    <h4><strong>Здравствуйте, ${user.fName} ${user.lName}!</strong></h4>

                </div>
            </div>
            <div class="row">
                <form class="form-inline" role="form">
                    <div class="form-group">
                        <label class="sr-only" for="numMess">Mess</label>
                        <select class="form-control" id="numMess">
                            <option>№ запроса</option>
                            <option>212</option>
                            <option>213</option>
                        </select>
                        <label class="sr-only" for="numSpr">Spr</label>
                        <select class="form-control" id="numSpr">
                            <option>№ справочника</option>
                            <option>93</option>
                            <option>95</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="st">Станция</label>
                        <input type="text" class="form-control" id="st" placeholder="Код станции">
                        <input type="hidden" name="id_user" value="${user.id}" id="id_user"/>
                    </div>
                    <button type="button" class="btn btn-info" onclick="writing();">OK</button>
                </form>

            </div>
            <div class="row">
                <p class="progressInfo"></p>
            </div>
            <div class="row" id="tbl">

            </div>
        </div>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="resources/js/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="resources/js/bootstrap.js"></script>


        <script type="text/javascript">
                        var rg = /^([a-z_0-9.]{1,})\|([\s\S]*)/;
                        function writing() {
                            var kodOrg = 0;//document.getElementById('q').value;
                            var numMess = document.getElementById('numMess').value;
                            var numSpr = document.getElementById('numSpr').value;
                            var object = document.getElementById('st').value;
                            var id_user = document.getElementById('id_user').value;

                            webSocket.send("spr\u0003" + kodOrg + "," + numMess + "," + numSpr + "," + object + "," + id_user);
//                windows.spr(p){
//                    document.getElementById("otvet").innerHTML = message.data;
//                };

                        }
                        ;
            <%-- получаем натурлий лист поезда  --%>
                        function getTGNL(p) {
                            var x = p.replace("  ", " ");
                            console.log(x);
                            webSocket.send("getTGNL\u0003"+x);
                        }
                        ;

                        var webSocket = new WebSocket("ws://127.0.0.1:8080/MessageToASOUP//ws");
                        webSocket.onopen = function (message) {
                            processOpen(message);
                            console.log(message);
                        };
                        webSocket.onmessage = function (message) {
                            // processMessage(message);
                            console.log(message);
                            document.getElementById("tbl").innerHTML = message.data;

                            checking();

//                            var r = rg.exec(response.data);
//                            try {
//                                if (r[1].includes('.')) {
//                                    var d = r[1].split(',');
//                                    window[d[0]][d[1]](r[2]);
//                                } else {
//                                    window[r[1]](r[2]);
//                                }
//                            } catch (er) {
//                                console.log('ошибка ' + er.stack);
//                                console.log('вызов ' + r[1]);
//                                console.trace();
//
//                            }

                        };
                        function processOpen(message) {
                            console.log(message);
                        }
                        function processMessage(message) {
                            console.log(message.valueOf());
//                            messagesTextArea.value = "";
//                            messagesTextArea.value += message.data;
                        }

                        function checking() {
//                            $(".progressInfo").html($(".mytable tr:last td:first").html());
                            console.log($(".mytable tr").length);
                            console.log($(".mytable tr:last td:first").html());
                            var rowsCount = document.getElementsByClassName("mytable")[0].getElementsByTagName('tr').length;
                            var endRowFCellVal = document.getElementsByClassName("mytable")[0].rows[rowsCount - 1].cells[0].innerHTML;
//                            document.getElementsByClassName("progressInfo")[0].innerHTML = endRowFCellVal;

//                            console.log(rowsCount - 1);
//                            console.log(endRowFCellVal);
                            if ((rowsCount - 1) != endRowFCellVal) {
                                document.getElementsByClassName("progressInfo")[0].innerHTML = "Таблица неполная!";
                                console.log("Таблица неполная!")
                            }
                        }
        </script>
    </body>