var jsonList = "{\n" +
    "  \"references\": [\n" +
    "    {\"number\": \"5065\",\"title\": \"Пономерное наличие вагонов на станции\",\"wagNum\": \"Номер вагона\",\"nazn\": \"Станция назначения\",\"ves\":\"Вес вагона\",\"gruz\":\"Код груза\",\"poluch\":\"Код получателя\",\"oper\":\"Тип последней операции\",\"date\":\"Дата проведения операции\",\"time\":\"Время проведения операции\",\"idx\":\"Индекс поезда\"},\n" +
    "    {\"number\": \"5066\",\"title\": \"Пономерное наличие вагонов на станции\",\"wagNum\": \"Номер вагона\",\"nazn\": \"Станция назначения\",\"ves\":\"Вес вагона\",\"gruz\":\"Код груза\",\"poluch\":\"Код получателя\",\"oper\":\"Тип последней операции\",\"date\":\"Дата проведения операции\",\"time\":\"Время проведения операции\",\"idx\":\"Индекс поезда\"}\n" +
    "  ]\n" +
    "}";


var jo = JSON.parse(jsonList);
var reference = jo.references;

function getInformation() {
    alert("Справочник в процессе разработки, просим свои извинения.")
    // var sTitles = [];
    // sTitles = document.getElementsByClassName('tTitle');
    // console.log(sTitles);
    // var c = [];
    // set
    //
    //
    // reference.forEach(function (value) {
    //     if (value.number == "5065") {
    //         console.log(value.number);
    //         debugger;
    //         return;
    //     }
    // });

    // console.log(reference[0].number + " " + reference[0].title);
    // alert("Номер справки: " + reference[0].number +
    //     "\nНаименование: " + reference[0].title +
    //     "\n№ вагона: " + reference[0].wagNum)
}
