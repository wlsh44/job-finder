var bookmarkNum

function popOpen(recruit) {

    console.log(recruit)
    bookmarkNum = recruit

    var modalPop = document.getElementById('modal-wrap');
    var modalBg = document.getElementById('modal-bg');

    modalPop.style.display = "block";
    modalBg.style.display = "block";

    getCategoryList()
}

function popClose() {
    var modalPop = document.getElementById('modal-wrap');
    var modalBg = document.getElementById('modal-bg');

    modalPop.style.display = "none";
    modalBg.style.display = "none";
}

function getCategoryList() {
    $.ajax({
        type: "GET",
        url: "/category",
        dataType: "text",
    }).done(function (fragment) {
        $("#categoryList").replaceWith(fragment);
    });
}

function createCategory() {

    var name = $("#add-input").val();

    var newCategoryRequestDto = {
        name: name
    }

    $.ajax({
        type: "POST",
        url: "/category",
        data: newCategoryRequestDto
    }).done(function (fragment) {
        $("#categoryList").replaceWith(fragment);
    }).fail(function (xhr, status) {
        console.log(xhr)
        alert(xhr.responseJSON["message"])
    });

    rollbackModalFooter()
}

function bookmarkRecruit() {
    let arr = []
    $(".bookmark-input").map(function (idx, ele) {
        if ($(ele).is(":checked")) {
            let categoryName = $(ele).next("label").text().trim();
            arr.push(categoryName)
        }
    });

    for (const arrElement of arr) {
        console.log(arrElement)
    }
    console.log(recruitDtoList[bookmarkNum])

    let newBookmarkRequestDto = {
        categoryList: arr,
        recruitDto: recruitDtoList[bookmarkNum]
    }
    console.log(newBookmarkRequestDto)

    $.ajax({
        type: "POST",
        url: "/bookmark",
        contentType: "application/json",
        data: JSON.stringify(newBookmarkRequestDto)
    }).done(function () {
        alert("북마크 되었습니다.")
        popClose()
    }).fail(function (data) {
        console.log(data)
        alert("북마크에 실패했습니다.")
    })
}

function rollbackModalFooter() {
    let parentNode = document.getElementById("bookmark-footer");
    let removeItem = document.getElementById("add-category");
    parentNode.removeChild(removeItem)

    let addButton = document.createElement("button");
    addButton.type = "button"
    addButton.id = "add-button"
    addButton.className = "btn btn-primary"
    addButton.onclick = addCategory
    addButton.textContent = "카테고리 추가"

    let bookmarkButton = document.createElement("button");
    bookmarkButton.type = "button"
    bookmarkButton.id = "bookmark-button"
    bookmarkButton.className = "btn btn-primary"
    bookmarkButton.onclick = bookmarkRecruit
    bookmarkButton.textContent = "북마크 추가"

    parentNode.appendChild(addButton)
    parentNode.appendChild(bookmarkButton)
}

function addCategory() {
    const parentNode = document.getElementById("bookmark-footer")
    const addButton = document.getElementById("add-button")
    const bookmarkButton = document.getElementById("bookmark-button")
    parentNode.removeChild(addButton)
    parentNode.removeChild(bookmarkButton)

    const addCategory = document.createElement("div")
    addCategory.id = "add-category"

    const newItemInput = document.createElement("input")
    newItemInput.type = "text"
    newItemInput.id = "add-input"

    const newItemCancelBtn = document.createElement("button")
    newItemCancelBtn.onclick = rollbackModalFooter
    newItemCancelBtn.className = "btn btn-secondary"
    newItemCancelBtn.textContent = "취소"

    const newItemCreateBtn = document.createElement("button")
    newItemCreateBtn.onclick = createCategory
    newItemCreateBtn.className = "btn btn-primary"
    newItemCreateBtn.textContent = "저장"
    newItemCreateBtn.id = "add-category"

    addCategory.appendChild(newItemInput)
    addCategory.appendChild(newItemCancelBtn)
    addCategory.appendChild(newItemCreateBtn)

    parentNode.appendChild(addCategory)
}

// $('#exampleModal1').on('show.bs.modal', function () {
//     $.get("/modals/modal1", function (data) {
//         $('#exampleModal1').find('.modal-body').html(data);
//     })
// });
//
// $('#exampleModal2').on('show.bs.modal', function () {
//     var name = prompt("Please enter your name", "John Doe");
//     $.get("/modals/modal2?name=" + name, function (data) {
//         $('#exampleModal2').find('.modal-body').html(data);
//     })
// })